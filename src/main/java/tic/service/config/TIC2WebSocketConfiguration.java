// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.service.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import tic.frame.TICMode;

/**
 * Configuration class for TIC2WebSocket service.
 *
 * <p>This class manages configuration parameters for the TIC2WebSocket server, including server
 * port, TIC mode, and the list of TIC port names. It provides validation and conversion logic for
 * each parameter, ensuring correct types and values.
 *
 * <p>Key features include:
 *
 * <ul>
 *   <li>Validation of server port range and TIC port names
 *   <li>Support for multiple construction methods (map, DataDictionary, explicit values)
 *   <li>Integration with the configuration base and key descriptor system
 * </ul>
 *
 * @author Enedis Smarties team
 * @see ConfigurationBase
 * @see TICMode
 */
public class TIC2WebSocketConfiguration {

  public static final String KEY_SERVER_PORT = "serverPort";
  public static final String KEY_TIC_MODE = "ticMode";
  public static final String KEY_TIC_PORT_NAMES = "ticPortNames";

  public static final int SERVER_PORT_MIN = 1;
  public static final int SERVER_PORT_MAX = 65535;

  public static final TICMode DEFAULT_TIC_MODE = TICMode.AUTO;

  private int serverPort;
  private TICMode ticMode;
  private List<String> ticPortNames;

  /**
   * Constructs a configuration with a mandatory server port.
   *
   * @param serverPort the server port number
   */
  public TIC2WebSocketConfiguration(int serverPort) {
    this(serverPort, null, null);
  }

  /**
   * Constructs a configuration with a mandatory server port, and optional TIC mode and port names.
   *
   * @param serverPort the server port number
   * @param ticMode the TIC mode (optional, defaults to {@link #DEFAULT_TIC_MODE} when null)
   * @param ticPortNames list of TIC native port names (optional, null/empty means not set)
   */
  public TIC2WebSocketConfiguration(int serverPort, TICMode ticMode, List<String> ticPortNames) {
    this.setServerPort(serverPort);
    this.setTicMode(ticMode);
    this.setTicPortNames(ticPortNames);
  }

  public int getServerPort() {
    return this.serverPort;
  }

  public TICMode getTicMode() {
    return this.ticMode;
  }

  /**
   * Returns the list of configured TIC port names.
   *
   * @return an unmodifiable list, or null if not set
   */
  public List<String> getTicPortNames() {
    return this.ticPortNames;
  }

  private void setServerPort(int serverPort) {
    checkServerPort(serverPort);
    this.serverPort = serverPort;
  }

  private void setTicMode(TICMode ticMode) {
    this.ticMode = (ticMode == null) ? DEFAULT_TIC_MODE : ticMode;
  }

  private void setTicPortNames(List<String> ticPortNames) {
    this.ticPortNames = normalizeAndCheckPortNames(ticPortNames);
  }

  private static void checkServerPort(int serverPort) {
    if (serverPort < SERVER_PORT_MIN || serverPort > SERVER_PORT_MAX) {
      throw new IllegalArgumentException(
          "Server port must be between " + SERVER_PORT_MIN + " and " + SERVER_PORT_MAX);
    }
  }

  private static List<String> normalizeAndCheckPortNames(List<String> ticPortNames) {
    if (ticPortNames == null || ticPortNames.isEmpty()) {
      return null;
    }

    List<String> normalized = new ArrayList<>(ticPortNames.size());
    Set<String> seen = new HashSet<>();

    for (int i = 0; i < ticPortNames.size(); i++) {
      String portName = ticPortNames.get(i);
      if (portName == null) {
        throw new IllegalArgumentException(
            "Key " + KEY_TIC_PORT_NAMES + ": value at index " + i + " cannot be null");
      }

      String trimmed = portName.trim();
      if (trimmed.isEmpty()) {
        throw new IllegalArgumentException(
            "Key " + KEY_TIC_PORT_NAMES + ": value at index " + i + " cannot be empty");
      }

      if (!seen.add(trimmed)) {
        throw new IllegalArgumentException(
            "Key " + KEY_TIC_PORT_NAMES + ": duplicate value '" + trimmed + "'");
      }

      normalized.add(trimmed);
    }

    return Collections.unmodifiableList(normalized);
  }
}
