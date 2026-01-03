// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.io.modem;

import java.util.ArrayList;
import java.util.List;
import tic.io.serialport.SerialPortDescriptor;
import tic.io.serialport.SerialPortFinder;
import tic.io.usb.UsbPortDescriptor;
import tic.io.usb.UsbPortFinder;

/** Class used to find all modem descriptors */
public class ModemFinderBase implements ModemFinder {

  /**
   * Create ModemFinder instance
   *
   * @param serialPortFinder the SerialPortFinder instance
   * @param usbPortFinder the UsbPortFinder instance
   * @return ModemFinder instance created
   * @throws IllegalArgumentException if any input instance is null
   */
  public static ModemFinder create(SerialPortFinder serialPortFinder, UsbPortFinder usbPortFinder) {

    if (serialPortFinder == null) {
      throw new IllegalArgumentException("Cannot set null serial port finder");
    }
    if (usbPortFinder == null) {
      throw new IllegalArgumentException("Cannot set null USB port finder");
    }

    return new ModemFinderBase(serialPortFinder, usbPortFinder);
  }

  private final SerialPortFinder serialPortFinder;
  private final UsbPortFinder usbPortFinder;

  /**
   * Constructor with finder parameters
   *
   * @param serialPortFinder the serial port finder interface
   * @param usbPortFinder the USB port finder interface
   */
  private ModemFinderBase(SerialPortFinder serialPortFinder, UsbPortFinder usbPortFinder) {
    this.serialPortFinder = serialPortFinder;
    this.usbPortFinder = usbPortFinder;
  }

  @Override
  public List<ModemDescriptor> findAll() {
    List<ModemDescriptor> descriptors = new ArrayList<>();

    for (ModemType modemType : ModemType.values()) {
      List<SerialPortDescriptor> tmpSerialPort =
          this.serialPortFinder.findByProductIdAndVendorId(
              modemType.productId(), modemType.vendorId());

      if (tmpSerialPort.isEmpty()) {
        List<UsbPortDescriptor> tmpUSBPort =
            this.usbPortFinder.findByProductIdAndVendorId(
                modemType.productId(), modemType.vendorId());

        for (UsbPortDescriptor upd : tmpUSBPort) {
          try {
            ModemDescriptor descriptor =
                new ModemDescriptor.Builder<>().copy(upd).modemType(modemType).build();
            descriptors.add(descriptor);
          } catch (IllegalArgumentException e) {
            // Ignore descriptors that fail validation
          }
        }
      } else {
        for (SerialPortDescriptor spd : tmpSerialPort) {
          try {
            ModemDescriptor descriptor =
                new ModemDescriptor.Builder<>().copy(spd).modemType(modemType).build();
            descriptors.add(descriptor);
          } catch (IllegalArgumentException e) {
            // Ignore descriptors that fail validation
          }
        }
      }
    }

    return descriptors;
  }

  @Override
  public ModemDescriptor findNative(String portName) {
    ModemDescriptor modemDescriptor = null;
    SerialPortDescriptor serialPortDescriptor = this.serialPortFinder.findNative(portName);

    if (serialPortDescriptor != null) {
      try {
        modemDescriptor = new ModemDescriptor.Builder<>().copy(serialPortDescriptor).build();
      } catch (IllegalArgumentException e) {
        // Ignore descriptors that fail validation
      }
    }

    return modemDescriptor;
  }
}
