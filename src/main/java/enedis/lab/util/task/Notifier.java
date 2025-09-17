// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.util.task;

import java.util.Collection;

/**
 * Notifier interface
 *
 * @param <T>
 */
public interface Notifier<T extends Subscriber>
{
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
