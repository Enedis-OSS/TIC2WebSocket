// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.tic.service.message;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import enedis.lab.types.DataDictionary;
import enedis.lab.types.DataDictionaryException;
import enedis.lab.types.datadictionary.KeyDescriptor;
import enedis.lab.types.datadictionary.KeyDescriptorDataDictionary;
import enedis.lab.util.message.Response;
import enedis.tic.core.TICCoreFrame;

/**
 * ResponseReadTIC class
 * 
 * Generated
 */
public class ResponseReadTIC extends Response
{
	protected static final String						KEY_DATA	= "data";

	/** Message name */
	public static final String							NAME		= "ReadTIC";

	private List<KeyDescriptor<?>>						keys		= new ArrayList<KeyDescriptor<?>>();

	protected KeyDescriptorDataDictionary<TICCoreFrame>	kData;

	protected ResponseReadTIC()
	{
		super();
		this.loadKeyDescriptors();

		this.kName.setAcceptedValues(NAME);
	}

	/**
	 * Constructor using map
	 *
	 * @param map
	 * @throws DataDictionaryException
	 */
	public ResponseReadTIC(Map<String, Object> map) throws DataDictionaryException
	{
		this();
		this.copy(fromMap(map));
	}

	/**
	 * Constructor using datadictionary
	 *
	 * @param other
	 * @throws DataDictionaryException
	 */
	public ResponseReadTIC(DataDictionary other) throws DataDictionaryException
	{
		this();
		this.copy(other);
	}

	/**
	 * Constructor setting parameters to specific values
	 *
	 * @param dateTime
	 * @param errorCode
	 * @param errorMessage
	 * @param data
	 * @throws DataDictionaryException
	 */
	public ResponseReadTIC(LocalDateTime dateTime, Number errorCode, String errorMessage, TICCoreFrame data) throws DataDictionaryException
	{
		this();

		this.setDateTime(dateTime);
		this.setErrorCode(errorCode);
		this.setErrorMessage(errorMessage);
		this.setData(data);

		this.checkAndUpdate();
	}

	@Override
	protected void updateOptionalParameters() throws DataDictionaryException
	{
		if (!this.exists(KEY_NAME))
		{
			this.setName(NAME);
		}
		super.updateOptionalParameters();
	}

	/**
	 * Get data
	 *
	 * @return the data
	 */
	public TICCoreFrame getData()
	{
		return (TICCoreFrame) this.data.get(KEY_DATA);
	}

	/**
	 * Set data
	 *
	 * @param data
	 * @throws DataDictionaryException
	 */
	public void setData(TICCoreFrame data) throws DataDictionaryException
	{
		this.setData((Object) data);
	}

	protected void setData(Object data) throws DataDictionaryException
	{
		this.data.put(KEY_DATA, this.kData.convert(data));
	}

	private void loadKeyDescriptors()
	{
		try
		{
			this.kData = new KeyDescriptorDataDictionary<TICCoreFrame>(KEY_DATA, false, TICCoreFrame.class);
			this.keys.add(this.kData);

			this.addAllKeyDescriptor(this.keys);
		}
		catch (DataDictionaryException e)
		{
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}
