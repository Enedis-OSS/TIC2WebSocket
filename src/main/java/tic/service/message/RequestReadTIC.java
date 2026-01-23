// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.service.message;

import tic.core.TICIdentifier;
import tic.util.message.Request;

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
  /** Message name for this request. */
  public static final String NAME = "ReadTIC";

  private TICIdentifier data;

  /**
   * Constructs a request for reading TIC data with a specific identifier.
   *
   * @param data the TIC identifier
   */
  public RequestReadTIC(TICIdentifier data) {
    super(NAME);
    this.setData(data);
  }

  /**
   * Returns the TIC identifier data associated with this request.
   *
   * @return the TIC identifier
   */
  public TICIdentifier getData() {
    return this.data;
  }

  /**
   * Sets the TIC identifier data for this request.
   *
   * @param data the TIC identifier
   */
  public void setData(TICIdentifier data) {
    this.data = data;
  }
}
