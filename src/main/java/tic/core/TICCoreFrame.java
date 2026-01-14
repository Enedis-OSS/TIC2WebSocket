// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.core;

import java.time.LocalDateTime;
import java.util.Objects;
import tic.frame.TICFrame;
import tic.frame.TICMode;

/**
 * Class representing a core frame with identifier, mode, capture time, and content.
 *
 * <p>This class provides mechanisms for constructing, accessing, and managing frame information
 * including identifiers, modes, timestamps, and associated content. It is designed for
 * general-purpose frame handling.
 *
 * <p>Common use cases include:
 *
 * <ul>
 *   <li>Representing frames with structured data
 *   <li>Managing frame identifiers and modes
 *   <li>Associating content and timestamps with frames
 * </ul>
 *
 * @author Enedis Smarties team
 */
public class TICCoreFrame {

  private final TICIdentifier identifier;
  private final TICMode mode;
  private final LocalDateTime captureDateTime;
  private final TICFrame frame;

  public TICCoreFrame(
      TICIdentifier identifier, TICMode mode, LocalDateTime captureDateTime, TICFrame frame) {
    this.identifier = Objects.requireNonNull(identifier, "identifier must not be null");
    this.mode = Objects.requireNonNull(mode, "mode must not be null");
    this.captureDateTime =
        Objects.requireNonNull(captureDateTime, "captureDateTime must not be null");
    this.frame = Objects.requireNonNull(frame, "frame must not be null");
  }

  /**
   * Get identifier
   *
   * @return the identifier
   */
  public TICIdentifier getIdentifier() {
    return this.identifier;
  }

  /**
   * Get mode
   *
   * @return the mode
   */
  public TICMode getMode() {
    return this.mode;
  }

  /**
   * Get capture date time
   *
   * @return the capture date time
   */
  public LocalDateTime getCaptureDateTime() {
    return this.captureDateTime;
  }

  /**
   * Get content
   *
   * @return the content
   */
  public TICFrame getFrame() {
    return this.frame;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("TICCoreFrame {\n");
    sb.append("  identifier: ").append(this.identifier).append(",\n");
    sb.append("  mode: ").append(this.mode).append(",\n");
    sb.append("  captureDateTime: ").append(this.captureDateTime).append(",\n");
    sb.append("  frame: ").append(this.frame).append("\n");
    sb.append("}");
    return sb.toString();
  }
}
