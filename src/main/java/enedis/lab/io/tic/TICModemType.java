// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.io.tic;

/**
 * TIC Modem type
 */
public enum TICModemType
{
	/** Modem michaud */
	MICHAUD(0x6001, 0x0403),
	/** Télé info */
	TELEINFO(0x6015, 0x0403);

	private int	productId;
	private int	vendorId;

	TICModemType(int productId, int vendorId)
	{
		this.productId = productId;
		this.vendorId = vendorId;
	}

	/**
	 * Get product id
	 *
	 * @return product id
	 */
	public int getProductId()
	{
		return this.productId;
	}

	/**
	 * Get vendor id
	 *
	 * @return vendor id
	 */
	public int getVendorId()
	{
		return this.vendorId;
	}
}
