// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.io.usb;

import org.junit.Assert;
import org.junit.Test;
import tic.frame.TICMode;

public class UsbPortDescriptorTest {

  @Test
  public void test_instance_with_all_parameters() {
    // Given
    UsbPortDescriptor.Builder builder =
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
            .serialNumber("SN123");
    // When
    UsbPortDescriptor descriptor = builder.build();

    // Then
    Assert.assertEquals(1, descriptor.bcdDevice());
    Assert.assertEquals(2, descriptor.bcdUSB());
    Assert.assertEquals(3, descriptor.bDescriptorType());
    Assert.assertEquals(4, descriptor.bDeviceClass());
    Assert.assertEquals(5, descriptor.bDeviceProtocol());
    Assert.assertEquals(6, descriptor.bDeviceSubClass());
    Assert.assertEquals(7, descriptor.bLength());
    Assert.assertEquals(8, descriptor.bMaxPacketSize0());
    Assert.assertEquals(9, descriptor.bNumConfigurations());
    Assert.assertEquals(10, descriptor.idProduct());
    Assert.assertEquals(11, descriptor.idVendor());
    Assert.assertEquals(12, descriptor.iManufacturer());
    Assert.assertEquals(13, descriptor.iProduct());
    Assert.assertEquals(14, descriptor.iSerialNumber());
    Assert.assertEquals("test_manufacturer", descriptor.manufacturer());
    Assert.assertEquals("test_product", descriptor.product());
    Assert.assertEquals("SN123", descriptor.serialNumber());
  }

  @Test
  public void test_instance_with_null_manufacturer() {
    // Given
    UsbPortDescriptor.Builder builder =
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
            .manufacturer(null)
            .product("test_product")
            .serialNumber("SN123");
    // When
    UsbPortDescriptor descriptor = builder.build();

    // Then
    Assert.assertEquals(1, descriptor.bcdDevice());
    Assert.assertEquals(2, descriptor.bcdUSB());
    Assert.assertEquals(3, descriptor.bDescriptorType());
    Assert.assertEquals(4, descriptor.bDeviceClass());
    Assert.assertEquals(5, descriptor.bDeviceProtocol());
    Assert.assertEquals(6, descriptor.bDeviceSubClass());
    Assert.assertEquals(7, descriptor.bLength());
    Assert.assertEquals(8, descriptor.bMaxPacketSize0());
    Assert.assertEquals(9, descriptor.bNumConfigurations());
    Assert.assertEquals(10, descriptor.idProduct());
    Assert.assertEquals(11, descriptor.idVendor());
    Assert.assertEquals(12, descriptor.iManufacturer());
    Assert.assertEquals(13, descriptor.iProduct());
    Assert.assertEquals(14, descriptor.iSerialNumber());
    Assert.assertEquals(null, descriptor.manufacturer());
    Assert.assertEquals("test_product", descriptor.product());
    Assert.assertEquals("SN123", descriptor.serialNumber());
  }

  @Test
  public void test_instance_with_null_product() {
    // Given
    UsbPortDescriptor.Builder builder =
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
            .product(null)
            .serialNumber("SN123");
    // When
    UsbPortDescriptor descriptor = builder.build();

    // Then
    Assert.assertEquals(1, descriptor.bcdDevice());
    Assert.assertEquals(2, descriptor.bcdUSB());
    Assert.assertEquals(3, descriptor.bDescriptorType());
    Assert.assertEquals(4, descriptor.bDeviceClass());
    Assert.assertEquals(5, descriptor.bDeviceProtocol());
    Assert.assertEquals(6, descriptor.bDeviceSubClass());
    Assert.assertEquals(7, descriptor.bLength());
    Assert.assertEquals(8, descriptor.bMaxPacketSize0());
    Assert.assertEquals(9, descriptor.bNumConfigurations());
    Assert.assertEquals(10, descriptor.idProduct());
    Assert.assertEquals(11, descriptor.idVendor());
    Assert.assertEquals(12, descriptor.iManufacturer());
    Assert.assertEquals(13, descriptor.iProduct());
    Assert.assertEquals(14, descriptor.iSerialNumber());
    Assert.assertEquals("test_manufacturer", descriptor.manufacturer());
    Assert.assertEquals(null, descriptor.product());
    Assert.assertEquals("SN123", descriptor.serialNumber());
  }

  @Test
  public void test_instance_with_null_serialNumber() {
    // Given
    UsbPortDescriptor.Builder builder =
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
            .serialNumber(null);
    // When
    UsbPortDescriptor descriptor = builder.build();

    // Then
    Assert.assertEquals(1, descriptor.bcdDevice());
    Assert.assertEquals(2, descriptor.bcdUSB());
    Assert.assertEquals(3, descriptor.bDescriptorType());
    Assert.assertEquals(4, descriptor.bDeviceClass());
    Assert.assertEquals(5, descriptor.bDeviceProtocol());
    Assert.assertEquals(6, descriptor.bDeviceSubClass());
    Assert.assertEquals(7, descriptor.bLength());
    Assert.assertEquals(8, descriptor.bMaxPacketSize0());
    Assert.assertEquals(9, descriptor.bNumConfigurations());
    Assert.assertEquals(10, descriptor.idProduct());
    Assert.assertEquals(11, descriptor.idVendor());
    Assert.assertEquals(12, descriptor.iManufacturer());
    Assert.assertEquals(13, descriptor.iProduct());
    Assert.assertEquals(14, descriptor.iSerialNumber());
    Assert.assertEquals("test_manufacturer", descriptor.manufacturer());
    Assert.assertEquals("test_product", descriptor.product());
    Assert.assertEquals(null, descriptor.serialNumber());
  }

