// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.io.serialport;

import enedis.lab.types.DataDictionary;
import enedis.lab.types.DataDictionaryException;
import enedis.lab.types.datadictionary.DataDictionaryBase;
import enedis.lab.types.datadictionary.KeyDescriptor;
import enedis.lab.types.datadictionary.KeyDescriptorNumberMinMax;
import enedis.lab.types.datadictionary.KeyDescriptorString;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Descriptor class for serial port information and USB device properties.
 *
 * <p>This class extends {@link DataDictionaryBase} and provides a structured way to store and
 * access serial port information, including both native serial ports and USB-based serial devices.
 * It encapsulates all relevant properties needed to identify, configure, and connect to serial
 * ports.
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
 * @see DataDictionaryBase
 * @see DataDictionaryException
 */
public class SerialPortDescriptor extends DataDictionaryBase {
  protected static final String KEY_PORT_ID = "portId";
  protected static final String KEY_PORT_NAME = "portName";
  protected static final String KEY_DESCRIPTION = "description";
  protected static final String KEY_PRODUCT_ID = "productId";
  protected static final String KEY_VENDOR_ID = "vendorId";
  protected static final String KEY_PRODUCT_NAME = "productName";
  protected static final String KEY_MANUFACTURER = "manufacturer";
  protected static final String KEY_SERIAL_NUMBER = "serialNumber";

  private static final Number PRODUCT_ID_MIN = 0;
  private static final Number PRODUCT_ID_MAX = 65535;
  private static final Number VENDOR_ID_MIN = 0;
  private static final Number VENDOR_ID_MAX = 65535;

  private List<KeyDescriptor<?>> keys = new ArrayList<KeyDescriptor<?>>();

  protected KeyDescriptorString kPortId;
  protected KeyDescriptorString kPortName;
  protected KeyDescriptorString kDescription;
  protected KeyDescriptorNumberMinMax kProductId;
  protected KeyDescriptorNumberMinMax kVendorId;
  protected KeyDescriptorString kProductName;
  protected KeyDescriptorString kManufacturer;
  protected KeyDescriptorString kSerialNumber;

  /**
   * Constructs an empty SerialPortDescriptor.
   *
   * <p>This constructor initializes the descriptor with default values and loads the key
   * descriptors for validation and type conversion.
   */
  public SerialPortDescriptor() {
    super();
    this.loadKeyDescriptors();
  }

  /**
   * Constructs a SerialPortDescriptor from a map of key-value pairs.
   *
   * <p>This constructor creates a descriptor by copying values from the provided map. The map
   * should contain keys corresponding to the descriptor properties.
   *
   * @param map the map containing serial port descriptor parameters
   * @throws DataDictionaryException if the map contains invalid values
   */
  public SerialPortDescriptor(Map<String, Object> map) throws DataDictionaryException {
    this();
    this.copy(fromMap(map));
  }

  /**
   * Constructs a SerialPortDescriptor by copying from another DataDictionary.
   *
   * <p>This constructor creates a new descriptor by copying all values from the provided
   * DataDictionary. This is useful for creating descriptors from existing data structures.
   *
   * @param other the DataDictionary to copy descriptor data from
   * @throws DataDictionaryException if the source dictionary contains invalid values
   */
  public SerialPortDescriptor(DataDictionary other) throws DataDictionaryException {
    this();
    this.copy(other);
  }

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
   * @throws DataDictionaryException if any parameter is invalid or out of range
   */
  public SerialPortDescriptor(
      String portId,
      String portName,
      String description,
      Number productId,
      Number vendorId,
      String productName,
      String manufacturer,
      String serialNumber)
      throws DataDictionaryException {
    this();

    this.setPortId(portId);
    this.setPortName(portName);
    this.setDescription(description);
    this.setProductId(productId);
    this.setVendorId(vendorId);
    this.setProductName(productName);
    this.setManufacturer(manufacturer);
    this.setSerialNumber(serialNumber);

    this.checkAndUpdate();
  }

  /**
   * Returns the port unique identifier.
   *
   * @return the port unique identifier
   */
  public String getPortId() {
    return (String) this.data.get(KEY_PORT_ID);
  }

