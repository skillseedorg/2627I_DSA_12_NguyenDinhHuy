package edu.princeton.cs.algs4;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.TreeMap;

public class ST<Key extends Comparable<Key>, Value> implements Iterable<Key> {
   private TreeMap<Key, Value> st = new TreeMap();

   public Value get(Key key) {
      if (key == null) {
         throw new IllegalArgumentException("calls get() with null key");
      } else {
         return (Value)this.st.get(key);
      }
   }

   public void put(Key key, Value val) {
      if (key == null) {
         throw new IllegalArgumentException("calls put() with null key");
      } else {
         if (val == null) {
            this.st.remove(key);
         } else {
            this.st.put(key, val);
         }

      }
   }

   public void delete(Key key) {
      if (key == null) {
         throw new IllegalArgumentException("calls delete() with null key");
      } else {
         this.st.remove(key);
      }
   }

   public void remove(Key key) {
      if (key == null) {
         throw new IllegalArgumentException("calls remove() with null key");
      } else {
         this.st.remove(key);
      }
   }

   public boolean contains(Key key) {
      if (key == null) {
         throw new IllegalArgumentException("calls contains() with null key");
      } else {
         return this.st.containsKey(key);
      }
   }

   public int size() {
      return this.st.size();
   }

   public boolean isEmpty() {
      return this.size() == 0;
   }

   public Iterable<Key> keys() {
      return this.st.keySet();
   }

   /** @deprecated */
   @Deprecated
   public Iterator<Key> iterator() {
      return this.st.keySet().iterator();
   }

   public Key min() {
      if (this.isEmpty()) {
         throw new NoSuchElementException("calls min() with empty symbol table");
      } else {
         return (Key)(this.st.firstKey());
      }
   }

   public Key max() {
      if (this.isEmpty()) {
         throw new NoSuchElementException("calls max() with empty symbol table");
      } else {
         return (Key)(this.st.lastKey());
      }
   }

   public Key ceiling(Key key) {
      if (key == null) {
         throw new IllegalArgumentException("argument to ceiling() is null");
      } else {
         Key k = (Key)(this.st.ceilingKey(key));
         if (k == null) {
            throw new NoSuchElementException("argument to ceiling() is too large");
         } else {
            return k;
         }
      }
   }

   public Key floor(Key key) {
      if (key == null) {
         throw new IllegalArgumentException("argument to floor() is null");
      } else {
         Key k = (Key)(this.st.floorKey(key));
         if (k == null) {
            throw new NoSuchElementException("argument to floor() is too small");
         } else {
            return k;
         }
      }
   }

   public static void main(String[] args) {
      ST<String, Integer> st = new ST<String, Integer>();

      for(int i = 0; !StdIn.isEmpty(); ++i) {
         String key = StdIn.readString();
         st.put(key, i);
      }

      for(String s : st.keys()) {
         StdOut.println(s + " " + st.get(s));
      }

   }
}
