import java.util.*;
import java.awt.Color;
import javalib.impworld.*;
import javalib.worldimages.*;

// represents an edge
class Edge {
    Cell one;
    Cell two;
    int weight;

    // constructor
    Edge(Cell one, Cell two, int weight) {
        this.one = one;
        this.two = two;
        this.weight = weight;
    }

    // is the given cell part of this edge?
    boolean isConnected(Cell c) {
        return (c.equals(this.one) || c.equals(this.two));
    }

    // returns other cell of this edge
    Cell otherCell(Cell c) {
        if (c.equals(this.one)) {
            return this.two;
        }
        else if (c.equals(this.two)) {
            return this.one;
        }
        else {
            throw new RuntimeException("Cell is not part of this edge!");
        }
    }

    // EFFECT: draws this edge onto the WorldScene
    void draw(WorldScene acc) {
        WorldImage line;
        // this.one and this.two are cells
        if (this.one.x == this.two.x) {
            line = new LineImage(new Posn(Maze.SCALE, 0), Color.BLACK);
            acc.placeImageXY(line, 
                    (this.one.x * Maze.SCALE) + (Maze.SCALE / 2), 
                    this.two.y * Maze.SCALE);
        }
        else {
            line = new LineImage(new Posn(0, Maze.SCALE), Color.BLACK);
            acc.placeImageXY(line, 
                    (this.two.x * Maze.SCALE), 
                    (this.one.y * Maze.SCALE) + (Maze.SCALE / 2));
        }
    }
}

// edge comparator
class EdgeComparator implements Comparator<Edge> {

    // compares two Edges 
    public int compare(Edge e1, Edge e2) {
        return e1.weight - e2.weight;
    }
}
