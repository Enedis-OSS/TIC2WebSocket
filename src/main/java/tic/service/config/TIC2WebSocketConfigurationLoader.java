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
      return TIC2WebSocketConfigurationCodec.getInstance().decodeFromJsonString(json);
    } catch (Exception exception) {
      throw new IllegalStateException("Unable to load TIC2WebSocket configuration", exception);
    }
  }

  private static String readFileAsString(String filepath) throws IOException {
    Path path = Paths.get(filepath);
    return new String(Files.readAllBytes(path));
  }
}
