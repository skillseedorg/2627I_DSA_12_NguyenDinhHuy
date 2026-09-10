package edu.princeton.cs.algs4;

public class GaussJordanElimination {
   private static final double EPSILON = 1.0E-8;
   private final int n;
   private double[][] a;

   public GaussJordanElimination(double[][] A, double[] b) {
      this.n = b.length;
      this.a = new double[this.n][this.n + this.n + 1];

      for(int i = 0; i < this.n; ++i) {
         for(int j = 0; j < this.n; ++j) {
            this.a[i][j] = A[i][j];
         }
      }

      for(int i = 0; i < this.n; ++i) {
         this.a[i][this.n + i] = (double)1.0F;
      }

      for(int i = 0; i < this.n; ++i) {
         this.a[i][this.n + this.n] = b[i];
      }

      this.solve();

      assert this.certifySolution(A, b);

   }

   private void solve() {
      for(int p = 0; p < this.n; ++p) {
         int max = p;

         for(int i = p + 1; i < this.n; ++i) {
            if (Math.abs(this.a[i][p]) > Math.abs(this.a[max][p])) {
               max = i;
            }
         }

         this.swap(p, max);
         if (!(Math.abs(this.a[p][p]) <= 1.0E-8)) {
            this.pivot(p, p);
         }
      }

   }

   private void swap(int row1, int row2) {
      double[] temp = this.a[row1];
      this.a[row1] = this.a[row2];
      this.a[row2] = temp;
   }

   private void pivot(int p, int q) {
      for(int i = 0; i < this.n; ++i) {
         double alpha = this.a[i][q] / this.a[p][q];

         for(int j = 0; j <= this.n + this.n; ++j) {
            if (i != p && j != q) {
               double[] var10000 = this.a[i];
               var10000[j] -= alpha * this.a[p][j];
            }
         }
      }

      for(int i = 0; i < this.n; ++i) {
         if (i != p) {
            this.a[i][q] = (double)0.0F;
         }
      }

      for(int j = 0; j <= this.n + this.n; ++j) {
         if (j != q) {
            double[] var9 = this.a[p];
            var9[j] /= this.a[p][q];
         }
      }

      this.a[p][q] = (double)1.0F;
   }

   public double[] primal() {
      double[] x = new double[this.n];

      for(int i = 0; i < this.n; ++i) {
         if (Math.abs(this.a[i][i]) > 1.0E-8) {
            x[i] = this.a[i][this.n + this.n] / this.a[i][i];
         } else if (Math.abs(this.a[i][this.n + this.n]) > 1.0E-8) {
            return null;
         }
      }

      return x;
   }

   public double[] dual() {
      double[] y = new double[this.n];

      for(int i = 0; i < this.n; ++i) {
         if (Math.abs(this.a[i][i]) <= 1.0E-8 && Math.abs(this.a[i][this.n + this.n]) > 1.0E-8) {
            for(int j = 0; j < this.n; ++j) {
               y[j] = this.a[i][this.n + j];
            }

            return y;
         }
      }

      return null;
   }

   public boolean isFeasible() {
      return this.primal() != null;
   }

   private void show() {
      for(int i = 0; i < this.n; ++i) {
         for(int j = 0; j < this.n; ++j) {
            StdOut.printf("%8.3f ", this.a[i][j]);
         }

         StdOut.printf("| ");

         for(int j = this.n; j < this.n + this.n; ++j) {
            StdOut.printf("%8.3f ", this.a[i][j]);
         }

         StdOut.printf("| %8.3f\n", this.a[i][this.n + this.n]);
      }

      StdOut.println();
   }

   private boolean certifySolution(double[][] A, double[] b) {
      if (this.isFeasible()) {
         double[] x = this.primal();

         for(int i = 0; i < this.n; ++i) {
            double sum = (double)0.0F;

            for(int j = 0; j < this.n; ++j) {
               sum += A[i][j] * x[j];
            }

            if (Math.abs(sum - b[i]) > 1.0E-8) {
               StdOut.println("not feasible");
               StdOut.printf("b[%d] = %8.3f, sum = %8.3f\n", i, b[i], sum);
               return false;
            }
         }

         return true;
      } else {
         double[] y = this.dual();

         for(int j = 0; j < this.n; ++j) {
            double sum = (double)0.0F;

            for(int i = 0; i < this.n; ++i) {
               sum += A[i][j] * y[i];
            }

            if (Math.abs(sum) > 1.0E-8) {
               StdOut.println("invalid certificate of infeasibility");
               StdOut.printf("sum = %8.3f\n", sum);
               return false;
            }
         }

         double sum = (double)0.0F;

         for(int i = 0; i < this.n; ++i) {
            sum += y[i] * b[i];
         }

         if (Math.abs(sum) < 1.0E-8) {
            StdOut.println("invalid certificate of infeasibility");
            StdOut.printf("yb  = %8.3f\n", sum);
            return false;
         } else {
            return true;
         }
      }
   }

