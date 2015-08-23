package org.jbenchx.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
  
  private StringUtil() {}
  
  public static String join(String separator, String... strings) {
    return join(separator, Arrays.asList(strings), strings.length);
  }
  
  public static String join(String separator, Iterable<String> strings) {
    return join(separator, strings, 5);
  }
  
  public static String join(String separator, List<String> strings) {
    return join(separator, strings, strings.size());
  }
  
  private static final Pattern wildCard2RegexPattern = Pattern.compile("[^*?]+|(\\*)|(\\?)");
  
  public static Pattern wildCardToRegexpPattern(String wildCardPattern) {
    Matcher matcher = wildCard2RegexPattern.matcher(wildCardPattern);
    StringBuffer b = new StringBuffer();
    while (matcher.find()) {
      if (matcher.group(1) != null) {
        matcher.appendReplacement(b, ".*");
      } else if (matcher.group(2) != null) {
        matcher.appendReplacement(b, ".");
      }
      else {
        matcher.appendReplacement(b, "\\\\Q" + matcher.group(0) + "\\\\E");
      }
    }
    matcher.appendTail(b);
    return Pattern.compile(b.toString());
  }
  
  private static String join(String separator, Iterable<String> strings, int estimatedStringCount) {
    Iterator<String> iterator = strings.iterator();
    if (!iterator.hasNext()) return "";
    
    String text = iterator.next();
    if (!iterator.hasNext()) return text;
    
    StringBuilder builder = new StringBuilder(estimatedStringCount * (separator.length() + 4));
    builder.append(text);
    
    while (iterator.hasNext()) {
      text = iterator.next();
      builder.append(separator);
      builder.append(text);
    }
    return builder.toString();
  }
  
  public static List<String> split(String string, String regexp) {
    return Arrays.asList(string.split(regexp, -1));
  }
  
}
