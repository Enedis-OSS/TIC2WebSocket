// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.protocol.tic.datastreams;

import enedis.lab.codec.CodecException;
import enedis.lab.io.datastreams.DataInputStream;
import enedis.lab.io.datastreams.DataStreamException;
import enedis.lab.io.datastreams.DataStreamType;
import enedis.lab.protocol.tic.TICMode;
import enedis.lab.protocol.tic.channels.ChannelTICSerialPort;
import enedis.lab.protocol.tic.codec.TICCodec;
import enedis.lab.protocol.tic.frame.TICError;
import enedis.lab.protocol.tic.frame.TICFrame;
import enedis.lab.types.DataDictionary;
import enedis.lab.types.DataDictionaryException;
import enedis.lab.types.datadictionary.DataDictionaryBase;

/**
 * TIC input stream
 */
public class TICInputStream extends DataInputStream
{
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// CONSTANTS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** Key timestamp */
	public static final String	KEY_TIMESTAMP	= "timestamp";
	/** Key channel */
	public static final String	KEY_CHANNEL		= "channel";
	/** Key data */
	public static final String	KEY_DATA		= "data";

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

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// ATTRIBUTES
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	protected TICCodec			codec;

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// CONSTRUCTORS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Constructor using configuration
	 *
	 * @param configuration
	 * @throws DataStreamException
	 */
	public TICInputStream(TICStreamConfiguration configuration) throws DataStreamException
	{
		super(configuration);
		this.codec = new TICCodec();
		this.codec.setCurrentMode(configuration.getTicMode());
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// INTERFACE
	/// DataInputStream
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public DataDictionary read() throws DataStreamException
	{
		return null;
	}

	@Override
	public DataStreamType getType()
	{
		return DataStreamType.TIC;
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// INTERFACE
	/// ChannelListener
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void onDataRead(String channelName, byte[] data)
	{
		if (!this.notifier.getSubscribers().isEmpty())
		{
			DataDictionary ticFrame = null;
			try
			{
				ticFrame = this.decodeTICFrame(data);
				if (ticFrame != null)
				{
					this.notifyOnDataReceived(ticFrame);
				}
			}
			catch (DataDictionaryException exception)
			{
				this.logger.error(exception.getMessage(), exception);
			}
			catch (CodecException exception)
			{
				DataDictionaryBase errorDataDictionary = new DataDictionaryBase();

				String KEY_PARTIAL_FRAME = "partialTICFrame";

				errorDataDictionary.addKey(KEY_PARTIAL_FRAME);

				try 
				{
					Object exceptionData = exception.getData();
					if (exceptionData != null && exceptionData instanceof TICFrame)
					{
						try 
						{
							DataDictionary frameDataDictionary = ((TICFrame) exceptionData).getDataDictionary();
							if (frameDataDictionary != null) 
							{
								errorDataDictionary.set(KEY_PARTIAL_FRAME, frameDataDictionary);
							}
							else
							{
								this.logger.warn("Frame data dictionary is null");
								errorDataDictionary.set(KEY_PARTIAL_FRAME, "");
							}
						} 
						catch (DataDictionaryException frameDataDictionaryException)
						{
							this.logger.warn("Can't get TICFrame data dictionary: " + frameDataDictionaryException.getMessage());
							errorDataDictionary.set(KEY_PARTIAL_FRAME, "");
						}
					}
					else
					{
						this.logger.error("No frame data available");
						errorDataDictionary.set(KEY_PARTIAL_FRAME, "");
					}
				}
				catch (DataDictionaryException dataDictionaryException)
				{
					this.logger.error("Can't convert TICFrame to DataDictonary" + dataDictionaryException.getMessage());
				}

				this.logger.error(exception.getMessage() + errorDataDictionary.toString());
				this.onErrorDetected(channelName, TICError.TIC_READER_READ_FRAME_CHECKSUM_INVALID.getValue(), exception.getMessage(), errorDataDictionary);
			}
		}
	}

	@Override
	public void onDataWritten(String channelName, byte[] data)
	{
		// Not used
	}

	@Override
	public void onErrorDetected(String channelName, int errorCode, String errorMessage)
	{
		this.notifyOnErrorDetected(errorCode, errorMessage, null);
	}

	@Override
	public void onErrorDetected(String channelName, int errorCode, String errorMessage, DataDictionary errorData)
	{
		this.notifyOnErrorDetected(errorCode, errorMessage, errorData);
	}
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// PUBLIC METHODS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Get Mode
	 *
	 * @return TIC Mode
	 */
	public TICMode getMode()
	{
		ChannelTICSerialPort channelTIC = (ChannelTICSerialPort) this.channel;

		return channelTIC.getMode();
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// PROTECTED METHODS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	protected DataDictionary decodeTICFrame(byte[] data) throws DataDictionaryException, CodecException
	{
		TICFrame ticFrame;
		try
		{
			ticFrame = this.codec.decode(data);
		}
		catch (CodecException exception)
		{
			throw new CodecException(exception.getMessage(), exception.getData());
		}

		long timestamp = System.currentTimeMillis();

		DataDictionaryBase decodedTICFrame = new DataDictionaryBase();

		decodedTICFrame.set(KEY_TIMESTAMP, timestamp);
		decodedTICFrame.set(KEY_CHANNEL, this.getConfiguration().getChannelName());
		decodedTICFrame.set(KEY_DATA, ticFrame);

		return decodedTICFrame;
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// PRIVATE METHODS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
