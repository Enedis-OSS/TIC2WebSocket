// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.diagnostic.modem;

import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;

import tic.io.PlugSubscriber;
import tic.io.PortPlugNotifier;
import tic.io.modem.ModemDescriptor;
import tic.io.modem.ModemFinder;
import tic.io.modem.ModemFinderBase;
import tic.io.modem.ModemJsonCodec;
import tic.io.modem.ModemPlugNotifier;
import tic.io.serialport.SerialPortFinderBase;
import tic.io.usb.UsbPortFinderBase;

public class ModemPlugNotifierApp {

  /**
   * Program writing on the output stream when a modem has been plugged or unplugged
   *
   * @param args not used
   */
  public static void main(String[] args) {
    ModemFinder modemFinder =
        ModemFinderBase.create(SerialPortFinderBase.getInstance(), UsbPortFinderBase.getInstance());
    /* 1. Create notification service */
    ModemPlugNotifier notifier = new ModemPlugNotifier(1000, modemFinder);
    /* 2. Create subscriber to print when a modem has been plugged or unplugged */
    PlugSubscriber<ModemDescriptor> subscriber =
        new PlugSubscriber<ModemDescriptor>() {
          @Override
          public void onPlugged(ModemDescriptor descriptor) {
            List<ModemDescriptor> list = new java.util.ArrayList<>(Arrays.asList(descriptor));
            JSONArray array;
            try {
              array = (JSONArray) ModemJsonCodec.getInstance().encodeToJsonArray(list);
            } catch (Exception e) {
              array = new JSONArray();
            }
            String payload = array.toString(2);
            System.out.println("onPlugged event:\n" + payload + "\n");
          }

          @Override
          public void onUnplugged(ModemDescriptor descriptor) {
            List<ModemDescriptor> list = new java.util.ArrayList<>(Arrays.asList(descriptor));
            JSONArray array;
            try {
              array = (JSONArray) ModemJsonCodec.getInstance().encodeToJsonArray(list);
            } catch (Exception e) {
              array = new JSONArray();
            }
            String payload = array.toString(2);
            System.out.println("onUnplugged event:\n" + payload + "\n");
          }
        };
    /* 3. Run program printing when a modem has been plugged or unplugged until CTRL+C is pressed */
    PortPlugNotifier.main(notifier, subscriber);
  }
}
