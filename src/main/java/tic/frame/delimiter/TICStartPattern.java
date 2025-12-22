// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.frame.delimiter;

import tic.frame.TICMode;

/** Enumeration representing TIC frame start patterns for different TIC modes. */
public enum TICStartPattern {
  /** Start pattern (STX, LF, A, D, C, O, SPACE) for historic TIC frames. */
  HISTORIC(new byte[] {2, 10, 65, 68, 67, 79, 32}),
  /** Start pattern (STX, LF, A, D, S, C, TAB) for standard TIC frames. */
  STANDARD(new byte[] {2, 10, 65, 68, 83, 67, 9});

  private final byte[] value;

  /**
   * Constructs a TICStartPattern with the specified hexadecimal value.
   *
   * @param value the byte array representing the start pattern
   */
  private TICStartPattern(byte[] value) {
    this.value = value;
  }

  /**
   * Gets the hexadecimal value of the start pattern.
   *
   * @return the hexadecimal value as a byte array
   */
  public byte[] getValue() {
    return value;
  }

  /**
   * Gets the start pattern value corresponding to the given TIC mode.
   *
   * @param mode the TIC mode
   * @return the byte array representing the start pattern for the specified mode
   * @throws IllegalArgumentException if the TIC mode is unsupported
   */
  public static byte[] getValueFromMode(TICMode mode) {
    if (mode == null) {
      return null;
    }
    switch (mode) {
      case HISTORIC:
        return HISTORIC.getValue();
      case STANDARD:
        return STANDARD.getValue();
      default:
        return null;
    }
  }

  /**
   * Gets the length of the start pattern.
   *
   * @return the length as an integer
   */
  public static int length() {
    return 7;
  }
}
