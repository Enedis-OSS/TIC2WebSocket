// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.types.datadictionary;

import enedis.lab.types.DataDictionaryException;

/** DataDictionary key descriptor String */
public class KeyDescriptorString extends KeyDescriptorBase<String> {
  private static final boolean DEFAULT_EMPTY_ALLOW_FLAG = true;

  private boolean emptyAllow;

  /**
   * Default constructor
   *
   * @param name
   * @param mandatory
   */
  public KeyDescriptorString(String name, boolean mandatory) {
    this(name, mandatory, DEFAULT_EMPTY_ALLOW_FLAG);
  }

  /**
   * Default constructor
   *
   * @param name
   * @param mandatory
   * @param emptyAllow
   */
  public KeyDescriptorString(String name, boolean mandatory, boolean emptyAllow) {
    super(name, mandatory);
    this.emptyAllow = emptyAllow;
  }

  @Override
  public String convertValue(Object value) throws DataDictionaryException {
    String convertedValue = null;

    convertedValue = value.toString();

    this.check(convertedValue);

    return convertedValue;
  }

  /**
   * Get empty allow flag
   *
   * @return empty allow flag
   */
  public boolean isEmptyAllow() {
    return this.emptyAllow;
  }

  /**
   * Set empty allow flag
   *
   * @param emptyAllow
   */
  public void setEmptyAllow(boolean emptyAllow) {
    this.emptyAllow = emptyAllow;
  }

  private void check(String value) throws DataDictionaryException {
    if (!this.emptyAllow && value.trim().isEmpty()) {
      throw new DataDictionaryException("Key " + this.getName() + ": value can't be empty");
    }
  }
}
