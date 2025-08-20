// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.tic.service.message;

import org.junit.Assert;
import org.junit.Test;

import enedis.lab.util.message.Message;
import enedis.lab.util.message.MessageType;
import enedis.lab.util.message.exception.MessageInvalidContentException;
import enedis.lab.util.message.exception.MessageInvalidFormatException;
import enedis.lab.util.message.exception.MessageInvalidTypeException;
import enedis.lab.util.message.exception.MessageKeyNameDoesntExistException;
import enedis.lab.util.message.exception.MessageKeyTypeDoesntExistException;
import enedis.lab.util.message.exception.UnsupportedMessageException;
import enedis.tic.service.message.factory.TIC2WebSocketResponseFactory;

@SuppressWarnings("javadoc")
public class TIC2WebSocketResponseFactoryTest
{
	@Test
	public void testGetMessage_ResponseGetAvailableTics() throws MessageInvalidFormatException, MessageKeyTypeDoesntExistException, MessageKeyNameDoesntExistException,
			MessageInvalidTypeException, UnsupportedMessageException, MessageInvalidContentException
	{
		TIC2WebSocketResponseFactory factory = new TIC2WebSocketResponseFactory();

		String text = "{\"type\":\"" + MessageType.RESPONSE.name() + "\",\"name\":\"" + ResponseGetAvailableTICs.NAME
				+ "\",\"dateTime\":\"01/01/2000 00:00:00\",\"errorCode\":0,\"data\":[{\"serialNumber\":\"010203040506\"}]}";

		Message message = factory.getMessage(text, ResponseGetAvailableTICs.NAME);

		Assert.assertNotNull(message);
		Assert.assertTrue(message instanceof ResponseGetAvailableTICs);

		ResponseGetAvailableTICs response = (ResponseGetAvailableTICs) message;

		Assert.assertEquals(MessageType.RESPONSE, response.getType());
		Assert.assertEquals(ResponseGetAvailableTICs.NAME, response.getName());
		Assert.assertNotNull(response.getDateTime());
		Assert.assertEquals(0, response.getErrorCode().intValue());
		Assert.assertNull(response.getErrorMessage());
		Assert.assertNotNull(response.getData());

		Message messageDecoded = factory.getMessage(response.toString(), ResponseGetAvailableTICs.NAME);
		Assert.assertNotNull(message);
		Assert.assertTrue(messageDecoded.equals(response));
	}

	@Test
	public void testGetMessage_ResponseGetModemsInfo() throws MessageInvalidFormatException, MessageKeyTypeDoesntExistException, MessageKeyNameDoesntExistException,
			MessageInvalidTypeException, UnsupportedMessageException, MessageInvalidContentException
	{
		TIC2WebSocketResponseFactory factory = new TIC2WebSocketResponseFactory();

		String text = "{\"type\":\"" + MessageType.RESPONSE.name() + "\",\"name\":\"" + ResponseGetModemsInfo.NAME
				+ "\",\"dateTime\":\"01/01/2000 00:00:00\",\"errorCode\":0,\"errorMessage\":\"error\"}";

		Message message = factory.getMessage(text, ResponseGetModemsInfo.NAME);

		Assert.assertNotNull(message);
		Assert.assertTrue(message instanceof ResponseGetModemsInfo);

		ResponseGetModemsInfo response = (ResponseGetModemsInfo) message;

		Assert.assertEquals(MessageType.RESPONSE, response.getType());
		Assert.assertEquals(ResponseGetModemsInfo.NAME, response.getName());
		Assert.assertNotNull(response.getDateTime());
		Assert.assertEquals(0, response.getErrorCode().intValue());
		Assert.assertEquals("error", response.getErrorMessage());
		Assert.assertNull(response.getData());

		Message messageDecoded = factory.getMessage(response.toString(), ResponseGetModemsInfo.NAME);
		Assert.assertNotNull(message);
		Assert.assertTrue(messageDecoded.equals(response));
	}

