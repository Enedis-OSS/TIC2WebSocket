// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.tic.service.client;

import enedis.tic.service.endpoint.EventSender;
import io.netty.channel.Channel;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Default implementation of the TIC2WebSocket client pool.
 *
 * <p>This class manages a thread-safe set of {@link TIC2WebSocketClient} instances, providing
 * creation, lookup, existence checks, and removal by channel ID. It implements the {@link
 * TIC2WebSocketClientPool} interface for centralized client management in the WebSocket service.
 *
 * <p>Key features include:
 *
 * <ul>
 *   <li>Thread-safe client storage using {@link CopyOnWriteArraySet}
 *   <li>Efficient lookup and registration by channel ID
 *   <li>Argument validation for client creation
 * </ul>
 *
 * @author Enedis Smarties team
 * @see TIC2WebSocketClientPool
 * @see TIC2WebSocketClient
 * @see Channel
 * @see EventSender
 */
public class TIC2WebSocketClientPoolBase implements TIC2WebSocketClientPool {
  /** Thread-safe set of registered clients. */
  private final Set<TIC2WebSocketClient> clients;

  /** Constructs a new client pool with an empty set of clients. */
  public TIC2WebSocketClientPoolBase() {
    super();
    this.clients = new CopyOnWriteArraySet<>();
  }

  /**
   * Retrieves the client associated with the specified channel ID.
   *
   * <p>Searches the pool for a client whose channel matches the given ID.
   *
   * @param channelId the unique identifier of the WebSocket channel
   * @return an Optional containing the client, or empty if not found
   */
  @Override
  public Optional<TIC2WebSocketClient> getClient(String channelId) {
    return this.clients.stream()
        .filter(c -> c.getChannel().id().asLongText().equals(channelId))
        .findAny();
  }

  /**
   * Checks if a client exists for the specified channel ID.
   *
   * @param channelId the unique identifier of the WebSocket channel
   * @return true if a client exists, false otherwise
   */
  @Override
  public boolean exists(String channelId) {
    return this.getClient(channelId).isPresent();
  }

  /**
   * Creates and registers a new client for the specified channel and event sender.
   *
   * <p>If a client for the channel already exists, returns the existing client. Otherwise, creates
   * a new client, registers it, and returns it.
   *
   * @param channel the Netty WebSocket channel for communication
   * @param sender the event sender responsible for dispatching events
   * @return the newly created or existing client instance
   * @throws IllegalArgumentException if channel or sender is null
   */
  @Override
  public TIC2WebSocketClient createClient(Channel channel, EventSender sender) {
    this.checkArguments(channel, sender);

    String channelId = channel.id().asLongText();
    Optional<TIC2WebSocketClient> client = this.getClient(channelId);
    if (!client.isPresent()) {
      TIC2WebSocketClient newClient = new TIC2WebSocketClient(channel, sender);
      this.clients.add(newClient);
      return newClient;
    } else {
      return client.get();
    }
  }

  /**
   * Removes the client associated with the specified channel ID.
   *
   * <p>Unregisters and cleans up the client for the given channel ID, if present.
   *
   * @param channelId the unique identifier of the WebSocket channel
   */
  @Override
  public void remove(String channelId) {
    Optional<TIC2WebSocketClient> client = this.getClient(channelId);
    if (client.isPresent()) {
      this.clients.remove(client.get());
    }
  }

  /**
   * Validates arguments for client creation.
   *
   * <p>Throws an exception if either argument is null.
   *
   * @param channel the Netty WebSocket channel
   * @param sender the event sender
   * @throws IllegalArgumentException if channel or sender is null
   */
  private void checkArguments(Channel channel, EventSender sender) {
    if (channel == null || sender == null) {
      throw new IllegalArgumentException(
          "Cannot create client with a null Channel or null EventSender");
    }
  }
}
