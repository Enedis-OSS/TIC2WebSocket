// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.types.datadictionary;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import enedis.lab.types.DataDictionary;
import enedis.lab.types.DataDictionaryException;

/**
 * DataDictionary key descriptor DataDictionary
 * 
 * @param <T>
 */
public class KeyDescriptorDataDictionary<T extends DataDictionaryBase> extends KeyDescriptorBase<T>
{
	private Class<T> dataDictionaryClass;

	/**
	 * Default constructor
	 * 
	 * @param name
	 * @param mandatory
	 * @param dataDictionaryClass
	 */
	public KeyDescriptorDataDictionary(String name, boolean mandatory, Class<T> dataDictionaryClass)
	{
		super(name, mandatory);
		this.dataDictionaryClass = dataDictionaryClass;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T convertValue(Object value) throws DataDictionaryException
	{
		T convertedValue = null;

		if (this.dataDictionaryClass.isAssignableFrom(value.getClass()))
		{
			convertedValue = this.dataDictionaryClass.cast(value);
		}
		else if (value instanceof String)
		{
			convertedValue = (T) DataDictionaryBase.fromString((String) value, this.dataDictionaryClass);
		}
		else if (value instanceof DataDictionary)
		{
			try
			{
				Constructor<T> constructor = this.dataDictionaryClass.getConstructor(DataDictionary.class);
				convertedValue = constructor.newInstance((DataDictionary) value);
			}
			catch (InvocationTargetException e)
			{
				throw new DataDictionaryException("Key " + this.getName() + ": Cannot convert type " + value.getClass().getSimpleName() + " to "
						+ this.dataDictionaryClass.getSimpleName() + " : " + e.getTargetException().getMessage());
			}
			catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException e)
			{
				throw new DataDictionaryException("Key " + this.getName() + ": Cannot convert type " + value.getClass().getSimpleName() + " to "
						+ this.dataDictionaryClass.getSimpleName() + " : " + e.getMessage());
			}
		}
		else if (value instanceof Map<?, ?>)
		{
			try
			{
				Constructor<T> constructor = this.dataDictionaryClass.getConstructor(Map.class);
				convertedValue = constructor.newInstance((Map<?, ?>) value);
			}
			catch (InvocationTargetException e)
			{
				throw new DataDictionaryException("Key " + this.getName() + ": Cannot convert type " + value.getClass().getSimpleName() + " to "
						+ this.dataDictionaryClass.getSimpleName() + " : " + e.getTargetException().getMessage());
			}
			catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException e)
			{
				throw new DataDictionaryException("Key " + this.getName() + ": Cannot convert type " + value.getClass().getSimpleName() + " to "
						+ this.dataDictionaryClass.getSimpleName() + " : " + e.getMessage());
			}
		}
		else
		{
			throw new DataDictionaryException(
					"Key " + this.getName() + ": Cannot convert type " + value.getClass().getSimpleName() + " to " + this.dataDictionaryClass.getSimpleName());
		}

		return convertedValue;
	}

}
