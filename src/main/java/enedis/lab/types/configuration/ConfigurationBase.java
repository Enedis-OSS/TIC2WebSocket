// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
// 
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.types.configuration;

import java.io.File;
import java.util.Map;

import enedis.lab.types.DataDictionary;
import enedis.lab.types.DataDictionaryException;
import enedis.lab.types.datadictionary.DataDictionaryBase;

/**
 * Basic implementation of Configuration
 */
public class ConfigurationBase extends DataDictionaryBase implements Configuration
{
	private String	name	= null;
	private File	file	= null;

	protected ConfigurationBase()
	{
		super();
	}

	/**
	 * Constructor using datadictionary
	 * 
	 * @param other
	 * @throws DataDictionaryException
	 */
	public ConfigurationBase(DataDictionary other) throws DataDictionaryException
	{
		super(other);
	}

	/**
	 * Constructor using Map
	 * 
	 * @param map
	 * @throws DataDictionaryException
	 */
	public ConfigurationBase(Map<String, Object> map) throws DataDictionaryException
	{
		super(map);
	}

	/**
	 * Constructor using name and file
	 * 
	 * @param name
	 * @param file
	 */
	public ConfigurationBase(String name, File file)
	{
		super();
		this.init(name, file);
	}

	@Override
	public void load() throws ConfigurationException
	{
		DataDictionary configuration;

		try
		{
			configuration = DataDictionaryBase.fromFile(this.file, this.getClass());
		}
		catch (Exception exception)
		{
			throw new ConfigurationException("Cannot load configuration '" + ((this.name == null) ? "" : this.name) + "' from file '" + ((this.file == null) ? "" : this.file)
					+ "' (" + exception.getMessage() + ") " + exception.getClass().getSimpleName());
		}
		try
		{
			this.copy(configuration);
		}
		catch (Exception exception)
		{
			throw new ConfigurationException("Cannot copy configuration '" + ((this.name == null) ? "" : this.name) + "' from file '" + ((this.file == null) ? "" : this.file)
					+ "' (" + exception.getMessage() + ")");
		}
	}

	@Override
	public void save() throws ConfigurationException
	{
		try
		{
			this.toFile(this.file, 2);
		}
		catch (Exception exception)
		{
			throw new ConfigurationException("Cannot save configuration '" + ((this.name == null) ? "" : this.name) + "' to file '" + ((this.file == null) ? "" : this.file) + "' ("
					+ exception.getMessage() + ")");
		}
	}

	@Override
	public String getConfigName()
	{
		return this.name;
	}

	@Override
	public File getConfigFile()
	{
		return this.file;
	}

	/**
	 * Set config name
	 * 
	 * @param configName
	 */
	public void setConfigName(String configName)
	{
		this.name = configName;
	}

	protected void init(String name, File file)
	{
		this.name = name;
		this.file = file;
	}

}
