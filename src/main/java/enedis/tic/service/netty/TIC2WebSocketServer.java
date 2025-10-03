// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.tic.service.netty;

import enedis.tic.service.client.TIC2WebSocketClientPool;
import enedis.tic.service.requesthandler.TIC2WebSocketRequestHandler;
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

/** TIC2WebSocket Netty WebSocket Server */
public class TIC2WebSocketServer {
  private static final Logger logger = LogManager.getLogger(TIC2WebSocketServer.class);

  private final String host;
  private final int port;
  private final TIC2WebSocketClientPool clientPool;
  private final TIC2WebSocketRequestHandler requestHandler;

  private EventLoopGroup bossGroup;
  private EventLoopGroup workerGroup;
  private Channel serverChannel;

  /**
   * Constructor
   *
   * @param host the host to bind to
   * @param port the port to bind to
   * @param clientPool the client pool
   * @param requestHandler the request handler
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
   * Start the WebSocket server
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

  /** Stop the WebSocket server */
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
   * Wait for the server to close
   *
   * @throws InterruptedException if interrupted while waiting
   */
  public void waitForClose() throws InterruptedException {
    if (serverChannel != null) {
      serverChannel.closeFuture().sync();
    }
  }
}
