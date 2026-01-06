// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.util;

/**
 * Utility class for validating values such as numbers and strings.
 *
 * <p>This class provides methods to validate numerical ranges and string properties, throwing
 * exceptions for invalid inputs. It is useful for ensuring data integrity in various contexts.
 *
 * <p>Common use cases include:
 *
 * <ul>
 *   <li>Validating numerical inputs against specified ranges
 *   <li>Checking for null or empty strings
 *   <li>Ensuring data consistency before processing
 * </ul>
 *
 * @author Enedis Smarties team
 */
public class ValueChecker {

  public static Number validateNumber(Number value, String key, boolean allowNull) {
    if (value == null) {
      if (!allowNull) {
        throw new IllegalArgumentException("Value '" + key + "' cannot be null");
      }
      return null;
    }
    return value;
  }

  public static Number validateNumber(
      Number value, String key, int min, int max, boolean allowNull) {
    if (value == null) {
      if (!allowNull) {
        throw new IllegalArgumentException("Value '" + key + "' cannot be null");
      }
      return null;
    }
    if (value.doubleValue() < min || value.doubleValue() > max) {
      throw new IllegalArgumentException(
          "Value '" + key + "' must be in range [" + min + "-" + max + "], but got: " + value);
    }
    return value;
  }

  public static String validateString(
      String value, String key, boolean allowNull, boolean allowEmpty) {
    if (value == null && allowNull) {
      return null;
    }

    if (value == null && !allowNull) {
      throw new IllegalArgumentException("Value '" + key + "' cannot be null");
    }

    if (!allowEmpty && value.isEmpty()) {
      throw new IllegalArgumentException("Value '" + key + "' cannot be empty");
    }

    return value;
  }
}
