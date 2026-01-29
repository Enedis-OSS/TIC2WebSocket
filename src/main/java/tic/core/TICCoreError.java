// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.core;

import java.util.Objects;

import tic.frame.TICFrame;

/**
 * Class representing a core error with identifier, code, message, and optional data.
 *
 * <p>This class provides mechanisms for constructing, accessing, and managing error information
 * including error codes, messages, and associated data. It is designed for general-purpose error
 * handling.
 *
 * <p>Common use cases include:
 *
 * <ul>
 *   <li>Representing errors with structured data
 *   <li>Managing error codes and messages
 *   <li>Associating additional data with errors
 * </ul>
 *
 * @author Enedis Smarties team
 */
public class TICCoreError {

  private final TICIdentifier identifier;
  private final Number errorCode;
  private final String errorMessage;
  private final TICFrame frame;

  /**
   * Constructor setting parameters to specific values
   *
   * @param identifier
   * @param errorCode
   * @param errorMessage
   */
  public TICCoreError(TICIdentifier identifier, Number errorCode, String errorMessage) {
    this(identifier, errorCode, errorMessage, null);
  }

  /**
   * Constructor setting parameters to specific values
   *
   * @param identifier
   * @param errorCode
   * @param errorMessage
   * @param frame
   */
  public TICCoreError(
      TICIdentifier identifier, Number errorCode, String errorMessage, TICFrame frame) {
    this.identifier = Objects.requireNonNull(identifier, "identifier must not be null");
    this.errorCode = Objects.requireNonNull(errorCode, "errorCode must not be null");
    this.errorMessage = Objects.requireNonNull(errorMessage, "errorMessage must not be null");
    this.frame = frame;
  }

  /**
   * Get identifier
   *
   * @return the identifier
   */
  public TICIdentifier getIdentifier() {
    return this.identifier;
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
   * Get frame
   *
   * @return the frame
   */
  public TICFrame getFrame() {
    return this.frame;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("TICCoreError {\n");
    sb.append("  identifier: ").append(this.identifier).append(",\n");
    sb.append("  errorCode: ").append(this.errorCode).append(",\n");
    sb.append("  errorMessage: ").append(this.errorMessage).append(",\n");
    sb.append("  frame: ").append(this.frame).append("\n");
    sb.append("}");
    return sb.toString();
  }
}
