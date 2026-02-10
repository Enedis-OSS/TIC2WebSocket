// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.core;

/**
 * Exception class for representing core errors and failures.
 *
 * <p>This class provides mechanisms for constructing exceptions with error codes and messages,
 * supporting structured error handling and reporting.
 *
 * @author Enedis Smarties team
 */
public class TICCoreException extends Exception {
  private static final long serialVersionUID = -3285641164559292710L;

  private final int errorCode;
  private final String errorInfo;

  public TICCoreException(int errorCode, String errorInfo) {
    this.errorCode = errorCode;
    this.errorInfo = errorInfo;
  }

  @Override
  public String getMessage() {
    return this.errorInfo + " (" + this.errorCode + ")";
  }

  public int getErrorCode() {
    return this.errorCode;
  }

  public String getErrorInfo() {
    return this.errorInfo;
  }
}