  @Test
  public void test_instance_with_missing_bcdDevice() {
    // Given
    UsbPortDescriptor.Builder builder =
        new UsbPortDescriptor.Builder()
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
            .serialNumber("SN123");
    // When
    UsbPortDescriptor descriptor = builder.build();

    // Then
    Assert.assertEquals(0, descriptor.bcdDevice());
    Assert.assertEquals(2, descriptor.bcdUSB());
    Assert.assertEquals(3, descriptor.bDescriptorType());
    Assert.assertEquals(4, descriptor.bDeviceClass());
    Assert.assertEquals(5, descriptor.bDeviceProtocol());
    Assert.assertEquals(6, descriptor.bDeviceSubClass());
    Assert.assertEquals(7, descriptor.bLength());
    Assert.assertEquals(8, descriptor.bMaxPacketSize0());
    Assert.assertEquals(9, descriptor.bNumConfigurations());
    Assert.assertEquals(10, descriptor.idProduct());
    Assert.assertEquals(11, descriptor.idVendor());
    Assert.assertEquals(12, descriptor.iManufacturer());
    Assert.assertEquals(13, descriptor.iProduct());
    Assert.assertEquals(14, descriptor.iSerialNumber());
    Assert.assertEquals("test_manufacturer", descriptor.manufacturer());
    Assert.assertEquals("test_product", descriptor.product());
    Assert.assertEquals("SN123", descriptor.serialNumber());
  }

  @Test
  public void test_instance_with_missing_bcdUSB() {
    // Given
    UsbPortDescriptor.Builder builder =
        new UsbPortDescriptor.Builder()
            .bcdDevice((short) 1)
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
            .serialNumber("SN123");
    // When
    UsbPortDescriptor descriptor = builder.build();

    // Then
    Assert.assertEquals(1, descriptor.bcdDevice());
    Assert.assertEquals(0, descriptor.bcdUSB());
    Assert.assertEquals(3, descriptor.bDescriptorType());
    Assert.assertEquals(4, descriptor.bDeviceClass());
    Assert.assertEquals(5, descriptor.bDeviceProtocol());
    Assert.assertEquals(6, descriptor.bDeviceSubClass());
    Assert.assertEquals(7, descriptor.bLength());
    Assert.assertEquals(8, descriptor.bMaxPacketSize0());
    Assert.assertEquals(9, descriptor.bNumConfigurations());
    Assert.assertEquals(10, descriptor.idProduct());
    Assert.assertEquals(11, descriptor.idVendor());
    Assert.assertEquals(12, descriptor.iManufacturer());
    Assert.assertEquals(13, descriptor.iProduct());
    Assert.assertEquals(14, descriptor.iSerialNumber());
    Assert.assertEquals("test_manufacturer", descriptor.manufacturer());
    Assert.assertEquals("test_product", descriptor.product());
    Assert.assertEquals("SN123", descriptor.serialNumber());
  }

  @Test
  public void test_instance_with_missing_bDescriptorType() {
    // Given
    UsbPortDescriptor.Builder builder =
        new UsbPortDescriptor.Builder()
            .bcdDevice((short) 1)
            .bcdUSB((short) 2)
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
            .serialNumber("SN123");
    // When
    UsbPortDescriptor descriptor = builder.build();

    // Then
    Assert.assertEquals(1, descriptor.bcdDevice());
    Assert.assertEquals(2, descriptor.bcdUSB());
    Assert.assertEquals(0, descriptor.bDescriptorType());
    Assert.assertEquals(4, descriptor.bDeviceClass());
    Assert.assertEquals(5, descriptor.bDeviceProtocol());
    Assert.assertEquals(6, descriptor.bDeviceSubClass());
    Assert.assertEquals(7, descriptor.bLength());
    Assert.assertEquals(8, descriptor.bMaxPacketSize0());
    Assert.assertEquals(9, descriptor.bNumConfigurations());
    Assert.assertEquals(10, descriptor.idProduct());
    Assert.assertEquals(11, descriptor.idVendor());
    Assert.assertEquals(12, descriptor.iManufacturer());
    Assert.assertEquals(13, descriptor.iProduct());
    Assert.assertEquals(14, descriptor.iSerialNumber());
    Assert.assertEquals("test_manufacturer", descriptor.manufacturer());
    Assert.assertEquals("test_product", descriptor.product());
    Assert.assertEquals("SN123", descriptor.serialNumber());
  }

