package edu.princeton.cs.algs4;

public class DijkstraSP {
   private double[] distTo;
   private DirectedEdge[] edgeTo;
   private IndexMinPQ<Double> pq;

   public DijkstraSP(EdgeWeightedDigraph G, int s) {
      for(DirectedEdge e : G.edges()) {
         if (e.weight() < (double)0.0F) {
            throw new IllegalArgumentException("edge " + e + " has negative weight");
         }
      }

      this.distTo = new double[G.V()];
      this.edgeTo = new DirectedEdge[G.V()];
      this.validateVertex(s);

      for(int v = 0; v < G.V(); ++v) {
         this.distTo[v] = Double.POSITIVE_INFINITY;
      }

      this.distTo[s] = (double)0.0F;
      this.pq = new IndexMinPQ<Double>(G.V());
      this.pq.insert(s, this.distTo[s]);

      while(!this.pq.isEmpty()) {
         int v = this.pq.delMin();

         for(DirectedEdge e : G.adj(v)) {
            this.relax(e);
         }
      }

      assert this.check(G, s);

   }

   private void relax(DirectedEdge e) {
      int v = e.from();
      int w = e.to();
      if (this.distTo[w] > this.distTo[v] + e.weight()) {
         this.distTo[w] = this.distTo[v] + e.weight();
         this.edgeTo[w] = e;
         if (this.pq.contains(w)) {
            this.pq.decreaseKey(w, this.distTo[w]);
         } else {
            this.pq.insert(w, this.distTo[w]);
         }
      }

   }

   public double distTo(int v) {
      this.validateVertex(v);
      return this.distTo[v];
   }

   public boolean hasPathTo(int v) {
      this.validateVertex(v);
      return this.distTo[v] < Double.POSITIVE_INFINITY;
   }

   public Iterable<DirectedEdge> pathTo(int v) {
      this.validateVertex(v);
      if (!this.hasPathTo(v)) {
         return null;
      } else {
         Stack<DirectedEdge> path = new Stack<DirectedEdge>();

         for(DirectedEdge e = this.edgeTo[v]; e != null; e = this.edgeTo[e.from()]) {
            path.push(e);
         }

         return path;
      }
   }

   private boolean check(EdgeWeightedDigraph G, int s) {
      for(DirectedEdge e : G.edges()) {
         if (e.weight() < (double)0.0F) {
            System.err.println("negative edge weight detected");
            return false;
         }
      }

      if (this.distTo[s] == (double)0.0F && this.edgeTo[s] == null) {
         for(int v = 0; v < G.V(); ++v) {
            if (v != s && this.edgeTo[v] == null && this.distTo[v] != Double.POSITIVE_INFINITY) {
               System.err.println("distTo[] and edgeTo[] inconsistent");
               return false;
            }
         }

         for(int v = 0; v < G.V(); ++v) {
            for(DirectedEdge e : G.adj(v)) {
               int w = e.to();
               if (this.distTo[v] + e.weight() < this.distTo[w]) {
                  System.err.println("edge " + e + " not relaxed");
                  return false;
               }
            }
         }

         for(int w = 0; w < G.V(); ++w) {
            if (this.edgeTo[w] != null) {
               DirectedEdge e = this.edgeTo[w];
               int v = e.from();
               if (w != e.to()) {
                  return false;
               }

               if (this.distTo[v] + e.weight() != this.distTo[w]) {
                  System.err.println("edge " + e + " on shortest path not tight");
                  return false;
               }
            }
         }

         return true;
      } else {
         System.err.println("distTo[s] and edgeTo[s] inconsistent");
         return false;
      }
   }

   private void validateVertex(int v) {
      int V = this.distTo.length;
      if (v < 0 || v >= V) {
         throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
      }
   }

   public static void main(String[] args) {
      In in = new In(args[0]);
      EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);
      int s = Integer.parseInt(args[1]);
      DijkstraSP sp = new DijkstraSP(G, s);

      for(int t = 0; t < G.V(); ++t) {
         if (!sp.hasPathTo(t)) {
            StdOut.printf("%d to %d         no path\n", s, t);
         } else {
            StdOut.printf("%d to %d (%.2f)  ", s, t, sp.distTo(t));

            for(DirectedEdge e : sp.pathTo(t)) {
               StdOut.print(e + "   ");
            }

            StdOut.println();
         }
      }

   }
}
