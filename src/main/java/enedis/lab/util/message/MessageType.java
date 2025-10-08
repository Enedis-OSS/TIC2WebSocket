// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.util.message;

/**
 * Enumeration of message types.
 *
 * <p>This enum defines the supported message types, including events, requests, and responses. It is used to categorize
 * messages and control their processing in the communication pipeline.
 *
 * <ul>
 *   <li>{@link #EVENT} - Event messages</li>
 *   <li>{@link #REQUEST} - Request messages</li>
 *   <li>{@link #RESPONSE} - Response messages</li>
 * </ul>
 *
 * @author Enedis Smarties team
 */
public enum MessageType {
  /** Event */
  EVENT,
  /** Request */
  REQUEST,
  /** Response */
  RESPONSE;
}
