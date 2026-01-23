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
import tic.frame.codec.TICFrameDetailledEncoder;
import tic.frame.codec.TICFrameSummarizedEncoder;

/**
 * Codec utilities for {@link tic.core.TICCoreFrame}.
 *
 * <p>Currently focuses on JSON encoding (pretty-printed) for diagnostics and logging.
 */
public final class TICCoreFrameCodec {

  private static final int DEFAULT_INDENT = 2;
  private static final boolean DEFAULT_SUMMARIZED_FRAME = true;

  private TICCoreFrameCodec() {}

  /**
   * Encodes a {@link tic.core.TICCoreFrame} into a {@link org.json.JSONObject}.
   *
   * <p>Unlike {@link #encode(TICCoreFrame)}, this method does not pretty-print nor stringify the
   * JSON. It is intended for embedding frame data inside larger JSON documents (e.g. WebSocket
   * responses) without double-encoding.
   */
  public static JSONObject encodeAsJsonObject(TICCoreFrame frame) {
    return encodeAsJsonObject(frame, DEFAULT_SUMMARIZED_FRAME);
  }

  public static JSONObject encodeAsJsonObject(TICCoreFrame frame, boolean summarizedFrame) {
    if (frame == null) {
      throw new IllegalArgumentException("frame cannot be null");
    }

    JSONObject json = new JSONObject();
    json.put(
        "identifier", TICIdentifierCodec.getInstance().encodeToJsonObject(frame.getIdentifier()));
    json.put("mode", frame.getMode().name());
    json.put("captureDateTime", frame.getCaptureDateTime().toString());
    json.put("frame", toJson(frame.getFrame(), summarizedFrame));

    return json;
  }

  public static String encode(TICCoreFrame frame) {
    return encode(frame, DEFAULT_INDENT, DEFAULT_SUMMARIZED_FRAME);
  }

  public static String encode(TICCoreFrame frame, int indentFactor) {
    return encode(frame, indentFactor, DEFAULT_SUMMARIZED_FRAME);
  }

  public static String encode(TICCoreFrame frame, int indentFactor, boolean summarizedFrame) {
    JSONObject json = encodeAsJsonObject(frame, summarizedFrame);
    return indentFactor < 0 ? json.toString() : json.toString(indentFactor);
  }

  private static JSONObject toJson(TICFrame frame, boolean summarized) {
    if (frame == null) {
      return new JSONObject();
    }
    return summarized
        ? TICFrameSummarizedEncoder.encodeAsObject(frame)
        : TICFrameDetailledEncoder.encodeAsObject(frame);
  }
}
