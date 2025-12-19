// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.frame.delimiter;

import org.junit.Assert;
import org.junit.Test;

public class TICGroupDelimiterTest {
  @Test
  public void test_value_begin() {
    // Given

    // When
    byte begin = TICGroupDelimiter.BEGIN.getValue();

    // Then
    Assert.assertEquals("TIC group begin invalid", '\n', begin);
  }

  @Test
  public void test_value_end() {
    // Given

    // When
    byte end = TICGroupDelimiter.END.getValue();

    // Then
    Assert.assertEquals("TIC group end invalid", '\r', end);
  }
}
