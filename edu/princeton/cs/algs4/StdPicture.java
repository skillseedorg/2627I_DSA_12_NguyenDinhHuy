package edu.princeton.cs.algs4;

public final class StdPicture {
   private static final int DEFAULT_SIZE = 512;
   private static Picture picture = new Picture(512, 512);

   private StdPicture() {
   }

   public static void init(int width, int height) {
      if (picture.isVisible()) {
         hide();
         picture = new Picture(width, height);
         show();
      } else {
         picture = new Picture(width, height);
      }

   }

   public static void read(String filename) {
      Picture newPicture = new Picture(filename);
      if (newPicture.width() == picture.width() && newPicture.height() == picture.height()) {
         for(int col = 0; col < picture.width(); ++col) {
            for(int row = 0; row < picture.height(); ++row) {
               picture.setARGB(col, row, newPicture.getARGB(col, row));
            }
         }
      } else if (picture.isVisible()) {
         hide();
         picture = newPicture;
         show();
      } else {
         picture = newPicture;
      }

   }

   public static boolean isVisible() {
      return picture.isVisible();
   }

   public static void show() {
      picture.show();
   }

   public static void hide() {
      picture.hide();
   }

   public static void pause(int t) {
      if (t < 0) {
         throw new IllegalArgumentException("argument must be non-negative");
      } else {
         try {
            Thread.sleep((long)t);
         } catch (InterruptedException var2) {
            System.out.println("Error sleeping");
         }

      }
   }

   public static int height() {
      return picture.height();
   }

   public static int width() {
      return picture.width();
   }

   public static int getAlpha(int col, int row) {
      int rgb = picture.getARGB(col, row);
      return rgb >> 24 & 255;
   }

   public static int getRed(int col, int row) {
      int rgb = picture.getARGB(col, row);
      return rgb >> 16 & 255;
   }

   public static int getGreen(int col, int row) {
      int rgb = picture.getARGB(col, row);
      return rgb >> 8 & 255;
   }

   public static int getBlue(int col, int row) {
      int rgb = picture.getARGB(col, row);
      return rgb >> 0 & 255;
   }

   public static int getARGB(int col, int row) {
      int argb = picture.getARGB(col, row);
      return argb;
   }

   public static void setRGB(int col, int row, int r, int g, int b) {
      validateComponent(r, "red");
      validateComponent(g, "green");
      validateComponent(b, "blue");
      int a = 255;
      int argb = a << 24 | r << 16 | g << 8 | b << 0;
      picture.setARGB(col, row, argb);
   }

   public static void setARGB(int col, int row, int a, int r, int g, int b) {
      validateComponent(a, "alpha");
      validateComponent(r, "red");
      validateComponent(g, "green");
      validateComponent(b, "blue");
      int argb = a << 24 | r << 16 | g << 8 | b << 0;
      picture.setARGB(col, row, argb);
   }

   public static void setARGB(int col, int row, int argb) {
      picture.setARGB(col, row, argb);
   }

   public static void setTitle(String title) {
      picture.setTitle(title);
   }

   public static void save(String filename) {
      picture.save(filename);
   }

   private static void validateComponent(int val, String name) {
      if (val < 0 || val >= 256) {
         throw new IllegalArgumentException(name + " must be between 0 and 255");
      }
   }

   public static void main(String[] args) {
      read(args[0]);
      System.out.printf("%d-by-%d\n", picture.width(), picture.height());
      show();
   }
}
