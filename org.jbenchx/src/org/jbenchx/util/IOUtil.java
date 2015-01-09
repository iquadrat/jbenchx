package org.jbenchx.util;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Arrays;

import javax.annotation.CheckForNull;

public class IOUtil {

  private IOUtil() {}

  /**
   * Closes the given {@link Closeable} ignoring the I/O exception that might occur.
   * Does nothing if <code>null</code> is given as argument.
   */
  public static void closeSilently(@CheckForNull Closeable closeable) {
    if (closeable == null) {
      return;
    }
    try {
      closeable.close();
    } catch (IOException e) {
      // ignore
    }
  }

  /**
   * Copies the data from the given input stream to the given output stream.
   * Closes both streams in any case.
   *
   * @param in stream to read from
   * @param out stream to write to
   * @throws IOException if any I/O exception occurs during reading or writing
   */
  public static final void copyInputStreamToOutputStream(InputStream in, OutputStream out) throws IOException {
    byte[] buffer = new byte[1024 * 4];
    try {
      int len;
      while ((len = in.read(buffer)) >= 0) {
        out.write(buffer, 0, len);
      }
    } finally {
      closeSilently(in);
      closeSilently(out);
    }
  }

  public static IOException createIOException(Exception e) {
    IOException ioException = new IOException(e.getMessage());
    ioException.initCause(e);
    return ioException;
  }

  /**
   * Compares the contents of two streams by reading them.
   * <p>
   * NOTE: The streams get closed in any case.
   *
   * @param contents1 the first content stream
   * @param contents2 the second content stream
   * @return true iff both stream had the same length and the same data
   * @throws IOException if an I/O exception occurs while reading one of the streams
   */
  public static boolean hasSameContents(InputStream contents1, InputStream contents2) throws IOException {
    try {
      int bufSize = 10000;
      byte[] buffer1 = new byte[bufSize];
      byte[] buffer2 = new byte[bufSize];

      boolean eof1 = false;
      boolean eof2 = false;
      while (!eof1 && !eof2) {

        int pos1 = 0;
        while (pos1 != bufSize) {
          int count = contents1.read(buffer1, pos1, bufSize - pos1);
          if (count == -1) {
            eof1 = true;
            break;
          }
          pos1 += count;
        }

        int pos2 = 0;
        while (pos2 != bufSize) {
          int count = contents2.read(buffer2, pos2, bufSize - pos2);
          if (count == -1) {
            eof2 = true;
            break;
          }
          pos2 += count;
        }

        if (eof1 || eof2) {
          if (pos1 != pos2 || !firstBytesEquals(buffer1, buffer2, pos1)) {
            return false;
          }
        } else {
          if (!Arrays.equals(buffer1, buffer2)) {
            return false;
          }
        }

      }

      return true;
    } finally {
      IOUtil.closeSilently(contents1);
      IOUtil.closeSilently(contents2);
    }
  }

  /**
   * Reads the remaining part of the input stream.
   * Closes the input stream in any case.
   *
   * @throws IOException if reading fails
   */
  public static byte[] readToEnd(InputStream input) throws IOException {
    ByteArrayOutputStream builder = new ByteArrayOutputStream();
    copyInputStreamToOutputStream(input, builder);
    return builder.toByteArray();
  }

  public static String readToEnd(Reader reader) throws IOException {
    StringBuilder builder = new StringBuilder();
    char[] buffer = new char[1024 * 4];
    int count = reader.read(buffer);
    while (count != -1) {
      builder.append(buffer, 0, count);
      count = reader.read(buffer);
    }
    reader.close();
    return builder.toString();
  }

  /**
   * Compares the first <code>n</code> bytes from the given two buffers and returns true
   * iff they are equal.
   */
  private static boolean firstBytesEquals(byte[] buffer1, byte[] buffer2, int n) {
    for (int i = 0; i < n; ++i) {
      if (buffer1[i] != buffer2[i]) {
        return false;
      }
    }
    return true;
  }

}
