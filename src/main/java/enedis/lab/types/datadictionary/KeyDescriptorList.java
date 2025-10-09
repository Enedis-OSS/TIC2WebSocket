// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.types.datadictionary;

import enedis.lab.types.DataDictionary;
import enedis.lab.types.DataDictionaryException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

/**
 * Key descriptor for values that are lists of a specific type.
 *
 * <p>This descriptor allows a key to be associated with a list of items, supporting conversion from
 * arrays, lists, maps, and JSON objects. It handles type conversion for each item, including
 * support for nested data dictionaries and enums.
 *
 * @param <T> the type of item in the list
 * @author Enedis Smarties team
 */
public class KeyDescriptorList<T> extends KeyDescriptorBase<List<T>> {
  /** The class of the item accepted in the list for this key. */
  private Class<T> itemClass;

  /**
   * Constructs a key descriptor for a list value.
   *
   * @param name the key name (must not be null)
   * @param mandatory true if the key is mandatory, false otherwise
   * @param itemClass the class of the item accepted in the list
   */
  public KeyDescriptorList(String name, boolean mandatory, Class<T> itemClass) {
    super(name, mandatory);
    this.itemClass = itemClass;
  }

  /**
   * Converts an object to a list of items of type T for this key. Accepts single items, lists,
   * arrays, or maps, and converts each item as needed.
   *
   * @param value the object to convert
   * @return the converted list of items
   * @throws DataDictionaryException if the value or any item cannot be converted
   */
  @Override
  public List<T> convertValue(Object value) throws DataDictionaryException {
    List<T> convertedValue = new ArrayList<T>();

    if (this.itemClass.isAssignableFrom(value.getClass())) {
      T convertedItem = this.convertItem(value);
      convertedValue.add(convertedItem);
    } else if (value instanceof HashMap<?, ?>) {
      T convertedItem = this.convertItem(value);
      convertedValue.add(convertedItem);
    } else if (value instanceof List<?>) {
      for (Object item : (List<?>) value) {
        T convertedItem = this.convertItem(item);
        convertedValue.add(convertedItem);
      }
    } else if (value instanceof Object[]) {
      for (Object item : (Object[]) value) {
        T convertedItem = this.convertItem(item);
        convertedValue.add(convertedItem);
      }
    } else {
      throw new DataDictionaryException(
          "Key "
              + this.getName()
              + ": Cannot convert type "
              + value.getClass().getSimpleName()
              + " to List<"
              + this.itemClass.getSimpleName()
              + ">");
    }
    return convertedValue;
  }

  /**
   * Converts a single item to the type T, handling data dictionaries and enums.
   *
   * @param item the item to convert
   * @return the converted item
   * @throws DataDictionaryException if the item cannot be converted
   */
  private T convertItem(Object item) throws DataDictionaryException {
    T convertedItem = null;
    if (this.itemClass.isAssignableFrom(item.getClass())) {
      convertedItem = this.itemClass.cast(item);
    } else if (DataDictionary.class.isAssignableFrom(this.itemClass)) {
      convertedItem = this.convertDataDictionaryItem(item);
    } else if (Enum.class.isAssignableFrom(this.itemClass)) {
      convertedItem = this.convertEnumItem(item);
    } else {
      throw new DataDictionaryException(
          "Key "
              + this.getName()
              + ": at least on item isn't a "
              + this.itemClass.getSimpleName()
              + ", type received :"
              + item.getClass().getSimpleName());
    }
    return convertedItem;
  }

  /**
   * Converts an item to a data dictionary of type T, using a Map or JSONObject.
   *
   * @param item the item to convert
   * @return the converted data dictionary item
   * @throws DataDictionaryException if the item cannot be converted
   */
  private T convertDataDictionaryItem(Object item) throws DataDictionaryException {
    T convertedItem = null;
    if (item instanceof JSONObject) {
      JSONObject itemJsonObject = (JSONObject) item;
      try {
        Constructor<T> constructor = this.itemClass.getConstructor(Map.class);
        convertedItem = (T) constructor.newInstance(itemJsonObject.toMap());
      } catch (SecurityException
          | InstantiationException
          | IllegalAccessException
          | IllegalArgumentException
          | NoSuchMethodException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      }
    } else if (item instanceof Map<?, ?>) {
      try {
        Constructor<T> constructor = this.itemClass.getConstructor(Map.class);
        convertedItem = (T) constructor.newInstance((Map<?, ?>) item);
      } catch (SecurityException
          | InstantiationException
          | IllegalAccessException
          | IllegalArgumentException
          | NoSuchMethodException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      }
    } else {
      throw new DataDictionaryException(
          "Key "
              + this.getName()
              + ": at least on item isn't a JSONObject, type received :"
              + item.getClass().getSimpleName());
    }
    return convertedItem;
  }

  /**
   * Converts an item to an enum of type T, using the valueOf method.
   *
   * @param item the item to convert (must be a String)
   * @return the converted enum item
   * @throws DataDictionaryException if the item cannot be converted
   */
  @SuppressWarnings("unchecked")
  private T convertEnumItem(Object item) throws DataDictionaryException {
    T convertedItem = null;
    if (item instanceof String) {
      String itemString = (String) item;
      try {
        Method valueOf = this.itemClass.getMethod("valueOf", String.class);
        convertedItem = (T) valueOf.invoke(null, itemString.toUpperCase());
      } catch (SecurityException
          | IllegalAccessException
          | IllegalArgumentException
          | NoSuchMethodException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      }
    } else {
      throw new DataDictionaryException(
          "Key "
              + this.getName()
              + ": at least on item isn't a JSONObject, type received :"
              + item.getClass().getSimpleName());
    }
    return convertedItem;
  }
}
