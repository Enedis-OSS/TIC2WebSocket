// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.protocol.tic.frame.historic;

import enedis.lab.protocol.tic.frame.TICFrame;
import enedis.lab.protocol.tic.frame.TICFrameDataSet;
import enedis.lab.types.BytesArray;
import org.json.JSONObject;

/**
 * Data set representation for historic TIC frames.
 *
 * <p>This class extends {@link TICFrameDataSet} to provide checksum calculation, byte
 * serialization, and JSON conversion for historic TIC data sets. It defines the separator and
 * implements the protocol-specific checksum logic.
 *
 * <p>Key features:
 *
 * <ul>
 *   <li>Defines the separator for historic TIC data sets
 *   <li>Implements checksum calculation for label and data
 *   <li>Serializes the data set to bytes and JSON
 * </ul>
 *
 * @author Enedis Smarties team
 * @see TICFrameDataSet
 * @see TICFrame
 */
public class TICFrameHistoricDataSet extends TICFrameDataSet {
  /** Separator character (space, 0x20) used in historic TIC data sets. */
  public static final byte SEPARATOR = 0x20; // SP

  /** Constructs an empty historic TIC data set. */
  public TICFrameHistoricDataSet() {
    super();
  }

  /**
   * Computes the consistent checksum for this data set according to the historic TIC protocol.
   *
   * <p>The checksum is calculated over the label, separator, and data fields.
   *
   * @return the computed checksum, or null if label or data is missing
   */
  @Override
  public Byte getConsistentChecksum() {
    Byte crc = 0;
    byte[] labelByte;
    byte[] dataByte;
    if ((this.label != null) && (this.data != null)) {
      labelByte = this.label.getBytes();
      for (int i = 0; i < labelByte.length; i++) {
        crc = this.computeUpdate(crc, labelByte[i]);
      }

      crc = this.computeUpdate(crc, SEPARATOR);
      dataByte = this.data.getBytes();

      for (int i = 0; i < dataByte.length; i++) {
        crc = this.computeUpdate(crc, dataByte[i]);
      }

      return this.computeEnd(crc);
    } else {
      return null;
    }
  }

  /**
   * Updates the checksum value with the given byte.
   *
   * @param crc the current checksum value
   * @param octet the byte to add
   * @return the updated checksum value
   */
  public Byte computeUpdate(Byte crc, byte octet) {
    return (byte) (crc + (octet & 0xff));
  }

  /**
   * Finalizes the checksum value according to the historic TIC protocol.
   *
   * @param crc the current checksum value
   * @return the finalized checksum value
   */
  public Byte computeEnd(Byte crc) {
    return (byte) ((crc & 0x3F) + 0x20);
  }

  /**
   * Serializes this data set to a byte array according to the historic TIC protocol.
   *
   * @return the byte array representation of the data set
   */
  @Override
  public byte[] getBytes() {
    BytesArray dataSet = new BytesArray();

    if ((this.label != null) && (this.data != null)) {
      dataSet.add(BEGINNING_PATTERN);
      dataSet.addAll(this.label.getBytes());

      dataSet.add(SEPARATOR);
      dataSet.addAll(this.data.getBytes());

      dataSet.add(SEPARATOR);
      dataSet.add(this.checksum);

      dataSet.add(END_PATTERN);
    }

    return dataSet.getBytes();
  }

  /**
   * Converts this data set to a JSON object using the default options.
   *
   * @return the JSON representation of the data set
   */
  @Override
  public JSONObject toJSON() {
    return this.toJSON(TICFrame.TRIMMED);
  }

  /**
   * Converts this data set to a JSON object with the specified options.
   *
   * @param option bitmask of options (e.g., {@link TICFrame#NOCHECKSUM})
   * @return the JSON representation of the data set
   */
  @Override
  public JSONObject toJSON(int option) {
    JSONObject jsonObject = new JSONObject();

    if ((option & TICFrame.NOCHECKSUM) > 0) {
      jsonObject.put(new String(this.label.getBytes()), new String(this.data.getBytes()));
    } else {
      JSONObject content = new JSONObject();
      content.put(TICFrame.KEY_DATA, new String(this.data.getBytes()));
      content.put(TICFrame.KEY_CHECKSUM, this.checksum);
      jsonObject.put(new String(this.label.getBytes()), content);
    }

    return jsonObject;
  }
}
