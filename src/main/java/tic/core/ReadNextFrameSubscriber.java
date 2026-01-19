// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.core;

/**
 * Subscriber implementation for receiving the next frame and error notifications.
 *
 * <p>This class listens for frame and error events, storing the latest received frame or error. It
 * provides methods to access the received data and to clear its state.
 *
 * <p>Common use cases include:
 *
 * <ul>
 *   <li>Receiving and processing the next available frame
 *   <li>Handling error notifications
 *   <li>Resetting subscriber state for reuse
 * </ul>
 *
 * @author Enedis Smarties team
 */
public class ReadNextFrameSubscriber implements TICCoreSubscriber {
  private TICCoreFrame frame;
  private TICCoreError error;

  public ReadNextFrameSubscriber() {
    super();
    this.clear();
  }

  @Override
  public void onData(TICCoreFrame frame) {
    this.frame = frame;
  }

  @Override
  public void onError(TICCoreError error) {
    this.error = error;
  }

  public TICCoreFrame getData() {
    return this.frame;
  }

  public TICCoreError getError() {
    return this.error;
  }

  public void clear() {
    this.frame = null;
    this.error = null;
  }
}
