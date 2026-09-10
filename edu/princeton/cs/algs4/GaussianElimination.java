package edu.princeton.cs.algs4;

public class GaussianElimination {
   private static final double EPSILON = 1.0E-8;
   private final int m;
   private final int n;
   private double[][] a;

   public GaussianElimination(double[][] A, double[] b) {
      this.m = A.length;
      this.n = A[0].length;
      if (b.length != this.m) {
         throw new IllegalArgumentException("Dimensions disagree");
      } else {
         this.a = new double[this.m][this.n + 1];

         for(int i = 0; i < this.m; ++i) {
            for(int j = 0; j < this.n; ++j) {
               this.a[i][j] = A[i][j];
            }
         }

         for(int i = 0; i < this.m; ++i) {
            this.a[i][this.n] = b[i];
         }

         this.forwardElimination();

         assert this.certifySolution(A, b);

      }
   }

   private void forwardElimination() {
      for(int p = 0; p < Math.min(this.m, this.n); ++p) {
         int max = p;

         for(int i = p + 1; i < this.m; ++i) {
            if (Math.abs(this.a[i][p]) > Math.abs(this.a[max][p])) {
               max = i;
            }
         }

         this.swap(p, max);
         if (!(Math.abs(this.a[p][p]) <= 1.0E-8)) {
            this.pivot(p);
         }
      }

   }

   private void swap(int row1, int row2) {
      double[] temp = this.a[row1];
      this.a[row1] = this.a[row2];
      this.a[row2] = temp;
   }

   private void pivot(int p) {
      for(int i = p + 1; i < this.m; ++i) {
         double alpha = this.a[i][p] / this.a[p][p];

         for(int j = p; j <= this.n; ++j) {
            double[] var10000 = this.a[i];
            var10000[j] -= alpha * this.a[p][j];
         }
      }

   }

   public double[] primal() {
      double[] x = new double[this.n];

      for(int i = Math.min(this.n - 1, this.m - 1); i >= 0; --i) {
         double sum = (double)0.0F;

         for(int j = i + 1; j < this.n; ++j) {
            sum += this.a[i][j] * x[j];
         }

         if (Math.abs(this.a[i][i]) > 1.0E-8) {
            x[i] = (this.a[i][this.n] - sum) / this.a[i][i];
         } else if (Math.abs(this.a[i][this.n] - sum) > 1.0E-8) {
            return null;
         }
      }

      for(int i = this.n; i < this.m; ++i) {
         double sum = (double)0.0F;

         for(int j = 0; j < this.n; ++j) {
            sum += this.a[i][j] * x[j];
         }

         if (Math.abs(this.a[i][this.n] - sum) > 1.0E-8) {
            return null;
         }
      }

      return x;
   }

   public boolean isFeasible() {
      return this.primal() != null;
   }

   private boolean certifySolution(double[][] A, double[] b) {
      if (!this.isFeasible()) {
         return true;
      } else {
         double[] x = this.primal();

         for(int i = 0; i < this.m; ++i) {
            double sum = (double)0.0F;

            for(int j = 0; j < this.n; ++j) {
               sum += A[i][j] * x[j];
            }

            if (Math.abs(sum - b[i]) > 1.0E-8) {
               StdOut.println("not feasible");
               StdOut.println("b[" + i + "] = " + b[i] + ", sum = " + sum);
               return false;
            }
         }

         return true;
      }
   }

   private static void test(String name, double[][] A, double[] b) {
      StdOut.println("----------------------------------------------------");
      StdOut.println(name);
      StdOut.println("----------------------------------------------------");
      GaussianElimination gaussian = new GaussianElimination(A, b);
      double[] x = gaussian.primal();
      if (gaussian.isFeasible()) {
         for(int i = 0; i < x.length; ++i) {
            StdOut.printf("%.6f\n", x[i]);
         }
      } else {
         StdOut.println("System is infeasible");
      }

      StdOut.println();
      StdOut.println();
   }

   private static void test1() {
      double[][] A = new double[][]{{(double)0.0F, (double)1.0F, (double)1.0F}, {(double)2.0F, (double)4.0F, (double)-2.0F}, {(double)0.0F, (double)3.0F, (double)15.0F}};
      double[] b = new double[]{(double)4.0F, (double)2.0F, (double)36.0F};
      test("test 1 (3-by-3 system, nonsingular)", A, b);
   }

