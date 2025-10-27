import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class BinaryTreeView extends JPanel {
    private final Shapes shapesPanel = new Shapes();
    private int[] values = new int[0];
    private int nodeRadius = 20;
    private int vGap = 80;

    private Shapes.Shape[] nodes = new Shapes.Shape[0];
    private Shapes.Shape[] links = new Shapes.Shape[0];

    private Timer animTimer;
    private static final int MAX_NODES = 63;

    public BinaryTreeView() {
        setLayout(null);
        setOpaque(false);
        shapesPanel.setBounds(0, 0, 800, 400);
        add(shapesPanel);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                if (values.length > 0) updatePositions();
            }
        });
    }

    // Enforce MAX_NODES
    public void updateTree(int[] vals) {
        if (vals == null) vals = new int[0];
        if (vals.length > MAX_NODES) vals = Arrays.copyOf(vals, MAX_NODES);
        this.values = vals;
        drawTree();
    }

    private void drawTree() {
        shapesPanel.getShapes().clear();
        if (values.length == 0) { repaint(); return; }
        links = new Shapes.Shape[Math.max(0, values.length - 1)];
        nodes = new Shapes.Shape[values.length];
        for (int i = 0; i < links.length; i++) links[i] = shapesPanel.addLine(0, 0, 0, 0, Color.BLACK);
        for (int i = 0; i < values.length; i++) nodes[i] = shapesPanel.addCircle(0, 0, nodeRadius, Color.gray, Color.WHITE, String.valueOf(values[i]));
        updatePositions();
    }

    private void updatePositions() {
        int w = getWidth(), n = values.length;
        if (n == 0) return;
        int[] xs = new int[n], ys = new int[n];
        for (int i = 0; i < n; i++) {
            int level = 31 - Integer.numberOfLeadingZeros(i + 1);
            int idx = i - ((1 << level) - 1);
            int per = 1 << level;
            int spacing = Math.max(1, w / (per + 1));
            xs[i] = spacing * (idx + 1);
            ys[i] = 30 + level * vGap;
            nodes[i].setPos(xs[i], ys[i]);
        }
        int li = 0;
        for (int i = 0; i < n; i++) {
            int L = 2 * i + 1, R = 2 * i + 2;
            if (L < n && li < links.length) links[li++].setLine(xs[i], ys[i], xs[L], ys[L]);
            if (R < n && li < links.length) links[li++].setLine(xs[i], ys[i], xs[R], ys[R]);
        }
        shapesPanel.repaint();
        repaint();
    }

    public void animateSwap(int i1, int i2, int ms, boolean skip, Runnable done) {
        if (i1 < 0 || i2 < 0 || i1 >= nodes.length || i2 >= nodes.length) { if (done != null) done.run(); return; }
        if (animTimer != null && animTimer.isRunning()) { animTimer.stop(); animTimer = null; }
        if (skip) {
            Shapes.Shape t = nodes[i1]; nodes[i1] = nodes[i2]; nodes[i2] = t;
            updatePositions();
            if (done != null) done.run();
            return;
        }
        final Shapes.Shape a = nodes[i1], b = nodes[i2];
        final int ax0 = a.getX(), ay0 = a.getY(), bx0 = b.getX(), by0 = b.getY();
        final int steps = Math.max(1, ms / 20); final int[] s = {0};
        animTimer = new Timer(20, ev -> {
            s[0]++; float t = (float) s[0] / steps; float et = t < 0.5f ? 2 * t * t : -1 + (4 - 2 * t) * t;
            a.setPos((int) (ax0 + et * (bx0 - ax0)), (int) (ay0 + et * (by0 - ay0)));
            b.setPos((int) (bx0 + et * (ax0 - bx0)), (int) (by0 + et * (ay0 - by0)));
            shapesPanel.repaint();
            if (s[0] >= steps) {
                animTimer.stop(); animTimer = null;
                Shapes.Shape tmp = nodes[i1]; nodes[i1] = nodes[i2]; nodes[i2] = tmp;
                updatePositions();
                if (done != null) done.run();
            }
        });
        animTimer.start();
    }

    @Override
    public void setBounds(int x, int y, int w, int h) {
        super.setBounds(x, y, w, h);
        if (shapesPanel != null) shapesPanel.setBounds(0, 0, w, h);
        if (values.length > 0) updatePositions();
    }
}