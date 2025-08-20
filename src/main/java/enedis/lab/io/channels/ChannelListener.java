// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
// 
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.io.channels;

import enedis.lab.types.DataDictionary;
import enedis.lab.util.task.Subscriber;

/**
 * Channel listener
 */
public interface ChannelListener extends Subscriber
{
	/**
	 * On data read
	 *
	 * @param channelName
	 *            the channel name
	 * @param data
	 *            the bytes read
	 */
	public void onDataRead(String channelName, byte[] data);

	/**
	 * On data written
	 *
	 * @param channelName
	 *            the channel name
	 * @param data
	 *            the bytes written
	 */
	public void onDataWritten(String channelName, byte[] data);

	/**
	 * On status changed
	 *
	 * @param channelName
	 *            the channel name
	 * @param status
	 *            the new status
	 */
	public void onStatusChanged(String channelName, ChannelStatus status);

	/**
	 * On error detected
	 *
	 * @param channelName
	 *            the channel name
	 * @param errorCode
	 *            the error code
	 * @param errorMessage
	 *            the error message
	 */
	public void onErrorDetected(String channelName, int errorCode, String errorMessage);

	/**
	 * On error detected
	 *
	 * @param channelName
	 *            the channel name
	 * @param errorCode
	 *            the error code
	 * @param errorMessage
	 *            the error message
	 * @param data
	 *            the data associated with error message
	 */
	public void onErrorDetected(String channelName, int errorCode, String errorMessage, DataDictionary data);
}
