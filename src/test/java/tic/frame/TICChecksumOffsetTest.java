// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.frame;

import org.junit.Assert;
import org.junit.Test;

import tic.frame.checksum.TICChecksumOffset;

public class TICChecksumOffsetTest {
  @Test
  public void test_getOffsetBegin() {
    // When
    int offsetBegin = TICChecksumOffset.getOffsetBegin();

    // Then
    Assert.assertEquals(1, offsetBegin);
  }

    @Test
    public void test_getOffsetEnd_historic() {
        // Given
        byte[] groupBuffer = "\nADCO 031664001115 3\r".getBytes();

        // When
        int offsetEnd = TICChecksumOffset.getOffsetEnd(groupBuffer, TICMode.HISTORIC);

        // Then
        Assert.assertEquals(18, offsetEnd);
    }

    @Test
    public void test_getOffsetEnd_standard() {
        // Given
        byte[] groupBuffer = "\nADSC\t031664001115\t)\r".getBytes();

        // When
        int offsetEnd = TICChecksumOffset.getOffsetEnd(groupBuffer, TICMode.STANDARD);

        // Then
        Assert.assertEquals(19, offsetEnd);
    }

    @Test 
    public void test_getOffsetEnd_without_mode() {
        // Given
        byte[] groupBuffer = "\nADSC\t031664001115\t)\r".getBytes();

        // When
        int offsetEnd = TICChecksumOffset.getOffsetEnd(groupBuffer);

        // Then
        Assert.assertEquals(19, offsetEnd);
    }

    @Test
    public void test_getOffsetEnd_auto_with_historic() {
        // Given
        byte[] groupBuffer = "\nADCO 031664001115 3\r".getBytes();

        // When
        int offsetEnd = TICChecksumOffset.getOffsetEnd(groupBuffer, TICMode.AUTO);

        // Then
        Assert.assertEquals(18, offsetEnd);
    }

    @Test
    public void test_getOffsetEnd_auto_with_standard() {
        // Given
        byte[] groupBuffer = "\nADSC\t031664001115\t)\r".getBytes();

        // When
        int offsetEnd = TICChecksumOffset.getOffsetEnd(groupBuffer, TICMode.AUTO);

        // Then
        Assert.assertEquals(19, offsetEnd);
    }

    @Test
    public void test_getOffsetEnd_with_nullBuffer() {
        // When
        IllegalArgumentException exception =
            Assert.assertThrows(
                IllegalArgumentException.class,
                () -> TICChecksumOffset.getOffsetEnd(null, TICMode.STANDARD));

        // Then
        Assert.assertEquals("groupBuffer must not be null", exception.getMessage());
    }

    @Test
    public void test_getOffsetEnd_with_nullMode() {
        // Given
        byte[] groupBuffer = "\nADSC\t031664001115\t)\r".getBytes();

        // When
        IllegalArgumentException exception =
            Assert.assertThrows(
                IllegalArgumentException.class,
                () -> TICChecksumOffset.getOffsetEnd(groupBuffer, null));

        // Then
        Assert.assertEquals("mode must not be null", exception.getMessage());
    }

    @Test
    public void test_getOffsetEnd_with_unknownMode() {
        // Given
        byte[] groupBuffer = "\nADSC\t031664001115\t)\r".getBytes();

        // When
        IllegalArgumentException exception =
            Assert.assertThrows(
                IllegalArgumentException.class,
                () -> TICChecksumOffset.getOffsetEnd(groupBuffer, TICMode.UNKNOWN));

        // Then
        Assert.assertEquals(
            "Unknown TIC mode for offset end determination: UNKNOWN", exception.getMessage());
    }

    @Test
    public void test_checkBufferOffsets_with_invalidOffsetBegin() {
        // Given
        byte[] groupBuffer = "\nADSC\t031664001115\t)\r".getBytes();
        int offsetBegin = -1;
        int offsetEnd = 5;

        // When
        IllegalArgumentException exception =
            Assert.assertThrows(
                IllegalArgumentException.class,
                () ->
                    TICChecksumOffset.checkBufferOffsets(
                        groupBuffer, offsetBegin, offsetEnd));

        // Then
        Assert.assertEquals(
            "Invalid offsetBegin for checksum computation (must be positive)",
            exception.getMessage());
    }

    @Test
    public void test_checkBufferOffsets_with_invalidOffsetEnd() {
        // Given
        byte[] groupBuffer = "\nADSC\t031664001115\t)\r".getBytes();
        int offsetBegin = 1;
        int offsetEnd = groupBuffer.length + 1;

        // When
        IllegalArgumentException exception =
            Assert.assertThrows(
                IllegalArgumentException.class,
                () ->
                    TICChecksumOffset.checkBufferOffsets(
                        groupBuffer, offsetBegin, offsetEnd));

        // Then
        Assert.assertEquals(
            "Invalid offsetEnd for checksum computation (must be lower than data length)",
            exception.getMessage());
    }

    @Test
    public void test_checkBufferOffsets_with_invalidOffsetRange() {
        // Given
        byte[] groupBuffer = "\nADSC\t031664001115\t)\r".getBytes();
        int offsetBegin = 5;
        int offsetEnd = 3;

        // When
        IllegalArgumentException exception =
            Assert.assertThrows(
                IllegalArgumentException.class,
                () ->
                    TICChecksumOffset.checkBufferOffsets(
                        groupBuffer, offsetBegin, offsetEnd));

        // Then
        Assert.assertEquals(
            "Invalid offset range for checksum computation (offsetBegin must be lower than offsetEnd)",
            exception.getMessage());
    }

    @Test
    public void test_checkBufferOffsets_with_invalidLength() {
        // Given
        byte[] groupBuffer = "\nADSC\t05\t)\r".getBytes();
        int offsetBegin = 0;
        int offsetEnd = 11;

        // When
        IllegalArgumentException exception =
            Assert.assertThrows(
                IllegalArgumentException.class,
                () ->
                    TICChecksumOffset.checkBufferOffsets(
                        groupBuffer, offsetBegin, offsetEnd));

        // Then
        Assert.assertEquals(
            "Invalid length for checksum computation (length must be lower than data length)",
            exception.getMessage());
    }
}