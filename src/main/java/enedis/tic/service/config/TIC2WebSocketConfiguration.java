// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.tic.service.config;

import enedis.lab.protocol.tic.TICMode;
import enedis.lab.types.DataDictionary;
import enedis.lab.types.DataDictionaryException;
import enedis.lab.types.configuration.ConfigurationBase;
import enedis.lab.types.datadictionary.KeyDescriptor;
import enedis.lab.types.datadictionary.KeyDescriptorEnum;
import enedis.lab.types.datadictionary.KeyDescriptorListMinMaxSize;
import enedis.lab.types.datadictionary.KeyDescriptorNumberMinMax;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
public class TIC2WebSocketConfiguration extends ConfigurationBase {
  /** Key for server port configuration. */
  protected static final String KEY_SERVER_PORT = "serverPort";

  /** Key for TIC mode configuration. */
  protected static final String KEY_TIC_MODE = "ticMode";

  /** Key for TIC port names configuration. */
  protected static final String KEY_TIC_PORT_NAMES = "ticPortNames";

  /** Minimum allowed server port value. */
  private static final Number SERVER_PORT_MIN = 1;

  /** Maximum allowed server port value. */
  private static final Number SERVER_PORT_MAX = 65535;

  /** Minimum number of TIC port names required. */
  private static final int TIC_PORT_NAMES_MIN_SIZE = 1;

  /** List of key descriptors for configuration parameters. */
  private final List<KeyDescriptor<?>> keys = new ArrayList<>();

  /** Key descriptor for server port. */
  protected KeyDescriptorNumberMinMax kServerPort;

  /** Key descriptor for TIC mode. */
  protected KeyDescriptorEnum<TICMode> kTicMode;

  /** Key descriptor for TIC port names. */
  protected KeyDescriptorListMinMaxSize<String> kTicPortNames;

  /** Constructs a configuration with default values and loads key descriptors. */
  protected TIC2WebSocketConfiguration() {
    super();
    this.loadKeyDescriptors();
  }

  /**
   * Constructs a configuration from a map of values.
   *
   * <p>Initializes the configuration using a map of key-value pairs.
   *
   * @param map the map containing configuration parameters
   * @throws DataDictionaryException if validation fails
   */
  public TIC2WebSocketConfiguration(Map<String, Object> map) throws DataDictionaryException {
    this();
    this.copy(fromMap(map));
  }

  /**
   * Constructs a configuration from another DataDictionary instance.
   *
   * <p>Copies configuration values from the provided DataDictionary.
   *
   * @param other the DataDictionary to copy from
   * @throws DataDictionaryException if validation fails
   */
  public TIC2WebSocketConfiguration(DataDictionary other) throws DataDictionaryException {
    this();
    this.copy(other);
  }

  /**
   * Constructs a configuration with a specified name and file, using default parameter values.
   *
   * @param name the configuration name
   * @param file the configuration file
   */
  public TIC2WebSocketConfiguration(String name, File file) {
    this();
    this.init(name, file);
  }

  /**
   * Constructs a configuration with explicit parameter values.
   *
   * <p>Initializes the configuration with the specified server port, TIC mode, and TIC port names.
   *
   * @param serverPort the server port number
   * @param ticMode the TIC mode
   * @param ticPortNames the list of TIC port names
   * @throws DataDictionaryException if validation fails
   */
  public TIC2WebSocketConfiguration(Number serverPort, TICMode ticMode, List<String> ticPortNames)
      throws DataDictionaryException {
    this();

    this.setServerPort(serverPort);
    this.setTicMode(ticMode);
    this.setTicPortNames(ticPortNames);

    this.checkAndUpdate();
  }

  /**
   * Returns the configured server port number.
   *
   * @return the server port
   */
  public Number getServerPort() {
    return (Number) this.data.get(KEY_SERVER_PORT);
  }

  /**
   * Returns the configured TIC mode.
   *
   * @return the TIC mode
   */
  public TICMode getTicMode() {
    return (TICMode) this.data.get(KEY_TIC_MODE);
  }

