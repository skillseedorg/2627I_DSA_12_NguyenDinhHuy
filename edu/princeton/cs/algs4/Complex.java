package edu.princeton.cs.algs4;

public class Complex {
   private final double re;
   private final double im;

   public Complex(double real, double imag) {
      this.re = real;
      this.im = imag;
   }

   public String toString() {
      if (this.im == (double)0.0F) {
         return "" + this.re;
      } else if (this.re == (double)0.0F) {
         return this.im + "i";
      } else {
         return this.im < (double)0.0F ? this.re + " - " + -this.im + "i" : this.re + " + " + this.im + "i";
      }
   }

   public double abs() {
      return Math.hypot(this.re, this.im);
   }

   public double phase() {
      return Math.atan2(this.im, this.re);
   }

   public Complex plus(Complex that) {
      double real = this.re + that.re;
      double imag = this.im + that.im;
      return new Complex(real, imag);
   }

   public Complex minus(Complex that) {
      double real = this.re - that.re;
      double imag = this.im - that.im;
      return new Complex(real, imag);
   }

   public Complex times(Complex that) {
      double real = this.re * that.re - this.im * that.im;
      double imag = this.re * that.im + this.im * that.re;
      return new Complex(real, imag);
   }

   public Complex scale(double alpha) {
      return new Complex(alpha * this.re, alpha * this.im);
   }

   /** @deprecated */
   @Deprecated
   public Complex times(double alpha) {
      return new Complex(alpha * this.re, alpha * this.im);
   }

   public Complex conjugate() {
      return new Complex(this.re, -this.im);
   }

   public Complex reciprocal() {
      double scale = this.re * this.re + this.im * this.im;
      return new Complex(this.re / scale, -this.im / scale);
   }

   public double re() {
      return this.re;
   }

   public double im() {
      return this.im;
   }

   public Complex divides(Complex that) {
      return this.times(that.reciprocal());
   }

   public Complex exp() {
      return new Complex(Math.exp(this.re) * Math.cos(this.im), Math.exp(this.re) * Math.sin(this.im));
   }

   public Complex sin() {
      return new Complex(Math.sin(this.re) * Math.cosh(this.im), Math.cos(this.re) * Math.sinh(this.im));
   }

   public Complex cos() {
      return new Complex(Math.cos(this.re) * Math.cosh(this.im), -Math.sin(this.re) * Math.sinh(this.im));
   }

   public Complex tan() {
      return this.sin().divides(this.cos());
   }

   public static void main(String[] args) {
      Complex a = new Complex((double)5.0F, (double)6.0F);
      Complex b = new Complex((double)-3.0F, (double)4.0F);
      StdOut.println("a            = " + a);
      StdOut.println("b            = " + b);
      StdOut.println("Re(a)        = " + a.re());
      StdOut.println("Im(a)        = " + a.im());
      Complex var10000 = b.plus(a);
      StdOut.println("b + a        = " + var10000);
      var10000 = a.minus(b);
      StdOut.println("a - b        = " + var10000);
      var10000 = a.times(b);
      StdOut.println("a * b        = " + var10000);
      var10000 = b.times(a);
      StdOut.println("b * a        = " + var10000);
      var10000 = a.divides(b);
      StdOut.println("a / b        = " + var10000);
      StdOut.println("(a / b) * b  = " + a.divides(b).times(b));
      StdOut.println("conj(a)      = " + a.conjugate());
      StdOut.println("|a|          = " + a.abs());
      StdOut.println("tan(a)       = " + a.tan());
   }
}
