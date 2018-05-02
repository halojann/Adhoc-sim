package adhoc.network.simulation;

import java.util.concurrent.CopyOnWriteArrayList;

public class GetRoute extends Thread {

    public static CopyOnWriteArrayList<Integer> Route = new CopyOnWriteArrayList<>();
    public static CopyOnWriteArrayList<Integer> explored = new CopyOnWriteArrayList<>();

    class Result {

        public CopyOnWriteArrayList<Integer> path;
        public boolean status;

        public Result() {
            path = new CopyOnWriteArrayList<>();
            status = false;
        }
    }

    @Override
    public void run() {

        while (true) {

            try {
                Route();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {
                Thread.sleep((int) BasicJPanel.DELAY);
            } catch (InterruptedException e) {
                System.out.println("Route thread error " + e.getMessage());
            }
        }
    }

    private void Route() {//just to get to the nearest CDS node

        Route.clear();
        explored.clear();

        Route.add(BasicJPanel.sn_id);//add source to route
        Route.add(BasicJPanel.en_id);//add dest to route

        Node n = BasicJPanel.Nodes.get(BasicJPanel.sn_id);

        if (n.isNeighbour(BasicJPanel.en_id)) {//dest is a neighbour of source
            return;
        }

        int nCDS;

        if (GetCDS.CDS.contains(BasicJPanel.sn_id)) {//if source node is CDS
            nCDS = BasicJPanel.sn_id;
        } else {//get neighbouring CDS closest to dest
            nCDS = neighbouringClosestUnexploredCDS(BasicJPanel.sn_id);
        }

        Result reach = new Result();

        if (nCDS != -1) {
            reach = follow(nCDS);
        }

        if (reach.status) {
            Route.addAll(reach.path);
        }

    }//route fn end

    public double distance(int n1, int n2) {

        Node no1 = BasicJPanel.Nodes.get(n1);
        Node no2 = BasicJPanel.Nodes.get(n2);
        double dis = (no2.y - no1.y) * (no2.y - no1.y);
        dis += (no2.x - no1.x) * (no2.x - no1.x);
        dis = Math.sqrt(dis);
        return dis;

    }

    public int neighbouringClosestUnexploredCDS(int n) {

        Node node = BasicJPanel.Nodes.get(n);

        if (node.neighbours.isEmpty()) {
            return -1;
        }

        int c = -1;
        double curr_min = Double.MAX_VALUE;

        for (int j = 0; j < node.neighbours.size(); j++) {
            int nd = node.neighbours.get(j);

            if ((!GetCDS.CDS.contains(nd)) || (explored.contains(nd))) {
                continue;
            }

            double dist = distance(BasicJPanel.en_id, nd);
            if (dist < curr_min) {
                c = nd;
                curr_min = dist;
            }
        }

        return c;
    }

    public CopyOnWriteArrayList<Integer> getNeighbouringUnexploredCDS(int n) {

        Node node = BasicJPanel.Nodes.get(n);
        CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<>();

        for (int i = 0; i < node.neighbours.size(); i++) {

            int nd = node.neighbours.get(i);
            if (!(GetCDS.CDS.contains(nd)) || (explored.contains(nd))) {
                continue;
            }

            list.add(nd);

        }
        return list;
    }

    private Result follow(int nCDS) {

        Result reach = new Result();
        if (nCDS == -1) {
            return reach;
        }

        explored.add(nCDS);

        reach.path.add(nCDS);

        Node n = BasicJPanel.Nodes.get(nCDS);

        if (n.isNeighbour(BasicJPanel.en_id)) {
            reach.status = true;
            return reach;
        }

        while (neighbouringClosestUnexploredCDS(nCDS) != -1) {

            Result subres = follow(neighbouringClosestUnexploredCDS(nCDS));

            if (subres.status) {
                reach.path.addAll(subres.path);
                reach.status = true;
            }
        }

        return reach;
    }
}
