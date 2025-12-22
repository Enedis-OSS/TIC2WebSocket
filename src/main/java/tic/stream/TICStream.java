// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.stream;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tic.frame.TICFrame;
import tic.frame.TICMode;
import tic.frame.codec.TICFrameCodec;
import tic.io.serialport.SerialPortDescriptor;
import tic.io.serialport.SerialPortFinder;
import tic.io.serialport.SerialPortFinderBase;
import tic.stream.configuration.TICStreamConfiguration;
import tic.stream.configuration.TICStreamConfigurationLoader;
import tic.stream.identifier.TICStreamIdentifier;
import tic.stream.identifier.TICStreamIdentifierType;
import tic.util.task.Task;
import tic.util.task.TaskBase;
import tic.util.task.TaskPeriodicWithSubscribers;

public class TICStream extends TaskPeriodicWithSubscribers<TICStreamListener> {

  private static final Logger logger = LogManager.getLogger(TICStream.class);

  private final TICStreamConfiguration configuration;
  private final SerialPortFinder portFinder;

  private final int timeoutMillis;
  private TICStreamModeDetector streamModeDetector;
  private TICMode currentMode;
  private TICStreamReader streamReader;
  private TICFrame lastFrame;

  public static void main(String[] args) {
    if (args.length != 1) {
      System.err.println("Usage: java tic.stream.TICStream <CONFIG_PATH>");
      System.exit(1);
    }

    String configPath = args[0];

    try {
      TICStreamConfiguration configuration = TICStreamConfigurationLoader.load(configPath);
      TICStream stream = new TICStream(configuration);
      CountDownLatch shutdownLatch = new CountDownLatch(1);

      stream.subscribe(
          new TICStreamListener() {
            @Override
            public void onFrame(TICFrame frame) {
              String serialNumber;
              if (frame.containsGroupLabel("ADSC")) {
                serialNumber = frame.getGroup("ADSC").getValue();
              } else if (frame.containsGroupLabel("ADCO")) {
                serialNumber = frame.getGroup("ADCO").getValue();
              } else {
                System.err.println("Received TIC frame without ADSC/ADCO group");
                return;
              }
              String date = frame.getGroup("DATE").getValue();

              System.out.println("SerialNumber=" + serialNumber + ", DATE=" + date);
            }

            @Override
            public void onError(String error) {
              System.err.println("TIC stream error: " + error);
            }
          });

      Runtime.getRuntime()
          .addShutdownHook(
              new Thread(
                  () -> {
                    stream.stop();
                    shutdownLatch.countDown();
                  }));

      System.out.println("Starting TIC stream with configuration " + configPath);
      System.out.println("Press CTRL-C to stop.");
      stream.start();
      shutdownLatch.await();
    } catch (Exception exception) {
      System.err.println("Failed to start TIC stream: " + exception.getMessage());
      System.exit(1);
    }
  }

  public TICStream(TICStreamConfiguration configuration) {
    this(configuration, SerialPortFinderBase.getInstance());
  }

  public TICStream(TICStreamConfiguration configuration, SerialPortFinder portFinder) {
    this.configuration = Objects.requireNonNull(configuration, "configuration must not be null");
    this.portFinder = Objects.requireNonNull(portFinder, "portFinder must not be null");
    this.timeoutMillis = configuration.getTimeout() * 1000;
    this.initializeStreamReader();
    this.initializeStreamModeDetector();
  }

  private void initializeStreamReader() {
    String portName = this.resolvePortName();
    int baudrate = this.resolveBaudrate(this.configuration.getTicMode());
    if (this.streamReader != null) {
      try {
        this.streamReader.close();
      } catch (Exception e) {
        logger.warn("Failed to close existing TIC stream reader: {}", e.getMessage());
      }
    }
    this.streamReader = new TICStreamReader(portName, baudrate, timeoutMillis);

    if (this.streamModeDetector == null) {
      this.initializeStreamModeDetector();
    } else {
      this.streamModeDetector.setStreamReader(this.streamReader);
    }
  }

