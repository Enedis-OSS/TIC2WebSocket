// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
// 
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.io.serialport;

import static com.sun.jna.platform.win32.SetupApi.DICS_FLAG_GLOBAL;
import static com.sun.jna.platform.win32.SetupApi.DIGCF_DEVICEINTERFACE;
import static com.sun.jna.platform.win32.SetupApi.DIGCF_PRESENT;
import static com.sun.jna.platform.win32.SetupApi.DIREG_DEV;
import static com.sun.jna.platform.win32.SetupApi.GUID_DEVINTERFACE_COMPORT;
import static com.sun.jna.platform.win32.SetupApi.SPDRP_DEVICEDESC;
import static com.sun.jna.platform.win32.WinBase.INVALID_HANDLE_VALUE;
import static com.sun.jna.platform.win32.WinDef.MAX_PATH;
import static com.sun.jna.platform.win32.WinError.ERROR_SUCCESS;
import static com.sun.jna.platform.win32.WinNT.KEY_QUERY_VALUE;
import static com.sun.jna.platform.win32.WinNT.KEY_READ;
import static com.sun.jna.platform.win32.WinNT.REG_DWORD;
import static com.sun.jna.platform.win32.WinNT.REG_SZ;
import static com.sun.jna.platform.win32.WinReg.HKEY_LOCAL_MACHINE;

import java.util.ArrayList;
import java.util.List;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Advapi32;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.Cfgmgr32;
import com.sun.jna.platform.win32.Cfgmgr32Util;
import com.sun.jna.platform.win32.Guid.GUID;
import com.sun.jna.platform.win32.SetupApi;
import com.sun.jna.platform.win32.SetupApi.SP_DEVINFO_DATA;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.platform.win32.WinReg.HKEY;
import com.sun.jna.ptr.IntByReference;

import enedis.lab.types.DataArrayList;
import enedis.lab.types.DataDictionaryException;
import enedis.lab.types.DataList;

/**
 * Class used to find all serial port descriptor for Windows
 */
public class SerialPortFinderForWindows implements SerialPortFinder
{
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// CONSTANTS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private static final int	SPDRP_MFG				= 0x0000000B;
	private static final int	SPDRP_ADDRESS			= 0x0000001C;
	private static final int	PROPERTY_MAX_SIZE		= 1024;

	private static final GUID	GUID_DEVCLASS_PORTS		= new GUID("{4d36e978-e325-11ce-bfc1-08002be10318}");
	private static final GUID	GUID_DEVCLASS_MODEM		= new GUID("{4d36e96d-e325-11ce-bfc1-08002be10318}");
	private static final GUID	GUID_DEVINTERFACE_MODEM	= new GUID("{2c7089aa-2e0e-11d1-b114-00c04fc2aae4}");

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

