// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.service.client;

import tic.service.endpoint.EventSender;
import io.netty.channel.Channel;
import java.util.Optional;

/**
 * Pool interface for managing TIC2WebSocketClient instances.
 *
 * <p>This interface defines methods for creating, retrieving, checking, and removing {@link
 * TIC2WebSocketClient} objects associated with Netty WebSocket channels. It provides centralized
 * management of client lifecycles and lookup by channel identifier.
 *
 * <p>Key features include:
 *
 * <ul>
 *   <li>Client creation and registration
 *   <li>Client lookup by channel ID
 *   <li>Existence checks for active clients
 *   <li>Client removal and cleanup
 * </ul>
 *
 * @author Enedis Smarties team
 * @see TIC2WebSocketClient
 * @see Channel
 * @see EventSender
 */
public interface TIC2WebSocketClientPool {
  /**
   * Retrieves the client associated with the specified channel ID.
   *
   * <p>Returns an {@link Optional} containing the client if found, or empty if no client exists for
   * the given channel ID.
   *
   * @param channelId the unique identifier of the WebSocket channel
   * @return an Optional containing the client, or empty if not found
   */
  Optional<TIC2WebSocketClient> getClient(String channelId);

  /**
   * Checks if a client exists for the specified channel ID.
   *
   * <p>Returns true if a client is registered for the given channel ID, false otherwise.
   *
   * @param channelId the unique identifier of the WebSocket channel
   * @return true if a client exists, false otherwise
   */
  boolean exists(String channelId);

  /**
   * Creates and registers a new client for the specified channel and event sender.
   *
   * <p>Initializes a new {@link TIC2WebSocketClient} and associates it with the given channel.
   *
   * @param channel the Netty WebSocket channel for communication
   * @param sender the event sender responsible for dispatching events
   * @return the newly created client instance
   */
  TIC2WebSocketClient createClient(Channel channel, EventSender sender);

  /**
   * Removes the client associated with the specified channel ID.
   *
   * <p>Unregisters and cleans up the client for the given channel ID, if present.
   *
   * @param channelId the unique identifier of the WebSocket channel
   */
  void remove(String channelId);
}
