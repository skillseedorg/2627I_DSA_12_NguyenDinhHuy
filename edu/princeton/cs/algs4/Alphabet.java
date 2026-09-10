package edu.princeton.cs.algs4;

public class Alphabet {
   public static final Alphabet BINARY = new Alphabet("01");
   public static final Alphabet OCTAL = new Alphabet("01234567");
   public static final Alphabet DECIMAL = new Alphabet("0123456789");
   public static final Alphabet HEXADECIMAL = new Alphabet("0123456789ABCDEF");
   public static final Alphabet DNA = new Alphabet("ACGT");
   public static final Alphabet LOWERCASE = new Alphabet("abcdefghijklmnopqrstuvwxyz");
   public static final Alphabet UPPERCASE = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
   public static final Alphabet PROTEIN = new Alphabet("ACDEFGHIKLMNPQRSTVWY");
   public static final Alphabet BASE64 = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/");
   public static final Alphabet ASCII = new Alphabet(128);
   public static final Alphabet EXTENDED_ASCII = new Alphabet(256);
   public static final Alphabet UNICODE16 = new Alphabet(65536);
   private char[] alphabet;
   private int[] inverse;
   private final int R;

   public Alphabet(String alpha) {
      boolean[] unicode = new boolean['\uffff'];

      for(int i = 0; i < alpha.length(); ++i) {
         char c = alpha.charAt(i);
         if (unicode[c]) {
            throw new IllegalArgumentException("Illegal alphabet: repeated character = '" + c + "'");
         }

         unicode[c] = true;
      }

      this.alphabet = alpha.toCharArray();
      this.R = alpha.length();
      this.inverse = new int['\uffff'];

      for(int i = 0; i < this.inverse.length; ++i) {
         this.inverse[i] = -1;
      }

      for(int c = 0; c < this.R; this.inverse[this.alphabet[c]] = c++) {
      }

   }

   private Alphabet(int radix) {
      this.R = radix;
      this.alphabet = new char[this.R];
      this.inverse = new int[this.R];

      for(int i = 0; i < this.R; ++i) {
         this.alphabet[i] = (char)i;
      }

      for(int i = 0; i < this.R; this.inverse[i] = i++) {
      }

   }

   public Alphabet() {
      this(256);
   }

   public boolean contains(char c) {
      return this.inverse[c] != -1;
   }

   /** @deprecated */
   @Deprecated
   public int R() {
      return this.R;
   }

   public int radix() {
      return this.R;
   }

   public int lgR() {
      int lgR = 0;

      for(int t = this.R - 1; t >= 1; t /= 2) {
         ++lgR;
      }

      return lgR;
   }

   public int toIndex(char c) {
      if (c < this.inverse.length && this.inverse[c] != -1) {
         return this.inverse[c];
      } else {
         throw new IllegalArgumentException("Character " + c + " not in alphabet");
      }
   }

   public int[] toIndices(String s) {
      char[] source = s.toCharArray();
      int[] target = new int[s.length()];

      for(int i = 0; i < source.length; ++i) {
         target[i] = this.toIndex(source[i]);
      }

      return target;
   }

   public char toChar(int index) {
      if (index >= 0 && index < this.R) {
         return this.alphabet[index];
      } else {
         throw new IllegalArgumentException("index must be between 0 and " + this.R + ": " + index);
      }
   }

   public String toChars(int[] indices) {
      StringBuilder s = new StringBuilder(indices.length);

      for(int i = 0; i < indices.length; ++i) {
         s.append(this.toChar(indices[i]));
      }

      return s.toString();
   }

   public static void main(String[] args) {
      int[] encoded1 = BASE64.toIndices("NowIsTheTimeForAllGoodMen");
      String decoded1 = BASE64.toChars(encoded1);
      StdOut.println(decoded1);
      int[] encoded2 = DNA.toIndices("AACGAACGGTTTACCCCG");
      String decoded2 = DNA.toChars(encoded2);
      StdOut.println(decoded2);
      int[] encoded3 = DECIMAL.toIndices("01234567890123456789");
      String decoded3 = DECIMAL.toChars(encoded3);
      StdOut.println(decoded3);
   }
}
