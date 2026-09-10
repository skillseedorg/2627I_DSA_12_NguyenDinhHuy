package edu.princeton.cs.algs4;

import java.util.NoSuchElementException;

public class BST<Key extends Comparable<Key>, Value> {
   private BST<Key, Value>.Node root;

   public boolean isEmpty() {
      return this.size() == 0;
   }

   public int size() {
      return this.size(this.root);
   }

   private int size(BST<Key, Value>.Node x) {
      return x == null ? 0 : x.size;
   }

   public boolean contains(Key key) {
      if (key == null) {
         throw new IllegalArgumentException("argument to contains() is null");
      } else {
         return this.get(key) != null;
      }
   }

   public Value get(Key key) {
      return (Value)this.get(this.root, key);
   }

   private Value get(BST<Key, Value>.Node x, Key key) {
      if (key == null) {
         throw new IllegalArgumentException("calls get() with a null key");
      } else if (x == null) {
         return null;
      } else {
         int cmp = key.compareTo(x.key);
         if (cmp < 0) {
            return (Value)this.get(x.left, key);
         } else {
            return (Value)(cmp > 0 ? this.get(x.right, key) : x.val);
         }
      }
   }

   public void put(Key key, Value val) {
      if (key == null) {
         throw new IllegalArgumentException("calls put() with a null key");
      } else if (val == null) {
         this.delete(key);
      } else {
         this.root = this.put(this.root, key, val);

         assert this.check();

      }
   }

   private BST<Key, Value>.Node put(BST<Key, Value>.Node x, Key key, Value val) {
      if (x == null) {
         return new Node(key, val, 1);
      } else {
         int cmp = key.compareTo(x.key);
         if (cmp < 0) {
            x.left = this.put(x.left, key, val);
         } else if (cmp > 0) {
            x.right = this.put(x.right, key, val);
         } else {
            x.val = val;
         }

         x.size = 1 + this.size(x.left) + this.size(x.right);
         return x;
      }
   }

   public void deleteMin() {
      if (this.isEmpty()) {
         throw new NoSuchElementException("Symbol table underflow");
      } else {
         this.root = this.deleteMin(this.root);

         assert this.check();

      }
   }

   private BST<Key, Value>.Node deleteMin(BST<Key, Value>.Node x) {
      if (x.left == null) {
         return x.right;
      } else {
         x.left = this.deleteMin(x.left);
         x.size = this.size(x.left) + this.size(x.right) + 1;
         return x;
      }
   }

   public void deleteMax() {
      if (this.isEmpty()) {
         throw new NoSuchElementException("Symbol table underflow");
      } else {
         this.root = this.deleteMax(this.root);

         assert this.check();

      }
   }

   private BST<Key, Value>.Node deleteMax(BST<Key, Value>.Node x) {
      if (x.right == null) {
         return x.left;
      } else {
         x.right = this.deleteMax(x.right);
         x.size = this.size(x.left) + this.size(x.right) + 1;
         return x;
      }
   }

   public void delete(Key key) {
      if (key == null) {
         throw new IllegalArgumentException("calls delete() with a null key");
      } else {
         this.root = this.delete(this.root, key);

         assert this.check();

      }
   }

   private BST<Key, Value>.Node delete(BST<Key, Value>.Node x, Key key) {
      if (x == null) {
         return null;
      } else {
         int cmp = key.compareTo(x.key);
         if (cmp < 0) {
            x.left = this.delete(x.left, key);
         } else if (cmp > 0) {
            x.right = this.delete(x.right, key);
         } else {
            if (x.right == null) {
               return x.left;
            }

            if (x.left == null) {
               return x.right;
            }

            BST<Key, Value>.Node t = x;
            x = this.min(x.right);
            x.right = this.deleteMin(t.right);
            x.left = t.left;
         }

         x.size = this.size(x.left) + this.size(x.right) + 1;
         return x;
      }
   }

   public Key min() {
      if (this.isEmpty()) {
         throw new NoSuchElementException("calls min() with empty symbol table");
      } else {
         return this.min(this.root).key;
      }
   }

   private BST<Key, Value>.Node min(BST<Key, Value>.Node x) {
      return x.left == null ? x : this.min(x.left);
   }

