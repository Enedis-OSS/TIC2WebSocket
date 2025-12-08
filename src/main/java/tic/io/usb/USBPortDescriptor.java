// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.io.usb;

import java.util.Objects;
import tic.util.ValueChecker;

public class USBPortDescriptor {
  private static final String KEY_BCD_DEVICE = "bcdDevice";
  private static final String KEY_BCD_USB = "bcdUSB";
  private static final String KEY_B_DESCRIPTOR_TYPE = "bDescriptorType";
  private static final String KEY_B_DEVICE_CLASS = "bDeviceClass";
  private static final String KEY_B_DEVICE_PROTOCOL = "bDeviceProtocol";
  private static final String KEY_B_DEVICE_SUB_CLASS = "bDeviceSubClass";
  private static final String KEY_B_LENGTH = "bLength";
  private static final String KEY_B_MAX_PACKET_SIZE0 = "bMaxPacketSize0";
  private static final String KEY_B_NUM_CONFIGURATIONS = "bNumConfigurations";
  private static final String KEY_ID_PRODUCT = "idProduct";
  private static final String KEY_ID_VENDOR = "idVendor";
  private static final String KEY_I_MANUFACTURER = "iManufacturer";
  private static final String KEY_I_PRODUCT = "iProduct";
  private static final String KEY_I_SERIAL_NUMBER = "iSerialNumber";
  private static final String KEY_MANUFACTURER = "manufacturer";
  private static final String KEY_PRODUCT = "product";
  private static final String KEY_SERIAL_NUMBER = "serialNumber";

  private Number bcdDevice;
  private Number bcdUSB;
  private Number bDescriptorType;
  private Number bDeviceClass;
  private Number bDeviceProtocol;
  private Number bDeviceSubClass;
  private Number bLength;
  private Number bMaxPacketSize0;
  private Number bNumConfigurations;
  private Number idProduct;
  private Number idVendor;
  private Number iManufacturer;
  private Number iProduct;
  private Number iSerialNumber;
  private String manufacturer;
  private String product;
  private String serialNumber;

