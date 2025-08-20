// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.protocol.tic;

import java.util.Arrays;

/**
 * TIC mode used available
 */

import enedis.lab.protocol.tic.frame.TICError;
import enedis.lab.protocol.tic.frame.standard.TICException;

/**
 * TIC Mode
 *
 */
public enum TICMode
{
	/** Unknown mode */
	UNKNOWN,
	/** Standard mode */
	STANDARD,
	/** Historic mode */
	HISTORIC,
	/** Auto mode */
	AUTO;

	/** Historic separator */
	public static final char	HISTORIC_SEPARATOR		= ' ';
	/** Standard separator */
	public static final char	STANDARD_SEPARATOR		= '\t';
	/** Historic buffer start */
	public static final byte[]	HISTORIC_BUFFER_START	= { 2, 10, 65, 68, 67, 79 };
	/** Standard buffer start */
	public static final byte[]	STANDARD_BUFFER_START	= { 2, 10, 65, 68, 83, 67 };

	/**
	 * Find mode from Frame Buffer
	 *
	 * @param frameBuffer
	 * @return TICMode
	 * @throws TICException
	 */
	public static TICMode findModeFromFrameBuffer(byte[] frameBuffer) throws TICException
	{
		byte[] frameBufferStart = new byte[HISTORIC_BUFFER_START.length];
		if (frameBuffer.length < frameBufferStart.length)
		{
			throw new TICException("Tic frame read 0x" + bytesToHex(frameBuffer) + " too short to determine TIC Mode !",
					TICError.TIC_READER_FRAME_DECODE_FAILED);
		}
		System.arraycopy(frameBuffer, 0, frameBufferStart, 0, frameBufferStart.length);
		if (Arrays.equals(frameBufferStart, HISTORIC_BUFFER_START))
		{
			return HISTORIC;
		}
		else
		{
			if (STANDARD_BUFFER_START.length != HISTORIC_BUFFER_START.length)
			{
				frameBufferStart = new byte[STANDARD_BUFFER_START.length];
				System.arraycopy(frameBuffer, 0, frameBufferStart, 0, frameBufferStart.length);
			}
			if (Arrays.equals(frameBufferStart, STANDARD_BUFFER_START))
			{
				return STANDARD;
			}
			return null;
		}
	}

	/**
	 * Find Mode frome Group Buffer
	 *
	 * @param groupBuffer
	 * @return TICMode
	 */
	public static TICMode findModeFromGroupBuffer(byte[] groupBuffer)
	{
		for (int i = 0; i < groupBuffer.length; i++)
		{
			if (groupBuffer[i] == HISTORIC_SEPARATOR)
			{
				return HISTORIC;
			}
			else if (groupBuffer[i] == STANDARD_SEPARATOR)
			{
				return STANDARD;
			}
		}
		return null;
	}

	/**
	 * Convert byte array to hexadecimal string (replacement for
	 * DatatypeConverter.printHexBinary)
	 * 
	 * @param bytes the byte array to convert
	 * @return hexadecimal string representation
	 */
	private static String bytesToHex(byte[] bytes) {
		StringBuilder result = new StringBuilder();
		for (byte b : bytes) {
			result.append(String.format("%02X", b));
		}
		return result.toString();
	}

}
