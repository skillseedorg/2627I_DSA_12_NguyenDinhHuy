package edu.princeton.cs.algs4;

public class LazyPrimMST {
   private static final double FLOATING_POINT_EPSILON = 1.0E-12;
   private double weight;
   private Queue<Edge> mst = new Queue<Edge>();
   private boolean[] marked;
   private MinPQ<Edge> pq = new MinPQ<Edge>();

   public LazyPrimMST(EdgeWeightedGraph G) {
      this.marked = new boolean[G.V()];

      for(int v = 0; v < G.V(); ++v) {
         if (!this.marked[v]) {
            this.prim(G, v);
         }
      }

      assert this.check(G);

   }

   private void prim(EdgeWeightedGraph G, int s) {
      this.scan(G, s);

      while(!this.pq.isEmpty()) {
         Edge e = this.pq.delMin();
         int v = e.either();
         int w = e.other(v);

         assert this.marked[v] || this.marked[w];

         if (!this.marked[v] || !this.marked[w]) {
            this.mst.enqueue(e);
            this.weight += e.weight();
            if (!this.marked[v]) {
               this.scan(G, v);
            }

            if (!this.marked[w]) {
               this.scan(G, w);
            }
         }
      }

   }

   private void scan(EdgeWeightedGraph G, int v) {
      assert !this.marked[v];

      this.marked[v] = true;

      for(Edge e : G.adj(v)) {
         if (!this.marked[e.other(v)]) {
            this.pq.insert(e);
         }
      }

   }

   public Iterable<Edge> edges() {
      return this.mst;
   }

   public double weight() {
      return this.weight;
   }

   private boolean check(EdgeWeightedGraph G) {
      double totalWeight = (double)0.0F;

      for(Edge e : this.edges()) {
         totalWeight += e.weight();
      }

      if (Math.abs(totalWeight - this.weight()) > 1.0E-12) {
         System.err.printf("Weight of edges does not equal weight(): %f vs. %f\n", totalWeight, this.weight());
         return false;
      } else {
         UF uf = new UF(G.V());

         for(Edge e : this.edges()) {
            int v = e.either();
            int w = e.other(v);
            if (uf.find(v) == uf.find(w)) {
               System.err.println("Not a forest");
               return false;
            }

            uf.union(v, w);
         }

         for(Edge e : G.edges()) {
            int v = e.either();
            int w = e.other(v);
            if (uf.find(v) != uf.find(w)) {
               System.err.println("Not a spanning forest");
               return false;
            }
         }

         for(Edge e : this.edges()) {
            uf = new UF(G.V());

            for(Edge f : this.mst) {
               int x = f.either();
               int y = f.other(x);
               if (f != e) {
                  uf.union(x, y);
               }
            }

            for(Edge f : G.edges()) {
               int x = f.either();
               int y = f.other(x);
               if (uf.find(x) != uf.find(y) && f.weight() < e.weight()) {
                  System.err.println("Edge " + f + " violates cut optimality conditions");
                  return false;
               }
            }
         }

         return true;
      }
   }

   public static void main(String[] args) {
      In in = new In(args[0]);
      EdgeWeightedGraph G = new EdgeWeightedGraph(in);
      LazyPrimMST mst = new LazyPrimMST(G);

      for(Edge e : mst.edges()) {
         StdOut.println(e);
      }

      StdOut.printf("%.5f\n", mst.weight());
   }
}
