// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.tic.service.endpoint;

/** End point error codes list */
public enum TIC2WebSocketEndPointErrorCode {
  /** Success code */
  NO_ERROR(0),
  /** Unvalid message */
  INVALID_MESSAGE_FORMAT(-1),
  /** Message type missing */
  MESSAGE_TYPE_MISSING(-2),
  /** Message name missing */
  MESSAGE_NAME_MISSING(-3),
  /** Message type invalid */
  MESSAGE_TYPE_INVALID(-4),
  /** Unsupported message */
  UNSUPPORTED_MESSAGE(-5),
  /** Invalid messge content */
  INVALID_MESSAGE_CONTENT(-6),
  /** Internal error */
  INTERNAL_ERROR(-7),
  /** Subscription fail */
  SUBSCRIPTION_FAIL(-10),
  /** Unsubscription fail */
  UNSUBSCRIPTION_FAIL(-11),
  /** TIC identifier not found */
  IDENTIFIER_NOT_FOUND(-12),
  /** Read TIC timeout */
  READ_TIMEOUT(-13);

  private int value;

  private TIC2WebSocketEndPointErrorCode(int value) {
    this.value = value;
  }

  /**
   * Return error code
   *
   * @return code
   */
  public int value() {
    return this.value;
  }
}
