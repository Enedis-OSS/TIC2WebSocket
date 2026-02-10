// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.io.modem;

import org.junit.Assert;
import org.junit.Test;
import tic.frame.TICMode;
import tic.io.serialport.SerialPortDescriptor;
import tic.io.usb.UsbPortDescriptor;

public class ModemDescriptorTest {

  @Test
  public void test_instance_with_all_parameters() {
    // Given
    ModemDescriptor.Builder<?> builder =
        new ModemDescriptor.Builder<>()
            .portId("ABCD1")
            .portName("/dev/ttyUSB0")
            .description("USB Serial Port")
            .productId((short) 0x6001)
            .vendorId((short) 0x0403)
            .productName("test_product")
            .manufacturer("test_manufacturer")
            .serialNumber("SN123")
            .modemType(ModemType.MICHAUD);
    // When
    ModemDescriptor descriptor = builder.build();

    // Then
    Assert.assertEquals("ABCD1", descriptor.portId());
    Assert.assertEquals("/dev/ttyUSB0", descriptor.portName());
    Assert.assertEquals("USB Serial Port", descriptor.description());
    Assert.assertEquals(Short.valueOf((short) 0x6001), descriptor.productId());
    Assert.assertEquals(Short.valueOf((short) 0x0403), descriptor.vendorId());
    Assert.assertEquals("test_product", descriptor.productName());
    Assert.assertEquals("test_manufacturer", descriptor.manufacturer());
    Assert.assertEquals("SN123", descriptor.serialNumber());
    Assert.assertEquals(ModemType.MICHAUD, descriptor.modemType());
  }

  @Test
  public void test_copy_with_SerialPortDesciptor() {
    // Given
    SerialPortDescriptor serialDescriptor =
        new SerialPortDescriptor.Builder<>()
            .portId("ABCD1")
            .portName("/dev/ttyUSB0")
            .description("USB Serial Port")
            .productId((short) 1)
            .vendorId((short) 2)
            .productName("test_product")
            .manufacturer("test_manufacturer")
            .serialNumber("SN123")
            .build();
    // When
    ModemDescriptor modemDescriptor =
        new ModemDescriptor.Builder<>().copy(serialDescriptor).build();

    // Then
    Assert.assertEquals("ABCD1", modemDescriptor.portId());
    Assert.assertEquals("/dev/ttyUSB0", modemDescriptor.portName());
    Assert.assertEquals("USB Serial Port", modemDescriptor.description());
    Assert.assertEquals(Short.valueOf((short) 1), modemDescriptor.productId());
    Assert.assertEquals(Short.valueOf((short) 2), modemDescriptor.vendorId());
    Assert.assertEquals("test_product", modemDescriptor.productName());
    Assert.assertEquals("test_manufacturer", modemDescriptor.manufacturer());
    Assert.assertEquals("SN123", modemDescriptor.serialNumber());
    Assert.assertEquals(null, modemDescriptor.modemType());
  }

  @Test
  public void test_copy_with_UsbPortDescriptor() {
    // Given
    UsbPortDescriptor usbPortDescriptor =
        new UsbPortDescriptor.Builder()
            .bcdDevice((short) 1)
            .bcdUSB((short) 2)
            .bDescriptorType((byte) 3)
            .bDeviceClass((byte) 4)
            .bDeviceProtocol((byte) 5)
            .bDeviceSubClass((byte) 6)
            .bLength((byte) 7)
            .bMaxPacketSize0((byte) 8)
            .bNumConfigurations((byte) 9)
            .idProduct((short) 10)
            .idVendor((short) 11)
            .iManufacturer((byte) 12)
            .iProduct((byte) 13)
            .iSerialNumber((byte) 14)
            .manufacturer("test_manufacturer")
            .product("test_product")
            .serialNumber("SN123")
            .build();
    // When
    ModemDescriptor modemDescriptor =
        new ModemDescriptor.Builder<>().copy(usbPortDescriptor).build();

    // Then
    Assert.assertEquals(null, modemDescriptor.portId());
    Assert.assertEquals(null, modemDescriptor.portName());
    Assert.assertEquals(null, modemDescriptor.description());
    Assert.assertEquals(Short.valueOf((short) 10), modemDescriptor.productId());
    Assert.assertEquals(Short.valueOf((short) 11), modemDescriptor.vendorId());
    Assert.assertEquals("test_product", modemDescriptor.productName());
    Assert.assertEquals("test_manufacturer", modemDescriptor.manufacturer());
    Assert.assertEquals("SN123", modemDescriptor.serialNumber());
    Assert.assertEquals(null, modemDescriptor.modemType());
  }

