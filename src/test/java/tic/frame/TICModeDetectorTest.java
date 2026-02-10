// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.frame;

import org.junit.Assert;
import org.junit.Test;

public class TICModeDetectorTest {

  @Test
  public void test_findModeFromFrameBuffer_historic() {
    // Given
    String frameBuffer = "\u0002\nADCO 031664001115 3\r\u0003";

    // When
    TICMode mode = TICModeDetector.findModeFromFrameBuffer(frameBuffer.getBytes());

    // Then
    Assert.assertEquals("TIC frame mode mismatch", TICMode.HISTORIC, mode);
  }

  @Test
  public void test_findModeFromFrameBuffer_standard() {
    // Given
    String frameBuffer = "\u0002\nADSC\t031664001115\t)\r\u0003";

    // When
    TICMode mode = TICModeDetector.findModeFromFrameBuffer(frameBuffer.getBytes());

    // Then
    Assert.assertEquals("TIC frame mode mismatch", TICMode.STANDARD, mode);
  }

  @Test
  public void test_findModeFromFrameBuffer_unknown_mode() {
    // Given
    String frameBuffer = "\u0002\nADSC\0031664001115\0)\r\u0003";

    // When
    TICMode mode = TICModeDetector.findModeFromFrameBuffer(frameBuffer.getBytes());

    // Then
    Assert.assertEquals("TIC frame mode mismatch", null, mode);
  }

  @Test
  public void test_findModeFromFrameBuffer_null_buffer() {
    // Given
    byte[] frameBuffer = null;

    // When
    IllegalArgumentException exception =
        Assert.assertThrows(
            IllegalArgumentException.class,
            () -> TICModeDetector.findModeFromFrameBuffer(frameBuffer));

    // Then
    Assert.assertEquals(
        "Tic frame buffer is null, unable to determine TIC Mode!", exception.getMessage());
  }

  @Test
  public void test_findModeFromFrameBuffer_buffer_length_too_short() {
    // Given
    String frameBuffer = "\u0002\nADSC";

    // When
    IllegalArgumentException exception =
        Assert.assertThrows(
            IllegalArgumentException.class,
            () -> TICModeDetector.findModeFromFrameBuffer(frameBuffer.getBytes()));

    // Then
    Assert.assertEquals(
        "Tic frame buffer 0x020A41445343 too short to determine TIC Mode!", exception.getMessage());
  }

  @Test
  public void test_findModeFromGroupBuffer_historic() {
    // Given
    String groupBuffer = "\nADCO 031664001115 3\r";

    // When
    TICMode mode = TICModeDetector.findModeFromGroupBuffer(groupBuffer.getBytes());

    // Then
    Assert.assertEquals("TIC group mode mismatch", TICMode.HISTORIC, mode);
  }

  @Test
  public void test_findModeFromGroupBuffer_standard() {
    // Given
    String groupBuffer = "\nADSC\t031664001115\t)\r";

    // When
    TICMode mode = TICModeDetector.findModeFromGroupBuffer(groupBuffer.getBytes());

    // Then
    Assert.assertEquals("TIC group mode mismatch", TICMode.STANDARD, mode);
  }

  @Test
  public void test_findModeFromGroupBuffer_unknown_mode() {
    // Given
    String groupBuffer = "\nADSC\0031664001115\0)\r";

    // When
    TICMode mode = TICModeDetector.findModeFromGroupBuffer(groupBuffer.getBytes());

    // Then
    Assert.assertEquals("TIC group mode mismatch", null, mode);
  }

  @Test
  public void test_findModeFromGroupBuffer_null_buffer() {
    // Given
    byte[] groupBuffer = null;

    // When
    IllegalArgumentException exception =
        Assert.assertThrows(
            IllegalArgumentException.class,
            () -> TICModeDetector.findModeFromGroupBuffer(groupBuffer));

    // Then
    Assert.assertEquals(
        "Tic group buffer is null, unable to determine TIC Mode!", exception.getMessage());
  }
}
