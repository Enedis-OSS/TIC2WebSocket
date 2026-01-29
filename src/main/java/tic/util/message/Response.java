// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.util.message;

import java.time.LocalDateTime;

/**
 * Abstract base class for response messages.
 *
 * <p>This class represents response messages. It provides support for response-specific fields such
 * as date/time, error code, and error message, and enforces the accepted message type. Subclasses
 * can define additional response data and behavior.
 *
 * <p>Common use cases include:
 *
 * <ul>
 *   <li>Representing server responses to client requests
 *   <li>Handling error reporting and status information
 *   <li>Extending for custom response types
 * </ul>
 *
 * @author Enedis Smarties team
 * @see Message
 */
public abstract class Response extends Message {
  private LocalDateTime dateTime;
  private Number errorCode;
  private String errorMessage;

  /**
   * Constructor setting parameters to specific values
   *
   * @param name
   * @param dateTime
   * @param errorCode
   * @param errorMessage
   */
  public Response(
      String name, LocalDateTime dateTime, Number errorCode, String errorMessage) {
    super(MessageType.RESPONSE, name);

    this.setDateTime(dateTime);
    this.setErrorCode(errorCode);
    this.setErrorMessage(errorMessage);
  }

  /**
   * Get date time
   *
   * @return the date time
   */
  public LocalDateTime getDateTime() {
    return this.dateTime;
  }

  /**
   * Get error code
   *
   * @return the error code
   */
  public Number getErrorCode() {
    return this.errorCode;
  }

  /**
   * Get error message
   *
   * @return the error message
   */
  public String getErrorMessage() {
    return this.errorMessage;
  }

  /**
   * Set date time
   *
   * @param dateTime
   */
  public void setDateTime(LocalDateTime dateTime) {
    this.dateTime = dateTime;
  }

  /**
   * Set error code
   *
   * @param errorCode
   */
  public void setErrorCode(Number errorCode) {
    this.errorCode = errorCode;
  }

  /**
   * Set error message
   *
   * @param errorMessage
   */
  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }
}
