// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.io.tic;

import enedis.lab.io.serialport.SerialPortDescriptor;
import enedis.lab.io.usb.USBPortDescriptor;
import enedis.lab.types.DataDictionary;
import enedis.lab.types.DataDictionaryException;
import enedis.lab.types.datadictionary.KeyDescriptor;
import enedis.lab.types.datadictionary.KeyDescriptorEnum;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TICPortDescriptor class
 *
 * <p>Generated
 */
public class TICPortDescriptor extends SerialPortDescriptor {
  protected static final String KEY_MODEM_TYPE = "modemType";

  private List<KeyDescriptor<?>> keys = new ArrayList<KeyDescriptor<?>>();

  protected KeyDescriptorEnum<TICModemType> kModemType;

  protected TICPortDescriptor() {
    super();
    this.loadKeyDescriptors();
  }

  /**
   * Constructor using map
   *
   * @param map
   * @throws DataDictionaryException
   */
  public TICPortDescriptor(Map<String, Object> map) throws DataDictionaryException {
    this();
    this.copy(fromMap(map));
  }

  /**
   * Constructor using datadictionary
   *
   * @param other
   * @throws DataDictionaryException
   */
  public TICPortDescriptor(DataDictionary other) throws DataDictionaryException {
    this();
    this.copy(other);
  }

  /**
   * Constructor setting parameters to specific values
   *
   * @param portId
   * @param portName
   * @param description
   * @param productName
   * @param manufacturer
   * @param serialNumber
   * @param modemType
   * @throws DataDictionaryException
   */
  public TICPortDescriptor(
      String portId,
      String portName,
      String description,
      String productName,
      String manufacturer,
      String serialNumber,
      TICModemType modemType)
      throws DataDictionaryException {
    this();

    this.setPortId(portId);
    this.setPortName(portName);
    this.setDescription(description);
    this.setProductName(productName);
    this.setManufacturer(manufacturer);
    this.setSerialNumber(serialNumber);
    this.setModemType(modemType);

    this.checkAndUpdate();
  }

  /**
   * Constructor setting parameters to specific values
   *
   * @param serialPortDescriptor
   * @param modemType
   * @throws DataDictionaryException
   */
  public TICPortDescriptor(SerialPortDescriptor serialPortDescriptor, TICModemType modemType)
      throws DataDictionaryException {
    this();

    this.checkProductId(serialPortDescriptor.getProductId(), modemType);
    this.checkVendorId(serialPortDescriptor.getVendorId(), modemType);

    this.setPortId(serialPortDescriptor.getPortId());
    this.setPortName(serialPortDescriptor.getPortName());
    this.setDescription(serialPortDescriptor.getDescription());
    this.setProductId(serialPortDescriptor.getProductId());
    this.setVendorId(serialPortDescriptor.getVendorId());
    this.setProductName(serialPortDescriptor.getProductName());
    this.setManufacturer(serialPortDescriptor.getManufacturer());
    this.setSerialNumber(serialPortDescriptor.getSerialNumber());
    this.setModemType(modemType);

    this.checkAndUpdate();
  }

  /**
   * Constructor setting parameters to specific values
   *
   * @param usbPortDescriptor
   * @param modemType
   * @throws DataDictionaryException
   */
  public TICPortDescriptor(USBPortDescriptor usbPortDescriptor, TICModemType modemType)
      throws DataDictionaryException {
    this();

    this.checkProductId(usbPortDescriptor.getIdProduct(), modemType);
    this.checkVendorId(usbPortDescriptor.getIdVendor(), modemType);

    this.setProductId(usbPortDescriptor.getIdProduct());
    this.setVendorId(usbPortDescriptor.getIdVendor());
    this.setProductName(usbPortDescriptor.getProduct());
    this.setManufacturer(usbPortDescriptor.getManufacturer());
    this.setSerialNumber(usbPortDescriptor.getSerialNumber());
    this.setModemType(modemType);

    this.checkAndUpdate();
  }

  @Override
  protected void updateOptionalParameters() throws DataDictionaryException {
    if (this.exists(KEY_MODEM_TYPE)) {
      if (this.getModemType() != null) {
        this.setProductId(this.getModemType().getProductId());
        this.setVendorId(this.getModemType().getVendorId());
      } else {
        this.setProductId(null);
        this.setVendorId(null);
      }
    }
    super.updateOptionalParameters();
  }

  /**
   * Get modem type
   *
   * @return the modem type
   */
  public TICModemType getModemType() {
    return (TICModemType) this.data.get(KEY_MODEM_TYPE);
  }

  /**
   * Set modem type
   *
   * @param modemType
   * @throws DataDictionaryException
   */
  public void setModemType(TICModemType modemType) throws DataDictionaryException {
    this.setModemType((Object) modemType);
    if (modemType != null) {
      this.setProductId(modemType.getProductId());
      this.setVendorId(modemType.getVendorId());
    } else {
      this.setProductId(null);
      this.setVendorId(null);
    }
  }

  protected void setModemType(Object modemType) throws DataDictionaryException {
    if (modemType == null) {
      this.data.put(KEY_MODEM_TYPE, null);
    } else {
      this.data.put(KEY_MODEM_TYPE, this.kModemType.convert(modemType));
    }
  }

  private void checkProductId(Number productId, TICModemType modemType)
      throws DataDictionaryException {
    if (modemType != null
        && productId != null
        && productId.intValue() != modemType.getProductId()) {
      throw new DataDictionaryException("TIC modem productId is inconsistent with the given one");
    }
  }

  private void checkVendorId(Number vendorId, TICModemType modemType)
      throws DataDictionaryException {
    if (modemType != null && vendorId != null && vendorId.intValue() != modemType.getVendorId()) {
      throw new DataDictionaryException("TIC modem vendorId is inconsistent with the given one");
    }
  }

  private void loadKeyDescriptors() {
    try {
      this.kModemType =
          new KeyDescriptorEnum<TICModemType>(KEY_MODEM_TYPE, false, TICModemType.class);
      this.keys.add(this.kModemType);

      this.addAllKeyDescriptor(this.keys);
    } catch (DataDictionaryException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }
}
