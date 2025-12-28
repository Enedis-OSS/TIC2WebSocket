// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.io.modem;

/** Supported modem types. */
public enum ModemType {
  /** Modem michaud */
  MICHAUD((short) 0x6001, (short) 0x0403),
  /** Télé info */
  TELEINFO((short) 0x6015, (short) 0x0403);

  private final short productId;
  private final short vendorId;

  ModemType(short productId, short vendorId) {
    this.productId = productId;
    this.vendorId = vendorId;
  }

  /**
   * Get product id
   *
   * @return product id
   */
  public short getProductId() {
    return this.productId;
  }

  /**
   * Get vendor id
   *
   * @return vendor id
   */
  public short getVendorId() {
    return this.vendorId;
  }
}
