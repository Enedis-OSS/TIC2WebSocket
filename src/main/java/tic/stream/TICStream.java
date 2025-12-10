// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.stream;

import enedis.lab.protocol.tic.frame.TICFrame;
import tic.stream.configuration.TICStreamConfiguration;
import tic.util.task.Subscriber;
import tic.util.task.TaskPeriodicWithSubscribers;


public class TICStream extends TaskPeriodicWithSubscribers<Subscriber>{
  public TICStream(TICStreamConfiguration configuration) {
    configuration.getTicMode();
    // TODO: set TICMode from configuration
  }

  public byte[] read()  {
    return null;
  }

  public TICFrame readFrame() {
    return null;
  }

  @Override
  protected void process() {
    throw new UnsupportedOperationException("Unimplemented method 'process'");
  }

  @Override
  public void subscribe(Subscriber listener) {
    throw new UnsupportedOperationException("Unimplemented method 'subscribe'");
  }
  @Override
  public void unsubscribe(Subscriber listener) {
    throw new UnsupportedOperationException("Unimplemented method 'unsubscribe'");
  }

  @Override
  public void start() {
    throw new UnsupportedOperationException("Unimplemented method 'start'");
  }
  @Override
  public void stop() {
    throw new UnsupportedOperationException("Unimplemented method 'stop'");
  }
}
