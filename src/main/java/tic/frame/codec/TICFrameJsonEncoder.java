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

public class TICFrameJsonEncoder {

  private static final int DEFAULT_INDENT = 2;
  private static final boolean DEFAULT_SUMMARIZED = true;

  private TICFrameJsonEncoder() {}

  /**
   * Encodes a TICFrame object into its JSON string representation with default settings.
   *
   * @param frame the TICFrame object to encode
   * @return the JSON string representation of the TICFrame
   */
  public static String encode(TICFrame frame) {
    return encode(frame, DEFAULT_INDENT, DEFAULT_SUMMARIZED);
  }

  /**
   * Encodes a TICFrame object into its JSON string representation with specified indentation.
   *
   * @param frame the TICFrame object to encode
   * @param indentFactor the number of spaces to add to each level of indentation
   * @param summarized whether to produce a summarized version of the JSON
   * @return the JSON string representation of the TICFrame
   */
  public static String encode(TICFrame frame, int indentFactor, boolean summarized) {

    return summarized
        ? encodeSummarized(frame, indentFactor)
        : encodeFullDetails(frame, indentFactor);
  }

  private static String encodeFullDetails(TICFrame frame, int indentFactor) {

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

    return indentFactor < 0 ? jsonFrame.toString() : jsonFrame.toString(indentFactor);
  }

  private static String encodeSummarized(TICFrame frame, int indentFactor) {

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

    return indentFactor < 0 ? jsonFrame.toString() : jsonFrame.toString(indentFactor);
  }
}
