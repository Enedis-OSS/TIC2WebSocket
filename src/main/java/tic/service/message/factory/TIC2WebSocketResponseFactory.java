// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.service.message.factory;

import enedis.lab.util.message.factory.ResponseFactory;
import tic.service.message.ResponseGetAvailableTICs;
import tic.service.message.ResponseGetModemsInfo;
import tic.service.message.ResponseReadTIC;
import tic.service.message.ResponseSubscribeTIC;
import tic.service.message.ResponseUnsubscribeTIC;

/**
 * Factory for creating TIC2WebSocket response messages.
 *
 * <p>This class extends {@link ResponseFactory} to register and instantiate response message types
 * specific to the TIC2WebSocket protocol, including responses for TICs, modems, subscriptions, and
 * reads.
 *
 * <p>Key features include:
 *
 * <ul>
 *   <li>Registers all supported TIC2WebSocket response message classes
 *   <li>Supports dynamic response creation by message name
 * </ul>
 *
 * @author Enedis Smarties team
 * @see ResponseFactory
 * @see ResponseGetAvailableTICs
 * @see ResponseGetModemsInfo
 * @see ResponseReadTIC
 * @see ResponseSubscribeTIC
 * @see ResponseUnsubscribeTIC
 */
public class TIC2WebSocketResponseFactory extends ResponseFactory {
  /** Constructs a new response factory and registers TIC2WebSocket response message types. */
  public TIC2WebSocketResponseFactory() {
    super();
    this.addMessageClass(ResponseGetAvailableTICs.NAME, ResponseGetAvailableTICs.class);
    this.addMessageClass(ResponseGetModemsInfo.NAME, ResponseGetModemsInfo.class);
    this.addMessageClass(ResponseReadTIC.NAME, ResponseReadTIC.class);
    this.addMessageClass(ResponseSubscribeTIC.NAME, ResponseSubscribeTIC.class);
    this.addMessageClass(ResponseUnsubscribeTIC.NAME, ResponseUnsubscribeTIC.class);
  }
}
