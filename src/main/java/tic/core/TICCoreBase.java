// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.function.Predicate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tic.core.codec.TICCoreErrorCodec;
import tic.core.codec.TICCoreFrameCodec;
import tic.frame.TICMode;
import tic.io.PlugSubscriber;
import tic.io.modem.ModemDescriptor;
import tic.io.modem.ModemFinder;
import tic.io.modem.ModemFinderBase;
import tic.io.modem.ModemJsonCodec;
import tic.io.modem.ModemPlugNotifier;
import tic.io.serialport.SerialPortFinderBase;
import tic.io.usb.UsbPortFinderBase;
import tic.util.task.FilteredNotifier;
import tic.util.task.FilteredNotifierBase;
import tic.util.task.Task;
import tic.util.task.TaskBase;
import tic.util.time.Time;

/**
 * Core implementation for managing frame reading and subscriber notifications.
 *
 * <p>This class provides mechanisms for reading frames, managing streams, handling subscribers, and
 * notifying events. It implements the core contract for frame acquisition and event delivery.
 *
 * <p>Common use cases include:
 *
 * <ul>
 *   <li>Reading frames from available sources
 *   <li>Managing and notifying subscribers
 *   <li>Handling stream lifecycle and modem events
 * </ul>
 *
 * @author Enedis Smarties team
 */
public class TICCoreBase implements TICCore, TICCoreSubscriber, PlugSubscriber<ModemDescriptor> {
  private static final int PLUG_NOTIFIER_POLLING_PERIOD = 100;
  private static final int READ_NEXT_FRAME_TIMEOUT = 30000;
  private static final int READ_NEXT_FRAME_POLLING_PERIOD = 100;

  private ModemFinder modemFinder;
  private ModemPlugNotifier plugNotifier;
  private long plugNotifierPeriod;
  private TICMode streamMode;
  private List<String> nativePortNamesOnStart;
  private Collection<TICCoreStream> streamList;
  private FilteredNotifier<TICIdentifier, TICCoreSubscriber> eventNotifier;
  private static Logger logger = LogManager.getLogger();

  public TICCoreBase() {
    this(TICMode.AUTO, null);
  }

  public TICCoreBase(TICMode streamMode, List<String> nativePortNamesStart) {
    this(
        ModemFinderBase.create(SerialPortFinderBase.getInstance(), UsbPortFinderBase.getInstance()),
        PLUG_NOTIFIER_POLLING_PERIOD,
        streamMode,
        nativePortNamesStart);
  }

  public TICCoreBase(
      ModemFinder modemFinder,
      long plugNotifierPeriod,
      TICMode streamMode,
      List<String> nativePortNamesOnStart) {
    super();
    this.modemFinder = modemFinder;
    this.plugNotifierPeriod = plugNotifierPeriod;
    if (streamMode == null) {
      throw new IllegalArgumentException("TICMode should be defined");
    }
    this.streamMode = streamMode;
    this.nativePortNamesOnStart = nativePortNamesOnStart;
    this.streamList = Collections.synchronizedSet(new HashSet<TICCoreStream>());
    this.eventNotifier = new FilteredNotifierBase<TICIdentifier, TICCoreSubscriber>();
  }

  @Override
  public List<TICIdentifier> getAvailableTICs() {
    List<TICIdentifier> identifiers = new ArrayList<>();

    for (TICCoreStream stream : this.streamList) {
      if (stream == null) {
        continue;
      }

      TICIdentifier identifier = stream.getIdentifier();
      if (!identifiers.contains(identifier)) {
        identifiers.add(identifier);
      }
    }

    return identifiers;
  }

  @Override
  public List<ModemDescriptor> getModemsInfo() {
    List<ModemDescriptor> descriptors = new ArrayList<>();

    for (TICCoreStream stream : this.streamList) {
      if (stream == null) {
        continue;
      }

      TICIdentifier identifier = stream.getIdentifier();
      ModemDescriptor descriptor = null;
      if (identifier != null && identifier.getPortName() != null) {
        descriptor = this.modemFinder.findByPortName(identifier.getPortName());
      }
      if (!descriptors.contains(descriptor)) {
        descriptors.add(descriptor);
      }
    }

    return descriptors;
  }

  @Override
  public TICCoreFrame readNextFrame(TICIdentifier identifier) throws TICCoreException {
    return this.readNextFrame(identifier, READ_NEXT_FRAME_TIMEOUT);
  }

