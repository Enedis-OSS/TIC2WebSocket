// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.io.usb;

import java.util.Objects;
import tic.util.ValueChecker;

/**
 * A structure representing the standard USB device descriptor with additional string descriptors
 * (manufacturer, product, serial number) if provided. This USB device descriptor is documented in
 * section 9.6.1 of the USB 3.0 specification. All multiple-byte fields are represented in
 * host-endian format.
 */
public class UsbPortDescriptor {

  private short bcdDevice;
  private short bcdUSB;
  private byte bDescriptorType;
  private byte bDeviceClass;
  private byte bDeviceProtocol;
  private byte bDeviceSubClass;
  private byte bLength;
  private byte bMaxPacketSize0;
  private byte bNumConfigurations;
  private short idProduct;
  private short idVendor;
  private byte iManufacturer;
  private byte iProduct;
  private byte iSerialNumber;
  private String manufacturer;
  private String product;
  private String serialNumber;

  /** Builder class for constructing USBPortDescriptor instances. */
  public static class Builder {
    private short bcdDevice;
    private short bcdUSB;
    private byte bDescriptorType;
    private byte bDeviceClass;
    private byte bDeviceProtocol;
    private byte bDeviceSubClass;
    private byte bLength;
    private byte bMaxPacketSize0;
    private byte bNumConfigurations;
    private short idProduct;
    private short idVendor;
    private byte iManufacturer;
    private byte iProduct;
    private byte iSerialNumber;
    private String manufacturer;
    private String product;
    private String serialNumber;

    /**
     * Sets the bcdDevice field.
     *
     * @param bcdDevice the binary-coded decimal device release number
     * @return the Builder instance
     */
    public Builder bcdDevice(short bcdDevice) {
      this.bcdDevice = bcdDevice;
      return this;
    }

    /**
     * Sets the bcdUSB field.
     *
     * @param bcdUSB the binary-coded decimal USB specification release number
     * @return the Builder instance
     */
    public Builder bcdUSB(short bcdUSB) {
      this.bcdUSB = bcdUSB;
      return this;
    }

    /**
     * Sets the bDescriptorType field.
     *
     * @param bDescriptorType the USB descriptor type
     * @return the Builder instance
     */
    public Builder bDescriptorType(byte bDescriptorType) {
      this.bDescriptorType = bDescriptorType;
      return this;
    }

    /**
     * Sets the bDeviceClass field.
     *
     * @param bDeviceClass the USB device class
     * @return the Builder instance
     */
    public Builder bDeviceClass(byte bDeviceClass) {
      this.bDeviceClass = bDeviceClass;
      return this;
    }

    /**
     * Sets the bDeviceProtocol field.
     *
     * @param bDeviceProtocol the USB device protocol
     * @return the Builder instance
     */
    public Builder bDeviceProtocol(byte bDeviceProtocol) {
      this.bDeviceProtocol = bDeviceProtocol;
      return this;
    }

    /**
     * Sets the bDeviceSubClass field.
     *
     * @param bDeviceSubClass the USB device subclass
     * @return the Builder instance
     */
    public Builder bDeviceSubClass(byte bDeviceSubClass) {
      this.bDeviceSubClass = bDeviceSubClass;
      return this;
    }

    /**
     * Sets the bLength field.
     *
     * @param bLength the length of the USB device descriptor
     * @return the Builder instance
     */
    public Builder bLength(byte bLength) {
      this.bLength = bLength;
      return this;
    }

    /**
     * Sets the bMaxPacketSize0 field.
     *
     * @param bMaxPacketSize0 the maximum packet size for endpoint 0
     * @return the Builder instance
     */
    public Builder bMaxPacketSize0(byte bMaxPacketSize0) {
      this.bMaxPacketSize0 = bMaxPacketSize0;
      return this;
    }

    /**
     * Sets the bNumConfigurations field.
     *
     * @param bNumConfigurations the number of possible configurations
     * @return the Builder instance
     */
    public Builder bNumConfigurations(byte bNumConfigurations) {
      this.bNumConfigurations = bNumConfigurations;
      return this;
    }

    /**
     * Sets the idProduct field.
     *
     * @param idProduct the USB product ID
     * @return the Builder instance
     */
    public Builder idProduct(short idProduct) {
      this.idProduct = idProduct;
      return this;
    }

