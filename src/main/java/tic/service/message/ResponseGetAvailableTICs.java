// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.service.message;

import java.time.LocalDateTime;
import java.util.List;
import tic.core.TICIdentifier;
import tic.util.message.Response;
import tic.util.message.ResponseWithData;

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
public class ResponseGetAvailableTICs extends ResponseWithData<List<TICIdentifier>> {
  /** Message name for this response. */
  public static final String NAME = "GetAvailableTICs";

  private List<TICIdentifier> ticIdentifiers;

  /**
   * Constructs a response for available TIC identifiers with explicit parameters.
   *
   * @param dateTime the response date and time
   * @param errorCode the error code, if any
   * @param errorMessage the error message, if any
   * @param data the list of available TIC identifiers
   */
  public ResponseGetAvailableTICs(
      LocalDateTime dateTime, Number errorCode, String errorMessage, List<TICIdentifier> data) {
    super(NAME, dateTime, errorCode, errorMessage, data);
    this.ticIdentifiers = data;
  }

  /**
   * Returns the list of available TIC identifier data associated with this response.
   *
   * @return the list of TIC identifiers
   */
  @Override
  public List<TICIdentifier> getData() {
    return this.ticIdentifiers;
  }

  /**
   * Sets the list of available TIC identifier data for this response.
   *
   * @param data the list of TIC identifiers
   */
  @Override
  public void setData(List<TICIdentifier> data) {
    this.ticIdentifiers = data;
  }
}
