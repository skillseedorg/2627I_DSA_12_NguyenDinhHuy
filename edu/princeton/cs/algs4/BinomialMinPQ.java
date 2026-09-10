package edu.princeton.cs.algs4;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class BinomialMinPQ<Key> implements Iterable<Key> {
   private BinomialMinPQ<Key>.Node head;
   private final Comparator<Key> comp;

   public BinomialMinPQ() {
      this.comp = new MyComparator();
   }

   public BinomialMinPQ(Comparator<Key> C) {
      this.comp = C;
   }

   public BinomialMinPQ(Key[] a) {
      this.comp = new MyComparator();

      for(Key k : a) {
         this.insert(k);
      }

   }

   public BinomialMinPQ(Comparator<Key> C, Key[] a) {
      this.comp = C;

      for(Key k : a) {
         this.insert(k);
      }

   }

   public boolean isEmpty() {
      return this.head == null;
   }

   public int size() {
      int result = 0;

      for(BinomialMinPQ<Key>.Node node = this.head; node != null; node = node.sibling) {
         if (node.order > 30) {
            throw new ArithmeticException("The number of elements cannot be evaluated, but the priority queue is still valid.");
         }

         int tmp = 1 << node.order;
         result |= tmp;
      }

      return result;
   }

   public void insert(Key key) {
      BinomialMinPQ<Key>.Node x = new Node();
      x.key = key;
      x.order = 0;
      BinomialMinPQ<Key> H = new BinomialMinPQ<Key>();
      H.head = x;
      this.head = this.union(H).head;
   }

   public Key minKey() {
      if (this.isEmpty()) {
         throw new NoSuchElementException("Priority queue is empty");
      } else {
         BinomialMinPQ<Key>.Node min = this.head;

         for(BinomialMinPQ<Key>.Node current = this.head; current.sibling != null; current = current.sibling) {
            min = this.greater(min.key, current.sibling.key) ? current : min;
         }

         return min.key;
      }
   }

   public Key delMin() {
      if (this.isEmpty()) {
         throw new NoSuchElementException("Priority queue is empty");
      } else {
         BinomialMinPQ<Key>.Node min = this.eraseMin();
         BinomialMinPQ<Key>.Node x = min.child == null ? min : min.child;
         if (min.child != null) {
            min.child = null;
            BinomialMinPQ<Key>.Node prevx = null;

            for(BinomialMinPQ<Key>.Node nextx = x.sibling; nextx != null; nextx = nextx.sibling) {
               x.sibling = prevx;
               prevx = x;
               x = nextx;
            }

            x.sibling = prevx;
            BinomialMinPQ<Key> H = new BinomialMinPQ<Key>();
            H.head = x;
            this.head = this.union(H).head;
         }

         return min.key;
      }
   }

   public BinomialMinPQ<Key> union(BinomialMinPQ<Key> heap) {
      if (heap == null) {
         throw new IllegalArgumentException("Cannot merge a Binomial Heap with null");
      } else {
         this.head = this.merge(new Node(), this.head, heap.head).sibling;
         BinomialMinPQ<Key>.Node x = this.head;
         BinomialMinPQ<Key>.Node prevx = null;

         for(BinomialMinPQ<Key>.Node nextx = x.sibling; nextx != null; nextx = x.sibling) {
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

   private void link(BinomialMinPQ<Key>.Node root1, BinomialMinPQ<Key>.Node root2) {
      root1.sibling = root2.child;
      root2.child = root1;
      ++root2.order;
   }

   private BinomialMinPQ<Key>.Node eraseMin() {
      BinomialMinPQ<Key>.Node min = this.head;
      BinomialMinPQ<Key>.Node previous = null;

      for(BinomialMinPQ<Key>.Node current = this.head; current.sibling != null; current = current.sibling) {
         if (this.greater(min.key, current.sibling.key)) {
            previous = current;
            min = current.sibling;
         }
      }

      previous.sibling = min.sibling;
      if (min == this.head) {
         this.head = min.sibling;
      }

      return min;
   }

   private BinomialMinPQ<Key>.Node merge(BinomialMinPQ<Key>.Node h, BinomialMinPQ<Key>.Node x, BinomialMinPQ<Key>.Node y) {
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

   public Iterator<Key> iterator() {
      return new MyIterator();
   }

   private class Node {
      Key key;
      int order;
      BinomialMinPQ<Key>.Node child;
      BinomialMinPQ<Key>.Node sibling;
   }

   private class MyIterator implements Iterator<Key> {
      BinomialMinPQ<Key> data;

      public MyIterator() {
         this.data = new BinomialMinPQ<Key>(BinomialMinPQ.this.comp);
         this.data.head = this.clone(BinomialMinPQ.this.head, (Node)null);
      }

      private BinomialMinPQ<Key>.Node clone(BinomialMinPQ<Key>.Node x, BinomialMinPQ<Key>.Node parent) {
         if (x == null) {
            return null;
         } else {
            BinomialMinPQ<Key>.Node node = BinomialMinPQ.this.new Node();
            node.key = x.key;
            node.sibling = this.clone(x.sibling, parent);
            node.child = this.clone(x.child, node);
            return node;
         }
      }

      public boolean hasNext() {
         return !this.data.isEmpty();
      }

      public Key next() {
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
