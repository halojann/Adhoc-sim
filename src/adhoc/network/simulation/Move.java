package adhoc.network.simulation;

import static adhoc.network.simulation.BasicJPanel.Nodes;
import static adhoc.network.simulation.BasicJPanel.max_turns;
import static adhoc.network.simulation.BasicJPanel.move_f;
import static adhoc.network.simulation.BasicJPanel.ns;
import static adhoc.network.simulation.BasicJPanel.x0;
import static adhoc.network.simulation.BasicJPanel.x_end;
import static adhoc.network.simulation.BasicJPanel.y0;
import static adhoc.network.simulation.BasicJPanel.y_end;
import java.util.Random;

public class Move extends Thread {

    @Override
    public void run() {

        while (true) {

            try {
                moveNodes();
            } catch (Exception ex) {
                System.out.println("Neighbour thread error" + ex.getMessage());
            }

            try {
                Thread.sleep((int) BasicJPanel.DELAY);

            } catch (InterruptedException e) {
                System.out.println("Interrupted: " + e.getMessage());
            }
        }
    }

    private void moveNodes() {

        //collision check
        collide();

        //move the nodes
        Random rand = new Random();

        for (Node s : Nodes) {

            if (s.id == BasicJPanel.en_id) {
                continue;
            }

            int ix = s.x;
            int iy = s.y;

            if (s.turns == max_turns) {
                s.dx = rand.nextInt(move_f) - rand.nextInt(move_f);
                s.dy = rand.nextInt(move_f) - rand.nextInt(move_f);
                s.turns = 0;
                continue;
            }
            //move the node <random> units in a random direction
            ix += s.dx;
            iy += s.dy;
            s.turns++;
            boolean co = false;

            for (int i = 0; i < s.neighbours.size(); i++) {//for each of its neighbours
                Node neigh = BasicJPanel.Nodes.get(s.neighbours.get(i));

                if (!(GetNeighbours.distance(neigh.x, neigh.y, ix, iy) < ns + 10)) {
                    s.x = ix;
                    s.y = iy;
                } else {
                    s.x = s.x + 3 * s.dx;
                    s.y = s.y + 3 * s.dy;
                }
            }

        }

    }

    private void collide() {

        for (Node s : Nodes) {
            if (s.x > x_end - 1) {//collision with right boundary
                s.dx = -move_f;
                s.turns = 0;
            }

            if (s.x < x0 + 1) {//collision with left boundary
                s.dx = move_f;
                s.turns = 0;
            }

            if (s.y > y_end - 1) {//collision with bottom boundary
                s.dy = -move_f;
                s.turns = 0;
            }

            if (s.y < y0 + 1) {//collision with top boundary
                s.dy = move_f;
                s.turns = 0;
            }

            s.x += s.dx;
            s.y += s.dy;
            s.turns++;

        }

    }

}
