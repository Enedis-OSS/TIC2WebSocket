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

public class TICFrameSummarizedEncoder {

  private static final int DEFAULT_INDENT = 2;

  private TICFrameSummarizedEncoder() {}

  /**
   * Encodes a TICFrame object into its JSON string representation with default settings.
   *
   * @param frame the TICFrame object to encode
   * @return the JSON string representation of the TICFrame
   */
  public static String encodeAsString(TICFrame frame) {
    return encodeAsString(frame, DEFAULT_INDENT);
  }

  /**
   * Encodes a TICFrame object into its JSON string representation with specified indentation.
   *
   * @param frame the TICFrame object to encode
   * @param indentFactor the number of spaces to add to each level of indentation
   * @return the JSON string representation of the TICFrame
   */
  public static String encodeAsString(TICFrame frame, int indentFactor) {
    JSONObject jsonFrame = encodeAsObject(frame);
    return indentFactor < 0 ? jsonFrame.toString() : jsonFrame.toString(indentFactor);
  }

  /**
   * Encodes a TICFrame object into its JSON object representation in summarized form.
   *
   * @param frame the TICFrame object to encode
   * @return the JSON object representation of the TICFrame
   */
  public static JSONObject encodeAsObject(TICFrame frame) {
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
}
