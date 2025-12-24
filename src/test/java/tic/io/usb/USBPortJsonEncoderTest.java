// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.io.usb;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import tic.ResourceLoader;

public class USBPortJsonEncoderTest {

  @Test
  public void encodeShouldSerializeAllDescriptorFields()
      throws JSONException, IOException, URISyntaxException {
    // Given
    USBPortDescriptor descriptor =
        new USBPortDescriptor(
            1,
            2,
            3,
            4,
            5,
            6,
            7,
            8,
            9,
            10,
            11,
            12,
            13,
            14,
            "test_manufacturer",
            "test_product",
            "SN123");

    // When
    String actualJsonText = USBPortJsonEncoder.encode(Arrays.asList(descriptor), -1);

    // Then
    String expectedJsonText = ResourceLoader.readString("/tic/io/usb/AllDescriptorFields.json");
    JSONAssert.assertEquals(expectedJsonText, actualJsonText, false);
  }

  @Test
  public void encodeShouldOmitNullStringFields()
      throws JSONException, IOException, URISyntaxException {
    // Given
    USBPortDescriptor descriptor =
        new USBPortDescriptor(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, null, null, null);

    // When
    String actualJsonText = USBPortJsonEncoder.encode(Arrays.asList(descriptor), -1);

    // Then
    String expectedJsonText = ResourceLoader.readString("/tic/io/usb/WithNullStringFields.json");
    JSONAssert.assertEquals(expectedJsonText, actualJsonText, false);
  }

  @Test
  public void encodeHandlesNullList() throws IOException, URISyntaxException, JSONException {
    // Given
    List<USBPortDescriptor> list = null;

    // When
    String actualJsonText = USBPortJsonEncoder.encode(list);

    // Then
    String expectedJsonText = ResourceLoader.readString("/tic/io/usb/NullList.json");
    JSONAssert.assertEquals(expectedJsonText, actualJsonText, false);
  }
}
