import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


public class MyShape implements Shape {

    private Shape ellipse;
    private Area shapeArea = new Area();
    private double angle = 0;
    private final int width, height;

    public MyShape(int width, int height) throws IllegalArgumentException {
        if(width <= 0 || height <= 0)
            throw new IllegalArgumentException("0 or negative value is passed as an argument");

        this.width = width;
        this.height = height;
        setAngle(angle);
    }

    public void setAngle(double angle) {
        this.angle = angle;

        ellipse = AffineTransform.getRotateInstance(-angle, width / 2, height / 2).createTransformedShape(new Ellipse2D.Double(0, 0, width, height));

        if(!shapeArea.isEmpty())
            shapeArea.reset();
        shapeArea.add(new Area(ellipse));
    }

    @Override
    public boolean contains(Point2D arg0) {
        return shapeArea.contains(arg0);
    }

    @Override
    public boolean contains(Rectangle2D arg0) {
        return shapeArea.contains(arg0);
    }

    @Override
    public boolean contains(double arg0, double arg1) {
        return shapeArea.contains(arg0, arg1);
    }

    @Override
    public boolean contains(double arg0, double arg1, double arg2, double arg3) {
        return shapeArea.contains(arg0, arg1, arg2, arg3);
    }

    @Override
    public Rectangle getBounds() {
        return shapeArea.getBounds();
    }

    @Override
    public Rectangle2D getBounds2D() {
        return shapeArea.getBounds2D();
    }

    @Override
    public PathIterator getPathIterator(AffineTransform arg0) {
        return shapeArea.getPathIterator(arg0);
    }

    @Override
    public PathIterator getPathIterator(AffineTransform arg0, double arg1) {
        return shapeArea.getPathIterator(arg0, arg1);
    }

    @Override
    public boolean intersects(Rectangle2D arg0) {
        return shapeArea.intersects(arg0);
    }

    @Override
    public boolean intersects(double arg0, double arg1, double arg2, double arg3) {
        return shapeArea.intersects(arg0, arg1, arg2, arg3);
    }

}
