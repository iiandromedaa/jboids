package jboids.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import jboids.Boid;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static jboids.Settings.*;

public class JBoidsGUI {

    public JBoidsGUI() {
        JFrame frame = new JFrame("JBoids");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new JBoidsCanvas());
        frame.setUndecorated(true);
        frame.setVisible(true);
    }

    private class JBoidsCanvas extends JPanel implements ActionListener {

        private List<Boid> boids = new ArrayList<>();
        private Timer timer;

        private static final int[] X_POINTS = {-BOID_SIZE, 2*BOID_SIZE, -BOID_SIZE};
        private static final int[] Y_POINTS = {BOID_SIZE, 0, -BOID_SIZE};

        JBoidsCanvas() {
            timer = new Timer(16, this);
            timer.start();
            Random random = new Random();
            for (int i = 0; i < 15; i++) {
                boids.add(new Boid(random.nextInt(0, 500), random.nextInt(0, 500), random.nextInt(0, 6)));
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());

            for (Boid boid : boids) {
                drawBoid(boid, g);
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            for (Boid boid : boids) {
                boid.updatePosition(getWidth(), getHeight(), boids);
                if (LOG_BOIDS)
                    System.out.println(boid.getX() + "x, " + 
                        boid.getY() + "y, " + boid.getAngle() + " rad");
            }
            repaint();
            Toolkit.getDefaultToolkit().sync();
        }

        private void drawBoid(Boid boid, Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();

            if (DRAW_LINE_TO_FLOCKMATES) {
                g2d.setColor(Color.WHITE);
                for (Boid b : boids) {
                    for (Boid nearby : b.getNearby(boids, BOID_AVOID, getWidth(), getHeight())) {
                        g2d.drawLine(
                            (int)b.getX(), 
                            (int)b.getY(), 
                            (int)nearby.getX(), 
                            (int)nearby.getY()
                        );
                    }
                }
            }

            g2d.translate(boid.getX(), boid.getY());
            g2d.rotate(boid.getAngle());

            g2d.setColor(Color.WHITE);
            g2d.fillPolygon(X_POINTS, Y_POINTS, 3);

            g2d.setColor(Color.RED);
            g2d.fillOval(-3, -3, 6, 6);

            if (DRAW_RADII) {
                g2d.setColor(Color.WHITE);
                g2d.drawOval(-BOID_SIGHT/2, -BOID_SIGHT/2, BOID_SIGHT, BOID_SIGHT);

                g2d.setColor(Color.YELLOW);
                g2d.drawOval(-BOID_AVOID/2, -BOID_AVOID/2, BOID_AVOID, BOID_AVOID);
            }

            g2d.dispose();
        }

    }
    
}
