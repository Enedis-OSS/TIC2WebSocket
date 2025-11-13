// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.io.datastreams;

import enedis.lab.types.DataDictionary;
import enedis.lab.util.task.Notifier;

/**
 * Interface defining the user-facing operations for data stream interaction.
 *
 * <p>This interface extends {@link Notifier} with {@link DataStreamListener} support, providing the
 * core functionality for reading and writing data, as well as querying stream properties and
 * configuration. It represents the primary interface through which users interact with data
 * streams.
 *
 * <p>The interface provides methods for:
 *
 * <ul>
 *   <li>Data operations (read/write)
 *   <li>Stream identification (name)
 *   <li>Configuration access
 *   <li>Stream property queries (direction, type, status)
 *   <li>Listener management (through Notifier interface)
 * </ul>
 *
 * @author Enedis Smarties team
 * @see DataStream
 * @see DataStreamBase
 * @see DataStreamListener
 * @see DataStreamConfiguration
 */
public interface DataStreamUser extends Notifier<DataStreamListener> {
  /**
   * Reads data from the stream.
   *
   * <p>This method retrieves data from the stream and returns it as a DataDictionary. The method
   * blocks until data is available or an error occurs. The availability and behavior of this
   * operation depend on the stream direction and status.
   *
   * @return the data read from the stream as a DataDictionary
   * @throws DataStreamException if the stream is not readable, an I/O error occurs, or the
   *     operation is not supported by the stream direction
   */
  public DataDictionary read() throws DataStreamException;

  /**
   * Writes data to the stream.
   *
   * <p>This method sends the provided data through the stream. The data is encoded according to the
   * stream type and transmitted to the configured destination. The availability of this operation
   * depends on the stream direction and status.
   *
   * @param data the data to write to the stream
   * @throws DataStreamException if the stream is not writable, an I/O error occurs, the data is
   *     invalid, or the operation is not supported by the stream direction
   */
  public void write(DataDictionary data) throws DataStreamException;

  /**
   * Returns the name of this data stream.
   *
   * <p>The name uniquely identifies the stream within the system and is used for logging,
   * monitoring, and listener notifications.
   *
   * @return the name of the data stream
   */
  public String getName();

  /**
   * Returns the configuration of this data stream.
   *
   * <p>The configuration contains all parameters that define how the stream operates, including
   * name, type, direction, and channel association.
   *
   * @return the stream configuration
   */
  public DataStreamConfiguration getConfiguration();

  /**
   * Returns the direction of this data stream.
   *
   * <p>The direction indicates whether the stream is configured for input (reading), output
   * (writing), or bidirectional (both reading and writing) operations.
   *
   * @return the stream direction
   */
  public DataStreamDirection getDirection();

  /**
   * Returns the type of this data stream.
   *
   * <p>The type identifies the protocol or data format used by the stream, such as RAW, TIC,
   * Modbus, or IEC 61850.
   *
   * @return the stream type
   */
  public DataStreamType getType();

  /**
   * Returns the current status of this data stream.
   *
   * <p>The status indicates the operational state of the stream, such as UNKNOWN, CLOSED, OPENED,
   * or ERROR. This is useful for monitoring stream health and availability.
   *
   * @return the current stream status
   */
  public DataStreamStatus getStatus();
}
