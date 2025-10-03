// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.tic.service.message.factory;

import enedis.lab.util.message.factory.MessageFactory;

/** TIC2WebSocket message factory */
public class TIC2WebSocketMessageFactory extends MessageFactory {
  /** Default constructor */
  public TIC2WebSocketMessageFactory() {
    super(
        new TIC2WebSocketRequestFactory(),
        new TIC2WebSocketResponseFactory(),
        new TIC2WebSocketEventFactory());
  }
}
