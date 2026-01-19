// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.service.message.factory;

import enedis.lab.util.message.factory.EventFactory;
import tic.service.message.EventOnError;
import tic.service.message.EventOnTICData;

/**
 * Factory for creating TIC2WebSocket event messages.
 *
 * <p>This class extends {@link EventFactory} to register and instantiate event message types
 * specific to the TIC2WebSocket protocol, including TIC data and error events.
 *
 * <p>Key features include:
 *
 * <ul>
 *   <li>Registers {@link EventOnTICData} and {@link EventOnError} message classes
 *   <li>Supports dynamic event creation by message name
 * </ul>
 *
 * @author Enedis Smarties team
 * @see EventFactory
 * @see EventOnTICData
 * @see EventOnError
 */
public class TIC2WebSocketEventFactory extends EventFactory {
  /** Constructs a new event factory and registers TIC2WebSocket event message types. */
  public TIC2WebSocketEventFactory() {
    super();
    this.addMessageClass(EventOnTICData.NAME, EventOnTICData.class);
    this.addMessageClass(EventOnError.NAME, EventOnError.class);
  }
}
