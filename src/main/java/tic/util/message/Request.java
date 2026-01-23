// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.util.message;

/**
 * Abstract base class for request messages.
 *
 * <p>This class represents request messages. It enforces the accepted message type and provides
 * support for request-specific fields. Subclasses can define additional request data and behavior.
 *
 * <p>Common use cases include:
 *
 * <ul>
 *   <li>Representing client requests for data or actions
 *   <li>Handling request validation and processing
 *   <li>Extending for custom request types
 * </ul>
 *
 * @author Enedis Smarties team
 * @see Message
 */
public abstract class Request extends Message {

  /**
   * Constructor setting parameters to specific values
   *
   * @param name
   */
  public Request(String name) {
    super(MessageType.REQUEST, name);
  }
}
