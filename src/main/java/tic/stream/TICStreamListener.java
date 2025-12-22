// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.stream;

import tic.frame.TICFrame;
import tic.util.task.Subscriber;

public interface TICStreamListener extends Subscriber {
  /**
   * Notify when a new TIC frame is received
   *
   * @param frame the frame received
   */
  public void onFrame(TICFrame frame);

  /**
   * Notify when an error occurs during TIC stream reading
   *
   * @param error the error detected
   */
  public void onError(String error);
    
}
