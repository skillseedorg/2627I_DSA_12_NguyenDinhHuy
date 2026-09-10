package edu.princeton.cs.algs4;

import java.util.NoSuchElementException;

public class AVLTreeST<Key extends Comparable<Key>, Value> {
   private AVLTreeST<Key, Value>.Node root;

   public boolean isEmpty() {
      return this.root == null;
   }

   public int size() {
      return this.size(this.root);
   }

   private int size(AVLTreeST<Key, Value>.Node x) {
      return x == null ? 0 : x.size;
   }

   public int height() {
      return this.height(this.root);
   }

   private int height(AVLTreeST<Key, Value>.Node x) {
      return x == null ? -1 : x.height;
   }

   public Value get(Key key) {
      if (key == null) {
         throw new IllegalArgumentException("argument to get() is null");
      } else {
         AVLTreeST<Key, Value>.Node x = this.get(this.root, key);
         return (Value)(x == null ? null : x.val);
      }
   }

   private AVLTreeST<Key, Value>.Node get(AVLTreeST<Key, Value>.Node x, Key key) {
      if (x == null) {
         return null;
      } else {
         int cmp = key.compareTo(x.key);
         if (cmp < 0) {
            return this.get(x.left, key);
         } else {
            return cmp > 0 ? this.get(x.right, key) : x;
         }
      }
   }

   public boolean contains(Key key) {
      return this.get(key) != null;
   }

   public void put(Key key, Value val) {
      if (key == null) {
         throw new IllegalArgumentException("first argument to put() is null");
      } else if (val == null) {
         this.delete(key);
      } else {
         this.root = this.put(this.root, key, val);

         assert this.check();

      }
   }

   private AVLTreeST<Key, Value>.Node put(AVLTreeST<Key, Value>.Node x, Key key, Value val) {
      if (x == null) {
         return new Node(key, val, 0, 1);
      } else {
         int cmp = key.compareTo(x.key);
         if (cmp < 0) {
            x.left = this.put(x.left, key, val);
         } else {
            if (cmp <= 0) {
               x.val = val;
               return x;
            }

            x.right = this.put(x.right, key, val);
         }

         x.size = 1 + this.size(x.left) + this.size(x.right);
         x.height = 1 + Math.max(this.height(x.left), this.height(x.right));
         return this.balance(x);
      }
   }

   private AVLTreeST<Key, Value>.Node balance(AVLTreeST<Key, Value>.Node x) {
      if (this.balanceFactor(x) < -1) {
         if (this.balanceFactor(x.right) > 0) {
            x.right = this.rotateRight(x.right);
         }

         x = this.rotateLeft(x);
      } else if (this.balanceFactor(x) > 1) {
         if (this.balanceFactor(x.left) < 0) {
            x.left = this.rotateLeft(x.left);
         }

         x = this.rotateRight(x);
      }

      return x;
   }

   private int balanceFactor(AVLTreeST<Key, Value>.Node x) {
      return this.height(x.left) - this.height(x.right);
   }

   private AVLTreeST<Key, Value>.Node rotateRight(AVLTreeST<Key, Value>.Node x) {
      AVLTreeST<Key, Value>.Node y = x.left;
      x.left = y.right;
      y.right = x;
      y.size = x.size;
      x.size = 1 + this.size(x.left) + this.size(x.right);
      x.height = 1 + Math.max(this.height(x.left), this.height(x.right));
      y.height = 1 + Math.max(this.height(y.left), this.height(y.right));
      return y;
   }

   private AVLTreeST<Key, Value>.Node rotateLeft(AVLTreeST<Key, Value>.Node x) {
      AVLTreeST<Key, Value>.Node y = x.right;
      x.right = y.left;
      y.left = x;
      y.size = x.size;
      x.size = 1 + this.size(x.left) + this.size(x.right);
      x.height = 1 + Math.max(this.height(x.left), this.height(x.right));
      y.height = 1 + Math.max(this.height(y.left), this.height(y.right));
      return y;
   }

   public void delete(Key key) {
      if (key == null) {
         throw new IllegalArgumentException("argument to delete() is null");
      } else if (this.contains(key)) {
         this.root = this.delete(this.root, key);

         assert this.check();

      }
   }

