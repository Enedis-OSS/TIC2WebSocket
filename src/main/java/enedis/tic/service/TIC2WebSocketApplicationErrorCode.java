// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.tic.service;

/** Application error codes list */
public enum TIC2WebSocketApplicationErrorCode {
  /** Success code */
  NO_ERROR(0),
  /** Command line invalid code */
  COMMAND_LINE_INVALID(1),
  /** Update logger configuration code */
  UPDATE_LOGGER_CONFIGURATION_FAILURE(2),
  /** Application already started code */
  APPLICATION_ALREADY_STARTED(3),
  /** Application not started code */
  APPLICATION_NOT_STARTED(4),
  /** Load configuration failure code */
  LOAD_CONFIGURATION_FAILURE(5);

  private int code;

  private TIC2WebSocketApplicationErrorCode(int code) {
    this.code = code;
  }

  /**
   * Return error code
   *
   * @return code
   */
  public int code() {
    return this.code;
  }
}