	@Test
	public void testGetMessage_ResponseReadTIC() throws MessageInvalidFormatException, MessageKeyTypeDoesntExistException, MessageKeyNameDoesntExistException,
			MessageInvalidTypeException, UnsupportedMessageException, MessageInvalidContentException
	{
		TIC2WebSocketResponseFactory factory = new TIC2WebSocketResponseFactory();

		String text = "{\"type\":\"" + MessageType.RESPONSE.name() + "\",\"name\":\"" + ResponseReadTIC.NAME
				+ "\",\"dateTime\":\"01/01/2000 00:00:00\",\"errorCode\":0,\"data\":{\"captureDateTime\":\"01/01/2000 00:00:00\",\"mode\":\"STANDARD\",\"identifier\":{\"serialNumber\":\"010203040506\"},\"content\":{\"URMS\":230}}}";

		Message message = factory.getMessage(text, ResponseReadTIC.NAME);

		Assert.assertNotNull(message);
		Assert.assertTrue(message instanceof ResponseReadTIC);

		ResponseReadTIC response = (ResponseReadTIC) message;

		Assert.assertEquals(MessageType.RESPONSE, response.getType());
		Assert.assertEquals(ResponseReadTIC.NAME, response.getName());
		Assert.assertNotNull(response.getDateTime());
		Assert.assertEquals(0, response.getErrorCode().intValue());
		Assert.assertNull(response.getErrorMessage());
		Assert.assertNotNull(response.getData());

		Message messageDecoded = factory.getMessage(response.toString(), ResponseReadTIC.NAME);
		Assert.assertNotNull(message);
		Assert.assertTrue(messageDecoded.equals(response));
	}

	@Test
	public void testGetMessage_ResponseSubscribeTIC() throws MessageInvalidFormatException, MessageKeyTypeDoesntExistException, MessageKeyNameDoesntExistException,
			MessageInvalidTypeException, UnsupportedMessageException, MessageInvalidContentException
	{
		TIC2WebSocketResponseFactory factory = new TIC2WebSocketResponseFactory();

		String text = "{\"type\":\"" + MessageType.RESPONSE.name() + "\",\"name\":\"" + ResponseSubscribeTIC.NAME + "\",\"dateTime\":\"01/01/2000 00:00:00\",\"errorCode\":0}";

		Message message = factory.getMessage(text, ResponseSubscribeTIC.NAME);

		Assert.assertNotNull(message);
		Assert.assertTrue(message instanceof ResponseSubscribeTIC);

		ResponseSubscribeTIC response = (ResponseSubscribeTIC) message;

		Assert.assertEquals(MessageType.RESPONSE, response.getType());
		Assert.assertEquals(ResponseSubscribeTIC.NAME, response.getName());
		Assert.assertNotNull(response.getDateTime());
		Assert.assertEquals(0, response.getErrorCode().intValue());
		Assert.assertNull(response.getErrorMessage());

		Message messageDecoded = factory.getMessage(response.toString(), ResponseSubscribeTIC.NAME);
		Assert.assertNotNull(message);
		Assert.assertTrue(messageDecoded.equals(response));
	}

	@Test
	public void testGetMessage_ResponseUnsubscribeTIC_allTIC() throws MessageInvalidFormatException, MessageKeyTypeDoesntExistException, MessageKeyNameDoesntExistException,
			MessageInvalidTypeException, UnsupportedMessageException, MessageInvalidContentException
	{
		TIC2WebSocketResponseFactory factory = new TIC2WebSocketResponseFactory();

		String text = "{\"type\":\"" + MessageType.RESPONSE.name() + "\",\"name\":\"" + ResponseUnsubscribeTIC.NAME + "\",\"dateTime\":\"01/01/2000 00:00:00\",\"errorCode\":0}";

		Message message = factory.getMessage(text, ResponseUnsubscribeTIC.NAME);

		Assert.assertNotNull(message);
		Assert.assertTrue(message instanceof ResponseUnsubscribeTIC);

		ResponseUnsubscribeTIC response = (ResponseUnsubscribeTIC) message;

		Assert.assertEquals(MessageType.RESPONSE, response.getType());
		Assert.assertEquals(ResponseUnsubscribeTIC.NAME, response.getName());
		Assert.assertNotNull(response.getDateTime());
		Assert.assertEquals(0, response.getErrorCode().intValue());
		Assert.assertNull(response.getErrorMessage());

		Message messageDecoded = factory.getMessage(response.toString(), ResponseUnsubscribeTIC.NAME);
		Assert.assertNotNull(message);
		Assert.assertTrue(messageDecoded.equals(response));
	}
}