  @Test
  public void test_instance_with_missing_bDeviceClass() {
    // Given
    UsbPortDescriptor.Builder builder =
        new UsbPortDescriptor.Builder()
            .bcdDevice((short) 1)
            .bcdUSB((short) 2)
            .bDescriptorType((byte) 3)
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
            .serialNumber("SN123");
    // When
    UsbPortDescriptor descriptor = builder.build();

    // Then
    Assert.assertEquals(1, descriptor.bcdDevice());
    Assert.assertEquals(2, descriptor.bcdUSB());
    Assert.assertEquals(3, descriptor.bDescriptorType());
    Assert.assertEquals(0, descriptor.bDeviceClass());
    Assert.assertEquals(5, descriptor.bDeviceProtocol());
    Assert.assertEquals(6, descriptor.bDeviceSubClass());
    Assert.assertEquals(7, descriptor.bLength());
    Assert.assertEquals(8, descriptor.bMaxPacketSize0());
    Assert.assertEquals(9, descriptor.bNumConfigurations());
    Assert.assertEquals(10, descriptor.idProduct());
    Assert.assertEquals(11, descriptor.idVendor());
    Assert.assertEquals(12, descriptor.iManufacturer());
    Assert.assertEquals(13, descriptor.iProduct());
    Assert.assertEquals(14, descriptor.iSerialNumber());
    Assert.assertEquals("test_manufacturer", descriptor.manufacturer());
    Assert.assertEquals("test_product", descriptor.product());
    Assert.assertEquals("SN123", descriptor.serialNumber());
  }

  @Test
  public void test_instance_with_missing_bDeviceProtocol() {
    // Given
    UsbPortDescriptor.Builder builder =
        new UsbPortDescriptor.Builder()
            .bcdDevice((short) 1)
            .bcdUSB((short) 2)
            .bDescriptorType((byte) 3)
            .bDeviceClass((byte) 4)
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
            .serialNumber("SN123");
    // When
    UsbPortDescriptor descriptor = builder.build();

    // Then
    Assert.assertEquals(1, descriptor.bcdDevice());
    Assert.assertEquals(2, descriptor.bcdUSB());
    Assert.assertEquals(3, descriptor.bDescriptorType());
    Assert.assertEquals(4, descriptor.bDeviceClass());
    Assert.assertEquals(0, descriptor.bDeviceProtocol());
    Assert.assertEquals(6, descriptor.bDeviceSubClass());
    Assert.assertEquals(7, descriptor.bLength());
    Assert.assertEquals(8, descriptor.bMaxPacketSize0());
    Assert.assertEquals(9, descriptor.bNumConfigurations());
    Assert.assertEquals(10, descriptor.idProduct());
    Assert.assertEquals(11, descriptor.idVendor());
    Assert.assertEquals(12, descriptor.iManufacturer());
    Assert.assertEquals(13, descriptor.iProduct());
    Assert.assertEquals(14, descriptor.iSerialNumber());
    Assert.assertEquals("test_manufacturer", descriptor.manufacturer());
    Assert.assertEquals("test_product", descriptor.product());
    Assert.assertEquals("SN123", descriptor.serialNumber());
  }

  @Test
  public void test_instance_with_missing_bDeviceSubClass() {
    // Given
    UsbPortDescriptor.Builder builder =
        new UsbPortDescriptor.Builder()
            .bcdDevice((short) 1)
            .bcdUSB((short) 2)
            .bDescriptorType((byte) 3)
            .bDeviceClass((byte) 4)
            .bDeviceProtocol((byte) 5)
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
            .serialNumber("SN123");
    // When
    UsbPortDescriptor descriptor = builder.build();

    // Then
    Assert.assertEquals(1, descriptor.bcdDevice());
    Assert.assertEquals(2, descriptor.bcdUSB());
    Assert.assertEquals(3, descriptor.bDescriptorType());
    Assert.assertEquals(4, descriptor.bDeviceClass());
    Assert.assertEquals(5, descriptor.bDeviceProtocol());
    Assert.assertEquals(0, descriptor.bDeviceSubClass());
    Assert.assertEquals(7, descriptor.bLength());
    Assert.assertEquals(8, descriptor.bMaxPacketSize0());
    Assert.assertEquals(9, descriptor.bNumConfigurations());
    Assert.assertEquals(10, descriptor.idProduct());
    Assert.assertEquals(11, descriptor.idVendor());
    Assert.assertEquals(12, descriptor.iManufacturer());
    Assert.assertEquals(13, descriptor.iProduct());
    Assert.assertEquals(14, descriptor.iSerialNumber());
    Assert.assertEquals("test_manufacturer", descriptor.manufacturer());
    Assert.assertEquals("test_product", descriptor.product());
    Assert.assertEquals("SN123", descriptor.serialNumber());
  }

  @Test
  public void test_instance_with_missing_bLength() {
    // Given
    UsbPortDescriptor.Builder builder =
        new UsbPortDescriptor.Builder()
            .bcdDevice((short) 1)
            .bcdUSB((short) 2)
            .bDescriptorType((byte) 3)
            .bDeviceClass((byte) 4)
            .bDeviceProtocol((byte) 5)
            .bDeviceSubClass((byte) 6)
            .bMaxPacketSize0((byte) 8)
            .bNumConfigurations((byte) 9)
            .idProduct((short) 10)
            .idVendor((short) 11)
            .iManufacturer((byte) 12)
            .iProduct((byte) 13)
            .iSerialNumber((byte) 14)
            .manufacturer("test_manufacturer")
            .product("test_product")
            .serialNumber("SN123");
    // When
    UsbPortDescriptor descriptor = builder.build();

    // Then
    Assert.assertEquals(1, descriptor.bcdDevice());
    Assert.assertEquals(2, descriptor.bcdUSB());
    Assert.assertEquals(3, descriptor.bDescriptorType());
    Assert.assertEquals(4, descriptor.bDeviceClass());
    Assert.assertEquals(5, descriptor.bDeviceProtocol());
    Assert.assertEquals(6, descriptor.bDeviceSubClass());
    Assert.assertEquals(0, descriptor.bLength());
    Assert.assertEquals(8, descriptor.bMaxPacketSize0());
    Assert.assertEquals(9, descriptor.bNumConfigurations());
    Assert.assertEquals(10, descriptor.idProduct());
    Assert.assertEquals(11, descriptor.idVendor());
    Assert.assertEquals(12, descriptor.iManufacturer());
    Assert.assertEquals(13, descriptor.iProduct());
    Assert.assertEquals(14, descriptor.iSerialNumber());
    Assert.assertEquals("test_manufacturer", descriptor.manufacturer());
    Assert.assertEquals("test_product", descriptor.product());
    Assert.assertEquals("SN123", descriptor.serialNumber());
  }

