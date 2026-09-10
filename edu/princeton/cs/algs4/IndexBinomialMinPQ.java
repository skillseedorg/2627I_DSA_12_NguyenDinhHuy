package edu.princeton.cs.algs4;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class IndexBinomialMinPQ<Key> implements Iterable<Integer> {
   private IndexBinomialMinPQ<Key>.Node<Key> head;
   private IndexBinomialMinPQ<Key>.Node<Key>[] nodes;
   private int n;
   private final Comparator<Key> comparator;

   public IndexBinomialMinPQ(int N) {
      if (N < 0) {
         throw new IllegalArgumentException("Cannot create a priority queue of negative size");
      } else {
         this.comparator = new MyComparator();
         this.nodes = new Node[N];
         this.n = N;
      }
   }

   public IndexBinomialMinPQ(int N, Comparator<Key> comparator) {
      if (N < 0) {
         throw new IllegalArgumentException("Cannot create a priority queue of negative size");
      } else {
         this.comparator = comparator;
         this.nodes = new Node[N];
         this.n = N;
      }
   }

   public boolean isEmpty() {
      return this.head == null;
   }

   public boolean contains(int i) {
      if (i >= 0 && i < this.n) {
         return this.nodes[i] != null;
      } else {
         throw new IllegalArgumentException();
      }
   }

   public int size() {
      int result = 0;

      for(IndexBinomialMinPQ<Key>.Node<Key> node = this.head; node != null; node = node.sibling) {
         if (node.order > 30) {
            throw new ArithmeticException("The number of elements cannot be evaluated, but the priority queue is still valid.");
         }

         int tmp = 1 << node.order;
         result |= tmp;
      }

      return result;
   }

   public void insert(int i, Key key) {
      if (i >= 0 && i < this.n) {
         if (this.contains(i)) {
            throw new IllegalArgumentException("Specified index is already in the queue");
         } else {
            IndexBinomialMinPQ<Key>.Node<Key> x = new Node<Key>();
            x.key = key;
            x.index = i;
            x.order = 0;
            this.nodes[i] = x;
            IndexBinomialMinPQ<Key> H = new IndexBinomialMinPQ<Key>();
            H.head = x;
            this.head = this.union(H).head;
         }
      } else {
         throw new IllegalArgumentException();
      }
   }

   public int minIndex() {
      if (this.isEmpty()) {
         throw new NoSuchElementException("Priority queue is empty");
      } else {
         IndexBinomialMinPQ<Key>.Node<Key> min = this.head;

         for(IndexBinomialMinPQ<Key>.Node<Key> current = this.head; current.sibling != null; current = current.sibling) {
            min = this.greater(min.key, current.sibling.key) ? current.sibling : min;
         }

         return min.index;
      }
   }

   public Key minKey() {
      if (this.isEmpty()) {
         throw new NoSuchElementException("Priority queue is empty");
      } else {
         IndexBinomialMinPQ<Key>.Node<Key> min = this.head;

         for(IndexBinomialMinPQ<Key>.Node<Key> current = this.head; current.sibling != null; current = current.sibling) {
            min = this.greater(min.key, current.sibling.key) ? current.sibling : min;
         }

         return min.key;
      }
   }

   public int delMin() {
      if (this.isEmpty()) {
         throw new NoSuchElementException("Priority queue is empty");
      } else {
         IndexBinomialMinPQ<Key>.Node<Key> min = this.eraseMin();
         IndexBinomialMinPQ<Key>.Node<Key> x = min.child == null ? min : min.child;
         if (min.child != null) {
            min.child = null;
            IndexBinomialMinPQ<Key>.Node<Key> prevx = null;

            for(IndexBinomialMinPQ<Key>.Node<Key> nextx = x.sibling; nextx != null; nextx = nextx.sibling) {
               x.parent = null;
               x.sibling = prevx;
               prevx = x;
               x = nextx;
            }

            x.parent = null;
            x.sibling = prevx;
            IndexBinomialMinPQ<Key> H = new IndexBinomialMinPQ<Key>();
            H.head = x;
            this.head = this.union(H).head;
         }

         return min.index;
      }
   }

   public Key keyOf(int i) {
      if (i >= 0 && i < this.n) {
         if (!this.contains(i)) {
            throw new IllegalArgumentException("Specified index is not in the queue");
         } else {
            return this.nodes[i].key;
         }
      } else {
         throw new IllegalArgumentException();
      }
   }

   public void changeKey(int i, Key key) {
      if (i >= 0 && i < this.n) {
         if (!this.contains(i)) {
            throw new IllegalArgumentException("Specified index is not in the queue");
         } else {
            if (this.greater(this.nodes[i].key, key)) {
               this.decreaseKey(i, key);
            } else {
               this.increaseKey(i, key);
            }

         }
      } else {
         throw new IllegalArgumentException();
      }
   }

   public void decreaseKey(int i, Key key) {
      if (i >= 0 && i < this.n) {
         if (!this.contains(i)) {
            throw new NoSuchElementException("Specified index is not in the queue");
         } else if (this.greater(key, this.nodes[i].key)) {
            throw new IllegalArgumentException("Calling with this argument would not decrease the key");
         } else {
            IndexBinomialMinPQ<Key>.Node<Key> x = this.nodes[i];
            x.key = key;
            this.swim(i);
         }
      } else {
         throw new IllegalArgumentException();
      }
   }

   public void increaseKey(int i, Key key) {
      if (i >= 0 && i < this.n) {
         if (!this.contains(i)) {
            throw new NoSuchElementException("Specified index is not in the queue");
         } else if (this.greater(this.nodes[i].key, key)) {
            throw new IllegalArgumentException("Calling with this argument would not increase the key");
         } else {
            this.delete(i);
            this.insert(i, key);
         }
      } else {
         throw new IllegalArgumentException();
      }
   }

   public void delete(int i) {
      if (i >= 0 && i < this.n) {
         if (!this.contains(i)) {
            throw new NoSuchElementException("Specified index is not in the queue");
         } else {
            this.toTheRoot(i);
            IndexBinomialMinPQ<Key>.Node<Key> x = this.erase(i);
            if (x.child != null) {
               IndexBinomialMinPQ<Key>.Node<Key> y = x;
               x = x.child;
               y.child = null;
               IndexBinomialMinPQ<Key>.Node<Key> prevx = null;

               for(IndexBinomialMinPQ<Key>.Node<Key> nextx = x.sibling; nextx != null; nextx = nextx.sibling) {
                  x.parent = null;
                  x.sibling = prevx;
                  prevx = x;
                  x = nextx;
               }

               x.parent = null;
               x.sibling = prevx;
               IndexBinomialMinPQ<Key> H = new IndexBinomialMinPQ<Key>();
               H.head = x;
               this.head = this.union(H).head;
            }

         }
      } else {
         throw new IllegalArgumentException();
      }
   }

   private boolean greater(Key n, Key m) {
      if (n == null) {
         return false;
      } else if (m == null) {
         return true;
      } else {
         return this.comparator.compare(n, m) > 0;
      }
   }

   private void exchange(IndexBinomialMinPQ<Key>.Node<Key> x, IndexBinomialMinPQ<Key>.Node<Key> y) {
      Key tempKey = x.key;
      x.key = y.key;
      y.key = tempKey;
      int tempInt = x.index;
      x.index = y.index;
      y.index = tempInt;
      this.nodes[x.index] = x;
      this.nodes[y.index] = y;
   }

   private void link(IndexBinomialMinPQ<Key>.Node<Key> root1, IndexBinomialMinPQ<Key>.Node<Key> root2) {
      root1.sibling = root2.child;
      root1.parent = root2;
      root2.child = root1;
      ++root2.order;
   }

   private void swim(int i) {
      IndexBinomialMinPQ<Key>.Node<Key> x = this.nodes[i];
      IndexBinomialMinPQ<Key>.Node<Key> parent = x.parent;
      if (parent != null && this.greater(parent.key, x.key)) {
         this.exchange(x, parent);
         this.swim(i);
      }

   }

   private void toTheRoot(int i) {
      IndexBinomialMinPQ<Key>.Node<Key> x = this.nodes[i];
      IndexBinomialMinPQ<Key>.Node<Key> parent = x.parent;
      if (parent != null) {
         this.exchange(x, parent);
         this.toTheRoot(i);
      }

   }

   private IndexBinomialMinPQ<Key>.Node<Key> erase(int i) {
      IndexBinomialMinPQ<Key>.Node<Key> reference = this.nodes[i];
      IndexBinomialMinPQ<Key>.Node<Key> x = this.head;

      IndexBinomialMinPQ<Key>.Node<Key> previous;
      for(previous = null; x != reference; x = x.sibling) {
         previous = x;
      }

      previous.sibling = x.sibling;
      if (x == this.head) {
         this.head = this.head.sibling;
      }

      this.nodes[i] = null;
      return x;
   }

   private IndexBinomialMinPQ<Key>.Node<Key> eraseMin() {
      IndexBinomialMinPQ<Key>.Node<Key> min = this.head;
      IndexBinomialMinPQ<Key>.Node<Key> previous = null;

      for(IndexBinomialMinPQ<Key>.Node<Key> current = this.head; current.sibling != null; current = current.sibling) {
         if (this.greater(min.key, current.sibling.key)) {
            previous = current;
            min = current.sibling;
         }
      }

      previous.sibling = min.sibling;
      if (min == this.head) {
         this.head = min.sibling;
      }

      this.nodes[min.index] = null;
      return min;
   }

   private IndexBinomialMinPQ<Key>.Node<Key> merge(IndexBinomialMinPQ<Key>.Node<Key> h, IndexBinomialMinPQ<Key>.Node<Key> x, IndexBinomialMinPQ<Key>.Node<Key> y) {
      if (x == null && y == null) {
         return h;
      } else {
         if (x == null) {
            h.sibling = this.merge(y, (Node)null, y.sibling);
         } else if (y == null) {
            h.sibling = this.merge(x, x.sibling, (Node)null);
         } else if (x.order < y.order) {
            h.sibling = this.merge(x, x.sibling, y);
         } else {
            h.sibling = this.merge(y, x, y.sibling);
         }

         return h;
      }
   }

   private IndexBinomialMinPQ<Key> union(IndexBinomialMinPQ<Key> heap) {
      this.head = this.merge(new Node(), this.head, heap.head).sibling;
      IndexBinomialMinPQ<Key>.Node<Key> x = this.head;
      IndexBinomialMinPQ<Key>.Node<Key> prevx = null;

      for(IndexBinomialMinPQ<Key>.Node<Key> nextx = x.sibling; nextx != null; nextx = x.sibling) {
         if (x.order >= nextx.order && (nextx.sibling == null || nextx.sibling.order != x.order)) {
            if (this.greater(nextx.key, x.key)) {
               x.sibling = nextx.sibling;
               this.link(nextx, x);
            } else {
               if (prevx == null) {
                  this.head = nextx;
               } else {
                  prevx.sibling = nextx;
               }

               this.link(x, nextx);
               x = nextx;
            }
         } else {
            prevx = x;
            x = nextx;
         }
      }

      return this;
   }

   private IndexBinomialMinPQ() {
      this.comparator = null;
   }

   public Iterator<Integer> iterator() {
      return new MyIterator();
   }

   private class Node<Key> {
      Key key;
      int order;
      int index;
      IndexBinomialMinPQ<Key>.Node<Key> parent;
      IndexBinomialMinPQ<Key>.Node<Key> child;
      IndexBinomialMinPQ<Key>.Node<Key> sibling;
   }

   private class MyIterator implements Iterator<Integer> {
      IndexBinomialMinPQ<Key> data;

      public MyIterator() {
         this.data = new IndexBinomialMinPQ<Key>(IndexBinomialMinPQ.this.n, IndexBinomialMinPQ.this.comparator);
         this.data.head = this.clone(IndexBinomialMinPQ.this.head, (Node)null);
      }

      private IndexBinomialMinPQ<Key>.Node<Key> clone(IndexBinomialMinPQ<Key>.Node<Key> x, IndexBinomialMinPQ<Key>.Node<Key> parent) {
         if (x == null) {
            return null;
         } else {
            IndexBinomialMinPQ<Key>.Node<Key> node = IndexBinomialMinPQ.this.new Node<Key>();
            node.index = x.index;
            node.key = x.key;
            this.data.nodes[node.index] = node;
            node.parent = parent;
            node.sibling = this.clone(x.sibling, parent);
            node.child = this.clone(x.child, node);
            return node;
         }
      }

      public boolean hasNext() {
         return !this.data.isEmpty();
      }

      public Integer next() {
         if (!this.hasNext()) {
            throw new NoSuchElementException();
         } else {
            return this.data.delMin();
         }
      }

      public void remove() {
         throw new UnsupportedOperationException();
      }
   }

   private class MyComparator implements Comparator<Key> {
      public int compare(Key key1, Key key2) {
         return ((Comparable)key1).compareTo(key2);
      }
   }
}
