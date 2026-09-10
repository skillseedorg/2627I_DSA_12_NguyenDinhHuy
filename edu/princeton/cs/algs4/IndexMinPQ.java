package edu.princeton.cs.algs4;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class IndexMinPQ<Key extends Comparable<Key>> implements Iterable<Integer> {
   private int maxN;
   private int n;
   private int[] pq;
   private int[] qp;
   private Key[] keys;

   public IndexMinPQ(int maxN) {
      if (maxN < 0) {
         throw new IllegalArgumentException();
      } else {
         this.maxN = maxN;
         this.n = 0;
         this.keys = new Comparable[maxN + 1];
         this.pq = new int[maxN + 1];
         this.qp = new int[maxN + 1];

         for(int i = 0; i <= maxN; ++i) {
            this.qp[i] = -1;
         }

      }
   }

   public boolean isEmpty() {
      return this.n == 0;
   }

   public boolean contains(int i) {
      this.validateIndex(i);
      return this.qp[i] != -1;
   }

   public int size() {
      return this.n;
   }

   public void insert(int i, Key key) {
      this.validateIndex(i);
      if (this.contains(i)) {
         throw new IllegalArgumentException("index is already in the priority queue");
      } else {
         ++this.n;
         this.qp[i] = this.n;
         this.pq[this.n] = i;
         this.keys[i] = key;
         this.swim(this.n);
      }
   }

   public int minIndex() {
      if (this.n == 0) {
         throw new NoSuchElementException("Priority queue underflow");
      } else {
         return this.pq[1];
      }
   }

   public Key minKey() {
      if (this.n == 0) {
         throw new NoSuchElementException("Priority queue underflow");
      } else {
         return (Key)this.keys[this.pq[1]];
      }
   }

   public int delMin() {
      if (this.n == 0) {
         throw new NoSuchElementException("Priority queue underflow");
      } else {
         int min = this.pq[1];
         this.exch(1, this.n--);
         this.sink(1);

         assert min == this.pq[this.n + 1];

         this.qp[min] = -1;
         this.keys[min] = null;
         this.pq[this.n + 1] = -1;
         return min;
      }
   }

   public Key keyOf(int i) {
      this.validateIndex(i);
      if (!this.contains(i)) {
         throw new NoSuchElementException("index is not in the priority queue");
      } else {
         return (Key)this.keys[i];
      }
   }

   public void changeKey(int i, Key key) {
      this.validateIndex(i);
      if (!this.contains(i)) {
         throw new NoSuchElementException("index is not in the priority queue");
      } else {
         this.keys[i] = key;
         this.swim(this.qp[i]);
         this.sink(this.qp[i]);
      }
   }

   /** @deprecated */
   @Deprecated
   public void change(int i, Key key) {
      this.changeKey(i, key);
   }

   public void decreaseKey(int i, Key key) {
      this.validateIndex(i);
      if (!this.contains(i)) {
         throw new NoSuchElementException("index is not in the priority queue");
      } else if (this.keys[i].compareTo(key) == 0) {
         throw new IllegalArgumentException("Calling decreaseKey() with a key equal to the key in the priority queue");
      } else if (this.keys[i].compareTo(key) < 0) {
         throw new IllegalArgumentException("Calling decreaseKey() with a key strictly greater than the key in the priority queue");
      } else {
         this.keys[i] = key;
         this.swim(this.qp[i]);
      }
   }

   public void increaseKey(int i, Key key) {
      this.validateIndex(i);
      if (!this.contains(i)) {
         throw new NoSuchElementException("index is not in the priority queue");
      } else if (this.keys[i].compareTo(key) == 0) {
         throw new IllegalArgumentException("Calling increaseKey() with a key equal to the key in the priority queue");
      } else if (this.keys[i].compareTo(key) > 0) {
         throw new IllegalArgumentException("Calling increaseKey() with a key strictly less than the key in the priority queue");
      } else {
         this.keys[i] = key;
         this.sink(this.qp[i]);
      }
   }

   public void delete(int i) {
      this.validateIndex(i);
      if (!this.contains(i)) {
         throw new NoSuchElementException("index is not in the priority queue");
      } else {
         int index = this.qp[i];
         this.exch(index, this.n--);
         this.swim(index);
         this.sink(index);
         this.keys[i] = null;
         this.qp[i] = -1;
      }
   }

   private void validateIndex(int i) {
      if (i < 0) {
         throw new IllegalArgumentException("index is negative: " + i);
      } else if (i >= this.maxN) {
         throw new IllegalArgumentException("index >= capacity: " + i);
      }
   }

   private boolean greater(int i, int j) {
      return this.keys[this.pq[i]].compareTo(this.keys[this.pq[j]]) > 0;
   }

   private void exch(int i, int j) {
      int swap = this.pq[i];
      this.pq[i] = this.pq[j];
      this.pq[j] = swap;
      this.qp[this.pq[i]] = i;
      this.qp[this.pq[j]] = j;
   }

   private void swim(int k) {
      while(k > 1 && this.greater(k / 2, k)) {
         this.exch(k, k / 2);
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

   public Iterator<Integer> iterator() {
      return new HeapIterator();
   }

   public static void main(String[] args) {
      String[] strings = new String[]{"it", "was", "the", "best", "of", "times", "it", "was", "the", "worst"};
      IndexMinPQ<String> pq = new IndexMinPQ<String>(strings.length);

      for(int i = 0; i < strings.length; ++i) {
         pq.insert(i, strings[i]);
      }

      while(!pq.isEmpty()) {
         int i = pq.delMin();
         StdOut.println(i + " " + strings[i]);
      }

      StdOut.println();

      for(int i = 0; i < strings.length; ++i) {
         pq.insert(i, strings[i]);
      }

      for(int i : pq) {
         StdOut.println(i + " " + strings[i]);
      }

      while(!pq.isEmpty()) {
         pq.delMin();
      }

   }

   private class HeapIterator implements Iterator<Integer> {
      private IndexMinPQ<Key> copy;

      public HeapIterator() {
         this.copy = new IndexMinPQ<Key>(IndexMinPQ.this.pq.length - 1);

         for(int i = 1; i <= IndexMinPQ.this.n; ++i) {
            this.copy.insert(IndexMinPQ.this.pq[i], IndexMinPQ.this.keys[IndexMinPQ.this.pq[i]]);
         }

      }

      public boolean hasNext() {
         return !this.copy.isEmpty();
      }

      public void remove() {
         throw new UnsupportedOperationException();
      }

      public Integer next() {
         if (!this.hasNext()) {
            throw new NoSuchElementException();
         } else {
            return this.copy.delMin();
         }
      }
   }
}
