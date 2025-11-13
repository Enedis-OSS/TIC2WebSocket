// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.io;

import enedis.lab.types.DataList;

/**
 * Generic interface for discovering and enumerating ports on the system.
 *
 * <p>This interface defines a contract for port discovery implementations that can find and return
 * information about available ports. It uses generics to support different types of port
 * descriptors, such as serial ports, USB ports, or other communication ports.
 *
 * <p>Implementations of this interface are responsible for querying the underlying system (via
 * OS-specific APIs, device managers, or file systems) to enumerate all available ports and return
 * their descriptors.
 *
 * @param <T> the type of port descriptor returned by this finder
 * @author Enedis Smarties team
 * @see DataList
 */
public interface PortFinder<T> {
  /**
   * Discovers and returns all available ports on the system.
   *
   * <p>This method queries the system to find all ports that are currently available. The specific
   * discovery mechanism depends on the implementation and may involve platform-specific APIs or
   * system queries.
   *
   * @return a list of port descriptors representing all discovered ports
   */
  public DataList<T> findAll();
}