   private static void test2() {
      double[][] A = new double[][]{{(double)1.0F, (double)-3.0F, (double)1.0F}, {(double)2.0F, (double)-8.0F, (double)8.0F}, {(double)-6.0F, (double)3.0F, (double)-15.0F}};
      double[] b = new double[]{(double)4.0F, (double)-2.0F, (double)9.0F};
      test("test 2 (3-by-3 system, nonsingular)", A, b);
   }

   private static void test3() {
      double[][] A = new double[][]{{(double)2.0F, (double)-3.0F, (double)-1.0F, (double)2.0F, (double)3.0F}, {(double)4.0F, (double)-4.0F, (double)-1.0F, (double)4.0F, (double)11.0F}, {(double)2.0F, (double)-5.0F, (double)-2.0F, (double)2.0F, (double)-1.0F}, {(double)0.0F, (double)2.0F, (double)1.0F, (double)0.0F, (double)4.0F}, {(double)-4.0F, (double)6.0F, (double)0.0F, (double)0.0F, (double)7.0F}};
      double[] b = new double[]{(double)4.0F, (double)4.0F, (double)9.0F, (double)-6.0F, (double)5.0F};
      test("test 3 (5-by-5 system, no solutions)", A, b);
   }

   private static void test4() {
      double[][] A = new double[][]{{(double)2.0F, (double)-3.0F, (double)-1.0F, (double)2.0F, (double)3.0F}, {(double)4.0F, (double)-4.0F, (double)-1.0F, (double)4.0F, (double)11.0F}, {(double)2.0F, (double)-5.0F, (double)-2.0F, (double)2.0F, (double)-1.0F}, {(double)0.0F, (double)2.0F, (double)1.0F, (double)0.0F, (double)4.0F}, {(double)-4.0F, (double)6.0F, (double)0.0F, (double)0.0F, (double)7.0F}};
      double[] b = new double[]{(double)4.0F, (double)4.0F, (double)9.0F, (double)-5.0F, (double)5.0F};
      test("test 4 (5-by-5 system, infinitely many solutions)", A, b);
   }

   private static void test5() {
      double[][] A = new double[][]{{(double)2.0F, (double)-1.0F, (double)1.0F}, {(double)3.0F, (double)2.0F, (double)-4.0F}, {(double)-6.0F, (double)3.0F, (double)-3.0F}};
      double[] b = new double[]{(double)1.0F, (double)4.0F, (double)2.0F};
      test("test 5 (3-by-3 system, no solutions)", A, b);
   }

   private static void test6() {
      double[][] A = new double[][]{{(double)1.0F, (double)-1.0F, (double)2.0F}, {(double)4.0F, (double)4.0F, (double)-2.0F}, {(double)-2.0F, (double)2.0F, (double)-4.0F}};
      double[] b = new double[]{(double)-3.0F, (double)1.0F, (double)6.0F};
      test("test 6 (3-by-3 system, infinitely many solutions)", A, b);
   }

   private static void test7() {
      double[][] A = new double[][]{{(double)0.0F, (double)1.0F, (double)1.0F}, {(double)2.0F, (double)4.0F, (double)-2.0F}, {(double)0.0F, (double)3.0F, (double)15.0F}, {(double)2.0F, (double)8.0F, (double)14.0F}};
      double[] b = new double[]{(double)4.0F, (double)2.0F, (double)36.0F, (double)42.0F};
      test("test 7 (4-by-3 system, full rank)", A, b);
   }

   private static void test8() {
      double[][] A = new double[][]{{(double)0.0F, (double)1.0F, (double)1.0F}, {(double)2.0F, (double)4.0F, (double)-2.0F}, {(double)0.0F, (double)3.0F, (double)15.0F}, {(double)2.0F, (double)8.0F, (double)14.0F}};
      double[] b = new double[]{(double)4.0F, (double)2.0F, (double)36.0F, (double)40.0F};
      test("test 8 (4-by-3 system, no solution)", A, b);
   }

   private static void test9() {
      double[][] A = new double[][]{{(double)1.0F, (double)-3.0F, (double)1.0F, (double)1.0F}, {(double)2.0F, (double)-8.0F, (double)8.0F, (double)2.0F}, {(double)-6.0F, (double)3.0F, (double)-15.0F, (double)3.0F}};
      double[] b = new double[]{(double)4.0F, (double)-2.0F, (double)9.0F};
      test("test 9 (3-by-4 system, full rank)", A, b);
   }

   public static void main(String[] args) {
      test1();
      test2();
      test3();
      test4();
      test5();
      test6();
      test7();
      test8();
      test9();
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

      test(n + "-by-" + n + " (probably nonsingular)", A, b);
   }
}
