// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.tic.core;

import enedis.lab.io.tic.TICPortDescriptor;
import enedis.lab.util.task.Task;
import java.util.List;

/**
 * Core interface for managing frame reading and subscriber notifications.
 *
 * <p>This interface defines methods for reading frames, managing subscribers, and retrieving modem information.
 *
 * <p>Common use cases include:
 * <ul>
 *   <li>Reading frames from available sources</li>
 *   <li>Managing and notifying subscribers</li>
 *   <li>Retrieving modem and identifier information</li>
 * </ul>
 *
 * @author Enedis Smarties team
 */
public interface TICCore extends Task {
  /**
   * Get available TICs
   *
   * @return The collection of TIC identifiers
   */
  public List<TICIdentifier> getAvailableTICs();

  /**
   * Get modems informations
   *
   * @return The collection of TIC port descriptors
   */
  public List<TICPortDescriptor> getModemsInfo();

  /**
   * Read next frame
   *
   * @param identifier the TIC identifier
   * @return Frame read or null if timed out
   * @throws TICCoreException if identifier not found or if any error occurs
   */
  public TICCoreFrame readNextFrame(TICIdentifier identifier) throws TICCoreException;

  /**
   * Read next frame
   *
   * @param identifier the TIC identifier
   * @param timeout the read timeout in milliseconds
   * @return Frame read or null if timed out
   * @throws TICCoreException if identifier not found or if any error occurs
   */
  public TICCoreFrame readNextFrame(TICIdentifier identifier, int timeout) throws TICCoreException;

  /**
   * Add a subscriber
   *
   * @param subscriber
   */
  public void subscribe(TICCoreSubscriber subscriber);

  /**
   * Remove a subscriber
   *
   * @param subscriber
   */
  public void unsubscribe(TICCoreSubscriber subscriber);

  /**
   * Add a subscriber with identifier
   *
   * @param identifier the identifier used for subscription
   * @param listener the subscriber reference
   * @throws Exception on subscription failure
   */
  public void subscribe(TICIdentifier identifier, TICCoreSubscriber listener)
      throws TICCoreException;

  /**
   * Remove a subscriber with identifier
   *
   * @param identifier the identifier used for subscription
   * @param listener the subscriber reference
   * @throws Exception if filter or listener not found
   */
  public void unsubscribe(TICIdentifier identifier, TICCoreSubscriber listener)
      throws TICCoreException;

  /**
   * Get TICs identifier associated with a subscriber
   *
   * @param listener the subscriber reference
   * @return The collection of TIC identifiers for the subscriber
   */
  public List<TICIdentifier> getIndentifiers(TICCoreSubscriber listener);
}
