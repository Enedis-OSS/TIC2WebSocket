// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.tic.service.message;

import enedis.lab.util.message.MessageType;
import tic.util.message.exception.MessageInvalidContentException;
import tic.util.message.exception.MessageInvalidFormatException;
import tic.util.message.exception.MessageInvalidTypeException;
import tic.util.message.exception.MessageKeyNameDoesntExistException;
import tic.util.message.exception.MessageKeyTypeDoesntExistException;
import tic.util.message.exception.UnsupportedMessageException;
import enedis.tic.service.message.factory.TIC2WebSocketMessageFactory;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class TIC2WebSocketMessageFactoryTest {
  @Test(expected = UnsupportedMessageException.class)
  public void testGetMessage_UnsupportedRequest()
      throws MessageInvalidFormatException,
          MessageKeyTypeDoesntExistException,
          MessageKeyNameDoesntExistException,
          MessageInvalidTypeException,
          UnsupportedMessageException,
          MessageInvalidContentException {
    TIC2WebSocketMessageFactory factory = new TIC2WebSocketMessageFactory();

    String text = "{\"type\":\"" + MessageType.REQUEST.name() + "\",\"name\":\"coucou\"}";

    factory.getMessage(text);
  }
}
