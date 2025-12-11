package tic.stream.configuration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import org.junit.Assert;
import org.junit.Test;
import tic.frame.TICMode;
import tic.stream.identifier.TICStreamIdentifierType;

public class TICStreamConfigurationLoaderTest {

  @Test
  public void test_load_withValidPortNameConfig_returnsConfiguration() throws IOException {
    // Given
    Path configPath =
        createTempConfig(
            "{"
                + "\"ticMode\":\"HISTORIC\","
                + "\"timeout\":25,"
                + "\"identifier\":{"
                + "\"type\":\"PORT_NAME\","
                + "\"value\":\"COM7\"}"
                + "}");

    // When
    TICStreamConfiguration configuration = TICStreamConfigurationLoader.load(configPath.toString());

    // Then
    Assert.assertEquals(TICMode.HISTORIC, configuration.getTicMode());
    Assert.assertEquals(25, configuration.getTimeout());
    Assert.assertEquals(TICStreamIdentifierType.PORT_NAME, configuration.getIdentifier().getType());
    Assert.assertEquals("COM7", configuration.getIdentifier().getPortName());
  }

  @Test
  public void test_load_withPortIdIdentifier_usesDefaults() throws IOException {
    // Given
    Path configPath =
        createTempConfig("{\"identifier\":{\"type\":\"PORT_ID\",\"value\":\"12345\"}}");

    // When
    TICStreamConfiguration configuration = TICStreamConfigurationLoader.load(configPath.toString());

    // Then
    Assert.assertEquals(TICMode.AUTO, configuration.getTicMode());
    Assert.assertEquals(TICStreamConfiguration.DEFAULT_TIMEOUT, configuration.getTimeout());
    Assert.assertEquals("12345", configuration.getIdentifier().getPortId());
  }

  @Test
  public void test_load_withMissingFile_throwsIllegalStateException() {
    // Given
    String missingPath = "missing-config-" + System.nanoTime() + ".json";

    // When
    IllegalStateException exception =
        Assert.assertThrows(
            IllegalStateException.class, () -> TICStreamConfigurationLoader.load(missingPath));

    // Then
    Assert.assertTrue(exception.getMessage().contains("Unable to load"));
    Assert.assertTrue(exception.getCause() instanceof IOException);
  }

  @Test
  public void test_load_withInvalidTimeout_throwsIllegalArgumentException() throws IOException {
    // Given
    Path configPath =
        createTempConfig(
            "{"
                + "\"timeout\":0,"
                + "\"identifier\":{"
                + "\"type\":\"PORT_NAME\","
                + "\"value\":\"COM1\"}"
                + "}");

    // When
    IllegalArgumentException exception =
        Assert.assertThrows(
            IllegalArgumentException.class,
            () -> TICStreamConfigurationLoader.load(configPath.toString()));

    // Then
    Assert.assertTrue(exception.getMessage().contains("Timeout"));
  }

  @Test
  public void test_load_withMissingIdentifier_throwsIllegalArgumentException() throws IOException {
    // Given
    Path configPath = createTempConfig("{" + "\"ticMode\":\"STANDARD\"," + "\"timeout\":15" + "}");

    // When
    IllegalArgumentException exception =
        Assert.assertThrows(
            IllegalArgumentException.class,
            () -> TICStreamConfigurationLoader.load(configPath.toString()));

    // Then
    Assert.assertTrue(exception.getMessage().contains("identifier"));
  }

  @Test
  public void test_load_withInvalidIdentifierType_throwsIllegalArgumentException()
      throws IOException {
    // Given
    Path configPath =
        createTempConfig(
            "{" + "\"identifier\":{" + "\"type\":\"USB\"," + "\"value\":\"device\"}" + "}");

    // When
    IllegalArgumentException exception =
        Assert.assertThrows(
            IllegalArgumentException.class,
            () -> TICStreamConfigurationLoader.load(configPath.toString()));

    // Then
    Assert.assertTrue(exception.getMessage().contains("Invalid TIC stream identifier type"));
  }

  @Test
  public void test_load_withInvalidTicMode_throwsIllegalArgumentException() throws IOException {
    // Given
    Path configPath =
        createTempConfig(
            "{"
                + "\"ticMode\":\"INVALID_MODE\","
                + "\"timeout\":42,"
                + "\"identifier\":{"
                + "\"type\":\"PORT_NAME\","
                + "\"value\":\"COM9\"}"
                + "}");

    // When
    IllegalArgumentException exception =
        Assert.assertThrows(
            IllegalArgumentException.class,
            () -> TICStreamConfigurationLoader.load(configPath.toString()));

    // Then
    Assert.assertTrue(exception.getMessage().contains("No enum constant"));
  }

  @Test
  public void test_load_withResourceFile_returnsConfiguration() throws Exception {
    // Given
    String resourceLocation = "tic/stream/configuration/loader-valid.json";
    Path resourcePath =
        Paths.get(
            Objects.requireNonNull(
                    TICStreamConfigurationLoaderTest.class
                        .getClassLoader()
                        .getResource(resourceLocation))
                .toURI());

    // When
    TICStreamConfiguration configuration =
        TICStreamConfigurationLoader.load(resourcePath.toString());

    // Then
    Assert.assertEquals(TICMode.STANDARD, configuration.getTicMode());
    Assert.assertEquals(42, configuration.getTimeout());
    Assert.assertEquals("COM9", configuration.getIdentifier().getPortName());
  }

  @Test
  public void test_load_withEmptyFilePath_throwsIllegalStateException() {
    // Given
    String emptyPath = "";

    // When
    IllegalStateException exception =
        Assert.assertThrows(
            IllegalStateException.class, () -> TICStreamConfigurationLoader.load(emptyPath));

    // Then
    Assert.assertTrue(exception.getMessage().contains("Unable to load"));
    Assert.assertTrue(exception.getCause() instanceof IOException);
  }

  @Test
  public void test_load_withNullFilePath_throwsIllegalStateException() {
    // Given
    String nullPath = null;

    // When
    IllegalStateException exception =
        Assert.assertThrows(
            IllegalStateException.class, () -> TICStreamConfigurationLoader.load(nullPath));

    // Then
    Assert.assertTrue(exception.getMessage().contains("Unable to load"));
    Assert.assertTrue(exception.getCause() instanceof IOException);
  }

  private static Path createTempConfig(String jsonContent) throws IOException {
    Path tempFile = Files.createTempFile("tic-stream-config-", ".json");
    Files.write(tempFile, jsonContent.getBytes(StandardCharsets.UTF_8));
    tempFile.toFile().deleteOnExit();
    return tempFile;
  }
}
