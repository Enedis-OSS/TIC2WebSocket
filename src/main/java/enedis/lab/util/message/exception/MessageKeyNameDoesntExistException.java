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
public class MessageKeyNameDoesntExistException extends MessageException
{

	private static final long serialVersionUID = -2263755971102386572L;

	/**
	 * Default constructor
	 */
	public MessageKeyNameDoesntExistException()
	{
		super();
	}

	/**
	 * Constructor using message and cause
	 * 
	 * @param message
	 * @param cause
	 */
	public MessageKeyNameDoesntExistException(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * Constructor using message
	 * 
	 * @param message
	 */
	public MessageKeyNameDoesntExistException(String message)
	{
		super(message);
	}

	/**
	 * Constructor using cause
	 * 
	 * @param cause
	 */
	public MessageKeyNameDoesntExistException(Throwable cause)
	{
		super(cause);
	}

}
