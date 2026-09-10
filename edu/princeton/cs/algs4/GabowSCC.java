package edu.princeton.cs.algs4;

public class GabowSCC {
   private boolean[] marked;
   private int[] id;
   private int[] preorder;
   private int pre;
   private int count;
   private Stack<Integer> stack1;
   private Stack<Integer> stack2;

   public GabowSCC(Digraph G) {
      this.marked = new boolean[G.V()];
      this.stack1 = new Stack<Integer>();
      this.stack2 = new Stack<Integer>();
      this.id = new int[G.V()];
      this.preorder = new int[G.V()];

      for(int v = 0; v < G.V(); ++v) {
         this.id[v] = -1;
      }

      for(int v = 0; v < G.V(); ++v) {
         if (!this.marked[v]) {
            this.dfs(G, v);
         }
      }

      assert this.check(G);

   }

   private void dfs(Digraph G, int v) {
      this.marked[v] = true;
      this.preorder[v] = this.pre++;
      this.stack1.push(v);
      this.stack2.push(v);

      for(int w : G.adj(v)) {
         if (!this.marked[w]) {
            this.dfs(G, w);
         } else if (this.id[w] == -1) {
            while(this.preorder[(Integer)this.stack2.peek()] > this.preorder[w]) {
               this.stack2.pop();
            }
         }
      }

      if ((Integer)this.stack2.peek() == v) {
         this.stack2.pop();

         int w;
         do {
            w = (Integer)this.stack1.pop();
            this.id[w] = this.count;
         } while(w != v);

         ++this.count;
      }

   }

   public int count() {
      return this.count;
   }

   public boolean stronglyConnected(int v, int w) {
      this.validateVertex(v);
      this.validateVertex(w);
      return this.id[v] == this.id[w];
   }

   public int id(int v) {
      this.validateVertex(v);
      return this.id[v];
   }

   private boolean check(Digraph G) {
      TransitiveClosure tc = new TransitiveClosure(G);

      for(int v = 0; v < G.V(); ++v) {
         for(int w = 0; w < G.V(); ++w) {
            if (this.stronglyConnected(v, w) != (tc.reachable(v, w) && tc.reachable(w, v))) {
               return false;
            }
         }
      }

      return true;
   }

   private void validateVertex(int v) {
      int V = this.marked.length;
      if (v < 0 || v >= V) {
         throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
      }
   }

   public static void main(String[] args) {
      In in = new In(args[0]);
      Digraph G = new Digraph(in);
      GabowSCC scc = new GabowSCC(G);
      int m = scc.count();
      StdOut.println(m + " components");
      Queue<Integer>[] components = new Queue[m];

      for(int i = 0; i < m; ++i) {
         components[i] = new Queue();
      }

      for(int v = 0; v < G.V(); ++v) {
         components[scc.id(v)].enqueue(v);
      }

      for(int i = 0; i < m; ++i) {
         for(int v : components[i]) {
            StdOut.print(v + " ");
         }

         StdOut.println();
      }

   }
}
