package edu.princeton.cs.algs4;

import java.io.BufferedOutputStream;
import java.io.IOException;

public final class BinaryStdOut {
   private static BufferedOutputStream out;
   private static int buffer;
   private static int n;
   private static boolean isInitialized;

   private BinaryStdOut() {
   }

   private static void initialize() {
      out = new BufferedOutputStream(System.out);
      buffer = 0;
      n = 0;
      isInitialized = true;
   }

   private static void writeBit(boolean bit) {
      if (!isInitialized) {
         initialize();
      }

      buffer <<= 1;
      if (bit) {
         buffer |= 1;
      }

      ++n;
      if (n == 8) {
         clearBuffer();
      }

   }

   private static void writeByte(int x) {
      if (!isInitialized) {
         initialize();
      }

      assert x >= 0 && x < 256;

      if (n == 0) {
         try {
            out.write(x);
         } catch (IOException e) {
            e.printStackTrace();
         }

      } else {
         for(int i = 0; i < 8; ++i) {
            boolean bit = (x >>> 8 - i - 1 & 1) == 1;
            writeBit(bit);
         }

      }
   }

   private static void clearBuffer() {
      if (!isInitialized) {
         initialize();
      }

      if (n != 0) {
         if (n > 0) {
            buffer <<= 8 - n;
         }

         try {
            out.write(buffer);
         } catch (IOException e) {
            e.printStackTrace();
         }

         n = 0;
         buffer = 0;
      }
   }

   public static void flush() {
      clearBuffer();

      try {
         out.flush();
      } catch (IOException e) {
         e.printStackTrace();
      }

   }

   public static void close() {
      flush();

      try {
         out.close();
         isInitialized = false;
      } catch (IOException e) {
         e.printStackTrace();
      }

   }

   public static void write(boolean x) {
      writeBit(x);
   }

   public static void write(byte x) {
      writeByte(x & 255);
   }

   public static void write(int x) {
      writeByte(x >>> 24 & 255);
      writeByte(x >>> 16 & 255);
      writeByte(x >>> 8 & 255);
      writeByte(x >>> 0 & 255);
   }

   public static void write(int x, int r) {
      if (r == 32) {
         write(x);
      } else if (r >= 1 && r <= 32) {
         if (x >= 0 && x < 1 << r) {
            for(int i = 0; i < r; ++i) {
               boolean bit = (x >>> r - i - 1 & 1) == 1;
               writeBit(bit);
            }

         } else {
            throw new IllegalArgumentException("Illegal " + r + "-bit char = " + x);
         }
      } else {
         throw new IllegalArgumentException("Illegal value for r = " + r);
      }
   }

   public static void write(double x) {
      write(Double.doubleToRawLongBits(x));
   }

   public static void write(long x) {
      writeByte((int)(x >>> 56 & 255L));
      writeByte((int)(x >>> 48 & 255L));
      writeByte((int)(x >>> 40 & 255L));
      writeByte((int)(x >>> 32 & 255L));
      writeByte((int)(x >>> 24 & 255L));
      writeByte((int)(x >>> 16 & 255L));
      writeByte((int)(x >>> 8 & 255L));
      writeByte((int)(x >>> 0 & 255L));
   }

   public static void write(float x) {
      write(Float.floatToRawIntBits(x));
   }

   public static void write(short x) {
      writeByte(x >>> 8 & 255);
      writeByte(x >>> 0 & 255);
   }

   public static void write(char x) {
      if (x >= 0 && x < 256) {
         writeByte(x);
      } else {
         throw new IllegalArgumentException("Illegal 8-bit char = " + x);
      }
   }

   public static void write(char x, int r) {
      if (r == 8) {
         write(x);
      } else if (r >= 1 && r <= 16) {
         if (x >= 1 << r) {
            throw new IllegalArgumentException("Illegal " + r + "-bit char = " + x);
         } else {
            for(int i = 0; i < r; ++i) {
               boolean bit = (x >>> r - i - 1 & 1) == 1;
               writeBit(bit);
            }

         }
      } else {
         throw new IllegalArgumentException("Illegal value for r = " + r);
      }
   }

   public static void write(String s) {
      for(int i = 0; i < s.length(); ++i) {
         write(s.charAt(i));
      }

   }

   public static void write(String s, int r) {
      for(int i = 0; i < s.length(); ++i) {
         write(s.charAt(i), r);
      }

   }

   public static void main(String[] args) {
      int m = Integer.parseInt(args[0]);

      for(int i = 0; i < m; ++i) {
         write(i);
      }

      flush();
   }
}
