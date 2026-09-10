package edu.princeton.cs.algs4;

public class LinearProgramming {
   private static final double EPSILON = 1.0E-10;
   private double[][] a;
   private int m;
   private int n;
   private int[] basis;

   public LinearProgramming(double[][] A, double[] b, double[] c) {
      this.m = b.length;
      this.n = c.length;

      for(int i = 0; i < this.m; ++i) {
         if (!(b[i] >= (double)0.0F)) {
            throw new IllegalArgumentException("RHS must be nonnegative");
         }
      }

      this.a = new double[this.m + 1][this.n + this.m + 1];

      for(int i = 0; i < this.m; ++i) {
         for(int j = 0; j < this.n; ++j) {
            this.a[i][j] = A[i][j];
         }
      }

      for(int i = 0; i < this.m; ++i) {
         this.a[i][this.n + i] = (double)1.0F;
      }

      for(int j = 0; j < this.n; ++j) {
         this.a[this.m][j] = c[j];
      }

      for(int i = 0; i < this.m; ++i) {
         this.a[i][this.m + this.n] = b[i];
      }

      this.basis = new int[this.m];

      for(int i = 0; i < this.m; ++i) {
         this.basis[i] = this.n + i;
      }

      this.solve();

      assert this.check(A, b, c);

   }

   private void solve() {
      while(true) {
         int q = this.bland();
         if (q == -1) {
            return;
         }

         int p = this.minRatioRule(q);
         if (p == -1) {
            throw new ArithmeticException("Linear program is unbounded");
         }

         this.pivot(p, q);
         this.basis[p] = q;
      }
   }

   private int bland() {
      for(int j = 0; j < this.m + this.n; ++j) {
         if (this.a[this.m][j] > (double)0.0F) {
            return j;
         }
      }

      return -1;
   }

   private int dantzig() {
      int q = 0;

      for(int j = 1; j < this.m + this.n; ++j) {
         if (this.a[this.m][j] > this.a[this.m][q]) {
            q = j;
         }
      }

      if (this.a[this.m][q] <= (double)0.0F) {
         return -1;
      } else {
         return q;
      }
   }

   private int minRatioRule(int q) {
      int p = -1;

      for(int i = 0; i < this.m; ++i) {
         if (!(this.a[i][q] <= 1.0E-10)) {
            if (p == -1) {
               p = i;
            } else if (this.a[i][this.m + this.n] / this.a[i][q] < this.a[p][this.m + this.n] / this.a[p][q]) {
               p = i;
            }
         }
      }

      return p;
   }

   private void pivot(int p, int q) {
      for(int i = 0; i <= this.m; ++i) {
         for(int j = 0; j <= this.m + this.n; ++j) {
            if (i != p && j != q) {
               double[] var10000 = this.a[i];
               var10000[j] -= this.a[p][j] * (this.a[i][q] / this.a[p][q]);
            }
         }
      }

      for(int i = 0; i <= this.m; ++i) {
         if (i != p) {
            this.a[i][q] = (double)0.0F;
         }
      }

      for(int j = 0; j <= this.m + this.n; ++j) {
         if (j != q) {
            double[] var7 = this.a[p];
            var7[j] /= this.a[p][q];
         }
      }

      this.a[p][q] = (double)1.0F;
   }

   public double value() {
      return -this.a[this.m][this.m + this.n];
   }

   public double[] primal() {
      double[] x = new double[this.n];

      for(int i = 0; i < this.m; ++i) {
         if (this.basis[i] < this.n) {
            x[this.basis[i]] = this.a[i][this.m + this.n];
         }
      }

      return x;
   }

   public double[] dual() {
      double[] y = new double[this.m];

      for(int i = 0; i < this.m; ++i) {
         y[i] = -this.a[this.m][this.n + i];
         if (y[i] == (double)-0.0F) {
            y[i] = (double)0.0F;
         }
      }

      return y;
   }

   private boolean isPrimalFeasible(double[][] A, double[] b) {
      double[] x = this.primal();

      for(int j = 0; j < x.length; ++j) {
         if (x[j] < -1.0E-10) {
            StdOut.println("x[" + j + "] = " + x[j] + " is negative");
            return false;
         }
      }

      for(int i = 0; i < this.m; ++i) {
         double sum = (double)0.0F;

         for(int j = 0; j < this.n; ++j) {
            sum += A[i][j] * x[j];
         }

         if (sum > b[i] + 1.0E-10) {
            StdOut.println("not primal feasible");
            StdOut.println("b[" + i + "] = " + b[i] + ", sum = " + sum);
            return false;
         }
      }

      return true;
   }

   private boolean isDualFeasible(double[][] A, double[] c) {
      double[] y = this.dual();

      for(int i = 0; i < y.length; ++i) {
         if (y[i] < -1.0E-10) {
            StdOut.println("y[" + i + "] = " + y[i] + " is negative");
            return false;
         }
      }

      for(int j = 0; j < this.n; ++j) {
         double sum = (double)0.0F;

         for(int i = 0; i < this.m; ++i) {
            sum += A[i][j] * y[i];
         }

         if (sum < c[j] - 1.0E-10) {
            StdOut.println("not dual feasible");
            StdOut.println("c[" + j + "] = " + c[j] + ", sum = " + sum);
            return false;
         }
      }

      return true;
   }

