// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.frame;

import org.junit.Assert;
import org.junit.Test;

import tic.TICMode;

public class TICChecksumTest {
  @Test
  public void test_computeChecksum_historic() {
    // Given
    byte[] data = "ADCO 031664001115 ".getBytes();
    // When
    int checksum = TICChecksum.computeChecksum(data, TICMode.HISTORIC);
    // Then
    Assert.assertEquals((int)'3', checksum);
  }
}
