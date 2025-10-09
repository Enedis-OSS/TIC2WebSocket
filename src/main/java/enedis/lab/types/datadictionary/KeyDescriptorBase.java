// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.types.datadictionary;

import enedis.lab.types.DataDictionaryException;
import java.util.Arrays;
import java.util.List;

/**
 * Abstract base class for key descriptors in a data dictionary.
 *
 * <p>Provides common logic for key name, mandatory flag, accepted values, and value
 * conversion/validation. Subclasses must implement the type-specific conversion logic.
 *
 * @param <T> the type of value associated with the key
 * @author Enedis Smarties team
 */
public abstract class KeyDescriptorBase<T> implements KeyDescriptor<T> {
  /** The name of the key described by this descriptor. */
  private String name;

  /** Whether the key is mandatory in the data dictionary. */
  private boolean mandatory;

  /** List of accepted values for this key, or null if any value is accepted. */
  protected List<T> acceptedValues;

  /**
   * Constructs a key descriptor with the given name and mandatory flag.
   *
   * @param name the key name (must not be null)
   * @param mandatory true if the key is mandatory, false otherwise
   */
  public KeyDescriptorBase(String name, boolean mandatory) {
    super();
    this.name = name;
    this.mandatory = mandatory;
  }

  /**
   * Converts an object to the value type T for this key, checking accepted values if set.
   *
   * @param value the object to convert
   * @return the converted value of type T
   * @throws DataDictionaryException if the value is null and mandatory, or not accepted, or cannot
   *     be converted
   */
  @Override
  public final T convert(Object value) throws DataDictionaryException {
    T convertedValue = null;

    if (value == null) {
      this.handleNullValue();
      return null;
    }

    convertedValue = this.convertValue(value);

    this.checkAcceptedValues(convertedValue);

    return convertedValue;
  }

  /**
   * Returns the string representation of a value of type T.
   *
   * @param value the value to convert to String
   * @return the string representation, or null if value is null
   */
  @Override
  public String toString(T value) {
    if (value == null) {
      return null;
    }

    return value.toString();
  }

  /**
   * Returns the name of the key described by this descriptor.
   *
   * @return the key name
   */
  @Override
  public String getName() {
    return this.name;
  }

  /**
   * Sets the name of the key described by this descriptor.
   *
   * @param name the key name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Indicates whether the key is mandatory in the data dictionary.
   *
   * @return true if the key is mandatory, false otherwise
   */
  @Override
  public boolean isMandatory() {
    return this.mandatory;
  }

  /**
   * Sets whether the key is mandatory in the data dictionary.
   *
   * @param mandatory true to mark the key as mandatory, false otherwise
   */
  @Override
  public void setMandatory(boolean mandatory) {
    this.mandatory = mandatory;
  }

  /**
   * Sets the list of accepted values for this key.
   *
   * @param acceptedValues the accepted values for this key
   */
  @SuppressWarnings("unchecked")
  @Override
  public void setAcceptedValues(T... acceptedValues) {
    this.acceptedValues = Arrays.asList(acceptedValues);
  }

  /**
   * Converts an object to the value type T for this key (type-specific logic). Subclasses must
   * implement this method.
   *
   * @param value the object to convert
   * @return the converted value of type T
   * @throws DataDictionaryException if the value cannot be converted
   */
  protected abstract T convertValue(Object value) throws DataDictionaryException;

  /**
   * Handles the case where a null value is set for this key. Throws an exception if the key is
   * mandatory.
   *
   * @throws DataDictionaryException if the key is mandatory
   */
  protected final void handleNullValue() throws DataDictionaryException {
    if (this.isMandatory()) {
      throw new DataDictionaryException("Cannot set null " + this.getName());
    }
  }

  /**
   * Checks if the given value is among the accepted values for this key, if any are set. Throws an
   * exception if the value is not accepted.
   *
   * @param value the value to check
   * @throws DataDictionaryException if the value is not accepted
   */
  protected void checkAcceptedValues(T value) throws DataDictionaryException {
    if (this.acceptedValues != null) {
      boolean accepted = false;

      for (T v : this.acceptedValues) {
        if (v.equals(value)) {
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
}
