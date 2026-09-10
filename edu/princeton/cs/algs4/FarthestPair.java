package edu.princeton.cs.algs4;

public class FarthestPair {
   private Point2D best1;
   private Point2D best2;
   private double bestDistanceSquared = Double.NEGATIVE_INFINITY;

   public FarthestPair(Point2D[] points) {
      if (points == null) {
         throw new IllegalArgumentException("constructor argument is null");
      } else {
         for(int i = 0; i < points.length; ++i) {
            if (points[i] == null) {
               throw new IllegalArgumentException("array element " + i + " is null");
            }
         }

         GrahamScan graham = new GrahamScan(points);
         if (points.length > 1) {
            int m = 0;

            for(Point2D p : graham.hull()) {
               ++m;
            }

            Point2D[] hull = new Point2D[m + 1];
            m = 1;

            for(Point2D p : graham.hull()) {
               hull[m++] = p;
            }

            --m;
            if (m != 1) {
               if (m == 2) {
                  this.best1 = hull[1];
                  this.best2 = hull[2];
                  this.bestDistanceSquared = this.best1.distanceSquaredTo(this.best2);
               } else {
                  int k;
                  for(k = 2; Point2D.area2(hull[m], hull[1], hull[k + 1]) > Point2D.area2(hull[m], hull[1], hull[k]); ++k) {
                  }

                  int j = k;

                  for(int i = 1; i <= k && j <= m; ++i) {
                     if (hull[i].distanceSquaredTo(hull[j]) > this.bestDistanceSquared) {
                        this.best1 = hull[i];
                        this.best2 = hull[j];
                        this.bestDistanceSquared = hull[i].distanceSquaredTo(hull[j]);
                     }

                     while(j < m && Point2D.area2(hull[i], hull[i + 1], hull[j + 1]) > Point2D.area2(hull[i], hull[i + 1], hull[j])) {
                        ++j;
                        double distanceSquared = hull[i].distanceSquaredTo(hull[j]);
                        if (distanceSquared > this.bestDistanceSquared) {
                           this.best1 = hull[i];
                           this.best2 = hull[j];
                           this.bestDistanceSquared = hull[i].distanceSquaredTo(hull[j]);
                        }
                     }
                  }

               }
            }
         }
      }
   }

   public Point2D either() {
      return this.best1;
   }

   public Point2D other() {
      return this.best2;
   }

   public double distance() {
      return Math.sqrt(this.bestDistanceSquared);
   }

   public static void main(String[] args) {
      int n = StdIn.readInt();
      Point2D[] points = new Point2D[n];

      for(int i = 0; i < n; ++i) {
         int x = StdIn.readInt();
         int y = StdIn.readInt();
         points[i] = new Point2D((double)x, (double)y);
      }

      FarthestPair farthest = new FarthestPair(points);
      double var10000 = farthest.distance();
      StdOut.println(var10000 + " from " + farthest.either() + " to " + farthest.other());
   }
}
