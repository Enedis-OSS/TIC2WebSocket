// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.io.datastreams;

/**
 * Enumeration representing the operational status of a data stream.
 *
 * <p>This enum defines the possible states that a data stream can be in during its lifecycle. The
 * status reflects the current operational state of the stream and is used to monitor and manage
 * stream health and availability. Status changes are typically notified to registered listeners
 * through the {@link DataStreamListener} interface.
 *
 * @author Enedis Smarties team
 * @see DataStream
 * @see DataStreamBase
 * @see DataStreamListener
 */
public enum DataStreamStatus {
  /**
   * Unknown status - the default initial state. This status is assigned when a stream is first
   * created and before it has been properly initialized or configured.
   */
  UNKNOWN,

  /**
   * Closed status - the stream is not active. This status indicates that the stream has been closed
   * and is not processing data. A stream can be reopened after being closed.
   */
  CLOSED,

  /**
   * Opened status - the stream is active and operational. This status indicates that the stream has
   * been successfully opened and is ready to perform read/write operations according to its
   * direction.
   */
  OPENED,

  /**
   * Error status - the stream has encountered an error. This status indicates that an error has
   * occurred during stream operations and the stream may not be functioning properly. Recovery or
   * intervention may be required.
   */
  ERROR
}
