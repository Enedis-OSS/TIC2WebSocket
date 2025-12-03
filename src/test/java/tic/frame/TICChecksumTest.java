// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.frame;

import org.junit.Assert;
import org.junit.Test;

public class TICChecksumTest {
  @Test
  public void test_computeChecksum_emptyArray() {
    // Given
    byte[] data = new byte[0];

    // When
    byte checksum = TICChecksum.computeChecksum(data);

    // Then
    Assert.assertEquals(0, checksum);
  }

  @Test
  public void test_computeChecksum_singleByte() {
    // Given
    byte[] data = new byte[] {(byte) 0x5A};

    // When
    byte checksum = TICChecksum.computeChecksum(data);

    // Then
    Assert.assertEquals((byte) 0x5A, checksum);
  }

  @Test
  public void test_computeChecksum_multipleBytes() {
    // Given
    byte[] data = new byte[] {(byte) 0x01, (byte) 0x02, (byte) 0x03};

    // When
    byte checksum = TICChecksum.computeChecksum(data);

    // Then
    Assert.assertEquals(0, checksum);
  }

  @Test
  public void test_computeChecksum_negativeBytes() {
    // Given
    byte[] data = new byte[] {(byte) 0xFF, (byte) 0x0F, (byte) 0xF0};

    // When
    byte checksum = TICChecksum.computeChecksum(data);

    // Then
    Assert.assertEquals((byte) 0x00, checksum);
  }
}