  @Override
  public TICCoreFrame readNextFrame(TICIdentifier identifier, int timeout) throws TICCoreException {
    TICCoreFrame frame = null;
    ModemDescriptor descriptor = null;
    TICCoreStream stream = this.findStream(identifier);

    if (stream == null) {
      descriptor = this.modemFinder.findNative(identifier.getPortName());
      if (descriptor == null) {
        TICCoreException exception =
            new TICCoreException(
                TICCoreErrorCode.STREAM_IDENTIFIER_NOT_FOUND.getCode(),
                "Stream " + identifier + " not found!");
        logger.error(exception.getMessage());
        throw exception;
      }
      stream = this.startNewStream(descriptor);
    }
    ReadNextFrameSubscriber subscriber = new ReadNextFrameSubscriber();
    stream.subscribe(subscriber);

    long begin = System.nanoTime();
    long elapsed = 0;
    while (elapsed < timeout) {
      if (subscriber.getError() != null) {
        TICCoreException exception =
            new TICCoreException(
                subscriber.getError().getErrorCode().intValue(),
                subscriber.getError().getErrorMessage());
        logger.error(exception.getMessage());
        stream.unsubscribe(subscriber);
        this.closeNativeStream(identifier, stream, subscriber);
        throw exception;
      }
      if (subscriber.getData() != null) {
        frame = subscriber.getData();
        break;
      }
      Time.sleep(READ_NEXT_FRAME_POLLING_PERIOD);
      elapsed = (System.nanoTime() - begin) / 1000000;
    }
    stream.unsubscribe(subscriber);
    this.closeNativeStream(identifier, stream, subscriber);
    if (elapsed >= timeout) {
      TICCoreException exception =
          new TICCoreException(
              TICCoreErrorCode.DATA_READ_TIMEOUT.getCode(),
              "Stream " + identifier + " data read timeout !");
      logger.error(exception.getMessage());
      throw exception;
    }
    return frame;
  }

