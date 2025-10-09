// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.types.configuration;

import enedis.lab.types.DataDictionary;
import java.io.File;

/**
 * Generic interface for handling application configuration objects.
 *
 * <p>This interface defines the basic operations for loading, saving, and identifying a
 * configuration, regardless of its type or format. It extends {@link DataDictionary} to allow
 * structured manipulation of configuration parameters.
 *
 * <ul>
 *   <li>Robust loading and saving with exception handling
 *   <li>Unique identification by name and associated file
 *   <li>Interoperability with data dictionaries
 * </ul>
 *
 * @author Enedis Smarties team
 * @see DataDictionary
 * @see ConfigurationException
 */
public interface Configuration extends DataDictionary {

  /**
   * Loads the configuration.
   *
   * <p>Must throw a {@link ConfigurationException} if reading fails, the format is invalid, or the
   * data is inconsistent.
   *
   * @throws ConfigurationException if loading fails or the format is invalid
   */
  public void load() throws ConfigurationException;

  /**
   * Saves the current configuration.
   *
   * <p>Must throw a {@link ConfigurationException} if writing fails or if there is an access
   * problem.
   *
   * @throws ConfigurationException if saving fails
   */
  public void save() throws ConfigurationException;

  /**
   * Returns the unique name of the configuration.
   *
   * <p>This name is used to identify the configuration within the application or for user display.
   *
   * @return the configuration name (never null)
   */
  public String getConfigName();

  /**
   * Returns the reference to the associated configuration file.
   *
   * <p>May return {@code null} if the configuration is not linked to a physical file.
   *
   * @return the configuration file, or {@code null} if not applicable
   */
  public File getConfigFile();
}