  @Test
  public void test_instance_with_null_productId() {
    // Given
    ModemDescriptor.Builder<?> builder =
        new ModemDescriptor.Builder<>()
            .portId("ABCD1")
            .portName("/dev/ttyUSB0")
            .description("USB Serial Port")
            .productId(null)
            .vendorId((short) 0x0403)
            .productName("test_product")
            .manufacturer("test_manufacturer")
            .serialNumber("SN123")
            .modemType(ModemType.MICHAUD);
    // When
    ModemDescriptor descriptor = builder.build();

    // Then
    Assert.assertEquals("ABCD1", descriptor.portId());
    Assert.assertEquals("/dev/ttyUSB0", descriptor.portName());
    Assert.assertEquals("USB Serial Port", descriptor.description());
    Assert.assertEquals(Short.valueOf((short) 0x6001), descriptor.productId());
    Assert.assertEquals(Short.valueOf((short) 0x0403), descriptor.vendorId());
    Assert.assertEquals("test_product", descriptor.productName());
    Assert.assertEquals("test_manufacturer", descriptor.manufacturer());
    Assert.assertEquals("SN123", descriptor.serialNumber());
    Assert.assertEquals(ModemType.MICHAUD, descriptor.modemType());
  }

  @Test
  public void test_instance_with_null_vendorId() {
    // Given
    ModemDescriptor.Builder<?> builder =
        new ModemDescriptor.Builder<>()
            .portId("ABCD1")
            .portName("/dev/ttyUSB0")
            .description("USB Serial Port")
            .productId((short) 0x6001)
            .vendorId(null)
            .productName("test_product")
            .manufacturer("test_manufacturer")
            .serialNumber("SN123")
            .modemType(ModemType.MICHAUD);
    // When
    ModemDescriptor descriptor = builder.build();

    // Then
    Assert.assertEquals("ABCD1", descriptor.portId());
    Assert.assertEquals("/dev/ttyUSB0", descriptor.portName());
    Assert.assertEquals("USB Serial Port", descriptor.description());
    Assert.assertEquals(Short.valueOf((short) 0x6001), descriptor.productId());
    Assert.assertEquals(Short.valueOf((short) 0x0403), descriptor.vendorId());
    Assert.assertEquals("test_product", descriptor.productName());
    Assert.assertEquals("test_manufacturer", descriptor.manufacturer());
    Assert.assertEquals("SN123", descriptor.serialNumber());
    Assert.assertEquals(ModemType.MICHAUD, descriptor.modemType());
  }

  @Test
  public void test_instance_with_null_modemType() {
    // Given
    ModemDescriptor.Builder<?> builder =
        new ModemDescriptor.Builder<>()
            .portId("ABCD1")
            .portName("/dev/ttyUSB0")
            .description("USB Serial Port")
            .productId((short) 0x6001)
            .vendorId((short) 0x0403)
            .productName("test_product")
            .manufacturer("test_manufacturer")
            .serialNumber("SN123")
            .modemType(null);
    // When
    ModemDescriptor descriptor = builder.build();

    // Then
    Assert.assertEquals("ABCD1", descriptor.portId());
    Assert.assertEquals("/dev/ttyUSB0", descriptor.portName());
    Assert.assertEquals("USB Serial Port", descriptor.description());
    Assert.assertEquals(null, descriptor.productId());
    Assert.assertEquals(null, descriptor.vendorId());
    Assert.assertEquals("test_product", descriptor.productName());
    Assert.assertEquals("test_manufacturer", descriptor.manufacturer());
    Assert.assertEquals("SN123", descriptor.serialNumber());
    Assert.assertEquals(null, descriptor.modemType());
  }

  @Test
  public void test_instance_with_missing_productId() {
    // Given
    ModemDescriptor.Builder<?> builder =
        new ModemDescriptor.Builder<>()
            .portId("ABCD1")
            .portName("/dev/ttyUSB0")
            .description("USB Serial Port")
            .vendorId((short) 0x0403)
            .productName("test_product")
            .manufacturer("test_manufacturer")
            .serialNumber("SN123")
            .modemType(ModemType.MICHAUD);
    // When
    ModemDescriptor descriptor = builder.build();

    // Then
    Assert.assertEquals("ABCD1", descriptor.portId());
    Assert.assertEquals("/dev/ttyUSB0", descriptor.portName());
    Assert.assertEquals("USB Serial Port", descriptor.description());
    Assert.assertEquals(Short.valueOf((short) 0x6001), descriptor.productId());
    Assert.assertEquals(Short.valueOf((short) 0x0403), descriptor.vendorId());
    Assert.assertEquals("test_product", descriptor.productName());
    Assert.assertEquals("test_manufacturer", descriptor.manufacturer());
    Assert.assertEquals("SN123", descriptor.serialNumber());
    Assert.assertEquals(ModemType.MICHAUD, descriptor.modemType());
  }

