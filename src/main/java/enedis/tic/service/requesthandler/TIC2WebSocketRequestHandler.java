// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
// 
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.tic.service.requesthandler;

import enedis.lab.util.message.Request;
import enedis.lab.util.message.Response;
import enedis.tic.service.client.TIC2WebSocketClient;

/**
 * TIC2WebSocket request handler interface
 */
public interface TIC2WebSocketRequestHandler
{
	/**
	 * Handle all TIC2WebSocket request
	 * 
	 * @param request
	 * @param client
	 * @return request response
	 */
	public Response handle(Request request, TIC2WebSocketClient client);
}
