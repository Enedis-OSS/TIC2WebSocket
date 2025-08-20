// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
// 
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.io.channels.serialport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang3.SystemUtils;

import enedis.lab.io.channels.ChannelConfiguration;
import enedis.lab.io.channels.ChannelException;
import enedis.lab.io.channels.ChannelPhysical;
import enedis.lab.io.channels.ChannelStatus;
import enedis.lab.io.serialport.SerialPortDescriptor;
import enedis.lab.io.serialport.SerialPortFinderBase;
import enedis.lab.types.DataDictionaryException;
import enedis.lab.util.SystemError;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

/**
 * Channel serial port
 */
public class ChannelSerialPort extends ChannelPhysical implements SerialPortEventListener
{
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// CONSTANTS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	protected static final int	ERROR_CODE_OFFSET	= 1000;
	private static final int	DEFAULT_DELAY		= 500;
	private static final int	DELAY_REOPEN		= DEFAULT_DELAY * 10;

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
	 * Check if the port is found
	 *
	 * @param portId
	 * @param portName
	 * @return true if the specified port is found
	 */
	public static boolean isPortFound(String portId, String portName)
	{
		return SerialPortFinderBase.getInstance().findByPortIdOrPortName(portId, portName) != null;
	}

	/**
	 * Find the port name corresponding to the given portId or portName. portId has a higher priority
	 *
	 * @param portId
	 * @param portName
	 * @return port name
	 */
	public static String findPortName(String portId, String portName)
	{
		SerialPortDescriptor descriptor = null;
		if (portId != null)
		{
			descriptor = SerialPortFinderBase.getInstance().findByPortId(portId);
		}
		else
		{
			descriptor = SerialPortFinderBase.getInstance().findByPortName(portName);
		}

		return (descriptor != null) ? descriptor.getPortName() : null;
	}

	/**
	 * Convert the parity provided by the SerialPortConfiguration to a value usable by the channel
	 *
	 * @param parity
	 * @return parity
	 */
	public static int parityFromConfiguration(Parity parity)
	{
		int retVal = -1;

		switch (parity)
		{
			case EVEN:
				retVal = SerialPort.PARITY_EVEN;
				break;
			case MARK:
				retVal = SerialPort.PARITY_MARK;
				break;
			case NONE:
				retVal = SerialPort.PARITY_NONE;
				break;
			case ODD:
				retVal = SerialPort.PARITY_ODD;
				break;
			case SPACE:
				retVal = SerialPort.PARITY_SPACE;
				break;
			default: /* Cas non atteignable */
		}

		return retVal;
	}

