// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.diagnostic.modem;

import java.util.List;
import tic.io.modem.ModemDescriptor;
import tic.io.modem.ModemFinder;
import tic.io.modem.ModemFinderBase;
import tic.io.modem.ModemJsonCodec;
import tic.io.serialport.SerialPortFinderBase;
import tic.io.usb.UsbPortFinderBase;

public class ModemFinderApp {

  /**
   * Program writing the modem descriptor list (JSON format) on the output stream
   *
   * @param args not used
   */
  public static void main(String[] args) {
    try {
      ModemFinder modemFinder =
          ModemFinderBase.create(
              SerialPortFinderBase.getInstance(), UsbPortFinderBase.getInstance());
      List<ModemDescriptor> descriptors = modemFinder.findAll();
      System.out.println(ModemJsonCodec.encode(descriptors));
    } catch (Exception exception) {
      System.err.println(exception.getMessage());
    }
  }
}
