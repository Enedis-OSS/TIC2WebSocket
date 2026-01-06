// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.io.serialport;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import tic.ResourceLoader;

public class SerialPortJsonEncoderTest {

  @Test
  public void encodeShouldSerializeAllFields()
      throws JSONException, IOException, URISyntaxException {
    // Given
    SerialPortDescriptor descriptor =
        new SerialPortDescriptor.Builder()
            .portId("ABCD1")
            .portName("COM7")
            .description("USB Serial Port")
            .productId((short) 1)
            .vendorId((short) 2)
            .productName("test_product")
            .manufacturer("test_manufacturer")
            .serialNumber("SN123")
            .build();

    // When
    String actualJsonText = SerialPortJsonEncoder.encode(Arrays.asList(descriptor), -1);

    // Then
    String expectedJsonText = ResourceLoader.readString("/tic/io/serialport/AllFields.json");
    JSONAssert.assertEquals(expectedJsonText, actualJsonText, false);
  }

  @Test
  public void encodeShouldOmitNullifiedStrings()
      throws JSONException, IOException, URISyntaxException {
    // Given
    SerialPortDescriptor descriptor =
        new SerialPortDescriptor.Builder()
            .portId("ABCD1")
            .portName(null)
            .description(null)
            .productId((short) 1)
            .vendorId((short) 2)
            .productName(null)
            .manufacturer(null)
            .serialNumber(null)
            .build();

    // When
    String actualJsonText = SerialPortJsonEncoder.encode(Collections.singletonList(descriptor), 1);

    // Then
    String expectedJsonText = ResourceLoader.readString("/tic/io/serialport/NullifiedStrings.json");
    JSONAssert.assertEquals(expectedJsonText, actualJsonText, false);
  }

  @Test
  public void encodeHandlesNullList() throws IOException, URISyntaxException, JSONException {
    // Given
    List<SerialPortDescriptor> descriptors = null;

    // When
    String actualJsonText = SerialPortJsonEncoder.encode(descriptors);

    // Then
    String expectedJsonText = ResourceLoader.readString("/tic/io/serialport/NullList.json");
    JSONAssert.assertEquals(expectedJsonText, actualJsonText, false);
  }
}