   private boolean isOptimal(double[] b, double[] c) {
      double[] x = this.primal();
      double[] y = this.dual();
      double value = this.value();
      double value1 = (double)0.0F;

      for(int j = 0; j < x.length; ++j) {
         value1 += c[j] * x[j];
      }

      double value2 = (double)0.0F;

      for(int i = 0; i < y.length; ++i) {
         value2 += y[i] * b[i];
      }

      if (!(Math.abs(value - value1) > 1.0E-10) && !(Math.abs(value - value2) > 1.0E-10)) {
         return true;
      } else {
         StdOut.println("value = " + value + ", cx = " + value1 + ", yb = " + value2);
         return false;
      }
   }

   private boolean check(double[][] A, double[] b, double[] c) {
      return this.isPrimalFeasible(A, b) && this.isDualFeasible(A, c) && this.isOptimal(b, c);
   }

   private void show() {
      StdOut.println("m = " + this.m);
      StdOut.println("n = " + this.n);

      for(int i = 0; i <= this.m; ++i) {
         for(int j = 0; j <= this.m + this.n; ++j) {
            StdOut.printf("%7.2f ", this.a[i][j]);
         }

         StdOut.println();
      }

      StdOut.println("value = " + this.value());

      for(int i = 0; i < this.m; ++i) {
         if (this.basis[i] < this.n) {
            int var10000 = this.basis[i];
            StdOut.println("x_" + var10000 + " = " + this.a[i][this.m + this.n]);
         }
      }

      StdOut.println();
   }

   private static void test(double[][] A, double[] b, double[] c) {
      LinearProgramming lp;
      try {
         lp = new LinearProgramming(A, b, c);
      } catch (ArithmeticException e) {
         System.out.println(e);
         return;
      }

      StdOut.println("value = " + lp.value());
      double[] x = lp.primal();

      for(int i = 0; i < x.length; ++i) {
         StdOut.println("x[" + i + "] = " + x[i]);
      }

      double[] y = lp.dual();

      for(int j = 0; j < y.length; ++j) {
         StdOut.println("y[" + j + "] = " + y[j]);
      }

   }

   private static void test1() {
      double[][] A = new double[][]{{(double)-1.0F, (double)1.0F, (double)0.0F}, {(double)1.0F, (double)4.0F, (double)0.0F}, {(double)2.0F, (double)1.0F, (double)0.0F}, {(double)3.0F, (double)-4.0F, (double)0.0F}, {(double)0.0F, (double)0.0F, (double)1.0F}};
      double[] c = new double[]{(double)1.0F, (double)1.0F, (double)1.0F};
      double[] b = new double[]{(double)5.0F, (double)45.0F, (double)27.0F, (double)24.0F, (double)4.0F};
      test(A, b, c);
   }

   private static void test2() {
      double[] c = new double[]{(double)13.0F, (double)23.0F};
      double[] b = new double[]{(double)480.0F, (double)160.0F, (double)1190.0F};
      double[][] A = new double[][]{{(double)5.0F, (double)15.0F}, {(double)4.0F, (double)4.0F}, {(double)35.0F, (double)20.0F}};
      test(A, b, c);
   }

   private static void test3() {
      double[] c = new double[]{(double)2.0F, (double)3.0F, (double)-1.0F, (double)-12.0F};
      double[] b = new double[]{(double)3.0F, (double)2.0F};
      double[][] A = new double[][]{{(double)-2.0F, (double)-9.0F, (double)1.0F, (double)9.0F}, {(double)1.0F, (double)1.0F, (double)-1.0F, (double)-2.0F}};
      test(A, b, c);
   }

   private static void test4() {
      double[] c = new double[]{(double)10.0F, (double)-57.0F, (double)-9.0F, (double)-24.0F};
      double[] b = new double[]{(double)0.0F, (double)0.0F, (double)1.0F};
      double[][] A = new double[][]{{(double)0.5F, (double)-5.5F, (double)-2.5F, (double)9.0F}, {(double)0.5F, (double)-1.5F, (double)-0.5F, (double)1.0F}, {(double)1.0F, (double)0.0F, (double)0.0F, (double)0.0F}};
      test(A, b, c);
   }

   public static void main(String[] args) {
      StdOut.println("----- test 1 --------------------");
      test1();
      StdOut.println();
      StdOut.println("----- test 2 --------------------");
      test2();
      StdOut.println();
      StdOut.println("----- test 3 --------------------");
      test3();
      StdOut.println();
      StdOut.println("----- test 4 --------------------");
      test4();
      StdOut.println();
      StdOut.println("----- test random ---------------");
      int m = Integer.parseInt(args[0]);
      int n = Integer.parseInt(args[1]);
      double[] c = new double[n];
      double[] b = new double[m];
      double[][] A = new double[m][n];

      for(int j = 0; j < n; ++j) {
         c[j] = (double)StdRandom.uniformInt(1000);
      }

      for(int i = 0; i < m; ++i) {
         b[i] = (double)StdRandom.uniformInt(1000);
      }

      for(int i = 0; i < m; ++i) {
         for(int j = 0; j < n; ++j) {
            A[i][j] = (double)StdRandom.uniformInt(100);
         }
      }

      new LinearProgramming(A, b, c);
      test(A, b, c);
   }
}
