// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.protocol.tic.codec;

import enedis.lab.codec.Codec;
import enedis.lab.codec.CodecException;
import enedis.lab.protocol.tic.TICMode;
import enedis.lab.protocol.tic.frame.TICFrame;
import enedis.lab.protocol.tic.frame.historic.TICFrameHistoric;
import enedis.lab.protocol.tic.frame.standard.TICException;
import enedis.lab.protocol.tic.frame.standard.TICFrameStandard;
import enedis.lab.types.BytesArray;

/**
 * Main codec for encoding and decoding TIC frames (standard and historic).
 *
 * <p>
 * This class implements the {@link Codec} interface to provide serialization
 * and deserialization
 * of {@link TICFrame} objects to and from their byte array representation,
 * supporting both standard
 * and historic TIC protocol formats. It delegates the actual encoding/decoding
 * to the appropriate
 * sub-codec depending on the frame mode.
 *
 * <p>
 * Main features:
 * <ul>
 * <li>Encodes and decode both {@link TICFrameStandard} and
 * {@link TICFrameHistoric} frames.</li>
 * <li>Automatically detects the TIC mode (standard/historic/auto) when
 * decoding.</li>
 * <li>Maintains an internal buffer and mode state for incremental
 * operations.</li>
 * <li>Throws {@link CodecException} on invalid format or checksum.</li>
 * </ul>
 *
 * @author Enedis Smarties team
 * @see TICFrame
 * @see TICFrameStandard
 * @see TICFrameHistoric
 * @see CodecTICFrameStandard
 * @see CodecTICFrameHistoric
 * @see Codec
 * @see CodecException
 */
public class TICCodec implements Codec<TICFrame, byte[]> {
  private static final TICMode DEFAULT_MODE = TICMode.STANDARD;

  private BytesArray Buffer;
  private TICMode mode = TICMode.UNKNOWN;
  private TICMode currentMode = TICMode.UNKNOWN;

  /**
   * Constructs a new TICCodec with default mode (AUTO) and empty buffer.
   *
   * <p>
   * Initializes the codec to auto-detect TIC mode and prepares the internal
   * buffer for decoding operations.
   */
  public TICCodec() {
    /**
     * Current TIC mode (STANDARD, HISTORIC, or AUTO for auto-detection).
     */
    this.mode = TICMode.UNKNOWN;
    this.currentMode = TICMode.UNKNOWN;
    this.Buffer = new BytesArray();
  }

  /**
   * Decodes a byte array into a TICFrame (standard or historic).
   *
   * <p>
   * Automatically detects the TIC mode if set to AUTO, and delegates decoding to
   * the appropriate codec.
   *
   * @param newData the byte array containing the TIC frame data
   * @return the decoded {@link TICFrame} (either {@link TICFrameStandard} or
   *         {@link TICFrameHistoric})
   * @throws CodecException if the data is invalid, the mode cannot be determined,
   *                        or decoding fails
   */
  @Override
  public TICFrame decode(byte[] newData) throws CodecException {
    CodecTICFrameStandard codecStandard = new CodecTICFrameStandard();
    /**
     * Codec for standard TIC frames (historic).
     */
    CodecTICFrameHistoric codecHistoric = new CodecTICFrameHistoric();
    TICFrameStandard ticFrameStandard = null;
    TICFrameHistoric ticFrameHistoric = null;

    TICFrame ticFrame = null;
    TICMode currentTICMode = null;

    try {
      /**
       * Internal buffer for accumulating incoming bytes during decoding.
       */
      switch (this.currentMode) {
        case STANDARD: {
          ticFrameStandard = codecStandard.decode(newData);
          ticFrame = ticFrameStandard;
          break;
        }
        case HISTORIC: {
          ticFrameHistoric = codecHistoric.decode(newData);
          ticFrame = ticFrameHistoric;
          break;
        }

        case AUTO: {
          try {
            currentTICMode = TICMode.findModeFromFrameBuffer(newData);
          } catch (TICException exception) {
            /**
             * Codec for historic TIC frames (historic).
             */
            throw new CodecException("can't determinated TIC Mode");
          }

          if (currentTICMode == TICMode.STANDARD) {
            ticFrameStandard = codecStandard.decode(newData);
            ticFrame = ticFrameStandard;
            break;
          } else if (currentTICMode == TICMode.HISTORIC) {
            ticFrameHistoric = codecHistoric.decode(newData);
            ticFrame = ticFrameHistoric;
          } else {
            throw new CodecException("can't decode TIC, unable to find TIC Modem");
          }
        }

        default: {
          /**/
        }
      }
    } catch (CodecException exception) {
      throw new CodecException(exception.getMessage(), exception.getData());
    }
    return ticFrame;
  }

