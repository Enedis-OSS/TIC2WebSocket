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
public final class UsbPortJsonEncoder {
  private static final int DEFAULT_INDENT = 2;

  private UsbPortJsonEncoder() {}

  /**
   * Encodes the provided USB descriptors list into a JSON string using the default indentation
   * factor (2 spaces).
   *
   * @param descriptors descriptors to encode; {@code null} is treated as an empty list
   * @return the JSON string
   */
  public static String encode(List<UsbPortDescriptor> descriptors) {
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
  public static String encode(List<UsbPortDescriptor> descriptors, int indentFactor) {
    List<UsbPortDescriptor> safeDescriptors =
        descriptors == null ? Collections.emptyList() : descriptors;

    JSONArray array = new JSONArray();
    safeDescriptors.forEach(
        descriptor -> {
          JSONObject jsonObject = new JSONObject();
          jsonObject.put("bcdDevice", descriptor.bcdDevice());
          jsonObject.put("bcdUSB", descriptor.bcdUSB());
          jsonObject.put("bDescriptorType", descriptor.bDescriptorType());
          jsonObject.put("bDeviceClass", descriptor.bDeviceClass());
          jsonObject.put("bDeviceProtocol", descriptor.bDeviceProtocol());
          jsonObject.put("bDeviceSubClass", descriptor.bDeviceSubClass());
          jsonObject.put("bLength", descriptor.bLength());
          jsonObject.put("bMaxPacketSize0", descriptor.bMaxPacketSize0());
          jsonObject.put("bNumConfigurations", descriptor.bNumConfigurations());
          jsonObject.put("idProduct", descriptor.idProduct());
          jsonObject.put("idVendor", descriptor.idVendor());
          jsonObject.put("iManufacturer", descriptor.iManufacturer());
          jsonObject.put("iProduct", descriptor.iProduct());
          jsonObject.put("iSerialNumber", descriptor.iSerialNumber());
          jsonObject.put("manufacturer", descriptor.manufacturer());
          jsonObject.put("product", descriptor.product());
          jsonObject.put("serialNumber", descriptor.serialNumber());
          array.put(jsonObject);
        });

    return indentFactor < 0 ? array.toString() : array.toString(indentFactor);
  }
}
