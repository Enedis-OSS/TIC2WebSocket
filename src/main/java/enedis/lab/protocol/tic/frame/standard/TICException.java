// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.protocol.tic.frame.standard;

import enedis.lab.protocol.tic.frame.TICError;

/**
 * Exception class for errors encountered during TIC frame processing.
 *
 * <p>This exception is thrown when a TIC frame cannot be parsed, validated, or processed correctly.
 * It encapsulates a {@link TICError} code to provide additional context about the error type.
 *
 * <p>Key features:
 *
 * <ul>
 *   <li>Associates a {@link TICError} with each exception instance
 *   <li>Supports standard exception message and error code
 *   <li>Provides a reset method to restore default error state
 * </ul>
 *
 * @author Enedis Smarties team
 * @see TICError
 * @see Exception
 */
public class TICException extends Exception {
  /** Unique identifier used for serialization. */
  private static final long serialVersionUID = -2780151361870269473L;

  /** The TIC error code associated with this exception. */
  private TICError error;

  /** Constructs a new TICException with default error code. */
  public TICException() {
    super();
    this.reset();
  }

  /**
   * Constructs a new TICException with the specified error message and default error code.
   *
   * @param message the detail message
   */
  public TICException(String message) {
    super(message);
    this.error = TICError.TIC_READER_DEFAULT_ERROR;
  }

  /**
   * Constructs a new TICException with the specified error message and error code.
   *
   * @param message the detail message
   * @param readerError the TIC error code
   */
  public TICException(String message, TICError readerError) {
    super(message);
    this.error = readerError;
  }

  /** Resets the error code to the default value. */
  public void reset() {
    this.error = TICError.TIC_READER_DEFAULT_ERROR;
  }

  /**
   * Returns the TIC error code associated with this exception.
   *
   * @return the TIC error code
   */
  public TICError getError() {
    return this.error;
  }
}
