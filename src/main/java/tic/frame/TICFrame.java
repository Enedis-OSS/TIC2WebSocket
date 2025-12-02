// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.frame;

import enedis.lab.protocol.tic.TICMode;
import enedis.lab.types.BytesArray;
import enedis.lab.types.DataDictionary;
import enedis.lab.types.DataDictionaryException;
import enedis.lab.types.datadictionary.DataDictionaryBase;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.json.JSONObject;

/**
 * Abstract base class for TIC frames.
 *
 * <p>This class provides the structure and common operations for TIC frames, including data set
 * management, byte serialization, and data dictionary conversion. Subclasses implement
 * protocol-specific details for standard and historic TIC formats.
 *
 * <p>Key features:
 *
 * <ul>
 *   <li>Defines constants for frame delimiters and options
 *   <li>Manages a list of labeled data sets
 *   <li>Provides methods for adding, removing, and retrieving data sets
 *   <li>Supports conversion to byte arrays and data dictionaries
 *   <li>Abstract methods for protocol-specific data set and mode handling
 * </ul>
 *
 * @author Enedis Smarties team
 * @see TICFrameDataSet
 * @see TICMode
 */
public abstract class TICFrame {
  /** Frame start delimiter (STX, 0x02). */
  public static final byte BEGINNING_PATTERN = 0x02; // STX

  /** Frame end delimiter (ETX, 0x03). */
  public static final byte END_PATTERN = 0x03; // ETX

  /** End of transmission delimiter (EOT, 0x04). */
  public static final byte EOT = 0x04; // EOT

  // Frame options and keys
  /** No specific options. */
  public static final int EXPLICIT = 0x0000;

  /** Hide checksums in data dictionary. */
  public static final int NOCHECKSUM = 0x0001;

  /** Hide date/time fields in data dictionary. */
  public static final int NODATETIME = 0x0002;

  /** Hide TIC mode in data dictionary. */
  public static final int NOTICMODE = 0x0004;

  /** Filter invalid data sets in data dictionary. */
  public static final int NOINVALID = 0x0008;

  /** Trimmed options: hide checksum, date/time, and invalid data sets. */
  public static final int TRIMMED = NOCHECKSUM | NODATETIME | NOINVALID;

  /** Key for TIC frame in data dictionary. */
  public static final String KEY_TICFRAME = "ticFrame";

  /** Key for TIC mode in data dictionary. */
  public static final String KEY_TICMODE = "ticMode";

  /** Key for label in data dictionary. */
  public static final String KEY_LABEL = "label";

  /** Key for data in data dictionary. */
  public static final String KEY_DATA = "data";

  /** Key for checksum in data dictionary. */
  public static final String KEY_CHECKSUM = "checksum";

  /** Key for date/time in data dictionary. */
  public static final String KEY_DATETIME = "dateTime";

  /** List of data sets contained in this TIC frame. */
  protected List<TICFrameDataSet> DataSetList;

