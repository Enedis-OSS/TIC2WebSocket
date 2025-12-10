// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.frame.group;

public class TICGroup {
  private String label;
  private String value;
  private boolean isValid;

  /**
   * Constructs a TIC group with the specified label and value.
   *
   * @param label the label of the group
   * @param value the value of the group
   */
  public TICGroup(String label, String value) {
    this.label = label;
    this.value = value;
    this.isValid = true;
  }

  /**
   * Constructs a TIC group with the specified label, value, and validity.
   *
   * @param label the label of the group
   * @param value the value of the group
   * @param isValid the validity of the group
   */
  public TICGroup(String label, String value, boolean isValid) {
    this.label = label;
    this.value = value;
    this.isValid = isValid;
  }

  @Override
  public int hashCode() {
    return this.label.hashCode() + this.value.hashCode() + Boolean.hashCode(this.isValid);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || this.getClass() != obj.getClass()) {
      return false;
    }
    TICGroup other = (TICGroup) obj;
    return this.label.equals(other.label)
        && this.value.equals(other.value)
        && this.isValid == other.isValid;
  }

  /**
   * Sets the validity of the TIC group.
   *
   * @param isValid true if the group is valid, false otherwise
   */
  public boolean isValid() {
    return isValid;
  }

  /**
   * Returns the label of the TIC group.
   *
   * @return the label of the group
   */
  public String getLabel() {
    return label;
  }

  /**
   * Returns the value of the TIC group.
   *
   * @return the value of the group
   */
  public String getValue() {
    return value;
  }
}
