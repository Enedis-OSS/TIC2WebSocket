// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.util.message.factory;

import enedis.lab.util.message.Message;
import enedis.lab.util.message.exception.MessageInvalidContentException;
import enedis.lab.util.message.exception.MessageInvalidFormatException;
import enedis.lab.util.message.exception.MessageInvalidTypeException;
import enedis.lab.util.message.exception.MessageKeyNameDoesntExistException;
import enedis.lab.util.message.exception.MessageKeyTypeDoesntExistException;
import enedis.lab.util.message.exception.UnsupportedMessageException;

/** Message factory */
public class MessageFactory {
  private RequestFactory requestFactory;
  private ResponseFactory responseFactory;
  private EventFactory eventFactory;

  /** Default constructor */
  public MessageFactory() {
    super();
  }

  /**
   * Constructor using field
   *
   * @param requestFactory
   * @param responseFactory
   * @param eventFactory
   */
  public MessageFactory(
      RequestFactory requestFactory, ResponseFactory responseFactory, EventFactory eventFactory) {
    super();
    this.requestFactory = requestFactory;
    this.responseFactory = responseFactory;
    this.eventFactory = eventFactory;
  }

  /**
   * Get message from String
   *
   * @param text
   * @return message
   * @throws MessageInvalidTypeException
   * @throws MessageKeyNameDoesntExistException
   * @throws MessageKeyTypeDoesntExistException
   * @throws MessageInvalidFormatException
   * @throws MessageInvalidContentException
   * @throws UnsupportedMessageException
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
   * Set request factory
   *
   * @param requestFactory
   */
  public void setRequestFactory(RequestFactory requestFactory) {
    this.requestFactory = requestFactory;
  }

  /**
   * Set response factory
   *
   * @param responseFactory
   */
  public void setResponseFactory(ResponseFactory responseFactory) {
    this.responseFactory = responseFactory;
  }

  /**
   * Set event factory
   *
   * @param eventFactory
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
