// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
// 
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.types.datadictionary;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import enedis.lab.types.DataDictionary;
import enedis.lab.types.DataDictionaryException;

/**
 * DataDictionary key descriptor List
 * 
 * @param <T>
 */
public class KeyDescriptorList<T> extends KeyDescriptorBase<List<T>>
{
	private Class<T> itemClass;

	/**
	 * Default constructor
	 * 
	 * @param name
	 * @param mandatory
	 * @param itemClass
	 */
	public KeyDescriptorList(String name, boolean mandatory, Class<T> itemClass)
	{
		super(name, mandatory);
		this.itemClass = itemClass;
	}

	@Override
	public List<T> convertValue(Object value) throws DataDictionaryException
	{
		List<T> convertedValue = new ArrayList<T>();

		if (this.itemClass.isAssignableFrom(value.getClass()))
		{
			T convertedItem = this.convertItem(value);
			convertedValue.add(convertedItem);
		}
		else if (value instanceof HashMap<?, ?>)
		{
			T convertedItem = this.convertItem(value);
			convertedValue.add(convertedItem);
		}
		else if (value instanceof List<?>)
		{
			for (Object item : (List<?>) value)
			{
				T convertedItem = this.convertItem(item);
				convertedValue.add(convertedItem);
			}
		}
		else if (value instanceof Object[])
		{
			for (Object item : (Object[]) value)
			{
				T convertedItem = this.convertItem(item);
				convertedValue.add(convertedItem);
			}
		}
		else
		{
			throw new DataDictionaryException(
					"Key " + this.getName() + ": Cannot convert type " + value.getClass().getSimpleName() + " to List<" + this.itemClass.getSimpleName() + ">");
		}
		return convertedValue;
	}

	private T convertItem(Object item) throws DataDictionaryException
	{
		T convertedItem = null;
		if (this.itemClass.isAssignableFrom(item.getClass()))
		{
			convertedItem = this.itemClass.cast(item);
		}
		else if (DataDictionary.class.isAssignableFrom(this.itemClass))
		{
			convertedItem = this.convertDataDictionaryItem(item);
		}
		else if (Enum.class.isAssignableFrom(this.itemClass))
		{
			convertedItem = this.convertEnumItem(item);
		}
		else
		{
			throw new DataDictionaryException(
					"Key " + this.getName() + ": at least on item isn't a " + this.itemClass.getSimpleName() + ", type received :" + item.getClass().getSimpleName());
		}
		return convertedItem;
	}

	private T convertDataDictionaryItem(Object item) throws DataDictionaryException
	{
		T convertedItem = null;
		if (item instanceof JSONObject)
		{
			JSONObject itemJsonObject = (JSONObject) item;
			try
			{
				Constructor<T> constructor = this.itemClass.getConstructor(Map.class);
				convertedItem = (T) constructor.newInstance(itemJsonObject.toMap());
			}
			catch (SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | NoSuchMethodException e)
			{
				e.printStackTrace();
			}
			catch (InvocationTargetException e)
			{
				e.printStackTrace();
			}
		}
		else if (item instanceof Map<?, ?>)
		{
			try
			{
				Constructor<T> constructor = this.itemClass.getConstructor(Map.class);
				convertedItem = (T) constructor.newInstance((Map<?, ?>) item);
			}
			catch (SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | NoSuchMethodException e)
			{
				e.printStackTrace();
			}
			catch (InvocationTargetException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			throw new DataDictionaryException("Key " + this.getName() + ": at least on item isn't a JSONObject, type received :" + item.getClass().getSimpleName());
		}
		return convertedItem;
	}

	@SuppressWarnings("unchecked")
	private T convertEnumItem(Object item) throws DataDictionaryException
	{
		T convertedItem = null;
		if (item instanceof String)
		{
			String itemString = (String) item;
			try
			{
				Method valueOf = this.itemClass.getMethod("valueOf", String.class);
				convertedItem = (T) valueOf.invoke(null, itemString.toUpperCase());
			}
			catch (SecurityException | IllegalAccessException | IllegalArgumentException | NoSuchMethodException e)
			{
				e.printStackTrace();
			}
			catch (InvocationTargetException e)
			{
				e.printStackTrace();
			}

		}
		else
		{
			throw new DataDictionaryException("Key " + this.getName() + ": at least on item isn't a JSONObject, type received :" + item.getClass().getSimpleName());
		}
		return convertedItem;
	}
}
