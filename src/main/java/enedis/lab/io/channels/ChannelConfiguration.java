// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.io.channels;

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
 * Configuration class for communication channels.
 *
 * <p>
 * This class extends {@link ConfigurationBase} and provides the base
 * configuration parameters
 * required for all types of communication channels. It defines the essential
 * properties that every
 * channel must have: name, protocol, direction, and alias.
 *
 * <p>
 * The configuration supports validation through key descriptors and provides
 * methods for setting
 * and retrieving channel parameters. It can be instantiated from various
 * sources including maps,
 * data dictionaries, and direct parameter values.
 *
 * <p>
 * This class serves as the foundation for more specific channel configurations.
 *
 * @author Enedis Smarties team
 * @see ConfigurationBase
 * @see ChannelProtocol
 * @see ChannelDirection
 */
public class ChannelConfiguration extends ConfigurationBase {

  /** Key for the channel name configuration parameter. */
  protected static final String KEY_NAME = "name";

  /** Key for the channel protocol configuration parameter. */
  protected static final String KEY_PROTOCOL = "protocol";

  /** Key for the channel direction configuration parameter. */
  protected static final String KEY_DIRECTION = "direction";

  /** Key for the channel alias configuration parameter. */
  protected static final String KEY_ALIAS = "alias";

  /** List of key descriptors for configuration validation. */
  private List<KeyDescriptor<?>> keys = new ArrayList<KeyDescriptor<?>>();

  /** Key descriptor for channel name validation. */
  protected KeyDescriptorString kName;

  /** Key descriptor for channel protocol validation. */
  protected KeyDescriptorEnum<ChannelProtocol> kProtocol;

  /** Key descriptor for channel direction validation. */
  protected KeyDescriptorEnum<ChannelDirection> kDirection;

  /** Key descriptor for channel alias validation. */
  protected KeyDescriptorString kAlias;

  /**
   * Default constructor for channel configuration.
   *
   * <p>
   * Initializes the configuration with default values and sets up the key
   * descriptors for
   * parameter validation.
   */
  protected ChannelConfiguration() {
    super();
    this.loadKeyDescriptors();
  }

  /**
   * Constructs a new channel configuration from a map of key-value pairs.
   *
   * <p>
   * This constructor initializes the configuration with values from the provided
   * map, performing
   * validation and setting default values for missing parameters.
   *
   * @param map the map containing configuration parameters
   * @throws DataDictionaryException if the map contains invalid values or
   *                                 required parameters are
   *                                 missing
   */
  public ChannelConfiguration(Map<String, Object> map) throws DataDictionaryException {
    this();
    this.copy(fromMap(map));
  }

  /**
   * Constructs a new channel configuration by copying from another data
   * dictionary.
   *
   * <p>
   * This constructor creates a new configuration instance by copying all
   * parameters from the
   * provided data dictionary, ensuring consistency between configurations.
   *
   * @param other the data dictionary to copy configuration from
   * @throws DataDictionaryException if the source data dictionary contains
   *                                 invalid values or is
   *                                 incompatible with channel configuration
   */
  public ChannelConfiguration(DataDictionary other) throws DataDictionaryException {
    this();
    this.copy(other);
  }

  /**
   * Constructs a new channel configuration with a name and file reference,
   * initializing all
   * parameters to their default values.
   *
   * <p>
   * This constructor is typically used when creating a new configuration that
   * will be populated
   * later with specific channel parameters.
   *
   * @param name the configuration name
   * @param file the configuration file associated with this configuration
   */
  public ChannelConfiguration(String name, File file) {
    this();
    this.init(name, file);
  }

  /**
   * Constructs a new channel configuration with all parameters set to specific
   * values.
   *
   * <p>
   * This constructor provides a convenient way to create a fully configured
   * channel with all
   * necessary parameters specified at construction time.
   *
   * @param name      the channel name
   * @param protocol  the communication protocol to use
   * @param direction the communication direction (RX, TX, or RXTX)
   * @param alias     the channel alias (optional)
   * @throws DataDictionaryException if any of the provided values are invalid or
   *                                 incompatible with
   *                                 channel configuration requirements
   */
  public ChannelConfiguration(
      String name, ChannelProtocol protocol, ChannelDirection direction, String alias)
      throws DataDictionaryException {
    this();

    this.setName(name);
    this.setProtocol(protocol);
    this.setDirection(direction);
    this.setAlias(alias);

    this.checkAndUpdate();
  }

  /**
   * Updates optional parameters with default values if not already set.
   *
   * <p>
   * This method is called during configuration validation to ensure that all
   * required parameters
   * are properly set. For the base channel configuration, this method delegates
   * to the parent class
   * implementation.
   *
   * @throws DataDictionaryException if there are validation errors with the
   *                                 configuration
   */
  @Override
  protected void updateOptionalParameters() throws DataDictionaryException {
    super.updateOptionalParameters();
  }

  /**
   * Retrieves the channel name.
   *
   * <p>
   * The channel name is used for identification and logging purposes. It is
   * typically set during
   * channel creation or configuration.
   *
   * @return the channel name, or null if not set
   */
  public String getName() {
    return (String) this.data.get(KEY_NAME);
  }

