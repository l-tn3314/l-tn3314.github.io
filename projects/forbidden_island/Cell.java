import java.awt.Color;
import javalib.worldimages.*;
import javalib.impworld.*;

// Represents a single square of the game area
class Cell {
    // represents absolute height of this cell, in feet
    double height;
    // In logical coordinates, with the origin at the top-left corner of the screen
    int x;
    int y;
    // the four adjacent cells to this one
    Cell left;
    Cell top;
    Cell right;
    Cell bottom;
    // reports whether this cell is flooded or not
    boolean isFlooded;

    // constructor
    Cell(double height, int x, int y, boolean isFlooded) {
        this.height = height;
        this.x = x;
        this.y = y;
        this.isFlooded = isFlooded;
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

    // makes scene for a Cell
    public WorldScene draw(WorldScene acc, int waterHeight) {
        int scale = ForbiddenIslandWorld.SCALE;
        int isleHeight = ForbiddenIslandWorld.ISLAND_HEIGHT;

        if (waterHeight < 0) {
            waterHeight = 0;
        }

        RectangleImage cellImage;        

        if (isFlooded) {
            int green = 77 - (int)(1.5 * (waterHeight - this.height));
            int blue = 255 - (int)(4.5 * (waterHeight - this.height));
            cellImage = new RectangleImage(scale, scale, OutlineMode.SOLID,
                    new Color(0, green, blue));
        }
        else {
            // does not work if size > 87
            int temp = isleHeight - ((int)this.height - waterHeight);
            int green = 255 - (temp * (125 / isleHeight));
            int redBlue = Math.abs(255 - (temp * (255 / isleHeight)));

            cellImage = new RectangleImage(scale, scale, OutlineMode.SOLID, 
                    new Color(redBlue, green, redBlue));
        }
        acc.placeImageXY(cellImage, (this.x * scale) + (scale / 2), 
                (this.y * scale) + (scale / 2));
        return acc;
    }

    // updates flood status of this cell based on the input
    void flood(int waterHeight) {
        if (this.height <= waterHeight && !isFlooded) {
            this.isFlooded = true;
        }
    }

    // adds this cell to acc if it is a coast cell
    IList<Cell> coast(IList<Cell> acc) {
        if (!this.isFlooded &&
                // this.left, this.top, this.right, this.bottom are cells
                (this.left.isFlooded || this.top.isFlooded || 
                        this.right.isFlooded || this.bottom.isFlooded)) {
            return new ConsList<Cell>(this, acc);
        }
        return acc;
    }

    // does the given cell have the same coordinates as this cell?
    boolean sameCoord(Cell that) {
	return this.x == that.x && this.y == that.y;
    }
}

// Represents an Ocean Cell
class OceanCell extends Cell {

    // constructor
    OceanCell(double height, int x, int y, boolean isFlooded) {
        super(height, x, y, true);
    }

    // convenience constructor
    OceanCell(double height, int x, int y) {
        super(height, x, y, true);
    }

    // makes scene for an Ocean Cell
    public WorldScene draw(WorldScene acc, int waterHeight) {
        int scale = ForbiddenIslandWorld.SCALE;

        RectangleImage cellImage = 
                new RectangleImage(scale, scale, OutlineMode.SOLID, 
                        new Color(25, 59, 224));
        acc.placeImageXY(cellImage, (this.x * scale) + (scale / 2), (this.y * scale) + (scale / 2));
        return acc;
    }
}
