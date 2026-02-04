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
import enedis.lab.types.datadictionary.KeyDescriptor;
import enedis.lab.types.datadictionary.KeyDescriptorLocalDateTime;
import enedis.lab.types.datadictionary.KeyDescriptorNumber;
import enedis.lab.types.datadictionary.KeyDescriptorString;

/**
 * Response class
 *
 * Generated
 */
public abstract class Response extends Message
{
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// CONSTANTS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	protected static final String			KEY_DATE_TIME		= "dateTime";
	protected static final String			KEY_ERROR_CODE		= "errorCode";
	protected static final String			KEY_ERROR_MESSAGE	= "errorMessage";

	private static final MessageType		TYPE_ACCEPTED_VALUE	= MessageType.RESPONSE;

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

	private List<KeyDescriptor<?>>			keys				= new ArrayList<KeyDescriptor<?>>();

	protected KeyDescriptorLocalDateTime	kDateTime;
	protected KeyDescriptorNumber			kErrorCode;
	protected KeyDescriptorString			kErrorMessage;

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// CONSTRUCTORS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	protected Response()
	{
		super();
		this.loadKeyDescriptors();

		this.kType.setAcceptedValues(TYPE_ACCEPTED_VALUE);
	}

	/**
	 * Constructor using map
	 *
	 * @param map
	 * @throws DataDictionaryException
	 */
	public Response(Map<String, Object> map) throws DataDictionaryException
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
	public Response(DataDictionary other) throws DataDictionaryException
	{
		this();
		this.copy(other);
	}

	/**
	 * Constructor setting parameters to specific values
	 *
	 * @param type
	 * @param name
	 * @param dateTime
	 * @param errorCode
	 * @param errorMessage
	 * @throws DataDictionaryException
	 */
	public Response(MessageType type, String name, LocalDateTime dateTime, Number errorCode, String errorMessage) throws DataDictionaryException
	{
		this();

		this.setType(type);
		this.setName(name);
		this.setDateTime(dateTime);
		this.setErrorCode(errorCode);
		this.setErrorMessage(errorMessage);

		this.checkAndUpdate();
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// INTERFACE
	/// Message
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	protected void updateOptionalParameters() throws DataDictionaryException
	{
		if (!this.exists(KEY_TYPE))
		{
			this.setType(TYPE_ACCEPTED_VALUE);
		}
		super.updateOptionalParameters();
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// PUBLIC METHODS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Get date time
	 *
	 * @return the date time
	 */
	public LocalDateTime getDateTime()
	{
		return (LocalDateTime) this.data.get(KEY_DATE_TIME);
	}

	/**
	 * Get error code
	 *
	 * @return the error code
	 */
	public Number getErrorCode()
	{
		return (Number) this.data.get(KEY_ERROR_CODE);
	}

	/**
	 * Get error message
	 *
	 * @return the error message
	 */
	public String getErrorMessage()
	{
		return (String) this.data.get(KEY_ERROR_MESSAGE);
	}

	/**
	 * Set date time
	 *
	 * @param dateTime
	 * @throws DataDictionaryException
	 */
	public void setDateTime(LocalDateTime dateTime) throws DataDictionaryException
	{
		this.setDateTime((Object) dateTime);
	}

	/**
	 * Set error code
	 *
	 * @param errorCode
	 * @throws DataDictionaryException
	 */
	public void setErrorCode(Number errorCode) throws DataDictionaryException
	{
		this.setErrorCode((Object) errorCode);
	}

	/**
	 * Set error message
	 *
	 * @param errorMessage
	 * @throws DataDictionaryException
	 */
	public void setErrorMessage(String errorMessage) throws DataDictionaryException
	{
		this.setErrorMessage((Object) errorMessage);
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// PROTECTED METHODS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	protected void setDateTime(Object dateTime) throws DataDictionaryException
	{
		this.data.put(KEY_DATE_TIME, this.kDateTime.convert(dateTime));
	}

	protected void setErrorCode(Object errorCode) throws DataDictionaryException
	{
		this.data.put(KEY_ERROR_CODE, this.kErrorCode.convert(errorCode));
	}

	protected void setErrorMessage(Object errorMessage) throws DataDictionaryException
	{
		this.data.put(KEY_ERROR_MESSAGE, this.kErrorMessage.convert(errorMessage));
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// PRIVATE METHODS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void loadKeyDescriptors()
	{
		try
		{
			this.kDateTime = new KeyDescriptorLocalDateTime(KEY_DATE_TIME, true);
			this.keys.add(this.kDateTime);

			this.kErrorCode = new KeyDescriptorNumber(KEY_ERROR_CODE, true);
			this.keys.add(this.kErrorCode);

			this.kErrorMessage = new KeyDescriptorString(KEY_ERROR_MESSAGE, false, true);
			this.keys.add(this.kErrorMessage);

			this.addAllKeyDescriptor(this.keys);
		}
		catch (DataDictionaryException e)
		{
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}
