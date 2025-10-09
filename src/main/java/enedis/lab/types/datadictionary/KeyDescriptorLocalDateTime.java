// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.types.datadictionary;

import enedis.lab.types.DataDictionaryException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Key descriptor for values that are {@link LocalDateTime} instances.
 *
 * <p>This descriptor allows a key to be associated with a date-time value, supporting conversion
 * from strings using a configurable pattern. It provides default formatting and parsing logic for
 * date-time values.
 *
 * @author Enedis Smarties team
 */
public class KeyDescriptorLocalDateTime extends KeyDescriptorBase<LocalDateTime> {
  /** Default date-time pattern used for formatting and parsing. */
  public static final String DEFAULT_PATTERN = "dd/MM/yyyy HH:mm:ss";

  /** Default formatter used for date-time values. */
  public static final DateTimeFormatter DEFAULT_FORMATTER =
      DateTimeFormatter.ofPattern(DEFAULT_PATTERN);

  /** Formatter used for this key descriptor. */
  private DateTimeFormatter formatter;

  /**
   * Constructs a key descriptor for a date-time value using the default pattern.
   *
   * @param name the key name (must not be null)
   * @param mandatory true if the key is mandatory, false otherwise
   */
  public KeyDescriptorLocalDateTime(String name, boolean mandatory) {
    this(name, mandatory, DEFAULT_PATTERN);
  }

  /**
   * Constructs a key descriptor for a date-time value using a custom pattern.
   *
   * @param name the key name (must not be null)
   * @param mandatory true if the key is mandatory, false otherwise
   * @param formatterPattern the date-time pattern to use for formatting and parsing
   */
  public KeyDescriptorLocalDateTime(String name, boolean mandatory, String formatterPattern) {
    super(name, mandatory);
    this.formatter = DateTimeFormatter.ofPattern(formatterPattern);
  }

  /**
   * Converts a {@link LocalDateTime} value to its string representation using the configured
   * formatter.
   *
   * @param value the date-time value to convert
   * @return the formatted string, or null if value is null
   */
  @Override
  public String toString(LocalDateTime value) {
    if (value == null) {
      return null;
    }
    return this.formatter.format(value);
  }

  /**
   * Converts an object to a {@link LocalDateTime} value for this key. Accepts {@link LocalDateTime}
   * instances or strings (parsed using the configured formatter).
   *
   * @param value the object to convert
   * @return the converted date-time value
   * @throws DataDictionaryException if the value cannot be converted
   */
  @Override
  public LocalDateTime convertValue(Object value) throws DataDictionaryException {
    LocalDateTime convertedValue = null;
    if (value instanceof LocalDateTime) {
      convertedValue = (LocalDateTime) value;
    } else if (value instanceof String) {
      convertedValue = this.toLocalDateTime((String) value);
    } else {
      throw new DataDictionaryException(
          "Key "
              + this.getName()
              + ": Cannot convert type "
              + value.getClass().getSimpleName()
              + " to LocalDateTime");
    }
    return convertedValue;
  }

  /**
   * Parses a string to a {@link LocalDateTime} using the configured formatter.
   *
   * @param value the string to parse
   * @return the parsed date-time value
   * @throws DataDictionaryException if the string cannot be parsed
   */
  private LocalDateTime toLocalDateTime(String value) throws DataDictionaryException {
    LocalDateTime convertedValue = null;
    try {
      convertedValue = LocalDateTime.parse(value, this.formatter);
    } catch (DateTimeParseException e) {
      throw new DataDictionaryException(
          "Key " + this.getName() + ": string " + value + " cannot be converted to LocalDateTime");
    }
    return convertedValue;
  }
}
