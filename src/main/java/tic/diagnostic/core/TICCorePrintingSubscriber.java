// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.diagnostic.core;

import tic.core.TICCoreError;
import tic.core.TICCoreFrame;
import tic.core.TICCoreSubscriber;
import tic.core.codec.TICCoreErrorCodec;
import tic.core.codec.TICCoreFrameCodec;

/** Subscriber that prints received frames/errors to stdout/stderr. */
public final class TICCorePrintingSubscriber implements TICCoreSubscriber {
  private final int indent;
  private volatile long frameCount;
  private volatile TICCoreError lastError;

  public TICCorePrintingSubscriber(int indent) {
    this.indent = indent;
    this.frameCount = 0;
    this.lastError = null;
  }

  @Override
  public void onData(TICCoreFrame frame) {
    this.frameCount++;
    System.out.println("--- Frame #" + this.frameCount + " ---");
    try {
      System.out.println(frame == null ? "null" : TICCoreFrameCodec.getInstance().encodeToJsonString(frame, this.indent));
    } catch (Exception e) {
      System.err.println("[ERROR] Failed to encode frame to JSON string: " + e.getMessage());
    }
  }

  @Override
  public void onError(TICCoreError error) {
    this.lastError = error;
    System.err.println("--- Error ---");
    try {
      System.err.println(
          error == null
              ? "null"
              : TICCoreErrorCodec.getInstance().encodeToJsonString(error, this.indent));
    } catch (Exception e) {
      System.err.println("[ERROR] Failed to encode error to JSON string: " + e.getMessage());
    }
  }

  public long getFrameCount() {
    return this.frameCount;
  }

  public boolean hasError() {
    return this.lastError != null;
  }

  public String getLastErrorMessage() {
    return this.lastError == null ? null : this.lastError.getErrorMessage();
  }
}
