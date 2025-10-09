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
import enedis.lab.util.message.Response;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Response message for TIC subscription.
 *
 * <p>This class represents a response to a TIC subscription request. It provides constructors for
 * various initialization scenarios and integrates with the response messaging system.
 *
 * <p>Key features include:
 *
 * <ul>
 *   <li>Encapsulates response for TIC subscription
 *   <li>Supports construction from map, DataDictionary, or explicit parameter list
 *   <li>Validates and manages response parameters using key descriptors
 * </ul>
 *
 * @author Enedis Smarties team
 * @see Response
 */
public class ResponseSubscribeTIC extends Response {
  /** Message name for this response. */
  public static final String NAME = "SubscribeTIC";

  private List<KeyDescriptor<?>> keys = new ArrayList<KeyDescriptor<?>>();

  /** Constructs a response for TIC subscription with default values. */
  protected ResponseSubscribeTIC() {
    super();
    this.loadKeyDescriptors();
    this.kName.setAcceptedValues(NAME);
  }

  /**
   * Constructs a response for TIC subscription from a map of values.
   *
   * @param map the map containing response parameters
   * @throws DataDictionaryException if validation fails
   */
  public ResponseSubscribeTIC(Map<String, Object> map) throws DataDictionaryException {
    this();
    this.copy(fromMap(map));
  }

  /**
   * Constructs a response for TIC subscription from another DataDictionary instance.
   *
   * @param other the DataDictionary to copy from
   * @throws DataDictionaryException if validation fails
   */
  public ResponseSubscribeTIC(DataDictionary other) throws DataDictionaryException {
    this();
    this.copy(other);
  }

  /**
   * Constructs a response for TIC subscription with explicit parameters.
   *
   * @param dateTime the response date and time
   * @param errorCode the error code, if any
   * @param errorMessage the error message, if any
   * @throws DataDictionaryException if validation fails
   */
  public ResponseSubscribeTIC(LocalDateTime dateTime, Number errorCode, String errorMessage)
      throws DataDictionaryException {
    this();
    this.setDateTime(dateTime);
    this.setErrorCode(errorCode);
    this.setErrorMessage(errorMessage);
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
   * Loads key descriptors for response parameters.
   *
   * <p>Initializes the descriptors and adds them to the response.
   */
  private void loadKeyDescriptors() {
    try {
      this.addAllKeyDescriptor(this.keys);
    } catch (DataDictionaryException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }
}
