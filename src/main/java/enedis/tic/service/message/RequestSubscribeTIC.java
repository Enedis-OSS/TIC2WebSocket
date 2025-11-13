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
import enedis.lab.util.message.Request;
import enedis.tic.core.TICIdentifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Request message for subscribing to TIC data.
 *
 * <p>This class represents a request to subscribe to TIC data for one or more identifiers. It
 * provides constructors for various initialization scenarios and integrates with the request
 * messaging system.
 *
 * <p>Key features include:
 *
 * <ul>
 *   <li>Encapsulates request for TIC data subscription
 *   <li>Supports construction from map, DataDictionary, or explicit identifier list
 *   <li>Validates and manages request parameters using key descriptors
 * </ul>
 *
 * @author Enedis Smarties team
 * @see Request
 * @see TICIdentifier
 */
public class RequestSubscribeTIC extends Request {
  /** Key for TIC identifier data in the request. */
  protected static final String KEY_DATA = "data";

  /** Message name for this request. */
  public static final String NAME = "SubscribeTIC";

  private List<KeyDescriptor<?>> keys = new ArrayList<KeyDescriptor<?>>();

  /** Key descriptor for TIC identifier data. */
  protected KeyDescriptorList<TICIdentifier> kData;

  /** Constructs a request for subscribing to TIC data with default values. */
  protected RequestSubscribeTIC() {
    super();
    this.loadKeyDescriptors();
    this.kName.setAcceptedValues(NAME);
  }

  /**
   * Constructs a request for subscribing to TIC data from a map of values.
   *
   * @param map the map containing request parameters
   * @throws DataDictionaryException if validation fails
   */
  public RequestSubscribeTIC(Map<String, Object> map) throws DataDictionaryException {
    this();
    this.copy(fromMap(map));
  }

  /**
   * Constructs a request for subscribing to TIC data from another DataDictionary instance.
   *
   * @param other the DataDictionary to copy from
   * @throws DataDictionaryException if validation fails
   */
  public RequestSubscribeTIC(DataDictionary other) throws DataDictionaryException {
    this();
    this.copy(other);
  }

  /**
   * Constructs a request for subscribing to TIC data with a specific list of identifiers.
   *
   * @param data the list of TIC identifiers
   * @throws DataDictionaryException if validation fails
   */
  public RequestSubscribeTIC(List<TICIdentifier> data) throws DataDictionaryException {
    this();
    this.setData(data);
    this.checkAndUpdate();
  }

  /**
   * Updates optional parameters for the request, ensuring the name is set.
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
   * Returns the list of TIC identifier data associated with this request.
   *
   * @return the list of TIC identifiers
   */
  @SuppressWarnings("unchecked")
  public List<TICIdentifier> getData() {
    return (List<TICIdentifier>) this.data.get(KEY_DATA);
  }

  /**
   * Sets the list of TIC identifier data for this request.
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
   * Loads key descriptors for request parameters.
   *
   * <p>Initializes the descriptor for TIC identifier data and adds it to the request.
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
