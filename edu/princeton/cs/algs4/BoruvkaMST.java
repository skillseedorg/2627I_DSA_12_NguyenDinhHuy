package edu.princeton.cs.algs4;

public class BoruvkaMST {
   private static final double FLOATING_POINT_EPSILON = 1.0E-12;
   private Bag<Edge> mst = new Bag<Edge>();
   private double weight;

   public BoruvkaMST(EdgeWeightedGraph G) {
      UF uf = new UF(G.V());

      for(int t = 1; t < G.V() && this.mst.size() < G.V() - 1; t += t) {
         Edge[] closest = new Edge[G.V()];

         for(Edge e : G.edges()) {
            int v = e.either();
            int w = e.other(v);
            int i = uf.find(v);
            int j = uf.find(w);
            if (i != j) {
               if (closest[i] == null || less(e, closest[i])) {
                  closest[i] = e;
               }

               if (closest[j] == null || less(e, closest[j])) {
                  closest[j] = e;
               }
            }
         }

         for(int i = 0; i < G.V(); ++i) {
            Edge e = closest[i];
            if (e != null) {
               int v = e.either();
               int w = e.other(v);
               if (uf.find(v) != uf.find(w)) {
                  this.mst.add(e);
                  this.weight += e.weight();
                  uf.union(v, w);
               }
            }
         }
      }

      assert this.check(G);

   }

   public Iterable<Edge> edges() {
      return this.mst;
   }

   public double weight() {
      return this.weight;
   }

   private static boolean less(Edge e, Edge f) {
      return e.compareTo(f) < 0;
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
      BoruvkaMST mst = new BoruvkaMST(G);

      for(Edge e : mst.edges()) {
         StdOut.println(e);
      }

      StdOut.printf("%.5f\n", mst.weight());
   }
}