   public Key max() {
      if (this.isEmpty()) {
         throw new NoSuchElementException("calls max() with empty symbol table");
      } else {
         return this.max(this.root).key;
      }
   }

   private BST<Key, Value>.Node max(BST<Key, Value>.Node x) {
      return x.right == null ? x : this.max(x.right);
   }

   public Key floor(Key key) {
      if (key == null) {
         throw new IllegalArgumentException("argument to floor() is null");
      } else if (this.isEmpty()) {
         throw new NoSuchElementException("calls floor() with empty symbol table");
      } else {
         BST<Key, Value>.Node x = this.floor(this.root, key);
         if (x == null) {
            throw new NoSuchElementException("argument to floor() is too small");
         } else {
            return x.key;
         }
      }
   }

   private BST<Key, Value>.Node floor(BST<Key, Value>.Node x, Key key) {
      if (x == null) {
         return null;
      } else {
         int cmp = key.compareTo(x.key);
         if (cmp == 0) {
            return x;
         } else if (cmp < 0) {
            return this.floor(x.left, key);
         } else {
            BST<Key, Value>.Node t = this.floor(x.right, key);
            return t != null ? t : x;
         }
      }
   }

   public Key floor2(Key key) {
      Key x = this.floor2(this.root, key, (Comparable)null);
      if (x == null) {
         throw new NoSuchElementException("argument to floor() is too small");
      } else {
         return x;
      }
   }

   private Key floor2(BST<Key, Value>.Node x, Key key, Key best) {
      if (x == null) {
         return best;
      } else {
         int cmp = key.compareTo(x.key);
         if (cmp < 0) {
            return (Key)this.floor2(x.left, key, best);
         } else {
            return (Key)(cmp > 0 ? this.floor2(x.right, key, x.key) : x.key);
         }
      }
   }

   public Key ceiling(Key key) {
      if (key == null) {
         throw new IllegalArgumentException("argument to ceiling() is null");
      } else if (this.isEmpty()) {
         throw new NoSuchElementException("calls ceiling() with empty symbol table");
      } else {
         BST<Key, Value>.Node x = this.ceiling(this.root, key);
         if (x == null) {
            throw new NoSuchElementException("argument to ceiling() is too large");
         } else {
            return x.key;
         }
      }
   }

   private BST<Key, Value>.Node ceiling(BST<Key, Value>.Node x, Key key) {
      if (x == null) {
         return null;
      } else {
         int cmp = key.compareTo(x.key);
         if (cmp == 0) {
            return x;
         } else if (cmp < 0) {
            BST<Key, Value>.Node t = this.ceiling(x.left, key);
            return t != null ? t : x;
         } else {
            return this.ceiling(x.right, key);
         }
      }
   }

   public Key select(int rank) {
      if (rank >= 0 && rank < this.size()) {
         return (Key)this.select(this.root, rank);
      } else {
         throw new IllegalArgumentException("argument to select() is invalid: " + rank);
      }
   }

   private Key select(BST<Key, Value>.Node x, int rank) {
      if (x == null) {
         return null;
      } else {
         int leftSize = this.size(x.left);
         if (leftSize > rank) {
            return (Key)this.select(x.left, rank);
         } else {
            return (Key)(leftSize < rank ? this.select(x.right, rank - leftSize - 1) : x.key);
         }
      }
   }

   public int rank(Key key) {
      if (key == null) {
         throw new IllegalArgumentException("argument to rank() is null");
      } else {
         return this.rank(key, this.root);
      }
   }

   private int rank(Key key, BST<Key, Value>.Node x) {
      if (x == null) {
         return 0;
      } else {
         int cmp = key.compareTo(x.key);
         if (cmp < 0) {
            return this.rank(key, x.left);
         } else {
            return cmp > 0 ? 1 + this.size(x.left) + this.rank(key, x.right) : this.size(x.left);
         }
      }
   }

   public Iterable<Key> keys() {
      return (Iterable<Key>)(this.isEmpty() ? new Queue() : this.keys(this.min(), this.max()));
   }

   public Iterable<Key> keys(Key lo, Key hi) {
      if (lo == null) {
         throw new IllegalArgumentException("first argument to keys() is null");
      } else if (hi == null) {
         throw new IllegalArgumentException("second argument to keys() is null");
      } else {
         Queue<Key> queue = new Queue<Key>();
         this.keys(this.root, queue, lo, hi);
         return queue;
      }
   }

