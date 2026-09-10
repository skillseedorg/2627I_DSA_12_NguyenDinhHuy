package edu.princeton.cs.algs4;

public class LSD {
   private static final int BITS_PER_BYTE = 8;

   private LSD() {
   }

   public static void sort(String[] a, int w) {
      int n = a.length;
      int R = 256;
      String[] aux = new String[n];

      for(int d = w - 1; d >= 0; --d) {
         int[] count = new int[R + 1];

         for(int i = 0; i < n; ++i) {
            ++count[a[i].charAt(d) + 1];
         }

         for(int r = 0; r < R; ++r) {
            count[r + 1] += count[r];
         }

         for(int i = 0; i < n; ++i) {
            aux[count[a[i].charAt(d)]++] = a[i];
         }

         for(int i = 0; i < n; ++i) {
            a[i] = aux[i];
         }
      }

   }

   public static void sort(int[] a) {
      int BITS = 32;
      int R = 256;
      int MASK = 255;
      int w = 4;
      int n = a.length;
      int[] aux = new int[n];

      for(int d = 0; d < 4; ++d) {
         int[] count = new int[257];

         for(int i = 0; i < n; ++i) {
            int c = a[i] >> 8 * d & 255;
            ++count[c + 1];
         }

         for(int r = 0; r < 256; ++r) {
            count[r + 1] += count[r];
         }

         if (d == 3) {
            int shift1 = count[256] - count[128];
            int shift2 = count[128];

            for(int r = 0; r < 128; ++r) {
               count[r] += shift1;
            }

            for(int r = 128; r < 256; ++r) {
               count[r] -= shift2;
            }
         }

         for(int i = 0; i < n; ++i) {
            int c = a[i] >> 8 * d & 255;
            aux[count[c]++] = a[i];
         }

         int[] temp = a;
         a = aux;
         aux = temp;
      }

   }

   public static void main(String[] args) {
      String[] a = StdIn.readAllStrings();
      int n = a.length;
      int w = a[0].length();

      for(int i = 0; i < n; ++i) {
         assert a[i].length() == w : "Strings must have fixed length";
      }

      sort(a, w);

      for(int i = 0; i < n; ++i) {
         StdOut.println(a[i]);
      }

   }
}
