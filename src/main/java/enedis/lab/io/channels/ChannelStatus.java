// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.io.channels;

/**
 * Enumeration representing the operational status of a communication channel
 * in the TIC (Télé Information Client) system.
 * 
 * <p>This enum defines the possible states that a channel can be in during its lifecycle,
 * from initialization through operation to error conditions. The status is used to monitor
 * and manage the health and availability of communication channels.
 * 
 * @author Jehan BOUSCH
 * @author Mathieu SABARTHES
 * @since 1.0
 */
public enum ChannelStatus {
  /**
   * Channel is stopped and not actively processing data.
   * This is the initial state when a channel is created but not yet started,
   * or when it has been explicitly stopped.
   */
  STOPPED,
  
  /**
   * Channel is started and actively processing data.
   * The channel is operational and ready to handle communication requests.
   */
  STARTED,
  
  /**
   * Channel has encountered an error and is not functioning properly.
   * This state indicates that the channel needs attention or recovery actions.
   */
  ERROR
}
