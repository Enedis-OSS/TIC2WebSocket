// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.service.client;

import io.netty.channel.Channel;
import java.time.LocalDateTime;
import tic.core.TICCoreError;
import tic.core.TICCoreFrame;
import tic.core.TICCoreSubscriber;
import tic.service.endpoint.EventSender;
import tic.service.message.EventOnError;
import tic.service.message.EventOnTICData;
import tic.util.message.Event;

/**
 * WebSocket client for TIC2WebSocket service.
 *
 * <p>This class implements {@link TICCoreSubscriber} to handle TIC data and error events,
 * forwarding them to the WebSocket channel using the provided {@link EventSender}.
 *
 * <p>Key features include:
 *
 * <ul>
 *   <li>Receives TIC data frames and error notifications
 *   <li>Forwards events to the associated Netty WebSocket channel
 *   <li>Handles event serialization and transmission
 * </ul>
 *
 * @author Enedis Smarties team
 * @see TICCoreSubscriber
 * @see EventSender
 * @see Channel
 */
public class TIC2WebSocketClient implements TICCoreSubscriber {

  /** Associated Netty WebSocket channel for communication. */
  private final Channel channel;

  /** Event sender for dispatching TIC events to the channel. */
  private final EventSender eventSender;

  /**
   * Constructs a new TIC2WebSocketClient instance.
   *
   * <p>Initializes the client with the specified WebSocket channel and event sender.
   *
   * @param channel the Netty WebSocket channel for communication
   * @param eventSender the event sender responsible for dispatching events
   */
  public TIC2WebSocketClient(Channel channel, EventSender eventSender) {
    super();
    this.channel = channel;
    this.eventSender = eventSender;
  }

  /**
   * Handles incoming TIC data frames.
   *
   * <p>This method is called when a new TIC data frame is received. It creates an {@link
   * EventOnTICData} event and sends it to the WebSocket channel.
   *
   * @param frame the TIC data frame received
   */
  @Override
  public void onData(TICCoreFrame frame) {
    Event event = new EventOnTICData(LocalDateTime.now(), frame);
    this.eventSender.sendEvent(this.channel, event);
  }

  /**
   * Handles TIC error notifications.
   *
   * <p>This method is called when a TIC error occurs. It creates an {@link EventOnError} event and
   * sends it to the WebSocket channel.
   *
   * @param error the TIC error detected
   */
  @Override
  public void onError(TICCoreError error) {
    Event event = new EventOnError(LocalDateTime.now(), error);
    this.eventSender.sendEvent(this.channel, event);
  }

  /**
   * Returns the associated WebSocket channel for this client.
   *
   * @return the Netty WebSocket channel
   */
  public Channel getChannel() {
    return this.channel;
  }
}
