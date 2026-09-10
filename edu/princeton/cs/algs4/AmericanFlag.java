package edu.princeton.cs.algs4;

public class AmericanFlag {
   private static final int BITS_PER_BYTE = 8;
   private static final int BITS_PER_INT = 32;
   private static final int R = 256;
   private static final int CUTOFF = 15;

   private AmericanFlag() {
   }

   private static int charAt(String s, int d) {
      assert d >= 0 && d <= s.length();

      return d == s.length() ? -1 : s.charAt(d);
   }

   public static void sort(String[] a) {
      sort((String[])a, 0, a.length - 1);
   }

   public static void sort(String[] a, int lo, int hi) {
      Stack<Integer> st = new Stack<Integer>();
      int[] first = new int[258];
      int[] next = new int[258];
      int d = 0;
      st.push(lo);
      st.push(hi);
      st.push(d);

      while(!st.isEmpty()) {
         d = (Integer)st.pop();
         hi = (Integer)st.pop();
         lo = (Integer)st.pop();
         if (hi <= lo + 15) {
            insertion(a, lo, hi, d);
         } else {
            for(int i = lo; i <= hi; ++i) {
               int c = charAt(a[i], d) + 1;
               ++first[c + 1];
            }

            first[0] = lo;

            for(int c = 0; c <= 256; ++c) {
               first[c + 1] += first[c];
               if (c > 0 && first[c + 1] - 1 > first[c]) {
                  st.push(first[c]);
                  st.push(first[c + 1] - 1);
                  st.push(d + 1);
               }
            }

            for(int c = 0; c < 258; ++c) {
               next[c] = first[c];
            }

            for(int k = lo; k <= hi; ++k) {
               int c;
               for(c = charAt(a[k], d) + 1; first[c] > k; c = charAt(a[k], d) + 1) {
                  exch(a, k, next[c]++);
               }

               int var10002 = next[c]++;
            }

            for(int c = 0; c < 258; ++c) {
               first[c] = 0;
               next[c] = 0;
            }
         }
      }

   }

   private static void insertion(String[] a, int lo, int hi, int d) {
      for(int i = lo; i <= hi; ++i) {
         for(int j = i; j > lo && less(a[j], a[j - 1], d); --j) {
            exch(a, j, j - 1);
         }
      }

   }

   private static void exch(String[] a, int i, int j) {
      String temp = a[i];
      a[i] = a[j];
      a[j] = temp;
   }

   private static boolean less(String v, String w, int d) {
      for(int i = d; i < Math.min(v.length(), w.length()); ++i) {
         if (v.charAt(i) < w.charAt(i)) {
            return true;
         }

         if (v.charAt(i) > w.charAt(i)) {
            return false;
         }
      }

      return v.length() < w.length();
   }

   public static void sort(int[] a) {
      sort((int[])a, 0, a.length - 1);
   }

   private static void sort(int[] a, int lo, int hi) {
      Stack<Integer> st = new Stack<Integer>();
      int[] first = new int[257];
      int[] next = new int[257];
      int mask = 255;
      int d = 0;
      st.push(lo);
      st.push(hi);
      st.push(d);

      while(!st.isEmpty()) {
         d = (Integer)st.pop();
         hi = (Integer)st.pop();
         lo = (Integer)st.pop();
         if (hi <= lo + 15) {
            insertion(a, lo, hi, d);
         } else {
            int shift = 32 - 8 * d - 8;

            for(int i = lo; i <= hi; ++i) {
               int c = a[i] >> shift & mask;
               ++first[c + 1];
            }

            first[0] = lo;

            for(int c = 0; c < 256; ++c) {
               first[c + 1] += first[c];
               if (d < 3 && first[c + 1] - 1 > first[c]) {
                  st.push(first[c]);
                  st.push(first[c + 1] - 1);
                  st.push(d + 1);
               }
            }

            for(int c = 0; c < 257; ++c) {
               next[c] = first[c];
            }

            for(int k = lo; k <= hi; ++k) {
               int c;
               for(c = a[k] >> shift & mask; first[c] > k; c = a[k] >> shift & mask) {
                  exch(a, k, next[c]++);
               }

               int var10002 = next[c]++;
            }

            for(int c = 0; c < 257; ++c) {
               first[c] = 0;
               next[c] = 0;
            }
         }
      }

   }

   private static void insertion(int[] a, int lo, int hi, int d) {
      for(int i = lo; i <= hi; ++i) {
         for(int j = i; j > lo && less(a[j], a[j - 1], d); --j) {
            exch(a, j, j - 1);
         }
      }

   }

   private static void exch(int[] a, int i, int j) {
      int temp = a[i];
      a[i] = a[j];
      a[j] = temp;
   }

   private static boolean less(int v, int w, int d) {
      int mask = 255;

      for(int i = d; i < 4; ++i) {
         int shift = 32 - 8 * i - 8;
         int a = v >> shift & mask;
         int b = w >> shift & mask;
         if (a < b) {
            return true;
         }

         if (a > b) {
            return false;
         }
      }

      return false;
   }

   public static void main(String[] args) {
      if (args.length > 0 && args[0].equals("int")) {
         int[] a = StdIn.readAllInts();
         sort(a);

         for(int i = 0; i < a.length; ++i) {
            StdOut.println(a[i]);
         }
      } else {
         String[] a = StdIn.readAllStrings();
         sort(a);

         for(int i = 0; i < a.length; ++i) {
            StdOut.println(a[i]);
         }
      }

   }
}
