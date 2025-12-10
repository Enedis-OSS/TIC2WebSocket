// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.frame;

import enedis.lab.protocol.tic.frame.TICError;
import enedis.lab.protocol.tic.frame.standard.TICException;
import java.util.Arrays;

import tic.frame.constants.TICSeparator;
import tic.frame.constants.TICStartPattern;

/**
 * Utility class for detecting TIC modes from frame or group buffers.
 *
 * <p>This class provides methods to identify the TIC mode (STANDARD, HISTORIC) based on the
 * contents of frame or group byte arrays.
 *
 * @author Enedis Smarties team
 */
public class TICModeDetector {
  /**
   * Detects the TIC mode from the given frame buffer.
   *
   * @param frameBuffer the byte array containing the frame start
   * @return the detected {@link TICMode}, or null if not recognized
   * @throws TICException if the buffer is too short or invalid
   */
  public static TICMode findModeFromFrameBuffer(byte[] frameBuffer) throws TICException {
    byte[] frameBufferStart = new byte[TICStartPattern.length()];
    if (frameBuffer.length < frameBufferStart.length) {
      throw new TICException(
          "Tic frame read 0x" + bytesToHex(frameBuffer) + " too short to determine TIC Mode !",
          TICError.TIC_READER_FRAME_DECODE_FAILED);
    }
    System.arraycopy(frameBuffer, 0, frameBufferStart, 0, frameBufferStart.length);
    if (Arrays.equals(frameBufferStart, TICStartPattern.HISTORIC.getHexValue())) {
      return TICMode.HISTORIC;
    } else {
      if (Arrays.equals(frameBufferStart, TICStartPattern.STANDARD.getHexValue())) {
        return TICMode.STANDARD;
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
      if (groupBuffer[i] == TICSeparator.HISTORIC.getByteValue()) {
        return TICMode.HISTORIC;
      } else if (groupBuffer[i] == TICSeparator.STANDARD.getByteValue()) {
        return TICMode.STANDARD;
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
