// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.frame;

public class TICChecksum {
  /**
   * Computes the checksum for the given byte array.
   *
   * @param data the byte array to compute the checksum for
   * @return the computed checksum byte
   */
  public static byte computeChecksum(byte[] data) {
    byte checksum = 0;
    for (byte b : data) {
      checksum ^= b;
    }
    return checksum;
  }
}