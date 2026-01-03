// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.tic.core;

import enedis.lab.io.channels.Channel;
import enedis.lab.io.channels.ChannelException;
import enedis.lab.io.datastreams.DataStreamBase;
import enedis.lab.io.datastreams.DataStreamDirection;
import enedis.lab.io.datastreams.DataStreamException;
import enedis.lab.io.datastreams.DataStreamListener;
import enedis.lab.io.datastreams.DataStreamStatus;
import enedis.lab.protocol.tic.TICMode;
import enedis.lab.protocol.tic.channels.ChannelTICSerialPort;
import enedis.lab.protocol.tic.channels.ChannelTICSerialPortConfiguration;
import enedis.lab.protocol.tic.datastreams.TICInputStream;
import enedis.lab.protocol.tic.datastreams.TICStreamConfiguration;
import enedis.lab.protocol.tic.frame.TICFrame;
import enedis.lab.protocol.tic.frame.TICFrameDataSet;
import enedis.lab.protocol.tic.frame.historic.TICFrameHistoric;
import enedis.lab.protocol.tic.frame.standard.TICFrameStandard;
import enedis.lab.types.DataDictionary;
import enedis.lab.types.DataDictionaryException;
import enedis.lab.types.datadictionary.DataDictionaryBase;
import java.time.LocalDateTime;
import java.util.Collection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tic.io.modem.ModemDescriptor;
import tic.io.modem.ModemFinder;
import tic.util.task.Notifier;
import tic.util.task.NotifierBase;

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
public class TICCoreStreamBase implements TICCoreStream, DataStreamListener {

  private TICIdentifier identifier;
  private DataStreamBase stream;
  private Channel channel;
  private Notifier<TICCoreSubscriber> notifier;
  private static Logger logger = LogManager.getLogger();

  public TICCoreStreamBase(String portId, String portName, TICMode ticMode, ModemFinder modemFinder)
      throws TICCoreException {
    ModemDescriptor descriptor = null;

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
    try {
      this.identifier = new TICIdentifier(portId, portName, null);
      this.channel =
          new ChannelTICSerialPort(
              new ChannelTICSerialPortConfiguration(
                  "Channel@" + descriptor.portName(), portName, ticMode));
      this.stream =
          new TICInputStream(
              new TICStreamConfiguration(
                  "Stream@" + descriptor.portName(),
                  DataStreamDirection.INPUT,
                  "Channel@" + descriptor.portName(),
                  ticMode));
      this.stream.setChannel(this.channel);
      this.notifier = new NotifierBase<TICCoreSubscriber>();
      logger = LogManager.getLogger();
    } catch (DataDictionaryException | ChannelException | DataStreamException e) {
      TICCoreException exception =
          new TICCoreException(
              TICCoreErrorCode.OTHER_REASON.getCode(),
              "TICCore stream instanciation failed : " + e.getMessage());
      logger.error(exception.getMessage(), exception);
      throw exception;
    }
  }

  @Override
  public TICIdentifier getIdentifier() {
    TICIdentifier identifier;

    synchronized (this.identifier) {
      identifier = (TICIdentifier) this.identifier.clone();
    }

    return identifier;
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
  public void onDataReceived(String dataStreamName, DataDictionary data) {
    TICCoreFrame frame = this.createFrame(data);

    if (frame != null) {
      this.notifyOnData(frame);
    } else {
      logger.debug("Frame creation skipped due to null TICFrame");
    }
  }

  @Override
  public void onDataSent(String dataStreamName, DataDictionary data) {}

  @Override
  public void onStatusChanged(String dataStreamName, DataStreamStatus newStatus) {}

  @Override
  public void onErrorDetected(
      String dataStreamName, int errorCode, String errorMessage, DataDictionary data) {
    TICCoreError error = this.createError(errorCode, errorMessage, data);
    this.notifyOnError(error);
  }

  @Override
  public void start() {
    this.channel.start();
    try {
      this.stream.open();
      this.stream.subscribe(this);
    } catch (DataStreamException e) {
      logger.error(e.getMessage());
    }
  }

  @Override
  public void stop() {
    this.stream.unsubscribe(this);
    try {
      this.stream.close();
    } catch (DataStreamException e) {
      logger.error(e.getMessage());
    }
    this.channel.stop();
  }

  @Override
  public boolean isRunning() {
    return this.channel.isRunning();
  }

  private TICCoreFrame createFrame(DataDictionary data) {
    TICCoreFrame frame = new TICCoreFrame();
    try {
      frame.setCaptureDateTime(LocalDateTime.now());
      TICFrame ticFrame = (TICFrame) data.get(TICInputStream.KEY_DATA);

      if (ticFrame == null) {
        logger.warn("TICFrame is null. Skipping frame creation.");
        return null;
      }

      DataDictionaryBase content = new DataDictionaryBase();
      for (TICFrameDataSet frameDataSet : ticFrame.getDataSetList()) {
        String label = frameDataSet.getLabel();
        content.set(label, ticFrame.getData(label));
      }
      frame.setContent(content);
      String serialNumber = null;
      if (ticFrame instanceof TICFrameStandard) {
        frame.setMode(TICMode.STANDARD);
        serialNumber = (String) content.get("ADSC");
      } else if (ticFrame instanceof TICFrameHistoric) {
        frame.setMode(TICMode.HISTORIC);
        serialNumber = (String) content.get("ADCO");
      }
      synchronized (this.identifier) {
        this.identifier.setSerialNumber(serialNumber);
        frame.setIdentifier(this.identifier.clone());
      }
    } catch (DataDictionaryException e) {
      logger.error(e.getMessage());
      frame = null;
    }

    return frame;
  }

  private TICCoreError createError(int errorCode, String errorMessage, DataDictionary data) {
    TICCoreError error = null;
    try {
      error = new TICCoreError(this.getIdentifier(), errorCode, errorMessage, data);
    } catch (DataDictionaryException e) {
      logger.error(e.getMessage());
    }

    return error;
  }

  private void notifyOnData(TICCoreFrame frame) {
    if (frame == null) {
      return;
    }
    for (TICCoreSubscriber subscriber : this.notifier.getSubscribers()) {
      subscriber.onData(frame);
    }
  }

  private void notifyOnError(TICCoreError error) {
    if (error == null) {
      return;
    }
    for (TICCoreSubscriber subscriber : this.notifier.getSubscribers()) {
      subscriber.onError(error);
    }
  }
}
