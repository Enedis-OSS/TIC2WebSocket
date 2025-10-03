// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.types;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.RandomAccess;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Resizable-array implementation of the {@code DataList} interface.
 *
 * @param <E> the type of elements in this data list
 */
public class DataArrayList<E> extends ArrayList<E> implements DataList<E> {
  private static final long serialVersionUID = 3116080161929203526L;

  private static final int JSON_INDENT_FACTOR = 0;

  /**
   * Returns a fixed-size data list backed by the specified array. (Changes to the returned data
   * list "write through" to the array.) This method acts as bridge between array-based and
   * collection-based APIs, in combination with {@link Collection#toArray}. The returned list is
   * serializable and implements {@link RandomAccess}.
   *
   * <p>This method also provides a convenient way to create a fixed-size data list initialized to
   * contain several elements:
   *
   * <pre>
   *     DataList&lt;String&gt; stooges = DataArrayList.asList("Larry", "Moe", "Curly");
   * </pre>
   *
   * @param <E> the class of the objects in the array
   * @param items the array by which the data list will be backed
   * @return a data list view of the specified array
   */
  @SafeVarargs
  public static <E> DataList<E> asList(E... items) {
    DataList<E> dataList = new DataArrayList<E>();

    for (E item : items) {
      dataList.add(item);
    }

    return dataList;
  }

  /**
   * Instantiate a data list from a File
   *
   * @param <E> the class of the objects in the list
   * @param file
   * @param clazz
   * @return a data list
   * @throws JSONException
   * @throws IOException
   */
  public static <E> DataList<E> fromFile(File file, Class<E> clazz)
      throws JSONException, IOException {
    InputStream stream = new FileInputStream(file);

    return fromStream(stream, clazz);
  }

  /**
   * Instantiate a data list from a Stream
   *
   * @param <E> the class of the objects in the list
   * @param stream
   * @param clazz
   * @return a data list
   * @throws JSONException
   * @throws IOException
   */
  public static <E> DataList<E> fromStream(InputStream stream, Class<E> clazz)
      throws JSONException, IOException {
    byte[] buffer = new byte[stream.available()];
    stream.read(buffer);
    stream.close();
    String text = new String(buffer);

    return fromString(text, clazz);
  }

  /**
   * Instantiate a data list from a String
   *
   * @param <E> the class of the objects in the list
   * @param text
   * @param clazz
   * @return a data list
   * @throws JSONException
   */
  public static <E> DataList<E> fromString(String text, Class<E> clazz) throws JSONException {
    if (text == null) {
      return null;
    }

    JSONArray jsonArray = new JSONArray(text);

    return fromJSON(jsonArray, clazz);
  }

  /**
   * Instantiate a data list from a JSONObject
   *
   * @param <E> the class of the objects in the list
   * @param jsonArray
   * @param clazz
   * @return a data list
   * @throws JSONException
   */
  public static <E> DataList<E> fromJSON(JSONArray jsonArray, Class<E> clazz) throws JSONException {
    if (jsonArray == null) {
      return null;
    }
    DataList<E> dataList = new DataArrayList<E>();
    for (int i = 0; i < jsonArray.length(); i++) {
      Object jsonItem = jsonArray.get(i);
      E dataListItem;
      try {
        if (JSONObject.NULL.equals(jsonItem)) {
          dataListItem = null;
        } else if (clazz.isAssignableFrom(jsonItem.getClass())) {
          dataListItem = clazz.cast(jsonItem);
        } else if (Enum.class.isAssignableFrom(clazz)) {
          dataListItem = convertToEnum(jsonItem, clazz);
        } else if (LocalDateTime.class.isAssignableFrom(clazz)) {
          dataListItem = convertToLocalDateTime(jsonItem, clazz);
        } else if (List.class.isAssignableFrom(clazz)) {
          dataListItem = convertToDataList(jsonItem, clazz);
        } else if (DataDictionary.class.isAssignableFrom(clazz)) {
          dataListItem = convertToDataDictionary(jsonItem, clazz);
        } else {
          throw new IllegalArgumentException(
              "Cannot convert " + jsonItem.getClass().getName() + " to " + clazz.getName());
        }
      } catch (NoSuchMethodException
          | SecurityException
          | IllegalAccessException
          | IllegalArgumentException e) {
        throw new IllegalArgumentException(
            "Cannot convert " + jsonItem + " to " + clazz.getName() + " (" + e.getMessage() + ")",
            e);
      } catch (InvocationTargetException e) {
        throw new IllegalArgumentException(
            "Cannot convert "
                + jsonItem
                + " to "
                + clazz.getName()
                + " ("
                + e.getTargetException().getMessage()
                + ")",
            e);
      }
      dataList.add(dataListItem);
    }

    return dataList;
  }

