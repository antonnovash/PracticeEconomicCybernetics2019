import java.awt.BasicStroke;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;

public class MyStroke implements Stroke {
    BasicStroke stroke;

    public MyStroke(float width) {
        this.stroke = new BasicStroke(width);
    }

    public Shape createStrokedShape(Shape shape) {
        GeneralPath newshape = new GeneralPath(); // Start with an empty shape

        float[] coords = new float[2];
        float[] prevCoord = new float[2];
        double got = 0;
        for (PathIterator i = shape.getPathIterator(null); !i.isDone(); i.next()) {
            int type = i.currentSegment(coords);

            switch (type) {
                case PathIterator.SEG_MOVETO:
                    newshape.moveTo(coords[0], coords[1]);
                    break;

                case PathIterator.SEG_LINETO:
                    double x1 = prevCoord[0];
                    double y1 = prevCoord[1];
                    double x2 = coords[0];
                    double y2 = coords[1];

                    double dx = x2 - x1;
                    double dy = y2 - y1;

                    double length = Math.sqrt(dx * dx + dy * dy);
                    dx /= length;
                    dy /= length;
                    x1 += dx * got;
                    y1 += dy * got;
                    length -= got;
                    got = 0;

                    double step = 4.2;
                    int faza = 0;
                    while (got < length) {
                        if (faza % 2 == 0) {
                            x1 += dx * step;
                            y1 += dy * step;
                            got += step;
                            newshape.lineTo(x1, y1);
                        } else if (faza % 2 == 1) {
                            x1 += dx * step;
                            y1 += dy * step;
                            got += step;
                            newshape.lineTo(x1 + dy * step, y1 - dx * step);
                            x1 += dx * step;
                            y1 += dy * step;
                            got += step;
                            newshape.lineTo(x1, y1);
                        }
                        faza++;
                    }
                    //got -= length;

                    break;

            }

            prevCoord[0] = coords[0];
            prevCoord[1] = coords[1];
        }

        return stroke.createStrokedShape(newshape);
    }

}
