package edu.princeton.cs.algs4;

import java.util.Arrays;
import java.util.Comparator;

public class Interval1D {
   public static final Comparator<Interval1D> MIN_ENDPOINT_ORDER = new MinEndpointComparator();
   public static final Comparator<Interval1D> MAX_ENDPOINT_ORDER = new MaxEndpointComparator();
   public static final Comparator<Interval1D> LENGTH_ORDER = new LengthComparator();
   private final double min;
   private final double max;

   public Interval1D(double min, double max) {
      if (!Double.isInfinite(min) && !Double.isInfinite(max)) {
         if (!Double.isNaN(min) && !Double.isNaN(max)) {
            if (min == (double)0.0F) {
               min = (double)0.0F;
            }

            if (max == (double)0.0F) {
               max = (double)0.0F;
            }

            if (min <= max) {
               this.min = min;
               this.max = max;
            } else {
               throw new IllegalArgumentException("Illegal interval");
            }
         } else {
            throw new IllegalArgumentException("Endpoints cannot be NaN");
         }
      } else {
         throw new IllegalArgumentException("Endpoints must be finite");
      }
   }

   /** @deprecated */
   @Deprecated
   public double left() {
      return this.min;
   }

   /** @deprecated */
   @Deprecated
   public double right() {
      return this.max;
   }

   public double min() {
      return this.min;
   }

   public double max() {
      return this.max;
   }

   public boolean intersects(Interval1D that) {
      if (this.max < that.min) {
         return false;
      } else {
         return !(that.max < this.min);
      }
   }

   public boolean contains(Interval1D that) {
      return this.max >= that.max && this.min <= that.min;
   }

   public boolean contains(double x) {
      return this.min <= x && x <= this.max;
   }

   public double length() {
      return this.max - this.min;
   }

   public String toString() {
      return "[" + this.min + ", " + this.max + "]";
   }

   public boolean equals(Object other) {
      if (other == this) {
         return true;
      } else if (other == null) {
         return false;
      } else if (other.getClass() != this.getClass()) {
         return false;
      } else {
         Interval1D that = (Interval1D)other;
         return this.min == that.min && this.max == that.max;
      }
   }

   public int hashCode() {
      int hash1 = Double.valueOf(this.min).hashCode();
      int hash2 = Double.valueOf(this.max).hashCode();
      return 31 * hash1 + hash2;
   }

   public static void main(String[] args) {
      Interval1D[] intervals = new Interval1D[]{new Interval1D((double)15.0F, (double)33.0F), new Interval1D((double)45.0F, (double)60.0F), new Interval1D((double)20.0F, (double)70.0F), new Interval1D((double)46.0F, (double)55.0F)};
      StdOut.println("Unsorted");

      for(int i = 0; i < intervals.length; ++i) {
         StdOut.println(intervals[i]);
      }

      StdOut.println();
      StdOut.println("Sort by min endpoint");
      Arrays.sort(intervals, MIN_ENDPOINT_ORDER);

      for(int i = 0; i < intervals.length; ++i) {
         StdOut.println(intervals[i]);
      }

      StdOut.println();
      StdOut.println("Sort by max endpoint");
      Arrays.sort(intervals, MAX_ENDPOINT_ORDER);

      for(int i = 0; i < intervals.length; ++i) {
         StdOut.println(intervals[i]);
      }

      StdOut.println();
      StdOut.println("Sort by length");
      Arrays.sort(intervals, LENGTH_ORDER);

      for(int i = 0; i < intervals.length; ++i) {
         StdOut.println(intervals[i]);
      }

      StdOut.println();
   }

   private static class MinEndpointComparator implements Comparator<Interval1D> {
      public int compare(Interval1D a, Interval1D b) {
         if (a.min < b.min) {
            return -1;
         } else if (a.min > b.min) {
            return 1;
         } else if (a.max < b.max) {
            return -1;
         } else {
            return a.max > b.max ? 1 : 0;
         }
      }
   }

   private static class MaxEndpointComparator implements Comparator<Interval1D> {
      public int compare(Interval1D a, Interval1D b) {
         if (a.max < b.max) {
            return -1;
         } else if (a.max > b.max) {
            return 1;
         } else if (a.min < b.min) {
            return -1;
         } else {
            return a.min > b.min ? 1 : 0;
         }
      }
   }

   private static class LengthComparator implements Comparator<Interval1D> {
      public int compare(Interval1D a, Interval1D b) {
         double alen = a.length();
         double blen = b.length();
         if (alen < blen) {
            return -1;
         } else {
            return alen > blen ? 1 : 0;
         }
      }
   }
}
