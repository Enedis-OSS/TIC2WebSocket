// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.tic.service.endpoint;

import enedis.lab.util.message.Event;
import io.netty.channel.Channel;

/** Event sender interface */
public interface EventSender {
  /**
   * Send event
   *
   * @param channel
   * @param event
   */
  public void sendEvent(Channel channel, Event event);
}
