package edu.princeton.cs.algs4;

import java.util.Iterator;

public class PatriciaSET implements Iterable<String> {
   private Node head = new Node("", 0);
   private int count;

   public PatriciaSET() {
      this.head.left = this.head;
      this.head.right = this.head;
      this.count = 0;
   }

   public void add(String key) {
      if (key == null) {
         throw new IllegalArgumentException("called add(null)");
      } else if (key.length() == 0) {
         throw new IllegalArgumentException("invalid key");
      } else {
         Node x = this.head;

         Node p;
         do {
            p = x;
            if (safeBitTest(key, x.b)) {
               x = x.right;
            } else {
               x = x.left;
            }
         } while(p.b < x.b);

         if (!x.key.equals(key)) {
            int b = firstDifferingBit(x.key, key);
            x = this.head;

            do {
               p = x;
               if (safeBitTest(key, x.b)) {
                  x = x.right;
               } else {
                  x = x.left;
               }
            } while(p.b < x.b && x.b < b);

            Node t = new Node(key, b);
            if (safeBitTest(key, b)) {
               t.left = x;
               t.right = t;
            } else {
               t.left = t;
               t.right = x;
            }

            if (safeBitTest(key, p.b)) {
               p.right = t;
            } else {
               p.left = t;
            }

            ++this.count;
         }

      }
   }

   public boolean contains(String key) {
      if (key == null) {
         throw new IllegalArgumentException("called contains(null)");
      } else if (key.length() == 0) {
         throw new IllegalArgumentException("invalid key");
      } else {
         Node x = this.head;

         Node p;
         do {
            p = x;
            if (safeBitTest(key, x.b)) {
               x = x.right;
            } else {
               x = x.left;
            }
         } while(p.b < x.b);

         return x.key.equals(key);
      }
   }

   public void delete(String key) {
      if (key == null) {
         throw new IllegalArgumentException("called delete(null)");
      } else if (key.length() == 0) {
         throw new IllegalArgumentException("invalid key");
      } else {
         Node p = this.head;
         Node x = this.head;

         Node g;
         do {
            g = p;
            p = x;
            if (safeBitTest(key, x.b)) {
               x = x.right;
            } else {
               x = x.left;
            }
         } while(p.b < x.b);

         if (x.key.equals(key)) {
            Node y = this.head;

            Node z;
            do {
               z = y;
               if (safeBitTest(key, y.b)) {
                  y = y.right;
               } else {
                  y = y.left;
               }
            } while(y != x);

            if (x == p) {
               Node c;
               if (safeBitTest(key, x.b)) {
                  c = x.left;
               } else {
                  c = x.right;
               }

               if (safeBitTest(key, z.b)) {
                  z.right = c;
               } else {
                  z.left = c;
               }
            } else {
               Node c;
               if (safeBitTest(key, p.b)) {
                  c = p.left;
               } else {
                  c = p.right;
               }

               if (safeBitTest(key, g.b)) {
                  g.right = c;
               } else {
                  g.left = c;
               }

               if (safeBitTest(key, z.b)) {
                  z.right = p;
               } else {
                  z.left = p;
               }

               p.left = x.left;
               p.right = x.right;
               p.b = x.b;
            }

            --this.count;
         }

      }
   }

   boolean isEmpty() {
      return this.count == 0;
   }

   int size() {
      return this.count;
   }

   public Iterator<String> iterator() {
      Queue<String> queue = new Queue<String>();
      if (this.head.left != this.head) {
         this.collect(this.head.left, 0, queue);
      }

      if (this.head.right != this.head) {
         this.collect(this.head.right, 0, queue);
      }

      return queue.iterator();
   }

   private void collect(Node x, int b, Queue<String> queue) {
      if (x.b > b) {
         this.collect(x.left, x.b, queue);
         queue.enqueue(x.key);
         this.collect(x.right, x.b, queue);
      }

   }

   public String toString() {
      StringBuilder s = new StringBuilder();

      for(String key : this) {
         s.append(key + " ");
      }

      if (s.length() > 0) {
         s.deleteCharAt(s.length() - 1);
      }

      return s.toString();
   }

