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
import tic.util.codec.JsonArrayCodec;
import tic.util.codec.JsonObjectCodec;
import tic.util.codec.JsonStringCodec;

/** Utility class to convert modem descriptors to their JSON representation. */
public final class ModemJsonCodec
    implements JsonArrayCodec<List<ModemDescriptor>>,
        JsonObjectCodec<ModemDescriptor>,
        JsonStringCodec<ModemDescriptor> {

  private static ModemJsonCodec instance = new ModemJsonCodec();

  public static ModemJsonCodec getInstance() {
    if (instance == null) {
      instance = new ModemJsonCodec();
    }
    return instance;
  }

  private ModemJsonCodec() {}

  @Override
  public Object encodeToJsonArray(List<ModemDescriptor> descriptors) throws Exception {
    List<ModemDescriptor> safeList = descriptors == null ? Collections.emptyList() : descriptors;

    JSONArray array = new JSONArray();
    safeList.forEach(
        descriptor -> {
          try {
            array.put(encodeToJsonObject(descriptor));
          } catch (Exception e) {
            array.put(JSONObject.NULL);
          }
        });

    return array;
  }

  @Override
  public List<ModemDescriptor> decodeFromJsonArray(Object jsonArray) throws Exception {
    throw new UnsupportedOperationException("Unimplemented method 'decodeFromJsonArray'");
  }

  @Override
  public Object encodeToJsonObject(ModemDescriptor descriptor) throws Exception {
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

  @Override
  public ModemDescriptor decodeFromJsonObject(Object jsonObject) throws Exception {
    throw new UnsupportedOperationException("Unimplemented method 'decodeFromJsonObject'");
  }

  @Override
  public String encodeToJsonString(ModemDescriptor descriptor, int indentFactor) throws Exception {
    if (descriptor == null) {
      throw new IllegalArgumentException("descriptor cannot be null");
    }

    JSONObject json = (JSONObject) encodeToJsonObject(descriptor);
    return indentFactor < 0 ? json.toString() : json.toString(indentFactor);
  }

  @Override
  public ModemDescriptor decodeFromJsonString(String jsonString, int indentFactor)
      throws Exception {
    throw new UnsupportedOperationException("Unimplemented method 'decodeFromJsonString'");
  }
}
