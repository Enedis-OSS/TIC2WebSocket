// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.service.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import org.json.JSONException;
import org.junit.Test;
import tic.ResourceLoader;
import tic.frame.TICMode;

public class TIC2WebSocketConfigurationLoaderTest {

  @Test
  public void load_minimalConfiguration_ok() throws Exception {
    // Given
    String path = ResourceLoader.getFilePath("/tic/service/config_minimal.json");

    // When
    TIC2WebSocketConfiguration cfg = TIC2WebSocketConfigurationLoader.load(path);

    // Then
    assertEquals(1234, cfg.getServerPort());
    assertEquals(TICMode.AUTO, cfg.getTicMode());
    assertNull(cfg.getTicPortNames());
  }

  @Test
  public void load_fullConfiguration_ok() throws Exception {
    // Given
    String path = ResourceLoader.getFilePath("/tic/service/config_full.json");

    // When
    TIC2WebSocketConfiguration cfg = TIC2WebSocketConfigurationLoader.load(path);

    // Then
    assertEquals(1234, cfg.getServerPort());
    assertEquals(TICMode.HISTORIC, cfg.getTicMode());
    assertEquals(Arrays.asList("COM3", "COM4"), cfg.getTicPortNames());
  }

  @Test
  public void load_ticPortNamesEmptyArray_resultsInNull() throws Exception {
    // Given
    String path = ResourceLoader.getFilePath("/tic/service/config_empty_portnames.json");

    // When
    TIC2WebSocketConfiguration cfg = TIC2WebSocketConfigurationLoader.load(path);

    // Then
    assertEquals(1234, cfg.getServerPort());
    assertEquals(TICMode.AUTO, cfg.getTicMode());
    assertNull(cfg.getTicPortNames());
  }

  @Test
  public void load_ticModeLowercase_ok() throws Exception {
    // Given
    String path = ResourceLoader.getFilePath("/tic/service/config_mode_lowercase.json");

    // When
    TIC2WebSocketConfiguration cfg = TIC2WebSocketConfigurationLoader.load(path);

    // Then
    assertEquals(1234, cfg.getServerPort());
    assertEquals(TICMode.HISTORIC, cfg.getTicMode());
  }

  @Test
  public void load_missingServerPort_throwsIllegalStateException() throws Exception {
    // Given
    String path = ResourceLoader.getFilePath("/tic/service/config_invalid_missing_port.json");
    Exception exception = null;

    // When
    try {
      TIC2WebSocketConfigurationLoader.load(path);
    } catch (Exception ex) {
      exception = ex;
    }

    // Then
    assertNotNull(exception);
    assertNotNull(exception.getCause());
    assertTrue(exception.getCause() instanceof IllegalArgumentException);
  }

  @Test
  public void load_serverPortOutOfRange_throwsIllegalStateException() throws Exception {
    // Given
    String path = ResourceLoader.getFilePath("/tic/service/config_invalid_port_range.json");
    Exception exception = null;

    // When
    try {
      TIC2WebSocketConfigurationLoader.load(path);
    } catch (Exception ex) {
      exception = ex;
    }

    // Then
    assertNotNull(exception);
    assertNotNull(exception.getCause());
    assertTrue(exception.getCause() instanceof IllegalArgumentException);
  }

  @Test
  public void load_invalidTicMode_throwsIllegalStateException() throws Exception {
    // Given
    String path = ResourceLoader.getFilePath("/tic/service/config_invalid_mode.json");
    Exception exception = null;

    // When
    try {
      TIC2WebSocketConfigurationLoader.load(path);
    } catch (Exception ex) {
      exception = ex;
    }

    // Then
    assertNotNull(exception);
    assertNotNull(exception.getCause());
    assertTrue(exception.getCause() instanceof IllegalArgumentException);
  }

  @Test
  public void load_emptyPortName_throwsIllegalStateException() throws Exception {
    // Given
    String path = ResourceLoader.getFilePath("/tic/service/config_invalid_portnames_empty.json");
    Exception exception = null;

    // When
    try {
      TIC2WebSocketConfigurationLoader.load(path);
    } catch (Exception ex) {
      exception = ex;
    }

    // Then
    assertNotNull(exception);
    assertNotNull(exception.getCause());
    assertTrue(exception.getCause() instanceof IllegalArgumentException);
  }

  @Test
  public void load_duplicatePortNameAfterTrim_throwsIllegalStateException() throws Exception {
    // Given
    String path =
        ResourceLoader.getFilePath("/tic/service/config_invalid_portnames_duplicates_trim.json");
    Exception exception = null;

    // When
    try {
      TIC2WebSocketConfigurationLoader.load(path);
    } catch (Exception ex) {
      exception = ex;
    }

    // Then
    assertNotNull(exception);
    assertNotNull(exception.getCause());
    assertTrue(exception.getCause() instanceof IllegalArgumentException);
  }

  @Test
  public void load_nullPortNameElement_throwsIllegalStateException() throws Exception {
    // Given
    String path =
        ResourceLoader.getFilePath("/tic/service/config_invalid_portnames_null_element.json");
    Exception exception = null;

    // When
    try {
      TIC2WebSocketConfigurationLoader.load(path);
    } catch (Exception ex) {
      exception = ex;
    }

    // Then
    assertNotNull(exception);
    assertNotNull(exception.getCause());
    assertTrue(exception.getCause() instanceof JSONException);
  }
}
