// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.io.usb;

import java.util.ArrayList;
import java.util.List;
import org.usb4java.Context;
import org.usb4java.Device;
import org.usb4java.DeviceDescriptor;
import org.usb4java.DeviceHandle;
import org.usb4java.DeviceList;
import org.usb4java.LibUsb;

/** Class used to find all USB port descriptor */
public class UsbPortFinderBase implements UsbPortFinder {

  /**
   * Get instance
   *
   * @return Unique instance
   */
  public static UsbPortFinderBase getInstance() {
    if (instance == null) {
      instance = new UsbPortFinderBase();
    }

    return instance;
  }

  private static UsbPortFinderBase instance;

  private UsbPortFinderBase() {}

  @Override
  public List<UsbPortDescriptor> findAll() {
    List<UsbPortDescriptor> usbPortList = new ArrayList<>();

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
        UsbPortDescriptor usbPort =
            new UsbPortDescriptor.Builder()
                .bcdDevice(descriptor.bcdDevice())
                .bcdUSB(descriptor.bcdUSB())
                .bDescriptorType(descriptor.bDescriptorType())
                .bDeviceClass(descriptor.bDeviceClass())
                .bDeviceProtocol(descriptor.bDeviceProtocol())
                .bDeviceSubClass(descriptor.bDeviceSubClass())
                .bLength(descriptor.bLength())
                .bMaxPacketSize0(descriptor.bMaxPacketSize0())
                .bNumConfigurations(descriptor.bNumConfigurations())
                .idProduct(descriptor.idProduct())
                .idVendor(descriptor.idVendor())
                .iManufacturer(descriptor.iManufacturer())
                .iProduct(descriptor.iProduct())
                .iSerialNumber(descriptor.iSerialNumber())
                .manufacturer(manufacturer)
                .product(productName)
                .serialNumber(serialNumber)
                .build();
        usbPortList.add(usbPort);
      } catch (IllegalArgumentException e) {
      }
    }

    // Finalize USB library access
    LibUsb.freeDeviceList(deviceList, true);
    LibUsb.exit(context);
    return usbPortList;
  }
}
