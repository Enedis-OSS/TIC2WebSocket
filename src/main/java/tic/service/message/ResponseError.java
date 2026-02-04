// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.service.message;

import tic.util.message.Response;
import java.time.LocalDateTime;

public class ResponseError extends Response {
  /**
   * Constructs a generic response with explicit parameters.
   *
   * @param name the message name
   * @param dateTime the response date and time
   * @param errorCode the error code, if any
   * @param errorMessage the error message, if any
   */
  public ResponseError(String name,
      LocalDateTime dateTime, Number errorCode, String errorMessage) {
    super(name, dateTime, errorCode, errorMessage);
  }
}
