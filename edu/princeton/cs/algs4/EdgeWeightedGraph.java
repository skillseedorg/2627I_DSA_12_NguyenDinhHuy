package edu.princeton.cs.algs4;

import java.util.NoSuchElementException;

public class EdgeWeightedGraph {
   private static final String NEWLINE = System.getProperty("line.separator");
   private final int V;
   private int E;
   private Bag<Edge>[] adj;

   public EdgeWeightedGraph(int V) {
      if (V < 0) {
         throw new IllegalArgumentException("Number of vertices must be non-negative");
      } else {
         this.V = V;
         this.E = 0;
         this.adj = new Bag[V];

         for(int v = 0; v < V; ++v) {
            this.adj[v] = new Bag();
         }

      }
   }

   public EdgeWeightedGraph(int V, int E) {
      this(V);
      if (E < 0) {
         throw new IllegalArgumentException("Number of edges must be non-negative");
      } else {
         for(int i = 0; i < E; ++i) {
            int v = StdRandom.uniformInt(V);
            int w = StdRandom.uniformInt(V);
            double weight = 0.01 * (double)StdRandom.uniformInt(0, 100);
            Edge e = new Edge(v, w, weight);
            this.addEdge(e);
         }

      }
   }

   public EdgeWeightedGraph(In in) {
      if (in == null) {
         throw new IllegalArgumentException("argument is null");
      } else {
         try {
            this.V = in.readInt();
            this.adj = new Bag[this.V];

            for(int v = 0; v < this.V; ++v) {
               this.adj[v] = new Bag();
            }

            int E = in.readInt();
            if (E < 0) {
               throw new IllegalArgumentException("Number of edges must be non-negative");
            } else {
               for(int i = 0; i < E; ++i) {
                  int v = in.readInt();
                  int w = in.readInt();
                  this.validateVertex(v);
                  this.validateVertex(w);
                  double weight = in.readDouble();
                  Edge e = new Edge(v, w, weight);
                  this.addEdge(e);
               }

            }
         } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("invalid input format in EdgeWeightedGraph constructor", e);
         }
      }
   }

   public EdgeWeightedGraph(EdgeWeightedGraph G) {
      this(G.V());
      this.E = G.E();

      for(int v = 0; v < G.V(); ++v) {
         Stack<Edge> reverse = new Stack<Edge>();

         for(Edge e : G.adj[v]) {
            reverse.push(e);
         }

         for(Edge e : reverse) {
            this.adj[v].add(e);
         }
      }

   }

   public int V() {
      return this.V;
   }

   public int E() {
      return this.E;
   }

   private void validateVertex(int v) {
      if (v < 0 || v >= this.V) {
         throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (this.V - 1));
      }
   }

   public void addEdge(Edge e) {
      int v = e.either();
      int w = e.other(v);
      this.validateVertex(v);
      this.validateVertex(w);
      this.adj[v].add(e);
      this.adj[w].add(e);
      ++this.E;
   }

   public Iterable<Edge> adj(int v) {
      this.validateVertex(v);
      return this.adj[v];
   }

   public int degree(int v) {
      this.validateVertex(v);
      return this.adj[v].size();
   }

   public Iterable<Edge> edges() {
      Bag<Edge> list = new Bag<Edge>();

      for(int v = 0; v < this.V; ++v) {
         int selfLoops = 0;

         for(Edge e : this.adj(v)) {
            if (e.other(v) > v) {
               list.add(e);
            } else if (e.other(v) == v) {
               if (selfLoops % 2 == 0) {
                  list.add(e);
               }

               ++selfLoops;
            }
         }
      }

      return list;
   }

   public String toString() {
      StringBuilder s = new StringBuilder();
      s.append(this.V + " " + this.E + NEWLINE);

      for(int v = 0; v < this.V; ++v) {
         s.append(v + ": ");

         for(Edge e : this.adj[v]) {
            s.append(e + "  ");
         }

         s.append(NEWLINE);
      }

      return s.toString();
   }

   public String toDot() {
      StringBuilder s = new StringBuilder();
      s.append("graph {" + NEWLINE);
      s.append("node[shape=circle, style=filled, fixedsize=true, width=0.3, fontsize=\"10pt\"]" + NEWLINE);
      s.append("edge[arrowhead=normal, fontsize=\"9pt\"]" + NEWLINE);

      for(Edge e : this.edges()) {
         int v = e.either();
         int w = e.other(v);
         s.append(v + " --" + w + " [label=\"" + e.weight() + "\"]" + NEWLINE);
      }

      s.append("}" + NEWLINE);
      return s.toString();
   }

   public static void main(String[] args) {
      In in = new In(args[0]);
      EdgeWeightedGraph G = new EdgeWeightedGraph(in);
      StdOut.println(G);
   }
}
