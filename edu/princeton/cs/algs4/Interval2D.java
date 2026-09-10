package edu.princeton.cs.algs4;

public class Interval2D {
   private final Interval1D x;
   private final Interval1D y;

   public Interval2D(Interval1D x, Interval1D y) {
      this.x = x;
      this.y = y;
   }

   public boolean intersects(Interval2D that) {
      if (!this.x.intersects(that.x)) {
         return false;
      } else {
         return this.y.intersects(that.y);
      }
   }

   public boolean contains(Interval2D that) {
      return this.x.contains(that.x) && this.y.contains(that.y);
   }

   public boolean contains(Point2D p) {
      return this.x.contains(p.x()) && this.y.contains(p.y());
   }

   public double area() {
      return this.x.length() * this.y.length();
   }

   public String toString() {
      return this.x + " x " + this.y;
   }

   public boolean equals(Object other) {
      if (other == this) {
         return true;
      } else if (other == null) {
         return false;
      } else if (other.getClass() != this.getClass()) {
         return false;
      } else {
         Interval2D that = (Interval2D)other;
         return this.x.equals(that.x) && this.y.equals(that.y);
      }
   }

   public int hashCode() {
      int hash1 = this.x.hashCode();
      int hash2 = this.y.hashCode();
      return 31 * hash1 + hash2;
   }

   public void draw() {
      double xc = (this.x.min() + this.x.max()) / (double)2.0F;
      double yc = (this.y.min() + this.y.max()) / (double)2.0F;
      StdDraw.rectangle(xc, yc, this.x.length() / (double)2.0F, this.y.length() / (double)2.0F);
   }

   public static void main(String[] args) {
      double xmin = Double.parseDouble(args[0]);
      double xmax = Double.parseDouble(args[1]);
      double ymin = Double.parseDouble(args[2]);
      double ymax = Double.parseDouble(args[3]);
      int trials = Integer.parseInt(args[4]);
      Interval1D xInterval = new Interval1D(xmin, xmax);
      Interval1D yInterval = new Interval1D(ymin, ymax);
      Interval2D box = new Interval2D(xInterval, yInterval);
      box.draw();
      Counter counter = new Counter("hits");

      for(int t = 0; t < trials; ++t) {
         double x = StdRandom.uniformDouble((double)0.0F, (double)1.0F);
         double y = StdRandom.uniformDouble((double)0.0F, (double)1.0F);
         Point2D point = new Point2D(x, y);
         if (box.contains(point)) {
            counter.increment();
         } else {
            point.draw();
         }
      }

      StdOut.println(counter);
      StdOut.printf("box area = %.2f\n", box.area());
   }
}
