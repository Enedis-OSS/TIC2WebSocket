// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
// 
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.io.serialport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import enedis.lab.types.DataDictionary;
import enedis.lab.types.DataDictionaryException;
import enedis.lab.types.datadictionary.DataDictionaryBase;
import enedis.lab.types.datadictionary.KeyDescriptor;
import enedis.lab.types.datadictionary.KeyDescriptorNumberMinMax;
import enedis.lab.types.datadictionary.KeyDescriptorString;

/**
 * Class used to get the following serial port informations:<br>
 * - port unique identifier<br>
 * - port name used to open serial port<br>
 * - port description<br>
 * - USB device product identifier<br>
 * - USB device vendor identifier<br>
 * - USB device product name<br>
 * - USB device manufacturer<br>
 * - USB device serial number
 */
public class SerialPortDescriptor extends DataDictionaryBase
{
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// CONSTANTS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	protected static final String		KEY_PORT_ID			= "portId";
	protected static final String		KEY_PORT_NAME		= "portName";
	protected static final String		KEY_DESCRIPTION		= "description";
	protected static final String		KEY_PRODUCT_ID		= "productId";
	protected static final String		KEY_VENDOR_ID		= "vendorId";
	protected static final String		KEY_PRODUCT_NAME	= "productName";
	protected static final String		KEY_MANUFACTURER	= "manufacturer";
	protected static final String		KEY_SERIAL_NUMBER	= "serialNumber";

	private static final Number			PRODUCT_ID_MIN		= 0;
	private static final Number			PRODUCT_ID_MAX		= 65535;
	private static final Number			VENDOR_ID_MIN		= 0;
	private static final Number			VENDOR_ID_MAX		= 65535;

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

	private List<KeyDescriptor<?>>		keys				= new ArrayList<KeyDescriptor<?>>();

	protected KeyDescriptorString		kPortId;
	protected KeyDescriptorString		kPortName;
	protected KeyDescriptorString		kDescription;
	protected KeyDescriptorNumberMinMax	kProductId;
	protected KeyDescriptorNumberMinMax	kVendorId;
	protected KeyDescriptorString		kProductName;
	protected KeyDescriptorString		kManufacturer;
	protected KeyDescriptorString		kSerialNumber;

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// CONSTRUCTORS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Empty constructor
	 */
	public SerialPortDescriptor()
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
	public SerialPortDescriptor(Map<String, Object> map) throws DataDictionaryException
	{
		this();
		this.copy(fromMap(map));
	}

	/**
	 * Constructor using data dictionary
	 *
	 * @param other
	 * @throws DataDictionaryException
	 */
	public SerialPortDescriptor(DataDictionary other) throws DataDictionaryException
	{
		this();
		this.copy(other);
	}

