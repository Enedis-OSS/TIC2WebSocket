// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.io.serialport;

import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class SerialPortFinderBaseTest {

  @Test
  public void test_getInstance() {
    // Given

    // When
    SerialPortFinder instance1 = SerialPortFinderBase.getInstance();
    SerialPortFinder instance2 = SerialPortFinderBase.getInstance();

    // Then
    Assert.assertNotNull(instance1);
    Assert.assertNotNull(instance2);
    Assert.assertSame(instance1, instance2);
  }

  @Test
  public void test_findAll() {
    // Given

    // When
    List<SerialPortDescriptor> descriptors = SerialPortFinderBase.getInstance().findAll();

    // Then
    Assert.assertNotNull(descriptors);
  }

  @Test
  public void test_findByPortId() {
    // Given
    String portId = "portId?";

    // When
    SerialPortDescriptor descriptor = SerialPortFinderBase.getInstance().findByPortId(portId);

    // Then
    Assert.assertNull(descriptor);
  }

  @Test
  public void test_findByPortName() {
    // Given
    String portName = "portName?";
    // When
    SerialPortDescriptor descriptor = SerialPortFinderBase.getInstance().findByPortName(portName);

    // Then
    Assert.assertNull(descriptor);
  }

  @Test
  public void test_findByPortIdOrPortName() {
    // Given
    String portId = "portId?";
    String portName = "portName?";
    // When
    SerialPortDescriptor descriptor =
        SerialPortFinderBase.getInstance().findByPortIdOrPortName(portId, portName);

    // Then
    Assert.assertNull(descriptor);
  }

  @Test
  public void test_findNative() {
    // Given
    String portName = "portName?";
    // When
    SerialPortDescriptor descriptor = SerialPortFinderBase.getInstance().findNative(portName);

    // Then
    Assert.assertNull(descriptor);
  }

  @Test
  public void test_findByProductIdAndVendorId() {
    // Given
    short idProduct = 0;
    short idVendor = 0;
    // When
    List<SerialPortDescriptor> descriptors =
        SerialPortFinderBase.getInstance().findByProductIdAndVendorId(idProduct, idVendor);

    // Then
    Assert.assertNotNull(descriptors);
    Assert.assertEquals(0, descriptors.size());
  }
}
