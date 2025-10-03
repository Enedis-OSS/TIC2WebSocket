// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
// 
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.types.datadictionary;

import java.util.Arrays;
import java.util.List;

import enedis.lab.types.DataDictionaryException;

/**
 * DataDictionary key descriptor base
 *
 * @param <T>
 */
public abstract class KeyDescriptorBase<T> implements KeyDescriptor<T>
{
	private String		name;
	private boolean		mandatory;
	protected List<T>	acceptedValues;

	/**
	 * Constructor setting all attributes
	 *
	 * @param name
	 * @param mandatory
	 */
	public KeyDescriptorBase(String name, boolean mandatory)
	{
		super();
		this.name = name;
		this.mandatory = mandatory;
	}

	@Override
	public final T convert(Object value) throws DataDictionaryException
	{
		T convertedValue = null;

		if (value == null)
		{
			this.handleNullValue();
			return null;
		}

		convertedValue = this.convertValue(value);

		this.checkAcceptedValues(convertedValue);

		return convertedValue;
	}

	@Override
	public String toString(T value)
	{
		if (value == null)
		{
			return null;
		}

		return value.toString();
	}

	/**
	 * Get key name
	 *
	 * @return key name
	 */
	@Override
	public String getName()
	{
		return this.name;
	}

	/**
	 * Set key name
	 *
	 * @param name
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Get mandatory flag
	 *
	 * @return mandatory flag
	 */
	@Override
	public boolean isMandatory()
	{
		return this.mandatory;
	}

	/**
	 * Set mandatory flag
	 *
	 * @param mandatory
	 */
	@Override
	public void setMandatory(boolean mandatory)
	{
		this.mandatory = mandatory;
	}

	/**
	 * Set mandatory value
	 *
	 * @param acceptedValues
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setAcceptedValues(T... acceptedValues)
	{
		this.acceptedValues = Arrays.asList(acceptedValues);
	}

	protected abstract T convertValue(Object value) throws DataDictionaryException;

	protected final void handleNullValue() throws DataDictionaryException
	{
		if (this.isMandatory())
		{
			throw new DataDictionaryException("Cannot set null " + this.getName());
		}
	}

	protected void checkAcceptedValues(T value) throws DataDictionaryException
	{
		if (this.acceptedValues != null)
		{
			boolean accepted = false;

			for (T v : this.acceptedValues)
			{
				if (v.equals(value))
				{
					accepted = true;
					break;
				}
			}

			if (!accepted)
			{
				throw new DataDictionaryException("Key " + this.getName() + " doesn't respect mandatory value");
			}
		}
	}

}
