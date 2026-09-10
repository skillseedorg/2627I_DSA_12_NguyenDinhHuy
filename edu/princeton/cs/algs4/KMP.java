package edu.princeton.cs.algs4;

public class KMP {
   private final int R;
   private final int m;
   private int[][] dfa;

   public KMP(String pat) {
      this.R = 256;
      this.m = pat.length();
      this.dfa = new int[this.R][this.m];
      this.dfa[pat.charAt(0)][0] = 1;
      int x = 0;

      for(int j = 1; j < this.m; ++j) {
         for(int c = 0; c < this.R; ++c) {
            this.dfa[c][j] = this.dfa[c][x];
         }

         this.dfa[pat.charAt(j)][j] = j + 1;
         x = this.dfa[pat.charAt(j)][x];
      }

   }

   public KMP(char[] pattern, int R) {
      this.R = R;
      this.m = pattern.length;
      int m = pattern.length;
      this.dfa = new int[R][m];
      this.dfa[pattern[0]][0] = 1;
      int x = 0;

      for(int j = 1; j < m; ++j) {
         for(int c = 0; c < R; ++c) {
            this.dfa[c][j] = this.dfa[c][x];
         }

         this.dfa[pattern[j]][j] = j + 1;
         x = this.dfa[pattern[j]][x];
      }

   }

   public int search(String txt) {
      int n = txt.length();
      int i = 0;

      int j;
      for(j = 0; i < n && j < this.m; ++i) {
         j = this.dfa[txt.charAt(i)][j];
      }

      return j == this.m ? i - this.m : n;
   }

   public int search(char[] text) {
      int n = text.length;
      int i = 0;

      int j;
      for(j = 0; i < n && j < this.m; ++i) {
         j = this.dfa[text[i]][j];
      }

      return j == this.m ? i - this.m : n;
   }

   public static void main(String[] args) {
      String pat = args[0];
      String txt = args[1];
      char[] pattern = pat.toCharArray();
      char[] text = txt.toCharArray();
      KMP kmp1 = new KMP(pat);
      int offset1 = kmp1.search(txt);
      KMP kmp2 = new KMP(pattern, 256);
      int offset2 = kmp2.search(text);
      StdOut.println("text:    " + txt);
      StdOut.print("pattern: ");

      for(int i = 0; i < offset1; ++i) {
         StdOut.print(" ");
      }

      StdOut.println(pat);
      StdOut.print("pattern: ");

      for(int i = 0; i < offset2; ++i) {
         StdOut.print(" ");
      }

      StdOut.println(pat);
   }
}
