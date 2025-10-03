// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.protocol.tic.datastreams;

import enedis.lab.io.datastreams.DataStreamConfiguration;
import enedis.lab.io.datastreams.DataStreamDirection;
import enedis.lab.io.datastreams.DataStreamType;
import enedis.lab.protocol.tic.TICMode;
import enedis.lab.types.DataDictionary;
import enedis.lab.types.DataDictionaryException;
import enedis.lab.types.datadictionary.KeyDescriptor;
import enedis.lab.types.datadictionary.KeyDescriptorEnum;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TICStreamConfiguration class
 *
 * <p>Generated
 */
public class TICStreamConfiguration extends DataStreamConfiguration {
  protected static final String KEY_TIC_MODE = "ticMode";

  private static final DataStreamType TYPE_ACCEPTED_VALUE = DataStreamType.TIC;
  private static final DataStreamDirection[] DIRECTION_ACCEPTED_VALUES = {
    DataStreamDirection.OUTPUT, DataStreamDirection.INPUT
  };
  private static final TICMode TIC_MODE_DEFAULT_VALUE = TICMode.AUTO;

  private List<KeyDescriptor<?>> keys = new ArrayList<KeyDescriptor<?>>();

  protected KeyDescriptorEnum<TICMode> kTicMode;

  protected TICStreamConfiguration() {
    super();
    this.loadKeyDescriptors();

    this.kType.setAcceptedValues(TYPE_ACCEPTED_VALUE);
    this.kDirection.setAcceptedValues(DIRECTION_ACCEPTED_VALUES);
    this.kChannelName.setMandatory(true);
  }

  /**
   * Constructor using map
   *
   * @param map
   * @throws DataDictionaryException
   */
  public TICStreamConfiguration(Map<String, Object> map) throws DataDictionaryException {
    this();
    this.copy(fromMap(map));
  }

  /**
   * Constructor using datadictionary
   *
   * @param other
   * @throws DataDictionaryException
   */
  public TICStreamConfiguration(DataDictionary other) throws DataDictionaryException {
    this();
    this.copy(other);
  }

  /**
   * Constructor setting configuration name/file and parameters to default values
   *
   * @param name the configuration name
   * @param file the configuration file
   */
  public TICStreamConfiguration(String name, File file) {
    this();
    this.init(name, file);
  }

  /**
   * Constructor setting parameters to specific values
   *
   * @param name
   * @param direction
   * @param channelName
   * @param ticMode
   * @throws DataDictionaryException
   */
  public TICStreamConfiguration(
      String name, DataStreamDirection direction, String channelName, TICMode ticMode)
      throws DataDictionaryException {
    this();

    this.setName(name);
    this.setDirection(direction);
    this.setChannelName(channelName);
    this.setTicMode(ticMode);

    this.checkAndUpdate();
  }

  @Override
  protected void updateOptionalParameters() throws DataDictionaryException {
    if (!this.exists(KEY_TYPE)) {
      this.setType(TYPE_ACCEPTED_VALUE);
    }
    if (!this.exists(KEY_TIC_MODE)) {
      this.setTicMode(TIC_MODE_DEFAULT_VALUE);
    }
    super.updateOptionalParameters();
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
   * Set tic mode
   *
   * @param ticMode
   * @throws DataDictionaryException
   */
  public void setTicMode(TICMode ticMode) throws DataDictionaryException {
    this.setTicMode((Object) ticMode);
  }

  protected void setTicMode(Object ticMode) throws DataDictionaryException {
    this.data.put(KEY_TIC_MODE, this.kTicMode.convert(ticMode));
  }

  private void loadKeyDescriptors() {
    try {
      this.kTicMode = new KeyDescriptorEnum<TICMode>(KEY_TIC_MODE, true, TICMode.class);
      this.keys.add(this.kTicMode);

      this.addAllKeyDescriptor(this.keys);
    } catch (DataDictionaryException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }
}
