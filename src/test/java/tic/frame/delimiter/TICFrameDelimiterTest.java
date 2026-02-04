// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.frame.delimiter;

import org.junit.Assert;
import org.junit.Test;

public class TICFrameDelimiterTest {

  @Test
  public void test_value_begin() {
    // Given

    // When
    byte begin = TICFrameDelimiter.BEGIN.getValue();

    // Then
    Assert.assertEquals("TIC frame begin invalid", 0x02, begin);
  }

  @Test
  public void test_value_end() {
    // Given

    // When
    byte end = TICFrameDelimiter.END.getValue();

    // Then
    Assert.assertEquals("TIC frame end invalid", 0x03, end);
  }
}
