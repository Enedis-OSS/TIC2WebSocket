// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.util.message;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import enedis.lab.types.DataDictionary;
import enedis.lab.types.DataDictionaryException;
import enedis.lab.types.datadictionary.KeyDescriptor;
import enedis.lab.types.datadictionary.KeyDescriptorLocalDateTime;

/**
 * Event class
 * 
 * Generated
 */
public abstract class Event extends Message
{
	protected static final String			KEY_DATE_TIME		= "dateTime";

	private static final MessageType		TYPE_ACCEPTED_VALUE	= MessageType.EVENT;

	private List<KeyDescriptor<?>>			keys				= new ArrayList<KeyDescriptor<?>>();

	protected KeyDescriptorLocalDateTime	kDateTime;

	protected Event()
	{
		super();
		this.loadKeyDescriptors();

		this.kType.setAcceptedValues(TYPE_ACCEPTED_VALUE);
	}

	/**
	 * Constructor using map
	 *
	 * @param map
	 * @throws DataDictionaryException
	 */
	public Event(Map<String, Object> map) throws DataDictionaryException
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
	public Event(DataDictionary other) throws DataDictionaryException
	{
		this();
		this.copy(other);
	}

	/**
	 * Constructor setting parameters to specific values
	 *
	 * @param name
	 * @param dateTime
	 * @throws DataDictionaryException
	 */
	public Event(String name, LocalDateTime dateTime) throws DataDictionaryException
	{
		this();

		this.setName(name);
		this.setDateTime(dateTime);

		this.checkAndUpdate();
	}

	@Override
	protected void updateOptionalParameters() throws DataDictionaryException
	{
		if (!this.exists(KEY_TYPE))
		{
			this.setType(TYPE_ACCEPTED_VALUE);
		}
		super.updateOptionalParameters();
	}

	/**
	 * Get date time
	 *
	 * @return the date time
	 */
	public LocalDateTime getDateTime()
	{
		return (LocalDateTime) this.data.get(KEY_DATE_TIME);
	}

	/**
	 * Set date time
	 *
	 * @param dateTime
	 * @throws DataDictionaryException
	 */
	public void setDateTime(LocalDateTime dateTime) throws DataDictionaryException
	{
		this.setDateTime((Object) dateTime);
	}

	protected void setDateTime(Object dateTime) throws DataDictionaryException
	{
		this.data.put(KEY_DATE_TIME, this.kDateTime.convert(dateTime));
	}

	private void loadKeyDescriptors()
	{
		try
		{
			this.kDateTime = new KeyDescriptorLocalDateTime(KEY_DATE_TIME, true);
			this.keys.add(this.kDateTime);

			this.addAllKeyDescriptor(this.keys);
		}
		catch (DataDictionaryException e)
		{
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}
