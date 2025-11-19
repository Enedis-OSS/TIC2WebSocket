// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.io.serialport;

import enedis.lab.types.DataArrayList;
import enedis.lab.types.DataList;
import tic.io.PortFinder;

/**
 * Interface for discovering and searching serial ports on the system.
 *
 * <p>This interface extends {@link PortFinder} with {@link SerialPortDescriptor} support, providing
 * specialized methods for finding serial ports based on various criteria such as port ID, port
 * name, USB product/vendor identifiers, or native port detection.
 *
 * <p>The interface provides default implementations for all search methods, allowing
 * implementations to focus on the {@link PortFinder#findAll()} method which retrieves all available
 * serial ports on the system.
 *
 * @author Enedis Smarties team
 * @see PortFinder
 * @see SerialPortDescriptor
 */
public interface SerialPortFinder extends PortFinder<SerialPortDescriptor> {
  /**
   * Finds a serial port descriptor matching the specified port ID.
   *
   * <p>This method searches through all available serial ports and returns the first descriptor
   * with a matching port ID. The port ID is typically a unique identifier for USB serial devices.
   *
   * @param portId the unique port identifier to search for
   * @return the matching serial port descriptor, or null if no match is found
   */
  public default SerialPortDescriptor findByPortId(String portId) {
    return this.findAll().stream()
        .filter(k -> (k.getPortId() != null) ? k.getPortId().equals(portId) : portId == null)
        .findFirst()
        .orElse(null);
  }

  /**
   * Finds a serial port descriptor matching the specified port name.
   *
   * <p>This method searches through all available serial ports and returns the first descriptor
   * with a matching port name. The port name is the system identifier used to open the serial port
   * (e.g., COM1, /dev/ttyUSB0).
   *
   * @param portName the port name to search for
   * @return the matching serial port descriptor, or null if no match is found
   */
  public default SerialPortDescriptor findByPortName(String portName) {
    return this.findAll().stream()
        .filter(
            k -> (k.getPortName() != null) ? k.getPortName().equals(portName) : portName == null)
        .findFirst()
        .orElse(null);
  }

  /**
   * Finds a native serial port (not USB) matching the specified port name.
   *
   * <p>This method searches specifically for native serial ports, excluding USB-to-serial adapters.
   * A port is considered native if it has a port name but no port ID, typically indicating a
   * built-in hardware serial port.
   *
   * @param portName the port name to search for
   * @return the matching native serial port descriptor, or null if no match is found
   */
  public default SerialPortDescriptor findNative(String portName) {
    return this.findAll().stream()
        .filter(k -> k.isNative() && k.getPortName().equals(portName))
        .findFirst()
        .orElse(null);
  }

  /**
   * Finds a serial port descriptor matching either port ID or port name, with port ID having
   * priority.
   *
   * <p>This method provides a convenient way to search by either identifier. If a port ID is
   * provided, it is used first. If no port ID is provided but a port name is available, the search
   * is performed by port name. If neither is provided, null is returned.
   *
   * @param portId the unique port identifier to search for (has priority)
   * @param portName the port name to search for (used if portId is null)
   * @return the matching serial port descriptor, or null if no match is found
   */
  public default SerialPortDescriptor findByPortIdOrPortName(String portId, String portName) {
    SerialPortDescriptor descriptor;

    if (portId != null) {
      descriptor = this.findByPortId(portId);
    } else if (portName != null) {
      descriptor = this.findByPortName(portName);
    } else {
      descriptor = null;
    }

    return descriptor;
  }

  /**
   * Finds all serial port descriptors matching the specified USB product and vendor identifiers.
   *
   * <p>This method searches for USB serial devices matching both the product ID (PID) and vendor ID
   * (VID). Both identifiers must match for a descriptor to be included in the results. This is
   * useful for finding specific USB serial devices from a particular manufacturer.
   *
   * @param productId the USB device product identifier (PID) to match
   * @param vendorId the USB device vendor identifier (VID) to match
   * @return a list of matching serial port descriptors (empty list if no matches found)
   */
  public default DataList<SerialPortDescriptor> findByProductIdAndVendorId(
      Number productId, Number vendorId) {
    DataList<SerialPortDescriptor> descriptorList = new DataArrayList<SerialPortDescriptor>();

    for (SerialPortDescriptor descriptor : this.findAll()) {
      if (descriptor.getProductId() == null && productId != null) {
        continue;
      }
      if (descriptor.getVendorId() == null && vendorId != null) {
        continue;
      }
      if (descriptor.getProductId().equals(productId)
          && descriptor.getVendorId().equals(vendorId)) {
        descriptorList.add(descriptor);
      }
    }

    return descriptorList;
  }
}
