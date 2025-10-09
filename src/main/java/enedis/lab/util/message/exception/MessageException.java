// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.util.message.exception;

/**
 * Exception for signaling errors during message processing in the TIC2WebSocket framework.
 *
 * <p>This exception serves as the base class for all message-related errors encountered while parsing,
 * validating, or handling messages.
 * It enables robust error reporting and propagation throughout the message handling pipeline.
 *
 * <p>Common use cases include:
 * <ul>
 *   <li>Reporting invalid message formats</li>
 *   <li>Handling missing or unsupported message types</li>
 *   <li>Signaling errors in message content or structure</li>
 *   <li>Chaining underlying exceptions for debugging</li>
 * </ul>
 *
 * @author Enedis Smarties team
 */
public class MessageException extends Exception {

  private static final long serialVersionUID = -2263755971102386572L;

  /**
   * Creates a new MessageException with no detail message.
   *
   * <p>This constructor is typically used when no specific error information is available.
   */
  public MessageException() {
    super();
  }

  /**
   * Creates a new MessageException with a detail message and underlying cause.
   *
   * <p>This constructor is used when both a descriptive error message and the original cause
   * of the error are available, providing complete context for debugging.
   *
   * @param message the detail message explaining the error
   * @param cause the underlying exception that caused this error
   */
  public MessageException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Creates a new MessageException with a detail message.
   *
   * <p>This constructor is used when a descriptive error message is available but there is no
   * underlying cause to chain.
   *
   * @param message the detail message explaining the error
   */
  public MessageException(String message) {
    super(message);
  }

  /**
   * Creates a new MessageException with an underlying cause.
   *
   * <p>This constructor is used when the original cause is known but no additional descriptive
   * message is needed beyond what the cause provides.
   *
   * @param cause the underlying exception that caused this error
   */
  public MessageException(Throwable cause) {
    super(cause);
  }
}
