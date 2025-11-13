// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.util.message.exception;

/**
 * Exception indicating that a message contains invalid or unexpected content.
 *
 * <p>This exception is thrown when the content of a message does not conform to the expected
 * structure, contains invalid values, or fails validation during parsing or processing. It is part
 * of the message exception hierarchy used to signal specific errors in communication.
 *
 * <p>Common use cases include:
 *
 * <ul>
 *   <li>Detecting missing or malformed fields in a message
 *   <li>Reporting invalid data values or types
 *   <li>Handling content validation failures
 *   <li>Chaining underlying exceptions for debugging
 * </ul>
 *
 * @author Enedis Smarties team
 */
public class MessageInvalidContentException extends MessageException {

  private static final long serialVersionUID = -2263755971102386572L;

  /**
   * Creates a new MessageInvalidContentException with no detail message.
   *
   * <p>This constructor is typically used when no specific error information is available.
   */
  public MessageInvalidContentException() {
    super();
  }

  /**
   * Creates a new MessageInvalidContentException with a detail message and underlying cause.
   *
   * <p>This constructor is used when both a descriptive error message and the original cause of the
   * error are available, providing complete context for debugging.
   *
   * @param message the detail message explaining the error
   * @param cause the underlying exception that caused this error
   */
  public MessageInvalidContentException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Creates a new MessageInvalidContentException with a detail message.
   *
   * <p>This constructor is used when a descriptive error message is available but there is no
   * underlying cause to chain.
   *
   * @param message the detail message explaining the error
   */
  public MessageInvalidContentException(String message) {
    super(message);
  }

  /**
   * Creates a new MessageInvalidContentException with an underlying cause.
   *
   * <p>This constructor is used when the original cause is known but no additional descriptive
   * message is needed beyond what the cause provides.
   *
   * @param cause the underlying exception that caused this error
   */
  public MessageInvalidContentException(Throwable cause) {
    super(cause);
  }
}