	/**
	 * Get instance
	 *
	 * @return Unique instance
	 */
	public static SerialPortFinderForWindows getInstance()
	{
		if(instance == null)
		{
			instance = new SerialPortFinderForWindows();
		}

		return instance;
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// ATTRIBUTES
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private static SerialPortFinderForWindows instance;

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// CONSTRUCTORS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private SerialPortFinderForWindows()
	{
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// INTERFACE
	/// SerialPortFinder
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public DataList<SerialPortDescriptor> findAll()
	{
		final class SetupToken
		{
			public GUID	guid;
			public int	flags;

			public SetupToken(GUID guid, int flags)
			{
				super();
				this.guid = guid;
				this.flags = flags;
			}
		}

		SetupToken[] setupTokens = new SetupToken[] { new SetupToken(GUID_DEVCLASS_PORTS, DIGCF_PRESENT), new SetupToken(GUID_DEVCLASS_MODEM, DIGCF_PRESENT),
				new SetupToken(GUID_DEVINTERFACE_COMPORT, DIGCF_PRESENT | DIGCF_DEVICEINTERFACE), new SetupToken(GUID_DEVINTERFACE_MODEM, DIGCF_PRESENT | DIGCF_DEVICEINTERFACE) };

		DataList<SerialPortDescriptor> serialPortDescriptorList = new DataArrayList<SerialPortDescriptor>();

		for (int i = 0; i < setupTokens.length; ++i)
		{
			HANDLE deviceInfoSet = SetupApi.INSTANCE.SetupDiGetClassDevs(setupTokens[i].guid, null, null, setupTokens[i].flags);
			if (deviceInfoSet == INVALID_HANDLE_VALUE)
			{
				return serialPortDescriptorList;
			}
			SP_DEVINFO_DATA deviceInfoData = new SP_DEVINFO_DATA();
			int index = 0;
			while (SetupApi.INSTANCE.SetupDiEnumDeviceInfo(deviceInfoSet, index++, deviceInfoData))
			{
				SerialPortDescriptor serialPortDescriptor;

				String portName = devicePortName(deviceInfoSet, deviceInfoData);
				if (portName == null || portName.isEmpty() || portName.contains("LPT"))
				{
					continue;
				}
				if (anyOfPorts(serialPortDescriptorList, portName))
				{
					continue;
				}
				String description = deviceDescription(deviceInfoSet, deviceInfoData);
				String address = deviceAddress(deviceInfoSet, deviceInfoData);
				String manufacturer = deviceManufacturer(deviceInfoSet, deviceInfoData);
				String instanceIdentifier = deviceInstanceIdentifier(deviceInfoData.DevInst);
				String serialNumber = deviceSerialNumber(instanceIdentifier, deviceInfoData.DevInst);
				Number vendorIdentifier = deviceVendorIdentifier(instanceIdentifier);
				Number productIdentifier = deviceProductIdentifier(instanceIdentifier);
				try
				{
					serialPortDescriptor = new SerialPortDescriptor(address, portName, description, productIdentifier, vendorIdentifier, null, manufacturer, serialNumber);
				}
				catch (DataDictionaryException e)
				{
					e.printStackTrace(System.err);
					continue;
				}
				serialPortDescriptorList.add(serialPortDescriptor);

			}
			SetupApi.INSTANCE.SetupDiDestroyDeviceInfoList(deviceInfoSet);
		}

		List<String> portNames = portNamesFromHardwareDeviceMap();
		for (String portName : portNames)
		{
			if (!anyOfPorts(serialPortDescriptorList, portName))
			{
				try
				{
					SerialPortDescriptor serialPortDescriptor = new SerialPortDescriptor(null, portName, null, null, null, null, null, null);
					serialPortDescriptorList.add(serialPortDescriptor);
				}
				catch (DataDictionaryException e)
				{
					e.printStackTrace(System.err);
				}
			}
		}

		return serialPortDescriptorList;
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// PUBLIC METHODS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// PROTECTED METHODS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// PRIVATE METHODS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private static boolean anyOfPorts(DataList<SerialPortDescriptor> descriptors, String portName)
	{
		if (descriptors == null)
		{
			return false;
		}
		for (SerialPortDescriptor descriptor : descriptors)
		{
			if (descriptor.getPortName() == null)
			{
				if (portName != null)
				{
					continue;
				}
				else
				{
					return true;
				}
			}
			if (descriptor.getPortName().equals(portName))
			{
				return true;
			}
		}

		return false;
	}

	private static String devicePortName(HANDLE deviceInfoSet, SP_DEVINFO_DATA deviceInfoData)
	{
		HKEY key = SetupApi.INSTANCE.SetupDiOpenDevRegKey(deviceInfoSet, deviceInfoData, DICS_FLAG_GLOBAL, 0, DIREG_DEV, KEY_READ);

		if (key == INVALID_HANDLE_VALUE)
		{
			return null;
		}
		String[] keyTokens = { "PortName", "PortNumber" };
		String portName = null;
		for (int i = 0; i < keyTokens.length && portName == null; i++)
		{
			portName = Advapi32Util.registryGetStringValue(key, keyTokens[i]);
		}

		return portName;
	}

	private static String deviceDescription(HANDLE deviceInfoSet, SP_DEVINFO_DATA deviceInfoData)
	{
		return (String) deviceRegistryProperty(deviceInfoSet, deviceInfoData, SPDRP_DEVICEDESC);
	}

	private static String deviceManufacturer(HANDLE deviceInfoSet, SP_DEVINFO_DATA deviceInfoData)
	{
		return (String) deviceRegistryProperty(deviceInfoSet, deviceInfoData, SPDRP_MFG);
	}

	private static String deviceAddress(HANDLE deviceInfoSet, SP_DEVINFO_DATA deviceInfoData)
	{
		Integer address = (Integer) deviceRegistryProperty(deviceInfoSet, deviceInfoData, SPDRP_ADDRESS);
		return (address != null) ? address.toString() : null;
	}

	private static Number deviceVendorIdentifier(String instanceIdentifier)
	{
		final int vendorIdentifierSize = 4;

		Number result = parseDeviceIdentifier(instanceIdentifier, "VID_", vendorIdentifierSize);
		if (result == null)
		{
			result = parseDeviceIdentifier(instanceIdentifier, "VEN_", vendorIdentifierSize);
		}

		return result;
	}

	private static Number deviceProductIdentifier(String instanceIdentifier)
	{
		final int productIdentifierSize = 4;

		Number result = parseDeviceIdentifier(instanceIdentifier, "PID_", productIdentifierSize);
		if (result == null)
		{
			result = parseDeviceIdentifier(instanceIdentifier, "DEV_", productIdentifierSize);
		}

		return result;
	}

	private static String deviceInstanceIdentifier(int deviceInstanceNumber)
	{
		return Cfgmgr32Util.CM_Get_Device_ID(deviceInstanceNumber);
	}

	private static String deviceSerialNumber(String instanceIdentifier, int deviceInstanceNumber)
	{
		for (;;)
		{
			String serialNumber = parseDeviceSerialNumber(instanceIdentifier);
			if (serialNumber != null)
			{
				return serialNumber;
			}
			deviceInstanceNumber = parentDeviceInstanceNumber(deviceInstanceNumber);
			if (deviceInstanceNumber == 0)
			{
				break;
			}
			instanceIdentifier = deviceInstanceIdentifier(deviceInstanceNumber);
			if (instanceIdentifier.isEmpty())
			{
				break;
			}
		}

		return null;
	}

	private static Object deviceRegistryProperty(HANDLE deviceInfoSet, SP_DEVINFO_DATA deviceInfoData, int property)
	{
		Object propertyValue = null;
		IntByReference dataType = new IntByReference(0);
		Pointer outputBuffer = new Memory(PROPERTY_MAX_SIZE);
		IntByReference bytesRequired = new IntByReference(PROPERTY_MAX_SIZE);

		if (SetupApi.INSTANCE.SetupDiGetDeviceRegistryProperty(deviceInfoSet, deviceInfoData, property, dataType, outputBuffer, bytesRequired.getValue(), bytesRequired))
		{
			switch (dataType.getValue())
			{
				case REG_DWORD:
					propertyValue = outputBuffer.getInt(0);
					break;
				case REG_SZ:
					propertyValue = outputBuffer.getWideString(0);
					break;
			}
		}

		return propertyValue;
	}

	private static int parentDeviceInstanceNumber(int childDeviceInstanceNumber)
	{
		IntByReference parentInstanceNumber = new IntByReference(0);
		if (Cfgmgr32.INSTANCE.CM_Get_Parent(parentInstanceNumber, childDeviceInstanceNumber, 0) != Cfgmgr32.CR_SUCCESS)
		{
			return 0;
		}
		return parentInstanceNumber.getValue();
	}

	private static String parseDeviceSerialNumber(String instanceIdentifier)
	{
		int firstbound = instanceIdentifier.lastIndexOf('\\');
		int lastbound = instanceIdentifier.indexOf('_', firstbound);
		if (instanceIdentifier.startsWith("USB\\"))
		{
			if (lastbound != instanceIdentifier.length() - 3)
				lastbound = instanceIdentifier.length();
			int ampersand = instanceIdentifier.indexOf('&', firstbound);
			if (ampersand != -1 && ampersand < lastbound)
				return null;
		}
		else if (instanceIdentifier.startsWith("FTDIBUS\\"))
		{
			firstbound = instanceIdentifier.lastIndexOf('+');
			lastbound = instanceIdentifier.indexOf('\\', firstbound);
			if (lastbound == -1)
				return null;
		}
		else
		{
			return null;
		}

		return instanceIdentifier.substring(firstbound + 1, lastbound);
	}

	private static Number parseDeviceIdentifier(String instanceIdentifier, String identifierPrefix, int identifierSize)
	{
		int index = instanceIdentifier.indexOf(identifierPrefix);

		if (index == -1)
		{
			return null;
		}
		String indentifierText = instanceIdentifier.substring(index + identifierPrefix.length(), index + identifierPrefix.length() + identifierSize);
		Number identifierValue;
		try
		{
			identifierValue = Integer.parseInt(indentifierText, 16);
		}
		catch (Exception e)
		{
			identifierValue = null;
		}

		return identifierValue;
	}

	private static List<String> portNamesFromHardwareDeviceMap()
	{
		List<String> result = new ArrayList<String>();
		HKEY hKey = null;
		try
		{
			hKey = Advapi32Util.registryGetKey(HKEY_LOCAL_MACHINE, "HARDWARE\\DEVICEMAP\\SERIALCOMM", KEY_QUERY_VALUE).getValue();
		}
		catch(Win32Exception exception)
		{
			return result;
		}
		int index = 0;
		for (;;)
		{
			// This is a maximum length of value name, see:
			// https://msdn.microsoft.com/en-us/library/windows/desktop/ms724872%28v=vs.85%29.aspx
			IntByReference requiredValueNameChars = new IntByReference(16383);
			char[] outputValueName = new char[requiredValueNameChars.getValue()];
			Pointer outputBuffer = new Memory(MAX_PATH);
			IntByReference bytesRequired = new IntByReference(MAX_PATH);
			int ret = Advapi32.INSTANCE.RegEnumValue(hKey, index, outputValueName, requiredValueNameChars, null, null, outputBuffer, bytesRequired);
			if (ret == ERROR_SUCCESS)
			{
				result.add(outputBuffer.getWideString(0));
				++index;
			}
			else
			{
				break;
			}
		}
		Advapi32.INSTANCE.RegCloseKey(hKey);

		return result;
	}
}
