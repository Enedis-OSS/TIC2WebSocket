// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.util.task;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

/**
 * Generic base implementation for managing filtered notifications to subscribers.
 *
 * <p>This class provides mechanisms for subscribing, unsubscribing, and querying subscribers with
 * associated filters. It supports both filtered and unfiltered (global) subscriptions, enabling
 * flexible event delivery and observer patterns.
 *
 * <p>Common use cases include:
 *
 * <ul>
 *   <li>Delivering events to subscribers that match specific filters
 *   <li>Supporting selective notification in observer patterns
 *   <li>Extending for custom filtering logic
 * </ul>
 *
 * @param <F> the filter type
 * @param <T> the subscriber type
 */
public class FilteredNotifierBase<F, T extends Subscriber> implements FilteredNotifier<F, T> {
  protected Map<F, Collection<T>> subscribersFiltered;
  private Lock subscribersFilteredLock = new ReentrantLock();
  protected Collection<T> subscribersUnfiltered;
  private Lock subscribersUnfilteredLock = new ReentrantLock();

  /** Default constructor */
  public FilteredNotifierBase() {
    super();
    this.subscribersFiltered = new ConcurrentHashMap<F, Collection<T>>();
    this.subscribersUnfiltered = new CopyOnWriteArraySet<T>();
  }

  @Override
  public void subscribe(T listener) {
    if (listener == null) {
      return;
    }
    this.subscribersUnfilteredLock.lock();
    this.subscribersUnfiltered.add(listener);
    this.subscribersUnfilteredLock.unlock();
  }

  @Override
  public void unsubscribe(T listener) {
    if (listener == null) {
      return;
    }
    for (F filter : this.getFilters()) {
      this.unsubscribe(filter, listener);
    }
    this.subscribersUnfilteredLock.lock();
    this.subscribersUnfiltered.remove(listener);
    this.subscribersUnfilteredLock.unlock();
  }

  @Override
  public boolean hasSubscriber(T listener) {
    for (F filter : this.getFilters()) {
      if (this.hasSubscriber(filter, listener)) {
        return true;
      }
    }

    this.subscribersUnfilteredLock.lock();
    boolean hasSubscriber = this.subscribersUnfiltered.contains(listener);
    this.subscribersUnfilteredLock.unlock();

    return hasSubscriber;
  }

  @Override
  public Collection<T> getSubscribers() {
    return this.getSubscribers(true, true);
  }

  @Override
  public void subscribe(F filter, T listener) {
    if (filter == null || listener == null) {
      return;
    }
    Collection<T> subscribers;
    if (this.hasFilter(filter)) {
      subscribers = this.getSubscribers(filter);
      if (!subscribers.contains(listener)) {
        subscribers.add(listener);
      }
    } else {
      subscribers = new HashSet<T>();
      subscribers.add(listener);
    }
    this.subscribersFilteredLock.lock();
    this.subscribersFiltered.put(filter, subscribers);
    this.subscribersFilteredLock.unlock();
  }

  @Override
  public void unsubscribe(F filter, T listener) {
    if (filter == null || listener == null) {
      return;
    }
    Collection<T> subscribers = this.getSubscribers(filter);
    if (subscribers.contains(listener)) {
      subscribers.remove(listener);
      if (subscribers.size() == 0) {
        this.subscribersFilteredLock.lock();
        this.subscribersFiltered.remove(filter);
        this.subscribersFilteredLock.unlock();
      }
    }
  }

  @Override
  public boolean hasSubscriber(F filter, T listener) {
    Collection<T> subscribers = this.getSubscribers(filter);

    return subscribers.contains(listener);
  }

  @Override
  public Collection<T> getSubscribers(F filter) {
    this.subscribersFilteredLock.lock();
    Collection<T> subscribersFiltered =
        (this.hasFilter(filter)) ? this.subscribersFiltered.get(filter) : new HashSet<T>();
    this.subscribersFilteredLock.unlock();

    return subscribersFiltered;
  }

  @Override
  public Collection<T> getSubscribers(boolean includeGlobal, boolean includeFilter) {
    Collection<T> subscribers = new HashSet<T>();

    if (includeFilter) {
      for (F filter : this.getFilters()) {
        Collection<T> filterSubscribers = this.getSubscribers(filter);
        subscribers.addAll(filterSubscribers);
      }
    }
    if (includeGlobal) {
      this.subscribersUnfilteredLock.lock();
      subscribers.addAll(this.subscribersUnfiltered);
      this.subscribersUnfilteredLock.unlock();
    }

    return subscribers;
  }

  @Override
  public Collection<T> getSubscribers(Predicate<F> predicate, boolean includeGlobal) {
    Collection<T> subscribers = new HashSet<T>();

    if (predicate != null) {
      this.subscribersFilteredLock.lock();
      for (F filter : this.subscribersFiltered.keySet()) {
        if (predicate.test(filter)) {
          subscribers.addAll(this.subscribersFiltered.get(filter));
        }
      }
      this.subscribersFilteredLock.unlock();
    }
    if (includeGlobal) {
      this.subscribersUnfilteredLock.lock();
      subscribers.addAll(this.subscribersUnfiltered);
      this.subscribersUnfilteredLock.unlock();
    }

    return subscribers;
  }

  @Override
  public boolean hasFilter(F filter) {
    this.subscribersFilteredLock.lock();
    boolean hasFilter = (filter != null) ? this.subscribersFiltered.containsKey(filter) : false;
    this.subscribersFilteredLock.unlock();

    return hasFilter;
  }

  @Override
  public Collection<F> getFilters() {
    this.subscribersFilteredLock.lock();
    Collection<F> subscribersFiltered = this.subscribersFiltered.keySet();
    this.subscribersFilteredLock.unlock();

    return subscribersFiltered;
  }

  @Override
  public Collection<F> getFilters(T listener) {
    Collection<F> filters = new HashSet<F>();

    for (F filter : this.getFilters()) {
      Collection<T> subscribers = this.getSubscribers(filter);
      if (subscribers.contains(listener)) {
        filters.add(filter);
      }
    }

    return filters;
  }
}
