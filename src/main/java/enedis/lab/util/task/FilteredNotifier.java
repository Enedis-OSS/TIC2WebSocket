// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.util.task;

import java.util.Collection;
import java.util.function.Predicate;

/**
 * Notifier interface with filter
 *
 * @param <F> the filter
 * @param <T> the subscriber
 */
public interface FilteredNotifier<F, T extends Subscriber> extends Notifier<T> {
  /**
   * Add a subscriber with filter
   *
   * @param filter the filter used for subscription
   * @param listener the subscriber reference
   * @throws Exception on subscription failure
   */
  public void subscribe(F filter, T listener) throws Exception;

  /**
   * Remove a subscriber with filter
   *
   * @param filter the filter used for subscription
   * @param listener the subscriber reference
   * @throws Exception if filter or listener not found
   */
  public void unsubscribe(F filter, T listener) throws Exception;

  /**
   * Check if the given filter has a given subscriber
   *
   * @param filter the filter used for subscription
   * @param listener the subscriber reference
   * @return true if the given subscriber exists
   */
  public boolean hasSubscriber(F filter, T listener);

  /**
   * Get subscribers associated with filter
   *
   * @param filter the filter used for subscription
   * @return The subscribers collection
   */
  public Collection<T> getSubscribers(F filter);

  /**
   * Get subscribers including global and/or filters
   *
   * @param includeGlobal indicates if includes global subscribers
   * @param includeFilter indicates if includes filters subscribers
   * @return The subscribers collection
   */
  public Collection<T> getSubscribers(boolean includeGlobal, boolean includeFilter);

  /**
   * Get subscribers associated with predicate filter
   *
   * @param predicate the predicate used to test with filters used for subscription
   * @param includeGlobal indicates if includes global subscribers
   * @return The subscribers collection
   */
  public Collection<T> getSubscribers(Predicate<F> predicate, boolean includeGlobal);

  /**
   * Check if the given filter has been set
   *
   * @param filter the filter used for subscription
   * @return true if the given filter exists
   */
  public boolean hasFilter(F filter);

  /**
   * Get filters
   *
   * @return The filters collection
   */
  public Collection<F> getFilters();

  /**
   * Get filters associated with listener
   *
   * @param listener the subscriber reference
   * @return The filters collection
   */
  public Collection<F> getFilters(T listener);
}
