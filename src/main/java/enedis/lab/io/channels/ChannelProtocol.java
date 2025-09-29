// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.io.channels;

/**
 * Enumeration representing different types of communication channel protocols
 * that can be used for data transmission in the TIC (Télé Information Client) system.
 * 
 * <p>This enum defines the various communication protocols supported by the system,
 * ranging from file-based operations to network protocols and industrial communication
 * standards like Modbus.
 * 
 * @author Jehan BOUSCH
 * @author Mathieu SABARTHES
 * @since 1.0
 */
public enum ChannelProtocol {

  /**
   * File-based communication protocol.
   * Used for reading from or writing to local files on the filesystem.
   */
  FILE,
  
  /**
   * Serial port communication protocol.
   * Used for direct communication through serial interfaces (RS-232, RS-485, etc.).
   */
  SERIAL_PORT,
  
  /**
   * Modbus RTU (Remote Terminal Unit) protocol.
   * A serial communication protocol commonly used in industrial automation systems.
   */
  MODBUS_RTU,
  
  /**
   * Modbus TCP protocol.
   * A network-based implementation of the Modbus protocol over TCP/IP.
   */
  MODBUS_TCP,
  
  /**
   * Modbus stub protocol.
   * A testing/mock implementation of Modbus for development and testing purposes.
   */
  MODBUS_STUB,
  
  /**
   * Transmission Control Protocol (TCP).
   * A reliable, connection-oriented network protocol for data transmission.
   */
  TCP,
  
  /**
   * User Datagram Protocol (UDP).
   * A connectionless network protocol for fast, lightweight data transmission.
   */
  UDP,
}
