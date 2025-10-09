// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.types.datadictionary;

import enedis.lab.types.DataArrayList;
import enedis.lab.types.DataDictionary;
import enedis.lab.types.DataDictionaryException;
import enedis.lab.types.DataList;
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

/**
 * Basic implementation of the {@link DataDictionary} interface.
 *
 * <p>This class provides a flexible and extensible data dictionary structure for storing key-value
 * pairs, supporting serialization to and from JSON, file and stream I/O, and key descriptor
 * management. It is designed to be used as a base class for more specific data dictionary
 * implementations.
 *
 * <p>Features include:
 *
 * <ul>
 *   <li>Static factory methods for instantiating from files, streams, strings, JSON objects, and
 *       maps
 *   <li>Support for nested data dictionaries and lists
 *   <li>Key descriptor management for type safety and validation
 *   <li>Serialization and deserialization to/from JSON and Java Map
 *   <li>Cloning, equality, and string representation
 *   <li>Mandatory and optional key validation
 * </ul>
 *
 * @author Enedis Smarties team
 */
public class DataDictionaryBase implements DataDictionary {
  private static final int JSON_INDENT_FACTOR = 0;

  /**
   * Creates a new data dictionary instance from a file containing JSON data.
   *
   * @param file the file to read from
   * @param clazz the class of the data dictionary to instantiate
   * @return a new data dictionary instance
   * @throws JSONException if the file content is not valid JSON
   * @throws DataDictionaryException if the dictionary cannot be created
   * @throws IOException if an I/O error occurs
   */
  public static DataDictionary fromFile(File file, Class<? extends DataDictionary> clazz)
      throws JSONException, DataDictionaryException, IOException {
    if (file == null) {
      throw new DataDictionaryException("Can't load file from null");
    }

    InputStream stream = new FileInputStream(file);

    return DataDictionaryBase.fromStream(stream, clazz);
  }

  /**
   * Creates a new data dictionary instance from an input stream containing JSON data.
   *
   * @param stream the input stream to read from
   * @param clazz the class of the data dictionary to instantiate
   * @return a new data dictionary instance
   * @throws JSONException if the stream content is not valid JSON
   * @throws DataDictionaryException if the dictionary cannot be created
   * @throws IOException if an I/O error occurs
   */
  public static DataDictionary fromStream(InputStream stream, Class<? extends DataDictionary> clazz)
      throws JSONException, DataDictionaryException, IOException {
    if (stream == null) {
      return null;
    }

    byte[] buffer = new byte[stream.available()];
    stream.read(buffer);
    stream.close();
    String text = new String(buffer);

    return DataDictionaryBase.fromString(text, clazz);
  }

  /**
   * Creates a new data dictionary instance from a JSON string.
   *
   * @param text the JSON string
   * @param clazz the class of the data dictionary to instantiate
   * @return a new data dictionary instance
   * @throws JSONException if the string is not valid JSON
   * @throws DataDictionaryException if the dictionary cannot be created
   */
  public static DataDictionary fromString(String text, Class<? extends DataDictionary> clazz)
      throws JSONException, DataDictionaryException {
    if (text == null) {
      return null;
    }

    JSONObject jsonObject = new JSONObject(text);

    return DataDictionaryBase.fromJSON(jsonObject, clazz);
  }

  /**
   * Creates a new data dictionary instance from a {@link JSONObject}.
   *
   * @param jsonObject the JSON object
   * @param clazz the class of the data dictionary to instantiate
   * @return a new data dictionary instance
   * @throws JSONException if the object is not valid JSON
   * @throws DataDictionaryException if the dictionary cannot be created
   */
  public static DataDictionary fromJSON(
      JSONObject jsonObject, Class<? extends DataDictionary> clazz)
      throws JSONException, DataDictionaryException {
    if (jsonObject == null) {
      return null;
    }

    return fromMap(jsonObject.toMap(), clazz);
  }

