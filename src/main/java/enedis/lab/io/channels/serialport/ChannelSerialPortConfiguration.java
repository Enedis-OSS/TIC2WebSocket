// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
// 
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.io.channels.serialport;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import enedis.lab.io.channels.ChannelConfiguration;
import enedis.lab.io.channels.ChannelDirection;
import enedis.lab.io.channels.ChannelProtocol;
import enedis.lab.types.DataDictionary;
import enedis.lab.types.DataDictionaryException;
import enedis.lab.types.datadictionary.KeyDescriptor;
import enedis.lab.types.datadictionary.KeyDescriptorEnum;
import enedis.lab.types.datadictionary.KeyDescriptorNumber;
import enedis.lab.types.datadictionary.KeyDescriptorString;

/**
 * Configuration class for serial port communication channels.
 * <p>
 * This class extends {@link ChannelConfiguration} and provides specific configuration
 * parameters for serial port communication, including port identification, baud rate,
 * parity settings, data bits, stop bits, and timeout configurations.
 * <p>
 * The configuration supports both port ID and port name identification methods,
 * with validation for standard serial communication parameters.
 * <p>
 * Supported baud rates: 110, 300, 1200, 2400, 4800, 9600, 19200, 38400, 57600,
 * 115200, 230400, 460800, 921600, 1843200, 3686400
 * <p>
 * Supported data bits: 5, 6, 7, 8
 * <p>
 * Supported stop bits: 1.0, 1.5, 2.0
 * <p>
 * Default sync read timeout: 10000 milliseconds
 *
 * @author Enedis Smarties team
 * @see ChannelConfiguration
 * @see ChannelProtocol#SERIAL_PORT
 * @see ChannelDirection#RXTX
 */
public class ChannelSerialPortConfiguration extends ChannelConfiguration {

	/** Key for the serial port identifier configuration parameter. */
	protected static final String KEY_PORT_ID = "portId";
	/** Key for the serial port name configuration parameter. */
	protected static final String KEY_PORT_NAME = "portName";
	/** Key for the baud rate configuration parameter. */
	protected static final String KEY_BAUDRATE = "baudrate";
	/** Key for the parity configuration parameter. */
	protected static final String KEY_PARITY = "parity";
	/** Key for the data bits configuration parameter. */
	protected static final String KEY_DATA_BITS = "dataBits";
	/** Key for the stop bits configuration parameter. */
	protected static final String KEY_STOP_BITS = "stopBits";
	/** Key for the synchronous read timeout configuration parameter. */
	protected static final String KEY_SYNC_READ_TIMEOUT = "syncReadTimeout";

	/** Accepted protocol value for serial port channels. */
	private static final ChannelProtocol PROTOCOL_ACCEPTED_VALUE = ChannelProtocol.SERIAL_PORT;
	/** Accepted direction value for serial port channels (bidirectional). */
	private static final ChannelDirection DIRECTION_ACCEPTED_VALUE = ChannelDirection.RXTX;
	/** Array of supported baud rate values for serial communication. */
	private static final Number[] BAUDRATE_ACCEPTED_VALUES = { 110, 300, 1200, 2400, 4800, 9600, 19200, 38400, 57600,
			115200, 230400, 460800, 921600, 1843200, 3686400 };
	/** Array of supported data bits values (5, 6, 7, or 8 bits). */
	private static final Number[] DATA_BITS_ACCEPTED_VALUES = { 5, 6, 7, 8 };
	/** Array of supported stop bits values (1.0, 1.5, or 2.0). */
	private static final Number[] STOP_BITS_ACCEPTED_VALUES = { 1.0d, 1.5d, 2.0d };
	/** Default timeout value for synchronous read operations (10000 milliseconds). */
	private static final Number SYNC_READ_TIMEOUT_DEFAULT_VALUE = 10000;

	/** List of key descriptors for configuration validation. */
	private List<KeyDescriptor<?>> keys = new ArrayList<KeyDescriptor<?>>();

	/** Key descriptor for port ID validation. */
	protected KeyDescriptorString kPortId;
	/** Key descriptor for port name validation. */
	protected KeyDescriptorString kPortName;
	/** Key descriptor for baud rate validation. */
	protected KeyDescriptorNumber kBaudrate;
	/** Key descriptor for parity validation. */
	protected KeyDescriptorEnum<Parity> kParity;
	/** Key descriptor for data bits validation. */
	protected KeyDescriptorNumber kDataBits;
	/** Key descriptor for stop bits validation. */
	protected KeyDescriptorNumber kStopBits;
	/** Key descriptor for sync read timeout validation. */
	protected KeyDescriptorNumber kSyncReadTimeout;

