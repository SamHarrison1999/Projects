import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BelishaBeacon {

    public class Drawing extends JPanel {

        private int x = 275;
        private int y = 95;
        private boolean flashing = false;

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            // Shapes
            g2.drawRect(295, 145, 10, 40);
            g2.drawRect(295, 185, 10, 40);
            g2.drawRect(295, 225, 10, 40);
            g2.drawRect(295, 265, 10, 40);
            g2.drawRect(295, 305, 10, 40);
            g2.drawRect(295, 345, 10, 40);
            g2.drawRect(295, 385, 10, 40);
            g2.drawRect(295, 425, 10, 40);
            g2.drawOval(x, y, 50, 50);
            g2.setColor(Color.BLACK);
            g2.fillRect(295, 145, 10, 40);
            g2.fillRect(295, 225, 10, 40);
            g2.fillRect(295, 305, 10, 40);
            g2.fillRect(295, 385, 10, 40);
            g2.setColor(Color.ORANGE);
            g2.fillOval(x, y, 50, 50);

            flashing= !flashing;

            if (flashing) {
                g2.setColor(Color.lightGray);
                g2.fillOval(x, y, 50, 50);
            }
        }

        public void flashing(){
            flashing = true;
            repaint();
        }
    }

    private Timer timer;

    public BelishaBeacon() {
        JFrame f = new JFrame("Belisha Beacon");
        f.setSize(600,600);
        f.setLayout(new BorderLayout(0, 0));
        final Drawing shapes = new Drawing();

        timer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shapes.repaint();
            }
        });

        JButton flash = new JButton("Flashing");
        flash.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.start();
            }
        });

        JButton steady = new JButton("Steady");
        steady.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.stop();
                shapes.flashing();
            }
        });
        // Positioning
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(1, 2, 1, 0));
        p.add(flash);
        p.add(steady);

        f.add(p, BorderLayout.SOUTH);
        f.add(shapes);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
        f.setResizable(false);

        timer.start();
    }
    public static void main(String[] args){
        new BelishaBeacon();
    }
}