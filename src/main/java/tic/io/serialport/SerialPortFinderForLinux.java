// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.io.serialport;

import com.sun.jna.platform.linux.Udev;
import com.sun.jna.platform.linux.Udev.UdevContext;
import com.sun.jna.platform.linux.Udev.UdevDevice;
import com.sun.jna.platform.linux.Udev.UdevEnumerate;
import com.sun.jna.platform.linux.Udev.UdevListEntry;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Linux-specific implementation of serial port discovery.
 *
 * <p>This class implements {@link SerialPortFinder} and provides specialized functionality for
 * discovering serial ports on Linux systems. It uses multiple detection methods with fallback
 * strategies to ensure comprehensive port discovery:
 *
 * <ul>
 *   <li><b>Udev</b> - Primary method using the Linux udev device manager (most reliable)
 *   <li><b>Sysfs</b> - Fallback method reading from /sys/class/tty
 *   <li><b>Device filtering</b> - Final fallback scanning /dev for known device patterns
 * </ul>
 *
 * <p>The class supports a wide range of serial devices
 *
 * <p>The class follows the singleton pattern with lazy initialization.
 *
 * @author Enedis Smarties team
 * @see SerialPortFinder
 * @see SerialPortDescriptor
 */
public class SerialPortFinderForLinux implements SerialPortFinder {
  private static String MOXA_IDENTIFIER = "ttyr";
  private static Pattern MOXA_PATTERN = Pattern.compile("(.*)" + MOXA_IDENTIFIER + "(\\d+)");
  private static String MOXA_FORMAT = MOXA_IDENTIFIER + "%02d";

  private static SerialPortFinderForLinux instance;

  /**
   * Returns the singleton instance of the Linux serial port finder.
   *
   * <p>This method performs lazy initialization and caches the instance for subsequent calls.
   *
   * @return the singleton SerialPortFinderForLinux instance
   */
  public static SerialPortFinderForLinux getInstance() {
    if (instance == null) {
      instance = new SerialPortFinderForLinux();
    }

    return instance;
  }

  /**
   * Private constructor to prevent direct instantiation.
   *
   * <p>Use {@link #getInstance()} to obtain the singleton instance.
   */
  private SerialPortFinderForLinux() {}

  /**
   * Finds all available serial ports on the Linux system.
   *
   * <p>This method uses a cascading fallback strategy with three detection methods:
   *
   * <ol>
   *   <li>Udev - Queries the Linux device manager for tty devices (preferred)
   *   <li>Sysfs - Reads from /sys/class/tty if udev fails or returns no results
   *   <li>Device filtering - Scans /dev for known serial device patterns as last resort
   * </ol>
   *
   * @return a list of all serial port descriptors found on the system
   */
  @Override
  public List<SerialPortDescriptor> findAll() {
    List<SerialPortDescriptor> serialPortDescriptorList = new ArrayList<>();

    serialPortDescriptorList = availablePortsByUdev();
    if (serialPortDescriptorList.isEmpty()) {
      serialPortDescriptorList = availablePortsBySysfs();
    }
    if (serialPortDescriptorList.isEmpty()) {
      serialPortDescriptorList = availablePortsByFiltersOfDevices();
    }

    return serialPortDescriptorList;
  }

