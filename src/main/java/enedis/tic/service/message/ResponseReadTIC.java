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
import enedis.lab.util.message.Response;
import enedis.tic.core.TICCoreFrame;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Response message for TIC frame data.
 *
 * <p>This class represents a response containing a TIC frame. It provides constructors for various
 * initialization scenarios and integrates with the response messaging system.
 *
 * <p>Key features include:
 *
 * <ul>
 *   <li>Encapsulates response for TIC frame data
 *   <li>Supports construction from map, DataDictionary, or explicit parameter list
 *   <li>Validates and manages response parameters using key descriptors
 * </ul>
 *
 * @author Enedis Smarties team
 * @see Response
 * @see TICCoreFrame
 */
public class ResponseReadTIC extends Response {
  /** Key for TIC frame data in the response. */
  protected static final String KEY_DATA = "data";

  /** Message name for this response. */
  public static final String NAME = "ReadTIC";

  private List<KeyDescriptor<?>> keys = new ArrayList<KeyDescriptor<?>>();

  /** Key descriptor for TIC frame data. */
  protected KeyDescriptorDataDictionary<TICCoreFrame> kData;

  /** Constructs a response for TIC frame data with default values. */
  protected ResponseReadTIC() {
    super();
    this.loadKeyDescriptors();
    this.kName.setAcceptedValues(NAME);
  }

  /**
   * Constructs a response for TIC frame data from a map of values.
   *
   * @param map the map containing response parameters
   * @throws DataDictionaryException if validation fails
   */
  public ResponseReadTIC(Map<String, Object> map) throws DataDictionaryException {
    this();
    this.copy(fromMap(map));
  }

  /**
   * Constructs a response for TIC frame data from another DataDictionary instance.
   *
   * @param other the DataDictionary to copy from
   * @throws DataDictionaryException if validation fails
   */
  public ResponseReadTIC(DataDictionary other) throws DataDictionaryException {
    this();
    this.copy(other);
  }

  /**
   * Constructs a response for TIC frame data with explicit parameters.
   *
   * @param dateTime the response date and time
   * @param errorCode the error code, if any
   * @param errorMessage the error message, if any
   * @param data the TIC frame data
   * @throws DataDictionaryException if validation fails
   */
  public ResponseReadTIC(
      LocalDateTime dateTime, Number errorCode, String errorMessage, TICCoreFrame data)
      throws DataDictionaryException {
    this();
    this.setDateTime(dateTime);
    this.setErrorCode(errorCode);
    this.setErrorMessage(errorMessage);
    this.setData(data);
    this.checkAndUpdate();
  }

  /**
   * Updates optional parameters for the response, ensuring the name is set.
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
   * Returns the TIC frame data associated with this response.
   *
   * @return the TIC frame data
   */
  public TICCoreFrame getData() {
    return (TICCoreFrame) this.data.get(KEY_DATA);
  }

  /**
   * Sets the TIC frame data for this response.
   *
   * @param data the TIC frame data
   * @throws DataDictionaryException if validation fails
   */
  public void setData(TICCoreFrame data) throws DataDictionaryException {
    this.setData((Object) data);
  }

  /**
   * Internal setter for TIC frame data, with conversion and validation.
   *
   * @param data the TIC frame data (Object or TICCoreFrame)
   * @throws DataDictionaryException if validation fails
   */
  protected void setData(Object data) throws DataDictionaryException {
    this.data.put(KEY_DATA, this.kData.convert(data));
  }

  /**
   * Loads key descriptors for response parameters.
   *
   * <p>Initializes the descriptor for TIC frame data and adds it to the response.
   */
  private void loadKeyDescriptors() {
    try {
      this.kData =
          new KeyDescriptorDataDictionary<TICCoreFrame>(KEY_DATA, false, TICCoreFrame.class);
      this.keys.add(this.kData);

      this.addAllKeyDescriptor(this.keys);
    } catch (DataDictionaryException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }
}
