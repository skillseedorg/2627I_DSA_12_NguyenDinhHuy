package edu.princeton.cs.algs4;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeSet;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public final class StdDraw implements ActionListener, MouseListener, MouseMotionListener, KeyListener {
   public static final Color AQUA = new Color(0, 255, 255);
   public static final Color BLACK;
   public static final Color BLUE;
   public static final Color CYAN;
   public static final Color FUSCIA;
   public static final Color DARK_GRAY;
   public static final Color GRAY;
   public static final Color GREEN;
   public static final Color LIGHT_GRAY;
   public static final Color LIME;
   public static final Color MAGENTA;
   public static final Color MAROON;
   public static final Color NAVY;
   public static final Color OLIVE;
   public static final Color ORANGE;
   public static final Color PINK;
   public static final Color PURPLE;
   public static final Color RED;
   public static final Color SILVER;
   public static final Color TEAL;
   public static final Color WHITE;
   public static final Color YELLOW;
   public static final Color TRANSPARENT;
   public static final Color BOOK_BLUE;
   public static final Color BOOK_LIGHT_BLUE;
   public static final Color BOOK_RED;
   public static final Color PRINCETON_ORANGE;
   private static final Color DEFAULT_PEN_COLOR;
   private static final Color DEFAULT_BACKGROUND_COLOR;
   private static Color penColor;
   private static Color backgroundColor;
   private static final String DEFAULT_WINDOW_TITLE = "Standard Draw";
   private static String windowTitle;
   private static final int DEFAULT_SIZE = 512;
   private static int width;
   private static int height;
   private static final double DEFAULT_PEN_RADIUS = 0.002;
   private static double penRadius;
   private static boolean defer;
   private static final double BORDER = (double)0.0F;
   private static final double DEFAULT_XMIN = (double)0.0F;
   private static final double DEFAULT_XMAX = (double)1.0F;
   private static final double DEFAULT_YMIN = (double)0.0F;
   private static final double DEFAULT_YMAX = (double)1.0F;
   private static double xmin;
   private static double xmax;
   private static double ymin;
   private static double ymax;
   private static final Object MOUSE_LOCK;
   private static final Object KEY_LOCK;
   private static final Font DEFAULT_FONT;
   private static Font font;
   private static BufferedImage offscreenImage;
   private static BufferedImage onscreenImage;
   private static Graphics2D offscreen;
   private static Graphics2D onscreen;
   private static StdDraw std;
   private static JFrame frame;
   private static boolean isJFrameVisible;
   private static boolean isMousePressed;
   private static double mouseX;
   private static double mouseY;
   private static LinkedList<Character> keysTyped;
   private static TreeSet<Integer> keysDown;

   private StdDraw() {
   }

   public static void setVisible(boolean isVisible) {
      isJFrameVisible = isVisible;
      frame.setVisible(isVisible);
   }

   public static void setCanvasSize() {
      setCanvasSize(512, 512);
   }

   public static void setCanvasSize(int canvasWidth, int canvasHeight) {
      if (canvasWidth <= 0) {
         throw new IllegalArgumentException("width must be positive");
      } else if (canvasHeight <= 0) {
         throw new IllegalArgumentException("height must be positive");
      } else {
         width = canvasWidth;
         height = canvasHeight;
         initCanvas();
         initGUI();
      }
   }

   private static void initCanvas() {
      offscreenImage = new BufferedImage(2 * width, 2 * height, 2);
      onscreenImage = new BufferedImage(2 * width, 2 * height, 2);
      offscreen = offscreenImage.createGraphics();
      onscreen = onscreenImage.createGraphics();
      offscreen.scale((double)2.0F, (double)2.0F);
      offscreen.setBackground(DEFAULT_BACKGROUND_COLOR);
      offscreen.clearRect(0, 0, width, height);
      onscreen.setBackground(DEFAULT_BACKGROUND_COLOR);
      onscreen.clearRect(0, 0, 2 * width, 2 * height);
      offscreen.setColor(DEFAULT_PEN_COLOR);
      RenderingHints hints = new RenderingHints((Map)null);
      hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
      offscreen.addRenderingHints(hints);
   }

   private static void initGUI() {
      if (frame == null) {
         frame = new JFrame();
         frame.addKeyListener(std);
         frame.setFocusTraversalKeysEnabled(false);
         frame.setResizable(false);
         frame.setDefaultCloseOperation(3);
         frame.setTitle(windowTitle);
         frame.setJMenuBar(createMenuBar());
      }

      RetinaImageIcon icon = new RetinaImageIcon(onscreenImage);
      JLabel draw = new JLabel(icon);
      draw.addMouseListener(std);
      draw.addMouseMotionListener(std);
      frame.setContentPane(draw);
      frame.pack();
      frame.requestFocusInWindow();
      frame.setVisible(false);
   }

   private static JMenuBar createMenuBar() {
      JMenuBar menuBar = new JMenuBar();
      JMenu menu = new JMenu("File");
      menuBar.add(menu);
      JMenuItem menuItem1 = new JMenuItem(" Save...   ");
      menuItem1.addActionListener(std);
      menuItem1.setAccelerator(KeyStroke.getKeyStroke(83, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
      menu.add(menuItem1);
      return menuBar;
   }

   public static void close() {
      frame.dispose();
   }

   private static void validate(double x, String name) {
      if (Double.isNaN(x)) {
         throw new IllegalArgumentException(name + " is NaN");
      } else if (Double.isInfinite(x)) {
         throw new IllegalArgumentException(name + " is infinite");
      }
   }

   private static void validateNonnegative(double x, String name) {
      if (x < (double)0.0F) {
         throw new IllegalArgumentException(name + " negative");
      }
   }

   private static void validateNotNull(Object x, String name) {
      if (x == null) {
         throw new IllegalArgumentException(name + " is null");
      }
   }

   public static void setTitle(String title) {
      validateNotNull(title, "title");
      frame.setTitle(title);
      windowTitle = title;
   }

   public static void setXscale() {
      setXscale((double)0.0F, (double)1.0F);
   }

   public static void setYscale() {
      setYscale((double)0.0F, (double)1.0F);
   }

   public static void setScale() {
      setXscale();
      setYscale();
   }

   public static void setXscale(double min, double max) {
      validate(min, "min");
      validate(max, "max");
      double size = max - min;
      if (size == (double)0.0F) {
         throw new IllegalArgumentException("the min and max are the same");
      } else {
         synchronized(MOUSE_LOCK) {
            xmin = min - (double)0.0F * size;
            xmax = max + (double)0.0F * size;
         }
      }
   }

   public static void setYscale(double min, double max) {
      validate(min, "min");
      validate(max, "max");
      double size = max - min;
      if (size == (double)0.0F) {
         throw new IllegalArgumentException("the min and max are the same");
      } else {
         synchronized(MOUSE_LOCK) {
            ymin = min - (double)0.0F * size;
            ymax = max + (double)0.0F * size;
         }
      }
   }

   public static void setScale(double min, double max) {
      validate(min, "min");
      validate(max, "max");
      double size = max - min;
      if (size == (double)0.0F) {
         throw new IllegalArgumentException("the min and max are the same");
      } else {
         synchronized(MOUSE_LOCK) {
            xmin = min - (double)0.0F * size;
            xmax = max + (double)0.0F * size;
            ymin = min - (double)0.0F * size;
            ymax = max + (double)0.0F * size;
         }
      }
   }

   private static double scaleX(double x) {
      return (double)width * (x - xmin) / (xmax - xmin);
   }

   private static double scaleY(double y) {
      return (double)height * (ymax - y) / (ymax - ymin);
   }

   private static double factorX(double w) {
      return w * (double)width / Math.abs(xmax - xmin);
   }

   private static double factorY(double h) {
      return h * (double)height / Math.abs(ymax - ymin);
   }

   private static double userX(double x) {
      return xmin + x * (xmax - xmin) / (double)width;
   }

   private static double userY(double y) {
      return ymax - y * (ymax - ymin) / (double)height;
   }

   public static void clear() {
      clear(DEFAULT_BACKGROUND_COLOR);
   }

   public static void clear(Color color) {
      validateNotNull(color, "color");
      backgroundColor = color;
      offscreen.setBackground(backgroundColor);
      offscreen.clearRect(0, 0, width, height);
      onscreen.setBackground(backgroundColor);
      onscreen.clearRect(0, 0, 2 * width, 2 * height);
      draw();
   }

   public static double getPenRadius() {
      return penRadius;
   }

   public static void setPenRadius() {
      setPenRadius(0.002);
   }

   public static void setPenRadius(double radius) {
      validate(radius, "pen radius");
      validateNonnegative(radius, "pen radius");
      penRadius = radius;
      float scaledPenRadius = (float)(radius * (double)512.0F);
      BasicStroke stroke = new BasicStroke(scaledPenRadius, 1, 1);
      offscreen.setStroke(stroke);
   }

   public static Color getPenColor() {
      return penColor;
   }

   public static Color getBackgroundColor() {
      return backgroundColor;
   }

   public static void setPenColor() {
      setPenColor(DEFAULT_PEN_COLOR);
   }

   public static void setPenColor(Color color) {
      validateNotNull(color, "color");
      penColor = color;
      offscreen.setColor(penColor);
   }

   public static void setPenColor(int red, int green, int blue) {
      if (red >= 0 && red < 256) {
         if (green >= 0 && green < 256) {
            if (blue >= 0 && blue < 256) {
               setPenColor(new Color(red, green, blue));
            } else {
               throw new IllegalArgumentException("blue must be between 0 and 255");
            }
         } else {
            throw new IllegalArgumentException("green must be between 0 and 255");
         }
      } else {
         throw new IllegalArgumentException("red must be between 0 and 255");
      }
   }

   public static Font getFont() {
      return font;
   }

   public static void setFont() {
      setFont(DEFAULT_FONT);
   }

   public static void setFont(Font font) {
      validateNotNull(font, "font");
      StdDraw.font = font;
   }

   public static void line(double x0, double y0, double x1, double y1) {
      validate(x0, "x0");
      validate(y0, "y0");
      validate(x1, "x1");
      validate(y1, "y1");
      offscreen.draw(new Line2D.Double(scaleX(x0), scaleY(y0), scaleX(x1), scaleY(y1)));
      draw();
   }

   private static void pixel(double x, double y) {
      validate(x, "x");
      validate(y, "y");
      offscreen.fillRect((int)Math.round(scaleX(x)), (int)Math.round(scaleY(y)), 1, 1);
   }

   public static void point(double x, double y) {
      validate(x, "x");
      validate(y, "y");
      double xs = scaleX(x);
      double ys = scaleY(y);
      double r = penRadius;
      float scaledPenRadius = (float)(r * (double)512.0F);
      if (scaledPenRadius <= 1.0F) {
         pixel(x, y);
      } else {
         offscreen.fill(new Ellipse2D.Double(xs - (double)(scaledPenRadius / 2.0F), ys - (double)(scaledPenRadius / 2.0F), (double)scaledPenRadius, (double)scaledPenRadius));
      }

      draw();
   }

   public static void circle(double x, double y, double radius) {
      validate(x, "x");
      validate(y, "y");
      validate(radius, "radius");
      validateNonnegative(radius, "radius");
      double xs = scaleX(x);
      double ys = scaleY(y);
      double ws = factorX((double)2.0F * radius);
      double hs = factorY((double)2.0F * radius);
      if (ws <= (double)1.0F && hs <= (double)1.0F) {
         pixel(x, y);
      } else {
         offscreen.draw(new Ellipse2D.Double(xs - ws / (double)2.0F, ys - hs / (double)2.0F, ws, hs));
      }

      draw();
   }

   public static void filledCircle(double x, double y, double radius) {
      validate(x, "x");
      validate(y, "y");
      validate(radius, "radius");
      validateNonnegative(radius, "radius");
      double xs = scaleX(x);
      double ys = scaleY(y);
      double ws = factorX((double)2.0F * radius);
      double hs = factorY((double)2.0F * radius);
      if (ws <= (double)1.0F && hs <= (double)1.0F) {
         pixel(x, y);
      } else {
         offscreen.fill(new Ellipse2D.Double(xs - ws / (double)2.0F, ys - hs / (double)2.0F, ws, hs));
      }

      draw();
   }

   public static void ellipse(double x, double y, double semiMajorAxis, double semiMinorAxis) {
      validate(x, "x");
      validate(y, "y");
      validate(semiMajorAxis, "semimajor axis");
      validate(semiMinorAxis, "semiminor axis");
      validateNonnegative(semiMajorAxis, "semimajor axis");
      validateNonnegative(semiMinorAxis, "semiminor axis");
      double xs = scaleX(x);
      double ys = scaleY(y);
      double ws = factorX((double)2.0F * semiMajorAxis);
      double hs = factorY((double)2.0F * semiMinorAxis);
      if (ws <= (double)1.0F && hs <= (double)1.0F) {
         pixel(x, y);
      } else {
         offscreen.draw(new Ellipse2D.Double(xs - ws / (double)2.0F, ys - hs / (double)2.0F, ws, hs));
      }

      draw();
   }

   public static void filledEllipse(double x, double y, double semiMajorAxis, double semiMinorAxis) {
      validate(x, "x");
      validate(y, "y");
      validate(semiMajorAxis, "semimajor axis");
      validate(semiMinorAxis, "semiminor axis");
      validateNonnegative(semiMajorAxis, "semimajor axis");
      validateNonnegative(semiMinorAxis, "semiminor axis");
      double xs = scaleX(x);
      double ys = scaleY(y);
      double ws = factorX((double)2.0F * semiMajorAxis);
      double hs = factorY((double)2.0F * semiMinorAxis);
      if (ws <= (double)1.0F && hs <= (double)1.0F) {
         pixel(x, y);
      } else {
         offscreen.fill(new Ellipse2D.Double(xs - ws / (double)2.0F, ys - hs / (double)2.0F, ws, hs));
      }

      draw();
   }

   public static void arc(double x, double y, double radius, double angle1, double angle2) {
      validate(x, "x");
      validate(y, "y");
      validate(radius, "arc radius");
      validate(angle1, "angle1");
      validate(angle2, "angle2");
      validateNonnegative(radius, "arc radius");

      while(angle2 < angle1) {
         angle2 += (double)360.0F;
      }

      double xs = scaleX(x);
      double ys = scaleY(y);
      double ws = factorX((double)2.0F * radius);
      double hs = factorY((double)2.0F * radius);
      if (ws <= (double)1.0F && hs <= (double)1.0F) {
         pixel(x, y);
      } else {
         offscreen.draw(new Arc2D.Double(xs - ws / (double)2.0F, ys - hs / (double)2.0F, ws, hs, angle1, angle2 - angle1, 0));
      }

      draw();
   }

   public static void square(double x, double y, double halfLength) {
      validate(x, "x");
      validate(y, "y");
      validate(halfLength, "halfLength");
      validateNonnegative(halfLength, "half length");
      double xs = scaleX(x);
      double ys = scaleY(y);
      double ws = factorX((double)2.0F * halfLength);
      double hs = factorY((double)2.0F * halfLength);
      if (ws <= (double)1.0F && hs <= (double)1.0F) {
         pixel(x, y);
      } else {
         offscreen.draw(new Rectangle2D.Double(xs - ws / (double)2.0F, ys - hs / (double)2.0F, ws, hs));
      }

      draw();
   }

   public static void filledSquare(double x, double y, double halfLength) {
      validate(x, "x");
      validate(y, "y");
      validate(halfLength, "halfLength");
      validateNonnegative(halfLength, "half length");
      double xs = scaleX(x);
      double ys = scaleY(y);
      double ws = factorX((double)2.0F * halfLength);
      double hs = factorY((double)2.0F * halfLength);
      if (ws <= (double)1.0F && hs <= (double)1.0F) {
         pixel(x, y);
      } else {
         offscreen.fill(new Rectangle2D.Double(xs - ws / (double)2.0F, ys - hs / (double)2.0F, ws, hs));
      }

      draw();
   }

   public static void rectangle(double x, double y, double halfWidth, double halfHeight) {
      validate(x, "x");
      validate(y, "y");
      validate(halfWidth, "halfWidth");
      validate(halfHeight, "halfHeight");
      validateNonnegative(halfWidth, "half width");
      validateNonnegative(halfHeight, "half height");
      double xs = scaleX(x);
      double ys = scaleY(y);
      double ws = factorX((double)2.0F * halfWidth);
      double hs = factorY((double)2.0F * halfHeight);
      if (ws <= (double)1.0F && hs <= (double)1.0F) {
         pixel(x, y);
      } else {
         offscreen.draw(new Rectangle2D.Double(xs - ws / (double)2.0F, ys - hs / (double)2.0F, ws, hs));
      }

      draw();
   }

   public static void filledRectangle(double x, double y, double halfWidth, double halfHeight) {
      validate(x, "x");
      validate(y, "y");
      validate(halfWidth, "halfWidth");
      validate(halfHeight, "halfHeight");
      validateNonnegative(halfWidth, "half width");
      validateNonnegative(halfHeight, "half height");
      double xs = scaleX(x);
      double ys = scaleY(y);
      double ws = factorX((double)2.0F * halfWidth);
      double hs = factorY((double)2.0F * halfHeight);
      if (ws <= (double)1.0F && hs <= (double)1.0F) {
         pixel(x, y);
      } else {
         offscreen.fill(new Rectangle2D.Double(xs - ws / (double)2.0F, ys - hs / (double)2.0F, ws, hs));
      }

      draw();
   }

   public static void polygon(double[] x, double[] y) {
      validateNotNull(x, "x-coordinate array");
      validateNotNull(y, "y-coordinate array");

      for(int i = 0; i < x.length; ++i) {
         validate(x[i], "x[" + i + "]");
      }

      for(int i = 0; i < y.length; ++i) {
         validate(y[i], "y[" + i + "]");
      }

      int n1 = x.length;
      int n2 = y.length;
      if (n1 != n2) {
         throw new IllegalArgumentException("arrays must be of the same length");
      } else {
         int n = n1;
         if (n1 != 0) {
            GeneralPath path = new GeneralPath();
            path.moveTo((float)scaleX(x[0]), (float)scaleY(y[0]));

            for(int i = 0; i < n; ++i) {
               path.lineTo((float)scaleX(x[i]), (float)scaleY(y[i]));
            }

            path.closePath();
            offscreen.draw(path);
            draw();
         }
      }
   }

   public static void filledPolygon(double[] x, double[] y) {
      validateNotNull(x, "x-coordinate array");
      validateNotNull(y, "y-coordinate array");

      for(int i = 0; i < x.length; ++i) {
         validate(x[i], "x[" + i + "]");
      }

      for(int i = 0; i < y.length; ++i) {
         validate(y[i], "y[" + i + "]");
      }

      int n1 = x.length;
      int n2 = y.length;
      if (n1 != n2) {
         throw new IllegalArgumentException("arrays must be of the same length");
      } else {
         int n = n1;
         if (n1 != 0) {
            GeneralPath path = new GeneralPath();
            path.moveTo((float)scaleX(x[0]), (float)scaleY(y[0]));

            for(int i = 0; i < n; ++i) {
               path.lineTo((float)scaleX(x[i]), (float)scaleY(y[i]));
            }

            path.closePath();
            offscreen.fill(path);
            draw();
         }
      }
   }

   private static Image getImage(String filename) {
      if (filename == null) {
         throw new IllegalArgumentException();
      } else {
         ImageIcon icon = new ImageIcon(filename);
         if (icon.getImageLoadStatus() != 8) {
            try {
               URI uri = new URI(filename);
               if (uri.isAbsolute()) {
                  URL url = uri.toURL();
                  icon = new ImageIcon(url);
               }
            } catch (URISyntaxException | MalformedURLException var4) {
            }
         }

         if (icon.getImageLoadStatus() != 8) {
            URL url = StdDraw.class.getResource(filename);
            if (url != null) {
               icon = new ImageIcon(url);
            }
         }

         if (icon.getImageLoadStatus() != 8) {
            URL url = StdDraw.class.getResource("/" + filename);
            if (url == null) {
               throw new IllegalArgumentException("could not read image: '" + filename + "'");
            }

            icon = new ImageIcon(url);
         }

         return icon.getImage();
      }
   }

   public static void picture(double x, double y, String filename) {
      validate(x, "x");
      validate(y, "y");
      validateNotNull(filename, "filename");
      Image image = getImage(filename);
      double xs = scaleX(x);
      double ys = scaleY(y);
      int ws = image.getWidth((ImageObserver)null);
      int hs = image.getHeight((ImageObserver)null);
      if (ws >= 0 && hs >= 0) {
         offscreen.drawImage(image, (int)Math.round(xs - (double)ws / (double)2.0F), (int)Math.round(ys - (double)hs / (double)2.0F), (ImageObserver)null);
         draw();
      } else {
         throw new IllegalArgumentException("image " + filename + " is corrupt");
      }
   }

   public static void picture(double x, double y, String filename, double degrees) {
      validate(x, "x");
      validate(y, "y");
      validate(degrees, "degrees");
      validateNotNull(filename, "filename");
      Image image = getImage(filename);
      double xs = scaleX(x);
      double ys = scaleY(y);
      int ws = image.getWidth((ImageObserver)null);
      int hs = image.getHeight((ImageObserver)null);
      if (ws >= 0 && hs >= 0) {
         offscreen.rotate(Math.toRadians(-degrees), xs, ys);
         offscreen.drawImage(image, (int)Math.round(xs - (double)ws / (double)2.0F), (int)Math.round(ys - (double)hs / (double)2.0F), (ImageObserver)null);
         offscreen.rotate(Math.toRadians(degrees), xs, ys);
         draw();
      } else {
         throw new IllegalArgumentException("image " + filename + " is corrupt");
      }
   }

   public static void picture(double x, double y, String filename, double scaledWidth, double scaledHeight) {
      validate(x, "x");
      validate(y, "y");
      validate(scaledWidth, "scaled width");
      validate(scaledHeight, "scaled height");
      validateNotNull(filename, "filename");
      validateNonnegative(scaledWidth, "scaled width");
      validateNonnegative(scaledHeight, "scaled height");
      Image image = getImage(filename);
      double xs = scaleX(x);
      double ys = scaleY(y);
      double ws = factorX(scaledWidth);
      double hs = factorY(scaledHeight);
      if (!(ws < (double)0.0F) && !(hs < (double)0.0F)) {
         if (ws <= (double)1.0F && hs <= (double)1.0F) {
            pixel(x, y);
         } else {
            offscreen.drawImage(image, (int)Math.round(xs - ws / (double)2.0F), (int)Math.round(ys - hs / (double)2.0F), (int)Math.round(ws), (int)Math.round(hs), (ImageObserver)null);
         }

         draw();
      } else {
         throw new IllegalArgumentException("image " + filename + " is corrupt");
      }
   }

   public static void picture(double x, double y, String filename, double scaledWidth, double scaledHeight, double degrees) {
      validate(x, "x");
      validate(y, "y");
      validate(scaledWidth, "scaled width");
      validate(scaledHeight, "scaled height");
      validate(degrees, "degrees");
      validateNotNull(filename, "filename");
      validateNonnegative(scaledWidth, "scaled width");
      validateNonnegative(scaledHeight, "scaled height");
      Image image = getImage(filename);
      double xs = scaleX(x);
      double ys = scaleY(y);
      double ws = factorX(scaledWidth);
      double hs = factorY(scaledHeight);
      if (!(ws < (double)0.0F) && !(hs < (double)0.0F)) {
         if (ws <= (double)1.0F && hs <= (double)1.0F) {
            pixel(x, y);
         }

         offscreen.rotate(Math.toRadians(-degrees), xs, ys);
         offscreen.drawImage(image, (int)Math.round(xs - ws / (double)2.0F), (int)Math.round(ys - hs / (double)2.0F), (int)Math.round(ws), (int)Math.round(hs), (ImageObserver)null);
         offscreen.rotate(Math.toRadians(degrees), xs, ys);
         draw();
      } else {
         throw new IllegalArgumentException("image " + filename + " is corrupt");
      }
   }

   public static void text(double x, double y, String text) {
      validate(x, "x");
      validate(y, "y");
      validateNotNull(text, "text");
      offscreen.setFont(font);
      FontMetrics metrics = offscreen.getFontMetrics();
      double xs = scaleX(x);
      double ys = scaleY(y);
      int ws = metrics.stringWidth(text);
      int hs = metrics.getDescent();
      offscreen.drawString(text, (float)(xs - (double)ws / (double)2.0F), (float)(ys + (double)hs));
      draw();
   }

   public static void text(double x, double y, String text, double degrees) {
      validate(x, "x");
      validate(y, "y");
      validate(degrees, "degrees");
      validateNotNull(text, "text");
      double xs = scaleX(x);
      double ys = scaleY(y);
      offscreen.rotate(Math.toRadians(-degrees), xs, ys);
      text(x, y, text);
      offscreen.rotate(Math.toRadians(degrees), xs, ys);
   }

   public static void textLeft(double x, double y, String text) {
      validate(x, "x");
      validate(y, "y");
      validateNotNull(text, "text");
      offscreen.setFont(font);
      FontMetrics metrics = offscreen.getFontMetrics();
      double xs = scaleX(x);
      double ys = scaleY(y);
      int hs = metrics.getDescent();
      offscreen.drawString(text, (float)xs, (float)(ys + (double)hs));
      draw();
   }

   public static void textRight(double x, double y, String text) {
      validate(x, "x");
      validate(y, "y");
      validateNotNull(text, "text");
      offscreen.setFont(font);
      FontMetrics metrics = offscreen.getFontMetrics();
      double xs = scaleX(x);
      double ys = scaleY(y);
      int ws = metrics.stringWidth(text);
      int hs = metrics.getDescent();
      offscreen.drawString(text, (float)(xs - (double)ws), (float)(ys + (double)hs));
      draw();
   }

   /** @deprecated */
   @Deprecated
   public static void show(int t) {
      validateNonnegative((double)t, "t");
      show();
      pause(t);
      enableDoubleBuffering();
   }

   public static void pause(int t) {
      validateNonnegative((double)t, "t");

      try {
         Thread.sleep((long)t);
      } catch (InterruptedException var2) {
         System.out.println("Error sleeping");
      }

   }

   public static void show() {
      onscreen.drawImage(offscreenImage, 0, 0, (ImageObserver)null);
      if (frame.isVisible() != isJFrameVisible) {
         frame.setVisible(isJFrameVisible);
      }

      frame.repaint();
   }

   private static void draw() {
      if (!defer) {
         show();
      }

   }

   public static void enableDoubleBuffering() {
      defer = true;
   }

   public static void disableDoubleBuffering() {
      defer = false;
   }

   public static void save(String filename) {
      validateNotNull(filename, "filename");
      if (filename.length() == 0) {
         throw new IllegalArgumentException("argument to save() is the empty string");
      } else {
         File file = new File(filename);
         String suffix = filename.substring(filename.lastIndexOf(46) + 1);
         if (filename.contains(".") && suffix.length() != 0) {
            try {
               if (!ImageIO.write(onscreenImage, suffix, file)) {
                  BufferedImage saveImage = new BufferedImage(2 * width, 2 * height, 1);
                  saveImage.createGraphics().drawImage(onscreenImage, 0, 0, Color.WHITE, (ImageObserver)null);
                  if (!ImageIO.write(saveImage, suffix, file)) {
                     throw new IllegalArgumentException("The filetype '" + suffix + "' is not supported");
                  }
               }
            } catch (IOException e) {
               throw new IllegalArgumentException("could not write file '" + filename + "'", e);
            }
         } else {
            throw new IllegalArgumentException("The filename '" + filename + "' has no filetype extension, such as .jpg or .png");
         }
      }
   }

   public void actionPerformed(ActionEvent event) {
      FileDialog chooser = new FileDialog(frame, "Use a .png or .jpg extension", 1);
      chooser.setVisible(true);
      String selectedDirectory = chooser.getDirectory();
      String selectedFilename = chooser.getFile();
      if (selectedDirectory != null && selectedFilename != null) {
         try {
            save(selectedDirectory + selectedFilename);
         } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
         }
      }

   }

   public static boolean isMousePressed() {
      synchronized(MOUSE_LOCK) {
         return isMousePressed;
      }
   }

   /** @deprecated */
   @Deprecated
   public static boolean mousePressed() {
      synchronized(MOUSE_LOCK) {
         return isMousePressed;
      }
   }

   public static double mouseX() {
      synchronized(MOUSE_LOCK) {
         return mouseX;
      }
   }

   public static double mouseY() {
      synchronized(MOUSE_LOCK) {
         return mouseY;
      }
   }

   public void mouseClicked(MouseEvent event) {
   }

   public void mouseEntered(MouseEvent event) {
   }

   public void mouseExited(MouseEvent event) {
   }

   public void mousePressed(MouseEvent event) {
      synchronized(MOUSE_LOCK) {
         mouseX = userX((double)event.getX());
         mouseY = userY((double)event.getY());
         isMousePressed = true;
      }
   }

   public void mouseReleased(MouseEvent event) {
      synchronized(MOUSE_LOCK) {
         isMousePressed = false;
      }
   }

   public void mouseDragged(MouseEvent event) {
      synchronized(MOUSE_LOCK) {
         mouseX = userX((double)event.getX());
         mouseY = userY((double)event.getY());
      }
   }

   public void mouseMoved(MouseEvent event) {
      synchronized(MOUSE_LOCK) {
         mouseX = userX((double)event.getX());
         mouseY = userY((double)event.getY());
      }
   }

   public static boolean hasNextKeyTyped() {
      synchronized(KEY_LOCK) {
         return !keysTyped.isEmpty();
      }
   }

   public static char nextKeyTyped() {
      synchronized(KEY_LOCK) {
         if (keysTyped.isEmpty()) {
            throw new NoSuchElementException("your program has already processed all keystrokes");
         } else {
            return (Character)keysTyped.remove(keysTyped.size() - 1);
         }
      }
   }

   public static boolean isKeyPressed(int keycode) {
      synchronized(KEY_LOCK) {
         return keysDown.contains(keycode);
      }
   }

   public void keyTyped(KeyEvent event) {
      synchronized(KEY_LOCK) {
         keysTyped.addFirst(event.getKeyChar());
      }
   }

   public void keyPressed(KeyEvent event) {
      synchronized(KEY_LOCK) {
         keysDown.add(event.getKeyCode());
      }
   }

   public void keyReleased(KeyEvent event) {
      synchronized(KEY_LOCK) {
         keysDown.remove(event.getKeyCode());
      }
   }

   public static void main(String[] args) {
      square(0.2, 0.8, 0.1);
      filledSquare(0.8, 0.8, 0.2);
      circle(0.8, 0.2, 0.2);
      setPenColor(BOOK_RED);
      setPenRadius(0.02);
      arc(0.8, 0.2, 0.1, (double)200.0F, (double)45.0F);
      setPenRadius();
      setPenColor(BOOK_BLUE);
      double[] x = new double[]{0.1, 0.2, 0.3, 0.2};
      double[] y = new double[]{0.2, 0.3, 0.2, 0.1};
      filledPolygon(x, y);
      setPenColor(BLACK);
      text(0.2, (double)0.5F, "black text");
      setPenColor(WHITE);
      text(0.8, 0.8, "white text");
   }

   static {
      BLACK = Color.BLACK;
      BLUE = Color.BLUE;
      CYAN = Color.CYAN;
      FUSCIA = new Color(255, 0, 255);
      DARK_GRAY = Color.DARK_GRAY;
      GRAY = Color.GRAY;
      GREEN = new Color(0, 128, 0);
      LIGHT_GRAY = Color.LIGHT_GRAY;
      LIME = new Color(0, 255, 0);
      MAGENTA = Color.MAGENTA;
      MAROON = new Color(128, 0, 0);
      NAVY = new Color(0, 0, 128);
      OLIVE = new Color(128, 128, 0);
      ORANGE = Color.ORANGE;
      PINK = Color.PINK;
      PURPLE = new Color(128, 0, 128);
      RED = Color.RED;
      SILVER = new Color(192, 192, 192);
      TEAL = new Color(0, 128, 128);
      WHITE = Color.WHITE;
      YELLOW = Color.YELLOW;
      TRANSPARENT = new Color(0, 0, 0, 0);
      BOOK_BLUE = new Color(9, 90, 166);
      BOOK_LIGHT_BLUE = new Color(103, 198, 243);
      BOOK_RED = new Color(150, 35, 31);
      PRINCETON_ORANGE = new Color(245, 128, 37);
      DEFAULT_PEN_COLOR = BLACK;
      DEFAULT_BACKGROUND_COLOR = WHITE;
      penColor = DEFAULT_PEN_COLOR;
      backgroundColor = DEFAULT_BACKGROUND_COLOR;
      windowTitle = "Standard Draw";
      width = 512;
      height = 512;
      penRadius = 0.002;
      defer = false;
      xmin = (double)0.0F;
      xmax = (double)1.0F;
      ymin = (double)0.0F;
      ymax = (double)1.0F;
      MOUSE_LOCK = new Object();
      KEY_LOCK = new Object();
      DEFAULT_FONT = new Font("SansSerif", 0, 16);
      font = DEFAULT_FONT;
      std = new StdDraw();
      isJFrameVisible = true;
      isMousePressed = false;
      mouseX = (double)0.0F;
      mouseY = (double)0.0F;
      keysTyped = new LinkedList();
      keysDown = new TreeSet();
      initCanvas();
      initGUI();
   }

   private static class RetinaImageIcon extends ImageIcon {
      public RetinaImageIcon(Image image) {
         super(image);
      }

      public int getIconWidth() {
         return super.getIconWidth() / 2;
      }

      public int getIconHeight() {
         return super.getIconHeight() / 2;
      }

      public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
         Graphics2D g2 = (Graphics2D)g.create();
         g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
         g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         g2.scale((double)0.5F, (double)0.5F);
         super.paintIcon(c, g2, x * 2, y * 2);
         g2.dispose();
      }
   }
}
