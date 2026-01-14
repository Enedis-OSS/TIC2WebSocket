// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import tic.frame.TICMode;
import tic.io.modem.ModemFinder;

public class TICCoreStreamMock implements TICCoreStream {
  public static List<TICCoreStreamMock> streams = new ArrayList<TICCoreStreamMock>();
  public Collection<TICCoreSubscriber> subscribers;
  public boolean running;
  public TICIdentifier identifier;

  public TICCoreStreamMock(String portId, String portName, TICMode mode, ModemFinder finder) {
    super();
    this.subscribers = new HashSet<TICCoreSubscriber>();
    this.running = false;
    this.identifier = new TICIdentifier.Builder().portId(portId).portName(portName).build();
    streams.add(this);
  }

  @Override
  public TICIdentifier getIdentifier() {
    return this.identifier;
  }

  @Override
  public boolean isRunning() {
    return this.running;
  }

  @Override
  public void start() {
    this.running = true;
  }

  @Override
  public void stop() {
    this.running = false;
  }

  @Override
  public Collection<TICCoreSubscriber> getSubscribers() {
    return this.subscribers;
  }

  @Override
  public boolean hasSubscriber(TICCoreSubscriber subscriber) {
    return this.subscribers.contains(subscriber);
  }

  @Override
  public void subscribe(TICCoreSubscriber subscriber) {
    this.subscribers.add(subscriber);
  }

  @Override
  public void unsubscribe(TICCoreSubscriber subscriber) {
    this.subscribers.remove(subscriber);
  }

  public void notifyOnData(TICCoreFrame frame) {
    for (TICCoreSubscriber subscriber : this.subscribers) {
      subscriber.onData(frame);
    }
  }

  public void notifyOnError(TICCoreError error) {
    for (TICCoreSubscriber subscriber : this.subscribers) {
      subscriber.onError(error);
    }
  }
}
