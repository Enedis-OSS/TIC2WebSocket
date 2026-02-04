// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.core;

import org.junit.Assert;
import org.junit.Test;

public class TICIdentifierTest {
  @Test
  public void test_constructor_full() {
    TICIdentifier identifier = new TICIdentifier.Builder().portId("{1-1}").portName("COM3").serialNumber("021976551632").build();

    Assert.assertEquals("{1-1}", identifier.getPortId());
    Assert.assertEquals("COM3", identifier.getPortName());
    Assert.assertEquals("021976551632", identifier.getSerialNumber());
  }

  @Test
  public void test_constructor_onlyPortId() {
    TICIdentifier identifier = new TICIdentifier.Builder().portId("{1-1}").build();

    Assert.assertEquals("{1-1}", identifier.getPortId());
    Assert.assertNull(identifier.getPortName());
    Assert.assertNull(identifier.getSerialNumber());
  }

  @Test
  public void test_constructor_onlyPortName() {
    TICIdentifier identifier = new TICIdentifier.Builder().portName("COM3").build();

    Assert.assertNull(identifier.getPortId());
    Assert.assertEquals("COM3", identifier.getPortName());
    Assert.assertNull(identifier.getSerialNumber());
  }

  @Test
  public void test_constructor_onlySerialNumber() {
    TICIdentifier identifier = new TICIdentifier.Builder().serialNumber("021976551632").build();

    Assert.assertNull(identifier.getPortId());
    Assert.assertNull(identifier.getPortName());
    Assert.assertEquals("021976551632", identifier.getSerialNumber());
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_constructor_empty() {
    new TICIdentifier.Builder().build();
  }

  @Test
  public void test_matches_null() {
    TICIdentifier identifier = new TICIdentifier.Builder().serialNumber("021976551632").build();
    Assert.assertFalse(identifier.matches(null));
  }

  @Test
  public void test_matches_same_serialNumber() {
    TICIdentifier identifier1 = new TICIdentifier.Builder().portId("{1-1}").portName("COM3").serialNumber("021976551632").build();
    TICIdentifier identifier2 = new TICIdentifier.Builder().portId("{1-2}").portName("COM4").serialNumber("021976551632").build();

    Assert.assertTrue(identifier1.matches(identifier2));
  }

  @Test
  public void test_matches_different_serialNumber() {
    TICIdentifier identifier1 = new TICIdentifier.Builder().portId("{1-1}").portName("COM3").serialNumber("021976551632").build();
    TICIdentifier identifier2 = new TICIdentifier.Builder().portId("{1-1}").portName("COM3").serialNumber("021976551638").build();

    Assert.assertFalse(identifier1.matches(identifier2));
  }

  @Test
  public void test_matches_same_portId() {
    TICIdentifier identifier1 = new TICIdentifier.Builder().portId("{1-1}").portName("COM3").serialNumber("021976551632").build();
    TICIdentifier identifier2 = new TICIdentifier.Builder().portId("{1-1}").portName("COM4").serialNumber("021976551633").build();
    TICIdentifier identifier3 = new TICIdentifier.Builder().portId("{1-1}").portName("COM5").build();

    Assert.assertTrue(identifier1.matches(identifier3));
    Assert.assertFalse(identifier1.matches(identifier2));
  }

  @Test
  public void test_matches_different_portId() {
    TICIdentifier identifier1 = new TICIdentifier.Builder().portId("{1-1}").build();
    TICIdentifier identifier2 = new TICIdentifier.Builder().portId("{1-7}").build();

    Assert.assertFalse(identifier1.matches(identifier2));
  }

  @Test
  public void test_matches_same_portName() {
    TICIdentifier identifier1 = new TICIdentifier.Builder().portId("{1-1}").portName("COM3").serialNumber("021976551632").build();
    TICIdentifier identifier2 = new TICIdentifier.Builder().portId("{1-2}").portName("COM3").serialNumber("021976551633").build();
    TICIdentifier identifier3 = new TICIdentifier.Builder().portId("{1-3}").portName("COM3").build();
    TICIdentifier identifier4 = new TICIdentifier.Builder().portName("COM3").build();

    Assert.assertTrue(identifier1.matches(identifier4));
    Assert.assertFalse(identifier1.matches(identifier3));
    Assert.assertFalse(identifier1.matches(identifier2));
  }

  @Test
  public void test_matches_different_portName() {
    TICIdentifier identifier1 = new TICIdentifier.Builder().portName("COM3").build();
    TICIdentifier identifier2 = new TICIdentifier.Builder().portName("COM8").build();

    Assert.assertFalse(identifier1.matches(identifier2));
  }
}
