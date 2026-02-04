// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.service.message;

import tic.util.message.Request;

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
 *   <li>Validates and manages request parameters
 * </ul>
 *
 * @author Enedis Smarties team
 * @see Request
 */
public class RequestGetModemsInfo extends Request {
  /** Message name for this request. */
  public static final String NAME = "GetModemsInfo";

  /** Constructs a request for modem information with default values. */
  public RequestGetModemsInfo() {
    super(NAME);
  }
}
