// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.frame.codec;

import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import tic.ResourceLoader;
import tic.frame.TICFrame;
import tic.frame.TICMode;
import tic.frame.group.TICGroup;

public class TICFrameJsonEncoderTest {

  static List<TICGroup> HISTORIC_GROUPS;
  static List<TICGroup> STANDARD_GROUPS;

  static {
    HISTORIC_GROUPS =
        Arrays.asList(
            new TICGroup("ADCO", "812164417227"),
            new TICGroup("OPTARIF", "BASE"),
            new TICGroup("ISOUSC", "15"),
            new TICGroup("BASE", "000000104"),
            new TICGroup("PTEC", "TH.."),
            new TICGroup("IINST", "000"),
            new TICGroup("IMAX", "000"),
            new TICGroup("PAPP", "00000"),
            new TICGroup("MOTDETAT", "000000", false));

    STANDARD_GROUPS =
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
                    + " NONUTILE NONUTILE"));
  }

  @Test
  public void test_encode_full_details() throws IOException, URISyntaxException, JSONException {
    // Given
    TICFrame frame = new TICFrame(TICMode.HISTORIC);
    HISTORIC_GROUPS.forEach(group -> frame.addGroup(group));
    String actualJsonText = null;
    Exception exception = null;

    // When
    try {
      actualJsonText = TICFrameDetailledCodec.getInstance().encodeToJsonString(frame);
    } catch (Exception e) {
      exception = e;
    }

    // Then
    assertNull("Exception should not be thrown during encoding", exception);
    String expectedJsonText =
        ResourceLoader.readString("/tic/frame/codec/ticFrameHistoric_full_details.json");
    JSONAssert.assertEquals(expectedJsonText, actualJsonText, false);
  }

  @Test
  public void test_encode_summarized() throws IOException, URISyntaxException, JSONException {
    // Given
    TICFrame frame = new TICFrame(TICMode.STANDARD);
    STANDARD_GROUPS.forEach(group -> frame.addGroup(group));
    String actualJsonText = null;
    Exception exception = null;

    // When
    try {
      actualJsonText = TICFrameSummarizedCodec.getInstance().encodeToJsonString(frame);
    } catch (Exception e) {
      exception = e;
    }

    // Then
    assertNull("Exception should not be thrown during encoding", exception);
    String expectedJsonText =
        ResourceLoader.readString("/tic/frame/codec/ticFrameStandard_summarized.json");
    JSONAssert.assertEquals(expectedJsonText, actualJsonText, false);
  }

  @Test
  public void test_encode_full_details_negative_indent()
      throws IOException, URISyntaxException, JSONException {
    // Given
    TICFrame frame = new TICFrame(TICMode.HISTORIC);
    HISTORIC_GROUPS.forEach(group -> frame.addGroup(group));
    String actualJsonText = null;
    Exception exception = null;

    // When
    try {
      actualJsonText = TICFrameDetailledCodec.getInstance().encodeToJsonString(frame, -1);
    } catch (Exception e) {
      exception = e;
    }

    // Then
    assertNull("Exception should not be thrown during encoding", exception);
    String expectedJsonText =
        ResourceLoader.readString("/tic/frame/codec/ticFrameHistoric_full_details.json");
    JSONAssert.assertEquals(expectedJsonText, actualJsonText, false);
  }

  @Test
  public void test_encode_summarized_negative_indent()
      throws IOException, URISyntaxException, JSONException {
    // Given
    TICFrame frame = new TICFrame(TICMode.STANDARD);
    STANDARD_GROUPS.forEach(group -> frame.addGroup(group));
    String actualJsonText = null;
    Exception exception = null;

    // When
    try {
      actualJsonText = TICFrameSummarizedCodec.getInstance().encodeToJsonString(frame, -1);
    } catch (Exception e) {
      exception = e;
    }

    // Then
    assertNull("Exception should not be thrown during encoding", exception);
    String expectedJsonText =
        ResourceLoader.readString("/tic/frame/codec/ticFrameStandard_summarized.json");
    JSONAssert.assertEquals(expectedJsonText, actualJsonText, false);
  }
}
