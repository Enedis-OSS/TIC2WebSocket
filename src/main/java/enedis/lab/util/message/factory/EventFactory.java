// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.util.message.factory;

import enedis.lab.util.message.Event;

/**
 * Factory for creating and decoding event message objects.
 *
 * <p>This class extends {@link AbstractMessageFactory} to provide specialized support for event
 * messages. It enables registration and decoding of event types from their text representations,
 * typically JSON, and supports extensible event handling in the message processing pipeline.
 *
 * <p>Common use cases include:
 * <ul>
 *   <li>Decoding incoming event messages into typed objects</li>
 *   <li>Registering custom event types for extensibility</li>
 *   <li>Supporting generic event processing pipelines</li>
 * </ul>
 *
 * @author Enedis Smarties team
 * @see AbstractMessageFactory
 * @see Event
 */
public class EventFactory extends AbstractMessageFactory<Event> {
  /**
   * Creates a new EventFactory for event message objects.
   *
   * <p>This constructor initializes the factory for the {@link Event} class and prepares the
   * internal registry for event type mappings.
   */
  public EventFactory() {
    super(Event.class);
  }
}
