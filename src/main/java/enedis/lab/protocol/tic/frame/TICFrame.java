// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
// 
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.protocol.tic.frame;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.json.JSONObject;

import enedis.lab.protocol.tic.TICMode;
import enedis.lab.types.BytesArray;
import enedis.lab.types.DataDictionary;
import enedis.lab.types.DataDictionaryException;
import enedis.lab.types.datadictionary.DataDictionaryBase;

/**
 * TICFrame
 */
public abstract class TICFrame
{
	/** Beginning pattern */
	public static final byte		BEGINNING_PATTERN	= 0x02;									// STX
	/** End pattern */
	public static final byte		END_PATTERN			= 0x03;									// ETX
	/** End of trame pattern */
	public static final byte		EOT					= 0x04;									// EOT

	// Options
	/** No specific options */
	public static final int			EXPLICIT			= 0x0000;
	/** No checksum options */
	public static final int			NOCHECKSUM			= 0x0001;
	/** No date time options */
	public static final int			NODATETIME			= 0x0002;
	/** No tic mode options */
	public static final int			NOTICMODE			= 0x0004;
	/** No invalid options */
	public static final int			NOINVALID			= 0x0008;
	/** Trimmed options */
	public static final int			TRIMMED				= NOCHECKSUM | NODATETIME | NOINVALID;

	/** Key tic frame */
	public static final String		KEY_TICFRAME		= "ticFrame";
	/** Key tic mode */
	public static final String		KEY_TICMODE			= "ticMode";
	/** Key label */
	public static final String		KEY_LABEL			= "label";
	/** Key data */
	public static final String		KEY_DATA			= "data";
	/** Key checksum */
	public static final String		KEY_CHECKSUM		= "checksum";
	/** Key date time */
	public static final String		KEY_DATETIME		= "dateTime";

	protected List<TICFrameDataSet>	DataSetList;

	/**
	 * Default constructor
	 */
	public TICFrame()
	{
		this.DataSetList = new ArrayList<TICFrameDataSet>();
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(this.DataSetList);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		TICFrame other = (TICFrame) obj;
		return Objects.equals(this.DataSetList, other.DataSetList);
	}

	/**
	 * Return the list of DataSet
	 *
	 * @return the list of DataSet
	 */
	public List<TICFrameDataSet> getDataSetList()
	{
		return this.DataSetList;
	}

	/**
	 * Set the list of DataSet
	 *
	 * @param dataSetList
	 */
	public void setDataSetList(List<TICFrameDataSet> dataSetList)
	{
		this.DataSetList = dataSetList;
	}

	/**
	 * Return the index of the given DataSet
	 *
	 * @param label
	 *            Label of the DataSet
	 * @return the index of the given DataSet or '-1' if it does not exist
	 */
	public int indexOf(String label)
	{
		int index = -1;

		for (int i = 0; i < this.DataSetList.size(); i++)
		{
			if (this.DataSetList.get(i).getLabel().equals(label))
			{
				index = i;
				break;
			}
		}

		return index;
	}

	/**
	 * Return the given DataSet
	 *
	 * @param label
	 *            Label of the DataSet
	 * @return the given DataSet or 'null' if it does not exist
	 */
	public TICFrameDataSet getDataSet(String label)
	{
		TICFrameDataSet dataSet = null;

		for (int i = 0; i < this.DataSetList.size(); i++)
		{
			if (this.DataSetList.get(i).getLabel().equals(label))
			{
				dataSet = this.DataSetList.get(i);
				break;
			}
		}

		return dataSet;
	}

	/**
	 * Return the data field of the given DataSet
	 *
	 * @param label
	 *            Label of the DataSet
	 * @return the data field of the given DataSet
	 */
	public String getData(String label)
	{
		TICFrameDataSet dataSet = this.getDataSet(label);

		if (dataSet != null)
		{
			return dataSet.getData();
		}

		else
		{
			return null;
		}
	}

	/**
	 * Add the dataSet at the end of the list. If the dataSet already existed, its data and checksum are just set
	 *
	 * @param dataSet
	 */
	public void addDataSet(TICFrameDataSet dataSet)
	{
		TICFrameDataSet oldDataSet = this.getDataSet(dataSet.getLabel());

		if (oldDataSet == null)
		{
			this.DataSetList.add(dataSet);
		}

		else
		{
			oldDataSet.setData(dataSet.getData());
			oldDataSet.setChecksum(dataSet.getChecksum());
		}
	}

	/**
	 * Add the dataSet at the given index of the list. If the dataSet already existed, its data and checksum are set and
	 * it is moved at the given index
	 *
	 * @param index
	 * @param dataSet
	 */
	public void addDataSet(int index, TICFrameDataSet dataSet)
	{
		int dataSetIndex = this.indexOf(dataSet.getLabel());

		if (dataSetIndex < 0)
		{
			this.DataSetList.add(index, dataSet);
		}

		else
		{
			if (dataSetIndex != index)
			{
				this.removeDataSet(dataSetIndex);
				this.DataSetList.add(index, dataSet);
			}

			else
			{
				this.DataSetList.get(index).setData(dataSet.getData());
				this.DataSetList.get(index).setChecksum(dataSet.getChecksum());
			}
		}
	}

	/**
	 * Add data set
	 *
	 * @param label
	 * @param data
	 * @return tic frame data set
	 */
	public abstract TICFrameDataSet addDataSet(String label, String data);

