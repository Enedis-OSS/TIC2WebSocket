// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.types.datadictionary;

import enedis.lab.types.DataDictionaryException;

/**
 * Interface for describing a key in a data dictionary structure.
 *
 * <p>A key descriptor defines the name, type, and validation rules for a key in a {@link
 * enedis.lab.types.DataDictionary}. Implementations may specify accepted values, conversion logic,
 * and whether the key is mandatory.
 *
 * @param <T> the type of value associated with the key
 * @author Enedis Smarties team
 */
public interface KeyDescriptor<T> {

  /**
   * Returns the name of the key described by this descriptor.
   *
   * @return the key name (never null)
   */
  String getName();

  /**
   * Indicates whether the key is mandatory in the data dictionary.
   *
   * @return true if the key is mandatory, false otherwise
   */
  boolean isMandatory();

  /**
   * Sets whether the key is mandatory in the data dictionary.
   *
   * @param mandatory true to mark the key as mandatory, false otherwise
   */
  void setMandatory(boolean mandatory);

  /**
   * Sets the list of accepted values for this key. If not set, any value of type T may be accepted
   * depending on implementation.
   *
   * @param acceptedValues the accepted values for this key
   */
  @SuppressWarnings("unchecked")
  void setAcceptedValues(T... acceptedValues);

  /**
   * Converts an object to the value type T for this key. Implementations should validate and
   * convert the input as needed.
   *
   * @param value the object to convert
   * @return the converted value of type T
   * @throws DataDictionaryException if the value cannot be converted or is invalid
   */
  T convert(Object value) throws DataDictionaryException;

  /**
   * Converts a value of type T to its string representation.
   *
   * @param value the value to convert to String
   * @return the string representation of the value
   */
  String toString(T value);
}
