// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
// 
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.io.channels.serialport;

/**
 * Enumeration representing parity settings for serial communication.
 * <p>
 * Parity is used for error detection in serial communication by adding an extra
 * bit to each data frame. The parity bit is calculated based on the number of
 * 1-bits in the data and the selected parity type.
 * <p>
 * This enum is used in {@link ChannelSerialPortConfiguration} to specify
 * the parity setting for serial port communication channels.
 *
 * @author Enedis Smarties team
 * @see ChannelSerialPortConfiguration
 */
public enum Parity {

	/**
	 * No parity bit is added to the data frame.
	 * <p>
	 * This setting disables parity checking, which means no error detection
	 * is performed on the data bits.
	 */
	NONE,

	/**
	 * Even parity is used for error detection.
	 * <p>
	 * The parity bit is set to 1 if the number of 1-bits in the data is odd,
	 * ensuring the total number of 1-bits (including parity) is even.
	 */
	EVEN,

	/**
	 * Odd parity is used for error detection.
	 * <p>
	 * The parity bit is set to 1 if the number of 1-bits in the data is even,
	 * ensuring the total number of 1-bits (including parity) is odd.
	 */
	ODD,

	/**
	 * Mark parity is used for error detection.
	 * <p>
	 * The parity bit is always set to 1, regardless of the data content.
	 * This is typically used for special communication protocols.
	 */
	MARK,

	/**
	 * Space parity is used for error detection.
	 * <p>
	 * The parity bit is always set to 0, regardless of the data content.
	 * This is typically used for special communication protocols.
	 */
	SPACE

}