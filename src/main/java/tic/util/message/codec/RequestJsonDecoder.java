// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.util.message.codec;

import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import tic.core.TICIdentifier;
import tic.core.codec.TICIdentifierCodec;
import tic.service.message.RequestGetAvailableTICs;
import tic.service.message.RequestGetModemsInfo;
import tic.service.message.RequestReadTIC;
import tic.service.message.RequestSubscribeTIC;
import tic.service.message.RequestUnsubscribeTIC;
import tic.util.message.Message;
import tic.util.message.MessageType;
import tic.util.message.exception.MessageException;
import tic.util.message.exception.MessageInvalidContentException;
import tic.util.message.exception.MessageInvalidFormatException;
import tic.util.message.exception.MessageInvalidTypeException;
import tic.util.message.exception.MessageKeyNameDoesntExistException;

public class RequestJsonDecoder {

  private RequestJsonDecoder() {}

  public static Message decode(Object object) throws Exception {
    if (object == null || object instanceof JSONObject == false) {
      throw new MessageInvalidFormatException("Input is not a valid JSON object");
    }

    JSONObject jsonObject = (JSONObject) object;

    if (!jsonObject.has("name") || !jsonObject.has("type")) {
      throw new MessageInvalidContentException(
          "JSON object missing required fields 'name' or 'type'");
    }

    String type = jsonObject.getString("type");
    if (!type.equals(MessageType.REQUEST.name())) {
      throw new MessageInvalidTypeException("Invalid message type: " + type);
    }

    Message message;

    String name = jsonObject.getString("name");
    switch (name) {
      case RequestGetAvailableTICs.NAME:
        message = decodeGetAvailableTICs(jsonObject);
        break;
      case RequestGetModemsInfo.NAME:
        message = decodeGetModemsInfo(jsonObject);
        break;
      case RequestReadTIC.NAME:
        message = decodeReadTIC(jsonObject);
        break;
      case RequestSubscribeTIC.NAME:
        message = decodeSubscribeTIC(jsonObject);
        break;
      case RequestUnsubscribeTIC.NAME:
        message = decodeUnsubscribeTIC(jsonObject);
        break;
      default:
        throw new MessageKeyNameDoesntExistException("Unsupported request message name: " + name);
    }
    return message;
  }

  private static Message decodeGetAvailableTICs(JSONObject jsonObject) {
    return new RequestGetAvailableTICs();
  }

  private static Message decodeGetModemsInfo(JSONObject jsonObject) {
    return new RequestGetModemsInfo();
  }

  private static Message decodeReadTIC(JSONObject jsonObject) throws MessageException {
    if (!jsonObject.has("data")) {
      throw new MessageInvalidContentException(
          "JSON object missing required field 'data' for ReadTIC request");
    }
    JSONObject dataObject = jsonObject.getJSONObject("data");
    return new RequestReadTIC(TICIdentifierCodec.getInstance().decodeFromJsonObject(dataObject));
  }

  private static Message decodeSubscribeTIC(JSONObject jsonObject) throws MessageException {
    return new RequestSubscribeTIC(decodeOptionalTICIdentifierList(jsonObject));
  }

  private static Message decodeUnsubscribeTIC(JSONObject jsonObject) throws MessageException {
    return new RequestUnsubscribeTIC(decodeOptionalTICIdentifierList(jsonObject));
  }

  private static List<TICIdentifier> decodeOptionalTICIdentifierList(JSONObject jsonObject)
      throws MessageException {
    if (jsonObject == null || !jsonObject.has("data") || jsonObject.isNull("data")) {
      return null;
    }

    Object dataValue = jsonObject.opt("data");
    if (dataValue == null || dataValue == JSONObject.NULL) {
      return null;
    }

    try {
      if (dataValue instanceof JSONArray) {
        return TICIdentifierCodec.getInstance().decodeFromJsonArray(dataValue);
      }
      if (dataValue instanceof JSONObject) {
        TICIdentifier identifier = TICIdentifierCodec.getInstance().decodeFromJsonObject(dataValue);
        return Collections.singletonList(identifier);
      }
    } catch (IllegalArgumentException e) {
      throw new MessageInvalidFormatException("Invalid 'data' field: " + e.getMessage(), e);
    }

    throw new MessageInvalidFormatException(
        "Invalid 'data' field: expected an array of TICIdentifier or a single TICIdentifier"
            + " object");
  }
}
