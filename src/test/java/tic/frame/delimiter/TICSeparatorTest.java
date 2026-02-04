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

public class TICSeparatorTest {

  @Test
  public void test_value_historic() {
    // Given

    // When
    byte separator = TICSeparator.HISTORIC.getValue();

    // Then
    Assert.assertEquals("Historic TIC separator invalid", ' ', separator);
  }

  @Test
  public void test_value_standard() {
    // Given

    // When
    byte separator = TICSeparator.STANDARD.getValue();
    // Then
    Assert.assertEquals("Standard TIC separator invalid", '\t', separator);
  }

  @Test
  public void test_value_from_historic_value() {
    // Given
    TICMode mode = TICMode.HISTORIC;

    // When
    byte separator = TICSeparator.getValueFromMode(mode);
    // Then
    Assert.assertEquals("Historic TIC separator invalid", ' ', separator);
  }

  @Test
  public void test_value_from_standard_value() {
    // Given
    TICMode mode = TICMode.STANDARD;

    // When
    byte separator = TICSeparator.getValueFromMode(mode);
    // Then
    Assert.assertEquals("Standard TIC separator invalid", '\t', separator);
  }

  @Test
  public void test_value_from_null_mode() {
    // Given
    TICMode mode = null;

    // When
    IllegalArgumentException exception =
        Assert.assertThrows(
            IllegalArgumentException.class, () -> TICSeparator.getValueFromMode(mode));

    // Then
    Assert.assertEquals("cannot get separator value from null mode", exception.getMessage());
  }

  @Test
  public void test_value_from_invalid_mode() {
    // Given
    TICMode mode = TICMode.AUTO;

    // When
    IllegalArgumentException exception =
        Assert.assertThrows(
            IllegalArgumentException.class, () -> TICSeparator.getValueFromMode(mode));

    // Then
    Assert.assertEquals("cannot get separator value from AUTO mode", exception.getMessage());
  }
}
