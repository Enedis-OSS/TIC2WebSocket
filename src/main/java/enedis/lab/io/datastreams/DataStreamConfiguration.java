// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
// 
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.io.datastreams;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import enedis.lab.types.DataDictionary;
import enedis.lab.types.DataDictionaryException;
import enedis.lab.types.configuration.ConfigurationBase;
import enedis.lab.types.datadictionary.KeyDescriptor;
import enedis.lab.types.datadictionary.KeyDescriptorEnum;
import enedis.lab.types.datadictionary.KeyDescriptorString;

/**
 * DataStreamConfiguration class
 * 
 * Generated
 */
public class DataStreamConfiguration extends ConfigurationBase
{
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// CONSTANTS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	protected static final String						KEY_NAME			= "name";
	protected static final String						KEY_TYPE			= "type";
	protected static final String						KEY_DIRECTION		= "direction";
	protected static final String						KEY_CHANNEL_NAME	= "channelName";

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

	private List<KeyDescriptor<?>>						keys				= new ArrayList<KeyDescriptor<?>>();

	protected KeyDescriptorString						kName;
	protected KeyDescriptorEnum<DataStreamType>			kType;
	protected KeyDescriptorEnum<DataStreamDirection>	kDirection;
	protected KeyDescriptorString						kChannelName;

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// CONSTRUCTORS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	protected DataStreamConfiguration()
	{
		super();
		this.loadKeyDescriptors();

	}

	/**
	 * Constructor using map
	 *
	 * @param map
	 * @throws DataDictionaryException
	 */
	public DataStreamConfiguration(Map<String, Object> map) throws DataDictionaryException
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
	public DataStreamConfiguration(DataDictionary other) throws DataDictionaryException
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
	public DataStreamConfiguration(String name, File file)
	{
		this();
		this.init(name, file);
	}

	/**
	 * Constructor setting parameters to specific values
	 *
	 * @param name
	 * @param type
	 * @param direction
	 * @param channelName
	 * @throws DataDictionaryException
	 */
	public DataStreamConfiguration(String name, DataStreamType type, DataStreamDirection direction, String channelName) throws DataDictionaryException
	{
		this();

		this.setName(name);
		this.setType(type);
		this.setDirection(direction);
		this.setChannelName(channelName);

		this.checkAndUpdate();
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// INTERFACE 
	/// ConfigurationBase
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	protected void updateOptionalParameters() throws DataDictionaryException
	{
		super.updateOptionalParameters();
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// PUBLIC METHODS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Get name
	 *
	 * @return the name
	 */
	public String getName()
	{
		return (String) this.data.get(KEY_NAME);
	}

	/**
	 * Get type
	 *
	 * @return the type
	 */
	public DataStreamType getType()
	{
		return (DataStreamType) this.data.get(KEY_TYPE);
	}

	/**
	 * Get direction
	 *
	 * @return the direction
	 */
	public DataStreamDirection getDirection()
	{
		return (DataStreamDirection) this.data.get(KEY_DIRECTION);
	}

	/**
	 * Get channel name
	 *
	 * @return the channel name
	 */
	public String getChannelName()
	{
		return (String) this.data.get(KEY_CHANNEL_NAME);
	}

	/**
	 * Set name
	 *
	 * @param name
	 * @throws DataDictionaryException
	 */
	public void setName(String name) throws DataDictionaryException
	{
		this.setName((Object) name);
	}

	/**
	 * Set type
	 *
	 * @param type
	 * @throws DataDictionaryException
	 */
	public void setType(DataStreamType type) throws DataDictionaryException
	{
		this.setType((Object) type);
	}

	/**
	 * Set direction
	 *
	 * @param direction
	 * @throws DataDictionaryException
	 */
	public void setDirection(DataStreamDirection direction) throws DataDictionaryException
	{
		this.setDirection((Object) direction);
	}

	/**
	 * Set channel name
	 *
	 * @param channelName
	 * @throws DataDictionaryException
	 */
	public void setChannelName(String channelName) throws DataDictionaryException
	{
		this.setChannelName((Object) channelName);
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// PROTECTED METHODS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	protected void setName(Object name) throws DataDictionaryException
	{
		this.data.put(KEY_NAME, this.kName.convert(name));
	}

	protected void setType(Object type) throws DataDictionaryException
	{
		this.data.put(KEY_TYPE, this.kType.convert(type));
	}

	protected void setDirection(Object direction) throws DataDictionaryException
	{
		this.data.put(KEY_DIRECTION, this.kDirection.convert(direction));
	}

	protected void setChannelName(Object channelName) throws DataDictionaryException
	{
		this.data.put(KEY_CHANNEL_NAME, this.kChannelName.convert(channelName));
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
			this.kName = new KeyDescriptorString(KEY_NAME, true, false);
			this.keys.add(this.kName);

			this.kType = new KeyDescriptorEnum<DataStreamType>(KEY_TYPE, true, DataStreamType.class);
			this.keys.add(this.kType);

			this.kDirection = new KeyDescriptorEnum<DataStreamDirection>(KEY_DIRECTION, true, DataStreamDirection.class);
			this.keys.add(this.kDirection);

			this.kChannelName = new KeyDescriptorString(KEY_CHANNEL_NAME, false, false);
			this.keys.add(this.kChannelName);

			this.addAllKeyDescriptor(this.keys);
		}
		catch (DataDictionaryException e)
		{
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}
