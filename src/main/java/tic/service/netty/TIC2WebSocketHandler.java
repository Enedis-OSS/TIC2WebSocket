// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.service.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import tic.core.TICIdentifier;
import tic.service.client.TIC2WebSocketClient;
import tic.service.client.TIC2WebSocketClientPool;
import tic.service.endpoint.EventSender;
import tic.service.endpoint.TIC2WebSocketEndPointErrorCode;
import tic.service.message.RequestUnsubscribeTIC;
import tic.service.message.ResponseError;
import tic.service.requesthandler.TIC2WebSocketRequestHandler;
import tic.util.message.Event;
import tic.util.message.Message;
import tic.util.message.Request;
import tic.util.message.Response;
import tic.util.message.codec.MessageJsonCodec;
import tic.util.message.exception.MessageException;
import tic.util.message.exception.MessageInvalidContentException;
import tic.util.message.exception.MessageInvalidFormatException;
import tic.util.message.exception.MessageInvalidTypeException;
import tic.util.message.exception.MessageKeyNameDoesntExistException;
import tic.util.message.exception.MessageKeyTypeDoesntExistException;
import tic.util.message.exception.UnsupportedMessageException;

/**
 * Netty WebSocket handler for TIC2WebSocket.
 *
 * <p>This handler manages WebSocket connections for TIC2WebSocket, handling incoming WebSocket
 * frames, client lifecycle events, and message dispatching. It integrates with the client pool and
 * request handler to process requests and send responses or events over the WebSocket channel.
 *
 * <p>Responsibilities include:
 *
 * <ul>
 *   <li>Managing client connections and their lifecycle
 *   <li>Parsing and validating incoming WebSocket messages
 *   <li>Handling requests and generating responses
 *   <li>Sending events and messages to clients
 *   <li>Logging and error handling for channel operations
 * </ul>
 *
 * <p>This class is intended to be used as a Netty handler in the server pipeline for real-time TIC
 * data exchange.
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

  private MessageJsonCodec messageJsonCodec = MessageJsonCodec.getInstance();

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

    logger.debug("Open channel " + channelId);

    if (!clientPool.exists(channelId)) {
      logger.debug("Client create with channel id : " + channelId);
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

    logger.debug("Close channel " + channelId);

    if (clientPool.exists(channelId)) {
      try {
        logger.debug("Generate unsubscribe request");
        Request request = new RequestUnsubscribeTIC((List<TICIdentifier>) null);
        this.handleRequest(clientPool.getClient(channelId).get(), request);

        logger.debug("Remove client with channel id : " + channelId);
        clientPool.remove(channelId);
      } catch (Exception e) {
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
   * <p>Processes text frames as TIC2WebSocket messages, validates and dispatches requests, and
   * sends responses. Unsupported frame types are logged as warnings.
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

      logger.debug("Message on channel " + channelId);

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
    JSONObject jsonObject = null;

    try {
      jsonObject = new JSONObject(text);
    } catch (JSONException e) {
      errorCode = TIC2WebSocketEndPointErrorCode.INVALID_MESSAGE_FORMAT;
      errorMessage = "Invalid JSON format: " + e.getMessage();
      logger.error("Error parsing message: " + errorMessage);
      this.sendErrorMessage(channel, errorCode, errorMessage);
      return Optional.empty();
    }

    try {
      message = messageJsonCodec.decodeFromJsonObject(jsonObject);
    } catch (MessageException e) {
      if (e instanceof MessageInvalidFormatException) {
        errorCode = TIC2WebSocketEndPointErrorCode.INVALID_MESSAGE_FORMAT;
        errorMessage = e.getMessage();
      } else if (e instanceof MessageInvalidTypeException) {
        errorCode = TIC2WebSocketEndPointErrorCode.MESSAGE_TYPE_INVALID;
        errorMessage = e.getMessage();
      } else if (e instanceof MessageKeyNameDoesntExistException) {
        errorCode = TIC2WebSocketEndPointErrorCode.MESSAGE_NAME_MISSING;
        errorMessage = e.getMessage();
      } else if (e instanceof MessageKeyTypeDoesntExistException) {
        errorCode = TIC2WebSocketEndPointErrorCode.MESSAGE_TYPE_MISSING;
        errorMessage = e.getMessage();
      } else if (e instanceof MessageInvalidContentException) {
        errorCode = TIC2WebSocketEndPointErrorCode.INVALID_MESSAGE_CONTENT;
        errorMessage = e.getMessage();
      } else if (e instanceof UnsupportedMessageException) {
        errorCode = TIC2WebSocketEndPointErrorCode.UNSUPPORTED_MESSAGE;
        errorMessage = e.getMessage();
      }
    } catch (Exception e) {
      errorCode = TIC2WebSocketEndPointErrorCode.INTERNAL_ERROR;
      errorMessage = "Internal error: " + e.getMessage();
    }

    if (errorCode != TIC2WebSocketEndPointErrorCode.NO_ERROR) {
      logger.error("Error parsing message: " + errorMessage);
      this.sendErrorMessage(channel, errorCode, errorMessage);
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
      this.sendErrorMessage(
          channel, TIC2WebSocketEndPointErrorCode.MESSAGE_TYPE_INVALID, "Message is not a request");
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

  private void sendErrorMessage(
      Channel channel, TIC2WebSocketEndPointErrorCode errorCode, String errorMessage) {
    ResponseError responseError =
        new ResponseError("ErrorResponse", LocalDateTime.now(), errorCode.value(), errorMessage);
    this.sendMessage(channel, responseError);
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
      String json = messageJsonCodec.encodeToJsonString(message);
      TextWebSocketFrame frame = new TextWebSocketFrame(json);
      channel.writeAndFlush(frame);

      logger.debug("Sent message to channel {}: {}", channel.id().asLongText(), json);
    } catch (Exception e) {
      logger.error("Error sending message to channel " + channel.id().asLongText(), e);
    }
  }
}
