import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;

public class GUI {

    private static ArrayList<Heap.Step> steps = new ArrayList<>();
    private static int currentStepIndex = 0;
    private static Timer timer;

    public static void frame() {
        int H = 1080, W = 1920;

        JFrame frame = new JFrame();
        frame.setLayout(null);
        frame.setTitle("Heap Sort Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(W / 2, H / 2);
        frame.setMinimumSize(new Dimension((W / 3) + 100, H / 2));
        frame.setVisible(true);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, frame.getWidth(), frame.getHeight());
        frame.add(layeredPane);

        JPanel view = new JPanel() {
            @Override
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
        layeredPane.add(view, Integer.valueOf(1));

        BinaryTreeView treeView = new BinaryTreeView();
        treeView.setBounds(0, 0, view.getWidth(), view.getHeight());
        view.add(treeView);

        JPanel settings = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0xD5D5D5));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
            }
        };
        settings.setOpaque(false);
        settings.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        settings.setBounds((frame.getWidth() / 2) - (frame.getWidth() / 4) / 2,
                frame.getHeight() - (frame.getHeight() / 6),
                (frame.getWidth() / 4),
                frame.getHeight() / 6);
        layeredPane.add(settings, Integer.valueOf(2));

        JTextField inputField = new JTextField();
        inputField.setPreferredSize(new Dimension(150, 30));
        inputField.setFont(new Font("Arial", Font.BOLD, 16));

        JButton startButton = new JButton("Start");
        JButton pauseButton = new JButton("Pause");
        JButton stepButton = new JButton("Step");
        JButton resetButton = new JButton("Reset");

        String[] heapOptions = {"Max Heap", "Min Heap"};
        JComboBox<String> heapSelector = new JComboBox<>(heapOptions);
        heapSelector.setFont(new Font("Arial", Font.BOLD, 14));

        settings.add(inputField);
        settings.add(startButton);
        settings.add(pauseButton);
        settings.add(stepButton);
        settings.add(resetButton);
        settings.add(heapSelector);

        timer = new Timer(500, null);

        timer.addActionListener(e -> {
            if (currentStepIndex < steps.size() * 2) {
                int stepIndex = currentStepIndex / 2;
                boolean isClearStep = (currentStepIndex % 2 == 1);

                if (isClearStep) {
                    treeView.clearHighlights();
                } else {
                    Heap.Step step = steps.get(stepIndex);
                    treeView.updateTreeWithHighlights(step.array, step.highlight1, step.highlight2);
                }

                currentStepIndex++;
            } else {
                timer.stop();
            }
        });

        ActionListener generateSteps = e -> {
            String text = inputField.getText().trim();
            if (text.isEmpty()) return;

            String[] nums = text.split("\\D+");
            ArrayList<Integer> arr = new ArrayList<>();
            for (String n : nums) arr.add(Integer.parseInt(n));

            String selectedHeap = (String) heapSelector.getSelectedItem();
            steps.clear();
            currentStepIndex = 0;

            if ("Max Heap".equals(selectedHeap))
                steps = Heap.heapSortMaxSteps(arr);
            else
                steps = Heap.heapSortMinSteps(arr);

            treeView.updateTree(arr.stream().mapToInt(i -> i).toArray());
        };

        inputField.addActionListener(generateSteps);

        startButton.addActionListener(e -> {
            if (steps.isEmpty()) generateSteps.actionPerformed(null);
            timer.start();
        });

        pauseButton.addActionListener(e -> timer.stop());

        stepButton.addActionListener(e -> {
            if (steps.isEmpty()) generateSteps.actionPerformed(null);
            if (currentStepIndex < steps.size() * 2) {
                int stepIndex = currentStepIndex / 2;
                boolean isClearStep = (currentStepIndex % 2 == 1);

                if (isClearStep) {
                    treeView.clearHighlights();
                } else {
                    Heap.Step step = steps.get(stepIndex);
                    treeView.updateTreeWithHighlights(step.array, step.highlight1, step.highlight2);
                }

                currentStepIndex++;
            }
        });

        resetButton.addActionListener(e -> {
            timer.stop();
            currentStepIndex = 0;
            steps.clear();
            treeView.updateTree(new int[0]);
        });

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int W = frame.getContentPane().getWidth();
                int H = frame.getContentPane().getHeight();

                layeredPane.setBounds(0, 0, W, H);

                int panelWidth = (W / 4) + 100;
                int panelHeight = H / 6;

                settings.setBounds((W - panelWidth) / 2, H - panelHeight, panelWidth, panelHeight);
                view.setBounds(10, 10, W - 20, H - 20);
                treeView.setBounds(0, 0, view.getWidth(), view.getHeight());

                frame.revalidate();
                frame.repaint();
            }
        });
    }
}