  @Test
  public void test_instance_with_missing_vendorId() {
    // Given
    ModemDescriptor.Builder<?> builder =
        new ModemDescriptor.Builder<>()
            .portId("ABCD1")
            .portName("/dev/ttyUSB0")
            .description("USB Serial Port")
            .productId((short) 0x6001)
            .productName("test_product")
            .manufacturer("test_manufacturer")
            .serialNumber("SN123")
            .modemType(ModemType.MICHAUD);
    // When
    ModemDescriptor descriptor = builder.build();

    // Then
    Assert.assertEquals("ABCD1", descriptor.portId());
    Assert.assertEquals("/dev/ttyUSB0", descriptor.portName());
    Assert.assertEquals("USB Serial Port", descriptor.description());
    Assert.assertEquals(Short.valueOf((short) 0x6001), descriptor.productId());
    Assert.assertEquals(Short.valueOf((short) 0x0403), descriptor.vendorId());
    Assert.assertEquals("test_product", descriptor.productName());
    Assert.assertEquals("test_manufacturer", descriptor.manufacturer());
    Assert.assertEquals("SN123", descriptor.serialNumber());
    Assert.assertEquals(ModemType.MICHAUD, descriptor.modemType());
  }

  @Test
  public void test_instance_with_missing_modemType() {
    // Given
    ModemDescriptor.Builder<?> builder =
        new ModemDescriptor.Builder<>()
            .portId("ABCD1")
            .portName("/dev/ttyUSB0")
            .description("USB Serial Port")
            .productId((short) 1)
            .vendorId((short) 2)
            .productName("test_product")
            .manufacturer("test_manufacturer")
            .serialNumber("SN123");
    // When
    ModemDescriptor descriptor = builder.build();

    // Then
    Assert.assertEquals("ABCD1", descriptor.portId());
    Assert.assertEquals("/dev/ttyUSB0", descriptor.portName());
    Assert.assertEquals("USB Serial Port", descriptor.description());
    Assert.assertEquals(Short.valueOf((short) 1), descriptor.productId());
    Assert.assertEquals(Short.valueOf((short) 2), descriptor.vendorId());
    Assert.assertEquals("test_product", descriptor.productName());
    Assert.assertEquals("test_manufacturer", descriptor.manufacturer());
    Assert.assertEquals("SN123", descriptor.serialNumber());
    Assert.assertEquals(null, descriptor.modemType());
  }

  @Test
  public void test_instance_with_modemType_and_null_productId() {
    // Given
    ModemDescriptor.Builder<?> builder =
        new ModemDescriptor.Builder<>()
            .portId("ABCD1")
            .portName("/dev/ttyUSB0")
            .description("USB Serial Port")
            .modemType(ModemType.MICHAUD)
            .productId(null)
            .vendorId((short) 0x0403)
            .productName("test_product")
            .manufacturer("test_manufacturer")
            .serialNumber("SN123");
    ;
    // When
    IllegalArgumentException exception =
        Assert.assertThrows(IllegalArgumentException.class, () -> builder.build());

    // Then
    Assert.assertEquals(
        "Modem type is specified while productId is not provided", exception.getMessage());
  }

  @Test
  public void test_instance_with_modemType_and_null_vendorId() {
    // Given
    ModemDescriptor.Builder<?> builder =
        new ModemDescriptor.Builder<>()
            .portId("ABCD1")
            .portName("/dev/ttyUSB0")
            .description("USB Serial Port")
            .modemType(ModemType.MICHAUD)
            .productId((short) 0x6001)
            .vendorId(null)
            .productName("test_product")
            .manufacturer("test_manufacturer")
            .serialNumber("SN123");
    ;
    // When
    IllegalArgumentException exception =
        Assert.assertThrows(IllegalArgumentException.class, () -> builder.build());

    // Then
    Assert.assertEquals(
        "Modem type is specified while vendorId is not provided", exception.getMessage());
  }

  @Test
  public void test_instance_with_modemType_and_inconsistent_productId() {
    // Given
    ModemDescriptor.Builder<?> builder =
        new ModemDescriptor.Builder<>()
            .portId("ABCD1")
            .portName("/dev/ttyUSB0")
            .description("USB Serial Port")
            .modemType(ModemType.MICHAUD)
            .productId((short) 0x6015)
            .vendorId((short) 0x0403)
            .productName("test_product")
            .manufacturer("test_manufacturer")
            .serialNumber("SN123");
    ;
    // When
    IllegalArgumentException exception =
        Assert.assertThrows(IllegalArgumentException.class, () -> builder.build());

    // Then
    Assert.assertEquals("Modem type is inconsistent with the productId", exception.getMessage());
  }

