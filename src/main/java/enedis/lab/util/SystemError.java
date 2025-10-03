// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.util;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.Kernel32Util;
import org.apache.commons.lang3.SystemUtils;

/** Class for system error */
public class SystemError {
  /**
   * Get system last error code
   *
   * @return System last error code
   */
  public static int getCode() {
    return Native.getLastError();
  }

  /**
   * Get system last error message
   *
   * @return System last error message
   */
  public static String getMessage() {
    return getMessage(getCode());
  }

  /**
   * Get system error message associated with code
   *
   * @param code the system error code
   * @return System error message
   */
  public static String getMessage(int code) {
    String message = null;

    if (SystemUtils.IS_OS_WINDOWS) {
      message = Kernel32Util.formatMessage(code);
    } else if (SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_UNIX || SystemUtils.IS_OS_MAC_OSX) {
      message = SystemLibC.INSTANCE.strerror(code);
    }

    return message;
  }
}
