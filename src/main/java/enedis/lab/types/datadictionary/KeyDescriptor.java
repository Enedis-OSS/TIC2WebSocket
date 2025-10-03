// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.types.datadictionary;

import enedis.lab.types.DataDictionaryException;

/**
 * DataDictionary key descriptor interface
 *
 * @param <T>
 */
public interface KeyDescriptor<T> {
  /**
   * Get key name
   *
   * @return key name
   */
  public String getName();

  /**
   * Get mandatory flag
   *
   * @return mandatory flag
   */
  public boolean isMandatory();

  /**
   * Set mandatory flag
   *
   * @param mandatory
   */
  public void setMandatory(boolean mandatory);

  /**
   * Set a list of accepted values
   *
   * @param acceptedValues
   */
  @SuppressWarnings("unchecked")
  public void setAcceptedValues(T... acceptedValues);

  /**
   * Convert a Object value to a T value
   *
   * @param value object to convert
   * @return value converted to T type
   * @throws DataDictionaryException
   */
  public T convert(Object value) throws DataDictionaryException;

  /**
   * Convert a T value to String
   *
   * @param value value to convert to String
   * @return String representation of the value
   */
  public String toString(T value);
}