  /**
   * Discovers serial ports using the Linux udev device manager.
   *
   * <p>This is the primary and most reliable method for finding serial ports on Linux. It queries
   * the udev subsystem for all tty devices, extracts USB device information (PID, VID,
   * manufacturer, serial number), and creates descriptors for each port.
   *
   * <p>The method filters out invalid serial8250 ports and includes special handling for rfcomm,
   * virtual null modem, gadget serial, and MOXA devices.
   *
   * @return a list of serial port descriptors discovered via udev
   */
  private static List<SerialPortDescriptor> availablePortsByUdev() {
    UdevContext udev = Udev.INSTANCE.udev_new();
    UdevEnumerate enumerate = Udev.INSTANCE.udev_enumerate_new(udev);
    Udev.INSTANCE.udev_enumerate_add_match_subsystem(enumerate, "tty");
    Udev.INSTANCE.udev_enumerate_scan_devices(enumerate);

    List<SerialPortDescriptor> serialPortDescriptorList = new ArrayList<>();

    for (UdevListEntry dev_list_entry = Udev.INSTANCE.udev_enumerate_get_list_entry(enumerate);
        dev_list_entry != null;
        dev_list_entry = dev_list_entry.getNext()) {
      UdevDevice device =
          Udev.INSTANCE.udev_device_new_from_syspath(
              udev, Udev.INSTANCE.udev_list_entry_get_name(dev_list_entry));
      if (device == null) {
        break;
      }
      SerialPortDescriptor serialPortDescriptor;

      String location = deviceLocation(device);
      String portName = deviceName(device);

      UdevDevice parentdev = Udev.INSTANCE.udev_device_get_parent(device);

      if (parentdev != null) {
        String driverName = deviceDriver(parentdev);
        if (isSerial8250Driver(driverName) && !isValidSerial8250(location)) {
          Udev.INSTANCE.udev_device_unref(device);
          continue;
        }
        String description = deviceDescription(device);
        String portId = devicePortId(device);
        String manufacturer = deviceManufacturer(device);
        String serialNumber = deviceSerialNumber(device);
        Short vendorIdentifier = deviceVendorIdentifier(device);
        Short productIdentifier = deviceProductIdentifier(device);
        String productName = deviceProductName(device);
        try {
          serialPortDescriptor =
              new SerialPortDescriptor.Builder()
                  .portId(portId)
                  .portName(location)
                  .description(description)
                  .productId(productIdentifier)
                  .vendorId(vendorIdentifier)
                  .productName(productName)
                  .manufacturer(manufacturer)
                  .serialNumber(serialNumber)
                  .build();
        } catch (IllegalArgumentException e) {
          e.printStackTrace(System.err);
          Udev.INSTANCE.udev_device_unref(device);
          continue;
        }
      } else {
        if (!isRfcommDevice(portName)
            && !isVirtualNullModemDevice(portName)
            && !isGadgetDevice(portName)
            && !isMoxaDevice(portName)) {
          Udev.INSTANCE.udev_device_unref(device);
          continue;
        }
        try {
          serialPortDescriptor =
              new SerialPortDescriptor.Builder()
                  .portId(null)
                  .portName(location)
                  .description(null)
                  .productId(null)
                  .vendorId(null)
                  .productName(null)
                  .manufacturer(null)
                  .serialNumber(null)
                  .build();
        } catch (IllegalArgumentException e) {
          e.printStackTrace(System.err);
          Udev.INSTANCE.udev_device_unref(device);
          continue;
        }
      }
      serialPortDescriptorList.add(serialPortDescriptor);
      Udev.INSTANCE.udev_device_unref(device);
    }
    Udev.INSTANCE.udev_enumerate_unref(enumerate);
    Udev.INSTANCE.udev_unref(udev);

    return serialPortDescriptorList;
  }

