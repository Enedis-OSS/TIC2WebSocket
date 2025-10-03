// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.tic.service.message.factory;

import enedis.lab.util.message.factory.RequestFactory;
import enedis.tic.service.message.RequestGetAvailableTICs;
import enedis.tic.service.message.RequestGetModemsInfo;
import enedis.tic.service.message.RequestReadTIC;
import enedis.tic.service.message.RequestSubscribeTIC;
import enedis.tic.service.message.RequestUnsubscribeTIC;

/** TIC2WebSocket request factory */
public class TIC2WebSocketRequestFactory extends RequestFactory {
  /** Default constructor */
  public TIC2WebSocketRequestFactory() {
    super();
    this.addMessageClass(RequestGetAvailableTICs.NAME, RequestGetAvailableTICs.class);
    this.addMessageClass(RequestGetModemsInfo.NAME, RequestGetModemsInfo.class);
    this.addMessageClass(RequestReadTIC.NAME, RequestReadTIC.class);
    this.addMessageClass(RequestSubscribeTIC.NAME, RequestSubscribeTIC.class);
    this.addMessageClass(RequestUnsubscribeTIC.NAME, RequestUnsubscribeTIC.class);
  }
}
