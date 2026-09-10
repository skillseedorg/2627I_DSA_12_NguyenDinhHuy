package edu.princeton.cs.algs4;

public final class RectHV {
   private final double xmin;
   private final double ymin;
   private final double xmax;
   private final double ymax;

   public RectHV(double xmin, double ymin, double xmax, double ymax) {
      this.xmin = xmin;
      this.ymin = ymin;
      this.xmax = xmax;
      this.ymax = ymax;
      if (!Double.isNaN(xmin) && !Double.isNaN(xmax)) {
         if (!Double.isNaN(ymin) && !Double.isNaN(ymax)) {
            if (xmax < xmin) {
               throw new IllegalArgumentException("xmax < xmin: " + this.toString());
            } else if (ymax < ymin) {
               throw new IllegalArgumentException("ymax < ymin: " + this.toString());
            }
         } else {
            throw new IllegalArgumentException("y-coordinate is NaN: " + this.toString());
         }
      } else {
         throw new IllegalArgumentException("x-coordinate is NaN: " + this.toString());
      }
   }

   public double xmin() {
      return this.xmin;
   }

   public double xmax() {
      return this.xmax;
   }

   public double ymin() {
      return this.ymin;
   }

   public double ymax() {
      return this.ymax;
   }

   public double width() {
      return this.xmax - this.xmin;
   }

   public double height() {
      return this.ymax - this.ymin;
   }

   public boolean intersects(RectHV that) {
      return this.xmax >= that.xmin && this.ymax >= that.ymin && that.xmax >= this.xmin && that.ymax >= this.ymin;
   }

   public boolean contains(Point2D p) {
      return p.x() >= this.xmin && p.x() <= this.xmax && p.y() >= this.ymin && p.y() <= this.ymax;
   }

   public double distanceTo(Point2D p) {
      return Math.sqrt(this.distanceSquaredTo(p));
   }

   public double distanceSquaredTo(Point2D p) {
      double dx = (double)0.0F;
      double dy = (double)0.0F;
      if (p.x() < this.xmin) {
         dx = p.x() - this.xmin;
      } else if (p.x() > this.xmax) {
         dx = p.x() - this.xmax;
      }

      if (p.y() < this.ymin) {
         dy = p.y() - this.ymin;
      } else if (p.y() > this.ymax) {
         dy = p.y() - this.ymax;
      }

      return dx * dx + dy * dy;
   }

   public boolean equals(Object other) {
      if (other == this) {
         return true;
      } else if (other == null) {
         return false;
      } else if (other.getClass() != this.getClass()) {
         return false;
      } else {
         RectHV that = (RectHV)other;
         if (this.xmin != that.xmin) {
            return false;
         } else if (this.ymin != that.ymin) {
            return false;
         } else if (this.xmax != that.xmax) {
            return false;
         } else {
            return this.ymax == that.ymax;
         }
      }
   }

   public int hashCode() {
      int hash1 = Double.valueOf(this.xmin).hashCode();
      int hash2 = Double.valueOf(this.ymin).hashCode();
      int hash3 = Double.valueOf(this.xmax).hashCode();
      int hash4 = Double.valueOf(this.ymax).hashCode();
      return 31 * (31 * (31 * hash1 + hash2) + hash3) + hash4;
   }

   public String toString() {
      return "[" + this.xmin + ", " + this.xmax + "] x [" + this.ymin + ", " + this.ymax + "]";
   }

   public void draw() {
      StdDraw.line(this.xmin, this.ymin, this.xmax, this.ymin);
      StdDraw.line(this.xmax, this.ymin, this.xmax, this.ymax);
      StdDraw.line(this.xmax, this.ymax, this.xmin, this.ymax);
      StdDraw.line(this.xmin, this.ymax, this.xmin, this.ymin);
   }
}
