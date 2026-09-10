package edu.princeton.cs.algs4;

public class Huffman {
   private static final int R = 256;

   private Huffman() {
   }

   public static void compress() {
      String s = BinaryStdIn.readString();
      char[] input = s.toCharArray();
      int[] freq = new int[256];

      for(int i = 0; i < input.length; ++i) {
         ++freq[input[i]];
      }

      Node root = buildTrie(freq);
      String[] st = new String[256];
      buildCode(st, root, "");
      writeTrie(root);
      BinaryStdOut.write(input.length);

      for(int i = 0; i < input.length; ++i) {
         String code = st[input[i]];

         for(int j = 0; j < code.length(); ++j) {
            if (code.charAt(j) == '0') {
               BinaryStdOut.write(false);
            } else {
               if (code.charAt(j) != '1') {
                  throw new IllegalStateException("Illegal state");
               }

               BinaryStdOut.write(true);
            }
         }
      }

      BinaryStdOut.close();
   }

   private static Node buildTrie(int[] freq) {
      MinPQ<Node> pq = new MinPQ<Node>();

      for(char c = 0; c < 256; ++c) {
         if (freq[c] > 0) {
            pq.insert(new Node(c, freq[c], (Node)null, (Node)null));
         }
      }

      while(pq.size() > 1) {
         Node left = pq.delMin();
         Node right = pq.delMin();
         Node parent = new Node('\u0000', left.freq + right.freq, left, right);
         pq.insert(parent);
      }

      return pq.delMin();
   }

   private static void writeTrie(Node x) {
      if (x.isLeaf()) {
         BinaryStdOut.write(true);
         BinaryStdOut.write((char)x.ch, 8);
      } else {
         BinaryStdOut.write(false);
         writeTrie(x.left);
         writeTrie(x.right);
      }
   }

   private static void buildCode(String[] st, Node x, String s) {
      if (!x.isLeaf()) {
         buildCode(st, x.left, s + "0");
         buildCode(st, x.right, s + "1");
      } else {
         st[x.ch] = s;
      }

   }

   public static void expand() {
      Node root = readTrie();
      int length = BinaryStdIn.readInt();

      for(int i = 0; i < length; ++i) {
         Node x = root;

         while(!x.isLeaf()) {
            boolean bit = BinaryStdIn.readBoolean();
            if (bit) {
               x = x.right;
            } else {
               x = x.left;
            }
         }

         BinaryStdOut.write((char)x.ch, 8);
      }

      BinaryStdOut.close();
   }

   private static Node readTrie() {
      boolean isLeaf = BinaryStdIn.readBoolean();
      return isLeaf ? new Node(BinaryStdIn.readChar(), -1, (Node)null, (Node)null) : new Node('\u0000', -1, readTrie(), readTrie());
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

   private static class Node implements Comparable<Node> {
      private final char ch;
      private final int freq;
      private final Node left;
      private final Node right;

      Node(char ch, int freq, Node left, Node right) {
         this.ch = ch;
         this.freq = freq;
         this.left = left;
         this.right = right;
      }

      private boolean isLeaf() {
         assert this.left == null && this.right == null || this.left != null && this.right != null;

         return this.left == null && this.right == null;
      }

      public int compareTo(Node that) {
         return this.freq - that.freq;
      }
   }
}
