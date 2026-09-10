package edu.princeton.cs.algs4;

public class DepthFirstPaths {
   private boolean[] marked;
   private int[] edgeTo;
   private final int s;

   public DepthFirstPaths(Graph G, int s) {
      this.s = s;
      this.edgeTo = new int[G.V()];
      this.marked = new boolean[G.V()];
      this.validateVertex(s);
      this.dfs(G, s);
   }

   private void dfs(Graph G, int v) {
      this.marked[v] = true;

      for(int w : G.adj(v)) {
         if (!this.marked[w]) {
            this.edgeTo[w] = v;
            this.dfs(G, w);
         }
      }

   }

   public boolean hasPathTo(int v) {
      this.validateVertex(v);
      return this.marked[v];
   }

   public Iterable<Integer> pathTo(int v) {
      this.validateVertex(v);
      if (!this.hasPathTo(v)) {
         return null;
      } else {
         Stack<Integer> path = new Stack<Integer>();

         for(int x = v; x != this.s; x = this.edgeTo[x]) {
            path.push(x);
         }

         path.push(this.s);
         return path;
      }
   }

   private void validateVertex(int v) {
      int V = this.marked.length;
      if (v < 0 || v >= V) {
         throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
      }
   }

   public static void main(String[] args) {
      In in = new In(args[0]);
      Graph G = new Graph(in);
      int s = Integer.parseInt(args[1]);
      DepthFirstPaths dfs = new DepthFirstPaths(G, s);

      for(int v = 0; v < G.V(); ++v) {
         if (!dfs.hasPathTo(v)) {
            StdOut.printf("%d to %d:  not connected\n", s, v);
         } else {
            StdOut.printf("%d to %d:  ", s, v);

            for(int x : dfs.pathTo(v)) {
               if (x == s) {
                  StdOut.print(x);
               } else {
                  StdOut.print("-" + x);
               }
            }

            StdOut.println();
         }
      }

   }
}
