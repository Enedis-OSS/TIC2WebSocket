// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.io.datastreams;

import enedis.lab.types.DataDictionary;

/**
 * Abstract base class for data input streams.
 *
 * <p>This class provides the foundation for implementing input-only data streams that can read data
 * from various sources. It extends {@link DataStreamBase} and enforces input-only operations by
 * preventing write operations and validating that the stream direction is set to INPUT.
 *
 * <p>Concrete implementations should override the read methods to provide specific input
 * functionality for different data sources (files, network connections, serial ports, etc.).
 *
 * @author Enedis Smarties team
 * @see DataStreamBase
 * @see DataStreamConfiguration
 * @see DataStreamDirection
 */
public abstract class DataInputStream extends DataStreamBase {

  /**
   * Constructs a new DataInputStream with the specified configuration.
   *
   * <p>The configuration must specify INPUT as the direction for this stream to be valid.
   *
   * @param configuration the configuration for this data input stream
   * @throws DataStreamException if the configuration is invalid
   */
  public DataInputStream(DataStreamConfiguration configuration) throws DataStreamException {
    super(configuration);
  }

  /**
   * This method is overridden to prevent write operations on input streams. Calling this method
   * will always throw a DataStreamException with an "operation denied" message.
   *
   * @param data the data dictionary to write (ignored)
   * @throws DataStreamException always thrown with "operation denied" message
   */
  @Override
  public final void write(DataDictionary data) throws DataStreamException {
    DataStreamException.raiseOperationDenied("write");
  }

  /**
   * This method validates that the configuration direction is set to INPUT. If the direction is not
   * INPUT, a DataStreamException is thrown with details about the expected and actual direction
   * values.
   *
   * @param configuration the configuration to validate and apply
   * @throws DataStreamException if the direction is not INPUT
   */
  @Override
  public void setup(DataStreamConfiguration configuration) throws DataStreamException {
    super.setup(configuration);

    if (DataStreamDirection.INPUT != configuration.getDirection()) {
      DataStreamException.raiseInvalidConfiguration(
          DataStreamConfiguration.KEY_DIRECTION
              + "="
              + configuration.getDirection().name()
              + "(expected "
              + DataStreamDirection.INPUT.name()
              + ")");
    }
  }
}