  /**
   * Constructor setting parameters to specific values
   *
   * @param bcdDevice
   * @param bcdUSB
   * @param bDescriptorType
   * @param bDeviceClass
   * @param bDeviceProtocol
   * @param bDeviceSubClass
   * @param bLength
   * @param bMaxPacketSize0
   * @param bNumConfigurations
   * @param idProduct
   * @param idVendor
   * @param iManufacturer
   * @param iProduct
   * @param iSerialNumber
   * @param manufacturer
   * @param product
   * @param serialNumber
   */
  public USBPortDescriptor(
      Number bcdDevice,
      Number bcdUSB,
      Number bDescriptorType,
      Number bDeviceClass,
      Number bDeviceProtocol,
      Number bDeviceSubClass,
      Number bLength,
      Number bMaxPacketSize0,
      Number bNumConfigurations,
      Number idProduct,
      Number idVendor,
      Number iManufacturer,
      Number iProduct,
      Number iSerialNumber,
      String manufacturer,
      String product,
      String serialNumber) {
    this.setBcdDevice(bcdDevice);
    this.setBcdUSB(bcdUSB);
    this.setBDescriptorType(bDescriptorType);
    this.setBDeviceClass(bDeviceClass);
    this.setBDeviceProtocol(bDeviceProtocol);
    this.setBDeviceSubClass(bDeviceSubClass);
    this.setBLength(bLength);
    this.setBMaxPacketSize0(bMaxPacketSize0);
    this.setBNumConfigurations(bNumConfigurations);
    this.setIdProduct(idProduct);
    this.setIdVendor(idVendor);
    this.setIManufacturer(iManufacturer);
    this.setIProduct(iProduct);
    this.setISerialNumber(iSerialNumber);
    this.setManufacturer(manufacturer);
    this.setProduct(product);
    this.setSerialNumber(serialNumber);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof USBPortDescriptor)) {
      return false;
    }
    USBPortDescriptor other = (USBPortDescriptor) obj;
    return Objects.equals(this.bcdDevice, other.bcdDevice)
        && Objects.equals(this.bcdUSB, other.bcdUSB)
        && Objects.equals(this.bDescriptorType, other.bDescriptorType)
        && Objects.equals(this.bDeviceClass, other.bDeviceClass)
        && Objects.equals(this.bDeviceProtocol, other.bDeviceProtocol)
        && Objects.equals(this.bDeviceSubClass, other.bDeviceSubClass)
        && Objects.equals(this.bLength, other.bLength)
        && Objects.equals(this.bMaxPacketSize0, other.bMaxPacketSize0)
        && Objects.equals(this.bNumConfigurations, other.bNumConfigurations)
        && Objects.equals(this.idProduct, other.idProduct)
        && Objects.equals(this.idVendor, other.idVendor)
        && Objects.equals(this.iManufacturer, other.iManufacturer)
        && Objects.equals(this.iProduct, other.iProduct)
        && Objects.equals(this.iSerialNumber, other.iSerialNumber)
        && Objects.equals(this.manufacturer, other.manufacturer)
        && Objects.equals(this.product, other.product)
        && Objects.equals(this.serialNumber, other.serialNumber);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        this.bcdDevice,
        this.bcdUSB,
        this.bDescriptorType,
        this.bDeviceClass,
        this.bDeviceProtocol,
        this.bDeviceSubClass,
        this.bLength,
        this.bMaxPacketSize0,
        this.bNumConfigurations,
        this.idProduct,
        this.idVendor,
        this.iManufacturer,
        this.iProduct,
        this.iSerialNumber,
        this.manufacturer,
        this.product,
        this.serialNumber);
  }

  /**
   * Get bcd device
   *
   * @return the bcd device
   */
  public Number getBcdDevice() {
    return this.bcdDevice;
  }

  /**
   * Get bcd usb
   *
   * @return the bcd usb
   */
  public Number getBcdUSB() {
    return this.bcdUSB;
  }

  /**
   * Get b descriptor type
   *
   * @return the b descriptor type
   */
  public Number getBDescriptorType() {
    return this.bDescriptorType;
  }

  /**
   * Get b device class
   *
   * @return the b device class
   */
  public Number getBDeviceClass() {
    return this.bDeviceClass;
  }

  /**
   * Get b device protocol
   *
   * @return the b device protocol
   */
  public Number getBDeviceProtocol() {
    return this.bDeviceProtocol;
  }

  /**
   * Get b device sub class
   *
   * @return the b device sub class
   */
  public Number getBDeviceSubClass() {
    return this.bDeviceSubClass;
  }

  /**
   * Get b length
   *
   * @return the b length
   */
  public Number getBLength() {
    return this.bLength;
  }

  /**
   * Get b max packet size0
   *
   * @return the b max packet size0
   */
  public Number getBMaxPacketSize0() {
    return this.bMaxPacketSize0;
  }

  /**
   * Get b num configurations
   *
   * @return the b num configurations
   */
  public Number getBNumConfigurations() {
    return this.bNumConfigurations;
  }

  /**
   * Get id product
   *
   * @return the id product
   */
  public Number getIdProduct() {
    return this.idProduct;
  }

  /**
   * Get id vendor
   *
   * @return the id vendor
   */
  public Number getIdVendor() {
    return this.idVendor;
  }

  /**
   * Get i manufacturer
   *
   * @return the i manufacturer
   */
  public Number getIManufacturer() {
    return this.iManufacturer;
  }

  /**
   * Get i product
   *
   * @return the i product
   */
  public Number getIProduct() {
    return this.iProduct;
  }

  /**
   * Get i serial number
   *
   * @return the i serial number
   */
  public Number getISerialNumber() {
    return this.iSerialNumber;
  }

  /**
   * Get manufacturer
   *
   * @return the manufacturer
   */
  public String getManufacturer() {
    return this.manufacturer;
  }

  /**
   * Get product
   *
   * @return the product
   */
  public String getProduct() {
    return this.product;
  }

  /**
   * Get serial number
   *
   * @return the serial number
   */
  public String getSerialNumber() {
    return this.serialNumber;
  }

  /**
   * Set bcd device
   *
   * @param bcdDevice
   * @throws IllegalArgumentException if validation fails
   */
  public void setBcdDevice(Number bcdDevice) {
    this.bcdDevice = ValueChecker.validateNumber(bcdDevice, KEY_BCD_DEVICE, false);
  }

  /**
   * Set bcd usb
   *
   * @param bcdUSB
   * @throws IllegalArgumentException if validation fails
   */
  public void setBcdUSB(Number bcdUSB) {
    this.bcdUSB = ValueChecker.validateNumber(bcdUSB, KEY_BCD_USB, false);
  }

  /**
   * Set b descriptor type
   *
   * @param bDescriptorType
   * @throws IllegalArgumentException if validation fails
   */
  public void setBDescriptorType(Number bDescriptorType) {
    this.bDescriptorType =
        ValueChecker.validateNumber(bDescriptorType, KEY_B_DESCRIPTOR_TYPE, false);
  }

  /**
   * Set b device class
   *
   * @param bDeviceClass
   * @throws IllegalArgumentException if validation fails
   */
  public void setBDeviceClass(Number bDeviceClass) {
    this.bDeviceClass = ValueChecker.validateNumber(bDeviceClass, KEY_B_DEVICE_CLASS, false);
  }

  /**
   * Set b device protocol
   *
   * @param bDeviceProtocol
   * @throws IllegalArgumentException if validation fails
   */
  public void setBDeviceProtocol(Number bDeviceProtocol) {
    this.bDeviceProtocol =
        ValueChecker.validateNumber(bDeviceProtocol, KEY_B_DEVICE_PROTOCOL, false);
  }

  /**
   * Set b device sub class
   *
   * @param bDeviceSubClass
   * @throws IllegalArgumentException if validation fails
   */
  public void setBDeviceSubClass(Number bDeviceSubClass) {
    this.bDeviceSubClass =
        ValueChecker.validateNumber(bDeviceSubClass, KEY_B_DEVICE_SUB_CLASS, false);
  }

  /**
   * Set b length
   *
   * @param bLength
   * @throws IllegalArgumentException if validation fails
   */
  public void setBLength(Number bLength) {
    this.bLength = ValueChecker.validateNumber(bLength, KEY_B_LENGTH, false);
  }

  /**
   * Set b max packet size0
   *
   * @param bMaxPacketSize0
   * @throws IllegalArgumentException if validation fails
   */
  public void setBMaxPacketSize0(Number bMaxPacketSize0) {
    this.bMaxPacketSize0 =
        ValueChecker.validateNumber(bMaxPacketSize0, KEY_B_MAX_PACKET_SIZE0, false);
  }

  /**
   * Set b num configurations
   *
   * @param bNumConfigurations
   * @throws IllegalArgumentException if validation fails
   */
  public void setBNumConfigurations(Number bNumConfigurations) {
    this.bNumConfigurations =
        ValueChecker.validateNumber(bNumConfigurations, KEY_B_NUM_CONFIGURATIONS, false);
  }

  /**
   * Set id product
   *
   * @param idProduct
   * @throws IllegalArgumentException if validation fails
   */
  public void setIdProduct(Number idProduct) {
    this.idProduct = ValueChecker.validateNumber(idProduct, KEY_ID_PRODUCT, false);
  }

  /**
   * Set id vendor
   *
   * @param idVendor
   * @throws IllegalArgumentException if validation fails
   */
  public void setIdVendor(Number idVendor) {
    this.idVendor = ValueChecker.validateNumber(idVendor, KEY_ID_VENDOR, false);
  }

  /**
   * Set i manufacturer
   *
   * @param iManufacturer
   * @throws IllegalArgumentException if validation fails
   */
  public void setIManufacturer(Number iManufacturer) {
    this.iManufacturer = ValueChecker.validateNumber(iManufacturer, KEY_I_MANUFACTURER, false);
  }

  /**
   * Set i product
   *
   * @param iProduct
   * @throws IllegalArgumentException if validation fails
   */
  public void setIProduct(Number iProduct) {
    this.iProduct = ValueChecker.validateNumber(iProduct, KEY_I_PRODUCT, false);
  }

  /**
   * Set i serial number
   *
   * @param iSerialNumber
   * @throws IllegalArgumentException if validation fails
   */
  public void setISerialNumber(Number iSerialNumber) {
    this.iSerialNumber = ValueChecker.validateNumber(iSerialNumber, KEY_I_SERIAL_NUMBER, false);
  }

  /**
   * Set manufacturer
   *
   * @param manufacturer
   * @throws IllegalArgumentException if validation fails
   */
  public void setManufacturer(String manufacturer) {
    this.manufacturer = ValueChecker.validateString(manufacturer, KEY_MANUFACTURER, true, false);
  }

  /**
   * Set product
   *
   * @param product
   * @throws IllegalArgumentException if validation fails
   */
  public void setProduct(String product) {
    this.product = ValueChecker.validateString(product, KEY_PRODUCT, true, false);
  }

  /**
   * Set serial number
   *
   * @param serialNumber
   * @throws IllegalArgumentException if validation fails
   */
  public void setSerialNumber(String serialNumber) {
    this.serialNumber = ValueChecker.validateString(serialNumber, KEY_SERIAL_NUMBER, true, false);
  }
}
