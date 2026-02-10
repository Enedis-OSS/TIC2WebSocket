// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.diagnostic.core;

import picocli.CommandLine.Option;
import tic.core.TICIdentifier;

/** Options to identify a TIC (portName/portId/serialNumber). */
public final class TICCoreIdentifierOptions {
  @Option(
      names = {"--portName"},
      paramLabel = "NAME",
      description = "Port name (e.g. COM3, /dev/ttyUSB0).")
  String portName;

  @Option(
      names = {"--portId"},
      paramLabel = "ID",
      description = "Port identifier (if available).")
  String portId;

  @Option(
      names = {"--serialNumber"},
      paramLabel = "SN",
      description = "Modem serial number (if available).")
  String serialNumber;

  TICIdentifier toIdentifierOrNull() {
    if (this.portName == null && this.portId == null && this.serialNumber == null) {
      return null;
    }
    return new TICIdentifier.Builder()
        .portId(this.portId)
        .portName(this.portName)
        .serialNumber(this.serialNumber)
        .build();
  }
}
