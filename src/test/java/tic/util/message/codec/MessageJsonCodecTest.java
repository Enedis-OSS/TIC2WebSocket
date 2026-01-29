// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.util.message.codec;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import tic.ResourceLoader;
import tic.core.TICIdentifier;
import tic.service.message.RequestGetAvailableTICs;
import tic.service.message.RequestGetModemsInfo;
import tic.service.message.RequestReadTIC;
import tic.service.message.RequestSubscribeTIC;
import tic.service.message.RequestUnsubscribeTIC;
import tic.util.message.Message;
import tic.util.message.MessageType;
import tic.util.message.exception.MessageException;
import tic.util.message.exception.UnsupportedMessageException;

public class MessageJsonCodecTest {

  private static JSONObject readJsonObject(String resourcePath)
      throws IOException, URISyntaxException {
    String jsonText = ResourceLoader.readString(resourcePath);
    return new JSONObject(jsonText);
  }

  @Test
  public void decodeFromJsonObject_withGetAvailableTICs() throws Exception {
    // Given
    JSONObject jsonObject = readJsonObject("/tic/util/message/codec/RequestGetAvailableTICs.json");

    // When
    Message message = MessageJsonCodec.getInstance().decodeFromJsonObject(jsonObject);

    // Then
    Assert.assertTrue(message instanceof RequestGetAvailableTICs);
    Assert.assertEquals(MessageType.REQUEST, message.getType());
    Assert.assertEquals(RequestGetAvailableTICs.NAME, message.getName());
  }

  @Test
  public void decodeFromJsonObject_withGetModemsInfo() throws Exception {
    // Given
    JSONObject jsonObject = readJsonObject("/tic/util/message/codec/RequestGetModemsInfo.json");

    // When
    Message message = MessageJsonCodec.getInstance().decodeFromJsonObject(jsonObject);

    // Then
    Assert.assertTrue(message instanceof RequestGetModemsInfo);
    Assert.assertEquals(MessageType.REQUEST, message.getType());
    Assert.assertEquals(RequestGetModemsInfo.NAME, message.getName());
  }

  @Test
  public void decodeFromJsonObject_withReadTIC_allFields() throws Exception {
    // Given
    JSONObject jsonObject =
        readJsonObject("/tic/util/message/codec/RequestReadTIC_WithAllFields.json");

    // When
    Message message = MessageJsonCodec.getInstance().decodeFromJsonObject(jsonObject);

    // Then
    Assert.assertTrue(message instanceof RequestReadTIC);
    RequestReadTIC request = (RequestReadTIC) message;
    Assert.assertEquals(RequestReadTIC.NAME, request.getName());

    TICIdentifier identifier = request.getData();
    Assert.assertNotNull(identifier);
    Assert.assertEquals("/dev/ttyUSB0", identifier.getPortName());
    Assert.assertEquals("1-1", identifier.getPortId());
    Assert.assertEquals("010203040506", identifier.getSerialNumber());
  }

  @Test
  public void decodeFromJsonObject_withReadTIC_withPortId() throws Exception {
    // Given
    JSONObject jsonObject =
        readJsonObject("/tic/util/message/codec/RequestReadTIC_WithPortId.json");

    // When
    Message message = MessageJsonCodec.getInstance().decodeFromJsonObject(jsonObject);

    // Then
    Assert.assertTrue(message instanceof RequestReadTIC);
    RequestReadTIC request = (RequestReadTIC) message;
    Assert.assertEquals(RequestReadTIC.NAME, request.getName());

    TICIdentifier identifier = request.getData();
    Assert.assertNotNull(identifier);
    Assert.assertNull(identifier.getPortName());
    Assert.assertEquals("1-1", identifier.getPortId());
    Assert.assertNull(identifier.getSerialNumber());
  }

  @Test
  public void decodeFromJsonObject_withReadTIC_withPortName() throws Exception {
    // Given
    JSONObject jsonObject =
        readJsonObject("/tic/util/message/codec/RequestReadTIC_WithPortName.json");

    // When
    Message message = MessageJsonCodec.getInstance().decodeFromJsonObject(jsonObject);

    // Then
    Assert.assertTrue(message instanceof RequestReadTIC);
    RequestReadTIC request = (RequestReadTIC) message;
    Assert.assertEquals(RequestReadTIC.NAME, request.getName());

    TICIdentifier identifier = request.getData();
    Assert.assertNotNull(identifier);
    Assert.assertEquals("/dev/ttyUSB0", identifier.getPortName());
    Assert.assertNull(identifier.getPortId());
    Assert.assertNull(identifier.getSerialNumber());
  }

