package edu.princeton.cs.algs4;

public class Topological {
   private Iterable<Integer> order;
   private int[] rank;

   public Topological(Digraph G) {
      DirectedCycle finder = new DirectedCycle(G);
      if (!finder.hasCycle()) {
         DepthFirstOrder dfs = new DepthFirstOrder(G);
         this.order = dfs.reversePost();
         this.rank = new int[G.V()];
         int i = 0;

         for(int v : this.order) {
            this.rank[v] = i++;
         }
      }

   }

   public Topological(EdgeWeightedDigraph G) {
      EdgeWeightedDirectedCycle finder = new EdgeWeightedDirectedCycle(G);
      if (!finder.hasCycle()) {
         DepthFirstOrder dfs = new DepthFirstOrder(G);
         this.order = dfs.reversePost();
      }

   }

   public Iterable<Integer> order() {
      return this.order;
   }

   public boolean hasOrder() {
      return this.order != null;
   }

   /** @deprecated */
   @Deprecated
   public boolean isDAG() {
      return this.hasOrder();
   }

   public int rank(int v) {
      this.validateVertex(v);
      return this.hasOrder() ? this.rank[v] : -1;
   }

   private void validateVertex(int v) {
      int V = this.rank.length;
      if (v < 0 || v >= V) {
         throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
      }
   }

   public static void main(String[] args) {
      String filename = args[0];
      String delimiter = args[1];
      SymbolDigraph sg = new SymbolDigraph(filename, delimiter);
      Topological topological = new Topological(sg.digraph());

      for(int v : topological.order()) {
         StdOut.println(sg.nameOf(v));
      }

   }
}
