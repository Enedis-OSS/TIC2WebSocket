// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.io.serialport;

import org.junit.Assert;
import org.junit.Test;
import tic.frame.TICMode;

public class SerialPortDescriptorTest {

  @Test
  public void test_instance_with_all_parameters() {
    // Given
    SerialPortDescriptor descriptor = createStandardDescriptor();

    // When

    // Then
    Assert.assertEquals("ABCD1", descriptor.portId());
    Assert.assertEquals("/dev/ttyUSB0", descriptor.portName());
    Assert.assertEquals("USB Serial Port", descriptor.description());
    Assert.assertEquals(Short.valueOf((short) 1), descriptor.productId());
    Assert.assertEquals(Short.valueOf((short) 2), descriptor.vendorId());
    Assert.assertEquals("test_product", descriptor.productName());
    Assert.assertEquals("test_manufacturer", descriptor.manufacturer());
    Assert.assertEquals("SN123", descriptor.serialNumber());
  }

  @Test
  public void test_instance_with_null_portId() {
    // Given
    SerialPortDescriptor.Builder<?> builder =
        new SerialPortDescriptor.Builder<>()
            .portId(null)
            .portName("/dev/ttyUSB0")
            .description("USB Serial Port")
            .productId((short) 1)
            .vendorId((short) 2)
            .productName("test_product")
            .manufacturer("test_manufacturer")
            .serialNumber("SN123");
    // When
    SerialPortDescriptor descriptor = builder.build();

    // Then
    Assert.assertEquals(null, descriptor.portId());
    Assert.assertEquals("/dev/ttyUSB0", descriptor.portName());
    Assert.assertEquals("USB Serial Port", descriptor.description());
    Assert.assertEquals(Short.valueOf((short) 1), descriptor.productId());
    Assert.assertEquals(Short.valueOf((short) 2), descriptor.vendorId());
    Assert.assertEquals("test_product", descriptor.productName());
    Assert.assertEquals("test_manufacturer", descriptor.manufacturer());
    Assert.assertEquals("SN123", descriptor.serialNumber());
  }

  @Test
  public void test_instance_with_null_portName() {
    // Given
    SerialPortDescriptor.Builder<?> builder =
        new SerialPortDescriptor.Builder<>()
            .portId("ABCD1")
            .portName(null)
            .description("USB Serial Port")
            .productId((short) 1)
            .vendorId((short) 2)
            .productName("test_product")
            .manufacturer("test_manufacturer")
            .serialNumber("SN123");
    // When
    SerialPortDescriptor descriptor = builder.build();

    // Then
    Assert.assertEquals("ABCD1", descriptor.portId());
    Assert.assertEquals(null, descriptor.portName());
    Assert.assertEquals("USB Serial Port", descriptor.description());
    Assert.assertEquals(Short.valueOf((short) 1), descriptor.productId());
    Assert.assertEquals(Short.valueOf((short) 2), descriptor.vendorId());
    Assert.assertEquals("test_product", descriptor.productName());
    Assert.assertEquals("test_manufacturer", descriptor.manufacturer());
    Assert.assertEquals("SN123", descriptor.serialNumber());
  }

  @Test
  public void test_instance_with_null_description() {
    // Given
    SerialPortDescriptor.Builder<?> builder =
        new SerialPortDescriptor.Builder<>()
            .portId("ABCD1")
            .portName("/dev/ttyUSB0")
            .description(null)
            .productId((short) 1)
            .vendorId((short) 2)
            .productName("test_product")
            .manufacturer("test_manufacturer")
            .serialNumber("SN123");
    // When
    SerialPortDescriptor descriptor = builder.build();

    // Then
    Assert.assertEquals("ABCD1", descriptor.portId());
    Assert.assertEquals("/dev/ttyUSB0", descriptor.portName());
    Assert.assertEquals(null, descriptor.description());
    Assert.assertEquals(Short.valueOf((short) 1), descriptor.productId());
    Assert.assertEquals(Short.valueOf((short) 2), descriptor.vendorId());
    Assert.assertEquals("test_product", descriptor.productName());
    Assert.assertEquals("test_manufacturer", descriptor.manufacturer());
    Assert.assertEquals("SN123", descriptor.serialNumber());
  }

