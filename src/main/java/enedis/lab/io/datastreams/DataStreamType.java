// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
// 
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.io.datastreams;

/**
 * DataStream type
 */
public enum DataStreamType
{
	/** raw stream */
	RAW,
	/** TIC stream */
	TIC,
	/** IEC61850 stream */
	IEC61850,
	/** Slave modbus stream */
	MODBUS_SLAVE,
	/** Master modbus stream */
	MODBUS_MASTER,
	/** LEM DC product */
	LEM_DC;
}
