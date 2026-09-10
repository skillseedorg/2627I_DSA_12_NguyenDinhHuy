package edu.princeton.cs.algs4;

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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

public final class Picture implements ActionListener {
   private BufferedImage image;
   private JFrame jframe;
   private String title;
   private boolean isOriginUpperLeft = true;
   private boolean isVisible = false;
   private boolean isDisposed = false;
   private final int width;
   private final int height;

   public Picture(int width, int height) {
      if (width <= 0) {
         throw new IllegalArgumentException("width must be positive");
      } else if (height <= 0) {
         throw new IllegalArgumentException("height must be positive");
      } else {
         this.width = width;
         this.height = height;
         this.title = width + "-by-" + height;
         this.image = new BufferedImage(width, height, 2);
      }
   }

   public Picture(Picture picture) {
      if (picture == null) {
         throw new IllegalArgumentException("constructor argument is null");
      } else {
         this.width = picture.width();
         this.height = picture.height();
         this.image = new BufferedImage(this.width, this.height, 2);
         this.title = picture.title;
         this.isOriginUpperLeft = picture.isOriginUpperLeft;

         for(int col = 0; col < this.width(); ++col) {
            for(int row = 0; row < this.height(); ++row) {
               this.image.setRGB(col, row, picture.image.getRGB(col, row));
            }
         }

      }
   }

   public Picture(String filename) {
      if (filename == null) {
         throw new IllegalArgumentException("constructor argument is null");
      } else if (filename.length() == 0) {
         throw new IllegalArgumentException("constructor argument is the empty string");
      } else {
         this.title = filename;

         try {
            File file = new File(filename);
            if (file.isFile()) {
               this.title = file.getName();
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
               if (this.image.getType() != 2) {
                  BufferedImage imageARGB = new BufferedImage(this.width, this.height, 2);
                  imageARGB.createGraphics().drawImage(this.image, 0, 0, (ImageObserver)null);
                  this.image = imageARGB;
               }

            }
         } catch (URISyntaxException | IOException e) {
            throw new IllegalArgumentException("could not open image: " + filename, e);
         }
      }
   }

   public Picture(File file) {
      if (file == null) {
         throw new IllegalArgumentException("constructor argument is null");
      } else {
         try {
            this.image = ImageIO.read(file);
            this.width = this.image.getWidth((ImageObserver)null);
            this.height = this.image.getHeight((ImageObserver)null);
            this.title = file.getName();
            if (this.image.getType() != 1) {
               BufferedImage imageARGB = new BufferedImage(this.width, this.height, 2);
               imageARGB.createGraphics().drawImage(this.image, 0, 0, (ImageObserver)null);
               this.image = imageARGB;
            }

         } catch (IOException ioe) {
            throw new IllegalArgumentException("could not open file: " + file, ioe);
         }
      }
   }

   private JFrame createGUI() {
      JFrame frame = new JFrame();
      JMenuBar menuBar = new JMenuBar();
      JMenu menu = new JMenu("File");
      menuBar.add(menu);
      JMenuItem menuItem1 = new JMenuItem(" Save...   ");
      menuItem1.addActionListener(this);
      menuItem1.setAccelerator(KeyStroke.getKeyStroke(83, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
      menu.add(menuItem1);
      frame.setJMenuBar(menuBar);
      frame.setContentPane(this.getJLabel());
      frame.setDefaultCloseOperation(2);
      frame.setTitle(this.title);
      frame.setResizable(false);
      frame.pack();
      frame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent event) {
            Picture.this.isVisible = false;
            Picture.this.isDisposed = true;
            super.windowClosing(event);
         }
      });
      return frame;
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
      if (this.jframe == null && !this.isDisposed) {
         this.jframe = this.createGUI();
         this.isVisible = true;
         this.jframe.setVisible(true);
         this.jframe.repaint();
      }

      if (this.jframe != null && !this.isDisposed) {
         this.isVisible = true;
         this.jframe.setVisible(true);
         this.jframe.repaint();
      }

   }

   public void hide() {
      if (this.jframe != null) {
         this.isVisible = false;
         this.jframe.setVisible(false);
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

   public Color get(int col, int row) {
      this.validateColumnIndex(col);
      this.validateRowIndex(row);
      int argb = this.getARGB(col, row);
      return new Color(argb, true);
   }

   public int getARGB(int col, int row) {
      this.validateColumnIndex(col);
      this.validateRowIndex(row);
      return this.isOriginUpperLeft ? this.image.getRGB(col, row) : this.image.getRGB(col, this.height - row - 1);
   }

   public void set(int col, int row, Color color) {
      this.validateColumnIndex(col);
      this.validateRowIndex(row);
      if (color == null) {
         throw new IllegalArgumentException("color argument is null");
      } else {
         int argb = color.getRGB();
         this.setARGB(col, row, argb);
      }
   }

   public void setARGB(int col, int row, int argb) {
      this.validateColumnIndex(col);
      this.validateRowIndex(row);
      if (this.isOriginUpperLeft) {
         this.image.setRGB(col, row, argb);
      } else {
         this.image.setRGB(col, this.height - row - 1, argb);
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
         Picture that = (Picture)other;
         if (this.width() != that.width()) {
            return false;
         } else if (this.height() != that.height()) {
            return false;
         } else {
            for(int col = 0; col < this.width(); ++col) {
               for(int row = 0; row < this.height(); ++row) {
                  if (this.getARGB(col, row) != that.getARGB(col, row)) {
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
      sb.append(this.width + "-by-" + this.height + " picture (RGB values given in hex)\n");

      for(int row = 0; row < this.height; ++row) {
         for(int col = 0; col < this.width; ++col) {
            int rgb;
            if (this.isOriginUpperLeft) {
               rgb = this.image.getRGB(col, row);
            } else {
               rgb = this.image.getRGB(col, this.height - row - 1);
            }

            sb.append(String.format("#%06X ", rgb & 16777215));
         }

         sb.append("\n");
      }

      return sb.toString().trim();
   }

   public int hashCode() {
      throw new UnsupportedOperationException("hashCode() is not supported because pictures are mutable");
   }

   public void setTitle(String title) {
      if (title == null) {
         throw new IllegalArgumentException("title is null");
      } else {
         this.title = title;
      }
   }

   private boolean hasAlpha() {
      for(int col = 0; col < this.width; ++col) {
         for(int row = 0; row < this.height; ++row) {
            int argb = this.image.getRGB(col, row);
            int alpha = argb >> 24 & 255;
            if (alpha != 255) {
               return true;
            }
         }
      }

      return false;
   }

   public void save(String filename) {
      if (filename == null) {
         throw new IllegalArgumentException("argument to save() is null");
      } else if (filename.length() == 0) {
         throw new IllegalArgumentException("argument to save() is the empty string");
      } else {
         File file = new File(filename);
         this.save(file);
      }
   }

   public void save(File file) {
      if (file == null) {
         throw new IllegalArgumentException("argument to save() is null");
      } else {
         this.title = file.getName();
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
            throw new IllegalArgumentException("The filename '" + this.title + "' has no filetype extension, such as .jpg or .png");
         }
      }
   }

   public void actionPerformed(ActionEvent event) {
      FileDialog chooser = new FileDialog(this.jframe, "The filetype extension must be either .jpg or .png", 1);
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
      Picture picture = new Picture(args[0]);
      System.out.printf("%d-by-%d\n", picture.width(), picture.height());
      picture.show();
   }
}