	/**
	 * Default constructor for serial port configuration.
	 * <p>
	 * Initializes the configuration with default values and sets up
	 * the key descriptors for parameter validation.
	 */
	protected ChannelSerialPortConfiguration() {
		super();
		this.loadKeyDescriptors();

		this.kProtocol.setAcceptedValues(PROTOCOL_ACCEPTED_VALUE);
		this.kDirection.setAcceptedValues(DIRECTION_ACCEPTED_VALUE);
	}

	/**
	 * Constructs a new serial port configuration from a map of key-value pairs.
	 * <p>
	 * This constructor initializes the configuration with values from the provided map,
	 * performing validation and setting default values for missing parameters.
	 *
	 * @param map the map containing configuration parameters
	 * @throws DataDictionaryException if the map contains invalid values or required
	 *         parameters are missing
	 */
	public ChannelSerialPortConfiguration(Map<String, Object> map) throws DataDictionaryException {
		this();
		this.copy(fromMap(map));
	}

	/**
	 * Constructs a new serial port configuration by copying from another data dictionary.
	 * <p>
	 * This constructor creates a new configuration instance by copying all parameters
	 * from the provided data dictionary, ensuring consistency between configurations.
	 *
	 * @param other the data dictionary to copy configuration from
	 * @throws DataDictionaryException if the source data dictionary contains invalid
	 *         values or is incompatible with serial port configuration
	 */
	public ChannelSerialPortConfiguration(DataDictionary other) throws DataDictionaryException {
		this();
		this.copy(other);
	}

	/**
	 * Constructs a new serial port configuration with a name and file reference,
	 * initializing all parameters to their default values.
	 * <p>
	 *
	 * @param name the configuration name
	 * @param file the configuration file associated with this configuration
	 */
	public ChannelSerialPortConfiguration(String name, File file) {
		this();
		this.init(name, file);
	}

	/**
	 * Constructs a new serial port configuration with all parameters set to specific values.
	 * <p>
	 * This constructor provides a convenient way to create a fully configured serial port
	 * channel with all necessary parameters specified at construction time.
	 *
	 * @param name the configuration name
	 * @param alias the configuration alias
	 * @param portId the serial port identifier (alternative to portName)
	 * @param portName the serial port name (alternative to portId)
	 * @param baudrate the communication baud rate
	 * @param parity the parity setting for data validation
	 * @param dataBits the number of data bits per frame
	 * @param stopBits the number of stop bits
	 * @param syncReadTimeout the timeout for synchronous read operations in milliseconds
	 * @throws DataDictionaryException if any of the provided values are invalid or
	 *         incompatible with serial port communication requirements
	 */
	public ChannelSerialPortConfiguration(String name, String alias, String portId, String portName, Number baudrate,
			Parity parity, Number dataBits, Number stopBits,
			Number syncReadTimeout) throws DataDictionaryException {
		this();

		this.setName(name);
		this.setAlias(alias);
		this.setPortId(portId);
		this.setPortName(portName);
		this.setBaudrate(baudrate);
		this.setParity(parity);
		this.setDataBits(dataBits);
		this.setStopBits(stopBits);
		this.setSyncReadTimeout(syncReadTimeout);

		this.checkAndUpdate();
	}

	/**
	 * Updates optional parameters with default values if not already set.
	 * <p>
	 * This method ensures that either port ID or port name is specified,
	 * and sets default values for protocol, direction, and sync read timeout
	 * if they are not already configured.
	 *
	 * @throws DataDictionaryException if neither port ID nor port name is defined,
	 *         or if there are validation errors with the configuration
	 */
	@Override
	protected void updateOptionalParameters() throws DataDictionaryException {
		if (!this.exists(KEY_PORT_ID) && !this.exists(KEY_PORT_NAME)) {
			throw new DataDictionaryException("Neither " + KEY_PORT_ID + " nor " + KEY_PORT_NAME + " is defined");
		}
		if (!this.exists(KEY_PROTOCOL)) {
			this.setProtocol(PROTOCOL_ACCEPTED_VALUE);
		}
		if (!this.exists(KEY_DIRECTION)) {
			this.setDirection(DIRECTION_ACCEPTED_VALUE);
		}
		if (!this.exists(KEY_SYNC_READ_TIMEOUT)) {
			this.setSyncReadTimeout(SYNC_READ_TIMEOUT_DEFAULT_VALUE);
		}
		super.updateOptionalParameters();
	}

	/**
	 * Retrieves the serial port identifier.
	 * <p>
	 * The port ID is an alternative way to identify the serial port,
	 * typically used when the system provides a unique identifier for the port.
	 *
	 * @return the port identifier, or null if not set
	 */
	public String getPortId() {
		return (String) this.data.get(KEY_PORT_ID);
	}

	/**
	 * Retrieves the serial port name.
	 * <p>
	 * The port name is typically a system-specific identifier such as
	 * "/dev/ttyUSB0" on Unix systems or "COM1" on Windows systems.
	 *
	 * @return the port name, or null if not set
	 */
	public String getPortName() {
		return (String) this.data.get(KEY_PORT_NAME);
	}

