// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.tic.service.message.factory;

import tic.util.message.factory.MessageFactory;

/**
 * Factory for creating TIC2WebSocket protocol messages.
 *
 * <p>This class extends {@link MessageFactory} to register and instantiate request, response, and
 * event message types specific to the TIC2WebSocket protocol. It aggregates the protocol's request,
 * response, and event factories for unified message creation.
 *
 * <p>Key features include:
 *
 * <ul>
 *   <li>Centralized registration of request, response, and event factories
 *   <li>Supports dynamic message creation for all protocol message types
 * </ul>
 *
 * @author Enedis Smarties team
 * @see MessageFactory
 * @see TIC2WebSocketRequestFactory
 * @see TIC2WebSocketResponseFactory
 * @see TIC2WebSocketEventFactory
 */
public class TIC2WebSocketMessageFactory extends MessageFactory {
  /**
   * Constructs a new message factory and registers TIC2WebSocket request, response, and event
   * factories.
   */
  public TIC2WebSocketMessageFactory() {
    super(
        new TIC2WebSocketRequestFactory(),
        new TIC2WebSocketResponseFactory(),
        new TIC2WebSocketEventFactory());
  }
}