  @Override
  public void subscribe(TICIdentifier identifier, TICCoreSubscriber subscriber)
      throws TICCoreException {
    TICCoreStream stream = this.findStream(identifier);

    if (stream == null) {
      ModemDescriptor descriptor = this.modemFinder.findNative(identifier.getPortName());
      if (descriptor == null) {
        TICCoreException exception =
            new TICCoreException(
                TICCoreErrorCode.STREAM_IDENTIFIER_NOT_FOUND.getCode(),
                "Stream " + identifier + " not found!");
        logger.error(exception.getMessage(), exception);
        throw exception;
      }
      this.startNewStream(descriptor);
    }
    try {
      this.eventNotifier.subscribe(identifier, subscriber);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
  }

  @Override
  public void unsubscribe(TICIdentifier identifier, TICCoreSubscriber subscriber)
      throws TICCoreException {
    ModemDescriptor descriptor = this.modemFinder.findNative(identifier.getPortName());
    if (descriptor != null) {
      if (this.nativePortNamesOnStart == null
          || !this.nativePortNamesOnStart.contains(identifier.getPortName())) {
        Collection<TICCoreSubscriber> subscribers = this.findSubscribers(identifier, false);
        if (subscribers.contains(subscriber) && subscribers.size() == 1) {
          this.stopStream(descriptor);
        }
      }
    }
    try {
      this.eventNotifier.unsubscribe(identifier, subscriber);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
  }

  @Override
  public void subscribe(TICCoreSubscriber subscriber) {
    this.eventNotifier.subscribe(subscriber);
  }

  @Override
  public void unsubscribe(TICCoreSubscriber subscriber) {
    this.eventNotifier.unsubscribe(subscriber);
  }

  @Override
  public List<TICIdentifier> getIndentifiers(TICCoreSubscriber listener) {
    List<TICIdentifier> identifiers = new ArrayList<TICIdentifier>();

    if (this.eventNotifier.getSubscribers(true, false).contains(listener)) {
      identifiers.addAll(this.getAvailableTICs());
    } else if (this.eventNotifier.getSubscribers(false, true).contains(listener)) {
      identifiers.addAll(this.eventNotifier.getFilters(listener));
    }

    return identifiers;
  }

  @Override
  public void onData(TICCoreFrame frame) {
    try {
      logger.trace("TICCore frame:\n" + TICCoreFrameCodec.getInstance().encodeToJsonString(frame));
    } catch (Exception e) {
      logger.error("Error encoding TICCore frame to JSON string", e);
    }
    Collection<TICCoreSubscriber> subscriberList =
        this.findSubscribers(frame.getIdentifier(), true);
    Task task =
        new TaskBase() {
          @Override
          public void process() {
            TICCoreBase.this.notifyOnData(frame, subscriberList);
          }
        };
    task.start();
  }

  @Override
  public void onError(TICCoreError error) {
    try {
      logger.trace("TICCore error:\n" + TICCoreErrorCodec.getInstance().encodeToJsonString(error));
    } catch (Exception e) {
      logger.error("Error encoding TICCore error to JSON string", e);
    }
    Collection<TICCoreSubscriber> subscriberList =
        this.findSubscribers(error.getIdentifier(), true);
    Task task =
        new TaskBase() {
          public void process() {
            TICCoreBase.this.notifyOnError(error, subscriberList);
          }
        };
    task.start();
  }

  @Override
  public void onPlugged(ModemDescriptor descriptor) {
    try {
      logger.info("TICCore modem plugged:\n" + ModemJsonCodec.getInstance().encodeToJsonString(descriptor));
    } catch (Exception e) {
      logger.error("Error encoding ModemDescriptor to JSON string", e);
    }
    this.startNewStream(descriptor);
  }

  @Override
  public void onUnplugged(ModemDescriptor descriptor) {
    try {
      logger.info("TICCore modem unplugged:\n" + ModemJsonCodec.getInstance().encodeToJsonString(descriptor));
    } catch (Exception e) {
      logger.error("Error encoding ModemDescriptor to JSON string", e);
    }
    TICIdentifier identifier = this.stopStream(descriptor);
    Task task =
        new TaskBase() {
          @Override
          public void process() {
            TICCoreBase.this.notifyOnUnpluggedAndUnsubscribe(identifier);
          }
        };
    task.start();
  }

  @Override
  public void start() {
    if (!this.isRunning()) {
      logger.info("Starting TICCore");
      logger.debug("Starting TIC port plug notifier");
      this.plugNotifier = new ModemPlugNotifier(this.plugNotifierPeriod, this.modemFinder);
      this.plugNotifier.subscribe(this);
      this.plugNotifier.start();
      logger.debug("TIC port plug notifier started");
      if (this.nativePortNamesOnStart != null && this.nativePortNamesOnStart.size() > 0) {
        logger.debug(
            "Starting natives TIC port: " + Arrays.toString(this.nativePortNamesOnStart.toArray()));
        for (String portName : this.nativePortNamesOnStart) {
          ModemDescriptor descriptor = this.modemFinder.findNative(portName);
          try {
            logger.debug(
                "Starting native TIC port " + portName + " : " + ModemJsonCodec.getInstance().encodeToJsonString(descriptor));
          } catch (Exception e) {
            logger.error("Error encoding ModemDescriptor to JSON string", e);
          }
          if (descriptor != null && this.findStream(descriptor) == null) {
            this.startNewStream(descriptor);
          }
        }
      }
    }
  }

  @Override
  public void stop() {
    logger.info("Stopping TICCore");
    logger.debug("Stopping TIC port plug notifier");
    this.plugNotifier.unsubscribe(this);
    this.plugNotifier.stop();
    logger.debug("Stopping all streams");
    for (TICCoreStream stream : this.streamList) {
      stream.unsubscribe(this);
      stream.stop();
    }
    this.streamList.clear();
    logger.debug("Removing all subscribers");
    this.unsubscribe(this.eventNotifier.getSubscribers());
  }

  @Override
  public boolean isRunning() {
    return (this.plugNotifier != null) ? this.plugNotifier.isRunning() : false;
  }

  private TICCoreStream findStream(TICIdentifier identifier) {
    for (TICCoreStream stream : this.streamList) {
      if (stream.getIdentifier().matches(identifier)) {
        return stream;
      }
    }

    return null;
  }

  private TICCoreStream findStream(ModemDescriptor descriptor) {
    for (TICCoreStream stream : this.streamList) {
      TICIdentifier identifier = stream.getIdentifier();
      if (descriptor.portId() != null && identifier.getPortId() != null) {
        if (descriptor.portId().equals(identifier.getPortId())) {
          return stream;
        }
      }
      if (descriptor.portName() != null && identifier.getPortName() != null) {
        if (descriptor.portName().equals(identifier.getPortName())) {
          return stream;
        }
      }
    }

    return null;
  }

  private TICCoreStream startNewStream(ModemDescriptor descriptor) {
    if (descriptor == null) {
      return null;
    }

    TICCoreStream existing = this.findStream(descriptor);
    if (existing != null) {
      if (existing.isRunning()) {
        return existing;
      }
      this.streamList.remove(existing);
    }

    try {
      logger.debug("TICCore starting new stream : " + ModemJsonCodec.getInstance().encodeToJsonString(descriptor));
    } catch (Exception e) {
      logger.error("Error encoding ModemDescriptor to JSON string", e);
    }
    try {
      TICCoreStream stream =
          TICCoreStreamBase.create(
              descriptor.portId(), descriptor.portName(), this.streamMode, this.modemFinder);

      stream.subscribe(this);
      stream.start();
      this.streamList.add(stream);
      try {
        logger.debug("TICCore started new stream : " + ModemJsonCodec.getInstance().encodeToJsonString(descriptor));
      } catch (Exception e) {
        logger.error("Error encoding ModemDescriptor to JSON string", e);
      }
      return stream;
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return null;
    }
  }

  private TICIdentifier stopStream(ModemDescriptor descriptor) {
    TICIdentifier identifier = null;
    TICCoreStream stream = this.findStream(descriptor);
    try {
      logger.debug("TICCore stopping stream : " + ModemJsonCodec.getInstance().encodeToJsonString(descriptor));
    } catch (Exception e) {
      logger.error("Error encoding ModemDescriptor to JSON string", e);
    }
    if (stream != null) {
      identifier = stream.getIdentifier();
      stream.unsubscribe(this);
      stream.stop();
      this.streamList.remove(stream);
      try {
        logger.debug("TICCore stopped stream : " + ModemJsonCodec.getInstance().encodeToJsonString(descriptor));
      } catch (Exception e) {
        logger.error("Error encoding ModemDescriptor to JSON string", e);
      }
    }

    return identifier;
  }

  private void closeNativeStream(
      TICIdentifier identifier, TICCoreStream stream, ReadNextFrameSubscriber subscriber) {
    ModemDescriptor nativeDescriptor = this.modemFinder.findNative(identifier.getPortName());
    if (nativeDescriptor != null) {
      if (this.nativePortNamesOnStart == null
          || !this.nativePortNamesOnStart.contains(identifier.getPortName())) {
        Collection<TICCoreSubscriber> streamSubscribers = stream.getSubscribers();
        Collection<TICCoreSubscriber> ticCoreSubscribers = this.findSubscribers(identifier, false);
        if (!streamSubscribers.contains(subscriber)
            && streamSubscribers.contains(this)
            && streamSubscribers.size() == 1
            && ticCoreSubscribers.size() == 0) {
          this.stopStream(nativeDescriptor);
        }
      }
    }
  }

  private Collection<TICCoreSubscriber> findSubscribers(
      TICIdentifier sourceIdentifier, boolean globalSubscribers) {
    Predicate<TICIdentifier> predicate =
        new Predicate<TICIdentifier>() {
          @Override
          public boolean test(TICIdentifier identifier) {
            return sourceIdentifier.matches(identifier);
          }
        };

    return this.eventNotifier.getSubscribers(predicate, globalSubscribers);
  }

  private void notifyOnUnpluggedAndUnsubscribe(TICIdentifier identifier) {
    TICCoreError error =
        new TICCoreError(
            identifier,
            TICCoreErrorCode.STREAM_UNPLUGGED.getCode(),
            "TICCore stream " + identifier + " has been unplugged (unsubscribe has been forced)");
    Collection<TICCoreSubscriber> subscriberList = this.findSubscribers(identifier, true);
    this.notifyOnError(error, subscriberList);
    subscriberList = this.findSubscribers(identifier, false);
    this.unsubscribe(subscriberList);
  }

  private void notifyOnData(TICCoreFrame frame, Collection<TICCoreSubscriber> subscriberList) {
    for (TICCoreSubscriber subscriber : subscriberList) {
      subscriber.onData(frame);
    }
  }

  private void notifyOnError(TICCoreError error, Collection<TICCoreSubscriber> subscriberList) {
    for (TICCoreSubscriber subscriber : subscriberList) {
      subscriber.onError(error);
    }
  }

  private void unsubscribe(Collection<TICCoreSubscriber> subscriberList) {
    for (TICCoreSubscriber subscriber : subscriberList) {
      this.unsubscribe(subscriber);
    }
  }
}
