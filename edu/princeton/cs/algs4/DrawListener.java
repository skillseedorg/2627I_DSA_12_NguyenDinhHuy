package edu.princeton.cs.algs4;

public interface DrawListener {
   default void mousePressed(double x, double y) {
   }

   default void mouseDragged(double x, double y) {
   }

   default void mouseReleased(double x, double y) {
   }

   default void mouseClicked(double x, double y) {
   }

   default void keyTyped(char c) {
   }

   default void keyPressed(int keycode) {
   }

   default void keyReleased(int keycode) {
   }

   default void update() {
   }
}
