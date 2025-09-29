// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.io.channels;

import enedis.lab.types.ExceptionBase;

/**
 * Exception class for channel-related errors.
 * <p>
 * This exception is thrown when errors occur during channel operations such as
 * setup, configuration, read/write operations, or channel management. It
 * extends
 * {@link ExceptionBase} to provide structured error handling with error codes
 * and detailed error messages.
 * <p>
 * The class provides static factory methods for creating specific types of
 * channel exceptions, each with appropriate error codes and descriptive
 * messages.
 * <p>
 * Common scenarios that trigger this exception include:
 * <ul>
 * <li>Invalid channel configuration</li>
 * <li>Channel not ready for operations</li>
 * <li>Conflicting channel types</li>
 * <li>Operation denied due to channel state</li>
 * <li>Internal channel errors</li>
 * </ul>
 *
 * @author Enedis Smarties team
 * @see ExceptionBase
 * @see Channel
 * @see ChannelConfiguration
 */
public class ChannelException extends ExceptionBase {

  private static final long serialVersionUID = -3507876436535833098L;

  /** Error code for invalid or unsupported channel types. */
  public static final int ERRCODE_INVALID_CHANNEL = -5;

  /** Error code for attempting to create a channel that already exists. */
  public static final int ERRCODE_ALREADY_EXISTS = -6;

  /** Error code for internal channel processing errors. */
  public static final int ERRCODE_INTERNAL_ERROR = -7;

  /** Error code for operations attempted on channels that are not ready. */
  public static final int ERRCODE_CHANNEL_NOT_READY = -8;

  /** Error code for invalid or incompatible configuration types. */
  public static final int ERRCODE_INVALID_CONFIGURATION_TYPE = -9;

  /** Error code for invalid configuration parameters or values. */
  public static final int ERRCODE_INVALID_CONFIGURATION = -10;

  /**
   * Error code for operations that are not allowed in the current channel state.
   */
  public static final int ERRCODE_OPERATION_DENIED = -11;

  /** Error code for operations attempted on non-existent channels. */
  public static final int ERRCODE_CHANNEL_DOESNT_EXIST = -12;

  /** Error code for unexpected or unhandled errors. */
  public static final int ERRCODE_UNEXPECTED = -99;

  /**
   * Creates and throws a ChannelException for unhandled channel types.
   * <p>
   * This method is used when an attempt is made to create or use a channel
   * type that is not supported by the system.
   *
   * @param channelType the unsupported channel type that was requested
   * @throws ChannelException always thrown with error code
   *                          {@link #ERRCODE_INVALID_CHANNEL}
   */
  public static void raiseUnhandledChannelType(String channelType) throws ChannelException {
    throw new ChannelException(ERRCODE_INVALID_CHANNEL, channelType + " channel is not handled");
  }

  /**
   * Creates and throws a ChannelException for conflicting channel types.
   * <p>
   * This method is used when a channel type conflicts with an expected type,
   * typically during channel configuration or type validation.
   *
   * @param channelType         the actual channel type that was provided
   * @param expectedChannelType the expected channel type
   * @throws ChannelException always thrown with error code
   *                          {@link #ERRCODE_INVALID_CHANNEL}
   */
  public static void raiseConflictingChannelType(String channelType, String expectedChannelType)
      throws ChannelException {
    throw new ChannelException(
        ERRCODE_INVALID_CHANNEL,
        channelType + " channel is conflicting with " + expectedChannelType);
  }

  /**
   * Creates and throws a ChannelException for internal errors.
   * <p>
   * This method is used when an internal error occurs during channel
   * processing that cannot be attributed to user input or configuration.
   *
   * @param info additional information about the internal error
   * @throws ChannelException always thrown with error code
   *                          {@link #ERRCODE_INTERNAL_ERROR}
   */
  public static void raiseInternalError(String info) throws ChannelException {
    throw new ChannelException(ERRCODE_INTERNAL_ERROR, info);
  }

