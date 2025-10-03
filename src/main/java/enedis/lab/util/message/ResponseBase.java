// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
// 
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.util.message;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import enedis.lab.types.DataDictionary;
import enedis.lab.types.DataDictionaryException;
import enedis.lab.types.datadictionary.DataDictionaryBase;
import enedis.lab.types.datadictionary.KeyDescriptor;
import enedis.lab.types.datadictionary.KeyDescriptorDataDictionary;

/**
 * ResponseBase class
 * 
 * Generated
 */
public class ResponseBase extends Response
{
	protected static final String								KEY_DATA	= "data";

	private List<KeyDescriptor<?>>								keys		= new ArrayList<KeyDescriptor<?>>();

	protected KeyDescriptorDataDictionary<DataDictionaryBase>	kData;

	protected ResponseBase()
	{
		super();
		this.loadKeyDescriptors();

	}

	/**
	 * Constructor using map
	 *
	 * @param map
	 * @throws DataDictionaryException
	 */
	public ResponseBase(Map<String, Object> map) throws DataDictionaryException
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
	public ResponseBase(DataDictionary other) throws DataDictionaryException
	{
		this();
		this.copy(other);
	}

	/**
	 * Constructor setting parameters to specific values
	 *
	 * @param name
	 * @param dateTime
	 * @param errorCode
	 * @param errorMessage
	 * @param data
	 * @throws DataDictionaryException
	 */
	public ResponseBase(String name, LocalDateTime dateTime, Number errorCode, String errorMessage, DataDictionaryBase data) throws DataDictionaryException
	{
		this();

		this.setName(name);
		this.setDateTime(dateTime);
		this.setErrorCode(errorCode);
		this.setErrorMessage(errorMessage);
		this.setData(data);

		this.checkAndUpdate();
	}

	@Override
	protected void updateOptionalParameters() throws DataDictionaryException
	{
		super.updateOptionalParameters();
	}

	/**
	 * Get data
	 *
	 * @return the data
	 */
	public DataDictionaryBase getData()
	{
		return (DataDictionaryBase) this.data.get(KEY_DATA);
	}

	/**
	 * Set data
	 *
	 * @param data
	 * @throws DataDictionaryException
	 */
	public void setData(DataDictionaryBase data) throws DataDictionaryException
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
			this.kData = new KeyDescriptorDataDictionary<DataDictionaryBase>(KEY_DATA, false, DataDictionaryBase.class);
			this.keys.add(this.kData);

			this.addAllKeyDescriptor(this.keys);
		}
		catch (DataDictionaryException e)
		{
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}
