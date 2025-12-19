// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.stream;

import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tic.frame.TICMode;
import tic.frame.delimiter.TICStartPattern;

public class TICStreamModeDetector {
  private static final Logger logger = LogManager.getLogger(TICStreamModeDetector.class);
  private TICMode selectedMode;
  private TICMode currentMode;
  private TICStreamReader streamReader;

  public TICStreamModeDetector(TICMode ticMode, TICStreamReader streamReader) {
    this.selectedMode = ticMode;
    this.streamReader = streamReader;
  }

  /**
   * Updates the stream reader reference so auto-detection keeps using the active reader after a
   * reconnect/reset.
   */
  public void setStreamReader(TICStreamReader streamReader) {
    this.streamReader = streamReader;
  }

  /**
   * Gets the current TIC mode being used by the channel. Returns the detected mode if available,
   * otherwise returns the selected mode from configuration.
   *
   * @return the current TIC mode (HISTORIC, STANDARD, or AUTO)
   */
  public TICMode getMode() {
    return (this.getCurrentMode() != null) ? this.getCurrentMode() : this.getSelectedMode();
  }

  /**
   * Gets the TIC mode selected in the channel configuration.
   *
   * @return the selected TIC mode from configuration
   */
  public TICMode getSelectedMode() {
    return this.selectedMode;
  }

  /**
   * Gets the currently detected TIC mode during operation. This may differ from the selected mode
   * if auto-detection is enabled.
   *
   * @return the currently detected TIC mode, or null if not yet detected
   */
  public TICMode getCurrentMode() {
    return this.currentMode;
  }

  public TICMode autoDetectMode() {
    if (this.selectedMode == TICMode.STANDARD || this.selectedMode == TICMode.HISTORIC) {
      this.currentMode = this.selectedMode;
      return this.currentMode;
    }
    logger.debug("Auto detecting TIC Mode");
    try {
      byte[] ticFrame = this.streamReader.read();

      if (this.checkHistoricMode(ticFrame)) {
        logger.debug("TIC Mode HISTORIC detected");
        this.currentMode = TICMode.HISTORIC;
        return this.currentMode;
      }

      if (this.checkStandardMode(ticFrame)) {
        logger.debug("TIC Mode STANDARD detected");
        this.currentMode = TICMode.STANDARD;
        return this.currentMode;
      }

      logger.debug("TIC Mode not detected");
      return null;
    } catch (Exception e) {
      logger.error("Error during TIC mode detection: " + e.getMessage(), e);
    }
    return null;
  }

  private boolean checkMode(TICMode ticMode, byte[] ticFrame) {
    if (ticFrame == null || ticFrame.length <= TICStartPattern.length()) {
      return false;
    }

    byte[] expectedStart = this.getExpectedStartBytes(ticMode);
    byte[] ticFrameStart = Arrays.copyOf(ticFrame, TICStartPattern.length());

    return Arrays.equals(ticFrameStart, expectedStart);
  }

  private byte[] getExpectedStartBytes(TICMode ticMode) {
    return TICStartPattern.getValueFromMode(ticMode);
  }

  private boolean checkHistoricMode(byte[] ticFrame) {
    return this.checkMode(TICMode.HISTORIC, ticFrame);
  }

  private boolean checkStandardMode(byte[] ticFrame) {
    return this.checkMode(TICMode.STANDARD, ticFrame);
  }
}
