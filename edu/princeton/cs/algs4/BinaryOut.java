package edu.princeton.cs.algs4;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public final class BinaryOut {
   private BufferedOutputStream out;
   private int buffer;
   private int n;

   public BinaryOut() {
      this.out = new BufferedOutputStream(System.out);
   }

   public BinaryOut(OutputStream os) {
      this.out = new BufferedOutputStream(os);
   }

   public BinaryOut(String filename) {
      if (filename == null) {
         throw new IllegalArgumentException("filename argument is null");
      } else if (filename.length() == 0) {
         throw new IllegalArgumentException("filename argument is the empty string");
      } else {
         try {
            OutputStream os = new FileOutputStream(filename);
            this.out = new BufferedOutputStream(os);
         } catch (IOException e) {
            throw new IllegalArgumentException("could not create file '" + filename + "' for writing", e);
         }
      }
   }

   public BinaryOut(Socket socket) {
      if (socket == null) {
         throw new IllegalArgumentException("socket argument is null");
      } else {
         try {
            OutputStream os = socket.getOutputStream();
            this.out = new BufferedOutputStream(os);
         } catch (IOException e) {
            throw new IllegalArgumentException("could not create output stream from socket", e);
         }
      }
   }

   private void writeBit(boolean x) {
      this.buffer <<= 1;
      if (x) {
         this.buffer |= 1;
      }

      ++this.n;
      if (this.n == 8) {
         this.clearBuffer();
      }

   }

   private void writeByte(int x) {
      assert x >= 0 && x < 256;

      if (this.n == 0) {
         try {
            this.out.write(x);
         } catch (IOException e) {
            e.printStackTrace();
         }

      } else {
         for(int i = 0; i < 8; ++i) {
            boolean bit = (x >>> 8 - i - 1 & 1) == 1;
            this.writeBit(bit);
         }

      }
   }

   private void clearBuffer() {
      if (this.n != 0) {
         if (this.n > 0) {
            this.buffer <<= 8 - this.n;
         }

         try {
            this.out.write(this.buffer);
         } catch (IOException e) {
            e.printStackTrace();
         }

         this.n = 0;
         this.buffer = 0;
      }
   }

   public void flush() {
      this.clearBuffer();

      try {
         this.out.flush();
      } catch (IOException e) {
         e.printStackTrace();
      }

   }

   public void close() {
      this.flush();

      try {
         this.out.close();
      } catch (IOException e) {
         e.printStackTrace();
      }

   }

   public void write(boolean x) {
      this.writeBit(x);
   }

   public void write(byte x) {
      this.writeByte(x & 255);
   }

   public void write(int x) {
      this.writeByte(x >>> 24 & 255);
      this.writeByte(x >>> 16 & 255);
      this.writeByte(x >>> 8 & 255);
      this.writeByte(x >>> 0 & 255);
   }

   public void write(int x, int r) {
      if (r == 32) {
         this.write(x);
      } else if (r >= 1 && r <= 32) {
         if (x >= 1 << r) {
            throw new IllegalArgumentException("Illegal " + r + "-bit char = " + x);
         } else {
            for(int i = 0; i < r; ++i) {
               boolean bit = (x >>> r - i - 1 & 1) == 1;
               this.writeBit(bit);
            }

         }
      } else {
         throw new IllegalArgumentException("Illegal value for r = " + r);
      }
   }

   public void write(double x) {
      this.write(Double.doubleToRawLongBits(x));
   }

   public void write(long x) {
      this.writeByte((int)(x >>> 56 & 255L));
      this.writeByte((int)(x >>> 48 & 255L));
      this.writeByte((int)(x >>> 40 & 255L));
      this.writeByte((int)(x >>> 32 & 255L));
      this.writeByte((int)(x >>> 24 & 255L));
      this.writeByte((int)(x >>> 16 & 255L));
      this.writeByte((int)(x >>> 8 & 255L));
      this.writeByte((int)(x >>> 0 & 255L));
   }

   public void write(float x) {
      this.write(Float.floatToRawIntBits(x));
   }

   public void write(short x) {
      this.writeByte(x >>> 8 & 255);
      this.writeByte(x >>> 0 & 255);
   }

   public void write(char x) {
      if (x >= 256) {
         throw new IllegalArgumentException("Illegal 8-bit char = " + x);
      } else {
         this.writeByte(x);
      }
   }

   public void write(char x, int r) {
      if (r == 8) {
         this.write(x);
      } else if (r >= 1 && r <= 16) {
         if (x >= 1 << r) {
            throw new IllegalArgumentException("Illegal " + r + "-bit char = " + x);
         } else {
            for(int i = 0; i < r; ++i) {
               boolean bit = (x >>> r - i - 1 & 1) == 1;
               this.writeBit(bit);
            }

         }
      } else {
         throw new IllegalArgumentException("Illegal value for r = " + r);
      }
   }

   public void write(String s) {
      for(int i = 0; i < s.length(); ++i) {
         this.write(s.charAt(i));
      }

   }

   public void write(String s, int r) {
      for(int i = 0; i < s.length(); ++i) {
         this.write(s.charAt(i), r);
      }

   }

   public static void main(String[] args) {
      String filename = args[0];
      BinaryOut out = new BinaryOut(filename);
      BinaryIn in = new BinaryIn();

      while(!in.isEmpty()) {
         char c = in.readChar();
         out.write(c);
      }

      out.flush();
   }
}
