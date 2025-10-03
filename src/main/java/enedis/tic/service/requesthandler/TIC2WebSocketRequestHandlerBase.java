// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.tic.service.requesthandler;

import enedis.lab.io.tic.TICPortDescriptor;
import enedis.lab.types.DataDictionaryException;
import enedis.lab.util.message.Request;
import enedis.lab.util.message.Response;
import enedis.lab.util.message.ResponseBase;
import enedis.tic.core.TICCore;
import enedis.tic.core.TICCoreErrorCode;
import enedis.tic.core.TICCoreException;
import enedis.tic.core.TICCoreFrame;
import enedis.tic.core.TICIdentifier;
import enedis.tic.service.client.TIC2WebSocketClient;
import enedis.tic.service.endpoint.TIC2WebSocketEndPointErrorCode;
import enedis.tic.service.message.RequestGetAvailableTICs;
import enedis.tic.service.message.RequestGetModemsInfo;
import enedis.tic.service.message.RequestReadTIC;
import enedis.tic.service.message.RequestSubscribeTIC;
import enedis.tic.service.message.RequestUnsubscribeTIC;
import enedis.tic.service.message.ResponseGetAvailableTICs;
import enedis.tic.service.message.ResponseGetModemsInfo;
import enedis.tic.service.message.ResponseReadTIC;
import enedis.tic.service.message.ResponseSubscribeTIC;
import enedis.tic.service.message.ResponseUnsubscribeTIC;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** TIC2WebSocket client pool interface */
public class TIC2WebSocketRequestHandlerBase implements TIC2WebSocketRequestHandler {
  private Logger logger;
  private TICCore ticCore;

  /**
   * Default constructor
   *
   * @param ticCore
   */
  public TIC2WebSocketRequestHandlerBase(TICCore ticCore) {
    super();
    this.logger = LogManager.getLogger(this.getClass());
    this.ticCore = ticCore;
  }

  @Override
  public Response handle(Request request, TIC2WebSocketClient client) {
    Response response = null;

    switch (request.getName()) {
      case RequestGetAvailableTICs.NAME:
        response = this.handleGetAvailableTICsRequest((RequestGetAvailableTICs) request);
        break;
      case RequestGetModemsInfo.NAME:
        response = this.handleGetModemsInfoRequest((RequestGetModemsInfo) request);
        break;
      case RequestReadTIC.NAME:
        response = this.handleReadTICRequest((RequestReadTIC) request);
        break;
      case RequestSubscribeTIC.NAME:
        response = this.handleSubscribeTICRequest((RequestSubscribeTIC) request, client);
        break;
      case RequestUnsubscribeTIC.NAME:
        response = this.handleUnsubscribeTICRequest((RequestUnsubscribeTIC) request, client);
        break;
      default:
        this.logger.error("Request " + request.getName() + " not supported");
        response =
            this.createErrorResponse(
                request.getName(),
                TIC2WebSocketEndPointErrorCode.UNSUPPORTED_MESSAGE,
                "Request " + request.getName() + " not supported");
        break;
    }

    return response;
  }

  private Response handleGetAvailableTICsRequest(RequestGetAvailableTICs request) {
    List<TICIdentifier> ticIdentifiers = this.ticCore.getAvailableTICs();

    Response response = null;
    try {
      response =
          new ResponseGetAvailableTICs(
              LocalDateTime.now(),
              TIC2WebSocketEndPointErrorCode.NO_ERROR.value(),
              null,
              ticIdentifiers);
    } catch (DataDictionaryException e) {
      this.logger.error(e.getMessage(), e);
      response =
          this.createErrorResponse(
              request.getName(), TIC2WebSocketEndPointErrorCode.INTERNAL_ERROR, e.getMessage());
    }

    return response;
  }

  private Response handleGetModemsInfoRequest(RequestGetModemsInfo request) {
    List<TICPortDescriptor> modemsInfo = this.ticCore.getModemsInfo();

    Response response = null;
    try {
      response =
          new ResponseGetModemsInfo(
              LocalDateTime.now(),
              TIC2WebSocketEndPointErrorCode.NO_ERROR.value(),
              null,
              modemsInfo);
    } catch (DataDictionaryException e) {
      this.logger.error(e.getMessage(), e);
      response =
          this.createErrorResponse(
              request.getName(), TIC2WebSocketEndPointErrorCode.INTERNAL_ERROR, e.getMessage());
    }

    return response;
  }

