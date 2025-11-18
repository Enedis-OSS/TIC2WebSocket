// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.io.usb;

import enedis.lab.io.PortFinder;
import enedis.lab.types.DataArrayList;
import enedis.lab.types.DataList;
import java.util.stream.Collectors;

/** Interface used to find all USB port descriptor */
public interface USBPortFinder extends PortFinder<USBPortDescriptor> {
  /**
   * Find USB device with the given product id
   *
   * @param idProduct the USB product identifier
   * @return the USB descriptor data list of all USB device connected with the given product id
   */
  public default DataList<USBPortDescriptor> findByProductId(int idProduct) {
    DataList<USBPortDescriptor> descriptors = new DataArrayList<USBPortDescriptor>();
    // @formatter:off
    descriptors.addAll(
        this.findAll().stream()
            .filter(d -> d.getIdProduct().intValue() == idProduct)
            .collect(Collectors.toList()));
    // @formatter:on
    return descriptors;
  }

  /**
   * Find USB device with the given vendor id
   *
   * @param idVendor the USB vendor identifier
   * @return the USB descriptor data list of all USB device connected with the given vendor id
   */
  public default DataList<USBPortDescriptor> findByVendorId(int idVendor) {
    DataList<USBPortDescriptor> descriptors = new DataArrayList<USBPortDescriptor>();
    // @formatter:off
    descriptors.addAll(
        this.findAll().stream()
            .filter(d -> d.getIdVendor().intValue() == idVendor)
            .collect(Collectors.toList()));
    // @formatter:on
    return descriptors;
  }

  /**
   * Find USB device with the given product id and vendor id
   *
   * @param idProduct the USB product identifier
   * @param idVendor the USB vendor identifier
   * @return the USB descriptor data list of all USB device connected with the given product id and
   *     vendor id
   */
  public default DataList<USBPortDescriptor> findByProductIdAndVendorId(
      int idProduct, int idVendor) {
    DataList<USBPortDescriptor> descriptors = new DataArrayList<USBPortDescriptor>();
    // @formatter:off
    descriptors.addAll(
        this.findAll().stream()
            .filter(d -> d.getIdProduct().intValue() == idProduct)
            .filter(d -> d.getIdVendor().intValue() == idVendor)
            .collect(Collectors.toList()));
    // @formatter:on
    return descriptors;
  }
}
