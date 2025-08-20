// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.tic.service.client;

import java.util.Optional;

import io.netty.channel.Channel;

import enedis.tic.service.endpoint.EventSender;

/**
 * TIC2WebSocket client pool interface
 */
public interface TIC2WebSocketClientPool
{
	/**
	 * Get client from channel id
	 * 
	 * @param channelId
	 * @return client
	 */
	public Optional<TIC2WebSocketClient> getClient(String channelId);

	/**
	 * Check if a client with the given channel id exists
	 * 
	 * @param channelId
	 * @return true if a client with the given channel id exists
	 */
	public boolean exists(String channelId);

	/**
	 * Create a new client
	 * 
	 * @param channel
	 * @param sender
	 * @return new client
	 */
	public TIC2WebSocketClient createClient(Channel channel, EventSender sender);

	/**
	 * Remove client with the given channel id
	 * 
	 * @param channelId
	 */
	public void remove(String channelId);
}
