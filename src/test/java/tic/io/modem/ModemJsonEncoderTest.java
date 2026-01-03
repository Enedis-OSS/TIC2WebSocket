// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.io.modem;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import tic.ResourceLoader;

public class ModemJsonEncoderTest {

  @Test
  public void test_encode_AllFields() throws JSONException, IOException, URISyntaxException {
    // Given
    ModemDescriptor descriptor =
        new ModemDescriptor.Builder<>()
            .portId("ABCD1")
            .portName("COM7")
            .description("USB Serial Port")
            .productName("test_product")
            .manufacturer("test_manufacturer")
            .serialNumber("SN123")
            .modemType(ModemType.MICHAUD)
            .build();

    // When
    String actualJsonText = ModemJsonEncoder.encode(Arrays.asList(descriptor), -1);

    // Then
    String expectedJsonText = ResourceLoader.readString("/tic/io/modem/AllFields.json");
    JSONAssert.assertEquals(expectedJsonText, actualJsonText, false);
  }

  @Test
  public void test_encode_Descriptor() throws JSONException, IOException, URISyntaxException {
    // Given
    ModemDescriptor descriptor =
        new ModemDescriptor.Builder<>()
            .portId("ABCD1")
            .portName("COM7")
            .description("USB Serial Port")
            .productName("test_product")
            .manufacturer("test_manufacturer")
            .serialNumber("SN123")
            .modemType(ModemType.MICHAUD)
            .build();

    // When
    String actualJsonText = ModemJsonEncoder.encode(descriptor);

    // Then
    String expectedJsonText = ResourceLoader.readString("/tic/io/modem/Descriptor.json");
    JSONAssert.assertEquals(expectedJsonText, actualJsonText, false);
  }

  @Test
  public void encodeShouldHandleNullModemType()
      throws JSONException, IOException, URISyntaxException {
    // Given
    ModemDescriptor descriptor =
        new ModemDescriptor.Builder<>()
            .portName("COM8")
            .productId((short) 1234)
            .vendorId((short) 5678)
            .build();

    // When
    String actualJsonText = ModemJsonEncoder.encode(Collections.singletonList(descriptor), 1);

    // Then
    String expectedJsonText = ResourceLoader.readString("/tic/io/modem/NullModemType.json");
    JSONAssert.assertEquals(expectedJsonText, actualJsonText, false);
  }

  @Test
  public void encodeHandlesNullList() throws IOException, URISyntaxException, JSONException {
    // Given
    List<ModemDescriptor> descriptors = null;

    // When
    String actualJsonText = ModemJsonEncoder.encode(descriptors);

    // Then
    String expectedJsonText = ResourceLoader.readString("/tic/io/modem/NullList.json");
    JSONAssert.assertEquals(expectedJsonText, actualJsonText, false);
  }
}
