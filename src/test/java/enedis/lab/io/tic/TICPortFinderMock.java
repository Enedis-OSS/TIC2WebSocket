// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
// 
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.io.tic;

import enedis.lab.io.PortFinderMock;
import enedis.lab.types.DataArrayList;
import enedis.lab.types.DataList;

@SuppressWarnings("javadoc")
public class TICPortFinderMock extends PortFinderMock<TICPortDescriptor> implements TICPortFinder
{
	public DataList<TICPortDescriptor> nativeDescriptorList = new DataArrayList<TICPortDescriptor>();

	public TICPortFinderMock()
	{
		super();
	}

	public TICPortFinderMock(TICPortDescriptor... descriptors)
	{
		this.setDescriptors(DataArrayList.asList(descriptors));
	}

	@Override
	public TICPortDescriptor findNative(String portName)
	{
		for (TICPortDescriptor descriptor : this.nativeDescriptorList)
		{
			if (descriptor.getPortName() == null && portName == null)
			{
				return descriptor;
			}
			if (descriptor.getPortName().equals(portName))
			{
				return descriptor;
			}
		}

		return null;
	}

}
