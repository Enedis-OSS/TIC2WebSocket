// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.types;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import org.json.JSONObject;

/**
 * Interface used to handle data dictionaries
 */
public interface DataDictionary extends Cloneable
{
	/**
	 * Check key exists
	 *
	 * @param key
	 *            Key to check
	 *
	 * @return true if key exists, false otherwise
	 */
	public boolean exists(String key);

	/**
	 * Get the datadictionary current map keys list
	 * 
	 * @return the datadictionary current map keys list
	 */
	public String[] keys();

	/**
	 * Check if the datadictionary map contains the given key
	 * 
	 * @param key
	 * @return true if the datadictionary map contains the given key
	 */
	public boolean existsInKeys(String key);

	/**
	 * Add the given key in the datadictionary map, if the given key already exists, do nothing
	 * 
	 * @param key
	 */
	public void addKey(String key);

	/**
	 * Remove the given key in the datadictionary map
	 * 
	 * @param key
	 */
	public void removeKey(String key);

	/**
	 * Clear the datadictionary map
	 */
	public void clear();

	/**
	 * Get value of the given key
	 * 
	 * @param key
	 * @return value of the given key
	 */
	public Object get(String key);

	/**
	 * Set value of the given key
	 * 
	 * @param key
	 * @param value
	 * @throws DataDictionaryException
	 */
	public void set(String key, Object value) throws DataDictionaryException;

	/**
	 * Copy an other datadictionary into this one
	 * 
	 * @param other
	 * @throws DataDictionaryException
	 */
	public void copy(DataDictionary other) throws DataDictionaryException;

	/**
	 * Convert this dataditionary to JSON
	 * 
	 * @return a JSONObject
	 */
	public JSONObject toJSON();

	/**
	 * Convert this dataditionary to Map
	 * 
	 * @return a JSONObject
	 */
	public Map<String, Object> toMap();

	/**
	 * Convert the given datadictionary to a File
	 * 
	 * @param file
	 * @param indentFactor
	 * @throws IOException
	 */
	public void toFile(File file, int indentFactor) throws IOException;

	/**
	 * Convert the given datadictionary to a Stream
	 * 
	 * @param stream
	 * @param indentFactor
	 * @throws IOException
	 */
	public void toStream(OutputStream stream, int indentFactor) throws IOException;

	@Override
	public String toString();

	/**
	 * Convert this dataditionary to a String
	 * 
	 * @param indentFactor
	 * @return a JSONObject
	 */
	public String toString(int indentFactor);

	/**
	 * Convert this dataditionary to Map
	 * 
	 * @return a JSONObject
	 * @throws CloneNotSupportedException
	 */
	public Object clone() throws CloneNotSupportedException;

	/**
	 * Print this datactionary in the console
	 */
	public void print();
}
