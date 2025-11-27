// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.io;

import enedis.lab.mock.FunctionCall;
import java.util.ArrayList;
import java.util.List;
import tic.io.PortFinder;

@SuppressWarnings("javadoc")
public class PortFinderMock<T> implements PortFinder<T> {
  public List<FunctionCall> findAllCalls = new ArrayList<>();
  public List<T> descriptorList = new ArrayList<>();

  public PortFinderMock() {}

  @Override
  public List<T> findAll() {
    this.findAllCalls.add(new FunctionCall());
    return this.getDescriptors();
  }

  public List<T> getDescriptors() {
    List<T> descriptors = new ArrayList<>();

    synchronized (this.descriptorList) {
      descriptors.addAll(this.descriptorList);
    }

    return descriptors;
  }

  public List<T> setDescriptors(List<T> descriptors) {
    synchronized (this.descriptorList) {
      this.descriptorList.clear();
      this.descriptorList.addAll(descriptors);
    }

    return descriptors;
  }

  public void addDescriptor(T descriptor) {
    synchronized (this.descriptorList) {
      this.descriptorList.add(descriptor);
    }
  }

  public void removeDescriptor(T descriptor) {
    synchronized (this.descriptorList) {
      this.descriptorList.remove(descriptor);
    }
  }
}
