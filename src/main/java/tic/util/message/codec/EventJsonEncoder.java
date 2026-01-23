// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.util.message.codec;

import org.json.JSONObject;
import tic.core.TICCoreError;
import tic.core.TICCoreFrame;
import tic.core.codec.TICIdentifierCodec;
import tic.frame.codec.TICFrameSummarizedEncoder;
import tic.service.message.EventOnError;
import tic.service.message.EventOnTICData;
import tic.util.message.Event;

public class EventJsonEncoder {
  private static TICIdentifierCodec ticIdentifierCodec = TICIdentifierCodec.getInstance();

  private EventJsonEncoder() {}

  public static JSONObject encode(Event message, JSONObject jsonMessage) {
    jsonMessage.put("datetime", message.getDateTime().toString());

    switch (message.getName()) {
      case EventOnError.NAME:
        return encodeEventOnError((EventOnError) message, jsonMessage);
      case EventOnTICData.NAME:
        return encodeEventOnTICData((EventOnTICData) message, jsonMessage);
      default:
        return jsonMessage;
    }
  }

  private static JSONObject encodeEventOnError(EventOnError message, JSONObject jsonMessage) {
    TICCoreError error = message.getData();
    jsonMessage.put("identifier", ticIdentifierCodec.encodeToJsonObject(error.getIdentifier()));
    jsonMessage.put("errorCode", error.getErrorCode());
    jsonMessage.put("errorMessage", error.getErrorMessage());
    jsonMessage.put("frame", TICFrameSummarizedEncoder.encodeAsObject(error.getFrame()));
    return jsonMessage;
  }

  private static JSONObject encodeEventOnTICData(EventOnTICData message, JSONObject jsonMessage) {
    TICCoreFrame frame = message.getData();
    jsonMessage.put("identifier", ticIdentifierCodec.encodeToJsonObject(frame.getIdentifier()));
    jsonMessage.put("mode", frame.getMode().toString());
    jsonMessage.put("captureDateTime", frame.getCaptureDateTime().toString());
    jsonMessage.put("frame", TICFrameSummarizedEncoder.encodeAsObject(frame.getFrame()));
    return jsonMessage;
  }
}
