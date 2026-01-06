// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.io.modem;

import java.util.List;
import tic.io.PortFinder;

/** Interface used to find all modem descriptors */
public interface ModemFinder extends PortFinder<ModemDescriptor> {
  /**
   * Find modem descriptor matching with port id.
   *
   * @param portId the unique port identifier desired
   * @return modem descriptor found, or null if nothing matches with portId
   */
  public default ModemDescriptor findByPortId(String portId) {
    return this.findAll().stream()
        .filter(p -> (p.portId() != null) ? p.portId().equals(portId) : portId == null)
        .findFirst()
        .orElse(null);
  }

  /**
   * Find modem descriptor matching with port name.
   *
   * @param portName the port name desired
   * @return modem descriptor found, or null if nothing matches with portName
   */
  public default ModemDescriptor findByPortName(String portName) {
    return this.findAll().stream()
        .filter(
            p -> (p.portName() != null) ? p.portName().equals(portName) : portName == null)
        .findFirst()
        .orElse(null);
  }

  /**
   * Find native modem (not USB) descriptor matching with port name.
   *
   * @param portName the port name desired
   * @return modem descriptor found, or null if nothing matches with portName
   */
  public ModemDescriptor findNative(String portName);

  /**
   * Find modem descriptor matching with port id or port name.
   *
   * @param portId the unique port identifier desired
   * @param portName the port name desired
   * @return modem descriptor found, or null if nothing matches with portName
   */
  public default ModemDescriptor findByPortIdOrPortName(String portId, String portName) {
    ModemDescriptor descriptor = this.findByPortId(portId);

    if (descriptor == null) {
      descriptor = this.findByPortName(portName);
    }

    return descriptor;
  }

  @Override
  List<ModemDescriptor> findAll();
}
