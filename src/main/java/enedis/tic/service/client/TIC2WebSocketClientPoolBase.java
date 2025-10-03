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

/** Client pool */
public class TIC2WebSocketClientPoolBase implements TIC2WebSocketClientPool {
  private Set<TIC2WebSocketClient> clients;

  /** Default constructor */
  public TIC2WebSocketClientPoolBase() {
    super();
    this.clients = new CopyOnWriteArraySet<TIC2WebSocketClient>();
  }

  @Override
  public Optional<TIC2WebSocketClient> getClient(String channelId) {
    // @formatter:off
    return this.clients.stream()
        .filter(c -> c.getChannel().id().asLongText().equals(channelId))
        .findAny();
    // @formatter:on
  }

  @Override
  public boolean exists(String channelId) {
    return this.getClient(channelId).isPresent();
  }

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

  @Override
  public void remove(String channelId) {
    Optional<TIC2WebSocketClient> client = this.getClient(channelId);
    if (client.isPresent()) {
      this.clients.remove(client.get());
    }
  }

  private void checkArguments(Channel channel, EventSender sender) {
    if (channel == null || sender == null) {
      throw new IllegalArgumentException(
          "Cannot create client with a null Channel or null EventSender");
    }
  }
}
