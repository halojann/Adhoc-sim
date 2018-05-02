package adhoc.network.simulation;

import java.awt.EventQueue;
import java.util.Random;
import javax.swing.JFrame;

public class AdhocNetworkSimulation extends JFrame {

    //default num of nodes
    public static int num_nodes = 200;

    public AdhocNetworkSimulation() {
        initUI();
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            JFrame ex = new AdhocNetworkSimulation();
            ex.setVisible(true);
        });

    }

    public static void addNodes(int num) {

        int prd = BasicJPanel.x_end * BasicJPanel.y_end;
        Random ran = new Random();

        for (int i = 0; i < num; i++) {

            int x = 0;
            int y = 0;
            int loc = ran.nextInt(prd);

            while (loc > 0) {
                x = loc % BasicJPanel.x_end;
                y++;
                loc = loc - BasicJPanel.x_end;
            }

            Node n = new Node(i, x, y);
            BasicJPanel.Nodes.add(n);

        }
    }

    private void initUI() {

        //Build a frame
        BasicFrame frame = new BasicFrame("Simple Ad-Hoc Network Simulator");
        frame.buildFrame();

        //add nodes
        addNodes(num_nodes);

        //add movement
        Move mn = new Move();
        mn.start();

        //initiate getNeighbours thread
        GetNeighbours gn = new GetNeighbours();
        gn.start();

        //initiate getCDS
        GetCDS gcds = new GetCDS();
        gcds.start();

        //highlight route b/w source and dest nodes
        GetRoute gr = new GetRoute();
        gr.start();
    }

}
