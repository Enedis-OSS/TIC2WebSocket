// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.service.netty;

import tic.service.client.TIC2WebSocketClientPool;
import tic.service.requesthandler.TIC2WebSocketRequestHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * Netty channel initializer for configuring the WebSocket server pipeline.
 *
 * <p>This class sets up the Netty channel pipeline for incoming socket connections, adding HTTP and WebSocket
 * handlers, compression, chunked writing, and custom request handling. It is responsible for preparing each
 * channel to handle WebSocket communication and routing requests to the appropriate handler.
 *
 * <p>Main responsibilities:
 * <ul>
 *   <li>Configure HTTP and WebSocket protocol handlers</li>
 *   <li>Enable compression and chunked writing for large messages</li>
 *   <li>Route requests to the custom request handler</li>
 *   <li>Manage client pool for active connections</li>
 * </ul>
 *
 * <p>Typical usage:
 * <ul>
 *   <li>Instantiate with a client pool and request handler</li>
 *   <li>Attach to a Netty server bootstrap for WebSocket support</li>
 * </ul>
 *
 * @author Enedis Smarties team
 */
public class TIC2WebSocketChannelInitializer extends ChannelInitializer<SocketChannel> {
  private static final String WEBSOCKET_PATH = "/";

  private final TIC2WebSocketClientPool clientPool;
  private final TIC2WebSocketRequestHandler requestHandler;

  /**
   * Constructor
   *
   * @param clientPool the client pool
   * @param requestHandler the request handler
   */
  public TIC2WebSocketChannelInitializer(
      TIC2WebSocketClientPool clientPool, TIC2WebSocketRequestHandler requestHandler) {
    this.clientPool = clientPool;
    this.requestHandler = requestHandler;
  }

  @Override
  protected void initChannel(SocketChannel ch) throws Exception {
    ChannelPipeline pipeline = ch.pipeline();

    // HTTP codec
    pipeline.addLast(new HttpServerCodec());

    // HTTP object aggregator
    pipeline.addLast(new HttpObjectAggregator(65536));

    // Chunked write handler for large messages
    pipeline.addLast(new ChunkedWriteHandler());

    // WebSocket compression
    pipeline.addLast(new WebSocketServerCompressionHandler());

    // WebSocket protocol handler
    pipeline.addLast(new WebSocketServerProtocolHandler(WEBSOCKET_PATH, null, true));

    // Custom TIC2WebSocket handler
    pipeline.addLast(new TIC2WebSocketHandler(clientPool, requestHandler));
  }
}
