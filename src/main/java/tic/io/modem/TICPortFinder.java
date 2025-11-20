// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.io.modem;

import java.util.List;
import tic.io.PortFinder;

/** Interface used to find all TIC port descriptor */
public interface TICPortFinder extends PortFinder<TICPortDescriptor> {
  /**
   * Find TIC port descriptor matching with port id
   *
   * @param portId the unique port identifier desired
   * @return TIC port descriptor found, or null if nothing matches with portId
   */
  public default TICPortDescriptor findByPortId(String portId) {
    return this.findAll().stream()
        .filter(p -> (p.getPortId() != null) ? p.getPortId().equals(portId) : portId == null)
        .findFirst()
        .orElse(null);
  }

  /**
   * Find TIC port descriptor matching with port name
   *
   * @param portName the port name desired
   * @return TIC port descriptor found, or null if nothing matches with portName
   */
  public default TICPortDescriptor findByPortName(String portName) {
    return this.findAll().stream()
        .filter(
            p -> (p.getPortName() != null) ? p.getPortName().equals(portName) : portName == null)
        .findFirst()
        .orElse(null);
  }

  /**
   * Find native TIC port (not USB) descriptor matching with port name
   *
   * @param portName the port name desired
   * @return TIC port descriptor found, or null if nothing matches with portName
   */
  public TICPortDescriptor findNative(String portName);

  /**
   * Find TIC port descriptor matching with port id or port name
   *
   * @param portId the unique port identifier desired
   * @param portName the port name desired
   * @return TIC port descriptor found, or null if nothing matches with portName
   */
  public default TICPortDescriptor findByPortIdOrPortName(String portId, String portName) {
    TICPortDescriptor descriptor = this.findByPortId(portId);

    if (descriptor == null) {
      descriptor = this.findByPortName(portName);
    }

    return descriptor;
  }

  @Override
  List<TICPortDescriptor> findAll();
}
