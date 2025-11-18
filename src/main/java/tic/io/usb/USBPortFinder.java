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
public interface USBPortFinder extends PortFinder<USBPortDescriptor> {
  /**
   * Find USB device with the given product id
   *
   * @param idProduct the USB product identifier
   * @return the USB descriptor data list of all USB device connected with the given product id
   */
  public default List<USBPortDescriptor> findByProductId(int idProduct) {
    // @formatter:off
    return this.findAll().stream()
        .filter(d -> d.getIdProduct().intValue() == idProduct)
        .collect(Collectors.toList());
    // @formatter:on
  }

  /**
   * Find USB device with the given vendor id
   *
   * @param idVendor the USB vendor identifier
   * @return the USB descriptor data list of all USB device connected with the given vendor id
   */
  public default List<USBPortDescriptor> findByVendorId(int idVendor) {
    // @formatter:off
    return this.findAll().stream()
        .filter(d -> d.getIdVendor().intValue() == idVendor)
        .collect(Collectors.toList());
    // @formatter:on
  }

  /**
   * Find USB device with the given product id and vendor id
   *
   * @param idProduct the USB product identifier
   * @param idVendor the USB vendor identifier
   * @return the USB descriptor data list of all USB device connected with the given product id and
   *     vendor id
   */
  public default List<USBPortDescriptor> findByProductIdAndVendorId(int idProduct, int idVendor) {
    // @formatter:off
    return this.findAll().stream()
        .filter(d -> d.getIdProduct().intValue() == idProduct)
        .filter(d -> d.getIdVendor().intValue() == idVendor)
        .collect(Collectors.toList());
    // @formatter:on
  }
}
