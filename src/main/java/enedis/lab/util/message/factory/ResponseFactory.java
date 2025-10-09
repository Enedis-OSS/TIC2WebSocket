// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.util.message.factory;

import enedis.lab.util.message.Response;

/**
 * Factory for creating and decoding response message objects.
 *
 * <p>This class extends {@link AbstractMessageFactory} to provide specialized support for response
 * messages. It enables registration and decoding of response types from their text representations,
 * typically JSON, and supports extensible response handling in the message processing pipeline.
 *
 * <p>Common use cases include:
 * <ul>
 *   <li>Decoding incoming response messages into typed objects</li>
 *   <li>Registering custom response types for extensibility</li>
 *   <li>Supporting generic response processing pipelines</li>
 * </ul>
 *
 * @author Enedis Smarties team
 * @see AbstractMessageFactory
 * @see Response
 */
public class ResponseFactory extends AbstractMessageFactory<Response> {
  /**
   * Creates a new ResponseFactory for response message objects.
   *
   * <p>This constructor initializes the factory for the {@link Response} class and prepares the
   * internal registry for response type mappings.
   */
  public ResponseFactory() {
    super(Response.class);
  }
}
