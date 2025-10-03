// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
// 
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.tic.service.message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import enedis.lab.types.DataDictionary;
import enedis.lab.types.DataDictionaryException;
import enedis.lab.types.datadictionary.KeyDescriptor;
import enedis.lab.types.datadictionary.KeyDescriptorList;
import enedis.lab.util.message.Request;
import enedis.tic.core.TICIdentifier;

/**
 * RequestSubscribeTIC class
 * 
 * Generated
 */
public class RequestSubscribeTIC extends Request
{
	protected static final String				KEY_DATA	= "data";

	/** Message name */
	public static final String					NAME		= "SubscribeTIC";

	private List<KeyDescriptor<?>>				keys		= new ArrayList<KeyDescriptor<?>>();

	protected KeyDescriptorList<TICIdentifier>	kData;

	protected RequestSubscribeTIC()
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
	public RequestSubscribeTIC(Map<String, Object> map) throws DataDictionaryException
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
	public RequestSubscribeTIC(DataDictionary other) throws DataDictionaryException
	{
		this();
		this.copy(other);
	}

	/**
	 * Constructor setting parameters to specific values
	 *
	 * @param data
	 * @throws DataDictionaryException
	 */
	public RequestSubscribeTIC(List<TICIdentifier> data) throws DataDictionaryException
	{
		this();

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
	@SuppressWarnings("unchecked")
	public List<TICIdentifier> getData()
	{
		return (List<TICIdentifier>) this.data.get(KEY_DATA);
	}

	/**
	 * Set data
	 *
	 * @param data
	 * @throws DataDictionaryException
	 */
	public void setData(List<TICIdentifier> data) throws DataDictionaryException
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
			this.kData = new KeyDescriptorList<TICIdentifier>(KEY_DATA, false, TICIdentifier.class);
			this.keys.add(this.kData);

			this.addAllKeyDescriptor(this.keys);
		}
		catch (DataDictionaryException e)
		{
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}
