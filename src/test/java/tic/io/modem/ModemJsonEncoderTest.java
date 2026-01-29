// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.io.modem;

import java.util.List;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import tic.ResourceLoader;

public class ModemJsonEncoderTest {

  @Test
  public void test_encode_AllFields() throws Exception {
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
    String actualJsonText =
        ModemJsonCodec.getInstance()
            .encodeToJsonObject(descriptor).toString();

    // Then
    String expectedJsonText = ResourceLoader.readString("/tic/io/modem/AllFields.json");
    JSONAssert.assertEquals(expectedJsonText, actualJsonText, false);
  }

  @Test
  public void test_encode_Descriptor() throws Exception {
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
    String actualJsonText = ModemJsonCodec.getInstance().encodeToJsonString(descriptor);

    // Then
    String expectedJsonText = ResourceLoader.readString("/tic/io/modem/Descriptor.json");
    JSONAssert.assertEquals(expectedJsonText, actualJsonText, false);
  }

  @Test
  public void encodeShouldHandleNullModemType() throws Exception {
    // Given
    ModemDescriptor descriptor =
        new ModemDescriptor.Builder<>()
            .portName("COM8")
            .productId((short) 1234)
            .vendorId((short) 5678)
            .build();

    // When
    String actualJsonText =
        ModemJsonCodec.getInstance()
            .encodeToJsonString(descriptor);

    // Then
    String expectedJsonText = ResourceLoader.readString("/tic/io/modem/NullModemType.json");
    JSONAssert.assertEquals(expectedJsonText, actualJsonText, false);
  }

  @Test
  public void encodeHandlesNullList() throws Exception {
    // Given
    List<ModemDescriptor> descriptors = null;

    // When
    String actualJsonText =
        ModemJsonCodec.getInstance().encodeToJsonArray(descriptors).toString();

    // Then
    String expectedJsonText = ResourceLoader.readString("/tic/io/modem/NullList.json");
    JSONAssert.assertEquals(expectedJsonText, actualJsonText, false);
  }
}
