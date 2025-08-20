// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.io;

import enedis.lab.util.task.Subscriber;

/**
 * Interface used to be notified when something has been plugged or unplugged
 *
 * @param <T> the information class associated with plugged or unplugged notification
 */
public interface PlugSubscriber<T> extends Subscriber
{
	/**
	 * Notification when something has been plugged
	 *
	 * @param info the information on what has been plugged
	 */
	public void onPlugged(T info);
	/**
	 * Notification when something has been unplugged
	 *
	 * @param info the information on what has been unplugged
	 */
	public void onUnplugged(T info);
}
