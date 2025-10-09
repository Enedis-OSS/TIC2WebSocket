// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.util;

/**
 * Utility class for checking if values are within a specified numeric range.
 *
 * <p>This class provides methods to set minimum and maximum bounds and to check whether a given value
 * falls within the defined range. It is designed for general-purpose range validation.
 *
 * <p>Common use cases include:
 * <ul>
 *   <li>Validating input values against numeric constraints</li>
 *   <li>Ensuring values remain within allowed limits</li>
 *   <li>Supporting configuration or parameter validation</li>
 * </ul>
 *
 * @author Enedis Smarties team
 */
public class MinMaxChecker {
  private Number min;
  private Number max;

  /** Default constructor */
  public MinMaxChecker() {
    super();
  }

  /**
   * Constructor setting parameters
   *
   * @param min
   * @param max
   */
  public MinMaxChecker(Number min, Number max) {
    this();
    this.setMin(min);
    this.setMax(max);
  }

  /**
   * Get min
   *
   * @return min
   */
  public Number getMin() {
    return this.min;
  }

  /**
   * Set min
   *
   * @param min
   * @throws IllegalArgumentException if min is greater than max
   */
  public void setMin(Number min) {
    if (min != null) {
      if (this.max != null && min.doubleValue() > this.max.doubleValue()) {
        throw new IllegalArgumentException("min can't be greater than max");
      }
    }
    this.min = min;
  }

  /**
   * Get max
   *
   * @return max
   */
  public Number getMax() {
    return this.max;
  }

  /**
   * Set max
   *
   * @param max
   * @throws IllegalArgumentException if max is smaller than min
   */
  public void setMax(Number max) {
    if (max != null) {
      if (this.min != null && max.doubleValue() < this.min.doubleValue()) {
        throw new IllegalArgumentException("max can't be smaller than min");
      }
    }
    this.max = max;
  }

  /**
   * Check the given value
   *
   * @param value
   * @return true if the value is in [min, max]
   */
  public boolean check(Number value) {
    if (value != null) {
      if (this.min != null) {
        if (value.doubleValue() < this.min.doubleValue()) {
          return false;
        }
      }
      if (this.max != null) {
        if (value.doubleValue() > this.max.doubleValue()) {
          return false;
        }
      }
      return true;
    } else {
      return false;
    }
  }
}
