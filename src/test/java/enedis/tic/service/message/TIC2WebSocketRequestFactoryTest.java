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
import enedis.tic.service.message.factory.TIC2WebSocketRequestFactory;
import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class TIC2WebSocketRequestFactoryTest {
  @Test
  public void testGetMessage_RequestGetAvailableTics()
      throws MessageInvalidFormatException,
          MessageKeyTypeDoesntExistException,
          MessageKeyNameDoesntExistException,
          MessageInvalidTypeException,
          UnsupportedMessageException,
          MessageInvalidContentException {
    TIC2WebSocketRequestFactory factory = new TIC2WebSocketRequestFactory();

    String text =
        "{\"type\":\""
            + MessageType.REQUEST.name()
            + "\",\"name\":\""
            + RequestGetAvailableTICs.NAME
            + "\"}";

    Message message = factory.getMessage(text, RequestGetAvailableTICs.NAME);

    Assert.assertNotNull(message);
    Assert.assertTrue(message instanceof RequestGetAvailableTICs);

    RequestGetAvailableTICs request = (RequestGetAvailableTICs) message;

    Assert.assertEquals(MessageType.REQUEST, request.getType());
    Assert.assertEquals(RequestGetAvailableTICs.NAME, request.getName());

    Message messageDecoded = factory.getMessage(request.toString(), RequestGetAvailableTICs.NAME);
    Assert.assertNotNull(message);
    Assert.assertTrue(messageDecoded.equals(request));
  }

  @Test
  public void testGetMessage_RequestGetModemsInfo()
      throws MessageInvalidFormatException,
          MessageKeyTypeDoesntExistException,
          MessageKeyNameDoesntExistException,
          MessageInvalidTypeException,
          UnsupportedMessageException,
          MessageInvalidContentException {
    TIC2WebSocketRequestFactory factory = new TIC2WebSocketRequestFactory();

    String text =
        "{\"type\":\""
            + MessageType.REQUEST.name()
            + "\",\"name\":\""
            + RequestGetModemsInfo.NAME
            + "\"}";

    Message message = factory.getMessage(text, RequestGetModemsInfo.NAME);

    Assert.assertNotNull(message);
    Assert.assertTrue(message instanceof RequestGetModemsInfo);

    RequestGetModemsInfo request = (RequestGetModemsInfo) message;

    Assert.assertEquals(MessageType.REQUEST, request.getType());
    Assert.assertEquals(RequestGetModemsInfo.NAME, request.getName());

    Message messageDecoded = factory.getMessage(request.toString(), RequestGetModemsInfo.NAME);
    Assert.assertNotNull(message);
    Assert.assertTrue(messageDecoded.equals(request));
  }

  @Test
  public void testGetMessage_RequestReadTIC()
      throws MessageInvalidFormatException,
          MessageKeyTypeDoesntExistException,
          MessageKeyNameDoesntExistException,
          MessageInvalidTypeException,
          UnsupportedMessageException,
          MessageInvalidContentException {
    TIC2WebSocketRequestFactory factory = new TIC2WebSocketRequestFactory();

    String text =
        "{\"type\":\""
            + MessageType.REQUEST.name()
            + "\",\"name\":\""
            + RequestReadTIC.NAME
            + "\",\"data\":{\"serialNumber\":\"010203040506\"}}";

    Message message = factory.getMessage(text, RequestReadTIC.NAME);

    Assert.assertNotNull(message);
    Assert.assertTrue(message instanceof RequestReadTIC);

    RequestReadTIC request = (RequestReadTIC) message;

    Assert.assertEquals(MessageType.REQUEST, request.getType());
    Assert.assertEquals(RequestReadTIC.NAME, request.getName());
    Assert.assertNotNull(request.getData());

    Message messageDecoded = factory.getMessage(request.toString(), RequestReadTIC.NAME);
    Assert.assertNotNull(message);
    Assert.assertTrue(messageDecoded.equals(request));
  }

  @Test
  public void testGetMessage_RequestSubscribeTIC_allTIC()
      throws MessageInvalidFormatException,
          MessageKeyTypeDoesntExistException,
          MessageKeyNameDoesntExistException,
          MessageInvalidTypeException,
          UnsupportedMessageException,
          MessageInvalidContentException {
    TIC2WebSocketRequestFactory factory = new TIC2WebSocketRequestFactory();

    String text =
        "{\"type\":\""
            + MessageType.REQUEST.name()
            + "\",\"name\":\""
            + RequestSubscribeTIC.NAME
            + "\"}";

    Message message = factory.getMessage(text, RequestSubscribeTIC.NAME);

    Assert.assertNotNull(message);
    Assert.assertTrue(message instanceof RequestSubscribeTIC);

    RequestSubscribeTIC request = (RequestSubscribeTIC) message;

    Assert.assertEquals(MessageType.REQUEST, request.getType());
    Assert.assertEquals(RequestSubscribeTIC.NAME, request.getName());

    Message messageDecoded = factory.getMessage(request.toString(), RequestSubscribeTIC.NAME);
    Assert.assertNotNull(message);
    Assert.assertTrue(messageDecoded.equals(request));
  }

  @Test
  public void testGetMessage_RequestSubscribeTIC_oneTIC()
      throws MessageInvalidFormatException,
          MessageKeyTypeDoesntExistException,
          MessageKeyNameDoesntExistException,
          MessageInvalidTypeException,
          UnsupportedMessageException,
          MessageInvalidContentException {
    TIC2WebSocketRequestFactory factory = new TIC2WebSocketRequestFactory();

    String text =
        "{\"type\":\""
            + MessageType.REQUEST.name()
            + "\",\"name\":\""
            + RequestSubscribeTIC.NAME
            + "\",\"data\":{\"serialNumber\":\"010203040506\"}}";

    Message message = factory.getMessage(text, RequestSubscribeTIC.NAME);

    Assert.assertNotNull(message);
    Assert.assertTrue(message instanceof RequestSubscribeTIC);

    RequestSubscribeTIC request = (RequestSubscribeTIC) message;

    Assert.assertEquals(MessageType.REQUEST, request.getType());
    Assert.assertEquals(RequestSubscribeTIC.NAME, request.getName());
    Assert.assertNotNull(request.getData());

    Message messageDecoded = factory.getMessage(request.toString(), RequestSubscribeTIC.NAME);
    Assert.assertNotNull(message);
    Assert.assertTrue(messageDecoded.equals(request));
  }

  @Test
  public void testGetMessage_RequestSubscribeTIC_severalTIC()
      throws MessageInvalidFormatException,
          MessageKeyTypeDoesntExistException,
          MessageKeyNameDoesntExistException,
          MessageInvalidTypeException,
          UnsupportedMessageException,
          MessageInvalidContentException {
    TIC2WebSocketRequestFactory factory = new TIC2WebSocketRequestFactory();

    String text =
        "{\"type\":\""
            + MessageType.REQUEST.name()
            + "\",\"name\":\""
            + RequestSubscribeTIC.NAME
            + "\",\"data\":[{\"serialNumber\":\"010203040506\"},{\"portId\":\"1-1\"}]}";

    Message message = factory.getMessage(text, RequestSubscribeTIC.NAME);

    Assert.assertNotNull(message);
    Assert.assertTrue(message instanceof RequestSubscribeTIC);

    RequestSubscribeTIC request = (RequestSubscribeTIC) message;

    Assert.assertEquals(MessageType.REQUEST, request.getType());
    Assert.assertEquals(RequestSubscribeTIC.NAME, request.getName());
    Assert.assertNotNull(request.getData());

    Message messageDecoded = factory.getMessage(request.toString(), RequestSubscribeTIC.NAME);
    Assert.assertNotNull(message);
    Assert.assertTrue(messageDecoded.equals(request));
  }

  @Test
  public void testGetMessage_RequestUnsubscribeTIC_allTIC()
      throws MessageInvalidFormatException,
          MessageKeyTypeDoesntExistException,
          MessageKeyNameDoesntExistException,
          MessageInvalidTypeException,
          UnsupportedMessageException,
          MessageInvalidContentException {
    TIC2WebSocketRequestFactory factory = new TIC2WebSocketRequestFactory();

    String text =
        "{\"type\":\""
            + MessageType.REQUEST.name()
            + "\",\"name\":\""
            + RequestUnsubscribeTIC.NAME
            + "\"}";

    Message message = factory.getMessage(text, RequestUnsubscribeTIC.NAME);

    Assert.assertNotNull(message);
    Assert.assertTrue(message instanceof RequestUnsubscribeTIC);

    RequestUnsubscribeTIC request = (RequestUnsubscribeTIC) message;

    Assert.assertEquals(MessageType.REQUEST, request.getType());
    Assert.assertEquals(RequestUnsubscribeTIC.NAME, request.getName());

    Message messageDecoded = factory.getMessage(request.toString(), RequestUnsubscribeTIC.NAME);
    Assert.assertNotNull(message);
    Assert.assertTrue(messageDecoded.equals(request));
  }

  @Test
  public void testGetMessage_RequestUnsubscribeTIC_oneTIC()
      throws MessageInvalidFormatException,
          MessageKeyTypeDoesntExistException,
          MessageKeyNameDoesntExistException,
          MessageInvalidTypeException,
          UnsupportedMessageException,
          MessageInvalidContentException {
    TIC2WebSocketRequestFactory factory = new TIC2WebSocketRequestFactory();

    String text =
        "{\"type\":\""
            + MessageType.REQUEST.name()
            + "\",\"name\":\""
            + RequestUnsubscribeTIC.NAME
            + "\",\"data\":{\"serialNumber\":\"010203040506\"}}";

    Message message = factory.getMessage(text, RequestUnsubscribeTIC.NAME);

    Assert.assertNotNull(message);
    Assert.assertTrue(message instanceof RequestUnsubscribeTIC);

    RequestUnsubscribeTIC request = (RequestUnsubscribeTIC) message;

    Assert.assertEquals(MessageType.REQUEST, request.getType());
    Assert.assertEquals(RequestUnsubscribeTIC.NAME, request.getName());
    Assert.assertNotNull(request.getData());

    Message messageDecoded = factory.getMessage(request.toString(), RequestUnsubscribeTIC.NAME);
    Assert.assertNotNull(message);
    Assert.assertTrue(messageDecoded.equals(request));
  }

  @Test
  public void testGetMessage_RequestUnsubscribeTIC_severalTIC()
      throws MessageInvalidFormatException,
          MessageKeyTypeDoesntExistException,
          MessageKeyNameDoesntExistException,
          MessageInvalidTypeException,
          UnsupportedMessageException,
          MessageInvalidContentException {
    TIC2WebSocketRequestFactory factory = new TIC2WebSocketRequestFactory();

    String text =
        "{\"type\":\""
            + MessageType.REQUEST.name()
            + "\",\"name\":\""
            + RequestUnsubscribeTIC.NAME
            + "\",\"data\":[{\"serialNumber\":\"010203040506\"},{\"portId\":\"1-1\"}]}";

    Message message = factory.getMessage(text, RequestUnsubscribeTIC.NAME);

    Assert.assertNotNull(message);
    Assert.assertTrue(message instanceof RequestUnsubscribeTIC);

    RequestUnsubscribeTIC request = (RequestUnsubscribeTIC) message;

    Assert.assertEquals(MessageType.REQUEST, request.getType());
    Assert.assertEquals(RequestUnsubscribeTIC.NAME, request.getName());
    Assert.assertNotNull(request.getData());

    Message messageDecoded = factory.getMessage(request.toString(), RequestUnsubscribeTIC.NAME);
    Assert.assertNotNull(message);
    Assert.assertTrue(messageDecoded.equals(request));
  }
}