	/**
	 * Retrieves the communication baud rate.
	 * <p>
	 * The baud rate determines the speed of data transmission over the serial port.
	 *
	 * @return the baud rate in bits per second, or null if not set
	 */
	public Number getBaudrate() {
		return (Number) this.data.get(KEY_BAUDRATE);
	}

	/**
	 * Retrieves the parity setting for data validation.
	 * <p>
	 * Parity is used for error detection in serial communication.
	 * It can be NONE, ODD, or EVEN depending on the communication protocol.
	 *
	 * @return the parity setting, or null if not set
	 */
	public Parity getParity() {
		return (Parity) this.data.get(KEY_PARITY);
	}

	/**
	 * Retrieves the number of data bits per frame.
	 * <p>
	 * Data bits determine how many bits are used to represent each character
	 * in the serial communication. Common values are 7 or 8 bits.
	 *
	 * @return the number of data bits, or null if not set
	 */
	public Number getDataBits() {
		return (Number) this.data.get(KEY_DATA_BITS);
	}

	/**
	 * Retrieves the number of stop bits.
	 * <p>
	 * Stop bits are used to signal the end of a data frame in serial communication.
	 *
	 * @return the number of stop bits, or null if not set
	 */
	public Number getStopBits() {
		return (Number) this.data.get(KEY_STOP_BITS);
	}

	/**
	 * Retrieves the timeout for synchronous read operations.
	 * <p>
	 * This timeout determines how long the system will wait for data to be
	 * available on the serial port before timing out the read operation.
	 *
	 * @return the timeout value in milliseconds, or null if not set
	 */
	public Number getSyncReadTimeout() {
		return (Number) this.data.get(KEY_SYNC_READ_TIMEOUT);
	}

	/**
	 * Sets the serial port identifier.
	 * <p>
	 * The port ID provides an alternative way to identify the serial port,
	 * typically used when the system provides a unique identifier for the port.
	 * Either port ID or port name must be specified for a valid configuration.
	 *
	 * @param portId the port identifier to set
	 * @throws DataDictionaryException if the port ID is invalid or conflicts
	 *         with other configuration parameters
	 */
	public void setPortId(String portId) throws DataDictionaryException {
		this.setPortId((Object) portId);
	}

	/**
	 * Sets the serial port name.
	 * <p>
	 * The port name is typically a system-specific identifier such as
	 * "/dev/ttyUSB0" on Unix systems or "COM1" on Windows systems.
	 * Either port ID or port name must be specified for a valid configuration.
	 *
	 * @param portName the port name to set
	 * @throws DataDictionaryException if the port name is invalid or conflicts
	 *         with other configuration parameters
	 */
	public void setPortName(String portName) throws DataDictionaryException {
		this.setPortName((Object) portName);
	}

	/**
	 * Sets the communication baud rate.
	 * <p>
	 * The baud rate determines the speed of data transmission over the serial port.
	 * Must be one of the supported values: {@link #BAUDRATE_ACCEPTED_VALUES}.
	 *
	 * @param baudrate the baud rate in bits per second
	 * @throws DataDictionaryException if the baud rate is not supported or invalid
	 */
	public void setBaudrate(Number baudrate) throws DataDictionaryException {
		this.setBaudrate((Object) baudrate);
	}

	/**
	 * Sets the parity setting for data validation.
	 * <p>
	 * Parity is used for error detection in serial communication.
	 * The parity setting must be one of the supported {@link Parity} enum values.
	 *
	 * @param parity the parity setting to use
	 * @throws DataDictionaryException if the parity value is invalid or not supported
	 */
	public void setParity(Parity parity) throws DataDictionaryException {
		this.setParity((Object) parity);
	}

	/**
	 * Sets the number of data bits per frame.
	 * <p>
	 * Data bits determine how many bits are used to represent each character
	 * in the serial communication. Must be one of: {@link #DATA_BITS_ACCEPTED_VALUES}.
	 *
	 * @param dataBits the number of data bits to use
	 * @throws DataDictionaryException if the data bits value is not supported or invalid
	 */
	public void setDataBits(Number dataBits) throws DataDictionaryException {
		this.setDataBits((Object) dataBits);
	}

	/**
	 * Sets the number of stop bits.
	 * <p>
	 * Stop bits are used to signal the end of a data frame in serial communication.
	 * Must be one of: {@link #STOP_BITS_ACCEPTED_VALUES}.
	 *
	 * @param stopBits the number of stop bits to use
	 * @throws DataDictionaryException if the stop bits value is not supported or invalid
	 */
	public void setStopBits(Number stopBits) throws DataDictionaryException {
		this.setStopBits((Object) stopBits);
	}

