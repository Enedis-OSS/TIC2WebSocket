// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.service.netty;

import enedis.lab.types.DataDictionaryException;
import enedis.lab.util.message.Event;
import enedis.lab.util.message.Message;
import enedis.lab.util.message.Request;
import enedis.lab.util.message.Response;
import enedis.lab.util.message.exception.MessageInvalidContentException;
import enedis.lab.util.message.exception.MessageInvalidFormatException;
import enedis.lab.util.message.exception.MessageInvalidTypeException;
import enedis.lab.util.message.exception.MessageKeyNameDoesntExistException;
import enedis.lab.util.message.exception.MessageKeyTypeDoesntExistException;
import enedis.lab.util.message.exception.UnsupportedMessageException;
import enedis.tic.core.TICIdentifier;
import tic.service.client.TIC2WebSocketClient;
import tic.service.client.TIC2WebSocketClientPool;
import tic.service.endpoint.EventSender;
import tic.service.endpoint.TIC2WebSocketEndPointErrorCode;
import tic.service.message.RequestUnsubscribeTIC;
import tic.service.message.factory.TIC2WebSocketMessageFactory;
import tic.service.requesthandler.TIC2WebSocketRequestHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Netty WebSocket handler for TIC2WebSocket.
 *
 * <p>This handler manages WebSocket connections for TIC2WebSocket, handling incoming WebSocket frames,
 * client lifecycle events, and message dispatching. It integrates with the client pool and request handler
 * to process requests and send responses or events over the WebSocket channel.
 *
 * <p>Responsibilities include:
 * <ul>
 *   <li>Managing client connections and their lifecycle
 *   <li>Parsing and validating incoming WebSocket messages
 *   <li>Handling requests and generating responses
 *   <li>Sending events and messages to clients
 *   <li>Logging and error handling for channel operations
 * </ul>
 *
 * <p>This class is intended to be used as a Netty handler in the server pipeline for real-time TIC data exchange.
 *
 * @author Enedis Smarties team
 * @see TIC2WebSocketClientPool
 * @see TIC2WebSocketRequestHandler
 * @see EventSender
 */
