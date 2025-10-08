// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.util.message;

import enedis.lab.types.DataDictionary;
import enedis.lab.types.DataDictionaryException;
import enedis.lab.types.datadictionary.KeyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Abstract base class for request messages.
 *
 * <p>This class represents request messages. It enforces the accepted message type and provides support for request-specific
 * fields. Subclasses can define additional request data and behavior.
 *
 * <p>Common use cases include:
 * <ul>
 *   <li>Representing client requests for data or actions</li>
 *   <li>Handling request validation and processing</li>
 *   <li>Extending for custom request types</li>
 * </ul>
 *
 * @author Enedis Smarties team
 * @see Message
 */
public abstract class Request extends Message {
  private static final MessageType TYPE_ACCEPTED_VALUE = MessageType.REQUEST;

  private List<KeyDescriptor<?>> keys = new ArrayList<KeyDescriptor<?>>();

  protected Request() {
    super();
    this.loadKeyDescriptors();

    this.kType.setAcceptedValues(TYPE_ACCEPTED_VALUE);
  }

  /**
   * Constructor using map
   *
   * @param map
   * @throws DataDictionaryException
   */
  public Request(Map<String, Object> map) throws DataDictionaryException {
    this();
    this.copy(fromMap(map));
  }

  /**
   * Constructor using datadictionary
   *
   * @param other
   * @throws DataDictionaryException
   */
  public Request(DataDictionary other) throws DataDictionaryException {
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
  public Request(MessageType type, String name) throws DataDictionaryException {
    this();

    this.setType(type);
    this.setName(name);

    this.checkAndUpdate();
  }

  @Override
  protected void updateOptionalParameters() throws DataDictionaryException {
    if (!this.exists(KEY_TYPE)) {
      this.setType(TYPE_ACCEPTED_VALUE);
    }
    super.updateOptionalParameters();
  }

  private void loadKeyDescriptors() {
    try {

      this.addAllKeyDescriptor(this.keys);
    } catch (DataDictionaryException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }
}
