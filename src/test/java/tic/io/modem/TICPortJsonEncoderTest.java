// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.io.modem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

public class TICPortJsonEncoderTest {

  @Test
  public void encodeShouldSerializeAllFields() {
    // Given
    TICPortDescriptor descriptor =
        new TICPortDescriptor(
            "ABCD1",
            "COM7",
            "USB Serial Port",
            "test_product",
            "test_manufacturer",
            "SN123",
            TICModemType.MICHAUD);

    // When
    String jsonPayload = TICPortJsonEncoder.encode(Arrays.asList(descriptor), -1);

    // Then
    JSONArray array = new JSONArray(jsonPayload);
    assertEquals(1, array.length());
    JSONObject json = array.getJSONObject(0);
    assertEquals("ABCD1", json.getString("portId"));
    assertEquals("COM7", json.getString("portName"));
    assertEquals("USB Serial Port", json.getString("description"));
    assertEquals(TICModemType.MICHAUD.getProductId(), json.getInt("productId"));
    assertEquals(TICModemType.MICHAUD.getVendorId(), json.getInt("vendorId"));
    assertEquals("test_product", json.getString("productName"));
    assertEquals("test_manufacturer", json.getString("manufacturer"));
    assertEquals("SN123", json.getString("serialNumber"));
    assertEquals(TICModemType.MICHAUD.name(), json.getString("modemType"));
  }

  @Test
  public void encodeShouldHandleNullModemType() {
    // Given
    TICPortDescriptor descriptor =
        new TICPortDescriptor(null, "COM8", null, null, null, null, null);
    descriptor.setProductId(1234);
    descriptor.setVendorId(5678);

    // When
    String jsonPayload = TICPortJsonEncoder.encode(Collections.singletonList(descriptor), -1);

    // Then
    JSONArray array = new JSONArray(jsonPayload);
    assertEquals(1, array.length());
    JSONObject json = array.getJSONObject(0);
    assertEquals(1234, json.getInt("productId"));
    assertEquals(5678, json.getInt("vendorId"));
    assertTrue(json.isNull("modemType"));
  }

  @Test
  public void encodeHandlesNullList() {
    // Given
    List<TICPortDescriptor> descriptors = null;

    // When
    String json = TICPortJsonEncoder.encode(descriptors);

    // Then
    assertEquals("[]", json.trim());
  }
}