  /**
   * Discovers serial ports by reading from the sysfs filesystem.
   *
   * <p>This method serves as a fallback when udev is not available or returns no results. It reads
   * from /sys/class/tty and follows symbolic links to extract device information. The method
   * traverses the device hierarchy to find USB properties.
   *
   * @return a list of serial port descriptors discovered via sysfs
   */
  private static List<SerialPortDescriptor> availablePortsBySysfs() {
    List<SerialPortDescriptor> serialPortDescriptorList = new ArrayList<>();
    File ttySysClassDir = new File("/sys/class/tty");

    if (!(ttySysClassDir.exists() && ttySysClassDir.canRead())) {
      return serialPortDescriptorList;
    }
    FileFilter filter =
        new FileFilter() {
          @Override
          public boolean accept(File pathname) {

            return pathname.isDirectory()
                && !pathname.getName().equals(".")
                && !pathname.getName().equals("..");
          }
        };
    File[] fileInfos = ttySysClassDir.listFiles(filter);
    for (File fileInfo : fileInfos) {
      if (!Files.isSymbolicLink(fileInfo.toPath())) {
        continue;
      }
      File targetDir = fileInfo.getAbsoluteFile();
      SerialPortDescriptor serialPortDescriptor;

      String portName = deviceName(targetDir);
      if (portName == null) {
        continue;
      }
      String driverName = deviceDriver(targetDir);
      if (driverName == null) {
        if (!isRfcommDevice(portName)
            && !isVirtualNullModemDevice(portName)
            && !isGadgetDevice(portName)
            && !isMoxaDevice(portName)) {
          continue;
        }
      }

      String device = portNameToSystemLocation(portName);
      if (isSerial8250Driver(driverName) && !isValidSerial8250(device)) {
        continue;
      }
      String description = null;
      String manufacturer = null;
      String serialNumber = null;
      Short vendorIdentifier = null;
      Short productIdentifier = null;
      String productName = null;
      do {
        if (description == null) {
          description = deviceDescription(targetDir);
        }
        if (manufacturer == null) {
          manufacturer = deviceManufacturer(targetDir);
        }
        if (serialNumber == null) {
          serialNumber = deviceSerialNumber(targetDir);
        }
        if (vendorIdentifier == null) {
          vendorIdentifier = deviceVendorIdentifier(targetDir);
        }
        if (productIdentifier == null) {
          productIdentifier = deviceProductIdentifier(targetDir);
        }
        if (productName == null) {
          productName = deviceProductName(targetDir);
        }
        if (description != null
            || manufacturer != null
            || serialNumber != null
            || vendorIdentifier != null
            || productIdentifier != null
            || productName != null) {
          break;
        }
        targetDir = targetDir.getParentFile();
      } while (targetDir != null);
      try {
        serialPortDescriptor =
            new SerialPortDescriptor.Builder()
                .portId(null)
                .portName(portName)
                .description(description)
                .productId(productIdentifier)
                .vendorId(vendorIdentifier)
                .productName(productName)
                .manufacturer(manufacturer)
                .serialNumber(serialNumber)
                .build();
      } catch (IllegalArgumentException e) {
        e.printStackTrace(System.err);
        continue;
      }
      serialPortDescriptorList.add(serialPortDescriptor);
    }

    return serialPortDescriptorList;
  }

  /**
   * Discovers serial ports by scanning /dev for known device file patterns.
   *
   * <p>This is the last resort fallback method when both udev and sysfs fail. It scans the /dev
   * directory for files matching known serial port patterns (ttyS*, ttyUSB*, ttyACM*, etc.) and
   * creates minimal descriptors without USB device information.
   *
   * @return a list of serial port descriptors discovered by device file filtering
   */
  private static List<SerialPortDescriptor> availablePortsByFiltersOfDevices() {
    List<SerialPortDescriptor> serialPortDescriptorList = new ArrayList<>();

    List<String> deviceFilePaths = filteredDeviceFilePaths();
    for (String deviceFilePath : deviceFilePaths) {
      SerialPortDescriptor serialPortDescriptor;
      String portName = portNameFromSystemLocation(deviceFilePath);
      try {
        serialPortDescriptor =
            new SerialPortDescriptor.Builder()
                .portId(null)
                .portName(portName)
                .description(null)
                .productId(null)
                .vendorId(null)
                .productName(null)
                .manufacturer(null)
                .serialNumber(null)
                .build();
      } catch (IllegalArgumentException e) {
        e.printStackTrace(System.err);
        continue;
      }
      serialPortDescriptorList.add(serialPortDescriptor);
    }

    return serialPortDescriptorList;
  }

