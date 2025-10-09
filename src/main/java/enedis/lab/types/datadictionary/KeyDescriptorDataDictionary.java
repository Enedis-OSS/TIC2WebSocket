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
import java.util.Map;

/**
 * Key descriptor for values that are themselves data dictionaries.
 *
 * <p>This descriptor allows a key to be associated with a nested {@link DataDictionaryBase} value.
 * It supports conversion from strings, maps, and other data dictionary instances to the target
 * type.
 *
 * @param <T> the type of data dictionary accepted as value
 * @author Enedis Smarties team
 */
public class KeyDescriptorDataDictionary<T extends DataDictionaryBase>
    extends KeyDescriptorBase<T> {
  /** The class of the data dictionary accepted as value for this key. */
  private Class<T> dataDictionaryClass;

  /**
   * Constructs a key descriptor for a data dictionary value.
   *
   * @param name the key name (must not be null)
   * @param mandatory true if the key is mandatory, false otherwise
   * @param dataDictionaryClass the class of the data dictionary accepted as value
   */
  public KeyDescriptorDataDictionary(String name, boolean mandatory, Class<T> dataDictionaryClass) {
    super(name, mandatory);
    this.dataDictionaryClass = dataDictionaryClass;
  }

  /**
   * Converts an object to the data dictionary type T for this key. Accepts instances of T, strings
   * (parsed as JSON), other DataDictionary, or Map.
   *
   * @param value the object to convert
   * @return the converted data dictionary value
   * @throws DataDictionaryException if the value cannot be converted
   */
  @SuppressWarnings("unchecked")
  @Override
  public T convertValue(Object value) throws DataDictionaryException {
    T convertedValue = null;

    if (this.dataDictionaryClass.isAssignableFrom(value.getClass())) {
      convertedValue = this.dataDictionaryClass.cast(value);
    } else if (value instanceof String) {
      convertedValue = (T) DataDictionaryBase.fromString((String) value, this.dataDictionaryClass);
    } else if (value instanceof DataDictionary) {
      try {
        Constructor<T> constructor = this.dataDictionaryClass.getConstructor(DataDictionary.class);
        convertedValue = constructor.newInstance((DataDictionary) value);
      } catch (InvocationTargetException e) {
        throw new DataDictionaryException(
            "Key "
                + this.getName()
                + ": Cannot convert type "
                + value.getClass().getSimpleName()
                + " to "
                + this.dataDictionaryClass.getSimpleName()
                + " : "
                + e.getTargetException().getMessage());
      } catch (NoSuchMethodException
          | SecurityException
          | InstantiationException
          | IllegalAccessException
          | IllegalArgumentException e) {
        throw new DataDictionaryException(
            "Key "
                + this.getName()
                + ": Cannot convert type "
                + value.getClass().getSimpleName()
                + " to "
                + this.dataDictionaryClass.getSimpleName()
                + " : "
                + e.getMessage());
      }
    } else if (value instanceof Map<?, ?>) {
      try {
        Constructor<T> constructor = this.dataDictionaryClass.getConstructor(Map.class);
        convertedValue = constructor.newInstance((Map<?, ?>) value);
      } catch (InvocationTargetException e) {
        throw new DataDictionaryException(
            "Key "
                + this.getName()
                + ": Cannot convert type "
                + value.getClass().getSimpleName()
                + " to "
                + this.dataDictionaryClass.getSimpleName()
                + " : "
                + e.getTargetException().getMessage());
      } catch (NoSuchMethodException
          | SecurityException
          | InstantiationException
          | IllegalAccessException
          | IllegalArgumentException e) {
        throw new DataDictionaryException(
            "Key "
                + this.getName()
                + ": Cannot convert type "
                + value.getClass().getSimpleName()
                + " to "
                + this.dataDictionaryClass.getSimpleName()
                + " : "
                + e.getMessage());
      }
    } else {
      throw new DataDictionaryException(
          "Key "
              + this.getName()
              + ": Cannot convert type "
              + value.getClass().getSimpleName()
              + " to "
              + this.dataDictionaryClass.getSimpleName());
    }

    return convertedValue;
  }
}
