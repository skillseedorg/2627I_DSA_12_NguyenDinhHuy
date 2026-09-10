package edu.princeton.cs.algs4;

public class InplaceMSD {
   private static final int R = 256;
   private static final int CUTOFF = 15;

   private InplaceMSD() {
   }

   public static void sort(String[] a) {
      int n = a.length;
      sort(a, 0, n - 1, 0);
   }

   private static int charAt(String s, int d) {
      assert d >= 0 && d <= s.length();

      return d == s.length() ? -1 : s.charAt(d);
   }

   private static void sort(String[] a, int lo, int hi, int d) {
      if (hi <= lo + 15) {
         insertion(a, lo, hi, d);
      } else {
         int[] heads = new int[258];
         int[] tails = new int[257];

         for(int i = lo; i <= hi; ++i) {
            int c = charAt(a[i], d);
            ++heads[c + 2];
         }

         heads[0] = lo;

         for(int r = 0; r < 257; ++r) {
            heads[r + 1] += heads[r];
            tails[r] = heads[r + 1];
         }

         for(int r = 0; r < 257; ++r) {
            while(heads[r] < tails[r]) {
               for(int c = charAt(a[heads[r]], d); c + 1 != r; c = charAt(a[heads[r]], d)) {
                  int var10001 = heads[r];
                  int var10003 = c + 1;
                  int var10005 = heads[c + 1];
                  heads[var10003] = heads[c + 1] + 1;
                  exch(a, var10001, var10005);
               }

               int var10002 = heads[r]++;
            }
         }

         for(int r = 0; r < 256; ++r) {
            sort(a, tails[r], tails[r + 1] - 1, d + 1);
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

   public static void main(String[] args) {
      String[] a = StdIn.readAllStrings();
      int n = a.length;
      sort(a);

      for(int i = 0; i < n; ++i) {
         StdOut.println(a[i]);
      }

   }
}
