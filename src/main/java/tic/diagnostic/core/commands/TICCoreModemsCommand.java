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
import tic.diagnostic.core.TICCoreCli;
import tic.io.modem.ModemDescriptor;
import tic.io.modem.ModemJsonCodec;

@Command(
    name = "modems",
    mixinStandardHelpOptions = true,
    description = "Show modem information (TICCore.getModemsInfo).")
public final class TICCoreModemsCommand implements Callable<Integer> {
  @CommandLine.ParentCommand TICCoreCli parent;

  @Override
  public Integer call() {
    TICCoreBase ticCore = null;
    try {
      ticCore = parent.startCore();
      List<ModemDescriptor> modems = ticCore.getModemsInfo();
      int count = (modems == null) ? 0 : modems.size();
      System.out.println("Modems (" + count + "):");
      if (modems != null) {
        for (ModemDescriptor modem : modems) {
          System.out.println("- " + (modem == null ? "null" : ModemJsonCodec.encode(modem)));
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
