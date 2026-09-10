package edu.princeton.cs.algs4;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.NoSuchElementException;

public final class BinaryStdIn {
   private static final int EOF = -1;
   private static BufferedInputStream in;
   private static int buffer;
   private static int n;
   private static boolean isInitialized;

   private BinaryStdIn() {
   }

   private static void initialize() {
      in = new BufferedInputStream(System.in);
      buffer = 0;
      n = 0;
      fillBuffer();
      isInitialized = true;
   }

   private static void fillBuffer() {
      try {
         buffer = in.read();
         n = 8;
      } catch (IOException var1) {
         System.out.println("EOF");
         buffer = -1;
         n = -1;
      }

   }

   public static void close() {
      if (!isInitialized) {
         initialize();
      }

      try {
         in.close();
         isInitialized = false;
      } catch (IOException ioe) {
         throw new IllegalStateException("Could not close BinaryStdIn", ioe);
      }
   }

   public static boolean isEmpty() {
      if (!isInitialized) {
         initialize();
      }

      return buffer == -1;
   }

   public static boolean readBoolean() {
      if (isEmpty()) {
         throw new NoSuchElementException("Reading from empty input stream");
      } else {
         --n;
         boolean bit = (buffer >> n & 1) == 1;
         if (n == 0) {
            fillBuffer();
         }

         return bit;
      }
   }

   public static char readChar() {
      if (isEmpty()) {
         throw new NoSuchElementException("Reading from empty input stream");
      } else if (n == 8) {
         int x = buffer;
         fillBuffer();
         return (char)(x & 255);
      } else {
         int x = buffer;
         x <<= 8 - n;
         int oldN = n;
         fillBuffer();
         if (isEmpty()) {
            throw new NoSuchElementException("Reading from empty input stream");
         } else {
            n = oldN;
            x |= buffer >>> n;
            return (char)(x & 255);
         }
      }
   }

   public static char readChar(int r) {
      if (r >= 1 && r <= 16) {
         if (r == 8) {
            return readChar();
         } else {
            char x = 0;

            for(int i = 0; i < r; ++i) {
               x = (char)(x << 1);
               boolean bit = readBoolean();
               if (bit) {
                  x = (char)(x | 1);
               }
            }

            return x;
         }
      } else {
         throw new IllegalArgumentException("Illegal value of r = " + r);
      }
   }

   public static String readString() {
      if (isEmpty()) {
         throw new NoSuchElementException("Reading from empty input stream");
      } else {
         StringBuilder sb = new StringBuilder();

         while(!isEmpty()) {
            char c = readChar();
            sb.append(c);
         }

         return sb.toString();
      }
   }

   public static short readShort() {
      short x = 0;

      for(int i = 0; i < 2; ++i) {
         char c = readChar();
         x = (short)(x << 8);
         x = (short)(x | c);
      }

      return x;
   }

   public static int readInt() {
      int x = 0;

      for(int i = 0; i < 4; ++i) {
         char c = readChar();
         x <<= 8;
         x |= c;
      }

      return x;
   }

   public static int readInt(int r) {
      if (r >= 1 && r <= 32) {
         if (r == 32) {
            return readInt();
         } else {
            int x = 0;

            for(int i = 0; i < r; ++i) {
               x <<= 1;
               boolean bit = readBoolean();
               if (bit) {
                  x |= 1;
               }
            }

            return x;
         }
      } else {
         throw new IllegalArgumentException("Illegal value of r = " + r);
      }
   }

   public static long readLong() {
      long x = 0L;

      for(int i = 0; i < 8; ++i) {
         char c = readChar();
         x <<= 8;
         x |= (long)c;
      }

      return x;
   }

   public static double readDouble() {
      return Double.longBitsToDouble(readLong());
   }

   public static float readFloat() {
      return Float.intBitsToFloat(readInt());
   }

   public static byte readByte() {
      char c = readChar();
      return (byte)(c & 255);
   }

   public static void main(String[] args) {
      while(!isEmpty()) {
         char c = readChar();
         BinaryStdOut.write(c);
      }

      BinaryStdOut.flush();
   }
}
