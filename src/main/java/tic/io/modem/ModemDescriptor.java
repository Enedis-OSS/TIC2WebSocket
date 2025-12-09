// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.io.modem;

import java.util.Objects;
import tic.io.serialport.SerialPortDescriptor;
import tic.io.usb.USBPortDescriptor;

/** Descriptor of a modem. */
public class ModemDescriptor extends SerialPortDescriptor {
  private ModemType modemType;

  protected ModemDescriptor() {
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
  public ModemDescriptor(
      String portId,
      String portName,
      String description,
      String productName,
      String manufacturer,
      String serialNumber,
      ModemType modemType) {
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
   * Constructor setting parameters to specific values for legacy descriptors
   *
   * @param serialPortDescriptor legacy descriptor from the historical API
   * @param modemType modem type
   */
  public ModemDescriptor(SerialPortDescriptor serialPortDescriptor, ModemType modemType) {
    this();
    if (serialPortDescriptor == null) {
      throw new IllegalArgumentException("Serial port descriptor cannot be null");
    }

    this.checkProductId(serialPortDescriptor.getProductId(), modemType);
    this.checkVendorId(serialPortDescriptor.getVendorId(), modemType);

    this.setPortId(serialPortDescriptor.getPortId());
    this.setPortName(serialPortDescriptor.getPortName());
    this.setDescription(serialPortDescriptor.getDescription());
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
  public ModemDescriptor(USBPortDescriptor usbPortDescriptor, ModemType modemType) {
    this();

    if (usbPortDescriptor == null) {
      throw new IllegalArgumentException("USB port descriptor cannot be null");
    }

    this.checkProductId(usbPortDescriptor.getIdProduct(), modemType);
    this.checkVendorId(usbPortDescriptor.getIdVendor(), modemType);

    this.setProductName(usbPortDescriptor.getProduct());
    this.setManufacturer(usbPortDescriptor.getManufacturer());
    this.setSerialNumber(usbPortDescriptor.getSerialNumber());
    this.setModemType(modemType);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof ModemDescriptor)) {
      return false;
    }
    if (!super.equals(obj)) {
      return false;
    }
    ModemDescriptor other = (ModemDescriptor) obj;
    return this.modemType == other.modemType;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), this.modemType);
  }

  /**
   * Get modem type
   *
   * @return the modem type
   */
  public ModemType getModemType() {
    return this.modemType;
  }

  /**
   * Set modem type
   *
   * @param modemType
   */
  public void setModemType(ModemType modemType) {
    this.modemType = modemType;
    if (modemType != null) {
      this.setProductId(modemType.getProductId());
      this.setVendorId(modemType.getVendorId());
    } else {
      this.setProductId(null);
      this.setVendorId(null);
    }
  }

  private void checkProductId(Number productId, ModemType modemType) {
    if (modemType != null
        && productId != null
        && productId.intValue() != modemType.getProductId()) {
      throw new IllegalArgumentException("Modem productId is inconsistent with the given one");
    }
  }

  private void checkVendorId(Number vendorId, ModemType modemType) {
    if (modemType != null && vendorId != null && vendorId.intValue() != modemType.getVendorId()) {
      throw new IllegalArgumentException("Modem vendorId is inconsistent with the given one");
    }
  }
}
