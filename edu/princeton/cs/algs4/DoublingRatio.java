package edu.princeton.cs.algs4;

public class DoublingRatio {
   private static final int MAXIMUM_INTEGER = 1000000;

   private DoublingRatio() {
   }

   public static double timeTrial(int n) {
      int[] a = new int[n];

      for(int i = 0; i < n; ++i) {
         a[i] = StdRandom.uniformInt(-1000000, 1000000);
      }

      Stopwatch timer = new Stopwatch();
      int ignore = ThreeSum.count(a);
      return timer.elapsedTime();
   }

   public static void main(String[] args) {
      double prev = timeTrial(125);
      int n = 250;

      while(true) {
         double time = timeTrial(n);
         StdOut.printf("%7d %7.1f %5.1f\n", n, time, time / prev);
         prev = time;
         n += n;
      }
   }
}
