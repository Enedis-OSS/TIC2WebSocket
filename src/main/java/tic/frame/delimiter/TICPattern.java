// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.frame.delimiter;

/** Class storing TIC control characters used in frame delimiters. */
public final class TICPattern {
  /** Start of Text (STX) control character - 0x02 */
  public static final byte STX = 2;

  /** End of Text (ETX) control character - 0x03 */
  public static final byte ETX = 3;

  /** Line Feed (LF) control character - 0x0A */
  public static final byte LF = 10;

  /** Carriage Return (CR) control character - 0x0D */
  public static final byte CR = 13;

  private TICPattern() {}
}
