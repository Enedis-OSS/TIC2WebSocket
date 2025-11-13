// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.io.datastreams;

/**
 * Enumeration representing different types of data streams based on protocol and data format.
 *
 * <p>This enum defines the various data stream types supported by the system, each corresponding to
 * a specific communication protocol or data format. The type determines how data is encoded,
 * decoded, and processed by the stream.
 *
 * <p>Stream types range from raw unformatted data to specialized industrial protocols like Modbus
 * and IEC 61850, as well as specific product protocols like TIC and LEM DC.
 *
 * @author Enedis Smarties team
 * @see DataStreamConfiguration
 * @see DataStream
 */
public enum DataStreamType {
  /**
   * Raw data stream without specific protocol formatting. Data is transmitted without
   * protocol-specific encoding or decoding, suitable for binary data or custom formats.
   */
  RAW,

  /**
   * TIC (Télé Information Client) protocol stream. Used for handling data from smart meters and
   * energy management devices using the TIC protocol standard.
   */
  TIC,

  /**
   * IEC 61850 protocol stream. Used for communication with electrical substation automation systems
   * following the IEC 61850 international standard.
   */
  IEC61850,

  /**
   * Modbus slave mode stream. The stream operates as a Modbus slave device, responding to requests
   * from a Modbus master.
   */
  MODBUS_SLAVE,

  /**
   * Modbus master mode stream. The stream operates as a Modbus master device, initiating requests
   * to Modbus slave devices.
   */
  MODBUS_MASTER,

  /**
   * LEM DC product protocol stream. Used for communication with LEM (Liaison
   * Électronique-Mécanique) DC measurement devices.
   */
  LEM_DC;
}
