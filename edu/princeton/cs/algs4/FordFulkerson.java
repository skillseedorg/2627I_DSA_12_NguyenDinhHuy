package edu.princeton.cs.algs4;

import java.io.PrintStream;

public class FordFulkerson {
   private static final double FLOATING_POINT_EPSILON = 1.0E-11;
   private final int V;
   private boolean[] marked;
   private FlowEdge[] edgeTo;
   private double value;

   public FordFulkerson(FlowNetwork G, int s, int t) {
      this.V = G.V();
      this.validate(s);
      this.validate(t);
      if (s == t) {
         throw new IllegalArgumentException("Source equals sink");
      } else if (!this.isFeasible(G, s, t)) {
         throw new IllegalArgumentException("Initial flow is infeasible");
      } else {
         double bottle;
         for(this.value = this.excess(G, t); this.hasAugmentingPath(G, s, t); this.value += bottle) {
            bottle = Double.POSITIVE_INFINITY;

            for(int v = t; v != s; v = this.edgeTo[v].other(v)) {
               bottle = Math.min(bottle, this.edgeTo[v].residualCapacityTo(v));
            }

            for(int v = t; v != s; v = this.edgeTo[v].other(v)) {
               this.edgeTo[v].addResidualFlowTo(v, bottle);
            }
         }

         assert this.check(G, s, t);

      }
   }

   public double value() {
      return this.value;
   }

   public boolean inCut(int v) {
      this.validate(v);
      return this.marked[v];
   }

   private void validate(int v) {
      if (v < 0 || v >= this.V) {
         throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (this.V - 1));
      }
   }

   private boolean hasAugmentingPath(FlowNetwork G, int s, int t) {
      this.edgeTo = new FlowEdge[G.V()];
      this.marked = new boolean[G.V()];
      Queue<Integer> queue = new Queue<Integer>();
      queue.enqueue(s);
      this.marked[s] = true;

      while(!queue.isEmpty() && !this.marked[t]) {
         int v = (Integer)queue.dequeue();

         for(FlowEdge e : G.adj(v)) {
            int w = e.other(v);
            if (e.residualCapacityTo(w) > (double)0.0F && !this.marked[w]) {
               this.edgeTo[w] = e;
               this.marked[w] = true;
               queue.enqueue(w);
            }
         }
      }

      return this.marked[t];
   }

   private double excess(FlowNetwork G, int v) {
      double excess = (double)0.0F;

      for(FlowEdge e : G.adj(v)) {
         if (v == e.from()) {
            excess -= e.flow();
         } else {
            excess += e.flow();
         }
      }

      return excess;
   }

   private boolean isFeasible(FlowNetwork G, int s, int t) {
      for(int v = 0; v < G.V(); ++v) {
         for(FlowEdge e : G.adj(v)) {
            if (e.flow() < -1.0E-11 || e.flow() > e.capacity() + 1.0E-11) {
               System.err.println("Edge does not satisfy capacity constraints: " + e);
               return false;
            }
         }
      }

      if (Math.abs(this.value + this.excess(G, s)) > 1.0E-11) {
         PrintStream var8 = System.err;
         double var9 = this.excess(G, s);
         var8.println("Excess at source = " + var9);
         System.err.println("Max flow         = " + this.value);
         return false;
      } else if (Math.abs(this.value - this.excess(G, t)) > 1.0E-11) {
         PrintStream var10000 = System.err;
         double var10001 = this.excess(G, t);
         var10000.println("Excess at sink   = " + var10001);
         System.err.println("Max flow         = " + this.value);
         return false;
      } else {
         for(int v = 0; v < G.V(); ++v) {
            if (v != s && v != t && Math.abs(this.excess(G, v)) > 1.0E-11) {
               System.err.println("Net flow out of " + v + " doesn't equal zero");
               return false;
            }
         }

         return true;
      }
   }

   private boolean check(FlowNetwork G, int s, int t) {
      if (!this.isFeasible(G, s, t)) {
         System.err.println("Flow is infeasible");
         return false;
      } else if (!this.inCut(s)) {
         System.err.println("source " + s + " is not on source side of min cut");
         return false;
      } else if (this.inCut(t)) {
         System.err.println("sink " + t + " is on source side of min cut");
         return false;
      } else {
         double mincutValue = (double)0.0F;

         for(int v = 0; v < G.V(); ++v) {
            for(FlowEdge e : G.adj(v)) {
               if (v == e.from() && this.inCut(e.from()) && !this.inCut(e.to())) {
                  mincutValue += e.capacity();
               }
            }
         }

         if (Math.abs(mincutValue - this.value) > 1.0E-11) {
            System.err.println("Max flow value = " + this.value + ", min cut value = " + mincutValue);
            return false;
         } else {
            return true;
         }
      }
   }

   public static void main(String[] args) {
      int V = Integer.parseInt(args[0]);
      int E = Integer.parseInt(args[1]);
      int s = 0;
      int t = V - 1;
      FlowNetwork G = new FlowNetwork(V, E);
      StdOut.println(G);
      FordFulkerson maxflow = new FordFulkerson(G, s, t);
      StdOut.println("Max flow from " + s + " to " + t);

      for(int v = 0; v < G.V(); ++v) {
         for(FlowEdge e : G.adj(v)) {
            if (v == e.from() && e.flow() > (double)0.0F) {
               StdOut.println("   " + e);
            }
         }
      }

      StdOut.print("Min cut: ");

      for(int v = 0; v < G.V(); ++v) {
         if (maxflow.inCut(v)) {
            StdOut.print(v + " ");
         }
      }

      StdOut.println();
      StdOut.println("Max flow value = " + maxflow.value());
   }
}