  @Test
  public void test_instance_with_modemType_and_inconsistent_vendorId() {
    // Given
    ModemDescriptor.Builder<?> builder =
        new ModemDescriptor.Builder<>()
            .portId("ABCD1")
            .portName("/dev/ttyUSB0")
            .description("USB Serial Port")
            .modemType(ModemType.MICHAUD)
            .productId((short) 0x6001)
            .vendorId((short) 0x0430)
            .productName("test_product")
            .manufacturer("test_manufacturer")
            .serialNumber("SN123");
    ;
    // When
    IllegalArgumentException exception =
        Assert.assertThrows(IllegalArgumentException.class, () -> builder.build());

    // Then
    Assert.assertEquals("Modem type is inconsistent with the vendorId", exception.getMessage());
  }

  @Test
  public void test_equals_with_same_instance() {
    // Given
    ModemDescriptor descriptor =
        new ModemDescriptor.Builder<>()
            .portId("ABCD1")
            .portName("/dev/ttyUSB0")
            .description("USB Serial Port")
            .productName("test_product")
            .manufacturer("test_manufacturer")
            .serialNumber("SN123")
            .modemType(ModemType.MICHAUD)
            .build();
    // When
    boolean match = descriptor.equals(descriptor);

    // Then
    Assert.assertEquals(true, match);
  }

  @Test
  public void test_equals_with_identical_object() {
    // Given
    ModemDescriptor descriptor =
        new ModemDescriptor.Builder<>()
            .portId("ABCD1")
            .portName("/dev/ttyUSB0")
            .description("USB Serial Port")
            .productName("test_product")
            .manufacturer("test_manufacturer")
            .serialNumber("SN123")
            .modemType(ModemType.MICHAUD)
            .build();

    ModemDescriptor otherDescriptor =
        new ModemDescriptor.Builder<>()
            .portId("ABCD1")
            .portName("/dev/ttyUSB0")
            .description("USB Serial Port")
            .productName("test_product")
            .manufacturer("test_manufacturer")
            .serialNumber("SN123")
            .modemType(ModemType.MICHAUD)
            .build();
    // When
    boolean match = descriptor.equals(otherDescriptor);

    // Then
    Assert.assertEquals(true, match);
  }

  @Test
  public void test_equals_with_different_object_type() {
    // Given
    ModemDescriptor descriptor =
        new ModemDescriptor.Builder<>()
            .portId("ABCD1")
            .portName("/dev/ttyUSB0")
            .description("USB Serial Port")
            .productName("test_product")
            .manufacturer("test_manufacturer")
            .serialNumber("SN123")
            .modemType(ModemType.MICHAUD)
            .build();

    // When
    boolean match = descriptor.equals((Object) TICMode.HISTORIC);

    // Then
    Assert.assertEquals(false, match);
  }

  @Test
  public void test_equals_with_different_modemType() {
    // Given
    ModemDescriptor descriptor =
        new ModemDescriptor.Builder<>()
            .portId("ABCD1")
            .portName("/dev/ttyUSB0")
            .description("USB Serial Port")
            .productName("test_product")
            .manufacturer("test_manufacturer")
            .serialNumber("SN123")
            .modemType(ModemType.MICHAUD)
            .build();

    ModemDescriptor otherDescriptor =
        new ModemDescriptor.Builder<>()
            .portId("ABCD1")
            .portName("/dev/ttyUSB0")
            .description("USB Serial Port")
            .productName("test_product")
            .manufacturer("test_manufacturer")
            .serialNumber("SN1234")
            .modemType(ModemType.TELEINFO)
            .build();
    // When
    boolean match = descriptor.equals(otherDescriptor);

    // Then
    Assert.assertEquals(false, match);
  }

  @Test
  public void test_hashCode() {
    // Given
    ModemDescriptor descriptor =
        new ModemDescriptor.Builder<>()
            .portId("ABCD1")
            .portName("/dev/ttyUSB0")
            .description("USB Serial Port")
            .productName("test_product")
            .manufacturer("test_manufacturer")
            .serialNumber("SN123")
            .modemType(ModemType.MICHAUD)
            .build();
    ModemDescriptor sameDescriptor =
        new ModemDescriptor.Builder<>()
            .portId("ABCD1")
            .portName("/dev/ttyUSB0")
            .description("USB Serial Port")
            .productName("test_product")
            .manufacturer("test_manufacturer")
            .serialNumber("SN123")
            .modemType(ModemType.MICHAUD)
            .build();

    // When
    int hashCode = descriptor.hashCode();
    int sameHashCode = sameDescriptor.hashCode();

    // Then
    Assert.assertEquals(sameHashCode, hashCode);
  }
}
