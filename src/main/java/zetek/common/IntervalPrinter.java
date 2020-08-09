/* @name IntervalPrinter.java

    Copyright (c) 2007 by Advanced Systems and Software Technologies.
    All Rights Reserved

*/

package zetek.common;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;

/**
 * Implement various methods for measuring time.  The purpose of this class
 * is to facilitate determining how long various computer operations take.

 * @author W Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class IntervalPrinter {

  public static final long serialVersionUID = 1;

  /** The number of milliseconds in a 24 hour day.*/
  public static final long MILLIS_IN_ONE_DAY = 24*3600*1000;

  /** The number of milliseconds in one hour.*/
  public static final long MILLIS_IN_ONE_HOUR = 3600*1000;

  /** The number of milliseconds in one minute.*/
  public static final long MILLIS_IN_ONE_MINUTE = 60*1000;

  /** The number of milliseconds in one second.*/
  public static final long MILLIS_IN_ONE_SECOND = 1000;

  /** SQL-specific date formatter for calendars.  This formatter
      produces a string which can be compared to SQL dates.  Date
      formatters cannot format calendars, only dates.  */
  public static final SimpleDateFormat SQL_DATE_FORMATTER =
  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  /** Array of constants for looping through the conversions.*/
  public static final long[] MILLISECOND_CONVERSIONS = {
    MILLIS_IN_ONE_DAY, MILLIS_IN_ONE_HOUR, MILLIS_IN_ONE_MINUTE,
    MILLIS_IN_ONE_SECOND
  };

  /** Format a single-digit number with a leading zero.*/
  public static NumberFormat LZERO_NUMBER = NumberFormat.getInstance();
  /** Format a single-digit number with up to 3 leading zeros.*/
  public static NumberFormat L3ZERO_NUMBER = NumberFormat.getInstance();
  static {
    LZERO_NUMBER.setMinimumIntegerDigits(2);
    L3ZERO_NUMBER.setMinimumIntegerDigits(3);
  }

  /** Format a long interval, that is, the number of milliseconds that
      elapsed between two clock times, into a printable string of the
      form dd:hh:mm:ss.milliseconds.  Any high-order zero values are
      ignored.
  @return the string representing the interval has an extra space on
  the end so that it can be prepended to other strings without
  confusion.*/
  public String millisToString(long interval) {
    StringBuffer answer = new StringBuffer(15);
    long partial;
    int i;
    /**/

    if (interval >= MILLISECOND_CONVERSIONS[0]) {
      partial = interval / MILLISECOND_CONVERSIONS[0];
      answer.append(String.valueOf(partial));
      interval -= partial * MILLISECOND_CONVERSIONS[0];
    }

    for (i = 1; i<MILLISECOND_CONVERSIONS.length; i++) {
      partial = interval / MILLISECOND_CONVERSIONS[i];
      if (answer.length() > 0) { answer.append(":"); }
      answer.append(LZERO_NUMBER.format(partial));
      interval -= partial * MILLISECOND_CONVERSIONS[i];
    }
    // Remaining milliseconds
    answer.append("." + L3ZERO_NUMBER.format(interval) + " ");
    return answer.toString();
  }

  /** Record the last time the timer was printed.*/
  long lastTime;

    /** Obligatory constructor.*/
  public IntervalPrinter() {
    reset();
  }

  /*
   * Reset the time tag to the present to provide a base for future
   * intervals.
   */
  public void reset() {
    lastTime = System.currentTimeMillis();
  }

  /**
   * Compute a string that tells how long since the last reset of the timer.
   * @return String that gives the time since the last reset.
   */
  public String howLongSince() {
    return howLongSince(false);
  }

  /**
   * Compute a string which gives the interval between the last reset and now.
   * OPtinoally prepend the current time.
   * @param printNow if true, the string starts with the present time
   * @return formatted time string with a space at the end.
   */
  public String howLongSince(boolean printNow) {
    long now = System.currentTimeMillis();

    String howLong = millisToString(now - lastTime);
    lastTime = now;
    return (printNow ? SQL_DATE_FORMATTER.format(now) + " " : "") + howLong;
  }
}
