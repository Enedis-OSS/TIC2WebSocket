// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.service.message;

import java.time.LocalDateTime;
import tic.core.TICCoreFrame;
import tic.util.message.Event;

/**
 * Event message representing TIC data in the TIC2WebSocket protocol.
 *
 * <p>This class encapsulates TIC data using a {@link TICCoreFrame} object and provides constructors
 * for various initialization scenarios. It integrates with the event messaging system for data
 * notification and handling.
 *
 * <p>Key features include:
 *
 * <ul>
 *   <li>Encapsulates TIC data for protocol events
 *   <li>Supports construction from map, DataDictionary, or explicit values
 *   <li>Validates and manages TIC data using key descriptors
 * </ul>
 *
 * @author Enedis Smarties team
 * @see Event
 * @see TICCoreFrame
 */
public class EventOnTICData extends Event {
  /** Message name for TIC data events. */
  public static final String NAME = "OnTICData";

  private TICCoreFrame data;

  /**
   * Constructs a TIC data event with explicit date/time and TIC data.
   *
   * @param dateTime the event timestamp
   * @param data the TIC data
   */
  public EventOnTICData(LocalDateTime dateTime, TICCoreFrame data) {
    super(NAME, dateTime);
    this.setData(data);
  }

  /**
   * Returns the TIC data associated with this event.
   *
   * @return the TIC data
   */
  public TICCoreFrame getData() {
    return this.data;
  }

  /**
   * Sets the TIC data for this event.
   *
   * @param data the TIC data
   * @throws DataDictionaryException if validation fails
   */
  public void setData(TICCoreFrame data) {
    this.data = data;
  }
}
