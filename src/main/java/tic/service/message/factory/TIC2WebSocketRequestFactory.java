// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.service.message.factory;

import tic.util.message.factory.RequestFactory;
import tic.service.message.RequestGetAvailableTICs;
import tic.service.message.RequestGetModemsInfo;
import tic.service.message.RequestReadTIC;
import tic.service.message.RequestSubscribeTIC;
import tic.service.message.RequestUnsubscribeTIC;

/**
 * Factory for creating TIC2WebSocket request messages.
 *
 * <p>This class extends {@link RequestFactory} to register and instantiate request message types
 * specific to the TIC2WebSocket protocol, including requests for TICs, modems, subscriptions, and
 * reads.
 *
 * <p>Key features include:
 *
 * <ul>
 *   <li>Registers all supported TIC2WebSocket request message classes
 *   <li>Supports dynamic request creation by message name
 * </ul>
 *
 * @author Enedis Smarties team
 * @see RequestFactory
 * @see RequestGetAvailableTICs
 * @see RequestGetModemsInfo
 * @see RequestReadTIC
 * @see RequestSubscribeTIC
 * @see RequestUnsubscribeTIC
 */
public class TIC2WebSocketRequestFactory extends RequestFactory {
  /** Constructs a new request factory and registers TIC2WebSocket request message types. */
  public TIC2WebSocketRequestFactory() {
    super();
    this.addMessageClass(RequestGetAvailableTICs.NAME, RequestGetAvailableTICs.class);
    this.addMessageClass(RequestGetModemsInfo.NAME, RequestGetModemsInfo.class);
    this.addMessageClass(RequestReadTIC.NAME, RequestReadTIC.class);
    this.addMessageClass(RequestSubscribeTIC.NAME, RequestSubscribeTIC.class);
    this.addMessageClass(RequestUnsubscribeTIC.NAME, RequestUnsubscribeTIC.class);
  }
}
