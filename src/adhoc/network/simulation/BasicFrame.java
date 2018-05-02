package adhoc.network.simulation;

import static adhoc.network.simulation.GetCDS.CDS;
import static adhoc.network.simulation.GetRoute.Route;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class BasicFrame extends JFrame {

    private boolean custom = false;

    public BasicFrame(String title) {
        super(title);
    }

    public void buildFrame() {

        RelativeLayout x = new RelativeLayout(RelativeLayout.X_AXIS);
        x.setFill(true);

        JPanel container = new JPanel(x);

        BasicJPanel sim_panel = new BasicJPanel();//add simulation JPanel        

        container.add(sim_panel, new Float(3));//add main panel
        container.add(addforms(), new Float(1));//add sidepanel

        this.setContentPane(container);
        this.setSize(BasicJPanel.x_end + 500, BasicJPanel.y_end + 100);

        //add menu bar
        createMenubar();

        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void createMenubar() {

        JMenuBar menubar = new JMenuBar();

        JMenu file = new JMenu("File");//File
        file.setMnemonic(KeyEvent.VK_F);

        JMenuItem eMenuItem = new JMenuItem("Exit");//File -> Exit
        eMenuItem.setMnemonic(KeyEvent.VK_E);
        eMenuItem.setToolTipText("Exit application");

        eMenuItem.addActionListener((ActionEvent event) -> {
            System.exit(0);
        });

        file.add(eMenuItem);

        menubar.add(file);

        setJMenuBar(menubar);

    }

    JPanel addforms() {

        RelativeLayout x = new RelativeLayout(RelativeLayout.X_AXIS);
        x.setFill(true);

        RelativeLayout y = new RelativeLayout(RelativeLayout.Y_AXIS);
        x.setFill(true);

        String[] labels = {"Delay (ms)", "Node Count", "Node Size", "Node Transmission Range", "Max Disp per Frame", "Frames per Direction", "Left Bound", "Right Bound", "Top Bound", "Bottom Bound",
            "Source Node (ID)", "Destination Node (ID)"};
        char[] mnemonics = {'Y', 'C', 'S', 'T', 'F', 'D', 'L', 'R', 'U', 'B', 'Q', 'W'};

        String[] presets = {
            String.valueOf(BasicJPanel.DELAY),
            String.valueOf(AdhocNetworkSimulation.num_nodes),
            String.valueOf(BasicJPanel.ns),
            String.valueOf(BasicJPanel.tr),
            String.valueOf(BasicJPanel.move_f),
            String.valueOf(BasicJPanel.max_turns),
            String.valueOf(BasicJPanel.x0),
            String.valueOf(BasicJPanel.x_end),
            String.valueOf(BasicJPanel.y0),
            String.valueOf(BasicJPanel.y_end), String.valueOf(BasicJPanel.sn_id), String.valueOf(BasicJPanel.en_id)};

        int widths = 27;
        String[] tips = {"Delay b/w each frame", "Number of Nodes in SIM", "Node Sphere Diameter", "Node Range (unit disk diameter)",
            "Maximum Displacement per frame", "Num of Frames before switching direction", "", "", "", "",
            "Start Node", "Target Node"};

        JPanel sidepanel = new JPanel(y);
        JPanel Menupanel = new JPanel(x);
        JPanel labelPanel = new JPanel(y);
        JPanel fieldPanel = new JPanel(y);

        Menupanel.add(labelPanel, new Float(1));
        Menupanel.add(fieldPanel, new Float(2));

        sidepanel.add(Menupanel, new Float(9));

        JTextField[] fields = new JTextField[labels.length];

        for (int i = 0; i < labels.length; i += 1) {
            fields[i] = new JTextField();
            if (i < tips.length) {
                fields[i].setToolTipText(tips[i]);
            }

            fields[i].setColumns(widths);

            JLabel lab = new JLabel(labels[i], JLabel.RIGHT);
            lab.setLabelFor(fields[i]);
            if (i < mnemonics.length) {
                lab.setDisplayedMnemonic(mnemonics[i]);
            }

            if (!custom) {
                fields[i].setText(presets[i]);
            }

            labelPanel.add(lab, new Float(1));
            fieldPanel.add(fields[i], new Float(1));
        }

        JButton submit = new JButton("Apply");//submit button
        sidepanel.add(submit, new Float(1));

        submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CDS.clear();
                Route.clear();
                //assign values taken from the forms
                custom = true;
                BasicJPanel.DELAY = Integer.valueOf(fields[0].getText());
                AdhocNetworkSimulation.num_nodes = Integer.valueOf(fields[1].getText());
                BasicJPanel.ns = Integer.valueOf(fields[2].getText());
                BasicJPanel.tr = Integer.valueOf(fields[3].getText());
                BasicJPanel.move_f = Integer.valueOf(fields[4].getText());
                BasicJPanel.max_turns = Integer.valueOf(fields[5].getText());
                BasicJPanel.x0 = Integer.valueOf(fields[6].getText());
                BasicJPanel.x_end = Integer.valueOf(fields[7].getText());
                BasicJPanel.y0 = Integer.valueOf(fields[8].getText());
                BasicJPanel.y_end = Integer.valueOf(fields[9].getText());
                BasicJPanel.sn_id = Integer.valueOf(fields[10].getText());
                BasicJPanel.en_id = Integer.valueOf(fields[11].getText());

                if (AdhocNetworkSimulation.num_nodes != BasicJPanel.curr_num) {
                    reset_Nodes();
                    BasicJPanel.curr_num = AdhocNetworkSimulation.num_nodes;
                }
            }

            private void reset_Nodes() {
                BasicJPanel.Nodes.clear();
                AdhocNetworkSimulation.addNodes(AdhocNetworkSimulation.num_nodes);
            }
        });

        return sidepanel;
    }

}
