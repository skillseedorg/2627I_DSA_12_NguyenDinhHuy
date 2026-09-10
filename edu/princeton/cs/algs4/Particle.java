package edu.princeton.cs.algs4;

import java.awt.Color;

public class Particle {
   private static final double INFINITY = Double.POSITIVE_INFINITY;
   private double rx;
   private double ry;
   private double vx;
   private double vy;
   private int count;
   private final double radius;
   private final double mass;
   private final Color color;

   public Particle(double rx, double ry, double vx, double vy, double radius, double mass, Color color) {
      if (!(radius > (double)0.0F)) {
         throw new IllegalArgumentException("radius must be positive");
      } else if (!(mass > (double)0.0F)) {
         throw new IllegalArgumentException("mass must be positive");
      } else if (!(rx - radius < (double)-1.0F) && !(rx + radius > (double)1.0F)) {
         if (!(ry - radius < (double)-1.0F) && !(ry + radius > (double)1.0F)) {
            this.vx = vx;
            this.vy = vy;
            this.rx = rx;
            this.ry = ry;
            this.radius = radius;
            this.mass = mass;
            this.color = color;
         } else {
            throw new IllegalArgumentException("out-of-bounds ry");
         }
      } else {
         throw new IllegalArgumentException("out-of-bounds rx");
      }
   }

   public Particle() {
      this.rx = StdRandom.uniformDouble((double)0.0F, (double)1.0F);
      this.ry = StdRandom.uniformDouble((double)0.0F, (double)1.0F);
      this.vx = StdRandom.uniformDouble(-0.005, 0.005);
      this.vy = StdRandom.uniformDouble(-0.005, 0.005);
      this.radius = 0.02;
      this.mass = (double)0.5F;
      this.color = Color.BLACK;
   }

   public void move(double dt) {
      this.rx += this.vx * dt;
      this.ry += this.vy * dt;
   }

   public void draw() {
      StdDraw.setPenColor(this.color);
      StdDraw.filledCircle(this.rx, this.ry, this.radius);
   }

   public int count() {
      return this.count;
   }

   public double timeToHit(Particle that) {
      if (this == that) {
         return Double.POSITIVE_INFINITY;
      } else {
         double dx = that.rx - this.rx;
         double dy = that.ry - this.ry;
         double dvx = that.vx - this.vx;
         double dvy = that.vy - this.vy;
         double dvdr = dx * dvx + dy * dvy;
         if (dvdr > (double)0.0F) {
            return Double.POSITIVE_INFINITY;
         } else {
            double dvdv = dvx * dvx + dvy * dvy;
            if (dvdv == (double)0.0F) {
               return Double.POSITIVE_INFINITY;
            } else {
               double drdr = dx * dx + dy * dy;
               double sigma = this.radius + that.radius;
               double d = dvdr * dvdr - dvdv * (drdr - sigma * sigma);
               if (d < (double)0.0F) {
                  return Double.POSITIVE_INFINITY;
               } else {
                  double t = -(dvdr + Math.sqrt(d)) / dvdv;
                  return t <= (double)0.0F ? Double.POSITIVE_INFINITY : t;
               }
            }
         }
      }
   }

   public double timeToHitVerticalWall() {
      if (this.vx > (double)0.0F) {
         return ((double)1.0F - this.rx - this.radius) / this.vx;
      } else {
         return this.vx < (double)0.0F ? (this.radius - this.rx) / this.vx : Double.POSITIVE_INFINITY;
      }
   }

   public double timeToHitHorizontalWall() {
      if (this.vy > (double)0.0F) {
         return ((double)1.0F - this.ry - this.radius) / this.vy;
      } else {
         return this.vy < (double)0.0F ? (this.radius - this.ry) / this.vy : Double.POSITIVE_INFINITY;
      }
   }

   public void bounceOff(Particle that) {
      double dx = that.rx - this.rx;
      double dy = that.ry - this.ry;
      double dvx = that.vx - this.vx;
      double dvy = that.vy - this.vy;
      double dvdr = dx * dvx + dy * dvy;
      double dist = this.radius + that.radius;
      double magnitude = (double)2.0F * this.mass * that.mass * dvdr / ((this.mass + that.mass) * dist);
      double fx = magnitude * dx / dist;
      double fy = magnitude * dy / dist;
      this.vx += fx / this.mass;
      this.vy += fy / this.mass;
      that.vx -= fx / that.mass;
      that.vy -= fy / that.mass;
      ++this.count;
      ++that.count;
   }

   public void bounceOffVerticalWall() {
      this.vx = -this.vx;
      ++this.count;
   }

   public void bounceOffHorizontalWall() {
      this.vy = -this.vy;
      ++this.count;
   }

   public double kineticEnergy() {
      return (double)0.5F * this.mass * (this.vx * this.vx + this.vy * this.vy);
   }
}
