// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.util.message;

import java.time.LocalDateTime;

/**
 * Abstract base class for event messages.
 *
 * <p>This class represents event messages. It provides support for event-specific fields such as
 * date/time and enforces the accepted message type. Subclasses can define additional event data and
 * behavior.
 *
 * <p>Common use cases include:
 *
 * <ul>
 *   <li>Representing system or application events
 *   <li>Handling event notifications in the message pipeline
 *   <li>Extending for custom event types
 * </ul>
 *
 * @author Enedis Smarties team
 * @see Message
 */
public abstract class Event extends Message {
  private LocalDateTime dateTime;

  /**
   * Constructor setting parameters to specific values
   *
   * @param name
   * @param dateTime
   */
  public Event(String name, LocalDateTime dateTime) {
    super(MessageType.EVENT, name);
    this.setDateTime(dateTime);
  }

  /**
   * Get date time
   *
   * @return the date time
   */
  public LocalDateTime getDateTime() {
    return this.dateTime;
  }

  /**
   * Set date time
   *
   * @param dateTime
   */
  public void setDateTime(LocalDateTime dateTime)  {
    this.dateTime = dateTime;
  }
}
