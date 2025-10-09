// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.tic.core;

import enedis.lab.types.ExceptionBase;

/**
 * Exception class for representing core errors and failures.
 *
 * <p>This class provides mechanisms for constructing exceptions with error codes and messages,
 * supporting structured error handling and reporting.
 *
 * @author Enedis Smarties team
 */
public class TICCoreException extends ExceptionBase {
  private static final long serialVersionUID = -3285641164559292710L;

  public TICCoreException(int code, String info) {
    super(code, info);
  }
}
