// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.frame.historic;

import enedis.lab.protocol.tic.TICMode;
import tic.frame.TICFrame;

/**
 * TIC frame representation for historic TIC protocol.
 *
 * <p>This class extends {@link TICFrame} to provide support for historic TIC frames, including
 * separator definition and data set management. It allows adding labeled data sets to the frame and
 * always reports its mode as {@link TICMode#HISTORIC}.
 *
 * <p>Key features:
 *
 * <ul>
 *   <li>Defines the separator for historic TIC frames
 *   <li>Supports adding labeled data sets at specific positions
 *   <li>Always returns HISTORIC as the TIC mode
 * </ul>
 *
 * @author Enedis Smarties team
 * @see TICFrame
 * @see TICFrameHistoricDataSet
 * @see TICMode
 */
public class TICFrameHistoric extends TICFrame {
  /** Separator character (space, 0x20) used in historic TIC frames. */
  public static final byte SEPARATOR = 0x20; // SP

  /** Constructs an empty historic TIC frame. */
  public TICFrameHistoric() {
    super();
  }

  /**
   * Adds a new data set with the given label and data to the end of the frame.
   *
   * @param label the label for the data set
   * @param data the data value
   * @return the created or updated {@link TICFrameHistoricDataSet}
   */
  @Override
  public TICFrameHistoricDataSet addDataSet(String label, String data) {
    return this.addDataSet(this.DataSetList.size(), label, data);
  }

  /**
   * Adds a new data set with the given label and data at the specified index in the frame.
   *
   * <p>If a data set with the same label exists, it is updated and moved to the new index.
   *
   * @param index the position to insert the data set
   * @param label the label for the data set
   * @param data the data value
   * @return the created or updated {@link TICFrameHistoricDataSet}
   */
  @Override
  public TICFrameHistoricDataSet addDataSet(int index, String label, String data) {
    TICFrameHistoricDataSet dataSet = (TICFrameHistoricDataSet) this.getDataSet(label);

    if (dataSet == null) {
      dataSet = new TICFrameHistoricDataSet();
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
   * Returns the TIC mode for this frame (always HISTORIC).
   *
   * @return {@link TICMode#HISTORIC}
   */
  @Override
  public TICMode getMode() {
    return TICMode.HISTORIC;
  }
}
