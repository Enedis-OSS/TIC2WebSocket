// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.protocol.tic.frame.standard;

import enedis.lab.protocol.tic.frame.TICFrame;
import enedis.lab.protocol.tic.frame.TICFrameDataSet;
import enedis.lab.types.BytesArray;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import org.json.JSONObject;

/**
 * Data set representation for standard TIC frames.
 *
 * <p>This class extends {@link TICFrameDataSet} to provide checksum calculation, byte
 * serialization, date/time handling, and JSON conversion for standard TIC data sets. It defines the
 * separator and implements the protocol-specific logic for standard frames.
 *
 * <p>Key features:
 *
 * <ul>
 *   <li>Defines the separator for standard TIC data sets
 *   <li>Implements checksum calculation for label, data, and optional date/time
 *   <li>Handles date/time fields as strings or {@link LocalDateTime}
 *   <li>Serializes the data set to bytes and JSON
 * </ul>
 *
 * @author Enedis Smarties team
 * @see TICFrameDataSet
 * @see TICFrame
 */
public class TICFrameStandardDataSet extends TICFrameDataSet {
  /** Separator character (horizontal tab, 0x09) used in standard TIC data sets. */
  public static final byte SEPARATOR = 0x09; // HT

  /** Date/time field for the data set (optional, may be null). */
  protected BytesArray dateTime = null;

  /** Constructs an empty standard TIC data set. */
  public TICFrameStandardDataSet() {
    super();
    this.init();
  }

  /**
   * Computes the consistent checksum for this data set according to the standard TIC protocol.
   *
   * <p>The checksum is calculated over the label, optional date/time, and data fields.
   *
   * @return the computed checksum, or null if label or data is missing
   */
  @Override
  public Byte getConsistentChecksum() {
    Byte crc = 0;
    byte[] labelByte;
    byte[] dataByte;
    byte[] dateByte;

    if ((this.label != null) && (this.data != null)) {
      labelByte = this.label.getBytes();
      for (int i = 0; i < labelByte.length; i++) {
        crc = this.computeUpdate(crc, labelByte[i]);
      }

      crc = this.computeUpdate(crc, SEPARATOR);

      if (this.dateTime != null) {
        dateByte = this.dateTime.getBytes();
        for (int i = 0; i < dateByte.length; i++) {
          crc = this.computeUpdate(crc, dateByte[i]);
        }

        crc = this.computeUpdate(crc, SEPARATOR);
      }

      dataByte = this.data.getBytes();

      for (int i = 0; i < dataByte.length; i++) {
        crc = this.computeUpdate(crc, dataByte[i]);
      }
      crc = this.computeUpdate(crc, SEPARATOR);

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
   * Finalizes the checksum value according to the standard TIC protocol.
   *
   * @param crc the current checksum value
   * @return the finalized checksum value
   */
  public Byte computeEnd(Byte crc) {
    return (byte) ((crc & 0x3F) + 0x20);
  }

  /**
   * Serializes this data set to a byte array according to the standard TIC protocol.
   *
   * @return the byte array representation of the data set
   */
  @Override
  public byte[] getBytes() {
    BytesArray dataSet = new BytesArray();

    if ((this.label != null) && (this.data != null)) {
      dataSet.add(BEGINNING_PATTERN);
      dataSet.addAll(this.label.getBytes());

      if (null != this.dateTime) {
        dataSet.add(SEPARATOR);
        dataSet.addAll(this.dateTime.getBytes());
      }

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
   * @param option bitmask of options (e.g., {@link TICFrame#NOCHECKSUM}, {@link
   *     TICFrame#NODATETIME})
   * @return the JSON representation of the data set
   */
  @Override
  public JSONObject toJSON(int option) {
    JSONObject jsonObject = new JSONObject();

    if ((option & TICFrame.NOCHECKSUM) > 0) {
      if ((this.dateTime == null) || ((option & TICFrame.NODATETIME) > 0)) {
        jsonObject.put(new String(this.label.getBytes()), new String(this.data.getBytes()));
      } else {
        JSONObject content = new JSONObject();
        content.put(TICFrame.KEY_DATA, new String(this.data.getBytes()));
        content.put(TICFrame.KEY_DATETIME, new String(this.dateTime.getBytes()));
        jsonObject.put(new String(this.label.getBytes()), content);
      }
    } else {
      if ((this.dateTime == null) || ((option & TICFrame.NODATETIME) > 0)) {
        JSONObject content = new JSONObject();
        content.put(TICFrame.KEY_DATA, new String(this.data.getBytes()));
        content.put(TICFrame.KEY_CHECKSUM, this.checksum);
        jsonObject.put(new String(this.label.getBytes()), content);
      } else {
        JSONObject content = new JSONObject();
        content.put(TICFrame.KEY_DATA, new String(this.data.getBytes()));
        content.put(TICFrame.KEY_DATETIME, new String(this.dateTime.getBytes()));
        content.put(TICFrame.KEY_CHECKSUM, this.checksum);
        jsonObject.put(new String(this.label.getBytes()), content);
      }
    }

    return jsonObject;
  }

  /**
   * Sets up the data set with the given label, data, and date/time strings.
   *
   * @param label the label string
   * @param data the data string
   * @param dateTime the date/time string
   */
  public void setup(String label, String data, String dateTime) {
    this.setup(label.getBytes(), data.getBytes(), dateTime.getBytes());
  }

  /**
   * Sets up the data set with the given label, data, and date/time byte arrays.
   *
   * @param label the label bytes
   * @param data the data bytes
   * @param dateTime the date/time bytes
   */
  public void setup(byte[] label, byte[] data, byte[] dateTime) {
    super.setup(label, data);
    this.dateTime = new BytesArray(dateTime);
  }

  /**
   * Returns the date/time string for this data set, or null if not set.
   *
   * @return the date/time string, or null if not set
   */
  public String getDateTime() {
    return this.dateTime == null ? null : new String(this.dateTime.getBytes());
  }

  /**
   * Checks if the date/time field is set.
   *
   * @return true if date/time is set, false otherwise
   */
  public Boolean checkDateTime() {
    return this.dateTime != null;
  }

  /**
   * Returns the date/time as a {@link LocalDateTime} for this data set, or null if not set or
   * invalid.
   *
   * <p>The expected format is "HyyMMddHHmmss" (13 characters, with the first character ignored).
   *
   * @return the date/time as {@link LocalDateTime}, or null if not set or invalid
   */
  public LocalDateTime getDateTimeAsLocalDateTime() {
    String strDateTime = this.getDateTime();
    LocalDateTime localDateTime = null;

    if (strDateTime != null && strDateTime.length() == 13) {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
      try {
        localDateTime = LocalDateTime.parse(strDateTime.substring(1), formatter);
      } catch (DateTimeParseException e) {
        localDateTime = null;
      }
    }

    return localDateTime;
  }

  /**
   * Sets the date/time field from a string value.
   *
   * @param dateTime the date/time string
   */
  public void setDateTime(String dateTime) {
    this.setDateTime(dateTime.getBytes());
  }

  /**
   * Sets the date/time field from a byte array value.
   *
   * @param dateTime the date/time bytes
   */
  public void setDateTime(byte[] dateTime) {
    this.setDateTime(new BytesArray(dateTime));
  }

  /**
   * Sets the date/time field from a {@link BytesArray} value.
   *
   * @param dateTime the date/time as a {@link BytesArray}
   */
  public void setDateTime(BytesArray dateTime) {
    this.dateTime = dateTime;
    this.setChecksum();
  }

  /** Initializes the data set, clearing the date/time field. */
  private void init() {
    this.dateTime = null;
  }
}
