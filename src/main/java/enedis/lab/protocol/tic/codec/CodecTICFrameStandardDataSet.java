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
import enedis.lab.protocol.tic.frame.standard.TICFrameStandardDataSet;
import enedis.lab.types.BytesArray;

/**
 * Codec TIC Frame Standard Data Set
 *
 */
public class CodecTICFrameStandardDataSet implements Codec<TICFrameStandardDataSet, byte[]>
{
	@Override
	public byte[] encode(TICFrameStandardDataSet ticFrameStandardDataSet) throws CodecException
	{
		BytesArray dataSet = new BytesArray();

		if ((null != ticFrameStandardDataSet.getLabel()) && (null != ticFrameStandardDataSet.getData()))
		{
			dataSet.add(TICFrameDataSet.BEGINNING_PATTERN);
			dataSet.addAll(ticFrameStandardDataSet.getLabel().getBytes());

			if (ticFrameStandardDataSet.checkDateTime() == true)
			{
				dataSet.add(TICFrameStandardDataSet.SEPARATOR);
				dataSet.addAll(ticFrameStandardDataSet.getDateTime().getBytes());
			}

			dataSet.add(TICFrameStandardDataSet.SEPARATOR);
			dataSet.addAll(ticFrameStandardDataSet.getData().getBytes());

			dataSet.add(TICFrameStandardDataSet.SEPARATOR);
			if (!ticFrameStandardDataSet.isValid())
			{
				throw new CodecException("Invalid Checksum value");
			}

			dataSet.add(ticFrameStandardDataSet.getChecksum());
			dataSet.add(TICFrameDataSet.END_PATTERN);
		}

		return dataSet.getBytes();
	}

	@Override
	public TICFrameStandardDataSet decode(byte[] bytes) throws CodecException
	{
		BytesArray bytesArray = new BytesArray(bytes);
		TICFrameStandardDataSet dataSet = null;

		if (this.isStartStopDelimiterPresent(bytesArray))
		{
			this.removeStartStopDelimiter(bytesArray);
		}

		List<BytesArray> parts = bytesArray.split(TICFrameStandardDataSet.SEPARATOR);

		// Structure "LABEL/DATA/CHECKSUM" :
		if (this.isStructureLabelDataChecksum(parts))
		{
			if (this.isChecksumInOneByte(parts.get(2)))
			{
				dataSet = new TICFrameStandardDataSet();
				dataSet = this.createDataSetLabelDataChecksum(parts, dataSet);
			}
			else
			{
				throw new CodecException("Invalid Checksum value : The part to the Checksum must occupy one and only one byte\r\n" + "The Label part must not exceed 8 bytes ");
			}
		}
		// Structure "LABEL/DATETIME/DATA/CHECKSUM" :
		else if (this.isStructureLabelDateTimeDataChecksum(parts))
		{
			if (this.isChecksumInOneByte(parts.get(3)))
			{
				dataSet = new TICFrameStandardDataSet();
				dataSet = this.createDataSetLabelDatetimeDataChecksum(parts, dataSet);
			}
			else
			{
				throw new CodecException("Invalid Checksum value : The part to the Checksum must occupy one and only one byte\r\n" + "The Label part must not exceed 8 bytes ");
			}
		}
		else
		{
			throw new CodecException("Invalid bytes for decode - Standard");
		}

		if (dataSet.isValid() == false)
		{
			throw new CodecException("Invalid Checksum value");
		}

		return dataSet;
	}

	private boolean isStructureLabelDateTimeDataChecksum(List<BytesArray> parts)
	{
		return parts.size() == 4;
	}

	private boolean isStructureLabelDataChecksum(List<BytesArray> parts)
	{
		return parts.size() == 3;
	}

	private boolean isChecksumInOneByte(BytesArray part)
	{
		return part.size() == 1;
	}

	private void removeStartStopDelimiter(BytesArray bytes)
	{
		bytes.remove(0);
		bytes.remove(bytes.size() - 1);
	}

	private boolean isStartStopDelimiterPresent(BytesArray bytes)
	{
		return bytes.startsWith(TICFrameDataSet.BEGINNING_PATTERN) && bytes.endsWith(TICFrameDataSet.END_PATTERN);
	}

	private TICFrameStandardDataSet createDataSetLabelDataChecksum(List<BytesArray> parts, TICFrameStandardDataSet dataSet)
	{
		dataSet.setup(parts.get(0).getBytes(), parts.get(1).getBytes());
		dataSet.setChecksum(parts.get(2).get(0));
		return dataSet;
	}

	private TICFrameStandardDataSet createDataSetLabelDatetimeDataChecksum(List<BytesArray> parts, TICFrameStandardDataSet dataSet)
	{
		dataSet.setup(parts.get(0).getBytes(), parts.get(2).getBytes(), parts.get(1).getBytes());
		dataSet.setChecksum(parts.get(3).get(0));
		return dataSet;
	}

}