import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Shapes extends JPanel {

    private final List<ShapeObject> shapes;

    public Shapes() {
        shapes = new ArrayList<>();
        setOpaque(false);
        setLayout(null);
    }

    // Getter for shapes
    public List<ShapeObject> getShapes() {
        return shapes;
    }

    // Add a circle with optional text
    public ShapeObject addCircle(int x, int y, int radius, Color color, Color fill, String text) {
        ShapeObject circle = new ShapeObject(
                ShapeObject.Type.CIRCLE, x, y, radius, 0, 0, 0, 0, color, fill, text);
        shapes.add(circle);
        repaint();
        return circle;
    }

    // Add a rounded rectangle
    public ShapeObject addRoundedRect(int x, int y, int width, int height, int arcWidth, int arcHeight, Color color, Color fill) {
        ShapeObject rect = new ShapeObject(
                ShapeObject.Type.ROUNDED_RECT, x, y, 0, width, height, arcWidth, arcHeight, color, fill, null);
        shapes.add(rect);
        repaint();
        return rect;
    }

    // Add a line
    public ShapeObject addLine(int x1, int y1, int x2, int y2, Color color) {
        ShapeObject line = new ShapeObject(
                ShapeObject.Type.LINE, 0, 0, 0, x1, y1, x2, y2, color, null, null);
        shapes.add(line);
        repaint();
        return line;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (ShapeObject shape : shapes) {
            shape.draw(g2);
        }
    }

    public static class ShapeObject {
        public enum Type { CIRCLE, ROUNDED_RECT, LINE }

        private Type type;
        int x, y;
        private int radius;
        private int x1, y1, x2, y2;
        private int width, height, arcW, arcH;
        private Color color, fill;
        private String text;

        public ShapeObject(Type type, int x, int y, int radius, int width, int height,
                           int arcW, int arcH, Color color, Color fill, String text) {
            this.type = type;
            this.x = x;
            this.y = y;
            this.radius = radius;
            this.width = width;
            this.height = height;
            this.arcW = arcW;
            this.arcH = arcH;
            this.color = color;
            this.fill = fill;
            this.text = text;
        }

        public void setPosition(int x, int y) { this.x = x; this.y = y; }

        public void setSize(int radiusOrWidth, int heightOrUnused) {
            if (type == Type.CIRCLE) this.radius = radiusOrWidth;
            else if (type == Type.ROUNDED_RECT) { this.width = radiusOrWidth; this.height = heightOrUnused; }
        }

        public void setLine(int x1, int y1, int x2, int y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }

        public void setColor(Color color) { this.color = color; }
        public void setFill(Color fill) { this.fill = fill; }
        public void setText(String text) { this.text = text; }

        public void draw(Graphics2D g2) {
            switch (type) {
                case CIRCLE:
                    // Fill first
                    if (fill != null) {
                        g2.setColor(fill);
                        g2.fillOval(x - radius, y - radius, radius * 2, radius * 2);
                    }
                    // Outline
                    if (color != null) {
                        g2.setColor(color);
                        g2.drawOval(x - radius, y - radius, radius * 2, radius * 2);
                    }
                    // Text
                    if (text != null) {
                        g2.setFont(new Font("SansSerif", Font.BOLD, 12)); // fixed bold font
                        FontMetrics fm = g2.getFontMetrics();
                        int textWidth = fm.stringWidth(text);
                        int textHeight = fm.getAscent() + fm.getDescent();
                        int textX = x - textWidth / 2;
                        int textY = y + (fm.getAscent() - fm.getDescent()) / 2;
                        g2.setColor(Color.BLACK);
                        g2.drawString(text, textX, textY);
                    }
                    break;

                case ROUNDED_RECT:
                    if (fill != null) {
                        g2.setColor(fill);
                        g2.fillRoundRect(x, y, width, height, arcW, arcH);
                    }
                    if (color != null) {
                        g2.setColor(color);
                        g2.drawRoundRect(x, y, width, height, arcW, arcH);
                    }
                    break;

                case LINE:
                    if (color != null) {
                        g2.setColor(color);
                        g2.drawLine(x1, y1, x2, y2);
                    }
                    break;
            }
        }
    }
}
