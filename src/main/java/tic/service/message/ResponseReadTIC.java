// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.service.message;

import tic.util.message.Response;
import tic.util.message.ResponseWithData;
import tic.core.TICCoreFrame;
import java.time.LocalDateTime;

/**
 * Response message for TIC frame data.
 *
 * <p>This class represents a response containing a TIC frame. It provides constructors for various
 * initialization scenarios and integrates with the response messaging system.
 *
 * <p>Key features include:
 *
 * <ul>
 *   <li>Encapsulates response for TIC frame data
 *   <li>Supports construction from map, DataDictionary, or explicit parameter list
 *   <li>Validates and manages response parameters using key descriptors
 * </ul>
 *
 * @author Enedis Smarties team
 * @see Response
 * @see TICCoreFrame
 */
public class ResponseReadTIC extends ResponseWithData<TICCoreFrame> {
  /** Message name for this response. */
  public static final String NAME = "ReadTIC";
  private TICCoreFrame ticCoreFrame;

  /**
   * Constructs a response for TIC frame data with explicit parameters.
   *
   * @param dateTime the response date and time
   * @param errorCode the error code, if any
   * @param errorMessage the error message, if any
   * @param data the TIC frame data
   */
  public ResponseReadTIC(
      LocalDateTime dateTime, Number errorCode, String errorMessage, TICCoreFrame data) {
    super(NAME, dateTime, errorCode, errorMessage, data);
    this.ticCoreFrame = data;
}

  @Override
  public TICCoreFrame getData() {
    return this.ticCoreFrame;
  }

  @Override
  public void setData(TICCoreFrame data) {
    this.ticCoreFrame = data;
  }
}