  private void initializeStreamModeDetector() {
    if (this.streamReader == null) {
      throw new IllegalStateException("TIC stream reader is not initialized");
    }
    this.streamModeDetector = new TICStreamModeDetector(this.currentMode, this.streamReader);
  }

  // Synchronous
  public TICFrame getNextFrame() {
    byte[] ticFrameAsByte = this.streamReader.read();
    if (ticFrameAsByte == null) {
      return null;
    }
    TICFrame ticFrame = TICFrameCodec.decode(ticFrameAsByte);
    return ticFrame;
  }

  // Asynchronous
  public TICFrame getLastFrame() {
    return this.lastFrame;
  }

  @Override
  protected void process() {
    if (this.streamReader == null) {
      throw new IllegalStateException("TIC stream reader is not initialized");
    }

    if (this.currentMode == null) {
      TICMode newMode = this.streamModeDetector.autoDetectMode();
      if (newMode == null) {
        this.onReadTimeout();
        return;
      }
      this.currentMode = newMode;
      this.initializeStreamReader();
    }

    try {
      TICFrame ticFrame = this.getNextFrame();

      if (ticFrame == null) {
        this.onReadTimeout();
      } else {
        this.notifyOnDataRead(ticFrame);
      }
      this.lastFrame = ticFrame;
    } catch (Exception e) {
      this.notifyOnErrorDetected("TIC read failed: " + e.getMessage());
    }
  }

  private void notifyOnDataRead(TICFrame ticFrame) {
    Collection<TICStreamListener> subscribers = this.getSubscribers();
    for (TICStreamListener subscriber : subscribers) {
      Task task =
          new TaskBase() {
            @Override
            public void process() {
              subscriber.onFrame(ticFrame);
            }
          };
      task.start();
    }
  }

  private void notifyOnErrorDetected(String message) {
    Collection<TICStreamListener> subscribers = this.getSubscribers();
    for (TICStreamListener subscriber : subscribers) {
      Task task =
          new TaskBase() {
            @Override
            public void process() {
              subscriber.onError(message);
            }
          };
      task.start();
    }
  }

  protected void onReadTimeout() {
    this.notifyOnErrorDetected("TIC read timeout");
    try {
      this.streamReader.reset();
    } catch (Exception e) {
    }

    this.currentMode = null;
  }

  private int resolveBaudrate(TICMode mode) {
    if (mode == TICMode.HISTORIC) {
      return 1200;
    }
    return 9600; // Default for STANDARD and AUTO
  }

  private String resolvePortName() {
    TICStreamIdentifier identifier = this.configuration.getIdentifier();
    if (identifier == null) {
      throw new IllegalStateException("No TIC stream identifier configured");
    }

    SerialPortDescriptor descriptor = null;
    if (identifier.getType() == TICStreamIdentifierType.PORT_ID) {
      descriptor = this.portFinder.findByPortId(identifier.getPortId());
      if (descriptor == null) {
        throw new IllegalStateException(
            "Serial port with id " + identifier.getPortId() + " not found");
      }
    } else if (identifier.getType() == TICStreamIdentifierType.PORT_NAME) {
      String configuredName = identifier.getPortName();
      if (configuredName == null || configuredName.isEmpty()) {
        throw new IllegalStateException("Serial port name is not configured");
      }
      descriptor = this.portFinder.findByPortName(configuredName);
      if (descriptor == null) {
        logger.warn(
            "Serial port {} not found via discovery, attempting to open directly", configuredName);
        return configuredName;
      }
    } else {
      throw new IllegalStateException("Unsupported identifier type " + identifier.getType());
    }

    if (descriptor.getPortName() == null || descriptor.getPortName().isEmpty()) {
      throw new IllegalStateException("Resolved descriptor lacks a port name");
    }
    return descriptor.getPortName();
  }
}