  @Test
  public void decodeFromJsonObject_withReadTIC_withSerialNumber() throws Exception {
    // Given
    JSONObject jsonObject =
        readJsonObject("/tic/util/message/codec/RequestReadTIC_WithSerialNumber.json");

    // When
    Message message = MessageJsonCodec.getInstance().decodeFromJsonObject(jsonObject);

    // Then
    Assert.assertTrue(message instanceof RequestReadTIC);
    RequestReadTIC request = (RequestReadTIC) message;
    Assert.assertEquals(RequestReadTIC.NAME, request.getName());

    TICIdentifier identifier = request.getData();
    Assert.assertNotNull(identifier);
    Assert.assertNull(identifier.getPortName());
    Assert.assertNull(identifier.getPortId());
    Assert.assertEquals("010203040506", identifier.getSerialNumber());
  }

  @Test
  public void decodeFromJsonObject_withSubscribeTIC_noData() throws Exception {
    // Given
    JSONObject jsonObject =
        readJsonObject("/tic/util/message/codec/RequestSubscribeTIC_WithNoData.json");

    // When
    Message message = MessageJsonCodec.getInstance().decodeFromJsonObject(jsonObject);

    // Then
    Assert.assertTrue(message instanceof RequestSubscribeTIC);
    RequestSubscribeTIC request = (RequestSubscribeTIC) message;
    Assert.assertNull(request.getData());
  }

  @Test
  public void decodeFromJsonObject_withSubscribeTIC_emptyArray() throws Exception {
    // Given
    JSONObject jsonObject =
        readJsonObject("/tic/util/message/codec/RequestSubscribeTIC_WithEmptyArray.json");

    // When
    Message message = MessageJsonCodec.getInstance().decodeFromJsonObject(jsonObject);

    // Then
    Assert.assertTrue(message instanceof RequestSubscribeTIC);
    RequestSubscribeTIC request = (RequestSubscribeTIC) message;
    Assert.assertNotNull(request.getData());
    Assert.assertTrue(request.getData().isEmpty());
  }

  @Test
  public void decodeFromJsonObject_withSubscribeTIC_oneIdentifierList() throws Exception {
    // Given
    JSONObject jsonObject =
        readJsonObject("/tic/util/message/codec/RequestSubscribeTIC_WithOneIdentifierList.json");
    // When
    Message message = MessageJsonCodec.getInstance().decodeFromJsonObject(jsonObject);

    // Then
    Assert.assertTrue(message instanceof RequestSubscribeTIC);
    RequestSubscribeTIC request = (RequestSubscribeTIC) message;

    List<TICIdentifier> identifiers = request.getData();
    Assert.assertNotNull(identifiers);
    Assert.assertEquals(1, identifiers.size());
    Assert.assertEquals("COM7", identifiers.get(0).getPortName());
    Assert.assertNull(identifiers.get(0).getPortId());
    Assert.assertNull(identifiers.get(0).getSerialNumber());
  }

  @Test
  public void decodeFromJsonObject_withSubscribeTIC_oneIdentifier() throws Exception {
    // Given
    JSONObject jsonObject =
        readJsonObject("/tic/util/message/codec/RequestSubscribeTIC_WithOneIdentifier.json");

    // When
    Message message = MessageJsonCodec.getInstance().decodeFromJsonObject(jsonObject);

    // Then
    Assert.assertTrue(message instanceof RequestSubscribeTIC);
    RequestSubscribeTIC request = (RequestSubscribeTIC) message;

    List<TICIdentifier> identifiers = request.getData();
    Assert.assertNotNull(identifiers);
    Assert.assertEquals(1, identifiers.size());
    Assert.assertNull(identifiers.get(0).getPortName());
    Assert.assertNull(identifiers.get(0).getPortId());
    Assert.assertEquals("010203040506", identifiers.get(0).getSerialNumber());
  }

