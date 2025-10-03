// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.tic.core;

public enum TICCoreErrorCode {
  NO_ERROR(0),
  STREAM_PORT_ID_NOT_FOUND(1),
  STREAM_PORT_NAME_NOT_FOUND(2),
  STREAM_PORT_DESCRIPTOR_EMPTY(3),
  STREAM_MODE_NOT_DEFINED(4),
  STREAM_IDENTIFIER_NOT_FOUND(5),
  STREAM_UNPLUGGED(6),
  DATA_READ_TIMEOUT(7),
  OTHER_REASON(99);

  private int code;

  private TICCoreErrorCode(int code) {
    this.code = code;
  }

  public int getCode() {
    return this.code;
  }
}
