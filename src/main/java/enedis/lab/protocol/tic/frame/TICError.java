// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.protocol.tic.frame;

import jssc.SerialPortException;

/** TICClient errors definition */
public enum TICError {
  /** No error */
  NO_ERROR(0),
  /** Serial port not found */
  SERIAL_PORT_NOT_FOUND(17),
  /** Serial port not found */
  SERIAL_PORT_INCORRECT_SERIAL_PORT(18),
  /** Serial port not found */
  SERIAL_PORT_NULL_NOT_PERMITTED(19),
  /** Serial port not found */
  SERIAL_PORT_ALREADY_OPENED(20),
  /** Serial port not found */
  SERIAL_PORT_SERIAL_PORT_BUSY(21),
  /** Serial port not found */
  SERIAL_PORT_CONFIGURATION_FAILURE(22),
  /** TIC reader default error */
  TIC_READER_DEFAULT_ERROR(23),
  /** TIC reader label name not found */
  TIC_READER_LABEL_NAME_NOT_FOUND(24),
  /** TIC reader frame decode failed */
  TIC_READER_FRAME_DECODE_FAILED(25),
  /** TIC reader read frame timeout */
  TIC_READER_READ_FRAME_TIMEOUT(26),
  /** TIC reader read frame with checksum invalid */
  TIC_READER_READ_FRAME_CHECKSUM_INVALID(27),
  ;

  private int value;

  private TICError(int value) {
    this.value = value;
  }

  /**
   * Get error value
   *
   * @return the value
   */
  public int getValue() {
    return this.value;
  }

  /**
   * Get serial port error
   *
   * @param serialPortException
   * @return TICError
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
