// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.io.usb;

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
    this.bcdDevice = this.requireNumber(bcdDevice, KEY_BCD_DEVICE);
  }

  /**
   * Set bcd usb
   *
   * @param bcdUSB
   * @throws IllegalArgumentException if validation fails
   */
  public void setBcdUSB(Number bcdUSB) {
    this.bcdUSB = this.requireNumber(bcdUSB, KEY_BCD_USB);
  }

  /**
   * Set b descriptor type
   *
   * @param bDescriptorType
   * @throws IllegalArgumentException if validation fails
   */
  public void setBDescriptorType(Number bDescriptorType) {
    this.bDescriptorType = this.requireNumber(bDescriptorType, KEY_B_DESCRIPTOR_TYPE);
  }

  /**
   * Set b device class
   *
   * @param bDeviceClass
   * @throws IllegalArgumentException if validation fails
   */
  public void setBDeviceClass(Number bDeviceClass) {
    this.bDeviceClass = this.requireNumber(bDeviceClass, KEY_B_DEVICE_CLASS);
  }

  /**
   * Set b device protocol
   *
   * @param bDeviceProtocol
   * @throws IllegalArgumentException if validation fails
   */
  public void setBDeviceProtocol(Number bDeviceProtocol) {
    this.bDeviceProtocol = this.requireNumber(bDeviceProtocol, KEY_B_DEVICE_PROTOCOL);
  }

  /**
   * Set b device sub class
   *
   * @param bDeviceSubClass
   * @throws IllegalArgumentException if validation fails
   */
  public void setBDeviceSubClass(Number bDeviceSubClass) {
    this.bDeviceSubClass = this.requireNumber(bDeviceSubClass, KEY_B_DEVICE_SUB_CLASS);
  }

  /**
   * Set b length
   *
   * @param bLength
   * @throws IllegalArgumentException if validation fails
   */
  public void setBLength(Number bLength) {
    this.bLength = this.requireNumber(bLength, KEY_B_LENGTH);
  }

  /**
   * Set b max packet size0
   *
   * @param bMaxPacketSize0
   * @throws IllegalArgumentException if validation fails
   */
  public void setBMaxPacketSize0(Number bMaxPacketSize0) {
    this.bMaxPacketSize0 = this.requireNumber(bMaxPacketSize0, KEY_B_MAX_PACKET_SIZE0);
  }

  /**
   * Set b num configurations
   *
   * @param bNumConfigurations
   * @throws IllegalArgumentException if validation fails
   */
  public void setBNumConfigurations(Number bNumConfigurations) {
    this.bNumConfigurations = this.requireNumber(bNumConfigurations, KEY_B_NUM_CONFIGURATIONS);
  }

  /**
   * Set id product
   *
   * @param idProduct
   * @throws IllegalArgumentException if validation fails
   */
  public void setIdProduct(Number idProduct) {
    this.idProduct = this.requireNumber(idProduct, KEY_ID_PRODUCT);
  }

  /**
   * Set id vendor
   *
   * @param idVendor
   * @throws IllegalArgumentException if validation fails
   */
  public void setIdVendor(Number idVendor) {
    this.idVendor = this.requireNumber(idVendor, KEY_ID_VENDOR);
  }

  /**
   * Set i manufacturer
   *
   * @param iManufacturer
   * @throws IllegalArgumentException if validation fails
   */
  public void setIManufacturer(Number iManufacturer) {
    this.iManufacturer = this.requireNumber(iManufacturer, KEY_I_MANUFACTURER);
  }

  /**
   * Set i product
   *
   * @param iProduct
   * @throws IllegalArgumentException if validation fails
   */
  public void setIProduct(Number iProduct) {
    this.iProduct = this.requireNumber(iProduct, KEY_I_PRODUCT);
  }

  /**
   * Set i serial number
   *
   * @param iSerialNumber
   * @throws IllegalArgumentException if validation fails
   */
  public void setISerialNumber(Number iSerialNumber) {
    this.iSerialNumber = this.requireNumber(iSerialNumber, KEY_I_SERIAL_NUMBER);
  }

  /**
   * Set manufacturer
   *
   * @param manufacturer
   * @throws IllegalArgumentException if validation fails
   */
  public void setManufacturer(String manufacturer) {
    this.manufacturer = this.normalizeString(manufacturer);
  }

  /**
   * Set product
   *
   * @param product
   * @throws IllegalArgumentException if validation fails
   */
  public void setProduct(String product) {
    this.product = this.normalizeString(product);
  }

  /**
   * Set serial number
   *
   * @param serialNumber
   * @throws IllegalArgumentException if validation fails
   */
  public void setSerialNumber(String serialNumber) {
    this.serialNumber = this.normalizeString(serialNumber);
  }

  private Number requireNumber(Number value, String key) {
    if (value == null) {
      throw new IllegalArgumentException("Value '" + key + "' cannot be null");
    }
    return value;
  }

  private String normalizeString(String value) {
    if (value == null) {
      return null;
    }
    String trimmed = value.trim();
    return trimmed.isEmpty() ? null : trimmed;
  }
}
