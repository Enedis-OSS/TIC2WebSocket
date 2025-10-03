// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.types.datadictionary;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import enedis.lab.types.DataDictionaryException;

/**
 * DataDictionary key descriptor LocalDateTime
 */
public class KeyDescriptorLocalDateTime extends KeyDescriptorBase<LocalDateTime>
{
	/** Default pattern used */
	public static final String				DEFAULT_PATTERN		= "dd/MM/yyyy HH:mm:ss";
	/** Default formatter used */
	public static final DateTimeFormatter	DEFAULT_FORMATTER	= DateTimeFormatter.ofPattern(DEFAULT_PATTERN);

	private DateTimeFormatter				formatter;

	/**
	 * Default constructor
	 *
	 * @param name
	 * @param mandatory
	 */
	public KeyDescriptorLocalDateTime(String name, boolean mandatory)
	{
		this(name, mandatory, DEFAULT_PATTERN);
	}

	/**
	 * Default constructor
	 *
	 * @param name
	 * @param mandatory
	 * @param formatterPattern
	 */
	public KeyDescriptorLocalDateTime(String name, boolean mandatory, String formatterPattern)
	{
		super(name, mandatory);
		this.formatter = DateTimeFormatter.ofPattern(formatterPattern);
	}

	@Override
	public String toString(LocalDateTime value)
	{
		if (value == null)
		{
			return null;
		}

		return this.formatter.format(value);
	}

	@Override
	public LocalDateTime convertValue(Object value) throws DataDictionaryException
	{
		LocalDateTime convertedValue = null;

		if (value instanceof LocalDateTime)
		{
			convertedValue = (LocalDateTime) value;
		}
		else if (value instanceof String)
		{
			convertedValue = this.toLocalDateTime((String) value);
		}
		else
		{
			throw new DataDictionaryException("Key " + this.getName() + ": Cannot convert type " + value.getClass().getSimpleName() + " to LocalDateTime");
		}

		return convertedValue;
	}

	private LocalDateTime toLocalDateTime(String value) throws DataDictionaryException
	{
		LocalDateTime convertedValue = null;
		try
		{
			convertedValue = LocalDateTime.parse(value, this.formatter);
		}
		catch (DateTimeParseException e)
		{
			throw new DataDictionaryException("Key " + this.getName() + ": string " + value + " cannot be converted to LocalDateTime");
		}
		return convertedValue;
	}

}
