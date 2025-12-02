// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.frame;

import enedis.lab.types.BytesArray;
import java.util.Objects;
import org.json.JSONObject;

/**
 * Abstract base class for data sets in TIC frames.
 *
 * <p>This class provides the structure and common operations for TIC frame data sets, including
 * label/data management, checksum calculation, and serialization. Subclasses implement
 * protocol-specific details for standard and historic TIC formats.
 *
 * <p>Key features:
 *
 * <ul>
 *   <li>Defines constants for data set delimiters
 *   <li>Manages label, data, and checksum fields
 *   <li>Provides methods for setting and validating fields
 *   <li>Abstract methods for protocol-specific serialization and checksum
 * </ul>
 *
 * @author Enedis Smarties team
 * @see BytesArray
 */
public abstract class TICFrameDataSet {
  /** Data set start delimiter (LF, 0x0A). */
  public static final byte BEGINNING_PATTERN = 0x0A; // LF

  /** Data set end delimiter (CR, 0x0D). */
  public static final byte END_PATTERN = 0x0D; // CR

  /** Label field for the data set. */
  protected BytesArray label = null;

  /** Data field for the data set. */
  protected BytesArray data = null;

  /** Checksum value for the data set. */
  protected byte checksum;

  /** Constructs an empty TIC frame data set. */
  public TICFrameDataSet() {
    this.init();
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.checksum, this.data, this.label);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (this.getClass() != obj.getClass()) return false;
    TICFrameDataSet other = (TICFrameDataSet) obj;
    return this.checksum == other.checksum
        && Objects.equals(this.data, other.data)
        && Objects.equals(this.label, other.label);
  }

  /**
   * Sets up the data set with the given label and data strings.
   *
   * @param label the label string
   * @param data the data string
   */
  public void setup(String label, String data) {
    this.setup(label.getBytes(), data.getBytes());
  }

  /**
   * Sets up the data set with the given label and data byte arrays.
   *
   * @param label the label bytes
   * @param data the data bytes
   */
  public void setup(byte[] label, byte[] data) {
    this.label = new BytesArray(label);
    this.data = new BytesArray(data);
  }

  /**
   * Returns the label string for this data set.
   *
   * @return the label string
   */
  public String getLabel() {
    return new String(this.label.getBytes());
  }

  /**
   * Sets the label field from a string value.
   *
   * @param label the label string
   */
  public void setLabel(String label) {
    this.setLabel(label.getBytes());
  }

  /**
   * Sets the label field from a byte array value.
   *
   * @param label the label bytes
   */
  public void setLabel(byte[] label) {
    this.setLabel(new BytesArray(label));
  }

  /**
   * Sets the label field from a {@link BytesArray} value and updates the checksum.
   *
   * @param label the label as a {@link BytesArray}
   */
  public void setLabel(BytesArray label) {
    this.label = label;
    this.setChecksum();
  }

  /**
   * Returns the data string for this data set.
   *
   * @return the data string
   */
  public String getData() {
    return new String(this.data.getBytes());
  }

  /**
   * Sets the data field from a string value.
   *
   * @param data the data string
   */
  public void setData(String data) {
    this.setData(data.getBytes());
  }

  /**
   * Sets the data field from a byte array value.
   *
   * @param data the data bytes
   */
  public void setData(byte[] data) {
    this.setData(new BytesArray(data));
  }

  /**
   * Sets the data field from a {@link BytesArray} value and updates the checksum.
   *
   * @param data the data as a {@link BytesArray}
   */
  public void setData(BytesArray data) {
    this.data = data;
    this.setChecksum();
  }

  /**
   * Returns the checksum value for this data set.
   *
   * @return the checksum value
   */
  public byte getChecksum() {
    return this.checksum;
  }

  /**
   * Computes and sets the checksum for this data set (label and data must be set).
   *
   * @return true if the operation succeeds, false otherwise
   */
  public boolean setChecksum() {
    Byte checksum = this.getConsistentChecksum();

    if (checksum != null) {
      this.checksum = checksum;
      return true;
    } else {
      return false;
    }
  }

  /**
   * Sets the checksum to the given value.
   *
   * @param checksum the checksum value to set
   */
  public void setChecksum(byte checksum) {
    this.checksum = checksum;
  }

  /**
   * Checks if the label, data, and checksum fields are consistent.
   *
   * @return true if the fields are consistent with the checksum, false otherwise
   */
  public boolean isValid() {
    Byte consistentChecksum = this.getConsistentChecksum();

    if ((consistentChecksum != null)
        && (consistentChecksum.byteValue() == (this.checksum & (byte) 0xFF))) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Serializes this data set to a byte array according to the TIC protocol.
   *
   * @return the byte array representation of the data set
   */
  public abstract byte[] getBytes();

  /**
   * Converts this data set to a JSON object using the default options.
   *
   * @return the JSON representation of the data set
   */
  public abstract JSONObject toJSON();

  /**
   * Converts this data set to a JSON object with the specified options.
   *
   * @param option bitmask of options (e.g., hide checksum, date/time)
   * @return the JSON representation of the data set
   */
  public abstract JSONObject toJSON(int option);

  /**
   * Computes the consistent checksum for this data set according to the protocol.
   *
   * @return the consistent checksum value, or null if not computable
   */
  protected abstract Byte getConsistentChecksum();

  /** Initializes the data set, clearing label, data, and checksum fields. */
  private void init() {
    this.label = null;
    this.data = null;
    this.checksum = -1;
  }
}
