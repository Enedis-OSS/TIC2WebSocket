// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.io.serialport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

public class SerialPortJsonEncoderTest {

  @Test
  public void encodeShouldSerializeAllFields() {
    // Given
    SerialPortDescriptor descriptor =
        new SerialPortDescriptor(
            "ABCD1", "COM7", "USB Serial Port", 1, 2, "test_product", "test_manufacturer", "SN123");

    // When
    String jsonPayload = SerialPortJsonEncoder.encode(Arrays.asList(descriptor), -1);

    // Then
    JSONArray array = new JSONArray(jsonPayload);
    assertEquals(1, array.length());
    JSONObject json = array.getJSONObject(0);
    assertEquals("ABCD1", json.getString("portId"));
    assertEquals("COM7", json.getString("portName"));
    assertEquals("USB Serial Port", json.getString("description"));
    assertEquals(1, json.getInt("productId"));
    assertEquals(2, json.getInt("vendorId"));
    assertEquals("test_product", json.getString("productName"));
    assertEquals("test_manufacturer", json.getString("manufacturer"));
    assertEquals("SN123", json.getString("serialNumber"));
  }

  @Test
  public void encodeShouldOmitNullifiedStrings() {
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
    String jsonPayload = SerialPortJsonEncoder.encode(Collections.singletonList(descriptor), -1);

    // Then
    JSONArray array = new JSONArray(jsonPayload);
    assertEquals(1, array.length());
    JSONObject json = array.getJSONObject(0);
    assertFalse(json.has("portName"));
    assertFalse(json.has("description"));
    assertFalse(json.has("productName"));
    assertFalse(json.has("manufacturer"));
    assertFalse(json.has("serialNumber"));
  }

  @Test
  public void encodeHandlesNullList() {
    // Given
    List<SerialPortDescriptor> descriptors = null;

    // When
    String json = SerialPortJsonEncoder.encode(descriptors);

    // Then
    assertEquals("[]", json.trim());
  }
}
