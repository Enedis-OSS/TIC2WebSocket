// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.mock;

@SuppressWarnings("javadoc")
public class FunctionCall {
  public long captureTime;

  public FunctionCall() {
    super();
    this.captureTime = System.nanoTime();
  }

  public FunctionCall(long captureTime) {
    super();
    this.captureTime = captureTime;
  }
}
