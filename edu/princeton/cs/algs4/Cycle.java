package edu.princeton.cs.algs4;

public class Cycle {
   private boolean[] marked;
   private int[] edgeTo;
   private Stack<Integer> cycle;

   public Cycle(Graph G) {
      if (!this.hasParallelEdges(G)) {
         this.marked = new boolean[G.V()];
         this.edgeTo = new int[G.V()];

         for(int v = 0; v < G.V(); ++v) {
            if (!this.marked[v]) {
               this.dfs(G, -1, v);
            }
         }

      }
   }

   private boolean hasSelfLoop(Graph G) {
      for(int v = 0; v < G.V(); ++v) {
         for(int w : G.adj(v)) {
            if (v == w) {
               this.cycle = new Stack<Integer>();
               this.cycle.push(v);
               this.cycle.push(v);
               return true;
            }
         }
      }

      return false;
   }

   private boolean hasParallelEdges(Graph G) {
      this.marked = new boolean[G.V()];

      for(int v = 0; v < G.V(); ++v) {
         for(int w : G.adj(v)) {
            if (this.marked[w]) {
               this.cycle = new Stack<Integer>();
               this.cycle.push(v);
               this.cycle.push(w);
               this.cycle.push(v);
               return true;
            }

            this.marked[w] = true;
         }

         for(int w : G.adj(v)) {
            this.marked[w] = false;
         }
      }

      return false;
   }

   public boolean hasCycle() {
      return this.cycle != null;
   }

   public Iterable<Integer> cycle() {
      return this.cycle;
   }

   private void dfs(Graph G, int u, int v) {
      this.marked[v] = true;

      for(int w : G.adj(v)) {
         if (this.cycle != null) {
            return;
         }

         if (!this.marked[w]) {
            this.edgeTo[w] = v;
            this.dfs(G, v, w);
         } else if (w != u) {
            this.cycle = new Stack<Integer>();

            for(int x = v; x != w; x = this.edgeTo[x]) {
               this.cycle.push(x);
            }

            this.cycle.push(w);
            this.cycle.push(v);
         }
      }

   }

   public static void main(String[] args) {
      In in = new In(args[0]);
      Graph G = new Graph(in);
      Cycle finder = new Cycle(G);
      if (finder.hasCycle()) {
         for(int v : finder.cycle()) {
            StdOut.print(v + " ");
         }

         StdOut.println();
      } else {
         StdOut.println("Graph is acyclic");
      }

   }
}
