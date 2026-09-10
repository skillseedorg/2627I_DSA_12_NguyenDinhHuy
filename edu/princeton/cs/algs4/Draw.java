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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public final class Draw implements ActionListener, MouseListener, MouseMotionListener, KeyListener {
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
   private static final double BORDER = (double)0.0F;
   private static final double DEFAULT_XMIN = (double)0.0F;
   private static final double DEFAULT_XMAX = (double)1.0F;
   private static final double DEFAULT_YMIN = (double)0.0F;
   private static final double DEFAULT_YMAX = (double)1.0F;
   private static final int DEFAULT_SIZE = 512;
   private static final double DEFAULT_PEN_RADIUS = 0.002;
   private static final Font DEFAULT_FONT;
   private static final String DEFAULT_WINDOW_TITLE = "Draw";
   private Color penColor;
   private Color backgroundColor;
   private String windowTitle;
   private int width;
   private int height;
   private double penRadius;
   private boolean defer;
   private double xmin;
   private double xmax;
   private double ymin;
   private double ymax;
   private final Object mouseLock;
   private final Object keyLock;
   private Font font;
   private JLabel draw;
   private BufferedImage offscreenImage;
   private BufferedImage onscreenImage;
   private Graphics2D offscreen;
   private Graphics2D onscreen;
   private JFrame frame;
   private static boolean isJFrameVisible;
   private boolean isMousePressed;
   private double mouseX;
   private double mouseY;
   private final LinkedList<Character> keysTyped;
   private final TreeSet<Integer> keysDown;
   private final ArrayList<DrawListener> listeners;
   private Timer timer;

   public Draw() {
      this.penColor = DEFAULT_PEN_COLOR;
      this.backgroundColor = DEFAULT_BACKGROUND_COLOR;
      this.windowTitle = "Draw";
      this.width = 512;
      this.height = 512;
      this.penRadius = 0.002;
      this.defer = false;
      this.xmin = (double)0.0F;
      this.xmax = (double)1.0F;
      this.ymin = (double)0.0F;
      this.ymax = (double)1.0F;
      this.mouseLock = new Object();
      this.keyLock = new Object();
      this.font = DEFAULT_FONT;
      this.isMousePressed = false;
      this.mouseX = (double)0.0F;
      this.mouseY = (double)0.0F;
      this.keysTyped = new LinkedList();
      this.keysDown = new TreeSet();
      this.listeners = new ArrayList();
      this.initCanvas();
      this.initGUI();
   }

   private void initCanvas() {
      this.offscreenImage = new BufferedImage(2 * this.width, 2 * this.height, 2);
      this.onscreenImage = new BufferedImage(2 * this.width, 2 * this.height, 2);
      this.offscreen = this.offscreenImage.createGraphics();
      this.onscreen = this.onscreenImage.createGraphics();
      this.offscreen.scale((double)2.0F, (double)2.0F);
      this.offscreen.setBackground(DEFAULT_BACKGROUND_COLOR);
      this.offscreen.clearRect(0, 0, this.width, this.height);
      this.onscreen.setBackground(DEFAULT_BACKGROUND_COLOR);
      this.onscreen.clearRect(0, 0, 2 * this.width, 2 * this.height);
      this.offscreen.setColor(this.penColor);
      RenderingHints hints = new RenderingHints((Map)null);
      hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
      this.offscreen.addRenderingHints(hints);
   }

   private void initGUI() {
      if (this.frame == null) {
         this.frame = new JFrame();
         this.frame.addKeyListener(this);
         this.frame.setFocusTraversalKeysEnabled(false);
         this.frame.setResizable(false);
         this.frame.setDefaultCloseOperation(2);
         this.frame.setTitle(this.windowTitle);
         this.frame.setJMenuBar(this.createMenuBar());
      }

      RetinaImageIcon icon = new RetinaImageIcon(this.onscreenImage);
      this.draw = new JLabel(icon);
      this.draw.addMouseListener(this);
      this.draw.addMouseMotionListener(this);
      this.frame.setContentPane(this.draw);
      this.frame.pack();
      this.frame.requestFocusInWindow();
      this.frame.setVisible(false);
   }

   public void setVisible(boolean isVisible) {
      isJFrameVisible = isVisible;
      this.frame.setVisible(isVisible);
   }

   public void setLocationOnScreen(int x, int y) {
      if (x > 0 && y > 0) {
         this.frame.setLocation(x, y);
      } else {
         throw new IllegalArgumentException();
      }
   }

   public void setDefaultCloseOperation(int value) {
      this.frame.setDefaultCloseOperation(value);
   }

   public void setCanvasSize() {
      this.setCanvasSize(512, 512);
   }

   public void setCanvasSize(int canvasWidth, int canvasHeight) {
      if (canvasWidth >= 1 && canvasHeight >= 1) {
         this.width = canvasWidth;
         this.height = canvasHeight;
         this.initCanvas();
         this.initGUI();
      } else {
         throw new IllegalArgumentException("width and height must be positive");
      }
   }

   private JMenuBar createMenuBar() {
      JMenuBar menuBar = new JMenuBar();
      JMenu menu = new JMenu("File");
      menuBar.add(menu);
      JMenuItem menuItem1 = new JMenuItem(" Save...   ");
      menuItem1.addActionListener(this);
      menuItem1.setAccelerator(KeyStroke.getKeyStroke(83, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
      menu.add(menuItem1);
      return menuBar;
   }

   public void close() {
      this.frame.dispose();
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

   public void setTitle(String windowTitle) {
      validateNotNull(windowTitle, "title");
      this.windowTitle = windowTitle;
      this.frame.setTitle(windowTitle);
   }

   public void setXscale() {
      this.setXscale((double)0.0F, (double)1.0F);
   }

   public void setYscale() {
      this.setYscale((double)0.0F, (double)1.0F);
   }

   public void setXscale(double min, double max) {
      validate(min, "min");
      validate(max, "max");
      double size = max - min;
      if (size == (double)0.0F) {
         throw new IllegalArgumentException("the min and max are the same");
      } else {
         this.xmin = min - (double)0.0F * size;
         this.xmax = max + (double)0.0F * size;
      }
   }

   public void setYscale(double min, double max) {
      validate(min, "min");
      validate(max, "max");
      double size = max - min;
      if (size == (double)0.0F) {
         throw new IllegalArgumentException("the min and max are the same");
      } else {
         this.ymin = min - (double)0.0F * size;
         this.ymax = max + (double)0.0F * size;
      }
   }

   public void setScale() {
      this.setXscale();
      this.setYscale();
   }

   public void setScale(double min, double max) {
      this.setXscale(min, max);
      this.setYscale(min, max);
   }

   private double scaleX(double x) {
      return (double)this.width * (x - this.xmin) / (this.xmax - this.xmin);
   }

   private double scaleY(double y) {
      return (double)this.height * (this.ymax - y) / (this.ymax - this.ymin);
   }

   private double factorX(double w) {
      return w * (double)this.width / Math.abs(this.xmax - this.xmin);
   }

   private double factorY(double h) {
      return h * (double)this.height / Math.abs(this.ymax - this.ymin);
   }

   private double userX(double x) {
      return this.xmin + x * (this.xmax - this.xmin) / (double)this.width;
   }

   private double userY(double y) {
      return this.ymax - y * (this.ymax - this.ymin) / (double)this.height;
   }

   public void clear() {
      this.clear(DEFAULT_BACKGROUND_COLOR);
   }

   public void clear(Color color) {
      validateNotNull(color, "color");
      this.backgroundColor = color;
      this.offscreen.setBackground(this.backgroundColor);
      this.offscreen.clearRect(0, 0, this.width, this.height);
      this.draw();
   }

   public double getPenRadius() {
      return this.penRadius;
   }

   public void setPenRadius() {
      this.setPenRadius(0.002);
   }

   public void setPenRadius(double radius) {
      validate(radius, "pen radius");
      validateNonnegative(radius, "pen radius");
      this.penRadius = radius * (double)512.0F;
      BasicStroke stroke = new BasicStroke((float)this.penRadius, 1, 1);
      this.offscreen.setStroke(stroke);
   }

   public Color getPenColor() {
      return this.penColor;
   }

   public Color getBackgroundColor() {
      return this.backgroundColor;
   }

   public void setPenColor() {
      this.setPenColor(DEFAULT_PEN_COLOR);
   }

   public void setPenColor(Color color) {
      validateNotNull(color, "color");
      this.penColor = color;
      this.offscreen.setColor(this.penColor);
   }

   public void setPenColor(int red, int green, int blue) {
      if (red >= 0 && red < 256) {
         if (green >= 0 && green < 256) {
            if (blue >= 0 && blue < 256) {
               this.setPenColor(new Color(red, green, blue));
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

   public void xorOn() {
      this.offscreen.setXORMode(this.backgroundColor);
   }

   public void xorOff() {
      this.offscreen.setPaintMode();
   }

   public JLabel getJLabel() {
      return this.draw;
   }

   public Font getFont() {
      return this.font;
   }

   public void setFont() {
      this.setFont(DEFAULT_FONT);
   }

   public void setFont(Font font) {
      validateNotNull(font, "font");
      this.font = font;
   }

   public void line(double x0, double y0, double x1, double y1) {
      validate(x0, "x0");
      validate(y0, "y0");
      validate(x1, "x1");
      validate(y1, "y1");
      this.offscreen.draw(new Line2D.Double(this.scaleX(x0), this.scaleY(y0), this.scaleX(x1), this.scaleY(y1)));
      this.draw();
   }

   private void pixel(double x, double y) {
      validate(x, "x");
      validate(y, "y");
      this.offscreen.fillRect((int)Math.round(this.scaleX(x)), (int)Math.round(this.scaleY(y)), 1, 1);
   }

   public void point(double x, double y) {
      validate(x, "x");
      validate(y, "y");
      double xs = this.scaleX(x);
      double ys = this.scaleY(y);
      double r = this.penRadius;
      if (r <= (double)1.0F) {
         this.pixel(x, y);
      } else {
         this.offscreen.fill(new Ellipse2D.Double(xs - r / (double)2.0F, ys - r / (double)2.0F, r, r));
      }

      this.draw();
   }

   public void circle(double x, double y, double radius) {
      validate(x, "x");
      validate(y, "y");
      validate(radius, "radius");
      validateNonnegative(radius, "radius");
      double xs = this.scaleX(x);
      double ys = this.scaleY(y);
      double ws = this.factorX((double)2.0F * radius);
      double hs = this.factorY((double)2.0F * radius);
      if (ws <= (double)1.0F && hs <= (double)1.0F) {
         this.pixel(x, y);
      } else {
         this.offscreen.draw(new Ellipse2D.Double(xs - ws / (double)2.0F, ys - hs / (double)2.0F, ws, hs));
      }

      this.draw();
   }

   public void filledCircle(double x, double y, double radius) {
      validate(x, "x");
      validate(y, "y");
      validate(radius, "radius");
      validateNonnegative(radius, "radius");
      double xs = this.scaleX(x);
      double ys = this.scaleY(y);
      double ws = this.factorX((double)2.0F * radius);
      double hs = this.factorY((double)2.0F * radius);
      if (ws <= (double)1.0F && hs <= (double)1.0F) {
         this.pixel(x, y);
      } else {
         this.offscreen.fill(new Ellipse2D.Double(xs - ws / (double)2.0F, ys - hs / (double)2.0F, ws, hs));
      }

      this.draw();
   }

   public void ellipse(double x, double y, double semiMajorAxis, double semiMinorAxis) {
      validate(x, "x");
      validate(y, "y");
      validate(semiMajorAxis, "semimajor axis");
      validate(semiMinorAxis, "semiminor axis");
      validateNonnegative(semiMajorAxis, "semimajor axis");
      validateNonnegative(semiMinorAxis, "semiminor axis");
      double xs = this.scaleX(x);
      double ys = this.scaleY(y);
      double ws = this.factorX((double)2.0F * semiMajorAxis);
      double hs = this.factorY((double)2.0F * semiMinorAxis);
      if (ws <= (double)1.0F && hs <= (double)1.0F) {
         this.pixel(x, y);
      } else {
         this.offscreen.draw(new Ellipse2D.Double(xs - ws / (double)2.0F, ys - hs / (double)2.0F, ws, hs));
      }

      this.draw();
   }

   public void filledEllipse(double x, double y, double semiMajorAxis, double semiMinorAxis) {
      validate(x, "x");
      validate(y, "y");
      validate(semiMajorAxis, "semimajor axis");
      validate(semiMinorAxis, "semiminor axis");
      validateNonnegative(semiMajorAxis, "semimajor axis");
      validateNonnegative(semiMinorAxis, "semiminor axis");
      double xs = this.scaleX(x);
      double ys = this.scaleY(y);
      double ws = this.factorX((double)2.0F * semiMajorAxis);
      double hs = this.factorY((double)2.0F * semiMinorAxis);
      if (ws <= (double)1.0F && hs <= (double)1.0F) {
         this.pixel(x, y);
      } else {
         this.offscreen.fill(new Ellipse2D.Double(xs - ws / (double)2.0F, ys - hs / (double)2.0F, ws, hs));
      }

      this.draw();
   }

   public void arc(double x, double y, double radius, double angle1, double angle2) {
      validate(x, "x");
      validate(y, "y");
      validate(radius, "arc radius");
      validate(angle1, "angle1");
      validate(angle2, "angle2");
      validateNonnegative(radius, "arc radius");

      while(angle2 < angle1) {
         angle2 += (double)360.0F;
      }

      double xs = this.scaleX(x);
      double ys = this.scaleY(y);
      double ws = this.factorX((double)2.0F * radius);
      double hs = this.factorY((double)2.0F * radius);
      if (ws <= (double)1.0F && hs <= (double)1.0F) {
         this.pixel(x, y);
      } else {
         this.offscreen.draw(new Arc2D.Double(xs - ws / (double)2.0F, ys - hs / (double)2.0F, ws, hs, angle1, angle2 - angle1, 0));
      }

      this.draw();
   }

   public void square(double x, double y, double halfLength) {
      validate(x, "x");
      validate(y, "y");
      validate(halfLength, "halfLength");
      validateNonnegative(halfLength, "half length");
      double xs = this.scaleX(x);
      double ys = this.scaleY(y);
      double ws = this.factorX((double)2.0F * halfLength);
      double hs = this.factorY((double)2.0F * halfLength);
      if (ws <= (double)1.0F && hs <= (double)1.0F) {
         this.pixel(x, y);
      } else {
         this.offscreen.draw(new Rectangle2D.Double(xs - ws / (double)2.0F, ys - hs / (double)2.0F, ws, hs));
      }

      this.draw();
   }

   public void filledSquare(double x, double y, double halfLength) {
      validate(x, "x");
      validate(y, "y");
      validate(halfLength, "halfLength");
      validateNonnegative(halfLength, "half length");
      double xs = this.scaleX(x);
      double ys = this.scaleY(y);
      double ws = this.factorX((double)2.0F * halfLength);
      double hs = this.factorY((double)2.0F * halfLength);
      if (ws <= (double)1.0F && hs <= (double)1.0F) {
         this.pixel(x, y);
      } else {
         this.offscreen.fill(new Rectangle2D.Double(xs - ws / (double)2.0F, ys - hs / (double)2.0F, ws, hs));
      }

      this.draw();
   }

   public void rectangle(double x, double y, double halfWidth, double halfHeight) {
      validate(x, "x");
      validate(y, "y");
      validate(halfWidth, "halfWidth");
      validate(halfHeight, "halfHeight");
      validateNonnegative(halfWidth, "half width");
      validateNonnegative(halfHeight, "half height");
      double xs = this.scaleX(x);
      double ys = this.scaleY(y);
      double ws = this.factorX((double)2.0F * halfWidth);
      double hs = this.factorY((double)2.0F * halfHeight);
      if (ws <= (double)1.0F && hs <= (double)1.0F) {
         this.pixel(x, y);
      } else {
         this.offscreen.draw(new Rectangle2D.Double(xs - ws / (double)2.0F, ys - hs / (double)2.0F, ws, hs));
      }

      this.draw();
   }

   public void filledRectangle(double x, double y, double halfWidth, double halfHeight) {
      validate(x, "x");
      validate(y, "y");
      validate(halfWidth, "halfWidth");
      validate(halfHeight, "halfHeight");
      validateNonnegative(halfWidth, "half width");
      validateNonnegative(halfHeight, "half height");
      double xs = this.scaleX(x);
      double ys = this.scaleY(y);
      double ws = this.factorX((double)2.0F * halfWidth);
      double hs = this.factorY((double)2.0F * halfHeight);
      if (ws <= (double)1.0F && hs <= (double)1.0F) {
         this.pixel(x, y);
      } else {
         this.offscreen.fill(new Rectangle2D.Double(xs - ws / (double)2.0F, ys - hs / (double)2.0F, ws, hs));
      }

      this.draw();
   }

   public void polygon(double[] x, double[] y) {
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
            path.moveTo((float)this.scaleX(x[0]), (float)this.scaleY(y[0]));

            for(int i = 0; i < n; ++i) {
               path.lineTo((float)this.scaleX(x[i]), (float)this.scaleY(y[i]));
            }

            path.closePath();
            this.offscreen.draw(path);
            this.draw();
         }
      }
   }

   public void filledPolygon(double[] x, double[] y) {
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
            path.moveTo((float)this.scaleX(x[0]), (float)this.scaleY(y[0]));

            for(int i = 0; i < n; ++i) {
               path.lineTo((float)this.scaleX(x[i]), (float)this.scaleY(y[i]));
            }

            path.closePath();
            this.offscreen.fill(path);
            this.draw();
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
            URL url = Draw.class.getResource(filename);
            if (url != null) {
               icon = new ImageIcon(url);
            }
         }

         if (icon.getImageLoadStatus() != 8) {
            URL url = Draw.class.getResource("/" + filename);
            if (url == null) {
               throw new IllegalArgumentException("could not read image: '" + filename + "'");
            }

            icon = new ImageIcon(url);
         }

         return icon.getImage();
      }
   }

   public void picture(double x, double y, String filename) {
      validate(x, "x");
      validate(y, "y");
      validateNotNull(filename, "filename");
      Image image = getImage(filename);
      double xs = this.scaleX(x);
      double ys = this.scaleY(y);
      int ws = image.getWidth((ImageObserver)null);
      int hs = image.getHeight((ImageObserver)null);
      if (ws >= 0 && hs >= 0) {
         this.offscreen.drawImage(image, (int)Math.round(xs - (double)ws / (double)2.0F), (int)Math.round(ys - (double)hs / (double)2.0F), (ImageObserver)null);
         this.draw();
      } else {
         throw new IllegalArgumentException("image " + filename + " is corrupt");
      }
   }

   public void picture(double x, double y, String filename, double degrees) {
      validate(x, "x");
      validate(y, "y");
      validate(degrees, "degrees");
      validateNotNull(filename, "filename");
      Image image = getImage(filename);
      double xs = this.scaleX(x);
      double ys = this.scaleY(y);
      int ws = image.getWidth((ImageObserver)null);
      int hs = image.getHeight((ImageObserver)null);
      if (ws >= 0 && hs >= 0) {
         this.offscreen.rotate(Math.toRadians(-degrees), xs, ys);
         this.offscreen.drawImage(image, (int)Math.round(xs - (double)ws / (double)2.0F), (int)Math.round(ys - (double)hs / (double)2.0F), (ImageObserver)null);
         this.offscreen.rotate(Math.toRadians(degrees), xs, ys);
         this.draw();
      } else {
         throw new IllegalArgumentException("image " + filename + " is corrupt");
      }
   }

   public void picture(double x, double y, String filename, double scaledWidth, double scaledHeight) {
      validate(x, "x");
      validate(y, "y");
      validate(scaledWidth, "scaled width");
      validate(scaledHeight, "scaled height");
      validateNotNull(filename, "filename");
      validateNonnegative(scaledWidth, "scaled width");
      validateNonnegative(scaledHeight, "scaled height");
      Image image = getImage(filename);
      double xs = this.scaleX(x);
      double ys = this.scaleY(y);
      double ws = this.factorX(scaledWidth);
      double hs = this.factorY(scaledHeight);
      if (!(ws < (double)0.0F) && !(hs < (double)0.0F)) {
         if (ws <= (double)1.0F && hs <= (double)1.0F) {
            this.pixel(x, y);
         } else {
            this.offscreen.drawImage(image, (int)Math.round(xs - ws / (double)2.0F), (int)Math.round(ys - hs / (double)2.0F), (int)Math.round(ws), (int)Math.round(hs), (ImageObserver)null);
         }

         this.draw();
      } else {
         throw new IllegalArgumentException("image " + filename + " is corrupt");
      }
   }

   public void picture(double x, double y, String filename, double scaledWidth, double scaledHeight, double degrees) {
      validate(x, "x");
      validate(y, "y");
      validate(scaledWidth, "scaled width");
      validate(scaledHeight, "scaled height");
      validate(degrees, "degrees");
      validateNotNull(filename, "filename");
      validateNonnegative(scaledWidth, "scaled width");
      validateNonnegative(scaledHeight, "scaled height");
      Image image = getImage(filename);
      double xs = this.scaleX(x);
      double ys = this.scaleY(y);
      double ws = this.factorX(scaledWidth);
      double hs = this.factorY(scaledHeight);
      if (!(ws < (double)0.0F) && !(hs < (double)0.0F)) {
         if (ws <= (double)1.0F && hs <= (double)1.0F) {
            this.pixel(x, y);
         }

         this.offscreen.rotate(Math.toRadians(-degrees), xs, ys);
         this.offscreen.drawImage(image, (int)Math.round(xs - ws / (double)2.0F), (int)Math.round(ys - hs / (double)2.0F), (int)Math.round(ws), (int)Math.round(hs), (ImageObserver)null);
         this.offscreen.rotate(Math.toRadians(degrees), xs, ys);
         this.draw();
      } else {
         throw new IllegalArgumentException("image " + filename + " is corrupt");
      }
   }

   public void text(double x, double y, String text) {
      validate(x, "x");
      validate(y, "y");
      validateNotNull(text, "text");
      this.offscreen.setFont(this.font);
      FontMetrics metrics = this.offscreen.getFontMetrics();
      double xs = this.scaleX(x);
      double ys = this.scaleY(y);
      int ws = metrics.stringWidth(text);
      int hs = metrics.getDescent();
      this.offscreen.drawString(text, (float)(xs - (double)ws / (double)2.0F), (float)(ys + (double)hs));
      this.draw();
   }

   public void text(double x, double y, String text, double degrees) {
      validate(x, "x");
      validate(y, "y");
      validate(degrees, "degrees");
      validateNotNull(text, "text");
      double xs = this.scaleX(x);
      double ys = this.scaleY(y);
      this.offscreen.rotate(Math.toRadians(-degrees), xs, ys);
      this.text(x, y, text);
      this.offscreen.rotate(Math.toRadians(degrees), xs, ys);
   }

   public void textLeft(double x, double y, String text) {
      validate(x, "x");
      validate(y, "y");
      validateNotNull(text, "text");
      this.offscreen.setFont(this.font);
      FontMetrics metrics = this.offscreen.getFontMetrics();
      double xs = this.scaleX(x);
      double ys = this.scaleY(y);
      int hs = metrics.getDescent();
      this.offscreen.drawString(text, (float)xs, (float)(ys + (double)hs));
      this.draw();
   }

   public void textRight(double x, double y, String text) {
      validate(x, "x");
      validate(y, "y");
      validateNotNull(text, "text");
      this.offscreen.setFont(this.font);
      FontMetrics metrics = this.offscreen.getFontMetrics();
      double xs = this.scaleX(x);
      double ys = this.scaleY(y);
      int ws = metrics.stringWidth(text);
      int hs = metrics.getDescent();
      this.offscreen.drawString(text, (float)(xs - (double)ws), (float)(ys + (double)hs));
      this.draw();
   }

   /** @deprecated */
   @Deprecated
   public void show(int t) {
      this.show();
      this.pause(t);
      this.enableDoubleBuffering();
   }

   public void pause(int t) {
      try {
         Thread.sleep((long)t);
      } catch (InterruptedException var3) {
         System.out.println("Error sleeping");
      }

   }

   public void show() {
      this.onscreen.setBackground(this.backgroundColor);
      this.onscreen.clearRect(0, 0, 2 * this.width, 2 * this.height);
      this.onscreen.drawImage(this.offscreenImage, 0, 0, (ImageObserver)null);
      if (this.frame.isVisible() != isJFrameVisible) {
         this.frame.setVisible(isJFrameVisible);
      }

      this.frame.repaint();
   }

   private void draw() {
      if (!this.defer) {
         this.show();
      }

   }

   public void enableDoubleBuffering() {
      this.defer = true;
   }

   public void disableDoubleBuffering() {
      this.defer = false;
   }

   public void save(String filename) {
      validateNotNull(filename, "filename");
      if (filename.length() == 0) {
         throw new IllegalArgumentException("argument to save() is the empty string");
      } else {
         File file = new File(filename);
         String suffix = filename.substring(filename.lastIndexOf(46) + 1);
         if (filename.contains(".") && suffix.length() != 0) {
            try {
               if (!ImageIO.write(this.onscreenImage, suffix, file)) {
                  BufferedImage saveImage = new BufferedImage(2 * this.width, 2 * this.height, 1);
                  saveImage.createGraphics().drawImage(this.onscreenImage, 0, 0, Color.WHITE, (ImageObserver)null);
                  if (!ImageIO.write(saveImage, suffix, file)) {
                     throw new IllegalArgumentException("the filetype '" + suffix + "' is not supported");
                  }
               }
            } catch (IOException e) {
               throw new IllegalArgumentException("could not write the file + " + filename, e);
            }
         } else {
            throw new IllegalArgumentException("the filename '" + filename + "' has no file extension, such as .jpg or .png");
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

   public void addListener(DrawListener listener) {
      this.show();
      this.listeners.add(listener);
   }

   public boolean isMousePressed() {
      synchronized(this.mouseLock) {
         return this.isMousePressed;
      }
   }

   /** @deprecated */
   @Deprecated
   public boolean mousePressed() {
      synchronized(this.mouseLock) {
         return this.isMousePressed;
      }
   }

   public double mouseX() {
      synchronized(this.mouseLock) {
         return this.mouseX;
      }
   }

   public double mouseY() {
      synchronized(this.mouseLock) {
         return this.mouseY;
      }
   }

   public void mouseEntered(MouseEvent event) {
   }

   public void mouseExited(MouseEvent event) {
   }

   public void mousePressed(MouseEvent event) {
      synchronized(this.mouseLock) {
         this.mouseX = this.userX((double)event.getX());
         this.mouseY = this.userY((double)event.getY());
         this.isMousePressed = true;
      }

      if (event.getButton() == 1) {
         for(DrawListener listener : this.listeners) {
            listener.mousePressed(this.userX((double)event.getX()), this.userY((double)event.getY()));
         }
      }

   }

   public void mouseReleased(MouseEvent event) {
      synchronized(this.mouseLock) {
         this.isMousePressed = false;
      }

      if (event.getButton() == 1) {
         for(DrawListener listener : this.listeners) {
            listener.mouseReleased(this.userX((double)event.getX()), this.userY((double)event.getY()));
         }
      }

   }

   public void mouseClicked(MouseEvent event) {
      if (event.getButton() == 1) {
         for(DrawListener listener : this.listeners) {
            listener.mouseClicked(this.userX((double)event.getX()), this.userY((double)event.getY()));
         }
      }

   }

   public void mouseDragged(MouseEvent event) {
      synchronized(this.mouseLock) {
         this.mouseX = this.userX((double)event.getX());
         this.mouseY = this.userY((double)event.getY());
      }

      for(DrawListener listener : this.listeners) {
         listener.mouseDragged(this.userX((double)event.getX()), this.userY((double)event.getY()));
      }

   }

   public void mouseMoved(MouseEvent event) {
      synchronized(this.mouseLock) {
         this.mouseX = this.userX((double)event.getX());
         this.mouseY = this.userY((double)event.getY());
      }
   }

   public boolean hasNextKeyTyped() {
      synchronized(this.keyLock) {
         return !this.keysTyped.isEmpty();
      }
   }

   public char nextKeyTyped() {
      synchronized(this.keyLock) {
         return (Character)this.keysTyped.removeLast();
      }
   }

   public boolean isKeyPressed(int keycode) {
      synchronized(this.keyLock) {
         return this.keysDown.contains(keycode);
      }
   }

   public void keyTyped(KeyEvent event) {
      synchronized(this.keyLock) {
         this.keysTyped.addFirst(event.getKeyChar());
      }

      for(DrawListener listener : this.listeners) {
         listener.keyTyped(event.getKeyChar());
      }

   }

   public void keyPressed(KeyEvent event) {
      synchronized(this.keyLock) {
         this.keysDown.add(event.getKeyCode());
      }

      for(DrawListener listener : this.listeners) {
         listener.keyPressed(event.getKeyCode());
      }

   }

   public void keyReleased(KeyEvent event) {
      synchronized(this.keyLock) {
         this.keysDown.remove(event.getKeyCode());
      }

      for(DrawListener listener : this.listeners) {
         listener.keyReleased(event.getKeyCode());
      }

   }

   public void enableTimer(int callsPerSecond) {
      this.disableTimer();
      this.timer = new Timer();
      this.timer.schedule(new MyTimerTask(), 0L, (long)((int)Math.round((double)1000.0F / (double)callsPerSecond)));
   }

   public void disableTimer() {
      if (this.timer != null) {
         this.timer.cancel();
      }

   }

   public static void main(String[] args) {
      Draw draw1 = new Draw();
      draw1.setTitle("Test client 1");
      draw1.square(0.2, 0.8, 0.1);
      draw1.filledSquare(0.8, 0.8, 0.2);
      draw1.circle(0.8, 0.2, 0.2);
      draw1.setPenColor(MAGENTA);
      draw1.setPenRadius(0.02);
      draw1.arc(0.8, 0.2, 0.1, (double)200.0F, (double)45.0F);
      Draw draw2 = new Draw();
      draw2.setCanvasSize(900, 200);
      draw2.setTitle("Test client 2");
      draw2.setPenRadius();
      draw2.setPenColor(BLUE);
      double[] x = new double[]{0.1, 0.2, 0.3, 0.2};
      double[] y = new double[]{0.2, 0.3, 0.2, 0.1};
      draw2.filledPolygon(x, y);
      draw2.setPenColor(BLACK);
      draw2.text(0.2, (double)0.5F, "black text");
      draw2.setPenColor(WHITE);
      draw2.text(0.2, 0.2, "white text");
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
      DEFAULT_FONT = new Font("SansSerif", 0, 16);
      isJFrameVisible = true;
   }

   private class MyTimerTask extends TimerTask {
      public void run() {
         for(DrawListener listener : Draw.this.listeners) {
            listener.update();
         }

      }
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
