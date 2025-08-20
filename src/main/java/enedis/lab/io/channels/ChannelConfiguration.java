// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.io.channels;

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
 * ChannelConfiguration class
 *
 * Generated
 */
public class ChannelConfiguration extends ConfigurationBase
{
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// CONSTANTS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	protected static final String					KEY_NAME		= "name";
	protected static final String					KEY_PROTOCOL	= "protocol";
	protected static final String					KEY_DIRECTION	= "direction";
	protected static final String					KEY_ALIAS		= "alias";

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

	private List<KeyDescriptor<?>>					keys			= new ArrayList<KeyDescriptor<?>>();

	protected KeyDescriptorString					kName;
	protected KeyDescriptorEnum<ChannelProtocol>	kProtocol;
	protected KeyDescriptorEnum<ChannelDirection>	kDirection;
	protected KeyDescriptorString					kAlias;

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// CONSTRUCTORS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	protected ChannelConfiguration()
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
	public ChannelConfiguration(Map<String, Object> map) throws DataDictionaryException
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
	public ChannelConfiguration(DataDictionary other) throws DataDictionaryException
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
	public ChannelConfiguration(String name, File file)
	{
		this();
		this.init(name, file);
	}

	/**
	 * Constructor setting parameters to specific values
	 *
	 * @param name
	 * @param protocol
	 * @param direction
	 * @param alias
	 * @throws DataDictionaryException
	 */
	public ChannelConfiguration(String name, ChannelProtocol protocol, ChannelDirection direction, String alias) throws DataDictionaryException
	{
		this();

		this.setName(name);
		this.setProtocol(protocol);
		this.setDirection(direction);
		this.setAlias(alias);

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
	 * Get protocol
	 *
	 * @return the protocol
	 */
	public ChannelProtocol getProtocol()
	{
		return (ChannelProtocol) this.data.get(KEY_PROTOCOL);
	}

	/**
	 * Get direction
	 *
	 * @return the direction
	 */
	public ChannelDirection getDirection()
	{
		return (ChannelDirection) this.data.get(KEY_DIRECTION);
	}

	/**
	 * Get alias
	 *
	 * @return the alias
	 */
	public String getAlias()
	{
		return (String) this.data.get(KEY_ALIAS);
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
	 * Set protocol
	 *
	 * @param protocol
	 * @throws DataDictionaryException
	 */
	public void setProtocol(ChannelProtocol protocol) throws DataDictionaryException
	{
		this.setProtocol((Object) protocol);
	}

	/**
	 * Set direction
	 *
	 * @param direction
	 * @throws DataDictionaryException
	 */
	public void setDirection(ChannelDirection direction) throws DataDictionaryException
	{
		this.setDirection((Object) direction);
	}

	/**
	 * Set alias
	 *
	 * @param alias
	 * @throws DataDictionaryException
	 */
	public void setAlias(String alias) throws DataDictionaryException
	{
		this.setAlias((Object) alias);
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

	protected void setProtocol(Object protocol) throws DataDictionaryException
	{
		this.data.put(KEY_PROTOCOL, this.kProtocol.convert(protocol));
	}

	protected void setDirection(Object direction) throws DataDictionaryException
	{
		this.data.put(KEY_DIRECTION, this.kDirection.convert(direction));
	}

	protected void setAlias(Object alias) throws DataDictionaryException
	{
		this.data.put(KEY_ALIAS, this.kAlias.convert(alias));
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

			this.kProtocol = new KeyDescriptorEnum<ChannelProtocol>(KEY_PROTOCOL, true, ChannelProtocol.class);
			this.keys.add(this.kProtocol);

			this.kDirection = new KeyDescriptorEnum<ChannelDirection>(KEY_DIRECTION, true, ChannelDirection.class);
			this.keys.add(this.kDirection);

			this.kAlias = new KeyDescriptorString(KEY_ALIAS, false, false);
			this.keys.add(this.kAlias);

			this.addAllKeyDescriptor(this.keys);
		}
		catch (DataDictionaryException e)
		{
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}
