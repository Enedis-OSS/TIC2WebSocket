// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.tic.core;

import enedis.lab.util.task.Notifier;
import enedis.lab.util.task.Task;

/** TICCore stream interface */
public interface TICCoreStream extends Notifier<TICCoreSubscriber>, Task {
  /**
   * Get identifier
   *
   * @return The TICIdentifier associated with the stream
   */
  public TICIdentifier getIdentifier();
}