  @Test
  public void test_instance_with_null_productId() {
    // Given
    SerialPortDescriptor.Builder<?> builder =
        new SerialPortDescriptor.Builder<>()
            .portId("ABCD1")
            .portName("/dev/ttyUSB0")
            .description("USB Serial Port")
            .productId(null)
            .vendorId((short) 2)
            .productName("test_product")
            .manufacturer("test_manufacturer")
            .serialNumber("SN123");
    // When
    SerialPortDescriptor descriptor = builder.build();

    // Then
    Assert.assertEquals("ABCD1", descriptor.portId());
    Assert.assertEquals("/dev/ttyUSB0", descriptor.portName());
    Assert.assertEquals("USB Serial Port", descriptor.description());
    Assert.assertEquals(null, descriptor.productId());
    Assert.assertEquals(Short.valueOf((short) 2), descriptor.vendorId());
    Assert.assertEquals("test_product", descriptor.productName());
    Assert.assertEquals("test_manufacturer", descriptor.manufacturer());
    Assert.assertEquals("SN123", descriptor.serialNumber());
  }

  @Test
  public void test_instance_with_null_vendorId() {
    // Given
    SerialPortDescriptor.Builder<?> builder =
        new SerialPortDescriptor.Builder<>()
            .portId("ABCD1")
            .portName("/dev/ttyUSB0")
            .description("USB Serial Port")
            .productId((short) 1)
            .vendorId(null)
            .productName("test_product")
            .manufacturer("test_manufacturer")
            .serialNumber("SN123");
    // When
    SerialPortDescriptor descriptor = builder.build();

    // Then
    Assert.assertEquals("ABCD1", descriptor.portId());
    Assert.assertEquals("/dev/ttyUSB0", descriptor.portName());
    Assert.assertEquals("USB Serial Port", descriptor.description());
    Assert.assertEquals(Short.valueOf((short) 1), descriptor.productId());
    Assert.assertEquals(null, descriptor.vendorId());
    Assert.assertEquals("test_product", descriptor.productName());
    Assert.assertEquals("test_manufacturer", descriptor.manufacturer());
    Assert.assertEquals("SN123", descriptor.serialNumber());
  }

  @Test
  public void test_instance_with_null_productName() {
    // Given
    SerialPortDescriptor.Builder<?> builder =
        new SerialPortDescriptor.Builder<>()
            .portId("ABCD1")
            .portName("/dev/ttyUSB0")
            .description("USB Serial Port")
            .productId((short) 1)
            .vendorId((short) 2)
            .productName(null)
            .manufacturer("test_manufacturer")
            .serialNumber("SN123");
    // When
    SerialPortDescriptor descriptor = builder.build();

    // Then
    Assert.assertEquals("ABCD1", descriptor.portId());
    Assert.assertEquals("/dev/ttyUSB0", descriptor.portName());
    Assert.assertEquals("USB Serial Port", descriptor.description());
    Assert.assertEquals(Short.valueOf((short) 1), descriptor.productId());
    Assert.assertEquals(Short.valueOf((short) 2), descriptor.vendorId());
    Assert.assertEquals(null, descriptor.productName());
    Assert.assertEquals("test_manufacturer", descriptor.manufacturer());
    Assert.assertEquals("SN123", descriptor.serialNumber());
  }

  @Test
  public void test_instance_with_null_manufacturer() {
    // Given
    SerialPortDescriptor.Builder<?> builder =
        new SerialPortDescriptor.Builder<>()
            .portId("ABCD1")
            .portName("/dev/ttyUSB0")
            .description("USB Serial Port")
            .productId((short) 1)
            .vendorId((short) 2)
            .productName("test_product")
            .manufacturer(null)
            .serialNumber("SN123");
    // When
    SerialPortDescriptor descriptor = builder.build();

    // Then
    Assert.assertEquals("ABCD1", descriptor.portId());
    Assert.assertEquals("/dev/ttyUSB0", descriptor.portName());
    Assert.assertEquals("USB Serial Port", descriptor.description());
    Assert.assertEquals(Short.valueOf((short) 1), descriptor.productId());
    Assert.assertEquals(Short.valueOf((short) 2), descriptor.vendorId());
    Assert.assertEquals("test_product", descriptor.productName());
    Assert.assertEquals(null, descriptor.manufacturer());
    Assert.assertEquals("SN123", descriptor.serialNumber());
  }

