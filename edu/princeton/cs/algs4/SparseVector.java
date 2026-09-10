package edu.princeton.cs.algs4;

public class SparseVector {
   private int d;
   private ST<Integer, Double> st;

   public SparseVector(int d) {
      this.d = d;
      this.st = new ST<Integer, Double>();
   }

   public void put(int i, double value) {
      if (i >= 0 && i < this.d) {
         if (value == (double)0.0F) {
            this.st.delete(i);
         } else {
            this.st.put(i, value);
         }

      } else {
         throw new IllegalArgumentException("Illegal index");
      }
   }

   public double get(int i) {
      if (i >= 0 && i < this.d) {
         return this.st.contains(i) ? (Double)this.st.get(i) : (double)0.0F;
      } else {
         throw new IllegalArgumentException("Illegal index");
      }
   }

   public int nnz() {
      return this.st.size();
   }

   /** @deprecated */
   @Deprecated
   public int size() {
      return this.d;
   }

   public int dimension() {
      return this.d;
   }

   public double dot(SparseVector that) {
      if (this.d != that.d) {
         throw new IllegalArgumentException("Vector lengths disagree");
      } else {
         double sum = (double)0.0F;
         if (this.st.size() <= that.st.size()) {
            for(int i : this.st.keys()) {
               if (that.st.contains(i)) {
                  sum += this.get(i) * that.get(i);
               }
            }
         } else {
            for(int i : that.st.keys()) {
               if (this.st.contains(i)) {
                  sum += this.get(i) * that.get(i);
               }
            }
         }

         return sum;
      }
   }

   public double dot(double[] that) {
      double sum = (double)0.0F;

      for(int i : this.st.keys()) {
         sum += that[i] * this.get(i);
      }

      return sum;
   }

   public double magnitude() {
      return Math.sqrt(this.dot(this));
   }

   /** @deprecated */
   @Deprecated
   public double norm() {
      return Math.sqrt(this.dot(this));
   }

   public SparseVector scale(double alpha) {
      SparseVector c = new SparseVector(this.d);

      for(int i : this.st.keys()) {
         c.put(i, alpha * this.get(i));
      }

      return c;
   }

   public SparseVector plus(SparseVector that) {
      if (this.d != that.d) {
         throw new IllegalArgumentException("Vector lengths disagree");
      } else {
         SparseVector c = new SparseVector(this.d);

         for(int i : this.st.keys()) {
            c.put(i, this.get(i));
         }

         for(int i : that.st.keys()) {
            c.put(i, that.get(i) + c.get(i));
         }

         return c;
      }
   }

   public String toString() {
      StringBuilder s = new StringBuilder();

      for(int i : this.st.keys()) {
         s.append("(" + i + ", " + this.st.get(i) + ") ");
      }

      return s.toString();
   }

   public static void main(String[] args) {
      SparseVector a = new SparseVector(10);
      SparseVector b = new SparseVector(10);
      a.put(3, (double)0.5F);
      a.put(9, (double)0.75F);
      a.put(6, 0.11);
      a.put(6, (double)0.0F);
      b.put(3, 0.6);
      b.put(4, 0.9);
      StdOut.println("a = " + a);
      StdOut.println("b = " + b);
      double var10000 = a.dot(b);
      StdOut.println("a dot b = " + var10000);
      SparseVector var3 = a.plus(b);
      StdOut.println("a + b   = " + var3);
   }
}
