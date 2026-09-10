package edu.princeton.cs.algs4;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ResizingArrayStack<Item> implements Iterable<Item> {
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

   public void push(Item item) {
      if (this.n == this.a.length) {
         this.resize(2 * this.a.length);
      }

      this.a[this.n++] = item;
   }

   public Item pop() {
      if (this.isEmpty()) {
         throw new NoSuchElementException("Stack underflow");
      } else {
         Item item = (Item)this.a[this.n - 1];
         this.a[this.n - 1] = null;
         --this.n;
         if (this.n > 0 && this.n == this.a.length / 4) {
            this.resize(this.a.length / 2);
         }

         return item;
      }
   }

   public Item peek() {
      if (this.isEmpty()) {
         throw new NoSuchElementException("Stack underflow");
      } else {
         return (Item)this.a[this.n - 1];
      }
   }

   public Iterator<Item> iterator() {
      return new ReverseArrayIterator();
   }

   public static void main(String[] args) {
      ResizingArrayStack<String> stack = new ResizingArrayStack<String>();

      while(!StdIn.isEmpty()) {
         String item = StdIn.readString();
         if (!item.equals("-")) {
            stack.push(item);
         } else if (!stack.isEmpty()) {
            StdOut.print((String)stack.pop() + " ");
         }
      }

      StdOut.println("(" + stack.size() + " left on stack)");
   }

   private class ReverseArrayIterator implements Iterator<Item> {
      private int i;

      public ReverseArrayIterator() {
         this.i = ResizingArrayStack.this.n - 1;
      }

      public boolean hasNext() {
         return this.i >= 0;
      }

      public Item next() {
         if (!this.hasNext()) {
            throw new NoSuchElementException();
         } else {
            return (Item)ResizingArrayStack.this.a[this.i--];
         }
      }
   }
}
