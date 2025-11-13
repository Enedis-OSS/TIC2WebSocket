// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.tic.service.message;

import enedis.lab.types.DataDictionary;
import enedis.lab.types.DataDictionaryException;
import enedis.lab.types.datadictionary.KeyDescriptor;
import enedis.lab.types.datadictionary.KeyDescriptorDataDictionary;
import enedis.lab.util.message.Event;
import enedis.tic.core.TICCoreError;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
  /** Key for error data in the event. */
  protected static final String KEY_DATA = "data";

  /** Message name for error events. */
  public static final String NAME = "OnError";

  private List<KeyDescriptor<?>> keys = new ArrayList<KeyDescriptor<?>>();

  /** Key descriptor for error data. */
  protected KeyDescriptorDataDictionary<TICCoreError> kData;

  /** Constructs an error event with default values and loads key descriptors. */
  protected EventOnError() {
    super();
    this.loadKeyDescriptors();
    this.kName.setAcceptedValues(NAME);
  }

  /**
   * Constructs an error event from a map of values.
   *
   * @param map the map containing event parameters
   * @throws DataDictionaryException if validation fails
   */
  public EventOnError(Map<String, Object> map) throws DataDictionaryException {
    this();
    this.copy(fromMap(map));
  }

  /**
   * Constructs an error event from another DataDictionary instance.
   *
   * @param other the DataDictionary to copy from
   * @throws DataDictionaryException if validation fails
   */
  public EventOnError(DataDictionary other) throws DataDictionaryException {
    this();
    this.copy(other);
  }

  /**
   * Constructs an error event with explicit date/time and error data.
   *
   * @param dateTime the event timestamp
   * @param data the error data
   * @throws DataDictionaryException if validation fails
   */
  public EventOnError(LocalDateTime dateTime, TICCoreError data) throws DataDictionaryException {
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
   * Returns the error data associated with this event.
   *
   * @return the error data
   */
  public TICCoreError getData() {
    return (TICCoreError) this.data.get(KEY_DATA);
  }

  /**
   * Sets the error data for this event.
   *
   * @param data the error data
   * @throws DataDictionaryException if validation fails
   */
  public void setData(TICCoreError data) throws DataDictionaryException {
    this.setData((Object) data);
  }

  /**
   * Internal setter for error data, with conversion and validation.
   *
   * @param data the error data (Object or TICCoreError)
   * @throws DataDictionaryException if validation fails
   */
  protected void setData(Object data) throws DataDictionaryException {
    this.data.put(KEY_DATA, this.kData.convert(data));
  }

  /**
   * Loads key descriptors for event parameters.
   *
   * <p>Initializes the descriptor for error data and adds it to the event.
   */
  private void loadKeyDescriptors() {
    try {
      this.kData =
          new KeyDescriptorDataDictionary<TICCoreError>(KEY_DATA, false, TICCoreError.class);
      this.keys.add(this.kData);

      this.addAllKeyDescriptor(this.keys);
    } catch (DataDictionaryException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }
}