  @Test
  public void test_instance_with_null_serialNumber() {
    // Given
    SerialPortDescriptor.Builder<?> builder =
        new SerialPortDescriptor.Builder<>()
            .portId("ABCD1")
            .portName("/dev/ttyUSB0")
            .description("USB Serial Port")
            .productId((short) 1)
            .vendorId((short) 2)
            .productName("test_product")
            .manufacturer("test_manufacturer")
            .serialNumber(null);
    // When
    SerialPortDescriptor descriptor = builder.build();

    // Then
    Assert.assertEquals("ABCD1", descriptor.portId());
    Assert.assertEquals("/dev/ttyUSB0", descriptor.portName());
    Assert.assertEquals("USB Serial Port", descriptor.description());
    Assert.assertEquals(Short.valueOf((short) 1), descriptor.productId());
    Assert.assertEquals(Short.valueOf((short) 2), descriptor.vendorId());
    Assert.assertEquals("test_product", descriptor.productName());
    Assert.assertEquals("test_manufacturer", descriptor.manufacturer());
    Assert.assertEquals(null, descriptor.serialNumber());
  }

  @Test
  public void test_instance_with_missing_portId() {
    // Given
    SerialPortDescriptor.Builder<?> builder =
        new SerialPortDescriptor.Builder<>()
            .portName("/dev/ttyUSB0")
            .description("USB Serial Port")
            .productId((short) 1)
            .vendorId((short) 2)
            .productName("test_product")
            .manufacturer("test_manufacturer")
            .serialNumber("SN123");
    // When
    SerialPortDescriptor descriptor = builder.build();

    // Then
    Assert.assertEquals(null, descriptor.portId());
    Assert.assertEquals("/dev/ttyUSB0", descriptor.portName());
    Assert.assertEquals("USB Serial Port", descriptor.description());
    Assert.assertEquals(Short.valueOf((short) 1), descriptor.productId());
    Assert.assertEquals(Short.valueOf((short) 2), descriptor.vendorId());
    Assert.assertEquals("test_product", descriptor.productName());
    Assert.assertEquals("test_manufacturer", descriptor.manufacturer());
    Assert.assertEquals("SN123", descriptor.serialNumber());
  }

  @Test
  public void test_instance_with_missing_portName() {
    // Given
    SerialPortDescriptor.Builder<?> builder =
        new SerialPortDescriptor.Builder<>()
            .portId("ABCD1")
            .description("USB Serial Port")
            .productId((short) 1)
            .vendorId((short) 2)
            .productName("test_product")
            .manufacturer("test_manufacturer")
            .serialNumber("SN123");
    // When
    SerialPortDescriptor descriptor = builder.build();

    // Then
    Assert.assertEquals("ABCD1", descriptor.portId());
    Assert.assertEquals(null, descriptor.portName());
    Assert.assertEquals("USB Serial Port", descriptor.description());
    Assert.assertEquals(Short.valueOf((short) 1), descriptor.productId());
    Assert.assertEquals(Short.valueOf((short) 2), descriptor.vendorId());
    Assert.assertEquals("test_product", descriptor.productName());
    Assert.assertEquals("test_manufacturer", descriptor.manufacturer());
    Assert.assertEquals("SN123", descriptor.serialNumber());
  }

  @Test
  public void test_instance_with_missing_description() {
    // Given
    SerialPortDescriptor.Builder<?> builder =
        new SerialPortDescriptor.Builder<>()
            .portId("ABCD1")
            .portName("/dev/ttyUSB0")
            .productId((short) 1)
            .vendorId((short) 2)
            .productName("test_product")
            .manufacturer("test_manufacturer")
            .serialNumber("SN123");
    // When
    SerialPortDescriptor descriptor = builder.build();

    // Then
    Assert.assertEquals("ABCD1", descriptor.portId());
    Assert.assertEquals("/dev/ttyUSB0", descriptor.portName());
    Assert.assertEquals(null, descriptor.description());
    Assert.assertEquals(Short.valueOf((short) 1), descriptor.productId());
    Assert.assertEquals(Short.valueOf((short) 2), descriptor.vendorId());
    Assert.assertEquals("test_product", descriptor.productName());
    Assert.assertEquals("test_manufacturer", descriptor.manufacturer());
    Assert.assertEquals("SN123", descriptor.serialNumber());
  }

