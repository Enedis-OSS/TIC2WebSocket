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
import enedis.lab.util.message.Request;
import enedis.tic.core.TICIdentifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Request message for reading TIC data in the TIC2WebSocket protocol.
 *
 * <p>This class represents a request to read TIC data for a specific identifier. It provides
 * constructors for various initialization scenarios and integrates with the request messaging
 * system.
 *
 * <p>Key features include:
 *
 * <ul>
 *   <li>Encapsulates request for reading TIC data
 *   <li>Supports construction from map, DataDictionary, or explicit identifier
 *   <li>Validates and manages request parameters using key descriptors
 * </ul>
 *
 * @author Enedis Smarties team
 * @see Request
 * @see TICIdentifier
 */
public class RequestReadTIC extends Request {
  /** Key for TIC identifier data in the request. */
  protected static final String KEY_DATA = "data";

  /** Message name for this request. */
  public static final String NAME = "ReadTIC";

  private List<KeyDescriptor<?>> keys = new ArrayList<KeyDescriptor<?>>();

  /** Key descriptor for TIC identifier data. */
  protected KeyDescriptorDataDictionary<TICIdentifier> kData;

  /** Constructs a request for reading TIC data with default values. */
  protected RequestReadTIC() {
    super();
    this.loadKeyDescriptors();
    this.kName.setAcceptedValues(NAME);
  }

  /**
   * Constructs a request for reading TIC data from a map of values.
   *
   * @param map the map containing request parameters
   * @throws DataDictionaryException if validation fails
   */
  public RequestReadTIC(Map<String, Object> map) throws DataDictionaryException {
    this();
    this.copy(fromMap(map));
  }

  /**
   * Constructs a request for reading TIC data from another DataDictionary instance.
   *
   * @param other the DataDictionary to copy from
   * @throws DataDictionaryException if validation fails
   */
  public RequestReadTIC(DataDictionary other) throws DataDictionaryException {
    this();
    this.copy(other);
  }

  /**
   * Constructs a request for reading TIC data with a specific identifier.
   *
   * @param data the TIC identifier
   * @throws DataDictionaryException if validation fails
   */
  public RequestReadTIC(TICIdentifier data) throws DataDictionaryException {
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
   * Returns the TIC identifier data associated with this request.
   *
   * @return the TIC identifier
   */
  public TICIdentifier getData() {
    return (TICIdentifier) this.data.get(KEY_DATA);
  }

  /**
   * Sets the TIC identifier data for this request.
   *
   * @param data the TIC identifier
   * @throws DataDictionaryException if validation fails
   */
  public void setData(TICIdentifier data) throws DataDictionaryException {
    this.setData((Object) data);
  }

  /**
   * Internal setter for TIC identifier data, with conversion and validation.
   *
   * @param data the TIC identifier (Object or TICIdentifier)
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
      this.kData =
          new KeyDescriptorDataDictionary<TICIdentifier>(KEY_DATA, true, TICIdentifier.class);
      this.keys.add(this.kData);

      this.addAllKeyDescriptor(this.keys);
    } catch (DataDictionaryException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }
}
