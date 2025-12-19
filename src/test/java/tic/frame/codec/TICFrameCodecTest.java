// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.frame.codec;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;
import tic.frame.TICFrame;
import tic.frame.TICMode;
import tic.frame.group.TICGroup;

public class TICFrameCodecTest {

  @Test
  public void test_decode_historic() throws IOException, URISyntaxException {
    // Given
    byte[] frameBuffer = readResourceFile("/codec/ticFrameHistoric.txt");

    // When
    TICFrame frame = TICFrameCodec.decode(frameBuffer);

    // Then
    Assert.assertEquals(TICMode.HISTORIC, frame.getMode());
    Arrays.asList(
            new TICGroup("ADCO", "812164417227"),
            new TICGroup("OPTARIF", "BASE"),
            new TICGroup("ISOUSC", "15"),
            new TICGroup("BASE", "000000104"),
            new TICGroup("PTEC", "TH.."),
            new TICGroup("IINST", "000"),
            new TICGroup("IMAX", "000"),
            new TICGroup("PAPP", "00000"),
            new TICGroup("MOTDETAT", "000000"))
        .forEach(
            expectedGroup -> {
              Assert.assertTrue(
                  "Expected group " + expectedGroup + " not found in frame groups",
                  frame.getGroupList().contains(expectedGroup));
            });
  }

  @Test
  public void test_decode_standard() throws IOException, URISyntaxException {
    // Given
    byte[] frameBuffer = readResourceFile("/codec/ticFrameStandard.txt");

    // When
    TICFrame frame = TICFrameCodec.decode(frameBuffer);

    // Then
    Assert.assertEquals(TICMode.STANDARD, frame.getMode());
    Arrays.asList(
            new TICGroup("ADSC", "031664001115"),
            new TICGroup("VTIC", "02"),
            new TICGroup("DATE", "E170915101601\t"),
            new TICGroup("NGTF", "H PLEINE/CREUSE "),
            new TICGroup("LTARF", "INDEX INACTIF 2 "),
            new TICGroup("EAST", "000000000"),
            new TICGroup("EASF01", "000000000"),
            new TICGroup("EASF02", "000000000"),
            new TICGroup("EASF03", "000000000"),
            new TICGroup("EASF04", "000000000"),
            new TICGroup("EASF05", "000000000"),
            new TICGroup("EASF06", "000000000"),
            new TICGroup("EASF07", "000000000"),
            new TICGroup("EASF08", "000000000"),
            new TICGroup("EASF09", "000000000"),
            new TICGroup("EASF10", "000000000"),
            new TICGroup("EASD01", "000000000"),
            new TICGroup("EASD02", "000000000"),
            new TICGroup("EASD03", "000000000"),
            new TICGroup("EASD04", "000000000"),
            new TICGroup("IRMS1", "000"),
            new TICGroup("URMS1", "230"),
            new TICGroup("PREF", "03"),
            new TICGroup("PCOUP", "02"),
            new TICGroup("SINSTS", "00000"),
            new TICGroup("SMAXSN", "E170915100926\t00000"),
            new TICGroup("SMAXSN-1", "E170913000000\t00000"),
            new TICGroup("CCASN", "E170913113000\t00000"),
            new TICGroup("CCASN-1", "E170913110000\t00000"),
            new TICGroup("UMOY1", "E170915101500\t230"),
            new TICGroup("STGE", "000A4411"),
            new TICGroup("MSG1", "PAS DE          MESSAGE         "),
            new TICGroup("PRM", "00000000000000"),
            new TICGroup("RELAIS", "000"),
            new TICGroup("NTARF", "02"),
            new TICGroup("NJOURF", "00"),
            new TICGroup("NJOURF+1", "00"),
            new TICGroup(
                "PJOURF+1",
                "00008002 0100C001 06008002 1230C001 15308002 NONUTILE NONUTILE NONUTILE NONUTILE"
                    + " NONUTILE NONUTILE"))
        .forEach(
            expectedGroup -> {
              Assert.assertTrue(
                  "Expected group " + expectedGroup + " not found in frame groups",
                  frame.getGroupList().contains(expectedGroup));
            });
  }

  @Test
  public void test_encode_historic() throws IOException, URISyntaxException {
    // Given
    TICFrame frame = new TICFrame(TICMode.HISTORIC);
    Arrays.asList(
            new TICGroup("ADCO", "812164417227"),
            new TICGroup("OPTARIF", "BASE"),
            new TICGroup("ISOUSC", "15"),
            new TICGroup("BASE", "000000104"),
            new TICGroup("PTEC", "TH.."),
            new TICGroup("IINST", "000"),
            new TICGroup("IMAX", "000"),
            new TICGroup("PAPP", "00000"),
            new TICGroup("MOTDETAT", "000000"))
        .forEach(
            group -> {
              frame.addGroup(group);
            });

    // When
    byte[] encodedFrameBuffer = TICFrameCodec.encode(frame);

    // Then
    byte[] expectedFrameBuffer = readResourceFile("/codec/ticFrameHistoric.txt");
    Assert.assertArrayEquals(expectedFrameBuffer, encodedFrameBuffer);
  }

