package edu.princeton.cs.algs4;

public class Average {
   private Average() {
   }

   public static void main(String[] args) {
      int count = 0;

      double sum;
      for(sum = (double)0.0F; !StdIn.isEmpty(); ++count) {
         double value = StdIn.readDouble();
         sum += value;
      }

      double average = sum / (double)count;
      StdOut.println("Average is " + average);
   }
}
