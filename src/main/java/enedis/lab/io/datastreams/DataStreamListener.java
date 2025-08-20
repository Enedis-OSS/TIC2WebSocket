// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.io.datastreams;

import enedis.lab.types.DataDictionary;
import enedis.lab.util.task.Subscriber;

/**
 * Data stream listener
 */
public interface DataStreamListener extends Subscriber
{
	/**
	 * Function call when data has been received
	 *
	 * @param dataStreamName
	 *            the stream name
	 * @param data
	 *            the data received
	 */
	public void onDataReceived(String dataStreamName, DataDictionary data);

	/**
	 * Function call when data has been sent
	 *
	 * @param dataStreamName
	 *            the stream name
	 * @param data
	 *            the data sent
	 */
	public void onDataSent(String dataStreamName, DataDictionary data);

	/**
	 * Function call when data stream status changed
	 *
	 * @param dataStreamName
	 *            the stream name
	 * @param status
	 *            the new status
	 */
	public void onStatusChanged(String dataStreamName, DataStreamStatus status);

	/**
	 * On error detected
	 *
	 * @param dataStreamName
	 *            the stream name
	 * @param errorCode
	 *            the error code
	 * @param errorMessage
	 *            the error message
	 * @param data
	 *            the data associated with error
	 */
	public void onErrorDetected(String dataStreamName, int errorCode, String errorMessage, DataDictionary data);
}