  /**
   * Returns a list of device file paths matching known serial port patterns.
   *
   * <p>This method scans the /dev directory and filters files based on predefined patterns that
   * correspond to various types of serial devices on Linux, FreeBSD, and QNX systems. It excludes
   * symbolic links and certain non-serial files (.init, .lock).
   *
   * @return a list of absolute paths to potential serial port device files
   */
  private static List<String> filteredDeviceFilePaths() {
    List<String> deviceFileNameFilterList = new ArrayList<String>();

    // Linux
    deviceFileNameFilterList.add("ttyS*"); // Standard UART 8250 and etc.
    deviceFileNameFilterList.add("ttyO*"); // OMAP UART 8250 and etc.
    deviceFileNameFilterList.add("ttyUSB*"); // Usb/serial converters PL2303 and etc.
    deviceFileNameFilterList.add("ttyACM*"); // CDC_ACM converters (i.e. Mobile Phones).
    deviceFileNameFilterList.add(
        "ttyGS*"); // Gadget serial device (i.e. Mobile Phones with gadget serial driver).
    deviceFileNameFilterList.add("ttyMI*"); // MOXA pci/serial converters.
    deviceFileNameFilterList.add("ttymxc*"); // Motorola IMX serial ports (i.e. Freescale i.MX).
    deviceFileNameFilterList.add(
        "ttyAMA*"); // AMBA serial device for embedded platform on ARM (i.e. Raspberry Pi).
    deviceFileNameFilterList.add(
        "ttyTHS*"); // Serial device for embedded platform on ARM (i.e. Tegra Jetson TK1).
    deviceFileNameFilterList.add("rfcomm*"); // Bluetooth serial device.
    deviceFileNameFilterList.add("ircomm*"); // IrDA serial device.
    deviceFileNameFilterList.add("tnt*"); // Virtual tty0tty serial device.
    deviceFileNameFilterList.add(MOXA_IDENTIFIER + "*"); // MOXA ethernet device.
    // FreeBSD
    deviceFileNameFilterList.add("cu*");
    // QNX
    deviceFileNameFilterList.add("ser*");

    List<String> result = new ArrayList<String>();

    File deviceDir = new File("/dev");
    if (deviceDir.exists()) {
      FileFilter deviceFileNameFilter =
          new FileFilter() {
            @Override
            public boolean accept(File pathname) {
              Path path = pathname.toPath();

              return deviceFileNameFilterList.contains(pathname.getName())
                  && pathname.isFile()
                  && !Files.isSymbolicLink(path);
            }
          };
      File[] deviceFileInfos = deviceDir.listFiles(deviceFileNameFilter);
      for (File deviceFileInfo : deviceFileInfos) {
        String deviceAbsoluteFilePath = deviceFileInfo.getAbsolutePath();
        // it is a quick workaround to skip the non-serial devices (FreeBSD)
        if (deviceAbsoluteFilePath.endsWith(".init") || deviceAbsoluteFilePath.endsWith(".lock")) {
          continue;
        }
        if (!result.contains(deviceAbsoluteFilePath)) {
          result.add(deviceAbsoluteFilePath);
        }
      }
    }

    return result;
  }

  private static boolean isSerial8250Driver(String driverName) {
    return (driverName == "serial8250");
  }

  private static boolean isValidSerial8250(String systemLocation) {
    return false;
  }