   private static void test(String name, double[][] A, double[] b) {
      StdOut.println("----------------------------------------------------");
      StdOut.println(name);
      StdOut.println("----------------------------------------------------");
      GaussJordanElimination gaussian = new GaussJordanElimination(A, b);
      if (gaussian.isFeasible()) {
         StdOut.println("Solution to Ax = b");
         double[] x = gaussian.primal();

         for(int i = 0; i < x.length; ++i) {
            StdOut.printf("%10.6f\n", x[i]);
         }
      } else {
         StdOut.println("Certificate of infeasibility");
         double[] y = gaussian.dual();

         for(int j = 0; j < y.length; ++j) {
            StdOut.printf("%10.6f\n", y[j]);
         }
      }

      StdOut.println();
      StdOut.println();
   }

   private static void test1() {
      double[][] A = new double[][]{{(double)0.0F, (double)1.0F, (double)1.0F}, {(double)2.0F, (double)4.0F, (double)-2.0F}, {(double)0.0F, (double)3.0F, (double)15.0F}};
      double[] b = new double[]{(double)4.0F, (double)2.0F, (double)36.0F};
      test("test 1", A, b);
   }

   private static void test2() {
      double[][] A = new double[][]{{(double)1.0F, (double)-3.0F, (double)1.0F}, {(double)2.0F, (double)-8.0F, (double)8.0F}, {(double)-6.0F, (double)3.0F, (double)-15.0F}};
      double[] b = new double[]{(double)4.0F, (double)-2.0F, (double)9.0F};
      test("test 2", A, b);
   }

   private static void test3() {
      double[][] A = new double[][]{{(double)2.0F, (double)-3.0F, (double)-1.0F, (double)2.0F, (double)3.0F}, {(double)4.0F, (double)-4.0F, (double)-1.0F, (double)4.0F, (double)11.0F}, {(double)2.0F, (double)-5.0F, (double)-2.0F, (double)2.0F, (double)-1.0F}, {(double)0.0F, (double)2.0F, (double)1.0F, (double)0.0F, (double)4.0F}, {(double)-4.0F, (double)6.0F, (double)0.0F, (double)0.0F, (double)7.0F}};
      double[] b = new double[]{(double)4.0F, (double)4.0F, (double)9.0F, (double)-6.0F, (double)5.0F};
      test("test 3", A, b);
   }

   private static void test4() {
      double[][] A = new double[][]{{(double)2.0F, (double)-3.0F, (double)-1.0F, (double)2.0F, (double)3.0F}, {(double)4.0F, (double)-4.0F, (double)-1.0F, (double)4.0F, (double)11.0F}, {(double)2.0F, (double)-5.0F, (double)-2.0F, (double)2.0F, (double)-1.0F}, {(double)0.0F, (double)2.0F, (double)1.0F, (double)0.0F, (double)4.0F}, {(double)-4.0F, (double)6.0F, (double)0.0F, (double)0.0F, (double)7.0F}};
      double[] b = new double[]{(double)4.0F, (double)4.0F, (double)9.0F, (double)-5.0F, (double)5.0F};
      test("test 4", A, b);
   }

   private static void test5() {
      double[][] A = new double[][]{{(double)2.0F, (double)-1.0F, (double)1.0F}, {(double)3.0F, (double)2.0F, (double)-4.0F}, {(double)-6.0F, (double)3.0F, (double)-3.0F}};
      double[] b = new double[]{(double)1.0F, (double)4.0F, (double)2.0F};
      test("test 5", A, b);
   }

   private static void test6() {
      double[][] A = new double[][]{{(double)1.0F, (double)-1.0F, (double)2.0F}, {(double)4.0F, (double)4.0F, (double)-2.0F}, {(double)-2.0F, (double)2.0F, (double)-4.0F}};
      double[] b = new double[]{(double)-3.0F, (double)1.0F, (double)6.0F};
      test("test 6 (infinitely many solutions)", A, b);
   }

   public static void main(String[] args) {
      test1();
      test2();
      test3();
      test4();
      test5();
      test6();
      int n = Integer.parseInt(args[0]);
      double[][] A = new double[n][n];

      for(int i = 0; i < n; ++i) {
         for(int j = 0; j < n; ++j) {
            A[i][j] = (double)StdRandom.uniformInt(1000);
         }
      }

      double[] b = new double[n];

      for(int i = 0; i < n; ++i) {
         b[i] = (double)StdRandom.uniformInt(1000);
      }

      test("random " + n + "-by-" + n + " (likely full rank)", A, b);
      A = new double[n][n];

      for(int i = 0; i < n - 1; ++i) {
         for(int j = 0; j < n; ++j) {
            A[i][j] = (double)StdRandom.uniformInt(1000);
         }
      }

      for(int i = 0; i < n - 1; ++i) {
         double alpha = (double)StdRandom.uniformInt(-5, 5);

         for(int j = 0; j < n; ++j) {
            A[n - 1][j] += alpha * A[i][j];
         }
      }

      b = new double[n];

      for(int i = 0; i < n; ++i) {
         b[i] = (double)StdRandom.uniformInt(1000);
      }

      test("random " + n + "-by-" + n + " (likely infeasible)", A, b);
   }
}
