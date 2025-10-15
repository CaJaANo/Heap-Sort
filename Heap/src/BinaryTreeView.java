import javax.swing.*;
import java.awt.*;

public class BinaryTreeView extends JPanel {

    private Shapes shapesContainer;
    private int[] values = new int[0];
    private int nodeRadius = 20;
    private int verticalSpacing = 80;

    private Shapes.ShapeObject[] circles;
    private Shapes.ShapeObject[] lines;

    private int highlight1 = -1; // index of first highlighted node
    private int highlight2 = -1; // index of second highlighted node

    public BinaryTreeView() {
        setLayout(null);
        setOpaque(false);

        shapesContainer = new Shapes();
        shapesContainer.setBounds(0, 0, 800, 400);
        add(shapesContainer);

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                if (values.length > 0) updatePositions();
            }
        });
    }

    public void updateTree(int[] values) {
        this.values = values;
        this.highlight1 = -1;
        this.highlight2 = -1;
        drawTree();
    }

    // Overloaded: update tree with highlighted nodes
    public void updateTree(int[] values, int highlight1, int highlight2) {
        this.values = values;
        this.highlight1 = highlight1;
        this.highlight2 = highlight2;
        drawTree();
    }

    private void drawTree() {
        shapesContainer.getShapes().clear();
        shapesContainer.repaint();

        if (values.length == 0) return;

        int lineCount = Math.max(0, values.length - 1);
        lines = new Shapes.ShapeObject[lineCount];
        circles = new Shapes.ShapeObject[values.length];

        // Lines first
        for (int i = 0; i < lineCount; i++) {
            lines[i] = shapesContainer.addLine(0, 0, 0, 0, Color.BLACK);
        }

        // Circles after
        for (int i = 0; i < values.length; i++) {
            Color fillColor = (i == highlight1 || i == highlight2) ? Color.YELLOW : Color.WHITE;
            Color outline = Color.BLACK; // outline same as fill
            circles[i] = shapesContainer.addCircle(0, 0, nodeRadius, outline, fillColor, String.valueOf(values[i]));
        }

        updatePositions();
    }

    private void updatePositions() {
        int panelWidth = getWidth();
        int nodeCount = values.length;

        if (nodeCount == 0) return;

        int depth = (int) (Math.log(nodeCount) / Math.log(2)) + 1;
        int[] xPositions = new int[nodeCount];
        int[] yPositions = new int[nodeCount];

        for (int i = 0; i < nodeCount; i++) {
            int level = (int) (Math.log(i + 1) / Math.log(2));
            int indexInLevel = i - ((1 << level) - 1);
            int nodesAtLevel = 1 << level;
            int spacing = panelWidth / (nodesAtLevel + 1);
            xPositions[i] = spacing * (indexInLevel + 1);
            yPositions[i] = 30 + level * verticalSpacing;
        }

        for (int i = 0; i < nodeCount; i++) {
            circles[i].setPosition(xPositions[i], yPositions[i]);
        }

        // Lines (center to center)
        int lineIndex = 0;
        for (int i = 0; i < nodeCount; i++) {
            int parentX = xPositions[i];
            int parentY = yPositions[i];

            int leftIndex = 2 * i + 1;
            if (leftIndex < nodeCount && lineIndex < lines.length)
                lines[lineIndex++].setLine(parentX, parentY, xPositions[leftIndex], yPositions[leftIndex]);

            int rightIndex = 2 * i + 2;
            if (rightIndex < nodeCount && lineIndex < lines.length)
                lines[lineIndex++].setLine(parentX, parentY, xPositions[rightIndex], yPositions[rightIndex]);
        }

        shapesContainer.repaint();
        repaint();
    }

    // Updates tree with swapping highlights and root highlight
    public void updateTreeWithHighlights(int[] values, int highlight1, int highlight2) {
        this.values = values;

        shapesContainer.getShapes().clear();
        lines = new Shapes.ShapeObject[Math.max(0, values.length - 1)];
        circles = new Shapes.ShapeObject[values.length];

        // Lines first
        for (int i = 0; i < lines.length; i++)
            lines[i] = shapesContainer.addLine(0, 0, 0, 0, Color.BLACK);

        // Circles
        for (int i = 0; i < values.length; i++) {
            // Only highlight swapping nodes
            Color outline = (i == highlight1 || i == highlight2) ? Color.YELLOW : Color.BLACK;

            // Fill stays constant
            circles[i] = shapesContainer.addCircle(0, 0, 20, outline, Color.WHITE, String.valueOf(values[i]));
        }

        updatePositions();
    }

    // Optional: clear highlights (reset outline)
    public void clearHighlights() {
        if (circles == null) return;
        for (Shapes.ShapeObject c : circles) {
            c.setColor(Color.BLACK); // reset outline
        }
        repaint();
    }


    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        if (shapesContainer != null) shapesContainer.setBounds(0, 0, width, height);
        if (values.length > 0) updatePositions();
    }
}
