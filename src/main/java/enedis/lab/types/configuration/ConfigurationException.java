// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.types.configuration;

import enedis.lab.types.DataDictionaryException;

/** Configuration exception */
public class ConfigurationException extends DataDictionaryException {
  private static final long serialVersionUID = 8090072693974075297L;

  /**
   * Create missing parameter exception
   *
   * @param parameter
   * @return configurationException
   */
  public static ConfigurationException createMissingParameterException(String parameter) {
    return new ConfigurationException("Parameter \'" + parameter + "\' is missing");
  }

  /**
   * Create unknown parameter exception
   *
   * @param parameter
   * @return configurationException
   */
  public static ConfigurationException createUnknownParameterException(String parameter) {
    return new ConfigurationException("Parameter \'" + parameter + "\' is unknown");
  }

  /**
   * Create invalid parameter value exception
   *
   * @param parameter
   * @param info
   * @return configurationException
   */
  public static ConfigurationException createInvalidParameterValueException(
      String parameter, String info) {
    return new ConfigurationException(
        "Parameter \'" + parameter + "\' has an invalid value : " + info);
  }

  /**
   * Create invalid paramter type exception
   *
   * @param parameter
   * @param type
   * @return configurationException
   */
  public static ConfigurationException createInvalidParameterTypeException(
      String parameter, Class<?> type) {
    return new ConfigurationException(
        "Parameter \'" + parameter + "\' has an invalid type : " + type.getName());
  }

  /** Default constructor */
  public ConfigurationException() {
    super();
  }

  /**
   * Constructor using message, cause, enable suppression flag and writable stack trace flag
   *
   * @param message
   * @param cause
   * @param enableSuppression
   * @param writableStackTrace
   */
  public ConfigurationException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  /**
   * Constructor using message and cause
   *
   * @param message
   * @param cause
   */
  public ConfigurationException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructor using message
   *
   * @param message
   */
  public ConfigurationException(String message) {
    super(message);
  }

  /**
   * Constructor using cause
   *
   * @param cause
   */
  public ConfigurationException(Throwable cause) {
    super(cause);
  }
}
