import java.util.*;
import javalib.impworld.*;
import javalib.worldimages.*;

class Movable {
    Cell cur;
    WorldImage img;

    // constructor
    Movable(Cell c) {
        this.cur = c;
        this.img = new FromFileImage("sp.png");
    }

    // convenience constructor
    Movable(Cell c, WorldImage img) {
        this(c);
        this.img = img;
    }

    // EFFECT: updates the movable's current cell to the given cell
    void updateCell(Cell that) {
        this.cur = that;
    }

    // EFFECT: draws this movable onto the given WorldScene
    void draw(WorldScene acc) {
        acc.placeImageXY(this.img, (this.cur.x * Maze.SCALE) + (Maze.SCALE / 2),
                (this.cur.y * Maze.SCALE) + (Maze.SCALE / 2));
    }
}

// ------------------------------- PLAYER ------------------------------- //
// represents a player
class Player extends Movable {
    ArrayList<Cell> visitedCells;

    // constructor
    Player(Cell c) {
        super(c, new FromFileImage("player.png"));
        this.visitedCells = new ArrayList<Cell>();
        this.visitedCells.add(this.cur);
    }

    // convenience constructor
    Player(Cell c, WorldImage img) {
        this(c);
        this.img = img;
    }

    // EFFECT: adds this player's cell to the list of visited cells
    void updateCell(Cell that) {
        this.cur = that;
        if (!this.visitedCells.contains(that)) {
            this.visitedCells.add(that);
        }
    }
}
