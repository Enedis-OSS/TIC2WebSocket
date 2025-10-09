// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.types.datadictionary;

import enedis.lab.types.DataDictionaryException;

/**
 * Key descriptor for values that are {@link Number} instances.
 *
 * <p>This descriptor allows a key to be associated with a numeric value, supporting conversion from
 * strings and type-safe validation. It provides logic for accepted values and conversion from
 * string representations.
 *
 * @author Enedis Smarties team
 */
public class KeyDescriptorNumber extends KeyDescriptorBase<Number> {
  /**
   * Constructs a key descriptor for a numeric value.
   *
   * @param name the key name (must not be null)
   * @param mandatory true if the key is mandatory, false otherwise
   */
  public KeyDescriptorNumber(String name, boolean mandatory) {
    super(name, mandatory);
  }

  /**
   * Converts an object to a {@link Number} value for this key. Accepts {@link Number} instances or
   * strings (parsed as double).
   *
   * @param value the object to convert
   * @return the converted number value
   * @throws DataDictionaryException if the value cannot be converted
   */
  @Override
  public Number convertValue(Object value) throws DataDictionaryException {
    Number convertedValue = null;

    if (value instanceof Number) {
      convertedValue = (Number) value;
    } else if (value instanceof String) {
      convertedValue = this.toNumber((String) value);
    } else {
      throw new DataDictionaryException(
          "Key "
              + this.getName()
              + ": Cannot convert type "
              + value.getClass().getSimpleName()
              + " to Number");
    }

    return convertedValue;
  }

  /**
   * Checks if the given value is among the accepted values for this key, if any are set. Uses
   * double value comparison for numeric equality.
   *
   * @param value the value to check
   * @throws DataDictionaryException if the value is not accepted
   */
  @Override
  protected void checkAcceptedValues(Number value) throws DataDictionaryException {
    if (this.acceptedValues != null) {
      boolean accepted = false;

      for (Number v : this.acceptedValues) {
        if (v.doubleValue() == value.doubleValue()) {
          accepted = true;
          break;
        }
      }

      if (!accepted) {
        throw new DataDictionaryException(
            "Key " + this.getName() + " doesn't respect mandatory value");
      }
    }
  }

  /**
   * Converts a string to a {@link Number} (as double).
   *
   * @param value the string to convert
   * @return the parsed number
   * @throws DataDictionaryException if the string cannot be parsed as a number
   */
  private Number toNumber(String value) throws DataDictionaryException {
    Number out = null;
    try {
      out = Double.valueOf((String) value);
    } catch (NumberFormatException e) {
      throw new DataDictionaryException(
          "Key "
              + this.getName()
              + ": Cannot convert type "
              + value.getClass().getSimpleName()
              + " to Number");
    }
    return out;
  }
}
