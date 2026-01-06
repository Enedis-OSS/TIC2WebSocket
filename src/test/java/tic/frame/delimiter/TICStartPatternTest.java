// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.frame.delimiter;

import org.junit.Assert;
import org.junit.Test;
import tic.frame.TICMode;

public class TICStartPatternTest {

  @Test
  public void test_historic() {
    // Given

    // When
    byte[] actualBytes = TICStartPattern.HISTORIC.getValue();

    // Then
    Assert.assertArrayEquals(
        "Historic TIC start pattern invalid",
        new byte[] {2, '\n', 'A', 'D', 'C', 'O', ' '},
        actualBytes);
  }

  @Test
  public void test_standard() {
    // Given

    // When
    byte[] actualBytes = TICStartPattern.STANDARD.getValue();

    // Then
    Assert.assertArrayEquals(
        "Standard TIC start pattern invalid",
        new byte[] {2, '\n', 'A', 'D', 'S', 'C', '\t'},
        actualBytes);
  }

  @Test
  public void test_length() {
    // Given

    // When
    int actualLength = TICStartPattern.length();

    // Then
    Assert.assertEquals("TIC start pattern length invalid", 7, actualLength);
  }

  @Test
  public void test_value_from_historic_value() {
    // Given
    TICMode mode = TICMode.HISTORIC;

    // When
    byte[] actualBytes = TICStartPattern.getValueFromMode(mode);

    // Then
    Assert.assertArrayEquals(
        "Historic TIC start pattern invalid",
        new byte[] {2, '\n', 'A', 'D', 'C', 'O', ' '},
        actualBytes);
  }

  @Test
  public void test_value_from_standard_value() {
    // Given
    TICMode mode = TICMode.STANDARD;

    // When
    byte[] actualBytes = TICStartPattern.getValueFromMode(mode);

    // Then
    Assert.assertArrayEquals(
        "Standard TIC start pattern invalid",
        new byte[] {2, '\n', 'A', 'D', 'S', 'C', '\t'},
        actualBytes);
  }

  @Test
  public void test_value_from_null_mode() {
    // Given
    TICMode mode = null;

    // When
    IllegalArgumentException exception =
        Assert.assertThrows(
            IllegalArgumentException.class, () -> TICStartPattern.getValueFromMode(mode));

    // Then
    Assert.assertEquals("cannot get start pattern value from null mode", exception.getMessage());
  }

  @Test
  public void test_value_from_invalid_mode() {
    // Given
    TICMode mode = TICMode.AUTO;

    // When
    IllegalArgumentException exception =
        Assert.assertThrows(
            IllegalArgumentException.class, () -> TICStartPattern.getValueFromMode(mode));

    // Then
    Assert.assertEquals("cannot get start pattern value from AUTO mode", exception.getMessage());
  }
}
