// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.io.channels;

/**
 * Enumeration representing the communication direction for channels.
 *
 * <p>This enum defines the possible communication directions that a channel can support. The
 * direction determines which operations are available on the channel (read, write, or both).
 *
 * <p>This enum is used in {@link ChannelConfiguration} to specify the communication direction for
 * channel instances.
 *
 * @author Enedis Smarties team
 * @see ChannelConfiguration
 * @see Channel
 */
public enum ChannelDirection {

  /**
   * Receive-only direction.
   *
   * <p>Channels with this direction can only receive data from the communication endpoint. Read
   * operations are supported, but write operations will fail.
   */
  RX,

  /**
   * Transmit-only direction.
   *
   * <p>Channels with this direction can only send data to the communication endpoint. Write
   * operations are supported, but read operations will fail.
   */
  TX,

  /**
   * Bidirectional communication.
   *
   * <p>Channels with this direction can both receive and send data. Both read and write operations
   * are supported, enabling full bidirectional communication. This is the most common direction for
   * interactive communication channels.
   */
  RXTX
}
