// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.util.message.factory;

import enedis.lab.util.message.Request;

/**
 * Factory for decoding and instantiating request messages.
 *
 * <p>This factory is responsible for creating {@link Request} objects from their serialized
 * representations. It supports extensible registration of request message types, enabling dynamic
 * decoding and validation of incoming request messages.
 *
 * <p>Common use cases include:
 *
 * <ul>
 *   <li>Decoding incoming JSON request messages into typed {@link Request} objects
 *   <li>Registering custom request message classes for extensibility
 *   <li>Supporting modular request processing pipelines
 *   <li>Managing request message type mappings for robust decoding
 * </ul>
 *
 * <p>The factory pattern allows for decoupled message instantiation, enabling flexible handling of
 * different request types and supporting future protocol extensions.
 *
 * @author Enedis Smarties team
 * @see Request
 * @see AbstractMessageFactory
 */
public class RequestFactory extends AbstractMessageFactory<Request> {
  /** Default constructor */
  public RequestFactory() {
    super(Request.class);
  }
}
