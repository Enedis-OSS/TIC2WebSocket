// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.util.task;

import java.util.Collection;

/**
 * Interface for managing subscribers and sending notifications.
 *
 * <p>This interface defines methods for adding, removing, and querying subscribers, as well as
 * retrieving the current set of subscribers. It is used as a generic contract for event-driven
 * and observer patterns.
 *
 * <p>Common use cases include:
 * <ul>
 *   <li>Managing lists of listeners or observers</li>
 *   <li>Sending notifications to registered subscribers</li>
 *   <li>Supporting event-driven architectures</li>
 * </ul>
 *
 * @param <T> the subscriber type
 * @author Enedis Smarties team
 */
public interface Notifier<T extends Subscriber> {
  /**
   * Add a subscriber
   *
   * @param subscriber
   */
  public void subscribe(T subscriber);

  /**
   * Remove a subscriber
   *
   * @param subscriber
   */
  public void unsubscribe(T subscriber);

  /**
   * Check if the given subscriber has been added
   *
   * @param subscriber
   * @return true if the given subscriber has been added
   */
  public boolean hasSubscriber(T subscriber);

  /**
   * Get subscribers
   *
   * @return The subscribers collection
   */
  public Collection<T> getSubscribers();
}
