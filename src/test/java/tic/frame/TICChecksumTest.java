// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.frame;

import org.junit.Assert;
import org.junit.Test;

import tic.frame.checksum.TICChecksum;

public class TICChecksumTest {
  @Test
  public void test_computeChecksum_historic() {
    // Given
    byte[] groupBuffer = "\nADCO 031664001115 3\r".getBytes();

    // When
    int checksum = TICChecksum.computeChecksum(groupBuffer, TICMode.HISTORIC);

    // Then
    Assert.assertEquals((int) '3', checksum);
  }

  @Test
  public void test_computeChecksum_standard() {
    // Given
    byte[] groupBuffer = "\nADSC\t031664001115\t)\r".getBytes();

    // When
    int checksum = TICChecksum.computeChecksum(groupBuffer, TICMode.STANDARD);

    // Then
    Assert.assertEquals((int) ')', checksum);
  }

  @Test
  public void test_computeChecksum_auto_with_historic() {
    // Given
    byte[] groupBuffer = "\nADCO 031664001115 3\r".getBytes();

    // When
    int checksum = TICChecksum.computeChecksum(groupBuffer, TICMode.AUTO);

    // Then
    Assert.assertEquals((int) '3', checksum);
  }

  @Test
  public void test_computeChecksum_auto_with_standard() {
    // Given
    byte[] groupBuffer = "\nADSC\t031664001115\t)\r".getBytes();

    // When
    int checksum = TICChecksum.computeChecksum(groupBuffer, TICMode.AUTO);

    // Then
    Assert.assertEquals((int) ')', checksum);
  }

  @Test
  public void test_computeChecksum_historic_with_tooShortBuffer() {
    // Given: only <lf>DATA<cr>, missing separator/checksum payload
    byte[] groupBuffer = "\nA\r".getBytes();

    // When
    IllegalArgumentException exception =
        Assert.assertThrows(
            IllegalArgumentException.class,
            () -> TICChecksum.computeChecksum(groupBuffer, TICMode.HISTORIC));

    // Then
    Assert.assertTrue(exception.getMessage().contains("offset range"));
  }

  @Test
  public void test_computeChecksum_standard_with_tooShortBuffer() {
    // Given: truncated group without checksum and carriage return
    byte[] groupBuffer = "\n".getBytes();

    // When
    IllegalArgumentException exception =
        Assert.assertThrows(
            IllegalArgumentException.class,
            () -> TICChecksum.computeChecksum(groupBuffer, TICMode.STANDARD));
    // Then
    Assert.assertTrue(exception.getMessage().contains("offset range"));
  }
}