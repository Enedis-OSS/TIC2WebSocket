// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.frame.checksum;

import tic.TICMode;

public class TICChecksum {

  /**
   * Computes the checksum for the given byte array.
   *
   * @param groupBuffer the byte array to compute the checksum for. Starting with <lf> and ending
   *     with <cr>
   * @param mode the TIC mode used. If auto mode is used, the mode will be detected from the group
   *     buffer
   * @return the computed checksum byte
   */
  public static int computeChecksum(byte[] groupBuffer, TICMode mode) {
    int offsetBegin = TICChecksumOffset.getOffsetBegin();
    int offsetEnd = TICChecksumOffset.getOffsetEnd(groupBuffer, mode);

    return computeChecksum(groupBuffer, offsetBegin, offsetEnd);
  }

  /**
   * Computes the checksum for the given byte array between the specified offsets.
   *
   * @param groupBuffer the byte array to compute the checksum for. Starting with <lf> and ending
   *     with <cr>
   * @param offsetBegin the begin offset for checksum computation
   * @param offsetEnd the end offset for checksum computation
   * @return the computed checksum byte
   */
  private static int computeChecksum(byte[] groupBuffer, int offsetBegin, int offsetEnd) {
    int crc = 0;

    TICChecksumOffset.checkBufferOffsets(groupBuffer, offsetBegin, offsetEnd);

    for (int i = offsetBegin; i < offsetEnd; i++) {
      crc = computeUpdate(crc, groupBuffer[i]);
    }

    return computeEnd(crc);
  }

  /**
   * Update checksum computation of a TIC group
   *
   * @param crc current checksum value
   * @param octet current byte of the TIC group
   * @return checksum updated
   */
  public static int computeUpdate(int crc, byte octet) {
    return crc + (octet & 0xff);
  }

  /**
   * Terminate checksum computation of a TIC group
   *
   * @param crc current checksum value
   * @return checksum computed
   */
  public static int computeEnd(int crc) {
    return (crc & 0x3F) + 0x20;
  }
}
