// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.tic.service.requesthandler;

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
import tic.io.modem.ModemDescriptor;

/**
 * Base implementation of the TIC2WebSocket request handler.
 *
 * <p>This class provides the default logic for handling TIC2WebSocket requests, including
 * subscription, unsubscription, reading TIC frames, and retrieving available TICs and modem
 * information. It integrates with the TICCore to perform operations and generate appropriate
 * responses for each request type.
 *
 * <p>Responsibilities include:
 *
 * <ul>
 *   <li>Dispatching and processing supported TIC2WebSocket requests
 *   <li>Managing client subscriptions and unsubscriptions
 *   <li>Interfacing with TICCore for data operations
 *   <li>Generating error responses for unsupported or failed operations
 *   <li>Logging request handling and errors
 * </ul>
 *
 * @author Enedis Smarties team
 * @see TIC2WebSocketRequestHandler
 * @see TIC2WebSocketClient
 * @see TICCore
 */
public class TIC2WebSocketRequestHandlerBase implements TIC2WebSocketRequestHandler {
  /** Logger for request handling and errors. */
  private Logger logger;

  /** TICCore instance for data operations. */
  private TICCore ticCore;

  /**
   * Constructs a new TIC2WebSocketRequestHandlerBase.
   *
   * @param ticCore the TICCore instance for data operations
   */
  public TIC2WebSocketRequestHandlerBase(TICCore ticCore) {
    super();
    this.logger = LogManager.getLogger(this.getClass());
    this.ticCore = ticCore;
  }

  /**
   * Handles a TIC2WebSocket request and returns a response.
   *
   * <p>Dispatches the request to the appropriate handler method based on its type. Generates error
   * responses for unsupported requests.
   *
   * @param request the TIC2WebSocket request to handle
   * @param client the client associated with the request
   * @return the response generated for the request
   */
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

  /**
   * Handles a request to get available TIC identifiers.
   *
   * @param request the request to process
   * @return the response containing available TIC identifiers or an error
   */
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

  /**
   * Handles a request to get modem information.
   *
   * @param request the request to process
   * @return the response containing modem information or an error
   */
  private Response handleGetModemsInfoRequest(RequestGetModemsInfo request) {
    List<ModemDescriptor> modemsInfo = this.ticCore.getModemsInfo();

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

  /**
   * Handles a request to read a TIC frame.
   *
   * @param request the request to process
   * @return the response containing the TIC frame or an error
   */
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

  /**
   * Handles a request to subscribe to TIC identifiers for a client.
   *
   * @param request the subscription request
   * @param client the client to subscribe
   * @return the response indicating subscription success or failure
   */
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

  /**
   * Determines new TIC subscriptions requested by the client.
   *
   * <p>Compares current subscriptions with requested identifiers and returns only new
   * subscriptions.
   *
   * @param currentSubscriptions the client's current subscriptions
   * @param askedTicIdentifiers the requested TIC identifiers
   * @return a list of new TIC identifiers to subscribe
   */
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

  /**
   * Handles a request to unsubscribe from TIC identifiers for a client.
   *
   * @param request the unsubscription request
   * @param client the client to unsubscribe
   * @return the response indicating unsubscription success or failure
   */
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

  /**
   * Creates a generic error response for a given request name and error code.
   *
   * @param name the name of the request
   * @param code the error code
   * @param message the error message
   * @return the error response
   */
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
