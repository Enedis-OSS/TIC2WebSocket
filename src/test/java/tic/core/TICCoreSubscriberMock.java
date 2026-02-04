// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.core;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("javadoc")
public class TICCoreSubscriberMock implements TICCoreSubscriber {
  public List<TICCoreSubscriberOnDataCall> onDataCalls;
  public List<TICCoreSubscriberOnErrorCall> onErrorCalls;

  public TICCoreSubscriberMock() {
    super();
    this.onDataCalls = new ArrayList<TICCoreSubscriberOnDataCall>();
    this.onErrorCalls = new ArrayList<TICCoreSubscriberOnErrorCall>();
  }

  @Override
  public void onData(TICCoreFrame frame) {
    this.onDataCalls.add(new TICCoreSubscriberOnDataCall(frame));
  }

  @Override
  public void onError(TICCoreError error) {
    this.onErrorCalls.add(new TICCoreSubscriberOnErrorCall(error));
  }
}
