// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.tic.core;

import enedis.lab.util.task.Notifier;
import enedis.lab.util.task.Task;

/**
 * Interface for managing core streams, frame acquisition, and subscriber notifications.
 *
 * <p>This interface defines methods for accessing stream identifiers and managing subscribers.
 * It provides a contract for implementations that handle frame delivery and event notification.
 *
 * <p>Common use cases include:
 * <ul>
 *   <li>Acquiring frames from streams</li>
 *   <li>Managing and notifying subscribers</li>
 *   <li>Supporting event-driven stream operations</li>
 * </ul>
 *
 * @author Enedis Smarties team
 */
public interface TICCoreStream extends Notifier<TICCoreSubscriber>, Task {
  /**
   * Get identifier
   *
   * @return The TICIdentifier associated with the stream
   */
  public TICIdentifier getIdentifier();
}
