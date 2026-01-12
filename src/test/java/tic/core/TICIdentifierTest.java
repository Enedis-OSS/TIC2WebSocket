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
    TICIdentifier identifier = new TICIdentifier("{1-1}", "COM3", "021976551632");

    Assert.assertEquals("{1-1}", identifier.getPortId());
    Assert.assertEquals("COM3", identifier.getPortName());
    Assert.assertEquals("021976551632", identifier.getSerialNumber());
  }

  @Test
  public void test_constructor_onlyPortId() {
    TICIdentifier identifier = new TICIdentifier("{1-1}", null, null);

    Assert.assertEquals("{1-1}", identifier.getPortId());
    Assert.assertNull(identifier.getPortName());
    Assert.assertNull(identifier.getSerialNumber());
  }

  @Test
  public void test_constructor_onlyPortName() {
    TICIdentifier identifier = new TICIdentifier(null, "COM3", null);

    Assert.assertNull(identifier.getPortId());
    Assert.assertEquals("COM3", identifier.getPortName());
    Assert.assertNull(identifier.getSerialNumber());
  }

  @Test
  public void test_constructor_onlySerialNumber() {
    TICIdentifier identifier = new TICIdentifier(null, null, "021976551632");

    Assert.assertNull(identifier.getPortId());
    Assert.assertNull(identifier.getPortName());
    Assert.assertEquals("021976551632", identifier.getSerialNumber());
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_constructor_empty() {
    new TICIdentifier(null, null, null);
  }

  @Test
  public void test_withSerialNumber() {
    TICIdentifier identifier = new TICIdentifier("{1-1}", "COM3", null);
    TICIdentifier updated = identifier.withSerialNumber("021976551632");

    Assert.assertEquals("{1-1}", updated.getPortId());
    Assert.assertEquals("COM3", updated.getPortName());
    Assert.assertEquals("021976551632", updated.getSerialNumber());
  }

  @Test
  public void test_matches_null() {
    TICIdentifier identifier = new TICIdentifier(null, null, "021976551632");

    Assert.assertFalse(identifier.matches(null));
  }

  @Test
  public void test_matches_same_serialNumber() {
    TICIdentifier identifier1 = new TICIdentifier("{1-1}", "COM3", "021976551632");
    TICIdentifier identifier2 = new TICIdentifier("{1-2}", "COM4", "021976551632");

    Assert.assertTrue(identifier1.matches(identifier2));
  }

  @Test
  public void test_matches_different_serialNumber() {
    TICIdentifier identifier1 = new TICIdentifier("{1-1}", "COM3", "021976551632");
    TICIdentifier identifier2 = new TICIdentifier("{1-1}", "COM3", "021976551638");

    Assert.assertFalse(identifier1.matches(identifier2));
  }

  @Test
  public void test_matches_same_portId() {
    TICIdentifier identifier1 = new TICIdentifier("{1-1}", "COM3", "021976551632");
    TICIdentifier identifier2 = new TICIdentifier("{1-1}", "COM4", "021976551633");
    TICIdentifier identifier3 = new TICIdentifier("{1-1}", "COM5", null);

    Assert.assertTrue(identifier1.matches(identifier3));
    Assert.assertFalse(identifier1.matches(identifier2));
  }

  @Test
  public void test_matches_different_portId() {
    TICIdentifier identifier1 = new TICIdentifier("{1-1}", null, null);
    TICIdentifier identifier2 = new TICIdentifier("{1-7}", null, null);

    Assert.assertFalse(identifier1.matches(identifier2));
  }

  @Test
  public void test_matches_same_portName() {
    TICIdentifier identifier1 = new TICIdentifier("{1-1}", "COM3", "021976551632");
    TICIdentifier identifier2 = new TICIdentifier("{1-2}", "COM3", "021976551633");
    TICIdentifier identifier3 = new TICIdentifier("{1-3}", "COM3", null);
    TICIdentifier identifier4 = new TICIdentifier(null, "COM3", null);

    Assert.assertTrue(identifier1.matches(identifier4));
    Assert.assertFalse(identifier1.matches(identifier3));
    Assert.assertFalse(identifier1.matches(identifier2));
  }

  @Test
  public void test_matches_different_portName() {
    TICIdentifier identifier1 = new TICIdentifier(null, "COM3", null);
    TICIdentifier identifier2 = new TICIdentifier(null, "COM8", null);

    Assert.assertFalse(identifier1.matches(identifier2));
  }
}
