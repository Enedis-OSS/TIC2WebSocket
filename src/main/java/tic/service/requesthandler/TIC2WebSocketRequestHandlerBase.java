// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.service.requesthandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tic.core.TICCore;
import tic.core.TICCoreErrorCode;
import tic.core.TICCoreException;
import tic.core.TICCoreFrame;
import tic.core.TICIdentifier;
import tic.io.modem.ModemDescriptor;
import tic.service.client.TIC2WebSocketClient;
import tic.service.endpoint.TIC2WebSocketEndPointErrorCode;
import tic.service.message.RequestGetAvailableTICs;
import tic.service.message.RequestGetModemsInfo;
import tic.service.message.RequestReadTIC;
import tic.service.message.RequestSubscribeTIC;
import tic.service.message.RequestUnsubscribeTIC;
import tic.service.message.ResponseError;
import tic.service.message.ResponseGetAvailableTICs;
import tic.service.message.ResponseGetModemsInfo;
import tic.service.message.ResponseReadTIC;
import tic.service.message.ResponseSubscribeTIC;
import tic.service.message.ResponseUnsubscribeTIC;
import tic.util.message.Request;
import tic.util.message.Response;

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

    logger.info("Handling request: " + request.getName());
    switch (request.getName()) {
      case RequestGetAvailableTICs.NAME:
        response = this.handleGetAvailableTICsRequest(request);
        break;
      case RequestGetModemsInfo.NAME:
        response = this.handleGetModemsInfoRequest(request);
        break;
      case RequestReadTIC.NAME:
        response = this.handleReadTICRequest(request);
        break;
      case RequestSubscribeTIC.NAME:
        response = this.handleSubscribeTICRequest(request, client);
        break;
      case RequestUnsubscribeTIC.NAME:
        response = this.handleUnsubscribeTICRequest(request, client);
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
  private Response handleGetAvailableTICsRequest(Request request) {
    List<TICIdentifier> ticIdentifiers = this.ticCore.getAvailableTICs();

    Response response = null;

    try {
      response =
          new ResponseGetAvailableTICs(
              LocalDateTime.now(),
              TIC2WebSocketEndPointErrorCode.NO_ERROR.value(),
              null,
              ticIdentifiers);
    } catch (Exception e) {
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
  private Response handleGetModemsInfoRequest(Request request) {
    List<ModemDescriptor> modemsInfo = this.ticCore.getModemsInfo();

    Response response = null;
    try {
      response =
          new ResponseGetModemsInfo(
              LocalDateTime.now(),
              TIC2WebSocketEndPointErrorCode.NO_ERROR.value(),
              null,
              modemsInfo);
    } catch (Exception e) {
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
  private Response handleReadTICRequest(Request request) {
    Response response = null;
    try {
      if (!(request instanceof RequestReadTIC)) {
        return this.createErrorResponse(
            request.getName(),
            TIC2WebSocketEndPointErrorCode.INTERNAL_ERROR,
            "Invalid request type for " + request.getName());
      }

      TICIdentifier identifier = ((RequestReadTIC) request).getData();

      TICCoreFrame frame = this.ticCore.readNextFrame(identifier);
      response =
          new ResponseReadTIC(
              LocalDateTime.now(), TIC2WebSocketEndPointErrorCode.NO_ERROR.value(), null, frame);
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
  private Response handleSubscribeTICRequest(Request request, TIC2WebSocketClient client) {
    Response response = null;

    if (!(request instanceof RequestSubscribeTIC)) {
      return this.createErrorResponse(
          request.getName(),
          TIC2WebSocketEndPointErrorCode.INTERNAL_ERROR,
          "Invalid request type for " + request.getName());
    }

    List<TICIdentifier> requestedIdentifiers = ((RequestSubscribeTIC) request).getData();

    Optional<List<TICIdentifier>> ticIdentifiers = Optional.ofNullable(requestedIdentifiers);

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
            } catch (Exception e) {
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
      } catch (Exception e) {
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
  private Response handleUnsubscribeTICRequest(Request request, TIC2WebSocketClient client) {
    Response response = null;

    if (!(request instanceof RequestUnsubscribeTIC)) {
      return this.createErrorResponse(
          request.getName(),
          TIC2WebSocketEndPointErrorCode.INTERNAL_ERROR,
          "Invalid request type for " + request.getName());
    }

    List<TICIdentifier> requestedIdentifiers = ((RequestUnsubscribeTIC) request).getData();

    Optional<List<TICIdentifier>> ticIdentifiers = Optional.ofNullable(requestedIdentifiers);

    if (ticIdentifiers.isPresent()) {
      for (TICIdentifier identifier : ticIdentifiers.get()) {
        try {
          this.ticCore.unsubscribe(identifier, client);
          try {
            response =
                new ResponseUnsubscribeTIC(
                    LocalDateTime.now(), TIC2WebSocketEndPointErrorCode.NO_ERROR.value(), null);
          } catch (Exception e) {
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
      } catch (Exception e) {
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
      return new ResponseError(name, LocalDateTime.now(), code.value(), message);
    } catch (Exception e) {
      this.logger.error(e.getMessage(), e);
      return null;
    }
  }
}
