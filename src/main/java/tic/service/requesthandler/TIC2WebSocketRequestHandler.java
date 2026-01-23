// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.service.requesthandler;

import tic.util.message.Request;
import tic.util.message.Response;
import tic.service.client.TIC2WebSocketClient;

/**
 * Interface for handling TIC2WebSocket requests.
 *
 * <p>This interface defines the contract for processing incoming TIC2WebSocket requests and generating
 * appropriate responses. Implementations of this interface are responsible for interpreting requests,
 * performing necessary operations, and returning a response to the client.
 *
 * <p>Common use cases include:
 * <ul>
 *   <li>Validating and dispatching TIC2WebSocket requests
 *   <li>Managing client-specific operations
 *   <li>Generating responses based on request type and client state
 *   <li>Error handling and reporting
 * </ul>
 *
 * @author Enedis Smarties team
 * @see Request
 * @see Response
 * @see TIC2WebSocketClient
 */
public interface TIC2WebSocketRequestHandler {
  /**
   * Handles a TIC2WebSocket request and returns a response.
   *
   * <p>Processes the given request for the specified client, performing any necessary operations
   * and returning the corresponding response.
   *
   * @param request the TIC2WebSocket request to handle
   * @param client the client associated with the request
   * @return the response generated for the request
   */
  public Response handle(Request request, TIC2WebSocketClient client);
}
