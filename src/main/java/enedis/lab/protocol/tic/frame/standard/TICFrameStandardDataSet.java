// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
// 
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.protocol.tic.frame.standard;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.json.JSONObject;

import enedis.lab.protocol.tic.frame.TICFrame;
import enedis.lab.protocol.tic.frame.TICFrameDataSet;
import enedis.lab.types.BytesArray;

/**
 * TIC frame standard data set
 */
public class TICFrameStandardDataSet extends TICFrameDataSet
{
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// CONSTANTS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** Separator */
	public static final byte	SEPARATOR	= 0x09;	// HT

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// TYPES
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// STATIC METHODS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// ATTRIBUTES
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	protected BytesArray		dateTime	= null;

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// CONSTRUCTORS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Constructor
	 */
	public TICFrameStandardDataSet()
	{
		super();
		this.init();
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// INTERFACE
	/// TICFrameDataSet
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public Byte getConsistentChecksum()
	{
		Byte crc = 0;
		byte[] labelByte;
		byte[] dataByte;
		byte[] dateByte;

		if ((this.label != null) && (this.data != null))
		{
			labelByte = this.label.getBytes();
			for (int i = 0; i < labelByte.length; i++)
			{
				crc = this.computeUpdate(crc, labelByte[i]);
			}

			crc = this.computeUpdate(crc, SEPARATOR);

			if (this.dateTime != null)
			{
				dateByte = this.dateTime.getBytes();
				for (int i = 0; i < dateByte.length; i++)
				{
					crc = this.computeUpdate(crc, dateByte[i]);
				}

				crc = this.computeUpdate(crc, SEPARATOR);
			}

			dataByte = this.data.getBytes();

			for (int i = 0; i < dataByte.length; i++)
			{
				crc = this.computeUpdate(crc, dataByte[i]);
			}
			crc = this.computeUpdate(crc, SEPARATOR);

			return this.computeEnd(crc);
		}
		else
		{
			return null;
		}

	}

	/**
	 * Compute update
	 *
	 * @param crc
	 * @param octet
	 * @return Byte crc compute after Update
	 */
	public Byte computeUpdate(Byte crc, byte octet)
	{
		return (byte) (crc + (octet & 0xff));
	}

	/**
	 * Compute end
	 *
	 * @param crc
	 * @return Byte crc compute after End
	 */
	public Byte computeEnd(Byte crc)
	{
		return (byte) ((crc & 0x3F) + 0x20);
	}

	@Override
	public byte[] getBytes()
	{
		BytesArray dataSet = new BytesArray();

		if ((this.label != null) && (this.data != null))
		{
			dataSet.add(BEGINNING_PATTERN);
			dataSet.addAll(this.label.getBytes());

			if (null != this.dateTime)
			{
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

	@Override
	public JSONObject toJSON()
	{
		return this.toJSON(TICFrame.TRIMMED);
	}

	@Override
	public JSONObject toJSON(int option)
	{
		JSONObject jsonObject = new JSONObject();

		if ((option & TICFrame.NOCHECKSUM) > 0)
		{
			if ((this.dateTime == null) || ((option & TICFrame.NODATETIME) > 0))
			{
				jsonObject.put(new String(this.label.getBytes()), new String(this.data.getBytes()));
			}

			else
			{
				JSONObject content = new JSONObject();
				content.put(TICFrame.KEY_DATA, new String(this.data.getBytes()));
				content.put(TICFrame.KEY_DATETIME, new String(this.dateTime.getBytes()));
				jsonObject.put(new String(this.label.getBytes()), content);
			}
		}

		else
		{
			if ((this.dateTime == null) || ((option & TICFrame.NODATETIME) > 0))
			{
				JSONObject content = new JSONObject();
				content.put(TICFrame.KEY_DATA, new String(this.data.getBytes()));
				content.put(TICFrame.KEY_CHECKSUM, this.checksum);
				jsonObject.put(new String(this.label.getBytes()), content);
			}

			else
			{
				JSONObject content = new JSONObject();
				content.put(TICFrame.KEY_DATA, new String(this.data.getBytes()));
				content.put(TICFrame.KEY_DATETIME, new String(this.dateTime.getBytes()));
				content.put(TICFrame.KEY_CHECKSUM, this.checksum);
				jsonObject.put(new String(this.label.getBytes()), content);
			}
		}

		return jsonObject;
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// PUBLIC METHODS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Setup
	 *
	 * @param label
	 * @param data
	 * @param dateTime
	 */
	public void setup(String label, String data, String dateTime)
	{
		this.setup(label.getBytes(), data.getBytes(), dateTime.getBytes());
	}

	/**
	 * Setup
	 *
	 * @param label
	 * @param data
	 * @param dateTime
	 */
	public void setup(byte[] label, byte[] data, byte[] dateTime)
	{
		super.setup(label, data);
		this.dateTime = new BytesArray(dateTime);
	}

	/**
	 * Get datetime
	 *
	 * @return datetime
	 */
	public String getDateTime()
	{
		return new String(this.dateTime.getBytes());
	}

	/**
	 * Check datetime
	 *
	 * @return result
	 */
	public Boolean checkDateTime()
	{
		if (this.dateTime == null)
		{
			return false;
		}
		return true;
	}

	/**
	 * Get datetime as local datetime
	 *
	 * @return datetime as local datetime
	 */
	// DATE H190730100158 yyMMddHHmmss
	public LocalDateTime getDateTimeAsLocalDateTime()
	{
		String strDateTime = this.getDateTime();
		LocalDateTime localDateTime = null;

		if (strDateTime != null && strDateTime.length() == 13)
		{
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
			try
			{
				localDateTime = LocalDateTime.parse(strDateTime.substring(1), formatter);
			}
			catch (DateTimeParseException e)
			{
				localDateTime = null;
			}
		}

		return localDateTime;
	}

	/**
	 * Set date time
	 *
	 * @param dateTime
	 */
	public void setDateTime(String dateTime)
	{
		this.setDateTime(dateTime.getBytes());
	}

	/**
	 * Set datetime
	 *
	 * @param dateTime
	 */
	public void setDateTime(byte[] dateTime)
	{
		this.setDateTime(new BytesArray(dateTime));
	}

	/**
	 * Set date time
	 *
	 * @param dateTime
	 */
	public void setDateTime(BytesArray dateTime)
	{
		this.dateTime = dateTime;
		this.setChecksum();
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// PROTECTED METHODS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// PRIVATE METHODS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void init()
	{
		this.dateTime = null;
	}
}
