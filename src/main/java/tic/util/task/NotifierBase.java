// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.util.task;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Generic base implementation for managing subscribers and sending
 * notifications.
 *
 * <p>
 * This class provides mechanisms for adding, removing, and querying
 * subscribers. It is used as a
 * foundation for event-driven and observer patterns, supporting flexible
 * notification delivery.
 *
 * <p>
 * Common use cases include:
 *
 * <ul>
 * <li>Managing lists of listeners or observers
 * <li>Sending notifications to registered subscribers
 * <li>Extending for custom notification logic
 * </ul>
 *
 * @param <T> the subscriber type
 */
public class NotifierBase<T extends Subscriber> implements Notifier<T> {
    protected Set<T> subscribers;

    /** Default constructor */
    public NotifierBase() {
        super();
        this.subscribers = new CopyOnWriteArraySet<T>();
    }

    @Override
    public void subscribe(T listener) {
        if (listener != null && !this.subscribers.contains(listener)) {
            this.subscribers.add(listener);
        }
    }

    @Override
    public void unsubscribe(T listener) {
        if (listener != null && this.subscribers.contains(listener)) {
            this.subscribers.remove(listener);
        }
    }

    @Override
    public boolean hasSubscriber(T listener) {
        return this.subscribers.contains(listener);
    }

    @Override
    public Collection<T> getSubscribers() {
        return this.subscribers;
    }
}
