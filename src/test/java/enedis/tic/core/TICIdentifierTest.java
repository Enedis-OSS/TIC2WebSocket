// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.tic.core;

import enedis.lab.types.DataDictionaryException;
import org.junit.Assert;
import org.junit.Test;

public class TICIdentifierTest {
  @Test
  public void test_constructor_full() throws DataDictionaryException {
    TICIdentifier identifier = new TICIdentifier("{1-1}", "COM3", "021976551632");

    Assert.assertEquals("{1-1}", identifier.getPortId());
    Assert.assertEquals("COM3", identifier.getPortName());
    Assert.assertEquals("021976551632", identifier.getSerialNumber());
  }

  @Test
  public void test_constructor_onlyPortId() throws DataDictionaryException {
    TICIdentifier identifier = new TICIdentifier("{1-1}", null, null);

    Assert.assertEquals("{1-1}", identifier.getPortId());
    Assert.assertNull(identifier.getPortName());
    Assert.assertNull(identifier.getSerialNumber());
  }

  @Test
  public void test_constructor_onlyPortName() throws DataDictionaryException {
    TICIdentifier identifier = new TICIdentifier(null, "COM3", null);

    Assert.assertNull(identifier.getPortId());
    Assert.assertEquals("COM3", identifier.getPortName());
    Assert.assertNull(identifier.getSerialNumber());
  }

  @Test
  public void test_constructor_onlySerialNumber() throws DataDictionaryException {
    TICIdentifier identifier = new TICIdentifier(null, null, "021976551632");

    Assert.assertNull(identifier.getPortId());
    Assert.assertNull(identifier.getPortName());
    Assert.assertEquals("021976551632", identifier.getSerialNumber());
  }

  @Test(expected = DataDictionaryException.class)
  public void test_constructor_empty() throws DataDictionaryException {
    new TICIdentifier(null, null, null);
  }

  @Test
  public void test_setPortId_null_notEmpty() throws DataDictionaryException {
    TICIdentifier identifier = new TICIdentifier("{1-1}", null, "021976551632");

    Assert.assertEquals("{1-1}", identifier.getPortId());
    identifier.setPortId(null);
    Assert.assertNull(identifier.getPortId());
  }

  @Test(expected = DataDictionaryException.class)
  public void test_setPortId_null_empty() throws DataDictionaryException {
    TICIdentifier identifier = new TICIdentifier("{1-1}", null, null);

    Assert.assertEquals("{1-1}", identifier.getPortId());
    identifier.setPortId(null);
  }

  @Test
  public void test_setPortName_null_notEmpty() throws DataDictionaryException {
    TICIdentifier identifier = new TICIdentifier(null, "COM3", "021976551632");

    Assert.assertEquals("COM3", identifier.getPortName());
    identifier.setPortName(null);
    Assert.assertNull(identifier.getPortName());
  }

  @Test(expected = DataDictionaryException.class)
  public void test_setPortName_null_empty() throws DataDictionaryException {
    TICIdentifier identifier = new TICIdentifier(null, "COM3", null);

    Assert.assertEquals("COM3", identifier.getPortName());
    identifier.setPortName(null);
  }

  @Test
  public void test_setSerialNumber_null_notEmpty() throws DataDictionaryException {
    TICIdentifier identifier = new TICIdentifier(null, "COM3", "021976551632");

    Assert.assertEquals("021976551632", identifier.getSerialNumber());
    identifier.setSerialNumber(null);
    Assert.assertNull(identifier.getSerialNumber());
  }

  @Test(expected = DataDictionaryException.class)
  public void test_setSerialNumber_null_empty() throws DataDictionaryException {
    TICIdentifier identifier = new TICIdentifier(null, null, "021976551632");

    Assert.assertEquals("021976551632", identifier.getSerialNumber());
    identifier.setSerialNumber(null);
  }

  @Test
  public void test_matches_null() throws DataDictionaryException {
    TICIdentifier identifier = new TICIdentifier(null, null, "021976551632");

    Assert.assertFalse(identifier.matches(null));
  }

  @Test
  public void test_matches_same_serialNumber() throws DataDictionaryException {
    TICIdentifier identifier1 = new TICIdentifier("{1-1}", "COM3", "021976551632");
    TICIdentifier identifier2 = new TICIdentifier("{1-2}", "COM4", "021976551632");

    Assert.assertTrue(identifier1.matches(identifier2));
  }

  @Test
  public void test_matches_different_serialNumber() throws DataDictionaryException {
    TICIdentifier identifier1 = new TICIdentifier("{1-1}", "COM3", "021976551632");
    TICIdentifier identifier2 = new TICIdentifier("{1-1}", "COM3", "021976551638");

    Assert.assertFalse(identifier1.matches(identifier2));
  }

  @Test
  public void test_matches_same_portId() throws DataDictionaryException {
    TICIdentifier identifier1 = new TICIdentifier("{1-1}", "COM3", "021976551632");
    TICIdentifier identifier2 = new TICIdentifier("{1-1}", "COM4", "021976551633");
    TICIdentifier identifier3 = new TICIdentifier("{1-1}", "COM5", null);

    Assert.assertTrue(identifier1.matches(identifier3));
    Assert.assertFalse(identifier1.matches(identifier2));
  }

  @Test
  public void test_matches_different_portId() throws DataDictionaryException {
    TICIdentifier identifier1 = new TICIdentifier("{1-1}", null, null);
    TICIdentifier identifier2 = new TICIdentifier("{1-7}", null, null);

    Assert.assertFalse(identifier1.matches(identifier2));
  }

  @Test
  public void test_matches_same_portName() throws DataDictionaryException {
    TICIdentifier identifier1 = new TICIdentifier("{1-1}", "COM3", "021976551632");
    TICIdentifier identifier2 = new TICIdentifier("{1-2}", "COM3", "021976551633");
    TICIdentifier identifier3 = new TICIdentifier("{1-3}", "COM3", null);
    TICIdentifier identifier4 = new TICIdentifier(null, "COM3", null);

    Assert.assertTrue(identifier1.matches(identifier4));
    Assert.assertFalse(identifier1.matches(identifier3));
    Assert.assertFalse(identifier1.matches(identifier2));
  }

  @Test
  public void test_matches_different_portName() throws DataDictionaryException {
    TICIdentifier identifier1 = new TICIdentifier(null, "COM3", null);
    TICIdentifier identifier2 = new TICIdentifier(null, "COM8", null);

    Assert.assertFalse(identifier1.matches(identifier2));
  }
}
