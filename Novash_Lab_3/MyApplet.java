import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;


public class MyApplet extends Applet {

    public void init() {
        setSize(700, 700);
        setBackground(new Color(124, 182, 255));
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.translate(300, 350);
        g2.setColor(new Color(64, 64, 64));
        CustomShape s = new CustomShape(250);
        g2.setStroke(new MyStroke(3f));
        g2.draw(s);
    }
}