// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.tic.core;

import enedis.lab.util.task.TaskBase;

public class TICCoreReadNextFrameTask extends TaskBase {
  public TICCore ticCore;
  public TICIdentifier identifier;
  public TICCoreFrame frame;
  public Exception exception;
  public int timeout;

  public TICCoreReadNextFrameTask(TICCore ticCore, TICIdentifier identifier) {
    this(ticCore, identifier, -1);
  }

  public TICCoreReadNextFrameTask(TICCore ticCore, TICIdentifier identifier, int timeout) {
    super();
    this.ticCore = ticCore;
    this.identifier = identifier;
    this.timeout = timeout;
  }

  @Override
  protected void process() {
    try {
      if (this.timeout > 0) {
        this.frame = this.ticCore.readNextFrame(this.identifier, this.timeout);
      } else {
        this.frame = this.ticCore.readNextFrame(this.identifier);
      }
    } catch (Exception exception) {
      this.exception = exception;
    }
  }
}
