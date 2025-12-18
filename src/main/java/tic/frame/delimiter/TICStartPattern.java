// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.frame.delimiter;

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
   * Gets the length of the start pattern.
   *
   * @return the length as an integer
   */
  public static int length() {
    return 7;
  }
}
