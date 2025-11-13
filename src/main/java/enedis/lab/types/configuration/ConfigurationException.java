// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.types.configuration;

import enedis.lab.types.DataDictionaryException;

/**
 * Exception type for configuration-related errors.
 *
 * <p>This exception is thrown to indicate problems encountered during configuration operations,
 * such as missing or invalid parameters, file I/O errors, or type mismatches. It extends {@link
 * DataDictionaryException} to provide compatibility with the data dictionary error handling
 * framework.
 *
 * <p>Static factory methods are provided for common error scenarios, and all standard exception
 * constructors are available for flexibility.
 *
 * @author Enedis Smarties team
 * @see DataDictionaryException
 */
public class ConfigurationException extends DataDictionaryException {

  /** Serialization identifier for compatibility. */
  private static final long serialVersionUID = 8090072693974075297L;

  /**
   * Creates an exception indicating that a required configuration parameter is missing.
   *
   * @param parameter the name of the missing parameter
   * @return a new {@code ConfigurationException} describing the error
   */
  public static ConfigurationException createMissingParameterException(String parameter) {
    return new ConfigurationException("Parameter '" + parameter + "' is missing");
  }

  /**
   * Creates an exception indicating that an unknown configuration parameter was encountered.
   *
   * @param parameter the name of the unknown parameter
   * @return a new {@code ConfigurationException} describing the error
   */
  public static ConfigurationException createUnknownParameterException(String parameter) {
    return new ConfigurationException("Parameter '" + parameter + "' is unknown");
  }

  /**
   * Creates an exception indicating that a configuration parameter has an invalid value.
   *
   * @param parameter the name of the parameter
   * @param info additional information about the invalid value
   * @return a new {@code ConfigurationException} describing the error
   */
  public static ConfigurationException createInvalidParameterValueException(
      String parameter, String info) {
    return new ConfigurationException(
        "Parameter '" + parameter + "' has an invalid value : " + info);
  }

  /**
   * Creates an exception indicating that a configuration parameter has an invalid type.
   *
   * @param parameter the name of the parameter
   * @param type the expected type
   * @return a new {@code ConfigurationException} describing the error
   */
  public static ConfigurationException createInvalidParameterTypeException(
      String parameter, Class<?> type) {
    return new ConfigurationException(
        "Parameter '" + parameter + "' has an invalid type : " + type.getName());
  }

  /** Constructs a new configuration exception with no detail message. */
  public ConfigurationException() {
    super();
  }

  /**
   * Constructs a new configuration exception with the specified detail message, cause, suppression
   * enabled or disabled, and writable stack trace enabled or disabled.
   *
   * @param message the detail message
   * @param cause the cause
   * @param enableSuppression whether suppression is enabled or disabled
   * @param writableStackTrace whether the stack trace should be writable
   */
  public ConfigurationException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  /**
   * Constructs a new configuration exception with the specified detail message and cause.
   *
   * @param message the detail message
   * @param cause the cause
   */
  public ConfigurationException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructs a new configuration exception with the specified detail message.
   *
   * @param message the detail message
   */
  public ConfigurationException(String message) {
    super(message);
  }

  /**
   * Constructs a new configuration exception with the specified cause.
   *
   * @param cause the cause
   */
  public ConfigurationException(Throwable cause) {
    super(cause);
  }
}
