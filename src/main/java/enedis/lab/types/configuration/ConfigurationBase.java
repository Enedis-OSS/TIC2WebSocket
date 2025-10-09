// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.types.configuration;

import enedis.lab.types.DataDictionary;
import enedis.lab.types.DataDictionaryException;
import enedis.lab.types.datadictionary.DataDictionaryBase;
import java.io.File;
import java.util.Map;

/**
 * Base implementation of the {@link Configuration} interface for application configuration
 * management.
 *
 * <p>This class extends {@link DataDictionaryBase} and provides default mechanisms for loading,
 * saving, and identifying configuration objects.
 *
 * <p>Key features include:
 *
 * <ul>
 *   <li>File-based persistence with structured exception reporting
 *   <li>Support for cloning and copying configuration data
 *   <li>Consistent identification by name and file reference
 * </ul>
 *
 * <p>Intended to be subclassed for specific configuration types (e.g., channel, datastream).
 *
 * @author Enedis Smarties team
 * @see Configuration
 * @see DataDictionaryBase
 * @see ConfigurationException
 */
public class ConfigurationBase extends DataDictionaryBase implements Configuration {

  /** The unique name identifying this configuration. */
  private String name = null;

  /** The file associated with this configuration, or null if not file-based. */
  private File file = null;

  /**
   * Protected default constructor for subclassing.
   *
   * <p>Initializes an empty configuration with no name or file.
   */
  protected ConfigurationBase() {
    super();
  }

  /**
   * Constructs a configuration by copying from another {@link DataDictionary}.
   *
   * @param other the data dictionary to copy from
   * @throws DataDictionaryException if copying fails
   */
  public ConfigurationBase(DataDictionary other) throws DataDictionaryException {
    super(other);
  }

  /**
   * Constructs a configuration from a map of key-value pairs.
   *
   * @param map the map containing configuration data
   * @throws DataDictionaryException if mapping fails
   */
  public ConfigurationBase(Map<String, Object> map) throws DataDictionaryException {
    super(map);
  }

  /**
   * Constructs a configuration with a specific name and file reference.
   *
   * @param name the configuration name
   * @param file the configuration file
   */
  public ConfigurationBase(String name, File file) {
    super();
    this.init(name, file);
  }

  /**
   * Loads the configuration from the associated file.
   *
   * <p>Throws a {@link ConfigurationException} if loading or copying fails.
   *
   * @throws ConfigurationException if the file cannot be read or parsed
   */
  @Override
  public void load() throws ConfigurationException {
    DataDictionary configuration;
    try {
      configuration = DataDictionaryBase.fromFile(this.file, this.getClass());
    } catch (Exception exception) {
      throw new ConfigurationException(
          "Cannot load configuration '"
              + ((this.name == null) ? "" : this.name)
              + "' from file '"
              + ((this.file == null) ? "" : this.file)
              + "' ("
              + exception.getMessage()
              + ") "
              + exception.getClass().getSimpleName());
    }
    try {
      this.copy(configuration);
    } catch (Exception exception) {
      throw new ConfigurationException(
          "Cannot copy configuration '"
              + ((this.name == null) ? "" : this.name)
              + "' from file '"
              + ((this.file == null) ? "" : this.file)
              + "' ("
              + exception.getMessage()
              + ")");
    }
  }

  /**
   * Saves the configuration to the associated file.
   *
   * <p>Throws a {@link ConfigurationException} if writing fails.
   *
   * @throws ConfigurationException if the file cannot be written
   */
  @Override
  public void save() throws ConfigurationException {
    try {
      this.toFile(this.file, 2);
    } catch (Exception exception) {
      throw new ConfigurationException(
          "Cannot save configuration '"
              + ((this.name == null) ? "" : this.name)
              + "' to file '"
              + ((this.file == null) ? "" : this.file)
              + "' ("
              + exception.getMessage()
              + ")");
    }
  }

  /**
   * Returns the unique name of this configuration.
   *
   * @return the configuration name, or null if not set
   */
  @Override
  public String getConfigName() {
    return this.name;
  }

  /**
   * Returns the file associated with this configuration.
   *
   * @return the configuration file, or null if not file-based
   */
  @Override
  public File getConfigFile() {
    return this.file;
  }

  /**
   * Sets the configuration name.
   *
   * @param configName the new configuration name
   */
  public void setConfigName(String configName) {
    this.name = configName;
  }

  /**
   * Initializes the configuration with a name and file reference.
   *
   * @param name the configuration name
   * @param file the configuration file
   */
  protected void init(String name, File file) {
    this.name = name;
    this.file = file;
  }
}
