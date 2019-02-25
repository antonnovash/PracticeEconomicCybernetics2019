import java.applet.Applet;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

@SuppressWarnings("serial")
public class Applett extends Applet {
    private Color backgroundColor;
    private Color contourColor;
    private Color fillColorGray;
    private Color fillColorDarkGray;
    private Shape triangle;
    private int unitWidth = 400;
    private int unitHeight = 300;
    private int centerX = unitWidth / 2;
    private int centerY = unitHeight / 2;
    private int median = unitHeight / 3;

    public Applett() {
    }

    @Override
    public void init() {
        backgroundColor = parseColor("bgr", Color.lightGray);
        fillColorGray = parseColor("fillColorGray", Color.gray);
        fillColorDarkGray = parseColor("fillColorDarkGray", Color.darkGray);
        contourColor = parseColor("col", Color.blue);
        triangle = new Triangle(centerX, centerY, median);
    }

    private Color parseColor(String paramName, Color defaultValue) {
        try {
            return Color.decode(getParameter(paramName));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(backgroundColor);
        g.fillRect(0, 0, getWidth(), getHeight());
        Graphics2D g2d = (Graphics2D) g;
        BufferedImage original = initOriginal();
        g2d.drawImage(original, 0, 0, null);
        ConvolveOp op = new ConvolveOp(new Kernel(3, 3, new float[]{0.0f, -0.75f, 0.0f, -0.75f, 4.0f, -0.75f, 0.0f, -0.75f, 0.0f}));
        g2d.drawImage(op.filter(original, null), unitWidth, 0, null);
    }

    private BufferedImage initOriginal() {
        BufferedImage biSrc = new BufferedImage(unitWidth, unitHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = biSrc.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setBackground(backgroundColor);
        g2d.clearRect(0, 0, unitWidth, unitHeight);
        Paint shadowPaint = new Color(80, 80, 80, 120);
        AffineTransform shadowTransform = AffineTransform.getShearInstance(-2.450, 0.0);
        shadowTransform.scale(1.0350, 0.52);
        g2d.setPaint(shadowPaint);
        g2d.translate(250, 100);
        g2d.fill(shadowTransform.createTransformedShape(triangle));
        g2d.translate(-250, -100);
        g2d.setPaint(new GradientPaint(centerX, centerY + (median >> 1), fillColorGray, centerX, centerY - median, fillColorDarkGray));
        g2d.fill(triangle);
        g2d.setColor(contourColor);
        g2d.setStroke(new BasicStroke(10));
        g2d.draw(triangle);
        Font font = new Font("Serif", Font.BOLD, 8);
        Font bigfont = font.deriveFont(AffineTransform.getScaleInstance(18.0, 18.0));
        GlyphVector gv = bigfont.createGlyphVector(g2d.getFontRenderContext(), "!");
        Shape jshape = gv.getGlyphOutline(0);
        g2d.setColor(contourColor);
        g2d.translate(175, 185);
        g2d.fill(jshape);
        g2d.translate(-175, -185);
        return biSrc;
    }
}