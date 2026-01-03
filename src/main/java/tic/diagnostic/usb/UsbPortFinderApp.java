// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.diagnostic.usb;

import java.util.List;
import tic.io.usb.UsbPortDescriptor;
import tic.io.usb.UsbPortFinderBase;
import tic.io.usb.UsbPortJsonEncoder;

public class UsbPortFinderApp {

  /**
   * Program writing the USB port descriptor list (JSON format) on the output stream
   *
   * @param args not used
   */
  public static void main(String[] args) {
    try {
      List<UsbPortDescriptor> descriptors = UsbPortFinderBase.getInstance().findAll();
      System.out.println(UsbPortJsonEncoder.encode(descriptors));
    } catch (Exception exception) {
      System.err.println(exception.getMessage());
    }
  }
}
