// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.protocol.tic.channels;

import java.util.Arrays;

import enedis.lab.io.channels.ChannelConfiguration;
import enedis.lab.io.channels.ChannelException;
import enedis.lab.io.channels.serialport.ChannelSerialPort;
import enedis.lab.io.channels.serialport.ChannelSerialPortErrorCode;
import enedis.lab.protocol.tic.TICMode;
import enedis.lab.types.BytesArray;
import enedis.lab.types.DataDictionaryException;
import enedis.lab.util.time.Time;

/**
 * Channel TIC SerialPort
 */
public class ChannelTICSerialPort extends ChannelSerialPort
{
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// CONSTANTS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private static final byte	START_PATTERN				= (byte) 0x02;
	private static final byte	END_PATTERN					= (byte) 0x03;
	private static final int	RECEIVE_DATA_POLLING_PERIOD	= 100;
	private static final byte[]	HISTORIC_BUFFER_START		= { 2, 10, 65, 68, 67, 79 };
	private static final byte[]	STANDARD_BUFFER_START		= { 2, 10, 65, 68, 83, 67 };

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

	private TICMode				currentMode;

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// CONSTRUCTORS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Constructor: create an instance from a SerialPortConfiguration
	 *
	 * @param configuration
	 * @throws ChannelException
	 */
	public ChannelTICSerialPort(ChannelTICSerialPortConfiguration configuration) throws ChannelException
	{
		super(configuration);
		this.currentMode = null;
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// INTERFACE
	/// Channel
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void setup(ChannelConfiguration configuration) throws ChannelException
	{
		if (configuration == null)
		{
			ChannelException.raiseInvalidConfiguration("null");
		}
		if (!(configuration instanceof ChannelTICSerialPortConfiguration))
		{
			ChannelException.raiseInvalidConfigurationType(configuration, ChannelTICSerialPortConfiguration.class.getSimpleName());
		}
		super.setup(configuration);
	}

	@Override
	public ChannelTICSerialPortConfiguration getConfiguration()
	{
		return (ChannelTICSerialPortConfiguration) this.configuration;
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// PUBLIC METHODS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Get mode
	 *
	 * @return TIC Mode
	 */
	public TICMode getMode()
	{
		return (this.getCurrentMode() != null) ? this.getCurrentMode() : this.getSelectedMode();
	}

	/**
	 * Get selected mode
	 *
	 * @return selected Mode
	 */
	public TICMode getSelectedMode()
	{
		return this.getConfiguration().getTicMode();
	}

	/**
	 * Get current mode
	 *
	 * @return current Mode
	 */
	public TICMode getCurrentMode()
	{
		return this.currentMode;
	}

	@Override
	public byte[] read() throws ChannelException
	{
		long beginTime = System.currentTimeMillis();
		long elapsedTime = 0;
		long timeout = this.getSyncReadTimeout();
		BytesArray buffer = new BytesArray();
		byte[] ticFrame = null;
		int startOfFrame = -1, endOfFrame = -1;

		while (elapsedTime < timeout || timeout == 0)
		{
			if (this.available() > 0)
			{
				buffer.addAll(super.read(1));
				if (startOfFrame == -1)
				{
					startOfFrame = buffer.indexOf(START_PATTERN);
					if (startOfFrame != -1)
					{
						endOfFrame = buffer.indexOf(END_PATTERN, startOfFrame + 1);
						if (endOfFrame != -1)
						{
							ticFrame = buffer.subList(startOfFrame, endOfFrame).getBytes();
							break;
						}
					}
				}
				else
				{
					endOfFrame = buffer.indexOf(END_PATTERN, startOfFrame + 1);
					if (endOfFrame != -1)
					{
						ticFrame = buffer.subList(startOfFrame, endOfFrame).getBytes();
						break;
					}
				}
			}
			else
			{
				Time.sleep(RECEIVE_DATA_POLLING_PERIOD);
				elapsedTime = (System.currentTimeMillis() - beginTime);
			}
		}

		return ticFrame;
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// PROTECTED METHODS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	protected void receiveData()
	{
		if (this.currentMode == null)
		{
			if (this.autoDetectMode() == null)
			{
				this.onReadTimeout();
				return;
			}
		}
		try
		{
			byte[] ticFrame = this.read();

			if (ticFrame == null)
			{
				this.onReadTimeout();
			}
			else
			{
				this.notifyOnDataRead(ticFrame);
			}
		}
		catch (ChannelException e)
		{
			this.notifyOnErrorDetected(ChannelSerialPortErrorCode.READ_FAILED.getCode(ERROR_CODE_OFFSET), "TIC read failed: " + e.getErrorInfo());
		}
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// PRIVATE METHODS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void onReadTimeout()
	{
		this.notifyOnErrorDetected(ChannelSerialPortErrorCode.READ_TIMEOUT.getCode(ERROR_CODE_OFFSET), "TIC read timeout");
		this.configurePort();
		this.currentMode = null;
	}

	private void configurePort()
	{
		this.closePort();
		this.openPort();
		try
		{
			this.flush();
		}
		catch (ChannelException e)
		{
		}
	}

	private void setSelectedMode(TICMode ticMode)
	{
		try
		{
			this.getConfiguration().setTicMode(ticMode);
		}
		catch (DataDictionaryException e)
		{
			this.logger.error("Cannot set TIC mode " + ticMode, e);
		}
	}

	private boolean checkAndUpdateMode(TICMode ticMode)
	{
		boolean result = false;
		this.setSelectedMode(ticMode);
		this.configurePort();
		try
		{
			byte[] ticFrame = this.read();
			if (ticFrame != null && ticFrame.length > HISTORIC_BUFFER_START.length)
			{
				byte[] ticFrameStart = new byte[HISTORIC_BUFFER_START.length];
				System.arraycopy(ticFrame, 0, ticFrameStart, 0, ticFrameStart.length);
				if (ticMode == TICMode.HISTORIC)
				{
					if (Arrays.equals(ticFrameStart, HISTORIC_BUFFER_START))
					{
						result = true;
					}
				}
				else if (ticMode == TICMode.STANDARD)
				{
					if (Arrays.equals(ticFrameStart, STANDARD_BUFFER_START))
					{
						result = true;
					}
				}
			}
		}
		catch (ChannelException e)
		{
			this.notifyOnErrorDetected(ChannelSerialPortErrorCode.READ_FAILED.getCode(ERROR_CODE_OFFSET), "TIC read failed: " + e.getErrorInfo());
		}
		if (result)
		{
			this.currentMode = ticMode;
		}
		this.setSelectedMode(TICMode.AUTO);

		return result;
	}

	private TICMode autoDetectMode()
	{
		if (this.getSelectedMode() != TICMode.AUTO)
		{
			this.currentMode = this.getSelectedMode();
			return this.getSelectedMode();
		}
		this.logger.debug("Auto detecting TIC Mode");
		if (!this.checkAndUpdateMode(TICMode.HISTORIC))
		{
			if (!this.checkAndUpdateMode(TICMode.STANDARD))
			{
				this.logger.debug("TIC Mode not detected");

				return null;
			}
			this.logger.debug("TIC Mode STANDARD detected");

			return TICMode.STANDARD;
		}
		this.logger.debug("TIC Mode HISTORIC detected");

		return TICMode.HISTORIC;
	}
}
