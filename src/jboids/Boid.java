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
        List<Boid> flockmates = getNearbyBoids(boids, BOID_SIGHT);
        List<Boid> dangerZone = getNearbyBoids(boids, BOID_AVOID);

        separation(dangerZone);
        alignment(flockmates);
        cohesion(flockmates);

        x += BOID_SPEED * Math.cos(angleRadians);
        y += BOID_SPEED * Math.sin(angleRadians);

        angleRadians += new Random().nextDouble(-STEER_AMOUNT, STEER_AMOUNT);

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
        
    }

    /**
     * try to match the average heading of boids we can see
     */
    private void alignment(List<Boid> boids) {
        
    }

    /**
     * steer towards average position of flockmates
     */
    private void cohesion(List<Boid> boids) {
        
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

    public List<Boid> getNearbyBoids(List<Boid> boids, int radius) {
        List<Boid> nearby = new ArrayList<>();
        for (Boid boid : boids) {
            double distance = Math.sqrt(
                Math.pow((boid.getX() - getX()), 2)
                + Math.pow((boid.getY() - getY()), 2)
            );
            // the choice to ignore boids of distance 0 is because the boid rules are deterministic
            // so, boids with the same coordinate and same heading are essentially the same boid
            if (distance <= radius && distance > 0)
                nearby.add(boid);
        }
        return nearby;
    }

}
