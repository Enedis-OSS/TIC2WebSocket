// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.io.usb;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

public class JSONEncoderTest {

  @Test
  public void encodeShouldSerializeDescriptors() {
    USBPortDescriptor descriptor1 =
        new USBPortDescriptor(
            0x0100,
            0x0200,
            1,
            2,
            3,
            4,
            5,
            6,
            1,
            0x1234,
            0x5678,
            1,
            2,
            3,
            "toto",
            "Product1",
            "SN123");
    USBPortDescriptor descriptor2 =
        new USBPortDescriptor(
            0x0300, 0x0400, 1, 6, 7, 8, 9, 64, 2, 0x9876, 0x5432, 4, 5, 6, null, "Product2", null);

    String json = JSONEncoder.encode(Arrays.asList(descriptor1, descriptor2), -1);
    JSONArray array = new JSONArray(json);

    assertEquals(2, array.length());

    JSONObject first = array.getJSONObject(0);
    assertEquals(0x0100, first.getInt("bcdDevice"));
    assertEquals("Product1", first.getString("product"));
    assertEquals("SN123", first.getString("serialNumber"));

    JSONObject second = array.getJSONObject(1);
    assertEquals(0x9876, second.getInt("idProduct"));
    assertEquals("Product2", second.getString("product"));
    // Serial number omitted because descriptor value is null
    assertEquals(false, second.has("serialNumber"));
  }

  @Test
  public void encodeHandlesNullList() {
    String json = JSONEncoder.encode(null);
    assertEquals("[]", json.trim());
  }
}
