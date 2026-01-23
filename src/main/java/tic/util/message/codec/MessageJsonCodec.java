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
import tic.util.codec.JsonObjectCodec;
import tic.util.codec.JsonStringCodec;
import tic.util.message.Event;
import tic.util.message.Message;
import tic.util.message.MessageType;
import tic.util.message.Request;
import tic.util.message.Response;
import tic.util.message.exception.MessageException;
import tic.util.message.exception.MessageInvalidContentException;
import tic.util.message.exception.MessageInvalidFormatException;
import tic.util.message.exception.MessageInvalidTypeException;
import tic.util.message.exception.MessageKeyNameDoesntExistException;

/**
 * Codec for encoding and decoding Message objects to and from JSON format.
 *
 * <p>This codec supports serialization of various Message types, including Event, Request, and
 * Response, into JSON objects and strings. It also handles deserialization from JSON back into the
 * appropriate Message subclass based on the message name and type.
 *
 * <p>Usage examples:
 *
 * <ul>
 *   <li>Encoding a Message to a JSON string for transmission or storage
 *   <li>Decoding a JSON string received from a network or file into a Message object
 *   <li>Handling different Message types with specific encoding/decoding logic
 * </ul>
 *
 * @author Enedis Smarties team
 */
public class MessageJsonCodec implements JsonObjectCodec<Message>, JsonStringCodec<Message> {

  private static MessageJsonCodec instance = null;

  public static MessageJsonCodec getInstance() {
    if (instance == null) {
      return new MessageJsonCodec();
    }
    return instance;
  }

  private MessageJsonCodec() {}

  @Override
  public String encodeToJsonString(Message object, int indentFactor) throws Exception {
    JSONObject jsonObject = encodeToJsonObject(object);
    return jsonObject.toString(indentFactor);
  }

  @Override
  public Message decodeFromJsonString(String jsonString, int indentFactor) throws Exception {
    throw new UnsupportedOperationException("Unimplemented method 'decodeFromJsonString'");
  }

  @Override
  public JSONObject encodeToJsonObject(Message message) {
    JSONObject jsonMessage = new JSONObject();
    jsonMessage.put("name", message.getName());
    jsonMessage.put("type", message.getType().toString());

    switch (message.getType()) {
      case EVENT:
        return EventJsonEncoder.encode((Event) message, jsonMessage);
      case REQUEST:
        return RequestJsonEncoder.encode((Request) message, jsonMessage);
      case RESPONSE:
        return ResponseJsonEncoder.encode((Response) message, jsonMessage);
      default:
        return jsonMessage;
    }
  }

  @Override
  public Message decodeFromJsonObject(Object object) throws MessageException {
    if (object == null || object instanceof JSONObject == false) {
      throw new MessageInvalidFormatException("Input is not a valid JSON object");
    }

    JSONObject jsonObject = (JSONObject) object;

    if (!jsonObject.has("name") || !jsonObject.has("type")) {
      throw new MessageInvalidContentException("JSON object missing required fields 'name' or 'type'");
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

  private Message decodeGetAvailableTICs(JSONObject jsonObject) {
    return new RequestGetAvailableTICs();
  }

  private Message decodeGetModemsInfo(JSONObject jsonObject) {
    return new RequestGetModemsInfo();
  }

  private Message decodeReadTIC(JSONObject jsonObject) throws MessageException {
    if (!jsonObject.has("data")) {
      throw new MessageInvalidContentException("JSON object missing required field 'data' for ReadTIC request");
    }
    JSONObject dataObject = jsonObject.getJSONObject("data");
    return new RequestReadTIC(TICIdentifierCodec.getInstance().decodeFromJsonObject(dataObject));
  }

  private Message decodeSubscribeTIC(JSONObject jsonObject) throws MessageException {
    return new RequestSubscribeTIC(decodeOptionalTICIdentifierList(jsonObject));
  }

  private Message decodeUnsubscribeTIC(JSONObject jsonObject) throws MessageException {
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
