// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.tic.service.endpoint;

/**
 * Error codes for TIC2WebSocket endpoint operations.
 *
 * <p>This enumeration defines error codes used to represent the result of endpoint operations,
 * including message validation, subscription management, and internal errors. Each code is
 * associated with a unique integer value for protocol communication.
 *
 * <p>Key features include:
 *
 * <ul>
 *   <li>Success and error codes for message handling
 *   <li>Subscription and unsubscription error codes
 *   <li>Timeout and identifier errors
 * </ul>
 *
 * @author Enedis Smarties team
 */
public enum TIC2WebSocketEndPointErrorCode {
  /** Operation completed successfully. */
  NO_ERROR(0),
  /** Message format is invalid. */
  INVALID_MESSAGE_FORMAT(-1),
  /** Message type is missing. */
  MESSAGE_TYPE_MISSING(-2),
  /** Message name is missing. */
  MESSAGE_NAME_MISSING(-3),
  /** Message type is invalid. */
  MESSAGE_TYPE_INVALID(-4),
  /** Message is not supported. */
  UNSUPPORTED_MESSAGE(-5),
  /** Message content is invalid. */
  INVALID_MESSAGE_CONTENT(-6),
  /** Internal server error occurred. */
  INTERNAL_ERROR(-7),
  /** Subscription request failed. */
  SUBSCRIPTION_FAIL(-10),
  /** Unsubscription request failed. */
  UNSUBSCRIPTION_FAIL(-11),
  /** TIC identifier not found. */
  IDENTIFIER_NOT_FOUND(-12),
  /** TIC read operation timed out. */
  READ_TIMEOUT(-13);

  /** Integer value associated with the error code. */
  private final int value;

  /**
   * Constructs an error code with the specified integer value.
   *
   * @param value the integer value for the error code
   */
  TIC2WebSocketEndPointErrorCode(int value) {
    this.value = value;
  }

  /**
   * Returns the integer value associated with this error code.
   *
   * @return the error code value
   */
  public int value() {
    return this.value;
  }
}
