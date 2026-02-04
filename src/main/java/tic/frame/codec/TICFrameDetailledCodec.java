// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.frame.codec;

import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import tic.frame.TICFrame;
import tic.frame.group.TICGroup;
import tic.util.codec.JsonObjectCodec;
import tic.util.codec.JsonStringCodec;

public class TICFrameDetailledCodec implements JsonObjectCodec<TICFrame>, JsonStringCodec<TICFrame> {
  public static TICFrameDetailledCodec instance = null;

  private TICFrameDetailledCodec() {}

  public static final TICFrameDetailledCodec getInstance() {
    if (instance == null) {
      instance = new TICFrameDetailledCodec();
    }
    return instance;
  }

  @Override
  public String encodeToJsonString(TICFrame frame, int indentFactor) throws Exception {
    if (frame == null) {
      throw new IllegalArgumentException("frame cannot be null");
    }
    JSONObject jsonFrame = (JSONObject) encodeToJsonObject(frame);
    return indentFactor > 0 ? jsonFrame.toString(indentFactor) : jsonFrame.toString();
  }

  @Override
  public TICFrame decodeFromJsonString(String jsonString, int indentFactor) throws Exception {
    throw new UnsupportedOperationException("Unimplemented method 'decodeFromJsonString'");
  }

  @Override
  public Object encodeToJsonObject(TICFrame frame) throws Exception {
    JSONObject jsonFrame = new JSONObject();
    jsonFrame.put("mode", frame.getMode().name());
    List<TICGroup> groups = frame.getGroupList();
    JSONArray jsonGroups = new JSONArray();
    groups.forEach(
        group -> {
          JSONObject jsonGroup = new JSONObject();
          jsonGroup.put("label", group.getLabel());
          jsonGroup.put("value", group.getValue());
          jsonGroup.put("isValid", group.isValid());
          jsonGroups.put(jsonGroup);
        });
    jsonFrame.put("groupList", jsonGroups);
    return jsonFrame;
  }

  @Override
  public TICFrame decodeFromJsonObject(Object jsonObject) throws Exception {
    throw new UnsupportedOperationException("Unimplemented method 'decodeFromJsonObject'");
  }

}
