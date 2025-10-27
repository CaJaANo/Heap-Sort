import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Shapes extends JPanel {
    private final List<Shape> shapes = new ArrayList<>();

    public Shapes() {
        setOpaque(false);
        setLayout(null);
    }

    public List<Shape> getShapes() { return shapes; }

    public Shape addCircle(int x, int y, int r, Color border, Color fill, String text) {
        Shape s = new Shape(Shape.Type.CIRCLE, x, y, r, 0, 0, 0, 0, border, fill, text);
        shapes.add(s);
        repaint();
        return s;
    }

    public Shape addLine(int x1, int y1, int x2, int y2, Color color) {
        Shape s = new Shape(Shape.Type.LINE, 0, 0, 0, x1, y1, x2, y2, color, null, null);
        shapes.add(s);
        repaint();
        return s;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (Shape s : shapes) s.draw(g2);
    }

    public static class Shape {
        public enum Type { CIRCLE, LINE }

        private final Type type;
        private int x, y, r;
        private int x1, y1, x2, y2;
        private Color border, fill;
        private String text;

        public Shape(Type type, int x, int y, int r,
                     int a, int b, int c, int d, Color border, Color fill, String text) {
            this.type = type;
            this.x = x; this.y = y; this.r = r;
            this.x1 = a; this.y1 = b; this.x2 = c; this.y2 = d;
            this.border = border; this.fill = fill; this.text = text;
        }

        public Shape(Type type, int x, int y, int r, int x1, int y1, int x2, Color border, Color fill, String text) {
            this(type, x, y, r, x1, y1, x2, 0, border, fill, text);
        }

        public int getX() { return x; }
        public int getY() { return y; }

        public void setPos(int x, int y) { this.x = x; this.y = y; }
        public void setLine(int x1, int y1, int x2, int y2) { this.x1 = x1; this.y1 = y1; this.x2 = x2; this.y2 = y2; }
        public void setBorder(Color c) { this.border = c; }
        public void setFill(Color f) { this.fill = f; }
        public void setText(String t) { this.text = t; }

        public void draw(Graphics2D g2) {
            if (type == Type.CIRCLE) {
                if (fill != null) { g2.setColor(fill); g2.fillOval(x - r, y - r, r * 2, r * 2); }
                if (border != null) { g2.setColor(border); g2.drawOval(x - r, y - r, r * 2, r * 2); }
                if (text != null) {
                    g2.setFont(new Font("SansSerif", Font.BOLD, 12));
                    FontMetrics fm = g2.getFontMetrics();
                    int tw = fm.stringWidth(text);
                    int th = fm.getAscent();
                    g2.setColor(Color.BLACK);
                    g2.drawString(text, x - tw / 2, y + (th / 2) - 1);
                }
            } else {
                if (border != null) { g2.setColor(border); g2.drawLine(x1, y1, x2, y2); }
            }
        }
    }
}