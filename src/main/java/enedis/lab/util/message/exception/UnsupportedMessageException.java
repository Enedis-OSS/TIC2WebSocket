// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.util.message.exception;

/**
 * Unvalid message format exception
 */
public class UnsupportedMessageException extends MessageException
{

	private static final long serialVersionUID = -2263755971102386572L;

	/**
	 * Default constructor
	 */
	public UnsupportedMessageException()
	{
		super();
	}

	/**
	 * Constructor using message and cause
	 * 
	 * @param message
	 * @param cause
	 */
	public UnsupportedMessageException(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * Constructor using message
	 * 
	 * @param message
	 */
	public UnsupportedMessageException(String message)
	{
		super(message);
	}

	/**
	 * Constructor using cause
	 * 
	 * @param cause
	 */
	public UnsupportedMessageException(Throwable cause)
	{
		super(cause);
	}

}