  /**
   * Creates a new data dictionary instance from a map of key-value pairs and a dictionary class.
   *
   * @param map the map containing key-value pairs
   * @param clazz the class of the data dictionary to instantiate
   * @return a new data dictionary instance
   * @throws DataDictionaryException if the dictionary cannot be created
   */
  public static DataDictionary fromMap(
      Map<String, Object> map, Class<? extends DataDictionary> clazz)
      throws DataDictionaryException {
    if (map == null) {
      return null;
    }
    if (clazz == null) {
      return fromMap(map);
    }
    DataDictionary dataDictionnary;
    try {
      Constructor<? extends DataDictionary> constructor = clazz.getConstructor(Map.class);
      dataDictionnary = constructor.newInstance(map);
    } catch (InvocationTargetException exception) {
      throw new DataDictionaryException(
          "Cannot create instance of "
              + clazz.getName()
              + " : "
              + exception.getTargetException().getMessage());
    } catch (Exception exception) {
      throw new DataDictionaryException(
          "Cannot create instance of " + clazz.getName() + " : " + exception.getMessage());
    }

    return dataDictionnary;
  }

  /**
   * Creates a new data dictionary instance from a map of key-value pairs.
   *
   * @param map the map containing key-value pairs
   * @return a new data dictionary instance
   * @throws DataDictionaryException if the dictionary cannot be created
   */
  public static DataDictionary fromMap(Map<String, Object> map) throws DataDictionaryException {
    if (map == null) {
      return null;
    }
    DataDictionaryBase dataDictionnary = new DataDictionaryBase();
    for (String key : map.keySet()) {
      Object value = map.get(key);
      if (value instanceof JSONObject) {
        JSONObject jsonObjectValue = (JSONObject) value;
        DataDictionary dataDictionaryValue = fromMap(jsonObjectValue.toMap());
        dataDictionnary.set(key, dataDictionaryValue);
      } else if (value instanceof JSONArray) {
        JSONArray jsonArrayValue = (JSONArray) value;
        DataList<Object> listValue = new DataArrayList<Object>(jsonArrayValue.toList());
        dataDictionnary.set(key, listValue);
      } else {
        dataDictionnary.set(key, value);
      }
    }

    return dataDictionnary;
  }

  /** List of key descriptors defining the structure and validation rules for this dictionary. */
  private List<KeyDescriptor<?>> keyDescriptors;

  /** Internal map storing the key-value pairs of the dictionary. */
  protected HashMap<String, Object> data;

  /** Constructs an empty data dictionary with no key descriptors or data. */
  public DataDictionaryBase() {
    this.init();
  }

  /**
   * Constructs a data dictionary from a map of key-value pairs.
   *
   * @param map the map containing initial key-value pairs
   * @throws DataDictionaryException if an error occurs during initialization
   */
  public DataDictionaryBase(Map<String, Object> map) throws DataDictionaryException {
    this();
    this.copy(fromMap(map));
  }

  /**
   * Constructs a data dictionary by copying another data dictionary.
   *
   * @param other the data dictionary to copy
   * @throws DataDictionaryException if an error occurs during initialization
   */
  public DataDictionaryBase(DataDictionary other) throws DataDictionaryException {
    this();
    this.copy(other);
  }

  @Override
  /**
   * Checks if a key exists in the data dictionary.
   *
   * @param key the key to check
   * @return true if the key exists, false otherwise
   */
  public final boolean exists(String key) {
    return this.data.containsKey(key);
  }

  @Override
  /**
   * Checks if a key exists in the key descriptors.
   *
   * @param key the key to check
   * @return true if the key exists in the key descriptors, false otherwise
   */
  public final boolean existsInKeys(String key) {
    return this.keyDescriptors.stream().filter(k -> k.getName().equals(key)).findAny().isPresent();
  }

  @Override
  /**
   * Returns all keys present in the data dictionary.
   *
   * @return an array of all keys
   */
  public final String[] keys() {
    Set<String> setKeys = this.data.keySet();
    String[] keys = new String[setKeys.size()];

    setKeys.toArray(keys);

    return keys;
  }

