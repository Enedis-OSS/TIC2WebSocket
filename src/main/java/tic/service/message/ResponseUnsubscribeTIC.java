// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.service.message;

import tic.util.message.Response;
import java.time.LocalDateTime;

/**
 * Response message for TIC unsubscription.
 *
 * <p>This class represents a response to a TIC unsubscription request. It provides constructors for
 * various initialization scenarios and integrates with the response messaging system.
 *
 * <p>Key features include:
 *
 * <ul>
 *   <li>Encapsulates response for TIC unsubscription
 *   <li>Supports construction from map, DataDictionary, or explicit parameter list
 *   <li>Validates and manages response parameters using key descriptors
 * </ul>
 *
 * @author Enedis Smarties team
 * @see Response
 */
public class ResponseUnsubscribeTIC extends Response {
  /** Message name for this response. */
  public static final String NAME = "UnsubscribeTIC";
  
  /**
   * Constructs a response for TIC unsubscription with explicit parameters.
   *
   * @param dateTime the response date and time
   * @param errorCode the error code, if any
   * @param errorMessage the error message, if any
   */
  public ResponseUnsubscribeTIC(LocalDateTime dateTime, Number errorCode, String errorMessage) {
    super(NAME, dateTime, errorCode, errorMessage);
  }
}
