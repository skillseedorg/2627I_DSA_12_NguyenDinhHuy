package edu.princeton.cs.algs4;

public class PatriciaST<Value> {
   private PatriciaST<Value>.Node head = new Node("", (Object)null, 0);
   private int count;

   public PatriciaST() {
      this.head.left = this.head;
      this.head.right = this.head;
      this.count = 0;
   }

   public void put(String key, Value val) {
      if (key == null) {
         throw new IllegalArgumentException("called put(null)");
      } else if (key.length() == 0) {
         throw new IllegalArgumentException("invalid key");
      } else {
         if (val == null) {
            this.delete(key);
         }

         PatriciaST<Value>.Node x = this.head;

         PatriciaST<Value>.Node p;
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

            PatriciaST<Value>.Node t = new Node(key, val, b);
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
         } else {
            x.val = val;
         }

      }
   }

   public Value get(String key) {
      if (key == null) {
         throw new IllegalArgumentException("called get(null)");
      } else if (key.length() == 0) {
         throw new IllegalArgumentException("invalid key");
      } else {
         PatriciaST<Value>.Node x = this.head;

         PatriciaST<Value>.Node p;
         do {
            p = x;
            if (safeBitTest(key, x.b)) {
               x = x.right;
            } else {
               x = x.left;
            }
         } while(p.b < x.b);

         if (x.key.equals(key)) {
            return x.val;
         } else {
            return null;
         }
      }
   }

   public void delete(String key) {
      if (key == null) {
         throw new IllegalArgumentException("called delete(null)");
      } else if (key.length() == 0) {
         throw new IllegalArgumentException("invalid key");
      } else {
         PatriciaST<Value>.Node p = this.head;
         PatriciaST<Value>.Node x = this.head;

         PatriciaST<Value>.Node g;
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
            PatriciaST<Value>.Node y = this.head;

            PatriciaST<Value>.Node z;
            do {
               z = y;
               if (safeBitTest(key, y.b)) {
                  y = y.right;
               } else {
                  y = y.left;
               }
            } while(y != x);

            if (x == p) {
               PatriciaST<Value>.Node c;
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
               PatriciaST<Value>.Node c;
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

   public boolean contains(String key) {
      return this.get(key) != null;
   }

   boolean isEmpty() {
      return this.count == 0;
   }

   int size() {
      return this.count;
   }

   public Iterable<String> keys() {
      Queue<String> queue = new Queue<String>();
      if (this.head.left != this.head) {
         this.keys(this.head.left, 0, queue);
      }

      if (this.head.right != this.head) {
         this.keys(this.head.right, 0, queue);
      }

      return queue;
   }

   private void keys(PatriciaST<Value>.Node x, int b, Queue<String> queue) {
      if (x.b > b) {
         this.keys(x.left, x.b, queue);
         queue.enqueue(x.key);
         this.keys(x.right, x.b, queue);
      }

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
      PatriciaST<Integer> st = new PatriciaST<Integer>();
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
         int[] v = new int[limitItem];
         StdOut.printf("Creating dataset (%d items)...\n", limitItem);

         for(int i = 0; i < limitItem; v[i] = i++) {
            a[i] = Integer.toString(i, 16);
         }

         StdOut.printf("Shuffling...\n");
         StdRandom.shuffle(v);
         StdOut.printf("Adding (%d items)...\n", limitItem);

         for(int i = 0; i < limitItem; ++i) {
            st.put(a[v[i]], v[i]);
         }

         int countKeys = 0;
         StdOut.printf("Iterating...\n");

         for(String key : st.keys()) {
            ++countKeys;
         }

         StdOut.printf("%d items iterated\n", countKeys);
         if (countKeys != limitItem) {
            ok = false;
         }

         if (countKeys != st.size()) {
            ok = false;
         }

         StdOut.printf("Shuffling...\n");
         StdRandom.shuffle(v);
         int limitDelete = limitItem / 2;
         StdOut.printf("Deleting (%d items)...\n", limitDelete);

         for(int i = 0; i < limitDelete; ++i) {
            st.delete(a[v[i]]);
         }

         countKeys = 0;
         StdOut.printf("Iterating...\n");

         for(String key : st.keys()) {
            ++countKeys;
         }

         StdOut.printf("%d items iterated\n", countKeys);
         if (countKeys != limitItem - limitDelete) {
            ok = false;
         }

         if (countKeys != st.size()) {
            ok = false;
         }

         int countDelete = 0;
         int countRemain = 0;
         StdOut.printf("Checking...\n");

         for(int i = 0; i < limitItem; ++i) {
            if (i < limitDelete) {
               if (!st.contains(a[v[i]])) {
                  ++countDelete;
               }
            } else {
               int val = (Integer)st.get(a[v[i]]);
               if (val == v[i]) {
                  ++countRemain;
               }
            }
         }

         StdOut.printf("%d items found and %d (deleted) items missing\n", countRemain, countDelete);
         if (countRemain + countDelete != limitItem) {
            ok = false;
         }

         if (countRemain != st.size()) {
            ok = false;
         }

         if (st.isEmpty()) {
            ok = false;
         }

         StdOut.printf("Deleting the rest (%d items)...\n", limitItem - countDelete);

         for(int i = countDelete; i < limitItem; ++i) {
            st.delete(a[v[i]]);
         }

         if (!st.isEmpty()) {
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
      private PatriciaST<Value>.Node left;
      private PatriciaST<Value>.Node right;
      private String key;
      private Value val;
      private int b;

      public Node(String key, Value val, int b) {
         this.key = key;
         this.val = val;
         this.b = b;
      }
   }
}
