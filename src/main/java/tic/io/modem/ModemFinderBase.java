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
import tic.io.serialport.SerialPortFinderBase;
import tic.io.usb.UsbPortDescriptor;
import tic.io.usb.UsbPortFinder;
import tic.io.usb.UsbPortFinderBase;

/** Class used to find all modem descriptors */
public class ModemFinderBase implements ModemFinder {
  private static final int DEFAULT_JSON_INDENT = 2;

  /**
   * Program writing the modem descriptor list (JSON format) on the output stream
   *
   * @param args not used
   */
  public static void main(String[] args) {
    List<ModemDescriptor> descriptors = getInstance().findAll();
    System.out.println(ModemJsonEncoder.encode(descriptors, DEFAULT_JSON_INDENT));
  }

  /**
   * Get instance
   *
   * @return Unique instance
   */
  public static ModemFinderBase getInstance() {
    if (instance == null) {
      instance = new ModemFinderBase();
    }

    return instance;
  }

  private static ModemFinderBase instance;

  private final SerialPortFinder serialPortFinder;
  private final UsbPortFinder usbPortFinder;

  private ModemFinderBase() {
    this(SerialPortFinderBase.getInstance(), UsbPortFinderBase.getInstance());
  }

  /**
   * Constructor with finder parameters
   *
   * @param serialPortFinder the serial port finder interface
   * @param usbPortFinder the USB port finder interface
   */
  public ModemFinderBase(SerialPortFinder serialPortFinder, UsbPortFinder usbPortFinder) {
    if (serialPortFinder == null) {
      throw new IllegalArgumentException("Cannot set null serial port finder");
    }
    if (usbPortFinder == null) {
      throw new IllegalArgumentException("Cannot set null USB port finder");
    }
    this.serialPortFinder = serialPortFinder;
    this.usbPortFinder = usbPortFinder;
  }

  @Override
  public List<ModemDescriptor> findAll() {
    List<ModemDescriptor> descriptors = new ArrayList<>();

    for (ModemType modemType : ModemType.values()) {
      List<SerialPortDescriptor> tmpSerialPort =
          this.serialPortFinder.findByProductIdAndVendorId(
              modemType.getProductId(), modemType.getVendorId());

      if (tmpSerialPort.isEmpty()) {
        List<UsbPortDescriptor> tmpUSBPort =
            this.usbPortFinder.findByProductIdAndVendorId(
                modemType.getProductId(), modemType.getVendorId());

        for (UsbPortDescriptor upd : tmpUSBPort) {
          try {
            ModemDescriptor descriptor = new ModemDescriptor(upd, modemType);
            descriptors.add(descriptor);
          } catch (IllegalArgumentException e) {
            // Ignore descriptors that fail validation
          }
        }
      } else {
        for (SerialPortDescriptor spd : tmpSerialPort) {
          try {
            ModemDescriptor descriptor = new ModemDescriptor(spd, modemType);
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
        modemDescriptor = new ModemDescriptor(serialPortDescriptor, null);
      } catch (IllegalArgumentException e) {
        // Ignore descriptors that fail validation
      }
    }

    return modemDescriptor;
  }
}
