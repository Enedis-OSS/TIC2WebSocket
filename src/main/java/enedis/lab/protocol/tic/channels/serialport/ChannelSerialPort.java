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
 * Serial port communication channel implementation for TIC protocol.
 *
 * <p>This class extends {@link ChannelPhysical} and implements {@link SerialPortEventListener} to provide
 * a robust serial port communication channel. It handles automatic port discovery, configuration,
 * connection management, and real-time data transmission for TIC-compliant devices.
 *
 * <p>Key features include:
 * <ul>
 *   <li>Automatic port detection and reconnection when devices are plugged/unplugged</li>
 *   <li>Support for both port ID and port name identification methods</li>
 *   <li>Configurable serial communication parameters (baud rate, parity, data bits, stop bits)</li>
 *   <li>Event-driven data reception with optional timeout handling</li>
 *   <li>Symbolic link resolution for Linux systems</li>
 *   <li>Error detection and automatic recovery mechanisms</li>
 * </ul>
 *
 * <p>The channel operates in a periodic polling mode to monitor port availability and automatically
 * handles port changes, busy conditions, and communication errors. It supports both synchronous
 * and asynchronous data operations with proper exception handling.
 *
 * @author Enedis Smarties team
 * @see ChannelPhysical
 * @see ChannelSerialPortConfiguration
 * @see SerialPortEventListener
 * @see ChannelStatus
 * @see ChannelSerialPortErrorCode
 */
public class ChannelSerialPort extends ChannelPhysical implements SerialPortEventListener
{
	/** Error code offset for serial port specific errors. */
	protected static final int	ERROR_CODE_OFFSET	= 1000;
	
	/** Default polling delay in milliseconds for port monitoring. */
	private static final int	DEFAULT_DELAY		= 500;
	
	/** Extended delay in milliseconds for port reopening attempts. */
	private static final int	DELAY_REOPEN		= DEFAULT_DELAY * 10;


	/**
	 * Checks if a serial port is available on the system.
	 *
	 * <p>This method queries the system to determine if a port with the specified identifier
	 * or name is currently available. The port ID takes precedence over port name if both
	 * are provided.
	 *
	 * @param portId the unique port identifier (may be null)
	 * @param portName the port name or path (may be null)
	 * @return true if the port is found and available, false otherwise
	 * @see SerialPortFinderBase#findByPortIdOrPortName(String, String)
	 */
	public static boolean isPortFound(String portId, String portName)
	{
		return SerialPortFinderBase.getInstance().findByPortIdOrPortName(portId, portName) != null;
	}

	/**
	 * Finds the actual port name corresponding to the given port ID or port name.
	 *
	 * <p>This method resolves the port identifier to its actual system port name. The port ID
	 * parameter takes priority over the port name parameter if both are provided. This is useful
	 * for converting abstract port identifiers to concrete system paths.
	 *
	 * @param portId the unique port identifier (has priority if not null)
	 * @param portName the port name or path (used as fallback if portId is null)
	 * @return the resolved port name, or null if no matching port is found
	 * @see SerialPortFinderBase#findByPortId(String)
	 * @see SerialPortFinderBase#findByPortName(String)
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
	 * Converts a {@link Parity} enumeration value to the corresponding JSSC library constant.
	 *
	 * <p>This utility method maps the application-level parity enumeration to the underlying
	 * serial communication library constants required for port configuration.
	 *
	 * @param parity the parity setting from the configuration
	 * @return the corresponding JSSC SerialPort parity constant, or -1 for unsupported values
	 * @see SerialPort#PARITY_EVEN
	 * @see SerialPort#PARITY_ODD
	 * @see SerialPort#PARITY_NONE
	 * @see SerialPort#PARITY_MARK
	 * @see SerialPort#PARITY_SPACE
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
	 * Converts a stop bits configuration value to the corresponding JSSC library constant.
	 *
	 * <p>This utility method maps the configured stop bits value (1.0, 1.5, or 2.0) to the
	 * underlying serial communication library constants required for port configuration.
	 *
	 * @param stop_bits the stop bits value from the configuration (1.0, 1.5, or 2.0)
	 * @return the corresponding JSSC SerialPort stop bits constant, or -1 for unsupported values
	 * @see SerialPort#STOPBITS_1
	 * @see SerialPort#STOPBITS_1_5
	 * @see SerialPort#STOPBITS_2
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

	/** JSSC SerialPort instance for handling low-level serial communication. */
	private SerialPort	portHandler	= null;

