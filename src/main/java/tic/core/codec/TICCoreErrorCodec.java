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
import tic.frame.TICFrame;
import tic.frame.codec.TICFrameCodec;
import tic.util.codec.JsonObjectCodec;
import tic.util.codec.JsonStringCodec;

/**
 * Codec utilities for {@link tic.core.TICCoreError}.
 *
 * <p>Currently focuses on JSON encoding (pretty-printed) for diagnostics and logging.
 */
public final class TICCoreErrorCodec
    implements JsonStringCodec<TICCoreError>, JsonObjectCodec<TICCoreError> {

  public static TICCoreErrorCodec instance = new TICCoreErrorCodec();

  public static TICCoreErrorCodec getInstance() {
    if (instance == null) {
      instance = new TICCoreErrorCodec();
    }
    return instance;
  }

  private TICCoreErrorCodec() {}

  @Override
  public Object encodeToJsonObject(TICCoreError error) throws Exception {
    if (error == null) {
      throw new IllegalArgumentException("error cannot be null");
    }

    JSONObject json = new JSONObject();
    json.put(
        "identifier", TICIdentifierCodec.getInstance().encodeToJsonObject(error.getIdentifier()));
    json.put("errorMessage", error.getErrorMessage());
    json.put("errorCode", error.getErrorCode());

    TICFrame frame = error.getFrame();
    if (frame != null) {
      json.put("frame", TICFrameCodec.encode(frame));
    }
    return json;
  }

  @Override
  public TICCoreError decodeFromJsonObject(Object jsonObject) throws Exception {
    throw new UnsupportedOperationException("Unimplemented method 'decodeFromJsonObject'");
  }

  @Override
  public String encodeToJsonString(TICCoreError error, int indentFactor) throws Exception {
    JSONObject json = (JSONObject) encodeToJsonObject(error);
    return indentFactor > 0 ? json.toString(indentFactor) : json.toString();
  }

  @Override
  public TICCoreError decodeFromJsonString(String jsonString, int indentFactor) throws Exception {
    throw new UnsupportedOperationException("Unimplemented method 'decodeFromJsonString'");
  }
}