   private AVLTreeST<Key, Value>.Node delete(AVLTreeST<Key, Value>.Node x, Key key) {
      int cmp = key.compareTo(x.key);
      if (cmp < 0) {
         x.left = this.delete(x.left, key);
      } else if (cmp > 0) {
         x.right = this.delete(x.right, key);
      } else {
         if (x.left == null) {
            return x.right;
         }

         if (x.right == null) {
            return x.left;
         }

         AVLTreeST<Key, Value>.Node y = x;
         x = this.min(x.right);
         x.right = this.deleteMin(y.right);
         x.left = y.left;
      }

      x.size = 1 + this.size(x.left) + this.size(x.right);
      x.height = 1 + Math.max(this.height(x.left), this.height(x.right));
      return this.balance(x);
   }

   public void deleteMin() {
      if (this.isEmpty()) {
         throw new NoSuchElementException("called deleteMin() with empty symbol table");
      } else {
         this.root = this.deleteMin(this.root);

         assert this.check();

      }
   }

   private AVLTreeST<Key, Value>.Node deleteMin(AVLTreeST<Key, Value>.Node x) {
      if (x.left == null) {
         return x.right;
      } else {
         x.left = this.deleteMin(x.left);
         x.size = 1 + this.size(x.left) + this.size(x.right);
         x.height = 1 + Math.max(this.height(x.left), this.height(x.right));
         return this.balance(x);
      }
   }

   public void deleteMax() {
      if (this.isEmpty()) {
         throw new NoSuchElementException("called deleteMax() with empty symbol table");
      } else {
         this.root = this.deleteMax(this.root);

         assert this.check();

      }
   }

   private AVLTreeST<Key, Value>.Node deleteMax(AVLTreeST<Key, Value>.Node x) {
      if (x.right == null) {
         return x.left;
      } else {
         x.right = this.deleteMax(x.right);
         x.size = 1 + this.size(x.left) + this.size(x.right);
         x.height = 1 + Math.max(this.height(x.left), this.height(x.right));
         return this.balance(x);
      }
   }

   public Key min() {
      if (this.isEmpty()) {
         throw new NoSuchElementException("called min() with empty symbol table");
      } else {
         return this.min(this.root).key;
      }
   }

   private AVLTreeST<Key, Value>.Node min(AVLTreeST<Key, Value>.Node x) {
      return x.left == null ? x : this.min(x.left);
   }

   public Key max() {
      if (this.isEmpty()) {
         throw new NoSuchElementException("called max() with empty symbol table");
      } else {
         return this.max(this.root).key;
      }
   }

   private AVLTreeST<Key, Value>.Node max(AVLTreeST<Key, Value>.Node x) {
      return x.right == null ? x : this.max(x.right);
   }

   public Key floor(Key key) {
      if (key == null) {
         throw new IllegalArgumentException("argument to floor() is null");
      } else if (this.isEmpty()) {
         throw new NoSuchElementException("called floor() with empty symbol table");
      } else {
         AVLTreeST<Key, Value>.Node x = this.floor(this.root, key);
         return (Key)(x == null ? null : x.key);
      }
   }

   private AVLTreeST<Key, Value>.Node floor(AVLTreeST<Key, Value>.Node x, Key key) {
      if (x == null) {
         return null;
      } else {
         int cmp = key.compareTo(x.key);
         if (cmp == 0) {
            return x;
         } else if (cmp < 0) {
            return this.floor(x.left, key);
         } else {
            AVLTreeST<Key, Value>.Node y = this.floor(x.right, key);
            return y != null ? y : x;
         }
      }
   }

   public Key ceiling(Key key) {
      if (key == null) {
         throw new IllegalArgumentException("argument to ceiling() is null");
      } else if (this.isEmpty()) {
         throw new NoSuchElementException("called ceiling() with empty symbol table");
      } else {
         AVLTreeST<Key, Value>.Node x = this.ceiling(this.root, key);
         return (Key)(x == null ? null : x.key);
      }
   }

