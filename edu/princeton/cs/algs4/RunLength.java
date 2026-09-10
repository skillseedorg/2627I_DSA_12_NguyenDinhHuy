package edu.princeton.cs.algs4;

public class RunLength {
   private static final int R = 256;
   private static final int LG_R = 8;

   private RunLength() {
   }

   public static void expand() {
      for(boolean b = false; !BinaryStdIn.isEmpty(); b = !b) {
         int run = BinaryStdIn.readInt(8);

         for(int i = 0; i < run; ++i) {
            BinaryStdOut.write(b);
         }
      }

      BinaryStdOut.close();
   }

   public static void compress() {
      char run = 0;
      boolean old = false;

      while(!BinaryStdIn.isEmpty()) {
         boolean b = BinaryStdIn.readBoolean();
         if (b != old) {
            BinaryStdOut.write((char)run, 8);
            run = 1;
            old = !old;
         } else {
            if (run == 255) {
               BinaryStdOut.write((char)run, 8);
               run = 0;
               BinaryStdOut.write((char)run, 8);
            }

            ++run;
         }
      }

      BinaryStdOut.write((char)run, 8);
      BinaryStdOut.close();
   }

   public static void main(String[] args) {
      if (args[0].equals("-")) {
         compress();
      } else {
         if (!args[0].equals("+")) {
            throw new IllegalArgumentException("Illegal command line argument");
         }

         expand();
      }

   }
}