	/** Previously detected port name, used for change detection. */
	private String		previousPortName;
	
	/** Currently detected port name, used for change detection. */
	private String		currentPortName;

	/**
	 * Constructs a new serial port channel with the specified configuration.
	 *
	 * <p>This constructor initializes the channel with the provided configuration and sets up
	 * the initial port detection. The channel will automatically resolve the port name based
	 * on the configuration's port ID or port name settings.
	 *
	 * <p>The constructor sets up the periodic polling mechanism with a default delay and
	 * initializes the port change detection system.
	 *
	 * @param configuration the channel configuration containing port settings and parameters
	 * @throws ChannelException if the configuration is invalid or port cannot be resolved
	 * @throws IllegalArgumentException if configuration is null or not a ChannelSerialPortConfiguration
	 * @see ChannelSerialPortConfiguration
	 */
	public ChannelSerialPort(ChannelConfiguration configuration) throws ChannelException
	{
		super(configuration);
		this.setPeriod(DEFAULT_DELAY);

		this.currentPortName = this.findPortName();
		this.previousPortName = this.currentPortName;
	}


	/**
	 * Starts the serial port channel and begins communication.
	 *
	 * <p>This method initiates the channel by opening the serial port connection and starting
	 * the underlying periodic task. The port is opened with the configuration parameters
	 * specified during construction.
	 *
	 * <p>If the port cannot be opened, the channel will enter an error state and attempt
	 * to reconnect periodically.
	 *
	 * @see #stop()
	 * @see #openPort()
	 */
	@Override
	public void start()
	{
		this.logger.info("Channel " + this.getName() + " start (" + this.findPortName() + ")");
		this.openPort();
		super.start();
	}

	/**
	 * Stops the serial port channel and closes the connection.
	 *
	 * <p>This method gracefully stops the channel by first stopping the underlying periodic task
	 * and then closing the serial port connection. All event listeners are removed and the
	 * port is properly released.
	 *
	 * <p>The channel can be restarted by calling {@link #start()} again.
	 *
	 * @see #start()
	 * @see #closePort()
	 */
	@Override
	public void stop()
	{
		this.logger.info("Channel " + this.getName() + " stop (" + this.findPortName() + ")");
		super.stop();
		this.closePort();
	}

	/**
	 * Configures the channel with a new configuration.
	 *
	 * <p>This method validates and applies a new configuration to the channel. The configuration
	 * must be a valid {@link ChannelSerialPortConfiguration} instance. If the channel is currently
	 * started, it will be stopped and restarted with the new configuration.
	 *
	 * <p>The configuration update includes port parameter changes, which may require reopening
	 * the serial port connection.
	 *
	 * @param configuration the new channel configuration to apply
	 * @throws ChannelException if the configuration is null, invalid, or not a serial port configuration
	 * @see ChannelSerialPortConfiguration
	 */
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

	/**
	 * Reads available data from the serial port.
	 *
	 * <p>This method performs a non-blocking read operation to retrieve all available data
	 * from the serial port input buffer. The method returns immediately with whatever data
	 * is currently available, or an empty array if no data is present.
	 *
	 * <p>The channel must be in a STARTED state and the port must be open for this operation
	 * to succeed.
	 *
	 * @return array of bytes read from the port, or empty array if no data available
	 * @throws ChannelException if the channel is not ready, port is not open, or read operation fails
	 * @see #read(int)
	 * @see #read(int, int)
	 */
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

