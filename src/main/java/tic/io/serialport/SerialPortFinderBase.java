// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.io.serialport;

import java.util.List;
import org.apache.commons.lang3.SystemUtils;

/**
 * Cross-platform serial port finder implementation with automatic OS detection.
 *
 * <p>This class implements {@link SerialPortFinder} and provides a unified interface for
 * discovering serial ports across different operating systems. It automatically detects the current
 * OS and delegates to the appropriate platform-specific implementation.
 *
 * <p>Supported operating systems:
 *
 * <ul>
 *   <li>Windows - uses {@code SerialPortFinderForWindows}
 *   <li>Linux - uses {@code SerialPortFinderForLinux}
 * </ul>
 *
 * <p>The class follows the singleton pattern with lazy initialization, ensuring only one instance
 * exists for the lifetime of the application.
 *
 * @author Enedis Smarties team
 * @see SerialPortFinder
 * @see SerialPortDescriptor
 */
public class SerialPortFinderBase implements SerialPortFinder {

  /**
   * Returns the singleton instance of the serial port finder.
   *
   * <p>This method performs lazy initialization and automatically selects the appropriate
   * platform-specific implementation based on the detected operating system. The instance is cached
   * after the first call.
   *
   * <p>Supported platforms are detected using Apache Commons SystemUtils:
   *
   * <ul>
   *   <li>Windows - returns Windows-specific finder
   *   <li>Linux - returns Linux-specific finder
   *   <li>Mac OS X - returns Mac OS X-specific finder
   * </ul>
   *
   * @return the singleton SerialPortFinder instance for the current OS
   * @throws RuntimeException if the current operating system is not supported
   */
  public static SerialPortFinder getInstance() {
    if (SystemUtils.IS_OS_WINDOWS) {
      return SerialPortFinderForWindows.getInstance();
    } else if (SystemUtils.IS_OS_LINUX) {
      return SerialPortFinderForLinux.getInstance();
    } else if (SystemUtils.IS_OS_MAC_OSX) {
      return SerialPortFinderForMacOsX.getInstance();
    } else {
      throw new RuntimeException(
          "Operating system "
              + SystemUtils.OS_NAME
              + " is not handled by "
              + SerialPortFinderBase.class.getName());
    }
  }

  /**
   * Private constructor to prevent direct instantiation.
   *
   * <p>Use {@link #getInstance()} to obtain the singleton instance.
   */
  private SerialPortFinderBase() {}

  /**
   * Finds all available serial ports on the system.
   *
   * <p>This method delegates to the platform-specific instance obtained through {@link
   * #getInstance()}. The actual implementation depends on the detected operating system.
   *
   * @return a list of all serial port descriptors available on the system
   */
  @Override
  public List<SerialPortDescriptor> findAll() {
    return getInstance().findAll();
  }
}
