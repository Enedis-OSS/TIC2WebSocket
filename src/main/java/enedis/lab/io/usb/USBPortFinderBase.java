// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.io.usb;

import enedis.lab.types.DataArrayList;
import enedis.lab.types.DataDictionaryException;
import enedis.lab.types.DataList;
import org.usb4java.Context;
import org.usb4java.Device;
import org.usb4java.DeviceDescriptor;
import org.usb4java.DeviceHandle;
import org.usb4java.DeviceList;
import org.usb4java.LibUsb;

/** Class used to find all USB port descriptor */
public class USBPortFinderBase implements USBPortFinder {
  /**
   * Program writing the USB port descriptor list (JSON format) on the output stream
   *
   * @param args not used
   */
  public static void main(String[] args) {
    DataList<USBPortDescriptor> descriptors = getInstance().findAll();

    System.out.println(descriptors.toString(2));
  }

  /**
   * Get instance
   *
   * @return Unique instance
   */
  public static USBPortFinderBase getInstance() {
    if (instance == null) {
      instance = new USBPortFinderBase();
    }

    return instance;
  }

  private static USBPortFinderBase instance;

  private USBPortFinderBase() {}

  @Override
  public DataList<USBPortDescriptor> findAll() {
    DataList<USBPortDescriptor> usbPortList = new DataArrayList<USBPortDescriptor>();

    Context context = new Context();
    DeviceList deviceList = new DeviceList();

    // Initialize USB library access
    int result = LibUsb.init(context);
    if (result != LibUsb.SUCCESS) {
      return usbPortList;
    }

    // Get available USB device list
    result = LibUsb.getDeviceList(context, deviceList);
    if (result < 0) {
      return usbPortList;
    }

    // For each USB device
    for (Device device : deviceList) {
      DeviceDescriptor descriptor = new DeviceDescriptor();

      // Get USB device descriptor
      result = LibUsb.getDeviceDescriptor(device, descriptor);
      if (result != LibUsb.SUCCESS) {
        continue;
      }
      String productName = null;
      String manufacturer = null;
      String serialNumber = null;
      // If USB device has string descriptors
      if (descriptor.idProduct() != 0) {
        // Get string descriptors for USB device
        DeviceHandle handle = new DeviceHandle();
        result = LibUsb.open(device, handle);
        if (result == LibUsb.SUCCESS) {
          productName = LibUsb.getStringDescriptor(handle, descriptor.iProduct());
          manufacturer = LibUsb.getStringDescriptor(handle, descriptor.iManufacturer());
          serialNumber = LibUsb.getStringDescriptor(handle, descriptor.iSerialNumber());
          LibUsb.close(handle);
        }
      }

      try {
        // @formatter:off
        USBPortDescriptor usbPort =
            new USBPortDescriptor(
                descriptor.bcdDevice() & 0xFFFF,
                descriptor.bcdUSB() & 0xFFFF,
                descriptor.bDescriptorType() & 0xFF,
                descriptor.bDeviceClass() & 0xFF,
                descriptor.bDeviceProtocol() & 0xFF,
                descriptor.bDeviceSubClass() & 0xFF,
                descriptor.bLength() & 0xFF,
                descriptor.bMaxPacketSize0() & 0xFF,
                descriptor.bNumConfigurations() & 0xFF,
                descriptor.idProduct() & 0xFFFF,
                descriptor.idVendor() & 0xFFFF,
                descriptor.iManufacturer() & 0xFF,
                descriptor.iProduct() & 0xFF,
                descriptor.iSerialNumber() & 0xFF,
                manufacturer,
                productName,
                serialNumber);
        // @formatter:on
        usbPortList.add(usbPort);
      } catch (DataDictionaryException e) {
      }
    }

    // Finalize USB library access
    LibUsb.freeDeviceList(deviceList, true);
    LibUsb.exit(context);
    return usbPortList;
  }

}
