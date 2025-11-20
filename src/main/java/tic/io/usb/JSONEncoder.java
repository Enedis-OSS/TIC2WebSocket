// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.io.usb;

import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/** Utility class to convert USB port descriptors to their JSON representation. */
public final class JSONEncoder {
  private static final int DEFAULT_INDENT = 2;

  private JSONEncoder() {}

  /**
   * Encodes the provided USB descriptors list into a JSON string using the default indentation
   * factor (2 spaces).
   *
   * @param descriptors descriptors to encode; {@code null} is treated as an empty list
   * @return the JSON string
   */
  public static String encode(List<USBPortDescriptor> descriptors) {
    return encode(descriptors, DEFAULT_INDENT);
  }

  /**
   * Encodes the provided USB descriptors list into a JSON string.
   *
   * @param descriptors descriptors to encode; {@code null} is treated as an empty list
   * @param indentFactor indentation factor forwarded to {@link JSONArray#toString(int)}. If the
   *     value is negative the compact form is returned
   * @return the JSON string
   */
  public static String encode(List<USBPortDescriptor> descriptors, int indentFactor) {
    List<USBPortDescriptor> safeDescriptors =
        descriptors == null ? Collections.emptyList() : descriptors;

    JSONArray array = new JSONArray();
    safeDescriptors.forEach(
        descriptor -> {
          JSONObject jsonObject = new JSONObject();
          jsonObject.put("bcdDevice", descriptor.getBcdDevice());
          jsonObject.put("bcdUSB", descriptor.getBcdUSB());
          jsonObject.put("bDescriptorType", descriptor.getBDescriptorType());
          jsonObject.put("bDeviceClass", descriptor.getBDeviceClass());
          jsonObject.put("bDeviceProtocol", descriptor.getBDeviceProtocol());
          jsonObject.put("bDeviceSubClass", descriptor.getBDeviceSubClass());
          jsonObject.put("bLength", descriptor.getBLength());
          jsonObject.put("bMaxPacketSize0", descriptor.getBMaxPacketSize0());
          jsonObject.put("bNumConfigurations", descriptor.getBNumConfigurations());
          jsonObject.put("idProduct", descriptor.getIdProduct());
          jsonObject.put("idVendor", descriptor.getIdVendor());
          jsonObject.put("iManufacturer", descriptor.getIManufacturer());
          jsonObject.put("iProduct", descriptor.getIProduct());
          jsonObject.put("iSerialNumber", descriptor.getISerialNumber());
          jsonObject.put("manufacturer", descriptor.getManufacturer());
          jsonObject.put("product", descriptor.getProduct());
          jsonObject.put("serialNumber", descriptor.getSerialNumber());
          array.put(jsonObject);
        });

    return indentFactor < 0 ? array.toString() : array.toString(indentFactor);
  }
}
