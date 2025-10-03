// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.io.datastreams;

/**
 * Enumeration representing the data flow direction of a stream.
 *
 * <p>This enum defines the possible directions for data transmission in a data stream, determining
 * whether the stream handles incoming data, outgoing data, or both. The direction is a fundamental
 * characteristic of a stream that affects its behavior and the operations it supports.
 *
 * @author Enedis Smarties team
 * @see DataStreamConfiguration
 * @see DataStream
 */
public enum DataStreamDirection {
  /**
   * Input direction for streams that receive data. Streams with this direction are configured to
   * read incoming data from a source.
   */
  INPUT,

  /**
   * Output direction for streams that send data. Streams with this direction are configured to
   * write outgoing data to a destination.
   */
  OUTPUT,

  /**
   * Bidirectional streams that can both receive and send data. Streams with this direction support
   * both read and write operations, allowing two-way communication.
   */
  INOUTPUT;
}
