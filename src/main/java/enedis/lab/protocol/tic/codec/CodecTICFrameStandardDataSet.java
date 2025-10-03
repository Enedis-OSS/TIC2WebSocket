// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.protocol.tic.codec;

import enedis.lab.codec.Codec;
import enedis.lab.codec.CodecException;
import enedis.lab.protocol.tic.frame.TICFrameDataSet;
import enedis.lab.protocol.tic.frame.standard.TICFrameStandardDataSet;
import enedis.lab.types.BytesArray;
import java.util.List;

/**
 * Codec for encoding and decoding TIC standard frame data sets.
 *
 * <p>
 * This class implements the {@link Codec} interface to provide serialization
 * and deserialization
 * of {@link TICFrameStandardDataSet} objects to and from their byte array
 * representation, according
 * to the standard TIC protocol format.
 *
 * <p>
 * Main features:
 * <ul>
 * <li>Encodes a {@link TICFrameStandardDataSet} into a byte array with proper
 * delimiters, separators, and checksum.</li>
 * <li>Decodes a byte array into a {@link TICFrameStandardDataSet}, validating
 * structure and checksum.</li>
 * <li>Handles both "LABEL/DATA/CHECKSUM" and "LABEL/DATETIME/DATA/CHECKSUM"
 * formats.</li>
 * <li>Throws {@link CodecException} on invalid format or checksum.</li>
 * </ul>
 *
 * @author Enedis Smarties team
 * @see TICFrameStandardDataSet
 * @see CodecTICFrameStandard
 * @see Codec
 * @see CodecException
 */
public class CodecTICFrameStandardDataSet implements Codec<TICFrameStandardDataSet, byte[]> {
  /**
   * Encode a TICFrameStandardDataSet into its byte array representation.
   *
   * @param ticFrameStandardDataSet the data set to encode
   * @return the encoded byte array
   * @throws CodecException if the data set is invalid or checksum is incorrect
   */
  @Override
  public byte[] encode(TICFrameStandardDataSet ticFrameStandardDataSet) throws CodecException {
    BytesArray dataSet = new BytesArray();

    if ((null != ticFrameStandardDataSet.getLabel())
        && (null != ticFrameStandardDataSet.getData())) {
      dataSet.add(TICFrameDataSet.BEGINNING_PATTERN);
      dataSet.addAll(ticFrameStandardDataSet.getLabel().getBytes());

      if (ticFrameStandardDataSet.checkDateTime() == true) {
        dataSet.add(TICFrameStandardDataSet.SEPARATOR);
        dataSet.addAll(ticFrameStandardDataSet.getDateTime().getBytes());
      }

      dataSet.add(TICFrameStandardDataSet.SEPARATOR);
      dataSet.addAll(ticFrameStandardDataSet.getData().getBytes());

      dataSet.add(TICFrameStandardDataSet.SEPARATOR);
      if (!ticFrameStandardDataSet.isValid()) {
        throw new CodecException("Invalid Checksum value");
      }

      dataSet.add(ticFrameStandardDataSet.getChecksum());
      dataSet.add(TICFrameDataSet.END_PATTERN);
    }

    return dataSet.getBytes();
  }