	/**
	 * Constructor setting parameters to specific values
	 *
	 * @param portId
	 *            the port unique identifier
	 * @param portName
	 *            the port name used to open serial port
	 * @param description
	 *            the port description
	 * @param productId
	 *            the USB device product identifier
	 * @param vendorId
	 *            the USB device vendor identifier
	 * @param productName
	 *            the USB device product name
	 * @param manufacturer
	 *            the USB device manufacturer
	 * @param serialNumber
	 *            the USB device serial number
	 * @throws DataDictionaryException
	 *             if any parameters is invalid
	 */
	public SerialPortDescriptor(String portId, String portName, String description, Number productId, Number vendorId, String productName, String manufacturer, String serialNumber)
			throws DataDictionaryException
	{
		this();

		this.setPortId(portId);
		this.setPortName(portName);
		this.setDescription(description);
		this.setProductId(productId);
		this.setVendorId(vendorId);
		this.setProductName(productName);
		this.setManufacturer(manufacturer);
		this.setSerialNumber(serialNumber);

		this.checkAndUpdate();
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// INTERFACE
	/// DataDictionaryBase
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// PUBLIC METHODS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Get port id
	 *
	 * @return the port unique identifier
	 */
	public String getPortId()
	{
		return (String) this.data.get(KEY_PORT_ID);
	}

	/**
	 * Get port name
	 *
	 * @return the port name used to open serial port
	 */
	public String getPortName()
	{
		return (String) this.data.get(KEY_PORT_NAME);
	}

	/**
	 * Get description
	 *
	 * @return the port description
	 */
	public String getDescription()
	{
		return (String) this.data.get(KEY_DESCRIPTION);
	}

	/**
	 * Get product id
	 *
	 * @return the USB device product identifier
	 */
	public Number getProductId()
	{
		return (Number) this.data.get(KEY_PRODUCT_ID);
	}

	/**
	 * Get vendor id
	 *
	 * @return the USB device vendor identifier
	 */
	public Number getVendorId()
	{
		return (Number) this.data.get(KEY_VENDOR_ID);
	}

	/**
	 * Get product name
	 *
	 * @return the USB device product name
	 */
	public String getProductName()
	{
		return (String) this.data.get(KEY_PRODUCT_NAME);
	}

	/**
	 * Get manufacturer
	 *
	 * @return the USB device manufacturer
	 */
	public String getManufacturer()
	{
		return (String) this.data.get(KEY_MANUFACTURER);
	}

	/**
	 * Get serial number
	 *
	 * @return the USB device serial number
	 */
	public String getSerialNumber()
	{
		return (String) this.data.get(KEY_SERIAL_NUMBER);
	}

	/**
	 * Set port id
	 *
	 * @param portId
	 *            the port unique identifier
	 * @throws DataDictionaryException
	 *             if portId is empty
	 */
	public void setPortId(String portId) throws DataDictionaryException
	{
		this.setPortId((Object) portId);
	}

	/**
	 * Set port name
	 *
	 * @param portName
	 *            the port name used to open serial port
	 * @throws DataDictionaryException
	 *             if portName is empty
	 */
	public void setPortName(String portName) throws DataDictionaryException
	{
		this.setPortName((Object) portName);
	}

	/**
	 * Set description
	 *
	 * @param description
	 *            the port description
	 * @throws DataDictionaryException
	 *             if description is empty
	 */
	public void setDescription(String description) throws DataDictionaryException
	{
		this.setDescription((Object) description);
	}

	/**
	 * Set product id
	 *
	 * @param productId
	 *            the USB device product identifier
	 * @throws DataDictionaryException
	 *             if productId is out of range [0-65535]
	 */
	public void setProductId(Number productId) throws DataDictionaryException
	{
		this.setProductId((Object) productId);
	}

	/**
	 * Set vendor id
	 *
	 * @param vendorId
	 *            the USB device vendor identifier
	 * @throws DataDictionaryException
	 *             if vendorId is out of range [0-65535]
	 */
	public void setVendorId(Number vendorId) throws DataDictionaryException
	{
		this.setVendorId((Object) vendorId);
	}

	/**
	 * Set product name
	 *
	 * @param productName
	 *            the USB device product name
	 * @throws DataDictionaryException
	 *             if productName is empty
	 */
	public void setProductName(String productName) throws DataDictionaryException
	{
		this.setProductName((Object) productName);
	}

	/**
	 * Set manufacturer
	 *
	 * @param manufacturer
	 *            the USB device manufacturer
	 * @throws DataDictionaryException
	 *             if manufacturer is empty
	 */
	public void setManufacturer(String manufacturer) throws DataDictionaryException
	{
		this.setManufacturer((Object) manufacturer);
	}

	/**
	 * Set serial number
	 *
	 * @param serialNumber
	 *            the USB device serial number
	 * @throws DataDictionaryException
	 *             if serialNumber is empty
	 */
	public void setSerialNumber(String serialNumber) throws DataDictionaryException
	{
		this.setSerialNumber((Object) serialNumber);
	}

	/**
	 * Check if serial port is native (not USB)
	 *
	 * @return true if native port, false otherwise
	 */
	public boolean isNative()
	{
		return this.getPortName() != null && !this.getPortName().isEmpty() && this.getPortId() == null;
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// PROTECTED METHODS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	protected void setPortId(Object portId) throws DataDictionaryException
	{
		this.data.put(KEY_PORT_ID, this.kPortId.convert(portId));
	}

	protected void setPortName(Object portName) throws DataDictionaryException
	{
		this.data.put(KEY_PORT_NAME, this.kPortName.convert(portName));
	}

	protected void setDescription(Object description) throws DataDictionaryException
	{
		this.data.put(KEY_DESCRIPTION, this.kDescription.convert(description));
	}

	protected void setProductId(Object productId) throws DataDictionaryException
	{
		this.data.put(KEY_PRODUCT_ID, this.kProductId.convert(productId));
	}

	protected void setVendorId(Object vendorId) throws DataDictionaryException
	{
		this.data.put(KEY_VENDOR_ID, this.kVendorId.convert(vendorId));
	}

	protected void setProductName(Object productName) throws DataDictionaryException
	{
		this.data.put(KEY_PRODUCT_NAME, this.kProductName.convert(productName));
	}

	protected void setManufacturer(Object manufacturer) throws DataDictionaryException
	{
		this.data.put(KEY_MANUFACTURER, this.kManufacturer.convert(manufacturer));
	}

	protected void setSerialNumber(Object serialNumber) throws DataDictionaryException
	{
		this.data.put(KEY_SERIAL_NUMBER, this.kSerialNumber.convert(serialNumber));
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
			this.kPortId = new KeyDescriptorString(KEY_PORT_ID, false, false);
			this.keys.add(this.kPortId);

			this.kPortName = new KeyDescriptorString(KEY_PORT_NAME, false, false);
			this.keys.add(this.kPortName);

			this.kDescription = new KeyDescriptorString(KEY_DESCRIPTION, false, false);
			this.keys.add(this.kDescription);

			this.kProductId = new KeyDescriptorNumberMinMax(KEY_PRODUCT_ID, false, PRODUCT_ID_MIN, PRODUCT_ID_MAX);
			this.keys.add(this.kProductId);

			this.kVendorId = new KeyDescriptorNumberMinMax(KEY_VENDOR_ID, false, VENDOR_ID_MIN, VENDOR_ID_MAX);
			this.keys.add(this.kVendorId);

			this.kProductName = new KeyDescriptorString(KEY_PRODUCT_NAME, false, false);
			this.keys.add(this.kProductName);

			this.kManufacturer = new KeyDescriptorString(KEY_MANUFACTURER, false, false);
			this.keys.add(this.kManufacturer);

			this.kSerialNumber = new KeyDescriptorString(KEY_SERIAL_NUMBER, false, false);
			this.keys.add(this.kSerialNumber);

			this.addAllKeyDescriptor(this.keys);
		}
		catch (DataDictionaryException e)
		{
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}
