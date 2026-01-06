// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.diagnostic.serialport;

import java.util.List;
import tic.io.serialport.SerialPortDescriptor;
import tic.io.serialport.SerialPortFinderBase;
import tic.io.serialport.SerialPortJsonEncoder;

public class SerialPortFinderApp {

  /**
   * Main method that outputs all serial port descriptors in JSON format.
   *
   * <p>This utility method can be used to quickly inspect available serial ports on the system. The
   * output is JSON-formatted with an indentation of 2 spaces.
   *
   * @param args command-line arguments (not used)
   */
  public static void main(String[] args) {
    try {
      List<SerialPortDescriptor> descriptors = SerialPortFinderBase.getInstance().findAll();
      System.out.println(SerialPortJsonEncoder.encode(descriptors));
    } catch (Exception exception) {
      System.err.println(exception.getMessage());
    }
  }
}
