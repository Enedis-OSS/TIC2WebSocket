// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.core.codec;

import org.json.JSONObject;
import tic.core.TICCoreError;
import tic.core.TICIdentifier;
import tic.frame.TICFrame;
import tic.frame.codec.TICFrameJsonEncoder;

/**
 * Codec utilities for {@link tic.core.TICCoreError}.
 *
 * <p>Currently focuses on JSON encoding (pretty-printed) for diagnostics and logging.
 */
public final class TICCoreErrorCodec {

  private static final int DEFAULT_INDENT = 2;
  private static final boolean DEFAULT_SUMMARIZED_FRAME = true;

  private TICCoreErrorCodec() {}

  public static String encode(TICCoreError error) {
    return encode(error, DEFAULT_INDENT, DEFAULT_SUMMARIZED_FRAME);
  }

  public static String encode(TICCoreError error, int indentFactor) {
    return encode(error, indentFactor, DEFAULT_SUMMARIZED_FRAME);
  }

  public static String encode(TICCoreError error, int indentFactor, boolean summarizedFrame) {
    if (error == null) {
      throw new IllegalArgumentException("error cannot be null");
    }

    JSONObject json = new JSONObject();
    json.put("identifier", toJson(error.getIdentifier()));
    json.put("errorMessage", error.getErrorMessage());
    json.put("errorCode", error.getErrorCode());

    TICFrame frame = error.getFrame();
    if (frame != null) {
      json.put("frame", toJson(frame, summarizedFrame));
    }

    return indentFactor < 0 ? json.toString() : json.toString(indentFactor);
  }

  private static JSONObject toJson(TICIdentifier identifier) {
    JSONObject json = new JSONObject();
    json.put(
        "portName", identifier.getPortName() == null ? JSONObject.NULL : identifier.getPortName());
    json.put("portId", identifier.getPortId() == null ? JSONObject.NULL : identifier.getPortId());
    json.put(
        "serialNumber",
        identifier.getSerialNumber() == null ? JSONObject.NULL : identifier.getSerialNumber());
    return json;
  }

  private static JSONObject toJson(TICFrame frame, boolean summarized) {
    // Encode with no indentation, then parse back to JSONObject to embed cleanly.
    String jsonText = TICFrameJsonEncoder.encode(frame, -1, summarized);
    return new JSONObject(jsonText);
  }
}
