// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.service.message;

import java.time.LocalDateTime;
import tic.core.TICCoreError;
import tic.util.message.Event;

/**
 * Event message representing an error in the TIC2WebSocket protocol.
 *
 * <p>This class encapsulates error information using a {@link TICCoreError} object and provides
 * constructors for various initialization scenarios. It integrates with the event messaging system
 * for error notification and handling.
 *
 * <p>Key features include:
 *
 * <ul>
 *   <li>Encapsulates error data for protocol events
 *   <li>Supports construction from map, DataDictionary, or explicit values
 *   <li>Validates and manages error data using key descriptors
 * </ul>
 *
 * @author Enedis Smarties team
 * @see Event
 * @see TICCoreError
 */
public class EventOnError extends Event {
  /** Message name for error events. */
  public static final String NAME = "OnError";

  private TICCoreError data;

  /**
   * Constructs an error event with explicit date/time and error data.
   *
   * @param dateTime the event timestamp
   * @param data the error data
   */
  public EventOnError(LocalDateTime dateTime, TICCoreError data) {
    super(NAME, dateTime);
    this.setData(data);
  }

  /**
   * Returns the error data associated with this event.
   *
   * @return the error data
   */
  public TICCoreError getData() {
    return this.data;
  }

  /**
   * Sets the error data for this event.
   *
   * @param data the error data
   * @throws DataDictionaryException if validation fails
   */
  public void setData(TICCoreError data) {
    this.data = data;
  }
}
