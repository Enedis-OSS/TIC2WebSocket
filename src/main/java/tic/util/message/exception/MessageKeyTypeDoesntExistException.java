// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.util.message.exception;

/**
 * Exception indicating that a required message key type does not exist.
 *
 * <p>This exception is thrown when a message references a key type that is missing, undefined, or
 * not recognized during parsing or processing. It is part of the message exception hierarchy used
 * to signal specific errors in communication.
 *
 * <p>Common use cases include:
 *
 * <ul>
 *   <li>Detecting missing key types in message definitions
 *   <li>Reporting undefined or unrecognized key types
 *   <li>Handling validation failures due to absent key types
 *   <li>Chaining underlying exceptions for debugging
 * </ul>
 *
 * @author Enedis Smarties team
 */
public class MessageKeyTypeDoesntExistException extends MessageException {

  private static final long serialVersionUID = -2263755971102386572L;

  /**
   * Creates a new MessageKeyTypeDoesntExistException with no detail message.
   *
   * <p>This constructor is typically used when no specific error information is available.
   */
  public MessageKeyTypeDoesntExistException() {
    super();
  }

  /**
   * Creates a new MessageKeyTypeDoesntExistException with a detail message and underlying cause.
   *
   * <p>This constructor is used when both a descriptive error message and the original cause of the
   * error are available, providing complete context for debugging.
   *
   * @param message the detail message explaining the error
   * @param cause the underlying exception that caused this error
   */
  public MessageKeyTypeDoesntExistException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Creates a new MessageKeyTypeDoesntExistException with a detail message.
   *
   * <p>This constructor is used when a descriptive error message is available but there is no
   * underlying cause to chain.
   *
   * @param message the detail message explaining the error
   */
  public MessageKeyTypeDoesntExistException(String message) {
    super(message);
  }

  /**
   * Creates a new MessageKeyTypeDoesntExistException with an underlying cause.
   *
   * <p>This constructor is used when the original cause is known but no additional descriptive
   * message is needed beyond what the cause provides.
   *
   * @param cause the underlying exception that caused this error
   */
  public MessageKeyTypeDoesntExistException(Throwable cause) {
    super(cause);
  }
}