  @Test
  public void test_instance_with_missing_productId() {
    // Given
    SerialPortDescriptor.Builder<?> builder =
        new SerialPortDescriptor.Builder<>()
            .portId("ABCD1")
            .portName("/dev/ttyUSB0")
            .description("USB Serial Port")
            .vendorId((short) 2)
            .productName("test_product")
            .manufacturer("test_manufacturer")
            .serialNumber("SN123");
    // When
    SerialPortDescriptor descriptor = builder.build();

    // Then
    Assert.assertEquals("ABCD1", descriptor.portId());
    Assert.assertEquals("/dev/ttyUSB0", descriptor.portName());
    Assert.assertEquals("USB Serial Port", descriptor.description());
    Assert.assertEquals(null, descriptor.productId());
    Assert.assertEquals(Short.valueOf((short) 2), descriptor.vendorId());
    Assert.assertEquals("test_product", descriptor.productName());
    Assert.assertEquals("test_manufacturer", descriptor.manufacturer());
    Assert.assertEquals("SN123", descriptor.serialNumber());
  }

  @Test
  public void test_instance_with_missing_vendorId() {
    // Given
    SerialPortDescriptor.Builder<?> builder =
        new SerialPortDescriptor.Builder<>()
            .portId("ABCD1")
            .portName("/dev/ttyUSB0")
            .description("USB Serial Port")
            .productId((short) 1)
            .productName("test_product")
            .manufacturer("test_manufacturer")
            .serialNumber("SN123");
    // When
    SerialPortDescriptor descriptor = builder.build();

    // Then
    Assert.assertEquals("ABCD1", descriptor.portId());
    Assert.assertEquals("/dev/ttyUSB0", descriptor.portName());
    Assert.assertEquals("USB Serial Port", descriptor.description());
    Assert.assertEquals(Short.valueOf((short) 1), descriptor.productId());
    Assert.assertEquals(null, descriptor.vendorId());
    Assert.assertEquals("test_product", descriptor.productName());
    Assert.assertEquals("test_manufacturer", descriptor.manufacturer());
    Assert.assertEquals("SN123", descriptor.serialNumber());
  }

  @Test
  public void test_instance_with_missing_productName() {
    // Given
    SerialPortDescriptor.Builder<?> builder =
        new SerialPortDescriptor.Builder<>()
            .portId("ABCD1")
            .portName("/dev/ttyUSB0")
            .description("USB Serial Port")
            .productId((short) 1)
            .vendorId((short) 2)
            .manufacturer("test_manufacturer")
            .serialNumber("SN123");
    // When
    SerialPortDescriptor descriptor = builder.build();

    // Then
    Assert.assertEquals("ABCD1", descriptor.portId());
    Assert.assertEquals("/dev/ttyUSB0", descriptor.portName());
    Assert.assertEquals("USB Serial Port", descriptor.description());
    Assert.assertEquals(Short.valueOf((short) 1), descriptor.productId());
    Assert.assertEquals(Short.valueOf((short) 2), descriptor.vendorId());
    Assert.assertEquals(null, descriptor.productName());
    Assert.assertEquals("test_manufacturer", descriptor.manufacturer());
    Assert.assertEquals("SN123", descriptor.serialNumber());
  }

  @Test
  public void test_instance_with_missing_manufacturer() {
    // Given
    SerialPortDescriptor.Builder<?> builder =
        new SerialPortDescriptor.Builder<>()
            .portId("ABCD1")
            .portName("/dev/ttyUSB0")
            .description("USB Serial Port")
            .productId((short) 1)
            .vendorId((short) 2)
            .productName("test_product")
            .serialNumber("SN123");
    // When
    SerialPortDescriptor descriptor = builder.build();

    // Then
    Assert.assertEquals("ABCD1", descriptor.portId());
    Assert.assertEquals("/dev/ttyUSB0", descriptor.portName());
    Assert.assertEquals("USB Serial Port", descriptor.description());
    Assert.assertEquals(Short.valueOf((short) 1), descriptor.productId());
    Assert.assertEquals(Short.valueOf((short) 2), descriptor.vendorId());
    Assert.assertEquals("test_product", descriptor.productName());
    Assert.assertEquals(null, descriptor.manufacturer());
    Assert.assertEquals("SN123", descriptor.serialNumber());
  }

