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
        new SerialPortDescriptor(
            "ABCD1", "COM7", "USB Serial Port", 1, 2, "test_product", "test_manufacturer", "SN123");

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
    SerialPortDescriptor descriptor = new SerialPortDescriptor();
    descriptor.setPortId("ABCD1");
    descriptor.setPortName(null);
    descriptor.setDescription(null);
    descriptor.setProductId(1);
    descriptor.setVendorId(2);
    descriptor.setProductName(null);
    descriptor.setManufacturer(null);
    descriptor.setSerialNumber(null);

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