  /**
   * Decode a byte array into a TICFrameStandardDataSet.
   *
   * <p>
   * Supports both classic and datetime-extended formats. Validates delimiters,
   * structure, and checksum.
   *
   * @param bytes the byte array to decode
   * @return the decoded TICFrameStandardDataSet
   * @throws CodecException if the format or checksum is invalid
   */
  @Override
  public TICFrameStandardDataSet decode(byte[] bytes) throws CodecException {
    BytesArray bytesArray = new BytesArray(bytes);
    TICFrameStandardDataSet dataSet = null;

    if (this.isStartStopDelimiterPresent(bytesArray)) {
      this.removeStartStopDelimiter(bytesArray);
    }

    List<BytesArray> parts = bytesArray.split(TICFrameStandardDataSet.SEPARATOR);

    // Structure "LABEL/DATA/CHECKSUM" :
    if (this.isStructureLabelDataChecksum(parts)) {
      if (this.isChecksumInOneByte(parts.get(2))) {
        dataSet = new TICFrameStandardDataSet();
        dataSet = this.createDataSetLabelDataChecksum(parts, dataSet);
      } else {
        throw new CodecException(
            "Invalid Checksum value : The part to the Checksum must occupy one and only one byte\r\n"
                + "The Label part must not exceed 8 bytes ");
      }
    }
    // Structure "LABEL/DATETIME/DATA/CHECKSUM" :
    else if (this.isStructureLabelDateTimeDataChecksum(parts)) {
      if (this.isChecksumInOneByte(parts.get(3))) {
        dataSet = new TICFrameStandardDataSet();
        dataSet = this.createDataSetLabelDatetimeDataChecksum(parts, dataSet);
      } else {
        throw new CodecException(
            "Invalid Checksum value : The part to the Checksum must occupy one and only one byte\r\n"
                + "The Label part must not exceed 8 bytes ");
      }
    } else {
      throw new CodecException("Invalid bytes for decode - Standard");
    }

    if (dataSet.isValid() == false) {
      throw new CodecException("Invalid Checksum value");
    }

    return dataSet;
  }

  /**
   * Checks if the split frame has the expected structure: label, datetime, data,
   * checksum.
   *
   * @param parts the list of BytesArray
   * @return true if the structure matches, false otherwise
   */
  private boolean isStructureLabelDateTimeDataChecksum(List<BytesArray> parts) {
    return parts.size() == 4;
  }

  /**
   * Checks if the split frame has the expected structure: label, data, checksum.
   *
   * @param parts the list of BytesArray
   * @return true if the structure matches, false otherwise
   */
  private boolean isStructureLabelDataChecksum(List<BytesArray> parts) {
    return parts.size() == 3;
  }

  /**
   * Checks if the checksum part is exactly one byte.
   *
   * @param part the BytesArray to check
   * @return true if the part size is 1, false otherwise
   */
  private boolean isChecksumInOneByte(BytesArray part) {
    return part.size() == 1;
  }

  /**
   * Removes the start and stop delimiters from the byte array (LF and CR).
   *
   * @param bytes the byte array to modify
   */
  private void removeStartStopDelimiter(BytesArray bytes) {
    bytes.remove(0);
    bytes.remove(bytes.size() - 1);
  }

  /**
   * Checks if the byte array starts and ends with the expected TIC delimiters.
   *
   * @param bytes the byte array to check
   * @return true if delimiters are present, false otherwise
   */
  private boolean isStartStopDelimiterPresent(BytesArray bytes) {
    return bytes.startsWith(TICFrameDataSet.BEGINNING_PATTERN)
        && bytes.endsWith(TICFrameDataSet.END_PATTERN);
  }

  /**
   * Initializes a TICFrameStandardDataSet from label, data, and checksum parts.
   *
   * @param parts   the list of BytesArray: label, data, checksum
   * @param dataSet the data set to initialize
   * @return the initialized TICFrameStandardDataSet
   */
  private TICFrameStandardDataSet createDataSetLabelDataChecksum(
      List<BytesArray> parts, TICFrameStandardDataSet dataSet) {
    dataSet.setup(parts.get(0).getBytes(), parts.get(1).getBytes());
    dataSet.setChecksum(parts.get(2).get(0));
    return dataSet;
  }

  /**
   * Initializes a TICFrameStandardDataSet from label, datetime, data, and
   * checksum parts.
   *
   * @param parts   the list of BytesArray: label, datetime, data, checksum
   * @param dataSet the data set to initialize
   * @return the initialized TICFrameStandardDataSet
   */
  private TICFrameStandardDataSet createDataSetLabelDatetimeDataChecksum(
      List<BytesArray> parts, TICFrameStandardDataSet dataSet) {
    dataSet.setup(parts.get(0).getBytes(), parts.get(2).getBytes(), parts.get(1).getBytes());
    dataSet.setChecksum(parts.get(3).get(0));
    return dataSet;
  }
}