    /**
     * Sets the idVendor field.
     *
     * @param idVendor the USB vendor ID
     * @return the Builder instance
     */
    public Builder idVendor(short idVendor) {
      this.idVendor = idVendor;
      return this;
    }

    /**
     * Sets the iManufacturer field.
     *
     * @param iManufacturer the index of the manufacturer string descriptor
     * @return the Builder instance
     */
    public Builder iManufacturer(byte iManufacturer) {
      this.iManufacturer = iManufacturer;
      return this;
    }

    /**
     * Sets the iProduct field.
     *
     * @param iProduct the index of the product string descriptor
     * @return the Builder instance
     */
    public Builder iProduct(byte iProduct) {
      this.iProduct = iProduct;
      return this;
    }

    /**
     * Sets the iSerialNumber field.
     *
     * @param iSerialNumber the index of the serial number string descriptor
     * @return the Builder instance
     */
    public Builder iSerialNumber(byte iSerialNumber) {
      this.iSerialNumber = iSerialNumber;
      return this;
    }

    /**
     * Sets the manufacturer field.
     *
     * @param manufacturer the manufacturer string
     * @return the Builder instance
     */
    public Builder manufacturer(String manufacturer) {
      this.manufacturer = manufacturer;
      return this;
    }

    /**
     * Sets the product field.
     *
     * @param product the product string
     * @return the Builder instance
     */
    public Builder product(String product) {
      this.product = product;
      return this;
    }

    /**
     * Sets the serialNumber field.
     *
     * @param serialNumber the serial number string
     * @return the Builder instance
     */
    public Builder serialNumber(String serialNumber) {
      this.serialNumber = serialNumber;
      return this;
    }

    /**
     * Builds the USBPortDescriptor instance.
     *
     * @return the constructed USBPortDescriptor instance
     * @throws IllegalArgumentException if any required field is invalid
     */
    public UsbPortDescriptor build() {
      String manufacturer =
          ValueChecker.validateString(this.manufacturer, "manufacturer", true, false);
      String product = ValueChecker.validateString(this.product, "product", true, false);
      String serialNumber =
          ValueChecker.validateString(this.serialNumber, "serialNumber", true, false);

      return new UsbPortDescriptor(
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
          manufacturer,
          product,
          serialNumber);
    }
  }

