// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.types.configuration;

import enedis.lab.types.DataDictionary;
import java.io.File;

/** Interface used to handle any configuration */
public interface Configuration extends DataDictionary {

  /**
   * Load configuration from file
   *
   * @throws ConfigurationException load file failed
   */
  public void load() throws ConfigurationException;

  /**
   * Save configuration to file
   *
   * @throws ConfigurationException save file failed
   */
  public void save() throws ConfigurationException;

  /**
   * Get config name
   *
   * @return configuration name
   */
  public String getConfigName();

  /**
   * Get config file
   *
   * @return configuration file reference
   */
  public File getConfigFile();
}
