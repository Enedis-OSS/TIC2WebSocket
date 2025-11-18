// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.io;

import enedis.lab.util.time.Time;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import tic.util.task.TaskPeriodicWithSubscribers;

/**
 * Periodic monitor that detects and notifies when ports are plugged or unplugged.
 *
 * <p>This class extends {@link TaskPeriodicWithSubscribers} to provide automatic detection of port
 * connection and disconnection events. It periodically polls the system using a {@link PortFinder}
 * to discover available ports, compares the results with the previous state, and notifies
 * registered {@link PlugSubscriber} instances of any changes.
 *
 * <p>The notifier maintains a cached list of known port descriptors and performs delta detection to
 * identify:
 *
 * <ul>
 *   <li>Newly plugged ports (present in current scan but not in previous)
 *   <li>Unplugged ports (present in previous scan but not in current)
 * </ul>
 *
 * <p>The class is thread-safe and uses atomic references for the finder and synchronized access for
 * the descriptor list to ensure consistency during concurrent access.
 *
 * @param <F> the port finder type that extends PortFinder
 * @param <T> the port descriptor type
 * @author Enedis Smarties team
 * @see PortFinder
 * @see PlugSubscriber
 * @see TaskPeriodicWithSubscribers
 */
public class PortPlugNotifier<F extends PortFinder<T>, T>
    extends TaskPeriodicWithSubscribers<PlugSubscriber<T>> {
  private AtomicReference<F> finder = new AtomicReference<F>();
  private List<T> descriptors = new ArrayList<T>();

  /**
   * Main utility method that runs a port plug monitoring loop.
   *
   * <p>This method sets up a shutdown hook to gracefully stop the notifier when CTRL+C is pressed,
   * subscribes the provided subscriber, starts the notifier, and runs an infinite loop until the
   * notifier is stopped.
   *
   * <p>This is typically used for testing or standalone monitoring applications.
   *
   * @param <F> the port finder type that extends PortFinder
   * @param <T> the port descriptor type
   * @param notifier the port plug notifier instance to run
   * @param subscriber the subscriber to receive plug/unplug notifications
   */
  public static <F extends PortFinder<T>, T> void main(
      PortPlugNotifier<F, T> notifier, PlugSubscriber<T> subscriber) {
    /* 1. Add program shutdown hook when CTRL+C is pressed to exit program properly */
    Runtime.getRuntime()
        .addShutdownHook(
            new Thread(
                new Runnable() {
                  @Override
                  public void run() {
                    notifier.unsubscribe(subscriber);
                    notifier.stop();
                  }
                }));
    /* 4. Add a subscriber to notification service */
    notifier.subscribe(subscriber);
    /* 5. Start the notification service */
    notifier.start();
    /* 6. Execute forever loop until CTRL+C is pressed */
    while (notifier.isRunning()) {
      Time.sleep(1000);
    }
  }

  /**
   * Constructs a PortPlugNotifier with the specified polling period and port finder.
   *
   * <p>The notifier will periodically scan for ports using the provided finder at the specified
   * interval. The period determines the responsiveness of plug/unplug detection.
   *
   * @param period the polling period in milliseconds
   * @param finder the port finder used to discover available ports
   */
  public PortPlugNotifier(long period, F finder) {
    super(period);
    this.setFinder(finder);
  }

  /**
   * Sets the port finder used to discover available ports.
   *
   * <p>This method updates the finder used for port discovery. The finder is stored in an atomic
   * reference to ensure thread-safe access during periodic polling.
   *
   * @param finder the port finder to use for discovering ports
   * @throws IllegalArgumentException if finder is null
   */
  public void setFinder(F finder) {
    if (finder == null) {
      throw new IllegalArgumentException("Cannot set null finder");
    }
    this.finder.set(finder);
  }

  /**
   * Periodically executed process that detects port changes and notifies subscribers.
   *
   * <p>This method is called at each polling interval and performs the following steps:
   *
   * <ol>
   *   <li>Discovers currently available ports using the finder
   *   <li>Checks for unplugged ports (in old list but not in new list)
   *   <li>Checks for newly plugged ports (in new list but not in old list)
   *   <li>Updates the internal descriptor cache with the new state
   * </ol>
   */
  @Override
  protected void process() {
    List<T> newDescriptors = this.finder.get().findAll();

    this.checkAndNotifyIfUnplugged(newDescriptors);
    this.checkAndNotifyIfPlugged(newDescriptors);
    this.updateDescriptors(newDescriptors);
  }

  /**
   * Checks for unplugged ports and notifies subscribers.
   *
   * <p>This method compares the current descriptor list with the new list to identify ports that
   * were present before but are no longer available. For each unplugged port, it notifies all
   * subscribers.
   *
   * @param newDescriptors the newly discovered list of port descriptors
   */
  private void checkAndNotifyIfUnplugged(List<T> newDescriptors) {
    Iterator<T> it = this.descriptors.iterator();

    while (it.hasNext()) {
      T descriptor = it.next();
      if (!newDescriptors.contains(descriptor)) {
        this.notifyOnUnplugged(descriptor);
      }
    }
  }

  /**
   * Checks for newly plugged ports and notifies subscribers.
   *
   * <p>This method compares the new descriptor list with the current list to identify ports that
   * are newly available. For each newly plugged port, it notifies all subscribers.
   *
   * @param newDescriptors the newly discovered list of port descriptors
   */
  private void checkAndNotifyIfPlugged(List<T> newDescriptors) {
    Iterator<T> it = newDescriptors.iterator();

    while (it.hasNext()) {
      T newDescriptor = it.next();
      if (!this.descriptors.contains(newDescriptor)) {
        this.notifyOnPlugged(newDescriptor);
      }
    }
  }

  /**
   * Updates the internal descriptor cache with the new list of ports.
   *
   * <p>This method clears the current descriptor list and replaces it with the newly discovered
   * ports. The operation is synchronized to ensure thread-safe access.
   *
   * @param newDescriptors the new list of port descriptors to cache
   */
  private void updateDescriptors(List<T> newDescriptors) {
    synchronized (this.descriptors) {
      this.descriptors.clear();
      this.descriptors.addAll(newDescriptors);
    }
  }

  /**
   * Notifies all subscribers that a port has been plugged in.
   *
   * <p>This method iterates through all subscribed listeners and calls their onPlugged method with
   * the port information.
   *
   * @param info the information about the port that was plugged in
   */
  private void notifyOnPlugged(T info) {
    for (PlugSubscriber<T> subscriber : this.notifier.getSubscribers()) {
      subscriber.onPlugged(info);
    }
  }

  /**
   * Notifies all subscribers that a port has been unplugged.
   *
   * <p>This method iterates through all subscribed listeners and calls their onUnplugged method
   * with the port information.
   *
   * @param info the information about the port that was unplugged
   */
  private void notifyOnUnplugged(T info) {
    for (PlugSubscriber<T> subscriber : this.notifier.getSubscribers()) {
      subscriber.onUnplugged(info);
    }
  }
}
