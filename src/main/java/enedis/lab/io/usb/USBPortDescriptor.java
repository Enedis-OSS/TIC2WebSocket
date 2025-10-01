// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.io.usb;

import enedis.lab.types.DataDictionary;
import enedis.lab.types.DataDictionaryException;
import enedis.lab.types.datadictionary.DataDictionaryBase;
import enedis.lab.types.datadictionary.KeyDescriptor;
import enedis.lab.types.datadictionary.KeyDescriptorNumber;
import enedis.lab.types.datadictionary.KeyDescriptorString;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * USBPortDescriptor class
 *
 * <p>Generated
 */
public class USBPortDescriptor extends DataDictionaryBase {
  ///
  // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  ///
  /// CONSTANTS
  ///
  ///
  // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  protected static final String KEY_BCD_DEVICE = "bcdDevice";
  protected static final String KEY_BCD_USB = "bcdUSB";
  protected static final String KEY_B_DESCRIPTOR_TYPE = "bDescriptorType";
  protected static final String KEY_B_DEVICE_CLASS = "bDeviceClass";
  protected static final String KEY_B_DEVICE_PROTOCOL = "bDeviceProtocol";
  protected static final String KEY_B_DEVICE_SUB_CLASS = "bDeviceSubClass";
  protected static final String KEY_B_LENGTH = "bLength";
  protected static final String KEY_B_MAX_PACKET_SIZE0 = "bMaxPacketSize0";
  protected static final String KEY_B_NUM_CONFIGURATIONS = "bNumConfigurations";
  protected static final String KEY_ID_PRODUCT = "idProduct";
  protected static final String KEY_ID_VENDOR = "idVendor";
  protected static final String KEY_I_MANUFACTURER = "iManufacturer";
  protected static final String KEY_I_PRODUCT = "iProduct";
  protected static final String KEY_I_SERIAL_NUMBER = "iSerialNumber";
  protected static final String KEY_MANUFACTURER = "manufacturer";
  protected static final String KEY_PRODUCT = "product";
  protected static final String KEY_SERIAL_NUMBER = "serialNumber";

  ///
  // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  ///
  /// TYPES
  ///
  ///
  // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  ///
  // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  ///
  /// STATIC METHODS
  ///
  ///
  // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  ///
  // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  ///
  /// ATTRIBUTES
  ///
  ///
  // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  private List<KeyDescriptor<?>> keys = new ArrayList<KeyDescriptor<?>>();

  protected KeyDescriptorNumber kBcdDevice;
  protected KeyDescriptorNumber kBcdUSB;
  protected KeyDescriptorNumber kBDescriptorType;
  protected KeyDescriptorNumber kBDeviceClass;
  protected KeyDescriptorNumber kBDeviceProtocol;
  protected KeyDescriptorNumber kBDeviceSubClass;
  protected KeyDescriptorNumber kBLength;
  protected KeyDescriptorNumber kBMaxPacketSize0;
  protected KeyDescriptorNumber kBNumConfigurations;
  protected KeyDescriptorNumber kIdProduct;
  protected KeyDescriptorNumber kIdVendor;
  protected KeyDescriptorNumber kIManufacturer;
  protected KeyDescriptorNumber kIProduct;
  protected KeyDescriptorNumber kISerialNumber;
  protected KeyDescriptorString kManufacturer;
  protected KeyDescriptorString kProduct;
  protected KeyDescriptorString kSerialNumber;

