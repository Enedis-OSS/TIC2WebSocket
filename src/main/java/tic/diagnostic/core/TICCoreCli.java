// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.diagnostic.core;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.ExitCode;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;
import tic.core.TICCoreBase;
import tic.core.TICCoreError;
import tic.core.TICCoreFrame;
import tic.core.TICCoreSubscriber;
import tic.core.TICIdentifier;
import tic.diagnostic.core.commands.TICCoreAvailableCommand;
import tic.diagnostic.core.commands.TICCoreModemsCommand;
import tic.diagnostic.core.commands.TICCoreReadCommand;
import tic.diagnostic.core.commands.TICCoreSubscribeCommand;
import tic.frame.TICMode;
import tic.io.modem.ModemDescriptor;
import tic.io.modem.ModemFinder;
import tic.io.modem.ModemFinderBase;
import tic.io.serialport.SerialPortFinderBase;
import tic.io.usb.UsbPortFinderBase;
import tic.util.time.Time;

/**
 * Root Picocli command for TICCore diagnostic.
 *
 * <p>This class contains all CLI parsing and shared behaviors; {@link TICCoreApp} should only
 * delegate to it.
 */
@Command(
    name = "tic-core",
    mixinStandardHelpOptions = true,
    description =
        "TICCore diagnostic (getAvailableTICs, getModemsInfo, readNextFrame, subscribe,"
            + " unsubscribe).",
    subcommands = {
      TICCoreAvailableCommand.class,
      TICCoreModemsCommand.class,
      TICCoreReadCommand.class,
      TICCoreSubscribeCommand.class
    })
public final class TICCoreCli implements Callable<Integer> {
  private static final TICCoreSubscriber NOOP_SUBSCRIBER =
      new TICCoreSubscriber() {
        @Override
        public void onData(TICCoreFrame frame) {
          // no-op
        }

        @Override
        public void onError(TICCoreError error) {
          // no-op
        }
      };

  @Spec CommandSpec spec;

  public static int execute(String[] args) {
    return new CommandLine(new TICCoreCli()).execute(args);
  }

  @Option(
      names = {"-m", "--mode"},
      defaultValue = "AUTO",
      description = "TIC mode: ${COMPLETION-CANDIDATES} (default: ${DEFAULT-VALUE})")
  TICMode mode = TICMode.AUTO;

  @Option(
      names = {"--startPort"},
      paramLabel = "PORT",
      description =
          "Ports to start at launch (useful if discovery is slow). " + "Example: --startPort COM3")
  String startPort;

  @Option(
      names = {"--waitMs"},
      defaultValue = "200",
      paramLabel = "MS",
      description = "Wait after startup to let discovery happen (default: ${DEFAULT-VALUE}).")
  int waitMs = 200;

  @Option(
      names = {"--scan"},
      negatable = true,
      defaultValue = "true",
      description =
          "Immediately scan already-plugged modems (recommended). Use --no-scan to disable.")
  boolean scanAlreadyPlugged = true;

  @Override
  public Integer call() {
    // If no subcommand is provided, show usage.
    if (this.spec != null) {
      this.spec.commandLine().usage(System.out);
    }
    return ExitCode.USAGE;
  }

  public TICCoreBase startCore() {
    List<String> startPorts =
        (this.startPort == null || this.startPort.trim().isEmpty())
            ? null
            : Collections.singletonList(this.startPort.trim());

    TICCoreBase ticCore = new TICCoreBase(this.mode, startPorts);
    System.out.println("Starting TICCore (mode=" + this.mode + ")...");
    ticCore.start();

    if (this.waitMs > 0) {
      Time.sleep(this.waitMs);
    }

    if (this.scanAlreadyPlugged) {
      this.scanAndStartAlreadyPluggedStreams(ticCore);
    }

    return ticCore;
  }

  public void stopCore(TICCoreBase ticCore) {
    if (ticCore == null) {
      return;
    }
    try {
      System.out.println("Stopping TICCore...");
      ticCore.stop();
    } catch (Exception e) {
      System.err.println("[WARN] Failed to stop TICCore: " + e.getMessage());
      e.printStackTrace(System.err);
    }
  }

  void scanAndStartAlreadyPluggedStreams(TICCoreBase ticCore) {
    // Start streams immediately to avoid waiting for the notifier tick.
    try {
      ModemFinder modemFinder =
          ModemFinderBase.create(
              SerialPortFinderBase.getInstance(), UsbPortFinderBase.getInstance());
      List<ModemDescriptor> detected = modemFinder.findAll();
      if (detected == null || detected.isEmpty()) {
        return;
      }
      for (ModemDescriptor descriptor : detected) {
        if (descriptor == null) {
          continue;
        }

        TICIdentifier identifier = new TICIdentifier.Builder()
            .portId(descriptor.portId())
            .portName(descriptor.portName())
            .build();

        // subscribe() will start the stream if needed.
        ticCore.subscribe(identifier, NOOP_SUBSCRIBER);
      }
    } catch (Exception e) {
      System.err.println("[WARN] Unable to scan already-plugged modems: " + e.getMessage());
      e.printStackTrace(System.err);
    }
  }

  public TICIdentifier resolveIdentifier(TICCoreBase ticCore, TICCoreIdentifierOptions idOptions)
      throws IllegalArgumentException {
    TICIdentifier specified = (idOptions == null) ? null : idOptions.toIdentifierOrNull();
    if (specified != null) {
      return specified;
    }

    List<TICIdentifier> available = ticCore.getAvailableTICs();
    if (available == null || available.isEmpty()) {
      throw new IllegalArgumentException(
          "No TIC detected. Plug a modem or provide --portName/--portId/--serialNumber.");
    }
    return available.get(0);
  }
}
