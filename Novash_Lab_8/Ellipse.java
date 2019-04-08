//package bsu.fpmi.educational_practice;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;


public class Ellipse extends Canvas {

    private static final long serialVersionUID = 1L;
    private double height = 10.0, width = 10.0;
    private Color color = new Color(0, 0, 0);
    private double x = 100.0, y = 100.0;

    public Ellipse() {
    }

    public Ellipse(double height, double width, Color color) {
        this.height = height;
        this.width = width;
        this.color = color;
    }

    public Ellipse(double height, double width) {
        this.height = width;
        this.width = width;
    }

    public Ellipse(Color color) {
        this.color = color;
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Shape ellipse = new Ellipse2D.Double(this.x, this.y, this.width, this.height);
        g2.setColor(this.color);
        g2.draw(ellipse);
        g2.fill(ellipse);
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.width = height;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setX_START_COORDINATE(double x_START_COORDINATE) {
        this.x = x_START_COORDINATE;
    }


    public void setY_COORDINATE(double y_COORDINATE) {
        this.y = y_COORDINATE;
    }

}