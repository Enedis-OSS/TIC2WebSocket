// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.io.datastreams;

import enedis.lab.io.channels.Channel;
import enedis.lab.io.channels.ChannelListener;
import enedis.lab.io.channels.ChannelStatus;
import enedis.lab.types.DataDictionary;
import enedis.lab.util.task.NotifierBase;
import java.util.Collection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Abstract base class providing common functionality for data stream implementations.
 *
 * <p>This class implements both {@link DataStream} and {@link ChannelListener} interfaces,
 * providing a foundation for concrete data stream implementations. It manages the stream lifecycle,
 * status tracking, and event notification to registered listeners.
 *
 * <p>The class handles channel status changes and automatically updates the stream status
 * accordingly. It also provides notification mechanisms for data events and status changes to
 * subscribed listeners.
 *
 * @author Enedis Smarties team
 * @see DataStream
 * @see ChannelListener
 * @see DataStreamListener
 * @see DataStreamConfiguration
 * @see DataStreamStatus
 */
public abstract class DataStreamBase implements DataStream, ChannelListener {
  protected NotifierBase<DataStreamListener> notifier;
  protected DataStreamConfiguration configuration;
  private DataStreamStatus status;
  protected Channel channel;
  protected Logger logger;

  /**
   * Constructs a new DataStreamBase with the specified configuration.
   *
   * <p>The constructor initializes the stream with the given configuration, sets up the notifier
   * for event handling, and configures logging. The stream status is initially set to UNKNOWN.
   *
   * @param configuration the configuration used to set up the stream
   * @throws DataStreamException if the configuration is invalid or setup fails
   */
  public DataStreamBase(DataStreamConfiguration configuration) throws DataStreamException {
    this.init(configuration);
  }

  /**
   * Handles channel status changes and updates the stream status accordingly.
   *
   * <p>This method is called when the associated channel's status changes. It maps channel statuses
   * to appropriate stream statuses and notifies listeners if the status has changed.
   *
   * @param channelName the name of the channel that changed status
   * @param channelStatus the new status of the channel
   */
  @Override
  public void onStatusChanged(String channelName, ChannelStatus channelStatus) {
    DataStreamStatus status_cpy = this.status;

    switch (channelStatus) {
      case STOPPED:
        {
          if (DataStreamStatus.CLOSED != this.status) {
            this.setStatus(DataStreamStatus.ERROR);
          }
          break;
        }
      case STARTED:
        {
          if (DataStreamStatus.ERROR == this.status) {
            this.setStatus(DataStreamStatus.OPENED);
          }
          break;
        }
      case ERROR:
        {
          this.setStatus(DataStreamStatus.ERROR);
          break;
        }

      default:
        {
        }
    }

    if (!this.notifier.getSubscribers().isEmpty() && (status_cpy != this.status)) {
      this.notifyOnStatusChanged(this.status);
    }
  }

  /**
   * Opens the data stream and subscribes to the associated channel.
   *
   * <p>This method attempts to open the stream by subscribing to the channel specified in the
   * configuration. If the channel exists, the stream status is set to OPENED. If the channel does
   * not exist, an error is raised and the status is set to ERROR.
   *
   * @throws DataStreamException if the channel does not exist or if an error occurs during the
   *     subscription process
   */
  @Override
  public void open() throws DataStreamException {
    String channelName = this.configuration.getChannelName();

    if (null != channelName) {
      if (null != this.channel) {
        this.channel.subscribe(this);
        this.setStatus(DataStreamStatus.OPENED);
        this.logger.info("Stream " + this.getName() + " start");
      } else {
        this.setStatus(DataStreamStatus.ERROR);
        DataStreamException.raiseChannelError(channelName, "channel does not exist");
      }
    }
  }

  /**
   * Closes the data stream and unsubscribes from the associated channel.
   *
   * <p>This method closes the stream by unsubscribing from the channel and setting the status to
   * CLOSED. If the channel does not exist, an error is raised and the status is set to ERROR.
   *
   * @throws DataStreamException if the channel does not exist or if an error occurs during the
   *     unsubscription process
   */
  @Override
  public void close() throws DataStreamException {
    String channelName = this.configuration.getChannelName();

    if (null != channelName) {
      if (null != this.channel) {
        this.channel.unsubscribe(this);
        this.setStatus(DataStreamStatus.CLOSED);
        this.logger.info("Service " + this.getName() + " stop");
      } else {
        this.setStatus(DataStreamStatus.ERROR);
        DataStreamException.raiseChannelError(channelName, "channel does not exist");
      }
    }
  }

  /**
   * Sets up the data stream with the specified configuration.
   *
   * <p>This method updates the stream configuration. It cannot be called when the stream is already
   * open, as this would be an invalid operation.
   *
   * @param configuration the new configuration for the stream
   * @throws DataStreamException if the stream is already open or if the configuration is invalid
   */
  @Override
  public void setup(DataStreamConfiguration configuration) throws DataStreamException {
    if (this.status == DataStreamStatus.OPENED) {
      DataStreamException.raiseInternalError(
          "Cannot setup stream " + this.getName() + " (already open)");
    }
    this.configuration = configuration;
  }

  /**
   * Subscribes a listener to receive data stream events.
   *
   * @param listener the listener to subscribe to stream events
   */
  @Override
  public void subscribe(DataStreamListener listener) {
    this.notifier.subscribe(listener);
  }

