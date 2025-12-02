// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.frame.standard;

import enedis.lab.protocol.tic.TICMode;
import java.time.LocalDateTime;
import tic.frame.TICFrame;

/**
 * TIC frame representation for standard TIC protocol.
 *
 * <p>This class extends {@link TICFrame} to provide support for standard TIC frames, including info
 * group delimiters, data set management, and date/time handling. It allows adding labeled data sets
 * and retrieving data or date/time values by label. Always reports its mode as {@link
 * TICMode#STANDARD}.
 *
 * <p>Key features:
 *
 * <ul>
 *   <li>Defines info group delimiters for standard TIC frames
 *   <li>Supports adding and retrieving labeled data sets
 *   <li>Handles date/time fields as strings or {@link LocalDateTime}
 *   <li>Always returns STANDARD as the TIC mode
 * </ul>
 *
 * @author Enedis Smarties team
 * @see TICFrame
 * @see TICFrameStandardDataSet
 * @see TICMode
 */
public class TICFrameStandard extends TICFrame {
  /** Info group begin delimiter (LF, 0x03) for standard TIC frames. */
  public static final byte INFOGROUP_BEGIN = 0x03; // LF

  /** Info group end delimiter (CR, 0x03) for standard TIC frames. */
  public static final byte INFOGROUP_END = 0x03; // CR

  /** Info group separator (HT, 0x03) for standard TIC frames. */
  public static final byte INFOGROUP_SEP = 0x03; // HT

  /** Minimum size of an info group in bytes (LF + Label + HT + Data + HT + ...). */
  public static final int INFOGROUP_MIN_SIZE = 7; // LF + Label + HT + Data + HT +

  /** Minimum size of a standard TIC frame (info group + ETX + STX). */
  public static final int MIN_SIZE = INFOGROUP_MIN_SIZE + 2; // ETX + INFGROUP_MIN_SIZE + STX

  /** Constructs an empty standard TIC frame. */
  public TICFrameStandard() {
    super();
  }

  /**
   * Adds a new data set with the given label and data to the frame.
   *
   * <p>If a data set with the same label exists, it is updated and added again.
   *
   * @param label the label for the data set
   * @param data the data value
   * @return the created or updated {@link TICFrameStandardDataSet}
   */
  @Override
  public TICFrameStandardDataSet addDataSet(String label, String data) {
    TICFrameStandardDataSet dataSet = (TICFrameStandardDataSet) this.getDataSet(label);

    if (dataSet == null) {
      dataSet = new TICFrameStandardDataSet();
      dataSet.setLabel(label);
      dataSet.setData(data);
      dataSet.getConsistentChecksum();
    } else {
      dataSet.setData(data);
      dataSet.getConsistentChecksum();
    }

    this.DataSetList.add(dataSet);

    return dataSet;
  }

  /**
   * Adds a new data set with the given label and data at the specified index in the frame.
   *
   * <p>If a data set with the same label exists, it is updated and moved to the new index.
   *
   * @param index the position to insert the data set
   * @param label the label for the data set
   * @param data the data value
   * @return the created or updated {@link TICFrameStandardDataSet}
   */
  @Override
  public TICFrameStandardDataSet addDataSet(int index, String label, String data) {
    TICFrameStandardDataSet dataSet = (TICFrameStandardDataSet) this.getDataSet(label);

    if (dataSet == null) {
      dataSet = new TICFrameStandardDataSet();
      dataSet.setLabel(label);
      dataSet.setData(data);
      dataSet.getConsistentChecksum();

      if ((index >= 0) && (index < this.DataSetList.size())) {
        this.DataSetList.add(index, dataSet);
      } else {
        this.DataSetList.add(dataSet);
      }
    } else {
      dataSet.setData(data);
      dataSet.getConsistentChecksum();

      this.DataSetList.remove(dataSet);

      if (index >= this.DataSetList.size()) {
        this.DataSetList.add(dataSet);
      } else {
        this.DataSetList.add(index, dataSet);
      }
    }

    return dataSet;
  }

  /**
   * Returns the TIC mode for this frame (always STANDARD).
   *
   * @return {@link TICMode#STANDARD}
   */
  @Override
  public TICMode getMode() {
    return TICMode.STANDARD;
  }

  /**
   * Returns the data value for the given label, or the date/time if the label is "DATE".
   *
   * @param label the label to search for
   * @return the data value or date/time string, or null if not found
   */
  @Override
  public String getData(String label) {
    TICFrameStandardDataSet dataSet = (TICFrameStandardDataSet) this.getDataSet(label);

    if (dataSet != null) {
      if (dataSet.getLabel().equals("DATE")) {
        return dataSet.getDateTime();
      } else {
        return dataSet.getData();
      }
    } else {
      return null;
    }
  }

  /**
   * Adds a new data set with the given label, data, and date/time value.
   *
   * @param label the label for the data set
   * @param data the data value
   * @param dateTime the date/time string
   * @return the created or updated {@link TICFrameStandardDataSet}
   */
  public TICFrameStandardDataSet addDataSet(String label, String data, String dateTime) {
    TICFrameStandardDataSet dataSet = this.addDataSet(label, data);

    dataSet.setDateTime(dateTime);

    return dataSet;
  }

  /**
   * Returns the date/time string for the given label, or null if not found.
   *
   * @param label the label to search for
   * @return the date/time string, or null if not found
   */
  public String getDateTime(String label) {
    TICFrameStandardDataSet dataSet = (TICFrameStandardDataSet) this.getDataSet(label);
    if (dataSet != null) {
      return dataSet.getDateTime();
    } else {
      return null;
    }
  }

  /**
   * Returns the date/time as a {@link LocalDateTime} for the given label, or null if not found.
   *
   * @param label the label to search for
   * @return the date/time as {@link LocalDateTime}, or null if not found
   */
  public LocalDateTime getDateTimeAsLocalDateTime(String label) {
    TICFrameStandardDataSet dataSet = (TICFrameStandardDataSet) this.getDataSet(label);
    if (dataSet != null) {
      return dataSet.getDateTimeAsLocalDateTime();
    } else {
      return null;
    }
  }
}