public class TIC2WebSocketHandler extends SimpleChannelInboundHandler<WebSocketFrame>
    implements EventSender {
  /** Logger for this handler. */
  private static final Logger logger = LogManager.getLogger(TIC2WebSocketHandler.class);

  /** Pool managing active WebSocket clients. */
  private final TIC2WebSocketClientPool clientPool;
  /** Handler for processing incoming requests. */
  private final TIC2WebSocketRequestHandler requestHandler;
  /** Factory for creating and parsing TIC2WebSocket messages. */
  private final TIC2WebSocketMessageFactory factory;

  /**
   * Constructs a new TIC2WebSocketHandler.
   *
   * @param clientPool the pool managing WebSocket clients
   * @param requestHandler the handler for processing requests
   */
  public TIC2WebSocketHandler(
      TIC2WebSocketClientPool clientPool, TIC2WebSocketRequestHandler requestHandler) {
    this.clientPool = clientPool;
    this.requestHandler = requestHandler;
    this.factory = new TIC2WebSocketMessageFactory();
  }

  /**
   * Sends an event message to the specified channel.
   *
   * @param channel the Netty channel to send the event to
   * @param event the event message to send
   */
  @Override
  public void sendEvent(Channel channel, Event event) {
    this.sendMessage(channel, event);
  }

  /**
   * Invoked when a new channel becomes active.
   *
   * <p>Creates a new client for the channel if it does not already exist.
   *
   * @param ctx the channel handler context
   * @throws Exception if an error occurs during activation
   */
  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    Channel channel = ctx.channel();
    String channelId = channel.id().asLongText();

    logger.info("Open channel " + channelId);

    if (!clientPool.exists(channelId)) {
      logger.info("Client create with channel id : " + channelId);
      clientPool.createClient(channel, this);
    } else {
      logger.error("Client with channel id : " + channelId + " already exists ! ");
    }

    super.channelActive(ctx);
  }

  /**
   * Invoked when a channel becomes inactive.
   *
   * <p>Handles client removal and sends an unsubscribe request if necessary.
   *
   * @param ctx the channel handler context
   * @throws Exception if an error occurs during deactivation
   */
  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    Channel channel = ctx.channel();
    String channelId = channel.id().asLongText();

    logger.info("Close channel " + channelId);

    if (clientPool.exists(channelId)) {
      try {
        logger.info("Generate unsubscribe request");
        Request request = new RequestUnsubscribeTIC((List<TICIdentifier>) null);
        this.handleRequest(clientPool.getClient(channelId).get(), request);

        logger.info("Remove client with channel id : " + channelId);
        clientPool.remove(channelId);
      } catch (DataDictionaryException e) {
        logger.error("Error during channel close", e);
      }
    } else {
      logger.error("Client with channel id : " + channelId + " doesn't exist ! ");
    }

    super.channelInactive(ctx);
  }

  /**
   * Handles exceptions caught during channel operations.
   *
   * <p>Logs the error and closes the channel.
   *
   * @param ctx the channel handler context
   * @param cause the exception that was caught
   * @throws Exception if an error occurs during exception handling
   */
  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    Channel channel = ctx.channel();
    String channelId = channel.id().asLongText();

    logger.error("Error on channel " + channelId, cause);

    ctx.close();
  }

  /**
   * Handles incoming WebSocket frames.
   *
   * <p>Processes text frames as TIC2WebSocket messages, validates and dispatches requests,
   * and sends responses. Unsupported frame types are logged as warnings.
   *
   * @param ctx the channel handler context
   * @param frame the received WebSocket frame
   * @throws Exception if an error occurs during frame processing
   */
  @Override
  protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
    if (frame instanceof TextWebSocketFrame) {
      TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
      String text = textFrame.text();
      Channel channel = ctx.channel();
      String channelId = channel.id().asLongText();

      logger.info("Message on channel " + channelId);

      TIC2WebSocketClient client = this.getClient(channel);

      Optional<Message> message = this.getMessage(channel, text);
      if (!message.isPresent()) {
        return;
      }

      Optional<Request> request = this.getRequest(channel, message.get());
      if (!request.isPresent()) {
        return;
      }

      Response response = this.handleRequest(client, request.get());

      this.sendMessage(channel, response);
    } else {
      // Handle other frame types if needed
      logger.warn("Unsupported frame type: " + frame.getClass().getSimpleName());
    }
  }

  /**
   * Retrieves the TIC2WebSocketClient associated with the given channel.
   *
   * <p>If no client exists for the channel, a new one is created and registered.
   *
   * @param channel the Netty channel
   * @return the associated TIC2WebSocketClient
   */
  private TIC2WebSocketClient getClient(Channel channel) {
    String channelId = channel.id().asLongText();
    Optional<TIC2WebSocketClient> clientOpt = clientPool.getClient(channelId);
    if (!clientOpt.isPresent()) {
      return clientPool.createClient(channel, this);
    }
    return clientOpt.get();
  }

  /**
   * Parses and validates a message from the given text.
   *
   * <p>Returns an empty Optional if the message is invalid or an error occurs.
   *
   * @param channel the Netty channel
   * @param text the raw message text
   * @return an Optional containing the parsed Message, or empty if invalid
   */
  private Optional<Message> getMessage(Channel channel, String text) {
    Message message = null;
    TIC2WebSocketEndPointErrorCode errorCode = TIC2WebSocketEndPointErrorCode.NO_ERROR;
    String errorMessage = "";

    try {
      message = this.factory.getMessage(text);
    } catch (MessageInvalidFormatException e) {
      errorCode = TIC2WebSocketEndPointErrorCode.INVALID_MESSAGE_FORMAT;
      errorMessage = e.getMessage();
    } catch (MessageInvalidTypeException e) {
      errorCode = TIC2WebSocketEndPointErrorCode.MESSAGE_TYPE_INVALID;
      errorMessage = e.getMessage();
    } catch (MessageKeyNameDoesntExistException e) {
      errorCode = TIC2WebSocketEndPointErrorCode.MESSAGE_NAME_MISSING;
      errorMessage = e.getMessage();
    } catch (MessageKeyTypeDoesntExistException e) {
      errorCode = TIC2WebSocketEndPointErrorCode.MESSAGE_TYPE_MISSING;
      errorMessage = e.getMessage();
    } catch (MessageInvalidContentException e) {
      errorCode = TIC2WebSocketEndPointErrorCode.INVALID_MESSAGE_CONTENT;
      errorMessage = e.getMessage();
    } catch (UnsupportedMessageException e) {
      errorCode = TIC2WebSocketEndPointErrorCode.UNSUPPORTED_MESSAGE;
      errorMessage = e.getMessage();
    }

    if (errorCode != TIC2WebSocketEndPointErrorCode.NO_ERROR) {
      logger.error("Error parsing message: " + errorMessage);
      // TODO: Send error event
      return Optional.empty();
    }

    return Optional.of(message);
  }

  /**
   * Extracts a Request from the given Message if possible.
   *
   * <p>Returns an empty Optional if the message is not a Request.
   *
   * @param channel the Netty channel
   * @param message the parsed Message
   * @return an Optional containing the Request, or empty if not applicable
   */
  private Optional<Request> getRequest(Channel channel, Message message) {
    if (!(message instanceof Request)) {
      logger.error("Message is not a request");
      // TODO: Send error event
      return Optional.empty();
    }

    return Optional.of((Request) message);
  }

  /**
   * Handles the given request using the request handler and client.
   *
   * @param client the TIC2WebSocketClient
   * @param request the request to handle
   * @return the generated Response
   */
  private Response handleRequest(TIC2WebSocketClient client, Request request) {
    return requestHandler.handle(request, client);
  }

  /**
   * Sends a message to the specified channel as a JSON WebSocket frame.
   *
   * <p>Logs the sent message and handles any errors during transmission.
   *
   * @param channel the Netty channel to send the message to
   * @param message the message to send
   */
  private void sendMessage(Channel channel, Message message) {
    try {
      String json = message.toJSON().toString();
      TextWebSocketFrame frame = new TextWebSocketFrame(json);
      channel.writeAndFlush(frame);

      logger.debug("Sent message to channel {}: {}", channel.id().asLongText(), json);
    } catch (Exception e) {
      logger.error("Error sending message to channel " + channel.id().asLongText(), e);
    }
  }
}
