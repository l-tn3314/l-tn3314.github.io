import java.awt.Color;
import javalib.impworld.*;
import javalib.worldimages.*;

// represents a node
class Cell {
    // x,y is unique for each cell
    int x;
    int y;
    // the four adjacent cells to this cell
    Cell left;
    Cell top;
    Cell right;
    Cell bottom;
    Color color;

    String rep; // just for testing

    // constructor
    Cell(int y, int x) {
        this.x = x;
        this.y = y;
        this.rep = Integer.toString(y) + ", " + Integer.toString(x);
        this.color = Color.GRAY;
    }

    // is the given obj equal to this Cell?
    public boolean equals(Object obj) {
        if (obj instanceof Cell) {
            Cell that = (Cell)obj;
            return this.x == that.x && this.y == that.y;
        }
        else {
            return false;
        }
    }

    // EFFECT: updates color of this cell
    void updateColor(Color c) {
        this.color = c;
    }

    // EFFECT: updates the left of this Cell
    void updateLeft(Cell that) {
        this.left = that;
    }

    // EFFECT: updates the top of this Cell
    void updateTop(Cell that) {
        this.top = that;
    }

    // EFFECT: updates the right of this Cell
    void updateRight(Cell that) {
        this.right = that;
    }

    // EFFECT: updates the bottom of this Cell
    void updateBottom(Cell that) {
        this.bottom = that;
    }

    // EFFECT: connects this Cell with that Cell
    // assumes cells are adjacent and the x and y of that are >= this
    void connectCell(Cell that) {
        if (this.x == that.x) {
            this.updateBottom(that);
            that.updateTop(this);
        }
        if (this.y == that.y) {
            this.updateRight(that);
            that.updateLeft(this);
        }
    }

    // returns hashCode of this Cell
    public int hashCode() {
        return this.x * 17 + this.y * 21;
    }

    // EFFECT: draws this cell onto the given WorldScene
    void draw(WorldScene acc) {
        this.draw(acc, this.color);
    }

    // EFFECT: draws this cell onto the given WorldScene with the given color
    void draw(WorldScene acc, Color c) {
        WorldImage cellImage = new RectangleImage(Maze.SCALE, Maze.SCALE,
                OutlineMode.SOLID, c);
        acc.placeImageXY(cellImage, (this.x * Maze.SCALE) + (Maze.SCALE / 2),
                (this.y * Maze.SCALE) + (Maze.SCALE / 2));
    }
}
