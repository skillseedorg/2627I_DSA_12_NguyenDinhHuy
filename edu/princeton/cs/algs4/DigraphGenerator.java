package edu.princeton.cs.algs4;

public class DigraphGenerator {
   private DigraphGenerator() {
   }

   public static Digraph simple(int V, int E) {
      if ((long)E > (long)V * (long)(V - 1)) {
         throw new IllegalArgumentException("Too many edges");
      } else if (E < 0) {
         throw new IllegalArgumentException("Too few edges");
      } else {
         Digraph G = new Digraph(V);
         SET<Edge> set = new SET<Edge>();

         while(G.E() < E) {
            int v = StdRandom.uniformInt(V);
            int w = StdRandom.uniformInt(V);
            Edge e = new Edge(v, w);
            if (v != w && !set.contains(e)) {
               set.add(e);
               G.addEdge(v, w);
            }
         }

         return G;
      }
   }

   public static Digraph simple(int V, double p) {
      if (!(p < (double)0.0F) && !(p > (double)1.0F)) {
         Digraph G = new Digraph(V);

         for(int v = 0; v < V; ++v) {
            for(int w = 0; w < V; ++w) {
               if (v != w && StdRandom.bernoulli(p)) {
                  G.addEdge(v, w);
               }
            }
         }

         return G;
      } else {
         throw new IllegalArgumentException("Probability must be between 0 and 1");
      }
   }

   public static Digraph complete(int V) {
      Digraph G = new Digraph(V);

      for(int v = 0; v < V; ++v) {
         for(int w = 0; w < V; ++w) {
            if (v != w) {
               G.addEdge(v, w);
            }
         }
      }

      return G;
   }

   public static Digraph dag(int V, int E) {
      if ((long)E > (long)V * (long)(V - 1) / 2L) {
         throw new IllegalArgumentException("Too many edges");
      } else if (E < 0) {
         throw new IllegalArgumentException("Too few edges");
      } else {
         Digraph G = new Digraph(V);
         SET<Edge> set = new SET<Edge>();
         int[] vertices = new int[V];

         for(int i = 0; i < V; vertices[i] = i++) {
         }

         StdRandom.shuffle(vertices);

         while(G.E() < E) {
            int v = StdRandom.uniformInt(V);
            int w = StdRandom.uniformInt(V);
            Edge e = new Edge(v, w);
            if (v < w && !set.contains(e)) {
               set.add(e);
               G.addEdge(vertices[v], vertices[w]);
            }
         }

         return G;
      }
   }

   public static Digraph tournament(int V) {
      Digraph G = new Digraph(V);

      for(int v = 0; v < G.V(); ++v) {
         for(int w = v + 1; w < G.V(); ++w) {
            if (StdRandom.bernoulli((double)0.5F)) {
               G.addEdge(v, w);
            } else {
               G.addEdge(w, v);
            }
         }
      }

      return G;
   }

   public static Digraph completeRootedInDAG(int V) {
      Digraph G = new Digraph(V);
      int[] vertices = new int[V];

      for(int i = 0; i < V; vertices[i] = i++) {
      }

      StdRandom.shuffle(vertices);

      for(int i = 0; i < V; ++i) {
         for(int j = i + 1; j < V; ++j) {
            G.addEdge(vertices[i], vertices[j]);
         }
      }

      return G;
   }

   public static Digraph rootedInDAG(int V, int E) {
      if ((long)E > (long)V * (long)(V - 1) / 2L) {
         throw new IllegalArgumentException("Too many edges");
      } else if (E < V - 1) {
         throw new IllegalArgumentException("Too few edges");
      } else {
         Digraph G = new Digraph(V);
         SET<Edge> set = new SET<Edge>();
         int[] vertices = new int[V];

         for(int i = 0; i < V; vertices[i] = i++) {
         }

         StdRandom.shuffle(vertices);

         for(int v = 0; v < V - 1; ++v) {
            int w = StdRandom.uniformInt(v + 1, V);
            Edge e = new Edge(v, w);
            set.add(e);
            G.addEdge(vertices[v], vertices[w]);
         }

         while(G.E() < E) {
            int v = StdRandom.uniformInt(V);
            int w = StdRandom.uniformInt(V);
            Edge e = new Edge(v, w);
            if (v < w && !set.contains(e)) {
               set.add(e);
               G.addEdge(vertices[v], vertices[w]);
            }
         }

         return G;
      }
   }

