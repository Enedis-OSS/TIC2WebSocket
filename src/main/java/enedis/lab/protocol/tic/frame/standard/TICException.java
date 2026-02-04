// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
// 
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.protocol.tic.frame.standard;

import enedis.lab.protocol.tic.frame.TICError;

/**
 * TICException
 *
 */
public class TICException extends Exception
{
	/**
	 * Unique identifier used for serialization
	 */
	private static final long	serialVersionUID	= -2780151361870269473L;
	private TICError			error;

	/**
	 * Constructor TICException
	 */
	public TICException()
	{
		super();
		this.reset();
	}

	/**
	 * Constructor TICException
	 *
	 * @param message
	 */
	public TICException(String message)
	{
		super(message);
		this.error = TICError.TIC_READER_DEFAULT_ERROR;
	}

	/**
	 * Constructor TICException
	 *
	 * @param message
	 * @param readerError
	 */
	public TICException(String message, TICError readerError)
	{
		super(message);
		this.error = readerError;
	}

	/**
	 * Reset TICError
	 */
	public void reset()
	{
		this.error = TICError.TIC_READER_DEFAULT_ERROR;
	}

	/**
	 * Get error
	 *
	 * @return the readerError
	 */
	public TICError getError()
	{
		return this.error;
	}
}