	/**
	 * Writes data to the serial port.
	 *
	 * <p>This method transmits the provided data bytes to the serial port. The operation
	 * is synchronous and will block until all data has been written to the port buffer.
	 *
	 * <p>The channel must be in a STARTED state and the port must be open for this operation
	 * to succeed.
	 *
	 * @param data the byte array to write to the port (must not be null)
	 * @throws ChannelException if the channel is not ready, port is not open, or write operation fails
	 * @throws IllegalArgumentException if data parameter is null
	 */
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

	/**
	 * Returns a copy of the current channel configuration.
	 *
	 * <p>This method returns a defensive copy of the channel's configuration to prevent
	 * external modification of the internal state. The returned configuration contains
	 * all current settings including port identification, communication parameters,
	 * and timeout values.
	 *
	 * @return a copy of the current channel configuration
	 * @see ChannelSerialPortConfiguration
	 */
	@Override
	public ChannelSerialPortConfiguration getConfiguration()
	{
		return (ChannelSerialPortConfiguration) this.configuration.clone();
	}


	/**
	 * Sets up the channel with updated configuration.
	 *
	 * <p>This protected method handles the internal setup process when the channel configuration
	 * is updated. It saves the current status, stops the channel, updates the real path for
	 * symbolic links on Linux systems, creates a new port handler if needed, and restores
	 * the previous state.
	 *
	 * <p>This method ensures that configuration changes are applied atomically and that the
	 * channel state is properly maintained throughout the update process.
	 *
	 * @throws ChannelException if the setup process fails or configuration is invalid
	 * @see #updateRealPath()
	 */
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


	/**
	 * Handles serial port events from the JSSC library.
	 *
	 * <p>This method is called by the JSSC library when serial port events occur. Currently,
	 * it only handles RXCHAR events (data received) by triggering the data reception process.
	 * Other event types are ignored as they are not relevant for TIC communication.
	 *
	 * <p>The event handling is designed to be lightweight and delegates the actual data
	 * processing to the {@link #receiveData()} method.
	 *
	 * @param event the serial port event that occurred
	 * @see SerialPortEvent#RXCHAR
	 * @see #receiveData()
	 */
	@Override
	public void serialEvent(SerialPortEvent event)
	{
		switch (event.getEventType())
		{
			case SerialPortEvent.RXCHAR:
				this.receiveData();
				break;
			default: /* No action for other event types */
		}
	}


	/**
	 * Main processing loop for the serial port channel.
	 *
	 * <p>This method is called periodically to monitor the port status and handle various
	 * scenarios including port detection, connection management, and data reception.
	 *
	 * <p>When the channel is STARTED, it:
	 * <ul>
	 *   <li>Checks if the port is still available and handles port not found scenarios</li>
	 *   <li>Detects port name changes (e.g., due to USB device reconnection)</li>
	 *   <li>Receives data if no event listener is configured (polling mode)</li>
	 * </ul>
	 *
	 * <p>When the channel is not STARTED, it attempts to open the port if it becomes
	 * available, with extended delay on error conditions.
	 *
	 * <p>The processing delay is dynamically adjusted based on the current state and
	 * error conditions to optimize performance and responsiveness.
	 *
	 * @see #onPortNotFound()
	 * @see #onPortNameChanged()
	 * @see #receiveData()
	 * @see #openPort()
	 */
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


