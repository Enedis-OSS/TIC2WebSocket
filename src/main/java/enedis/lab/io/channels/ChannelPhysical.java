// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.io.channels;

import enedis.lab.util.task.NotifierBase;
import java.util.Collection;

/**
 * Abstract base class for physical communication channels.
 *
 * <p>This class extends {@link ChannelBase} and provides the foundation for all physical
 * communication channel implementations. It handles the common functionality for physical channels
 * including status management, listener notification, and event propagation.
 *
 * <p>Physical channels represent actual communication endpoints such as serial ports, network
 * sockets, or other hardware interfaces. This class provides the infrastructure for managing
 * channel state and notifying registered listeners about channel events.
 *
 * <p>Key features provided by this class:
 *
 * <ul>
 *   <li>Status management with automatic listener notification
 *   <li>Event notification system for data read/write operations
 *   <li>Error detection and reporting
 *   <li>Listener subscription management
 * </ul>
 *
 * @author Enedis Smarties team
 * @see ChannelBase
 * @see ChannelListener
 * @see ChannelStatus
 * @see NotifierBase
 */
public abstract class ChannelPhysical extends ChannelBase {

  /** Notifier for managing channel event listeners. */
  private NotifierBase<ChannelListener> notifier;

  /** Current status of the physical channel. */
  protected ChannelStatus status;

  /**
   * Constructs a new physical channel with the specified configuration.
   *
   * <p>This constructor initializes the physical channel with the provided configuration and sets
   * up the internal notification system. The channel starts in a STOPPED status and is ready for
   * setup operations.
   *
   * @param configuration the configuration to use for setting up the channel
   * @throws ChannelException if the configuration is invalid or the channel cannot be properly
   *     initialized
   */
  protected ChannelPhysical(ChannelConfiguration configuration) throws ChannelException {
    super(configuration);
    this.init();
  }

  @Override
  public void subscribe(ChannelListener listener) {
    this.notifier.subscribe(listener);
  }

  @Override
  public void unsubscribe(ChannelListener listener) {
    this.notifier.unsubscribe(listener);
  }

  @Override
  public boolean hasSubscriber(ChannelListener subscriber) {
    return this.notifier.hasSubscriber(subscriber);
  }

  @Override
  public Collection<ChannelListener> getSubscribers() {
    return this.notifier.getSubscribers();
  }

  @Override
  public String getName() {
    return this.configuration.getName();
  }

  @Override
  public ChannelProtocol getProtocol() {
    return this.configuration.getProtocol();
  }

  @Override
  public ChannelDirection getDirection() {
    return this.configuration.getDirection();
  }

  @Override
  public ChannelStatus getStatus() {
    return this.status;
  }

  @Override
  public ChannelConfiguration getConfiguration() {
    return this.configuration;
  }

  /**
   * Sets the channel status and notifies all registered listeners.
   *
   * <p>This method updates the channel status and automatically notifies all registered listeners
   * about the status change.
   *
   * @param status the new status for the channel
   */
  protected void setStatus(ChannelStatus status) {
    this.setStatus(status, true);
  }

  /**
   * Sets the channel status with optional listener notification.
   *
   * <p>This method updates the channel status and optionally notifies registered listeners about
   * the status change. This allows for controlled status updates without triggering notifications.
   *
   * @param status the new status for the channel
   * @param notify if true, listeners will be notified of the status change; if false, no
   *     notifications will be sent
   */
  protected void setStatus(ChannelStatus status, boolean notify) {
    if (status != this.status) {
      this.status = status;
      if (status == ChannelStatus.ERROR) {
        this.logger.error("Channel " + this.getName() + " new status : " + status.name());
      } else {
        this.logger.info("Channel " + this.getName() + " new status : " + status.name());
      }
      if (true == notify) {
        this.notifyOnStatusChanged();
      }
    }
  }

  /**
   * Notifies all registered listeners about data that was read from the channel.
   *
   * <p>This method is called internally when data is successfully read from the physical channel.
   * It notifies all registered listeners with the channel name and the raw data that was read.
   *
   * @param data the raw bytes that were read from the channel
   */
  protected void notifyOnDataRead(byte[] data) {
    if (data != null) {
      for (ChannelListener subscriber : this.notifier.getSubscribers()) {
        subscriber.onDataRead(this.getName(), data);
      }
    }
  }

  /**
   * Notifies all registered listeners about data that was written to the channel.
   *
   * <p>This method is called internally when data is successfully written to the physical channel.
   * It notifies all registered listeners with the channel name and the raw data that was written.
   *
   * @param data the raw bytes that were written to the channel
   */
  protected void notifyOnDataWritten(byte[] data) {
    if (data != null) {
      for (ChannelListener subscriber : this.notifier.getSubscribers()) {
        subscriber.onDataWritten(this.getName(), data);
      }
    }
  }

  /**
   * Notifies all registered listeners about a status change.
   *
   * <p>This method is called internally when the channel status changes. It notifies all registered
   * listeners with the channel name and the new status.
   */
  protected void notifyOnStatusChanged() {
    for (ChannelListener subscriber : this.notifier.getSubscribers()) {
      subscriber.onStatusChanged(this.getName(), this.status);
    }
  }

  /**
   * Notifies all registered listeners about an error that was detected.
   *
   * <p>This method is called internally when an error occurs during channel operations. It notifies
   * all registered listeners with the channel name, error code, and error message.
   *
   * @param errorCode the numeric error code identifying the type of error
   * @param errorMessage a descriptive message explaining the error
   */
  protected void notifyOnErrorDetected(int errorCode, String errorMessage) {
    for (ChannelListener subscriber : this.notifier.getSubscribers()) {
      subscriber.onErrorDetected(this.getName(), errorCode, errorMessage);
    }
  }

  /**
   * Initializes the physical channel with default values.
   *
   * <p>This method sets up the initial state of the physical channel, including setting the status
   * to STOPPED and initializing the notification system for managing listeners.
   */
  private void init() {
    this.status = ChannelStatus.STOPPED;
    this.notifier = new NotifierBase<ChannelListener>();
  }
}
