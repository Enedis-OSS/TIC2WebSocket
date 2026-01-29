// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.core.codec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import tic.core.TICIdentifier;
import tic.util.codec.JsonArrayCodec;
import tic.util.codec.JsonObjectCodec;
import tic.util.codec.JsonStringCodec;

/**
 * Codec utilities for {@link tic.core.TICIdentifier}.
 *
 * <p>Currently focuses on JSON encoding (pretty-printed) for diagnostics and logging.
 */
public final class TICIdentifierCodec
    implements JsonObjectCodec<TICIdentifier>,
        JsonStringCodec<TICIdentifier>,
        JsonArrayCodec<List<TICIdentifier>> {

  public static TICIdentifierCodec instance = null;

  public static final TICIdentifierCodec getInstance() {
    if (instance == null) {
      return new TICIdentifierCodec();
    }
    return instance;
  }

  private TICIdentifierCodec() {}

  @Override
  public JSONObject encodeToJsonObject(TICIdentifier identifier) {
    if (identifier == null) {
      throw new IllegalArgumentException("identifier cannot be null");
    }

    JSONObject json = new JSONObject();
    json.put(
        "portName", identifier.getPortName() == null ? JSONObject.NULL : identifier.getPortName());
    json.put("portId", identifier.getPortId() == null ? JSONObject.NULL : identifier.getPortId());
    json.put(
        "serialNumber",
        identifier.getSerialNumber() == null ? JSONObject.NULL : identifier.getSerialNumber());
    return json;
  }

  @Override
  public TICIdentifier decodeFromJsonObject(Object object) {
    if (object == null || object instanceof JSONObject == false) {
      throw new IllegalArgumentException("Input is not a valid JSON object");
    }

    JSONObject jsonObject = (JSONObject) object;

    String portName = jsonObject.isNull("portName") ? null : jsonObject.getString("portName");
    String portId = jsonObject.isNull("portId") ? null : jsonObject.getString("portId");
    String serialNumber =
        jsonObject.isNull("serialNumber") ? null : jsonObject.getString("serialNumber");

    return new TICIdentifier.Builder()
        .portName(portName)
        .portId(portId)
        .serialNumber(serialNumber)
        .build();
  }

  @Override
  public JSONArray encodeToJsonArray(List<TICIdentifier> identifiers) {
    List<TICIdentifier> safeList = identifiers == null ? Collections.emptyList() : identifiers;
    JSONArray jsonArray = new JSONArray();

    for (TICIdentifier identifier : safeList) {
      jsonArray.put(encodeToJsonObject(identifier));
    }
    return jsonArray;
  }

  @Override
  public List<TICIdentifier> decodeFromJsonArray(Object object) {
    if (object == null || object instanceof JSONArray == false) {
      throw new IllegalArgumentException("Input is not a valid JSON array");
    }

    JSONArray jsonArray = (JSONArray) object;

    List<TICIdentifier> identifiers = new ArrayList<>();
    for (int i = 0; i < jsonArray.length(); i++) {
      JSONObject jsonObject = jsonArray.getJSONObject(i);
      TICIdentifier identifier = decodeFromJsonObject(jsonObject);
      identifiers.add(identifier);
    }
    return identifiers;
  }

  @Override
  public String encodeToJsonString(TICIdentifier identifier, int indentFactor) {
    if (identifier == null) {
      throw new IllegalArgumentException("identifier cannot be null");
    }

    JSONObject json = encodeToJsonObject(identifier);
    return indentFactor < 0 ? json.toString() : json.toString(indentFactor);
  }

  @Override
  public TICIdentifier decodeFromJsonString(String jsonString, int indentFactor) {
    throw new UnsupportedOperationException("Unimplemented method 'decodeFromJsonString'");
  }
}
