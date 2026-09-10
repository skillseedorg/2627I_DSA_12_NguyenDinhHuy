package edu.princeton.cs.algs4;

public class BinaryDump {
   private BinaryDump() {
   }

   public static void main(String[] args) {
      int bitsPerLine = 16;
      if (args.length == 1) {
         bitsPerLine = Integer.parseInt(args[0]);
      }

      int count;
      for(count = 0; !BinaryStdIn.isEmpty(); ++count) {
         if (bitsPerLine == 0) {
            BinaryStdIn.readBoolean();
         } else {
            if (count != 0 && count % bitsPerLine == 0) {
               StdOut.println();
            }

            if (BinaryStdIn.readBoolean()) {
               StdOut.print((int)1);
            } else {
               StdOut.print((int)0);
            }
         }
      }

      if (bitsPerLine != 0) {
         StdOut.println();
      }

      StdOut.println(count + " bits");
   }
}
