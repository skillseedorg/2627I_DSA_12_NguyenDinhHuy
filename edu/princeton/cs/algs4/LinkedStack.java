package edu.princeton.cs.algs4;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedStack<Item> implements Iterable<Item> {
   private int n = 0;
   private LinkedStack<Item>.Node first = null;

   public LinkedStack() {
      assert this.check();

   }

   public boolean isEmpty() {
      return this.first == null;
   }

   public int size() {
      return this.n;
   }

   public void push(Item item) {
      LinkedStack<Item>.Node oldfirst = this.first;
      this.first = new Node();
      this.first.item = item;
      this.first.next = oldfirst;
      ++this.n;

      assert this.check();

   }

   public Item pop() {
      if (this.isEmpty()) {
         throw new NoSuchElementException("Stack underflow");
      } else {
         Item item = this.first.item;
         this.first = this.first.next;
         --this.n;

         assert this.check();

         return item;
      }
   }

   public Item peek() {
      if (this.isEmpty()) {
         throw new NoSuchElementException("Stack underflow");
      } else {
         return this.first.item;
      }
   }

   public String toString() {
      StringBuilder s = new StringBuilder();

      for(Item item : this) {
         s.append(item + " ");
      }

      return s.toString();
   }

   public Iterator<Item> iterator() {
      return new LinkedIterator();
   }

   private boolean check() {
      if (this.n < 0) {
         return false;
      } else {
         if (this.n == 0) {
            if (this.first != null) {
               return false;
            }
         } else if (this.n == 1) {
            if (this.first == null) {
               return false;
            }

            if (this.first.next != null) {
               return false;
            }
         } else {
            if (this.first == null) {
               return false;
            }

            if (this.first.next == null) {
               return false;
            }
         }

         int numberOfNodes = 0;

         for(LinkedStack<Item>.Node x = this.first; x != null && numberOfNodes <= this.n; x = x.next) {
            ++numberOfNodes;
         }

         return numberOfNodes == this.n;
      }
   }

   public static void main(String[] args) {
      LinkedStack<String> stack = new LinkedStack<String>();

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

   private class Node {
      private Item item;
      private LinkedStack<Item>.Node next;
   }

   private class LinkedIterator implements Iterator<Item> {
      private LinkedStack<Item>.Node current;

      private LinkedIterator() {
         this.current = LinkedStack.this.first;
      }

      public boolean hasNext() {
         return this.current != null;
      }

      public Item next() {
         if (!this.hasNext()) {
            throw new NoSuchElementException();
         } else {
            Item item = this.current.item;
            this.current = this.current.next;
            return item;
         }
      }
   }
}
