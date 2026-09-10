package edu.princeton.cs.algs4;

import java.util.Iterator;

public class TrieSET implements Iterable<String> {
   private static final int R = 256;
   private Node root;
   private int n;

   public boolean contains(String key) {
      if (key == null) {
         throw new IllegalArgumentException("argument to contains() is null");
      } else {
         Node x = this.get(this.root, key, 0);
         return x == null ? false : x.isString;
      }
   }

   private Node get(Node x, String key, int d) {
      if (x == null) {
         return null;
      } else if (d == key.length()) {
         return x;
      } else {
         char c = key.charAt(d);
         return this.get(x.next[c], key, d + 1);
      }
   }

   public void add(String key) {
      if (key == null) {
         throw new IllegalArgumentException("argument to add() is null");
      } else {
         this.root = this.add(this.root, key, 0);
      }
   }

   private Node add(Node x, String key, int d) {
      if (x == null) {
         x = new Node();
      }

      if (d == key.length()) {
         if (!x.isString) {
            ++this.n;
         }

         x.isString = true;
      } else {
         char c = key.charAt(d);
         x.next[c] = this.add(x.next[c], key, d + 1);
      }

      return x;
   }

   public int size() {
      return this.n;
   }

   public boolean isEmpty() {
      return this.size() == 0;
   }

   public Iterator<String> iterator() {
      return this.keysWithPrefix("").iterator();
   }

   public Iterable<String> keysWithPrefix(String prefix) {
      Queue<String> results = new Queue<String>();
      Node x = this.get(this.root, prefix, 0);
      this.collect(x, new StringBuilder(prefix), results);
      return results;
   }

   private void collect(Node x, StringBuilder prefix, Queue<String> results) {
      if (x != null) {
         if (x.isString) {
            results.enqueue(prefix.toString());
         }

         for(char c = 0; c < 256; ++c) {
            prefix.append(c);
            this.collect(x.next[c], prefix, results);
            prefix.deleteCharAt(prefix.length() - 1);
         }

      }
   }

   public Iterable<String> keysThatMatch(String pattern) {
      Queue<String> results = new Queue<String>();
      StringBuilder prefix = new StringBuilder();
      this.collect(this.root, prefix, pattern, results);
      return results;
   }

   private void collect(Node x, StringBuilder prefix, String pattern, Queue<String> results) {
      if (x != null) {
         int d = prefix.length();
         if (d == pattern.length() && x.isString) {
            results.enqueue(prefix.toString());
         }

         if (d != pattern.length()) {
            char c = pattern.charAt(d);
            if (c == '.') {
               for(char ch = 0; ch < 256; ++ch) {
                  prefix.append(ch);
                  this.collect(x.next[ch], prefix, pattern, results);
                  prefix.deleteCharAt(prefix.length() - 1);
               }
            } else {
               prefix.append(c);
               this.collect(x.next[c], prefix, pattern, results);
               prefix.deleteCharAt(prefix.length() - 1);
            }

         }
      }
   }

   public String longestPrefixOf(String query) {
      if (query == null) {
         throw new IllegalArgumentException("argument to longestPrefixOf() is null");
      } else {
         int length = this.longestPrefixOf(this.root, query, 0, -1);
         return length == -1 ? null : query.substring(0, length);
      }
   }

   private int longestPrefixOf(Node x, String query, int d, int length) {
      if (x == null) {
         return length;
      } else {
         if (x.isString) {
            length = d;
         }

         if (d == query.length()) {
            return length;
         } else {
            char c = query.charAt(d);
            return this.longestPrefixOf(x.next[c], query, d + 1, length);
         }
      }
   }

   public void delete(String key) {
      if (key == null) {
         throw new IllegalArgumentException("argument to delete() is null");
      } else {
         this.root = this.delete(this.root, key, 0);
      }
   }

   private Node delete(Node x, String key, int d) {
      if (x == null) {
         return null;
      } else {
         if (d == key.length()) {
            if (x.isString) {
               --this.n;
            }

            x.isString = false;
         } else {
            char c = key.charAt(d);
            x.next[c] = this.delete(x.next[c], key, d + 1);
         }

         if (x.isString) {
            return x;
         } else {
            for(int c = 0; c < 256; ++c) {
               if (x.next[c] != null) {
                  return x;
               }
            }

            return null;
         }
      }
   }

   public static void main(String[] args) {
      TrieSET set = new TrieSET();

      while(!StdIn.isEmpty()) {
         String key = StdIn.readString();
         set.add(key);
      }

      if (set.size() < 100) {
         StdOut.println("keys(\"\"):");

         for(String key : set) {
            StdOut.println(key);
         }

         StdOut.println();
      }

      StdOut.println("longestPrefixOf(\"shellsort\"):");
      StdOut.println(set.longestPrefixOf("shellsort"));
      StdOut.println();
      StdOut.println("longestPrefixOf(\"xshellsort\"):");
      StdOut.println(set.longestPrefixOf("xshellsort"));
      StdOut.println();
      StdOut.println("keysWithPrefix(\"shor\"):");

      for(String s : set.keysWithPrefix("shor")) {
         StdOut.println(s);
      }

      StdOut.println();
      StdOut.println("keysWithPrefix(\"shortening\"):");

      for(String s : set.keysWithPrefix("shortening")) {
         StdOut.println(s);
      }

      StdOut.println();
      StdOut.println("keysThatMatch(\".he.l.\"):");

      for(String s : set.keysThatMatch(".he.l.")) {
         StdOut.println(s);
      }

   }

   private static class Node {
      private Node[] next = new Node[256];
      private boolean isString;
   }
}
