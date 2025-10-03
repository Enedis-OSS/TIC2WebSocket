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
 * EventOnError class
 *
 * <p>Generated
 */
public class EventOnError extends Event {
  protected static final String KEY_DATA = "data";

  /** Message name */
  public static final String NAME = "OnError";

  private List<KeyDescriptor<?>> keys = new ArrayList<KeyDescriptor<?>>();

  protected KeyDescriptorDataDictionary<TICCoreError> kData;

  protected EventOnError() {
    super();
    this.loadKeyDescriptors();

    this.kName.setAcceptedValues(NAME);
  }

  /**
   * Constructor using map
   *
   * @param map
   * @throws DataDictionaryException
   */
  public EventOnError(Map<String, Object> map) throws DataDictionaryException {
    this();
    this.copy(fromMap(map));
  }

  /**
   * Constructor using datadictionary
   *
   * @param other
   * @throws DataDictionaryException
   */
  public EventOnError(DataDictionary other) throws DataDictionaryException {
    this();
    this.copy(other);
  }

  /**
   * Constructor setting parameters to specific values
   *
   * @param dateTime
   * @param data
   * @throws DataDictionaryException
   */
  public EventOnError(LocalDateTime dateTime, TICCoreError data) throws DataDictionaryException {
    this();

    this.setDateTime(dateTime);
    this.setData(data);

    this.checkAndUpdate();
  }

  @Override
  protected void updateOptionalParameters() throws DataDictionaryException {
    if (!this.exists(KEY_NAME)) {
      this.setName(NAME);
    }
    super.updateOptionalParameters();
  }

  /**
   * Get data
   *
   * @return the data
   */
  public TICCoreError getData() {
    return (TICCoreError) this.data.get(KEY_DATA);
  }

  /**
   * Set data
   *
   * @param data
   * @throws DataDictionaryException
   */
  public void setData(TICCoreError data) throws DataDictionaryException {
    this.setData((Object) data);
  }

  protected void setData(Object data) throws DataDictionaryException {
    this.data.put(KEY_DATA, this.kData.convert(data));
  }

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
