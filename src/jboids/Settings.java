package jboids;
public class Settings {
    
    private Settings() {}

    /**
     * size defining half of boids width (one third of boids height) when rendered
     */
    public static final int BOID_SIZE = 15;
    /**
     * radius for how far boid is able to consider other boids (in cohesion and alignment for example) (in pixels)
     */
    public static final int BOID_SIGHT = 500;
    /**
     * radius for how close another boid needs to be before boid begins steering away (in pixels)
     */
    public static final int BOID_AVOID = 150;
    /**
     * how fast boids move in pixels per frame
     */
    public static final int BOID_SPEED = 5;
    /**
     * the maximum speed boids can steer, in radians per frame
     */
    public static final double STEER_AMOUNT = 0.1;
    /**
     * whether or not to draw circles for the sight and avoidance radii
     */
    public static final boolean DRAW_RADII = true;
    /**
     * whether or not to draw a line between boids when they can see each other
     */
    public static final boolean DRAW_LINE_TO_FLOCKMATES = true;

}
