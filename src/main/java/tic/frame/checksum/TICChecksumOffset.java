// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.frame.checksum;

import tic.frame.TICMode;
import tic.frame.TICModeDetector;

public class TICChecksumOffset {
  public static final int OFFSET_BEGIN = 1;
  public static final int OFFSET_END_HISTORIC = 3; // excluding <space><checksum><cr>
  public static final int OFFSET_END_STANDARD = 2; // excluding <checksum><cr>
  public static final int OFFSET_CHECKSUM = 1; // excluding <cr>

  private TICChecksumOffset() {}

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

  public static int getOffsetChecksum(byte[] groupBuffer) {
    checkBuffer(groupBuffer);
    return getOffsetChecksum(groupBuffer, groupBuffer.length - 1);
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
    checkBuffer(groupBuffer);
    return getOffsetEnd(groupBuffer, mode, 0, groupBuffer.length - 1);
  }

  public static int getOffsetChecksum(byte[] buffer, int endOffset) {
    return endOffset - OFFSET_CHECKSUM;
  }

  /**
   * Determines the end offset for checksum computation based on the TIC mode and a given offset.
   *
   * @param buffer the byte array containing the group data
   * @param mode the TIC mode used. If auto mode is used, the mode will be detected from the group
   * @param beginOffset the begin offset for the buffer
   * @return the end offset for checksum computation, or null if mode is unknown
   */
  public static int getOffsetEnd(byte[] buffer, TICMode mode, int beginOffset, int endOffset) {
    checkBufferOffsets(buffer, beginOffset, endOffset);
    if (mode == null) {
      throw new IllegalArgumentException("mode must not be null");
    }
    if (mode == TICMode.AUTO) {
      mode = TICModeDetector.findModeFromGroupBuffer(buffer);
    }
    if( mode == null) {
      throw new IllegalArgumentException("Unable to determine TIC mode from group buffer");
    }
    int length = endOffset - beginOffset + 1;
    if (mode == TICMode.HISTORIC) {
      return beginOffset + (length - OFFSET_END_HISTORIC);
    }
    else  {
      return beginOffset + (length - OFFSET_END_STANDARD);
    }
  }

  public static void checkBuffer(byte[] buffer) {
    if (buffer == null) {
      throw new IllegalArgumentException("groupBuffer must not be null");
    }
  }

  /**
   * Check if the buffer length is valid for checksum computation
   *
   * @param buffer the TIC buffer to check
   * @param offsetBegin the begin offset for checksum computation
   * @param offsetEnd the end offset for checksum computation
   */
  public static void checkBufferOffsets(byte[] buffer, int offsetBegin, int offsetEnd) {
    checkBuffer(buffer);
    if (offsetBegin < 0) {
      throw new IllegalArgumentException(
          "Invalid offsetBegin for checksum computation (must be positive)");
    }

    if (offsetEnd > buffer.length) {
      throw new IllegalArgumentException(
          "Invalid offsetEnd for checksum computation (must be lower than data length)");
    }

    if (offsetBegin >= offsetEnd) {
      throw new IllegalArgumentException(
          "Invalid offset range for checksum computation (offsetBegin must be lower than"
              + " offsetEnd)");
    }
    if (offsetEnd - offsetBegin + 1 > buffer.length) {
      throw new IllegalArgumentException(
          "Invalid length for checksum computation (length must be lower than data length)");
    }
  }
}
