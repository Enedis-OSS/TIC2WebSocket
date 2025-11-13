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
 * Listener interface for receiving data stream events and notifications.
 *
 * <p>This interface extends {@link Subscriber} and defines callback methods that are invoked when
 * specific events occur on a data stream. Implementations of this interface can be registered with
 * data streams to receive notifications about data transmission, status changes, and error
 * conditions.
 *
 * <p>The listener provides four types of notifications:
 *
 * <ul>
 *   <li>Data reception events when new data arrives on the stream
 *   <li>Data transmission events when data is sent through the stream
 *   <li>Status change events when the stream status changes
 *   <li>Error detection events when errors occur during stream operations
 * </ul>
 *
 * @author Enedis Smarties team
 * @see DataStream
 * @see DataStreamBase
 * @see DataStreamStatus
 * @see DataDictionary
 */
public interface DataStreamListener extends Subscriber {
  /**
   * Called when data has been received on the data stream.
   *
   * <p>This method is invoked whenever new data arrives on the stream and has been successfully
   * read. Implementations should handle the received data according to their specific requirements.
   *
   * @param dataStreamName the name of the stream that received the data
   * @param data the data dictionary containing the received data
   */
  public void onDataReceived(String dataStreamName, DataDictionary data);

  /**
   * Called when data has been sent through the data stream.
   *
   * <p>This method is invoked after data has been successfully written to the stream.
   * Implementations can use this to track data transmission, log sent data, or perform
   * post-transmission processing.
   *
   * @param dataStreamName the name of the stream that sent the data
   * @param data the data dictionary containing the sent data
   */
  public void onDataSent(String dataStreamName, DataDictionary data);

  /**
   * Called when the status of the data stream changes.
   *
   * <p>This method is invoked whenever the stream transitions to a new status or when an error
   * state is entered. Implementations can use this to monitor stream health and react to status
   * changes.
   *
   * @param dataStreamName the name of the stream whose status changed
   * @param status the new status of the stream
   */
  public void onStatusChanged(String dataStreamName, DataStreamStatus status);

  /**
   * Called when an error is detected during data stream operations.
   *
   * <p>This method is invoked when an error occurs during stream processing, such as communication
   * failures, data validation errors, or protocol violations. The error code and message provide
   * details about the error, and the data parameter may contain the data associated with the error
   * for diagnostic purposes.
   *
   * @param dataStreamName the name of the stream where the error was detected
   * @param errorCode the error code identifying the type of error
   * @param errorMessage a descriptive message about the error
   * @param data the data dictionary associated with the error, if any
   */
  public void onErrorDetected(
      String dataStreamName, int errorCode, String errorMessage, DataDictionary data);
}