  @Test
  public void decodeFromJsonObject_withUnsubscribeTIC_noData() throws Exception {
    // Given
    JSONObject jsonObject =
        readJsonObject("/tic/util/message/codec/RequestUnsubscribeTIC_WithNoData.json");
    // When
    Message message = MessageJsonCodec.getInstance().decodeFromJsonObject(jsonObject);

    // Then
    Assert.assertTrue(message instanceof RequestUnsubscribeTIC);
    RequestUnsubscribeTIC request = (RequestUnsubscribeTIC) message;
    Assert.assertNull(request.getData());
  }

  @Test
  public void decodeFromJsonObject_withUnsubscribeTIC_emptyArray() throws Exception {
    // Given
    JSONObject jsonObject =
        readJsonObject("/tic/util/message/codec/RequestUnsubscribeTIC_WithEmptyArray.json");
    // When
    Message message = MessageJsonCodec.getInstance().decodeFromJsonObject(jsonObject);

    // Then
    Assert.assertTrue(message instanceof RequestUnsubscribeTIC);
    RequestUnsubscribeTIC request = (RequestUnsubscribeTIC) message;
    Assert.assertNotNull(request.getData());
    Assert.assertTrue(request.getData().isEmpty());
  }

  @Test
  public void decodeFromJsonObject_withUnsubscribeTIC_oneIdentifierList() throws Exception {
    // Given
    JSONObject jsonObject =
        readJsonObject("/tic/util/message/codec/RequestUnsubscribeTIC_WithOneIdentifierList.json");

    // When
    Message message = MessageJsonCodec.getInstance().decodeFromJsonObject(jsonObject);

    // Then
    Assert.assertTrue(message instanceof RequestUnsubscribeTIC);
    RequestUnsubscribeTIC request = (RequestUnsubscribeTIC) message;

    List<TICIdentifier> identifiers = request.getData();
    Assert.assertNotNull(identifiers);
    Assert.assertEquals(1, identifiers.size());
    Assert.assertEquals("COM7", identifiers.get(0).getPortName());
    Assert.assertNull(identifiers.get(0).getPortId());
    Assert.assertNull(identifiers.get(0).getSerialNumber());
  }

  @Test
  public void decodeFromJsonObject_withUnsubscribeTIC_oneIdentifier() throws Exception {
    // Given
    JSONObject jsonObject =
        readJsonObject("/tic/util/message/codec/RequestUnsubscribeTIC_WithOneIdentifier.json");

    // When
    Message message = MessageJsonCodec.getInstance().decodeFromJsonObject(jsonObject);

    // Then
    Assert.assertTrue(message instanceof RequestUnsubscribeTIC);
    RequestUnsubscribeTIC request = (RequestUnsubscribeTIC) message;

    List<TICIdentifier> identifiers = request.getData();
    Assert.assertNotNull(identifiers);
    Assert.assertEquals(1, identifiers.size());
    Assert.assertEquals("COM7", identifiers.get(0).getPortName());
    Assert.assertNull(identifiers.get(0).getPortId());
    Assert.assertNull(identifiers.get(0).getSerialNumber());
  }

  @Test
  public void decodeFromJsonObject_withNullJson() {
    // Given
    JSONObject jsonObject = null;
    Exception exception = null;

    // When
    try {
      MessageJsonCodec.getInstance().decodeFromJsonObject(jsonObject);
      Assert.fail("Expected MessageException to be thrown");
    } catch (Exception ex) {
      exception = ex;
    }

    // Then
    Assert.assertNotNull(exception);
    Assert.assertTrue(exception instanceof MessageException);
  }

  @Test
  public void decodeFromJsonObject_withMissingName() throws Exception {
    // Given
    JSONObject jsonObject = readJsonObject("/tic/util/message/codec/Invalid_MissingName.json");
    Exception exception = null;

    // When
    try {
      MessageJsonCodec.getInstance().decodeFromJsonObject(jsonObject);
      Assert.fail("Expected MessageException to be thrown");
    } catch (Exception ex) {
      exception = ex;
    }

    // Then
    Assert.assertNotNull(exception);
    Assert.assertTrue(exception instanceof MessageException);
  }

  @Test
  public void decodeFromJsonObject_withMissingType() throws Exception {
    // Given
    JSONObject jsonObject = readJsonObject("/tic/util/message/codec/Invalid_MissingType.json");
    Exception exception = null;

    // When
    try {
      MessageJsonCodec.getInstance().decodeFromJsonObject(jsonObject);
      Assert.fail("Expected MessageException to be thrown");
    } catch (Exception ex) {
      exception = ex;
    }

    // Then
    Assert.assertNotNull(exception);
    Assert.assertTrue(exception instanceof MessageException);
  }

