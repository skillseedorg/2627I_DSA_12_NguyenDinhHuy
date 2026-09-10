package edu.princeton.cs.algs4;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ResizingArrayBag<Item> implements Iterable<Item> {
   private static final int INIT_CAPACITY = 8;
   private Item[] a = (Item[])(new Object[8]);
   private int n = 0;

   public boolean isEmpty() {
      return this.n == 0;
   }

   public int size() {
      return this.n;
   }

   private void resize(int capacity) {
      assert capacity >= this.n;

      Item[] copy = (Item[])(new Object[capacity]);

      for(int i = 0; i < this.n; ++i) {
         copy[i] = this.a[i];
      }

      this.a = copy;
   }

   public void add(Item item) {
      if (this.n == this.a.length) {
         this.resize(2 * this.a.length);
      }

      this.a[this.n++] = item;
   }

   public Iterator<Item> iterator() {
      return new ArrayIterator();
   }

   public static void main(String[] args) {
      ResizingArrayBag<String> bag = new ResizingArrayBag<String>();
      bag.add("Hello");
      bag.add("World");
      bag.add("how");
      bag.add("are");
      bag.add("you");

      for(String s : bag) {
         StdOut.println(s);
      }

   }

   private class ArrayIterator implements Iterator<Item> {
      private int i = 0;

      public boolean hasNext() {
         return this.i < ResizingArrayBag.this.n;
      }

      public Item next() {
         if (!this.hasNext()) {
            throw new NoSuchElementException();
         } else {
            return (Item)ResizingArrayBag.this.a[this.i++];
         }
      }
   }
}
