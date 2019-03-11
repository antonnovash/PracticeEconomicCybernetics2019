import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class CustomShape implements Shape {
    double a;

    public CustomShape(double a) {
        this.a = a;
    }

    public Rectangle getBounds() {
        return new Rectangle();
    }

    public Rectangle2D getBounds2D() {
        return new Rectangle();
    }

    public boolean contains(double x, double y) {
        return false;
    }

    public boolean contains(Point2D p) {
        return false;
    }

    public boolean contains(Rectangle2D r) {
        return false;
    }

    public boolean contains(double x, double y, double w, double h) {
        return false;
    }

    public boolean intersects(double x, double y, double w, double h) {

        return false;
    }

    public boolean intersects(Rectangle2D r) {
        return intersects(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    class ListIterator implements PathIterator {
        AffineTransform transform;
        boolean done = false;
        double angle = 0;

        public ListIterator(AffineTransform transform) {
            this.transform = transform;
        }

        public int getWindingRule() {
            return WIND_NON_ZERO;
        }

        public boolean isDone() {
            return done;
        }

        public int currentSegment(float[] coords) {
            double[] doubleCoords = new double[2];
            int result = currentSegment(doubleCoords);
            coords[0] = (float) doubleCoords[0];
            coords[1] = (float) doubleCoords[1];
            return result;
        }

        public int currentSegment(double[] coords) {
            coords[0] = a * Math.cos(angle * Math.PI / 180.0) * Math.cos(3 * angle * Math.PI / 180.0);
            coords[1] = a * Math.sin(angle * Math.PI / 180.0) * Math.cos(3 * angle * Math.PI / 180.0);
            if (angle > 360)
                done = true;

            if (transform != null)
                transform.transform(coords, 0, coords, 0, 1);

            if (angle < 1e-5)
                return SEG_MOVETO;

            return SEG_LINETO;
        }

        public void next() {
            if (done)
                return;
            angle += 1;
        }
    }

    @Override
    public PathIterator getPathIterator(AffineTransform arg0) {
        return new ListIterator(arg0);
    }

    @Override
    public PathIterator getPathIterator(AffineTransform arg0, double arg1) {
        return new ListIterator(arg0);
    }
}