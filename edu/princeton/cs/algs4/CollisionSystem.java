package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.Particle;
import java.awt.Color;

public class CollisionSystem {
   private static final double HZ = (double)0.5F;
   private MinPQ<Event> pq;
   private double t = (double)0.0F;
   private Particle[] particles;

   public CollisionSystem(Particle[] particles) {
      this.particles = particles.clone();
   }

   private void predict(Particle a, double limit) {
      if (a != null) {
         for(int i = 0; i < this.particles.length; ++i) {
            double dt = a.timeToHit(this.particles[i]);
            if (this.t + dt <= limit) {
               this.pq.insert(new Event(this.t + dt, a, this.particles[i]));
            }
         }

         double dtX = a.timeToHitVerticalWall();
         double dtY = a.timeToHitHorizontalWall();
         if (this.t + dtX <= limit) {
            this.pq.insert(new Event(this.t + dtX, a, (Particle)null));
         }

         if (this.t + dtY <= limit) {
            this.pq.insert(new Event(this.t + dtY, (Particle)null, a));
         }

      }
   }

   private void redraw(double limit) {
      StdDraw.clear();

      for(int i = 0; i < this.particles.length; ++i) {
         this.particles[i].draw();
      }

      StdDraw.show();
      StdDraw.pause(20);
      if (this.t < limit) {
         this.pq.insert(new Event(this.t + (double)2.0F, (Particle)null, (Particle)null));
      }

   }

   public void simulate(double limit) {
      this.pq = new MinPQ<Event>();

      for(int i = 0; i < this.particles.length; ++i) {
         this.predict(this.particles[i], limit);
      }

      this.pq.insert(new Event((double)0.0F, (Particle)null, (Particle)null));

      while(!this.pq.isEmpty()) {
         Event e = this.pq.delMin();
         if (e.isValid()) {
            Particle a = e.a;
            Particle b = e.b;

            for(int i = 0; i < this.particles.length; ++i) {
               this.particles[i].move(e.time - this.t);
            }

            this.t = e.time;
            if (a != null && b != null) {
               a.bounceOff(b);
            } else if (a != null && b == null) {
               a.bounceOffVerticalWall();
            } else if (a == null && b != null) {
               b.bounceOffHorizontalWall();
            } else if (a == null && b == null) {
               this.redraw(limit);
            }

            this.predict(a, limit);
            this.predict(b, limit);
         }
      }

   }

   public static void main(String[] args) {
      StdDraw.setCanvasSize(600, 600);
      StdDraw.enableDoubleBuffering();
      Particle[] particles;
      if (args.length == 1) {
         int n = Integer.parseInt(args[0]);
         particles = new Particle[n];

         for(int i = 0; i < n; ++i) {
            particles[i] = new Particle();
         }
      } else {
         int n = StdIn.readInt();
         particles = new Particle[n];

         for(int i = 0; i < n; ++i) {
            double rx = StdIn.readDouble();
            double ry = StdIn.readDouble();
            double vx = StdIn.readDouble();
            double vy = StdIn.readDouble();
            double radius = StdIn.readDouble();
            double mass = StdIn.readDouble();
            int r = StdIn.readInt();
            int g = StdIn.readInt();
            int b = StdIn.readInt();
            Color color = new Color(r, g, b);
            particles[i] = new Particle(rx, ry, vx, vy, radius, mass, color);
         }
      }

      CollisionSystem system = new CollisionSystem(particles);
      system.simulate((double)10000.0F);
   }

   private static class Event implements Comparable<Event> {
      private final double time;
      private final Particle a;
      private final Particle b;
      private final int countA;
      private final int countB;

      public Event(double t, Particle a, Particle b) {
         this.time = t;
         this.a = a;
         this.b = b;
         if (a != null) {
            this.countA = a.count();
         } else {
            this.countA = -1;
         }

         if (b != null) {
            this.countB = b.count();
         } else {
            this.countB = -1;
         }

      }

      public int compareTo(Event that) {
         return Double.compare(this.time, that.time);
      }

      public boolean isValid() {
         if (this.a != null && this.a.count() != this.countA) {
            return false;
         } else {
            return this.b == null || this.b.count() == this.countB;
         }
      }
   }
}
