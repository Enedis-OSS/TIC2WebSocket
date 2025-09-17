// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
// 
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.io.channels.serialport;

/**
 * Channel serial port error code
 */
public enum ChannelSerialPortErrorCode
{
	/** Port not found */
	PORT_NOT_FOUND(1),

	/** Port opened not found */
	PORT_OPENED_NOT_FOUND(2),

	/** Port name has changed */
	PORT_NAME_HAS_CHANGED(3),

	/** Port busy */
	PORT_BUSY(4),

	/** Read operation failed */
	READ_FAILED(5),

	/** Read operation timed out */
	READ_TIMEOUT(6);

	private int code;

	private ChannelSerialPortErrorCode(int code)
	{
		this.code = code;
	}

	/**
	 * Get error code without offset
	 *
	 * @return error code
	 */
	public int getCode()
	{
		return this.code;
	}

	/**
	 * Get error code with offset
	 *
	 * @param offset
	 *
	 * @return error code
	 */
	public int getCode(int offset)
	{
		return offset + this.code;
	}
}
