// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.frame.delimiter;

public enum TICGroupDelimiter {
  /** Begin byte (Line Feed, 0x0A) for TIC groups. */
  BEGIN((byte) 0x0A),
  /** Begin byte (Carriage Return, 0x0D) for TIC groups. */
  END((byte) 0x0D);

  private final byte value;

  private TICGroupDelimiter(byte value) {
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
