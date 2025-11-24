// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.io.modem;

import tic.io.PlugSubscriber;
import tic.io.PortPlugNotifier;
import java.util.Arrays;

/** Class used to notify when a TIC port has been plugged or unplugged */
public class TICPortPlugNotifier extends PortPlugNotifier<TICPortFinder, TICPortDescriptor> {
  private static final int DEFAULT_JSON_INDENTATION = 2;

  /**
   * Program writing on the output stream when an TIC port has been plugged or unplugged
   *
   * @param args not used
   */
  public static void main(String[] args) {
    /* 1. Create notification service */
    TICPortPlugNotifier notifier = new TICPortPlugNotifier();
    /* 2. Create subscriber to print when an TIC port has been plugged or unplugged */
    PlugSubscriber<TICPortDescriptor> subscriber =
        new PlugSubscriber<TICPortDescriptor>() {
          @Override
          public void onPlugged(TICPortDescriptor descriptor) {
            String payload =
                TICPortJsonEncoder.encode(
                    new java.util.ArrayList<>(Arrays.asList(descriptor)), DEFAULT_JSON_INDENTATION);
            System.out.println(payload + "\n");
          }

          @Override
          public void onUnplugged(TICPortDescriptor descriptor) {
            String payload =
                TICPortJsonEncoder.encode(
                    new java.util.ArrayList<>(Arrays.asList(descriptor)), DEFAULT_JSON_INDENTATION);
            System.out.println(payload + "\n");
          }
        };
    /* 3. Run program printing when an TIC port has been plugged or unplugged until CTRL+C is pressed */
    PortPlugNotifier.main(notifier, subscriber);
  }

  /**
   * Constructor with default parameter
   *
   * @see #DEFAULT_PERIOD
   * @see TICPortFinderBase
   */
  public TICPortPlugNotifier() {
    this(DEFAULT_PERIOD, TICPortFinderBase.getInstance());
  }

  /**
   * Constructor with all parameters
   *
   * @param period the period (in milliseconds) used to look for plugged or unplugged TIC port
   * @param finder the TIC port finder interface used to find all TIC port descriptors
   */
  public TICPortPlugNotifier(long period, TICPortFinder finder) {
    super(period, finder);
  }
}