  private Response handleReadTICRequest(RequestReadTIC request) {
    Response response = null;
    try {
      TICCoreFrame frame = this.ticCore.readNextFrame(request.getData());
      response =
          new ResponseReadTIC(
              LocalDateTime.now(), TIC2WebSocketEndPointErrorCode.NO_ERROR.value(), null, frame);
    } catch (DataDictionaryException e) {
      this.logger.error(e.getMessage(), e);
      response =
          this.createErrorResponse(
              request.getName(), TIC2WebSocketEndPointErrorCode.INTERNAL_ERROR, e.getMessage());
    } catch (TICCoreException e) {
      if (e.getErrorCode() == TICCoreErrorCode.STREAM_IDENTIFIER_NOT_FOUND.getCode()) {
        response =
            this.createErrorResponse(
                request.getName(),
                TIC2WebSocketEndPointErrorCode.IDENTIFIER_NOT_FOUND,
                e.getMessage());
      } else if (e.getErrorCode() == TICCoreErrorCode.DATA_READ_TIMEOUT.getCode()) {
        response =
            this.createErrorResponse(
                request.getName(), TIC2WebSocketEndPointErrorCode.READ_TIMEOUT, e.getMessage());
      } else {
        response =
            this.createErrorResponse(
                request.getName(), TIC2WebSocketEndPointErrorCode.INTERNAL_ERROR, e.getMessage());
      }
    }

    return response;
  }

  private Response handleSubscribeTICRequest(
      RequestSubscribeTIC request, TIC2WebSocketClient client) {
    Response response = null;
    Optional<List<TICIdentifier>> ticIdentifiers = Optional.ofNullable(request.getData());

    if (ticIdentifiers.isPresent()) {
      List<TICIdentifier> newSubscriptions =
          this.getNewSubcriptions(this.ticCore.getIndentifiers(client), ticIdentifiers.get());
      if (!newSubscriptions.isEmpty()) {
        for (TICIdentifier identifier : ticIdentifiers.get()) {
          try {
            this.ticCore.subscribe(identifier, client);
            try {
              response =
                  new ResponseSubscribeTIC(
                      LocalDateTime.now(), TIC2WebSocketEndPointErrorCode.NO_ERROR.value(), null);
            } catch (DataDictionaryException e) {
              this.logger.error(e.getMessage(), e);
            }
          } catch (TICCoreException e) {
            response =
                this.createErrorResponse(
                    request.getName(),
                    TIC2WebSocketEndPointErrorCode.SUBSCRIPTION_FAIL,
                    e.getMessage());
          }
        }
      }
    } else {
      this.ticCore.subscribe(client);
      try {
        response =
            new ResponseSubscribeTIC(
                LocalDateTime.now(), TIC2WebSocketEndPointErrorCode.NO_ERROR.value(), null);
      } catch (DataDictionaryException e) {
        this.logger.error(e.getMessage(), e);
      }
    }

    return response;
  }

  private List<TICIdentifier> getNewSubcriptions(
      List<TICIdentifier> currentSubscriptions, List<TICIdentifier> askedTicIdentifiers) {
    List<TICIdentifier> newSubscriptions = new ArrayList<TICIdentifier>();

    for (TICIdentifier askedTicIdentifier : askedTicIdentifiers) {
      boolean newSub = true;
      for (TICIdentifier currentSubscription : currentSubscriptions) {
        if (askedTicIdentifier.matches(currentSubscription)) {
          newSub = false;
          break;
        }
      }
      if (newSub) {
        newSubscriptions.add(askedTicIdentifier);
      }
    }
    return newSubscriptions;
  }

  private Response handleUnsubscribeTICRequest(
      RequestUnsubscribeTIC request, TIC2WebSocketClient client) {
    Response response = null;
    Optional<List<TICIdentifier>> ticIdentifiers = Optional.ofNullable(request.getData());

    if (ticIdentifiers.isPresent()) {
      for (TICIdentifier identifier : ticIdentifiers.get()) {
        try {
          this.ticCore.unsubscribe(identifier, client);
          try {
            response =
                new ResponseUnsubscribeTIC(
                    LocalDateTime.now(), TIC2WebSocketEndPointErrorCode.NO_ERROR.value(), null);
          } catch (DataDictionaryException e) {
            this.logger.error(e.getMessage(), e);
          }
        } catch (TICCoreException e) {
          response =
              this.createErrorResponse(
                  request.getName(),
                  TIC2WebSocketEndPointErrorCode.UNSUBSCRIPTION_FAIL,
                  e.getMessage());
        }
      }
    } else {
      this.ticCore.unsubscribe(client);
      try {
        response =
            new ResponseUnsubscribeTIC(
                LocalDateTime.now(), TIC2WebSocketEndPointErrorCode.NO_ERROR.value(), null);
      } catch (DataDictionaryException e) {
        this.logger.error(e.getMessage(), e);
      }
    }

    return response;
  }

  private Response createErrorResponse(
      String name, TIC2WebSocketEndPointErrorCode code, String message) {
    try {
      return new ResponseBase(name, LocalDateTime.now(), code.value(), message, null);
    } catch (DataDictionaryException e) {
      this.logger.error(e.getMessage(), e);
      return null;
    }
  }
}
