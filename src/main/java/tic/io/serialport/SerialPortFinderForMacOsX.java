package tic.io.serialport;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.platform.mac.CoreFoundation;
import com.sun.jna.platform.mac.CoreFoundation.CFAllocatorRef;
import com.sun.jna.platform.mac.CoreFoundation.CFIndex;
import com.sun.jna.platform.mac.CoreFoundation.CFMutableDictionaryRef;
import com.sun.jna.platform.mac.CoreFoundation.CFNumberRef;
import com.sun.jna.platform.mac.CoreFoundation.CFStringRef;
import com.sun.jna.platform.mac.CoreFoundation.CFTypeID;
import com.sun.jna.platform.mac.CoreFoundation.CFTypeRef;
import com.sun.jna.platform.mac.IOKit;
import com.sun.jna.platform.mac.IOKit.IOIterator;
import com.sun.jna.platform.mac.IOKit.IORegistryEntry;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.ptr.ShortByReference;
import java.util.ArrayList;
import java.util.List;

/** Class used to find all serial port descriptor for MAC OS X */
public class SerialPortFinderForMacOsX implements SerialPortFinder {

  private static final String kIOSerialBSDServiceValue = "IOSerialBSDClient";
  private static final String kIOSerialBSDTypeKey = "IOSerialBSDClientType";
  private static final String kIOSerialBSDAllTypes = "IOSerialStream";
  private static final String kIOCalloutDeviceKey = "IOCalloutDevice";
  private static final String kIOServicePlane = "IOService";
  private static final String kIODialinDeviceKey = "IODialinDevice";
  private static final String kIOPropertyProductNameKey = "Product Name";
  private static final String kUSBProductString = "USB Product Name";
  private static final String kUSBVendorString = "USB Vendor Name";
  private static final String kUSBSerialNumberString = "USB Serial Number";
  private static final String kUSBVendorID = "idVendor";
  private static final String kUSBProductID = "idProduct";
  private static final CFAllocatorRef kCFAllocatorDefault =
      CoreFoundation.INSTANCE.CFAllocatorGetDefault();
  // Encodages CFString
  private static final int kCFStringEncodingUTF8 = 0x08000100;
  private static final int kCFNumberSInt16Type = 2;
  private static final int kCFNumberSInt32Type = 3;

  /**
   * Get instance
   *
   * @return Unique instance
   */
  public static SerialPortFinderForMacOsX getInstance() {
    if (instance == null) {
      instance = new SerialPortFinderForMacOsX();
    }

    return instance;
  }

  private static SerialPortFinderForMacOsX instance;

  private SerialPortFinderForMacOsX() {}

