
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.Serializable;

public class CustomShape implements Shape, Serializable, Cloneable, Transferable {
    private double x, y;
    private int a;

    CustomShape(int a, double x, double y) {
        this.a = a;
        this.x = x;
        this.y = y;
    }

    public Rectangle getBounds() {
        return new Rectangle((int)(x - a), (int)(y - a), 2 * a, 2 * a);
    }

    public Rectangle2D getBounds2D() {
        return new Rectangle.Float((float)(x - a), (float)(y - a), 2 * a, 2 * a);
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
        return getBounds().intersects(x, y, w, h);
    }

    public boolean intersects(Rectangle2D r) {
        return getBounds().contains(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    class ListIterator implements PathIterator {
        AffineTransform transform;
        boolean done = false;
        double angle = 0;

        ListIterator(AffineTransform transform) {
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
            coords[0] = x + a * Math.cos(angle * Math.PI / 180.0) * Math.cos(3 * angle * Math.PI / 180.0);
            coords[1] = y + a * Math.sin(angle * Math.PI / 180.0) * Math.cos(3 * angle * Math.PI / 180.0);
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


    // ---------------- new in lab 05 ---------------------

    static DataFlavor decDataFlavor = new DataFlavor(CustomShape.class, "Witch of Agnesi");

    // This is a list of the flavors we know how to work with
    private static DataFlavor[] supportedFlavors = {decDataFlavor, DataFlavor.stringFlavor};

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return supportedFlavors.clone();
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor dataFlavor) {
        return (dataFlavor.equals(decDataFlavor) || dataFlavor.equals(DataFlavor.stringFlavor));
    }

    @Override
    public Object getTransferData(DataFlavor dataFlavor) throws UnsupportedFlavorException {
        if (dataFlavor.equals(decDataFlavor)) {
            return this;
        } else if (dataFlavor.equals(DataFlavor.stringFlavor)) {
            return toString();
        } else
            throw new UnsupportedFlavorException(dataFlavor);
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) { // This should never happen
            return this;
        }
    }

    @Override
    public String toString() {
        return a + " " + this.x + " " + this.y ;
    }


    static CustomShape getFromString(String line) {

        String[] arr = line.split(" ");
        return new CustomShape(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]),
                Integer.parseInt(arr[2]));
    }

    void translate(int x, int y) {
        this.x += x;
        this.y += y;
    }
}
