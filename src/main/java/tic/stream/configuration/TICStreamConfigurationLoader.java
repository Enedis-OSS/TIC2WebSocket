package tic.stream.configuration;

import tic.frame.TICMode;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import tic.stream.identifier.SerialPortId;
import tic.stream.identifier.SerialPortName;
import tic.stream.identifier.TICStreamIdentifier;
import tic.stream.identifier.TICStreamIdentifierType;

public final class TICStreamConfigurationLoader {
  private static final int DEFAULT_TIMEOUT_SECONDS = 10;

  private TICStreamConfigurationLoader() {
  }

  /** Load TIC stream configuration from a JSON file.
   *
   * @param filepath the path to the configuration file
   * @return the loaded TIC stream configuration
   * @throws IllegalStateException if there is an error loading or parsing the configuration
   */
  public static TICStreamConfiguration load(String filepath) {
    try (Reader reader = resolveConfigurationReader(filepath)) {
      JSONObject rootObject = new JSONObject(new JSONTokener(reader));

      TICMode ticMode = parseTicMode(rootObject);
      int timeout = parseTimeout(rootObject);
      TICStreamIdentifier identifier = parseIdentifier(rootObject);

      return new TICStreamConfiguration(ticMode, identifier, timeout);
    } catch (IOException | JSONException exception) {
      throw new IllegalStateException("Unable to load TIC stream configuration", exception);
    }
  }

  private static Reader resolveConfigurationReader(String filepath) throws IOException {
    if (filepath != null && !filepath.isEmpty()) {
      Path path = Paths.get(filepath);
      InputStream fileStream = Files.newInputStream(path);
      return new InputStreamReader(fileStream, StandardCharsets.UTF_8);
    } else {
      throw new IOException("Configuration file path is not provided");
    }
  }

  private static TICMode parseTicMode(JSONObject root) {
    String modeValue = root.optString("ticMode", TICMode.AUTO.name());
    return TICMode.valueOf(modeValue.toUpperCase());
  }

  private static int parseTimeout(JSONObject root) {
    int timeout = root.optInt("timeout", DEFAULT_TIMEOUT_SECONDS);
    if (timeout <= 0) {
      throw new IllegalArgumentException("Timeout must be a positive integer");
    }
    return timeout;
  }

  private static TICStreamIdentifier parseIdentifier(JSONObject root) {
    JSONObject identifierObject = root.optJSONObject("identifier");
    if (identifierObject == null) {
      throw new IllegalArgumentException("TIC stream identifier is required");
    }
    String typeValue = identifierObject.optString("type");
    String value = identifierObject.optString("value");
    if (typeValue.equalsIgnoreCase(TICStreamIdentifierType.PORT_NAME.name())) {
      return new TICStreamIdentifier(new SerialPortName(value));
    } else if (typeValue.equalsIgnoreCase(TICStreamIdentifierType.PORT_ID.name())) {
      return new TICStreamIdentifier(new SerialPortId(value));
    } else {
      throw new IllegalArgumentException("Invalid TIC stream identifier type: " + typeValue);
    }
  }
}