   private AVLTreeST<Key, Value>.Node ceiling(AVLTreeST<Key, Value>.Node x, Key key) {
      if (x == null) {
         return null;
      } else {
         int cmp = key.compareTo(x.key);
         if (cmp == 0) {
            return x;
         } else if (cmp > 0) {
            return this.ceiling(x.right, key);
         } else {
            AVLTreeST<Key, Value>.Node y = this.ceiling(x.left, key);
            return y != null ? y : x;
         }
      }
   }

   public Key select(int k) {
      if (k >= 0 && k < this.size()) {
         AVLTreeST<Key, Value>.Node x = this.select(this.root, k);
         return x.key;
      } else {
         int var10002 = this.size();
         throw new IllegalArgumentException("k is not in range 0-" + (var10002 - 1));
      }
   }

   private AVLTreeST<Key, Value>.Node select(AVLTreeST<Key, Value>.Node x, int k) {
      if (x == null) {
         return null;
      } else {
         int t = this.size(x.left);
         if (t > k) {
            return this.select(x.left, k);
         } else {
            return t < k ? this.select(x.right, k - t - 1) : x;
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

   private int rank(Key key, AVLTreeST<Key, Value>.Node x) {
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
      return this.keysInOrder();
   }

   public Iterable<Key> keysInOrder() {
      Queue<Key> queue = new Queue<Key>();
      this.keysInOrder(this.root, queue);
      return queue;
   }

   private void keysInOrder(AVLTreeST<Key, Value>.Node x, Queue<Key> queue) {
      if (x != null) {
         this.keysInOrder(x.left, queue);
         queue.enqueue(x.key);
         this.keysInOrder(x.right, queue);
      }
   }

   public Iterable<Key> keysLevelOrder() {
      Queue<Key> queue = new Queue<Key>();
      if (!this.isEmpty()) {
         Queue<AVLTreeST<Key, Value>.Node> queue2 = new Queue<AVLTreeST<Key, Value>.Node>();
         queue2.enqueue(this.root);

         while(!queue2.isEmpty()) {
            AVLTreeST<Key, Value>.Node x = queue2.dequeue();
            queue.enqueue(x.key);
            if (x.left != null) {
               queue2.enqueue(x.left);
            }

            if (x.right != null) {
               queue2.enqueue(x.right);
            }
         }
      }

      return queue;
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

   private void keys(AVLTreeST<Key, Value>.Node x, Queue<Key> queue, Key lo, Key hi) {
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

   private boolean check() {
      if (!this.isBST()) {
         StdOut.println("Symmetric order not consistent");
      }

      if (!this.isAVL()) {
         StdOut.println("AVL property not consistent");
      }

      if (!this.isSizeConsistent()) {
         StdOut.println("Subtree counts not consistent");
      }

      if (!this.isRankConsistent()) {
         StdOut.println("Ranks not consistent");
      }

      return this.isBST() && this.isAVL() && this.isSizeConsistent() && this.isRankConsistent();
   }

   private boolean isAVL() {
      return this.isAVL(this.root);
   }

   private boolean isAVL(AVLTreeST<Key, Value>.Node x) {
      if (x == null) {
         return true;
      } else {
         int bf = this.balanceFactor(x);
         if (bf <= 1 && bf >= -1) {
            return this.isAVL(x.left) && this.isAVL(x.right);
         } else {
            return false;
         }
      }
   }

   private boolean isBST() {
      return this.isBST(this.root, (Comparable)null, (Comparable)null);
   }

   private boolean isBST(AVLTreeST<Key, Value>.Node x, Key min, Key max) {
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

   private boolean isSizeConsistent(AVLTreeST<Key, Value>.Node x) {
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
      AVLTreeST<String, Integer> st = new AVLTreeST<String, Integer>();

      for(int i = 0; !StdIn.isEmpty(); ++i) {
         String key = StdIn.readString();
         st.put(key, i);
      }

      for(String s : st.keys()) {
         StdOut.println(s + " " + st.get(s));
      }

      StdOut.println();
   }

   private class Node {
      private final Key key;
      private Value val;
      private int height;
      private int size;
      private AVLTreeST<Key, Value>.Node left;
      private AVLTreeST<Key, Value>.Node right;

      public Node(Key key, Value val, int height, int size) {
         this.key = key;
         this.val = val;
         this.size = size;
         this.height = height;
      }
   }
}