  @Override
  /**
   * Adds a key to the data dictionary if it does not already exist.
   *
   * @param key the key to add
   */
  public final void addKey(String key) {
    if (key != null) {
      this.data.putIfAbsent(key, null);
    }
  }

  @Override
  /**
   * Removes a key from the data dictionary.
   *
   * @param key the key to remove
   */
  public final void removeKey(String key) {
    if (key != null) {
      this.data.remove(key);
    }
  }

  @Override
  /**
   * Retrieves the value associated with a key in the data dictionary.
   *
   * @param key the key whose value is to be returned
   * @return the value associated with the key, or null if not present
   */
  public final Object get(String key) {
    return this.data.get(key);
  }

  @Override
  /**
   * Sets the value for a key in the data dictionary.
   *
   * @param key the key to set
   * @param value the value to associate with the key
   * @throws DataDictionaryException if the key is not allowed or an error occurs
   */
  public final void set(String key, Object value) throws DataDictionaryException {
    if (key == null) {
      throw new DataDictionaryException("Key null not allowed");
    }

    if (this.keyDescriptors.isEmpty()) {
      this.data.put(key, value);
    } else {
      Optional<KeyDescriptor<?>> keyDescriptor = this.getKeyDescriptor(key);
      if (keyDescriptor.isPresent()) {
        try {
          String setterName = this.getSetterName(key);
          List<String> methodNameList =
              Arrays.asList(this.getClass().getDeclaredMethods()).stream()
                  .map(m -> m.getName())
                  .collect(Collectors.toList());
          if (methodNameList.contains(setterName)) {
            Method method = this.getClass().getDeclaredMethod(setterName, Object.class);
            method.setAccessible(true);
            method.invoke(this, value);
          } else {
            this.data.put(key, keyDescriptor.get().convert(value));
          }
        } catch (NoSuchMethodException
            | SecurityException
            | IllegalAccessException
            | IllegalArgumentException e) {
          throw new DataDictionaryException(
              "Set key "
                  + key
                  + " failed : "
                  + e.getClass().getSimpleName()
                  + " -> "
                  + e.getMessage());
        } catch (InvocationTargetException e) {
          throw new DataDictionaryException(
              "Set key "
                  + key
                  + " failed : "
                  + e.getTargetException().getClass().getSimpleName()
                  + " -> "
                  + e.getTargetException().getMessage());
        }

      } else {
        throw new DataDictionaryException("Key " + key + " not allowed");
      }
    }
  }

  @Override
  /**
   * Copies all key-value pairs from another data dictionary into this one.
   *
   * @param other the data dictionary to copy from
   * @throws DataDictionaryException if an error occurs during copying
   */
  public void copy(DataDictionary other) throws DataDictionaryException {
    String[] otherKeys = other.keys();

    this.clear();
    for (int i = 0; i < otherKeys.length; i++) {
      this.set(otherKeys[i], other.get(otherKeys[i]));
    }

    this.checkAndUpdate();
  }

  @Override
  /** Removes all key-value pairs from the data dictionary. */
  public final void clear() {
    this.data.clear();
  }

