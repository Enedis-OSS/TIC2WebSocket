// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.types.datadictionary;

import enedis.lab.types.DataDictionaryException;

/**
 * Key descriptor for values that are strings.
 *
 * <p>This descriptor allows a key in a data dictionary to be associated with a string value, with
 * optional control over whether empty strings are allowed. Provides conversion and validation logic
 * for string values.
 *
 * @author Enedis Smarties team
 */
public class KeyDescriptorString extends KeyDescriptorBase<String> {
  /** Default flag indicating whether empty strings are allowed. */
  private static final boolean DEFAULT_EMPTY_ALLOW_FLAG = true;

  /** Indicates whether empty strings are allowed for this key. */
  private boolean emptyAllow;

  /**
   * Constructs a string key descriptor with the given name and mandatory flag.
   *
   * <p>By default, empty strings are allowed.
   *
   * @param name the key name (must not be null)
   * @param mandatory true if the key is mandatory, false otherwise
   */
  public KeyDescriptorString(String name, boolean mandatory) {
    this(name, mandatory, DEFAULT_EMPTY_ALLOW_FLAG);
  }

  /**
   * Constructs a string key descriptor with the given name, mandatory flag, and empty string
   * allowance.
   *
   * @param name the key name (must not be null)
   * @param mandatory true if the key is mandatory, false otherwise
   * @param emptyAllow true if empty strings are allowed, false otherwise
   */
  public KeyDescriptorString(String name, boolean mandatory, boolean emptyAllow) {
    super(name, mandatory);
    this.emptyAllow = emptyAllow;
  }

  /**
   * Converts the given object to a string value for this key, applying validation rules.
   *
   * <p>The value is converted using {@code toString()} and checked for emptiness if not allowed.
   *
   * @param value the object to convert
   * @return the converted string value
   * @throws DataDictionaryException if the value is empty and empty strings are not allowed
   */
  @Override
  public String convertValue(Object value) throws DataDictionaryException {
    String convertedValue = value.toString();
    this.check(convertedValue);
    return convertedValue;
  }

  /**
   * Returns whether empty strings are allowed for this key.
   *
   * @return true if empty strings are allowed, false otherwise
   */
  public boolean isEmptyAllow() {
    return this.emptyAllow;
  }

  /**
   * Sets whether empty strings are allowed for this key.
   *
   * @param emptyAllow true to allow empty strings, false to disallow
   */
  public void setEmptyAllow(boolean emptyAllow) {
    this.emptyAllow = emptyAllow;
  }

  /**
   * Checks if the given string value is valid according to the empty string allowance.
   *
   * @param value the string value to check
   * @throws DataDictionaryException if the value is empty and empty strings are not allowed
   */
  private void check(String value) throws DataDictionaryException {
    if (!this.emptyAllow && value.trim().isEmpty()) {
      throw new DataDictionaryException("Key " + this.getName() + ": value can't be empty");
    }
  }
}
