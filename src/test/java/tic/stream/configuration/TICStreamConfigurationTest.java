// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.stream.configuration;

import tic.frame.TICMode;
import org.junit.Assert;
import org.junit.Test;

import tic.stream.identifier.SerialPortId;
import tic.stream.identifier.SerialPortName;
import tic.stream.identifier.TICStreamIdentifier;

public class TICStreamConfigurationTest {

  @Test
  public void test_constructor_withValidParametersByPortName_setsFields() {
    // Given
    TICStreamIdentifier identifier = newIdentifierByPortName();

    // When
    TICStreamConfiguration configuration =
        new TICStreamConfiguration(TICMode.STANDARD, identifier, 30);

    // Then
    Assert.assertEquals(TICMode.STANDARD, configuration.getTicMode());
    Assert.assertSame(identifier, configuration.getIdentifier());
    Assert.assertEquals(30, configuration.getTimeout());
  }

  @Test
  public void test_constructor_withValidParametersByPortId_setsFields() {
    // Given
    TICStreamIdentifier identifier = newIdentifierByPortId();

    // When
    TICStreamConfiguration configuration =
        new TICStreamConfiguration(TICMode.HISTORIC, identifier, 15);

    // Then
    Assert.assertEquals(TICMode.HISTORIC, configuration.getTicMode());
    Assert.assertSame(identifier, configuration.getIdentifier());
    Assert.assertEquals(15, configuration.getTimeout());
  }

  @Test
  public void test_constructor_withNullTicMode_throwsException() {
    // Given
    TICStreamIdentifier identifier = newIdentifierByPortName();

    // When
    IllegalArgumentException exception =
        Assert.assertThrows(
            IllegalArgumentException.class, () -> new TICStreamConfiguration(null, identifier, 10));

    // Then
    Assert.assertTrue(exception.getMessage().contains("cannot be null"));
  }

  @Test
  public void test_constructor_withNullIdentifier_throwsException() {
    // Given
    TICMode mode = TICMode.HISTORIC;

    // When
    IllegalArgumentException exception =
        Assert.assertThrows(
            IllegalArgumentException.class, () -> new TICStreamConfiguration(mode, null, 10));

    // Then
    Assert.assertTrue(exception.getMessage().contains("identifier cannot be null"));
  }

  @Test
  public void test_constructor_withZeroTimeout_throwsException() {
    // Given
    TICStreamIdentifier identifier = newIdentifierByPortName();

    // When
    IllegalArgumentException exception =
        Assert.assertThrows(
            IllegalArgumentException.class,
            () -> new TICStreamConfiguration(TICMode.AUTO, identifier, 0));

    // Then
    Assert.assertTrue(exception.getMessage().contains("positive integer"));
  }

  @Test
  public void test_constructor_withNegativeTimeout_throwsException() {
    // Given
    TICStreamIdentifier identifier = newIdentifierByPortName();

    // When
    IllegalArgumentException exception =
        Assert.assertThrows(
            IllegalArgumentException.class,
            () -> new TICStreamConfiguration(TICMode.AUTO, identifier, -5));

    // Then
    Assert.assertTrue(exception.getMessage().contains("positive integer"));
  }

  @Test
  public void test_constructor_withMaxTimeout_allowed() {
    // Given
    TICStreamIdentifier identifier = newIdentifierByPortName();

    // When
    TICStreamConfiguration configuration =
        new TICStreamConfiguration(TICMode.AUTO, identifier, Integer.MAX_VALUE);

    // Then
    Assert.assertEquals(Integer.MAX_VALUE, configuration.getTimeout());
  }

  private static TICStreamIdentifier newIdentifierByPortName() {
    return new TICStreamIdentifier(new SerialPortName("COM1"));
  }

    private static TICStreamIdentifier newIdentifierByPortId() {
        return new TICStreamIdentifier(new SerialPortId("12345"));
    }
}
