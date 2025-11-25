// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.io.modem;

import tic.io.serialport.SerialPortDescriptor;
import tic.io.usb.USBPortDescriptor;

/** Descriptor of a TIC port */
public class TICPortDescriptor extends SerialPortDescriptor {
  private TICModemType modemType;

  protected TICPortDescriptor() {
    super();
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
   */
  public TICPortDescriptor(
      String portId,
      String portName,
      String description,
      String productName,
      String manufacturer,
      String serialNumber,
      TICModemType modemType) {
    this();

    this.setPortId(portId);
    this.setPortName(portName);
    this.setDescription(description);
    this.setProductName(productName);
    this.setManufacturer(manufacturer);
    this.setSerialNumber(serialNumber);
    this.setModemType(modemType);
  }

  /**
   * Constructor setting parameters to specific values
   *
   * @param serialPortDescriptor
   * @param modemType
   */
  public TICPortDescriptor(SerialPortDescriptor serialPortDescriptor, TICModemType modemType) {
    this();
  }

  /**
   * Constructor setting parameters to specific values for legacy descriptors
   *
   * @param serialPortDescriptor legacy descriptor from the historical API
   * @param modemType modem type
   */
  public TICPortDescriptor(SerialPortDescriptor serialPortDescriptor, TICModemType modemType) {
    this();
    if (serialPortDescriptor == null) {
      throw new IllegalArgumentException("Serial port descriptor cannot be null");
    }

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
  }

  /**
   * Constructor setting parameters to specific values
   *
   * @param usbPortDescriptor
   * @param modemType
   */
  public TICPortDescriptor(USBPortDescriptor usbPortDescriptor, TICModemType modemType) {
    this();

    if (usbPortDescriptor == null) {
      throw new IllegalArgumentException("USB port descriptor cannot be null");
    }

    this.checkProductId(usbPortDescriptor.getIdProduct(), modemType);
    this.checkVendorId(usbPortDescriptor.getIdVendor(), modemType);

    this.setProductId(usbPortDescriptor.getIdProduct());
    this.setVendorId(usbPortDescriptor.getIdVendor());
    this.setProductName(usbPortDescriptor.getProduct());
    this.setManufacturer(usbPortDescriptor.getManufacturer());
    this.setSerialNumber(usbPortDescriptor.getSerialNumber());
    this.setModemType(modemType);
  }

  /**
   * Get modem type
   *
   * @return the modem type
   */
  public TICModemType getModemType() {
    return this.modemType;
  }

  /**
   * Set modem type
   *
   * @param modemType
   */
  public void setModemType(TICModemType modemType) {
    this.modemType = modemType;
    if (modemType != null) {
      this.setProductId(modemType.getProductId());
      this.setVendorId(modemType.getVendorId());
    } else {
      this.setProductId(null);
      this.setVendorId(null);
    }
  }

  private void checkProductId(Number productId, TICModemType modemType) {
    if (modemType != null
        && productId != null
        && productId.intValue() != modemType.getProductId()) {
      throw new IllegalArgumentException("TIC modem productId is inconsistent with the given one");
    }
  }

  private void checkVendorId(Number vendorId, TICModemType modemType) {
    if (modemType != null && vendorId != null && vendorId.intValue() != modemType.getVendorId()) {
      throw new IllegalArgumentException("TIC modem vendorId is inconsistent with the given one");
    }
  }
}
