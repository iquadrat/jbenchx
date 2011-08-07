package org.jbench.util;


public class TimeUtil {
  
  public static String SEPARATOR = ".";
  
  public static String toString(long ns) {
    if (ns == 0) {
      return "0ns";
    }
    if (ns < 0) {
      return "-" + toString(-ns);
    }
    
    // always print 3 digits
    int digits = (int)Math.floor(Math.log10(ns)) + 1;
    
    if (digits <= 3) {
      return ns + "ns";
    }
    
    double divisor = Math.pow(10, digits - 3);
    long nsRounded = (long)(Math.round(ns / divisor) * divisor);
    
    StringBuilder sb = new StringBuilder(String.valueOf(nsRounded));
    if (sb.length() <= 6) {
      return insertComma(sb, 3) + "us";
    }
    if (sb.length() <= 9) {
      return insertComma(sb, 6) + "ms";
    }
    if (sb.length() <= 12) {
      return insertComma(sb, 9) + "s";
    }
    
    return sb.substring(0, sb.length() - 9) + "s";
  }
  
  private static String insertComma(StringBuilder sb, int digitsComma) {
    if (sb.length() == digitsComma + 3) {
      return sb.substring(0, 3);
    }
    int commapos = sb.length() - digitsComma;
    sb.insert(commapos, SEPARATOR);
    return sb.substring(0, 4);
  }
  
}
