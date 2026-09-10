package edu.princeton.cs.algs4;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedBag<Item> implements Iterable<Item> {
   private LinkedBag<Item>.Node first = null;
   private int n = 0;

   public boolean isEmpty() {
      return this.first == null;
   }

   public int size() {
      return this.n;
   }

   public void add(Item item) {
      LinkedBag<Item>.Node oldfirst = this.first;
      this.first = new Node();
      this.first.item = item;
      this.first.next = oldfirst;
      ++this.n;
   }

   public Iterator<Item> iterator() {
      return new LinkedIterator();
   }

   public static void main(String[] args) {
      LinkedBag<String> bag = new LinkedBag<String>();

      while(!StdIn.isEmpty()) {
         String item = StdIn.readString();
         bag.add(item);
      }

      StdOut.println("size of bag = " + bag.size());

      for(String s : bag) {
         StdOut.println(s);
      }

   }

   private class Node {
      private Item item;
      private LinkedBag<Item>.Node next;
   }

   private class LinkedIterator implements Iterator<Item> {
      private LinkedBag<Item>.Node current;

      public LinkedIterator() {
         this.current = LinkedBag.this.first;
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
