package edu.princeton.cs.algs4;

import java.util.Arrays;

public class SegmentTree {
   private Node[] heap;
   private int[] array;
   private int size;

   public SegmentTree(int[] array) {
      this.array = Arrays.copyOf(array, array.length);
      this.size = (int)((double)2.0F * Math.pow((double)2.0F, Math.floor(Math.log((double)array.length) / Math.log((double)2.0F) + (double)1.0F)));
      this.heap = new Node[this.size];
      this.build(1, 0, array.length);
   }

   public int size() {
      return this.array.length;
   }

   private void build(int v, int from, int size) {
      this.heap[v] = new Node();
      this.heap[v].from = from;
      this.heap[v].to = from + size - 1;
      if (size == 1) {
         this.heap[v].sum = this.array[from];
         this.heap[v].min = this.array[from];
      } else {
         this.build(2 * v, from, size / 2);
         this.build(2 * v + 1, from + size / 2, size - size / 2);
         this.heap[v].sum = this.heap[2 * v].sum + this.heap[2 * v + 1].sum;
         this.heap[v].min = Math.min(this.heap[2 * v].min, this.heap[2 * v + 1].min);
      }

   }

   public int rsq(int from, int to) {
      return this.rsq(1, from, to);
   }

   private int rsq(int v, int from, int to) {
      Node n = this.heap[v];
      if (n.pendingVal != null && this.contains(n.from, n.to, from, to)) {
         return (to - from + 1) * n.pendingVal;
      } else if (this.contains(from, to, n.from, n.to)) {
         return this.heap[v].sum;
      } else if (this.intersects(from, to, n.from, n.to)) {
         this.propagate(v);
         int leftSum = this.rsq(2 * v, from, to);
         int rightSum = this.rsq(2 * v + 1, from, to);
         return leftSum + rightSum;
      } else {
         return 0;
      }
   }

   public int rMinQ(int from, int to) {
      return this.rMinQ(1, from, to);
   }

   private int rMinQ(int v, int from, int to) {
      Node n = this.heap[v];
      if (n.pendingVal != null && this.contains(n.from, n.to, from, to)) {
         return n.pendingVal;
      } else if (this.contains(from, to, n.from, n.to)) {
         return this.heap[v].min;
      } else if (this.intersects(from, to, n.from, n.to)) {
         this.propagate(v);
         int leftMin = this.rMinQ(2 * v, from, to);
         int rightMin = this.rMinQ(2 * v + 1, from, to);
         return Math.min(leftMin, rightMin);
      } else {
         return Integer.MAX_VALUE;
      }
   }

   public void update(int from, int to, int value) {
      this.update(1, from, to, value);
   }

   private void update(int v, int from, int to, int value) {
      Node n = this.heap[v];
      if (this.contains(from, to, n.from, n.to)) {
         this.change(n, value);
      }

      if (n.size() != 1) {
         if (this.intersects(from, to, n.from, n.to)) {
            this.propagate(v);
            this.update(2 * v, from, to, value);
            this.update(2 * v + 1, from, to, value);
            n.sum = this.heap[2 * v].sum + this.heap[2 * v + 1].sum;
            n.min = Math.min(this.heap[2 * v].min, this.heap[2 * v + 1].min);
         }

      }
   }

   private void propagate(int v) {
      Node n = this.heap[v];
      if (n.pendingVal != null) {
         this.change(this.heap[2 * v], n.pendingVal);
         this.change(this.heap[2 * v + 1], n.pendingVal);
         n.pendingVal = null;
      }

   }

   private void change(Node n, int value) {
      n.pendingVal = value;
      n.sum = n.size() * value;
      n.min = value;
      this.array[n.from] = value;
   }

   private boolean contains(int from1, int to1, int from2, int to2) {
      return from2 >= from1 && to2 <= to1;
   }

   private boolean intersects(int from1, int to1, int from2, int to2) {
      return from1 <= from2 && to1 >= from2 || from1 >= from2 && from1 <= to2;
   }

   public static void main(String[] args) {
      SegmentTree st = null;
      String cmd = "cmp";

      while(true) {
         String[] line = StdIn.readLine().split(" ");
         if (line[0].equals("exit")) {
            return;
         }

         int arg1 = 0;
         int arg2 = 0;
         int arg3 = 0;
         if (line.length > 1) {
            arg1 = Integer.parseInt(line[1]);
         }

         if (line.length > 2) {
            arg2 = Integer.parseInt(line[2]);
         }

         if (line.length > 3) {
            arg3 = Integer.parseInt(line[3]);
         }

         if (!line[0].equals("set") && !line[0].equals("init") && st == null) {
            StdOut.println("Segment Tree not initialized");
         } else if (line[0].equals("set")) {
            int[] array = new int[line.length - 1];

            for(int i = 0; i < line.length - 1; ++i) {
               array[i] = Integer.parseInt(line[i + 1]);
            }

            st = new SegmentTree(array);
         } else if (line[0].equals("init")) {
            int[] array = new int[arg1];
            Arrays.fill(array, arg2);
            st = new SegmentTree(array);

            for(int i = 0; i < st.size(); ++i) {
               int var12 = st.rsq(i, i);
               StdOut.print(var12 + " ");
            }

            StdOut.println();
         } else if (!line[0].equals("up")) {
            if (line[0].equals("rsq")) {
               StdOut.printf("Sum from %d to %d = %d%n", arg1, arg2, st.rsq(arg1, arg2));
            } else if (line[0].equals("rmq")) {
               StdOut.printf("Min from %d to %d = %d%n", arg1, arg2, st.rMinQ(arg1, arg2));
            } else {
               StdOut.println("Invalid command");
            }
         } else {
            st.update(arg1, arg2, arg3);

            for(int i = 0; i < st.size(); ++i) {
               int var10000 = st.rsq(i, i);
               StdOut.print(var10000 + " ");
            }

            StdOut.println();
         }
      }
   }

   static class Node {
      int sum;
      int min;
      Integer pendingVal = null;
      int from;
      int to;

      int size() {
         return this.to - this.from + 1;
      }
   }
}
