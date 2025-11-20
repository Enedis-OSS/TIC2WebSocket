// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.io.usb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Arrays;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

public class JSONEncoderTest {

  @Test
  public void encodeShouldSerializeAllDescriptorFields() {
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
    String jsonPayload = JSONEncoder.encode(Arrays.asList(descriptor), -1);

    // Then
    JSONArray array = new JSONArray(jsonPayload);
    assertEquals(1, array.length());
    JSONObject json = array.getJSONObject(0);
    assertEquals(1, json.getInt("bcdDevice"));
    assertEquals(2, json.getInt("bcdUSB"));
    assertEquals(3, json.getInt("bDescriptorType"));
    assertEquals(4, json.getInt("bDeviceClass"));
    assertEquals(5, json.getInt("bDeviceProtocol"));
    assertEquals(6, json.getInt("bDeviceSubClass"));
    assertEquals(7, json.getInt("bLength"));
    assertEquals(8, json.getInt("bMaxPacketSize0"));
    assertEquals(9, json.getInt("bNumConfigurations"));
    assertEquals(10, json.getInt("idProduct"));
    assertEquals(11, json.getInt("idVendor"));
    assertEquals(12, json.getInt("iManufacturer"));
    assertEquals(13, json.getInt("iProduct"));
    assertEquals(14, json.getInt("iSerialNumber"));
    assertEquals("test_manufacturer", json.getString("manufacturer"));
    assertEquals("test_product", json.getString("product"));
    assertEquals("SN123", json.getString("serialNumber"));
  }

  @Test
  public void encodeShouldOmitNullStringFields() {
    // Given
    USBPortDescriptor descriptor =
        new USBPortDescriptor(
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, null, null, null);

    // When
    String jsonPayload = JSONEncoder.encode(Arrays.asList(descriptor), -1);

    // Then
    JSONArray array = new JSONArray(jsonPayload);
    assertEquals(1, array.length());
    JSONObject json = array.getJSONObject(0);
    assertFalse(json.has("manufacturer"));
    assertFalse(json.has("product"));
    assertFalse(json.has("serialNumber"));
  }

  @Test
  public void encodeHandlesNullList() {
    // Given
    List<USBPortDescriptor> list = null;

    // When
    String json = JSONEncoder.encode(list);

    // Then
    assertEquals("[]", json.trim());
  }
}