  /**
   * Unsubscribes a listener from receiving data stream events.
   *
   * @param listener the listener to unsubscribe from stream events
   */
  @Override
  public void unsubscribe(DataStreamListener listener) {
    this.notifier.unsubscribe(listener);
  }

  /**
   * Checks if a specific listener is subscribed to this stream.
   *
   * @param subscriber the listener to check
   * @return true if the listener is subscribed, false otherwise
   */
  @Override
  public boolean hasSubscriber(DataStreamListener subscriber) {
    return this.notifier.hasSubscriber(subscriber);
  }

  /**
   * Returns a collection of all subscribed listeners.
   *
   * @return a collection of all subscribed listeners
   */
  @Override
  public Collection<DataStreamListener> getSubscribers() {
    return this.notifier.getSubscribers();
  }

  /**
   * Returns the name of this data stream.
   *
   * @return the stream name from the configuration
   */
  @Override
  public String getName() {
    return this.configuration.getName();
  }

  /**
   * Returns a clone of the current stream configuration.
   *
   * @return a copy of the stream configuration
   */
  @Override
  public DataStreamConfiguration getConfiguration() {
    return (DataStreamConfiguration) this.configuration.clone();
  }

  /**
   * Returns the direction of this data stream.
   *
   * @return the stream direction from the configuration
   */
  @Override
  public DataStreamDirection getDirection() {
    return this.configuration.getDirection();
  }

  /**
   * Returns the type of this data stream.
   *
   * @return the stream type from the configuration
   */
  @Override
  public DataStreamType getType() {
    return this.configuration.getType();
  }

  /**
   * Returns the current status of this data stream.
   *
   * @return the current stream status
   */
  @Override
  public DataStreamStatus getStatus() {
    return this.status;
  }

  /**
   * Sets the channel reference for this data stream.
   *
   * <p>This method associates a channel with the stream, enabling communication through the
   * specified channel. The channel must be compatible with the stream configuration.
   *
   * @param channel the channel to associate with this stream
   */
  public void setChannel(Channel channel) {
    this.channel = channel;
  }

  /**
   * Notifies all subscribed listeners that new data has been received.
   *
   * <p>This method iterates through all subscribed listeners and calls their onDataReceived method
   * with the stream name and the received data.
   *
   * @param data the new data that was received
   */
  protected void notifyOnDataReceived(DataDictionary data) {
    if (data != null) {
      for (DataStreamListener listener : this.notifier.getSubscribers()) {
        listener.onDataReceived(this.getName(), data);
      }
    }
  }

  /**
   * Notifies all subscribed listeners that data has been sent.
   *
   * <p>This method iterates through all subscribed listeners and calls their onDataSent method with
   * the stream name and the sent data.
   *
   * @param data the data that was sent
   */
  protected void notifyOnDataSent(DataDictionary data) {
    if (data != null) {
      for (DataStreamListener listener : this.notifier.getSubscribers()) {
        listener.onDataSent(this.getName(), data);
      }
    }
  }

  /**
   * Notifies all subscribed listeners that the stream status has changed.
   *
   * <p>This method iterates through all subscribed listeners and calls their onStatusChanged method
   * with the stream name and the new status.
   *
   * @param newStatus the new status of the stream
   */
  protected void notifyOnStatusChanged(DataStreamStatus newStatus) {
    if (newStatus != null) {
      for (DataStreamListener listener : this.notifier.getSubscribers()) {
        listener.onStatusChanged(this.getName(), newStatus);
      }
    }
  }

  /**
   * Notifies all subscribed listeners that an error has been detected.
   *
   * <p>This method iterates through all subscribed listeners and calls their onErrorDetected method
   * with the stream name, error details, and associated data.
   *
   * @param errorCode the error code identifying the type of error
   * @param errorMessage a descriptive message about the error
   * @param data the data associated with the error, if any
   */
  protected void notifyOnErrorDetected(int errorCode, String errorMessage, DataDictionary data) {
    for (DataStreamListener listener : this.notifier.getSubscribers()) {
      listener.onErrorDetected(this.getName(), errorCode, errorMessage, data);
    }
  }

  /**
   * Sets the stream status and notifies listeners of the change.
   *
   * <p>This is a convenience method that calls setStatus(status, true) to update the status and
   * notify all listeners.
   *
   * @param status the new status for the stream
   */
  protected void setStatus(DataStreamStatus status) {
    this.setStatus(status, true);
  }

  /**
   * Sets the stream status with optional listener notification.
   *
   * <p>This method updates the internal status and optionally notifies all subscribed listeners of
   * the status change. The notification only occurs if the status actually changes and the notify
   * parameter is true.
   *
   * @param status the new status for the stream
   * @param notify if true, listeners will be notified of the status change
   */
  protected void setStatus(DataStreamStatus status, boolean notify) {
    if (status != this.status) {
      this.status = status;

      if (true == notify) {
        this.notifyOnStatusChanged(this.status);
      }
    }
  }

  /**
   * Initializes the data stream with the specified configuration.
   *
   * <p>This private method sets up the internal components of the stream, including the notifier,
   * initial status, configuration, and logger. It also calls the setup method to apply the
   * configuration.
   *
   * @param configuration the configuration to initialize the stream with
   * @throws DataStreamException if the initialization fails
   */
  private void init(DataStreamConfiguration configuration) throws DataStreamException {
    this.notifier = new NotifierBase<DataStreamListener>();
    this.status = DataStreamStatus.UNKNOWN;
    this.configuration = configuration;
    this.logger = LogManager.getLogger(this.getClass());
    this.setup(configuration);
  }
}