  /**
   * Retrieves the communication protocol used by this channel.
   *
   * <p>
   * The protocol determines the underlying communication mechanism, such as
   * serial port, TCP,
   * UDP, or other supported protocols.
   *
   * @return the channel protocol, or null if not set
   */
  public ChannelProtocol getProtocol() {
    return (ChannelProtocol) this.data.get(KEY_PROTOCOL);
  }

  /**
   * Retrieves the communication direction of this channel.
   *
   * <p>
   * The direction indicates whether the channel can receive data (RX), transmit
   * data (TX), or
   * both (RXTX). This affects which operations are available.
   *
   * @return the channel direction, or null if not set
   */
  public ChannelDirection getDirection() {
    return (ChannelDirection) this.data.get(KEY_DIRECTION);
  }

  /**
   * Retrieves the channel alias.
   *
   * <p>
   * The alias provides an alternative identifier for the channel, typically used
   * for
   * user-friendly display or alternative referencing.
   *
   * @return the channel alias, or null if not set
   */
  public String getAlias() {
    return (String) this.data.get(KEY_ALIAS);
  }

  /**
   * Sets the channel name.
   *
   * <p>
   * The channel name is used for identification and logging purposes. It must be
   * a non-empty
   * string and is required for a valid configuration.
   *
   * @param name the channel name to set
   * @throws DataDictionaryException if the name is invalid, empty, or null
   */
  public void setName(String name) throws DataDictionaryException {
    this.setName((Object) name);
  }

  /**
   * Sets the communication protocol for this channel.
   *
   * <p>
   * The protocol determines the underlying communication mechanism. Must be one
   * of the supported
   * {@link ChannelProtocol} values.
   *
   * @param protocol the communication protocol to use
   * @throws DataDictionaryException if the protocol is invalid or not supported
   */
  public void setProtocol(ChannelProtocol protocol) throws DataDictionaryException {
    this.setProtocol((Object) protocol);
  }

  /**
   * Sets the communication direction for this channel.
   *
   * <p>
   * The direction determines whether the channel can receive data (RX), transmit
   * data (TX), or
   * both (RXTX). Must be one of the supported {@link ChannelDirection} values.
   *
   * @param direction the communication direction to use
   * @throws DataDictionaryException if the direction is invalid or not supported
   */
  public void setDirection(ChannelDirection direction) throws DataDictionaryException {
    this.setDirection((Object) direction);
  }

  /**
   * Sets the channel alias.
   *
   * <p>
   * The alias provides an alternative identifier for the channel, typically used
   * for
   * user-friendly display or alternative referencing. This parameter is optional
   * and can be null.
   *
   * @param alias the channel alias to set (can be null)
   * @throws DataDictionaryException if the alias format is invalid
   */
  public void setAlias(String alias) throws DataDictionaryException {
    this.setAlias((Object) alias);
  }

  ///
  // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  ///
  /// PROTECTED METHODS
  ///
  ///
  // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Internal method to set the channel name with object conversion.
   *
   * @param name the name object to convert and set
   * @throws DataDictionaryException if the conversion fails
   */
  protected void setName(Object name) throws DataDictionaryException {
    this.data.put(KEY_NAME, this.kName.convert(name));
  }

  /**
   * Internal method to set the channel protocol with object conversion.
   *
   * @param protocol the protocol object to convert and set
   * @throws DataDictionaryException if the conversion fails
   */
  protected void setProtocol(Object protocol) throws DataDictionaryException {
    this.data.put(KEY_PROTOCOL, this.kProtocol.convert(protocol));
  }

  /**
   * Internal method to set the channel direction with object conversion.
   *
   * @param direction the direction object to convert and set
   * @throws DataDictionaryException if the conversion fails
   */
  protected void setDirection(Object direction) throws DataDictionaryException {
    this.data.put(KEY_DIRECTION, this.kDirection.convert(direction));
  }

  /**
   * Internal method to set the channel alias with object conversion.
   *
   * @param alias the alias object to convert and set
   * @throws DataDictionaryException if the conversion fails
   */
  protected void setAlias(Object alias) throws DataDictionaryException {
    this.data.put(KEY_ALIAS, this.kAlias.convert(alias));
  }

  ///
  // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  ///
  /// PRIVATE METHODS
  ///
  ///
  // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Initializes and configures all key descriptors for parameter validation.
   *
   * <p>
   * This method sets up the validation rules for all channel configuration
   * parameters, including
   * required/optional status and type validation.
   *
   * @throws RuntimeException if there is an error during key descriptor
   *                          initialization
   */
  private void loadKeyDescriptors() {
    try {
      this.kName = new KeyDescriptorString(KEY_NAME, true, false);
      this.keys.add(this.kName);

      this.kProtocol = new KeyDescriptorEnum<ChannelProtocol>(KEY_PROTOCOL, true, ChannelProtocol.class);
      this.keys.add(this.kProtocol);

      this.kDirection = new KeyDescriptorEnum<ChannelDirection>(KEY_DIRECTION, true, ChannelDirection.class);
      this.keys.add(this.kDirection);

      this.kAlias = new KeyDescriptorString(KEY_ALIAS, false, false);
      this.keys.add(this.kAlias);

      this.addAllKeyDescriptor(this.keys);
    } catch (DataDictionaryException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }
}
