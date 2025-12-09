// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.frame.constants;

/** Enumeration representing TIC frame separators for different TIC modes. */
public enum TICSeparator {
  /** Separator character (space, 0x20) for historic TIC frames. */
  HISTORIC(' ', (byte) 0x20),
  /** Separator character (tab, 0x09) for standard TIC frames. */
  STANDARD('\t', (byte) 0x09);

  private final byte byteValue;
  private final char asciiValue;

  /**
   * Constructs a TICSeparator with the specified ASCII and hexadecimal values.
   *
   * @param asciiValue the ASCII character value of the separator
   * @param byteValue the hexadecimal byte value of the separator
   */
  private TICSeparator(char asciiValue, byte byteValue) {
    this.asciiValue = asciiValue;
    this.byteValue = byteValue;
  }

  /**
   * Gets the hexadecimal value of the separator.
   *
   * @return the hexadecimal value as a byte
   */
  public byte getByteValue() {
    return byteValue;
  }

  /**
   * Gets the ASCII character value of the separator.
   *
   * @return the ASCII character
   */
  public char getAsciiValue() {
    return asciiValue;
  }
}