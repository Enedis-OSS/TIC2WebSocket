// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.service.message;

import enedis.lab.types.DataDictionary;
import enedis.lab.types.DataDictionaryException;
import enedis.lab.util.message.Request;
import java.util.Map;

/**
 * Request message for retrieving modem information.
 *
 * <p>This class represents a request to obtain information about available modems. It provides
 * constructors for various initialization scenarios and integrates with the request messaging
 * system.
 *
 * <p>Key features include:
 *
 * <ul>
 *   <li>Encapsulates request for modem information
 *   <li>Supports construction from map, DataDictionary, or default values
 *   <li>Validates and manages request parameters
 * </ul>
 *
 * @author Enedis Smarties team
 * @see Request
 */
public class RequestGetModemsInfo extends Request {
  /** Message name for this request. */
  public static final String NAME = "GetModemsInfo";

  /**
   * Constructs a request for modem information with default values.
   *
   * @throws DataDictionaryException if validation fails
   */
  public RequestGetModemsInfo() throws DataDictionaryException {
    super();
    this.kName.setAcceptedValues(NAME);
    this.checkAndUpdate();
  }

  /**
   * Constructs a request for modem information from a map of values.
   *
   * @param map the map containing request parameters
   * @throws DataDictionaryException if validation fails
   */
  public RequestGetModemsInfo(Map<String, Object> map) throws DataDictionaryException {
    this();
    this.copy(fromMap(map));
  }

  /**
   * Constructs a request for modem information from another DataDictionary instance.
   *
   * @param other the DataDictionary to copy from
   * @throws DataDictionaryException if validation fails
   */
  public RequestGetModemsInfo(DataDictionary other) throws DataDictionaryException {
    this();
    this.copy(other);
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
}
