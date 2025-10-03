// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
// 
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.protocol.tic.codec;

import java.util.List;

import enedis.lab.codec.Codec;
import enedis.lab.codec.CodecException;
import enedis.lab.protocol.tic.frame.TICFrame;
import enedis.lab.protocol.tic.frame.TICFrameDataSet;
import enedis.lab.protocol.tic.frame.historic.TICFrameHistoric;
import enedis.lab.protocol.tic.frame.historic.TICFrameHistoricDataSet;
import enedis.lab.types.BytesArray;

/**
 * Codec for TIC Historic frames.
 * 
 * This codec handles the encoding and decoding of TIC Historic frames, which are used
 * in the French electricity meter communication protocol. Historic frames contain
 * historical consumption data and are part of the TIC protocol specification.
 * 
 * The codec processes byte arrays containing TIC Historic frame data and converts them
 * to TICFrameHistoric objects, and vice versa. It validates frame structure, handles
 * data set parsing, and ensures proper checksum validation.
 * 
 * @author Enedis Smarties team
 */
public class CodecTICFrameHistoric implements Codec<TICFrameHistoric, byte[]>
{

	/**
	 * Separator character used in TIC Historic frames.
	 * This is the space character (0x20) used to separate different parts
	 * of the frame structure in the TIC protocol.
	 */
	public static final byte SEPARATOR = 0x20; // SP


	/**
	 * Decodes a byte array containing TIC Historic frame data into a TICFrameHistoric object.
	 * 
	 * This method validates the frame structure by checking for proper beginning and end patterns,
	 * extracts individual data sets from the frame, and decodes each data set using the
	 * CodecTICFrameHistoricDataSet codec. If any data set fails to decode, the error is
	 * collected and thrown as a CodecException.
	 * 
	 * @param bytesBuffer the byte array containing the TIC Historic frame data
	 * @return a TICFrameHistoric object containing the decoded frame data
	 * @throws CodecException if the frame structure is invalid or if any data set fails to decode
	 */
	@Override
	public TICFrameHistoric decode(byte[] bytesBuffer) throws CodecException
	{
		String errorMessage = "";
		BytesArray rawDataSet = null;
		TICFrameHistoric ticFrame = null;
		CodecTICFrameHistoricDataSet codecTICFrameHistoricDataSet = new CodecTICFrameHistoricDataSet();

		BytesArray bytesArray = new BytesArray(bytesBuffer);

		if ((true == bytesArray.startsWith(TICFrame.BEGINNING_PATTERN)) && (true == bytesArray.endsWith(TICFrame.END_PATTERN)) && (bytesArray.contains(TICFrame.EOT) == false))
		{
			bytesArray.remove(0);
			bytesArray.remove(bytesArray.size() - 1);

			ticFrame = new TICFrameHistoric();

			List<BytesArray> datasetList = bytesArray.slice(TICFrameDataSet.BEGINNING_PATTERN, TICFrameDataSet.END_PATTERN, BytesArray.CONTIGUOUS);

			if (datasetList.isEmpty() == false)
			{
				for (int i = 0; i < datasetList.size(); i++)
				{
					TICFrameHistoricDataSet dataSet = null;
					rawDataSet = datasetList.get(i);
					byte[] rawDataSetByte = rawDataSet.getBytes();

					try
					{
						dataSet = codecTICFrameHistoricDataSet.decode(rawDataSetByte);
						ticFrame.addDataSet(dataSet);
					}
					catch (CodecException exception)
					{
						errorMessage += exception.getMessage() + " : " + new String(rawDataSet.getBytes()) + "\n";
					}
				}
			}
		}

		if (errorMessage.isEmpty())
		{
			return ticFrame;
		}
		else
		{
			throw new CodecException(errorMessage, ticFrame);
		}

	}

	/**
	 * Encodes a TICFrameHistoric object into a byte array representing a TIC Historic frame.
	 * 
	 * This method takes a TICFrameHistoric object and converts it into the byte representation
	 * of a TIC Historic frame. It processes each data set in the frame, encodes them using the
	 * CodecTICFrameHistoricDataSet codec, and wraps the result with proper frame delimiters.
	 * 
	 * @param ticFrameHistoric the TICFrameHistoric object to encode
	 * @return a byte array containing the encoded TIC Historic frame
	 * @throws CodecException if the frame is null, empty, or if any data set fails to encode
	 */
	@Override
	public byte[] encode(TICFrameHistoric ticFrameHistoric) throws CodecException
	{
		String errorMessage = "";
		CodecTICFrameHistoricDataSet codec = new CodecTICFrameHistoricDataSet();
		BytesArray dataSet = new BytesArray();

		List<TICFrameDataSet> ticFrameHistoricList = ticFrameHistoric.getDataSetList();

		if (ticFrameHistoric != null && !ticFrameHistoricList.isEmpty())
		{
			List<TICFrameDataSet> groups = ticFrameHistoric.getDataSetList();
			dataSet.add(TICFrame.BEGINNING_PATTERN);
			for (int i = 0; i < groups.size(); i++)
			{
				try
				{
					byte[] buff = codec.encode((TICFrameHistoricDataSet) groups.get(i));
					dataSet.addAll(buff);
				}
				catch (CodecException e)
				{
					errorMessage += e.getMessage() + " : " + new String(ticFrameHistoric.getBytes()) + "\n";
				}
			}
			dataSet.add(TICFrame.END_PATTERN);
		}

		else
		{
			return null;
		}

		if (errorMessage.isEmpty())
		{
			return dataSet.getBytes();
		}
		else
		{
			throw new CodecException(errorMessage, dataSet.getBytes());
		}
	}


}