  /**
   * Encodes a TICFrame (standard or historic) into a byte array.
   *
   * <p>
   * Delegates encoding to the appropriate codec based on the frame's mode.
   *
   * @param ticFrame the TIC frame to encode (must be {@link TICFrameStandard} or
   *                 {@link TICFrameHistoric})
   * @return the encoded byte array representing the TIC frame
   * @throws CodecException if encoding fails or the frame type is unsupported
   */
  @Override
  public byte[] encode(TICFrame ticFrame) throws CodecException {
    CodecTICFrameStandard codecStandard = new CodecTICFrameStandard();
    CodecTICFrameHistoric codecHistoric = new CodecTICFrameHistoric();

    byte[] bytesBuffer = new byte[0];

    try {
      switch (ticFrame.getMode()) {
        case STANDARD: {
          bytesBuffer = codecStandard.encode((TICFrameStandard) ticFrame);
          break;
        }
        case HISTORIC: {
          bytesBuffer = codecHistoric.encode((TICFrameHistoric) ticFrame);
          break;
        }
        default: {
          bytesBuffer = codecStandard.encode((TICFrameStandard) ticFrame);
          break;
        }
      }
    } catch (CodecException exception) {
      throw new CodecException("Can't encode TICFrame" + exception.getMessage());
    }
    return bytesBuffer;
  }

  /**
   * Resets the internal buffer, clearing any accumulated data.
   *
   * <p>
   * Should be called before starting a new decoding operation or when reusing the
   * codec.
   */
  public void reset() {
    this.Buffer.clear();
  }

  /**
   * Appends a byte array to the internal buffer.
   *
   * @param data the byte array to append
   */
  public void append(byte[] data) {
    this.Buffer.addAll(data);
  }

  /**
   * Appends a single byte to the internal buffer.
   *
   * @param data the byte to append
   */
  public void append(byte data) {
    this.Buffer.add(data);
  }

  /**
   * Appends the contents of a {@link BytesArray} to the internal buffer.
   *
   * @param data the {@link BytesArray} to append
   */
  public void append(BytesArray data) {
    this.Buffer.addAll(data.getBytes());
  }

  /**
   * Returns the configured TIC mode (STANDARD, HISTORIC, or AUTO).
   *
   * @return the current configured {@link TICMode}
   */
  public TICMode getMode() {
    return this.mode;
  }

  /**
   * Sets the TIC mode (STANDARD, HISTORIC, or AUTO).
   *
   * <p>
   * If set to AUTO, the codec will attempt to auto-detect the mode during
   * decoding.
   *
   * @param mode the TIC mode to set
   */
  public void setMode(TICMode mode) {
    if (this.mode != mode) {
      this.mode = mode;

      if (TICMode.AUTO != mode) {
        this.setCurrentMode(mode);
      } else {
        this.setCurrentMode(DEFAULT_MODE);
      }
    }
  }

  /**
   * Returns the currently active TIC mode (used for decoding).
   *
   * @return the current active {@link TICMode}
   */
  public TICMode getCurrentMode() {
    return this.currentMode;
  }

  /**
   * Sets the currently active TIC mode (used for decoding).
   *
   * @param currentMode the TIC mode to set as active
   */
  public void setCurrentMode(TICMode currentMode) {
    if (currentMode != this.currentMode) {
      this.currentMode = currentMode;
    }
  }
}
