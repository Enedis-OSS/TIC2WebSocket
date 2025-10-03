// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.protocol.tic.datastreams;

import enedis.lab.codec.CodecException;
import enedis.lab.io.datastreams.DataInputStream;
import enedis.lab.io.datastreams.DataStreamException;
import enedis.lab.io.datastreams.DataStreamType;
import enedis.lab.protocol.tic.TICMode;
import enedis.lab.protocol.tic.channels.ChannelTICSerialPort;
import enedis.lab.protocol.tic.codec.TICCodec;
import enedis.lab.protocol.tic.frame.TICError;
import enedis.lab.protocol.tic.frame.TICFrame;
import enedis.lab.types.DataDictionary;
import enedis.lab.types.DataDictionaryException;
import enedis.lab.types.datadictionary.DataDictionaryBase;

/**
 * Data input stream for TIC (Teleinformation Client) frames.
 *
 * <p>
 * This class extends {@link DataInputStream} to provide decoding and event
 * handling for TIC frames
 * received from a configured channel. It uses a {@link TICCodec} to decode raw
 * byte arrays into
 * {@link TICFrame} objects and exposes them as {@link DataDictionary}
 * instances. The stream supports
 * error handling, event notification, and mode management for both standard and
 * historic TIC protocols.
 *
 * <p>
 * Key features:
 * <ul>
 * <li>Decodes incoming TIC frames using {@link TICCodec}</li>
 * <li>Notifies subscribers on new data or errors</li>
 * <li>Supports both standard and historic TIC modes</li>
 * <li>Provides access to the current TIC mode</li>
 * </ul>
 *
 * @author Enedis Smarties team
 * @see TICCodec
 * @see TICFrame
 * @see DataInputStream
 * @see DataDictionary
 */
public class TICInputStream extends DataInputStream {
  /**
   * Key for the timestamp field in the decoded data dictionary.
   */
  public static final String KEY_TIMESTAMP = "timestamp";

  /**
   * Key for the channel name field in the decoded data dictionary.
   */
  public static final String KEY_CHANNEL = "channel";

  /**
   * Key for the TIC frame data field in the decoded data dictionary.
   */
  public static final String KEY_DATA = "data";

  /**
   * Codec used to decode TIC frames from byte arrays.
   */
  protected TICCodec codec;

  /**
   * Constructs a new TICInputStream with the specified configuration.
   *
   * <p>
   * Initializes the codec and sets the TIC mode according to the configuration.
   *
   * @param configuration the TIC stream configuration
   * @throws DataStreamException if the configuration is invalid
   */
  public TICInputStream(TICStreamConfiguration configuration) throws DataStreamException {
    super(configuration);
    this.codec = new TICCodec();
    this.codec.setCurrentMode(configuration.getTicMode());
  }

  /**
   * Reads a TIC frame from the input stream and returns it as a
   * {@link DataDictionary}.
   *
   * <p>
   * This method should be implemented to provide actual reading logic. Currently
   * returns null.
   *
   * @return the decoded TIC frame as a {@link DataDictionary}, or null if not
   *         implemented
   * @throws DataStreamException if a read error occurs
   */
  @Override
  public DataDictionary read() throws DataStreamException {
    return null;
  }

  /**
   * Returns the type of this data stream (TIC).
   *
   * @return {@link DataStreamType#TIC}
   */
  @Override
  public DataStreamType getType() {
    return DataStreamType.TIC;
  }

