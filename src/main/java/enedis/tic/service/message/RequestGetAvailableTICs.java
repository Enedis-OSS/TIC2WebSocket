// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.tic.service.message;

import enedis.lab.types.DataDictionary;
import enedis.lab.types.DataDictionaryException;
import enedis.lab.util.message.Request;
import java.util.Map;

/**
 * RequestGetAvailableTICs class
 *
 * <p>Generated
 */
public class RequestGetAvailableTICs extends Request {
  /** Message name */
  public static final String NAME = "GetAvailableTICs";

  /**
   * Default constructor
   *
   * @throws DataDictionaryException
   */
  public RequestGetAvailableTICs() throws DataDictionaryException {
    super();

    this.kName.setAcceptedValues(NAME);

    this.checkAndUpdate();
  }

  /**
   * Constructor using map
   *
   * @param map
   * @throws DataDictionaryException
   */
  public RequestGetAvailableTICs(Map<String, Object> map) throws DataDictionaryException {
    this();
    this.copy(fromMap(map));
  }

  /**
   * Constructor using datadictionary
   *
   * @param other
   * @throws DataDictionaryException
   */
  public RequestGetAvailableTICs(DataDictionary other) throws DataDictionaryException {
    this();
    this.copy(other);
  }

  @Override
  protected void updateOptionalParameters() throws DataDictionaryException {
    if (!this.exists(KEY_NAME)) {
      this.setName(NAME);
    }
    super.updateOptionalParameters();
  }
}