  @Test
  public void decodeFromJsonObject_withNonRequestType() throws Exception {
    // Given
    JSONObject jsonObject = readJsonObject("/tic/util/message/codec/Invalid_TypeEvent.json");
    Exception exception = null;

    // When
    try {
      MessageJsonCodec.getInstance().decodeFromJsonObject(jsonObject);
      Assert.fail("Expected MessageException to be thrown");
    } catch (Exception ex) {
      exception = ex;
    }

    // Then
    Assert.assertNotNull(exception);
    Assert.assertTrue(exception instanceof MessageException);
  }

  @Test
  public void decodeFromJsonObject_withUnsupportedName() throws Exception {
    // Given
    JSONObject jsonObject = readJsonObject("/tic/util/message/codec/Invalid_UnsupportedName.json");
    Exception exception = null;

    // When
    try {
      MessageJsonCodec.getInstance().decodeFromJsonObject(jsonObject);
      Assert.fail("Expected UnsupportedMessageException to be thrown");
    } catch (Exception ex) {
      exception = ex;
    }

    // Then
    Assert.assertNotNull(exception);
    Assert.assertTrue(exception instanceof UnsupportedMessageException);
  }

  @Test
  public void decodeFromJsonObject_withReadTicMissingData() throws Exception {
    // Given
    JSONObject jsonObject =
        readJsonObject("/tic/util/message/codec/Invalid_ReadTIC_MissingData.json");
    Exception exception = null;

    // When
    try {
      MessageJsonCodec.getInstance().decodeFromJsonObject(jsonObject);
      Assert.fail("Expected MessageException to be thrown");
    } catch (Exception ex) {
      exception = ex;
    }
    // Then
    Assert.assertNotNull(exception);
    Assert.assertTrue(exception instanceof MessageException);
  }

  @Test
  public void encodeToJsonObject_withGetAvailableTICs() {
    // Given
    Message message = new RequestGetAvailableTICs();

    // When
    JSONObject json = MessageJsonCodec.getInstance().encodeToJsonObject(message);

    // Then
    Assert.assertEquals(RequestGetAvailableTICs.NAME, json.getString("name"));
    Assert.assertEquals(MessageType.REQUEST.toString(), json.getString("type"));
    Assert.assertFalse(json.has("data"));
  }

  @Test
  public void encodeToJsonObject_withGetModemsInfo() {
    // Given
    Message message = new RequestGetModemsInfo();

    // When
    JSONObject json = MessageJsonCodec.getInstance().encodeToJsonObject(message);

    // Then
    Assert.assertEquals(RequestGetModemsInfo.NAME, json.getString("name"));
    Assert.assertEquals(MessageType.REQUEST.toString(), json.getString("type"));
    Assert.assertFalse(json.has("data"));
  }

  @Test
  public void encodeToJsonObject_withReadTIC() {
    // Given
    TICIdentifier identifier = new TICIdentifier.Builder().portName("COM7").build();
    Message message = new RequestReadTIC(identifier);

    // When
    JSONObject json = MessageJsonCodec.getInstance().encodeToJsonObject(message);

    // Then
    Assert.assertEquals(RequestReadTIC.NAME, json.getString("name"));
    Assert.assertEquals(MessageType.REQUEST.toString(), json.getString("type"));
    Assert.assertTrue(json.has("data"));

    JSONObject data = json.getJSONObject("data");
    Assert.assertEquals("COM7", data.getString("portName"));
    Assert.assertTrue(data.isNull("portId"));
    Assert.assertTrue(data.isNull("serialNumber"));
  }

  @Test
  public void encodeToJsonObject_withSubscribeTICNullList() {
    // Given
    Message message = new RequestSubscribeTIC(null);

    // When
    JSONObject json = MessageJsonCodec.getInstance().encodeToJsonObject(message);

    // Then
    Assert.assertEquals(RequestSubscribeTIC.NAME, json.getString("name"));
    Assert.assertEquals(MessageType.REQUEST.toString(), json.getString("type"));
    Assert.assertTrue(json.has("data"));
    Assert.assertEquals(0, json.getJSONArray("data").length());
  }
}
