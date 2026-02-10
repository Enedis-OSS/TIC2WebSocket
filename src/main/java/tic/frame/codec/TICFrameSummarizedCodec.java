// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.frame.codec;

import java.util.List;
import org.json.JSONObject;
import tic.frame.TICFrame;
import tic.frame.group.TICGroup;
import tic.util.codec.JsonObjectCodec;
import tic.util.codec.JsonStringCodec;

public class TICFrameSummarizedCodec
    implements JsonStringCodec<TICFrame>, JsonObjectCodec<TICFrame> {

  public static TICFrameSummarizedCodec instance = null;

  private TICFrameSummarizedCodec() {}

  public static final TICFrameSummarizedCodec getInstance() {
    if (instance == null) {
      instance = new TICFrameSummarizedCodec();
    }
    return instance;
  }

  @Override
  public String encodeToJsonString(TICFrame frame, int indentFactor) throws Exception {
    JSONObject jsonFrame = (JSONObject) encodeToJsonObject(frame);
    return indentFactor < 0 ? jsonFrame.toString() : jsonFrame.toString(indentFactor);
  }

  @Override
  public TICFrame decodeFromJsonString(String jsonString, int indentFactor) throws Exception {
    throw new UnsupportedOperationException("Unimplemented method 'decodeFromJsonString'");
  }

  @Override
  public Object encodeToJsonObject(TICFrame frame) throws Exception {
    List<TICGroup> groups = frame.getGroupList();
    JSONObject jsonFrame = new JSONObject();
    groups.forEach(
        group -> {
          if (group.isValid()) {
            jsonFrame.put(group.getLabel(), group.getValue());
          } else {
            jsonFrame.put("!" + group.getLabel(), group.getValue());
          }
        });
    return jsonFrame;
  }

  @Override
  public TICFrame decodeFromJsonObject(Object jsonObject) throws Exception {
    throw new UnsupportedOperationException("Unimplemented method 'decodeFromJsonObject'");
  }
}
