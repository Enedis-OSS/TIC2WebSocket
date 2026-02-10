// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.service.message;

import tic.util.message.Request;
import tic.core.TICIdentifier;
import java.util.List;

/**
 * Request message for unsubscribing from TIC data in the TIC2WebSocket protocol.
 *
 * <p>This class represents a request to unsubscribe from TIC data for one or more identifiers. It
 * provides constructors for various initialization scenarios and integrates with the request
 * messaging system.
 *
 * <p>Key features include:
 *
 * <ul>
 *   <li>Encapsulates request for TIC data unsubscription
 *   <li>Supports construction from map, DataDictionary, or explicit identifier list
 *   <li>Validates and manages request parameters using key descriptors
 * </ul>
 *
 * @author Enedis Smarties team
 * @see Request
 * @see TICIdentifier
 */
public class RequestUnsubscribeTIC extends Request {
  /** Message name for this request. */
  public static final String NAME = "UnsubscribeTIC";

  private List<TICIdentifier> data;

  /**
   * Constructs a request for unsubscribing from TIC data with a specific list of identifiers.
   *
   * @param data the list of TIC identifiers
   */
  public RequestUnsubscribeTIC(List<TICIdentifier> data) {
    super(NAME);
    this.setData(data);
  }

  /**
   * Returns the list of TIC identifier data associated with this request.
   *
   * @return the list of TIC identifiers
   */
  public List<TICIdentifier> getData() {
    return this.data;
  }

  /**
   * Sets the list of TIC identifier data for this request.
   *
   * @param data the list of TIC identifiers
   */
  public void setData(List<TICIdentifier> data) {
    this.data = data;
  }
}
