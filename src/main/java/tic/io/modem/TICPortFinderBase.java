// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.io.modem;

import enedis.lab.io.serialport.SerialPortDescriptor;
import enedis.lab.io.serialport.SerialPortFinder;
import enedis.lab.io.serialport.SerialPortFinderBase;
import java.util.ArrayList;
import java.util.List;
import tic.io.usb.USBPortDescriptor;
import tic.io.usb.USBPortFinder;
import tic.io.usb.USBPortFinderBase;

/** Class used to find all TIC port descriptor */
public class TICPortFinderBase implements TICPortFinder {
  private static final int DEFAULT_JSON_INDENT = 2;

  /**
   * Program writing the TIC port descriptor list (JSON format) on the output stream
   *
   * @param args not used
   */
  public static void main(String[] args) {
    List<TICPortDescriptor> descriptors = getInstance().findAll();
    // TODO: replace System.out.println(descriptors.toString(2));
  }

  /**
   * Get instance
   *
   * @return Unique instance
   */
  public static TICPortFinderBase getInstance() {
    if (instance == null) {
      instance = new TICPortFinderBase();
    }

    return instance;
  }

  private static TICPortFinderBase instance;

  private final SerialPortFinder serialPortFinder;
  private final USBPortFinder usbPortFinder;

  private TICPortFinderBase() {
    this(SerialPortFinderBase.getInstance(), USBPortFinderBase.getInstance());
  }

  /**
   * Constructor with finder parameters
   *
   * @param serialPortFinder the serial port finder interface
   * @param usbPortFinder the USB port finder interface
   */
  public TICPortFinderBase(SerialPortFinder serialPortFinder, USBPortFinder usbPortFinder) {
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
  public List<TICPortDescriptor> findAll() {
    List<TICPortDescriptor> ticSerialPort = new ArrayList<>();

    for (TICModemType modemType : TICModemType.values()) {
      List<SerialPortDescriptor> tmpSerialPort =
          this.serialPortFinder.findByProductIdAndVendorId(
              modemType.getProductId(), modemType.getVendorId());

      if (tmpSerialPort.isEmpty()) {
        List<USBPortDescriptor> tmpUSBPort =
            this.usbPortFinder.findByProductIdAndVendorId(
                modemType.getProductId(), modemType.getVendorId());

        for (USBPortDescriptor upd : tmpUSBPort) {
          try {
            TICPortDescriptor tic = new TICPortDescriptor(upd, modemType);
            ticSerialPort.add(tic);
          } catch (IllegalArgumentException e) {
            // Ignore descriptors that fail validation
          }
        }
      } else {
        for (SerialPortDescriptor spd : tmpSerialPort) {
          try {
            TICPortDescriptor tic = new TICPortDescriptor(spd, modemType);
            ticSerialPort.add(tic);
          } catch (IllegalArgumentException e) {
            // Ignore descriptors that fail validation
          }
        }
      }
    }

    return ticSerialPort;
  }

  @Override
  public TICPortDescriptor findNative(String portName) {
    TICPortDescriptor ticPortDescriptor = null;
    SerialPortDescriptor serialPortDescriptor = this.serialPortFinder.findNative(portName);

    if (serialPortDescriptor != null) {
      try {
        ticPortDescriptor = new TICPortDescriptor(serialPortDescriptor, null);
      } catch (IllegalArgumentException e) {
        // Ignore descriptors that fail validation
      }
    }

    return ticPortDescriptor;
  }
}
