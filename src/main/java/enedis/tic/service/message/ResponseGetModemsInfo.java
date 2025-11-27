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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import tic.io.modem.ModemDescriptor;

/**
 * Response message for modem information.
 *
 * <p>This class represents a response containing the list of modem port descriptors. It provides
 * constructors for various initialization scenarios and integrates with the response messaging
 * system.
 *
 * <p>Key features include:
 *
 * <ul>
 *   <li>Encapsulates response for modem information
 *   <li>Supports construction from map, DataDictionary, or explicit parameter list
 *   <li>Validates and manages response parameters using key descriptors
 * </ul>
 *
 * @author Enedis Smarties team
 * @see Response
 * @see ModemDescriptor
 */
public class ResponseGetModemsInfo extends Response {
  /** Key for modem port descriptor data in the response. */
  protected static final String KEY_DATA = "data";

  /** Message name for this response. */
  public static final String NAME = "GetModemsInfo";

  private List<KeyDescriptor<?>> keys = new ArrayList<KeyDescriptor<?>>();

  /** Key descriptor for modem port descriptor data. */
  protected KeyDescriptorList<ModemDescriptor> kData;

  /** Constructs a response for modem information with default values. */
  protected ResponseGetModemsInfo() {
    super();
    this.loadKeyDescriptors();
    this.kName.setAcceptedValues(NAME);
  }

  /**
   * Constructs a response for modem information from a map of values.
   *
   * @param map the map containing response parameters
   * @throws DataDictionaryException if validation fails
   */
  public ResponseGetModemsInfo(Map<String, Object> map) throws DataDictionaryException {
    this();
    this.copy(fromMap(map));
  }

  /**
   * Constructs a response for modem information from another DataDictionary instance.
   *
   * @param other the DataDictionary to copy from
   * @throws DataDictionaryException if validation fails
   */
  public ResponseGetModemsInfo(DataDictionary other) throws DataDictionaryException {
    this();
    this.copy(other);
  }

  /**
   * Constructs a response for modem information with explicit parameters.
   *
   * @param dateTime the response date and time
   * @param errorCode the error code, if any
   * @param errorMessage the error message, if any
   * @param data the list of modem port descriptors
   * @throws DataDictionaryException if validation fails
   */
  public ResponseGetModemsInfo(
      LocalDateTime dateTime, Number errorCode, String errorMessage, List<ModemDescriptor> data)
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
   * Returns the list of modem port descriptor data associated with this response.
   *
   * @return the list of modem port descriptors
   */
  @SuppressWarnings("unchecked")
  public List<ModemDescriptor> getData() {
    return (List<ModemDescriptor>) this.data.get(KEY_DATA);
  }

  /**
   * Sets the list of modem port descriptor data for this response.
   *
   * @param data the list of modem port descriptors
   * @throws DataDictionaryException if validation fails
   */
  public void setData(List<ModemDescriptor> data) throws DataDictionaryException {
    this.setData((Object) data);
  }

  /**
   * Internal setter for modem port descriptor data, with conversion and validation.
   *
   * @param data the modem descriptor list (Object or List<ModemDescriptor>)
   * @throws DataDictionaryException if validation fails
   */
  protected void setData(Object data) throws DataDictionaryException {
    this.data.put(KEY_DATA, this.kData.convert(data));
  }

  /**
   * Loads key descriptors for response parameters.
   *
   * <p>Initializes the descriptor for modem port descriptor data and adds it to the response.
   */
  private void loadKeyDescriptors() {
    try {
      this.kData = new KeyDescriptorList<ModemDescriptor>(KEY_DATA, false, ModemDescriptor.class);
      this.keys.add(this.kData);

      this.addAllKeyDescriptor(this.keys);
    } catch (DataDictionaryException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }
}