  /**
   * Returns the list of configured TIC port names.
   *
   * @return the list of TIC port names
   */
  @SuppressWarnings("unchecked")
  public List<String> getTicPortNames() {
    return (List<String>) this.data.get(KEY_TIC_PORT_NAMES);
  }

  /**
   * Sets the server port value.
   *
   * @param serverPort the server port number
   * @throws DataDictionaryException if validation fails
   */
  public void setServerPort(Number serverPort) throws DataDictionaryException {
    this.setServerPort((Object) serverPort);
  }

  /**
   * Sets the TIC mode value.
   *
   * @param ticMode the TIC mode
   * @throws DataDictionaryException if validation fails
   */
  public void setTicMode(TICMode ticMode) throws DataDictionaryException {
    this.setTicMode((Object) ticMode);
  }

  /**
   * Sets the list of TIC port names.
   *
   * @param ticPortNames the list of TIC port names
   * @throws DataDictionaryException if validation fails
   */
  public void setTicPortNames(List<String> ticPortNames) throws DataDictionaryException {
    this.setTicPortNames((Object) ticPortNames);
  }

  /**
   * Internal setter for server port, with conversion and validation.
   *
   * @param serverPort the server port value (Object or Number)
   * @throws DataDictionaryException if validation fails
   */
  protected void setServerPort(Object serverPort) throws DataDictionaryException {
    this.data.put(KEY_SERVER_PORT, this.kServerPort.convert(serverPort));
  }

  /**
   * Internal setter for TIC mode, with conversion and validation.
   *
   * @param ticMode the TIC mode value (Object or TICMode)
   * @throws DataDictionaryException if validation fails
   */
  protected void setTicMode(Object ticMode) throws DataDictionaryException {
    this.data.put(KEY_TIC_MODE, this.kTicMode.convert(ticMode));
  }

  /**
   * Internal setter for TIC port names, with conversion and validation.
   *
   * <p>Checks for null, empty, and duplicate port names.
   *
   * @param ticPortNames the list or object representing TIC port names
   * @throws DataDictionaryException if validation fails
   */
  protected void setTicPortNames(Object ticPortNames) throws DataDictionaryException {
    List<String> portNames = this.kTicPortNames.convert(ticPortNames);
    for (int i = 0; i < portNames.size(); i++) {
      String portName = portNames.get(i);
      if (portName == null) {
        throw new DataDictionaryException(
            "Key "
                + KEY_TIC_PORT_NAMES
                + ": value at index "
                + i
                + "("
                + portName
                + ") cannot be null");
      } else if (portName.isEmpty()) {
        throw new DataDictionaryException(
            "Key "
                + KEY_TIC_PORT_NAMES
                + ": value at index "
                + i
                + "("
                + portName
                + ") cannot be empty");
      } else if (i != portNames.lastIndexOf(portName)) {
        throw new DataDictionaryException(
            "Key "
                + KEY_TIC_PORT_NAMES
                + ": value at index "
                + i
                + " and "
                + portNames.lastIndexOf(portName)
                + "("
                + portName
                + ") are identical");
      }
    }
    this.data.put(KEY_TIC_PORT_NAMES, portNames);
  }

  /**
   * Loads key descriptors for configuration parameters.
   *
   * <p>Initializes descriptors for server port, TIC mode, and TIC port names, and adds them to the
   * configuration.
   */
  private void loadKeyDescriptors() {
    try {
      this.kServerPort =
          new KeyDescriptorNumberMinMax(KEY_SERVER_PORT, true, SERVER_PORT_MIN, SERVER_PORT_MAX);
      this.keys.add(this.kServerPort);

      this.kTicMode = new KeyDescriptorEnum<TICMode>(KEY_TIC_MODE, false, TICMode.class);
      this.keys.add(this.kTicMode);

      this.kTicPortNames =
          new KeyDescriptorListMinMaxSize<>(
              KEY_TIC_PORT_NAMES, false, String.class, TIC_PORT_NAMES_MIN_SIZE, null);
      this.keys.add(this.kTicPortNames);

      this.addAllKeyDescriptor(this.keys);
    } catch (DataDictionaryException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }
}
