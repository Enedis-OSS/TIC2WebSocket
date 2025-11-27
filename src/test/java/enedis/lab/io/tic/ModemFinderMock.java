// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.io.tic;

import enedis.lab.io.PortFinderMock;
import tic.io.modem.ModemDescriptor;
import tic.io.modem.ModemFinder;

import enedis.lab.types.DataArrayList;  
import enedis.lab.types.DataList;
import java.util.List;

@SuppressWarnings("javadoc")
public class ModemFinderMock extends PortFinderMock<ModemDescriptor> implements ModemFinder {
  public DataList<ModemDescriptor> nativeDescriptorList = new DataArrayList<ModemDescriptor>();

  public ModemFinderMock() {
    super();
  }

  public ModemFinderMock(List<ModemDescriptor> descriptors) {
    this.setDescriptors(descriptors);
  }

  @Override
  public ModemDescriptor findNative(String portName) {
    for (ModemDescriptor descriptor : this.nativeDescriptorList) {
      if (descriptor.getPortName() == null && portName == null) {
        return descriptor;
      }
      if (descriptor.getPortName().equals(portName)) {
        return descriptor;
      }
    }

    return null;
  }
}
