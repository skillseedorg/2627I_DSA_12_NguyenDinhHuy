package edu.princeton.cs.algs4;

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public final class GrayscalePicture implements ActionListener {
   private BufferedImage image;
   private JFrame frame;
   private String title;
   private boolean isOriginUpperLeft = true;
   private boolean isVisible = false;
   private final int width;
   private final int height;

   public GrayscalePicture(int width, int height) {
      if (width < 0) {
         throw new IllegalArgumentException("width must be non-negative");
      } else if (height < 0) {
         throw new IllegalArgumentException("height must be non-negative");
      } else {
         this.width = width;
         this.height = height;
         this.image = new BufferedImage(width, height, 1);
      }
   }

   public GrayscalePicture(GrayscalePicture picture) {
      if (picture == null) {
         throw new IllegalArgumentException("constructor argument is null");
      } else {
         this.width = picture.width();
         this.height = picture.height();
         this.image = new BufferedImage(this.width, this.height, 1);
         this.title = picture.title;
         this.isOriginUpperLeft = picture.isOriginUpperLeft;

         for(int col = 0; col < this.width(); ++col) {
            for(int row = 0; row < this.height(); ++row) {
               this.image.setRGB(col, row, picture.image.getRGB(col, row));
            }
         }

      }
   }

   public GrayscalePicture(String filename) {
      if (filename == null) {
         throw new IllegalArgumentException("constructor argument is null");
      } else {
         this.title = filename;

         try {
            File file = new File(filename);
            if (file.isFile()) {
               this.image = ImageIO.read(file);
            } else {
               URL url = this.getClass().getResource(filename);
               if (url == null) {
                  url = this.getClass().getClassLoader().getResource(filename);
               }

               if (url == null) {
                  URI uri = new URI(filename);
                  if (!uri.isAbsolute()) {
                     throw new IllegalArgumentException("could not read image: '" + filename + "'");
                  }

                  url = uri.toURL();
               }

               this.image = ImageIO.read(url);
            }

            if (this.image == null) {
               throw new IllegalArgumentException("could not read image: '" + filename + "'");
            } else {
               this.width = this.image.getWidth((ImageObserver)null);
               this.height = this.image.getHeight((ImageObserver)null);

               for(int col = 0; col < this.width; ++col) {
                  for(int row = 0; row < this.height; ++row) {
                     Color color = new Color(this.image.getRGB(col, row));
                     Color gray = toGray(color);
                     this.image.setRGB(col, row, gray.getRGB());
                  }
               }

            }
         } catch (URISyntaxException | IOException e) {
            throw new IllegalArgumentException("could not open image: " + filename, e);
         }
      }
   }

   private static Color toGray(Color color) {
      int r = color.getRed();
      int g = color.getGreen();
      int b = color.getBlue();
      int y = (int)Math.round(0.299 * (double)r + 0.587 * (double)g + 0.114 * (double)b);
      return new Color(y, y, y);
   }

   public JLabel getJLabel() {
      if (this.image == null) {
         return null;
      } else {
         ImageIcon icon = new ImageIcon(this.image);
         return new JLabel(icon);
      }
   }

   public void setOriginUpperLeft() {
      this.isOriginUpperLeft = true;
   }

   public void setOriginLowerLeft() {
      this.isOriginUpperLeft = false;
   }

   public void show() {
      if (this.frame == null) {
         this.frame = new JFrame();
         JMenuBar menuBar = new JMenuBar();
         JMenu menu = new JMenu("File");
         menuBar.add(menu);
         JMenuItem menuItem1 = new JMenuItem(" Save...   ");
         menuItem1.addActionListener(this);
         menuItem1.setAccelerator(KeyStroke.getKeyStroke(83, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
         menu.add(menuItem1);
         this.frame.setJMenuBar(menuBar);
         this.frame.setContentPane(this.getJLabel());
         this.frame.setDefaultCloseOperation(2);
         if (this.title == null) {
            this.frame.setTitle(this.width + "-by-" + this.height);
         } else {
            this.frame.setTitle(this.title);
         }

         this.frame.setResizable(false);
         this.frame.pack();
      }

      this.frame.setVisible(true);
      this.isVisible = true;
      this.frame.repaint();
   }

   public void hide() {
      if (this.frame != null) {
         this.isVisible = false;
         this.frame.setVisible(false);
      }

   }

   public boolean isVisible() {
      return this.isVisible;
   }

   public int height() {
      return this.height;
   }

   public int width() {
      return this.width;
   }

   private void validateRowIndex(int row) {
      if (row < 0 || row >= this.height()) {
         int var10002 = this.height() - 1;
         throw new IndexOutOfBoundsException("row index must be between 0 and " + var10002 + ": " + row);
      }
   }

   private void validateColumnIndex(int col) {
      if (col < 0 || col >= this.width()) {
         int var10002 = this.width() - 1;
         throw new IndexOutOfBoundsException("column index must be between 0 and " + var10002 + ": " + col);
      }
   }

   private void validateGrayscaleValue(int gray) {
      if (gray < 0 || gray >= 256) {
         throw new IllegalArgumentException("grayscale value must be between 0 and 255");
      }
   }

   public Color get(int col, int row) {
      this.validateColumnIndex(col);
      this.validateRowIndex(row);
      Color color = new Color(this.image.getRGB(col, row));
      return toGray(color);
   }

   public int getGrayscale(int col, int row) {
      this.validateColumnIndex(col);
      this.validateRowIndex(row);
      return this.isOriginUpperLeft ? this.image.getRGB(col, row) & 255 : this.image.getRGB(col, this.height - row - 1) & 255;
   }

   public void set(int col, int row, Color color) {
      this.validateColumnIndex(col);
      this.validateRowIndex(row);
      if (color == null) {
         throw new IllegalArgumentException("color argument is null");
      } else {
         Color gray = toGray(color);
         this.image.setRGB(col, row, gray.getRGB());
      }
   }

   public void setGrayscale(int col, int row, int gray) {
      this.validateColumnIndex(col);
      this.validateRowIndex(row);
      this.validateGrayscaleValue(gray);
      int rgb = gray | gray << 8 | gray << 16;
      if (this.isOriginUpperLeft) {
         this.image.setRGB(col, row, rgb);
      } else {
         this.image.setRGB(col, this.height - row - 1, rgb);
      }

   }

   public boolean equals(Object other) {
      if (other == this) {
         return true;
      } else if (other == null) {
         return false;
      } else if (other.getClass() != this.getClass()) {
         return false;
      } else {
         GrayscalePicture that = (GrayscalePicture)other;
         if (this.width() != that.width()) {
            return false;
         } else if (this.height() != that.height()) {
            return false;
         } else {
            for(int col = 0; col < this.width(); ++col) {
               for(int row = 0; row < this.height(); ++row) {
                  if (this.getGrayscale(col, row) != that.getGrayscale(col, row)) {
                     return false;
                  }
               }
            }

            return true;
         }
      }
   }

   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append(this.width + "-by-" + this.height + " grayscale picture (grayscale values given in hex)\n");

      for(int row = 0; row < this.height; ++row) {
         for(int col = 0; col < this.width; ++col) {
            int gray;
            if (this.isOriginUpperLeft) {
               gray = 255 & this.image.getRGB(col, row);
            } else {
               gray = 255 & this.image.getRGB(col, this.height - row - 1);
            }

            sb.append(String.format("%3d ", gray));
         }

         sb.append("\n");
      }

      return sb.toString().trim();
   }

   public int hashCode() {
      throw new UnsupportedOperationException("hashCode() is not supported because pictures are mutable");
   }

   public void save(String filename) {
      if (filename == null) {
         throw new IllegalArgumentException("argument to save() is null");
      } else {
         this.save(new File(filename));
         this.title = filename;
      }
   }

   public void save(File file) {
      if (file == null) {
         throw new IllegalArgumentException("argument to save() is null");
      } else {
         this.title = file.getName();
         if (this.frame != null) {
            this.frame.setTitle(this.title);
         }

         String suffix = this.title.substring(this.title.lastIndexOf(46) + 1);
         if (this.title.contains(".") && suffix.length() != 0) {
            try {
               if (!ImageIO.write(this.image, suffix, file)) {
                  BufferedImage imageRGB = new BufferedImage(this.width, this.height, 1);
                  imageRGB.createGraphics().drawImage(this.image, 0, 0, Color.WHITE, (ImageObserver)null);
                  if (!ImageIO.write(imageRGB, suffix, file)) {
                     throw new IllegalArgumentException("The filetype '" + suffix + "' is not supported");
                  }
               }
            } catch (IOException e) {
               throw new IllegalArgumentException("could not write file '" + this.title + "'", e);
            }
         } else {
            System.out.printf("Error: the filename '%s' has no file extension, such as .jpg or .png\n", this.title);
         }
      }
   }

   public void actionPerformed(ActionEvent event) {
      FileDialog chooser = new FileDialog(this.frame, "Use a .png or .jpg extension", 1);
      chooser.setVisible(true);
      String selectedDirectory = chooser.getDirectory();
      String selectedFilename = chooser.getFile();
      if (selectedDirectory != null && selectedFilename != null) {
         try {
            this.save(selectedDirectory + selectedFilename);
         } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
         }
      }

   }

   public static void main(String[] args) {
      GrayscalePicture picture = new GrayscalePicture(args[0]);
      StdOut.printf("%d-by-%d\n", picture.width(), picture.height());
      GrayscalePicture copy = new GrayscalePicture(picture);
      picture.show();
      copy.show();

      while(!StdIn.isEmpty()) {
         int row = StdIn.readInt();
         int col = StdIn.readInt();
         int gray = StdIn.readInt();
         picture.setGrayscale(row, col, gray);
         StdOut.println(picture.get(row, col));
         StdOut.println(picture.getGrayscale(row, col));
      }

   }
}
