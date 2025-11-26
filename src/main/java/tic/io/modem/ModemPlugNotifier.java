// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.io.modem;

import java.util.Arrays;

import tic.io.PlugSubscriber;
import tic.io.PortPlugNotifier;

/** Class used to notify when a modem has been plugged or unplugged */
public class ModemPlugNotifier extends PortPlugNotifier<ModemFinder, ModemDescriptor> {
  private static final int DEFAULT_JSON_INDENTATION = 2;

  /**
   * Program writing on the output stream when a modem has been plugged or unplugged
   *
   * @param args not used
   */
  public static void main(String[] args) {
    /* 1. Create notification service */
    ModemPlugNotifier notifier = new ModemPlugNotifier();
    /* 2. Create subscriber to print when a modem has been plugged or unplugged */
    PlugSubscriber<ModemDescriptor> subscriber =
        new PlugSubscriber<ModemDescriptor>() {
          @Override
          public void onPlugged(ModemDescriptor descriptor) {
            String payload =
                ModemJsonEncoder.encode(
                    new java.util.ArrayList<>(Arrays.asList(descriptor)), DEFAULT_JSON_INDENTATION);
            System.out.println("onPlugged event:\n" + payload + "\n");
          }

          @Override
          public void onUnplugged(ModemDescriptor descriptor) {
            String payload =
                ModemJsonEncoder.encode(
                    new java.util.ArrayList<>(Arrays.asList(descriptor)), DEFAULT_JSON_INDENTATION);
            System.out.println("onUnplugged event:\n" + payload + "\n");
          }
        };
    /* 3. Run program printing when a modem has been plugged or unplugged until CTRL+C is pressed */
    PortPlugNotifier.main(notifier, subscriber);
  }

  /**
   * Constructor with default parameter
   *
   * @see #DEFAULT_PERIOD
   * @see ModemFinderBase
   */
  public ModemPlugNotifier() {
    this(DEFAULT_PERIOD, ModemFinderBase.getInstance());
  }

  /**
   * Constructor with all parameters
   *
  * @param period the period (in milliseconds) used to look for plugged or unplugged modems
   * @param finder the modem finder interface used to find all modem descriptors
   */
  public ModemPlugNotifier(long period, ModemFinder finder) {
    super(period, finder);
  }
}
