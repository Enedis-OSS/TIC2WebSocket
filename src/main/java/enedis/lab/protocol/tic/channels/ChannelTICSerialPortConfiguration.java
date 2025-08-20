// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.protocol.tic.channels;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import enedis.lab.io.channels.serialport.ChannelSerialPortConfiguration;
import enedis.lab.io.channels.serialport.Parity;
import enedis.lab.protocol.tic.TICMode;
import enedis.lab.types.DataDictionary;
import enedis.lab.types.DataDictionaryException;
import enedis.lab.types.datadictionary.KeyDescriptor;
import enedis.lab.types.datadictionary.KeyDescriptorEnum;

/**
 * ChannelTICSerialPortConfiguration class
 *
 * Generated
 */
public class ChannelTICSerialPortConfiguration extends ChannelSerialPortConfiguration
{
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// CONSTANTS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	protected static final String			KEY_TIC_MODE				= "ticMode";

	protected static final Number			BAUDRATE_HISTORIC			= 1200;
	protected static final Number			BAUDRATE_STANDARD			= 9600;
	protected static final Number[]			BAUDRATE_ACCEPTED_VALUES	= { BAUDRATE_HISTORIC, BAUDRATE_STANDARD };
	protected static final Parity			PARITY_ACCEPTED_VALUE		= Parity.EVEN;
	protected static final Number			DATA_BITS_ACCEPTED_VALUE	= 7;
	protected static final Number			STOP_BITS_ACCEPTED_VALUE	= 1.0d;
	protected static final TICMode			TIC_MODE_DEFAULT_VALUE		= TICMode.AUTO;

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

	private List<KeyDescriptor<?>>			keys						= new ArrayList<KeyDescriptor<?>>();

	protected KeyDescriptorEnum<TICMode>	kTicMode;

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// CONSTRUCTORS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	protected ChannelTICSerialPortConfiguration()
	{
		super();
		this.loadKeyDescriptors();

		this.kBaudrate.setAcceptedValues(BAUDRATE_ACCEPTED_VALUES);
		this.kParity.setAcceptedValues(PARITY_ACCEPTED_VALUE);
		this.kDataBits.setAcceptedValues(DATA_BITS_ACCEPTED_VALUE);
		this.kStopBits.setAcceptedValues(STOP_BITS_ACCEPTED_VALUE);
	}

	/**
	 * Constructor using map
	 *
	 * @param map
	 * @throws DataDictionaryException
	 */
	public ChannelTICSerialPortConfiguration(Map<String, Object> map) throws DataDictionaryException
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
	public ChannelTICSerialPortConfiguration(DataDictionary other) throws DataDictionaryException
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
	public ChannelTICSerialPortConfiguration(String name, File file)
	{
		this();
		this.init(name, file);
	}

	/**
	 * Constructor setting parameters to specific values
	 *
	 * @param name
	 * @param portName
	 * @param ticMode
	 * @throws DataDictionaryException
	 */
	public ChannelTICSerialPortConfiguration(String name, String portName, TICMode ticMode) throws DataDictionaryException
	{
		this();

		this.setName(name);
		this.setPortName(portName);
		this.setTicMode(ticMode);

		this.checkAndUpdate();
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// INTERFACE
	/// ChannelSerialPortConfiguration
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	protected void updateOptionalParameters() throws DataDictionaryException
	{
		if (!this.exists(KEY_TIC_MODE))
		{
			this.setTicMode(TIC_MODE_DEFAULT_VALUE);
		}
		switch (this.getTicMode())
		{
			case HISTORIC:
			{
				this.setBaudrate(BAUDRATE_HISTORIC);
				this.setParity(PARITY_ACCEPTED_VALUE);
				this.setDataBits(DATA_BITS_ACCEPTED_VALUE);
				this.setStopBits(STOP_BITS_ACCEPTED_VALUE);
				break;
			}
			case STANDARD:
			case AUTO:
			default:
			{
				this.setBaudrate(BAUDRATE_STANDARD);
				this.setParity(PARITY_ACCEPTED_VALUE);
				this.setDataBits(DATA_BITS_ACCEPTED_VALUE);
				this.setStopBits(STOP_BITS_ACCEPTED_VALUE);
			}
		}
		super.updateOptionalParameters();
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// PUBLIC METHODS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Get tic mode
	 *
	 * @return the tic mode
	 */
	public TICMode getTicMode()
	{
		return (TICMode) this.data.get(KEY_TIC_MODE);
	}

	/**
	 * Set tic mode
	 *
	 * @param ticMode
	 * @throws DataDictionaryException
	 */
	public void setTicMode(TICMode ticMode) throws DataDictionaryException
	{
		this.setTicMode((Object) ticMode);
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// PROTECTED METHODS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	protected void setTicMode(Object ticMode) throws DataDictionaryException
	{
		this.data.put(KEY_TIC_MODE, this.kTicMode.convert(ticMode));
		switch (this.getTicMode())
		{
			case HISTORIC:
				this.setBaudrate(BAUDRATE_HISTORIC);
				break;
			case STANDARD:
			case AUTO:
				this.setBaudrate(BAUDRATE_STANDARD);
				break;
			default:
				break;
		}
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
			this.kTicMode = new KeyDescriptorEnum<TICMode>(KEY_TIC_MODE, true, TICMode.class);
			this.keys.add(this.kTicMode);

			this.addAllKeyDescriptor(this.keys);
		}
		catch (DataDictionaryException e)
		{
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}
