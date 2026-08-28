package jboids;

public class Boid {

    private double x, y;
    private double angleRadians;

    public Boid(double x, double y, double angleRadians) {
        this.x = x;
        this.y = y;
        this.angleRadians = angleRadians;

    }

    public void updatePosition(int width, int height) {
        x += 2;
        y++;
        if (x > width)
            x -= width;
        if (y > height)
            y -= height;

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

}
