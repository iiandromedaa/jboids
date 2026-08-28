package jboids.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import jboids.Boid;
import static jboids.Settings.BOID_SIZE;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class JBoidsGUI {

    public JBoidsGUI() {
        JFrame frame = new JFrame("JBoids");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new JBoidsCanvas());
        frame.setVisible(true);
    }

    private class JBoidsCanvas extends JPanel implements ActionListener {

        private List<Boid> boids = new ArrayList<>();
        private Timer timer;

        private static final int[] X_POINTS = {-BOID_SIZE, 0, BOID_SIZE};
        private static final int[] Y_POINTS = {BOID_SIZE, -2*BOID_SIZE, BOID_SIZE};

        JBoidsCanvas() {
            timer = new Timer(16, this);
            timer.start();
            boids.add(new Boid(100, 100, 0));
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
                boid.updatePosition(getWidth(), getHeight());
            }
            repaint();
            Toolkit.getDefaultToolkit().sync();
        }

        private void drawBoid(Boid boid, Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();

            g2d.translate(boid.getX(), boid.getY());
            g2d.rotate(boid.getAngle());

            g2d.setColor(Color.WHITE);
            g2d.fillPolygon(X_POINTS, Y_POINTS, 3);

            g2d.setColor(Color.RED);
            g2d.fillOval(-3, -3, 6, 6);
            g2d.dispose();
        }

    }
    
}
