// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.tic.core;

import enedis.lab.util.task.Subscriber;

/**
 * Interface for subscribing to core frame and error notifications.
 *
 * <p>This interface defines methods for receiving frame and error events. It is used as a contract
 * for objects that handle event-driven notifications in core operations.
 *
 * <p>Common use cases include:
 *
 * <ul>
 *   <li>Receiving frame notifications
 *   <li>Handling error events
 *   <li>Implementing event-driven logic
 * </ul>
 *
 * @author Enedis Smarties team
 */
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
