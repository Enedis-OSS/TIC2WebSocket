// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.service;

/**
 * Enumeration of application error codes for TIC2WebSocket.
 *
 * <p>This enum defines error codes used throughout the TIC2WebSocket application to indicate
 * specific error conditions, such as invalid command line arguments, configuration failures,
 * and application state errors. Each error code is associated with an integer value for
 * consistent error reporting and handling.
 *
 * <p>Common use cases include:
 * <ul>
 *   <li>Reporting application initialization and runtime errors
 *   <li>Standardizing error codes for logging and responses
 *   <li>Facilitating error handling in the main application logic
 * </ul>
 *
 * @author Enedis Smarties team
 */
public enum TIC2WebSocketApplicationErrorCode {
  /** No error (success). */
  NO_ERROR(0),
  /** Command line is invalid. */
  COMMAND_LINE_INVALID(1),
  /** Logger configuration update failure. */
  UPDATE_LOGGER_CONFIGURATION_FAILURE(2),
  /** Application is already started. */
  APPLICATION_ALREADY_STARTED(3),
  /** Application is not started. */
  APPLICATION_NOT_STARTED(4),
  /** Configuration loading failure. */
  LOAD_CONFIGURATION_FAILURE(5);

  /** Integer value of the error code. */
  private int code;

  /**
   * Constructs an error code enum value.
   *
   * @param code the integer value of the error code
   */
  private TIC2WebSocketApplicationErrorCode(int code) {
    this.code = code;
  }

  /**
   * Returns the integer value of the error code.
   *
   * @return the error code value
   */
  public int code() {
    return this.code;
  }
}