	/**
	 * Add data set
	 *
	 * @param index
	 * @param label
	 * @param data
	 * @return tic frame data set
	 */
	public abstract TICFrameDataSet addDataSet(int index, String label, String data);

	/**
	 * Move the DataSet at the given index.
	 *
	 * @param label
	 * @param index
	 * @return true if the operation has been done, else return false
	 */
	public boolean moveDataSet(String label, int index)
	{
		int previous_index = this.indexOf(label);

		// Si l'ensemble des conditions suivantes sont vérifiées :
		// - le Groupe d'Information existe,
		// - l'index donné est cohérent
		// - l'index donné diffère de l'index actuel
		if ((previous_index >= 0) && (index >= 0) && (index < this.DataSetList.size()) && (index != previous_index))
		{
			// Alors déplacer le Groupe d'Information à l'index donné:

			TICFrameDataSet dataSet = this.DataSetList.remove(previous_index);

			if (index < this.DataSetList.size())
			{
				this.DataSetList.add(index, dataSet);
			}

			else
			{
				this.DataSetList.add(dataSet);
			}

			return true;
		}

		else
		{
			// Sinon, l'opération n'est pas effectuée
			return false;
		}
	}

	/**
	 * Set data set
	 *
	 * @param label
	 * @param data
	 * @return true if succeed
	 */
	public boolean setDataSet(String label, String data)
	{
		TICFrameDataSet dataSet = this.getDataSet(label);

		if (dataSet != null)
		{
			dataSet.setData(data);
			dataSet.getConsistentChecksum();

			return true;
		}

		else
		{
			return false;
		}
	}

	/**
	 * Set data set
	 *
	 * @param label
	 * @param data
	 * @param checksum
	 * @return true if succeed
	 */
	public boolean setDataSet(String label, String data, byte checksum)
	{
		TICFrameDataSet dataSet = this.getDataSet(label);

		if (dataSet != null)
		{
			dataSet.setData(data);
			dataSet.setChecksum(checksum);

			return true;
		}

		else
		{
			return false;
		}
	}

	/**
	 * Remove data set
	 *
	 * @param label
	 * @return tic frame data set
	 */
	public TICFrameDataSet removeDataSet(String label)
	{
		TICFrameDataSet dataSet = null;

		int index = this.indexOf(label);

		if (index >= 0)
		{
			dataSet = this.DataSetList.remove(index);
		}

		return dataSet;
	}

	/**
	 * Remove data set
	 *
	 * @param index
	 * @return tic frame data set
	 */
	public TICFrameDataSet removeDataSet(int index)
	{
		TICFrameDataSet dataSet = null;

		if ((index >= 0) && (index < this.DataSetList.size()))
		{
			dataSet = this.DataSetList.remove(index);
		}

		return dataSet;
	}

	/**
	 * Get bytes
	 *
	 * @return bytes
	 */
	public byte[] getBytes()
	{
		BytesArray bytesFrame = new BytesArray();

		bytesFrame.add(BEGINNING_PATTERN);

		for (int i = 0; i < this.DataSetList.size(); i++)
		{
			bytesFrame.addAll(this.DataSetList.get(i).getBytes());
		}

		bytesFrame.add(END_PATTERN);

		return bytesFrame.getBytes();
	}

	/**
	 * Get data dictionary
	 *
	 * @return data dictionary
	 * @throws DataDictionaryException
	 */
	public DataDictionary getDataDictionary() throws DataDictionaryException
	{
		return this.getDataDictionary(0);
	}

	/**
	 * Return a DataDictionary object corresponding to the TIC frame
	 *
	 * @param options
	 *            options to DataDictionary render (use like bits fields) : - EXPLICIT : frame is detailed - NOCHECKSUM
	 *            : DataSet checksum are hidden - NODATETIME : DataSet dateTime fields are hidden - NOTICMODE : Tic mode
	 *            is hidden; - TRIMMED : NOCHECKSUM | NODATETIME - FILTER_INVALID : Invalid DataSet are filtered
	 *
	 *
	 * @return data dictionary
	 * @throws DataDictionaryException
	 */
	public DataDictionary getDataDictionary(int options) throws DataDictionaryException
	{
		DataDictionaryBase dictionary = new DataDictionaryBase();

		JSONObject jsonObject = new JSONObject();

		if (0 == (options & NOTICMODE))
		{
			dictionary.addKey(KEY_TICMODE);
			dictionary.set(KEY_TICMODE, this.getMode().name());
		}

		for (int i = 0; i < this.DataSetList.size(); i++)
		{
			TICFrameDataSet dataSet = this.DataSetList.get(i);

			// Si le DataSet n'est pas valid et que l'option "filtrer les
			// groupes invalides" est activée,
			if ((false == dataSet.isValid()) && ((options & NOINVALID) > 0))
			{
				// Ignorer le DataSet
			}

			// Sinon,
			else
			{
				jsonObject.append(KEY_TICFRAME, dataSet.toJSON(options));
			}

		}

		dictionary.addKey(KEY_TICFRAME);
		dictionary.set(KEY_TICFRAME, jsonObject.get(KEY_TICFRAME));

		return dictionary;
	}

	/**
	 * Get mode
	 *
	 * @return mode
	 */
	public abstract TICMode getMode();

}