	/**
	 * Performs a blocking read operation for a specific number of bytes.
	 *
	 * <p>This method reads exactly the specified number of bytes from the serial port.
	 * The operation will block until the requested number of bytes is available or
	 * until the operation times out.
	 *
	 * <p>The channel must be in a STARTED state and the port must be open for this
	 * operation to succeed.
	 *
	 * @param bytesCount the exact number of bytes to read
	 * @return array containing the read bytes, or null if the read operation fails
	 * @throws ChannelException if the channel is not ready, port is not open, or read operation fails
	 * @throws IllegalArgumentException if bytesCount is negative or zero
	 * @see #read()
	 * @see #read(int, int)
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
	 * Performs a blocking read operation with a specified timeout.
	 *
	 * <p>This method reads exactly the specified number of bytes from the serial port
	 * within the given timeout period. If the timeout expires before the requested
	 * number of bytes is available, a timeout exception is thrown.
	 *
	 * <p>The channel must be in a STARTED state and the port must be open for this
	 * operation to succeed.
	 *
	 * @param bytesCount the exact number of bytes to read
	 * @param timeout the timeout in milliseconds for the read operation
	 * @return array containing the read bytes, or null if the read operation fails
	 * @throws ChannelException if the channel is not ready, port is not open, read operation fails, or timeout occurs
	 * @throws IllegalArgumentException if bytesCount is negative or zero, or timeout is negative
	 * @see #read()
	 * @see #read(int)
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
	 * Returns the number of bytes available for reading from the serial port.
	 *
	 * <p>This method queries the serial port input buffer to determine how many bytes
	 * are currently available for reading. This information is useful for determining
	 * whether data is available before performing a read operation.
	 *
	 * <p>The channel must be in a STARTED state and the port must be open for this
	 * operation to succeed.
	 *
	 * @return the number of bytes available in the input buffer
	 * @throws ChannelException if the channel is not ready, port is not open, or the operation fails
	 * @see #read()
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
	 * Flushes all input and output buffers of the serial port.
	 *
	 * <p>This method clears both the input and output buffers of the serial port,
	 * discarding any pending data. This is useful for ensuring a clean communication
	 * state, particularly after errors or before starting a new communication session.
	 *
	 * <p>The flush operation includes clearing receive buffers, transmit buffers,
	 * and aborting any pending operations.
	 *
	 * <p>The channel must be in a STARTED state and the port must be open for this
	 * operation to succeed.
	 *
	 * @throws ChannelException if the channel is not ready, port is not open, or flush operation fails
	 * @see SerialPort#PURGE_RXCLEAR
	 * @see SerialPort#PURGE_TXCLEAR
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
	 * Returns the port identifier from the channel configuration.
	 *
	 * <p>The port ID is an alternative way to identify serial ports, typically used when
	 * the system provides unique identifiers for ports. This identifier takes precedence
	 * over the port name when both are specified.
	 *
	 * @return the port identifier, or null if not set in the configuration
	 * @see ChannelSerialPortConfiguration#getPortId()
	 * @see #getPortName()
	 */
	public String getPortId()
	{
		return this.getChannelConfiguration().getPortId();
	}

	/**
	 * Returns the port name from the channel configuration.
	 *
	 * <p>The port name is the system path or name used to identify the serial port.
	 * This is typically a device path like "/dev/ttyUSB0" on Linux or "COM1" on Windows.
	 *
	 * @return the port name, or null if not set in the configuration
	 * @see ChannelSerialPortConfiguration#getPortName()
	 * @see #getPortId()
	 */
	public String getPortName()
	{
		return this.getChannelConfiguration().getPortName();
	}

	/**
	 * Returns the name of the currently opened serial port.
	 *
	 * <p>This method returns the actual port name that is currently opened by the JSSC
	 * library. This may differ from the configured port name if symbolic links were
	 * resolved or if the port was dynamically assigned.
	 *
	 * @return the name of the opened port, or null if no port is currently open
	 * @see #getPortName()
	 * @see #getPortId()
	 */
	public String getPortNameOpened()
	{
		return (this.portHandler != null) ? this.portHandler.getPortName() : null;
	}

	/**
	 * Finds the port name associated with the current channel configuration.
	 *
	 * <p>This method resolves the port name based on the configuration's port ID or
	 * port name settings. It uses the same resolution logic as the static
	 * {@link #findPortName(String, String)} method but operates on the current
	 * configuration.
	 *
	 * @return the resolved port name, or null if no matching port is found
	 * @see #findPortName(String, String)
	 * @see #getPortId()
	 * @see #getPortName()
	 */
	public String findPortName()
	{
		return findPortName(this.getPortId(), this.getPortName());
	}

	/**
	 * Returns the current baud rate configured for the serial port.
	 *
	 * <p>The baud rate determines the speed of serial communication in bits per second.
	 *
	 * @return the configured baud rate value
	 * @see ChannelSerialPortConfiguration#getBaudrate()
	 */
	public int getBaudrate()
	{
		return this.getChannelConfiguration().getBaudrate().intValue();
	}

