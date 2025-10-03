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
 * Interface for receiving channel events and notifications.
 *
 * <p>This interface extends {@link Subscriber} and provides methods for handling various channel
 * events such as data read/write operations, status changes, and error conditions. Implementations
 * of this interface can be registered with channels to receive real-time notifications about
 * channel activities.
 *
 * <p>The listener pattern allows for decoupled event handling, enabling multiple listeners to be
 * registered with a single channel and receive notifications about all channel activities.
 *
 * <p>Common use cases include:
 *
 * <ul>
 *   <li>Logging channel activities
 *   <li>Monitoring channel status
 *   <li>Processing received data
 *   <li>Error handling and reporting
 * </ul>
 *
 * @author Enedis Smarties team
 * @see Subscriber
 * @see Channel
 * @see ChannelStatus
 */
public interface ChannelListener extends Subscriber {

  /**
   * Called when data is successfully read from a channel.
   *
   * <p>This method is invoked whenever a channel performs a successful read operation. The received
   * data is provided as a byte array containing the raw data that was read from the channel.
   *
   * @param channelName the name of the channel that read the data
   * @param data the raw bytes that were read from the channel
   */
  public void onDataRead(String channelName, byte[] data);

  /**
   * Called when data is successfully written to a channel.
   *
   * <p>This method is invoked whenever a channel performs a successful write operation. The
   * transmitted data is provided as a byte array containing the raw data that was written to the
   * channel.
   *
   * @param channelName the name of the channel that wrote the data
   * @param data the raw bytes that were written to the channel
   */
  public void onDataWritten(String channelName, byte[] data);

  /**
   * Called when the status of a channel changes.
   *
   * <p>This method is invoked whenever a channel's operational status changes, such as when it goes
   * from closed to open, or from ready to error state. This allows listeners to monitor channel
   * state transitions.
   *
   * @param channelName the name of the channel whose status changed
   * @param status the new status of the channel
   */
  public void onStatusChanged(String channelName, ChannelStatus status);

  /**
   * Called when an error is detected on a channel.
   *
   * <p>This method is invoked whenever an error occurs during channel operations. It provides basic
   * error information including the error code and message.
   *
   * @param channelName the name of the channel where the error occurred
   * @param errorCode the numeric error code identifying the type of error
   * @param errorMessage a descriptive message explaining the error
   */
  public void onErrorDetected(String channelName, int errorCode, String errorMessage);

  /**
   * Called when an error is detected on a channel with additional context data.
   *
   * <p>This method is invoked whenever an error occurs during channel operations and additional
   * context data is available. It provides comprehensive error information including the error
   * code, message, and associated data dictionary.
   *
   * @param channelName the name of the channel where the error occurred
   * @param errorCode the numeric error code identifying the type of error
   * @param errorMessage a descriptive message explaining the error
   * @param data additional context data associated with the error
   */
  public void onErrorDetected(
      String channelName, int errorCode, String errorMessage, DataDictionary data);
}