  @Test
  public void test_instance_with_missing_bMaxPacketSize0() {
    // Given
    UsbPortDescriptor.Builder builder =
        new UsbPortDescriptor.Builder()
            .bcdDevice((short) 1)
            .bcdUSB((short) 2)
            .bDescriptorType((byte) 3)
            .bDeviceClass((byte) 4)
            .bDeviceProtocol((byte) 5)
            .bDeviceSubClass((byte) 6)
            .bLength((byte) 7)
            .bNumConfigurations((byte) 9)
            .idProduct((short) 10)
            .idVendor((short) 11)
            .iManufacturer((byte) 12)
            .iProduct((byte) 13)
            .iSerialNumber((byte) 14)
            .manufacturer("test_manufacturer")
            .product("test_product")
            .serialNumber("SN123");
    // When
    UsbPortDescriptor descriptor = builder.build();

    // Then
    Assert.assertEquals(1, descriptor.bcdDevice());
    Assert.assertEquals(2, descriptor.bcdUSB());
    Assert.assertEquals(3, descriptor.bDescriptorType());
    Assert.assertEquals(4, descriptor.bDeviceClass());
    Assert.assertEquals(5, descriptor.bDeviceProtocol());
    Assert.assertEquals(6, descriptor.bDeviceSubClass());
    Assert.assertEquals(7, descriptor.bLength());
    Assert.assertEquals(0, descriptor.bMaxPacketSize0());
    Assert.assertEquals(9, descriptor.bNumConfigurations());
    Assert.assertEquals(10, descriptor.idProduct());
    Assert.assertEquals(11, descriptor.idVendor());
    Assert.assertEquals(12, descriptor.iManufacturer());
    Assert.assertEquals(13, descriptor.iProduct());
    Assert.assertEquals(14, descriptor.iSerialNumber());
    Assert.assertEquals("test_manufacturer", descriptor.manufacturer());
    Assert.assertEquals("test_product", descriptor.product());
    Assert.assertEquals("SN123", descriptor.serialNumber());
  }

  @Test
  public void test_instance_with_missing_bNumConfigurations() {
    // Given
    UsbPortDescriptor.Builder builder =
        new UsbPortDescriptor.Builder()
            .bcdDevice((short) 1)
            .bcdUSB((short) 2)
            .bDescriptorType((byte) 3)
            .bDeviceClass((byte) 4)
            .bDeviceProtocol((byte) 5)
            .bDeviceSubClass((byte) 6)
            .bLength((byte) 7)
            .bMaxPacketSize0((byte) 8)
            .idProduct((short) 10)
            .idVendor((short) 11)
            .iManufacturer((byte) 12)
            .iProduct((byte) 13)
            .iSerialNumber((byte) 14)
            .manufacturer("test_manufacturer")
            .product("test_product")
            .serialNumber("SN123");
    // When
    UsbPortDescriptor descriptor = builder.build();

    // Then
    Assert.assertEquals(1, descriptor.bcdDevice());
    Assert.assertEquals(2, descriptor.bcdUSB());
    Assert.assertEquals(3, descriptor.bDescriptorType());
    Assert.assertEquals(4, descriptor.bDeviceClass());
    Assert.assertEquals(5, descriptor.bDeviceProtocol());
    Assert.assertEquals(6, descriptor.bDeviceSubClass());
    Assert.assertEquals(7, descriptor.bLength());
    Assert.assertEquals(8, descriptor.bMaxPacketSize0());
    Assert.assertEquals(0, descriptor.bNumConfigurations());
    Assert.assertEquals(10, descriptor.idProduct());
    Assert.assertEquals(11, descriptor.idVendor());
    Assert.assertEquals(12, descriptor.iManufacturer());
    Assert.assertEquals(13, descriptor.iProduct());
    Assert.assertEquals(14, descriptor.iSerialNumber());
    Assert.assertEquals("test_manufacturer", descriptor.manufacturer());
    Assert.assertEquals("test_product", descriptor.product());
    Assert.assertEquals("SN123", descriptor.serialNumber());
  }

