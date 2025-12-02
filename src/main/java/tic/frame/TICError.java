// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.frame;

import jssc.SerialPortException;

/**
 * Enumeration of error codes for TIC frame and serial port operations.
 *
 * <p>This enum defines error codes for serial port issues and TIC frame processing errors. It
 * provides a mapping from {@link SerialPortException} types to TIC error codes for unified error
 * handling.
 *
 * <p>Key features:
 *
 * <ul>
 *   <li>Defines error codes for serial port and TIC frame errors
 *   <li>Provides a method to map {@link SerialPortException} to TICError
 *   <li>Each error code has an associated integer value
 * </ul>
 *
 * @author Enedis Smarties team
 * @see SerialPortException
 */
public enum TICError {
  /** No error. */
  NO_ERROR(0),
  /** Serial port not found. */
  SERIAL_PORT_NOT_FOUND(17),
  /** Incorrect serial port. */
  SERIAL_PORT_INCORRECT_SERIAL_PORT(18),
  /** Null not permitted for serial port. */
  SERIAL_PORT_NULL_NOT_PERMITTED(19),
  /** Serial port already opened. */
  SERIAL_PORT_ALREADY_OPENED(20),
  /** Serial port is busy. */
  SERIAL_PORT_SERIAL_PORT_BUSY(21),
  /** Serial port configuration failure. */
  SERIAL_PORT_CONFIGURATION_FAILURE(22),
  /** Default error for TIC reader. */
  TIC_READER_DEFAULT_ERROR(23),
  /** TIC reader label name not found. */
  TIC_READER_LABEL_NAME_NOT_FOUND(24),
  /** TIC reader frame decode failed. */
  TIC_READER_FRAME_DECODE_FAILED(25),
  /** TIC reader read frame timeout. */
  TIC_READER_READ_FRAME_TIMEOUT(26),
  /** TIC reader read frame with checksum invalid. */
  TIC_READER_READ_FRAME_CHECKSUM_INVALID(27),
  ;

  /** Integer value associated with the error code. */
  private int value;

  /**
   * Constructs a TICError with the specified integer value.
   *
   * @param value the integer value for the error code
   */
  private TICError(int value) {
    this.value = value;
  }

  /**
   * Returns the integer value associated with this error code.
   *
   * @return the error code value
   */
  public int getValue() {
    return this.value;
  }

  /**
   * Maps a {@link SerialPortException} to the corresponding {@link TICError} code.
   *
   * @param serialPortException the serial port exception to map
   * @return the corresponding {@link TICError}, or null if not recognized
   */
  public static TICError getSerialPortError(SerialPortException serialPortException) {
    if (serialPortException.getExceptionType().equals(SerialPortException.TYPE_PORT_BUSY)) {
      return TICError.SERIAL_PORT_SERIAL_PORT_BUSY;
    }
    if (serialPortException
        .getExceptionType()
        .equals(SerialPortException.TYPE_INCORRECT_SERIAL_PORT)) {
      return TICError.SERIAL_PORT_INCORRECT_SERIAL_PORT;
    }
    if (serialPortException
        .getExceptionType()
        .equals(SerialPortException.TYPE_NULL_NOT_PERMITTED)) {
      return TICError.SERIAL_PORT_NULL_NOT_PERMITTED;
    }
    if (serialPortException.getExceptionType().equals(SerialPortException.TYPE_PORT_NOT_FOUND)) {
      return TICError.SERIAL_PORT_NOT_FOUND;
    }
    if (serialPortException
        .getExceptionType()
        .equals(SerialPortException.TYPE_PORT_ALREADY_OPENED)) {
      return TICError.SERIAL_PORT_ALREADY_OPENED;
    }
    return null;
  }
}