	/**
	 * Returns the number of data bits configured for serial communication.
	 *
	 * <p>Data bits define the number of bits used to represent each character transmitted.
	 * Standard values are 7 (for ASCII) or 8 (for binary data). TIC protocol typically
	 * uses 7 data bits.
	 *
	 * @return the number of data bits (5, 6, 7, or 8)
	 * @see ChannelSerialPortConfiguration#getDataBits()
	 */
	public int getDataBits()
	{
		return this.getChannelConfiguration().getDataBits().intValue();
	}

	/**
	 * Returns the number of stop bits configured for serial communication.
	 *
	 * <p>Stop bits mark the end of each data frame. Standard values are 1.0, 1.5, or 2.0.
	 * Most serial communications use 1.0 stop bits.
	 *
	 * @return the number of stop bits (1.0, 1.5, or 2.0)
	 * @see ChannelSerialPortConfiguration#getStopBits()
	 */
	public float getStopBits()
	{
		return this.getChannelConfiguration().getStopBits().floatValue();
	}

	/**
	 * Returns the parity setting configured for serial communication.
	 *
	 * <p>Parity is used for error detection by adding an extra bit to each data frame.
	 * TIC protocol typically uses EVEN parity for reliable data transmission.
	 *
	 * @return the parity setting from the configuration
	 * @see Parity
	 * @see ChannelSerialPortConfiguration#getParity()
	 */
	public Parity getParity()
	{
		return this.getChannelConfiguration().getParity();
	}

	/**
	 * Returns the timeout value for synchronous read operations.
	 *
	 * <p>This timeout determines how long a synchronous read operation will wait for
	 * data before timing out. The value is specified in milliseconds.
	 *
	 * @return the timeout value in milliseconds for synchronous read operations
	 * @see ChannelSerialPortConfiguration#getSyncReadTimeout()
	 * @see #read(int, int)
	 */
	public int getSyncReadTimeout()
	{
		return this.getChannelConfiguration().getSyncReadTimeout().intValue();
	}


	/**
	 * Determines if the channel has an event listener configured.
	 *
	 * <p>This method indicates whether the channel is configured to use event-driven
	 * data reception (via SerialPortEventListener) or polling-based reception.
	 *
	 * <p>The base implementation returns false, indicating polling mode. Subclasses
	 * can override this method to enable event-driven mode.
	 *
	 * @return true if event listener is configured, false for polling mode
	 * @see #addEventListener()
	 * @see #removeEventListener()
	 */
	protected boolean hasEventListener()
	{
		return false;
	}

	/**
	 * Adds a serial port event listener for asynchronous data reception.
	 *
	 * <p>This method configures the serial port to generate events when data is received,
	 * control signals change, or other port events occur. It sets up the event mask to
	 * listen for RXCHAR (data received), CTS (clear to send), and DSR (data set ready)
	 * events.
	 *
	 * <p>The channel instance is registered as the event listener, so {@link #serialEvent(SerialPortEvent)}
	 * will be called when events occur.
	 *
	 * @return true if the event listener was successfully added, false otherwise
	 * @see #removeEventListener()
	 * @see #serialEvent(SerialPortEvent)
	 * @see SerialPort#MASK_RXCHAR
	 */
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

	/**
	 * Removes the serial port event listener.
	 *
	 * <p>This method unregisters the channel instance as an event listener from the
	 * serial port. After calling this method, the channel will no longer receive
	 * asynchronous events and will operate in polling mode.
	 *
	 * <p>This method is typically called when stopping the channel or switching to
	 * polling-based data reception.
	 *
	 * @return true if the event listener was successfully removed, false otherwise
	 * @see #addEventListener()
	 * @see #hasEventListener()
	 */
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

	/**
	 * Receives and processes data from the serial port.
	 *
	 * <p>This method checks for available data in the serial port input buffer and
	 * reads it if present. The received data is then passed to registered listeners
	 * via the notification system.
	 *
	 * <p>If no data is available, the method returns immediately without error.
	 * If a read error occurs, it notifies listeners with an appropriate error code.
	 *
	 * <p>This method is called both from the periodic processing loop (polling mode)
	 * and from the serial event handler (event-driven mode).
	 *
	 * @see #read()
	 * @see #available()
	 * @see #notifyOnDataRead(byte[])
	 * @see ChannelSerialPortErrorCode#READ_FAILED
	 */
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


