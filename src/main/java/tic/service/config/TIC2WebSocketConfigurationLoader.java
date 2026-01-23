// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.service.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import tic.frame.TICMode;

public final class TIC2WebSocketConfigurationLoader {

  private TIC2WebSocketConfigurationLoader() {}

  /**
   * Load TIC2WebSocket application configuration from a JSON file.
   *
   * @param filepath the path to the configuration file
   * @return the loaded configuration
   * @throws IllegalStateException if there is an error loading or parsing the configuration
   */
  public static TIC2WebSocketConfiguration load(String filepath) {
    try {
      String json = readFileAsString(filepath);
      JSONObject rootObject = new JSONObject(new JSONTokener(json));

      int serverPort = parseServerPort(rootObject);
      TICMode ticMode = parseTicMode(rootObject);
      List<String> ticPortNames = parseTicPortNames(rootObject);

      return new TIC2WebSocketConfiguration(serverPort, ticMode, ticPortNames);
    } catch (NullPointerException
        | IOException
        | JSONException
        | IllegalArgumentException exception) {
      throw new IllegalStateException("Unable to load TIC2WebSocket configuration", exception);
    }
  }

  private static String readFileAsString(String filepath) throws IOException {
    Path path = Paths.get(filepath);
    return new String(Files.readAllBytes(path));
  }

  private static int parseServerPort(JSONObject root) {
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
