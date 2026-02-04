// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.diagnostic.core.commands;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.ExitCode;
import picocli.CommandLine.Mixin;
import picocli.CommandLine.Option;
import tic.core.TICCoreBase;
import tic.core.TICIdentifier;
import tic.diagnostic.core.TICCoreCli;
import tic.diagnostic.core.TICCoreIdentifierOptions;
import tic.diagnostic.core.TICCorePrintingSubscriber;
import tic.util.time.Time;

@Command(
    name = "subscribe",
    mixinStandardHelpOptions = true,
    description =
        "Subscribe to a TIC and print received frames (TICCore.subscribe). "
            + "Subscription is automatically cancelled at the end (TICCore.unsubscribe).")
public final class TICCoreSubscribeCommand implements Callable<Integer> {
  @CommandLine.ParentCommand TICCoreCli parent;

  @Mixin TICCoreIdentifierOptions identifierOptions = new TICCoreIdentifierOptions();

  @Option(
      names = {"--durationSec"},
      defaultValue = "0",
      paramLabel = "S",
      description =
          "Listen duration in seconds (default: ${DEFAULT-VALUE}). "
              + "Use 0 to wait until Enter is pressed.")
  int durationSec = 0;

  @Option(
      names = {"--frames"},
      defaultValue = "0",
      paramLabel = "N",
      description = "Stop after N frames (0 = unlimited).")
  int maxFrames = 0;

  @Option(
      names = {"--indent"},
      defaultValue = "2",
      paramLabel = "N",
      description = "Frame JSON indentation (default: ${DEFAULT-VALUE}).")
  int indent = 2;

  @Override
  public Integer call() {
    TICCoreBase ticCore = null;
    TICCorePrintingSubscriber subscriber = null;
    TICIdentifier identifier = null;
    try {
      ticCore = parent.startCore();
      identifier = parent.resolveIdentifier(ticCore, identifierOptions);

      subscriber = new TICCorePrintingSubscriber(indent);
      System.out.println("Subscribing to " + identifier + " ...");
      ticCore.subscribe(identifier, subscriber);

      if (durationSec <= 0) {
        System.out.println("Listening. Press Enter to stop...");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        br.readLine();
      } else {
        long deadlineMs = System.currentTimeMillis() + (durationSec * 1000L);
        System.out.println("Listening for " + durationSec + "s (Ctrl+C to interrupt)...");
        while (System.currentTimeMillis() < deadlineMs) {
          if (subscriber.hasError()) {
            break;
          }
          if (maxFrames > 0 && subscriber.getFrameCount() >= maxFrames) {
            break;
          }
          Time.sleep(200);
        }
      }

      if (subscriber.hasError()) {
        System.err.println("[ERROR] Error received: " + subscriber.getLastErrorMessage());
        return ExitCode.SOFTWARE;
      }
      return ExitCode.OK;
    } catch (IllegalArgumentException e) {
      System.err.println("[ERROR] " + e.getMessage());
      return ExitCode.USAGE;
    } catch (Exception e) {
      System.err.println("[ERROR] " + e.getMessage());
      e.printStackTrace(System.err);
      return ExitCode.SOFTWARE;
    } finally {
      // Explicit unsubscribe test with the same subscriber instance.
      if (ticCore != null && subscriber != null && identifier != null) {
        try {
          System.out.println("Unsubscribing from " + identifier + " ...");
          ticCore.unsubscribe(identifier, subscriber);
        } catch (Exception e) {
          System.err.println("[WARN] Unsubscribe failed: " + e.getMessage());
        }
      }
      parent.stopCore(ticCore);
    }
  }
}