  @Override
  public List<SerialPortDescriptor> findAll() {
    List<SerialPortDescriptor> serialPortDescriptorList = new ArrayList<SerialPortDescriptor>();

    CFMutableDictionaryRef serialPortDictionary =
        IOKit.INSTANCE.IOServiceMatching(kIOSerialBSDServiceValue);
    if (serialPortDictionary == null) {
      return serialPortDescriptorList;
    }
    // Ajouter le filtre de type
    CFStringRef key = CFStringRef.createCFString(kIOSerialBSDTypeKey);
    CFStringRef value = CFStringRef.createCFString(kIOSerialBSDAllTypes);

    serialPortDictionary.setValue(key, value);

    IntByReference kIOMasterPortDefault = new IntByReference();
    int errorCode = IOKit.INSTANCE.IOMasterPort(0, kIOMasterPortDefault);
    if (errorCode != 0) {
      return serialPortDescriptorList;
    }
    PointerByReference serialPortIteratorRef = new PointerByReference();
    errorCode =
        IOKit.INSTANCE.IOServiceGetMatchingServices(
            kIOMasterPortDefault.getValue(), serialPortDictionary, serialPortIteratorRef);
    if (errorCode != 0) {
      return serialPortDescriptorList;
    }
    IOIterator serialPortIterator = new IOIterator(serialPortIteratorRef.getValue());

    for (; ; ) {
      IORegistryEntry serialPortService = serialPortIterator.next();
      if (serialPortService == null) {
        break;
      }
      String calloutDevice = null;
      String dialinDevice = null;
      Integer locationId = null;
      String deviceDescription = null;
      String manufacturer = null;
      String serialNumber = null;
      Short vendorIdentifier = null;
      Short productIdentifier = null;

      for (; ; ) {
        if (calloutDevice == null) {
          calloutDevice = calloutDeviceSystemLocation(serialPortService);
        }
        if (dialinDevice == null) {
          dialinDevice = dialinDeviceSystemLocation(serialPortService);
        }
        if (locationId == null) {
          locationId = deviceLocationId(serialPortService);
        }
        if (deviceDescription == null) {
          deviceDescription = deviceDescription(serialPortService);
        }
        if (manufacturer == null) {
          manufacturer = deviceManufacturer(serialPortService);
        }
        if (serialNumber == null) {
          serialNumber = deviceSerialNumber(serialPortService);
        }
        if (vendorIdentifier == null) {
          vendorIdentifier = deviceVendorIdentifier(serialPortService);
        }
        if (productIdentifier == null) {
          productIdentifier = deviceProductIdentifier(serialPortService);
        }
        if (isCompleteInfo(
            calloutDevice,
            dialinDevice,
            manufacturer,
            deviceDescription,
            serialNumber,
            vendorIdentifier,
            productIdentifier)) {
          serialPortService.release();
          break;
        }
        serialPortService = serialPortService.getParentEntry(kIOServicePlane);
        if (serialPortService == null) {
          break;
        }
      }
      String portId = (locationId != null) ? locationId.toString() : null;
      SerialPortDescriptor calloutCandidate =
          new SerialPortDescriptor.Builder<>()
              .portId(portId)
              .portName(calloutDevice)
              .description(deviceDescription)
              .productId(productIdentifier)
              .vendorId(vendorIdentifier)
              .productName(deviceDescription)
              .manufacturer(manufacturer)
              .serialNumber(serialNumber)
              .build();
      serialPortDescriptorList.add(calloutCandidate);

      SerialPortDescriptor dialinCandidate =
          new SerialPortDescriptor.Builder<>()
              .portId(portId)
              .portName(dialinDevice)
              .description(deviceDescription)
              .productId(productIdentifier)
              .vendorId(vendorIdentifier)
              .productName(deviceDescription)
              .manufacturer(manufacturer)
              .serialNumber(serialNumber)
              .build();
      serialPortDescriptorList.add(dialinCandidate);
    }
    serialPortIterator.release();

    return serialPortDescriptorList;
  }

  private static boolean isCompleteInfo(
      String calloutDevice,
      String dialinDevice,
      String manufacturer,
      String description,
      String serialNumber,
      Short vendorIdentifier,
      Short productIdentifier) {
    return calloutDevice != null
        && dialinDevice != null
        && manufacturer != null
        && description != null
        && serialNumber != null
        && vendorIdentifier != null
        && vendorIdentifier != null;
  }

  private static String calloutDeviceSystemLocation(IORegistryEntry ioRegistryEntry) {
    return searchStringProperty(ioRegistryEntry, kIOCalloutDeviceKey);
  }

  private static String dialinDeviceSystemLocation(IORegistryEntry ioRegistryEntry) {
    return searchStringProperty(ioRegistryEntry, kIODialinDeviceKey);
  }

  private static Integer deviceLocationId(IORegistryEntry ioRegistryEntry) {
    return searchIntProperty(ioRegistryEntry, "locationID");
  }

  private static String deviceDescription(IORegistryEntry ioRegistryEntry) {
    String result = searchStringProperty(ioRegistryEntry, kIOPropertyProductNameKey);
    if (result == null || result.isEmpty()) {
      result = searchStringProperty(ioRegistryEntry, kUSBProductString);
    }
    if (result == null || result.isEmpty()) {
      result = searchStringProperty(ioRegistryEntry, "BTName");
    }
    return result;
  }

  private static String deviceManufacturer(IORegistryEntry ioRegistryEntry) {
    return searchStringProperty(ioRegistryEntry, kUSBVendorString);
  }

  private static String deviceSerialNumber(IORegistryEntry ioRegistryEntry) {
    return searchStringProperty(ioRegistryEntry, kUSBSerialNumberString);
  }

  private static Short deviceVendorIdentifier(IORegistryEntry ioRegistryEntry) {
    return searchShortIntProperty(ioRegistryEntry, kUSBVendorID);
  }

  private static Short deviceProductIdentifier(IORegistryEntry ioRegistryEntry) {
    return searchShortIntProperty(ioRegistryEntry, kUSBProductID);
  }

