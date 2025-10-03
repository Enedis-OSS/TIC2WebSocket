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
 * TIC2WebSocketConfiguration class
 *
 * <p>Generated
 */
public class TIC2WebSocketConfiguration extends ConfigurationBase {
  protected static final String KEY_SERVER_PORT = "serverPort";
  protected static final String KEY_TIC_MODE = "ticMode";
  protected static final String KEY_TIC_PORT_NAMES = "ticPortNames";

  private static final Number SERVER_PORT_MIN = 1;
  private static final Number SERVER_PORT_MAX = 65535;
  private static final int TIC_PORT_NAMES_MIN_SIZE = 1;

  private List<KeyDescriptor<?>> keys = new ArrayList<KeyDescriptor<?>>();

  protected KeyDescriptorNumberMinMax kServerPort;
  protected KeyDescriptorEnum<TICMode> kTicMode;
  protected KeyDescriptorListMinMaxSize<String> kTicPortNames;

  protected TIC2WebSocketConfiguration() {
    super();
    this.loadKeyDescriptors();
  }

  /**
   * Constructor using map
   *
   * @param map
   * @throws DataDictionaryException
   */
  public TIC2WebSocketConfiguration(Map<String, Object> map) throws DataDictionaryException {
    this();
    this.copy(fromMap(map));
  }

  /**
   * Constructor using datadictionary
   *
   * @param other
   * @throws DataDictionaryException
   */
  public TIC2WebSocketConfiguration(DataDictionary other) throws DataDictionaryException {
    this();
    this.copy(other);
  }

  /**
   * Constructor setting configuration name/file and parameters to default values
   *
   * @param name the configuration name
   * @param file the configuration file
   */
  public TIC2WebSocketConfiguration(String name, File file) {
    this();
    this.init(name, file);
  }

  /**
   * Constructor setting parameters to specific values
   *
   * @param serverPort
   * @param ticMode
   * @param ticPortNames
   * @throws DataDictionaryException
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
   * Get server port
   *
   * @return the server port
   */
  public Number getServerPort() {
    return (Number) this.data.get(KEY_SERVER_PORT);
  }

  /**
   * Get tic mode
   *
   * @return the tic mode
   */
  public TICMode getTicMode() {
    return (TICMode) this.data.get(KEY_TIC_MODE);
  }

  /**
   * Get tic port names
   *
   * @return the tic port names
   */
  @SuppressWarnings("unchecked")
  public List<String> getTicPortNames() {
    return (List<String>) this.data.get(KEY_TIC_PORT_NAMES);
  }

  /**
   * Set server port
   *
   * @param serverPort
   * @throws DataDictionaryException
   */
  public void setServerPort(Number serverPort) throws DataDictionaryException {
    this.setServerPort((Object) serverPort);
  }

  /**
   * Set tic mode
   *
   * @param ticMode
   * @throws DataDictionaryException
   */
  public void setTicMode(TICMode ticMode) throws DataDictionaryException {
    this.setTicMode((Object) ticMode);
  }

  /**
   * Set tic port names
   *
   * @param ticPortNames
   * @throws DataDictionaryException
   */
  public void setTicPortNames(List<String> ticPortNames) throws DataDictionaryException {
    this.setTicPortNames((Object) ticPortNames);
  }

  protected void setServerPort(Object serverPort) throws DataDictionaryException {
    this.data.put(KEY_SERVER_PORT, this.kServerPort.convert(serverPort));
  }

  protected void setTicMode(Object ticMode) throws DataDictionaryException {
    this.data.put(KEY_TIC_MODE, this.kTicMode.convert(ticMode));
  }

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

  private void loadKeyDescriptors() {
    try {
      this.kServerPort =
          new KeyDescriptorNumberMinMax(KEY_SERVER_PORT, true, SERVER_PORT_MIN, SERVER_PORT_MAX);
      this.keys.add(this.kServerPort);

      this.kTicMode = new KeyDescriptorEnum<TICMode>(KEY_TIC_MODE, false, TICMode.class);
      this.keys.add(this.kTicMode);

      this.kTicPortNames =
          new KeyDescriptorListMinMaxSize<String>(
              KEY_TIC_PORT_NAMES, false, String.class, TIC_PORT_NAMES_MIN_SIZE, null);
      this.keys.add(this.kTicPortNames);

      this.addAllKeyDescriptor(this.keys);
    } catch (DataDictionaryException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }
}
