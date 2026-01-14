// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.core;

import java.time.LocalDateTime;
import java.util.Collection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tic.frame.TICFrame;
import tic.frame.TICMode;
import tic.frame.group.TICGroup;
import tic.io.modem.ModemDescriptor;
import tic.io.modem.ModemFinder;
import tic.stream.TICStream;
import tic.stream.TICStreamListener;
import tic.stream.configuration.TICStreamConfiguration;
import tic.stream.identifier.SerialPortName;
import tic.stream.identifier.TICStreamIdentifier;
import tic.util.task.Notifier;
import tic.util.task.NotifierBase;
import tic.util.task.Task;
import tic.util.task.TaskBase;

/**
 * Core stream implementation for frame acquisition and subscriber notifications.
 *
 * <p>This class provides mechanisms for managing streams, acquiring frames, handling errors, and
 * notifying subscribers. It implements the contract for event-driven stream operations.
 *
 * <p>Common use cases include:
 *
 * <ul>
 *   <li>Acquiring frames from streams
 *   <li>Managing and notifying subscribers
 *   <li>Handling errors and stream lifecycle
 * </ul>
 *
 * @author Enedis Smarties team
 */
public class TICCoreStreamBase implements TICCoreStream {

  private final Object identifierLock = new Object();
  private TICIdentifier identifier;
  private final TICStream stream;
  private final TICStreamListener streamListener;
  private final Notifier<TICCoreSubscriber> notifier;
  private static Logger logger = LogManager.getLogger();

  public static TICCoreStream create(
      String portId, String portName, TICMode ticMode, ModemFinder modemFinder)
      throws TICCoreException {
    ModemDescriptor descriptor = null;
    TICIdentifier identifier = null;
    TICStream stream = null;
    Notifier<TICCoreSubscriber> notifier = null;

    if (portId != null) {
      descriptor = modemFinder.findByPortId(portId);
      if (descriptor == null) {
        TICCoreException exception =
            new TICCoreException(
                TICCoreErrorCode.STREAM_PORT_ID_NOT_FOUND.getCode(),
                "TICCore stream port id " + portId + " not found!");
        logger.error(exception.getMessage(), exception);
        throw exception;
      }
    } else if (portName != null) {
      descriptor = modemFinder.findByPortName(portName);
      if (descriptor == null) {
        descriptor = modemFinder.findNative(portName);
        if (descriptor == null) {
          TICCoreException exception =
              new TICCoreException(
                  TICCoreErrorCode.STREAM_PORT_NAME_NOT_FOUND.getCode(),
                  "TICCore stream port name " + portName + " not found!");
          logger.error(exception.getMessage(), exception);
          throw exception;
        }
      }
    } else {
      TICCoreException exception =
          new TICCoreException(
              TICCoreErrorCode.STREAM_PORT_DESCRIPTOR_EMPTY.getCode(),
              "TICCore stream port descriptor empty!");
      logger.error(exception.getMessage(), exception);
      throw exception;
    }
    if (ticMode == null) {
      TICCoreException exception =
          new TICCoreException(
              TICCoreErrorCode.STREAM_MODE_NOT_DEFINED.getCode(),
              "TICCore stream mode not defined!");
      logger.error(exception.getMessage(), exception);
      throw exception;
    }

    String resolvedPortId = descriptor.portId();
    String resolvedPortName = descriptor.portName();
    if (resolvedPortName == null || resolvedPortName.trim().isEmpty()) {
      TICCoreException exception =
          new TICCoreException(
              TICCoreErrorCode.STREAM_PORT_NAME_NOT_FOUND.getCode(),
              "TICCore stream resolved port name is empty!");
      logger.error(exception.getMessage(), exception);
      throw exception;
    }

    identifier = new TICIdentifier(resolvedPortId, resolvedPortName, null);
    notifier = new NotifierBase<TICCoreSubscriber>();

    TICStreamIdentifier streamIdentifier =
        new TICStreamIdentifier(new SerialPortName(resolvedPortName));
    TICStreamConfiguration configuration =
        new TICStreamConfiguration(
            ticMode, streamIdentifier, TICStreamConfiguration.DEFAULT_TIMEOUT);
    stream = new TICStream(configuration);

    return new TICCoreStreamBase(identifier, stream, notifier);
  }

