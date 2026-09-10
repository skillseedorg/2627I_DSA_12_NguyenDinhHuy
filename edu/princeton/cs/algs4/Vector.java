package edu.princeton.cs.algs4;

public class Vector {
   private int d;
   private double[] data;

   public Vector(int d) {
      this.d = d;
      this.data = new double[d];
   }

   public Vector(double... a) {
      this.d = a.length;
      this.data = new double[this.d];

      for(int i = 0; i < this.d; ++i) {
         this.data[i] = a[i];
      }

   }

   /** @deprecated */
   @Deprecated
   public int length() {
      return this.d;
   }

   public int dimension() {
      return this.d;
   }

   public double dot(Vector that) {
      if (this.d != that.d) {
         throw new IllegalArgumentException("Dimensions don't agree");
      } else {
         double sum = (double)0.0F;

         for(int i = 0; i < this.d; ++i) {
            sum += this.data[i] * that.data[i];
         }

         return sum;
      }
   }

   public double magnitude() {
      return Math.sqrt(this.dot(this));
   }

   public double distanceTo(Vector that) {
      if (this.d != that.d) {
         throw new IllegalArgumentException("Dimensions don't agree");
      } else {
         return this.minus(that).magnitude();
      }
   }

   public Vector plus(Vector that) {
      if (this.d != that.d) {
         throw new IllegalArgumentException("Dimensions don't agree");
      } else {
         Vector c = new Vector(this.d);

         for(int i = 0; i < this.d; ++i) {
            c.data[i] = this.data[i] + that.data[i];
         }

         return c;
      }
   }

   public Vector minus(Vector that) {
      if (this.d != that.d) {
         throw new IllegalArgumentException("Dimensions don't agree");
      } else {
         Vector c = new Vector(this.d);

         for(int i = 0; i < this.d; ++i) {
            c.data[i] = this.data[i] - that.data[i];
         }

         return c;
      }
   }

   public double cartesian(int i) {
      return this.data[i];
   }

   /** @deprecated */
   @Deprecated
   public Vector times(double alpha) {
      Vector c = new Vector(this.d);

      for(int i = 0; i < this.d; ++i) {
         c.data[i] = alpha * this.data[i];
      }

      return c;
   }

   public Vector scale(double alpha) {
      Vector c = new Vector(this.d);

      for(int i = 0; i < this.d; ++i) {
         c.data[i] = alpha * this.data[i];
      }

      return c;
   }

   public Vector direction() {
      if (this.magnitude() == (double)0.0F) {
         throw new ArithmeticException("Zero-vector has no direction");
      } else {
         return this.times((double)1.0F / this.magnitude());
      }
   }

   public String toString() {
      StringBuilder s = new StringBuilder();

      for(int i = 0; i < this.d; ++i) {
         double var10001 = this.data[i];
         s.append(var10001 + " ");
      }

      return s.toString();
   }

   public static void main(String[] args) {
      double[] xdata = new double[]{(double)1.0F, (double)2.0F, (double)3.0F, (double)4.0F};
      double[] ydata = new double[]{(double)5.0F, (double)2.0F, (double)4.0F, (double)1.0F};
      Vector x = new Vector(xdata);
      Vector y = new Vector(ydata);
      StdOut.println("   x       = " + x);
      StdOut.println("   y       = " + y);
      Vector z = x.plus(y);
      StdOut.println("   z       = " + z);
      z = z.times((double)10.0F);
      StdOut.println(" 10z       = " + z);
      StdOut.println("  |x|      = " + x.magnitude());
      double var10000 = x.dot(y);
      StdOut.println(" <x, y>    = " + var10000);
      var10000 = x.distanceTo(y);
      StdOut.println("dist(x, y) = " + var10000);
      StdOut.println("dir(x)     = " + x.direction());
   }
}
