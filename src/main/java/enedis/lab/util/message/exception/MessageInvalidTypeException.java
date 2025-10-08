// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.util.message.exception;

/**
 * Exception indicating that a message has an invalid or unsupported type.
 *
 * <p>This exception is thrown when a message specifies a type that is not recognized, not supported,
 * or does not match the expected types during parsing or processing. It is part of the message exception
 * hierarchy used to signal specific errors in communication.
 *
 * <p>Common use cases include:
 * <ul>
 *   <li>Detecting unknown or unsupported message types</li>
 *   <li>Reporting type mismatches in message definitions</li>
 *   <li>Handling validation failures due to type errors</li>
 *   <li>Chaining underlying exceptions for debugging</li>
 * </ul>
 *
 * @author Enedis Smarties team
 */
public class MessageInvalidTypeException extends MessageException {

  private static final long serialVersionUID = -2263755971102386572L;

  /**
   * Creates a new MessageInvalidTypeException with no detail message.
   *
   * <p>This constructor is typically used when no specific error information is available.
   */
  public MessageInvalidTypeException() {
    super();
  }

  /**
   * Creates a new MessageInvalidTypeException with a detail message and underlying cause.
   *
   * <p>This constructor is used when both a descriptive error message and the original cause
   * of the error are available, providing complete context for debugging.
   *
   * @param message the detail message explaining the error
   * @param cause the underlying exception that caused this error
   */
  public MessageInvalidTypeException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Creates a new MessageInvalidTypeException with a detail message.
   *
   * <p>This constructor is used when a descriptive error message is available but there is no
   * underlying cause to chain.
   *
   * @param message the detail message explaining the error
   */
  public MessageInvalidTypeException(String message) {
    super(message);
  }

  /**
   * Creates a new MessageInvalidTypeException with an underlying cause.
   *
   * <p>This constructor is used when the original cause is known but no additional descriptive
   * message is needed beyond what the cause provides.
   *
   * @param cause the underlying exception that caused this error
   */
  public MessageInvalidTypeException(Throwable cause) {
    super(cause);
  }
}
