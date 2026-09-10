package edu.princeton.cs.algs4;

public class FloydWarshall {
   private boolean hasNegativeCycle;
   private double[][] distTo;
   private DirectedEdge[][] edgeTo;

   public FloydWarshall(AdjMatrixEdgeWeightedDigraph G) {
      int V = G.V();
      this.distTo = new double[V][V];
      this.edgeTo = new DirectedEdge[V][V];

      for(int v = 0; v < V; ++v) {
         for(int w = 0; w < V; ++w) {
            this.distTo[v][w] = Double.POSITIVE_INFINITY;
         }
      }

      for(int v = 0; v < G.V(); ++v) {
         for(DirectedEdge e : G.adj(v)) {
            this.distTo[e.from()][e.to()] = e.weight();
            this.edgeTo[e.from()][e.to()] = e;
         }

         if (this.distTo[v][v] >= (double)0.0F) {
            this.distTo[v][v] = (double)0.0F;
            this.edgeTo[v][v] = null;
         }
      }

      for(int i = 0; i < V; ++i) {
         for(int v = 0; v < V; ++v) {
            if (this.edgeTo[v][i] != null) {
               for(int w = 0; w < V; ++w) {
                  if (this.distTo[v][w] > this.distTo[v][i] + this.distTo[i][w]) {
                     this.distTo[v][w] = this.distTo[v][i] + this.distTo[i][w];
                     this.edgeTo[v][w] = this.edgeTo[i][w];
                  }
               }

               if (this.distTo[v][v] < (double)0.0F) {
                  this.hasNegativeCycle = true;
                  return;
               }
            }
         }
      }

      assert this.check(G);

   }

   public boolean hasNegativeCycle() {
      return this.hasNegativeCycle;
   }

   public Iterable<DirectedEdge> negativeCycle() {
      for(int v = 0; v < this.distTo.length; ++v) {
         if (this.distTo[v][v] < (double)0.0F) {
            int V = this.edgeTo.length;
            EdgeWeightedDigraph spt = new EdgeWeightedDigraph(V);

            for(int w = 0; w < V; ++w) {
               if (this.edgeTo[v][w] != null) {
                  spt.addEdge(this.edgeTo[v][w]);
               }
            }

            EdgeWeightedDirectedCycle finder = new EdgeWeightedDirectedCycle(spt);

            assert finder.hasCycle();

            return finder.cycle();
         }
      }

      return null;
   }

   public boolean hasPath(int s, int t) {
      this.validateVertex(s);
      this.validateVertex(t);
      return this.distTo[s][t] < Double.POSITIVE_INFINITY;
   }

   public double dist(int s, int t) {
      this.validateVertex(s);
      this.validateVertex(t);
      if (this.hasNegativeCycle()) {
         throw new UnsupportedOperationException("Negative cost cycle exists");
      } else {
         return this.distTo[s][t];
      }
   }

   public Iterable<DirectedEdge> path(int s, int t) {
      this.validateVertex(s);
      this.validateVertex(t);
      if (this.hasNegativeCycle()) {
         throw new UnsupportedOperationException("Negative cost cycle exists");
      } else if (!this.hasPath(s, t)) {
         return null;
      } else {
         Stack<DirectedEdge> path = new Stack<DirectedEdge>();

         for(DirectedEdge e = this.edgeTo[s][t]; e != null; e = this.edgeTo[s][e.from()]) {
            path.push(e);
         }

         return path;
      }
   }

   private boolean check(AdjMatrixEdgeWeightedDigraph G) {
      if (!this.hasNegativeCycle()) {
         for(int v = 0; v < G.V(); ++v) {
            for(DirectedEdge e : G.adj(v)) {
               int w = e.to();

               for(int i = 0; i < G.V(); ++i) {
                  if (this.distTo[i][w] > this.distTo[i][v] + e.weight()) {
                     System.err.println("edge " + e + " is eligible");
                     return false;
                  }
               }
            }
         }
      }

      return true;
   }

   private void validateVertex(int v) {
      int V = this.distTo.length;
      if (v < 0 || v >= V) {
         throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
      }
   }

   public static void main(String[] args) {
      int V = Integer.parseInt(args[0]);
      int E = Integer.parseInt(args[1]);
      AdjMatrixEdgeWeightedDigraph G = new AdjMatrixEdgeWeightedDigraph(V);

      for(int i = 0; i < E; ++i) {
         int v = StdRandom.uniformInt(V);
         int w = StdRandom.uniformInt(V);
         double weight = 0.01 * (double)StdRandom.uniformInt(-15, 100);
         if (v == w) {
            G.addEdge(new DirectedEdge(v, w, Math.abs(weight)));
         } else {
            G.addEdge(new DirectedEdge(v, w, weight));
         }
      }

      StdOut.println(G);
      FloydWarshall spt = new FloydWarshall(G);
      StdOut.printf("  ");

      for(int v = 0; v < G.V(); ++v) {
         StdOut.printf("%6d ", v);
      }

      StdOut.println();

      for(int v = 0; v < G.V(); ++v) {
         StdOut.printf("%3d: ", v);

         for(int w = 0; w < G.V(); ++w) {
            if (spt.hasPath(v, w)) {
               StdOut.printf("%6.2f ", spt.dist(v, w));
            } else {
               StdOut.printf("  Inf ");
            }
         }

         StdOut.println();
      }

      if (spt.hasNegativeCycle()) {
         StdOut.println("Negative cost cycle:");

         for(DirectedEdge e : spt.negativeCycle()) {
            StdOut.println(e);
         }

         StdOut.println();
      } else {
         for(int v = 0; v < G.V(); ++v) {
            for(int w = 0; w < G.V(); ++w) {
               if (!spt.hasPath(v, w)) {
                  StdOut.printf("%d to %d no path\n", v, w);
               } else {
                  StdOut.printf("%d to %d (%5.2f)  ", v, w, spt.dist(v, w));

                  for(DirectedEdge e : spt.path(v, w)) {
                     StdOut.print(e + "  ");
                  }

                  StdOut.println();
               }
            }
         }
      }

   }
}
