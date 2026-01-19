// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.util.message.exception;

/**
 * Exception indicating that a message type or operation is unsupported.
 *
 * <p>This exception is thrown when a message specifies a type, operation, or feature that is not
 * supported by the current implementation. It is part of the message exception hierarchy used to
 * signal specific errors in communication.
 *
 * <p>Common use cases include:
 *
 * <ul>
 *   <li>Detecting unsupported message types or operations
 *   <li>Reporting attempts to use unimplemented features
 *   <li>Handling validation failures due to unsupported requests
 *   <li>Chaining underlying exceptions for debugging
 * </ul>
 *
 * @author Enedis Smarties team
 */
public class UnsupportedMessageException extends MessageException {

  private static final long serialVersionUID = -2263755971102386572L;

  /**
   * Creates a new UnsupportedMessageException with no detail message.
   *
   * <p>This constructor is typically used when no specific error information is available.
   */
  public UnsupportedMessageException() {
    super();
  }

  /**
   * Creates a new UnsupportedMessageException with a detail message and underlying cause.
   *
   * <p>This constructor is used when both a descriptive error message and the original cause of the
   * error are available, providing complete context for debugging.
   *
   * @param message the detail message explaining the error
   * @param cause the underlying exception that caused this error
   */
  public UnsupportedMessageException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Creates a new UnsupportedMessageException with a detail message.
   *
   * <p>This constructor is used when a descriptive error message is available but there is no
   * underlying cause to chain.
   *
   * @param message the detail message explaining the error
   */
  public UnsupportedMessageException(String message) {
    super(message);
  }

  /**
   * Creates a new UnsupportedMessageException with an underlying cause.
   *
   * <p>This constructor is used when the original cause is known but no additional descriptive
   * message is needed beyond what the cause provides.
   *
   * @param cause the underlying exception that caused this error
   */
  public UnsupportedMessageException(Throwable cause) {
    super(cause);
  }
}
