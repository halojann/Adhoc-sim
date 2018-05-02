package adhoc.network.simulation;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class Node {

    int id;
    int x, y;//coordinates
    int dx, dy;//movement
    int turns;
    public ArrayList<Integer> neighbours;

    public Node(int i, int xx, int yy) {

        id = i;
        x = xx;
        y = yy;

        dx = 0;
        dy = 0;
        turns = 0;
        neighbours = new ArrayList<>();
    }

    public boolean isNeighbour(int id) {
        if (neighbours.contains(id)) {
            return true;
        } else {
            return false;
        }
    }

}
