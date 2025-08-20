// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
// 
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.protocol.tic.codec;

import enedis.lab.codec.Codec;
import enedis.lab.codec.CodecException;
import enedis.lab.protocol.tic.TICMode;
import enedis.lab.protocol.tic.frame.TICFrame;
import enedis.lab.protocol.tic.frame.historic.TICFrameHistoric;
import enedis.lab.protocol.tic.frame.standard.TICException;
import enedis.lab.protocol.tic.frame.standard.TICFrameStandard;
import enedis.lab.types.BytesArray;

/**
 * Codec TIC
 */
public class TICCodec implements Codec<TICFrame, byte[]>
{
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// CONSTANTS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private static final TICMode	DEFAULT_MODE	= TICMode.STANDARD;

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

	private BytesArray				Buffer;
	private TICMode 				mode 			= TICMode.UNKNOWN;
	private TICMode					currentMode		= TICMode.UNKNOWN;

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// CONSTRUCTORS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Default constructor
	 */
	public TICCodec()
	{
		this.mode = TICMode.UNKNOWN;
		this.currentMode = TICMode.UNKNOWN;
		this.Buffer = new BytesArray();
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// INTERFACE
	/// Codec
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public TICFrame decode(byte[] newData) throws CodecException
	{
		CodecTICFrameStandard codecStandard = new CodecTICFrameStandard();
		CodecTICFrameHistoric codecHistoric = new CodecTICFrameHistoric();
		TICFrameStandard ticFrameStandard = null;
		TICFrameHistoric ticFrameHistoric = null;

		TICFrame ticFrame = null;
		TICMode currentTICMode = null;

		try
		{
			switch (this.currentMode)
			{
				case STANDARD:
				{
					ticFrameStandard = codecStandard.decode(newData);
					ticFrame = ticFrameStandard;
					break;
				}
				case HISTORIC:
				{
					ticFrameHistoric = codecHistoric.decode(newData);
					ticFrame = ticFrameHistoric;
					break;
				}

				case AUTO:
				{
					try
					{
						currentTICMode = TICMode.findModeFromFrameBuffer(newData);
					}
					catch (TICException exception)
					{
						throw new CodecException("can't determinated TIC Mode");
					}

					if (currentTICMode == TICMode.STANDARD)
					{
						ticFrameStandard = codecStandard.decode(newData);
						ticFrame = ticFrameStandard;
						break;
					}
					else if (currentTICMode == TICMode.HISTORIC)
					{
						ticFrameHistoric = codecHistoric.decode(newData);
						ticFrame = ticFrameHistoric;
					}
					else
					{
						throw new CodecException("can't decode TIC, unable to find TIC Modem");
					}

				}

				default:
				{
					/**/
				}
			}
		}
		catch (CodecException exception)
		{
			throw new CodecException(exception.getMessage(), exception.getData());
		}
		return ticFrame;
	}

	@Override
	public byte[] encode(TICFrame ticFrame) throws CodecException
	{
		CodecTICFrameStandard codecStandard = new CodecTICFrameStandard();
		CodecTICFrameHistoric codecHistoric = new CodecTICFrameHistoric();

		byte[] bytesBuffer = new byte[0];

		try
		{
			switch (ticFrame.getMode())
			{
				case STANDARD:
				{
					bytesBuffer = codecStandard.encode((TICFrameStandard) ticFrame);
					break;
				}
				case HISTORIC:
				{
					bytesBuffer = codecHistoric.encode((TICFrameHistoric) ticFrame);
					break;
				}
				default:
				{
					bytesBuffer = codecStandard.encode((TICFrameStandard) ticFrame);
					break;
				}
			}
		}
		catch (CodecException exception)
		{
			throw new CodecException("Can't encode TICFrame" + exception.getMessage());
		}
		return bytesBuffer;
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// PUBLIC METHODS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Reset buffer
	 */
	public void reset()
	{
		this.Buffer.clear();
	}

	/**
	 * Append data to buffer
	 *
	 * @param data
	 */
	public void append(byte[] data)
	{
		this.Buffer.addAll(data);
	}

	/**
	 * Append data to buffer
	 *
	 * @param data
	 */
	public void append(byte data)
	{
		this.Buffer.add(data);
	}

	/**
	 * Append data to buffer
	 *
	 * @param data
	 */
	public void append(BytesArray data)
	{
		this.Buffer.addAll(data.getBytes());
	}

	/**
	 * Get mode
	 *
	 * @return mode
	 */
	public TICMode getMode()
	{
		return this.mode;
	}

	/**
	 * Set mode
	 *
	 * @param mode
	 */
	public void setMode(TICMode mode)
	{
		if (this.mode != mode)
		{
			this.mode = mode;

			if (TICMode.AUTO != mode)
			{
				this.setCurrentMode(mode);
			}

			else
			{
				this.setCurrentMode(DEFAULT_MODE);
			}
		}
	}

	/**
	 * Get current mode
	 *
	 * @return current mode
	 */
	public TICMode getCurrentMode()
	{
		return this.currentMode;
	}

	/**
	 * Set current mode
	 *
	 * @param currentMode
	 */
	public void setCurrentMode(TICMode currentMode)
	{
		if (currentMode != this.currentMode)
		{
			this.currentMode = currentMode;
		}
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

}
