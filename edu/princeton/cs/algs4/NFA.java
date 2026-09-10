package edu.princeton.cs.algs4;

public class NFA {
   private Digraph graph;
   private String regexp;
   private final int m;

   public NFA(String regexp) {
      this.regexp = regexp;
      this.m = regexp.length();
      Stack<Integer> ops = new Stack<Integer>();
      this.graph = new Digraph(this.m + 1);

      for(int i = 0; i < this.m; ++i) {
         int lp = i;
         if (regexp.charAt(i) != '(' && regexp.charAt(i) != '|') {
            if (regexp.charAt(i) == ')') {
               int or = (Integer)ops.pop();
               if (regexp.charAt(or) == '|') {
                  lp = (Integer)ops.pop();
                  this.graph.addEdge(lp, or + 1);
                  this.graph.addEdge(or, i);
               } else if (regexp.charAt(or) == '(') {
                  lp = or;
               } else {
                  assert false;
               }
            }
         } else {
            ops.push(i);
         }

         if (i < this.m - 1 && regexp.charAt(i + 1) == '*') {
            this.graph.addEdge(lp, i + 1);
            this.graph.addEdge(i + 1, lp);
         }

         if (regexp.charAt(i) == '(' || regexp.charAt(i) == '*' || regexp.charAt(i) == ')') {
            this.graph.addEdge(i, i + 1);
         }
      }

      if (ops.size() != 0) {
         throw new IllegalArgumentException("Invalid regular expression");
      }
   }

   public boolean recognizes(String txt) {
      DirectedDFS dfs = new DirectedDFS(this.graph, 0);
      Bag<Integer> pc = new Bag<Integer>();

      for(int v = 0; v < this.graph.V(); ++v) {
         if (dfs.marked(v)) {
            pc.add(v);
         }
      }

      for(int i = 0; i < txt.length(); ++i) {
         if (txt.charAt(i) == '*' || txt.charAt(i) == '|' || txt.charAt(i) == '(' || txt.charAt(i) == ')') {
            char var10002 = txt.charAt(i);
            throw new IllegalArgumentException("text contains the metacharacter '" + var10002 + "'");
         }

         Bag<Integer> match = new Bag<Integer>();

         for(int v : pc) {
            if (v != this.m && (this.regexp.charAt(v) == txt.charAt(i) || this.regexp.charAt(v) == '.')) {
               match.add(v + 1);
            }
         }

         if (!match.isEmpty()) {
            dfs = new DirectedDFS(this.graph, match);
            pc = new Bag<Integer>();

            for(int v = 0; v < this.graph.V(); ++v) {
               if (dfs.marked(v)) {
                  pc.add(v);
               }
            }

            if (pc.size() == 0) {
               return false;
            }
         }
      }

      for(int v : pc) {
         if (v == this.m) {
            return true;
         }
      }

      return false;
   }

   public static void main(String[] args) {
      String regexp = "(" + args[0] + ")";
      String txt = args[1];
      NFA nfa = new NFA(regexp);
      StdOut.println(nfa.recognizes(txt));
   }
}
