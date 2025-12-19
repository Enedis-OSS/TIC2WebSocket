// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.frame.delimiter;

public enum TICFrameDelimiter {
  /** Begin byte (STX, 0x02) for TIC frames. */
  BEGIN((byte) 0x02),
  /** End byte (ETX, 0x03) for TIC frames. */
  END((byte) 0x03);

  private final byte value;

  private TICFrameDelimiter(byte value) {
    this.value = value;
  }

  /**
   * Gets the hexadecimal value of the delimiter.
   *
   * @return the hexadecimal value as a byte
   */
  public byte getValue() {
    return value;
  }
}
