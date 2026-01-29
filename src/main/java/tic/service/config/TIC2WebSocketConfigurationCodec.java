// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.service.config;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import tic.frame.TICMode;
import tic.util.codec.JsonObjectCodec;
import tic.util.codec.JsonStringCodec;

public class TIC2WebSocketConfigurationCodec
    implements JsonStringCodec<TIC2WebSocketConfiguration>,
        JsonObjectCodec<TIC2WebSocketConfiguration> {

  private static TIC2WebSocketConfigurationCodec instance = null;

  public static TIC2WebSocketConfigurationCodec getInstance() {
    if (instance == null) {
      instance = new TIC2WebSocketConfigurationCodec();
    }
    return instance;
  }

  private TIC2WebSocketConfigurationCodec() {}

  @Override
  public String encodeToJsonString(TIC2WebSocketConfiguration object, int indentFactor)
      throws Exception {
    throw new UnsupportedOperationException("Unimplemented method 'encodeToJsonString'");
  }

  @Override
  public TIC2WebSocketConfiguration decodeFromJsonString(String jsonString, int indentFactor)
      throws Exception {
    JSONObject rootObject = new JSONObject(new JSONTokener(jsonString));
    return decodeFromJsonObject(rootObject);
  }

  @Override
  public Object encodeToJsonObject(TIC2WebSocketConfiguration object) throws Exception {
    throw new UnsupportedOperationException("Unimplemented method 'encodeToJsonObject'");
  }

  @Override
  public TIC2WebSocketConfiguration decodeFromJsonObject(Object jsonObject) throws Exception {

    JSONObject root = (JSONObject) jsonObject;

    int serverPort = parseServerPort(root);
    TICMode ticMode = parseTicMode(root);
    List<String> ticPortNames = parseTicPortNames(root);

    return new TIC2WebSocketConfiguration(serverPort, ticMode, ticPortNames);
  }

  private int parseServerPort(JSONObject root) {
    if (!root.has(TIC2WebSocketConfiguration.KEY_SERVER_PORT)) {
      throw new IllegalArgumentException(
          "Key '" + TIC2WebSocketConfiguration.KEY_SERVER_PORT + "' is required");
    }

    int serverPort = root.getInt(TIC2WebSocketConfiguration.KEY_SERVER_PORT);
    if (serverPort < TIC2WebSocketConfiguration.SERVER_PORT_MIN
        || serverPort > TIC2WebSocketConfiguration.SERVER_PORT_MAX) {
      throw new IllegalArgumentException(
          "Key '"
              + TIC2WebSocketConfiguration.KEY_SERVER_PORT
              + "' must be between "
              + TIC2WebSocketConfiguration.SERVER_PORT_MIN
              + " and "
              + TIC2WebSocketConfiguration.SERVER_PORT_MAX);
    }

    return serverPort;
  }

  private static TICMode parseTicMode(JSONObject root) {
    String modeValue =
        root.optString(
            TIC2WebSocketConfiguration.KEY_TIC_MODE,
            TIC2WebSocketConfiguration.DEFAULT_TIC_MODE.name());
    return TICMode.valueOf(modeValue.toUpperCase());
  }

  private static List<String> parseTicPortNames(JSONObject root) {
    JSONArray array = root.optJSONArray(TIC2WebSocketConfiguration.KEY_TIC_PORT_NAMES);
    if (array == null || array.length() == 0) {
      return null;
    }

    List<String> ticPortNames = new ArrayList<>(array.length());
    for (int i = 0; i < array.length(); i++) {
      String portName = array.getString(i);
      if (portName == null) {
        throw new IllegalArgumentException(
            "Key '"
                + TIC2WebSocketConfiguration.KEY_TIC_PORT_NAMES
                + "': value at index "
                + i
                + " cannot be null");
      }
      if (portName.isEmpty()) {
        throw new IllegalArgumentException(
            "Key '"
                + TIC2WebSocketConfiguration.KEY_TIC_PORT_NAMES
                + "': value at index "
                + i
                + " cannot be empty");
      }
      ticPortNames.add(portName);
    }

    return ticPortNames;
  }
}
