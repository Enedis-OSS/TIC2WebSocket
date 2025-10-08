// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.util.message;

import enedis.lab.types.DataDictionary;
import enedis.lab.types.DataDictionaryException;
import enedis.lab.types.datadictionary.DataDictionaryBase;
import enedis.lab.types.datadictionary.KeyDescriptor;
import enedis.lab.types.datadictionary.KeyDescriptorEnum;
import enedis.lab.types.datadictionary.KeyDescriptorString;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Base class for all messages.
 *
 * <p>This class provides the core structure for messages, including type and name fields. It supports construction from maps and data dictionaries,
 * and can be extended for specific message types such as requests, responses, and events.
 *
 * <p>Common use cases include:
 * <ul>
 *   <li>Representing generic messages in the communication pipeline</li>
 *   <li>Providing a base for request, response, and event messages</li>
 *   <li>Supporting extensible message structures</li>
 * </ul>
 *
 * @author Enedis Smarties team
 */
public class Message extends DataDictionaryBase {
  /** Key type */
  public static final String KEY_TYPE = "type";

  /** Key name */
  public static final String KEY_NAME = "name";

  private List<KeyDescriptor<?>> keys = new ArrayList<KeyDescriptor<?>>();

  protected KeyDescriptorEnum<MessageType> kType;
  protected KeyDescriptorString kName;

  protected Message() {
    super();
    this.loadKeyDescriptors();
  }

  /**
   * Constructor using map
   *
   * @param map
   * @throws DataDictionaryException
   */
  public Message(Map<String, Object> map) throws DataDictionaryException {
    this();
    this.copy(fromMap(map));
  }

  /**
   * Constructor using datadictionary
   *
   * @param other
   * @throws DataDictionaryException
   */
  public Message(DataDictionary other) throws DataDictionaryException {
    this();
    this.copy(other);
  }

  /**
   * Constructor setting parameters to specific values
   *
   * @param type
   * @param name
   * @throws DataDictionaryException
   */
  public Message(MessageType type, String name) throws DataDictionaryException {
    this();

    this.setType(type);
    this.setName(name);

    this.checkAndUpdate();
  }

  @Override
  protected void updateOptionalParameters() throws DataDictionaryException {
    super.updateOptionalParameters();
  }

  /**
   * Get type
   *
   * @return the type
   */
  public MessageType getType() {
    return (MessageType) this.data.get(KEY_TYPE);
  }

  /**
   * Get name
   *
   * @return the name
   */
  public String getName() {
    return (String) this.data.get(KEY_NAME);
  }

  /**
   * Set type
   *
   * @param type
   * @throws DataDictionaryException
   */
  public void setType(MessageType type) throws DataDictionaryException {
    this.setType((Object) type);
  }

  /**
   * Set name
   *
   * @param name
   * @throws DataDictionaryException
   */
  public void setName(String name) throws DataDictionaryException {
    this.setName((Object) name);
  }

  protected void setType(Object type) throws DataDictionaryException {
    this.data.put(KEY_TYPE, this.kType.convert(type));
  }

  protected void setName(Object name) throws DataDictionaryException {
    this.data.put(KEY_NAME, this.kName.convert(name));
  }

  private void loadKeyDescriptors() {
    try {
      this.kType = new KeyDescriptorEnum<MessageType>(KEY_TYPE, true, MessageType.class);
      this.keys.add(this.kType);

      this.kName = new KeyDescriptorString(KEY_NAME, true, false);
      this.keys.add(this.kName);

      this.addAllKeyDescriptor(this.keys);
    } catch (DataDictionaryException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }
}
