package edu.princeton.cs.algs4;

public class RandomSeq {
   private RandomSeq() {
   }

   public static void main(String[] args) {
      int n = Integer.parseInt(args[0]);
      if (args.length == 1) {
         for(int i = 0; i < n; ++i) {
            double x = StdRandom.uniformDouble((double)0.0F, (double)1.0F);
            StdOut.println(x);
         }
      } else {
         if (args.length != 3) {
            throw new IllegalArgumentException("Invalid number of arguments");
         }

         double lo = Double.parseDouble(args[1]);
         double hi = Double.parseDouble(args[2]);

         for(int i = 0; i < n; ++i) {
            double x = StdRandom.uniformDouble(lo, hi);
            StdOut.printf("%.2f\n", x);
         }
      }

   }
}
