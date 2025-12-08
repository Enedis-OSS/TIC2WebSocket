// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.frame;

import tic.TICMode;
import tic.TICModeDetector;
import tic.frame.constants.TICSeparator;

public class TICChecksum {
  public static final int OFFSET_BEGIN = 1;

  /**
   * Computes the checksum for the given byte array.
   *
   * @param data the byte array to compute the checksum for
   * @return the computed checksum byte
   */
  public static int computeChecksum(byte[] data, TICMode mode) {
    int crc = 0;
    for (int i = OFFSET_BEGIN; i < data.length - 3; i++) {
      crc = computeUpdate(crc, data[i]);
      if (mode == TICMode.AUTO) {
        if (data[i] == TICSeparator.HISTORIC.getByteValue()) {
          mode = TICMode.HISTORIC;
        } else if (data[i] == TICSeparator.STANDARD.getByteValue()) {
          mode = TICMode.STANDARD;
        }
      }
    }
    if (mode == TICMode.STANDARD) {
      crc = computeUpdate(crc, data[data.length - 2]);
    }
    return computeEnd(crc);
  }

  /**
   * Update checksum computation of a TIC group
   *
   * @param crc current checksum value
   * @param octet current byte of the TIC group
   * @return Checksum updated
   */
  public static int computeUpdate(int crc, byte octet) {
    return crc + (octet & 0xff);
  }

  /**
   * Terminate checksum computation of a TIC group
   *
   * @param crc current checksum value
   * @return Checksum computed
   */
  public static int computeEnd(int crc) {
    return (crc & 0x3F) + 0x20;
  }

  /**
   * Get offset of the end position used for checksum computation of a TIC group
   *
   * @param groupBuffer the TIC group buffer which end position is calculated
   * @return Offset position
   */
  public static int offsetEnd(byte[] groupBuffer) {
    return offsetEnd(groupBuffer, TICMode.AUTO);
  }

  /**
   * Get offset of the end position used for checksum computation of a TIC group for a specified
   * mode
   *
   * @param groupBuffer the TIC group buffer which end position is calculated
   * @param mode the TIC mode used
   * @return Offset position
   */
  public static int offsetEnd(byte[] groupBuffer, TICMode mode) {
    switch (mode) {
      case HISTORIC:
        return groupBuffer.length - 3;
      case STANDARD:
        return groupBuffer.length - 2;
      case AUTO:
        mode = TICModeDetector.findModeFromGroupBuffer(groupBuffer);
        if (mode == TICMode.HISTORIC) {
          return groupBuffer.length - 3;
        } else if (mode == TICMode.STANDARD) {
          return groupBuffer.length - 2;
        } else {
          return -1;
        }
      default:
        return -1;
    }
  }
}