   private void keys(BST<Key, Value>.Node x, Queue<Key> queue, Key lo, Key hi) {
      if (x != null) {
         int cmplo = lo.compareTo(x.key);
         int cmphi = hi.compareTo(x.key);
         if (cmplo < 0) {
            this.keys(x.left, queue, lo, hi);
         }

         if (cmplo <= 0 && cmphi >= 0) {
            queue.enqueue(x.key);
         }

         if (cmphi > 0) {
            this.keys(x.right, queue, lo, hi);
         }

      }
   }

   public int size(Key lo, Key hi) {
      if (lo == null) {
         throw new IllegalArgumentException("first argument to size() is null");
      } else if (hi == null) {
         throw new IllegalArgumentException("second argument to size() is null");
      } else if (lo.compareTo(hi) > 0) {
         return 0;
      } else {
         return this.contains(hi) ? this.rank(hi) - this.rank(lo) + 1 : this.rank(hi) - this.rank(lo);
      }
   }

   public int height() {
      return this.height(this.root);
   }

   private int height(BST<Key, Value>.Node x) {
      return x == null ? -1 : 1 + Math.max(this.height(x.left), this.height(x.right));
   }

   public Iterable<Key> levelOrder() {
      Queue<Key> keys = new Queue<Key>();
      Queue<BST<Key, Value>.Node> queue = new Queue<BST<Key, Value>.Node>();
      queue.enqueue(this.root);

      while(!queue.isEmpty()) {
         BST<Key, Value>.Node x = queue.dequeue();
         if (x != null) {
            keys.enqueue(x.key);
            queue.enqueue(x.left);
            queue.enqueue(x.right);
         }
      }

      return keys;
   }

   private boolean check() {
      if (!this.isBST()) {
         StdOut.println("Not in symmetric order");
      }

      if (!this.isSizeConsistent()) {
         StdOut.println("Subtree counts not consistent");
      }

      if (!this.isRankConsistent()) {
         StdOut.println("Ranks not consistent");
      }

      return this.isBST() && this.isSizeConsistent() && this.isRankConsistent();
   }

   private boolean isBST() {
      return this.isBST(this.root, (Comparable)null, (Comparable)null);
   }

   private boolean isBST(BST<Key, Value>.Node x, Key min, Key max) {
      if (x == null) {
         return true;
      } else if (min != null && x.key.compareTo(min) <= 0) {
         return false;
      } else if (max != null && x.key.compareTo(max) >= 0) {
         return false;
      } else {
         return this.isBST(x.left, min, x.key) && this.isBST(x.right, x.key, max);
      }
   }

   private boolean isSizeConsistent() {
      return this.isSizeConsistent(this.root);
   }

   private boolean isSizeConsistent(BST<Key, Value>.Node x) {
      if (x == null) {
         return true;
      } else if (x.size != this.size(x.left) + this.size(x.right) + 1) {
         return false;
      } else {
         return this.isSizeConsistent(x.left) && this.isSizeConsistent(x.right);
      }
   }

   private boolean isRankConsistent() {
      for(int i = 0; i < this.size(); ++i) {
         if (i != this.rank(this.select(i))) {
            return false;
         }
      }

      for(Key key : this.keys()) {
         if (key.compareTo(this.select(this.rank(key))) != 0) {
            return false;
         }
      }

      return true;
   }

   public static void main(String[] args) {
      BST<String, Integer> st = new BST<String, Integer>();

      for(int i = 0; !StdIn.isEmpty(); ++i) {
         String key = StdIn.readString();
         st.put(key, i);
      }

      for(String s : st.levelOrder()) {
         StdOut.println(s + " " + st.get(s));
      }

      StdOut.println();

      for(String s : st.keys()) {
         StdOut.println(s + " " + st.get(s));
      }

   }

   private class Node {
      private Key key;
      private Value val;
      private BST<Key, Value>.Node left;
      private BST<Key, Value>.Node right;
      private int size;

      public Node(Key key, Value val, int size) {
         this.key = key;
         this.val = val;
         this.size = size;
      }
   }
}
