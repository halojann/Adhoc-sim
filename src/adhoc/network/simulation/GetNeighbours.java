package adhoc.network.simulation;

public class GetNeighbours extends Thread {

    public static double distance(int x1, int y1, int x2, int y2) {
        return (Math.sqrt(((y2 - y1) * (y2 - y1)) + ((x2 - x1) * (x2 - x1))));
    }

    @Override
    public void run() {

        while (true) {

            try {
                findNeighbours();
            } catch (Exception ex) {
                System.out.println("Neighbour thread error" + ex.getMessage());
            }

            try {
                //Thread.sleep((int) BasicJPanel.DELAY);
                Thread.sleep(400);
            } catch (InterruptedException e) {
                System.out.println("Interrupted: " + e.getMessage());
            }
        }
    }

    private void findNeighbours() {
        int d;
        for (Node s : BasicJPanel.Nodes) {

            s.neighbours.clear();

            //add near nodes to neighbours
            for (Node o : BasicJPanel.Nodes) {
                if (s.id != o.id) {//all other nodes
                    d = (int) distance(s.x, s.y, o.x, o.y);
                    if (d < BasicJPanel.tr / 2) {
                        s.neighbours.add(o.id);
                    }
                }
            }
        }
    }
}
