package edu.princeton.cs.algs4;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class StopwatchCPU {
   private static final double NANOSECONDS_PER_SECOND = (double)1.0E9F;
   private final ThreadMXBean threadTimer = ManagementFactory.getThreadMXBean();
   private final long start;

   public StopwatchCPU() {
      this.start = this.threadTimer.getCurrentThreadCpuTime();
   }

   public double elapsedTime() {
      long now = this.threadTimer.getCurrentThreadCpuTime();
      return (double)(now - this.start) / (double)1.0E9F;
   }

   public static void main(String[] args) {
      int n = Integer.parseInt(args[0]);
      StopwatchCPU timer1 = new StopwatchCPU();
      double sum1 = (double)0.0F;

      for(int i = 1; i <= n; ++i) {
         sum1 += Math.sqrt((double)i);
      }

      double time1 = timer1.elapsedTime();
      StdOut.printf("%e (%.2f seconds)\n", sum1, time1);
      StopwatchCPU timer2 = new StopwatchCPU();
      double sum2 = (double)0.0F;

      for(int i = 1; i <= n; ++i) {
         sum2 += Math.pow((double)i, (double)0.5F);
      }

      double time2 = timer2.elapsedTime();
      StdOut.printf("%e (%.2f seconds)\n", sum2, time2);
   }
}
