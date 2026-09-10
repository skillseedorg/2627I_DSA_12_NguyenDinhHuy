package edu.princeton.cs.algs4;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class Out {
   private static final Charset CHARSET;
   private static final Locale LOCALE;
   private PrintWriter out;

   public Out(OutputStream os) {
      OutputStreamWriter osw = new OutputStreamWriter(os, CHARSET);
      this.out = new PrintWriter(osw, true);
   }

   public Out() {
      this((OutputStream)System.out);
   }

   public Out(Socket socket) {
      if (socket == null) {
         throw new IllegalArgumentException("socket argument is null");
      } else {
         try {
            OutputStream os = socket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, CHARSET);
            this.out = new PrintWriter(osw, true);
         } catch (IOException e) {
            throw new IllegalArgumentException("could not create output stream from socket", e);
         }
      }
   }

   public Out(String filename) {
      if (filename == null) {
         throw new IllegalArgumentException("filename argument is null");
      } else if (filename.length() == 0) {
         throw new IllegalArgumentException("filename argument is the empty string");
      } else {
         try {
            OutputStream os = new FileOutputStream(filename);
            OutputStreamWriter osw = new OutputStreamWriter(os, CHARSET);
            this.out = new PrintWriter(osw, true);
         } catch (IOException e) {
            throw new IllegalArgumentException("could not create file '" + filename + "' for writing", e);
         }
      }
   }

   public void close() {
      this.out.close();
   }

   public void println() {
      this.out.println();
   }

   public void println(Object x) {
      this.out.println(x);
   }

   public void println(boolean x) {
      this.out.println(x);
   }

   public void println(char x) {
      this.out.println(x);
   }

   public void println(double x) {
      this.out.println(x);
   }

   public void println(float x) {
      this.out.println(x);
   }

   public void println(int x) {
      this.out.println(x);
   }

   public void println(long x) {
      this.out.println(x);
   }

   public void println(byte x) {
      this.out.println(x);
   }

   public void print() {
      this.out.flush();
   }

   public void print(Object x) {
      this.out.print(x);
      this.out.flush();
   }

   public void print(boolean x) {
      this.out.print(x);
      this.out.flush();
   }

   public void print(char x) {
      this.out.print(x);
      this.out.flush();
   }

   public void print(double x) {
      this.out.print(x);
      this.out.flush();
   }

   public void print(float x) {
      this.out.print(x);
      this.out.flush();
   }

   public void print(int x) {
      this.out.print(x);
      this.out.flush();
   }

   public void print(long x) {
      this.out.print(x);
      this.out.flush();
   }

   public void print(byte x) {
      this.out.print(x);
      this.out.flush();
   }

   public void printf(String format, Object... args) {
      this.out.printf(LOCALE, format, args);
      this.out.flush();
   }

   public void printf(Locale locale, String format, Object... args) {
      this.out.printf(locale, format, args);
      this.out.flush();
   }

   public static void main(String[] args) {
      Out out = new Out();
      out.println("Test 1");
      out.close();
      out = new Out("test.txt");
      out.println("Test 2");
      out.close();
   }

   static {
      CHARSET = StandardCharsets.UTF_8;
      LOCALE = Locale.US;
   }
}