  /**
   * Constructor setting parameters to specific values
   *
   * @param bcdDevice the USB device release number in binary-coded decimal
   * @param bcdUSB the USB specification release number in binary-coded decimal
   * @param bDescriptorType the type of descriptor
   * @param bDeviceClass the class of device
   * @param bDeviceProtocol the protocol of device
   * @param bDeviceSubClass the subclass of device
   * @param bLength the length of the descriptor
   * @param bMaxPacketSize0 the maximum packet size for endpoint 0
   * @param bNumConfigurations the number of possible configurations
   * @param idProduct the product ID
   * @param idVendor the vendor ID
   * @param iManufacturer the index of the manufacturer string descriptor
   * @param iProduct the index of the product string descriptor
   * @param iSerialNumber the index of the serial number string descriptor
   * @param manufacturer the manufacturer
   * @param product the product
   * @param serialNumber the serial number
   */
  private UsbPortDescriptor(
      short bcdDevice,
      short bcdUSB,
      byte bDescriptorType,
      byte bDeviceClass,
      byte bDeviceProtocol,
      byte bDeviceSubClass,
      byte bLength,
      byte bMaxPacketSize0,
      byte bNumConfigurations,
      short idProduct,
      short idVendor,
      byte iManufacturer,
      byte iProduct,
      byte iSerialNumber,
      String manufacturer,
      String product,
      String serialNumber) {
    this.bcdDevice = bcdDevice;
    this.bcdUSB = bcdUSB;
    this.bDescriptorType = bDescriptorType;
    this.bDeviceClass = bDeviceClass;
    this.bDeviceProtocol = bDeviceProtocol;
    this.bDeviceSubClass = bDeviceSubClass;
    this.bLength = bLength;
    this.bMaxPacketSize0 = bMaxPacketSize0;
    this.bNumConfigurations = bNumConfigurations;
    this.idProduct = idProduct;
    this.idVendor = idVendor;
    this.iManufacturer = iManufacturer;
    this.iProduct = iProduct;
    this.iSerialNumber = iSerialNumber;
    this.manufacturer = manufacturer;
    this.product = product;
    this.serialNumber = serialNumber;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof UsbPortDescriptor)) {
      return false;
    }
    UsbPortDescriptor other = (UsbPortDescriptor) obj;
    return this.bcdDevice == other.bcdDevice
        && this.bcdUSB == other.bcdUSB
        && this.bDescriptorType == other.bDescriptorType
        && this.bDeviceClass == other.bDeviceClass
        && this.bDeviceProtocol == other.bDeviceProtocol
        && this.bDeviceSubClass == other.bDeviceSubClass
        && this.bLength == other.bLength
        && this.bMaxPacketSize0 == other.bMaxPacketSize0
        && this.bNumConfigurations == other.bNumConfigurations
        && this.idProduct == other.idProduct
        && this.idVendor == other.idVendor
        && this.iManufacturer == other.iManufacturer
        && this.iProduct == other.iProduct
        && this.iSerialNumber == other.iSerialNumber
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
   * Returns the USB device release number in binary-coded decimal.
   *
   * @return The USB device release number.
   */
  public short bcdDevice() {
    return this.bcdDevice;
  }

  /**
   * Returns the USB specification release number in binary-coded decimal. A value of 0x0200
   * indicates USB 2.0, 0x0110 indicates USB 1.1, etc.
   *
   * @return The USB specification release number.
   */
  public short bcdUSB() {
    return this.bcdUSB;
  }

  /**
   * Returns the USB descriptor type.
   *
   * @return The USB descriptor type.
   */
  public byte bDescriptorType() {
    return this.bDescriptorType;
  }

  /**
   * Returns the USB-IF class code for the device.
   *
   * @return The USB-IF class code.
   */
  public byte bDeviceClass() {
    return this.bDeviceClass;
  }

  /**
   * Returns the USB-IF protocol code for the device, qualified by the bDeviceClass and
   * bDeviceSubClass values
   *
   * @return The USB-IF protocol code.
   */
  public byte bDeviceProtocol() {
    return this.bDeviceProtocol;
  }

  /**
   * Returns the USB-IF subclass code for the device, qualified by the bDeviceClass value.
   *
   * @return The USB-IF subclass code.
   */
  public byte bDeviceSubClass() {
    return this.bDeviceSubClass;
  }

  /**
   * Returns the size of the USB device descriptor (in bytes) without
   * manufacturer/product/serialNumber fields.
   *
   * @return The size of this descriptor (in bytes).
   */
  public byte bLength() {
    return this.bLength;
  }

  /**
   * Returns the maximum packet size for endpoint 0.
   *
   * @return The maximum packet site for endpoint 0.
   */
  public byte bMaxPacketSize0() {
    return this.bMaxPacketSize0;
  }

  /**
   * Returns the number of possible configurations.
   *
   * @return The number of possible configurations.
   */
  public byte bNumConfigurations() {
    return this.bNumConfigurations;
  }

  /**
   * Returns the USB-IF product ID.
   *
   * @return The product ID.
   */
  public short idProduct() {
    return this.idProduct;
  }

  /**
   * Returns the USB-IF vendor ID.
   *
   * @return The vendor ID.
   */
  public short idVendor() {
    return this.idVendor;
  }

  /**
   * Returns the index of the string descriptor describing manufacturer.
   *
   * @return The manufacturer string descriptor index.
   */
  public byte iManufacturer() {
    return this.iManufacturer;
  }

  /**
   * Returns the index of the string descriptor describing product.
   *
   * @return The product string descriptor index.
   */
  public byte iProduct() {
    return this.iProduct;
  }

  /**
   * Returns the index of the string descriptor containing device serial number.
   *
   * @return The serial number string descriptor index.
   */
  public byte iSerialNumber() {
    return this.iSerialNumber;
  }

  /**
   * Returns the USB device manufacturer.
   *
   * @return The USB device manufacturer if exists or null otherwise.
   */
  public String manufacturer() {
    return this.manufacturer;
  }

  /**
   * Returns the USB device product.
   *
   * @return The USB device product if exists or null otherwise.
   */
  public String product() {
    return this.product;
  }

  /**
   * Returns the USB device serial number.
   *
   * @return The USB device serial number if exists or null otherwise.
   */
  public String serialNumber() {
    return this.serialNumber;
  }
}
