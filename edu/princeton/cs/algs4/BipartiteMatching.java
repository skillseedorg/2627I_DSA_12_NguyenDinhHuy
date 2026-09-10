package edu.princeton.cs.algs4;

public class BipartiteMatching {
   private static final int UNMATCHED = -1;
   private final int V;
   private BipartiteX bipartition;
   private int cardinality;
   private int[] mate;
   private boolean[] inMinVertexCover;
   private boolean[] marked;
   private int[] edgeTo;

   public BipartiteMatching(Graph G) {
      this.bipartition = new BipartiteX(G);
      if (!this.bipartition.isBipartite()) {
         throw new IllegalArgumentException("graph is not bipartite");
      } else {
         this.V = G.V();
         this.mate = new int[this.V];

         for(int v = 0; v < this.V; ++v) {
            this.mate[v] = -1;
         }

         while(this.hasAugmentingPath(G)) {
            int t = -1;

            for(int v = 0; v < G.V(); ++v) {
               if (!this.isMatched(v) && this.edgeTo[v] != -1) {
                  t = v;
                  break;
               }
            }

            for(int v = t; v != -1; v = this.edgeTo[this.edgeTo[v]]) {
               int w = this.edgeTo[v];
               this.mate[v] = w;
               this.mate[w] = v;
            }

            ++this.cardinality;
         }

         this.inMinVertexCover = new boolean[this.V];

         for(int v = 0; v < this.V; ++v) {
            if (this.bipartition.color(v) && !this.marked[v]) {
               this.inMinVertexCover[v] = true;
            }

            if (!this.bipartition.color(v) && this.marked[v]) {
               this.inMinVertexCover[v] = true;
            }
         }

         assert this.certifySolution(G);

      }
   }

   private boolean hasAugmentingPath(Graph G) {
      this.marked = new boolean[this.V];
      this.edgeTo = new int[this.V];

      for(int v = 0; v < this.V; ++v) {
         this.edgeTo[v] = -1;
      }

      Queue<Integer> queue = new Queue<Integer>();

      for(int v = 0; v < this.V; ++v) {
         if (this.bipartition.color(v) && !this.isMatched(v)) {
            queue.enqueue(v);
            this.marked[v] = true;
         }
      }

      while(!queue.isEmpty()) {
         int v = (Integer)queue.dequeue();

         for(int w : G.adj(v)) {
            if (this.isResidualGraphEdge(v, w) && !this.marked[w]) {
               this.edgeTo[w] = v;
               this.marked[w] = true;
               if (!this.isMatched(w)) {
                  return true;
               }

               queue.enqueue(w);
            }
         }
      }

      return false;
   }

   private boolean isResidualGraphEdge(int v, int w) {
      if (this.mate[v] != w && this.bipartition.color(v)) {
         return true;
      } else {
         return this.mate[v] == w && !this.bipartition.color(v);
      }
   }

   public int mate(int v) {
      this.validate(v);
      return this.mate[v];
   }

   public boolean isMatched(int v) {
      this.validate(v);
      return this.mate[v] != -1;
   }

   public int size() {
      return this.cardinality;
   }

   public boolean isPerfect() {
      return this.cardinality * 2 == this.V;
   }

   public boolean inMinVertexCover(int v) {
      this.validate(v);
      return this.inMinVertexCover[v];
   }

   private void validate(int v) {
      if (v < 0 || v >= this.V) {
         throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (this.V - 1));
      }
   }

   private boolean certifySolution(Graph G) {
      for(int v = 0; v < this.V; ++v) {
         if (this.mate(v) != -1 && this.mate(this.mate(v)) != v) {
            return false;
         }
      }

      int matchedVertices = 0;

      for(int v = 0; v < this.V; ++v) {
         if (this.mate(v) != -1) {
            ++matchedVertices;
         }
      }

      if (2 * this.size() != matchedVertices) {
         return false;
      } else {
         int sizeOfMinVertexCover = 0;

         for(int v = 0; v < this.V; ++v) {
            if (this.inMinVertexCover(v)) {
               ++sizeOfMinVertexCover;
            }
         }

         if (this.size() != sizeOfMinVertexCover) {
            return false;
         } else {
            boolean[] isMatched = new boolean[this.V];

            for(int v = 0; v < this.V; ++v) {
               int w = this.mate[v];
               if (w != -1) {
                  if (v == w) {
                     return false;
                  }

                  if (v < w) {
                     if (isMatched[v] || isMatched[w]) {
                        return false;
                     }

                     isMatched[v] = true;
                     isMatched[w] = true;
                  }
               }
            }

            for(int v = 0; v < this.V; ++v) {
               if (this.mate(v) != -1) {
                  boolean isEdge = false;

                  for(int w : G.adj(v)) {
                     if (this.mate(v) == w) {
                        isEdge = true;
                     }
                  }

                  if (!isEdge) {
                     return false;
                  }
               }
            }

            for(int v = 0; v < this.V; ++v) {
               for(int w : G.adj(v)) {
                  if (!this.inMinVertexCover(v) && !this.inMinVertexCover(w)) {
                     return false;
                  }
               }
            }

            return true;
         }
      }
   }

   public static void main(String[] args) {
      int V1 = Integer.parseInt(args[0]);
      int V2 = Integer.parseInt(args[1]);
      int E = Integer.parseInt(args[2]);
      Graph G = GraphGenerator.bipartite(V1, V2, E);
      if (G.V() < 1000) {
         StdOut.println(G);
      }

      BipartiteMatching matching = new BipartiteMatching(G);
      StdOut.printf("Number of edges in max matching        = %d\n", matching.size());
      StdOut.printf("Number of vertices in min vertex cover = %d\n", matching.size());
      StdOut.printf("Graph has a perfect matching           = %b\n", matching.isPerfect());
      StdOut.println();
      if (G.V() < 1000) {
         StdOut.print("Max matching: ");

         for(int v = 0; v < G.V(); ++v) {
            int w = matching.mate(v);
            if (matching.isMatched(v) && v < w) {
               StdOut.print(v + "-" + w + " ");
            }
         }

         StdOut.println();
         StdOut.print("Min vertex cover: ");

         for(int v = 0; v < G.V(); ++v) {
            if (matching.inMinVertexCover(v)) {
               StdOut.print(v + " ");
            }
         }

         StdOut.println();
      }
   }
}
