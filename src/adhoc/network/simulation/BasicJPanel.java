package adhoc.network.simulation;

import java.awt.*;
import java.awt.geom.*;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.*;

public class BasicJPanel extends JPanel implements Runnable {

    public static CopyOnWriteArrayList<Node> Nodes = new CopyOnWriteArrayList<>();//all nodes

    private Thread animator;

    public static int DELAY = 100;//delay bw each frame
    public static int move_f = 4;//max move distance per frame
    public static int max_turns = 3;//movements per direction

    public static int tr = 200;//transmission range
    public static int ns = 15;//node size
    public static int CDS_ring_size = 25;//CDS node indication

    public static int x0 = 10;//left border
    public static int x_end = 1300;//right border
    public static int y0 = 10;//top border
    public static int y_end = 600;//bottom border

    public static int curr_num = AdhocNetworkSimulation.num_nodes;
    public static int sn_id = 0;//source node ID
    public static int en_id = AdhocNetworkSimulation.num_nodes - 1;//end node ID

    public BasicJPanel() {
        super();
    }

    @Override
    public void paintComponent(Graphics g){

        super.paintComponent(g);

        g.drawRect(x0, y0, x_end, y_end);//black bordered rectangle        
        Graphics2D g2 = (Graphics2D) g.create();//fill rectangle - white
        g2.setColor(Color.white);
        g2.fillRect(x0 + 1, y0 + 1, x_end - 1, y_end - 1);

        for (Node s : Nodes) {

            //draw a node
            g2.setColor(Color.lightGray);//circle representing node transmission range
            g2.draw(new Ellipse2D.Double(s.x - (tr / 2), s.y - (tr / 2), tr, tr));

            if (GetCDS.CDS.contains(s.id)) {
                g2.setColor(Color.red);
            } else {
                g2.setColor(Color.orange);
            }

            g2.fillOval(s.x - (ns / 2), s.y - (ns / 2), ns, ns);//draw node

            g2.setColor(Color.black);
            g2.drawString(Integer.toString(s.id), s.x, s.y);

            Graphics2D g3 = (Graphics2D) g;
            g3.setStroke(new BasicStroke(3));

            //source and dest node indications
            if (s.id == sn_id) {
                g3.setColor(Color.green);
                g3.drawRect(s.x - 15, s.y - 15, 30, 30);
            }
            if (s.id == en_id) {
                g3.setColor(Color.MAGENTA);
                g3.drawRect(s.x - 15, s.y - 15, 30, 30);
            }

            boolean nr = GetRoute.Route.contains(s.id);//if node is part of the route

            //draw links
            for (int i : s.neighbours) {//get neighbour nodes
                g3.setStroke(new BasicStroke(1));
                Node n = Nodes.get(i);

                //CDS link
                if (GetCDS.CDS.contains(s.id) && GetCDS.CDS.contains(n.id)) {
                    g3.setColor(Color.red);
                    g3.setStroke(new BasicStroke(2));
                } else {
                    g3.setColor(Color.green);
                }

                //Route link
                if (nr) {
                    if (GetRoute.Route.contains(n.id)) {
                        g3.setColor(Color.BLUE);
                        g3.setStroke(new BasicStroke(3));
                    }
                }
                g3.drawLine(s.x, s.y, n.x, n.y);
            }
        }
    }

    @Override
    public void run() {

        long beforeTime, timeDiff, sleep;
        beforeTime = System.currentTimeMillis();

        while (true) {

            repaint();//paint new positions

            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDiff;

            if (sleep < 0) {
                sleep = 2;
            }

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                System.out.println("Interrupted: " + e.getMessage());
            }

            beforeTime = System.currentTimeMillis();
        }
    }

    @Override
    public void addNotify() {
        super.addNotify();
        animator = new Thread(this);
        animator.start();
    }

}
