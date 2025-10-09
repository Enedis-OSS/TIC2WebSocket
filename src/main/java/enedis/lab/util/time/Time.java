// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.util.time;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class providing time-related constants and helper methods.
 *
 * <p>This class offers constants for time units and static methods for sleeping, formatting
 * timestamps, and decorating messages with timestamps. It is designed for general-purpose time
 * management and formatting.
 *
 * <p>Common use cases include:
 *
 * <ul>
 *   <li>Sleeping for a specified duration
 *   <li>Formatting timestamps as date/time strings
 *   <li>Decorating log messages with current date/time
 * </ul>
 *
 * @author Enedis Smarties team
 */
public abstract class Time {
  /** Millisecond */
  public static final int MILLISECOND = 1;

  /** Second */
  public static final int SECOND = 1000 * MILLISECOND;

  /** Minute */
  public static final int MINUTE = 60 * SECOND;

  /** Hour */
  public static final int HOUR = 60 * MINUTE;

  /** Day */
  public static final int DAY = 24 * HOUR;

  /* Default format for displaying the date/time */
  private static final String DFLT_DATETIME_FORMAT = "yyyy/MM/dd HH:mm:ss.SSS";

  /**
   * Wait the given duration in millisecond
   *
   * @param duration
   */
  public static void sleep(long duration) {
    if (duration <= 0L) {
      return;
    }
    try {
      Thread.sleep(duration);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * Convert date/time from long number (since an origin) to a string in the default format
   *
   * @param timestamp a long number (of milliseconds since January 1, 1970, 00:00:00 GMT)
   * @return the time/date string in the default format
   */
  public static String timestampToDateTimeStr(long timestamp) {
    return timestampToDateTimeStr(timestamp, DFLT_DATETIME_FORMAT);
  }

  /**
   * Convert date/time from long number to a string in the specified format
   *
   * @param timestamp a long number (of milliseconds since January 1, 1970, 00:00:00 GMT)
   * @param dateTimeFormatStr the format specified
   * @return the time/date string in the specified format
   */
  public static String timestampToDateTimeStr(long timestamp, String dateTimeFormatStr) {
    DateFormat dateFormat = new SimpleDateFormat(dateTimeFormatStr);
    Date date = new Date(timestamp);
    return dateFormat.format(date);
  }

  /**
   * Timestamp a message with the current DateTime
   *
   * @param msg
   * @return the message decorated with current DateTime
   */
  public static String timestamp(String msg) {
    return timestamp(msg, DFLT_DATETIME_FORMAT);
  }

  /**
   * Timestamp a message with the current DateTime
   *
   * @param msg
   * @param dateTimeFormat
   * @return the message decorated with current DateTime
   */
  public static String timestamp(String msg, String dateTimeFormat) {
    DateFormat dateFormat = new SimpleDateFormat(dateTimeFormat);
    Date date = new Date();
    String text = dateFormat.format(date) + "  : " + msg;
    return text;
  }
}
