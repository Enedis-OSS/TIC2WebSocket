package tic.util.descriptor;

/**
 * Base class providing validation utilities for descriptors.
 *
 * <p>This class offers common validation methods for subclasses to ensure that descriptor values
 * meet specified criteria, such as non-nullity and range constraints.
 */
public class Descriptor {

  protected Number validateNumber(Number value, String key) {
    if (value == null) {
      throw new IllegalArgumentException("Value '" + key + "' cannot be null");
    }
    return value;
  }

  protected Number validateNumber(Number value, String key, int min, int max) {
    if (value == null) {
      throw new IllegalArgumentException("Value '" + key + "' cannot be null");
    }
    if (value.doubleValue() < min || value.doubleValue() > max) {
      throw new IllegalArgumentException(
          "Value '"
              + key
              + "' must be in range ["
              + min
              + "-"
              + max
              + "], but got: "
              + value);
    }
    return value;
  }

  protected String validateString(String value, String key) {
    if (value == null) {
      throw new IllegalArgumentException("Value '" + key + "' cannot be null");
    }
    String trimmed = value.trim();
    return trimmed.isEmpty() ? null : trimmed;
  }
}