  @Test
  public void test_instance_with_missing_serialNumber() {
    // Given
    SerialPortDescriptor.Builder<?> builder =
        new SerialPortDescriptor.Builder<>()
            .portId("ABCD1")
            .portName("/dev/ttyUSB0")
            .description("USB Serial Port")
            .productId((short) 1)
            .vendorId((short) 2)
            .productName("test_product")
            .manufacturer("test_manufacturer");
    // When
    SerialPortDescriptor descriptor = builder.build();

    // Then
    Assert.assertEquals("ABCD1", descriptor.portId());
    Assert.assertEquals("/dev/ttyUSB0", descriptor.portName());
    Assert.assertEquals("USB Serial Port", descriptor.description());
    Assert.assertEquals(Short.valueOf((short) 1), descriptor.productId());
    Assert.assertEquals(Short.valueOf((short) 2), descriptor.vendorId());
    Assert.assertEquals("test_product", descriptor.productName());
    Assert.assertEquals("test_manufacturer", descriptor.manufacturer());
    Assert.assertEquals(null, descriptor.serialNumber());
  }

  @Test
  public void test_instance_with_empty_portId() {
    // Given
    SerialPortDescriptor.Builder<?> builder =
        new SerialPortDescriptor.Builder<>()
            .portId("")
            .portName("/dev/ttyUSB0")
            .description("USB Serial Port")
            .productId((short) 1)
            .vendorId((short) 2)
            .productName("test_product")
            .manufacturer("test_manufacturer")
            .serialNumber("SN123");
    // When
    IllegalArgumentException exception =
        Assert.assertThrows(IllegalArgumentException.class, () -> builder.build());

    // Then
    Assert.assertEquals("Value 'portId' cannot be empty", exception.getMessage());
  }

  @Test
  public void test_instance_with_empty_portName() {
    // Given
    SerialPortDescriptor.Builder<?> builder =
        new SerialPortDescriptor.Builder<>()
            .portId("ABCD1")
            .portName("")
            .description("USB Serial Port")
            .productId((short) 1)
            .vendorId((short) 2)
            .productName("test_product")
            .manufacturer("test_manufacturer")
            .serialNumber("SN123");
    ;
    // When
    IllegalArgumentException exception =
        Assert.assertThrows(IllegalArgumentException.class, () -> builder.build());

    // Then
    Assert.assertEquals("Value 'portName' cannot be empty", exception.getMessage());
  }

  @Test
  public void test_instance_with_empty_description() {
    // Given
    SerialPortDescriptor.Builder<?> builder =
        new SerialPortDescriptor.Builder<>()
            .portId("ABCD1")
            .portName("/dev/ttyUSB0")
            .description("")
            .productId((short) 1)
            .vendorId((short) 2)
            .productName("test_product")
            .manufacturer("test_manufacturer")
            .serialNumber("SN123");
    ;
    // When
    IllegalArgumentException exception =
        Assert.assertThrows(IllegalArgumentException.class, () -> builder.build());

    // Then
    Assert.assertEquals("Value 'description' cannot be empty", exception.getMessage());
  }

  @Test
  public void test_instance_with_empty_productName() {
    // Given
    SerialPortDescriptor.Builder<?> builder =
        new SerialPortDescriptor.Builder<>()
            .portId("ABCD1")
            .portName("/dev/ttyUSB0")
            .description("USB Serial Port")
            .productId((short) 1)
            .vendorId((short) 2)
            .productName("")
            .manufacturer("test_manufacturer")
            .serialNumber("SN123");
    ;
    // When
    IllegalArgumentException exception =
        Assert.assertThrows(IllegalArgumentException.class, () -> builder.build());

    // Then
    Assert.assertEquals("Value 'productName' cannot be empty", exception.getMessage());
  }

  @Test
  public void test_instance_with_empty_manufacturer() {
    // Given
    SerialPortDescriptor.Builder<?> builder =
        new SerialPortDescriptor.Builder<>()
            .portId("ABCD1")
            .portName("/dev/ttyUSB0")
            .description("USB Serial Port")
            .productId((short) 1)
            .vendorId((short) 2)
            .productName("test_product")
            .manufacturer("")
            .serialNumber("SN123");
    ;
    // When
    IllegalArgumentException exception =
        Assert.assertThrows(IllegalArgumentException.class, () -> builder.build());

    // Then
    Assert.assertEquals("Value 'manufacturer' cannot be empty", exception.getMessage());
  }

