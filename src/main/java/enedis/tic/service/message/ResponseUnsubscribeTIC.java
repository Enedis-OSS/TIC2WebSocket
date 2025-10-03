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
import enedis.lab.util.message.Response;

/**
 * ResponseUnsubscribeTIC class
 * 
 * Generated
 */
public class ResponseUnsubscribeTIC extends Response
{
	/** Message name */
	public static final String		NAME	= "UnsubscribeTIC";

	private List<KeyDescriptor<?>>	keys				= new ArrayList<KeyDescriptor<?>>();

	protected ResponseUnsubscribeTIC()
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
	public ResponseUnsubscribeTIC(Map<String, Object> map) throws DataDictionaryException
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
	public ResponseUnsubscribeTIC(DataDictionary other) throws DataDictionaryException
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
	 * @throws DataDictionaryException
	 */
	public ResponseUnsubscribeTIC(LocalDateTime dateTime, Number errorCode, String errorMessage) throws DataDictionaryException
	{
		this();

		this.setDateTime(dateTime);
		this.setErrorCode(errorCode);
		this.setErrorMessage(errorMessage);

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

	private void loadKeyDescriptors()
	{
		try
		{

			this.addAllKeyDescriptor(this.keys);
		}
		catch (DataDictionaryException e)
		{
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}