  @Override
  /**
   * Returns the hash code value for this data dictionary.
   *
   * @return the hash code value
   */
  public final int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.data == null) ? 0 : this.data.hashCode());
    return result;
  }

  @Override
  /**
   * Compares this data dictionary to another object for equality.
   *
   * @param object the object to compare
   * @return true if the objects are equal, false otherwise
   */
  public final boolean equals(Object object) {
    if (object == null) {
      return false;
    }
    if (this == object) {
      return true;
    }
    if (!(object instanceof DataDictionary)) {
      return false;
    }
    DataDictionaryBase other = (DataDictionaryBase) object;

    if (this.data == null) {
      if (other.data != null) {
        return false;
      }
    } else {
      if (!this.data.keySet().equals(other.data.keySet())) {
        return false;
      }
      for (String key : this.data.keySet()) {
        Object thisValue = this.data.get(key);
        Object otherValue = other.data.get(key);

        if (thisValue == null) {
          if (otherValue != null) {
            return false;
          } else {
            continue;
          }
        } else if (otherValue == null) {
          return false;
        }
        if (!thisValue.getClass().equals(otherValue.getClass())) {
          if ((thisValue instanceof Number) && (otherValue instanceof Number)) {
            double thisDoubleValue = ((Number) thisValue).doubleValue();
            double otherDoubleValue = ((Number) otherValue).doubleValue();
            return thisDoubleValue == otherDoubleValue;
          } else {
            return false;
          }
        }
        if (thisValue.getClass().isArray()) {
          if (thisValue instanceof boolean[]) {
            if (!Arrays.equals((boolean[]) thisValue, (boolean[]) otherValue)) {
              return false;
            }
          } else if (thisValue instanceof byte[]) {
            if (!Arrays.equals((byte[]) thisValue, (byte[]) otherValue)) {
              return false;
            }
          } else if (thisValue instanceof char[]) {
            if (!Arrays.equals((char[]) thisValue, (char[]) otherValue)) {
              return false;
            }
          } else if (thisValue instanceof short[]) {
            if (!Arrays.equals((short[]) thisValue, (short[]) otherValue)) {
              return false;
            }
          } else if (thisValue instanceof int[]) {
            if (!Arrays.equals((int[]) thisValue, (int[]) otherValue)) {
              return false;
            }
          } else if (thisValue instanceof long[]) {
            if (!Arrays.equals((long[]) thisValue, (long[]) otherValue)) {
              return false;
            }
          } else if (thisValue instanceof float[]) {
            if (!Arrays.equals((float[]) thisValue, (float[]) otherValue)) {
              return false;
            }
          } else if (thisValue instanceof double[]) {
            if (!Arrays.equals((double[]) thisValue, (double[]) otherValue)) {
              return false;
            }
          } else if (thisValue instanceof Object[]) {
            if (!Arrays.equals((Object[]) thisValue, (Object[]) otherValue)) {
              return false;
            }
          }
        } else {
          if (!thisValue.equals(otherValue)) {
            return false;
          }
        }
      }
    }

    return true;
  }

  @Override
  /**
   * Writes the data dictionary to a file as JSON.
   *
   * @param file the file to write to
   * @param indentFactor the number of spaces to add to each level of indentation
   * @throws IOException if an I/O error occurs
   */
  public final void toFile(File file, int indentFactor) throws IOException {
    OutputStream stream = new FileOutputStream(file);

    this.toStream(stream, indentFactor);
  }

  @Override
  /**
   * Writes the data dictionary to an output stream as JSON.
   *
   * @param stream the output stream to write to
   * @param indentFactor the number of spaces to add to each level of indentation
   * @throws IOException if an I/O error occurs
   */
  public final void toStream(OutputStream stream, int indentFactor) throws IOException {
    String text = this.toString(indentFactor);

    stream.write(text.getBytes());
  }

  @Override
  /**
   * Serializes the data dictionary to a {@link JSONObject}.
   *
   * @return the JSON representation of the data dictionary
   */
  public JSONObject toJSON() {
    JSONObject jsonObject = new JSONObject();
    String[] keys = this.keys();

    for (int i = 0; i < keys.length; i++) {
      Object value = this.get(keys[i]);
      if (value instanceof DataDictionaryBase) {
        DataDictionaryBase dataDictionary = (DataDictionaryBase) value;
        jsonObject.put(keys[i], dataDictionary.toJSON());
      } else if (value instanceof LocalDateTime) {
        String textValue =
            ((LocalDateTime) value).format(KeyDescriptorLocalDateTime.DEFAULT_FORMATTER);
        jsonObject.put(keys[i], textValue);
      } else {
        jsonObject.put(keys[i], value);
      }
    }

    return jsonObject;
  }

  @SuppressWarnings("unchecked")
  @Override
  /**
   * Returns a shallow copy of the internal map representing the data dictionary.
   *
   * @return a map containing all key-value pairs
   */
  public final Map<String, Object> toMap() {
    return (Map<String, Object>) this.data.clone();
  }

  @Override
  /**
   * Returns a string representation of the data dictionary in JSON format.
   *
   * @return the string representation of the data dictionary
   */
  public String toString() {
    return this.toString(DataDictionaryBase.JSON_INDENT_FACTOR);
  }

  @Override
  /**
   * Returns a string representation of the data dictionary in JSON format with indentation.
   *
   * @param identFactor the number of spaces to add to each level of indentation
   * @return the string representation of the data dictionary
   */
  public String toString(int identFactor) {
    return this.toJSON().toString(identFactor);
  }

  @Override
  /**
   * Creates and returns a deep copy of this data dictionary.
   *
   * @return a clone of this data dictionary
   */
  public DataDictionaryBase clone() {
    try {
      Constructor<?> constructor = this.getClass().getConstructor(DataDictionary.class);
      return this.getClass().cast(constructor.newInstance(this));
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  @Override
  /** Prints the data dictionary to the standard output in pretty JSON format. */
  public void print() {
    System.out.println(this.toString(2));
  }

  /**
   * Updates optional parameters and checks that all mandatory parameters are present.
   *
   * @throws DataDictionaryException if a mandatory parameter is missing or invalid
   */
  protected final void checkAndUpdate() throws DataDictionaryException {
    this.updateOptionalParameters();
    this.checkMandatoryParameters();
  }

  /**
   * Checks that all mandatory parameters defined by key descriptors are present in the dictionary.
   *
   * @throws DataDictionaryException if a mandatory key is missing
   */
  protected void checkMandatoryParameters() throws DataDictionaryException {
    for (KeyDescriptor<?> key : this.keyDescriptors) {
      if (key.isMandatory() && !this.exists(key.getName())) {
        throw new DataDictionaryException("Mandatory key " + key.getName() + " not defined");
      }
    }
  }

  /**
   * Updates optional parameters in the data dictionary. Subclasses may override to provide custom
   * logic.
   *
   * @throws DataDictionaryException if an error occurs during update
   */
  protected void updateOptionalParameters() throws DataDictionaryException {}

  /**
   * Adds a key descriptor to the list of key descriptors for this dictionary.
   *
   * @param keyDescriptor the key descriptor to add
   * @throws DataDictionaryException if the key already exists
   */
  protected void addKeyDescriptor(KeyDescriptor<?> keyDescriptor) throws DataDictionaryException {
    if (this.existsInKeys(keyDescriptor.getName())) {
      throw new DataDictionaryException("Key " + keyDescriptor.getName() + "already exists");
    }
    this.keyDescriptors.add(keyDescriptor);
  }

  /**
   * Adds all key descriptors from the provided list to this dictionary.
   *
   * @param keyDescriptor the list of key descriptors to add
   * @throws DataDictionaryException if a key already exists
   */
  protected void addAllKeyDescriptor(List<KeyDescriptor<?>> keyDescriptor)
      throws DataDictionaryException {
    for (KeyDescriptor<?> key : keyDescriptor) {
      this.addKeyDescriptor(key);
    }
  }

  /**
   * Retrieves the key descriptor for the specified key name, if present.
   *
   * @param name the name of the key
   * @return an {@link Optional} containing the key descriptor if found, or empty otherwise
   */
  protected Optional<KeyDescriptor<?>> getKeyDescriptor(String name) {
    return this.keyDescriptors.stream().filter(k -> k.getName().equals(name)).findFirst();
  }

  private String getSetterName(String key) {
    return "set" + key.substring(0, 1).toUpperCase() + key.substring(1);
  }

  private void init() {
    this.keyDescriptors = new ArrayList<KeyDescriptor<?>>();
    this.data = new HashMap<String, Object>();
  }
}
