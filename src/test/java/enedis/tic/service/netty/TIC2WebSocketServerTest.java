// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.tic.service.netty;

import static org.junit.Assert.*;

import enedis.lab.protocol.tic.TICMode;
import enedis.tic.core.TICCoreBase;
import enedis.tic.service.client.TIC2WebSocketClientPool;
import enedis.tic.service.client.TIC2WebSocketClientPoolBase;
import enedis.tic.service.requesthandler.TIC2WebSocketRequestHandler;
import enedis.tic.service.requesthandler.TIC2WebSocketRequestHandlerBase;
import java.util.Arrays;
import org.junit.Test;

/** Test for TIC2WebSocketServer */
public class TIC2WebSocketServerTest {
  @Test
  public void testServerInstantiation() {
    // Create mock dependencies
    TIC2WebSocketClientPool clientPool = new TIC2WebSocketClientPoolBase();
    TIC2WebSocketRequestHandler requestHandler =
        new TIC2WebSocketRequestHandlerBase(
            new TICCoreBase(TICMode.HISTORIC, Arrays.asList("COM1")));

    // Test server creation
    TIC2WebSocketServer server =
        new TIC2WebSocketServer("localhost", 8080, clientPool, requestHandler);

    assertNotNull("Server should not be null", server);
  }
}
