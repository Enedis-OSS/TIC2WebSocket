// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.diagnostic.core.commands;

import java.util.concurrent.Callable;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.ExitCode;
import picocli.CommandLine.Mixin;
import picocli.CommandLine.Option;
import tic.core.TICCoreBase;
import tic.core.TICCoreFrame;
import tic.core.TICIdentifier;
import tic.core.codec.TICCoreFrameCodec;
import tic.diagnostic.core.TICCoreCli;
import tic.diagnostic.core.TICCoreIdentifierOptions;

@Command(
    name = "read",
    mixinStandardHelpOptions = true,
    description = "Read the next frame (TICCore.readNextFrame).")
public final class TICCoreReadCommand implements Callable<Integer> {
  @CommandLine.ParentCommand TICCoreCli parent;

  @Mixin TICCoreIdentifierOptions identifierOptions = new TICCoreIdentifierOptions();

  @Option(
      names = {"--timeoutMs"},
      defaultValue = "30000",
      paramLabel = "MS",
      description = "Read timeout in milliseconds (default: ${DEFAULT-VALUE}).")
  int timeoutMs = 30000;

  @Option(
      names = {"--indent"},
      defaultValue = "2",
      paramLabel = "N",
      description = "Frame JSON indentation (default: ${DEFAULT-VALUE}).")
  int indent = 2;

  @Override
  public Integer call() {
    TICCoreBase ticCore = null;
    try {
      ticCore = parent.startCore();
      TICIdentifier identifier = parent.resolveIdentifier(ticCore, identifierOptions);
      System.out.println("Reading next frame from " + identifier + " ...");
      TICCoreFrame frame = ticCore.readNextFrame(identifier, timeoutMs);
      System.out.println(
          frame == null
              ? "null"
              : TICCoreFrameCodec.getInstance().encodeToJsonString(frame, indent));
      return ExitCode.OK;
    } catch (IllegalArgumentException e) {
      System.err.println("[ERROR] " + e.getMessage());
      return ExitCode.USAGE;
    } catch (Exception e) {
      System.err.println("[ERROR] " + e.getMessage());
      e.printStackTrace(System.err);
      return ExitCode.SOFTWARE;
    } finally {
      parent.stopCore(ticCore);
    }
  }
}
