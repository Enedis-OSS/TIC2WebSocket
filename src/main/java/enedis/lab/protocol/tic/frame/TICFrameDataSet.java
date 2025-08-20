// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
// 
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.protocol.tic.frame;

import java.util.Objects;

import org.json.JSONObject;

import enedis.lab.types.BytesArray;

/**
 * TIC frame data set
 */
public abstract class TICFrameDataSet
{
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// CONSTANTS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** Beginning pattern */
	public static final byte	BEGINNING_PATTERN	= 0x0A;	// LF
	/** End pattern */
	public static final byte	END_PATTERN			= 0x0D;	// CR

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

	protected BytesArray		label				= null;
	protected BytesArray		data				= null;
	protected byte				checksum;

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// CONSTRUCTORS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Default constructor
	 */
	public TICFrameDataSet()
	{
		this.init();
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// INTERFACE
	/// Object
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public int hashCode()
	{
		return Objects.hash(this.checksum, this.data, this.label);
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
		TICFrameDataSet other = (TICFrameDataSet) obj;
		return this.checksum == other.checksum && Objects.equals(this.data, other.data) && Objects.equals(this.label, other.label);
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// PUBLIC METHODS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Setup from string label and data
	 *
	 * @param label
	 * @param data
	 */
	public void setup(String label, String data)
	{
		this.setup(label.getBytes(), data.getBytes());
	}

	/**
	 * Setup from byte[] label and data
	 *
	 * @param label
	 * @param data
	 */
	public void setup(byte[] label, byte[] data)
	{
		this.label = new BytesArray(label);
		this.data = new BytesArray(data);
	}

	/**
	 * Get label
	 *
	 * @return label
	 */
	public String getLabel()
	{
		return new String(this.label.getBytes());
	}

	/**
	 * Set the Label
	 *
	 * @param label
	 */
	public void setLabel(String label)
	{
		this.setLabel(label.getBytes());
	}

	/**
	 * Set the Label
	 *
	 * @param label
	 */
	public void setLabel(byte[] label)
	{
		this.setLabel(new BytesArray(label));
	}

	/**
	 * Set the Label
	 *
	 * @param label
	 */
	public void setLabel(BytesArray label)
	{
		this.label = label;
		this.setChecksum();
	}

	/**
	 * Get data
	 *
	 * @return data
	 */
	public String getData()
	{
		return new String(this.data.getBytes());
	}

	/**
	 * Set data
	 *
	 * @param data
	 */
	public void setData(String data)
	{
		this.setData(data.getBytes());
	}

	/**
	 * Set data
	 *
	 * @param data
	 */
	public void setData(byte[] data)
	{
		this.setData(new BytesArray(data));
	}

	/**
	 * Set data
	 *
	 * @param data
	 */
	public void setData(BytesArray data)
	{
		this.data = data;
		this.setChecksum();
	}

	/**
	 * Get checksum
	 *
	 * @return checksum
	 */
	public byte getChecksum()
	{

		return this.checksum;
	}

	/**
	 * Compute and set the checksum (NB: Label and Data shall have been set)
	 *
	 * @return true if the operation succeeds, else, return false
	 */
	public boolean setChecksum()
	{
		Byte checksum = this.getConsistentChecksum();

		if (checksum != null)
		{
			this.checksum = checksum;
			return true;
		}

		else
		{
			return false;
		}
	}

	/**
	 * Set the checksum at a given value
	 *
	 * @param checksum
	 */
	public void setChecksum(byte checksum)
	{
		this.checksum = checksum;

	}

	/**
	 *
	 * @return true if fields are consistent with checksum
	 */
	public boolean isValid()
	{
		Byte consistentChecksum = this.getConsistentChecksum();

		if ((consistentChecksum != null) && (consistentChecksum.byteValue() == (this.checksum & (byte) 0xFF)))
		{
			return true;
		}

		else
		{
			return false;
		}
	}

	/**
	 * Get bytes
	 *
	 * @return bytes
	 */
	public abstract byte[] getBytes();

	/**
	 * Conver to JSON
	 *
	 * @return json object
	 */
	public abstract JSONObject toJSON();

	/**
	 * Conver to JSON
	 *
	 * @param option
	 * @return json object
	 */
	public abstract JSONObject toJSON(int option);

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// PROTECTED METHODS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Return the consistent checksum of the DataSet
	 *
	 * @return the consistent checksum of the DataSet
	 */
	protected abstract Byte getConsistentChecksum();

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// PRIVATE METHODS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private void init()
	{
		this.label = null;
		this.data = null;
		this.checksum = -1;
	}
}