  @Test
  public void test_instance_with_missing_idProduct() {
    // Given
    UsbPortDescriptor.Builder builder =
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
            .idVendor((short) 11)
            .iManufacturer((byte) 12)
            .iProduct((byte) 13)
            .iSerialNumber((byte) 14)
            .manufacturer("test_manufacturer")
            .product("test_product")
            .serialNumber("SN123");
    // When
    UsbPortDescriptor descriptor = builder.build();

    // Then
    Assert.assertEquals(1, descriptor.bcdDevice());
    Assert.assertEquals(2, descriptor.bcdUSB());
    Assert.assertEquals(3, descriptor.bDescriptorType());
    Assert.assertEquals(4, descriptor.bDeviceClass());
    Assert.assertEquals(5, descriptor.bDeviceProtocol());
    Assert.assertEquals(6, descriptor.bDeviceSubClass());
    Assert.assertEquals(7, descriptor.bLength());
    Assert.assertEquals(8, descriptor.bMaxPacketSize0());
    Assert.assertEquals(9, descriptor.bNumConfigurations());
    Assert.assertEquals(0, descriptor.idProduct());
    Assert.assertEquals(11, descriptor.idVendor());
    Assert.assertEquals(12, descriptor.iManufacturer());
    Assert.assertEquals(13, descriptor.iProduct());
    Assert.assertEquals(14, descriptor.iSerialNumber());
    Assert.assertEquals("test_manufacturer", descriptor.manufacturer());
    Assert.assertEquals("test_product", descriptor.product());
    Assert.assertEquals("SN123", descriptor.serialNumber());
  }

  @Test
  public void test_instance_with_missing_idVendor() {
    // Given
    UsbPortDescriptor.Builder builder =
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
            .iManufacturer((byte) 12)
            .iProduct((byte) 13)
            .iSerialNumber((byte) 14)
            .manufacturer("test_manufacturer")
            .product("test_product")
            .serialNumber("SN123");
    // When
    UsbPortDescriptor descriptor = builder.build();

    // Then
    Assert.assertEquals(1, descriptor.bcdDevice());
    Assert.assertEquals(2, descriptor.bcdUSB());
    Assert.assertEquals(3, descriptor.bDescriptorType());
    Assert.assertEquals(4, descriptor.bDeviceClass());
    Assert.assertEquals(5, descriptor.bDeviceProtocol());
    Assert.assertEquals(6, descriptor.bDeviceSubClass());
    Assert.assertEquals(7, descriptor.bLength());
    Assert.assertEquals(8, descriptor.bMaxPacketSize0());
    Assert.assertEquals(9, descriptor.bNumConfigurations());
    Assert.assertEquals(10, descriptor.idProduct());
    Assert.assertEquals(0, descriptor.idVendor());
    Assert.assertEquals(12, descriptor.iManufacturer());
    Assert.assertEquals(13, descriptor.iProduct());
    Assert.assertEquals(14, descriptor.iSerialNumber());
    Assert.assertEquals("test_manufacturer", descriptor.manufacturer());
    Assert.assertEquals("test_product", descriptor.product());
    Assert.assertEquals("SN123", descriptor.serialNumber());
  }

  @Test
  public void test_instance_with_missing_iManufacturer() {
    // Given
    UsbPortDescriptor.Builder builder =
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
            .iProduct((byte) 13)
            .iSerialNumber((byte) 14)
            .manufacturer("test_manufacturer")
            .product("test_product")
            .serialNumber("SN123");
    // When
    UsbPortDescriptor descriptor = builder.build();

    // Then
    Assert.assertEquals(1, descriptor.bcdDevice());
    Assert.assertEquals(2, descriptor.bcdUSB());
    Assert.assertEquals(3, descriptor.bDescriptorType());
    Assert.assertEquals(4, descriptor.bDeviceClass());
    Assert.assertEquals(5, descriptor.bDeviceProtocol());
    Assert.assertEquals(6, descriptor.bDeviceSubClass());
    Assert.assertEquals(7, descriptor.bLength());
    Assert.assertEquals(8, descriptor.bMaxPacketSize0());
    Assert.assertEquals(9, descriptor.bNumConfigurations());
    Assert.assertEquals(10, descriptor.idProduct());
    Assert.assertEquals(11, descriptor.idVendor());
    Assert.assertEquals(0, descriptor.iManufacturer());
    Assert.assertEquals(13, descriptor.iProduct());
    Assert.assertEquals(14, descriptor.iSerialNumber());
    Assert.assertEquals("test_manufacturer", descriptor.manufacturer());
    Assert.assertEquals("test_product", descriptor.product());
    Assert.assertEquals("SN123", descriptor.serialNumber());
  }

  @Test
  public void test_instance_with_missing_iProduct() {
    // Given
    UsbPortDescriptor.Builder builder =
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
            .iSerialNumber((byte) 14)
            .manufacturer("test_manufacturer")
            .product("test_product")
            .serialNumber("SN123");
    // When
    UsbPortDescriptor descriptor = builder.build();

    // Then
    Assert.assertEquals(1, descriptor.bcdDevice());
    Assert.assertEquals(2, descriptor.bcdUSB());
    Assert.assertEquals(3, descriptor.bDescriptorType());
    Assert.assertEquals(4, descriptor.bDeviceClass());
    Assert.assertEquals(5, descriptor.bDeviceProtocol());
    Assert.assertEquals(6, descriptor.bDeviceSubClass());
    Assert.assertEquals(7, descriptor.bLength());
    Assert.assertEquals(8, descriptor.bMaxPacketSize0());
    Assert.assertEquals(9, descriptor.bNumConfigurations());
    Assert.assertEquals(10, descriptor.idProduct());
    Assert.assertEquals(11, descriptor.idVendor());
    Assert.assertEquals(12, descriptor.iManufacturer());
    Assert.assertEquals(0, descriptor.iProduct());
    Assert.assertEquals(14, descriptor.iSerialNumber());
    Assert.assertEquals("test_manufacturer", descriptor.manufacturer());
    Assert.assertEquals("test_product", descriptor.product());
    Assert.assertEquals("SN123", descriptor.serialNumber());
  }

