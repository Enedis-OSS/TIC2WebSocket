// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.tic.service.message.factory;

import enedis.lab.util.message.factory.ResponseFactory;
import enedis.tic.service.message.ResponseGetAvailableTICs;
import enedis.tic.service.message.ResponseGetModemsInfo;
import enedis.tic.service.message.ResponseReadTIC;
import enedis.tic.service.message.ResponseSubscribeTIC;
import enedis.tic.service.message.ResponseUnsubscribeTIC;

/** TIC2WebSocket request factory */
public class TIC2WebSocketResponseFactory extends ResponseFactory {
  /** Default constructor */
  public TIC2WebSocketResponseFactory() {
    super();
    this.addMessageClass(ResponseGetAvailableTICs.NAME, ResponseGetAvailableTICs.class);
    this.addMessageClass(ResponseGetModemsInfo.NAME, ResponseGetModemsInfo.class);
    this.addMessageClass(ResponseReadTIC.NAME, ResponseReadTIC.class);
    this.addMessageClass(ResponseSubscribeTIC.NAME, ResponseSubscribeTIC.class);
    this.addMessageClass(ResponseUnsubscribeTIC.NAME, ResponseUnsubscribeTIC.class);
  }
}
