// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.tic.core;

import enedis.lab.util.task.Subscriber;

/** TICCore subscriber interface */
public interface TICCoreSubscriber extends Subscriber {
  /**
   * Notify when data
   *
   * @param frame the frame received
   */
  public void onData(TICCoreFrame frame);

  /**
   * Notify when error
   *
   * @param error the error detected
   */
  public void onError(TICCoreError error);
}
