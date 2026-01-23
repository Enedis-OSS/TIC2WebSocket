package tic.util.message.codec;

import org.json.JSONObject;
import tic.core.codec.TICCoreFrameCodec;
import tic.core.codec.TICIdentifierCodec;
import tic.io.modem.ModemJsonCodec;
import tic.service.message.ResponseGetAvailableTICs;
import tic.service.message.ResponseGetModemsInfo;
import tic.service.message.ResponseReadTIC;
import tic.service.message.ResponseSubscribeTIC;
import tic.service.message.ResponseUnsubscribeTIC;
import tic.util.message.Response;

public class ResponseJsonEncoder {

  private static TICIdentifierCodec ticIdentifierCodec = TICIdentifierCodec.getInstance();

  private ResponseJsonEncoder() {}

  public static JSONObject encode(Response message, JSONObject jsonMessage) {
    jsonMessage.put("datetime", message.getDateTime().toString());
    jsonMessage.put("errorCode", message.getErrorCode());
    jsonMessage.put("errorMessage", message.getErrorMessage());

    switch (message.getName()) {
      case ResponseGetAvailableTICs.NAME:
        if (message instanceof ResponseGetAvailableTICs) {
          return encodeGetAvailableTICsResponse((ResponseGetAvailableTICs) message, jsonMessage);
        }
        return jsonMessage;
      case ResponseGetModemsInfo.NAME:
        if (message instanceof ResponseGetModemsInfo) {
          return encodeGetModemsInfoResponse((ResponseGetModemsInfo) message, jsonMessage);
        }
        return jsonMessage;
      case ResponseReadTIC.NAME:
        if (message instanceof ResponseReadTIC) {
          return encodeReadTICResponse((ResponseReadTIC) message, jsonMessage);
        }
        return jsonMessage;
      case ResponseSubscribeTIC.NAME:
        return jsonMessage;
      case ResponseUnsubscribeTIC.NAME:
        return jsonMessage;
      default:
        return jsonMessage;
    }
  }

  private static JSONObject encodeGetAvailableTICsResponse(
      ResponseGetAvailableTICs message, JSONObject jsonMessage) {
    jsonMessage.put("data", ticIdentifierCodec.encodeToJsonArray(message.getData()));
    return jsonMessage;
  }

  private static JSONObject encodeGetModemsInfoResponse(
      ResponseGetModemsInfo message, JSONObject jsonMessage) {
    jsonMessage.put("data", ModemJsonCodec.encodeAsArray(message.getData()));
    return jsonMessage;
  }

  private static JSONObject encodeReadTICResponse(ResponseReadTIC message, JSONObject jsonMessage) {
    jsonMessage.put("data", TICCoreFrameCodec.encodeAsJsonObject(message.getData()));
    return jsonMessage;
  }
}