	/**
	 * Sets the timeout for synchronous read operations.
	 * <p>
	 * This timeout determines how long the system will wait for data to be
	 * available on the serial port before timing out the read operation.
	 * If not set, defaults to {@link #SYNC_READ_TIMEOUT_DEFAULT_VALUE}.
	 *
	 * @param syncReadTimeout the timeout value in milliseconds
	 * @throws DataDictionaryException if the timeout value is invalid or negative
	 */
	public void setSyncReadTimeout(Number syncReadTimeout) throws DataDictionaryException {
		this.setSyncReadTimeout((Object) syncReadTimeout);
	}

	/**
	 * Internal method to set the port ID with object conversion.
	 *
	 * @param portId the port ID object to convert and set
	 * @throws DataDictionaryException if the conversion fails
	 */
	protected void setPortId(Object portId) throws DataDictionaryException {
		this.data.put(KEY_PORT_ID, this.kPortId.convert(portId));
	}

	/**
	 * Internal method to set the port name with object conversion.
	 *
	 * @param portName the port name object to convert and set
	 * @throws DataDictionaryException if the conversion fails
	 */
	protected void setPortName(Object portName) throws DataDictionaryException {
		this.data.put(KEY_PORT_NAME, this.kPortName.convert(portName));
	}

	/**
	 * Internal method to set the baud rate with object conversion.
	 *
	 * @param baudrate the baud rate object to convert and set
	 * @throws DataDictionaryException if the conversion fails
	 */
	protected void setBaudrate(Object baudrate) throws DataDictionaryException {
		this.data.put(KEY_BAUDRATE, this.kBaudrate.convert(baudrate));
	}

	/**
	 * Internal method to set the parity with object conversion.
	 *
	 * @param parity the parity object to convert and set
	 * @throws DataDictionaryException if the conversion fails
	 */
	protected void setParity(Object parity) throws DataDictionaryException {
		this.data.put(KEY_PARITY, this.kParity.convert(parity));
	}

	/**
	 * Internal method to set the data bits with object conversion.
	 *
	 * @param dataBits the data bits object to convert and set
	 * @throws DataDictionaryException if the conversion fails
	 */
	protected void setDataBits(Object dataBits) throws DataDictionaryException {
		this.data.put(KEY_DATA_BITS, this.kDataBits.convert(dataBits));
	}

	/**
	 * Internal method to set the stop bits with object conversion.
	 *
	 * @param stopBits the stop bits object to convert and set
	 * @throws DataDictionaryException if the conversion fails
	 */
	protected void setStopBits(Object stopBits) throws DataDictionaryException {
		this.data.put(KEY_STOP_BITS, this.kStopBits.convert(stopBits));
	}

	/**
	 * Internal method to set the sync read timeout with object conversion.
	 *
	 * @param syncReadTimeout the sync read timeout object to convert and set
	 * @throws DataDictionaryException if the conversion fails
	 */
	protected void setSyncReadTimeout(Object syncReadTimeout) throws DataDictionaryException {
		this.data.put(KEY_SYNC_READ_TIMEOUT, this.kSyncReadTimeout.convert(syncReadTimeout));
	}

	/**
	 * Initializes and configures all key descriptors for parameter validation.
	 * <p>
	 * This method sets up the validation rules for all serial port configuration
	 * parameters, including accepted values and required/optional status.
	 *
	 * @throws RuntimeException if there is an error during key descriptor initialization
	 */
	private void loadKeyDescriptors() {
		try {
			this.kPortId = new KeyDescriptorString(KEY_PORT_ID, false, false);
			this.keys.add(this.kPortId);

			this.kPortName = new KeyDescriptorString(KEY_PORT_NAME, false, false);
			this.keys.add(this.kPortName);

			this.kBaudrate = new KeyDescriptorNumber(KEY_BAUDRATE, true);
			this.kBaudrate.setAcceptedValues(BAUDRATE_ACCEPTED_VALUES);
			this.keys.add(this.kBaudrate);

			this.kParity = new KeyDescriptorEnum<Parity>(KEY_PARITY, true, Parity.class);
			this.keys.add(this.kParity);

			this.kDataBits = new KeyDescriptorNumber(KEY_DATA_BITS, true);
			this.kDataBits.setAcceptedValues(DATA_BITS_ACCEPTED_VALUES);
			this.keys.add(this.kDataBits);

			this.kStopBits = new KeyDescriptorNumber(KEY_STOP_BITS, true);
			this.kStopBits.setAcceptedValues(STOP_BITS_ACCEPTED_VALUES);
			this.keys.add(this.kStopBits);

			this.kSyncReadTimeout = new KeyDescriptorNumber(KEY_SYNC_READ_TIMEOUT, false);
			this.keys.add(this.kSyncReadTimeout);

			this.addAllKeyDescriptor(this.keys);
		} catch (DataDictionaryException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}