   public static Digraph completeRootedOutDAG(int V) {
      Digraph G = new Digraph(V);
      int[] vertices = new int[V];

      for(int i = 0; i < V; vertices[i] = i++) {
      }

      StdRandom.shuffle(vertices);

      for(int i = 0; i < V; ++i) {
         for(int j = i + 1; j < V; ++j) {
            G.addEdge(vertices[j], vertices[i]);
         }
      }

      return G;
   }

   public static Digraph rootedOutDAG(int V, int E) {
      if ((long)E > (long)V * (long)(V - 1) / 2L) {
         throw new IllegalArgumentException("Too many edges");
      } else if (E < V - 1) {
         throw new IllegalArgumentException("Too few edges");
      } else {
         Digraph G = new Digraph(V);
         SET<Edge> set = new SET<Edge>();
         int[] vertices = new int[V];

         for(int i = 0; i < V; vertices[i] = i++) {
         }

         StdRandom.shuffle(vertices);

         for(int v = 0; v < V - 1; ++v) {
            int w = StdRandom.uniformInt(v + 1, V);
            Edge e = new Edge(w, v);
            set.add(e);
            G.addEdge(vertices[w], vertices[v]);
         }

         while(G.E() < E) {
            int v = StdRandom.uniformInt(V);
            int w = StdRandom.uniformInt(V);
            Edge e = new Edge(w, v);
            if (v < w && !set.contains(e)) {
               set.add(e);
               G.addEdge(vertices[w], vertices[v]);
            }
         }

         return G;
      }
   }

   public static Digraph rootedInTree(int V) {
      return rootedInDAG(V, V - 1);
   }

   public static Digraph rootedOutTree(int V) {
      return rootedOutDAG(V, V - 1);
   }

   public static Digraph path(int V) {
      Digraph G = new Digraph(V);
      int[] vertices = new int[V];

      for(int i = 0; i < V; vertices[i] = i++) {
      }

      StdRandom.shuffle(vertices);

      for(int i = 0; i < V - 1; ++i) {
         G.addEdge(vertices[i], vertices[i + 1]);
      }

      return G;
   }

   public static Digraph binaryTree(int V) {
      Digraph G = new Digraph(V);
      int[] vertices = new int[V];

      for(int i = 0; i < V; vertices[i] = i++) {
      }

      StdRandom.shuffle(vertices);

      for(int i = 1; i < V; ++i) {
         G.addEdge(vertices[i], vertices[(i - 1) / 2]);
      }

      return G;
   }

   public static Digraph cycle(int V) {
      Digraph G = new Digraph(V);
      int[] vertices = new int[V];

      for(int i = 0; i < V; vertices[i] = i++) {
      }

      StdRandom.shuffle(vertices);

      for(int i = 0; i < V - 1; ++i) {
         G.addEdge(vertices[i], vertices[i + 1]);
      }

      G.addEdge(vertices[V - 1], vertices[0]);
      return G;
   }

   public static Digraph eulerianCycle(int V, int E) {
      if (E <= 0) {
         throw new IllegalArgumentException("An Eulerian cycle must have at least one edge");
      } else if (V <= 0) {
         throw new IllegalArgumentException("An Eulerian cycle must have at least one vertex");
      } else {
         Digraph G = new Digraph(V);
         int[] vertices = new int[E];

         for(int i = 0; i < E; ++i) {
            vertices[i] = StdRandom.uniformInt(V);
         }

         for(int i = 0; i < E - 1; ++i) {
            G.addEdge(vertices[i], vertices[i + 1]);
         }

         G.addEdge(vertices[E - 1], vertices[0]);
         return G;
      }
   }

   public static Digraph eulerianPath(int V, int E) {
      if (E < 0) {
         throw new IllegalArgumentException("negative number of edges");
      } else if (V <= 0) {
         throw new IllegalArgumentException("An Eulerian path must have at least one vertex");
      } else {
         Digraph G = new Digraph(V);
         int[] vertices = new int[E + 1];

         for(int i = 0; i < E + 1; ++i) {
            vertices[i] = StdRandom.uniformInt(V);
         }

         for(int i = 0; i < E; ++i) {
            G.addEdge(vertices[i], vertices[i + 1]);
         }

         return G;
      }
   }

