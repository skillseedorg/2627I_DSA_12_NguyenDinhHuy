package edu.princeton.cs.algs4;

import java.util.Arrays;

public class ClosestPair {
   private Point2D best1;
   private Point2D best2;
   private double bestDistance = Double.POSITIVE_INFINITY;

   public ClosestPair(Point2D[] points) {
      if (points == null) {
         throw new IllegalArgumentException("constructor argument is null");
      } else {
         for(int i = 0; i < points.length; ++i) {
            if (points[i] == null) {
               throw new IllegalArgumentException("array element " + i + " is null");
            }
         }

         int n = points.length;
         if (n > 1) {
            Point2D[] pointsByX = new Point2D[n];

            for(int i = 0; i < n; ++i) {
               pointsByX[i] = points[i];
            }

            Arrays.sort(pointsByX, Point2D.Y_ORDER);
            Arrays.sort(pointsByX, Point2D.X_ORDER);

            for(int i = 0; i < n - 1; ++i) {
               if (pointsByX[i].equals(pointsByX[i + 1])) {
                  this.bestDistance = (double)0.0F;
                  this.best1 = pointsByX[i];
                  this.best2 = pointsByX[i + 1];
                  return;
               }
            }

            Point2D[] pointsByY = new Point2D[n];

            for(int i = 0; i < n; ++i) {
               pointsByY[i] = pointsByX[i];
            }

            Point2D[] aux = new Point2D[n];
            this.closest(pointsByX, pointsByY, aux, 0, n - 1);
         }
      }
   }

   private double closest(Point2D[] pointsByX, Point2D[] pointsByY, Point2D[] aux, int lo, int hi) {
      if (hi <= lo) {
         return Double.POSITIVE_INFINITY;
      } else {
         int mid = lo + (hi - lo) / 2;
         Point2D median = pointsByX[mid];
         double delta1 = this.closest(pointsByX, pointsByY, aux, lo, mid);
         double delta2 = this.closest(pointsByX, pointsByY, aux, mid + 1, hi);
         double delta = Math.min(delta1, delta2);
         merge(pointsByY, aux, lo, mid, hi);
         int m = 0;

         for(int i = lo; i <= hi; ++i) {
            if (Math.abs(pointsByY[i].x() - median.x()) < delta) {
               aux[m++] = pointsByY[i];
            }
         }

         for(int i = 0; i < m; ++i) {
            for(int j = i + 1; j < m && aux[j].y() - aux[i].y() < delta; ++j) {
               double distance = aux[i].distanceTo(aux[j]);
               if (distance < delta) {
                  delta = distance;
                  if (distance < this.bestDistance) {
                     this.bestDistance = distance;
                     this.best1 = aux[i];
                     this.best2 = aux[j];
                  }
               }
            }
         }

         return delta;
      }
   }

   public Point2D either() {
      return this.best1;
   }

   public Point2D other() {
      return this.best2;
   }

   public double distance() {
      return this.bestDistance;
   }

   private static boolean less(Comparable v, Comparable w) {
      return v.compareTo(w) < 0;
   }

   private static void merge(Comparable[] a, Comparable[] aux, int lo, int mid, int hi) {
      for(int k = lo; k <= hi; ++k) {
         aux[k] = a[k];
      }

      int i = lo;
      int j = mid + 1;

      for(int k = lo; k <= hi; ++k) {
         if (i > mid) {
            a[k] = aux[j++];
         } else if (j > hi) {
            a[k] = aux[i++];
         } else if (less(aux[j], aux[i])) {
            a[k] = aux[j++];
         } else {
            a[k] = aux[i++];
         }
      }

   }

   public static void main(String[] args) {
      int n = StdIn.readInt();
      Point2D[] points = new Point2D[n];

      for(int i = 0; i < n; ++i) {
         double x = StdIn.readDouble();
         double y = StdIn.readDouble();
         points[i] = new Point2D(x, y);
      }

      ClosestPair closest = new ClosestPair(points);
      double var10000 = closest.distance();
      StdOut.println(var10000 + " from " + closest.either() + " to " + closest.other());
   }
}
