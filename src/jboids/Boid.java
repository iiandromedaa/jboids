package jboids;

import static jboids.Settings.*;

import java.util.ArrayList;
import java.util.List;

public class Boid {

    private double x, y;
    private double angleRadians;

    private boolean goalExists;
    private int goalX;
    private int goalY;

    public Boid(double x, double y, double angleRadians) {
        this.x = x;
        this.y = y;
        this.angleRadians = angleRadians;
    }

    public void updatePosition(int width, int height, List<Boid> boids) {
        List<Boid> flockmates = getNearby(boids, BOID_SIGHT, width, height);
        List<Boid> dangerZone = getNearby(boids, BOID_AVOID, width, height);

        separation(dangerZone, width, height);
        alignment(flockmates);
        cohesion(flockmates, width, height);
        if (goalExists)
            goal(width, height);

        x += BOID_SPEED * Math.cos(angleRadians);
        y += BOID_SPEED * Math.sin(angleRadians);

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
    private void separation(List<Boid> boids, int width, int height) {
        if (boids.isEmpty())
            return;
        for (Boid boid : boids) {
            double dx = wrappedDistance(boid.getX(), getX(), width);
            double dy = wrappedDistance(boid.getY(), getY(), height);
            // angle directly opposite other boid
            double awayAngle = Math.atan2(dy, dx);
            // difference between current angle and target
            double dAngle = Math.atan2(
                Math.sin(awayAngle - angleRadians),
                Math.cos(awayAngle - angleRadians)    
            );
            double distSq = (dx * dx) + (dy * dy);
            double steerStrength = AVOIDANCE_STEER_COEFF / (Math.sqrt(distSq) + 1);
            // extra steer given to separation, for fun mostly
            angleRadians += Math.clamp(
                dAngle, 
                -steerStrength * STEER_AMOUNT, 
                steerStrength * STEER_AMOUNT
            ); 
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
    private void cohesion(List<Boid> boids, int width, int height) {
        if (boids.isEmpty())
            return;
        double xSum = 0;
        double ySum = 0;
        for (Boid boid : boids) {
            xSum += wrappedDistance(getX(), boid.getX(), width);
            ySum += wrappedDistance(getY(), boid.getY(), height);
        }
        // angle towards average position of flockmates
        double targetAng = Math.atan2(ySum, xSum);
        // difference between current angle and target
        double dAngle = Math.atan2(
            Math.sin(targetAng - angleRadians),
            Math.cos(targetAng - angleRadians)    
        );
        angleRadians += Math.clamp(dAngle, -STEER_AMOUNT, STEER_AMOUNT);
    }

    private void goal(int width, int height) {
        // angle towards target position
        double targetAng = Math.atan2(
            wrappedDistance(getY(), goalY, height), 
            wrappedDistance(getX(), goalX, width)
        );
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
            double distance = Math.sqrt(
                Math.pow(wrappedDistance(getX(), boid.getX(), width), 2)
                + Math.pow(wrappedDistance(getY(), boid.getY(), height), 2)
            );
            // the choice to ignore boids of distance 0 is because the boid rules are deterministic
            // so, boids with the same coordinate and same heading are essentially the same boid
            if (distance <= radius && distance > 0)
                nearby.add(boid);
        }
        return nearby;
    }

    public static double wrappedDistance(double a, double b, double size) {
        double dx = b - a;
        if (dx > (size / 2))
            dx -= size;
        else if (dx < (-size / 2))
            dx += size;

        return dx;
    }

    public void setGoal(int x, int y) {
        goalExists = true;
        goalX = x;
        goalY = y;
    }

    public void clearGoal() {
        goalExists = false;
    }

}
