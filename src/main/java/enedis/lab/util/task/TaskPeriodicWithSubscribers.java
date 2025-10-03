// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
// 
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.util.task;

import java.util.Collection;

/**
 * Periodic task with subscribers basic implementation
 *
 * @param <T>
 *            the subscriber type
 */
public abstract class TaskPeriodicWithSubscribers<T extends Subscriber> extends TaskPeriodic implements Notifier<T>
{
	protected Notifier<T> notifier;

	/**
	 * Default constructor
	 */
	public TaskPeriodicWithSubscribers()
	{
		super();
		this.notifier = new NotifierBase<T>();
	}

	/**
	 * Constructor setting period
	 *
	 * @param period
	 *            the period in milliseconds
	 */
	public TaskPeriodicWithSubscribers(long period)
	{
		super(period);
		this.notifier = new NotifierBase<T>();
	}

	@Override
	public void subscribe(T listener)
	{
		this.notifier.subscribe(listener);
	}

	@Override
	public void unsubscribe(T listener)
	{
		this.notifier.unsubscribe(listener);
	}

	@Override
	public boolean hasSubscriber(T listener)
	{
		return this.notifier.hasSubscriber(listener);
	}

	@Override
	public Collection<T> getSubscribers()
	{
		return this.notifier.getSubscribers();
	}

}
