// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
// 
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.io;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReference;

import enedis.lab.types.DataArrayList;
import enedis.lab.types.DataList;
import enedis.lab.util.task.TaskPeriodicWithSubscribers;
import enedis.lab.util.time.Time;

/**
 * Class used to notify when a port has been plugged or unplugged
 *
 * @param <F>
 *            the port finder interface
 * @param <T>
 *            the port descriptor class
 */
public class PortPlugNotifier<F extends PortFinder<T>, T> extends TaskPeriodicWithSubscribers<PlugSubscriber<T>>
{
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// CONSTANTS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// TYPES
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// STATIC METHODS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Program writing on the output stream when a port has been plugged or unplugged
	 *
	 * @param <F>
	 *            the port finder interface
	 * @param <T>
	 *            the port descriptor class
	 * @param notifier
	 *            the port plug notifier instance
	 * @param subscriber
	 *            the port plus subscriber instance
	 */
	public static <F extends PortFinder<T>, T> void main(PortPlugNotifier<F, T> notifier, PlugSubscriber<T> subscriber)
	{
		/* 1. Add program shutdown hook when CTRL+C is pressed to exit program properly */
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				notifier.unsubscribe(subscriber);
				notifier.stop();
			}
		}));
		/* 4. Add a subscriber to notification service */
		notifier.subscribe(subscriber);
		/* 5. Start the notification service */
		notifier.start();
		/* 6. Execute forever loop until CTRL+C is pressed */
		while (notifier.isRunning())
		{
			Time.sleep(1000);
		}
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// ATTRIBUTES
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private AtomicReference<F>	finder		= new AtomicReference<F>();
	private DataList<T>			descriptors	= new DataArrayList<T>();

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// CONSTRUCTORS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Constructor with all parameters
	 *
	 * @param period
	 *            the period (in milliseconds) used to look for plugged or unplugged serial port
	 * @param finder
	 *            the serial port finder interface used to find all serial port descriptors
	 */
	public PortPlugNotifier(long period, F finder)
	{
		super(period);
		this.setFinder(finder);
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// INTERFACE
	/// interfaceName
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// PUBLIC METHODS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Set the finder
	 *
	 * @param finder
	 *            the serial port finder interface used to find all serial port descriptors
	 * @throws IllegalArgumentException
	 *             if finder is null
	 */
	public void setFinder(F finder)
	{
		if (finder == null)
		{
			throw new IllegalArgumentException("Cannot set null finder");
		}
		this.finder.set(finder);
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// PROTECTED METHODS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	protected void process()
	{
		DataList<T> newDescriptors = this.finder.get().findAll();

		this.checkAndNotifyIfUnplugged(newDescriptors);
		this.checkAndNotifyIfPlugged(newDescriptors);
		this.updateDescriptors(newDescriptors);
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// PRIVATE METHODS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void checkAndNotifyIfUnplugged(DataList<T> newDescriptors)
	{
		Iterator<T> it = this.descriptors.iterator();

		while (it.hasNext())
		{
			T descriptor = it.next();
			if (!newDescriptors.contains(descriptor))
			{
				this.notifyOnUnplugged(descriptor);
			}
		}
	}

	private void checkAndNotifyIfPlugged(DataList<T> newDescriptors)
	{
		Iterator<T> it = newDescriptors.iterator();

		while (it.hasNext())
		{
			T newDescriptor = it.next();
			if (!this.descriptors.contains(newDescriptor))
			{
				this.notifyOnPlugged(newDescriptor);
			}
		}
	}

	private void updateDescriptors(DataList<T> newDescriptors)
	{
		synchronized (this.descriptors)
		{
			this.descriptors.clear();
			this.descriptors.addAll(newDescriptors);
		}
	}

	private void notifyOnPlugged(T info)
	{
		for (PlugSubscriber<T> subscriber : this.notifier.getSubscribers())
		{
			subscriber.onPlugged(info);
		}
	}

	private void notifyOnUnplugged(T info)
	{
		for (PlugSubscriber<T> subscriber : this.notifier.getSubscribers())
		{
			subscriber.onUnplugged(info);
		}
	}
}
