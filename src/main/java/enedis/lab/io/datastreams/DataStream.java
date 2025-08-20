// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.io.datastreams;

/**
 * DataStream interface
 */
public interface DataStream extends DataStreamUser
{
	/**
	 * Open the stream
	 *
	 * @throws DataStreamException
	 */
	public void open() throws DataStreamException;

	/**
	 * Close the stream
	 *
	 * @throws DataStreamException
	 */
	public void close() throws DataStreamException;

	/**
	 * Setup the stream from a configuration
	 *
	 * @param configuration
	 *            configuration given for the stream
	 * @throws DataStreamException
	 *             if an error occurs
	 */
	public void setup(DataStreamConfiguration configuration) throws DataStreamException;
}
