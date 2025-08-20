// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.io.channels;

import enedis.lab.types.ExceptionBase;

/**
 * Class used for the channel exceptions
 */
public class ChannelException extends ExceptionBase
{
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// CONSTANTS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private static final long	serialVersionUID					= -3507876436535833098L;

	/** Invalid channel error code */
	public static final int		ERRCODE_INVALID_CHANNEL				= -5;
	/** Already exists error code */
	public static final int		ERRCODE_ALREADY_EXISTS				= -6;
	/** Internal error error code */
	public static final int		ERRCODE_INTERNAL_ERROR				= -7;
	/** Channel not ready error code */
	public static final int		ERRCODE_CHANNEL_NOT_READY			= -8;
	/** Invalid configuration type error code */
	public static final int		ERRCODE_INVALID_CONFIGURATION_TYPE	= -9;
	/** Invalid configuration error code */
	public static final int		ERRCODE_INVALID_CONFIGURATION		= -10;
	/** Operation denied error code */
	public static final int		ERRCODE_OPERATION_DENIED			= -11;
	/** Channel doesn't exist error code */
	public static final int		ERRCODE_CHANNEL_DOESNT_EXIST		= -12;
	/** Unexpected error code */
	public static final int		ERRCODE_UNEXPECTED					= -99;

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// TYPES
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// STATIC METHODS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * @param channelType
	 * @throws ChannelException
	 */
	public static void raiseUnhandledChannelType(String channelType) throws ChannelException
	{
		throw new ChannelException(ERRCODE_INVALID_CHANNEL, channelType + " channel is not handled");
	}

	/**
	 * @param channelType
	 * @param expectedChannelType
	 * @throws ChannelException
	 */
	public static void raiseConflictingChannelType(String channelType, String expectedChannelType) throws ChannelException
	{
		throw new ChannelException(ERRCODE_INVALID_CHANNEL, channelType + " channel is conflicting with " + expectedChannelType);
	}

	/**
	 * @param info
	 * @throws ChannelException
	 */
	public static void raiseInternalError(String info) throws ChannelException
	{
		throw new ChannelException(ERRCODE_INTERNAL_ERROR, info);
	}

	/**
	 * @param info
	 * @throws ChannelException
	 */
	public static void raiseChannelNotReady(String info) throws ChannelException
	{
		throw new ChannelException(ERRCODE_CHANNEL_NOT_READY, info);
	}

	/**
	 * @param configuration
	 * @param expected_configuration_name
	 * @throws ChannelException
	 */
	public static void raiseInvalidConfigurationType(ChannelConfiguration configuration, String expected_configuration_name) throws ChannelException
	{
		throw new ChannelException(ERRCODE_INVALID_CONFIGURATION_TYPE,
				"Configuration " + configuration.getClass().getSimpleName() + " is not a valid type (" + expected_configuration_name + " expected)");
	}

	/**
	 * @param info
	 * @throws ChannelException
	 */
	public static void raiseInvalidConfiguration(String info) throws ChannelException
	{
		throw new ChannelException(ERRCODE_INVALID_CONFIGURATION, "Configuration is invalid (" + info + ")");
	}

	/**
	 * @param operation
	 * @throws ChannelException
	 */
	public static void raiseOperationDenied(String operation) throws ChannelException
	{
		throw new ChannelException(ERRCODE_OPERATION_DENIED, "Operation \'" + operation + "\' is not allowed");
	}

	/**
	 * @param info
	 * @throws ChannelException
	 */
	public static void raiseUnexpectedError(String info) throws ChannelException
	{
		throw new ChannelException(ERRCODE_UNEXPECTED, "Unexpected error occurs : " + info);
	}

	/**
	 * @param channelName
	 * @throws ChannelException
	 */
	public static void raiseAlreadyExists(String channelName) throws ChannelException
	{
		throw new ChannelException(ERRCODE_ALREADY_EXISTS, "Channel " + channelName + " already exists");
	}

	/**
	 * @param channelName
	 * @throws ChannelException
	 */
	public static void raiseDoesntExist(String channelName) throws ChannelException
	{
		throw new ChannelException(ERRCODE_CHANNEL_DOESNT_EXIST, "Channel " + channelName + " doesn't exist");
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// ATTRIBUTES
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// CONSTRUCTORS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * @param code
	 * @param info
	 */
	public ChannelException(int code, String info)
	{
		super(code, info);
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// INTERFACE
	/// interfaceName
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// PUBLIC METHODS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// PROTECTED METHODS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// PRIVATE METHODS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
