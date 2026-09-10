package edu.princeton.cs.algs4;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class FibonacciMinPQ<Key> implements Iterable<Key> {
   private FibonacciMinPQ<Key>.Node head;
   private FibonacciMinPQ<Key>.Node min;
   private int size;
   private final Comparator<Key> comp;
   private HashMap<Integer, FibonacciMinPQ<Key>.Node> table = new HashMap();

   public FibonacciMinPQ(Comparator<Key> C) {
      this.comp = C;
   }

   public FibonacciMinPQ() {
      this.comp = new MyComparator();
   }

   public FibonacciMinPQ(Key[] a) {
      this.comp = new MyComparator();

      for(Key k : a) {
         this.insert(k);
      }

   }

   public FibonacciMinPQ(Comparator<Key> C, Key[] a) {
      this.comp = C;

      for(Key k : a) {
         this.insert(k);
      }

   }

   public boolean isEmpty() {
      return this.size == 0;
   }

   public int size() {
      return this.size;
   }

   public void insert(Key key) {
      FibonacciMinPQ<Key>.Node x = new Node();
      x.key = key;
      ++this.size;
      this.head = this.insert(x, this.head);
      if (this.min == null) {
         this.min = this.head;
      } else {
         this.min = this.greater(this.min.key, key) ? this.head : this.min;
      }

   }

   public Key minKey() {
      if (this.isEmpty()) {
         throw new NoSuchElementException("Priority queue is empty");
      } else {
         return this.min.key;
      }
   }

   public Key delMin() {
      if (this.isEmpty()) {
         throw new NoSuchElementException("Priority queue is empty");
      } else {
         this.head = this.cut(this.min, this.head);
         FibonacciMinPQ<Key>.Node x = this.min.child;
         Key key = this.min.key;
         this.min.key = null;
         if (x != null) {
            this.head = this.meld(this.head, x);
            this.min.child = null;
         }

         --this.size;
         if (!this.isEmpty()) {
            this.consolidate();
         } else {
            this.min = null;
         }

         return key;
      }
   }

   public FibonacciMinPQ<Key> union(FibonacciMinPQ<Key> that) {
      this.head = this.meld(this.head, that.head);
      this.min = this.greater(this.min.key, that.min.key) ? that.min : this.min;
      this.size += that.size;
      return this;
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

   private void link(FibonacciMinPQ<Key>.Node root1, FibonacciMinPQ<Key>.Node root2) {
      root2.child = this.insert(root1, root2.child);
      ++root2.order;
   }

   private void consolidate() {
      this.table.clear();
      FibonacciMinPQ<Key>.Node x = this.head;
      int maxOrder = 0;
      this.min = this.head;
      FibonacciMinPQ<Key>.Node y = null;
      FibonacciMinPQ<Key>.Node z = null;

      do {
         y = x;
         x = x.next;

         for(FibonacciMinPQ<Key>.Node var8 = (Node)this.table.get(y.order); var8 != null; var8 = (Node)this.table.get(y.order)) {
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

      for(FibonacciMinPQ<Key>.Node n : this.table.values()) {
         if (n != null) {
            this.min = this.greater(this.min.key, n.key) ? n : this.min;
            this.head = this.insert(n, this.head);
         }
      }

   }

   private FibonacciMinPQ<Key>.Node insert(FibonacciMinPQ<Key>.Node x, FibonacciMinPQ<Key>.Node head) {
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

   private FibonacciMinPQ<Key>.Node cut(FibonacciMinPQ<Key>.Node x, FibonacciMinPQ<Key>.Node head) {
      if (x.next == x) {
         x.next = null;
         x.prev = null;
         return null;
      } else {
         x.next.prev = x.prev;
         x.prev.next = x.next;
         FibonacciMinPQ<Key>.Node res = x.next;
         x.next = null;
         x.prev = null;
         return head == x ? res : head;
      }
   }

   private FibonacciMinPQ<Key>.Node meld(FibonacciMinPQ<Key>.Node x, FibonacciMinPQ<Key>.Node y) {
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

   public Iterator<Key> iterator() {
      return new MyIterator();
   }

   private class Node {
      Key key;
      int order;
      FibonacciMinPQ<Key>.Node prev;
      FibonacciMinPQ<Key>.Node next;
      FibonacciMinPQ<Key>.Node child;
   }

   private class MyIterator implements Iterator<Key> {
      private FibonacciMinPQ<Key> copy;

      public MyIterator() {
         this.copy = new FibonacciMinPQ<Key>(FibonacciMinPQ.this.comp);
         this.insertAll(FibonacciMinPQ.this.head);
      }

      private void insertAll(FibonacciMinPQ<Key>.Node head) {
         if (head != null) {
            FibonacciMinPQ<Key>.Node x = head;

            do {
               this.copy.insert(x.key);
               this.insertAll(x.child);
               x = x.next;
            } while(x != head);

         }
      }

      public void remove() {
         throw new UnsupportedOperationException();
      }

      public boolean hasNext() {
         return !this.copy.isEmpty();
      }

      public Key next() {
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
