// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.io.modem;

import java.util.Objects;
import tic.io.serialport.SerialPortDescriptor;
import tic.io.usb.UsbPortDescriptor;

/** Descriptor of a modem. */
public class ModemDescriptor extends SerialPortDescriptor {
  private ModemType modemType;

  public static class Builder<T extends Builder<T>> extends SerialPortDescriptor.Builder<T> {
    private ModemType modemType;

    public T modemType(ModemType modemType) {
      this.modemType = modemType;
      if (modemType == null) {
        this.productId = null;
        this.vendorId = null;
      } else {
        this.productId = modemType.productId();
        this.vendorId = modemType.vendorId();
      }
      return self();
    }

    public T copy(SerialPortDescriptor serialPortDescriptor) {
      this.portId = serialPortDescriptor.portId();
      this.portName = serialPortDescriptor.portName();
      this.description = serialPortDescriptor.description();
      this.productId = serialPortDescriptor.productId();
      this.vendorId = serialPortDescriptor.vendorId();
      this.productName = serialPortDescriptor.productName();
      this.manufacturer = serialPortDescriptor.manufacturer();
      this.serialNumber = serialPortDescriptor.serialNumber();
      return self();
    }

    public T copy(UsbPortDescriptor usbPortDescriptor) {
      this.productId = usbPortDescriptor.idProduct();
      this.vendorId = usbPortDescriptor.idVendor();
      this.productName = usbPortDescriptor.product();
      this.manufacturer = usbPortDescriptor.manufacturer();
      this.serialNumber = usbPortDescriptor.serialNumber();
      return self();
    }

    /**
     * Validates the builder's fields.
     *
     * @throws IllegalArgumentException if any required field is invalid
     */
    protected void validate() {
      super.validate();
      if (this.modemType != null) {
        if (this.productId == null) {
          throw new IllegalArgumentException(
              "Modem type is specified while productId is not provided");
        }
        if (this.productId != this.modemType.productId()) {
          throw new IllegalArgumentException("Modem type is inconsistent with the productId");
        }
        if (this.vendorId == null) {
          throw new IllegalArgumentException(
              "Modem type is specified while vendorId is not provided");
        }
        if (this.vendorId != this.modemType.vendorId()) {
          throw new IllegalArgumentException("Modem type is inconsistent with the vendorId");
        }
      }
    }

    public ModemDescriptor build() {
      this.validate();
      return new ModemDescriptor(this);
    }
  }

  /**
   * Constructs a ModemDescriptor with all parameters explicitly set.
   *
   * <p>This constructor creates a descriptor with all serial port and USB device properties
   * specified according to the modem type.
   *
   * @param builder the builder instance containing all the parameters
   */
  public ModemDescriptor(Builder<?> builder) {
    super(builder);
    this.modemType = builder.modemType;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof ModemDescriptor)) {
      return false;
    }
    ModemDescriptor other = (ModemDescriptor) obj;
    if (this.modemType != other.modemType) {
      return false;
    }
    return super.equals(obj);
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
  public ModemType modemType() {
    return this.modemType;
  }
}
