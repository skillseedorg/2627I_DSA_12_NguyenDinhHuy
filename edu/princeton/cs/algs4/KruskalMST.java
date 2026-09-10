package edu.princeton.cs.algs4;

import java.util.Arrays;

public class KruskalMST {
   private static final double FLOATING_POINT_EPSILON = 1.0E-12;
   private double weight;
   private Queue<Edge> mst = new Queue<Edge>();

   public KruskalMST(EdgeWeightedGraph G) {
      Edge[] edges = new Edge[G.E()];
      int t = 0;

      for(Edge e : G.edges()) {
         edges[t++] = e;
      }

      Arrays.sort(edges);
      UF uf = new UF(G.V());

      for(int i = 0; i < G.E() && this.mst.size() < G.V() - 1; ++i) {
         Edge e = edges[i];
         int v = e.either();
         int w = e.other(v);
         if (uf.find(v) != uf.find(w)) {
            uf.union(v, w);
            this.mst.enqueue(e);
            this.weight += e.weight();
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

   private boolean check(EdgeWeightedGraph G) {
      double total = (double)0.0F;

      for(Edge e : this.edges()) {
         total += e.weight();
      }

      if (Math.abs(total - this.weight()) > 1.0E-12) {
         System.err.printf("Weight of edges does not equal weight(): %f vs. %f\n", total, this.weight());
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
      KruskalMST mst = new KruskalMST(G);

      for(Edge e : mst.edges()) {
         StdOut.println(e);
      }

      StdOut.printf("%.5f\n", mst.weight());
   }
}
