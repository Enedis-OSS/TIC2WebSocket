// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.io;

import enedis.lab.util.task.Subscriber;

/**
 * Listener interface for receiving plug and unplug events from devices or hardware.
 *
 * <p>This interface extends {@link Subscriber} and defines callback methods that are invoked when
 * hardware devices are connected (plugged) or disconnected (unplugged) from the system.
 * Implementations of this interface can be registered with device monitors or port finders to
 * receive notifications about hardware state changes.
 *
 * <p>The interface uses generics to allow different types of information to be passed with the
 * plug/unplug events, such as device descriptors, port information, or other device-specific
 * details.
 *
 * @param <T> the type of information associated with plugged or unplugged notifications
 * @author Enedis Smarties team
 * @see Subscriber
 */
public interface PlugSubscriber<T> extends Subscriber {
  /**
   * Called when a device has been plugged into the system.
   *
   * <p>This method is invoked when the system detects that a new device has been connected.
   * Implementations should handle the new device information according to their specific
   * requirements.
   *
   * @param info the information about the device that has been plugged in
   */
  public void onPlugged(T info);

  /**
   * Called when a device has been unplugged from the system.
   *
   * <p>This method is invoked when the system detects that a device has been disconnected.
   * Implementations should handle the removal appropriately, such as closing connections or
   * releasing resources associated with the device.
   *
   * @param info the information about the device that has been unplugged
   */
  public void onUnplugged(T info);
}
