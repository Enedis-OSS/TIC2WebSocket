// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.util.message.factory;

import enedis.lab.types.DataDictionaryException;
import enedis.lab.types.datadictionary.DataDictionaryBase;
import enedis.lab.util.message.Message;
import enedis.lab.util.message.exception.MessageInvalidContentException;
import enedis.lab.util.message.exception.MessageInvalidFormatException;
import enedis.lab.util.message.exception.UnsupportedMessageException;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;

/**
 * Generic factory for creating and decoding message objects.
 *
 * <p>This class provides methods for registering message types and decoding messages from text
 * representations, typically JSON. It supports extensible message handling by allowing new message
 * classes to be registered and decoded dynamically based on their names.
 *
 * <p>Common use cases include:
 * <ul>
 *   <li>Decoding incoming JSON messages into typed objects</li>
 *   <li>Registering custom message types for extensibility</li>
 *   <li>Handling errors in message format or content</li>
 *   <li>Supporting generic message processing pipelines</li>
 * </ul>
 *
 * @param <T> the type of message handled by this factory
 * @author Enedis Smarties team
 */
public class AbstractMessageFactory<T extends Message> {
  private Class<T> clazz;
  private Map<String, Class<? extends T>> messageClasses;

  /**
   * Creates a new AbstractMessageFactory for the specified message type.
   *
   * <p>This constructor initializes the factory for a given message class and prepares the
   * internal registry for message type mappings.
   *
   * @param clazz the class of message objects handled by this factory
   */
  public AbstractMessageFactory(Class<T> clazz) {
    this.clazz = clazz;
    this.messageClasses = new HashMap<String, Class<? extends T>>();
  }

  /**
   * Decodes a message from its text representation and type name.
   *
   * <p>This method parses the provided text (typically JSON) and instantiates the corresponding
   * message object based on the registered type name. It throws specific exceptions for unsupported
   * message types, invalid formats, or content errors.
   *
   * @param text the text representation of the message (usually JSON)
   * @param name the type name of the message to decode
   * @return the decoded message object
   * @throws UnsupportedMessageException if the message type is not registered or supported
   * @throws MessageInvalidFormatException if the message format is invalid (e.g., malformed JSON)
   * @throws MessageInvalidContentException if the message content fails validation
   */
  public final T getMessage(String text, String name)
      throws UnsupportedMessageException,
          MessageInvalidFormatException,
          MessageInvalidContentException {
    T message = null;

    try {
      Class<? extends T> messageClazz = this.messageClasses.get(name);

      if (messageClazz != null) {
        message = messageClazz.cast(DataDictionaryBase.fromString(text, messageClazz));
      } else {
        throw new UnsupportedMessageException(
            "Unsupported " + this.clazz.getSimpleName() + " : " + name);
      }
    } catch (JSONException e) {
      throw new MessageInvalidFormatException(
          "Invalid "
              + this.clazz.getSimpleName()
              + " "
              + name
              + " format, it should be JSON : "
              + e.getMessage(),
          e);
    } catch (DataDictionaryException e) {
      throw new MessageInvalidContentException(
          "Invalid " + this.clazz.getSimpleName() + " " + name + " content : " + e.getMessage(), e);
    }

    return message;
  }

  /**
   * Registers a message class for decoding messages with the specified type name.
   *
   * <p>This method allows the factory to support new message types by associating a name with a
   * message class. Registered types can then be decoded from text representations.
   *
   * @param name the type name of the message
   * @param messageClazz the class to use for decoding messages of this type
   */
  public final void addMessageClass(String name, Class<? extends T> messageClazz) {
    if (name == null || messageClazz == null) {
      throw new IllegalArgumentException(
          "Name and " + this.clazz.getSimpleName() + " class can't be null");
    }

    this.messageClasses.put(name, messageClazz);
  }
}
