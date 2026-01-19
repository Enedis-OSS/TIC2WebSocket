// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.service.message;

import enedis.lab.types.DataDictionary;
import enedis.lab.types.DataDictionaryException;
import enedis.lab.types.datadictionary.KeyDescriptor;
import enedis.lab.types.datadictionary.KeyDescriptorDataDictionary;
import enedis.lab.util.message.Event;
import enedis.tic.core.TICCoreFrame;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
  /** Key for TIC data in the event. */
  protected static final String KEY_DATA = "data";

  /** Message name for TIC data events. */
  public static final String NAME = "OnTICData";

  private List<KeyDescriptor<?>> keys = new ArrayList<KeyDescriptor<?>>();

  /** Key descriptor for TIC data. */
  protected KeyDescriptorDataDictionary<TICCoreFrame> kData;

  /** Constructs a TIC data event with default values and loads key descriptors. */
  protected EventOnTICData() {
    super();
    this.loadKeyDescriptors();
    this.kName.setAcceptedValues(NAME);
  }

  /**
   * Constructs a TIC data event from a map of values.
   *
   * @param map the map containing event parameters
   * @throws DataDictionaryException if validation fails
   */
  public EventOnTICData(Map<String, Object> map) throws DataDictionaryException {
    this();
    this.copy(fromMap(map));
  }

  /**
   * Constructs a TIC data event from another DataDictionary instance.
   *
   * @param other the DataDictionary to copy from
   * @throws DataDictionaryException if validation fails
   */
  public EventOnTICData(DataDictionary other) throws DataDictionaryException {
    this();
    this.copy(other);
  }

  /**
   * Constructs a TIC data event with explicit date/time and TIC data.
   *
   * @param dateTime the event timestamp
   * @param data the TIC data
   * @throws DataDictionaryException if validation fails
   */
  public EventOnTICData(LocalDateTime dateTime, TICCoreFrame data) throws DataDictionaryException {
    this();
    this.setDateTime(dateTime);
    this.setData(data);
    this.checkAndUpdate();
  }

  /**
   * Updates optional parameters for the event, ensuring the name is set.
   *
   * @throws DataDictionaryException if validation fails
   */
  @Override
  protected void updateOptionalParameters() throws DataDictionaryException {
    if (!this.exists(KEY_NAME)) {
      this.setName(NAME);
    }
    super.updateOptionalParameters();
  }

  /**
   * Returns the TIC data associated with this event.
   *
   * @return the TIC data
   */
  public TICCoreFrame getData() {
    return (TICCoreFrame) this.data.get(KEY_DATA);
  }

  /**
   * Sets the TIC data for this event.
   *
   * @param data the TIC data
   * @throws DataDictionaryException if validation fails
   */
  public void setData(TICCoreFrame data) throws DataDictionaryException {
    this.setData((Object) data);
  }

  /**
   * Internal setter for TIC data, with conversion and validation.
   *
   * @param data the TIC data (Object or TICCoreFrame)
   * @throws DataDictionaryException if validation fails
   */
  protected void setData(Object data) throws DataDictionaryException {
    this.data.put(KEY_DATA, this.kData.convert(data));
  }

  /**
   * Loads key descriptors for event parameters.
   *
   * <p>Initializes the descriptor for TIC data and adds it to the event.
   */
  private void loadKeyDescriptors() {
    try {
      this.kData =
          new KeyDescriptorDataDictionary<TICCoreFrame>(KEY_DATA, true, TICCoreFrame.class);
      this.keys.add(this.kData);

      this.addAllKeyDescriptor(this.keys);
    } catch (DataDictionaryException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }
}