  @Test
  public void test_encode_standard() throws IOException, URISyntaxException {
    // Given
    TICFrame frame = new TICFrame(TICMode.STANDARD);
    Arrays.asList(
            new TICGroup("ADSC", "031664001115"),
            new TICGroup("VTIC", "02"),
            new TICGroup("DATE", "E170915101601\t"),
            new TICGroup("NGTF", "H PLEINE/CREUSE "),
            new TICGroup("LTARF", "INDEX INACTIF 2 "),
            new TICGroup("EAST", "000000000"),
            new TICGroup("EASF01", "000000000"),
            new TICGroup("EASF02", "000000000"),
            new TICGroup("EASF03", "000000000"),
            new TICGroup("EASF04", "000000000"),
            new TICGroup("EASF05", "000000000"),
            new TICGroup("EASF06", "000000000"),
            new TICGroup("EASF07", "000000000"),
            new TICGroup("EASF08", "000000000"),
            new TICGroup("EASF09", "000000000"),
            new TICGroup("EASF10", "000000000"),
            new TICGroup("EASD01", "000000000"),
            new TICGroup("EASD02", "000000000"),
            new TICGroup("EASD03", "000000000"),
            new TICGroup("EASD04", "000000000"),
            new TICGroup("IRMS1", "000"),
            new TICGroup("URMS1", "230"),
            new TICGroup("PREF", "03"),
            new TICGroup("PCOUP", "02", false),
            new TICGroup("SINSTS", "00000"),
            new TICGroup("SMAXSN", "E170915100926\t00000"),
            new TICGroup("SMAXSN-1", "E170913000000\t00000"),
            new TICGroup("CCASN", "E170913113000\t00000"),
            new TICGroup("CCASN-1", "E170913110000\t00000"),
            new TICGroup("UMOY1", "E170915101500\t230"),
            new TICGroup("STGE", "000A4411"),
            new TICGroup("MSG1", "PAS DE          MESSAGE         "),
            new TICGroup("PRM", "00000000000000"),
            new TICGroup("RELAIS", "000"),
            new TICGroup("NTARF", "02"),
            new TICGroup("NJOURF", "00"),
            new TICGroup("NJOURF+1", "00"),
            new TICGroup(
                "PJOURF+1",
                "00008002 0100C001 06008002 1230C001 15308002 NONUTILE NONUTILE NONUTILE NONUTILE"
                    + " NONUTILE NONUTILE"))
        .forEach(
            group -> {
              frame.addGroup(group);
            });

    // When
    byte[] encodedFrameBuffer = TICFrameCodec.encode(frame);

    // Then
    byte[] expectedFrameBuffer = readResourceFile("/codec/ticFrameStandard_PCOUP_Invalid.txt");
    Assert.assertArrayEquals(expectedFrameBuffer, encodedFrameBuffer);
  }

  @Test
  public void test_encode_with_nullFrame() {
    // Given
    TICFrame frame = null;

    // When
    IllegalArgumentException exception =
        Assert.assertThrows(IllegalArgumentException.class, () -> TICFrameCodec.encode(frame));

    // Then
    Assert.assertEquals("frame cannot be null", exception.getMessage());
  }

  @Test
  public void test_decode_with_nullBuffer() {
    // Given
    byte[] frameBuffer = null;

    // When
    IllegalArgumentException exception =
        Assert.assertThrows(
            IllegalArgumentException.class, () -> TICFrameCodec.decode(frameBuffer));

    // Then
    Assert.assertEquals("frameBuffer cannot be null", exception.getMessage());
  }

  @Test
  public void test_decode_with_invalidLength() {
    // Given
    byte[] frameBuffer = new byte[3];

    // When
    IllegalArgumentException exception =
        Assert.assertThrows(
            IllegalArgumentException.class, () -> TICFrameCodec.decode(frameBuffer));

    // Then
    Assert.assertEquals("frameBuffer length must be >= 4 bytes", exception.getMessage());
  }

  @Test
  public void test_decode_with_invalidBegin() {
    // Given
    byte[] frameBuffer = new byte[] {0x00, 0x01, 0x02, 0x03};

    // When
    IllegalArgumentException exception =
        Assert.assertThrows(
            IllegalArgumentException.class, () -> TICFrameCodec.decode(frameBuffer));

    // Then
    Assert.assertEquals("frameBuffer begin must be 2", exception.getMessage());
  }

  @Test
  public void test_decode_with_invalidEnd() {
    // Given
    byte[] frameBuffer = new byte[] {0x02, 0x0A, 0x0D, 0x00};

    // When
    IllegalArgumentException exception =
        Assert.assertThrows(
            IllegalArgumentException.class, () -> TICFrameCodec.decode(frameBuffer));

    // Then
    Assert.assertEquals("frameBuffer end must be 3", exception.getMessage());
  }

  @Test
  public void test_decode_with_bufferTooShort_for_modeDetection() {
    // Given
    byte[] frameBuffer = new byte[] {0x02, 0x0A, 'A', 0x0D, 0x03};

    // When
    IllegalArgumentException exception =
        Assert.assertThrows(
            IllegalArgumentException.class, () -> TICFrameCodec.decode(frameBuffer));

    // Then
    Assert.assertEquals(
        "Tic frame buffer 0x020A410D03 too short to determine TIC Mode!", exception.getMessage());
  }

  @Test
  public void test_decode_with_invalidBuffer_for_modeDetection() {
    // Given
    byte[] frameBuffer = new byte[] {0x02, 0x0A, 'A', 'D', 'C', 'C', 0x0D, 0x03};

    // When
    IllegalArgumentException exception =
        Assert.assertThrows(
            IllegalArgumentException.class, () -> TICFrameCodec.decode(frameBuffer));

    // Then
    Assert.assertEquals("Unable to determine TIC Mode from frame buffer!", exception.getMessage());
  }

  private byte[] readResourceFile(String resourcePath) throws IOException, URISyntaxException {
    URL url = getClass().getResource(resourcePath);
    URI uri = url.toURI();
    Path path = Paths.get(uri);

    return Files.readAllBytes(path);
  }
}