  private TICCoreStreamBase(
      TICIdentifier identifier, TICStream stream, Notifier<TICCoreSubscriber> notifier)
      throws TICCoreException {
    this.identifier = identifier;
    this.stream = stream;
    this.notifier = notifier;

    this.streamListener =
        new TICStreamListener() {
          @Override
          public void onFrame(TICFrame ticFrame) {
            TICCoreStreamBase.this.onFrame(ticFrame);
          }

          @Override
          public void onError(String errorMessage) {
            TICCoreStreamBase.this.onError(errorMessage);
          }
        };
  }

  @Override
  public TICIdentifier getIdentifier() {
    synchronized (this.identifierLock) {
      return this.identifier;
    }
  }

  @Override
  public Collection<TICCoreSubscriber> getSubscribers() {
    return this.notifier.getSubscribers();
  }

  @Override
  public boolean hasSubscriber(TICCoreSubscriber subscriber) {
    return this.notifier.hasSubscriber(subscriber);
  }

  @Override
  public void subscribe(TICCoreSubscriber subscriber) {
    this.notifier.subscribe(subscriber);
  }

  @Override
  public void unsubscribe(TICCoreSubscriber subscriber) {
    this.notifier.unsubscribe(subscriber);
  }

  @Override
  public void start() {
    this.stream.subscribe(this.streamListener);
    this.stream.start();
  }

  @Override
  public void stop() {
    this.stream.unsubscribe(this.streamListener);
    this.stream.stop();
  }

  @Override
  public boolean isRunning() {
    return this.stream.isRunning();
  }

  private void onFrame(TICFrame ticFrame) {
    if (ticFrame == null) {
      return;
    }

    String serialNumber = null;
    if (ticFrame.getMode() == TICMode.STANDARD) {
      TICGroup group = ticFrame.getGroup("ADSC");
      serialNumber = group != null ? group.getValue() : null;
    } else if (ticFrame.getMode() == TICMode.HISTORIC) {
      TICGroup group = ticFrame.getGroup("ADCO");
      serialNumber = group != null ? group.getValue() : null;
    }

    TICIdentifier frameIdentifier;
    synchronized (this.identifierLock) {
      this.identifier = this.identifier.withSerialNumber(serialNumber);
      frameIdentifier = this.identifier;
    }

    TICCoreFrame frame =
        new TICCoreFrame(frameIdentifier, ticFrame.getMode(), LocalDateTime.now(), ticFrame);
    this.notifyOnData(frame);
  }

  private void onError(String errorMessage) {
    int code = TICCoreErrorCode.OTHER_REASON.getCode();
    if (errorMessage != null && errorMessage.toLowerCase().contains("timeout")) {
      code = TICCoreErrorCode.DATA_READ_TIMEOUT.getCode();
    }
    TICCoreError error =
        new TICCoreError(this.getIdentifier(), code, errorMessage == null ? "" : errorMessage);
    this.notifyOnError(error);
  }

  private void notifyOnData(TICCoreFrame frame) {
    if (frame == null) {
      return;
    }

    Collection<TICCoreSubscriber> subscriberList = this.notifier.getSubscribers();
    Task task =
        new TaskBase() {
          @Override
          public void process() {
            for (TICCoreSubscriber subscriber : subscriberList) {
              try {
                subscriber.onData(frame);
              } catch (Exception exception) {
                logger.error("TICCoreStream subscriber onData aborted", exception);
              }
            }
          }
        };
    task.start();
  }

  private void notifyOnError(TICCoreError error) {
    if (error == null) {
      return;
    }

    Collection<TICCoreSubscriber> subscriberList = this.notifier.getSubscribers();
    Task task =
        new TaskBase() {
          @Override
          public void process() {
            for (TICCoreSubscriber subscriber : subscriberList) {
              try {
                subscriber.onError(error);
              } catch (Exception exception) {
                logger.error("TICCoreStream subscriber onError aborted", exception);
              }
            }
          }
        };
    task.start();
  }
}
