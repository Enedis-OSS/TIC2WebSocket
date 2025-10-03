// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.types.datadictionary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import enedis.lab.types.DataArrayList;
import enedis.lab.types.DataDictionary;
import enedis.lab.types.DataDictionaryException;
import enedis.lab.types.DataList;

/**
 * DataDictionary basic implementation
 */
public class DataDictionaryBase implements DataDictionary
{
	private static final int JSON_INDENT_FACTOR = 0;

	/**
	 * Instantiate a datadictionary from a File
	 *
	 * @param file
	 * @param clazz
	 *
	 * @return a datadictionary
	 * @throws JSONException
	 * @throws DataDictionaryException
	 * @throws IOException
	 */
	public static DataDictionary fromFile(File file, Class<? extends DataDictionary> clazz) throws JSONException, DataDictionaryException, IOException
	{
		if (file == null)
		{
			throw new DataDictionaryException("Can't load file from null");
		}

		InputStream stream = new FileInputStream(file);

		return DataDictionaryBase.fromStream(stream, clazz);
	}

	/**
	 * Instantiate a datadictionary from a Stream
	 *
	 * @param stream
	 * @param clazz
	 *
	 * @return a datadictionary
	 * @throws JSONException
	 * @throws DataDictionaryException
	 * @throws IOException
	 */
	public static DataDictionary fromStream(InputStream stream, Class<? extends DataDictionary> clazz) throws JSONException, DataDictionaryException, IOException
	{
		if (stream == null)
		{
			return null;
		}

		byte[] buffer = new byte[stream.available()];
		stream.read(buffer);
		stream.close();
		String text = new String(buffer);

		return DataDictionaryBase.fromString(text, clazz);
	}

	/**
	 * Instantiate a datadictionary from a String
	 *
	 * @param text
	 * @param clazz
	 *
	 * @return a datadictionary
	 * @throws JSONException
	 * @throws DataDictionaryException
	 */
	public static DataDictionary fromString(String text, Class<? extends DataDictionary> clazz) throws JSONException, DataDictionaryException
	{
		if (text == null)
		{
			return null;
		}

		JSONObject jsonObject = new JSONObject(text);

		return DataDictionaryBase.fromJSON(jsonObject, clazz);
	}

	/**
	 * Instantiate a datadictionary from a JSONObject
	 *
	 * @param jsonObject
	 * @param clazz
	 *
	 * @return a datadictionary
	 * @throws JSONException
	 * @throws DataDictionaryException
	 */
	public static DataDictionary fromJSON(JSONObject jsonObject, Class<? extends DataDictionary> clazz) throws JSONException, DataDictionaryException
	{
		if (jsonObject == null)
		{
			return null;
		}

		return fromMap(jsonObject.toMap(), clazz);
	}

	/**
	 * Instantiate a datadictionary from a Map
	 *
	 * @param map
	 * @param clazz
	 *
	 * @return a datadictionary
	 * @throws JSONException
	 * @throws DataDictionaryException
	 */
	public static DataDictionary fromMap(Map<String, Object> map, Class<? extends DataDictionary> clazz) throws DataDictionaryException
	{
		if (map == null)
		{
			return null;
		}
		if (clazz == null)
		{
			return fromMap(map);
		}
		DataDictionary dataDictionnary;
		try
		{
			Constructor<? extends DataDictionary> constructor = clazz.getConstructor(Map.class);
			dataDictionnary = constructor.newInstance(map);
		}
		catch (InvocationTargetException exception)
		{
			throw new DataDictionaryException("Cannot create instance of " + clazz.getName() + " : " + exception.getTargetException().getMessage());
		}
		catch (Exception exception)
		{
			throw new DataDictionaryException("Cannot create instance of " + clazz.getName() + " : " + exception.getMessage());
		}

		return dataDictionnary;
	}

	/**
	 * Instantiate a datadictionary from a Map
	 *
	 * @param map
	 *
	 * @return a datadictionary
	 * @throws JSONException
	 * @throws DataDictionaryException
	 */
	public static DataDictionary fromMap(Map<String, Object> map) throws DataDictionaryException
	{
		if (map == null)
		{
			return null;
		}
		DataDictionaryBase dataDictionnary = new DataDictionaryBase();
		for (String key : map.keySet())
		{
			Object value = map.get(key);
			if (value instanceof JSONObject)
			{
				JSONObject jsonObjectValue = (JSONObject) value;
				DataDictionary dataDictionaryValue = fromMap(jsonObjectValue.toMap());
				dataDictionnary.set(key, dataDictionaryValue);
			}
			else if (value instanceof JSONArray)
			{
				JSONArray jsonArrayValue = (JSONArray) value;
				DataList<Object> listValue = new DataArrayList<Object>(jsonArrayValue.toList());
				dataDictionnary.set(key, listValue);
			}
			else
			{
				dataDictionnary.set(key, value);
			}
		}

		return dataDictionnary;
	}

	private List<KeyDescriptor<?>>		keyDescriptors;
	protected HashMap<String, Object>	data;

