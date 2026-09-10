package edu.princeton.cs.algs4;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class IndexFibonacciMinPQ<Key> implements Iterable<Integer> {
   private IndexFibonacciMinPQ<Key>.Node<Key>[] nodes;
   private IndexFibonacciMinPQ<Key>.Node<Key> head;
   private IndexFibonacciMinPQ<Key>.Node<Key> min;
   private int size;
   private int n;
   private final Comparator<Key> comp;
   private HashMap<Integer, IndexFibonacciMinPQ<Key>.Node<Key>> table = new HashMap();

   public IndexFibonacciMinPQ(int N) {
      if (N < 0) {
         throw new IllegalArgumentException("Cannot create a priority queue of negative size");
      } else {
         this.n = N;
         this.nodes = new Node[this.n];
         this.comp = new MyComparator();
      }
   }

   public IndexFibonacciMinPQ(Comparator<Key> C, int N) {
      if (N < 0) {
         throw new IllegalArgumentException("Cannot create a priority queue of negative size");
      } else {
         this.n = N;
         this.nodes = new Node[this.n];
         this.comp = C;
      }
   }

   public boolean isEmpty() {
      return this.size == 0;
   }

   public boolean contains(int i) {
      if (i >= 0 && i < this.n) {
         return this.nodes[i] != null;
      } else {
         throw new IllegalArgumentException();
      }
   }

   public int size() {
      return this.size;
   }

   public void insert(int i, Key key) {
      if (i >= 0 && i < this.n) {
         if (this.contains(i)) {
            throw new IllegalArgumentException("Specified index is already in the queue");
         } else {
            IndexFibonacciMinPQ<Key>.Node<Key> x = new Node<Key>();
            x.key = key;
            x.index = i;
            this.nodes[i] = x;
            ++this.size;
            this.head = this.insert(x, this.head);
            if (this.min == null) {
               this.min = this.head;
            } else {
               this.min = this.greater(this.min.key, key) ? this.head : this.min;
            }

         }
      } else {
         throw new IllegalArgumentException();
      }
   }

   public int minIndex() {
      if (this.isEmpty()) {
         throw new NoSuchElementException("Priority queue is empty");
      } else {
         return this.min.index;
      }
   }

   public Key minKey() {
      if (this.isEmpty()) {
         throw new NoSuchElementException("Priority queue is empty");
      } else {
         return this.min.key;
      }
   }

   public int delMin() {
      if (this.isEmpty()) {
         throw new NoSuchElementException("Priority queue is empty");
      } else {
         this.head = this.cut(this.min, this.head);
         IndexFibonacciMinPQ<Key>.Node<Key> x = this.min.child;
         int index = this.min.index;
         this.min.key = null;
         if (x != null) {
            do {
               x.parent = null;
               x = x.next;
            } while(x != this.min.child);

            this.head = this.meld(this.head, x);
            this.min.child = null;
         }

         --this.size;
         if (!this.isEmpty()) {
            this.consolidate();
         } else {
            this.min = null;
         }

         this.nodes[index] = null;
         return index;
      }
   }

   public Key keyOf(int i) {
      if (i >= 0 && i < this.n) {
         if (!this.contains(i)) {
            throw new NoSuchElementException("Specified index is not in the queue");
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
            throw new NoSuchElementException("Specified index is not in the queue");
         } else {
            if (this.greater(key, this.nodes[i].key)) {
               this.increaseKey(i, key);
            } else {
               this.decreaseKey(i, key);
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
            IndexFibonacciMinPQ<Key>.Node<Key> x = this.nodes[i];
            x.key = key;
            if (this.greater(this.min.key, key)) {
               this.min = x;
            }

            if (x.parent != null && this.greater(x.parent.key, key)) {
               this.cut(i);
            }

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
            IndexFibonacciMinPQ<Key>.Node<Key> x = this.nodes[i];
            x.key = null;
            if (x.parent != null) {
               this.cut(i);
            }

            this.head = this.cut(x, this.head);
            if (x.child != null) {
               IndexFibonacciMinPQ<Key>.Node<Key> child = x.child;
               x.child = null;
               x = child;

               do {
                  child.parent = null;
                  child = child.next;
               } while(child != x);

               this.head = this.meld(this.head, child);
            }

            if (!this.isEmpty()) {
               this.consolidate();
            } else {
               this.min = null;
            }

            this.nodes[i] = null;
            --this.size;
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
         return this.comp.compare(n, m) > 0;
      }
   }

   private void link(IndexFibonacciMinPQ<Key>.Node<Key> root1, IndexFibonacciMinPQ<Key>.Node<Key> root2) {
      root1.parent = root2;
      root2.child = this.insert(root1, root2.child);
      ++root2.order;
   }

   private void cut(int i) {
      IndexFibonacciMinPQ<Key>.Node<Key> x = this.nodes[i];
      IndexFibonacciMinPQ<Key>.Node<Key> parent = x.parent;
      parent.child = this.cut(x, parent.child);
      x.parent = null;
      --parent.order;
      this.head = this.insert(x, this.head);
      parent.mark = !parent.mark;
      if (!parent.mark && parent.parent != null) {
         this.cut(parent.index);
      }

   }

   private void consolidate() {
      this.table.clear();
      IndexFibonacciMinPQ<Key>.Node<Key> x = this.head;
      int maxOrder = 0;
      this.min = this.head;
      IndexFibonacciMinPQ<Key>.Node<Key> y = null;
      IndexFibonacciMinPQ<Key>.Node<Key> z = null;

      do {
         y = x;
         x = x.next;

         for(IndexFibonacciMinPQ<Key>.Node<Key> var8 = (Node)this.table.get(y.order); var8 != null; var8 = (Node)this.table.get(y.order)) {
            this.table.remove(y.order);
            if (this.greater(y.key, var8.key)) {
               this.link(y, var8);
               y = var8;
            } else {
               this.link(var8, y);
            }
         }

         this.table.put(y.order, y);
         if (y.order > maxOrder) {
            maxOrder = y.order;
         }
      } while(x != this.head);

      this.head = null;

      for(IndexFibonacciMinPQ<Key>.Node<Key> n : this.table.values()) {
         this.min = this.greater(this.min.key, n.key) ? n : this.min;
         this.head = this.insert(n, this.head);
      }

   }

   private IndexFibonacciMinPQ<Key>.Node<Key> insert(IndexFibonacciMinPQ<Key>.Node<Key> x, IndexFibonacciMinPQ<Key>.Node<Key> head) {
      if (head == null) {
         x.prev = x;
         x.next = x;
      } else {
         head.prev.next = x;
         x.next = head;
         x.prev = head.prev;
         head.prev = x;
      }

      return x;
   }

   private IndexFibonacciMinPQ<Key>.Node<Key> cut(IndexFibonacciMinPQ<Key>.Node<Key> x, IndexFibonacciMinPQ<Key>.Node<Key> head) {
      if (x.next == x) {
         x.next = null;
         x.prev = null;
         return null;
      } else {
         x.next.prev = x.prev;
         x.prev.next = x.next;
         IndexFibonacciMinPQ<Key>.Node<Key> res = x.next;
         x.next = null;
         x.prev = null;
         return head == x ? res : head;
      }
   }

   private IndexFibonacciMinPQ<Key>.Node<Key> meld(IndexFibonacciMinPQ<Key>.Node<Key> x, IndexFibonacciMinPQ<Key>.Node<Key> y) {
      if (x == null) {
         return y;
      } else if (y == null) {
         return x;
      } else {
         x.prev.next = y.next;
         y.next.prev = x.prev;
         x.prev = y;
         y.next = x;
         return x;
      }
   }

   public Iterator<Integer> iterator() {
      return new MyIterator();
   }

   private class Node<Key> {
      Key key;
      int order;
      int index;
      IndexFibonacciMinPQ<Key>.Node<Key> prev;
      IndexFibonacciMinPQ<Key>.Node<Key> next;
      IndexFibonacciMinPQ<Key>.Node<Key> parent;
      IndexFibonacciMinPQ<Key>.Node<Key> child;
      boolean mark;
   }

   private class MyIterator implements Iterator<Integer> {
      private IndexFibonacciMinPQ<Key> copy;

      public MyIterator() {
         this.copy = new IndexFibonacciMinPQ<Key>(IndexFibonacciMinPQ.this.comp, IndexFibonacciMinPQ.this.n);

         for(IndexFibonacciMinPQ<Key>.Node<Key> x : IndexFibonacciMinPQ.this.nodes) {
            if (x != null) {
               this.copy.insert(x.index, x.key);
            }
         }

      }

      public void remove() {
         throw new UnsupportedOperationException();
      }

      public boolean hasNext() {
         return !this.copy.isEmpty();
      }

      public Integer next() {
         if (!this.hasNext()) {
            throw new NoSuchElementException();
         } else {
            return this.copy.delMin();
         }
      }
   }

   private class MyComparator implements Comparator<Key> {
      public int compare(Key key1, Key key2) {
         return ((Comparable)key1).compareTo(key2);
      }
   }
}