  @Test
  public void test_instance_with_empty_serialNumber() {
    // Given
    SerialPortDescriptor.Builder<?> builder =
        new SerialPortDescriptor.Builder<>()
            .portId("ABCD1")
            .portName("/dev/ttyUSB0")
            .description("USB Serial Port")
            .productId((short) 1)
            .vendorId((short) 2)
            .productName("test_product")
            .manufacturer("test_manufacturer")
            .serialNumber("");
    ;
    // When
    IllegalArgumentException exception =
        Assert.assertThrows(IllegalArgumentException.class, () -> builder.build());

    // Then
    Assert.assertEquals("Value 'serialNumber' cannot be empty", exception.getMessage());
  }

  @Test
  public void test_equals_with_same_instance() {
    // Given
    SerialPortDescriptor descriptor =
        new SerialPortDescriptor.Builder<>()
            .portId("ABCD1")
            .portName("/dev/ttyUSB0")
            .description("USB Serial Port")
            .productId(null)
            .vendorId((short) 2)
            .productName("test_product")
            .manufacturer("test_manufacturer")
            .serialNumber("SN123")
            .build();
    // When
    boolean match = descriptor.equals(descriptor);

    // Then
    Assert.assertEquals(true, match);
  }

  @Test
  public void test_equals_with_identical_object() {
    // Given
    SerialPortDescriptor descriptor = createStandardDescriptor();

    SerialPortDescriptor otherDescriptor = createStandardDescriptor();
    // When
    boolean match = descriptor.equals(otherDescriptor);

    // Then
    Assert.assertEquals(true, match);
  }

  @Test
  public void test_equals_with_different_object_type() {
    // Given
    SerialPortDescriptor descriptor = createStandardDescriptor();
    // When
    boolean match = descriptor.equals((Object) TICMode.HISTORIC);

    // Then
    Assert.assertEquals(false, match);
  }

  @Test
  public void test_equals_with_different_portId() {
    // Given
    SerialPortDescriptor descriptor = createStandardDescriptor();

    SerialPortDescriptor otherDescriptor =
        new SerialPortDescriptor.Builder<>()
            .portId("ABCD2")
            .portName("/dev/ttyUSB0")
            .description("USB Serial Port")
            .productId((short) 1)
            .vendorId((short) 2)
            .productName("test_product")
            .manufacturer("test_manufacturer")
            .serialNumber("SN123")
            .build();
    // When
    boolean match = descriptor.equals(otherDescriptor);

    // Then
    Assert.assertEquals(false, match);
  }

  @Test
  public void test_equals_with_different_portName() {
    // Given
    SerialPortDescriptor descriptor = createStandardDescriptor();

    SerialPortDescriptor otherDescriptor =
        new SerialPortDescriptor.Builder<>()
            .portId("ABCD1")
            .portName("/dev/ttyUSB1")
            .description("USB Serial Port")
            .productId((short) 1)
            .vendorId((short) 2)
            .productName("test_product")
            .manufacturer("test_manufacturer")
            .serialNumber("SN123")
            .build();
    // When
    boolean match = descriptor.equals(otherDescriptor);

    // Then
    Assert.assertEquals(false, match);
  }

  @Test
  public void test_equals_with_different_description() {
    // Given
    SerialPortDescriptor descriptor = createStandardDescriptor();

    SerialPortDescriptor otherDescriptor =
        new SerialPortDescriptor.Builder<>()
            .portId("ABCD1")
            .portName("/dev/ttyUSB0")
            .description("USB Serial")
            .productId((short) 1)
            .vendorId((short) 2)
            .productName("test_product")
            .manufacturer("test_manufacturer")
            .serialNumber("SN123")
            .build();
    // When
    boolean match = descriptor.equals(otherDescriptor);

    // Then
    Assert.assertEquals(false, match);
  }

  @Test
  public void test_equals_with_different_productId() {
    // Given
    SerialPortDescriptor descriptor = createStandardDescriptor();

    SerialPortDescriptor otherDescriptor =
        new SerialPortDescriptor.Builder<>()
            .portId("ABCD1")
            .portName("/dev/ttyUSB0")
            .description("USB Serial Port")
            .productId((short) 3)
            .vendorId((short) 2)
            .productName("test_product")
            .manufacturer("test_manufacturer")
            .serialNumber("SN123")
            .build();
    // When
    boolean match = descriptor.equals(otherDescriptor);

    // Then
    Assert.assertEquals(false, match);
  }