  @Test
  public void test_instance_with_missing_iSerialNumber() {
    // Given
    UsbPortDescriptor.Builder builder =
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
            .manufacturer("test_manufacturer")
            .product("test_product")
            .serialNumber("SN123");
    // When
    UsbPortDescriptor descriptor = builder.build();

    // Then
    Assert.assertEquals(1, descriptor.bcdDevice());
    Assert.assertEquals(2, descriptor.bcdUSB());
    Assert.assertEquals(3, descriptor.bDescriptorType());
    Assert.assertEquals(4, descriptor.bDeviceClass());
    Assert.assertEquals(5, descriptor.bDeviceProtocol());
    Assert.assertEquals(6, descriptor.bDeviceSubClass());
    Assert.assertEquals(7, descriptor.bLength());
    Assert.assertEquals(8, descriptor.bMaxPacketSize0());
    Assert.assertEquals(9, descriptor.bNumConfigurations());
    Assert.assertEquals(10, descriptor.idProduct());
    Assert.assertEquals(11, descriptor.idVendor());
    Assert.assertEquals(12, descriptor.iManufacturer());
    Assert.assertEquals(13, descriptor.iProduct());
    Assert.assertEquals(0, descriptor.iSerialNumber());
    Assert.assertEquals("test_manufacturer", descriptor.manufacturer());
    Assert.assertEquals("test_product", descriptor.product());
    Assert.assertEquals("SN123", descriptor.serialNumber());
  }

  @Test
  public void test_instance_with_missing_manufacturer() {
    // Given
    UsbPortDescriptor.Builder builder =
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
            .product("test_product")
            .serialNumber("SN123");
    // When
    UsbPortDescriptor descriptor = builder.build();

    // Then
    Assert.assertEquals(1, descriptor.bcdDevice());
    Assert.assertEquals(2, descriptor.bcdUSB());
    Assert.assertEquals(3, descriptor.bDescriptorType());
    Assert.assertEquals(4, descriptor.bDeviceClass());
    Assert.assertEquals(5, descriptor.bDeviceProtocol());
    Assert.assertEquals(6, descriptor.bDeviceSubClass());
    Assert.assertEquals(7, descriptor.bLength());
    Assert.assertEquals(8, descriptor.bMaxPacketSize0());
    Assert.assertEquals(9, descriptor.bNumConfigurations());
    Assert.assertEquals(10, descriptor.idProduct());
    Assert.assertEquals(11, descriptor.idVendor());
    Assert.assertEquals(12, descriptor.iManufacturer());
    Assert.assertEquals(13, descriptor.iProduct());
    Assert.assertEquals(14, descriptor.iSerialNumber());
    Assert.assertEquals(null, descriptor.manufacturer());
    Assert.assertEquals("test_product", descriptor.product());
    Assert.assertEquals("SN123", descriptor.serialNumber());
  }

  @Test
  public void test_instance_with_missing_product() {
    // Given
    UsbPortDescriptor.Builder builder =
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
            .serialNumber("SN123");
    // When
    UsbPortDescriptor descriptor = builder.build();

    // Then
    Assert.assertEquals(1, descriptor.bcdDevice());
    Assert.assertEquals(2, descriptor.bcdUSB());
    Assert.assertEquals(3, descriptor.bDescriptorType());
    Assert.assertEquals(4, descriptor.bDeviceClass());
    Assert.assertEquals(5, descriptor.bDeviceProtocol());
    Assert.assertEquals(6, descriptor.bDeviceSubClass());
    Assert.assertEquals(7, descriptor.bLength());
    Assert.assertEquals(8, descriptor.bMaxPacketSize0());
    Assert.assertEquals(9, descriptor.bNumConfigurations());
    Assert.assertEquals(10, descriptor.idProduct());
    Assert.assertEquals(11, descriptor.idVendor());
    Assert.assertEquals(12, descriptor.iManufacturer());
    Assert.assertEquals(13, descriptor.iProduct());
    Assert.assertEquals(14, descriptor.iSerialNumber());
    Assert.assertEquals("test_manufacturer", descriptor.manufacturer());
    Assert.assertEquals(null, descriptor.product());
    Assert.assertEquals("SN123", descriptor.serialNumber());
  }

