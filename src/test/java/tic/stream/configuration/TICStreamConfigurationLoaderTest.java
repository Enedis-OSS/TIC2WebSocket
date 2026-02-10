// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.stream.configuration;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import org.junit.Assert;
import org.junit.Test;
import tic.ResourceLoader;
import tic.frame.TICMode;
import tic.stream.identifier.TICStreamIdentifierType;

public class TICStreamConfigurationLoaderTest {

  @Test
  public void test_load_withValidPortNameConfig_returnsConfiguration()
      throws IOException, URISyntaxException {
    // Given
    String configPath =
        ResourceLoader.getFilePath("/tic/stream/configuration/withValidPortNameConfig.json");

    // When
    TICStreamConfiguration configuration = TICStreamConfigurationLoader.load(configPath);

    // Then
    Assert.assertEquals(TICMode.HISTORIC, configuration.getTicMode());
    Assert.assertEquals(25, configuration.getTimeout());
    Assert.assertEquals(TICStreamIdentifierType.PORT_NAME, configuration.getIdentifier().getType());
    Assert.assertEquals("COM7", configuration.getIdentifier().getPortName());
  }

  @Test
  public void test_load_withPortIdIdentifier_usesDefaults() throws IOException, URISyntaxException {
    // Given
    String configPath =
        ResourceLoader.getFilePath("/tic/stream/configuration/withPortIdIdentifier.json");

    // When
    TICStreamConfiguration configuration = TICStreamConfigurationLoader.load(configPath);

    // Then
    Assert.assertEquals(TICMode.AUTO, configuration.getTicMode());
    Assert.assertEquals(10, configuration.getTimeout());
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
  public void test_load_withInvalidTimeout_throwsIllegalArgumentException()
      throws IOException, URISyntaxException {
    // Given
    String configPath =
        ResourceLoader.getFilePath("/tic/stream/configuration/withInvalidTimeout.json");

    // When
    IllegalArgumentException exception =
        Assert.assertThrows(
            IllegalArgumentException.class, () -> TICStreamConfigurationLoader.load(configPath));

    // Then
    Assert.assertTrue(exception.getMessage().contains("Timeout"));
  }

  @Test
  public void test_load_withMissingIdentifier_throwsIllegalArgumentException()
      throws IOException, URISyntaxException {
    // Given
    String configPath =
        ResourceLoader.getFilePath("/tic/stream/configuration/withMissingIdentifier.json");

    // When
    IllegalArgumentException exception =
        Assert.assertThrows(
            IllegalArgumentException.class, () -> TICStreamConfigurationLoader.load(configPath));

    // Then
    Assert.assertTrue(exception.getMessage().contains("identifier"));
  }

  @Test
  public void test_load_withInvalidIdentifierType_throwsIllegalArgumentException()
      throws IOException, URISyntaxException {
    // Given
    String configPath =
        ResourceLoader.getFilePath("/tic/stream/configuration/withInvalidIdentifierType.json");

    // When
    IllegalArgumentException exception =
        Assert.assertThrows(
            IllegalArgumentException.class,
            () -> TICStreamConfigurationLoader.load(configPath.toString()));

    // Then
    Assert.assertTrue(exception.getMessage().contains("Invalid TIC stream identifier type"));
  }

  @Test
  public void test_load_withInvalidTicMode_throwsIllegalArgumentException()
      throws IOException, URISyntaxException {
    // Given
    String configPath =
        ResourceLoader.getFilePath("/tic/stream/configuration/withInvalidTicMode.json");

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
    Assert.assertTrue(exception.getCause() instanceof NullPointerException);
  }
}
