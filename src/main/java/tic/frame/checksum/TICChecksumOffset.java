// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.frame.checksum;

import tic.TICMode;
import tic.TICModeDetector;

public class TICChecksumOffset {
  public static final int OFFSET_BEGIN = 1;
  public static final int OFFSET_END_HISTORIC = 3; // excluding <space><checksum><cr>
  public static final int OFFSET_END_STANDARD = 2; // excluding <checksum><cr>

  public TICChecksumOffset() {}

  /**
   * Gets the begin offset for checksum computation.
   *
   * @return the begin offset for checksum computation
   */
  public static int getOffsetBegin() {
    return OFFSET_BEGIN;
  }

  public static int getOffsetEnd(byte[] groupBuffer) {
    return getOffsetEnd(groupBuffer, TICMode.AUTO);
  }

  /**
   * Determines the end offset for checksum computation based on the TIC mode.
   *
   * @param groupBuffer the byte array containing the group data
   * @param mode the TIC mode used. If auto mode is used, the mode will be detected from the group
   *     buffer
   * @return the end offset for checksum computation, or null if mode is unknown
   */
  public static int getOffsetEnd(byte[] groupBuffer, TICMode mode) {
    if (groupBuffer == null) {
      throw new IllegalArgumentException("groupBuffer must not be null");
    }

    if (mode == null) {
      throw new IllegalArgumentException("mode must not be null");
    }

    switch (mode) {
      case HISTORIC:
        return groupBuffer.length - OFFSET_END_HISTORIC;
      case STANDARD:
        return groupBuffer.length - OFFSET_END_STANDARD;
      case AUTO:
        mode = TICModeDetector.findModeFromGroupBuffer(groupBuffer);
        return getOffsetEnd(groupBuffer, mode);
      default:
        throw new IllegalArgumentException(
            "Unknown TIC mode for offset end determination: " + mode);
    }
  }

  /**
   * Check if the buffer length is valid for checksum computation
   *
   * @param groupBuffer the TIC group buffer to check
   * @param offsetBegin the begin offset for checksum computation
   * @param offsetEnd the end offset for checksum computation
   */
  public static void checkBufferOffsets(byte[] groupBuffer, int offsetBegin, int offsetEnd) {
    if (offsetBegin < 0) {
      throw new IllegalArgumentException(
          "Invalid offsetBegin for checksum computation (must be positive)");
    }

    if (offsetEnd > groupBuffer.length) {
      throw new IllegalArgumentException(
          "Invalid offsetEnd for checksum computation (must be lower than data length)");
    }

    if (offsetBegin >= offsetEnd) {
      throw new IllegalArgumentException(
          "Invalid offset range for checksum computation (offsetBegin must be lower than"
              + " offsetEnd)");
    }
    if (offsetEnd - offsetBegin + 1 > groupBuffer.length) {
      throw new IllegalArgumentException(
          "Invalid length for checksum computation (length must be lower than data length)");
    }
  }
}
