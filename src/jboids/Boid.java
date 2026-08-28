package jboids;

import static jboids.Settings.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Boid {

    private double x, y;
    private double angleRadians;

    public Boid(double x, double y, double angleRadians) {
        this.x = x;
        this.y = y;
        this.angleRadians = angleRadians;
    }

    public void updatePosition(int width, int height, List<Boid> boids) {
        List<Boid> flockmates = getNearby(boids, BOID_SIGHT, width, height);
        List<Boid> dangerZone = getNearby(boids, BOID_AVOID, width, height);

        // separation(dangerZone);
        // alignment(flockmates);
        cohesion(flockmates);

        x += BOID_SPEED * Math.cos(angleRadians);
        y += BOID_SPEED * Math.sin(angleRadians);

        // angleRadians += new Random().nextDouble(-STEER_AMOUNT, STEER_AMOUNT);

        if (x > width)
            x -= width;
        if (x < 0)
            x += width;

        if (y > height)
            y -= height;
        if (y < 0)
            y += height;
    }
    
    /**
     * if there are any boids in our avoid radius, steer away from them!
     */
    private void separation(List<Boid> boids) {
        if (boids.isEmpty())
            return;
        for (Boid boid : boids) {
            // angle directly opposite other boid
            double awayAngle = Math.atan2(getY() - boid.getY(), getX() - boid.getX());
            // difference between current angle and target
            double dAngle = Math.atan2(
                Math.sin(awayAngle - angleRadians),
                Math.cos(awayAngle - angleRadians)    
            );
            angleRadians += Math.clamp(dAngle, -STEER_AMOUNT, STEER_AMOUNT);   
        }
    }

    /**
     * try to match the average heading of boids we can see
     */
    private void alignment(List<Boid> boids) {
        if (boids.isEmpty())
            return;
        // mean of sines
        double sineSum = 0;
        for (Boid boid : boids) {
            sineSum += Math.sin(boid.getAngle());
        }
        // mean of cosines
        double cosineSum = 0;
        for (Boid boid : boids) {
            cosineSum += Math.cos(boid.getAngle());
        }
        double averageAngle = Math.atan2(sineSum / boids.size(), cosineSum / boids.size());
        // difference between current angle and target
        double dAngle = Math.atan2(
            Math.sin(averageAngle - angleRadians),
            Math.cos(averageAngle - angleRadians)    
        );
        angleRadians += Math.clamp(dAngle, -STEER_AMOUNT, STEER_AMOUNT);
    }

    /**
     * steer towards average position of flockmates
     */
    private void cohesion(List<Boid> boids) {
        if (boids.isEmpty())
            return;
        double xSum = 0;
        double ySum = 0;
        for (Boid boid : boids) {
            xSum += boid.getX() - getX();
            ySum += boid.getY() - getY();
        }
        // angle towards average position of flockmates
        double targetAng = Math.atan2(ySum / boids.size() - getY(), 
            xSum / boids.size() - getX());
        // difference between current angle and target
        double dAngle = Math.atan2(
            Math.sin(targetAng - angleRadians),
            Math.cos(targetAng - angleRadians)    
        );
        angleRadians += Math.clamp(dAngle, -STEER_AMOUNT, STEER_AMOUNT);
    }



    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getAngle() {
        return angleRadians;
    }

    public List<Boid> getNearby(List<Boid> boids, int radius, int width, int height) {
        List<Boid> nearby = new ArrayList<>();
        for (Boid boid : boids) {
            double dx = Math.abs(boid.getX() - getX());
            if (dx > (width / 2))
                dx = width - dx;
            double dy = Math.abs(boid.getY() - getY());
            if (dy > (width / 2))
                dy = width - dy;
            double distance = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
            // the choice to ignore boids of distance 0 is because the boid rules are deterministic
            // so, boids with the same coordinate and same heading are essentially the same boid
            if (distance <= radius && distance > 0)
                nearby.add(boid);
        }
        return nearby;
    }

    public static double wrappedDistance(double a, double b, double size) {
        double dx = Math.abs(b - a);
        if (dx > (size / 2))
            dx = size - dx;
        
        return dx;
    }

}