  ///
  // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  ///
  /// CONSTRUCTORS
  ///
  ///
  // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  protected USBPortDescriptor() {
    super();
    this.loadKeyDescriptors();
  }

  /**
   * Constructor using map
   *
   * @param map
   * @throws DataDictionaryException
   */
  public USBPortDescriptor(Map<String, Object> map) throws DataDictionaryException {
    this();
    this.copy(fromMap(map));
  }

  /**
   * Constructor using datadictionary
   *
   * @param other
   * @throws DataDictionaryException
   */
  public USBPortDescriptor(DataDictionary other) throws DataDictionaryException {
    this();
    this.copy(other);
  }

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
   * @throws DataDictionaryException
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
      String serialNumber)
      throws DataDictionaryException {
    this();

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

    this.checkAndUpdate();
  }

  ///
  // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  ///
  /// INTERFACE
  /// DataDictionaryBase
  ///
  ///
  // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  @Override
  protected void updateOptionalParameters() throws DataDictionaryException {
    super.updateOptionalParameters();
  }

  ///
  // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  ///
  /// PUBLIC METHODS
  ///
  ///
  // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Get bcd device
   *
   * @return the bcd device
   */
  public Number getBcdDevice() {
    return (Number) this.data.get(KEY_BCD_DEVICE);
  }

  /**
   * Get bcd u s b
   *
   * @return the bcd u s b
   */
  public Number getBcdUSB() {
    return (Number) this.data.get(KEY_BCD_USB);
  }

  /**
   * Get b descriptor type
   *
   * @return the b descriptor type
   */
  public Number getBDescriptorType() {
    return (Number) this.data.get(KEY_B_DESCRIPTOR_TYPE);
  }

  /**
   * Get b device class
   *
   * @return the b device class
   */
  public Number getBDeviceClass() {
    return (Number) this.data.get(KEY_B_DEVICE_CLASS);
  }

  /**
   * Get b device protocol
   *
   * @return the b device protocol
   */
  public Number getBDeviceProtocol() {
    return (Number) this.data.get(KEY_B_DEVICE_PROTOCOL);
  }

  /**
   * Get b device sub class
   *
   * @return the b device sub class
   */
  public Number getBDeviceSubClass() {
    return (Number) this.data.get(KEY_B_DEVICE_SUB_CLASS);
  }

  /**
   * Get b length
   *
   * @return the b length
   */
  public Number getBLength() {
    return (Number) this.data.get(KEY_B_LENGTH);
  }

  /**
   * Get b max packet size0
   *
   * @return the b max packet size0
   */
  public Number getBMaxPacketSize0() {
    return (Number) this.data.get(KEY_B_MAX_PACKET_SIZE0);
  }

  /**
   * Get b num configurations
   *
   * @return the b num configurations
   */
  public Number getBNumConfigurations() {
    return (Number) this.data.get(KEY_B_NUM_CONFIGURATIONS);
  }

  /**
   * Get id product
   *
   * @return the id product
   */
  public Number getIdProduct() {
    return (Number) this.data.get(KEY_ID_PRODUCT);
  }

  /**
   * Get id vendor
   *
   * @return the id vendor
   */
  public Number getIdVendor() {
    return (Number) this.data.get(KEY_ID_VENDOR);
  }

  /**
   * Get i manufacturer
   *
   * @return the i manufacturer
   */
  public Number getIManufacturer() {
    return (Number) this.data.get(KEY_I_MANUFACTURER);
  }

  /**
   * Get i product
   *
   * @return the i product
   */
  public Number getIProduct() {
    return (Number) this.data.get(KEY_I_PRODUCT);
  }

  /**
   * Get i serial number
   *
   * @return the i serial number
   */
  public Number getISerialNumber() {
    return (Number) this.data.get(KEY_I_SERIAL_NUMBER);
  }

  /**
   * Get manufacturer
   *
   * @return the manufacturer
   */
  public String getManufacturer() {
    return (String) this.data.get(KEY_MANUFACTURER);
  }

  /**
   * Get product
   *
   * @return the product
   */
  public String getProduct() {
    return (String) this.data.get(KEY_PRODUCT);
  }

  /**
   * Get serial number
   *
   * @return the serial number
   */
  public String getSerialNumber() {
    return (String) this.data.get(KEY_SERIAL_NUMBER);
  }

  /**
   * Set bcd device
   *
   * @param bcdDevice
   * @throws DataDictionaryException
   */
  public void setBcdDevice(Number bcdDevice) throws DataDictionaryException {
    this.setBcdDevice((Object) bcdDevice);
  }

  /**
   * Set bcd u s b
   *
   * @param bcdUSB
   * @throws DataDictionaryException
   */
  public void setBcdUSB(Number bcdUSB) throws DataDictionaryException {
    this.setBcdUSB((Object) bcdUSB);
  }

  /**
   * Set b descriptor type
   *
   * @param bDescriptorType
   * @throws DataDictionaryException
   */
  public void setBDescriptorType(Number bDescriptorType) throws DataDictionaryException {
    this.setBDescriptorType((Object) bDescriptorType);
  }

  /**
   * Set b device class
   *
   * @param bDeviceClass
   * @throws DataDictionaryException
   */
  public void setBDeviceClass(Number bDeviceClass) throws DataDictionaryException {
    this.setBDeviceClass((Object) bDeviceClass);
  }

  /**
   * Set b device protocol
   *
   * @param bDeviceProtocol
   * @throws DataDictionaryException
   */
  public void setBDeviceProtocol(Number bDeviceProtocol) throws DataDictionaryException {
    this.setBDeviceProtocol((Object) bDeviceProtocol);
  }

  /**
   * Set b device sub class
   *
   * @param bDeviceSubClass
   * @throws DataDictionaryException
   */
  public void setBDeviceSubClass(Number bDeviceSubClass) throws DataDictionaryException {
    this.setBDeviceSubClass((Object) bDeviceSubClass);
  }

  /**
   * Set b length
   *
   * @param bLength
   * @throws DataDictionaryException
   */
  public void setBLength(Number bLength) throws DataDictionaryException {
    this.setBLength((Object) bLength);
  }

  /**
   * Set b max packet size0
   *
   * @param bMaxPacketSize0
   * @throws DataDictionaryException
   */
  public void setBMaxPacketSize0(Number bMaxPacketSize0) throws DataDictionaryException {
    this.setBMaxPacketSize0((Object) bMaxPacketSize0);
  }

  /**
   * Set b num configurations
   *
   * @param bNumConfigurations
   * @throws DataDictionaryException
   */
  public void setBNumConfigurations(Number bNumConfigurations) throws DataDictionaryException {
    this.setBNumConfigurations((Object) bNumConfigurations);
  }

  /**
   * Set id product
   *
   * @param idProduct
   * @throws DataDictionaryException
   */
  public void setIdProduct(Number idProduct) throws DataDictionaryException {
    this.setIdProduct((Object) idProduct);
  }

  /**
   * Set id vendor
   *
   * @param idVendor
   * @throws DataDictionaryException
   */
  public void setIdVendor(Number idVendor) throws DataDictionaryException {
    this.setIdVendor((Object) idVendor);
  }

  /**
   * Set i manufacturer
   *
   * @param iManufacturer
   * @throws DataDictionaryException
   */
  public void setIManufacturer(Number iManufacturer) throws DataDictionaryException {
    this.setIManufacturer((Object) iManufacturer);
  }

  /**
   * Set i product
   *
   * @param iProduct
   * @throws DataDictionaryException
   */
  public void setIProduct(Number iProduct) throws DataDictionaryException {
    this.setIProduct((Object) iProduct);
  }

  /**
   * Set i serial number
   *
   * @param iSerialNumber
   * @throws DataDictionaryException
   */
  public void setISerialNumber(Number iSerialNumber) throws DataDictionaryException {
    this.setISerialNumber((Object) iSerialNumber);
  }

  /**
   * Set manufacturer
   *
   * @param manufacturer
   * @throws DataDictionaryException
   */
  public void setManufacturer(String manufacturer) throws DataDictionaryException {
    this.setManufacturer((Object) manufacturer);
  }

  /**
   * Set product
   *
   * @param product
   * @throws DataDictionaryException
   */
  public void setProduct(String product) throws DataDictionaryException {
    this.setProduct((Object) product);
  }

  /**
   * Set serial number
   *
   * @param serialNumber
   * @throws DataDictionaryException
   */
  public void setSerialNumber(String serialNumber) throws DataDictionaryException {
    this.setSerialNumber((Object) serialNumber);
  }

  ///
  // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  ///
  /// PROTECTED METHODS
  ///
  ///
  // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  protected void setBcdDevice(Object bcdDevice) throws DataDictionaryException {
    this.data.put(KEY_BCD_DEVICE, this.kBcdDevice.convert(bcdDevice));
  }

  protected void setBcdUSB(Object bcdUSB) throws DataDictionaryException {
    this.data.put(KEY_BCD_USB, this.kBcdUSB.convert(bcdUSB));
  }

  protected void setBDescriptorType(Object bDescriptorType) throws DataDictionaryException {
    this.data.put(KEY_B_DESCRIPTOR_TYPE, this.kBDescriptorType.convert(bDescriptorType));
  }

  protected void setBDeviceClass(Object bDeviceClass) throws DataDictionaryException {
    this.data.put(KEY_B_DEVICE_CLASS, this.kBDeviceClass.convert(bDeviceClass));
  }

  protected void setBDeviceProtocol(Object bDeviceProtocol) throws DataDictionaryException {
    this.data.put(KEY_B_DEVICE_PROTOCOL, this.kBDeviceProtocol.convert(bDeviceProtocol));
  }

  protected void setBDeviceSubClass(Object bDeviceSubClass) throws DataDictionaryException {
    this.data.put(KEY_B_DEVICE_SUB_CLASS, this.kBDeviceSubClass.convert(bDeviceSubClass));
  }

  protected void setBLength(Object bLength) throws DataDictionaryException {
    this.data.put(KEY_B_LENGTH, this.kBLength.convert(bLength));
  }

  protected void setBMaxPacketSize0(Object bMaxPacketSize0) throws DataDictionaryException {
    this.data.put(KEY_B_MAX_PACKET_SIZE0, this.kBMaxPacketSize0.convert(bMaxPacketSize0));
  }

  protected void setBNumConfigurations(Object bNumConfigurations) throws DataDictionaryException {
    this.data.put(KEY_B_NUM_CONFIGURATIONS, this.kBNumConfigurations.convert(bNumConfigurations));
  }

  protected void setIdProduct(Object idProduct) throws DataDictionaryException {
    this.data.put(KEY_ID_PRODUCT, this.kIdProduct.convert(idProduct));
  }

  protected void setIdVendor(Object idVendor) throws DataDictionaryException {
    this.data.put(KEY_ID_VENDOR, this.kIdVendor.convert(idVendor));
  }

  protected void setIManufacturer(Object iManufacturer) throws DataDictionaryException {
    this.data.put(KEY_I_MANUFACTURER, this.kIManufacturer.convert(iManufacturer));
  }

  protected void setIProduct(Object iProduct) throws DataDictionaryException {
    this.data.put(KEY_I_PRODUCT, this.kIProduct.convert(iProduct));
  }

  protected void setISerialNumber(Object iSerialNumber) throws DataDictionaryException {
    this.data.put(KEY_I_SERIAL_NUMBER, this.kISerialNumber.convert(iSerialNumber));
  }

  protected void setManufacturer(Object manufacturer) throws DataDictionaryException {
    this.data.put(KEY_MANUFACTURER, this.kManufacturer.convert(manufacturer));
  }

  protected void setProduct(Object product) throws DataDictionaryException {
    this.data.put(KEY_PRODUCT, this.kProduct.convert(product));
  }

  protected void setSerialNumber(Object serialNumber) throws DataDictionaryException {
    this.data.put(KEY_SERIAL_NUMBER, this.kSerialNumber.convert(serialNumber));
  }

  ///
  // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  ///
  /// PRIVATE METHODS
  ///
  ///
  // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  private void loadKeyDescriptors() {
    try {
      this.kBcdDevice = new KeyDescriptorNumber(KEY_BCD_DEVICE, true);
      this.keys.add(this.kBcdDevice);

      this.kBcdUSB = new KeyDescriptorNumber(KEY_BCD_USB, true);
      this.keys.add(this.kBcdUSB);

      this.kBDescriptorType = new KeyDescriptorNumber(KEY_B_DESCRIPTOR_TYPE, true);
      this.keys.add(this.kBDescriptorType);

      this.kBDeviceClass = new KeyDescriptorNumber(KEY_B_DEVICE_CLASS, true);
      this.keys.add(this.kBDeviceClass);

      this.kBDeviceProtocol = new KeyDescriptorNumber(KEY_B_DEVICE_PROTOCOL, true);
      this.keys.add(this.kBDeviceProtocol);

      this.kBDeviceSubClass = new KeyDescriptorNumber(KEY_B_DEVICE_SUB_CLASS, true);
      this.keys.add(this.kBDeviceSubClass);

      this.kBLength = new KeyDescriptorNumber(KEY_B_LENGTH, true);
      this.keys.add(this.kBLength);

      this.kBMaxPacketSize0 = new KeyDescriptorNumber(KEY_B_MAX_PACKET_SIZE0, true);
      this.keys.add(this.kBMaxPacketSize0);

      this.kBNumConfigurations = new KeyDescriptorNumber(KEY_B_NUM_CONFIGURATIONS, true);
      this.keys.add(this.kBNumConfigurations);

      this.kIdProduct = new KeyDescriptorNumber(KEY_ID_PRODUCT, true);
      this.keys.add(this.kIdProduct);

      this.kIdVendor = new KeyDescriptorNumber(KEY_ID_VENDOR, true);
      this.keys.add(this.kIdVendor);

      this.kIManufacturer = new KeyDescriptorNumber(KEY_I_MANUFACTURER, true);
      this.keys.add(this.kIManufacturer);

      this.kIProduct = new KeyDescriptorNumber(KEY_I_PRODUCT, true);
      this.keys.add(this.kIProduct);

      this.kISerialNumber = new KeyDescriptorNumber(KEY_I_SERIAL_NUMBER, true);
      this.keys.add(this.kISerialNumber);

      this.kManufacturer = new KeyDescriptorString(KEY_MANUFACTURER, false, true);
      this.keys.add(this.kManufacturer);

      this.kProduct = new KeyDescriptorString(KEY_PRODUCT, false, true);
      this.keys.add(this.kProduct);

      this.kSerialNumber = new KeyDescriptorString(KEY_SERIAL_NUMBER, false, true);
      this.keys.add(this.kSerialNumber);

      this.addAllKeyDescriptor(this.keys);
    } catch (DataDictionaryException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }
}
