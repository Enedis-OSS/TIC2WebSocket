// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.stream.configuration;

import enedis.lab.protocol.tic.TICMode;
import tic.stream.identifier.TICStreamIdentifier;

/**
 * Configuration class for TIC stream, including TIC mode, identifier, and timeout.
 * 
 * @author Enedis Smarties team
 */
public class TICStreamConfiguration {
  protected final static TICMode DEFAULT_TIC_MODE = TICMode.AUTO;
  protected final static int DEFAULT_TIMEOUT = 10;

  private TICMode ticMode = DEFAULT_TIC_MODE;
  private TICStreamIdentifier identifier;
  private int timeout = DEFAULT_TIMEOUT;

  /**
   * Constructs a TICStreamConfiguration with the specified parameters.
   *
   * @param ticMode the TIC mode (STANDARD, HISTORIC, or AUTO)
   */
  public TICStreamConfiguration(TICMode ticMode, TICStreamIdentifier identifier, int timeout) {
    this.setTicMode(ticMode);
    this.setIdentifier(identifier);
    this.setTimeout(timeout);
  }

  /**
   * Returns the configured TIC mode for this stream.
   *
   * @return the TIC mode (STANDARD, HISTORIC, or AUTO)
   */
  public TICMode getTicMode() {
    return this.ticMode;
  }

  /**
   * Returns the TIC stream identifier.
   *
   * @return the TIC stream identifier
   */
  public TICStreamIdentifier getIdentifier() {
    return this.identifier;
  }

  /**
   * Returns the timeout value for the TIC stream.
   *
   * @return the timeout value in seconds
   */
  public int getTimeout() {
    return this.timeout;
  }

  private void setTicMode(TICMode ticMode) {
    checkTicMode(ticMode);
    this.ticMode = ticMode;
  }

  private void setIdentifier(TICStreamIdentifier identifier) {
    checkIdentifier(identifier);
    this.identifier = identifier;
  }

  private void setTimeout(int timeout) {
    checkTimeout(timeout);
    this.timeout = timeout;
  }

  private void checkTicMode(TICMode ticMode) {
    if (ticMode == null) {
      throw new IllegalArgumentException("TIC mode cannot be null");
    }
    if (ticMode != TICMode.STANDARD && ticMode != TICMode.HISTORIC && ticMode != TICMode.AUTO) {
      throw new IllegalArgumentException("Invalid TIC mode: " + ticMode);
    }
  }

  private void checkIdentifier(TICStreamIdentifier identifier) {
    if (identifier == null) {
      throw new IllegalArgumentException("TIC stream identifier cannot be null");
    }
  }

  private void checkTimeout(int timeout) {
    if (timeout <= 0) {
      throw new IllegalArgumentException("Timeout must be a positive integer");
    }
  }
}
