// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.core;

/**
 * Enumeration of core error codes for error handling and diagnostics.
 *
 * <p>This enum defines error codes used to represent various error conditions in core operations.
 * It provides a structured way to manage and interpret error states.
 *
 * <p>Common use cases include:
 *
 * <ul>
 *   <li>Representing error states in core logic
 *   <li>Supporting diagnostics and error reporting
 *   <li>Mapping error codes to messages
 * </ul>
 *
 * @author Enedis Smarties team
 */
public enum TICCoreErrorCode {
  /** No error occurred. */
  NO_ERROR(0),
  /** The specified port ID was not found. */
  STREAM_PORT_ID_NOT_FOUND(1),
  /** The specified port name was not found. */
  STREAM_PORT_NAME_NOT_FOUND(2),
  /** The port descriptor is empty or missing required information. */
  STREAM_PORT_DESCRIPTOR_EMPTY(3),
  /** The stream mode is not defined or invalid. */
  STREAM_MODE_NOT_DEFINED(4),
  /** The stream identifier was not found or does not match any known stream. */
  STREAM_IDENTIFIER_NOT_FOUND(5),
  /** The stream was unplugged or disconnected. */
  STREAM_UNPLUGGED(6),
  /** Data read operation timed out before completion. */
  DATA_READ_TIMEOUT(7),
  /** An error occurred for another or unspecified reason. */
  OTHER_REASON(99);

  private int code;

  private TICCoreErrorCode(int code) {
    this.code = code;
  }

  public int getCode() {
    return this.code;
  }
}
