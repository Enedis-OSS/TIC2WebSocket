// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.io.channels;

/**
 * Enumeration of channel protocol
 *
 */
public enum ChannelProtocol
{
	/** File */
	FILE,
	/** Serial port */
	SERIAL_PORT,
	/** RTU modbus */
	MODBUS_RTU,
	/** TCP modbus */
	MODBUS_TCP,
	/** modbus stub */
	MODBUS_STUB,
	/** TCP */
	TCP,
	/** UDP */
	UDP,
}
