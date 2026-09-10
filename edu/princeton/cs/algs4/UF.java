package edu.princeton.cs.algs4;

public class UF {
   private int[] parent;
   private byte[] rank;
   private int count;

   public UF(int n) {
      if (n < 0) {
         throw new IllegalArgumentException();
      } else {
         this.count = n;
         this.parent = new int[n];
         this.rank = new byte[n];

         for(int i = 0; i < n; ++i) {
            this.parent[i] = i;
            this.rank[i] = 0;
         }

      }
   }

   public int find(int p) {
      this.validate(p);

      while(p != this.parent[p]) {
         this.parent[p] = this.parent[this.parent[p]];
         p = this.parent[p];
      }

      return p;
   }

   public int count() {
      return this.count;
   }

   /** @deprecated */
   @Deprecated
   public boolean connected(int p, int q) {
      return this.find(p) == this.find(q);
   }

   public void union(int p, int q) {
      int rootP = this.find(p);
      int rootQ = this.find(q);
      if (rootP != rootQ) {
         if (this.rank[rootP] < this.rank[rootQ]) {
            this.parent[rootP] = rootQ;
         } else if (this.rank[rootP] > this.rank[rootQ]) {
            this.parent[rootQ] = rootP;
         } else {
            this.parent[rootQ] = rootP;
            ++this.rank[rootP];
         }

         --this.count;
      }
   }

   private void validate(int p) {
      int n = this.parent.length;
      if (p < 0 || p >= n) {
         throw new IllegalArgumentException("index " + p + " is not between 0 and " + (n - 1));
      }
   }

   public static void main(String[] args) {
      int n = StdIn.readInt();
      UF uf = new UF(n);

      while(!StdIn.isEmpty()) {
         int p = StdIn.readInt();
         int q = StdIn.readInt();
         if (uf.find(p) != uf.find(q)) {
            uf.union(p, q);
            StdOut.println(p + " " + q);
         }
      }

      StdOut.println(uf.count() + " components");
   }
}
