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
 * ChannelSerialPortConfiguration class
 *
 * Generated
 */
public class ChannelSerialPortConfiguration extends ChannelConfiguration
{
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// CONSTANTS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	protected static final String			KEY_PORT_ID						= "portId";
	protected static final String			KEY_PORT_NAME					= "portName";
	protected static final String			KEY_BAUDRATE					= "baudrate";
	protected static final String			KEY_PARITY						= "parity";
	protected static final String			KEY_DATA_BITS					= "dataBits";
	protected static final String			KEY_STOP_BITS					= "stopBits";
	protected static final String			KEY_SYNC_READ_TIMEOUT			= "syncReadTimeout";

	private static final ChannelProtocol	PROTOCOL_ACCEPTED_VALUE			= ChannelProtocol.SERIAL_PORT;
	private static final ChannelDirection	DIRECTION_ACCEPTED_VALUE		= ChannelDirection.RXTX;
	private static final Number[]			BAUDRATE_ACCEPTED_VALUES		= { 110, 300, 1200, 2400, 4800, 9600, 19200, 38400, 57600, 115200, 230400, 460800, 921600, 1843200,
			3686400 };
	private static final Number[]			DATA_BITS_ACCEPTED_VALUES		= { 5, 6, 7, 8 };
	private static final Number[]			STOP_BITS_ACCEPTED_VALUES		= { 1.0d, 1.5d, 2.0d };
	private static final Number				SYNC_READ_TIMEOUT_DEFAULT_VALUE	= 10000;

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

	private List<KeyDescriptor<?>>			keys							= new ArrayList<KeyDescriptor<?>>();

	protected KeyDescriptorString			kPortId;
	protected KeyDescriptorString			kPortName;
	protected KeyDescriptorNumber			kBaudrate;
	protected KeyDescriptorEnum<Parity>		kParity;
	protected KeyDescriptorNumber			kDataBits;
	protected KeyDescriptorNumber			kStopBits;
	protected KeyDescriptorNumber			kSyncReadTimeout;

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// CONSTRUCTORS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	protected ChannelSerialPortConfiguration()
	{
		super();
		this.loadKeyDescriptors();

		this.kProtocol.setAcceptedValues(PROTOCOL_ACCEPTED_VALUE);
		this.kDirection.setAcceptedValues(DIRECTION_ACCEPTED_VALUE);
	}

	/**
	 * Constructor using map
	 *
	 * @param map
	 * @throws DataDictionaryException
	 */
	public ChannelSerialPortConfiguration(Map<String, Object> map) throws DataDictionaryException
	{
		this();
		this.copy(fromMap(map));
	}

	/**
	 * Constructor using datadictionary
	 *
	 * @param other
	 * @throws DataDictionaryException
	 */
	public ChannelSerialPortConfiguration(DataDictionary other) throws DataDictionaryException
	{
		this();
		this.copy(other);
	}

	/**
	 *
	 * Constructor setting configuration name/file and parameters to default values
	 *
	 * @param name
	 *            the configuration name
	 * @param file
	 *            the configuration file
	 */
	public ChannelSerialPortConfiguration(String name, File file)
	{
		this();
		this.init(name, file);
	}

