import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

public class GUI {

    public static void frame(){
        int H = 1080, W = 1920;

        JFrame frame = new JFrame();
        frame.setLayout(null);
        frame.setTitle("Heap Sort");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(W/2,H/2);
        frame.setMinimumSize(new Dimension(W/4, H/3));
        frame.setVisible(true);

        int fW = frame.getWidth();
        int fH = frame.getHeight();

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0,0,frame.getWidth(),frame.getHeight());
        frame.add(layeredPane);

        JPanel view = new JPanel();
        view.setBackground(new Color(0xCCCCCC));
        view.setBounds(10,10,fW-20, fH-20);

        JPanel settings = new JPanel();
        settings.setBackground(new Color(0xe0e0e0));
        settings.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        settings.setBounds((fW/2)-(fW/4)/2, fH-(fH/6), (fW/4), fH/6);

        layeredPane.add(view, Integer.valueOf(1));
        layeredPane.add(settings, Integer.valueOf(2));

        ComponentAdapter dySize = new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                int W = frame.getContentPane().getWidth();
                int H = frame.getContentPane().getHeight();

                layeredPane.setBounds(0,0,W,H);

                int panelWidth = (W / 4) + 100;
                int panelHeight = H / 6;

                settings.setBounds((W-panelWidth)/2, H - panelHeight, panelWidth, panelHeight);
                view.setBounds(10,10,W-20,H-20);

                frame.revalidate();
                frame.repaint();
            }};

        frame.addComponentListener(dySize);

        JTextField inputField = new JTextField();
        inputField.setBounds(0,0,200,30);
        inputField.setPreferredSize(new Dimension(200,30));
        Font tnr = new Font("Arial", Font.BOLD, 16);
        inputField.setFont(tnr);

        JButton submitButton = new JButton("Submit");
        ArrayList<Integer> arr = new ArrayList<>();

        Runnable submitAction = () -> {
            String text = inputField.getText().trim();
            if (text.isEmpty()) return;

            String[] nums = text.split("[, ]+");

            for (String i : nums) {
                int num = Integer.parseInt(i);
                arr.add(num);
            }

            System.out.println("Original array:");
            System.out.println(arr);
            Heap.heapSort(arr);
            System.out.println("Sorted array:");
            System.out.println(arr);
            inputField.setText("");
            arr.clear();
        };

        inputField.addActionListener(e -> submitAction.run());
        submitButton.addActionListener(e -> submitAction.run());

        settings.add(inputField);
        settings.add(submitButton);
        frame.revalidate();
        frame.repaint();
    }
}