   private static boolean safeBitTest(String key, int b) {
      if (b < key.length() * 16) {
         return bitTest(key, b) != 0;
      } else {
         return b <= key.length() * 16 + 15;
      }
   }

   private static int bitTest(String key, int b) {
      return key.charAt(b >>> 4) >>> (b & 15) & 1;
   }

   private static int safeCharAt(String key, int i) {
      if (i < key.length()) {
         return key.charAt(i);
      } else {
         return i > key.length() ? 0 : '\uffff';
      }
   }

   private static int firstDifferingBit(String k1, String k2) {
      int i = 0;
      int c1 = safeCharAt(k1, 0) & -2;
      int c2 = safeCharAt(k2, 0) & -2;
      if (c1 == c2) {
         for(i = 1; safeCharAt(k1, i) == safeCharAt(k2, i); ++i) {
         }

         c1 = safeCharAt(k1, i);
         c2 = safeCharAt(k2, i);
      }

      int b;
      for(b = 0; (c1 >>> b & 1) == (c2 >>> b & 1); ++b) {
      }

      return i * 16 + b;
   }

   public static void main(String[] args) {
      PatriciaSET set = new PatriciaSET();
      int limitItem = 1000000;
      int limitPass = 1;
      int countPass = 0;
      boolean ok = true;
      if (args.length > 0) {
         limitItem = Integer.parseInt(args[0]);
      }

      if (args.length > 1) {
         limitPass = Integer.parseInt(args[1]);
      }

      do {
         String[] a = new String[limitItem];
         StdOut.printf("Creating dataset (%d items)...\n", limitItem);

         for(int i = 0; i < limitItem; ++i) {
            a[i] = Integer.toString(i, 16);
         }

         StdOut.printf("Shuffling...\n");
         StdRandom.shuffle((Object[])a);
         StdOut.printf("Adding (%d items)...\n", limitItem);

         for(int i = 0; i < limitItem; ++i) {
            set.add(a[i]);
         }

         int countItems = 0;
         StdOut.printf("Iterating...\n");

         for(String key : set) {
            ++countItems;
         }

         StdOut.printf("%d items iterated\n", countItems);
         if (countItems != limitItem) {
            ok = false;
         }

         if (countItems != set.size()) {
            ok = false;
         }

         StdOut.printf("Shuffling...\n");
         StdRandom.shuffle((Object[])a);
         int limitDelete = limitItem / 2;
         StdOut.printf("Deleting (%d items)...\n", limitDelete);

         for(int i = 0; i < limitDelete; ++i) {
            set.delete(a[i]);
         }

         countItems = 0;
         StdOut.printf("Iterating...\n");

         for(String key : set) {
            ++countItems;
         }

         StdOut.printf("%d items iterated\n", countItems);
         if (countItems != limitItem - limitDelete) {
            ok = false;
         }

         if (countItems != set.size()) {
            ok = false;
         }

         int countDelete = 0;
         int countRemain = 0;
         StdOut.printf("Checking...\n");

         for(int i = 0; i < limitItem; ++i) {
            if (i < limitDelete) {
               if (!set.contains(a[i])) {
                  ++countDelete;
               }
            } else if (set.contains(a[i])) {
               ++countRemain;
            }
         }

         StdOut.printf("%d items found and %d (deleted) items missing\n", countRemain, countDelete);
         if (countRemain + countDelete != limitItem) {
            ok = false;
         }

         if (countRemain != set.size()) {
            ok = false;
         }

         if (set.isEmpty()) {
            ok = false;
         }

         StdOut.printf("Deleting the rest (%d items)...\n", limitItem - countDelete);

         for(int i = countDelete; i < limitItem; ++i) {
            set.delete(a[i]);
         }

         if (!set.isEmpty()) {
            ok = false;
         }

         ++countPass;
         if (ok) {
            StdOut.printf("PASS %d TESTS SUCCEEDED\n", countPass);
         } else {
            StdOut.printf("PASS %d TESTS FAILED\n", countPass);
         }
      } while(ok && countPass < limitPass);

      if (!ok) {
         throw new RuntimeException("TESTS FAILED");
      }
   }

   private class Node {
      private Node left;
      private Node right;
      private String key;
      private int b;

      public Node(String key, int b) {
         this.key = key;
         this.b = b;
      }
   }
}
