// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.types;

/**
 * Generic exception base class.
 *
 * <p>This class provides a simple mechanism to associate an error code and an informational
 * message with an exception. It can be used as a parent class for more specific exception types
 * that require standardized error reporting and structured diagnostic data.</p>
 *
 * <p>Both the code and info fields are intended to help identify and categorize the source of
 * the problem more precisely in distributed or modular systems.</p>
 *
 * @author Enedis Smarties team
 */
@SuppressWarnings("serial")
public class ExceptionBase extends Exception {

  /** Numeric error code identifying the specific error type. */
  protected int code;

  /** Descriptive information about the error context. */
  protected String info;

  /**
   * Creates a new {@code ExceptionBase} instance with the specified error code and message.
   *
   * @param code the numeric error code associated with this exception
   * @param info the descriptive message providing details about the error
   */
  public ExceptionBase(int code, String info) {
    this.setErrorCode(code);
    this.setErrorInfo(info);
  }

  /**
   * Returns a formatted message including both the descriptive info and the error code.
   *
   * @return the formatted exception message
   */
  @Override
  public String getMessage() {
    return this.info + " (" + this.code + ")";
  }

  /**
   * Returns the error code associated with this exception.
   *
   * @return the numeric error code
   */
  public int getErrorCode() {
    return this.code;
  }

  /**
   * Sets the error code for this exception.
   *
   * @param errorCode the numeric error code to assign
   */
  public void setErrorCode(int errorCode) {
    this.code = errorCode;
  }

  /**
   * Returns the descriptive information message associated with this exception.
   *
   * @return the error information message
   */
  public String getErrorInfo() {
    return this.info;
  }

  /**
   * Sets the descriptive information message for this exception.
   *
   * @param errorInfo the descriptive message to assign
   */
  public void setErrorInfo(String errorInfo) {
    this.info = errorInfo;
  }
}
