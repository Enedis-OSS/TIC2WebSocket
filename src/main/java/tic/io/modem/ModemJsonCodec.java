// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.io.modem;

import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/** Utility class to convert modem descriptors to their JSON representation. */
public final class ModemJsonCodec {
  private static final int DEFAULT_INDENT = 2;

  private ModemJsonCodec() {}

  /**
   * Encodes the provided modem descriptor list into a JSON string using the default indentation
   * factor (2 spaces).
   *
   * @param descriptors descriptors to encode; {@code null} is treated as an empty list
   * @return the JSON string
   */
  public static String encode(List<ModemDescriptor> descriptors) {
    return encode(descriptors, DEFAULT_INDENT);
  }

  /**
   * Encodes the provided modem descriptor list into a JSON string.
   *
   * @param descriptors descriptors to encode; {@code null} is treated as an empty list
   * @param indentFactor indentation factor forwarded to {@link JSONArray#toString(int)}. If the
   *     value is negative the compact form is returned
   * @return the JSON string
   */
  public static String encode(List<ModemDescriptor> descriptors, int indentFactor) {
    List<ModemDescriptor> safeList =
        descriptors == null ? Collections.emptyList() : descriptors;

    JSONArray array = new JSONArray();
    safeList.forEach(
        descriptor -> array.put(new JSONObject(encode(descriptor, indentFactor))));

    return indentFactor < 0 ? array.toString() : array.toString(indentFactor);
  }

  /**
   * Encodes a single modem descriptor into a JSON string using the default indentation factor (2
   * spaces).
   *
   * @param descriptor descriptor to encode
   * @return the JSON string
   */
  public static String encode(ModemDescriptor descriptor) {
    return encode(descriptor, DEFAULT_INDENT);
  }

  public static JSONArray encodeAsArray(List<ModemDescriptor> descriptors) {
    List<ModemDescriptor> safeList =
        descriptors == null ? Collections.emptyList() : descriptors;

    JSONArray array = new JSONArray();
    safeList.forEach(descriptor -> array.put(encodeAsObject(descriptor)));

    return array;
  }

  /**
   * Encodes a single modem descriptor into a JSON object.
   *
   * @param descriptor descriptor to encode
   * @return the JSON object
   */
  public static JSONObject encodeAsObject(ModemDescriptor descriptor) {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("portId", descriptor.portId());
    jsonObject.put("portName", descriptor.portName());
    jsonObject.put("description", descriptor.description());
    jsonObject.put("productId", descriptor.productId());
    jsonObject.put("vendorId", descriptor.vendorId());
    jsonObject.put("productName", descriptor.productName());
    jsonObject.put("manufacturer", descriptor.manufacturer());
    jsonObject.put("serialNumber", descriptor.serialNumber());
    jsonObject.put(
        "modemType",
        descriptor.modemType() == null ? JSONObject.NULL : descriptor.modemType().name());
    return jsonObject;
  }

  /**
   * Encodes a single modem descriptor into a JSON string.
   *
   * @param descriptor descriptor to encode
   * @param indentFactor indentation factor forwarded to {@link JSONObject#toString(int)}. If the
   *     value is negative the compact form is returned
   * @return the JSON string
   */
  public static String encode(ModemDescriptor descriptor, int indentFactor) {
    JSONObject jsonObject = encodeAsObject(descriptor);
    return indentFactor < 0 ? jsonObject.toString() : jsonObject.toString(indentFactor);
  }
}