  /**
   * Returns the port name used to open the serial port.
   *
   * @return the port name used to open the serial port
   */
  public String getPortName() {
    return (String) this.data.get(KEY_PORT_NAME);
  }

  /**
   * Returns the port description.
   *
   * @return the port description
   */
  public String getDescription() {
    return (String) this.data.get(KEY_DESCRIPTION);
  }

  /**
   * Returns the USB device product identifier (PID).
   *
   * @return the USB device product identifier in range [0-65535]
   */
  public Number getProductId() {
    return (Number) this.data.get(KEY_PRODUCT_ID);
  }

  /**
   * Returns the USB device vendor identifier (VID).
   *
   * @return the USB device vendor identifier in range [0-65535]
   */
  public Number getVendorId() {
    return (Number) this.data.get(KEY_VENDOR_ID);
  }

  /**
   * Returns the USB device product name.
   *
   * @return the USB device product name
   */
  public String getProductName() {
    return (String) this.data.get(KEY_PRODUCT_NAME);
  }

  /**
   * Returns the USB device manufacturer.
   *
   * @return the USB device manufacturer
   */
  public String getManufacturer() {
    return (String) this.data.get(KEY_MANUFACTURER);
  }

  /**
   * Returns the USB device serial number.
   *
   * @return the USB device serial number
   */
  public String getSerialNumber() {
    return (String) this.data.get(KEY_SERIAL_NUMBER);
  }

  /**
   * Sets the port unique identifier.
   *
   * @param portId the port unique identifier
   * @throws DataDictionaryException if portId is invalid
   */
  public void setPortId(String portId) throws DataDictionaryException {
    this.setPortId((Object) portId);
  }

  /**
   * Sets the port name used to open the serial port.
   *
   * @param portName the port name used to open the serial port
   * @throws DataDictionaryException if portName is invalid
   */
  public void setPortName(String portName) throws DataDictionaryException {
    this.setPortName((Object) portName);
  }

  /**
   * Sets the port description.
   *
   * @param description the port description
   * @throws DataDictionaryException if description is invalid
   */
  public void setDescription(String description) throws DataDictionaryException {
    this.setDescription((Object) description);
  }

  /**
   * Sets the USB device product identifier (PID).
   *
   * @param productId the USB device product identifier
   * @throws DataDictionaryException if productId is out of range [0-65535]
   */
  public void setProductId(Number productId) throws DataDictionaryException {
    this.setProductId((Object) productId);
  }

  /**
   * Sets the USB device vendor identifier (VID).
   *
   * @param vendorId the USB device vendor identifier
   * @throws DataDictionaryException if vendorId is out of range [0-65535]
   */
  public void setVendorId(Number vendorId) throws DataDictionaryException {
    this.setVendorId((Object) vendorId);
  }

  /**
   * Sets the USB device product name.
   *
   * @param productName the USB device product name
   * @throws DataDictionaryException if productName is invalid
   */
  public void setProductName(String productName) throws DataDictionaryException {
    this.setProductName((Object) productName);
  }

  /**
   * Sets the USB device manufacturer.
   *
   * @param manufacturer the USB device manufacturer
   * @throws DataDictionaryException if manufacturer is invalid
   */
  public void setManufacturer(String manufacturer) throws DataDictionaryException {
    this.setManufacturer((Object) manufacturer);
  }

  /**
   * Sets the USB device serial number.
   *
   * @param serialNumber the USB device serial number
   * @throws DataDictionaryException if serialNumber is invalid
   */
  public void setSerialNumber(String serialNumber) throws DataDictionaryException {
    this.setSerialNumber((Object) serialNumber);
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
    return this.getPortName() != null && !this.getPortName().isEmpty() && this.getPortId() == null;
  }

  /**
   * Sets the port ID using object conversion and validation.
   *
   * @param portId the port ID object to convert and set
   * @throws DataDictionaryException if the conversion or validation fails
   */
  protected void setPortId(Object portId) throws DataDictionaryException {
    this.data.put(KEY_PORT_ID, this.kPortId.convert(portId));
  }

