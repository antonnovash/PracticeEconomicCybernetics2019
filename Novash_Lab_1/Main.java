import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

import javax.swing.*;


public class Main {
    static final int WINDOW_WIDTH = 600;
    static final int WINDOW_HEIGHT = 700;
    static final int SHAPE_WIDTH = 500;
    static final int SHAPE_HEIGHT = 350;

    static MyShape shape = new MyShape(SHAPE_WIDTH, SHAPE_HEIGHT);
    static AffineTransform transform;
    static double degAngle = 0;
    static double radAngle;
    static Stroke stroke;
    private static Color BACKGROUND_COLOR = new Color(255,255,255), SHAPE_COLOR, FILL_COLOR;

    public static void main(String[] args) {
        stroke = new BasicStroke(Float.parseFloat(args[0]));
        SHAPE_COLOR = new Color(Integer.parseInt(args[1], 16));
        FILL_COLOR = new Color(Integer.parseInt(args[2], 16));

        JFrame frame = new JFrame();
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        final JPanel panel = new JPanel() {
            private static final long serialVersionUID = 1L;

            public void paint(Graphics g) {
                g.setColor(BACKGROUND_COLOR);
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(SHAPE_COLOR);
                Graphics2D g2d = (Graphics2D)g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                //translating shape so its center matches the frame center
                g2d.translate(getWidth() / 2 - shape.getBounds2D().getCenterX(), getHeight() / 2  - shape.getBounds2D().getCenterY());

                g2d.setStroke(stroke);
                g2d.draw(shape);
                g.setColor(FILL_COLOR);
                g2d.fill(shape);
            }
        };

        Timer timer = new Timer(5, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if(degAngle < 360)
                    degAngle += 1;
                else
                    degAngle = 1;
                radAngle = degAngle * Math.PI / 180;
                shape.setAngle(radAngle);

                panel.revalidate();
                panel.repaint();
            }
        });
        timer.start();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}
