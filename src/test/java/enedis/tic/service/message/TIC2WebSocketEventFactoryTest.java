// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.tic.service.message;

import enedis.lab.util.message.Message;
import enedis.lab.util.message.MessageType;
import tic.util.message.exception.MessageInvalidContentException;
import tic.util.message.exception.MessageInvalidFormatException;
import tic.util.message.exception.MessageInvalidTypeException;
import tic.util.message.exception.MessageKeyNameDoesntExistException;
import tic.util.message.exception.MessageKeyTypeDoesntExistException;
import tic.util.message.exception.UnsupportedMessageException;
import enedis.tic.service.message.factory.TIC2WebSocketEventFactory;
import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class TIC2WebSocketEventFactoryTest {
  @Test
  public void testGetMessage_EventOnTICData()
      throws MessageInvalidFormatException,
          MessageKeyTypeDoesntExistException,
          MessageKeyNameDoesntExistException,
          MessageInvalidTypeException,
          UnsupportedMessageException,
          MessageInvalidContentException {
    TIC2WebSocketEventFactory factory = new TIC2WebSocketEventFactory();

    String text =
        "{\"type\":\""
            + MessageType.EVENT.name()
            + "\",\"name\":\""
            + EventOnTICData.NAME
            + "\",\"dateTime\":\"01/01/2000 00:00:00\",\"data\":{\"captureDateTime\":\"01/01/2000 00:00:00\",\"mode\":\"STANDARD\",\"identifier\":{\"serialNumber\":\"010203040506\"},\"content\":{\"URMS\":230}}}";

    Message message = factory.getMessage(text, EventOnTICData.NAME);

    Assert.assertNotNull(message);
    Assert.assertTrue(message instanceof EventOnTICData);

    EventOnTICData event = (EventOnTICData) message;

    Assert.assertEquals(MessageType.EVENT, event.getType());
    Assert.assertEquals(EventOnTICData.NAME, event.getName());
    Assert.assertNotNull(event.getData());

    Message messageDecoded = factory.getMessage(event.toString(), EventOnTICData.NAME);
    Assert.assertNotNull(message);
    Assert.assertTrue(messageDecoded.equals(event));
  }

  @Test
  public void testGetMessage_EventOnError()
      throws MessageInvalidFormatException,
          MessageKeyTypeDoesntExistException,
          MessageKeyNameDoesntExistException,
          MessageInvalidTypeException,
          UnsupportedMessageException,
          MessageInvalidContentException {
    TIC2WebSocketEventFactory factory = new TIC2WebSocketEventFactory();

    String text =
        "{\"type\":\""
            + MessageType.EVENT.name()
            + "\",\"name\":\""
            + EventOnError.NAME
            + "\",\"dateTime\":\"01/01/2000 00:00:00\",\"data\":{\"errorCode\":\"-1\",\"errorMessage\":\"une erreur\",\"identifier\":{\"serialNumber\":\"010203040506\"}}}";

    Message message = factory.getMessage(text, EventOnError.NAME);

    Assert.assertNotNull(message);
    Assert.assertTrue(message instanceof EventOnError);

    EventOnError event = (EventOnError) message;

    Assert.assertEquals(MessageType.EVENT, event.getType());
    Assert.assertEquals(EventOnError.NAME, event.getName());
    Assert.assertNotNull(event.getData());

    Message messageDecoded = factory.getMessage(event.toString(), EventOnError.NAME);
    Assert.assertNotNull(message);
    Assert.assertTrue(messageDecoded.equals(event));
  }
}
