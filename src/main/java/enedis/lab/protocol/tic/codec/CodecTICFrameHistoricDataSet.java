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
import enedis.lab.protocol.tic.frame.TICFrameDataSet;
import enedis.lab.protocol.tic.frame.historic.TICFrameHistoricDataSet;
import enedis.lab.types.BytesArray;

/**
 * Codec TIC Frame Historic Data Set
 *
 */
public class CodecTICFrameHistoricDataSet implements Codec<TICFrameHistoricDataSet, byte[]>
{

	@Override
	public byte[] encode(TICFrameHistoricDataSet ticFrameHistoricDataSet) throws CodecException
	{
		BytesArray dataSet = new BytesArray();

		if ((ticFrameHistoricDataSet.getLabel() != null) && (ticFrameHistoricDataSet.getData() != null))
		{
			dataSet.add(TICFrameDataSet.BEGINNING_PATTERN);
			dataSet.addAll(ticFrameHistoricDataSet.getLabel().getBytes());

			dataSet.add(TICFrameHistoricDataSet.SEPARATOR);
			dataSet.addAll(ticFrameHistoricDataSet.getData().getBytes());

			dataSet.add(TICFrameHistoricDataSet.SEPARATOR);

			if (!ticFrameHistoricDataSet.isValid())
			{
				throw new CodecException("Invalid Checksum value");
			}
			dataSet.add(ticFrameHistoricDataSet.getChecksum());
			dataSet.add(TICFrameDataSet.END_PATTERN);
		}

		return dataSet.getBytes();
	}

	@Override
	public TICFrameHistoricDataSet decode(byte[] bytes) throws CodecException
	{
		BytesArray bytesArray = new BytesArray(bytes);
		TICFrameHistoricDataSet dataSet = null;

		if (this.isStartStopDelimiterPresent(bytesArray))
		{
			this.removeStartStopDelimiter(bytesArray);

		}

		List<BytesArray> parts = this.splitFrame(bytesArray);

		// Structure "LABEL/DATA/CHECKSUM" :
		if (this.isStructureLabelDataChecksum(parts))
		{

			if (this.isChecksumInOneByte(parts.get(2)))

			{
				dataSet = new TICFrameHistoricDataSet();
				dataSet = this.createDataSetLabelDataChecksum(parts, dataSet);

			}
			else
			{
				throw new CodecException("Invalid Checksum value : The part to the Checksum must occupy one and only one byte\r\n" + "The Label part must not exceed 8 bytes ");
			}
		}
		else
		{
			throw new CodecException("Invalid format of TICFrameHistoricDataSet");
		}

		if (dataSet.isValid() == false)
		{
			throw new CodecException("Invalid Checksum value - Historic");
		}

		return dataSet;
	}

	private boolean isStartStopDelimiterPresent(BytesArray bytes)
	{
		return bytes.startsWith(TICFrameDataSet.BEGINNING_PATTERN) && bytes.endsWith(TICFrameDataSet.END_PATTERN);
	}

	private void removeStartStopDelimiter(BytesArray bytes)

	{
		bytes.remove(0);
		bytes.remove(bytes.size() - 1);
	}

	private TICFrameHistoricDataSet createDataSetLabelDataChecksum(List<BytesArray> parts, TICFrameHistoricDataSet dataSet)
	{

		dataSet.setup(parts.get(0).getBytes(), parts.get(1).getBytes());
		dataSet.setChecksum(parts.get(2).get(0));
		return dataSet;
	}

	private boolean isChecksumInOneByte(BytesArray part)
	{
		return part.size() == 1;
	}

	private boolean isStructureLabelDataChecksum(List<BytesArray> parts)
	{
		return parts.size() == 3;
	}

	private List<BytesArray> splitFrame(BytesArray bytesArray) throws CodecException
	{
		List<BytesArray> parts;

		if (bytesArray.size() < 5)
		{
			throw new CodecException("Not enough bytes in TICFrameHistoricDataSet");
		}
		if (bytesArray.get(bytesArray.size() - 1) == TICFrameHistoricDataSet.SEPARATOR)
		{
			BytesArray subList = bytesArray.subList(0, bytesArray.size() - 2);
			parts = subList.split(TICFrameHistoricDataSet.SEPARATOR);
			if (parts.size() < 1)
			{
				throw new CodecException("Invalid format of TICFrameHistoricDataSet");
			}
			BytesArray checksum = parts.get(parts.size() - 1);
			checksum.addAll(new byte[] { TICFrameHistoricDataSet.SEPARATOR });
		}
		else
		{
			parts = bytesArray.split(TICFrameHistoricDataSet.SEPARATOR);
		}

		return parts;
	}

}
