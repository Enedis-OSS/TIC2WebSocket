// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.tic.core;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import enedis.lab.protocol.tic.TICMode;
import enedis.lab.types.DataDictionary;
import enedis.lab.types.DataDictionaryException;
import enedis.lab.types.datadictionary.DataDictionaryBase;
import enedis.lab.types.datadictionary.KeyDescriptor;
import enedis.lab.types.datadictionary.KeyDescriptorDataDictionary;
import enedis.lab.types.datadictionary.KeyDescriptorEnum;
import enedis.lab.types.datadictionary.KeyDescriptorLocalDateTime;

/**
 * TICCoreFrame class
 *
 * Generated
 */
public class TICCoreFrame extends DataDictionaryBase
{
	protected static final String								KEY_IDENTIFIER			= "identifier";
	protected static final String								KEY_MODE				= "mode";
	protected static final String								KEY_CAPTURE_DATE_TIME	= "captureDateTime";
	protected static final String								KEY_CONTENT				= "content";

	private List<KeyDescriptor<?>>								keys					= new ArrayList<KeyDescriptor<?>>();

	protected KeyDescriptorDataDictionary<TICIdentifier>		kIdentifier;
	protected KeyDescriptorEnum<TICMode>						kMode;
	protected KeyDescriptorLocalDateTime						kCaptureDateTime;
	protected KeyDescriptorDataDictionary<DataDictionaryBase>	kContent;

	protected TICCoreFrame()
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
	public TICCoreFrame(Map<String, Object> map) throws DataDictionaryException
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
	public TICCoreFrame(DataDictionary other) throws DataDictionaryException
	{
		this();
		this.copy(other);
	}

	/**
	 * Constructor setting parameters to specific values
	 *
	 * @param identifier
	 * @param mode
	 * @param captureDateTime
	 * @param content
	 * @throws DataDictionaryException
	 */
	public TICCoreFrame(TICIdentifier identifier, TICMode mode, LocalDateTime captureDateTime, DataDictionaryBase content) throws DataDictionaryException
	{
		this();

		this.setIdentifier(identifier);
		this.setMode(mode);
		this.setCaptureDateTime(captureDateTime);
		this.setContent(content);

		this.checkAndUpdate();
	}

	@Override
	protected void updateOptionalParameters() throws DataDictionaryException
	{
		super.updateOptionalParameters();
	}

	/**
	 * Get identifier
	 *
	 * @return the identifier
	 */
	public TICIdentifier getIdentifier()
	{
		return (TICIdentifier) this.data.get(KEY_IDENTIFIER);
	}

	/**
	 * Get mode
	 *
	 * @return the mode
	 */
	public TICMode getMode()
	{
		return (TICMode) this.data.get(KEY_MODE);
	}

	/**
	 * Get capture date time
	 *
	 * @return the capture date time
	 */
	public LocalDateTime getCaptureDateTime()
	{
		return (LocalDateTime) this.data.get(KEY_CAPTURE_DATE_TIME);
	}

	/**
	 * Get content
	 *
	 * @return the content
	 */
	public DataDictionaryBase getContent()
	{
		return (DataDictionaryBase) this.data.get(KEY_CONTENT);
	}

	/**
	 * Set identifier
	 *
	 * @param identifier
	 * @throws DataDictionaryException
	 */
	public void setIdentifier(TICIdentifier identifier) throws DataDictionaryException
	{
		this.setIdentifier((Object) identifier);
	}

	/**
	 * Set mode
	 *
	 * @param mode
	 * @throws DataDictionaryException
	 */
	public void setMode(TICMode mode) throws DataDictionaryException
	{
		this.setMode((Object) mode);
	}

	/**
	 * Set capture date time
	 *
	 * @param captureDateTime
	 * @throws DataDictionaryException
	 */
	public void setCaptureDateTime(LocalDateTime captureDateTime) throws DataDictionaryException
	{
		this.setCaptureDateTime((Object) captureDateTime);
	}

	/**
	 * Set content
	 *
	 * @param content
	 * @throws DataDictionaryException
	 */
	public void setContent(DataDictionaryBase content) throws DataDictionaryException
	{
		this.setContent((Object) content);
	}

	protected void setIdentifier(Object identifier) throws DataDictionaryException
	{
		this.data.put(KEY_IDENTIFIER, this.kIdentifier.convert(identifier));
	}

	protected void setMode(Object mode) throws DataDictionaryException
	{
		this.data.put(KEY_MODE, this.kMode.convert(mode));
	}

	protected void setCaptureDateTime(Object captureDateTime) throws DataDictionaryException
	{
		this.data.put(KEY_CAPTURE_DATE_TIME, this.kCaptureDateTime.convert(captureDateTime));
	}

	protected void setContent(Object content) throws DataDictionaryException
	{
		this.data.put(KEY_CONTENT, this.kContent.convert(content));
	}

	private void loadKeyDescriptors()
	{
		try
		{
			this.kIdentifier = new KeyDescriptorDataDictionary<TICIdentifier>(KEY_IDENTIFIER, true, TICIdentifier.class);
			this.keys.add(this.kIdentifier);

			this.kMode = new KeyDescriptorEnum<TICMode>(KEY_MODE, true, TICMode.class);
			this.keys.add(this.kMode);

			this.kCaptureDateTime = new KeyDescriptorLocalDateTime(KEY_CAPTURE_DATE_TIME, true);
			this.keys.add(this.kCaptureDateTime);

			this.kContent = new KeyDescriptorDataDictionary<DataDictionaryBase>(KEY_CONTENT, true, DataDictionaryBase.class);
			this.keys.add(this.kContent);

			this.addAllKeyDescriptor(this.keys);
		}
		catch (DataDictionaryException e)
		{
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}
