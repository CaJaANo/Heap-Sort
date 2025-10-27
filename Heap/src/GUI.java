import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GUI {
    private static ArrayList<Heap.Step> steps = new ArrayList<>();
    private static int stepIndex = 0;
    private static Timer timer;
    private static ArrayList<Integer> lastArr = new ArrayList<>();
    private static String lastHeap = "";
    private static int delay = 500;
    private static boolean canStep = true;
    private static boolean isAnimating = false;

    public static void frame() {
        JFrame frame = new JFrame("Heap Sort Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1440, 810);
        frame.setMinimumSize(new Dimension(1440, 810));
        frame.setLayout(null);
        frame.setVisible(true);

        JLayeredPane layered = new JLayeredPane();
        layered.setBounds(0, 0, frame.getWidth(), frame.getHeight());
        frame.add(layered);

        JPanel view = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0xE3E3E3));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        view.setLayout(null);
        view.setBounds(10, 10, frame.getWidth() - 20, frame.getHeight() - 20);
        layered.add(view, Integer.valueOf(1));

        BinaryTreeView tree = new BinaryTreeView();
        tree.setBounds(0, 0, view.getWidth(), view.getHeight());
        view.add(tree);

        JPanel settings = new JPanel(new GridBagLayout()) {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0xD5D5D5));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
            }
        };
        settings.setOpaque(false);
        int sw = frame.getWidth() / 4;
        int sh = frame.getHeight() / 6;
        settings.setBounds((frame.getWidth() - sw) / 2, frame.getHeight() - sh - 10, sw, sh);
        layered.add(settings, Integer.valueOf(2));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5,5,5,5); c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0; c.gridy = 0; c.gridwidth = 5;
        JTextField input = new JTextField();
        input.setFont(new Font("Arial", Font.BOLD, 16));
        settings.add(input, c);

        c.gridwidth = 1; c.gridy = 1;
        JButton start = new JButton("Start"), pause = new JButton("Pause"), stepBtn = new JButton("Step"), reset = new JButton("Reset");
        JComboBox<String> heapSel = new JComboBox<>(new String[] {"Max Heap", "Min Heap"});
        heapSel.setFont(new Font("Arial", Font.BOLD, 14));

        settings.add(start, c); c.gridx++; settings.add(pause, c); c.gridx++;
        settings.add(stepBtn, c); c.gridx++; settings.add(reset, c); c.gridx++;
        settings.add(heapSel, c);

        c.gridy = 2; c.gridx = 0; c.gridwidth = 1;
        JButton slower = new JButton("-"), faster = new JButton("+");
        JLabel speedLabel = new JLabel("Speed: " + (delay / 1000.0) + " s", JLabel.CENTER);
        slower.setPreferredSize(new Dimension(40,30)); faster.setPreferredSize(new Dimension(40,30));
        settings.add(slower, c); c.gridx++; settings.add(speedLabel, c); c.gridx++; settings.add(faster, c);

        slower.addActionListener(e -> { delay = Math.min(delay + 100, 500); speedLabel.setText("Speed: " + (delay / 1000.0) + " s"); });
        faster.addActionListener(e -> { delay = Math.max(delay - 100, 100); speedLabel.setText("Speed: " + (delay / 1000.0) + " s"); });

        timer = new Timer(delay, null);

        Runnable nextStep = () -> {
            if (stepIndex < steps.size()) {
                Heap.Step s = steps.get(stepIndex++);
                timer.stop();
                isAnimating = true;
                tree.animateSwap(s.i, s.j, delay, false, () -> {
                    isAnimating = false;
                    if (stepIndex < steps.size()) { timer.setDelay(delay); timer.start(); }
                });
            } else timer.stop();
        };

        timer.addActionListener(e -> nextStep.run());

        ActionListener generate = e -> {
            String txt = input.getText().trim();
            ArrayList<Integer> arr = new ArrayList<>();
            if (!txt.isEmpty()) {
                String[] parts = txt.split("\\D+");
                for (String p : parts) if (!p.isEmpty()) arr.add(Integer.parseInt(p));
                lastArr = new ArrayList<>(arr);
            } else if (!lastArr.isEmpty()) arr = new ArrayList<>(lastArr);
            else return;

            String sel = (String) heapSel.getSelectedItem();
            lastHeap = sel;
            steps.clear();
            stepIndex = 0;
            steps = "Max Heap".equals(sel) ? Heap.heapSortMaxSteps(arr) : Heap.heapSortMinSteps(arr);
            tree.updateTree(arr.stream().mapToInt(i -> i).toArray());
        };

        input.addActionListener(generate);

        start.addActionListener(e -> {
            String sel = (String) heapSel.getSelectedItem();
            if (steps.isEmpty() || !sel.equals(lastHeap)) generate.actionPerformed(null);
            timer.setDelay(delay);
            timer.start();
        });

        pause.addActionListener(e -> timer.stop());

        stepBtn.addActionListener(e -> {
            if (!canStep || isAnimating) return;

            canStep = false; isAnimating = true;
            stepBtn.setEnabled(false);

            if (steps.isEmpty()) generate.actionPerformed(null);
            if (stepIndex < steps.size()) {
                Heap.Step s = steps.get(stepIndex++);
                tree.animateSwap(s.i, s.j, delay, true, () -> {
                    isAnimating = false;
                    if (stepIndex < steps.size()) nextStep.run();

                    new Timer(300, new ActionListener() {
                        public void actionPerformed(ActionEvent ev) {
                            canStep = true;
                            stepBtn.setEnabled(true);
                            ((Timer) ev.getSource()).stop();
                        }
                    }).start();
                });
            } else {
                isAnimating = false;
                canStep = true;
                stepBtn.setEnabled(true);
            }
        });

        reset.addActionListener(e -> {
            timer.stop();
            stepIndex = 0; steps.clear(); lastArr.clear();
            tree.updateTree(new int[0]);
            tree.repaint();
        });

        frame.addComponentListener(new ComponentAdapter() {
            @Override public void componentResized(ComponentEvent e) {
                int W = frame.getContentPane().getWidth(), H = frame.getContentPane().getHeight();
                layered.setBounds(0,0,W,H);
                int panelW = Math.max(300, W / 4 + 100), panelH = Math.max(80, H / 6);
                settings.setBounds((W - panelW) / 2, H - panelH - 10, panelW, panelH);
                view.setBounds(10, 10, W - 20, H - 20);
                tree.setBounds(0, 0, view.getWidth(), view.getHeight());
                frame.revalidate(); frame.repaint();
            }
        });
    }
}