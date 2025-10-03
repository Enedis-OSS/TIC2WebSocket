// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.tic.service.netty;

import java.util.List;
import java.util.Optional;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
import enedis.tic.service.client.TIC2WebSocketClient;
import enedis.tic.service.client.TIC2WebSocketClientPool;
import enedis.tic.service.endpoint.EventSender;
import enedis.tic.service.endpoint.TIC2WebSocketEndPointErrorCode;
import enedis.tic.service.message.RequestUnsubscribeTIC;
import enedis.tic.service.message.factory.TIC2WebSocketMessageFactory;
import enedis.tic.service.requesthandler.TIC2WebSocketRequestHandler;

/**
 * Netty WebSocket handler for TIC2WebSocket
 */
public class TIC2WebSocketHandler extends SimpleChannelInboundHandler<WebSocketFrame> implements EventSender
{
    private static final Logger logger = LogManager.getLogger(TIC2WebSocketHandler.class);

    private final TIC2WebSocketClientPool clientPool;
    private final TIC2WebSocketRequestHandler requestHandler;
    private final TIC2WebSocketMessageFactory factory;

    /**
     * Constructor
     * 
     * @param clientPool     the client pool
     * @param requestHandler the request handler
     */
    public TIC2WebSocketHandler(TIC2WebSocketClientPool clientPool, TIC2WebSocketRequestHandler requestHandler)
    {
        this.clientPool = clientPool;
        this.requestHandler = requestHandler;
        this.factory = new TIC2WebSocketMessageFactory();
    }

    @Override
    public void sendEvent(Channel channel, Event event)
    {
        this.sendMessage(channel, event);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        Channel channel = ctx.channel();
        String channelId = channel.id().asLongText();

        logger.info("Open channel " + channelId);

        if (!clientPool.exists(channelId))
        {
            logger.info("Client create with channel id : " + channelId);
            clientPool.createClient(channel, this);
        }
        else
        {
            logger.error("Client with channel id : " + channelId + " already exists ! ");
        }

        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        Channel channel = ctx.channel();
        String channelId = channel.id().asLongText();

        logger.info("Close channel " + channelId);

        if (clientPool.exists(channelId))
        {
            try
            {
                logger.info("Generate unsubscribe request");
                Request request = new RequestUnsubscribeTIC((List<TICIdentifier>) null);
                this.handleRequest(clientPool.getClient(channelId).get(), request);

                logger.info("Remove client with channel id : " + channelId);
                clientPool.remove(channelId);
            }
            catch (DataDictionaryException e)
            {
                logger.error("Error during channel close", e);
            }
        }
        else
        {
            logger.error("Client with channel id : " + channelId + " doesn't exist ! ");
        }

        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        Channel channel = ctx.channel();
        String channelId = channel.id().asLongText();

        logger.error("Error on channel " + channelId, cause);

        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception
    {
        if (frame instanceof TextWebSocketFrame)
        {
            TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
            String text = textFrame.text();
            Channel channel = ctx.channel();
            String channelId = channel.id().asLongText();

            logger.info("Message on channel " + channelId);

            TIC2WebSocketClient client = this.getClient(channel);

            Optional<Message> message = this.getMessage(channel, text);
            if (!message.isPresent())
            {
                return;
            }

            Optional<Request> request = this.getRequest(channel, message.get());
            if (!request.isPresent())
            {
                return;
            }

            Response response = this.handleRequest(client, request.get());

            this.sendMessage(channel, response);
        }
        else
        {
            // Handle other frame types if needed
            logger.warn("Unsupported frame type: " + frame.getClass().getSimpleName());
        }
    }

    private TIC2WebSocketClient getClient(Channel channel)
    {
        String channelId = channel.id().asLongText();
        Optional<TIC2WebSocketClient> clientOpt = clientPool.getClient(channelId);
        if (!clientOpt.isPresent())
        {
            return clientPool.createClient(channel, this);
        }
        return clientOpt.get();
    }

    private Optional<Message> getMessage(Channel channel, String text)
    {
        Message message = null;
        TIC2WebSocketEndPointErrorCode errorCode = TIC2WebSocketEndPointErrorCode.NO_ERROR;
        String errorMessage = "";

        try
        {
            message = this.factory.getMessage(text);
        }
        catch (MessageInvalidFormatException e)
        {
            errorCode = TIC2WebSocketEndPointErrorCode.INVALID_MESSAGE_FORMAT;
            errorMessage = e.getMessage();
        }
        catch (MessageInvalidTypeException e)
        {
            errorCode = TIC2WebSocketEndPointErrorCode.MESSAGE_TYPE_INVALID;
            errorMessage = e.getMessage();
        }
        catch (MessageKeyNameDoesntExistException e)
        {
            errorCode = TIC2WebSocketEndPointErrorCode.MESSAGE_NAME_MISSING;
            errorMessage = e.getMessage();
        }
        catch (MessageKeyTypeDoesntExistException e)
        {
            errorCode = TIC2WebSocketEndPointErrorCode.MESSAGE_TYPE_MISSING;
            errorMessage = e.getMessage();
        }
        catch (MessageInvalidContentException e)
        {
            errorCode = TIC2WebSocketEndPointErrorCode.INVALID_MESSAGE_CONTENT;
            errorMessage = e.getMessage();
        }
        catch (UnsupportedMessageException e)
        {
            errorCode = TIC2WebSocketEndPointErrorCode.UNSUPPORTED_MESSAGE;
            errorMessage = e.getMessage();
        }

        if (errorCode != TIC2WebSocketEndPointErrorCode.NO_ERROR)
        {
            logger.error("Error parsing message: " + errorMessage);
            // TODO: Send error event
            return Optional.empty();
        }

        return Optional.of(message);
    }

    private Optional<Request> getRequest(Channel channel, Message message)
    {
        if (!(message instanceof Request))
        {
            logger.error("Message is not a request");
            // TODO: Send error event
            return Optional.empty();
        }

        return Optional.of((Request) message);
    }

    private Response handleRequest(TIC2WebSocketClient client, Request request)
    {
        return requestHandler.handle(request, client);
    }

    private void sendMessage(Channel channel, Message message)
    {
        try
        {
            String json = message.toJSON().toString();
            TextWebSocketFrame frame = new TextWebSocketFrame(json);
            channel.writeAndFlush(frame);

            logger.debug("Sent message to channel {}: {}", channel.id().asLongText(), json);
        }
        catch (Exception e)
        {
            logger.error("Error sending message to channel " + channel.id().asLongText(), e);
        }
    }
}