  private static String searchStringProperty(IORegistryEntry ioRegistryEntry, String propertyKey) {
    CoreFoundation.CFTypeRef container = searchProperty(ioRegistryEntry, propertyKey);
    String result = convertCFTypeRefToString(container);
    if (container != null) {
      container.release();
    }

    return result;
  }

  private static Short searchShortIntProperty(IORegistryEntry ioRegistryEntry, String propertyKey) {
    CoreFoundation.CFTypeRef container = searchProperty(ioRegistryEntry, propertyKey);
    Short result = convertCFTypeRefToShort(container);
    if (container != null) {
      container.release();
    }

    return result;
  }

  private static Integer searchIntProperty(IORegistryEntry ioRegistryEntry, String propertyKey) {
    CoreFoundation.CFTypeRef container = searchProperty(ioRegistryEntry, propertyKey);
    Integer result = convertCFTypeRefToInteger(container);
    if (container != null) {
      container.release();
    }

    return result;
  }

  private static CoreFoundation.CFTypeRef searchProperty(
      IORegistryEntry ioRegistryEntry, String propertyKey) {

    return IOKit.INSTANCE.IORegistryEntrySearchCFProperty(
        ioRegistryEntry,
        kIOServicePlane,
        CFStringRef.createCFString(propertyKey),
        kCFAllocatorDefault,
        0);
  }

  private static String convertCFTypeRefToString(CFTypeRef cfTypeRef) {
    if (cfTypeRef == null) {
      return null;
    }

    CFTypeID typeID = CoreFoundation.INSTANCE.CFGetTypeID(cfTypeRef);
    CFTypeID stringTypeID = CoreFoundation.INSTANCE.CFStringGetTypeID();

    if (!typeID.equals(stringTypeID)) {
      return null;
    }

    CFStringRef cfStringRef = new CFStringRef(cfTypeRef.getPointer());

    CFIndex length = CoreFoundation.INSTANCE.CFStringGetLength(cfStringRef);
    if (length.longValue() == 0) {
      return "";
    }

    CFIndex maxSize =
        CoreFoundation.INSTANCE.CFStringGetMaximumSizeForEncoding(length, kCFStringEncodingUTF8);

    int bufferSize = (int) maxSize.longValue() + 1;
    Pointer buffer = new Memory(bufferSize);

    CFIndex bufferLength = new CFIndex(bufferSize);

    byte success =
        CoreFoundation.INSTANCE.CFStringGetCString(
            cfStringRef, buffer, bufferLength, kCFStringEncodingUTF8);

    if (success == 0) {
      return null;
    }

    return buffer.getString(0, "UTF-8");
  }

  private static Integer convertCFTypeRefToInteger(CFTypeRef cfTypeRef) {
    if (cfTypeRef == null) {
      return null;
    }

    CFTypeID typeID = CoreFoundation.INSTANCE.CFGetTypeID(cfTypeRef);
    CFTypeID numberTypeID = CoreFoundation.INSTANCE.CFNumberGetTypeID();

    if (!typeID.equals(numberTypeID)) {
      return null;
    }

    CFNumberRef cfNumberRef = new CFNumberRef(cfTypeRef.getPointer());

    CFIndex numberType = new CFIndex(kCFNumberSInt32Type);

    IntByReference value = new IntByReference();
    byte success = CoreFoundation.INSTANCE.CFNumberGetValue(cfNumberRef, numberType, value);

    if (success == 0) {
      return null;
    }

    return value.getValue();
  }

  private static Short convertCFTypeRefToShort(CFTypeRef cfTypeRef) {
    if (cfTypeRef == null) {
      return null;
    }

    CFTypeID typeID = CoreFoundation.INSTANCE.CFGetTypeID(cfTypeRef);
    CFTypeID numberTypeID = CoreFoundation.INSTANCE.CFNumberGetTypeID();

    if (!typeID.equals(numberTypeID)) {
      return null;
    }

    CFNumberRef cfNumberRef = new CFNumberRef(cfTypeRef.getPointer());

    CFIndex numberType = new CFIndex(kCFNumberSInt16Type);

    ShortByReference value = new ShortByReference();
    byte success = CoreFoundation.INSTANCE.CFNumberGetValue(cfNumberRef, numberType, value);

    if (success == 0) {
      return null;
    }

    return value.getValue();
  }
}