	/**
	 * Checks if the port name has changed since the last check.
	 *
	 * <p>This method compares the currently detected port name with the previously
	 * detected port name to determine if the port has changed. This typically occurs
	 * when a USB device is unplugged and plugged back in, causing the system to assign
	 * a new device path.
	 *
	 * <p>The comparison is case-insensitive to handle variations in how the system
	 * reports port names.
	 *
	 * @return true if the port name has changed, false otherwise
	 * @see #onPortNameChanged()
	 */
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

	/**
	 * Handles the event when the port name has changed.
	 *
	 * <p>This method is called when a port name change is detected. It logs the change,
	 * sets the channel status to ERROR, forcibly closes the current port connection,
	 * notifies listeners of the error, and updates the previous port name for future
	 * comparisons.
	 *
	 * <p>Port name changes typically occur when USB devices are reconnected and the
	 * system assigns a new device path (e.g., /dev/ttyUSB0 → /dev/ttyUSB1).
	 *
	 * @see #hasPortChanged()
	 * @see ChannelSerialPortErrorCode#PORT_NAME_HAS_CHANGED
	 */
	private void onPortNameChanged()
	{
		String errorMessage = "Port name has changed ( " + this.previousPortName + " --> " + this.currentPortName + " )";
		this.logger.error(errorMessage);
		this.setStatus(ChannelStatus.ERROR);
		this.closePortForced();
		this.notifyOnErrorDetected(ChannelSerialPortErrorCode.PORT_NAME_HAS_CHANGED.getCode(ERROR_CODE_OFFSET), errorMessage);
		this.previousPortName = this.currentPortName;
	}

	/**
	 * Updates the port name to its real path by resolving symbolic links.
	 *
	 * <p>This method is only executed on Linux systems to resolve symbolic links to their
	 * actual device paths. This is important for USB devices that may be represented
	 * by symbolic links in /dev/disk/by-id/ or similar locations.
	 *
	 * <p>The method uses the system 'realpath' command to resolve the symbolic link
	 * and updates the channel configuration with the resolved path.
	 *
	 * @throws ChannelException if the realpath resolution fails or configuration update fails
	 * @see SystemUtils#IS_OS_LINUX
	 */
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

	/**
	 * Returns the channel configuration cast to the specific serial port configuration type.
	 *
	 * <p>This helper method provides type-safe access to the serial port specific
	 * configuration without requiring explicit casting throughout the code.
	 *
	 * @return the channel configuration as a ChannelSerialPortConfiguration instance
	 */
	private ChannelSerialPortConfiguration getChannelConfiguration()
	{
		return (ChannelSerialPortConfiguration) this.configuration;
	}

	/**
	 * Opens and configures the serial port connection.
	 *
	 * <p>This method performs the complete process of opening a serial port connection:
	 * <ol>
	 *   <li>Checks if the port is already open to avoid duplicate operations</li>
	 *   <li>Creates a new SerialPort handler if needed</li>
	 *   <li>Opens the port using the resolved port name</li>
	 *   <li>Configures the port with the specified communication parameters</li>
	 *   <li>Adds event listener if configured for event-driven mode</li>
	 *   <li>Sets the channel status to STARTED</li>
	 * </ol>
	 *
	 * <p>If any step fails, the method logs the error, handles specific error conditions
	 * (like PORT_BUSY), and ensures the channel enters an appropriate error state.
	 *
	 * @see #closePort()
	 * @see #findPortName()
	 * @see #hasEventListener()
	 * @see #addEventListener()
	 * @see ChannelSerialPortErrorCode#PORT_BUSY
	 */
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

