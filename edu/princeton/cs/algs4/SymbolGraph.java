package edu.princeton.cs.algs4;

public class SymbolGraph {
   private ST<String, Integer> st = new ST<String, Integer>();
   private String[] keys;
   private Graph graph;

   public SymbolGraph(String filename, String delimiter) {
      In in = new In(filename);

      while(!in.isEmpty()) {
         String[] a = in.readLine().split(delimiter);

         for(int i = 0; i < a.length; ++i) {
            if (!this.st.contains(a[i])) {
               this.st.put(a[i], this.st.size());
            }
         }
      }

      this.keys = new String[this.st.size()];

      for(String name : this.st.keys()) {
         this.keys[(Integer)this.st.get(name)] = name;
      }

      this.graph = new Graph(this.st.size());
      in = new In(filename);

      while(in.hasNextLine()) {
         String[] a = in.readLine().split(delimiter);
         int v = (Integer)this.st.get(a[0]);

         for(int i = 1; i < a.length; ++i) {
            int w = (Integer)this.st.get(a[i]);
            this.graph.addEdge(v, w);
         }
      }

   }

   public boolean contains(String s) {
      return this.st.contains(s);
   }

   /** @deprecated */
   @Deprecated
   public int index(String s) {
      return (Integer)this.st.get(s);
   }

   public int indexOf(String s) {
      return (Integer)this.st.get(s);
   }

   /** @deprecated */
   @Deprecated
   public String name(int v) {
      this.validateVertex(v);
      return this.keys[v];
   }

   public String nameOf(int v) {
      this.validateVertex(v);
      return this.keys[v];
   }

   /** @deprecated */
   @Deprecated
   public Graph G() {
      return this.graph;
   }

   public Graph graph() {
      return this.graph;
   }

   private void validateVertex(int v) {
      int V = this.graph.V();
      if (v < 0 || v >= V) {
         throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
      }
   }

   public static void main(String[] args) {
      String filename = args[0];
      String delimiter = args[1];
      SymbolGraph sg = new SymbolGraph(filename, delimiter);
      Graph graph = sg.graph();

      while(StdIn.hasNextLine()) {
         String source = StdIn.readLine();
         if (sg.contains(source)) {
            int s = sg.index(source);

            for(int v : graph.adj(s)) {
               String var10000 = sg.nameOf(v);
               StdOut.println("   " + var10000);
            }
         } else {
            StdOut.println("input not contain '" + source + "'");
         }
      }

   }
}