	/**
	 * Convert the stopBits parameter provided by the SerialPortConfiguration to a value usable by the channel
	 *
	 * @param stop_bits
	 * @return stopBits
	 */
	public static int stopBitsFromConfiguration(float stop_bits)
	{
		int retVal = -1;

		if (1 == stop_bits)
		{
			retVal = SerialPort.STOPBITS_1;
		}

		else if (1.5 == stop_bits)
		{
			retVal = SerialPort.STOPBITS_1_5;
		}

		else if (2 == stop_bits)
		{
			retVal = SerialPort.STOPBITS_2;
		}

		return retVal;
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// ATTRIBUTES
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private SerialPort	portHandler	= null;

	private String		previousPortName;
	private String		currentPortName;

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// CONSTRUCTORS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Constructor: create an instance from a ChannelConfiguration
	 *
	 * @param configuration
	 * @throws ChannelException
	 */
	public ChannelSerialPort(ChannelConfiguration configuration) throws ChannelException
	{
		super(configuration);
		this.setPeriod(DEFAULT_DELAY);

		this.currentPortName = this.findPortName();
		this.previousPortName = this.currentPortName;
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// INTERFACE
	/// Channel
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void start()
	{
		this.logger.info("Channel " + this.getName() + " start (" + this.findPortName() + ")");
		this.openPort();
		super.start();
	}

	@Override
	public void stop()
	{
		this.logger.info("Channel " + this.getName() + " stop (" + this.findPortName() + ")");
		super.stop();
		this.closePort();
	}

	@Override
	public void setup(ChannelConfiguration configuration) throws ChannelException
	{
		if (configuration == null)
		{
			ChannelException.raiseInvalidConfiguration("null");
		}
		if (!(configuration instanceof ChannelSerialPortConfiguration))
		{
			ChannelException.raiseInvalidConfigurationType(configuration, ChannelSerialPortConfiguration.class.getSimpleName());
		}
		super.setup(configuration);
	}

	@Override
	public byte[] read() throws ChannelException
	{
		byte[] buffer = null;

		if (this.portHandler == null || !this.portHandler.isOpened())
		{
			ChannelException.raiseChannelNotReady(this.getPortName());
		}
		try
		{
			buffer = this.portHandler.readBytes();
		}
		catch (SerialPortException exception)
		{
			this.setStatus(ChannelStatus.ERROR);
			this.logger.error("Cannot read bytes", exception);
			ChannelException.raiseInternalError(exception.getMessage());
		}

		return buffer;
	}

	@Override
	public void write(byte[] data) throws ChannelException
	{
		if (this.portHandler == null || !this.portHandler.isOpened())
		{
			ChannelException.raiseChannelNotReady(this.getPortName());
		}
		try
		{
			this.portHandler.writeBytes(data);
		}
		catch (SerialPortException exception)
		{
			this.setStatus(ChannelStatus.ERROR);
			this.logger.error("Cannot write bytes", exception);
			ChannelException.raiseInternalError(exception.getMessage());
		}
	}

	@Override
	public ChannelSerialPortConfiguration getConfiguration()
	{
		return (ChannelSerialPortConfiguration) this.configuration.clone();
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// INTERFACE
	/// ChannelBase
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	protected void setup() throws ChannelException
	{
		/* 1. Save current state */
		ChannelStatus currentStatus = this.status;
		/* 2. Stop serial port */
		this.stop();
		/* 3. update path if symbolic link */
		this.updateRealPath();
		/* 4. Create serial port handler */
		if ((null == this.portHandler) || (false == this.getPortName().equals(this.portHandler.getPortName())))
		{
			this.portHandler = new SerialPort(this.getPortName());
		}
		/* 5. Restore current state */
		if (ChannelStatus.STARTED == currentStatus)
		{
			this.start();
		}
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// INTERFACE
	/// SerialPortEventListener
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void serialEvent(SerialPortEvent event)
	{
		switch (event.getEventType())
		{
			case SerialPortEvent.RXCHAR:
				this.receiveData();
				break;
			default: /* Aucune action */
		}
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// INTERFACE
	/// Runnable
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	protected void process()
	{
		int delay = DEFAULT_DELAY;

		if (this.status == ChannelStatus.STARTED)
		{
			if (!this.isPortFound())
			{
				this.onPortNotFound();
			}
			else if (this.hasPortChanged())
			{
				this.onPortNameChanged();
			}
			else if (!this.hasEventListener())
			{
				this.receiveData();
			}
		}
		else
		{
			if (this.isPortFound())
			{
				this.openPort();
				if (this.status == ChannelStatus.ERROR)
				{
					delay = DELAY_REOPEN;
				}
			}
		}
		this.setPeriod(delay);
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// PUBLIC METHODS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Blocking Read
	 *
	 * @param bytesCount
	 * @return read bytes
	 * @throws ChannelException
	 */
	public byte[] read(int bytesCount) throws ChannelException
	{
		byte[] buffer = null;

		if (this.portHandler == null || !this.portHandler.isOpened())
		{
			ChannelException.raiseChannelNotReady(this.getPortName());
		}
		try
		{
			buffer = this.portHandler.readBytes(bytesCount);
		}
		catch (SerialPortException exception)
		{
			this.setStatus(ChannelStatus.ERROR);
			this.logger.error("Cannot read bytes", exception);
			ChannelException.raiseInternalError(exception.getMessage());
		}

		return buffer;
	}

	/**
	 * Blocking read with timeout
	 *
	 * @param bytesCount
	 * @param timeout
	 * @return read bytes
	 * @throws ChannelException
	 */
	public byte[] read(int bytesCount, int timeout) throws ChannelException
	{
		byte[] buffer = null;

		if (this.portHandler == null || !this.portHandler.isOpened())
		{
			ChannelException.raiseChannelNotReady(this.getPortName());
		}
		try
		{
			buffer = this.portHandler.readBytes(bytesCount, timeout);
		}
		catch (SerialPortException exception)
		{
			this.logger.error("Cannot read bytes", exception);
			ChannelException.raiseInternalError(exception.getMessage());
		}
		catch (SerialPortTimeoutException exception)
		{
			ChannelException.raiseInternalError(exception.getMessage());
		}

		return buffer;
	}

	/**
	 * Get available bytes count
	 *
	 * @return Available bytes count
	 * @throws ChannelException
	 *             if serial port not open or internal error occurs
	 */
	public int available() throws ChannelException
	{
		if (this.portHandler == null || !this.portHandler.isOpened())
		{
			ChannelException.raiseChannelNotReady(this.getPortName());
		}
		try
		{
			return this.portHandler.getInputBufferBytesCount();
		}
		catch (SerialPortException exception)
		{
			this.logger.error("Cannot get bytes available", exception);
			ChannelException.raiseInternalError(exception.getMessage());
		}

		return 0;
	}

	/**
	 * Flush port
	 *
	 * @throws ChannelException
	 */
	public void flush() throws ChannelException
	{
		if (this.portHandler == null || !this.portHandler.isOpened())
		{
			ChannelException.raiseChannelNotReady(this.getPortName());
		}
		try
		{
			int mask = SerialPort.PURGE_RXCLEAR | SerialPort.PURGE_RXABORT | SerialPort.PURGE_TXCLEAR | SerialPort.PURGE_TXABORT;
			if (!this.portHandler.purgePort(mask))
			{
				this.logger.error("Channel " + this.getName() + " failed to purge port " + this.getPortName() + " (" + SystemError.getMessage() + ") ");
			}
		}
		catch (SerialPortException exception)
		{
			this.logger.error("Cannot purge port", exception);
			ChannelException.raiseInternalError(exception.getMessage());
		}
	}

	/**
	 *
	 * @return the identifier of the port. If the parameter has not been set, 'null' is returned.
	 */
	public String getPortId()
	{
		return this.getChannelConfiguration().getPortId();
	}

	/**
	 *
	 * @return the name of the port. If the parameter has not been set, 'null' is returned.
	 */
	public String getPortName()
	{
		return this.getChannelConfiguration().getPortName();
	}

	/**
	 *
	 * @return the name of the port opened. If the port has not been opened, 'null' is returned.
	 */
	public String getPortNameOpened()
	{
		return (this.portHandler != null) ? this.portHandler.getPortName() : null;
	}

	/**
	 * Find the port name associated with configuration
	 *
	 * @return Port name found or null if nothing found
	 */
	public String findPortName()
	{
		return findPortName(this.getPortId(), this.getPortName());
	}

	/**
	 * @return the current baudrate value. If the Baudrate has not been set (port has not been used), -1 is returned
	 */
	public int getBaudrate()
	{
		return this.getChannelConfiguration().getBaudrate().intValue();
	}

	/**
	 * @return the number of bits used for data (5, 6, 7, 8). If the parameter does not exist in channel configuration
	 *         or is not consistent, the value '-1' is returned.
	 */
	public int getDataBits()
	{
		return this.getChannelConfiguration().getDataBits().intValue();
	}

	/**
	 * @return the current number of bits used for stop (1.0 , 1.5 , 2.0).
	 *
	 *         NB : If the port has been used, the method returns -1.
	 */
	public float getStopBits()
	{
		return this.getChannelConfiguration().getStopBits().floatValue();
	}

	/**
	 * @return the current parity used by the port
	 *
	 *         NB : If the port has been used, the method returns null.
	 */
	public Parity getParity()
	{
		return this.getChannelConfiguration().getParity();
	}

	/**
	 * @return the timeout used for a synchronous read operation
	 */
	public int getSyncReadTimeout()
	{
		return this.getChannelConfiguration().getSyncReadTimeout().intValue();
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// PROTECTED METHODS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	protected boolean hasEventListener()
	{
		return false;
	}

	protected boolean addEventListener()
	{
		if (this.portHandler == null)
		{
			return false;
		}
		int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;
		try
		{
			if (!this.portHandler.setEventsMask(mask))
			{
				this.logger.error("Channel " + this.getName() + " failed to set event mask on port " + this.getPortName() + " (" + SystemError.getMessage() + ") ");
				return false;
			}
		}
		catch (SerialPortException exception)
		{
			this.logger.error("Channel " + this.getName() + " failed to set event mask on port " + this.getPortName() + " : " + exception.getMessage());
			return false;
		}
		try
		{
			this.portHandler.addEventListener(this);
		}
		catch (SerialPortException e)
		{
			this.logger.error("Channel " + this.getName() + " failed to add listener :" + e.getMessage(), e);
			return false;
		}

		return true;
	}

	protected boolean removeEventListener()
	{
		if (this.portHandler == null)
		{
			return false;
		}
		try
		{
			if (!this.portHandler.removeEventListener())
			{
				this.logger.error("Channel " + this.getName() + " failed to remove listener");
				return false;
			}
		}
		catch (SerialPortException e)
		{
			this.logger.error("Channel " + this.getName() + " failed to remove listener :" + e.getMessage(), e);
			return false;
		}

		return true;
	}

	protected void receiveData()
	{
		try
		{
			if (this.available() > 0)
			{
				byte[] buffer = this.read();
				this.notifyOnDataRead(buffer);
			}
		}
		catch (ChannelException exception)
		{
			this.notifyOnErrorDetected(ChannelSerialPortErrorCode.READ_FAILED.getCode(ERROR_CODE_OFFSET), exception.getMessage());
		}
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// PRIVATE METHODS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private boolean hasPortChanged()
	{
		boolean portChanged = false;

		this.currentPortName = this.findPortName();

		if (this.currentPortName != null && !this.currentPortName.equalsIgnoreCase(this.previousPortName))
		{
			portChanged = true;
		}

		return portChanged;
	}

	private void onPortNameChanged()
	{
		String errorMessage = "Port name has changed ( " + this.previousPortName + " --> " + this.currentPortName + " )";
		this.logger.error(errorMessage);
		this.setStatus(ChannelStatus.ERROR);
		this.closePortForced();
		this.notifyOnErrorDetected(ChannelSerialPortErrorCode.PORT_NAME_HAS_CHANGED.getCode(ERROR_CODE_OFFSET), errorMessage);
		this.previousPortName = this.currentPortName;
	}

	private void updateRealPath() throws ChannelException
	{
		if (SystemUtils.IS_OS_LINUX)
		{
			String realPortName;
			try
			{
				Process p = Runtime.getRuntime().exec("realpath " + this.getPortName());
				BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
				realPortName = stdInput.readLine();
				this.getChannelConfiguration().setPortName(realPortName);
			}
			catch (IOException | DataDictionaryException e)
			{
				throw new ChannelException(ChannelException.ERRCODE_INVALID_CONFIGURATION, "Cannot resolve realpath (" + e.getMessage() + ")");
			}
		}
	}

	private ChannelSerialPortConfiguration getChannelConfiguration()
	{
		return (ChannelSerialPortConfiguration) this.configuration;
	}

	protected void openPort()
	{
		/* 1. Check if serial port is already opened */
		if (this.portHandler != null && this.portHandler.isOpened())
		{
			return;
		}
		/* 2. Open serial port */
		String portNameFound = this.findPortName();
		try
		{
			this.logger.info("Channel " + this.getName() + " opening port " + portNameFound);
			if (this.portHandler == null)
			{
				this.portHandler = new SerialPort(portNameFound);
			}
			if (!this.portHandler.openPort())
			{
				this.logger.error("Channel " + this.getName() + " failed to open port " + this.getPortNameOpened() + " (" + SystemError.getMessage() + ") ");
				return;
			}
			this.logger.info("Channel " + this.getName() + " configuring port " + portNameFound);
			if (!this.portHandler.setParams(this.getBaudrate(), this.getDataBits(), stopBitsFromConfiguration(this.getStopBits()), parityFromConfiguration(this.getParity())))
			{
				this.logger.error("Channel " + this.getName() + " failed to configure port " + this.getPortNameOpened() + " (" + SystemError.getMessage() + ") ");
				return;
			}
		}
		catch (SerialPortException exception)
		{
			String errorMessage = "Channel " + this.getName() + " failed to open port " + this.getPortNameOpened() + " : " + exception.getMessage();
			this.logger.error(errorMessage);
			if (exception.getExceptionType().equals(SerialPortException.TYPE_PORT_BUSY))
			{
				this.notifyOnErrorDetected(ChannelSerialPortErrorCode.PORT_BUSY.getCode(ERROR_CODE_OFFSET), errorMessage);
			}
			this.closePortForced();
			return;
		}
		/* 3. Add serial event listener */
		if (this.hasEventListener())
		{
			if (!this.addEventListener())
			{
				return;
			}
		}
		this.setStatus(ChannelStatus.STARTED);
	}

	protected void closePort()
	{
		/* 1. Check if serial port is already closed */
		if (this.portHandler == null || !this.portHandler.isOpened())
		{
			return;
		}
		/* 2. Remove serial event listener */
		if (this.hasEventListener())
		{
			this.removeEventListener();
		}
		/* 3. Close serial port */
		try
		{
			this.logger.info("Channel " + this.getName() + " closing port " + this.getPortNameOpened());
			if (!this.portHandler.closePort())
			{
				this.logger.error("Channel " + this.getName() + " failed to close port " + this.getPortNameOpened() + " (" + SystemError.getMessage() + ") ");
				return;
			}
		}
		catch (SerialPortException exception)
		{
			this.logger.error("Channel " + this.getName() + " failed to close port " + this.getPortNameOpened() + " : " + exception.getMessage());
			return;
		}
		this.setStatus(ChannelStatus.STOPPED);
	}

	protected void closePortForced()
	{
		try
		{
			this.portHandler.closePort();
		}
		catch (SerialPortException exception)
		{
			this.logger.error("Channel " + this.getName() + " fail on close : close " + this.getPortNameOpened() + " failed due to " + exception.getMessage());
		}
		this.portHandler = new SerialPort(this.findPortName());
	}

	protected boolean isPortFound()
	{
		return isPortFound(this.getPortId(), this.getPortName());
	}

	protected boolean isPortOpenedFound()
	{
		String portNameOpened = this.getPortNameOpened();
		if (portNameOpened == null)
		{
			return false;
		}
		String portNameFound = this.findPortName();

		return portNameOpened.equals(portNameFound);
	}

	protected void onPortNotFound()
	{
		String errorMessage = "Channel " + this.getName() + " : serial port " + this.findPortName() + " not found";
		this.logger.error(errorMessage);
		this.setStatus(ChannelStatus.ERROR);
		this.closePortForced();
		this.notifyOnErrorDetected(ChannelSerialPortErrorCode.PORT_NOT_FOUND.getCode(ERROR_CODE_OFFSET), errorMessage);
	}

	protected void onPortOpenedNotFound()
	{
		String errorMessage = "Channel " + this.getName() + " : serial port " + this.getPortNameOpened() + " opened not found";
		this.logger.error(errorMessage);
		this.setStatus(ChannelStatus.ERROR);
		this.closePortForced();
		this.notifyOnErrorDetected(ChannelSerialPortErrorCode.PORT_OPENED_NOT_FOUND.getCode(ERROR_CODE_OFFSET), errorMessage);
	}
}
