package edu.princeton.cs.algs4;

import java.util.NoSuchElementException;

public class BinarySearchST<Key extends Comparable<Key>, Value> {
   private static final int INIT_CAPACITY = 2;
   private Key[] keys;
   private Value[] vals;
   private int n;

   public BinarySearchST() {
      this(2);
   }

   public BinarySearchST(int capacity) {
      this.n = 0;
      this.keys = new Comparable[capacity];
      this.vals = (Value[])(new Object[capacity]);
   }

   private void resize(int capacity) {
      assert capacity >= this.n;

      Key[] tempk = new Comparable[capacity];
      Value[] tempv = (Value[])(new Object[capacity]);

      for(int i = 0; i < this.n; ++i) {
         tempk[i] = this.keys[i];
         tempv[i] = this.vals[i];
      }

      this.vals = tempv;
      this.keys = tempk;
   }

   public int size() {
      return this.n;
   }

   public boolean isEmpty() {
      return this.size() == 0;
   }

   public boolean contains(Key key) {
      if (key == null) {
         throw new IllegalArgumentException("argument to contains() is null");
      } else {
         return this.get(key) != null;
      }
   }

   public Value get(Key key) {
      if (key == null) {
         throw new IllegalArgumentException("argument to get() is null");
      } else if (this.isEmpty()) {
         return null;
      } else {
         int i = this.rank(key);
         return (Value)(i < this.n && this.keys[i].compareTo(key) == 0 ? this.vals[i] : null);
      }
   }

   public int rank(Key key) {
      if (key == null) {
         throw new IllegalArgumentException("argument to rank() is null");
      } else {
         int lo = 0;
         int hi = this.n - 1;

         while(lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            int cmp = key.compareTo(this.keys[mid]);
            if (cmp < 0) {
               hi = mid - 1;
            } else {
               if (cmp <= 0) {
                  return mid;
               }

               lo = mid + 1;
            }
         }

         return lo;
      }
   }

   public void put(Key key, Value val) {
      if (key == null) {
         throw new IllegalArgumentException("first argument to put() is null");
      } else if (val == null) {
         this.delete(key);
      } else {
         int i = this.rank(key);
         if (i < this.n && this.keys[i].compareTo(key) == 0) {
            this.vals[i] = val;
         } else {
            if (this.n == this.keys.length) {
               this.resize(2 * this.keys.length);
            }

            for(int j = this.n; j > i; --j) {
               this.keys[j] = this.keys[j - 1];
               this.vals[j] = this.vals[j - 1];
            }

            this.keys[i] = key;
            this.vals[i] = val;
            ++this.n;

            assert this.check();

         }
      }
   }

   public void delete(Key key) {
      if (key == null) {
         throw new IllegalArgumentException("argument to delete() is null");
      } else if (!this.isEmpty()) {
         int i = this.rank(key);
         if (i != this.n && this.keys[i].compareTo(key) == 0) {
            for(int j = i; j < this.n - 1; ++j) {
               this.keys[j] = this.keys[j + 1];
               this.vals[j] = this.vals[j + 1];
            }

            --this.n;
            this.keys[this.n] = null;
            this.vals[this.n] = null;
            if (this.n > 0 && this.n == this.keys.length / 4) {
               this.resize(this.keys.length / 2);
            }

            assert this.check();

         }
      }
   }

   public void deleteMin() {
      if (this.isEmpty()) {
         throw new NoSuchElementException("Symbol table underflow error");
      } else {
         this.delete(this.min());
      }
   }

   public void deleteMax() {
      if (this.isEmpty()) {
         throw new NoSuchElementException("Symbol table underflow error");
      } else {
         this.delete(this.max());
      }
   }

   public Key min() {
      if (this.isEmpty()) {
         throw new NoSuchElementException("called min() with empty symbol table");
      } else {
         return (Key)this.keys[0];
      }
   }

   public Key max() {
      if (this.isEmpty()) {
         throw new NoSuchElementException("called max() with empty symbol table");
      } else {
         return (Key)this.keys[this.n - 1];
      }
   }

   public Key select(int k) {
      if (k >= 0 && k < this.size()) {
         return (Key)this.keys[k];
      } else {
         throw new IllegalArgumentException("called select() with invalid argument: " + k);
      }
   }

   public Key floor(Key key) {
      if (key == null) {
         throw new IllegalArgumentException("argument to floor() is null");
      } else {
         int i = this.rank(key);
         if (i < this.n && key.compareTo(this.keys[i]) == 0) {
            return (Key)this.keys[i];
         } else if (i == 0) {
            throw new NoSuchElementException("argument to floor() is too small");
         } else {
            return (Key)this.keys[i - 1];
         }
      }
   }

   public Key ceiling(Key key) {
      if (key == null) {
         throw new IllegalArgumentException("argument to ceiling() is null");
      } else {
         int i = this.rank(key);
         if (i == this.n) {
            throw new NoSuchElementException("argument to ceiling() is too large");
         } else {
            return (Key)this.keys[i];
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

   public Iterable<Key> keys() {
      return this.keys(this.min(), this.max());
   }

   public Iterable<Key> keys(Key lo, Key hi) {
      if (lo == null) {
         throw new IllegalArgumentException("first argument to keys() is null");
      } else if (hi == null) {
         throw new IllegalArgumentException("second argument to keys() is null");
      } else {
         Queue<Key> queue = new Queue<Key>();
         if (lo.compareTo(hi) > 0) {
            return queue;
         } else {
            for(int i = this.rank(lo); i < this.rank(hi); ++i) {
               queue.enqueue(this.keys[i]);
            }

            if (this.contains(hi)) {
               queue.enqueue(this.keys[this.rank(hi)]);
            }

            return queue;
         }
      }
   }

   private boolean check() {
      return this.isSorted() && this.rankCheck();
   }

   private boolean isSorted() {
      for(int i = 1; i < this.size(); ++i) {
         if (this.keys[i].compareTo(this.keys[i - 1]) < 0) {
            return false;
         }
      }

      return true;
   }

   private boolean rankCheck() {
      for(int i = 0; i < this.size(); ++i) {
         if (i != this.rank(this.select(i))) {
            return false;
         }
      }

      for(int i = 0; i < this.size(); ++i) {
         if (this.keys[i].compareTo(this.select(this.rank(this.keys[i]))) != 0) {
            return false;
         }
      }

      return true;
   }

   public static void main(String[] args) {
      BinarySearchST<String, Integer> st = new BinarySearchST<String, Integer>();

      for(int i = 0; !StdIn.isEmpty(); ++i) {
         String key = StdIn.readString();
         st.put(key, i);
      }

      for(String s : st.keys()) {
         StdOut.println(s + " " + st.get(s));
      }

   }
}
