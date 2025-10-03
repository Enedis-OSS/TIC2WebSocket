// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.io.datastreams;

/**
 * Interface defining the lifecycle management operations for data streams.
 *
 * <p>This interface extends {@link DataStreamUser} and provides the essential lifecycle methods for
 * managing data streams. It defines the core operations that all data stream implementations must
 * support: opening, closing, and configuration setup.
 *
 * @author Enedis Smarties team
 * @see DataStreamException
 */
public interface DataStream extends DataStreamUser {
  /**
   * Opens the data stream and prepares it for data transmission.
   *
   * <p>This method initializes the underlying communication channel and makes the stream ready to
   * handle data operations. The specific implementation depends on the stream type and
   * configuration (file, network, serial, etc.).
   *
   * <p>After a successful open operation, the stream should be ready to perform read/write
   * operations as defined by the stream direction.
   *
   * @throws DataStreamException if the stream cannot be opened due to configuration errors,
   *     resource unavailability, or communication failures
   */
  public void open() throws DataStreamException;

  /**
   * Closes the data stream and releases associated resources.
   *
   * <p>This method performs cleanup operations and releases any system resources (file handles,
   * network connections, serial ports, etc.) that were acquired during the stream's operation.
   *
   * <p>After closing, the stream should not be used for further operations unless it is reopened.
   * Multiple close operations should be safe to call.
   *
   * @throws DataStreamException if an error occurs during the cleanup process
   */
  public void close() throws DataStreamException;

  /**
   * Configures the data stream with the specified configuration parameters.
   *
   * <p>This method applies the given configuration to the stream, setting up parameters such as
   * direction (input/output), protocol type, connection details, and other stream-specific
   * settings.
   *
   * <p>The configuration must be valid for the specific stream implementation. Invalid
   * configurations will result in a DataStreamException being thrown.
   *
   * @param configuration the configuration parameters for this data stream
   * @throws DataStreamException if the configuration is invalid, incompatible with the stream type,
   *     or if an error occurs during setup
   */
  public void setup(DataStreamConfiguration configuration) throws DataStreamException;
}
