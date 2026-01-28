// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.util.message.codec;

import org.json.JSONObject;
import tic.util.codec.JsonObjectCodec;
import tic.util.codec.JsonStringCodec;
import tic.util.message.Event;
import tic.util.message.Message;
import tic.util.message.Request;
import tic.util.message.Response;
import tic.util.message.exception.MessageException;
import tic.util.message.exception.MessageInvalidFormatException;

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
    if (jsonString == null || jsonString.isEmpty()) {
      throw new MessageInvalidFormatException("Input JSON string is null or empty");
    }

    JSONObject jsonObject = null;

    try {
      jsonObject = new JSONObject(jsonString);
    } catch (Exception e) {
      throw new MessageInvalidFormatException("Input is not a valid JSON string", e);
    }

    return decodeFromJsonObject(jsonObject);
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
    try {
      return RequestJsonDecoder.decode(object);
    } catch (Exception e) {
      throw new MessageException("Failed to decode Message from JSON object", e);
    }
  }
}
