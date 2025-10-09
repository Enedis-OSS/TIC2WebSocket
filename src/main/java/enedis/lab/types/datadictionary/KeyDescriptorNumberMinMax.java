// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.types.datadictionary;

import enedis.lab.types.DataDictionaryException;
import enedis.lab.util.MinMaxChecker;

/**
 * Key descriptor for numeric values with minimum and maximum constraints.
 *
 * <p>This descriptor extends {@link KeyDescriptorNumber} to enforce that the value is within
 * specified bounds. It uses a {@link MinMaxChecker} to validate the value after conversion.
 *
 * @author Enedis Smarties team
 */
public class KeyDescriptorNumberMinMax extends KeyDescriptorNumber {
  /** Utility for checking minimum and maximum value constraints. */
  private MinMaxChecker minMaxChecker;

  /**
   * Constructs a key descriptor for a numeric value with no min/max constraints.
   *
   * @param name the key name (must not be null)
   * @param mandatory true if the key is mandatory, false otherwise
   */
  public KeyDescriptorNumberMinMax(String name, boolean mandatory) {
    this(name, mandatory, null, null);
  }

  /**
   * Constructs a key descriptor for a numeric value with minimum and maximum constraints.
   *
   * @param name the key name (must not be null)
   * @param mandatory true if the key is mandatory, false otherwise
   * @param min the minimum allowed value (nullable)
   * @param max the maximum allowed value (nullable)
   */
  public KeyDescriptorNumberMinMax(String name, boolean mandatory, Number min, Number max) {
    super(name, mandatory);
    this.minMaxChecker = new MinMaxChecker(min, max);
  }

  /**
   * Converts an object to a {@link Number} value and checks the min/max constraints.
   *
   * @param value the object to convert
   * @return the converted number value
   * @throws DataDictionaryException if the value cannot be converted or is out of bounds
   */
  @Override
  public Number convertValue(Object value) throws DataDictionaryException {
    Number convertedValue = super.convertValue(value);
    this.check(convertedValue);
    return convertedValue;
  }

  /**
   * Returns the minimum allowed value, or null if not set.
   *
   * @return the minimum allowed value
   */
  public Number getMin() {
    return this.minMaxChecker.getMin();
  }

  /**
   * Sets the minimum allowed value.
   *
   * @param min the minimum allowed value
   * @throws IllegalArgumentException if min is greater than max
   */
  public void setMin(Number min) {
    this.minMaxChecker.setMin(min);
  }

  /**
   * Returns the maximum allowed value, or null if not set.
   *
   * @return the maximum allowed value
   */
  public Number getMax() {
    return this.minMaxChecker.getMax();
  }

  /**
   * Sets the maximum allowed value.
   *
   * @param max the maximum allowed value
   * @throws IllegalArgumentException if max is smaller than min
   */
  public void setMax(Number max) {
    this.minMaxChecker.setMax(max);
  }

  /**
   * Checks that the value is within the allowed bounds.
   *
   * @param convertedValue the value to check
   * @throws DataDictionaryException if the value is out of bounds
   */
  private void check(Number convertedValue) throws DataDictionaryException {
    if (convertedValue != null) {
      if (!this.minMaxChecker.check(convertedValue)) {
        throw new DataDictionaryException(
            "Key " + this.getName() + ": value (" + convertedValue + ") out of bound");
      }
    }
  }
}
