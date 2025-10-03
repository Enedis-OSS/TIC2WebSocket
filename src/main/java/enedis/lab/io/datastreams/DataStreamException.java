// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.io.datastreams;

/**
 * Exception class for data stream related errors.
 *
 * <p>This exception is thrown when errors occur during data stream operations such as
 * configuration, initialization, opening, closing, reading, or writing. It provides static factory
 * methods for creating exceptions with standardized error messages for common error scenarios.
 *
 * <p>The class includes utility methods to raise exceptions for specific error types: channel
 * errors, configuration errors, operation denials, and creation failures.
 *
 * @author Enedis Smarties team
 * @see DataStream
 * @see DataStreamConfiguration
 */
public class DataStreamException extends Exception {
  private static final long serialVersionUID = 4079445021346677107L;

  /**
   * Raises a channel error exception.
   *
   * <p>This method creates and throws an exception when an error occurs on a specific channel
   * during stream operations.
   *
   * @param channelName the name of the channel where the error occurred
   * @param info additional information about the error
   * @throws DataStreamException always thrown with a formatted error message
   */
  public static void raiseChannelError(String channelName, String info) throws DataStreamException {
    throw new DataStreamException("Error occurs on channel " + channelName + " (" + info + ")");
  }

  /**
   * Raises an unhandled type exception.
   *
   * <p>This method creates and throws an exception when a data stream type is not supported or
   * handled by the current implementation.
   *
   * @param type the type of data stream that is not handled
   * @throws DataStreamException always thrown with a formatted error message
   */
  public static void raiseUnhandledType(String type) throws DataStreamException {
    throw new DataStreamException(type + " datastream is not handled");
  }

  /**
   * Raises an internal error exception.
   *
   * <p>This method creates and throws an exception for internal errors that occur during stream
   * processing or operations.
   *
   * @param info information describing the internal error
   * @throws DataStreamException always thrown with the provided error information
   */
  public static void raiseInternalError(String info) throws DataStreamException {
    throw new DataStreamException(info);
  }

  /**
   * Raises an invalid configuration exception with type validation.
   *
   * <p>This method creates and throws an exception when the provided configuration is not of the
   * expected type or is incompatible with the stream requirements.
   *
   * @param configuration the invalid configuration object
   * @param expected_configuration_name the name of the expected configuration type
   * @throws DataStreamException always thrown with details about the configuration mismatch
   */
  public static void raiseInvalidConfiguration(
      DataStreamConfiguration configuration, String expected_configuration_name)
      throws DataStreamException {
    throw new DataStreamException(
        "Configuration "
            + configuration.getClass().getSimpleName()
            + " is not valid ("
            + expected_configuration_name
            + " expected)");
  }

  /**
   * Raises an invalid configuration exception with custom message.
   *
   * <p>This method creates and throws an exception when a configuration is invalid, with a custom
   * error message describing the specific validation failure.
   *
   * @param info information describing why the configuration is invalid
   * @throws DataStreamException always thrown with the provided error information
   */
  public static void raiseInvalidConfiguration(String info) throws DataStreamException {
    throw new DataStreamException(info);
  }

  /**
   * Raises an operation denied exception.
   *
   * <p>This method creates and throws an exception when an operation is not allowed on the stream,
   * such as attempting to write to an input-only stream.
   *
   * @param operation the name of the operation that was denied
   * @throws DataStreamException always thrown with a formatted error message
   */
  public static void raiseOperationDenied(String operation) throws DataStreamException {
    throw new DataStreamException("Operation \'" + operation + "\' is not allowed");
  }

  /**
   * Raises an unexpected error exception.
   *
   * <p>This method creates and throws an exception when an unexpected error occurs during stream
   * operations that doesn't fit into other specific error categories.
   *
   * @param info information describing the unexpected error
   * @throws DataStreamException always thrown with a formatted error message
   */
  public static void raiseUnexpectedError(String info) throws DataStreamException {
    throw new DataStreamException("Unexpected error occurs : " + info);
  }

  /**
   * Raises an already exists exception.
   *
   * <p>This method creates and throws an exception when attempting to create a data stream that
   * already exists in the system.
   *
   * @param dataStream the name of the data stream that already exists
   * @throws DataStreamException always thrown with a formatted error message
   */
  public static void raiseAlreadyExists(String dataStream) throws DataStreamException {
    throw new DataStreamException("DataStream " + dataStream + " already exists");
  }

  /**
   * Raises a creation failed exception.
   *
   * <p>This method creates and throws an exception when the creation of a data stream fails for any
   * reason.
   *
   * @param dataStream the name of the data stream that failed to be created
   * @param info additional information about why the creation failed
   * @throws DataStreamException always thrown with a formatted error message
   */
  public static void raiseCreationFailed(String dataStream, String info)
      throws DataStreamException {
    throw new DataStreamException(
        "Creation of dataStream " + dataStream + " creation failed : " + info);
  }

  /**
   * Constructs a new DataStreamException with a message and a cause.
   *
   * @param message the detail message describing the exception
   * @param cause the cause of this exception
   */
  public DataStreamException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructs a new DataStreamException with a detail message.
   *
   * @param message the detail message describing the exception
   */
  public DataStreamException(String message) {
    super(message);
  }

  /**
   * Constructs a new DataStreamException with a cause.
   *
   * @param cause the cause of this exception
   */
  public DataStreamException(Throwable cause) {
    super(cause);
  }
}
