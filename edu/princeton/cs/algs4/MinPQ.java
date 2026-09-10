package edu.princeton.cs.algs4;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MinPQ<Key> implements Iterable<Key> {
   private Key[] pq;
   private int n;
   private Comparator<Key> comparator;

   public MinPQ(int initCapacity) {
      this.pq = (Key[])(new Object[initCapacity + 1]);
      this.n = 0;
   }

   public MinPQ() {
      this(1);
   }

   public MinPQ(int initCapacity, Comparator<Key> comparator) {
      this.comparator = comparator;
      this.pq = (Key[])(new Object[initCapacity + 1]);
      this.n = 0;
   }

   public MinPQ(Comparator<Key> comparator) {
      this(1, comparator);
   }

   public MinPQ(Key[] keys) {
      this.n = keys.length;
      this.pq = (Key[])(new Object[keys.length + 1]);

      for(int i = 0; i < this.n; ++i) {
         this.pq[i + 1] = keys[i];
      }

      for(int k = this.n / 2; k >= 1; --k) {
         this.sink(k);
      }

      assert this.isMinHeap();

   }

   public boolean isEmpty() {
      return this.n == 0;
   }

   public int size() {
      return this.n;
   }

   public Key min() {
      if (this.isEmpty()) {
         throw new NoSuchElementException("Priority queue underflow");
      } else {
         return (Key)this.pq[1];
      }
   }

   private void resize(int capacity) {
      assert capacity > this.n;

      Key[] temp = (Key[])(new Object[capacity]);

      for(int i = 1; i <= this.n; ++i) {
         temp[i] = this.pq[i];
      }

      this.pq = temp;
   }

   public void insert(Key x) {
      if (this.n == this.pq.length - 1) {
         this.resize(2 * this.pq.length);
      }

      this.pq[++this.n] = x;
      this.swim(this.n);

      assert this.isMinHeap();

   }

   public Key delMin() {
      if (this.isEmpty()) {
         throw new NoSuchElementException("Priority queue underflow");
      } else {
         Key min = (Key)this.pq[1];
         this.exch(1, this.n--);
         this.sink(1);
         this.pq[this.n + 1] = null;
         if (this.n > 0 && this.n == (this.pq.length - 1) / 4) {
            this.resize(this.pq.length / 2);
         }

         assert this.isMinHeap();

         return min;
      }
   }

   private void swim(int k) {
      while(k > 1 && this.greater(k / 2, k)) {
         this.exch(k / 2, k);
         k /= 2;
      }

   }

   private void sink(int k) {
      while(true) {
         if (2 * k <= this.n) {
            int j = 2 * k;
            if (j < this.n && this.greater(j, j + 1)) {
               ++j;
            }

            if (this.greater(k, j)) {
               this.exch(k, j);
               k = j;
               continue;
            }
         }

         return;
      }
   }

   private boolean greater(int i, int j) {
      if (this.comparator == null) {
         return ((Comparable)this.pq[i]).compareTo(this.pq[j]) > 0;
      } else {
         return this.comparator.compare(this.pq[i], this.pq[j]) > 0;
      }
   }

   private void exch(int i, int j) {
      Key swap = (Key)this.pq[i];
      this.pq[i] = this.pq[j];
      this.pq[j] = swap;
   }

   private boolean isMinHeap() {
      for(int i = 1; i <= this.n; ++i) {
         if (this.pq[i] == null) {
            return false;
         }
      }

      for(int i = this.n + 1; i < this.pq.length; ++i) {
         if (this.pq[i] != null) {
            return false;
         }
      }

      if (this.pq[0] != null) {
         return false;
      } else {
         return this.isMinHeapOrdered(1);
      }
   }

   private boolean isMinHeapOrdered(int k) {
      if (k > this.n) {
         return true;
      } else {
         int left = 2 * k;
         int right = 2 * k + 1;
         if (left <= this.n && this.greater(k, left)) {
            return false;
         } else if (right <= this.n && this.greater(k, right)) {
            return false;
         } else {
            return this.isMinHeapOrdered(left) && this.isMinHeapOrdered(right);
         }
      }
   }

   public Iterator<Key> iterator() {
      return new HeapIterator();
   }

   public static void main(String[] args) {
      MinPQ<String> pq = new MinPQ<String>();

      while(!StdIn.isEmpty()) {
         String item = StdIn.readString();
         if (!item.equals("-")) {
            pq.insert(item);
         } else if (!pq.isEmpty()) {
            StdOut.print((String)pq.delMin() + " ");
         }
      }

      StdOut.println("(" + pq.size() + " left on pq)");
   }

   private class HeapIterator implements Iterator<Key> {
      private MinPQ<Key> copy;

      public HeapIterator() {
         if (MinPQ.this.comparator == null) {
            this.copy = new MinPQ<Key>(MinPQ.this.size());
         } else {
            this.copy = new MinPQ<Key>(MinPQ.this.size(), MinPQ.this.comparator);
         }

         for(int i = 1; i <= MinPQ.this.n; ++i) {
            this.copy.insert(MinPQ.this.pq[i]);
         }

      }

      public boolean hasNext() {
         return !this.copy.isEmpty();
      }

      public void remove() {
         throw new UnsupportedOperationException();
      }

      public Key next() {
         if (!this.hasNext()) {
            throw new NoSuchElementException();
         } else {
            return this.copy.delMin();
         }
      }
   }
}