  /** Constructs an empty data list with an initial capacity of ten. */
  public DataArrayList() {
    super();
  }

  /**
   * Constructs a data list containing the elements of the specified collection, in the order they
   * are returned by the collection's iterator.
   *
   * @param collection the collection whose elements are to be placed into this data list
   * @throws NullPointerException if the specified collection is null
   */
  public DataArrayList(Collection<? extends E> collection) {
    super(collection);
  }

  @Override
  public JSONArray toJSON() {
    JSONArray jsonArray = new JSONArray();

    for (E item : this) {
      Object objectItem;
      if (item instanceof DataDictionary) {
        DataDictionary dictionaryItem = (DataDictionary) item;
        objectItem = dictionaryItem.toJSON();
      } else {
        objectItem = item;
      }
      jsonArray.put(objectItem);
    }

    return jsonArray;
  }

  @Override
  public void toFile(File file, int indentFactor) throws IOException {
    OutputStream stream = new FileOutputStream(file);

    this.toStream(stream, indentFactor);
  }

  @Override
  public void toStream(OutputStream stream, int indentFactor) throws IOException {
    String text = this.toString(indentFactor);

    stream.write(text.getBytes());
  }

  @Override
  public String toString() {
    return this.toString(JSON_INDENT_FACTOR);
  }

  @Override
  public String toString(int identFactor) {
    return this.toJSON().toString(identFactor);
  }

  @SuppressWarnings("unchecked")
  private static <E> E convertToDataDictionary(Object value, Class<E> clazz)
      throws NoSuchMethodException,
          SecurityException,
          IllegalAccessException,
          IllegalArgumentException,
          InvocationTargetException {
    E result;

    if (value instanceof JSONObject) {
      Method method = clazz.getMethod("fromJSON", JSONObject.class, Class.class);
      result = (E) method.invoke(null, value, clazz);
    } else {
      throw new IllegalArgumentException(
          "Cannot convert " + value.getClass().getName() + " to " + clazz.getName());
    }

    return result;
  }

  @SuppressWarnings("unchecked")
  private static <E> E convertToDataList(Object value, Class<E> clazz) {
    E result;

    if (value instanceof JSONArray) {
      result = (E) DataArrayList.fromJSON((JSONArray) value, Object.class);
    } else {
      throw new IllegalArgumentException(
          "Cannot convert " + value.getClass().getName() + " to " + clazz.getName());
    }

    return result;
  }

  @SuppressWarnings("unchecked")
  private static <E> E convertToEnum(Object value, Class<E> clazz)
      throws NoSuchMethodException,
          SecurityException,
          IllegalAccessException,
          IllegalArgumentException,
          InvocationTargetException {
    E result;

    if (value instanceof String) {
      Method method = clazz.getDeclaredMethod("valueOf", String.class);
      result = (E) method.invoke(null, (String) value);
    } else {
      throw new IllegalArgumentException(
          "Cannot convert " + value.getClass().getName() + " to " + clazz.getName());
    }

    return result;
  }

  @SuppressWarnings("unchecked")
  private static <E> E convertToLocalDateTime(Object value, Class<E> clazz)
      throws NoSuchMethodException,
          SecurityException,
          IllegalAccessException,
          IllegalArgumentException,
          InvocationTargetException {
    E result;

    if (value instanceof String) {
      Method method = clazz.getDeclaredMethod("parse", CharSequence.class);
      result = (E) method.invoke(null, (String) value);
    } else {
      throw new IllegalArgumentException(
          "Cannot convert " + value.getClass().getName() + " to " + clazz.getName());
    }

    return result;
  }
}
