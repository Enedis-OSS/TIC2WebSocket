// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.io.serialport;

import enedis.lab.io.PortFinder;
import enedis.lab.types.DataArrayList;
import enedis.lab.types.DataList;

/**
 * Interface used to find all serial port descriptor
 */
public interface SerialPortFinder extends PortFinder<SerialPortDescriptor>
{
	/**
	 * Find serial port descriptor matching with port id
	 *
	 * @param portId
	 *            the unique port identifier desired
	 *
	 * @return Serial port descriptor found, or null if nothing matches with portId
	 */
	public default SerialPortDescriptor findByPortId(String portId)
	{
		return this.findAll().stream().filter(k -> (k.getPortId() != null) ? k.getPortId().equals(portId) : portId == null).findFirst().orElse(null);
	}

	/**
	 * Find serial port descriptor matching with port name
	 *
	 * @param portName
	 *            the port name desired
	 *
	 * @return Serial port descriptor found, or null if nothing matches with portName
	 */
	public default SerialPortDescriptor findByPortName(String portName)
	{
		return this.findAll().stream().filter(k -> (k.getPortName() != null) ? k.getPortName().equals(portName) : portName == null).findFirst().orElse(null);
	}

	/**
	 * Find native serial port (not USB) descriptor matching with port name
	 *
	 * @param portName
	 *            the port name desired
	 *
	 * @return Serial port descriptor found, or null if nothing matches with portName
	 */
	public default SerialPortDescriptor findNative(String portName)
	{
		return this.findAll().stream().filter(k -> k.isNative() && k.getPortName().equals(portName)).findFirst().orElse(null);
	}

	/**
	 * Find serial port descriptor matching with port id (having priority) or port name
	 *
	 * @param portId
	 *            the unique port identifier desired
	 * @param portName
	 *            the port name desired
	 *
	 * @return Serial port descriptor found, or null if nothing matches with portName
	 */
	public default SerialPortDescriptor findByPortIdOrPortName(String portId, String portName)
	{
		SerialPortDescriptor descriptor;

		if (portId != null)
		{
			descriptor = this.findByPortId(portId);
		}
		else if (portName != null)
		{
			descriptor = this.findByPortName(portName);
		}
		else
		{
			descriptor = null;
		}

		return descriptor;
	}

	/**
	 * Find serial port descriptor matching with pid/vid
	 *
	 * @param productId
	 *            the USB device product identifier
	 * @param vendorId
	 *            the USB device vendor identifier
	 *
	 * @return Serial port descriptor list found
	 */
	public default DataList<SerialPortDescriptor> findByProductIdAndVendorId(Number productId, Number vendorId)
	{
		DataList<SerialPortDescriptor> descriptorList = new DataArrayList<SerialPortDescriptor>();

		for (SerialPortDescriptor descriptor : this.findAll())
		{
			if (descriptor.getProductId() == null && productId != null)
			{
				continue;
			}
			if (descriptor.getVendorId() == null && vendorId != null)
			{
				continue;
			}
			if (descriptor.getProductId().equals(productId) && descriptor.getVendorId().equals(vendorId))
			{
				descriptorList.add(descriptor);
			}
		}

		return descriptorList;
	}
}
