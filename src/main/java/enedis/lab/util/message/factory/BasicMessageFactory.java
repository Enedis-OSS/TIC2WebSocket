// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.util.message.factory;

import enedis.lab.types.DataDictionaryException;
import enedis.lab.util.message.Message;
import enedis.lab.util.message.MessageType;
import enedis.lab.util.message.exception.MessageInvalidFormatException;
import enedis.lab.util.message.exception.MessageInvalidTypeException;
import enedis.lab.util.message.exception.MessageKeyNameDoesntExistException;
import enedis.lab.util.message.exception.MessageKeyTypeDoesntExistException;
import org.json.JSONException;
import org.json.JSONObject;

/** Message factory */
public abstract class BasicMessageFactory {
  /**
   * Get message from text
   *
   * @param text
   * @return message
   * @throws MessageInvalidFormatException
   * @throws MessageKeyTypeDoesntExistException
   * @throws MessageKeyNameDoesntExistException
   * @throws MessageInvalidTypeException
   */
  public static Message getMessage(String text)
      throws MessageInvalidFormatException,
          MessageKeyTypeDoesntExistException,
          MessageKeyNameDoesntExistException,
          MessageInvalidTypeException {
    JSONObject messageJson = convertTextToJson(text);

    MessageType type = extractType(messageJson);
    String name = extractName(messageJson);

    return createMessage(type, name);
  }

  private static JSONObject convertTextToJson(String text) throws MessageInvalidFormatException {
    try {
      return new JSONObject(text);
    } catch (JSONException e) {
      throw new MessageInvalidFormatException(
          "Invalid format, it should be JSON : " + e.getMessage());
    }
  }

  private static MessageType extractType(JSONObject jsonObj)
      throws MessageKeyTypeDoesntExistException,
          MessageInvalidFormatException,
          MessageInvalidTypeException {
    try {
      checkKeyTypeExists(jsonObj);
      String typeStr = jsonObj.getString(Message.KEY_TYPE);
      MessageType type = MessageType.valueOf(typeStr);
      return type;
    } catch (JSONException e) {
      throw new MessageInvalidFormatException("Invalid format : " + e.getMessage());
    } catch (IllegalArgumentException e) {
      throw new MessageInvalidTypeException("Invalid format : " + e.getMessage());
    }
  }

  private static void checkKeyTypeExists(JSONObject jsonObj)
      throws MessageKeyTypeDoesntExistException {
    if (!jsonObj.has(Message.KEY_TYPE)) {
      throw new MessageKeyTypeDoesntExistException("Key type missing");
    }
  }

  private static String extractName(JSONObject jsonObj)
      throws MessageInvalidFormatException, MessageKeyNameDoesntExistException {
    try {
      checkKeyNameExists(jsonObj);
      return jsonObj.getString(Message.KEY_NAME);
    } catch (JSONException e) {
      throw new MessageInvalidFormatException("Invalid format : " + e.getMessage());
    }
  }

  private static void checkKeyNameExists(JSONObject jsonObj)
      throws MessageKeyNameDoesntExistException {
    if (!jsonObj.has(Message.KEY_NAME)) {
      throw new MessageKeyNameDoesntExistException("Key name missing");
    }
  }

  private static Message createMessage(MessageType type, String name) {
    try {
      return new Message(type, name);
    } catch (DataDictionaryException e) {
      return null;
    }
  }
}
