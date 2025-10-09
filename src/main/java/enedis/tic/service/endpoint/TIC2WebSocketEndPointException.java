// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.tic.service.endpoint;

/**
 * Exception class for TIC2WebSocket endpoint errors.
 *
 * <p>This exception encapsulates an error code from {@link TIC2WebSocketEndPointErrorCode},
 * providing detailed context for endpoint operation failures. It supports multiple constructors for
 * message and cause chaining.
 *
 * <p>Key features include:
 *
 * <ul>
 *   <li>Associates an error code with each exception
 *   <li>Supports message and cause chaining for diagnostics
 * </ul>
 *
 * @author Enedis Smarties team
 * @see TIC2WebSocketEndPointErrorCode
 */
public class TIC2WebSocketEndPointException extends Exception {

  /** Serial version UID for serialization. */
  private static final long serialVersionUID = -2263755971102386572L;

  /** Error code associated with this exception. */
  private final TIC2WebSocketEndPointErrorCode code;

  /**
   * Constructs an exception with the specified error code.
   *
   * @param code the error code
   */
  public TIC2WebSocketEndPointException(TIC2WebSocketEndPointErrorCode code) {
    super();
    this.code = code;
  }

  /**
   * Constructs an exception with the specified error code, message, and cause.
   *
   * @param code the error code
   * @param message the detail message
   * @param cause the cause of the exception
   */
  public TIC2WebSocketEndPointException(
      TIC2WebSocketEndPointErrorCode code, String message, Throwable cause) {
    super(message, cause);
    this.code = code;
  }

  /**
   * Constructs an exception with the specified error code and message.
   *
   * @param code the error code
   * @param message the detail message
   */
  public TIC2WebSocketEndPointException(TIC2WebSocketEndPointErrorCode code, String message) {
    super(message);
    this.code = code;
  }

  /**
   * Constructs an exception with the specified error code and cause.
   *
   * @param code the error code
   * @param cause the cause of the exception
   */
  public TIC2WebSocketEndPointException(TIC2WebSocketEndPointErrorCode code, Throwable cause) {
    super(cause);
    this.code = code;
  }

  /**
   * Returns the error code associated with this exception.
   *
   * @return the endpoint error code
   */
  public TIC2WebSocketEndPointErrorCode getCode() {
    return this.code;
  }
}
