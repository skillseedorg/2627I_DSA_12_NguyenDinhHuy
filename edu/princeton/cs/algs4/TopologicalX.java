package edu.princeton.cs.algs4;

public class TopologicalX {
   private Queue<Integer> order;
   private int[] ranks;

   public TopologicalX(Digraph G) {
      int[] indegree = new int[G.V()];

      for(int v = 0; v < G.V(); ++v) {
         indegree[v] = G.indegree(v);
      }

      this.ranks = new int[G.V()];
      this.order = new Queue<Integer>();
      int count = 0;
      Queue<Integer> queue = new Queue<Integer>();

      for(int v = 0; v < G.V(); ++v) {
         if (indegree[v] == 0) {
            queue.enqueue(v);
         }
      }

      while(!queue.isEmpty()) {
         int v = (Integer)queue.dequeue();
         this.order.enqueue(v);
         this.ranks[v] = count++;

         for(int w : G.adj(v)) {
            int var10 = indegree[w]--;
            if (indegree[w] == 0) {
               queue.enqueue(w);
            }
         }
      }

      if (count != G.V()) {
         this.order = null;
      }

      assert this.check(G);

   }

   public TopologicalX(EdgeWeightedDigraph G) {
      int[] indegree = new int[G.V()];

      for(int v = 0; v < G.V(); ++v) {
         indegree[v] = G.indegree(v);
      }

      this.ranks = new int[G.V()];
      this.order = new Queue<Integer>();
      int count = 0;
      Queue<Integer> queue = new Queue<Integer>();

      for(int v = 0; v < G.V(); ++v) {
         if (indegree[v] == 0) {
            queue.enqueue(v);
         }
      }

      while(!queue.isEmpty()) {
         int v = (Integer)queue.dequeue();
         this.order.enqueue(v);
         this.ranks[v] = count++;

         for(DirectedEdge e : G.adj(v)) {
            int w = e.to();
            int var11 = indegree[w]--;
            if (indegree[w] == 0) {
               queue.enqueue(w);
            }
         }
      }

      if (count != G.V()) {
         this.order = null;
      }

      assert this.check(G);

   }

   public Iterable<Integer> order() {
      return this.order;
   }

   public boolean hasOrder() {
      return this.order != null;
   }

   public int rank(int v) {
      this.validateVertex(v);
      return this.hasOrder() ? this.ranks[v] : -1;
   }

   private boolean check(Digraph G) {
      if (this.hasOrder()) {
         boolean[] found = new boolean[G.V()];

         for(int i = 0; i < G.V(); ++i) {
            found[this.rank(i)] = true;
         }

         for(int i = 0; i < G.V(); ++i) {
            if (!found[i]) {
               System.err.println("No vertex with rank " + i);
               return false;
            }
         }

         for(int v = 0; v < G.V(); ++v) {
            for(int w : G.adj(v)) {
               if (this.rank(v) > this.rank(w)) {
                  System.err.printf("%d-%d: rank(%d) = %d, rank(%d) = %d\n", v, w, v, this.rank(v), w, this.rank(w));
                  return false;
               }
            }
         }

         int r = 0;

         for(int v : this.order()) {
            if (this.rank(v) != r) {
               System.err.println("order() and rank() inconsistent");
               return false;
            }

            ++r;
         }
      }

      return true;
   }

   private boolean check(EdgeWeightedDigraph G) {
      if (this.hasOrder()) {
         boolean[] found = new boolean[G.V()];

         for(int i = 0; i < G.V(); ++i) {
            found[this.rank(i)] = true;
         }

         for(int i = 0; i < G.V(); ++i) {
            if (!found[i]) {
               System.err.println("No vertex with rank " + i);
               return false;
            }
         }

         for(int v = 0; v < G.V(); ++v) {
            for(DirectedEdge e : G.adj(v)) {
               int w = e.to();
               if (this.rank(v) > this.rank(w)) {
                  System.err.printf("%d-%d: rank(%d) = %d, rank(%d) = %d\n", v, w, v, this.rank(v), w, this.rank(w));
                  return false;
               }
            }
         }

         int r = 0;

         for(int v : this.order()) {
            if (this.rank(v) != r) {
               System.err.println("order() and rank() inconsistent");
               return false;
            }

            ++r;
         }
      }

      return true;
   }

   private void validateVertex(int v) {
      int V = this.ranks.length;
      if (v < 0 || v >= V) {
         throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
      }
   }

   public static void main(String[] args) {
      int V = Integer.parseInt(args[0]);
      int E = Integer.parseInt(args[1]);
      int F = Integer.parseInt(args[2]);
      Digraph G1 = DigraphGenerator.dag(V, E);
      EdgeWeightedDigraph G2 = new EdgeWeightedDigraph(V);

      for(int v = 0; v < G1.V(); ++v) {
         for(int w : G1.adj(v)) {
            G2.addEdge(new DirectedEdge(v, w, (double)0.0F));
         }
      }

      for(int i = 0; i < F; ++i) {
         int v = StdRandom.uniformInt(V);
         int w = StdRandom.uniformInt(V);
         G1.addEdge(v, w);
         G2.addEdge(new DirectedEdge(v, w, (double)0.0F));
      }

      StdOut.println(G1);
      StdOut.println();
      StdOut.println(G2);
      TopologicalX topological1 = new TopologicalX(G1);
      if (!topological1.hasOrder()) {
         StdOut.println("Not a DAG");
      } else {
         StdOut.print("Topological order: ");

         for(int v : topological1.order()) {
            StdOut.print(v + " ");
         }

         StdOut.println();
      }

      TopologicalX topological2 = new TopologicalX(G2);
      if (!topological2.hasOrder()) {
         StdOut.println("Not a DAG");
      } else {
         StdOut.print("Topological order: ");

         for(int v : topological2.order()) {
            StdOut.print(v + " ");
         }

         StdOut.println();
      }

   }
}
