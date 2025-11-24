// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.io.serialport;

import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/** Utility class to convert serial port descriptors to their JSON representation. */
public final class SerialPortJsonEncoder {
  private static final int DEFAULT_INDENT = 2;

  private SerialPortJsonEncoder() {}

  /**
   * Encodes the provided serial port descriptors list into a JSON string using the default indentation
   * factor (2 spaces).
   *
   * @param descriptors descriptors to encode; {@code null} is treated as an empty list
   * @return the JSON string
   */
  public static String encode(List<SerialPortDescriptor> descriptors) {
    return encode(descriptors, DEFAULT_INDENT);
  }

  /**
   * Encodes the provided serial port descriptors list into a JSON string.
   *
   * @param descriptors descriptors to encode; {@code null} is treated as an empty list
   * @param indentFactor indentation factor forwarded to {@link JSONArray#toString(int)}. If the
   *     value is negative the compact form is returned
   * @return the JSON string
   */
  public static String encode(List<SerialPortDescriptor> descriptors, int indentFactor) {
    List<SerialPortDescriptor> safeDescriptors =
        descriptors == null ? Collections.emptyList() : descriptors;

    JSONArray array = new JSONArray();
    safeDescriptors.forEach(
        descriptor -> {
          JSONObject jsonObject = new JSONObject();
          jsonObject.put("portId", descriptor.getPortId());
          jsonObject.put("portName", descriptor.getPortName());
          jsonObject.put("description", descriptor.getDescription());
          jsonObject.put("productId", descriptor.getProductId());
          jsonObject.put("vendorId", descriptor.getVendorId());
          jsonObject.put("productName", descriptor.getProductName());
          jsonObject.put("manufacturer", descriptor.getManufacturer());
          jsonObject.put("serialNumber", descriptor.getSerialNumber());
          array.put(jsonObject);
        });

    return indentFactor < 0 ? array.toString() : array.toString(indentFactor);
  }
}