	/**
	 * Default constructor
	 */
	public DataDictionaryBase()
	{
		this.init();
	}

	/**
	 * Constructor using map and setting key not defined allowed flag
	 *
	 * @param map
	 * @throws DataDictionaryException
	 */
	public DataDictionaryBase(Map<String, Object> map) throws DataDictionaryException
	{
		this();
		this.copy(fromMap(map));
	}

	/**
	 * Constructor using dataDictionary and setting key not defined allowed flag
	 *
	 * @param other
	 * @throws DataDictionaryException
	 */
	public DataDictionaryBase(DataDictionary other) throws DataDictionaryException
	{
		this();
		this.copy(other);
	}

	@Override
	public final boolean exists(String key)
	{
		return this.data.containsKey(key);
	}

	@Override
	public final boolean existsInKeys(String key)
	{
		// @formatter:off
		return this.keyDescriptors.stream()
								  .filter(k -> k.getName().equals(key))
								  .findAny()
								  .isPresent();
		// @formatter:on
	}

	@Override
	public final String[] keys()
	{
		Set<String> setKeys = this.data.keySet();
		String[] keys = new String[setKeys.size()];

		setKeys.toArray(keys);

		return keys;
	}

	@Override
	public final void addKey(String key)
	{
		if (key != null)
		{
			this.data.putIfAbsent(key, null);
		}
	}

	@Override
	public final void removeKey(String key)
	{
		if (key != null)
		{
			this.data.remove(key);
		}
	}

	@Override
	public final Object get(String key)
	{
		return this.data.get(key);
	}

