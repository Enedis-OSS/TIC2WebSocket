// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.io.usb;

import java.util.List;
import java.util.stream.Collectors;
import tic.io.PortFinder;

/** Interface used to find all USB port descriptor */
public interface UsbPortFinder extends PortFinder<UsbPortDescriptor> {
  /**
   * Find USB device with the given product id
   *
   * @param idProduct the USB product identifier
   * @return the USB descriptor data list of all USB device connected with the given product id
   */
  public default List<UsbPortDescriptor> findByProductId(short idProduct) {
    return this.findAll().stream()
        .filter(d -> d.idProduct() == idProduct)
        .collect(Collectors.toList());
  }

  /**
   * Find USB device with the given vendor id
   *
   * @param idVendor the USB vendor identifier
   * @return the USB descriptor data list of all USB device connected with the given vendor id
   */
  public default List<UsbPortDescriptor> findByVendorId(short idVendor) {
    return this.findAll().stream()
        .filter(d -> d.idVendor() == idVendor)
        .collect(Collectors.toList());
  }

  /**
   * Find USB device with the given product id and vendor id
   *
   * @param idProduct the USB product identifier
   * @param idVendor the USB vendor identifier
   * @return the USB descriptor data list of all USB device connected with the given product id and
   *     vendor id
   */
  public default List<UsbPortDescriptor> findByProductIdAndVendorId(
      short idProduct, short idVendor) {
    return this.findAll().stream()
        .filter(d -> d.idProduct() == idProduct)
        .filter(d -> d.idVendor() == idVendor)
        .collect(Collectors.toList());
  }
}
