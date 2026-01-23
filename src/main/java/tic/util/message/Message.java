// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.util.message;

/**
 * Base class for all messages.
 *
 * <p>This class provides the core structure for messages, including type and name fields. It can be
 * extended for specific message types such as requests, responses, and events.
 *
 * <p>Common use cases include:
 *
 * <ul>
 *   <li>Representing generic messages in the communication pipeline
 *   <li>Providing a base for request, response, and event messages
 *   <li>Supporting extensible message structures
 * </ul>
 *
 * @author Enedis Smarties team
 */
public class Message {
  private MessageType type;
  private String name;

  /**
   * Constructor setting parameters to specific values
   *
   * @param type
   * @param name
   */
  public Message(MessageType type, String name) {
    this.setType(type);
    this.setName(name);
  }

  /**
   * Get type
   *
   * @return the type
   */
  public MessageType getType() {
    return this.type;
  }

  /**
   * Get name
   *
   * @return the name
   */
  public String getName() {
    return this.name;
  }

  /**
   * Set type
   *
   * @param type
   */
  public void setType(MessageType type) {
    this.type = type;
  }

  /**
   * Set name
   *
   * @param name
   */
  public void setName(String name) {
    this.name = name;
  }
}
