// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.core.codec;

import org.json.JSONObject;
import tic.core.TICCoreFrame;
import tic.frame.TICFrame;
import tic.frame.codec.TICFrameDetailledJsonEncoder;
import tic.frame.codec.TICFrameSummarizedJsonEncoder;

/**
 * Codec utilities for {@link tic.core.TICCoreFrame}.
 *
 * <p>Currently focuses on JSON encoding (pretty-printed) for diagnostics and logging.
 */
public final class TICCoreFrameCodec {

  private static final int DEFAULT_INDENT = 2;
  private static final boolean DEFAULT_SUMMARIZED_FRAME = true;

  private TICCoreFrameCodec() {}

  public static String encode(TICCoreFrame frame) {
    return encode(frame, DEFAULT_INDENT, DEFAULT_SUMMARIZED_FRAME);
  }

  public static String encode(TICCoreFrame frame, int indentFactor) {
    return encode(frame, indentFactor, DEFAULT_SUMMARIZED_FRAME);
  }

  public static String encode(TICCoreFrame frame, int indentFactor, boolean summarizedFrame) {
    if (frame == null) {
      throw new IllegalArgumentException("frame cannot be null");
    }

    JSONObject json = new JSONObject();
    json.put("identifier", TICIdentifierCodec.toJson(frame.getIdentifier()));
    json.put("mode", frame.getMode().name());
    json.put("captureDateTime", frame.getCaptureDateTime().toString());
    json.put("frame", toJson(frame.getFrame(), summarizedFrame));

    return indentFactor < 0 ? json.toString() : json.toString(indentFactor);
  }

  private static JSONObject toJson(TICFrame frame, boolean summarized) {
    if (frame == null) {
      return new JSONObject();
    }
    return summarized
        ? TICFrameSummarizedJsonEncoder.encodeAsObject(frame)
        : TICFrameDetailledJsonEncoder.encodeAsObject(frame);
  }
}
