// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.io.channels.serialport;

/**
 * Enumeration of error codes for serial port channel operations.
 *
 * <p>This enum defines specific error codes that can occur during serial port communication
 * operations. Each error code is associated with a unique numeric identifier that can be used for
 * error reporting, logging, and debugging purposes.
 *
 * <p>The error codes are designed to be used with an offset mechanism, allowing different
 * components or modules to have their own error code ranges while maintaining uniqueness across the
 * entire system.
 *
 * @author Enedis Smarties team
 */
public enum ChannelSerialPortErrorCode {
  /**
   * Error code indicating that the specified serial port was not found on the system. This
   * typically occurs when the port identifier or name does not correspond to any available serial
   * port.
   */
  PORT_NOT_FOUND(1),

  /**
   * Error code indicating that a previously opened serial port is no longer available. This can
   * happen when the port is disconnected or becomes unavailable after being opened.
   */
  PORT_OPENED_NOT_FOUND(2),

  /**
   * Error code indicating that the serial port name has changed during operation. This typically
   * occurs when the port is reconnected and gets assigned a different name.
   */
  PORT_NAME_HAS_CHANGED(3),

  /**
   * Error code indicating that the serial port is currently busy and cannot be accessed. This
   * typically occurs when another application or process is already using the port.
   */
  PORT_BUSY(4),

  /**
   * Error code indicating that a read operation on the serial port has failed. This can be due to
   * various reasons such as hardware issues, communication errors, or port configuration problems.
   */
  READ_FAILED(5),

  /**
   * Error code indicating that a read operation on the serial port has timed out. This occurs when
   * no data is received within the specified timeout period.
   */
  READ_TIMEOUT(6);

  private int code;

  /**
   * Constructs a new error code with the specified numeric identifier.
   *
   * @param code the numeric identifier for this error code
   */
  private ChannelSerialPortErrorCode(int code) {
    this.code = code;
  }

  /**
   * Returns the base error code without any offset applied.
   *
   * @return the base error code value
   */
  public int getCode() {
    return this.code;
  }

  /**
   * Returns the error code with the specified offset added.
   *
   * <p>This method allows different components or modules to have their own error code ranges by
   * applying an offset to the base error code. This helps maintain uniqueness across the entire
   * system while allowing for organized error code management.
   *
   * @param offset the offset to add to the base error code
   * @return the error code with the specified offset applied
   */
  public int getCode(int offset) {
    return offset + this.code;
  }
}
