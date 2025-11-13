// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.tic.service.endpoint;

import enedis.lab.util.message.Event;
import io.netty.channel.Channel;

/**
 * Interface for sending event messages to a WebSocket channel.
 *
 * <p>This interface defines the contract for dispatching {@link Event} objects to a specified Netty
 * {@link Channel}. Implementations are responsible for serializing and transmitting event data over
 * the channel.
 *
 * <p>Key features include:
 *
 * <ul>
 *   <li>Abstracts event delivery to WebSocket clients
 *   <li>Supports custom event serialization and transmission strategies
 * </ul>
 *
 * @author Enedis Smarties team
 * @see Event
 * @see Channel
 */
public interface EventSender {
  /**
   * Sends an event message to the specified WebSocket channel.
   *
   * <p>Implementations should serialize the event and transmit it to the client associated with the
   * given channel.
   *
   * @param channel the Netty WebSocket channel to send the event to
   * @param event the event message to be sent
   */
  void sendEvent(Channel channel, Event event);
}
