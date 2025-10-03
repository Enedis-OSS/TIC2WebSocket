// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.protocol.tic.frame.standard;

import java.time.LocalDateTime;

import enedis.lab.protocol.tic.TICMode;
import enedis.lab.protocol.tic.frame.TICFrame;

/**
 * TIC frame standard
 */
public class TICFrameStandard extends TICFrame
{
	/** Begin info group */
	public static final byte	INFOGROUP_BEGIN		= 0x03;						// LF
	/** End info group */
	public static final byte	INFOGROUP_END		= 0x03;						// CR
	/** Sep info group */
	public static final byte	INFOGROUP_SEP		= 0x03;						// HT

	/** Min size info group */
	public static final int		INFOGROUP_MIN_SIZE	= 7;						// LF + Label + HT + Data + HT +
	/** MIN_SIZE */
	public static final int		MIN_SIZE			= INFOGROUP_MIN_SIZE + 2;	// ETX + INFGROUP_MIN_SIZE + STX

	/**
	 * Default constructor
	 */
	public TICFrameStandard()
	{
		super();
	}

	@Override
	public TICFrameStandardDataSet addDataSet(String label, String data)
	{
		TICFrameStandardDataSet dataSet = (TICFrameStandardDataSet) this.getDataSet(label);

		if (dataSet == null)
		{
			dataSet = new TICFrameStandardDataSet();
			dataSet.setLabel(label);
			dataSet.setData(data);
			dataSet.getConsistentChecksum();
		}

		else
		{
			dataSet.setData(data);
			dataSet.getConsistentChecksum();
		}

		this.DataSetList.add(dataSet);

		return dataSet;
	}

	@Override
	public TICFrameStandardDataSet addDataSet(int index, String label, String data)
	{
		TICFrameStandardDataSet dataSet = (TICFrameStandardDataSet) this.getDataSet(label);

		if (dataSet == null)
		{
			dataSet = new TICFrameStandardDataSet();
			dataSet.setLabel(label);
			dataSet.setData(data);
			dataSet.getConsistentChecksum();

			if ((index >= 0) && (index < this.DataSetList.size()))
			{
				this.DataSetList.add(index, dataSet);
			}

			else
			{
				this.DataSetList.add(dataSet);
			}
		}

		else
		{
			dataSet.setData(data);
			dataSet.getConsistentChecksum();

			this.DataSetList.remove(dataSet);

			if (index >= this.DataSetList.size())
			{
				this.DataSetList.add(dataSet);
			}

			else
			{
				this.DataSetList.add(index, dataSet);
			}
		}

		return dataSet;
	}

	@Override
	public TICMode getMode()
	{
		return TICMode.STANDARD;
	}

	@Override
	public String getData(String label)
	{
		TICFrameStandardDataSet dataSet = (TICFrameStandardDataSet) this.getDataSet(label);

		if (dataSet != null)
		{
			if (dataSet.getLabel().equals("DATE"))
			{
				return dataSet.getDateTime();
			}
			else
			{
				return dataSet.getData();
			}
		}

		else
		{
			return null;
		}
	}

	/**
	 * Add data set
	 *
	 * @param label
	 * @param data
	 * @param dateTime
	 * @return the added data set
	 */
	public TICFrameStandardDataSet addDataSet(String label, String data, String dateTime)
	{
		TICFrameStandardDataSet dataSet = this.addDataSet(label, data);

		dataSet.setDateTime(dateTime);

		return dataSet;
	}

	/**
	 * Get string datetime
	 *
	 * @param label
	 * @return string datetime
	 */
	public String getDateTime(String label)
	{
		TICFrameStandardDataSet dataSet = (TICFrameStandardDataSet) this.getDataSet(label);
		if (dataSet != null)
		{
			return dataSet.getDateTime();
		}
		else
		{
			return null;
		}
	}

	/**
	 * Get date time as localDateTime
	 *
	 * @param label
	 * @return LocalDateTime
	 */
	public LocalDateTime getDateTimeAsLocalDateTime(String label)
	{
		TICFrameStandardDataSet dataSet = (TICFrameStandardDataSet) this.getDataSet(label);
		if (dataSet != null)
		{
			return dataSet.getDateTimeAsLocalDateTime();
		}
		else
		{
			return null;
		}
	}

}
