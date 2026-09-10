package edu.princeton.cs.algs4;

public class LZW {
   private static final int R = 256;
   private static final int L = 4096;
   private static final int W = 12;

   private LZW() {
   }

   public static void compress() {
      String input = BinaryStdIn.readString();
      TST<Integer> st = new TST<Integer>();

      for(int i = 0; i < 256; ++i) {
         st.put("" + (char)i, i);
      }

      int t;
      for(int code = 257; input.length() > 0; input = input.substring(t)) {
         String s = st.longestPrefixOf(input);
         BinaryStdOut.write((int)(Integer)st.get(s), 12);
         t = s.length();
         if (t < input.length() && code < 4096) {
            st.put(input.substring(0, t + 1), code++);
         }
      }

      BinaryStdOut.write((int)256, 12);
      BinaryStdOut.close();
   }

   public static void expand() {
      String[] st = new String[4096];

      int i;
      for(i = 0; i < 256; ++i) {
         st[i] = "" + (char)i;
      }

      st[i++] = "";
      int codeword = BinaryStdIn.readInt(12);
      if (codeword != 256) {
         String val = st[codeword];

         while(true) {
            BinaryStdOut.write(val);
            codeword = BinaryStdIn.readInt(12);
            if (codeword == 256) {
               BinaryStdOut.close();
               return;
            }

            String s = st[codeword];
            if (i == codeword) {
               s = val + val.charAt(0);
            }

            if (i < 4096) {
               st[i++] = val + s.charAt(0);
            }

            val = s;
         }
      }
   }

   public static void main(String[] args) {
      if (args[0].equals("-")) {
         compress();
      } else {
         if (!args[0].equals("+")) {
            throw new IllegalArgumentException("Illegal command line argument");
         }

         expand();
      }

   }
}
