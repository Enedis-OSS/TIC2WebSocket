// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.types;

/**
 * Exception type used to represent errors occurring when manipulating or interpreting
 * data dictionary elements.
 *
 * <p>This exception typically indicates issues such as invalid data mappings, corrupted
 * metadata, or unexpected dictionary structures encountered during runtime.</p>
 *
 * <p>It extends {@link java.lang.Exception}, allowing both checked exception handling and
 * detailed error propagation with message and cause information.</p>
 *
 * @author Enedis Smarties team
 */
public class DataDictionaryException extends Exception {

  private static final long serialVersionUID = -7967428428453584771L;

  /**
   * Creates a new {@code DataDictionaryException} with no detail message or cause.
   */
  public DataDictionaryException() {
    super();
  }

  /**
   * Creates a new {@code DataDictionaryException} with the specified detail message.
   *
   * @param message the detail message describing the error context
   */
  public DataDictionaryException(String message) {
    super(message);
  }

  /**
   * Creates a new {@code DataDictionaryException} with the specified cause.
   *
   * @param cause the underlying cause of the exception
   */
  public DataDictionaryException(Throwable cause) {
    super(cause);
  }

  /**
   * Creates a new {@code DataDictionaryException} with the specified detail message and cause.
   *
   * @param message the detail message describing the error context
   * @param cause the underlying cause of the exception
   */
  public DataDictionaryException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Creates a new {@code DataDictionaryException} with full control over suppression
   * and stack trace writability.
   *
   * @param message the detail message describing the error context
   * @param cause the underlying cause of the exception
   * @param enableSuppression whether suppression is enabled or disabled
   * @param writableStackTrace whether the stack trace should be writable
   */
  public DataDictionaryException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
