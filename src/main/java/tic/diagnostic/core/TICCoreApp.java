// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.diagnostic.core;

/** Minimal launcher: all CLI parsing lives in {@link TICCoreCli}. */
public final class TICCoreApp {
  public static void main(String[] args) {
    int exitCode = TICCoreCli.execute(args);
    System.exit(exitCode);
  }
}