  /**
   * Creates and throws a ChannelException for channels that are not ready.
   * <p>
   * This method is used when an operation is attempted on a channel that
   * is not in a ready state (e.g., not properly configured or initialized).
   *
   * @param info additional information about why the channel is not ready
   * @throws ChannelException always thrown with error code
   *                          {@link #ERRCODE_CHANNEL_NOT_READY}
   */
  public static void raiseChannelNotReady(String info) throws ChannelException {
    throw new ChannelException(ERRCODE_CHANNEL_NOT_READY, info);
  }

  /**
   * Creates and throws a ChannelException for invalid configuration types.
   * <p>
   * This method is used when a channel configuration is of the wrong type
   * for the expected channel implementation.
   *
   * @param configuration               the invalid configuration that was
   *                                    provided
   * @param expected_configuration_name the name of the expected configuration
   *                                    type
   * @throws ChannelException always thrown with error code
   *                          {@link #ERRCODE_INVALID_CONFIGURATION_TYPE}
   */
  public static void raiseInvalidConfigurationType(
      ChannelConfiguration configuration, String expected_configuration_name)
      throws ChannelException {
    throw new ChannelException(
        ERRCODE_INVALID_CONFIGURATION_TYPE,
        "Configuration "
            + configuration.getClass().getSimpleName()
            + " is not a valid type ("
            + expected_configuration_name
            + " expected)");
  }

  /**
   * Creates and throws a ChannelException for invalid configuration parameters.
   * <p>
   * This method is used when channel configuration parameters are invalid,
   * missing, or incompatible.
   *
   * @param info additional information about the configuration error
   * @throws ChannelException always thrown with error code
   *                          {@link #ERRCODE_INVALID_CONFIGURATION}
   */
  public static void raiseInvalidConfiguration(String info) throws ChannelException {
    throw new ChannelException(
        ERRCODE_INVALID_CONFIGURATION, "Configuration is invalid (" + info + ")");
  }

  /**
   * Creates and throws a ChannelException for denied operations.
   * <p>
   * This method is used when an operation is attempted that is not allowed
   * in the current channel state or configuration.
   *
   * @param operation the operation that was denied
   * @throws ChannelException always thrown with error code
   *                          {@link #ERRCODE_OPERATION_DENIED}
   */
  public static void raiseOperationDenied(String operation) throws ChannelException {
    throw new ChannelException(
        ERRCODE_OPERATION_DENIED, "Operation \'" + operation + "\' is not allowed");
  }

  /**
   * Creates and throws a ChannelException for unexpected errors.
   * <p>
   * This method is used when an unexpected or unhandled error occurs
   * during channel operations.
   *
   * @param info additional information about the unexpected error
   * @throws ChannelException always thrown with error code
   *                          {@link #ERRCODE_UNEXPECTED}
   */
  public static void raiseUnexpectedError(String info) throws ChannelException {
    throw new ChannelException(ERRCODE_UNEXPECTED, "Unexpected error occurs : " + info);
  }

  /**
   * Creates and throws a ChannelException for duplicate channel names.
   * <p>
   * This method is used when attempting to create a channel with a name
   * that already exists in the system.
   *
   * @param channelName the name of the channel that already exists
   * @throws ChannelException always thrown with error code
   *                          {@link #ERRCODE_ALREADY_EXISTS}
   */
  public static void raiseAlreadyExists(String channelName) throws ChannelException {
    throw new ChannelException(
        ERRCODE_ALREADY_EXISTS, "Channel " + channelName + " already exists");
  }

  /**
   * Creates and throws a ChannelException for non-existent channels.
   * <p>
   * This method is used when attempting to perform operations on a channel
   * that does not exist in the system.
   *
   * @param channelName the name of the channel that does not exist
   * @throws ChannelException always thrown with error code
   *                          {@link #ERRCODE_CHANNEL_DOESNT_EXIST}
   */
  public static void raiseDoesntExist(String channelName) throws ChannelException {
    throw new ChannelException(
        ERRCODE_CHANNEL_DOESNT_EXIST, "Channel " + channelName + " doesn't exist");
  }

  /**
   * Constructs a new ChannelException with the specified error code and message.
   * <p>
   * This constructor creates a channel exception with a specific error code
   * and descriptive message. The error code should be one of the predefined
   * constants in this class.
   *
   * @param code the error code for this exception
   * @param info the detailed error message
   */
  public ChannelException(int code, String info) {
    super(code, info);
  }
}