  private static boolean isRfcommDevice(String portName) {
    if (!portName.startsWith("rfcomm")) {
      return false;
    }
    try {
      String number = portName.substring(6);
      int portNumber = Integer.parseInt(number);
      if (portNumber < 0 || portNumber > 255) {
        return false;
      }
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  // provided by the tty0tty driver
  private static boolean isVirtualNullModemDevice(String portName) {
    return portName.startsWith("tnt");
  }

  // provided by the g_serial driver
  private static boolean isGadgetDevice(String portName) {
    return portName.startsWith("ttyGS");
  }

  // provided by the MOXA driver
  private static boolean isMoxaDevice(String portName) {
    return portName.startsWith(MOXA_IDENTIFIER);
  }

  private static String devicePortId(UdevDevice dev) {
    return deviceProperty(dev, "ID_PATH");
  }

  private static String deviceDescription(UdevDevice dev) {
    String description = deviceProperty(dev, "ID_MODEL_FROM_DATABASE");

    return (description != null) ? description.replace('_', ' ') : null;
  }

  private static String deviceDescription(File targetDir) {
    return deviceProperty(new File(targetDir, "product").getAbsolutePath());
  }

  private static String deviceManufacturer(UdevDevice dev) {
    String manufacturer = deviceProperty(dev, "ID_VENDOR");

    return (manufacturer != null) ? manufacturer.replace('_', ' ') : null;
  }

  private static String deviceManufacturer(File targetDir) {
    return deviceProperty(new File(targetDir, "manufacturer").getAbsolutePath());
  }

  private static Short deviceProductIdentifier(UdevDevice dev) {
    Short identifierValue;
    try {
      String indentifierText = deviceProperty(dev, "ID_MODEL_ID");
      identifierValue = (indentifierText != null) ? Short.parseShort(indentifierText, 16) : null;
    } catch (Exception e) {
      identifierValue = null;
    }
    return identifierValue;
  }

  private static String deviceProductName(UdevDevice dev) {
    return deviceProperty(dev, "ID_MODEL");
  }

  private static String deviceProductName(File targetDir) {
    return deviceProperty(new File(targetDir, "product").getAbsolutePath());
  }

  private static Short deviceProductIdentifier(File targetDir) {
    String indentifierText = deviceProperty(new File(targetDir, "idProduct").getAbsolutePath());

    if (indentifierText == null) {
      indentifierText = deviceProperty(new File(targetDir, "device").getAbsolutePath());
    }
    Short identifierValue;
    try {
      identifierValue = (indentifierText != null) ? Short.parseShort(indentifierText, 16) : null;
    } catch (Exception e) {
      identifierValue = null;
    }
    return identifierValue;
  }

  private static Short deviceVendorIdentifier(File targetDir) {
    String indentifierText = deviceProperty(new File(targetDir, "idVendor").getAbsolutePath());

    if (indentifierText == null) {
      indentifierText = deviceProperty(new File(targetDir, "vendor").getAbsolutePath());
    }
    Short identifierValue;
    try {
      identifierValue = (indentifierText != null) ? Short.parseShort(indentifierText, 16) : null;
    } catch (Exception e) {
      identifierValue = null;
    }
    return identifierValue;
  }

  private static String deviceSerialNumber(File targetDir) {
    return deviceProperty(new File(targetDir, "serial").getAbsolutePath());
  }

  private static Short deviceVendorIdentifier(UdevDevice dev) {
    Short identifierValue;
    try {
      String indentifierText = deviceProperty(dev, "ID_VENDOR_ID");
      identifierValue = (indentifierText != null) ? Short.parseShort(indentifierText, 16) : null;
    } catch (Exception e) {
      identifierValue = null;
    }
    return identifierValue;
  }

  private static String deviceSerialNumber(UdevDevice dev) {
    return deviceProperty(dev, "ID_SERIAL_SHORT");
  }

  private static String deviceProperty(UdevDevice dev, String name) {
    return Udev.INSTANCE.udev_device_get_property_value(dev, name);
  }

  private static String deviceProperty(String targetFilePath) {
    FileInputStream f;

    try {
      f = new FileInputStream(targetFilePath);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return null;
    }
    String text;
    try {
      int available = f.available();
      byte[] buffer = new byte[available];
      f.read(buffer);
      text = new String(buffer);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    } finally {
      try {
        f.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return text.trim();
  }

  private static String deviceDriver(UdevDevice dev) {
    return null;
  }

  private static String deviceDriver(File targetDir) {
    File deviceDir = new File(targetDir, "device");

    return ueventProperty(deviceDir, "DRIVER=");
  }

  private static String deviceName(UdevDevice dev) {
    return Udev.INSTANCE.udev_device_get_sysname(dev);
  }

  static String deviceName(File targetDir) {
    return ueventProperty(targetDir, "DEVNAME=");
  }

  private static String deviceLocation(UdevDevice dev) {
    String location = Udev.INSTANCE.udev_device_get_devnode(dev);
    // Patch for MOXA device
    // Udev prints location as /dev/ttyrN but only /dev/ttyr0N exists (with N belongs to [1-4])
    // So we are renaming location from /dev/ttyrN to /dev/ttyr0N
    Matcher matcher = MOXA_PATTERN.matcher(location);

    if (matcher.matches()) {
      String prefix = "";
      String indexValue;
      if (matcher.groupCount() == 2) {
        prefix = matcher.group(1);
        indexValue = matcher.group(2);
      } else {
        indexValue = matcher.group(2);
      }
      int index = Integer.parseUnsignedInt(indexValue);
      location = String.format("%s" + MOXA_FORMAT, prefix, index);
    }

    return location;
  }

  private static String portNameToSystemLocation(String source) {
    return (source.startsWith("/") || source.startsWith("./") || source.startsWith("../"))
        ? source
        : (("/dev/") + source);
  }

  private static String portNameFromSystemLocation(String source) {
    return source.startsWith("/dev/") ? source.substring(5) : source;
  }

  @SuppressWarnings("resource")
  private static String ueventProperty(File targetDir, String pattern) {
    FileInputStream f;

    try {
      f = new FileInputStream(new File(targetDir, "uevent"));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return null;
    }
    String content;
    try {
      int available = f.available();
      byte[] buffer = new byte[available];
      f.read(buffer);
      content = new String(buffer);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
    int firstbound = content.indexOf(pattern);
    if (firstbound == -1) {
      return null;
    }
    int lastbound = content.indexOf('\n', firstbound);

    return content.substring(firstbound, lastbound);
  }
}
