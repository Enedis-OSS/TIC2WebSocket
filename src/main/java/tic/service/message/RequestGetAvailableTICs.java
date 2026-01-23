// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.service.message;

import tic.util.message.Request;

/**
 * Request message for retrieving available TICs.
 *
 * <p>This class represents a request to obtain the list of available TICs. It provides constructors
 * for various initialization scenarios and integrates with the request messaging system.
 *
 * <p>Key features include:
 *
 * <ul>
 *   <li>Encapsulates request for available TICs
 *   <li>Supports construction from map, DataDictionary, or default values
 *   <li>Validates and manages request parameters
 * </ul>
 *
 * @author Enedis Smarties team
 * @see Request
 */
public class RequestGetAvailableTICs extends Request {
  /** Message name for this request. */
  public static final String NAME = "GetAvailableTICs";

  /** Constructs a request for available TICs with default values. */
  public RequestGetAvailableTICs() {
    super(NAME);
  }
}
