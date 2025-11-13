// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.io.datastreams;

import enedis.lab.types.DataDictionary;
import enedis.lab.types.DataDictionaryException;
import enedis.lab.types.configuration.ConfigurationBase;
import enedis.lab.types.datadictionary.KeyDescriptor;
import enedis.lab.types.datadictionary.KeyDescriptorEnum;
import enedis.lab.types.datadictionary.KeyDescriptorString;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Configuration class for data streams.
 *
 * <p>This class extends {@link ConfigurationBase} and provides configuration management for data
 * streams. It defines the essential parameters needed to configure a data stream, including name,
 * type, direction, and associated channel name.
 *
 * <p>The configuration supports multiple construction methods: from maps, data dictionaries, files,
 * or direct parameter specification. It includes validation and type conversion capabilities
 * through the key descriptor system.
 *
 * @author Enedis Smarties team
 * @see ConfigurationBase
 * @see DataStreamType
 * @see DataStreamDirection
 * @see DataDictionaryException
 */
public class DataStreamConfiguration extends ConfigurationBase {
  protected static final String KEY_NAME = "name";
  protected static final String KEY_TYPE = "type";
  protected static final String KEY_DIRECTION = "direction";
  protected static final String KEY_CHANNEL_NAME = "channelName";

  private List<KeyDescriptor<?>> keys = new ArrayList<KeyDescriptor<?>>();

  protected KeyDescriptorString kName;
  protected KeyDescriptorEnum<DataStreamType> kType;
  protected KeyDescriptorEnum<DataStreamDirection> kDirection;
  protected KeyDescriptorString kChannelName;

  /**
   * Protected default constructor for internal use.
   *
   * <p>This constructor initializes the configuration with default values and loads the key
   * descriptors for validation and type conversion.
   */
  protected DataStreamConfiguration() {
    super();
    this.loadKeyDescriptors();
  }

  /**
   * Constructs a DataStreamConfiguration from a map of key-value pairs.
   *
   * <p>This constructor creates a configuration by copying values from the provided map. The map
   * should contain keys corresponding to the configuration parameters (name, type, direction,
   * channelName).
   *
   * @param map the map containing configuration parameters
   * @throws DataDictionaryException if the map contains invalid values or missing required keys
   */
  public DataStreamConfiguration(Map<String, Object> map) throws DataDictionaryException {
    this();
    this.copy(fromMap(map));
  }

  /**
   * Constructs a DataStreamConfiguration by copying from another DataDictionary.
   *
   * <p>This constructor creates a new configuration by copying all values from the provided
   * DataDictionary. This is useful for creating configurations from existing data structures.
   *
   * @param other the DataDictionary to copy configuration from
   * @throws DataDictionaryException if the source dictionary contains invalid values
   */
  public DataStreamConfiguration(DataDictionary other) throws DataDictionaryException {
    this();
    this.copy(other);
  }

  /**
   * Constructs a DataStreamConfiguration with a name and configuration file.
   *
   * <p>This constructor initializes the configuration with a specific name and loads additional
   * parameters from the specified file. The file should contain valid configuration data that can
   * be parsed and applied to this configuration.
   *
   * @param name the configuration name
   * @param file the configuration file to load parameters from
   */
  public DataStreamConfiguration(String name, File file) {
    this();
    this.init(name, file);
  }

  /**
   * Constructs a DataStreamConfiguration with specific parameter values.
   *
   * <p>This constructor creates a configuration with all parameters explicitly set. It validates
   * the parameters and updates the configuration accordingly.
   *
   * @param name the name of the data stream
   * @param type the type of the data stream
   * @param direction the direction of the data stream
   * @param channelName the name of the associated channel
   * @throws DataDictionaryException if any of the parameters are invalid
   */
  public DataStreamConfiguration(
      String name, DataStreamType type, DataStreamDirection direction, String channelName)
      throws DataDictionaryException {
    this();

    this.setName(name);
    this.setType(type);
    this.setDirection(direction);
    this.setChannelName(channelName);

    this.checkAndUpdate();
  }

  /**
   * Updates optional parameters in the configuration.
   *
   * <p>This method is called during configuration setup to update any optional parameters that may
   * depend on other configuration values.
   *
   * @throws DataDictionaryException if the parameter update fails
   */
  @Override
  protected void updateOptionalParameters() throws DataDictionaryException {
    super.updateOptionalParameters();
  }

  /**
   * Returns the name of the data stream.
   *
   * @return the name of the data stream
   */
  public String getName() {
    return (String) this.data.get(KEY_NAME);
  }

