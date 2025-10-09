// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.types.datadictionary;

import enedis.lab.types.DataDictionaryException;

/**
 * Key descriptor for values that are Java enums.
 *
 * <p>This descriptor allows a key to be associated with an enum value, supporting conversion from
 * strings and type-safe validation. Optionally, a prefix can be added to the string value before
 * conversion.
 *
 * @param <T> the enum type accepted as value
 * @author Enedis Smarties team
 */
@SuppressWarnings("rawtypes")
public class KeyDescriptorEnum<T extends Enum> extends KeyDescriptorBase<T> {
  /** The enum class accepted as value for this key. */
  private Class<T> enumClass;

  /** Optional prefix to prepend to the string value before conversion. */
  private String prefix;

  /**
   * Constructs a key descriptor for an enum value with no prefix.
   *
   * @param name the key name (must not be null)
   * @param mandatory true if the key is mandatory, false otherwise
   * @param enumClass the enum class accepted as value
   */
  public KeyDescriptorEnum(String name, boolean mandatory, Class<T> enumClass) {
    this(name, mandatory, enumClass, "");
  }

  /**
   * Constructs a key descriptor for an enum value with a prefix.
   *
   * @param name the key name (must not be null)
   * @param mandatory true if the key is mandatory, false otherwise
   * @param enumClass the enum class accepted as value
   * @param prefix the prefix to prepend to the string value before conversion
   */
  public KeyDescriptorEnum(String name, boolean mandatory, Class<T> enumClass, String prefix) {
    super(name, mandatory);
    this.enumClass = enumClass;
    this.prefix = prefix;
  }

  /**
   * Converts an object to the enum type T for this key. Accepts instances of T or strings
   * (converted to enum constant).
   *
   * @param value the object to convert
   * @return the converted enum value
   * @throws DataDictionaryException if the value cannot be converted
   */
  @Override
  public T convertValue(Object value) throws DataDictionaryException {
    T convertedValue = null;

    if (this.enumClass.isAssignableFrom(value.getClass())) {
      convertedValue = this.enumClass.cast(value);
    } else if (value instanceof String) {
      convertedValue = this.toEnum((String) value);
    } else {
      throw new DataDictionaryException(
          "Key "
              + this.getName()
              + ": Cannot convert type "
              + value.getClass().getSimpleName()
              + " to "
              + this.enumClass.getSimpleName());
    }

    return convertedValue;
  }

  /**
   * Converts a string to the enum type T, applying the prefix if set. The string is uppercased and
   * dots/hyphens are replaced with underscores.
   *
   * @param value the string value to convert
   * @return the corresponding enum constant
   * @throws DataDictionaryException if the value does not match any enum constant
   */
  @SuppressWarnings("unchecked")
  protected T toEnum(String value) throws DataDictionaryException {
    T convertedValue = null;
    try {
      String preparedValue = this.prefix + value.toUpperCase().replace(".", "_").replace("-", "_");
      convertedValue = (T) Enum.valueOf(this.enumClass, preparedValue);
    } catch (IllegalArgumentException e) {
      throw new DataDictionaryException(
          "Key "
              + this.getName()
              + ": "
              + this.enumClass.getSimpleName()
              + " doesn't contain "
              + value);
    }
    return convertedValue;
  }
}
