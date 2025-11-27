// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.io.modem;

import enedis.lab.io.PortFinderMock;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Test utility that mimics a {@link ModemFinder} with in-memory descriptors. */
public class ModemFinderMock extends PortFinderMock<ModemDescriptor> implements ModemFinder {
  private final List<ModemDescriptor> nativeDescriptorList = new ArrayList<>();

  public ModemFinderMock() {
    super();
  }

  public ModemFinderMock(ModemDescriptor... descriptors) {
    super();
    if (descriptors != null) {
      this.setDescriptors(Arrays.asList(descriptors));
    }
  }

  @Override
  public ModemDescriptor findNative(String portName) {
    for (ModemDescriptor descriptor : this.nativeDescriptorList) {
      if (descriptor.getPortName() == null && portName == null) {
        return descriptor;
      }
      if (descriptor.getPortName() != null && descriptor.getPortName().equals(portName)) {
        return descriptor;
      }
    }
    return null;
  }

  public void addNativeDescriptor(ModemDescriptor descriptor) {
    if (descriptor != null) {
      this.nativeDescriptorList.add(descriptor);
    }
  }

  public void removeNativeDescriptor(ModemDescriptor descriptor) {
    this.nativeDescriptorList.remove(descriptor);
  }
}