	/**
	 * Closes the serial port connection gracefully.
	 *
	 * <p>This method performs the complete process of closing a serial port connection:
	 * <ol>
	 *   <li>Checks if the port is already closed to avoid duplicate operations</li>
	 *   <li>Removes event listener if one was configured</li>
	 *   <li>Closes the port and releases system resources</li>
	 *   <li>Sets the channel status to STOPPED</li>
	 * </ol>
	 *
	 * <p>If any step fails, the method logs the error but continues with the remaining
	 * cleanup steps to ensure the channel is in a consistent state.
	 *
	 * @see #openPort()
	 * @see #closePortForced()
	 * @see #hasEventListener()
	 * @see #removeEventListener()
	 */
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

	/**
	 * Forcefully closes the serial port and resets the port handler.
	 *
	 * <p>This method performs an emergency closure of the serial port connection without
	 * the normal cleanup procedures. It attempts to close the port and then creates a
	 * new SerialPort handler instance for future use.
	 *
	 * <p>This method is typically used in error conditions where the normal close
	 * procedure may fail or when the port needs to be forcibly reset.
	 *
	 * <p>Unlike {@link #closePort()}, this method does not remove event listeners or
	 * update the channel status, making it suitable for error recovery scenarios.
	 *
	 * @see #closePort()
	 * @see #findPortName()
	 */
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

	/**
	 * Checks if the configured port is currently available on the system.
	 *
	 * <p>This method uses the current channel configuration to determine if the
	 * specified port is available. It delegates to the static {@link #isPortFound(String, String)}
	 * method with the current port ID and port name.
	 *
	 * @return true if the configured port is found and available, false otherwise
	 * @see #isPortFound(String, String)
	 * @see #getPortId()
	 * @see #getPortName()
	 */
	protected boolean isPortFound()
	{
		return isPortFound(this.getPortId(), this.getPortName());
	}

	/**
	 * Checks if the currently opened port matches the expected port.
	 *
	 * <p>This method compares the name of the currently opened port with the port name
	 * that should be opened according to the current configuration. This is useful
	 * for detecting situations where the port has changed or become unavailable.
	 *
	 * @return true if the opened port matches the expected port, false otherwise
	 * @see #getPortNameOpened()
	 * @see #findPortName()
	 */
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

	/**
	 * Handles the event when the configured port is not found.
	 *
	 * <p>This method is called when the system cannot locate the configured serial port.
	 * It logs the error, sets the channel status to ERROR, forcibly closes any existing
	 * connection, and notifies listeners of the error condition.
	 *
	 * <p>This typically occurs when a USB device is unplugged or the device driver
	 * is not properly installed.
	 *
	 * @see #isPortFound()
	 * @see ChannelSerialPortErrorCode#PORT_NOT_FOUND
	 */
	protected void onPortNotFound()
	{
		String errorMessage = "Channel " + this.getName() + " : serial port " + this.findPortName() + " not found";
		this.logger.error(errorMessage);
		this.setStatus(ChannelStatus.ERROR);
		this.closePortForced();
		this.notifyOnErrorDetected(ChannelSerialPortErrorCode.PORT_NOT_FOUND.getCode(ERROR_CODE_OFFSET), errorMessage);
	}

	/**
	 * Handles the event when the currently opened port is no longer found.
	 *
	 * <p>This method is called when the system can no longer locate the port that was
	 * previously opened by the channel. This indicates that the device has been
	 * physically disconnected or the system has lost access to it.
	 *
	 * <p>It logs the error, sets the channel status to ERROR, forcibly closes the
	 * connection, and notifies listeners of the error condition.
	 *
	 * @see #isPortOpenedFound()
	 * @see ChannelSerialPortErrorCode#PORT_OPENED_NOT_FOUND
	 */
	protected void onPortOpenedNotFound()
	{
		String errorMessage = "Channel " + this.getName() + " : serial port " + this.getPortNameOpened() + " opened not found";
		this.logger.error(errorMessage);
		this.setStatus(ChannelStatus.ERROR);
		this.closePortForced();
		this.notifyOnErrorDetected(ChannelSerialPortErrorCode.PORT_OPENED_NOT_FOUND.getCode(ERROR_CODE_OFFSET), errorMessage);
	}
}