  /**
   * Sets the port name using object conversion and validation.
   *
   * @param portName the port name object to convert and set
   * @throws DataDictionaryException if the conversion or validation fails
   */
  protected void setPortName(Object portName) throws DataDictionaryException {
    this.data.put(KEY_PORT_NAME, this.kPortName.convert(portName));
  }

  /**
   * Sets the description using object conversion and validation.
   *
   * @param description the description object to convert and set
   * @throws DataDictionaryException if the conversion or validation fails
   */
  protected void setDescription(Object description) throws DataDictionaryException {
    this.data.put(KEY_DESCRIPTION, this.kDescription.convert(description));
  }

  /**
   * Sets the product ID using object conversion and validation.
   *
   * @param productId the product ID object to convert and set
   * @throws DataDictionaryException if the conversion or validation fails
   */
  protected void setProductId(Object productId) throws DataDictionaryException {
    this.data.put(KEY_PRODUCT_ID, this.kProductId.convert(productId));
  }

  /**
   * Sets the vendor ID using object conversion and validation.
   *
   * @param vendorId the vendor ID object to convert and set
   * @throws DataDictionaryException if the conversion or validation fails
   */
  protected void setVendorId(Object vendorId) throws DataDictionaryException {
    this.data.put(KEY_VENDOR_ID, this.kVendorId.convert(vendorId));
  }

  /**
   * Sets the product name using object conversion and validation.
   *
   * @param productName the product name object to convert and set
   * @throws DataDictionaryException if the conversion or validation fails
   */
  protected void setProductName(Object productName) throws DataDictionaryException {
    this.data.put(KEY_PRODUCT_NAME, this.kProductName.convert(productName));
  }

  /**
   * Sets the manufacturer using object conversion and validation.
   *
   * @param manufacturer the manufacturer object to convert and set
   * @throws DataDictionaryException if the conversion or validation fails
   */
  protected void setManufacturer(Object manufacturer) throws DataDictionaryException {
    this.data.put(KEY_MANUFACTURER, this.kManufacturer.convert(manufacturer));
  }

  /**
   * Sets the serial number using object conversion and validation.
   *
   * @param serialNumber the serial number object to convert and set
   * @throws DataDictionaryException if the conversion or validation fails
   */
  protected void setSerialNumber(Object serialNumber) throws DataDictionaryException {
    this.data.put(KEY_SERIAL_NUMBER, this.kSerialNumber.convert(serialNumber));
  }

  /**
   * Loads and initializes the key descriptors for descriptor validation.
   *
   * <p>This private method sets up the key descriptors that define the structure and validation
   * rules for all serial port descriptor properties. It creates descriptors for both string and
   * numeric fields with appropriate validation constraints (e.g., product and vendor IDs must be in
   * range [0-65535]).
   *
   * <p>If any descriptor creation fails, a RuntimeException is thrown with the original exception
   * as the cause.
   */
  private void loadKeyDescriptors() {
    try {
      this.kPortId = new KeyDescriptorString(KEY_PORT_ID, false, false);
      this.keys.add(this.kPortId);

      this.kPortName = new KeyDescriptorString(KEY_PORT_NAME, false, false);
      this.keys.add(this.kPortName);

      this.kDescription = new KeyDescriptorString(KEY_DESCRIPTION, false, false);
      this.keys.add(this.kDescription);

      this.kProductId =
          new KeyDescriptorNumberMinMax(KEY_PRODUCT_ID, false, PRODUCT_ID_MIN, PRODUCT_ID_MAX);
      this.keys.add(this.kProductId);

      this.kVendorId =
          new KeyDescriptorNumberMinMax(KEY_VENDOR_ID, false, VENDOR_ID_MIN, VENDOR_ID_MAX);
      this.keys.add(this.kVendorId);

      this.kProductName = new KeyDescriptorString(KEY_PRODUCT_NAME, false, false);
      this.keys.add(this.kProductName);

      this.kManufacturer = new KeyDescriptorString(KEY_MANUFACTURER, false, false);
      this.keys.add(this.kManufacturer);

      this.kSerialNumber = new KeyDescriptorString(KEY_SERIAL_NUMBER, false, false);
      this.keys.add(this.kSerialNumber);

      this.addAllKeyDescriptor(this.keys);
    } catch (DataDictionaryException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }
}
