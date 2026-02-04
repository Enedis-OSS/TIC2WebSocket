// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.service.message;

import java.time.LocalDateTime;
import java.util.List;
import tic.io.modem.ModemDescriptor;
import tic.util.message.ResponseWithData;

/**
 * Response message for modem information.
 *
 * <p>This class represents a response containing the list of modem port descriptors. It provides
 * constructors for various initialization scenarios and integrates with the response messaging
 * system.
 *
 * <p>Key features include:
 *
 * <ul>
 *   <li>Encapsulates response for modem information
 *   <li>Supports construction from map, DataDictionary, or explicit parameter list
 *   <li>Validates and manages response parameters using key descriptors
 * </ul>
 *
 * @author Enedis Smarties team
 * @see Response
 * @see ModemDescriptor
 */
public class ResponseGetModemsInfo extends ResponseWithData<List<ModemDescriptor>> {
  /** Message name for this response. */
  public static final String NAME = "GetModemsInfo";

  private List<ModemDescriptor> modemDescriptors;

  /**
   * Constructs a response for modem information with explicit parameters.
   *
   * @param dateTime the response date and time
   * @param errorCode the error code, if any
   * @param errorMessage the error message, if any
   * @param data the list of modem port descriptors
   */
  public ResponseGetModemsInfo(
      LocalDateTime dateTime, Number errorCode, String errorMessage, List<ModemDescriptor> data) {
    super(NAME, dateTime, errorCode, errorMessage, data);
    this.modemDescriptors = data;
  }

  /**
   * Returns the list of modem port descriptor data associated with this response.
   *
   * @return the list of modem descriptors
   */
  @Override
  public List<ModemDescriptor> getData() {
    return this.modemDescriptors;
  }

  /**
   * Sets the list of modem port descriptor data for this response.
   *
   * @param data the list of modem descriptors
   */
  @Override
  public void setData(List<ModemDescriptor> data) {
    this.modemDescriptors = data;
  }
}