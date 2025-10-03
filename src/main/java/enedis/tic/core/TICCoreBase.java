// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.tic.core;

import enedis.lab.io.PlugSubscriber;
import enedis.lab.io.tic.TICPortDescriptor;
import enedis.lab.io.tic.TICPortFinder;
import enedis.lab.io.tic.TICPortFinderBase;
import enedis.lab.io.tic.TICPortPlugNotifier;
import enedis.lab.protocol.tic.TICMode;
import enedis.lab.types.DataArrayList;
import enedis.lab.types.DataDictionaryException;
import enedis.lab.util.task.FilteredNotifier;
import enedis.lab.util.task.FilteredNotifierBase;
import enedis.lab.util.task.Task;
import enedis.lab.util.task.TaskBase;
import enedis.lab.util.time.Time;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.function.Predicate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** TICCore implementation */
public class TICCoreBase
    implements TICCore, Task, TICCoreSubscriber, PlugSubscriber<TICPortDescriptor> {
  private static final int PLUG_NOTIFIER_POLLING_PERIOD = 100;
  private static final int READ_NEXT_FRAME_TIMEOUT = 30000;
  private static final int READ_NEXT_FRAME_POLLING_PERIOD = 100;

  private TICPortFinder portFinder;
  private TICPortPlugNotifier plugNotifier;
  private long plugNotifierPeriod;
  private Constructor<? extends TICCoreStream> streamConstructor;
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
        TICPortFinderBase.getInstance(),
        PLUG_NOTIFIER_POLLING_PERIOD,
        TICCoreStreamBase.class,
        streamMode,
        nativePortNamesStart);
  }

  public TICCoreBase(
      TICPortFinder ticPortFinder,
      long plugNotifierPeriod,
      Class<? extends TICCoreStream> streamClass,
      TICMode streamMode,
      List<String> nativePortNamesOnStart) {
    super();
    this.portFinder = ticPortFinder;
    this.plugNotifierPeriod = plugNotifierPeriod;
    try {
      this.streamConstructor =
          streamClass.getConstructor(String.class, String.class, TICMode.class);
    } catch (NoSuchMethodException e) {
      throw new IllegalArgumentException(
          "Class " + streamClass.getName() + " has no valid constructor : " + e.getMessage());
    }
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
    List<TICIdentifier> identifiers = new DataArrayList<TICIdentifier>();

    for (TICCoreStream stream : this.streamList) {
      identifiers.add(stream.getIdentifier());
    }

    return identifiers;
  }

  @Override
  public List<TICPortDescriptor> getModemsInfo() {
    List<TICPortDescriptor> descriptors = new DataArrayList<TICPortDescriptor>();

    for (TICCoreStream stream : this.streamList) {
      TICPortDescriptor descriptor =
          this.portFinder.findByPortName(stream.getIdentifier().getPortName());
      descriptors.add(descriptor);
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
    TICPortDescriptor descriptor = null;
    TICCoreStream stream = this.findStream(identifier);

    if (stream == null) {
      descriptor = this.portFinder.findNative(identifier.getPortName());
      if (descriptor == null) {
        TICCoreException exception =
            new TICCoreException(
                TICCoreErrorCode.STREAM_IDENTIFIER_NOT_FOUND.getCode(),
                "Stream " + identifier + " not found!");
        logger.error(exception.getMessage(), exception);
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
        logger.error(exception.getMessage(), exception);
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
      logger.error(exception.getMessage(), exception);
      throw exception;
    }
    return frame;
  }

  @Override
  public void subscribe(TICIdentifier identifier, TICCoreSubscriber subscriber)
      throws TICCoreException {
    TICCoreStream stream = this.findStream(identifier);

    if (stream == null) {
      TICPortDescriptor descriptor = this.portFinder.findNative(identifier.getPortName());
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
    TICPortDescriptor descriptor = this.portFinder.findNative(identifier.getPortName());
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
    logger.trace("TICCore frame:\n" + frame.toString(2));
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
    logger.error("TICCore error:\n" + error.toString(2));
    Collection<TICCoreSubscriber> subscriberList =
        this.findSubscribers(error.getIdentifier(), true);
    Task task =
        new TaskBase() {
          @Override
          public void process() {
            TICCoreBase.this.notifyOnError(error, subscriberList);
          }
        };
    task.start();
  }

  @Override
  public void onPlugged(TICPortDescriptor descriptor) {
    logger.info("TICCore modem plugged:\n" + descriptor.toString(2));
    this.startNewStream(descriptor);
  }

  @Override
  public void onUnplugged(TICPortDescriptor descriptor) {
    logger.info("TICCore modem unplugged:\n" + descriptor.toString(2));
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
      this.plugNotifier = new TICPortPlugNotifier(this.plugNotifierPeriod, this.portFinder);
      this.plugNotifier.subscribe(this);
      this.plugNotifier.start();
      logger.debug("TIC port plug notifier started");
      if (this.nativePortNamesOnStart != null && this.nativePortNamesOnStart.size() > 0) {
        logger.debug(
            "Starting natives TIC port: " + Arrays.toString(this.nativePortNamesOnStart.toArray()));
        for (String portName : this.nativePortNamesOnStart) {
          TICPortDescriptor descriptor = this.portFinder.findNative(portName);
          logger.debug("Starting native TIC port " + portName + " : " + descriptor);
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

  private TICCoreStream findStream(TICPortDescriptor descriptor) {
    for (TICCoreStream stream : this.streamList) {
      TICIdentifier identifier = stream.getIdentifier();
      if (descriptor.getPortId() != null && identifier.getPortId() != null) {
        if (descriptor.getPortId().equals(identifier.getPortId())) {
          return stream;
        }
      }
      if (descriptor.getPortName() != null && identifier.getPortName() != null) {
        if (descriptor.getPortName().equals(identifier.getPortName())) {
          return stream;
        }
      }
    }

    return null;
  }

  private TICCoreStream startNewStream(TICPortDescriptor descriptor) {
    TICCoreStream stream = null;
    logger.debug("TICCore starting new stream : " + descriptor.toString());
    try {
      stream =
          this.streamConstructor.newInstance(
              descriptor.getPortId(), descriptor.getPortName(), this.streamMode);

      stream.subscribe(this);

      stream.start();
      this.streamList.add(stream);
      logger.debug("TICCore started new stream : " + descriptor.toString());
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
    return stream;
  }

  private TICIdentifier stopStream(TICPortDescriptor descriptor) {
    TICIdentifier identifier = null;
    TICCoreStream stream = this.findStream(descriptor);
    logger.debug("TICCore stopping new stream : " + descriptor.toString());
    if (stream != null) {
      identifier = stream.getIdentifier();
      stream.unsubscribe(this);
      stream.stop();
      this.streamList.remove(stream);
      logger.debug("TICCore stopped new stream : " + descriptor.toString());
    }

    return identifier;
  }

  private void closeNativeStream(
      TICIdentifier identifier, TICCoreStream stream, ReadNextFrameSubscriber subscriber) {
    TICPortDescriptor nativeDescriptor = this.portFinder.findNative(identifier.getPortName());
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
    try {
      TICCoreError error =
          new TICCoreError(
              identifier,
              TICCoreErrorCode.STREAM_UNPLUGGED.getCode(),
              "TICCore stream " + identifier + " has been unplugged (unsubscribe has been forced)");
      Collection<TICCoreSubscriber> subscriberList = this.findSubscribers(identifier, true);
      this.notifyOnError(error, subscriberList);
      subscriberList = this.findSubscribers(identifier, false);
      this.unsubscribe(subscriberList);
    } catch (DataDictionaryException e) {
      logger.error(e.getMessage(), e);
    }
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
