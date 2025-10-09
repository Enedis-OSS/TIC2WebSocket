// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.protocol.tic;

import enedis.lab.protocol.tic.frame.TICError;
import enedis.lab.protocol.tic.frame.standard.TICException;
import java.util.Arrays;

/**
 * Enumeration of available TIC modes and related utilities.
 *
 * <p>This enum defines the supported TIC protocol modes (STANDARD, HISTORIC, AUTO, UNKNOWN) and
 * provides utility methods for mode detection from frame or group buffers. It also defines
 * protocol-specific separator and buffer start patterns.
 *
 * <p>Key features:
 *
 * <ul>
 *   <li>Defines all supported TIC modes
 *   <li>Provides methods to detect mode from frame or group buffers
 *   <li>Defines protocol-specific separators and buffer start patterns
 * </ul>
 *
 * @author Enedis Smarties team
 */
public enum TICMode {
  /** Unknown mode. */
  UNKNOWN,
  /** Standard mode. */
  STANDARD,
  /** Historic mode. */
  HISTORIC,
  /** Auto-detect mode. */
  AUTO;

  /** Separator character (space, 0x20) for historic TIC frames. */
  public static final char HISTORIC_SEPARATOR = ' ';

  /** Separator character (tab, 0x09) for standard TIC frames. */
  public static final char STANDARD_SEPARATOR = '\t';

  /** Buffer start pattern for historic TIC frames. */
  public static final byte[] HISTORIC_BUFFER_START = {2, 10, 65, 68, 67, 79};

  /** Buffer start pattern for standard TIC frames. */
  public static final byte[] STANDARD_BUFFER_START = {2, 10, 65, 68, 83, 67};

  /**
   * Detects the TIC mode from the given frame buffer.
   *
   * @param frameBuffer the byte array containing the frame start
   * @return the detected {@link TICMode}, or null if not recognized
   * @throws TICException if the buffer is too short or invalid
   */
  public static TICMode findModeFromFrameBuffer(byte[] frameBuffer) throws TICException {
    byte[] frameBufferStart = new byte[HISTORIC_BUFFER_START.length];
    if (frameBuffer.length < frameBufferStart.length) {
      throw new TICException(
          "Tic frame read 0x" + bytesToHex(frameBuffer) + " too short to determine TIC Mode !",
          TICError.TIC_READER_FRAME_DECODE_FAILED);
    }
    System.arraycopy(frameBuffer, 0, frameBufferStart, 0, frameBufferStart.length);
    if (Arrays.equals(frameBufferStart, HISTORIC_BUFFER_START)) {
      return HISTORIC;
    } else {
      if (STANDARD_BUFFER_START.length != HISTORIC_BUFFER_START.length) {
        frameBufferStart = new byte[STANDARD_BUFFER_START.length];
        System.arraycopy(frameBuffer, 0, frameBufferStart, 0, frameBufferStart.length);
      }
      if (Arrays.equals(frameBufferStart, STANDARD_BUFFER_START)) {
        return STANDARD;
      }
      return null;
    }
  }

  /**
   * Detects the TIC mode from the given group buffer.
   *
   * @param groupBuffer the byte array containing the group data
   * @return the detected {@link TICMode}, or null if not recognized
   */
  public static TICMode findModeFromGroupBuffer(byte[] groupBuffer) {
    for (int i = 0; i < groupBuffer.length; i++) {
      if (groupBuffer[i] == HISTORIC_SEPARATOR) {
        return HISTORIC;
      } else if (groupBuffer[i] == STANDARD_SEPARATOR) {
        return STANDARD;
      }
    }
    return null;
  }

  /**
   * Converts a byte array to a hexadecimal string (replacement for
   * DatatypeConverter.printHexBinary).
   *
   * @param bytes the byte array to convert
   * @return the hexadecimal string representation
   */
  private static String bytesToHex(byte[] bytes) {
    StringBuilder result = new StringBuilder();
    for (byte b : bytes) {
      result.append(String.format("%02X", b));
    }
    return result.toString();
  }
}
