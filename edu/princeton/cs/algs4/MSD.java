package edu.princeton.cs.algs4;

public class MSD {
   private static final int BITS_PER_BYTE = 8;
   private static final int BITS_PER_INT = 32;
   private static final int R = 256;
   private static final int CUTOFF = 15;

   private MSD() {
   }

   public static void sort(String[] a) {
      int n = a.length;
      String[] aux = new String[n];
      sort((String[])a, 0, n - 1, 0, (String[])aux);
   }

   private static int charAt(String s, int d) {
      assert d >= 0 && d <= s.length();

      return d == s.length() ? -1 : s.charAt(d);
   }

   private static void sort(String[] a, int lo, int hi, int d, String[] aux) {
      if (hi <= lo + 15) {
         insertion(a, lo, hi, d);
      } else {
         int[] count = new int[258];

         for(int i = lo; i <= hi; ++i) {
            int c = charAt(a[i], d);
            ++count[c + 2];
         }

         for(int r = 0; r < 257; ++r) {
            count[r + 1] += count[r];
         }

         for(int i = lo; i <= hi; ++i) {
            int c = charAt(a[i], d);
            int var10002 = c + 1;
            int var10004 = count[c + 1];
            count[var10002] = count[c + 1] + 1;
            aux[var10004] = a[i];
         }

         for(int i = lo; i <= hi; ++i) {
            a[i] = aux[i - lo];
         }

         for(int r = 0; r < 256; ++r) {
            sort(a, lo + count[r], lo + count[r + 1] - 1, d + 1, aux);
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
      int n = a.length;
      int[] aux = new int[n];
      sort((int[])a, 0, n - 1, 0, (int[])aux);
   }

   private static void sort(int[] a, int lo, int hi, int d, int[] aux) {
      if (hi <= lo + 15) {
         insertion(a, lo, hi);
      } else {
         int[] count = new int[257];
         int mask = 255;
         int shift = 32 - 8 * d - 8;

         for(int i = lo; i <= hi; ++i) {
            int c = a[i] >> shift & mask;
            ++count[c + 1];
         }

         for(int r = 0; r < 256; ++r) {
            count[r + 1] += count[r];
         }

         if (d == 0) {
            int shift1 = count[256] - count[128];
            int shift2 = count[128];
            count[256] = shift1 + count[1];

            for(int r = 0; r < 128; ++r) {
               count[r] += shift1;
            }

            for(int r = 128; r < 256; ++r) {
               count[r] -= shift2;
            }
         }

         for(int i = lo; i <= hi; ++i) {
            int c = a[i] >> shift & mask;
            aux[count[c]++] = a[i];
         }

         for(int i = lo; i <= hi; ++i) {
            a[i] = aux[i - lo];
         }

         if (d != 3) {
            if (d == 0 && count[128] > 0) {
               sort(a, lo, lo + count[128] - 1, d + 1, aux);
            }

            if (d != 0 && count[0] > 0) {
               sort(a, lo, lo + count[0] - 1, d + 1, aux);
            }

            for(int r = 0; r < 256; ++r) {
               if (count[r + 1] > count[r]) {
                  sort(a, lo + count[r], lo + count[r + 1] - 1, d + 1, aux);
               }
            }

         }
      }
   }

   private static void insertion(int[] a, int lo, int hi) {
      for(int i = lo; i <= hi; ++i) {
         for(int j = i; j > lo && a[j] < a[j - 1]; --j) {
            exch(a, j, j - 1);
         }
      }

   }

   private static void exch(int[] a, int i, int j) {
      int temp = a[i];
      a[i] = a[j];
      a[j] = temp;
   }

   public static void main(String[] args) {
      String[] a = StdIn.readAllStrings();
      int n = a.length;
      sort(a);

      for(int i = 0; i < n; ++i) {
         StdOut.println(a[i]);
      }

   }
}
