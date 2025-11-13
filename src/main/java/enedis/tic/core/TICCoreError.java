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
import enedis.lab.types.datadictionary.KeyDescriptorDataDictionary;
import enedis.lab.types.datadictionary.KeyDescriptorNumber;
import enedis.lab.types.datadictionary.KeyDescriptorString;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class representing a core error with identifier, code, message, and optional data.
 *
 * <p>This class provides mechanisms for constructing, accessing, and managing error information
 * including error codes, messages, and associated data. It is designed for general-purpose error
 * handling.
 *
 * <p>Common use cases include:
 *
 * <ul>
 *   <li>Representing errors with structured data
 *   <li>Managing error codes and messages
 *   <li>Associating additional data with errors
 * </ul>
 *
 * @author Enedis Smarties team
 */
public class TICCoreError extends DataDictionaryBase {
  protected static final String KEY_IDENTIFIER = "identifier";
  protected static final String KEY_ERROR_CODE = "errorCode";
  protected static final String KEY_ERROR_MESSAGE = "errorMessage";
  protected static final String KEY_DATA = "data";

  private List<KeyDescriptor<?>> keys = new ArrayList<KeyDescriptor<?>>();

  protected KeyDescriptorDataDictionary<TICIdentifier> kIdentifier;
  protected KeyDescriptorNumber kErrorCode;
  protected KeyDescriptorString kErrorMessage;
  protected KeyDescriptorDataDictionary<DataDictionaryBase> kData;

  protected TICCoreError() {
    super();
    this.loadKeyDescriptors();
  }

  /**
   * Constructor using map
   *
   * @param map
   * @throws DataDictionaryException
   */
  public TICCoreError(Map<String, Object> map) throws DataDictionaryException {
    this();
    this.copy(fromMap(map));
  }

  /**
   * Constructor using datadictionary
   *
   * @param other
   * @throws DataDictionaryException
   */
  public TICCoreError(DataDictionary other) throws DataDictionaryException {
    this();
    this.copy(other);
  }

  /**
   * Constructor setting parameters to specific values
   *
   * @param identifier
   * @param errorCode
   * @param errorMessage
   * @throws DataDictionaryException
   */
  public TICCoreError(TICIdentifier identifier, Number errorCode, String errorMessage)
      throws DataDictionaryException {
    this(identifier, errorCode, errorMessage, null);
  }

  /**
   * Constructor setting parameters to specific values
   *
   * @param identifier
   * @param errorCode
   * @param errorMessage
   * @param data
   * @throws DataDictionaryException
   */
  public TICCoreError(
      TICIdentifier identifier, Number errorCode, String errorMessage, DataDictionary data)
      throws DataDictionaryException {
    this();

    this.setIdentifier(identifier);
    this.setErrorCode(errorCode);
    this.setErrorMessage(errorMessage);
    this.setData(data);

    this.checkAndUpdate();
  }

  /**
   * Get identifier
   *
   * @return the identifier
   */
  public TICIdentifier getIdentifier() {
    return (TICIdentifier) this.data.get(KEY_IDENTIFIER);
  }

  /**
   * Get error code
   *
   * @return the error code
   */
  public Number getErrorCode() {
    return (Number) this.data.get(KEY_ERROR_CODE);
  }

  /**
   * Get error message
   *
   * @return the error message
   */
  public String getErrorMessage() {
    return (String) this.data.get(KEY_ERROR_MESSAGE);
  }

  /**
   * Get data
   *
   * @return the data
   */
  public DataDictionaryBase getData() {
    return (DataDictionaryBase) this.data.get(KEY_DATA);
  }

  /**
   * Set identifier
   *
   * @param identifier
   * @throws DataDictionaryException
   */
  public void setIdentifier(TICIdentifier identifier) throws DataDictionaryException {
    this.setIdentifier((Object) identifier);
  }

  /**
   * Set error code
   *
   * @param errorCode
   * @throws DataDictionaryException
   */
  public void setErrorCode(Number errorCode) throws DataDictionaryException {
    this.setErrorCode((Object) errorCode);
  }

  /**
   * Set error message
   *
   * @param errorMessage
   * @throws DataDictionaryException
   */
  public void setErrorMessage(String errorMessage) throws DataDictionaryException {
    this.setErrorMessage((Object) errorMessage);
  }

  /**
   * Set data
   *
   * @param data
   * @throws DataDictionaryException
   */
  public void setData(DataDictionary data) throws DataDictionaryException {
    this.setData((Object) data);
  }

  protected void setIdentifier(Object identifier) throws DataDictionaryException {
    this.data.put(KEY_IDENTIFIER, this.kIdentifier.convert(identifier));
  }

  protected void setErrorCode(Object errorCode) throws DataDictionaryException {
    this.data.put(KEY_ERROR_CODE, this.kErrorCode.convert(errorCode));
  }

  protected void setErrorMessage(Object errorMessage) throws DataDictionaryException {
    this.data.put(KEY_ERROR_MESSAGE, this.kErrorMessage.convert(errorMessage));
  }

  protected void setData(Object data) throws DataDictionaryException {
    this.data.put(KEY_DATA, this.kData.convert(data));
  }

  private void loadKeyDescriptors() {
    try {
      this.kIdentifier =
          new KeyDescriptorDataDictionary<TICIdentifier>(KEY_IDENTIFIER, true, TICIdentifier.class);
      this.keys.add(this.kIdentifier);

      this.kErrorCode = new KeyDescriptorNumber(KEY_ERROR_CODE, true);
      this.keys.add(this.kErrorCode);

      this.kErrorMessage = new KeyDescriptorString(KEY_ERROR_MESSAGE, true, false);
      this.keys.add(this.kErrorMessage);

      this.kData =
          new KeyDescriptorDataDictionary<DataDictionaryBase>(
              KEY_DATA, false, DataDictionaryBase.class);
      this.keys.add(this.kData);

      this.addAllKeyDescriptor(this.keys);
    } catch (DataDictionaryException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }
}