  @Test
  public void test_instance_with_missing_serialNumber() {
    // Given
    UsbPortDescriptor.Builder builder =
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
            .product("test_product");
    // When
    UsbPortDescriptor descriptor = builder.build();

    // Then
    Assert.assertEquals(1, descriptor.bcdDevice());
    Assert.assertEquals(2, descriptor.bcdUSB());
    Assert.assertEquals(3, descriptor.bDescriptorType());
    Assert.assertEquals(4, descriptor.bDeviceClass());
    Assert.assertEquals(5, descriptor.bDeviceProtocol());
    Assert.assertEquals(6, descriptor.bDeviceSubClass());
    Assert.assertEquals(7, descriptor.bLength());
    Assert.assertEquals(8, descriptor.bMaxPacketSize0());
    Assert.assertEquals(9, descriptor.bNumConfigurations());
    Assert.assertEquals(10, descriptor.idProduct());
    Assert.assertEquals(11, descriptor.idVendor());
    Assert.assertEquals(12, descriptor.iManufacturer());
    Assert.assertEquals(13, descriptor.iProduct());
    Assert.assertEquals(14, descriptor.iSerialNumber());
    Assert.assertEquals("test_manufacturer", descriptor.manufacturer());
    Assert.assertEquals("test_product", descriptor.product());
    Assert.assertEquals(null, descriptor.serialNumber());
  }

  @Test
  public void test_instance_with_empty_manufacturer() {
    // Given
    UsbPortDescriptor.Builder builder =
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
            .manufacturer("")
            .product("test_product")
            .serialNumber("SN123");
    // When
    IllegalArgumentException exception =
        Assert.assertThrows(IllegalArgumentException.class, () -> builder.build());

    // Then
    Assert.assertEquals("Value 'manufacturer' cannot be empty", exception.getMessage());
  }

  @Test
  public void test_instance_with_empty_product() {
    // Given
    UsbPortDescriptor.Builder builder =
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
            .product("")
            .serialNumber("SN123");
    // When
    IllegalArgumentException exception =
        Assert.assertThrows(IllegalArgumentException.class, () -> builder.build());

    // Then
    Assert.assertEquals("Value 'product' cannot be empty", exception.getMessage());
  }

  @Test
  public void test_instance_with_empty_serialNumber() {
    // Given
    UsbPortDescriptor.Builder builder =
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
            .serialNumber("");
    // When
    IllegalArgumentException exception =
        Assert.assertThrows(IllegalArgumentException.class, () -> builder.build());

    // Then
    Assert.assertEquals("Value 'serialNumber' cannot be empty", exception.getMessage());
  }

  @Test
  public void test_equals_with_same_instance() {
    // Given
    UsbPortDescriptor descriptor =
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
    boolean match = descriptor.equals(descriptor);

    // Then
    Assert.assertEquals(true, match);
  }

  @Test
  public void test_equals_with_identical_object() {
    // Given
    UsbPortDescriptor descriptor =
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

    UsbPortDescriptor otherDescriptor =
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
    boolean match = descriptor.equals(otherDescriptor);

    // Then
    Assert.assertEquals(true, match);
  }

  @Test
  public void test_equals_with_different_object_type() {
    // Given
    UsbPortDescriptor descriptor =
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
    boolean match = descriptor.equals((Object) TICMode.HISTORIC);

    // Then
    Assert.assertEquals(false, match);
  }

  @Test
  public void test_equals_with_different_bcdDevice() {
    // Given
    UsbPortDescriptor descriptor =
        new UsbPortDescriptor.Builder()
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

    UsbPortDescriptor otherDescriptor =
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
    boolean match = descriptor.equals(otherDescriptor);

    // Then
    Assert.assertEquals(false, match);
  }

  @Test
  public void test_equals_with_different_bcdUSB() {
    // Given
    UsbPortDescriptor descriptor =
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

    UsbPortDescriptor otherDescriptor =
        new UsbPortDescriptor.Builder()
            .bcdDevice((short) 1)
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
    boolean match = descriptor.equals(otherDescriptor);

    // Then
    Assert.assertEquals(false, match);
  }

  @Test
  public void test_equals_with_different_bDescriptorType() {
    // Given
    UsbPortDescriptor descriptor =
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

    UsbPortDescriptor otherDescriptor =
        new UsbPortDescriptor.Builder()
            .bcdDevice((short) 1)
            .bcdUSB((short) 2)
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
    boolean match = descriptor.equals(otherDescriptor);

    // Then
    Assert.assertEquals(false, match);
  }

  @Test
  public void test_equals_with_different_bDeviceClass() {
    // Given
    UsbPortDescriptor descriptor =
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

    UsbPortDescriptor otherDescriptor =
        new UsbPortDescriptor.Builder()
            .bcdDevice((short) 1)
            .bcdUSB((short) 2)
            .bDescriptorType((byte) 3)
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
    boolean match = descriptor.equals(otherDescriptor);

    // Then
    Assert.assertEquals(false, match);
  }

  @Test
  public void test_equals_with_different_bDeviceProtocol() {
    // Given
    UsbPortDescriptor descriptor =
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

    UsbPortDescriptor otherDescriptor =
        new UsbPortDescriptor.Builder()
            .bcdDevice((short) 1)
            .bcdUSB((short) 2)
            .bDescriptorType((byte) 3)
            .bDeviceClass((byte) 4)
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
    boolean match = descriptor.equals(otherDescriptor);

    // Then
    Assert.assertEquals(false, match);
  }

  @Test
  public void test_equals_with_different_bDeviceSubClass() {
    // Given
    UsbPortDescriptor descriptor =
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

    UsbPortDescriptor otherDescriptor =
        new UsbPortDescriptor.Builder()
            .bcdDevice((short) 1)
            .bcdUSB((short) 2)
            .bDescriptorType((byte) 3)
            .bDeviceClass((byte) 4)
            .bDeviceProtocol((byte) 5)
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
    boolean match = descriptor.equals(otherDescriptor);

    // Then
    Assert.assertEquals(false, match);
  }

