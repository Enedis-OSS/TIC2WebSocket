// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.frame.checksum;

import org.junit.Assert;
import org.junit.Test;
import tic.frame.TICMode;

public class TICChecksumTest {

  @Test
  public void test_verifyChecksum_valid_historic() {
    // Given
    byte[] groupBuffer = "\nADCO 031664001115 3\r".getBytes();

    // When
    boolean isValid = TICChecksum.verifyChecksum(groupBuffer, TICMode.HISTORIC);

    // Then
    Assert.assertTrue(isValid);
  }

  @Test
  public void test_verifyChecksum_valid_standard() {
    // Given
    byte[] groupBuffer = "\nADSC\t031664001115\t)\r".getBytes();

    // When
    boolean isValid = TICChecksum.verifyChecksum(groupBuffer, TICMode.STANDARD);

    // Then
    Assert.assertTrue(isValid);
  }

  @Test
  public void test_verifyChecksum_valid_auto_with_historic() {
    // Given
    byte[] groupBuffer = "\nADCO 031664001115 3\r".getBytes();

    // When
    boolean isValid = TICChecksum.verifyChecksum(groupBuffer, TICMode.AUTO);

    // Then
    Assert.assertTrue(isValid);
  }

  @Test
  public void test_verifyChecksum_valid_auto_with_standard() {
    // Given
    byte[] groupBuffer = "\nADSC\t031664001115\t)\r".getBytes();

    // When
    boolean isValid = TICChecksum.verifyChecksum(groupBuffer, TICMode.AUTO);

    // Then
    Assert.assertTrue(isValid);
  }

  @Test
  public void test_verifyChecksum_invalid_historic() {
    // Given
    byte[] groupBuffer = "\nADCO 031664001115 4\r".getBytes();

    // When
    boolean isValid = TICChecksum.verifyChecksum(groupBuffer, TICMode.HISTORIC);

    // Then
    Assert.assertFalse(isValid);
  }

  @Test
  public void test_verifyChecksum_invalid_standard() {
    // Given
    byte[] groupBuffer = "\nADSC\t031664001115\t*\r".getBytes();

    // When
    boolean isValid = TICChecksum.verifyChecksum(groupBuffer, TICMode.STANDARD);

    // Then
    Assert.assertFalse(isValid);
  }

  @Test
  public void test_verifyChecksum_invalid_auto_with_historic() {
    // Given
    byte[] groupBuffer = "\nADCO 031664001115 4\r".getBytes();

    // When
    boolean isValid = TICChecksum.verifyChecksum(groupBuffer, TICMode.AUTO);

    // Then
    Assert.assertFalse(isValid);
  }

  @Test
  public void test_verifyChecksum_invalid_auto_with_standard() {
    // Given
    byte[] groupBuffer = "\nADSC\t031664001115\t*\r".getBytes();

    // When
    boolean isValid = TICChecksum.verifyChecksum(groupBuffer, TICMode.AUTO);

    // Then
    Assert.assertFalse(isValid);
  }

  @Test
  public void test_verifyChecksum_historic_with_tooShortBuffer() {
    // Given: only <lf>DATA<cr>, missing separator/checksum payload
    byte[] groupBuffer = "\nA\r".getBytes();

    // When
    IllegalArgumentException exception =
        Assert.assertThrows(
            IllegalArgumentException.class,
            () -> TICChecksum.verifyChecksum(groupBuffer, TICMode.HISTORIC));

    // Then
    Assert.assertTrue(exception.getMessage().contains("offset range"));
  }

  @Test
  public void test_verifyChecksum_standard_with_tooShortBuffer() {
    // Given: truncated group without checksum and carriage return
    byte[] groupBuffer = "\n".getBytes();

    // When
    IllegalArgumentException exception =
        Assert.assertThrows(
            IllegalArgumentException.class,
            () -> TICChecksum.verifyChecksum(groupBuffer, TICMode.STANDARD));
    // Then
    Assert.assertTrue(exception.getMessage().contains("offset range"));
  }
}
