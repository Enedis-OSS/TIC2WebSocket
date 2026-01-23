package tic.util.message.codec;

import org.json.JSONObject;
import tic.core.TICIdentifier;
import tic.core.codec.TICIdentifierCodec;
import tic.service.message.RequestGetAvailableTICs;
import tic.service.message.RequestGetModemsInfo;
import tic.service.message.RequestReadTIC;
import tic.service.message.RequestSubscribeTIC;
import tic.service.message.RequestUnsubscribeTIC;
import tic.util.message.Request;

public class RequestJsonEncoder {

  private static TICIdentifierCodec ticIdentifierCodec = TICIdentifierCodec.getInstance();

  private RequestJsonEncoder() {}

  public static JSONObject encode(Request message, JSONObject jsonMessage) {
    switch (message.getName()) {
      case RequestGetAvailableTICs.NAME:
        return jsonMessage;
      case RequestGetModemsInfo.NAME:
        return jsonMessage;
      case RequestReadTIC.NAME:
        return encodeReadTICRequest((RequestReadTIC) message, jsonMessage);
      case RequestSubscribeTIC.NAME:
        return encodeSubscribeTICRequest((RequestSubscribeTIC) message, jsonMessage);
      case RequestUnsubscribeTIC.NAME:
        return encodeUnsubscribeTICRequest((RequestUnsubscribeTIC) message, jsonMessage);
      default:
        return jsonMessage;
    }
  }

  private static JSONObject encodeReadTICRequest(RequestReadTIC message, JSONObject jsonMessage) {
    TICIdentifier identifier = message.getData();
    jsonMessage.put("data", ticIdentifierCodec.encodeToJsonObject(identifier));
    return jsonMessage;
  }

  private static JSONObject encodeSubscribeTICRequest(
      RequestSubscribeTIC message, JSONObject jsonMessage) {
    jsonMessage.put("data", ticIdentifierCodec.encodeToJsonArray(message.getData()));
    return jsonMessage;
  }

  private static JSONObject encodeUnsubscribeTICRequest(
      RequestUnsubscribeTIC message, JSONObject jsonMessage) {
    jsonMessage.put("data", ticIdentifierCodec.encodeToJsonArray(message.getData()));
    return jsonMessage;
  }
}
