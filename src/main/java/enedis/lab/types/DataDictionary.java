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
 * Interface for managing key-value data dictionaries with serialization and utility methods.
 *
 * <p>Provides methods for key management, value access, serialization to JSON and Map, file and
 * stream output, and cloning. Implementations may support additional validation and type safety.
 *
 * @author Enedis Smarties team
 */
public interface DataDictionary extends Cloneable {

  /**
   * Checks if the specified key exists in the data dictionary.
   *
   * @param key the key to check
   * @return true if the key exists, false otherwise
   */
  public boolean exists(String key);

  /**
   * Returns the list of all keys currently present in the data dictionary.
   *
   * @return an array of key names
   */
  public String[] keys();

  /**
   * Checks if the data dictionary contains the specified key in its key set.
   *
   * @param key the key to check
   * @return true if the key is present, false otherwise
   */
  public boolean existsInKeys(String key);

  /**
   * Adds the specified key to the data dictionary. If the key already exists, no action is taken.
   *
   * @param key the key to add
   */
  public void addKey(String key);

  /**
   * Removes the specified key from the data dictionary.
   *
   * @param key the key to remove
   */
  public void removeKey(String key);

  /** Removes all keys and values from the data dictionary. */
  public void clear();

  /**
   * Returns the value associated with the specified key.
   *
   * @param key the key whose value is to be returned
   * @return the value of the given key, or null if not present
   */
  public Object get(String key);

  /**
   * Sets the value for the specified key in the data dictionary.
   *
   * @param key the key to set
   * @param value the value to associate with the key
   * @throws DataDictionaryException if the value is invalid or cannot be set
   */
  public void set(String key, Object value) throws DataDictionaryException;

  /**
   * Copies all key-value pairs from another data dictionary into this one.
   *
   * @param other the data dictionary to copy from
   * @throws DataDictionaryException if a value cannot be copied
   */
  public void copy(DataDictionary other) throws DataDictionaryException;

  /**
   * Serializes this data dictionary to a {@link JSONObject}.
   *
   * @return a JSONObject representing the data dictionary
   */
  public JSONObject toJSON();

  /**
   * Serializes this data dictionary to a {@link Map}.
   *
   * @return a map representing the data dictionary
   */
  public Map<String, Object> toMap();

  /**
   * Writes the data dictionary to a file in JSON format.
   *
   * @param file the file to write to
   * @param indentFactor the number of spaces to add to each level of indentation
   * @throws IOException if an I/O error occurs
   */
  public void toFile(File file, int indentFactor) throws IOException;

  /**
   * Writes the data dictionary to an output stream in JSON format.
   *
   * @param stream the output stream to write to
   * @param indentFactor the number of spaces to add to each level of indentation
   * @throws IOException if an I/O error occurs
   */
  public void toStream(OutputStream stream, int indentFactor) throws IOException;

  @Override
  public String toString();

  /**
   * Returns a string representation of the data dictionary in JSON format with the specified
   * indentation.
   *
   * @param indentFactor the number of spaces to add to each level of indentation
   * @return a string containing the JSON representation
   */
  public String toString(int indentFactor);

  /**
   * Creates and returns a deep copy of this data dictionary.
   *
   * @return a clone of this data dictionary
   * @throws CloneNotSupportedException if the object cannot be cloned
   */
  public Object clone() throws CloneNotSupportedException;

  /** Prints the contents of this data dictionary to the console. */
  public void print();
}
