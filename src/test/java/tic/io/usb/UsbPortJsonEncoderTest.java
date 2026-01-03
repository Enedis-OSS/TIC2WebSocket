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

public class UsbPortJsonEncoderTest {

  @Test
  public void encodeShouldSerializeAllDescriptorFields()
      throws JSONException, IOException, URISyntaxException {
    // Given
    UsbPortDescriptor descriptor =
        new UsbPortDescriptor.Builder()
            .bcdDevice((short) 1)
            .bcdUSB((short) 2)
            .bDescriptorType((byte) 3)
            .bDeviceClass((byte) 4)
            .bDeviceProtocol((byte) 5)
            .bDeviceSubClass((byte) 6)
            .bLength((byte) 7)
            .bMaxPacketSize0((byte) 8)
            .bNumConfigurations((byte) 9)
            .idProduct((short) 10)
            .idVendor((short) 11)
            .iManufacturer((byte) 12)
            .iProduct((byte) 13)
            .iSerialNumber((byte) 14)
            .manufacturer("test_manufacturer")
            .product("test_product")
            .serialNumber("SN123")
            .build();

    // When
    String actualJsonText = UsbPortJsonEncoder.encode(Arrays.asList(descriptor), -1);

    // Then
    String expectedJsonText = ResourceLoader.readString("/tic/io/usb/AllDescriptorFields.json");
    JSONAssert.assertEquals(expectedJsonText, actualJsonText, false);
  }

  @Test
  public void encodeShouldOmitNullStringFields()
      throws JSONException, IOException, URISyntaxException {
    // Given
    UsbPortDescriptor descriptor =
        new UsbPortDescriptor.Builder()
            .bcdDevice((short) 1)
            .bcdUSB((short) 2)
            .bDescriptorType((byte) 3)
            .bDeviceClass((byte) 4)
            .bDeviceProtocol((byte) 5)
            .bDeviceSubClass((byte) 6)
            .bLength((byte) 7)
            .bMaxPacketSize0((byte) 8)
            .bNumConfigurations((byte) 9)
            .idProduct((short) 10)
            .idVendor((short) 11)
            .iManufacturer((byte) 12)
            .iProduct((byte) 13)
            .iSerialNumber((byte) 14)
            .manufacturer(null)
            .product(null)
            .serialNumber(null)
            .build();

    // When
    String actualJsonText = UsbPortJsonEncoder.encode(Arrays.asList(descriptor), -1);

    // Then
    String expectedJsonText = ResourceLoader.readString("/tic/io/usb/WithNullStringFields.json");
    JSONAssert.assertEquals(expectedJsonText, actualJsonText, false);
  }

  @Test
  public void encodeHandlesNullList() throws IOException, URISyntaxException, JSONException {
    // Given
    List<UsbPortDescriptor> list = null;

    // When
    String actualJsonText = UsbPortJsonEncoder.encode(list);

    // Then
    String expectedJsonText = ResourceLoader.readString("/tic/io/usb/NullList.json");
    JSONAssert.assertEquals(expectedJsonText, actualJsonText, false);
  }
}
