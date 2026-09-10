package edu.princeton.cs.algs4;

public class QuickX {
   private static final int INSERTION_SORT_CUTOFF = 8;

   private QuickX() {
   }

   public static void sort(Comparable[] a) {
      sort(a, 0, a.length - 1);

      assert isSorted(a);

   }

   private static void sort(Comparable[] a, int lo, int hi) {
      if (hi > lo) {
         int n = hi - lo + 1;
         if (n <= 8) {
            Insertion.sort(a, lo, hi + 1);
         } else {
            int j = partition(a, lo, hi);
            sort(a, lo, j - 1);
            sort(a, j + 1, hi);
         }
      }
   }

   private static int partition(Comparable[] a, int lo, int hi) {
      int n = hi - lo + 1;
      int m = median3(a, lo, lo + n / 2, hi);
      exch(a, m, lo);
      int i = lo;
      int j = hi + 1;
      Comparable v = a[lo];

      do {
         ++i;
         if (!less(a[i], v)) {
            do {
               --j;
               if (!less(v, a[j])) {
                  while(i < j) {
                     exch(a, i, j);

                     do {
                        ++i;
                     } while(less(a[i], v));

                     while(true) {
                        --j;
                        if (!less(v, a[j])) {
                           break;
                        }
                     }
                  }

                  exch(a, lo, j);
                  return j;
               }
            } while(j != lo + 1);

            return lo;
         }
      } while(i != hi);

      exch(a, lo, hi);
      return hi;
   }

   private static int median3(Comparable[] a, int i, int j, int k) {
      return less(a[i], a[j]) ? (less(a[j], a[k]) ? j : (less(a[i], a[k]) ? k : i)) : (less(a[k], a[j]) ? j : (less(a[k], a[i]) ? k : i));
   }

   private static boolean less(Comparable v, Comparable w) {
      return v.compareTo(w) < 0;
   }

   private static void exch(Object[] a, int i, int j) {
      Object swap = a[i];
      a[i] = a[j];
      a[j] = swap;
   }

   private static boolean isSorted(Comparable[] a) {
      for(int i = 1; i < a.length; ++i) {
         if (less(a[i], a[i - 1])) {
            return false;
         }
      }

      return true;
   }

   private static void show(Comparable[] a) {
      for(int i = 0; i < a.length; ++i) {
         StdOut.println(a[i]);
      }

   }

   public static void main(String[] args) {
      String[] a = StdIn.readAllStrings();
      sort(a);

      assert isSorted(a);

      show(a);
   }
}
