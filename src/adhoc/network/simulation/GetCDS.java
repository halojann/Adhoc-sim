package adhoc.network.simulation;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class GetCDS extends Thread {

    public static CopyOnWriteArrayList<Integer> CDS = new CopyOnWriteArrayList<>();
    public static int NHop = 2;
    int er = 0;

    @Override
    public void run() {

        while (true) {

            try {
                computeCDS();
            } catch (Exception ex) {
                er++;
            }

            try {
                pruneCDS();
            } catch (Exception exc) {
                exc.printStackTrace();
            }

            try {
                Thread.sleep((int) BasicJPanel.DELAY);
                CDS.clear();

            } catch (InterruptedException e) {
                System.out.println("CDS thread error " + e.getMessage());
            }
        }
    }

    private void computeCDS() {//marking

        for (Node s : BasicJPanel.Nodes) {//for all nodes

            boolean c = false;
            int l = s.neighbours.size();

            if (l == 1) {//only one neighbour
                continue;
            }

            for (int i = 0; i < l; i++) {//for each of its neighbours

                Node neigh = BasicJPanel.Nodes.get(s.neighbours.get(i));

                for (int k = 0; k < l; k++) {//compare with the other neighbours

                    Node otherneigh = BasicJPanel.Nodes.get(s.neighbours.get(k));

                    if (neigh.id == otherneigh.id) {
                        continue;
                    }

                    if (!neigh.isNeighbour(otherneigh.id)) {
                        c = true;
                        break;
                    }
                }

                if (c) {
                    break;
                }
            }

            if (c) {
                CDS.add(s.id);
            }
        }
    }

    private void pruneCDS() {//dominating set reduction using rule K
        double pr = 0;
        double cs = CDS.size();
        for (int l : CDS) {//for all nodes in CDS

            Node c = BasicJPanel.Nodes.get(l);

            ArrayList<Integer> neighset = new ArrayList<>();//total connected nodes between all CDS neighbours of  the node
            ArrayList<Integer> neighCDS = new ArrayList<>();//list of CDS neighbour ids

            for (int m : c.neighbours) {// for all CDS neighbours of the node

                if (m < l) {//id should be higher
                    continue;
                }

                if (!CDS.contains(m)) {//not a CDS node
                    continue;
                }

                Node cm = BasicJPanel.Nodes.get(m);
                neighset.addAll(cm.neighbours);//get all neighbours of its CDS neighbours
                neighCDS.add(m);//add neighbour to neighbourset

            }

            //boolean con = ifStronglyconnected(neighCDS, l);//check if CDS neighbours are strongly connected
            // all conditions satisfied
            if (neighset.containsAll(c.neighbours)) {// && (con == true)) {
                CDS.remove(Integer.valueOf(l));//remove node from CDS
                pr++;
            }
            neighset.clear();
            neighCDS.clear();
        }
        double per = (pr / cs) * 100;
        System.out.println("Prune rate: " + per);

    }

    private boolean ifStronglyconnected(ArrayList<Integer> neighCDS, int l) {
        boolean check = false;

        //check if all nodes in neighCDS are connected via CDS nodes
        ArrayList<Integer> connected = new ArrayList<>();
        getNHopCDS(neighCDS, l);

        for (int n : neighCDS) {

            for (int an : neighCDS) {

                Node node = BasicJPanel.Nodes.get(an);

                if (node.isNeighbour(n)) {

                    if (!connected.contains(n)) {
                        connected.add(n);
                    }

                    if (!connected.contains(an)) {
                        connected.add(an);
                    }
                }
            }
        }

        if (connected.size() == neighCDS.size()) {
            check = true;
        }

        return check;
    }

    private void getNHopCDS(ArrayList<Integer> neighCDS, int l) {

        for (int i = 0; i < NHop; i++) {

            for (int j : neighCDS) {

                Node node = BasicJPanel.Nodes.get(j);

                for (int k : node.neighbours) {

                    if (GetCDS.CDS.contains(k) && (!neighCDS.contains(k)) && (k != l)) {
                        neighCDS.add(k);
                    }
                }
            }
        }
    }

}
