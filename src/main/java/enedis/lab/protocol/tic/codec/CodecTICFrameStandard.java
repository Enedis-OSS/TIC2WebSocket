// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.protocol.tic.codec;

import enedis.lab.codec.Codec;
import enedis.lab.codec.CodecException;
import enedis.lab.protocol.tic.frame.TICFrame;
import enedis.lab.protocol.tic.frame.TICFrameDataSet;
import enedis.lab.protocol.tic.frame.standard.TICFrameStandard;
import enedis.lab.protocol.tic.frame.standard.TICFrameStandardDataSet;
import enedis.lab.types.BytesArray;
import java.util.List;

/**
 * Codec for encoding and decoding TIC standard frame data sets.
 *
 * <p>
 * This class implements the {@link Codec} interface to provide serialization
 * and deserialization
 * of {@link TICFrameStandard} objects to and from their byte array
 * representation, according
 * to the standard TIC protocol format. It ensures the correct structure,
 * delimiters, and checksum
 * validation for each data set in a frame.
 *
 * <p>
 * Main features:
 * <ul>
 * <li>Encodes a {@link TICFrameStandard} into a byte array with proper
 * delimiters, separators, and checksum.</li>
 * <li>Decodes a byte array into a {@link TICFrameStandard}, validating
 * structure and checksum of each data set.</li>
 * <li>Handles TIC standard data set format, including support for multiple data
 * sets per frame.</li>
 * <li>Throws {@link CodecException} on invalid format or checksum.</li>
 * </ul>
 *
 * @author Enedis Smarties team
 * @see TICFrameStandard
 * @see TICFrameStandardDataSet
 * @see CodecTICFrameStandardDataSet
 * @see Codec
 * @see CodecException
 */
public class CodecTICFrameStandard implements Codec<TICFrameStandard, byte[]> {
  /**
   * Decode a byte array into a TICFrameStandard.
   *
   * <p>
   * Validates delimiters, structure, and checksum of each data set.
   *
   * @param bytes the byte array to decode
   * @return the decoded TICFrameStandard
   * @throws CodecException if the format or checksum is invalid
   */
  @Override
  public TICFrameStandard decode(byte[] bytes) throws CodecException {
    String errorMessage = "";
    BytesArray rawDataSet = null;
    TICFrameStandard ticFrame = null;
    CodecTICFrameStandardDataSet codecTICFrameStandardDataSet = new CodecTICFrameStandardDataSet();

    BytesArray bytesArray = new BytesArray(bytes);

    // Extract the body of the frame
    // NB: the presence of an EOT character makes the content of a frame invalid
    if ((bytesArray.startsWith(TICFrame.BEGINNING_PATTERN) == true)
        && (bytesArray.endsWith(TICFrame.END_PATTERN) == true)
        && (bytesArray.contains(TICFrame.EOT) == false)) {
      bytesArray.remove(0);
      bytesArray.remove(bytesArray.size() - 1);

      ticFrame = new TICFrameStandard();

      // Isolate each memory area supposed to correspond to an Information Group
      // (DataSet)
      List<BytesArray> datasetList = bytesArray.slice(
          TICFrameDataSet.BEGINNING_PATTERN,
          TICFrameDataSet.END_PATTERN,
          BytesArray.CONTIGUOUS);

      // Analyze each memory area to extract the controls of the Group of information
      // If the format of a zone is invalid, then the browse is interrupted and the
      // whole frame is
      // invalid
      // (null)
      if (datasetList.isEmpty() == false) {
        for (int i = 0; i < datasetList.size(); i++) {
          TICFrameStandardDataSet dataSet = null;
          rawDataSet = datasetList.get(i);
          byte[] rawDataSetByte = rawDataSet.getBytes();
          try {
            dataSet = codecTICFrameStandardDataSet.decode(rawDataSetByte);
            ticFrame.addDataSet(dataSet);
          } catch (CodecException exception) {
            errorMessage += exception.getMessage() + " : " + new String(rawDataSet.getBytes()) + "\n";
          }
        }
      }
    }

    if (errorMessage.isEmpty()) {
      return ticFrame;
    } else {
      throw new CodecException(errorMessage, ticFrame);
    }
  }

  /**
   * Encode a TICFrameStandard into its byte array representation.
   *
   * <p>
   * Format: LF [DataSet1] ... [DataSetN] CR
   *
   * @param ticFrameStandard the frame to encode
   * @return the encoded byte array, or null if the frame is empty
   * @throws CodecException if a data set is invalid or checksum is incorrect
   */
  @Override
  public byte[] encode(TICFrameStandard ticFrameStandard) throws CodecException {
    CodecTICFrameStandardDataSet codec = new CodecTICFrameStandardDataSet();
    BytesArray dataSet = new BytesArray();

    List<TICFrameDataSet> ticFrameStandardList = ticFrameStandard.getDataSetList();

    if (ticFrameStandard != null && !ticFrameStandardList.isEmpty()) {
      List<TICFrameDataSet> groups = ticFrameStandard.getDataSetList();
      dataSet.add(TICFrame.BEGINNING_PATTERN);
      for (int i = 0; i < groups.size(); i++) {
        byte[] buff = codec.encode((TICFrameStandardDataSet) groups.get(i));
        dataSet.addAll(buff);
      }
      dataSet.add(TICFrame.END_PATTERN);
      return dataSet.getBytes();
    } else {
      return null;
    }
  }
}