  @Test
  public void test_equals_with_different_vendorId() {
    // Given
    SerialPortDescriptor descriptor = createStandardDescriptor();

    SerialPortDescriptor otherDescriptor =
        new SerialPortDescriptor.Builder<>()
            .portId("ABCD1")
            .portName("/dev/ttyUSB0")
            .description("USB Serial Port")
            .productId((short) 1)
            .vendorId((short) 3)
            .productName("test_product")
            .manufacturer("test_manufacturer")
            .serialNumber("SN123")
            .build();
    // When
    boolean match = descriptor.equals(otherDescriptor);

    // Then
    Assert.assertEquals(false, match);
  }

  @Test
  public void test_equals_with_different_productName() {
    // Given
    SerialPortDescriptor descriptor = createStandardDescriptor();

    SerialPortDescriptor otherDescriptor =
        new SerialPortDescriptor.Builder<>()
            .portId("ABCD1")
            .portName("/dev/ttyUSB0")
            .description("USB Serial Port")
            .productId((short) 1)
            .vendorId((short) 2)
            .productName("test_product 2")
            .manufacturer("test_manufacturer")
            .serialNumber("SN123")
            .build();
    // When
    boolean match = descriptor.equals(otherDescriptor);

    // Then
    Assert.assertEquals(false, match);
  }

  @Test
  public void test_equals_with_different_manufacturer() {
    // Given
    SerialPortDescriptor descriptor = createStandardDescriptor();

    SerialPortDescriptor otherDescriptor =
        new SerialPortDescriptor.Builder<>()
            .portId("ABCD1")
            .portName("/dev/ttyUSB0")
            .description("USB Serial Port")
            .productId((short) 1)
            .vendorId((short) 2)
            .productName("test_product")
            .manufacturer("test_manufacturer bis")
            .serialNumber("SN123")
            .build();
    // When
    boolean match = descriptor.equals(otherDescriptor);

    // Then
    Assert.assertEquals(false, match);
  }

  @Test
  public void test_equals_with_different_serialNumber() {
    // Given
    SerialPortDescriptor descriptor = createStandardDescriptor();

    SerialPortDescriptor otherDescriptor =
        new SerialPortDescriptor.Builder<>()
            .portId("ABCD1")
            .portName("/dev/ttyUSB0")
            .description("USB Serial Port")
            .productId((short) 1)
            .vendorId((short) 2)
            .productName("test_product")
            .manufacturer("test_manufacturer")
            .serialNumber("SN1234")
            .build();
    // When
    boolean match = descriptor.equals(otherDescriptor);

    // Then
    Assert.assertEquals(false, match);
  }

  @Test
  public void test_hashCode() {
    // Given
    SerialPortDescriptor descriptor = createStandardDescriptor();
    SerialPortDescriptor sameDescriptor = createStandardDescriptor();

    // When
    int hashCode = descriptor.hashCode();
    int sameHashCode = sameDescriptor.hashCode();

    // Then
    Assert.assertEquals(sameHashCode, hashCode);
  }

  @Test
  public void test_isNative_with_only_portName() {
    // Given
    SerialPortDescriptor descriptor =
        new SerialPortDescriptor.Builder<>().portName("/dev/ttyUSB0").build();
    // When
    boolean isNative = descriptor.isNative();

    // Then
    Assert.assertEquals(true, isNative);
  }

  @Test
  public void test_isNative_with_only_portId() {
    // Given
    SerialPortDescriptor descriptor = new SerialPortDescriptor.Builder<>().portId("ABCD1").build();
    // When
    boolean isNative = descriptor.isNative();

    // Then
    Assert.assertEquals(false, isNative);
  }

  @Test
  public void test_isNative_with_portName_and_portId() {
    // Given
    SerialPortDescriptor descriptor =
        new SerialPortDescriptor.Builder<>().portName("/dev/ttyUSB0").portId("ABCD1").build();
    // When
    boolean isNative = descriptor.isNative();

    // Then
    Assert.assertEquals(false, isNative);
  }

  private SerialPortDescriptor createStandardDescriptor() {
    return new SerialPortDescriptor.Builder<>()
        .portId("ABCD1")
        .portName("/dev/ttyUSB0")
        .description("USB Serial Port")
        .productId((short) 1)
        .vendorId((short) 2)
        .productName("test_product")
        .manufacturer("test_manufacturer")
        .serialNumber("SN123")
        .build();
  }
}
