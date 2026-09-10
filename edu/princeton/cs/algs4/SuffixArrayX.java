package edu.princeton.cs.algs4;

public class SuffixArrayX {
   private static final int CUTOFF = 5;
   private final char[] text;
   private final int[] index;
   private final int n;

   public SuffixArrayX(String text) {
      this.n = text.length();
      text = text + "\u0000";
      this.text = text.toCharArray();
      this.index = new int[this.n];

      for(int i = 0; i < this.n; this.index[i] = i++) {
      }

      this.sort(0, this.n - 1, 0);
   }

   private void sort(int lo, int hi, int d) {
      if (hi <= lo + 5) {
         this.insertion(lo, hi, d);
      } else {
         int lt = lo;
         int gt = hi;
         char v = this.text[this.index[lo] + d];
         int i = lo + 1;

         while(i <= gt) {
            char t = this.text[this.index[i] + d];
            if (t < v) {
               this.exch(lt++, i++);
            } else if (t > v) {
               this.exch(i, gt--);
            } else {
               ++i;
            }
         }

         this.sort(lo, lt - 1, d);
         if (v > 0) {
            this.sort(lt, gt, d + 1);
         }

         this.sort(gt + 1, hi, d);
      }
   }

   private void insertion(int lo, int hi, int d) {
      for(int i = lo; i <= hi; ++i) {
         for(int j = i; j > lo && this.less(this.index[j], this.index[j - 1], d); --j) {
            this.exch(j, j - 1);
         }
      }

   }

   private boolean less(int i, int j, int d) {
      if (i == j) {
         return false;
      } else {
         i += d;

         for(j += d; i < this.n && j < this.n; ++j) {
            if (this.text[i] < this.text[j]) {
               return true;
            }

            if (this.text[i] > this.text[j]) {
               return false;
            }

            ++i;
         }

         return i > j;
      }
   }

   private void exch(int i, int j) {
      int swap = this.index[i];
      this.index[i] = this.index[j];
      this.index[j] = swap;
   }

   public int length() {
      return this.n;
   }

   public int index(int i) {
      if (i >= 0 && i < this.n) {
         return this.index[i];
      } else {
         throw new IllegalArgumentException();
      }
   }

   public int lcp(int i) {
      if (i >= 1 && i < this.n) {
         return this.lcp(this.index[i], this.index[i - 1]);
      } else {
         throw new IllegalArgumentException();
      }
   }

   private int lcp(int i, int j) {
      int length;
      for(length = 0; i < this.n && j < this.n; ++length) {
         if (this.text[i] != this.text[j]) {
            return length;
         }

         ++i;
         ++j;
      }

      return length;
   }

   public String select(int i) {
      if (i >= 0 && i < this.n) {
         return new String(this.text, this.index[i], this.n - this.index[i]);
      } else {
         throw new IllegalArgumentException();
      }
   }

   public int rank(String query) {
      int lo = 0;
      int hi = this.n - 1;

      while(lo <= hi) {
         int mid = lo + (hi - lo) / 2;
         int cmp = this.compare(query, this.index[mid]);
         if (cmp < 0) {
            hi = mid - 1;
         } else {
            if (cmp <= 0) {
               return mid;
            }

            lo = mid + 1;
         }
      }

      return lo;
   }

   private int compare(String query, int i) {
      int m = query.length();

      int j;
      for(j = 0; i < this.n && j < m; ++j) {
         if (query.charAt(j) != this.text[i]) {
            return query.charAt(j) - this.text[i];
         }

         ++i;
      }

      if (i < this.n) {
         return -1;
      } else {
         return j < m ? 1 : 0;
      }
   }

   public static void main(String[] args) {
      String s = StdIn.readAll().replaceAll("\n", " ").trim();
      SuffixArrayX suffix1 = new SuffixArrayX(s);
      SuffixArray suffix2 = new SuffixArray(s);
      boolean check = true;

      for(int i = 0; check && i < s.length(); ++i) {
         if (suffix1.index(i) != suffix2.index(i)) {
            StdOut.println("suffix1(" + i + ") = " + suffix1.index(i));
            StdOut.println("suffix2(" + i + ") = " + suffix2.index(i));
            String var10000 = s.substring(suffix1.index(i), Math.min(suffix1.index(i) + 50, s.length()));
            String ith = "\"" + var10000 + "\"";
            var10000 = s.substring(suffix2.index(i), Math.min(suffix2.index(i) + 50, s.length()));
            String jth = "\"" + var10000 + "\"";
            StdOut.println(ith);
            StdOut.println(jth);
            check = false;
         }
      }

      StdOut.println("  i ind lcp rnk  select");
      StdOut.println("---------------------------");

      for(int i = 0; i < s.length(); ++i) {
         int index = suffix2.index(i);
         String var14 = s.substring(index, Math.min(index + 50, s.length()));
         String ith = "\"" + var14 + "\"";
         int rank = suffix2.rank(s.substring(index));

         assert s.substring(index).equals(suffix2.select(i));

         if (i == 0) {
            StdOut.printf("%3d %3d %3s %3d  %s\n", i, index, "-", rank, ith);
         } else {
            int lcp = suffix2.lcp(i);
            StdOut.printf("%3d %3d %3d %3d  %s\n", i, index, lcp, rank, ith);
         }
      }

   }
}
