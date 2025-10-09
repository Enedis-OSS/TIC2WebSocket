// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.util;

import com.sun.jna.Library;
import com.sun.jna.Native;

/** Interface for system of C library */
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