  /** Constructs an empty TIC frame with an empty data set list. */
  public TICFrame() {
    this.DataSetList = new ArrayList<TICFrameDataSet>();
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.DataSetList);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (this.getClass() != obj.getClass()) return false;
    TICFrame other = (TICFrame) obj;
    return Objects.equals(this.DataSetList, other.DataSetList);
  }

  /**
   * Returns the list of data sets contained in this frame.
   *
   * @return the list of data sets
   */
  public List<TICFrameDataSet> getDataSetList() {
    return this.DataSetList;
  }

  /**
   * Sets the list of data sets for this frame.
   *
   * @param dataSetList the list of data sets to set
   */
  public void setDataSetList(List<TICFrameDataSet> dataSetList) {
    this.DataSetList = dataSetList;
  }

  /**
   * Returns the index of the data set with the given label.
   *
   * @param label the label of the data set
   * @return the index of the data set, or -1 if not found
   */
  public int indexOf(String label) {
    int index = -1;

    for (int i = 0; i < this.DataSetList.size(); i++) {
      if (this.DataSetList.get(i).getLabel().equals(label)) {
        index = i;
        break;
      }
    }

    return index;
  }

  /**
   * Returns the data set with the given label, or null if not found.
   *
   * @param label the label of the data set
   * @return the data set, or null if not found
   */
  public TICFrameDataSet getDataSet(String label) {
    TICFrameDataSet dataSet = null;

    for (int i = 0; i < this.DataSetList.size(); i++) {
      if (this.DataSetList.get(i).getLabel().equals(label)) {
        dataSet = this.DataSetList.get(i);
        break;
      }
    }

    return dataSet;
  }

  /**
   * Returns the data value for the data set with the given label, or null if not found.
   *
   * @param label the label of the data set
   * @return the data value, or null if not found
   */
  public String getData(String label) {
    TICFrameDataSet dataSet = this.getDataSet(label);

    if (dataSet != null) {
      return dataSet.getData();
    } else {
      return null;
    }
  }

  /**
   * Adds the given data set to the end of the list. If a data set with the same label exists, its
   * data and checksum are updated.
   *
   * @param dataSet the data set to add or update
   */
  public void addDataSet(TICFrameDataSet dataSet) {
    TICFrameDataSet oldDataSet = this.getDataSet(dataSet.getLabel());

    if (oldDataSet == null) {
      this.DataSetList.add(dataSet);
    } else {
      oldDataSet.setData(dataSet.getData());
      oldDataSet.setChecksum(dataSet.getChecksum());
    }
  }

  /**
   * Adds the given data set at the specified index. If a data set with the same label exists, its
   * data and checksum are updated and it is moved to the new index.
   *
   * @param index the position to insert the data set
   * @param dataSet the data set to add or update
   */
  public void addDataSet(int index, TICFrameDataSet dataSet) {
    int dataSetIndex = this.indexOf(dataSet.getLabel());

    if (dataSetIndex < 0) {
      this.DataSetList.add(index, dataSet);
    } else {
      if (dataSetIndex != index) {
        this.removeDataSet(dataSetIndex);
        this.DataSetList.add(index, dataSet);
      } else {
        this.DataSetList.get(index).setData(dataSet.getData());
        this.DataSetList.get(index).setChecksum(dataSet.getChecksum());
      }
    }
  }

  /**
   * Adds a new data set with the given label and data to the frame.
   *
   * @param label the label for the data set
   * @param data the data value
   * @return the created or updated data set
   */
  public abstract TICFrameDataSet addDataSet(String label, String data);

  /**
   * Adds a new data set with the given label and data at the specified index in the frame.
   *
   * @param index the position to insert the data set
   * @param label the label for the data set
   * @param data the data value
   * @return the created or updated data set
   */
  public abstract TICFrameDataSet addDataSet(int index, String label, String data);

  /**
   * Moves the data set with the given label to the specified index.
   *
   * @param label the label of the data set to move
   * @param index the new index for the data set
   * @return true if the operation succeeded, false otherwise
   */
  public boolean moveDataSet(String label, int index) {
    int previous_index = this.indexOf(label);

    // Si l'ensemble des conditions suivantes sont vérifiées :
    // - le Groupe d'Information existe,
    // - l'index donné est cohérent
    // - l'index donné diffère de l'index actuel
    if ((previous_index >= 0)
        && (index >= 0)
        && (index < this.DataSetList.size())
        && (index != previous_index)) {
      // Alors déplacer le Groupe d'Information à l'index donné:

      TICFrameDataSet dataSet = this.DataSetList.remove(previous_index);

      if (index < this.DataSetList.size()) {
        this.DataSetList.add(index, dataSet);
      } else {
        this.DataSetList.add(dataSet);
      }

      return true;
    } else {
      // Sinon, l'opération n'est pas effectuée
      return false;
    }
  }

  /**
   * Sets the data value for the data set with the given label.
   *
   * @param label the label of the data set
   * @param data the new data value
   * @return true if the data set was found and updated, false otherwise
   */
  public boolean setDataSet(String label, String data) {
    TICFrameDataSet dataSet = this.getDataSet(label);

    if (dataSet != null) {
      dataSet.setData(data);
      dataSet.getConsistentChecksum();

      return true;
    } else {
      return false;
    }
  }

  /**
   * Sets the data value and checksum for the data set with the given label.
   *
   * @param label the label of the data set
   * @param data the new data value
   * @param checksum the new checksum value
   * @return true if the data set was found and updated, false otherwise
   */
  public boolean setDataSet(String label, String data, byte checksum) {
    TICFrameDataSet dataSet = this.getDataSet(label);

    if (dataSet != null) {
      dataSet.setData(data);
      dataSet.setChecksum(checksum);

      return true;
    } else {
      return false;
    }
  }

  /**
   * Removes the data set with the given label from the frame.
   *
   * @param label the label of the data set to remove
   * @return the removed data set, or null if not found
   */
  public TICFrameDataSet removeDataSet(String label) {
    TICFrameDataSet dataSet = null;

    int index = this.indexOf(label);

    if (index >= 0) {
      dataSet = this.DataSetList.remove(index);
    }

    return dataSet;
  }

  /**
   * Removes the data set at the specified index from the frame.
   *
   * @param index the index of the data set to remove
   * @return the removed data set, or null if not found
   */
  public TICFrameDataSet removeDataSet(int index) {
    TICFrameDataSet dataSet = null;

    if ((index >= 0) && (index < this.DataSetList.size())) {
      dataSet = this.DataSetList.remove(index);
    }

    return dataSet;
  }

  /**
   * Serializes this frame to a byte array according to the TIC protocol.
   *
   * @return the byte array representation of the frame
   */
  public byte[] getBytes() {
    BytesArray bytesFrame = new BytesArray();

    bytesFrame.add(BEGINNING_PATTERN);

    for (int i = 0; i < this.DataSetList.size(); i++) {
      bytesFrame.addAll(this.DataSetList.get(i).getBytes());
    }

    bytesFrame.add(END_PATTERN);

    return bytesFrame.getBytes();
  }

  /**
   * Returns a data dictionary representation of this frame with default options.
   *
   * @return the data dictionary
   * @throws DataDictionaryException if conversion fails
   */
  public DataDictionary getDataDictionary() throws DataDictionaryException {
    return this.getDataDictionary(0);
  }

  /**
   * Returns a data dictionary representation of this frame with the specified options.
   *
   * @param options options to DataDictionary render (use like bits fields) : - EXPLICIT : frame is
   *     detailed - NOCHECKSUM : DataSet checksum are hidden - NODATETIME : DataSet dateTime fields
   *     are hidden - NOTICMODE : Tic mode is hidden; - TRIMMED : NOCHECKSUM | NODATETIME -
   *     FILTER_INVALID : Invalid DataSet are filtered
   * @return data dictionary
   * @throws DataDictionaryException
   */
  public DataDictionary getDataDictionary(int options) throws DataDictionaryException {
    DataDictionaryBase dictionary = new DataDictionaryBase();

    JSONObject jsonObject = new JSONObject();

    if (0 == (options & NOTICMODE)) {
      dictionary.addKey(KEY_TICMODE);
      dictionary.set(KEY_TICMODE, this.getMode().name());
    }

    for (int i = 0; i < this.DataSetList.size(); i++) {
      TICFrameDataSet dataSet = this.DataSetList.get(i);

      // Si le DataSet n'est pas valid et que l'option "filtrer les
      // groupes invalides" est activée,
      if ((false == dataSet.isValid()) && ((options & NOINVALID) > 0)) {
        // Ignorer le DataSet
      }

      // Sinon,
      else {
        jsonObject.append(KEY_TICFRAME, dataSet.toJSON(options));
      }
    }

    dictionary.addKey(KEY_TICFRAME);
    dictionary.set(KEY_TICFRAME, jsonObject.get(KEY_TICFRAME));

    return dictionary;
  }

  /**
   * Returns the TIC mode for this frame (protocol-specific).
   *
   * @return the TIC mode
   */
  public abstract TICMode getMode();
}