	@Override
	public final void set(String key, Object value) throws DataDictionaryException
	{
		if (key == null)
		{
			throw new DataDictionaryException("Key null not allowed");
		}

		if (this.keyDescriptors.isEmpty())
		{
			this.data.put(key, value);
		}
		else
		{
			Optional<KeyDescriptor<?>> keyDescriptor = this.getKeyDescriptor(key);
			if (keyDescriptor.isPresent())
			{
				try
				{
					String setterName = this.getSetterName(key);
					// @formatter:off
					List<String> methodNameList = Arrays.asList(this.getClass().getDeclaredMethods())
														.stream()
														.map(m -> m.getName())
														.collect(Collectors.toList());
					// @formatter:on
					if (methodNameList.contains(setterName))
					{
						Method method = this.getClass().getDeclaredMethod(setterName, Object.class);
						method.setAccessible(true);
						method.invoke(this, value);
					}
					else
					{
						this.data.put(key, keyDescriptor.get().convert(value));
					}
				}
				catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException e)
				{
					throw new DataDictionaryException("Set key " + key + " failed : " + e.getClass().getSimpleName() + " -> " + e.getMessage());
				}
				catch (InvocationTargetException e)
				{
					throw new DataDictionaryException(
							"Set key " + key + " failed : " + e.getTargetException().getClass().getSimpleName() + " -> " + e.getTargetException().getMessage());
				}

			}
			else
			{
				throw new DataDictionaryException("Key " + key + " not allowed");
			}
		}
	}

	@Override
	public void copy(DataDictionary other) throws DataDictionaryException
	{
		String[] otherKeys = other.keys();

		this.clear();
		for (int i = 0; i < otherKeys.length; i++)
		{
			this.set(otherKeys[i], other.get(otherKeys[i]));
		}

		this.checkAndUpdate();
	}

	@Override
	public final void clear()
	{
		this.data.clear();
	}

	@Override
	public final int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.data == null) ? 0 : this.data.hashCode());
		return result;
	}

	@Override
	public final boolean equals(Object object)
	{
		if (object == null)
		{
			return false;
		}
		if (this == object)
		{
			return true;
		}
		if (!(object instanceof DataDictionary))
		{
			return false;
		}
		DataDictionaryBase other = (DataDictionaryBase) object;

		if (this.data == null)
		{
			if (other.data != null)
			{
				return false;
			}
		}
		else
		{
			if (!this.data.keySet().equals(other.data.keySet()))
			{
				return false;
			}
			for (String key : this.data.keySet())
			{
				Object thisValue = this.data.get(key);
				Object otherValue = other.data.get(key);

				if (thisValue == null)
				{
					if (otherValue != null)
					{
						return false;
					}
					else
					{
						continue;
					}
				}
				else if (otherValue == null)
				{
					return false;
				}
				if (!thisValue.getClass().equals(otherValue.getClass()))
				{
					if ((thisValue instanceof Number) && (otherValue instanceof Number))
					{
						double thisDoubleValue = ((Number) thisValue).doubleValue();
						double otherDoubleValue = ((Number) otherValue).doubleValue();
						return thisDoubleValue == otherDoubleValue;
					}
					else
					{
						return false;
					}
				}
				if (thisValue.getClass().isArray())
				{
					if (thisValue instanceof boolean[])
					{
						if (!Arrays.equals((boolean[]) thisValue, (boolean[]) otherValue))
						{
							return false;
						}
					}
					else if (thisValue instanceof byte[])
					{
						if (!Arrays.equals((byte[]) thisValue, (byte[]) otherValue))
						{
							return false;
						}
					}
					else if (thisValue instanceof char[])
					{
						if (!Arrays.equals((char[]) thisValue, (char[]) otherValue))
						{
							return false;
						}
					}
					else if (thisValue instanceof short[])
					{
						if (!Arrays.equals((short[]) thisValue, (short[]) otherValue))
						{
							return false;
						}
					}
					else if (thisValue instanceof int[])
					{
						if (!Arrays.equals((int[]) thisValue, (int[]) otherValue))
						{
							return false;
						}
					}
					else if (thisValue instanceof long[])
					{
						if (!Arrays.equals((long[]) thisValue, (long[]) otherValue))
						{
							return false;
						}
					}
					else if (thisValue instanceof float[])
					{
						if (!Arrays.equals((float[]) thisValue, (float[]) otherValue))
						{
							return false;
						}
					}
					else if (thisValue instanceof double[])
					{
						if (!Arrays.equals((double[]) thisValue, (double[]) otherValue))
						{
							return false;
						}
					}
					else if (thisValue instanceof Object[])
					{
						if (!Arrays.equals((Object[]) thisValue, (Object[]) otherValue))
						{
							return false;
						}
					}
				}
				else
				{
					if (!thisValue.equals(otherValue))
					{
						return false;
					}
				}
			}
		}

		return true;
	}

	@Override
	public final void toFile(File file, int indentFactor) throws IOException
	{
		OutputStream stream = new FileOutputStream(file);

		this.toStream(stream, indentFactor);
	}

	@Override
	public final void toStream(OutputStream stream, int indentFactor) throws IOException
	{
		String text = this.toString(indentFactor);

		stream.write(text.getBytes());
	}

	@Override
	public JSONObject toJSON()
	{
		JSONObject jsonObject = new JSONObject();
		String[] keys = this.keys();

		for (int i = 0; i < keys.length; i++)
		{
			Object value = this.get(keys[i]);
			if (value instanceof DataDictionaryBase)
			{
				DataDictionaryBase dataDictionary = (DataDictionaryBase) value;
				jsonObject.put(keys[i], dataDictionary.toJSON());
			}
			else if (value instanceof LocalDateTime)
			{
				String textValue = ((LocalDateTime) value).format(KeyDescriptorLocalDateTime.DEFAULT_FORMATTER);
				jsonObject.put(keys[i], textValue);
			}
			else
			{
				jsonObject.put(keys[i], value);
			}
		}

		return jsonObject;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final Map<String, Object> toMap()
	{
		return (Map<String, Object>) this.data.clone();
	}

	@Override
	public String toString()
	{
		return this.toString(DataDictionaryBase.JSON_INDENT_FACTOR);
	}

	@Override
	public String toString(int identFactor)
	{
		return this.toJSON().toString(identFactor);
	}

	@Override
	public DataDictionaryBase clone()
	{
		try
		{
			Constructor<?> constructor = this.getClass().getConstructor(DataDictionary.class);
			return this.getClass().cast(constructor.newInstance(this));
		}
		catch (Exception e)
		{
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public void print()
	{
		System.out.println(this.toString(2));
	}

	protected final void checkAndUpdate() throws DataDictionaryException
	{
		this.updateOptionalParameters();
		this.checkMandatoryParameters();
	}

	protected void checkMandatoryParameters() throws DataDictionaryException
	{
		for (KeyDescriptor<?> key : this.keyDescriptors)
		{
			if (key.isMandatory() && !this.exists(key.getName()))
			{
				throw new DataDictionaryException("Mandatory key " + key.getName() + " not defined");
			}
		}
	}

	protected void updateOptionalParameters() throws DataDictionaryException
	{
	}

	protected void addKeyDescriptor(KeyDescriptor<?> keyDescriptor) throws DataDictionaryException
	{
		if (this.existsInKeys(keyDescriptor.getName()))
		{
			throw new DataDictionaryException("Key " + keyDescriptor.getName() + "already exists");
		}
		this.keyDescriptors.add(keyDescriptor);
	}

	protected void addAllKeyDescriptor(List<KeyDescriptor<?>> keyDescriptor) throws DataDictionaryException
	{
		for (KeyDescriptor<?> key : keyDescriptor)
		{
			this.addKeyDescriptor(key);
		}
	}

	protected Optional<KeyDescriptor<?>> getKeyDescriptor(String name)
	{
		// @formatter:off
		return this.keyDescriptors.stream()
								 .filter(k -> k.getName().equals(name))
								 .findFirst();
		// @formatter:on
	}

	private String getSetterName(String key)
	{
		return "set" + key.substring(0, 1).toUpperCase() + key.substring(1);
	}

	private void init()
	{
		this.keyDescriptors = new ArrayList<KeyDescriptor<?>>();
		this.data = new HashMap<String, Object>();
	}

}
