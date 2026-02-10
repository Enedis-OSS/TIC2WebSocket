// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.service.netty;

import tic.service.client.TIC2WebSocketClientPool;
import tic.service.requesthandler.TIC2WebSocketRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * TIC2WebSocket Netty WebSocket Server.
 *
 * <p>This class manages the lifecycle of the WebSocket server, including startup,
 * shutdown, and connection handling. It configures the Netty pipeline and integrates with the client pool
 * and request handler to support real-time TIC data exchange over WebSocket.
 *
 * <p>Responsibilities include:
 * <ul>
 *   <li>Starting and stopping the WebSocket server
 *   <li>Managing server channel and event loop groups
 *   <li>Binding to the specified host and port
 *   <li>Integrating with TIC2WebSocket client pool and request handler
 *   <li>Logging server lifecycle events and errors
 * </ul>
 *
 * <p>This class is intended to be used as the main entry point for launching the TIC2WebSocket server.
 *
 * @author Enedis Smarties team
 * @see TIC2WebSocketClientPool
 * @see TIC2WebSocketRequestHandler
 * @see TIC2WebSocketChannelInitializer
 */
public class TIC2WebSocketServer {
  /** Logger for server lifecycle and events. */
  private static final Logger logger = LogManager.getLogger(TIC2WebSocketServer.class);

  /** Host address to bind the server. */
  private final String host;
  /** Port to bind the server. */
  private final int port;
  /** Pool managing active WebSocket clients. */
  private final TIC2WebSocketClientPool clientPool;
  /** Handler for processing incoming requests. */
  private final TIC2WebSocketRequestHandler requestHandler;

  /** Netty boss event loop group (accepts connections). */
  private EventLoopGroup bossGroup;
  /** Netty worker event loop group (handles traffic). */
  private EventLoopGroup workerGroup;
  /** Main server channel. */
  private Channel serverChannel;

  /**
   * Constructs a new TIC2WebSocketServer.
   *
   * @param host the host address to bind the server
   * @param port the port to bind the server
   * @param clientPool the pool managing WebSocket clients
   * @param requestHandler the handler for processing requests
   */
  public TIC2WebSocketServer(
      String host,
      int port,
      TIC2WebSocketClientPool clientPool,
      TIC2WebSocketRequestHandler requestHandler) {
    this.host = host;
    this.port = port;
    this.clientPool = clientPool;
    this.requestHandler = requestHandler;
  }

  /**
   * Starts the WebSocket server and binds to the configured host and port.
   *
   * <p>Initializes Netty event loop groups and configures the server pipeline. Logs startup events and
   * handles errors during server initialization.
   *
   * @throws Exception if server startup fails
   */
  public void start() throws Exception {
    logger.info("Starting TIC2WebSocket Netty server on {}:{}", host, port);

    bossGroup = new NioEventLoopGroup(1);
    workerGroup = new NioEventLoopGroup();

    try {
      ServerBootstrap bootstrap = new ServerBootstrap();
      bootstrap
          .group(bossGroup, workerGroup)
          .channel(NioServerSocketChannel.class)
          .handler(new LoggingHandler(LogLevel.INFO))
          .childHandler(new TIC2WebSocketChannelInitializer(clientPool, requestHandler));

      ChannelFuture future = bootstrap.bind(host, port).sync();
      serverChannel = future.channel();

      logger.info("TIC2WebSocket Netty server started successfully on {}:{}", host, port);
    } catch (Exception e) {
      logger.error("Failed to start TIC2WebSocket Netty server", e);
      stop();
      throw e;
    }
  }

  /**
   * Stops the WebSocket server and releases resources.
   *
   * <p>Closes the server channel and gracefully shuts down event loop groups. Logs shutdown events.
   */
  public void stop() {
    logger.info("Stopping TIC2WebSocket Netty server");

    try {
      if (serverChannel != null) {
        serverChannel.close().sync();
      }
    } catch (InterruptedException e) {
      logger.warn("Interrupted while closing server channel", e);
      Thread.currentThread().interrupt();
    } finally {
      if (bossGroup != null) {
        bossGroup.shutdownGracefully();
      }
      if (workerGroup != null) {
        workerGroup.shutdownGracefully();
      }
    }

    logger.info("TIC2WebSocket Netty server stopped");
  }

  /**
   * Waits for the server channel to close.
   *
   * <p>Blocks until the server channel is closed, allowing for graceful shutdown.
   *
   * @throws InterruptedException if interrupted while waiting
   */
  public void waitForClose() throws InterruptedException {
    if (serverChannel != null) {
      serverChannel.closeFuture().sync();
    }
  }
}
