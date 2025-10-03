// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
// 
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.types;

/**
 * Data dictionary exception
 */
public class DataDictionaryException extends Exception
{

	private static final long serialVersionUID = -7967428428453584771L;

	/**
	 * Default constructor
	 */
	public DataDictionaryException()
	{
		super();
	}

	/**
	 * Constructor using message, cause, enable suppression flag and writable stack trace flag
	 * 
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public DataDictionaryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * Constructor using message and cause
	 * 
	 * @param message
	 * @param cause
	 */
	public DataDictionaryException(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * Constructor using message
	 * 
	 * @param message
	 */
	public DataDictionaryException(String message)
	{
		super(message);
	}

	/**
	 * Constructor using cause
	 * 
	 * @param cause
	 */
	public DataDictionaryException(Throwable cause)
	{
		super(cause);
	}

}
