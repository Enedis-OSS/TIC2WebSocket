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
 * Configuration class for TIC data streams.
 *
 * <p>
 * This class extends {@link DataStreamConfiguration} to provide configuration
 * management
 * for TIC data streams, including TIC mode selection, type, direction, and
 * channel name.
 * It supports construction from maps, data dictionaries, or direct parameters,
 * and ensures
 * that all required configuration keys are present and valid for TIC
 * input/output streams.
 *
 * <p>
 * Key features:
 * <ul>
 * <li>Supports both standard and historic TIC modes</li>
 * <li>Validates configuration parameters for TIC data streams</li>
 * <li>Provides accessors for TIC mode and other stream properties</li>
 * </ul>
 *
 * @author Enedis Smarties team
 * @see DataStreamConfiguration
 * @see TICMode
 */
public class TICStreamConfiguration extends DataStreamConfiguration {
  /**
   * Key for the TIC mode parameter in the configuration.
   */
  protected static final String KEY_TIC_MODE = "ticMode";

  /**
   * Accepted value for the stream type (TIC).
   */
  private static final DataStreamType TYPE_ACCEPTED_VALUE = DataStreamType.TIC;

  /**
   * Accepted values for the stream direction (INPUT or OUTPUT).
   */
  private static final DataStreamDirection[] DIRECTION_ACCEPTED_VALUES = {
      DataStreamDirection.OUTPUT, DataStreamDirection.INPUT
  };

  /**
   * Default value for the TIC mode (AUTO).
   */
  private static final TICMode TIC_MODE_DEFAULT_VALUE = TICMode.AUTO;

  /**
   * List of key descriptors for configuration validation.
   */
  private List<KeyDescriptor<?>> keys = new ArrayList<KeyDescriptor<?>>();

  /**
   * Key descriptor for the TIC mode parameter.
   */
  protected KeyDescriptorEnum<TICMode> kTicMode;

  /**
   * Protected default constructor. Initializes key descriptors and sets default
   * values.
   */
  protected TICStreamConfiguration() {
    super();
    this.loadKeyDescriptors();

    this.kType.setAcceptedValues(TYPE_ACCEPTED_VALUE);
    this.kDirection.setAcceptedValues(DIRECTION_ACCEPTED_VALUES);
    this.kChannelName.setMandatory(true);
  }

  /**
   * Constructs a TICStreamConfiguration from a map of parameters.
   *
   * @param map the map containing configuration parameters
   * @throws DataDictionaryException if the configuration is invalid
   */
  public TICStreamConfiguration(Map<String, Object> map) throws DataDictionaryException {
    this();
    this.copy(fromMap(map));
  }

  /**
   * Constructs a TICStreamConfiguration from a {@link DataDictionary}.
   *
   * @param other the data dictionary containing configuration parameters
   * @throws DataDictionaryException if the configuration is invalid
   */
  public TICStreamConfiguration(DataDictionary other) throws DataDictionaryException {
    this();
    this.copy(other);
  }

  /**
   * Constructs a TICStreamConfiguration with the specified name and file, using
   * default parameters.
   *
   * @param name the configuration name
   * @param file the configuration file
   */
  public TICStreamConfiguration(String name, File file) {
    this();
    this.init(name, file);
  }

  /**
   * Constructs a TICStreamConfiguration with the specified parameters.
   *
   * @param name        the configuration name
   * @param direction   the data stream direction (INPUT or OUTPUT)
   * @param channelName the channel name
   * @param ticMode     the TIC mode (STANDARD, HISTORIC, or AUTO)
   * @throws DataDictionaryException if the configuration is invalid
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

  /**
   * Updates optional configuration parameters to their default values if not set.
   *
   * @throws DataDictionaryException if an error occurs during update
   */
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
   * Returns the configured TIC mode for this stream.
   *
   * @return the TIC mode (STANDARD, HISTORIC, or AUTO)
   */
  public TICMode getTicMode() {
    return (TICMode) this.data.get(KEY_TIC_MODE);
  }

  /**
   * Sets the TIC mode for this stream.
   *
   * @param ticMode the TIC mode to set (STANDARD, HISTORIC, or AUTO)
   * @throws DataDictionaryException if the value is invalid
   */
  public void setTicMode(TICMode ticMode) throws DataDictionaryException {
    this.setTicMode((Object) ticMode);
  }

  /**
   * Sets the TIC mode for this stream using a generic object.
   *
   * @param ticMode the TIC mode as an object
   * @throws DataDictionaryException if the value is invalid
   */
  protected void setTicMode(Object ticMode) throws DataDictionaryException {
    this.data.put(KEY_TIC_MODE, this.kTicMode.convert(ticMode));
  }

  /**
   * Loads key descriptors for configuration validation and type conversion.
   */
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
