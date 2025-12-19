package tic.stream;

import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tic.frame.TICMode;
import tic.frame.delimiter.TICStartPattern;

public class TICStreamModeDetector {
  private static final Logger logger = LogManager.getLogger(TICStreamModeDetector.class);
  private static final int START_LABEL_LENGTH = TICStartPattern.length();
  private TICMode selectedMode;
  private TICMode currentMode;
  private TICStreamReader streamReader;

  public TICStreamModeDetector(TICMode ticMode, TICStreamReader streamReader) {
    this.selectedMode = ticMode;
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
        return TICMode.HISTORIC;
      }

      if (this.checkStandardMode(ticFrame)) {
        logger.debug("TIC Mode STANDARD detected");
        return TICMode.STANDARD;
      }

      logger.debug("TIC Mode not detected");
      return null;
    } catch (Exception e) {
      logger.error("Error during TIC mode detection: " + e.getMessage(), e);
    }
    return null;
  }

  private boolean checkMode(TICMode ticMode, byte[] ticFrame) {
    if (ticFrame == null || ticFrame.length <= START_LABEL_LENGTH) {
      return false;
    }

    byte[] expectedStart = this.getExpectedStartBytes(ticMode);
    byte[] ticFrameStart = Arrays.copyOf(ticFrame, START_LABEL_LENGTH);

    return Arrays.equals(ticFrameStart, expectedStart);
  }

  private byte[] getExpectedStartBytes(TICMode ticMode) {
    if (ticMode == null) {
      return null;
    }
    if (ticMode == TICMode.HISTORIC) {
      return TICStartPattern.HISTORIC.getValue();
    } else if (ticMode == TICMode.STANDARD) {
      return TICStartPattern.STANDARD.getValue();
    }
    return null;
  }

  private boolean checkHistoricMode(byte[] ticFrame) {
    return this.checkMode(TICMode.HISTORIC, ticFrame);
  }

  private boolean checkStandardMode(byte[] ticFrame) {
    return this.checkMode(TICMode.STANDARD, ticFrame);
  }
}
