// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.core.codec;

import org.json.JSONObject;
import tic.core.TICCoreFrame;
import tic.frame.codec.TICFrameSummarizedCodec;
import tic.util.codec.JsonObjectCodec;
import tic.util.codec.JsonStringCodec;

/**
 * Codec utilities for {@link tic.core.TICCoreFrame}.
 *
 * <p>Currently focuses on JSON encoding (pretty-printed) for diagnostics and logging.
 */
public final class TICCoreFrameCodec
    implements JsonStringCodec<TICCoreFrame>, JsonObjectCodec<TICCoreFrame> {

  public static TICCoreFrameCodec instance = new TICCoreFrameCodec();

  private TICCoreFrameCodec() {}

  public static TICCoreFrameCodec getInstance() {
    if (instance == null) {
      instance = new TICCoreFrameCodec();
    }
    return instance;
  }

  @Override
  public Object encodeToJsonObject(TICCoreFrame frame) throws Exception {
    if (frame == null) {
      throw new IllegalArgumentException("frame cannot be null");
    }

    JSONObject json = new JSONObject();
    json.put(
        "identifier", TICIdentifierCodec.getInstance().encodeToJsonObject(frame.getIdentifier()));
    json.put("mode", frame.getMode().name());
    json.put("captureDateTime", frame.getCaptureDateTime().toString());
    json.put("frame", TICFrameSummarizedCodec.getInstance().encodeToJsonObject(frame.getFrame()));

    return json;
  }

  @Override
  public TICCoreFrame decodeFromJsonObject(Object jsonObject) throws Exception {
    throw new UnsupportedOperationException("Unimplemented method 'decodeFromJsonObject'");
  }

  @Override
  public String encodeToJsonString(TICCoreFrame frame, int indentFactor) throws Exception {
    JSONObject json = (JSONObject) encodeToJsonObject(frame);
    return indentFactor < 0 ? json.toString() : json.toString(indentFactor);
  }

  @Override
  public TICCoreFrame decodeFromJsonString(String jsonString, int indentFactor) throws Exception {
    throw new UnsupportedOperationException("Unimplemented method 'decodeFromJsonString'");
  }
}
