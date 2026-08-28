package jboids;
import javax.swing.SwingUtilities;

import jboids.gui.JBoidsGUI;

public class App {

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new JBoidsGUI();
            }
        });
    }

}
