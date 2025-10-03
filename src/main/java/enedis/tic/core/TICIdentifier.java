// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.tic.core;

import enedis.lab.types.DataDictionary;
import enedis.lab.types.DataDictionaryException;
import enedis.lab.types.datadictionary.DataDictionaryBase;
import enedis.lab.types.datadictionary.KeyDescriptor;
import enedis.lab.types.datadictionary.KeyDescriptorString;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TICIdentifier class
 *
 * <p>Generated
 */
public class TICIdentifier extends DataDictionaryBase {
  protected static final String KEY_PORT_ID = "portId";
  protected static final String KEY_PORT_NAME = "portName";
  protected static final String KEY_SERIAL_NUMBER = "serialNumber";

  private List<KeyDescriptor<?>> keys = new ArrayList<KeyDescriptor<?>>();

  protected KeyDescriptorString kPortId;
  protected KeyDescriptorString kPortName;
  protected KeyDescriptorString kSerialNumber;

  protected TICIdentifier() {
    super();
    this.loadKeyDescriptors();
  }

  /**
   * Constructor using map
   *
   * @param map
   * @throws DataDictionaryException
   */
  public TICIdentifier(Map<String, Object> map) throws DataDictionaryException {
    this();
    this.copy(fromMap(map));
  }

  /**
   * Constructor using datadictionary
   *
   * @param other
   * @throws DataDictionaryException
   */
  public TICIdentifier(DataDictionary other) throws DataDictionaryException {
    this();
    this.copy(other);
  }

  /**
   * Constructor setting parameters to specific values
   *
   * @param portId
   * @param portName
   * @param serialNumber
   * @throws DataDictionaryException
   */
  public TICIdentifier(String portId, String portName, String serialNumber)
      throws DataDictionaryException {
    this();

    this.setPortId((Object) portId);
    this.setPortName((Object) portName);
    this.setSerialNumber((Object) serialNumber);

    this.checkAndUpdate();
  }

  @Override
  protected void updateOptionalParameters() throws DataDictionaryException {
    if (this.getPortId() == null && this.getPortName() == null && this.getSerialNumber() == null) {
      throw new DataDictionaryException("Empty TICIdentifier not allowed!");
    }
  }

  public boolean matches(TICIdentifier identifier) {
    if (identifier != null) {
      if (this.getSerialNumber() != null && identifier.getSerialNumber() != null) {
        return this.getSerialNumber().equals(identifier.getSerialNumber());
      }
      if (this.getPortId() != null && identifier.getPortId() != null) {
        return this.getPortId().equals(identifier.getPortId());
      }
      if (this.getPortName() != null && identifier.getPortName() != null) {
        return this.getPortName().equals(identifier.getPortName());
      }
    }

    return false;
  }

  /**
   * Get port id
   *
   * @return the port id
   */
  public String getPortId() {
    return (String) this.data.get(KEY_PORT_ID);
  }

  /**
   * Get port name
   *
   * @return the port name
   */
  public String getPortName() {
    return (String) this.data.get(KEY_PORT_NAME);
  }

  /**
   * Get serial number
   *
   * @return the serial number
   */
  public String getSerialNumber() {
    return (String) this.data.get(KEY_SERIAL_NUMBER);
  }

  /**
   * Set port id
   *
   * @param portId
   * @throws DataDictionaryException
   */
  public void setPortId(String portId) throws DataDictionaryException {
    if (portId == null && this.getPortName() == null && this.getSerialNumber() == null) {
      throw new DataDictionaryException(
          "Cannot set null portId because empty TICIdentifier not allowed!");
    }
    this.setPortId((Object) portId);
  }

  /**
   * Set port name
   *
   * @param portName
   * @throws DataDictionaryException
   */
  public void setPortName(String portName) throws DataDictionaryException {
    if (this.getPortId() == null && portName == null && this.getSerialNumber() == null) {
      throw new DataDictionaryException(
          "Cannot set null portName because empty TICIdentifier not allowed!");
    }
    this.setPortName((Object) portName);
  }

  /**
   * Set serial number
   *
   * @param serialNumber
   * @throws DataDictionaryException
   */
  public void setSerialNumber(String serialNumber) throws DataDictionaryException {
    if (this.getPortId() == null && this.getPortName() == null && serialNumber == null) {
      throw new DataDictionaryException(
          "Cannot set null serialNumber because empty TICIdentifier not allowed!");
    }
    this.setSerialNumber((Object) serialNumber);
  }

  protected void setPortId(Object portId) throws DataDictionaryException {
    this.data.put(KEY_PORT_ID, this.kPortId.convert(portId));
  }

  protected void setPortName(Object portName) throws DataDictionaryException {
    this.data.put(KEY_PORT_NAME, this.kPortName.convert(portName));
  }

  protected void setSerialNumber(Object serialNumber) throws DataDictionaryException {
    this.data.put(KEY_SERIAL_NUMBER, this.kSerialNumber.convert(serialNumber));
  }

  private void loadKeyDescriptors() {
    try {
      this.kPortId = new KeyDescriptorString(KEY_PORT_ID, false, false);
      this.keys.add(this.kPortId);

      this.kPortName = new KeyDescriptorString(KEY_PORT_NAME, false, false);
      this.keys.add(this.kPortName);

      this.kSerialNumber = new KeyDescriptorString(KEY_SERIAL_NUMBER, false, false);
      this.keys.add(this.kSerialNumber);

      this.addAllKeyDescriptor(this.keys);
    } catch (DataDictionaryException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }
}
