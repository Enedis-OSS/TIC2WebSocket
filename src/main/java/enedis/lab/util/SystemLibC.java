// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.util;

import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * Interface for accessing native C library functions via JNA.
 *
 * <p>This interface provides access to system-level C library functions, such as retrieving error messages
 * associated with error codes. It is designed for general-purpose native integration and diagnostics.
 *
 * <p>Common use cases include:
 * <ul>
 *   <li>Obtaining human-readable error messages from system error codes</li>
 *   <li>Integrating with native system libraries</li>
 *   <li>Supporting cross-platform diagnostics</li>
 * </ul>
 *
 * @author Enedis Smarties team
 */
public interface SystemLibC extends Library {
  /** Instance */
  SystemLibC INSTANCE = Native.load("c", SystemLibC.class);

  /**
   * Get string error from code
   *
   * @param code
   * @return string error
   */
  public String strerror(int code);
}
