// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.io;

import enedis.lab.mock.FunctionCall;
import enedis.lab.types.DataArrayList;
import enedis.lab.types.DataList;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("javadoc")
public class PortFinderMock<T> implements PortFinder<T> {
  public List<FunctionCall> findAllCalls = new ArrayList<FunctionCall>();
  public DataList<T> descriptorList = new DataArrayList<T>();

  public PortFinderMock() {
    this.descriptorList = new DataArrayList<T>();
  }

  @Override
  public DataList<T> findAll() {
    this.findAllCalls.add(new FunctionCall());
    return this.getDescriptors();
  }

  public DataList<T> getDescriptors() {
    DataList<T> descriptors = new DataArrayList<T>();

    synchronized (this.descriptorList) {
      descriptors.addAll(this.descriptorList);
    }

    return descriptors;
  }

  public DataList<T> setDescriptors(DataList<T> descriptors) {
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
