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
import enedis.lab.protocol.tic.frame.historic.TICFrameHistoricDataSet;
import enedis.lab.types.BytesArray;
import java.util.List;

/**
 * Codec for encoding and decoding TIC historic frame data sets.
 *
 * <p>
 * This class implements the {@link Codec} interface to provide serialization
 * and deserialization
 * of {@link TICFrameHistoricDataSet} objects to and from their byte array
 * representation, according
 * to the historic TIC protocol format. It ensures the correct structure,
 * delimiters, and checksum
 * validation for each data set.
 *
 * <p>
 * Main features:
 * <ul>
 * <li>Encodes a {@link TICFrameHistoricDataSet} into a byte array with proper
 * delimiters, separators, and checksum.</li>
 * <li>Decodes a byte array into a {@link TICFrameHistoricDataSet}, validating
 * structure and checksum.</li>
 * <li>Handles TIC historic data set format:
 * <code>LF LABEL SP DATA SP CHECKSUM CR</code>.</li>
 * <li>Throws {@link CodecException} on invalid format or checksum.</li>
 * </ul>
 *
 * @author Enedis Smarties team
 * @see TICFrameHistoricDataSet
 * @see CodecTICFrameHistoric
 * @see Codec
 * @see CodecException
 */
public class CodecTICFrameHistoricDataSet implements Codec<TICFrameHistoricDataSet, byte[]> {

  /**
   * Encode a TICFrameHistoricDataSet into its byte array representation.
   *
   * <p>
   * Format: LF LABEL SP DATA SP CHECKSUM CR
   *
   * @param ticFrameHistoricDataSet the data set to encode
   * @return the encoded byte array
   * @throws CodecException if the data set is invalid or checksum is incorrect
   */
  @Override
  public byte[] encode(TICFrameHistoricDataSet ticFrameHistoricDataSet) throws CodecException {
    BytesArray dataSet = new BytesArray();

    if ((ticFrameHistoricDataSet.getLabel() != null)
        && (ticFrameHistoricDataSet.getData() != null)) {
      dataSet.add(TICFrameDataSet.BEGINNING_PATTERN);
      dataSet.addAll(ticFrameHistoricDataSet.getLabel().getBytes());

      dataSet.add(TICFrameHistoricDataSet.SEPARATOR);
      dataSet.addAll(ticFrameHistoricDataSet.getData().getBytes());

      dataSet.add(TICFrameHistoricDataSet.SEPARATOR);

      if (!ticFrameHistoricDataSet.isValid()) {
        throw new CodecException("Invalid Checksum value");
      }
      dataSet.add(ticFrameHistoricDataSet.getChecksum());
      dataSet.add(TICFrameDataSet.END_PATTERN);
    }

    return dataSet.getBytes();
  }

  /**
   * Decode a byte array into a TICFrameHistoricDataSet.
   *
   * <p>
   * Validates delimiters, structure, and checksum.
   *
   * @param bytes the byte array to decode
   * @return the decoded TICFrameHistoricDataSet
   * @throws CodecException if the format or checksum is invalid
   */
  @Override
  public TICFrameHistoricDataSet decode(byte[] bytes) throws CodecException {
    BytesArray bytesArray = new BytesArray(bytes);
    TICFrameHistoricDataSet dataSet = null;

    if (this.isStartStopDelimiterPresent(bytesArray)) {
      this.removeStartStopDelimiter(bytesArray);
    }

    List<BytesArray> parts = this.splitFrame(bytesArray);

    // Structure "LABEL/DATA/CHECKSUM" :
    if (this.isStructureLabelDataChecksum(parts)) {

      if (this.isChecksumInOneByte(parts.get(2))) {

        dataSet = new TICFrameHistoricDataSet();
        dataSet = this.createDataSetLabelDataChecksum(parts, dataSet);

      } else {
        throw new CodecException(
            "Invalid Checksum value : The part to the Checksum must occupy one and only one byte\r\n"
                + "The Label part must not exceed 8 bytes ");
      }
    } else {
      throw new CodecException("Invalid format of TICFrameHistoricDataSet");
    }

    if (dataSet.isValid() == false) {
      throw new CodecException("Invalid Checksum value - Historic");
    }

    return dataSet;
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
   * Removes the start and stop delimiters from the byte array (LF and CR).
   *
   * @param bytes the byte array to modify
   */
  private void removeStartStopDelimiter(BytesArray bytes) {

    bytes.remove(0);
    bytes.remove(bytes.size() - 1);
  }

  /**
   * Initializes a TICFrameHistoricDataSet from label, data, and checksum parts.
   *
   * @param parts   the list of BytesArray: label, data, checksum
   * @param dataSet the data set to initialize
   * @return the initialized TICFrameHistoricDataSet
   */
  private TICFrameHistoricDataSet createDataSetLabelDataChecksum(
      List<BytesArray> parts, TICFrameHistoricDataSet dataSet) {

    dataSet.setup(parts.get(0).getBytes(), parts.get(1).getBytes());
    dataSet.setChecksum(parts.get(2).get(0));
    return dataSet;
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
   * Checks if the split frame has the expected structure: label, data, checksum.
   *
   * @param parts the list of BytesArray
   * @return true if the structure matches, false otherwise
   */
  private boolean isStructureLabelDataChecksum(List<BytesArray> parts) {
    return parts.size() == 3;
  }

  /**
   * Splits the frame into its parts (label, data, checksum) using the TIC
   * separator.
   *
   * @param bytesArray the byte array to split
   * @return a list of BytesArray: label, data, checksum
   * @throws CodecException if the frame is too short or has an invalid format
   */
  private List<BytesArray> splitFrame(BytesArray bytesArray) throws CodecException {
    List<BytesArray> parts;

    if (bytesArray.size() < 5) {
      throw new CodecException("Not enough bytes in TICFrameHistoricDataSet");
    }
    if (bytesArray.get(bytesArray.size() - 1) == TICFrameHistoricDataSet.SEPARATOR) {
      BytesArray subList = bytesArray.subList(0, bytesArray.size() - 2);
      parts = subList.split(TICFrameHistoricDataSet.SEPARATOR);
      if (parts.size() < 1) {
        throw new CodecException("Invalid format of TICFrameHistoricDataSet");
      }
      BytesArray checksum = parts.get(parts.size() - 1);
      checksum.addAll(new byte[] { TICFrameHistoricDataSet.SEPARATOR });
    } else {
      parts = bytesArray.split(TICFrameHistoricDataSet.SEPARATOR);
    }

    return parts;
  }
}
