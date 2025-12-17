// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.frame.delimiter;

import tic.frame.TICMode;

/** Enumeration representing TIC frame separators for different TIC modes. */
public enum TICSeparator {
  /** Separator character (SPACE = 0x20) for historic TIC frames. */
  HISTORIC((byte) 0x20),
  /** Separator character (TAB = 0x09) for standard TIC frames. */
  STANDARD((byte) 0x09);

  private final byte value;

  /**
   * Constructs a TICSeparator with the specified hexadecimal value.
   *
   * @param value the hexadecimal byte value of the separator
   */
  private TICSeparator(byte value) {
    this.value = value;
  }

  /**
   * Gets the hexadecimal value of the separator.
   *
   * @return the hexadecimal value as a byte
   */
  public byte getValue() {
    return value;
  }

  public static byte getValueFromMode(TICMode mode) {
    if (mode == TICMode.HISTORIC) {
      return HISTORIC.getValue();
    } else if (mode == TICMode.STANDARD) {
      return STANDARD.getValue();
    }
    return 0;
  }
}
