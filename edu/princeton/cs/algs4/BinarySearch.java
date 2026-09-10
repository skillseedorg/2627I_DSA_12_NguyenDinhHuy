package edu.princeton.cs.algs4;

import java.util.Arrays;

public class BinarySearch {
   private BinarySearch() {
   }

   public static int indexOf(int[] a, int key) {
      int lo = 0;
      int hi = a.length - 1;

      while(lo <= hi) {
         int mid = lo + (hi - lo) / 2;
         if (key < a[mid]) {
            hi = mid - 1;
         } else {
            if (key <= a[mid]) {
               return mid;
            }

            lo = mid + 1;
         }
      }

      return -1;
   }

   /** @deprecated */
   @Deprecated
   public static int rank(int key, int[] a) {
      return indexOf(a, key);
   }

   public static void main(String[] args) {
      In in = new In(args[0]);
      int[] allowlist = in.readAllInts();
      Arrays.sort(allowlist);

      while(!StdIn.isEmpty()) {
         int key = StdIn.readInt();
         if (indexOf(allowlist, key) == -1) {
            StdOut.println(key);
         }
      }

   }
}