  /**
   * Handles incoming data read from the channel.
   *
   * <p>
   * Decodes the TIC frame and notifies subscribers. If decoding fails, notifies
   * error subscribers.
   *
   * @param channelName the name of the channel
   * @param data        the raw byte array received
   */
  @Override
  public void onDataRead(String channelName, byte[] data) {
    if (!this.notifier.getSubscribers().isEmpty()) {
      DataDictionary ticFrame = null;
      try {
        ticFrame = this.decodeTICFrame(data);
        if (ticFrame != null) {
          this.notifyOnDataReceived(ticFrame);
        }
      } catch (DataDictionaryException exception) {
        this.logger.error(exception.getMessage(), exception);
      } catch (CodecException exception) {
        DataDictionaryBase errorDataDictionary = new DataDictionaryBase();

        String KEY_PARTIAL_FRAME = "partialTICFrame";

        errorDataDictionary.addKey(KEY_PARTIAL_FRAME);

        try {
          Object exceptionData = exception.getData();
          if (exceptionData != null && exceptionData instanceof TICFrame) {
            try {
              DataDictionary frameDataDictionary = ((TICFrame) exceptionData).getDataDictionary();
              if (frameDataDictionary != null) {
                errorDataDictionary.set(KEY_PARTIAL_FRAME, frameDataDictionary);
              } else {
                this.logger.warn("Frame data dictionary is null");
                errorDataDictionary.set(KEY_PARTIAL_FRAME, "");
              }
            } catch (DataDictionaryException frameDataDictionaryException) {
              this.logger.warn(
                  "Can't get TICFrame data dictionary: "
                      + frameDataDictionaryException.getMessage());
              errorDataDictionary.set(KEY_PARTIAL_FRAME, "");
            }
          } else {
            this.logger.error("No frame data available");
            errorDataDictionary.set(KEY_PARTIAL_FRAME, "");
          }
        } catch (DataDictionaryException dataDictionaryException) {
          this.logger.error(
              "Can't convert TICFrame to DataDictonary" + dataDictionaryException.getMessage());
        }

        this.logger.error(exception.getMessage() + errorDataDictionary.toString());
        this.onErrorDetected(
            channelName,
            TICError.TIC_READER_READ_FRAME_CHECKSUM_INVALID.getValue(),
            exception.getMessage(),
            errorDataDictionary);
      }
    }
  }

  /**
   * Not used. TICInputStream does not handle data written events.
   *
   * @param channelName the name of the channel
   * @param data        the data written
   */
  @Override
  public void onDataWritten(String channelName, byte[] data) {
    // Not used
  }

  /**
   * Handles error events detected on the channel (without error data).
   *
   * @param channelName  the name of the channel
   * @param errorCode    the error code
   * @param errorMessage the error message
   */
  @Override
  public void onErrorDetected(String channelName, int errorCode, String errorMessage) {
    this.notifyOnErrorDetected(errorCode, errorMessage, null);
  }

  /**
   * Handles error events detected on the channel (with error data).
   *
   * @param channelName  the name of the channel
   * @param errorCode    the error code
   * @param errorMessage the error message
   * @param errorData    additional error data
   */
  @Override
  public void onErrorDetected(
      String channelName, int errorCode, String errorMessage, DataDictionary errorData) {
    this.notifyOnErrorDetected(errorCode, errorMessage, errorData);
  }

  /**
   * Returns the current TIC mode of the underlying channel.
   *
   * @return the current {@link TICMode}
   */
  public TICMode getMode() {
    ChannelTICSerialPort channelTIC = (ChannelTICSerialPort) this.channel;
    return channelTIC.getMode();
  }

  /**
   * Decodes a raw TIC frame byte array into a {@link DataDictionary}.
   *
   * <p>
   * Uses the internal {@link TICCodec} to decode the frame, adds timestamp and
   * channel info.
   *
   * @param data the raw TIC frame bytes
   * @return a {@link DataDictionary} containing the decoded frame, timestamp, and
   *         channel name
   * @throws DataDictionaryException if the data dictionary cannot be created
   * @throws CodecException          if decoding fails
   */
  protected DataDictionary decodeTICFrame(byte[] data)
      throws DataDictionaryException, CodecException {
    TICFrame ticFrame;
    try {
      ticFrame = this.codec.decode(data);
    } catch (CodecException exception) {
      throw new CodecException(exception.getMessage(), exception.getData());
    }

    long timestamp = System.currentTimeMillis();

    DataDictionaryBase decodedTICFrame = new DataDictionaryBase();

    decodedTICFrame.set(KEY_TIMESTAMP, timestamp);
    decodedTICFrame.set(KEY_CHANNEL, this.getConfiguration().getChannelName());
    decodedTICFrame.set(KEY_DATA, ticFrame);

    return decodedTICFrame;
  }
}
