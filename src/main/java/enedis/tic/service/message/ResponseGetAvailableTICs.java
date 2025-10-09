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
import enedis.lab.types.datadictionary.KeyDescriptorList;
import enedis.lab.util.message.Response;
import enedis.tic.core.TICIdentifier;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Response message for available TIC identifiers.
 *
 * <p>This class represents a response containing the list of available TIC identifiers. It provides
 * constructors for various initialization scenarios and integrates with the response messaging
 * system.
 *
 * <p>Key features include:
 *
 * <ul>
 *   <li>Encapsulates response for available TIC identifiers
 *   <li>Supports construction from map, DataDictionary, or explicit parameter list
 *   <li>Validates and manages response parameters using key descriptors
 * </ul>
 *
 * @author Enedis Smarties team
 * @see Response
 * @see TICIdentifier
 */
public class ResponseGetAvailableTICs extends Response {
  /** Key for TIC identifier data in the response. */
  protected static final String KEY_DATA = "data";

  /** Message name for this response. */
  public static final String NAME = "GetAvailableTICs";

  private List<KeyDescriptor<?>> keys = new ArrayList<KeyDescriptor<?>>();

  /** Key descriptor for TIC identifier data. */
  protected KeyDescriptorList<TICIdentifier> kData;

  /** Constructs a response for available TIC identifiers with default values. */
  protected ResponseGetAvailableTICs() {
    super();
    this.loadKeyDescriptors();
    this.kName.setAcceptedValues(NAME);
  }

  /**
   * Constructs a response for available TIC identifiers from a map of values.
   *
   * @param map the map containing response parameters
   * @throws DataDictionaryException if validation fails
   */
  public ResponseGetAvailableTICs(Map<String, Object> map) throws DataDictionaryException {
    this();
    this.copy(fromMap(map));
  }

  /**
   * Constructs a response for available TIC identifiers from another DataDictionary instance.
   *
   * @param other the DataDictionary to copy from
   * @throws DataDictionaryException if validation fails
   */
  public ResponseGetAvailableTICs(DataDictionary other) throws DataDictionaryException {
    this();
    this.copy(other);
  }

  /**
   * Constructs a response for available TIC identifiers with explicit parameters.
   *
   * @param dateTime the response date and time
   * @param errorCode the error code, if any
   * @param errorMessage the error message, if any
   * @param data the list of available TIC identifiers
   * @throws DataDictionaryException if validation fails
   */
  public ResponseGetAvailableTICs(
      LocalDateTime dateTime, Number errorCode, String errorMessage, List<TICIdentifier> data)
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
   * Returns the list of TIC identifier data associated with this response.
   *
   * @return the list of TIC identifiers
   */
  @SuppressWarnings("unchecked")
  public List<TICIdentifier> getData() {
    return (List<TICIdentifier>) this.data.get(KEY_DATA);
  }

  /**
   * Sets the list of TIC identifier data for this response.
   *
   * @param data the list of TIC identifiers
   * @throws DataDictionaryException if validation fails
   */
  public void setData(List<TICIdentifier> data) throws DataDictionaryException {
    this.setData((Object) data);
  }

  /**
   * Internal setter for TIC identifier data, with conversion and validation.
   *
   * @param data the TIC identifier list (Object or List<TICIdentifier>)
   * @throws DataDictionaryException if validation fails
   */
  protected void setData(Object data) throws DataDictionaryException {
    this.data.put(KEY_DATA, this.kData.convert(data));
  }

  /**
   * Loads key descriptors for response parameters.
   *
   * <p>Initializes the descriptor for TIC identifier data and adds it to the response.
   */
  private void loadKeyDescriptors() {
    try {
      this.kData = new KeyDescriptorList<TICIdentifier>(KEY_DATA, false, TICIdentifier.class);
      this.keys.add(this.kData);

      this.addAllKeyDescriptor(this.keys);
    } catch (DataDictionaryException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }
}