  /**
   * Returns the type of the data stream.
   *
   * @return the type of the data stream
   */
  public DataStreamType getType() {
    return (DataStreamType) this.data.get(KEY_TYPE);
  }

  /**
   * Returns the direction of the data stream.
   *
   * @return the direction of the data stream
   */
  public DataStreamDirection getDirection() {
    return (DataStreamDirection) this.data.get(KEY_DIRECTION);
  }

  /**
   * Returns the name of the associated channel.
   *
   * @return the name of the channel associated with this data stream
   */
  public String getChannelName() {
    return (String) this.data.get(KEY_CHANNEL_NAME);
  }

  /**
   * Sets the name of the data stream.
   *
   * @param name the name to set for the data stream
   * @throws DataDictionaryException if the name is invalid or null
   */
  public void setName(String name) throws DataDictionaryException {
    this.setName((Object) name);
  }

  /**
   * Sets the type of the data stream.
   *
   * @param type the type to set for the data stream
   * @throws DataDictionaryException if the type is invalid
   */
  public void setType(DataStreamType type) throws DataDictionaryException {
    this.setType((Object) type);
  }

  /**
   * Sets the direction of the data stream.
   *
   * @param direction the direction to set for the data stream
   * @throws DataDictionaryException if the direction is invalid
   */
  public void setDirection(DataStreamDirection direction) throws DataDictionaryException {
    this.setDirection((Object) direction);
  }

  /**
   * Sets the name of the associated channel.
   *
   * @param channelName the name of the channel to associate with this data stream
   * @throws DataDictionaryException if the channel name is invalid
   */
  public void setChannelName(String channelName) throws DataDictionaryException {
    this.setChannelName((Object) channelName);
  }

  /**
   * Sets the name using object conversion and validation.
   *
   * <p>This protected method performs the actual name setting with type conversion and validation
   * through the key descriptor system.
   *
   * @param name the name object to convert and set
   * @throws DataDictionaryException if the name conversion or validation fails
   */
  protected void setName(Object name) throws DataDictionaryException {
    this.data.put(KEY_NAME, this.kName.convert(name));
  }

  /**
   * Sets the type using object conversion and validation.
   *
   * <p>This protected method performs the actual type setting with enum conversion and validation
   * through the key descriptor system.
   *
   * @param type the type object to convert and set
   * @throws DataDictionaryException if the type conversion or validation fails
   */
  protected void setType(Object type) throws DataDictionaryException {
    this.data.put(KEY_TYPE, this.kType.convert(type));
  }

  /**
   * Sets the direction using object conversion and validation.
   *
   * <p>This protected method performs the actual direction setting with enum conversion and
   * validation through the key descriptor system.
   *
   * @param direction the direction object to convert and set
   * @throws DataDictionaryException if the direction conversion or validation fails
   */
  protected void setDirection(Object direction) throws DataDictionaryException {
    this.data.put(KEY_DIRECTION, this.kDirection.convert(direction));
  }

  /**
   * Sets the channel name using object conversion and validation.
   *
   * <p>This protected method performs the actual channel name setting with string conversion and
   * validation through the key descriptor system.
   *
   * @param channelName the channel name object to convert and set
   * @throws DataDictionaryException if the channel name conversion or validation fails
   */
  protected void setChannelName(Object channelName) throws DataDictionaryException {
    this.data.put(KEY_CHANNEL_NAME, this.kChannelName.convert(channelName));
  }

  /**
   * Loads and initializes the key descriptors for configuration validation.
   *
   * <p>This private method sets up the key descriptors that define the structure and validation
   * rules for the configuration parameters. It creates descriptors for name, type, direction, and
   * channel name with appropriate validation rules.
   *
   * <p>If any descriptor creation fails, a RuntimeException is thrown with the original exception
   * as the cause.
   */
  private void loadKeyDescriptors() {
    try {
      this.kName = new KeyDescriptorString(KEY_NAME, true, false);
      this.keys.add(this.kName);

      this.kType = new KeyDescriptorEnum<DataStreamType>(KEY_TYPE, true, DataStreamType.class);
      this.keys.add(this.kType);

      this.kDirection =
          new KeyDescriptorEnum<DataStreamDirection>(
              KEY_DIRECTION, true, DataStreamDirection.class);
      this.keys.add(this.kDirection);

      this.kChannelName = new KeyDescriptorString(KEY_CHANNEL_NAME, false, false);
      this.keys.add(this.kChannelName);

      this.addAllKeyDescriptor(this.keys);
    } catch (DataDictionaryException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }
}
