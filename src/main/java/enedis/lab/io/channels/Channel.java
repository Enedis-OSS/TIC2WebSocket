// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.io.channels;

import enedis.lab.util.task.Notifier;
import enedis.lab.util.task.Task;

/**
 * Interface defining the contract for communication channels.
 *
 * <p>
 * A channel represents a communication endpoint that can be used to send and
 * receive data. This
 * interface extends both {@link Task} and {@link Notifier} to provide task
 * execution capabilities
 * and event notification to registered {@link ChannelListener} instances.
 *
 * <p>
 * Channels support different protocols (e.g., serial port, TCP, UDP) and
 * directions
 * (receive-only, transmit-only, or bidirectional). Each channel must be
 * configured before use and
 * can be monitored for status changes.
 *
 * <p>
 * Implementations of this interface should handle the specific communication
 * protocol details
 * while providing a unified interface for data exchange.
 *
 * @author Enedis Smarties team
 * @see Task
 * @see Notifier
 * @see ChannelListener
 * @see ChannelConfiguration
 * @see ChannelProtocol
 * @see ChannelDirection
 * @see ChannelStatus
 */
public interface Channel extends Task, Notifier<ChannelListener> {

  /**
   * Initializes and configures the channel with the provided configuration.
   *
   * <p>
   * This method must be called before any read or write operations can be
   * performed. The
   * configuration contains all necessary parameters for the specific channel
   * type, such as port
   * settings for serial channels or network settings for TCP channels.
   *
   * <p>
   * After successful setup, the channel should be ready for communication
   * operations.
   *
   * @param configuration the channel configuration containing all necessary
   *                      parameters
   * @throws ChannelException if the configuration is invalid, the channel cannot
   *                          be initialized, or
   *                          there are resource allocation issues
   */
  public void setup(ChannelConfiguration configuration) throws ChannelException;

  /**
   * Reads data from the channel.
   *
   * <p>
   * This method performs a synchronous read operation, blocking until data is
   * available or a
   * timeout occurs (depending on the channel implementation). The returned byte
   * array contains the
   * raw data received from the channel.
   *
   * <p>
   * The channel must be properly configured and opened before calling this
   * method.
   *
   * @return a byte array containing the data read from the channel, or an empty
   *         array if no data is
   *         available
   * @throws ChannelException if the read operation fails, the channel is not
   *                          properly configured,
   *                          or a communication error occurs
   */
  public byte[] read() throws ChannelException;

  /**
   * Writes data to the channel.
   *
   * <p>
   * This method performs a synchronous write operation, sending the provided data
   * through the
   * channel. The operation blocks until the data is successfully transmitted or
   * an error occurs.
   *
   * <p>
   * The channel must be properly configured and opened before calling this
   * method.
   *
   * @param data the byte array containing the data to be written to the channel
   * @throws ChannelException if the write operation fails, the channel is not
   *                          properly configured,
   *                          or a communication error occurs
   */
  public void write(byte[] data) throws ChannelException;

  /**
   * Retrieves the name of this channel.
   *
   * <p>
   * The channel name is typically used for identification and logging purposes.
   * It is usually
   * set during channel creation or configuration.
   *
   * @return the channel name, or null if not set
   */
  public String getName();

  /**
   * Retrieves the communication protocol used by this channel.
   *
   * <p>
   * The protocol determines the underlying communication mechanism, such as
   * serial port, TCP,
   * UDP, or other supported protocols.
   *
   * @return the channel protocol, or null if not set
   */
  public ChannelProtocol getProtocol();

  /**
   * Retrieves the communication direction of this channel.
   *
   * <p>
   * The direction indicates whether the channel can receive data (RX), transmit
   * data (TX), or
   * both (RXTX). This affects which operations are available.
   *
   * @return the channel direction, or null if not set
   */
  public ChannelDirection getDirection();

  /**
   * Retrieves the current status of this channel.
   *
   * <p>
   * The status indicates the operational state of the channel, such as whether it
   * is open,
   * closed, error state, or in the process of opening/closing.
   *
   * @return the current channel status
   */
  public ChannelStatus getStatus();

  /**
   * Retrieves the configuration used by this channel.
   *
   * <p>
   * The configuration contains all the parameters that were used to set up the
   * channel,
   * including protocol-specific settings and operational parameters.
   *
   * @return the channel configuration, or null if the channel has not been
   *         configured
   */
  public ChannelConfiguration getConfiguration();
}