   public static Digraph strong(int V, int E, int c) {
      if (c < V && c > 0) {
         if (E <= 2 * (V - c)) {
            throw new IllegalArgumentException("Number of edges must be at least 2(V-c)");
         } else if ((long)E > (long)V * (long)(V - 1) / 2L) {
            throw new IllegalArgumentException("Too many edges");
         } else {
            Digraph G = new Digraph(V);
            SET<Edge> set = new SET<Edge>();
            int[] label = new int[V];

            for(int v = 0; v < V; ++v) {
               label[v] = StdRandom.uniformInt(c);
            }

            for(int i = 0; i < c; ++i) {
               int count = 0;

               for(int v = 0; v < G.V(); ++v) {
                  if (label[v] == i) {
                     ++count;
                  }
               }

               int[] vertices = new int[count];
               int j = 0;

               for(int v = 0; v < V; ++v) {
                  if (label[v] == i) {
                     vertices[j++] = v;
                  }
               }

               StdRandom.shuffle(vertices);

               for(int v = 0; v < count - 1; ++v) {
                  int w = StdRandom.uniformInt(v + 1, count);
                  Edge e = new Edge(w, v);
                  set.add(e);
                  G.addEdge(vertices[w], vertices[v]);
               }

               for(int v = 0; v < count - 1; ++v) {
                  int w = StdRandom.uniformInt(v + 1, count);
                  Edge e = new Edge(v, w);
                  set.add(e);
                  G.addEdge(vertices[v], vertices[w]);
               }
            }

            while(G.E() < E) {
               int v = StdRandom.uniformInt(V);
               int w = StdRandom.uniformInt(V);
               Edge e = new Edge(v, w);
               if (!set.contains(e) && v != w && label[v] <= label[w]) {
                  set.add(e);
                  G.addEdge(v, w);
               }
            }

            return G;
         }
      } else {
         throw new IllegalArgumentException("Number of components must be between 1 and V");
      }
   }

   public static void main(String[] args) {
      int V = Integer.parseInt(args[0]);
      int E = Integer.parseInt(args[1]);
      StdOut.println("complete graph");
      StdOut.println(complete(V));
      StdOut.println();
      StdOut.println("simple");
      StdOut.println(simple(V, E));
      StdOut.println();
      StdOut.println("path");
      StdOut.println(path(V));
      StdOut.println();
      StdOut.println("cycle");
      StdOut.println(cycle(V));
      StdOut.println();
      StdOut.println("Eulierian path");
      StdOut.println(eulerianPath(V, E));
      StdOut.println();
      StdOut.println("Eulierian cycle");
      StdOut.println(eulerianCycle(V, E));
      StdOut.println();
      StdOut.println("binary tree");
      StdOut.println(binaryTree(V));
      StdOut.println();
      StdOut.println("tournament");
      StdOut.println(tournament(V));
      StdOut.println();
      StdOut.println("DAG");
      StdOut.println(dag(V, E));
      StdOut.println();
      StdOut.println("rooted-in DAG");
      StdOut.println(rootedInDAG(V, E));
      StdOut.println();
      StdOut.println("rooted-out DAG");
      StdOut.println(rootedOutDAG(V, E));
      StdOut.println();
      StdOut.println("rooted-in tree");
      StdOut.println(rootedInTree(V));
      StdOut.println();
      StdOut.println("rooted-out DAG");
      StdOut.println(rootedOutTree(V));
      StdOut.println();
   }

   private static final class Edge implements Comparable<Edge> {
      private final int v;
      private final int w;

      private Edge(int v, int w) {
         this.v = v;
         this.w = w;
      }

      public int compareTo(Edge that) {
         if (this.v < that.v) {
            return -1;
         } else if (this.v > that.v) {
            return 1;
         } else if (this.w < that.w) {
            return -1;
         } else {
            return this.w > that.w ? 1 : 0;
         }
      }
   }
}
