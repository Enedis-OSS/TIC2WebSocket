// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.tic.service.message.factory;

import enedis.lab.util.message.factory.EventFactory;
import enedis.tic.service.message.EventOnError;
import enedis.tic.service.message.EventOnTICData;

/** TIC2WebSocket request factory */
public class TIC2WebSocketEventFactory extends EventFactory {
  /** Default constructor */
  public TIC2WebSocketEventFactory() {
    super();
    this.addMessageClass(EventOnTICData.NAME, EventOnTICData.class);
    this.addMessageClass(EventOnError.NAME, EventOnError.class);
  }
}