  @Test
  public void test_equals_with_different_bLength() {
    // Given
    UsbPortDescriptor descriptor =
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

    UsbPortDescriptor otherDescriptor =
        new UsbPortDescriptor.Builder()
            .bcdDevice((short) 1)
            .bcdUSB((short) 2)
            .bDescriptorType((byte) 3)
            .bDeviceClass((byte) 4)
            .bDeviceProtocol((byte) 5)
            .bDeviceSubClass((byte) 6)
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
    boolean match = descriptor.equals(otherDescriptor);

    // Then
    Assert.assertEquals(false, match);
  }

  @Test
  public void test_equals_with_different_bMaxPacketSize0() {
    // Given
    UsbPortDescriptor descriptor =
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

    UsbPortDescriptor otherDescriptor =
        new UsbPortDescriptor.Builder()
            .bcdDevice((short) 1)
            .bcdUSB((short) 2)
            .bDescriptorType((byte) 3)
            .bDeviceClass((byte) 4)
            .bDeviceProtocol((byte) 5)
            .bDeviceSubClass((byte) 6)
            .bLength((byte) 7)
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
    boolean match = descriptor.equals(otherDescriptor);

    // Then
    Assert.assertEquals(false, match);
  }

  @Test
  public void test_equals_with_different_bNumConfigurations() {
    // Given
    UsbPortDescriptor descriptor =
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

    UsbPortDescriptor otherDescriptor =
        new UsbPortDescriptor.Builder()
            .bcdDevice((short) 1)
            .bcdUSB((short) 2)
            .bDescriptorType((byte) 3)
            .bDeviceClass((byte) 4)
            .bDeviceProtocol((byte) 5)
            .bDeviceSubClass((byte) 6)
            .bLength((byte) 7)
            .bMaxPacketSize0((byte) 8)
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
    boolean match = descriptor.equals(otherDescriptor);

    // Then
    Assert.assertEquals(false, match);
  }

  @Test
  public void test_equals_with_different_idProduct() {
    // Given
    UsbPortDescriptor descriptor =
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

    UsbPortDescriptor otherDescriptor =
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
            .idVendor((short) 11)
            .iManufacturer((byte) 12)
            .iProduct((byte) 13)
            .iSerialNumber((byte) 14)
            .manufacturer("test_manufacturer")
            .product("test_product")
            .serialNumber("SN123")
            .build();
    // When
    boolean match = descriptor.equals(otherDescriptor);

    // Then
    Assert.assertEquals(false, match);
  }

  @Test
  public void test_equals_with_different_idVendor() {
    // Given
    UsbPortDescriptor descriptor =
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

    UsbPortDescriptor otherDescriptor =
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
            .iManufacturer((byte) 12)
            .iProduct((byte) 13)
            .iSerialNumber((byte) 14)
            .manufacturer("test_manufacturer")
            .product("test_product")
            .serialNumber("SN123")
            .build();
    // When
    boolean match = descriptor.equals(otherDescriptor);

    // Then
    Assert.assertEquals(false, match);
  }

  @Test
  public void test_equals_with_different_iManufacturer() {
    // Given
    UsbPortDescriptor descriptor =
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

    UsbPortDescriptor otherDescriptor =
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
            .iProduct((byte) 13)
            .iSerialNumber((byte) 14)
            .manufacturer("test_manufacturer")
            .product("test_product")
            .serialNumber("SN123")
            .build();
    // When
    boolean match = descriptor.equals(otherDescriptor);

    // Then
    Assert.assertEquals(false, match);
  }

  @Test
  public void test_equals_with_different_iProduct() {
    // Given
    UsbPortDescriptor descriptor =
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

    UsbPortDescriptor otherDescriptor =
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
            .iSerialNumber((byte) 14)
            .manufacturer("test_manufacturer")
            .product("test_product")
            .serialNumber("SN123")
            .build();
    // When
    boolean match = descriptor.equals(otherDescriptor);

    // Then
    Assert.assertEquals(false, match);
  }

  @Test
  public void test_equals_with_different_iSerialNumber() {
    // Given
    UsbPortDescriptor descriptor =
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

    UsbPortDescriptor otherDescriptor =
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
            .manufacturer("test_manufacturer")
            .product("test_product")
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
    UsbPortDescriptor descriptor =
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

    UsbPortDescriptor otherDescriptor =
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
            .manufacturer("test_manufacturer_different")
            .product("test_product")
            .serialNumber("SN123")
            .build();
    // When
    boolean match = descriptor.equals(otherDescriptor);

    // Then
    Assert.assertEquals(false, match);
  }

  @Test
  public void test_equals_with_different_product() {
    // Given
    UsbPortDescriptor descriptor =
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

    UsbPortDescriptor otherDescriptor =
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
            .product("test_product_different")
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
    UsbPortDescriptor descriptor =
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

    UsbPortDescriptor otherDescriptor =
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
            .serialNumber("SN123_different")
            .build();
    // When
    boolean match = descriptor.equals(otherDescriptor);

    // Then
    Assert.assertEquals(false, match);
  }

  @Test
  public void test_hashCode() {
    // Given
    UsbPortDescriptor descriptor =
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
    int hashCode = descriptor.hashCode();

    // Then
    Assert.assertEquals(-50826581, hashCode);
  }
}
