package edu.princeton.cs.algs4;

public class EdgeWeightedDirectedCycle {
   private boolean[] marked;
   private DirectedEdge[] edgeTo;
   private boolean[] onStack;
   private Stack<DirectedEdge> cycle;

   public EdgeWeightedDirectedCycle(EdgeWeightedDigraph G) {
      this.marked = new boolean[G.V()];
      this.onStack = new boolean[G.V()];
      this.edgeTo = new DirectedEdge[G.V()];

      for(int v = 0; v < G.V(); ++v) {
         if (!this.marked[v]) {
            this.dfs(G, v);
         }
      }

      assert this.check();

   }

   private void dfs(EdgeWeightedDigraph G, int v) {
      this.onStack[v] = true;
      this.marked[v] = true;

      for(DirectedEdge e : G.adj(v)) {
         int w = e.to();
         if (this.cycle != null) {
            return;
         }

         if (!this.marked[w]) {
            this.edgeTo[w] = e;
            this.dfs(G, w);
         } else if (this.onStack[w]) {
            this.cycle = new Stack<DirectedEdge>();

            DirectedEdge f;
            for(f = e; f.from() != w; f = this.edgeTo[f.from()]) {
               this.cycle.push(f);
            }

            this.cycle.push(f);
            return;
         }
      }

      this.onStack[v] = false;
   }

   public boolean hasCycle() {
      return this.cycle != null;
   }

   public Iterable<DirectedEdge> cycle() {
      return this.cycle;
   }

   private boolean check() {
      if (this.hasCycle()) {
         DirectedEdge first = null;
         DirectedEdge last = null;

         for(DirectedEdge e : this.cycle()) {
            if (first == null) {
               first = e;
            }

            if (last != null && last.to() != e.from()) {
               System.err.printf("cycle edges %s and %s not incident\n", last, e);
               return false;
            }

            last = e;
         }

         if (first == null || last == null) {
            System.err.printf("cycle contains no edges\n");
            return false;
         }

         if (last.to() != first.from()) {
            System.err.printf("cycle edges %s and %s not incident\n", last, first);
            return false;
         }
      }

      return true;
   }

   public static void main(String[] args) {
      int V = Integer.parseInt(args[0]);
      int E = Integer.parseInt(args[1]);
      int F = Integer.parseInt(args[2]);
      EdgeWeightedDigraph G = new EdgeWeightedDigraph(V);
      int[] vertices = new int[V];

      for(int i = 0; i < V; vertices[i] = i++) {
      }

      StdRandom.shuffle(vertices);

      for(int i = 0; i < E; ++i) {
         int v;
         int w;
         do {
            v = StdRandom.uniformInt(V);
            w = StdRandom.uniformInt(V);
         } while(v >= w);

         double weight = StdRandom.uniformDouble((double)0.0F, (double)1.0F);
         G.addEdge(new DirectedEdge(v, w, weight));
      }

      for(int i = 0; i < F; ++i) {
         int v = StdRandom.uniformInt(V);
         int w = StdRandom.uniformInt(V);
         double weight = StdRandom.uniformDouble((double)0.0F, (double)1.0F);
         G.addEdge(new DirectedEdge(v, w, weight));
      }

      StdOut.println(G);
      EdgeWeightedDirectedCycle finder = new EdgeWeightedDirectedCycle(G);
      if (finder.hasCycle()) {
         StdOut.print("Cycle: ");

         for(DirectedEdge e : finder.cycle()) {
            StdOut.print(e + " ");
         }

         StdOut.println();
      } else {
         StdOut.println("No directed cycle");
      }

   }
}
