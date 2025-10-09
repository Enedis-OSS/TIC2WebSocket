// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.types.datadictionary;

import enedis.lab.types.DataDictionaryException;
import enedis.lab.util.MinMaxChecker;
import java.util.List;

/**
 * Key descriptor for list values with minimum and maximum size constraints.
 *
 * <p>This descriptor extends {@link KeyDescriptorList} to enforce that the list size is within
 * specified bounds. It uses a {@link MinMaxChecker} to validate the size after conversion.
 *
 * @param <T> the type of item in the list
 * @author Enedis Smarties team
 */
public class KeyDescriptorListMinMaxSize<T> extends KeyDescriptorList<T> {
  /** Utility for checking minimum and maximum list size constraints. */
  private MinMaxChecker minMaxChecker;

  /**
   * Constructs a key descriptor for a list value with no size constraints.
   *
   * @param name the key name (must not be null)
   * @param mandatory true if the key is mandatory, false otherwise
   * @param itemClass the class of the item accepted in the list
   */
  public KeyDescriptorListMinMaxSize(String name, boolean mandatory, Class<T> itemClass) {
    this(name, mandatory, itemClass, null, null);
  }

  /**
   * Constructs a key descriptor for a list value with minimum and maximum size constraints.
   *
   * @param name the key name (must not be null)
   * @param mandatory true if the key is mandatory, false otherwise
   * @param itemClass the class of the item accepted in the list
   * @param min the minimum allowed list size (nullable)
   * @param max the maximum allowed list size (nullable)
   */
  public KeyDescriptorListMinMaxSize(
      String name, boolean mandatory, Class<T> itemClass, Integer min, Integer max) {
    super(name, mandatory, itemClass);
    this.minMaxChecker = new MinMaxChecker(min, max);
  }

  /**
   * Converts an object to a list of items of type T and checks the list size constraints.
   *
   * @param value the object to convert
   * @return the converted list of items
   * @throws DataDictionaryException if the value or any item cannot be converted, or if the list
   *     size is out of bounds
   */
  @Override
  public List<T> convertValue(Object value) throws DataDictionaryException {
    List<T> convertedValue = super.convertValue(value);
    this.check(convertedValue);
    return convertedValue;
  }

  /**
   * Returns the minimum allowed list size, or null if not set.
   *
   * @return the minimum allowed list size
   */
  public Integer getMin() {
    return this.minMaxChecker.getMin().intValue();
  }

  /**
   * Sets the minimum allowed list size.
   *
   * @param min the minimum allowed list size
   * @throws IllegalArgumentException if min is greater than max
   */
  public void setMin(Integer min) {
    this.minMaxChecker.setMin(min);
  }

  /**
   * Returns the maximum allowed list size, or null if not set.
   *
   * @return the maximum allowed list size
   */
  public Integer getMax() {
    return this.minMaxChecker.getMax().intValue();
  }

  /**
   * Sets the maximum allowed list size.
   *
   * @param max the maximum allowed list size
   * @throws IllegalArgumentException if max is smaller than min
   */
  public void setMax(Integer max) {
    this.minMaxChecker.setMax(max);
  }

  /**
   * Checks that the list size is within the allowed bounds.
   *
   * @param list the list to check
   * @throws DataDictionaryException if the list size is out of bounds
   */
  private void check(List<T> list) throws DataDictionaryException {
    if (list != null) {
      if (!this.minMaxChecker.check(list.size())) {
        throw new DataDictionaryException(
            "Key " + this.getName() + ": value size (" + list.size() + ") out of bound");
      }
    }
  }
}
