// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.core.codec;

import org.json.JSONObject;
import tic.core.TICIdentifier;

/**
 * Codec utilities for {@link tic.core.TICIdentifier}.
 *
 * <p>Currently focuses on JSON encoding (pretty-printed) for diagnostics and logging.
 */
public final class TICIdentifierCodec {

  private static final int DEFAULT_INDENT = 2;

  private TICIdentifierCodec() {}

  public static String encode(TICIdentifier identifier) {
    return encode(identifier, DEFAULT_INDENT);
  }

  public static String encode(TICIdentifier identifier, int indentFactor) {
    if (identifier == null) {
      throw new IllegalArgumentException("identifier cannot be null");
    }

    JSONObject json = toJson(identifier);
    return indentFactor < 0 ? json.toString() : json.toString(indentFactor);
  }

  public static JSONObject toJson(TICIdentifier identifier) {
    if (identifier == null) {
      throw new IllegalArgumentException("identifier cannot be null");
    }

    JSONObject json = new JSONObject();
    json.put(
        "portName", identifier.getPortName() == null ? JSONObject.NULL : identifier.getPortName());
    json.put("portId", identifier.getPortId() == null ? JSONObject.NULL : identifier.getPortId());
    json.put(
        "serialNumber",
        identifier.getSerialNumber() == null ? JSONObject.NULL : identifier.getSerialNumber());
    return json;
  }
}