	/**
	 * Constructor setting parameters to specific values
	 *
	 * @param name
	 * @param alias
	 * @param portId
	 * @param portName
	 * @param baudrate
	 * @param parity
	 * @param dataBits
	 * @param stopBits
	 * @param syncReadTimeout
	 * @throws DataDictionaryException
	 */
	public ChannelSerialPortConfiguration(String name, String alias, String portId, String portName, Number baudrate, Parity parity, Number dataBits, Number stopBits,
			Number syncReadTimeout) throws DataDictionaryException
	{
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

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// INTERFACE
	/// ChannelConfiguration
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	protected void updateOptionalParameters() throws DataDictionaryException
	{
		if (!this.exists(KEY_PORT_ID) && !this.exists(KEY_PORT_NAME))
		{
			throw new DataDictionaryException("Neither " + KEY_PORT_ID + " nor " + KEY_PORT_NAME + " is defined");
		}
		if (!this.exists(KEY_PROTOCOL))
		{
			this.setProtocol(PROTOCOL_ACCEPTED_VALUE);
		}
		if (!this.exists(KEY_DIRECTION))
		{
			this.setDirection(DIRECTION_ACCEPTED_VALUE);
		}
		if (!this.exists(KEY_SYNC_READ_TIMEOUT))
		{
			this.setSyncReadTimeout(SYNC_READ_TIMEOUT_DEFAULT_VALUE);
		}
		super.updateOptionalParameters();
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// PUBLIC METHODS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Get port id
	 *
	 * @return the port id
	 */
	public String getPortId()
	{
		return (String) this.data.get(KEY_PORT_ID);
	}

	/**
	 * Get port name
	 *
	 * @return the port name
	 */
	public String getPortName()
	{
		return (String) this.data.get(KEY_PORT_NAME);
	}

	/**
	 * Get baudrate
	 *
	 * @return the baudrate
	 */
	public Number getBaudrate()
	{
		return (Number) this.data.get(KEY_BAUDRATE);
	}

	/**
	 * Get parity
	 *
	 * @return the parity
	 */
	public Parity getParity()
	{
		return (Parity) this.data.get(KEY_PARITY);
	}

	/**
	 * Get data bits
	 *
	 * @return the data bits
	 */
	public Number getDataBits()
	{
		return (Number) this.data.get(KEY_DATA_BITS);
	}

	/**
	 * Get stop bits
	 *
	 * @return the stop bits
	 */
	public Number getStopBits()
	{
		return (Number) this.data.get(KEY_STOP_BITS);
	}

	/**
	 * Get sync read timeout
	 *
	 * @return the sync read timeout
	 */
	public Number getSyncReadTimeout()
	{
		return (Number) this.data.get(KEY_SYNC_READ_TIMEOUT);
	}

	/**
	 * Set port id
	 *
	 * @param portId
	 * @throws DataDictionaryException
	 */
	public void setPortId(String portId) throws DataDictionaryException
	{
		this.setPortId((Object) portId);
	}

	/**
	 * Set port name
	 *
	 * @param portName
	 * @throws DataDictionaryException
	 */
	public void setPortName(String portName) throws DataDictionaryException
	{
		this.setPortName((Object) portName);
	}

	/**
	 * Set baudrate
	 *
	 * @param baudrate
	 * @throws DataDictionaryException
	 */
	public void setBaudrate(Number baudrate) throws DataDictionaryException
	{
		this.setBaudrate((Object) baudrate);
	}

	/**
	 * Set parity
	 *
	 * @param parity
	 * @throws DataDictionaryException
	 */
	public void setParity(Parity parity) throws DataDictionaryException
	{
		this.setParity((Object) parity);
	}

	/**
	 * Set data bits
	 *
	 * @param dataBits
	 * @throws DataDictionaryException
	 */
	public void setDataBits(Number dataBits) throws DataDictionaryException
	{
		this.setDataBits((Object) dataBits);
	}

	/**
	 * Set stop bits
	 *
	 * @param stopBits
	 * @throws DataDictionaryException
	 */
	public void setStopBits(Number stopBits) throws DataDictionaryException
	{
		this.setStopBits((Object) stopBits);
	}

	/**
	 * Set sync read timeout
	 *
	 * @param syncReadTimeout
	 * @throws DataDictionaryException
	 */
	public void setSyncReadTimeout(Number syncReadTimeout) throws DataDictionaryException
	{
		this.setSyncReadTimeout((Object) syncReadTimeout);
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// PROTECTED METHODS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	protected void setPortId(Object portId) throws DataDictionaryException
	{
		this.data.put(KEY_PORT_ID, this.kPortId.convert(portId));
	}

	protected void setPortName(Object portName) throws DataDictionaryException
	{
		this.data.put(KEY_PORT_NAME, this.kPortName.convert(portName));
	}

	protected void setBaudrate(Object baudrate) throws DataDictionaryException
	{
		this.data.put(KEY_BAUDRATE, this.kBaudrate.convert(baudrate));
	}

	protected void setParity(Object parity) throws DataDictionaryException
	{
		this.data.put(KEY_PARITY, this.kParity.convert(parity));
	}

	protected void setDataBits(Object dataBits) throws DataDictionaryException
	{
		this.data.put(KEY_DATA_BITS, this.kDataBits.convert(dataBits));
	}

	protected void setStopBits(Object stopBits) throws DataDictionaryException
	{
		this.data.put(KEY_STOP_BITS, this.kStopBits.convert(stopBits));
	}

	protected void setSyncReadTimeout(Object syncReadTimeout) throws DataDictionaryException
	{
		this.data.put(KEY_SYNC_READ_TIMEOUT, this.kSyncReadTimeout.convert(syncReadTimeout));
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// PRIVATE METHODS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void loadKeyDescriptors()
	{
		try
		{
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
		}
		catch (DataDictionaryException e)
		{
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}
