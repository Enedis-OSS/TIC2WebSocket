// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.io.modem;

import tic.io.PortPlugNotifier;

/** Class used to notify when a modem has been plugged or unplugged */
public class ModemPlugNotifier extends PortPlugNotifier<ModemFinder, ModemDescriptor> {

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
