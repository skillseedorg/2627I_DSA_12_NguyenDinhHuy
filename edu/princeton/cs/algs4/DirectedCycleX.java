package edu.princeton.cs.algs4;

public class DirectedCycleX {
   private Stack<Integer> cycle;

   public DirectedCycleX(Digraph G) {
      int[] indegree = new int[G.V()];

      for(int v = 0; v < G.V(); ++v) {
         indegree[v] = G.indegree(v);
      }

      Queue<Integer> queue = new Queue<Integer>();

      for(int v = 0; v < G.V(); ++v) {
         if (indegree[v] == 0) {
            queue.enqueue(v);
         }
      }

      while(!queue.isEmpty()) {
         int v = (Integer)queue.dequeue();

         for(int w : G.adj(v)) {
            int var10002 = indegree[w]--;
            if (indegree[w] == 0) {
               queue.enqueue(w);
            }
         }
      }

      int[] edgeTo = new int[G.V()];
      int root = -1;

      for(int v = 0; v < G.V(); ++v) {
         if (indegree[v] != 0) {
            root = v;

            for(int w : G.adj(v)) {
               if (indegree[w] > 0) {
                  edgeTo[w] = v;
               }
            }
         }
      }

      if (root != -1) {
         for(boolean[] visited = new boolean[G.V()]; !visited[root]; root = edgeTo[root]) {
            visited[root] = true;
         }

         this.cycle = new Stack<Integer>();
         int v = root;

         do {
            this.cycle.push(v);
            v = edgeTo[v];
         } while(v != root);

         this.cycle.push(root);
      }

      assert this.check();

   }

   public Iterable<Integer> cycle() {
      return this.cycle;
   }

   public boolean hasCycle() {
      return this.cycle != null;
   }

   private boolean check() {
      if (this.hasCycle()) {
         int first = -1;
         int last = -1;

         for(int v : this.cycle()) {
            if (first == -1) {
               first = v;
            }

            last = v;
         }

         if (first != last) {
            System.err.printf("cycle begins with %d and ends with %d\n", first, last);
            return false;
         }
      }

      return true;
   }

   public static void main(String[] args) {
      int V = Integer.parseInt(args[0]);
      int E = Integer.parseInt(args[1]);
      int F = Integer.parseInt(args[2]);
      Digraph G = DigraphGenerator.dag(V, E);

      for(int i = 0; i < F; ++i) {
         int v = StdRandom.uniformInt(V);
         int w = StdRandom.uniformInt(V);
         G.addEdge(v, w);
      }

      StdOut.println(G);
      DirectedCycleX finder = new DirectedCycleX(G);
      if (finder.hasCycle()) {
         StdOut.print("Directed cycle: ");

         for(int v : finder.cycle()) {
            StdOut.print(v + " ");
         }

         StdOut.println();
      } else {
         StdOut.println("No directed cycle");
      }

      StdOut.println();
   }
}
