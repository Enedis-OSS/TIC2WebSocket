// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.util.message.factory;

import enedis.lab.util.message.Message;
import tic.util.message.exception.MessageInvalidContentException;
import tic.util.message.exception.MessageInvalidFormatException;
import tic.util.message.exception.MessageInvalidTypeException;
import tic.util.message.exception.MessageKeyNameDoesntExistException;
import tic.util.message.exception.MessageKeyTypeDoesntExistException;
import tic.util.message.exception.UnsupportedMessageException;

/**
 * Central factory for decoding and dispatching messages in the TIC2WebSocket framework.
 *
 * <p>This class coordinates the decoding of messages by delegating to specialized sub-factories for
 * requests, responses, and events. It parses the message type and name, then dispatches to the
 * appropriate factory for further decoding and validation.
 *
 * <p>Common use cases include:
 *
 * <ul>
 *   <li>Decoding incoming JSON messages into typed objects
 *   <li>Coordinating request, response, and event message handling
 *   <li>Supporting extensible message processing pipelines
 *   <li>Managing sub-factory references for modularity
 * </ul>
 *
 * @author Enedis Smarties team
 * @see BasicMessageFactory
 * @see RequestFactory
 * @see ResponseFactory
 * @see EventFactory
 */
public class MessageFactory {
  private RequestFactory requestFactory;
  private ResponseFactory responseFactory;
  private EventFactory eventFactory;

  /**
   * Creates a new MessageFactory with no sub-factories set.
   *
   * <p>Sub-factories must be set before decoding messages.
   */
  public MessageFactory() {
    super();
  }

  /**
   * Creates a new MessageFactory with the specified sub-factories.
   *
   * <p>This constructor initializes the factory with request, response, and event sub-factories for
   * coordinated message decoding.
   *
   * @param requestFactory the factory for decoding request messages
   * @param responseFactory the factory for decoding response messages
   * @param eventFactory the factory for decoding event messages
   */
  public MessageFactory(
      RequestFactory requestFactory, ResponseFactory responseFactory, EventFactory eventFactory) {
    super();
    this.requestFactory = requestFactory;
    this.responseFactory = responseFactory;
    this.eventFactory = eventFactory;
  }

  /**
   * Decodes and dispatches a message from its text representation.
   *
   * <p>This method parses the provided text (typically JSON), determines the message type, and
   * delegates decoding to the appropriate sub-factory. It throws specific exceptions for
   * unsupported message types, invalid formats, or missing keys.
   *
   * @param text the text representation of the message (usually JSON)
   * @return the decoded message object
   * @throws MessageInvalidTypeException if the message type is invalid or unsupported
   * @throws MessageKeyNameDoesntExistException if the message name key is missing
   * @throws MessageKeyTypeDoesntExistException if the message type key is missing
   * @throws MessageInvalidFormatException if the message format is invalid (e.g., malformed JSON)
   * @throws MessageInvalidContentException if the message content fails validation
   * @throws UnsupportedMessageException if the message type is not registered or supported
   */
  public Message getMessage(String text)
      throws MessageInvalidFormatException,
          MessageKeyTypeDoesntExistException,
          MessageKeyNameDoesntExistException,
          MessageInvalidTypeException,
          UnsupportedMessageException,
          MessageInvalidContentException {
    this.checkSubFactoryReferences();

    Message genericMessage = BasicMessageFactory.getMessage(text);
    Message message = null;

    // @formatter:off
    switch (genericMessage.getType()) {
      case REQUEST:
        message = this.requestFactory.getMessage(text, genericMessage.getName());
        break;
      case RESPONSE:
        message = this.responseFactory.getMessage(text, genericMessage.getName());
        break;
      case EVENT:
        message = this.eventFactory.getMessage(text, genericMessage.getName());
        break;
      default:
        break;
    }
    // @formatter:on

    return message;
  }

  /**
   * Sets the request sub-factory for decoding request messages.
   *
   * @param requestFactory the factory for decoding request messages
   */
  public void setRequestFactory(RequestFactory requestFactory) {
    this.requestFactory = requestFactory;
  }

  /**
   * Sets the response sub-factory for decoding response messages.
   *
   * @param responseFactory the factory for decoding response messages
   */
  public void setResponseFactory(ResponseFactory responseFactory) {
    this.responseFactory = responseFactory;
  }

  /**
   * Sets the event sub-factory for decoding event messages.
   *
   * @param eventFactory the factory for decoding event messages
   */
  public void setEventFactory(EventFactory eventFactory) {
    this.eventFactory = eventFactory;
  }

  private void checkSubFactoryReferences() {
    if (this.requestFactory == null || this.responseFactory == null || this.eventFactory == null) {
      throw new IllegalStateException(
          "requestFactory, responseFactory and eventFactory have to be set");
    }
  }
}
