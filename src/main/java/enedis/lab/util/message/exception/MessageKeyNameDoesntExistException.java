// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.util.message.exception;

/**
 * Exception indicating that a required message key name does not exist.
 *
 * <p>This exception is thrown when a message references a key name that is missing, undefined,
 * or not recognized during parsing or processing. It is part of the message exception hierarchy
 * used to signal specific errors in communication.
 *
 * <p>Common use cases include:
 * <ul>
 *   <li>Detecting missing key names in message definitions</li>
 *   <li>Reporting undefined or unrecognized keys</li>
 *   <li>Handling validation failures due to absent keys</li>
 *   <li>Chaining underlying exceptions for debugging</li>
 * </ul>
 *
 * @author Enedis Smarties team
 */
public class MessageKeyNameDoesntExistException extends MessageException {

  private static final long serialVersionUID = -2263755971102386572L;

  /**
   * Creates a new MessageKeyNameDoesntExistException with no detail message.
   *
   * <p>This constructor is typically used when no specific error information is available.
   */
  public MessageKeyNameDoesntExistException() {
    super();
  }

  /**
   * Creates a new MessageKeyNameDoesntExistException with a detail message and underlying cause.
   *
   * <p>This constructor is used when both a descriptive error message and the original cause
   * of the error are available, providing complete context for debugging.
   *
   * @param message the detail message explaining the error
   * @param cause the underlying exception that caused this error
   */
  public MessageKeyNameDoesntExistException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Creates a new MessageKeyNameDoesntExistException with a detail message.
   *
   * <p>This constructor is used when a descriptive error message is available but there is no
   * underlying cause to chain.
   *
   * @param message the detail message explaining the error
   */
  public MessageKeyNameDoesntExistException(String message) {
    super(message);
  }

  /**
   * Creates a new MessageKeyNameDoesntExistException with an underlying cause.
   *
   * <p>This constructor is used when the original cause is known but no additional descriptive
   * message is needed beyond what the cause provides.
   *
   * @param cause the underlying exception that caused this error
   */
  public MessageKeyNameDoesntExistException(Throwable cause) {
    super(cause);
  }
}
