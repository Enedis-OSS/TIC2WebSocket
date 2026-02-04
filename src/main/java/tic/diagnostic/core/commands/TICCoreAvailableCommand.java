// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.diagnostic.core.commands;

import java.util.List;
import java.util.concurrent.Callable;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.ExitCode;
import tic.core.TICCoreBase;
import tic.core.TICIdentifier;
import tic.diagnostic.core.TICCoreCli;

@Command(
    name = "available",
    mixinStandardHelpOptions = true,
    description = "List available TICs (TICCore.getAvailableTICs).")
public final class TICCoreAvailableCommand implements Callable<Integer> {
  @CommandLine.ParentCommand TICCoreCli parent;

  @Override
  public Integer call() {
    TICCoreBase ticCore = null;
    try {
      ticCore = parent.startCore();
      List<TICIdentifier> available = ticCore.getAvailableTICs();
      int count = (available == null) ? 0 : available.size();
      System.out.println("Available TICs (" + count + "):");
      if (available != null) {
        for (TICIdentifier id : available) {
          System.out.println("- " + id);
        }
      }
      return ExitCode.OK;
    } catch (Exception e) {
      System.err.println("[ERROR] " + e.getMessage());
      e.printStackTrace(System.err);
      return ExitCode.SOFTWARE;
    } finally {
      parent.stopCore(ticCore);
    }
  }
}
