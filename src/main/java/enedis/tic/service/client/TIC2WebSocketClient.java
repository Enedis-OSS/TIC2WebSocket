// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.tic.service.client;

import enedis.lab.types.DataDictionaryException;
import enedis.lab.util.message.Event;
import enedis.tic.core.TICCoreError;
import enedis.tic.core.TICCoreFrame;
import enedis.tic.core.TICCoreSubscriber;
import enedis.tic.service.endpoint.EventSender;
import enedis.tic.service.message.EventOnError;
import enedis.tic.service.message.EventOnTICData;
import io.netty.channel.Channel;
import java.time.LocalDateTime;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** TIC2WebSocket client */
public class TIC2WebSocketClient implements TICCoreSubscriber {
  private Logger logger;
  private Channel channel;
  private EventSender eventSender;

  /**
   * Constructor
   *
   * @param channel
   * @param eventSender
   */
  public TIC2WebSocketClient(Channel channel, EventSender eventSender) {
    super();
    this.logger = LogManager.getLogger(this.getClass());
    this.channel = channel;
    this.eventSender = eventSender;
  }

  @Override
  public void onData(TICCoreFrame frame) {
    try {
      Event event = new EventOnTICData(LocalDateTime.now(), frame);
      this.eventSender.sendEvent(this.channel, event);
    } catch (DataDictionaryException e) {
      this.logger.error(e.getMessage(), e);
    }
  }

  @Override
  public void onError(TICCoreError error) {
    try {
      Event event = new EventOnError(LocalDateTime.now(), error);
      this.eventSender.sendEvent(this.channel, event);
    } catch (DataDictionaryException e) {
      this.logger.error(e.getMessage(), e);
    }
  }

  /**
   * Get client websocket channel
   *
   * @return websocket channel
   */
  public Channel getChannel() {
    return this.channel;
  }
}
