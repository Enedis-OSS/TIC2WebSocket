// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.io.serialport;

import tic.util.descriptor.Descriptor;

/**
 * Descriptor class for serial port information and USB device properties.
 *
 * <p>This class provides a structured way to store and access serial port information, including
 * both native serial ports and USB-based serial devices, without relying on the legacy
 * DataDictionary infrastructure. It encapsulates all relevant properties needed to identify,
 * configure, and connect to serial ports.
 *
 * <p>The descriptor includes the following information:
 *
 * <ul>
 *   <li>Port unique identifier
 *   <li>Port name used to open the serial port
 *   <li>Port description
 *   <li>USB device product identifier (PID)
 *   <li>USB device vendor identifier (VID)
 *   <li>USB device product name
 *   <li>USB device manufacturer
 *   <li>USB device serial number
 * </ul>
 *
 * <p>The class supports distinguishing between native serial ports (e.g., COM ports) and USB serial
 * devices through the {@link #isNative()} method.
 *
 * @author Enedis Smarties team
 */
public class SerialPortDescriptor extends Descriptor {
  protected static final String KEY_PORT_ID = "portId";
  protected static final String KEY_PORT_NAME = "portName";
  protected static final String KEY_DESCRIPTION = "description";
  protected static final String KEY_PRODUCT_ID = "productId";
  protected static final String KEY_VENDOR_ID = "vendorId";
  protected static final String KEY_PRODUCT_NAME = "productName";
  protected static final String KEY_MANUFACTURER = "manufacturer";
  protected static final String KEY_SERIAL_NUMBER = "serialNumber";

  private static final int USB_IDENTIFIER_MIN = 0;
  private static final int USB_IDENTIFIER_MAX = 65535;

  private String portId;
  private String portName;
  private String description;
  private Number productId;
  private Number vendorId;
  private String productName;
  private String manufacturer;
  private String serialNumber;

  /**
   * Constructs an empty SerialPortDescriptor.
   *
   * <p>This constructor initializes the descriptor with default values and loads the key
   * descriptors for validation and type conversion.
   */
  public SerialPortDescriptor() {}

  /**
   * Constructs a SerialPortDescriptor with all parameters explicitly set.
   *
   * <p>This constructor creates a descriptor with all serial port and USB device properties
   * specified. It validates the parameters and updates the descriptor accordingly.
   *
   * @param portId the port unique identifier
   * @param portName the port name used to open the serial port
   * @param description the port description
   * @param productId the USB device product identifier (PID), must be in range [0-65535]
   * @param vendorId the USB device vendor identifier (VID), must be in range [0-65535]
   * @param productName the USB device product name
   * @param manufacturer the USB device manufacturer
   * @param serialNumber the USB device serial number
   * @throws IllegalArgumentException if any parameter is invalid or out of range
   */
  public SerialPortDescriptor(
      String portId,
      String portName,
      String description,
      Number productId,
      Number vendorId,
      String productName,
      String manufacturer,
      String serialNumber) {
    this.setPortId(portId);
    this.setPortName(portName);
    this.setDescription(description);
    this.setProductId(productId);
    this.setVendorId(vendorId);
    this.setProductName(productName);
    this.setManufacturer(manufacturer);
    this.setSerialNumber(serialNumber);
  }

  /**
   * Returns the port unique identifier.
   *
   * @return the port unique identifier
   */
  public String getPortId() {
    return this.portId;
  }

  /**
   * Returns the port name used to open the serial port.
   *
   * @return the port name used to open the serial port
   */
  public String getPortName() {
    return this.portName;
  }

  /**
   * Returns the port description.
   *
   * @return the port description
   */
  public String getDescription() {
    return this.description;
  }

  /**
   * Returns the USB device product identifier (PID).
   *
   * @return the USB device product identifier in range [0-65535]
   */
  public Number getProductId() {
    return this.productId;
  }

  /**
   * Returns the USB device vendor identifier (VID).
   *
   * @return the USB device vendor identifier in range [0-65535]
   */
  public Number getVendorId() {
    return this.vendorId;
  }

  /**
   * Returns the USB device product name.
   *
   * @return the USB device product name
   */
  public String getProductName() {
    return this.productName;
  }

  /**
   * Returns the USB device manufacturer.
   *
   * @return the USB device manufacturer
   */
  public String getManufacturer() {
    return this.manufacturer;
  }

  /**
   * Returns the USB device serial number.
   *
   * @return the USB device serial number
   */
  public String getSerialNumber() {
    return this.serialNumber;
  }

  /**
   * Sets the port unique identifier.
   *
   * @param portId the port unique identifier
   * @throws IllegalArgumentException if portId is invalid
   */
  public void setPortId(String portId) {
    this.portId = validateString(portId, KEY_PORT_ID);
  }

  /**
   * Sets the port name used to open the serial port.
   *
   * @param portName the port name used to open the serial port
   * @throws IllegalArgumentException if portName is invalid
   */
  public void setPortName(String portName) {
    this.portName = validateString(portName, KEY_PORT_NAME);
  }

  /**
   * Sets the port description.
   *
   * @param description the port description
   * @throws IllegalArgumentException if description is invalid
   */
  public void setDescription(String description) {
    this.description = validateString(description, KEY_DESCRIPTION);
  }

  /**
   * Sets the USB device product identifier (PID).
   *
   * @param productId the USB device product identifier
   * @throws IllegalArgumentException if productId is out of range [0-65535]
   */
  public void setProductId(Number productId) {
    this.productId =
        validateNumber(productId, KEY_PRODUCT_ID, USB_IDENTIFIER_MIN, USB_IDENTIFIER_MAX);
  }

  /**
   * Sets the USB device vendor identifier (VID).
   *
   * @param vendorId the USB device vendor identifier
   * @throws IllegalArgumentException if vendorId is out of range [0-65535]
   */
  public void setVendorId(Number vendorId) {
    this.vendorId = validateNumber(vendorId, KEY_VENDOR_ID, USB_IDENTIFIER_MIN, USB_IDENTIFIER_MAX);
  }

  /**
   * Sets the USB device product name.
   *
   * @param productName the USB device product name
   * @throws IllegalArgumentException if productName is invalid
   */
  public void setProductName(String productName) {
    this.productName = validateString(productName, KEY_PRODUCT_NAME);
  }

  /**
   * Sets the USB device manufacturer.
   *
   * @param manufacturer the USB device manufacturer
   * @throws IllegalArgumentException if manufacturer is invalid
   */
  public void setManufacturer(String manufacturer) {
    this.manufacturer = validateString(manufacturer, KEY_MANUFACTURER);
  }

  /**
   * Sets the USB device serial number.
   *
   * @param serialNumber the USB device serial number
   * @throws IllegalArgumentException if serialNumber is invalid
   */
  public void setSerialNumber(String serialNumber) {
    this.serialNumber = validateString(serialNumber, KEY_SERIAL_NUMBER);
  }

  /**
   * Checks if this serial port is a native port (not USB).
   *
   * <p>A port is considered native if it has a port name but no port ID. This typically indicates a
   * built-in serial port (e.g., COM1) rather than a USB-to-serial adapter.
   *
   * @return true if this is a native serial port, false if it's a USB device
   */
  public boolean isNative() {
    return this.portName != null && !this.portName.isEmpty() && this.portId == null;
  }
}
