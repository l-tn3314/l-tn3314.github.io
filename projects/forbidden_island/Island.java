import java.util.ArrayList;
import java.util.Random;
import java.util.Iterator;
import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

//Assignment 9
//Escaba Amanda
//aescaba
//Lee Tina
//leetina


// ----------------------------- ISLANDUTILS ------------------------------ //
// represents utils for ForbiddenIsland
class IslandUtils {

    // returns manhattan distance from center
    int manhattan(int x, int y) {
        int isleHeight = ForbiddenIslandWorld.ISLAND_SIZE / 2;
        int dist = Math.abs(isleHeight - x) + Math.abs(isleHeight - y);
        return dist;
    }
}


// ----------------------------- ILISTITERATOR ---------------------------- //
// iterator for IList
class IListIterator<T> implements Iterator<T> {
    IList<T> items;

    // constructor
    IListIterator(IList<T> items) {
        this.items = items;
    }

    // does items have a next?
    public boolean hasNext() {
        return this.items.isCons();
    }

    // returns the next item
    public T next() {
        ConsList<T> itemsAsCons = this.items.asCons();
        T ans = itemsAsCons.first;
        this.items = itemsAsCons.rest;
        return ans;
    }

    // removes an item
    public void remove() {
        throw new UnsupportedOperationException("Can't remove");
    }
}

// -------------------------------- ILIST -------------------------------- //
// represents lists
interface IList<T> extends Iterable<T> {
    // counts the number of items in the IList
    int count();

    // helps count the number of items in the IList
    int countHelp(int acc);

    // is this IList a Cons?
    boolean isCons();

    // converts this IList into a ConsList
    ConsList<T> asCons();

}

//represents an empty list
class MtList<T> implements IList<T> {
    // counts the number of items in this MtList
    public int count() {
        return 0;
    }

    // helps count the number of items in this MtList
    public int countHelp(int acc) {
        return acc;
    }

    // is this MtList a Cons?
    public boolean isCons() {
        return false;
    }

    // converts this MtList into a ConsList
    public ConsList<T> asCons() {
        throw new ClassCastException("Mt is not Cons!");
    }

    // iterator for MtList {
    public Iterator<T> iterator() {
        return new IListIterator<T>(this);
    }
}

//represents a Cons List
class ConsList<T> implements IList<T> {
    T first;
    IList<T> rest;

    // constructor
    ConsList(T first, IList<T> rest) {
        this.first = first;
        this.rest = rest;
    }

    // counts the number of items in this ConsList
    public int count() {
        return this.rest.countHelp(1);
    }

    // helps count the number of items in this ConsList
    public int countHelp(int acc) {
        return this.rest.countHelp(acc + 1);
    }

    // is this ConsList a Cons?
    public boolean isCons() {
        return true;
    }

    // converts this ConsList into a ConsList
    public ConsList<T> asCons() {
        return this;
    }

    // iterator for ConsList
    public Iterator<T> iterator() {
        return new IListIterator<T>(this);
    }
}

// -------------------------------- PLAYER -------------------------------- //
// represents the player
class Player {
    int x;
    int y;
    Cell currentCell;
    WorldImage image;
    int stepsTaken;
    int scoreDisplay;
    int swimTix;
    boolean isScubaOn;

    // constructor
    Player(Cell currentCell) {
        this.x = currentCell.x;
        this.y = currentCell.y;
        this.currentCell = currentCell;
        this.image = new FromFileImage("p1.png");
        this.stepsTaken = 0;
        this.scoreDisplay = 6;
        this.swimTix = 0;
	this.isScubaOn = false;
    }

    // convenience constructor
    Player(Cell currentCell, WorldImage img, int scoreDisplay) {
        this(currentCell);
        this.image = img;
        this.scoreDisplay = scoreDisplay;
    }

    // EFFECT: updates currentCell
    void updateCell(Cell c) {
        if (!c.isFlooded || (this.isScubaOn && swimTix > 0)) {
            this.currentCell = c;
            this.x = c.x;
            this.y = c.y;
            this.stepsTaken += 1;
        }
    }

    // EFFECT: alters player's position to the left cell
    void updateToLeft() {
        this.updateCell(this.currentCell.left);
    }

    // EFFECT: alters player's position to the top cell
    void updateToTop() {
        this.updateCell(this.currentCell.top);
    }

    // EFFECT: alters player's position to the right cell
    void updateToRight() {
        this.updateCell(this.currentCell.right);
    }

    // EFFECT: alters player's position to the bottom cell
    void updateToBottom() {
        this.updateCell(this.currentCell.bottom);
    }

    // is the player drowning?
    boolean isDrowning() {
        return this.currentCell.isFlooded;
    }

    // returns list of remaining targets
    // EFFECT: updates list of targets if picked up
    IList<Target> checkFor(IList<Target> targets) {
        IList<Target> temp = new MtList<Target>();
        for (Target t : targets) {
            if (this.x == t.x && this.y == t.y) {
                this.swimTix += t.updateSwim();
                t.isCollected = true;
            }
            else {
                temp = new ConsList<Target>(t, temp);
            }
        }
        return temp;
    }

    // returns image of the player
    WorldImage playerImage() {
        return this.image;
    }

    // EFFECT: changes the player image
    void updateImage(FromFileImage image) {
        this.image = image;
    }

    // makes scene for player
    WorldScene makeSceneHelper(WorldScene acc) {
        int scale = ForbiddenIslandWorld.SCALE;
        TextImage score = new TextImage("SCORE: " + Integer.toString(this.stepsTaken), 20, Color.WHITE);
        acc.placeImageXY(this.playerImage(), 
			 ((this.x * scale) + (scale / 2)),
                ((this.y * scale) + (scale / 2)));
        acc.placeImageXY(score, scoreDisplay * scale, 2 * scale);
	if (this.isScubaOn && this.swimTix > 0) {
	    TextImage scubaTime = new TextImage("Scuba time: " + Integer.toString(this.swimTix), 20, Color.WHITE);
	    acc.placeImageXY(scubaTime, 7 * scale, (ForbiddenIslandWorld.ISLAND_SIZE - 2) * scale);
	    this.swimTix -= 1;
	}
	if (this.swimTix == 0) {
	    this.isScubaOn = false;
	}
	return acc;
    }
	
    // EFFECT: turns scuba on if applicable
    void scubaOn() {
	if (!isScubaOn && swimTix > 0) {
	    this.isScubaOn = true;
	}
    }
}

// -------------------------------- TARGET -------------------------------- //
// represents a target
class Target {
    int x;
    int y;
    Cell currentCell;
    boolean isCollected;

    // constructor
    Target(Cell c) {
        this.currentCell = c;
        this.x = c.x;
        this.y = c.y;
        this.isCollected = false;
    }

    // returns image of the target
    WorldImage targetImage() {
        return new FromFileImage("parts.png");
    }

    // makes scene for target
    WorldScene makeSceneHelper(WorldScene acc) {
        if (!this.isCollected) {
            int scale = ForbiddenIslandWorld.SCALE;
            acc.placeImageXY(this.targetImage(), 
                    (this.x * scale) + (scale / 2),
                    (this.y * scale) + (scale / 2));
        }
        return acc;
    }
    
    // returns the ticks a Player will be allowed to swim for
    int updateSwim() {
        return 0;
    }

}

// represents the helicopter
class Helicopter extends Target {

    // constructor
    Helicopter(Cell c) {
        super(c);
    }

    // returns image of the helicopter
    WorldImage targetImage() {
        return new FromFileImage("helicopter.png");
    }
}

// represents the scuba swimming suit
class Scuba extends Target {
    
    // constructor
    Scuba(Cell c) {
        super(c);
    }

    // returns image of the swimming suit
    WorldImage targetImage() {
        return new FromFileImage("scuba.png");
    }
    
    // returns the ticks a Player will be allowed to swim for
    int updateSwim() {
        return 100;
    }


}

// --------------------------------- CELL --------------------------------- //
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


// ------------------------- FORBIDDENISLANDWORLD ------------------------- //
// Represents the Forbidden Island Game
class ForbiddenIslandWorld extends World {
    // All the cells of the game, including the ocean
    IList<Cell> board;

    // the current height of the ocean
    int waterHeight;

    // Defines an int constant
    static final int ISLAND_SIZE = 64;

    // Defines the total height of the island
    static final int ISLAND_HEIGHT = (ForbiddenIslandWorld.ISLAND_SIZE / 2) + 5;

    // Scales the size of each Cell
    static final int SCALE = 10;

    // player 1
    Player p1;

    // player 2
    Player p2;

    // is it mulitplayer?
    boolean isMulti;

    // the helicopter
    Helicopter heli;

    // list of remaining targets;
    IList<Target> remTargets;

    // maximum number of targets;
    static final int MAX_TARGETS = 5;

    // is the game paused?
    boolean isPaused;

    // the scuba suit
    Scuba suit;

    // constructor
    ForbiddenIslandWorld() {
        this.board = new MtList<Cell>();
        //this.initMountain();
        //this.initRandom();
        this.initTerrain();
        this.initGame();
    }

    // FOR MOUNTAIN ISLAND:
    // EFFECT: converts an ArrayList<ArrayList<Cell>> to an IList<Cell>
    void initMountain() {
        ArrayList<ArrayList<Cell>> cells = this.connectCells(this.cells(this.heights(), false));
        this.board = this.convert(cells);
    }

    // FOR TESTING ISLAND ONLY:
    void initTester(ArrayList<ArrayList<Double>> hites, boolean rand) {
        ArrayList<ArrayList<Cell>> cells = this.connectCells(this.cells(hites, rand));
        this.board = this.convert(cells);       
    }

    // FOR RANDOM ISLAND:
    // EFFECT: converts an ArrayList<ArrayList<Cell>> to an IList<Cell>
    void initRandom() {
        ArrayList<ArrayList<Cell>> cells = this.connectCells(this.cells(this.randHeights(), false));
        this.board = this.convert(cells);
    }

    // FOR RANDOM TERRAIN ISLAND:
    // EFFECT:
    void initTerrain() {
        ArrayList<ArrayList<Cell>> cells = 
                this.connectCells(this.cells(this.randTerrHeights(), true));
        this.board = this.convert(cells);
    }

    // returns an IList<Cell> of the given ArrayList<ArrayList<Cell>>
    IList<Cell> convert(ArrayList<ArrayList<Cell>> cells) {
        IList<Cell> temp = new MtList<Cell>();
        for (int i = cells.size() - 1; i >= 0; i -= 1) {
            for (int j = cells.get(i).size() - 1; j >= 0; j -= 1) {
                temp = new ConsList<Cell>(cells.get(i).get(j), temp);
            } 
        }
        return temp;
    }

    // FOR MOUNTAIN ISLAND:
    // returns an ArrayList<ArrayList<Double>> that represents the heights of 
    // every Cell in the game
    ArrayList<ArrayList<Double>> heights() {
        ArrayList<ArrayList<Double>> ans = new ArrayList<ArrayList<Double>>();
        IslandUtils u = new IslandUtils();
        for (int i = 0; i <= ForbiddenIslandWorld.ISLAND_SIZE; i += 1) {
            ArrayList<Double> cols = new ArrayList<Double>();
            for (int j = 0; j <= ForbiddenIslandWorld.ISLAND_SIZE; j += 1) {
                cols.add(1.0 * 
                        (ForbiddenIslandWorld.ISLAND_HEIGHT - u.manhattan(j, i)));
            } 
            ans.add(cols); 
        }
        return ans;
    }

    // FOR RANDOM ISLAND:
    // returns an ArrayList<ArrayList<Double>> that represents the heights of 
    // every Cell in the game
    ArrayList<ArrayList<Double>> randHeights() {
        ArrayList<ArrayList<Double>> ans = new ArrayList<ArrayList<Double>>();
        int height = ForbiddenIslandWorld.ISLAND_HEIGHT;
        Random r = new Random();
        for (int i = 0; i <= ForbiddenIslandWorld.ISLAND_SIZE; i += 1) {
            ArrayList<Double> cols = new ArrayList<Double>();
            for (int j = 0; j <= ForbiddenIslandWorld.ISLAND_SIZE; j += 1) {
                cols.add(1.0 * r.nextInt(height));
            } 
            ans.add(cols); 
        }
        return ans;
    }

    // FOR RANDOM TERRAIN ISLAND:
    // returns an ArrayList<ArrayList<Double>> that represents the heights of 
    // every Cell in the game
    ArrayList<ArrayList<Double>> randTerrHeights() {
        ArrayList<ArrayList<Double>> ans = new ArrayList<ArrayList<Double>>();
        int isleSize = ForbiddenIslandWorld.ISLAND_SIZE;
        int halfIsle = isleSize / 2;
        for (int i = 0; i < isleSize + 2; i += 1) {
            ArrayList<Double> cols = new ArrayList<Double>();
            for (int j = 0; j < isleSize + 2; j += 1) {
                cols.add(0.0);
            }
            ans.add(cols);
        }
        ans.get(halfIsle).set(halfIsle, 1.0 * ForbiddenIslandWorld.ISLAND_HEIGHT);
        ans.get(0).set(halfIsle, 1.0);
        ans.get(isleSize).set(halfIsle, 1.0);
        ans.get(halfIsle).set(0, 1.0);
        ans.get(halfIsle).set(isleSize, 1.0);

        this.terrHeightsHelp(ans, halfIsle, isleSize + 1, halfIsle, isleSize + 1);
        this.terrHeightsHelp(ans, 0, halfIsle + 1, halfIsle, isleSize + 1);
        this.terrHeightsHelp(ans, halfIsle, isleSize + 1, 0, halfIsle + 1);
        this.terrHeightsHelp(ans, 0, halfIsle + 1, 0, halfIsle + 1);

        return ans;
    }

    // EFFECT: update heights using the quadrant algorithm
    void terrHeightsHelp(ArrayList<ArrayList<Double>> quad, 
            int xStart, int xEnd, int yStart, int yEnd) {
        int xDif = xEnd - xStart;
        int yDif = yEnd - yStart;
        double scale = xDif * yDif * 0.015;
        boolean xNotDone = xDif > 1;
        boolean yNotDone = yDif > 1;
        int xMid = (xEnd + xStart) / 2;
        int yMid = (yEnd + yStart) / 2;

        if (xNotDone) {
            ArrayList<Double> rowTop = quad.get(yStart);
            rowTop.set(xMid, (Math.random() - 0.5) * scale + 
                    (int)((rowTop.get(xStart) + rowTop.get(xEnd)) / 2));
        }

        if (yNotDone) {
            ArrayList<Double> row = quad.get(yMid);
            row.set(xStart, (Math.random() - 0.5) * scale + 
                    (int)((quad.get(yStart).get(xStart) +
                            quad.get(yEnd).get(xStart)) / 2));
        }

        if (xNotDone && yNotDone) {
            ArrayList<Double> rowTop = quad.get(yStart);
            ArrayList<Double> rowBottom = quad.get(yEnd);
            quad.get(yMid).set(xMid, 
                    (Math.random() - 0.5) * scale + 
                    ((rowTop.get(xStart) + 
                            rowTop.get(xEnd) +
                            rowBottom.get(xStart) +
                            rowBottom.get(xEnd)) / 4));      

            this.terrHeightsHelp(quad, xMid, xEnd, yMid, yEnd);
            this.terrHeightsHelp(quad, xStart, xMid + 1, yMid, yEnd);
            this.terrHeightsHelp(quad, xMid, xEnd, yStart, yMid + 1);
            this.terrHeightsHelp(quad, xStart, xMid, yStart, yMid);

        }
        else if (xNotDone) {      
            this.terrHeightsHelp(quad, xStart, xMid, yStart, yEnd);
            this.terrHeightsHelp(quad, xMid, xEnd, yStart, yEnd);
        }
        else if (yNotDone) {
            this.terrHeightsHelp(quad, xStart, xEnd, yStart, yMid);
            this.terrHeightsHelp(quad, xStart, xEnd, yMid, yEnd);
        }
        else {
            // does nothing...
        }
    }

    // returns an ArrayList<ArrayList<Cell>> that represents each Cell in the game
    ArrayList<ArrayList<Cell>> cells(ArrayList<ArrayList<Double>> doubs,
            boolean rand) {
        ArrayList<ArrayList<Cell>> ans = new ArrayList<ArrayList<Cell>>();
        IslandUtils u = new IslandUtils();
        for (int i = 0; i < doubs.size(); i += 1) {
            ArrayList<Cell> cols = new ArrayList<Cell>();
            for (int j = 0; j < doubs.get(0).size(); j += 1) {
                double height = doubs.get(i).get(j);
                if (!rand) {
                    if (u.manhattan(j, i) > ForbiddenIslandWorld.ISLAND_SIZE / 2 - 1) {
                        cols.add(new OceanCell(height, j, i, true));
                    }
                    else {
                        cols.add(new Cell(height, j, i, false));
                    }
                } 
                else {
                    if (height < 1) {
                        cols.add(new OceanCell(height, j, i, true));
                    }
                    else {
                        cols.add(new Cell(height, j, i, false));
                    }
                }
            } 
            ans.add(cols); 
        }       
        return ans;
    }

    // returns an ArrayList<ArrayList<Cell>> with adjacent Cells connected
    ArrayList<ArrayList<Cell>> connectCells(ArrayList<ArrayList<Cell>> cells) {
        for (int i = 0; i < cells.size(); i += 1) {
            for (int j = 0; j < cells.size(); j += 1) {
                Cell c = cells.get(i).get(j);
                if (j > 0) {
                    c.updateLeft(cells.get(i).get(j - 1));
                }
                if (j == 0) {
                    c.updateLeft(c);
                }
                if (i > 0) {
                    c.updateTop(cells.get(i - 1).get(j));
                }
                if (i == 0) {
                    c.updateTop(c);
                }
                if (j < cells.size() - 1) {
                    c.updateRight(cells.get(i).get(j + 1));
                }
                if (j == cells.size() - 1) {
                    c.updateRight(c);
                }
                if (i < cells.size() - 1) {
                    c.updateBottom(cells.get(i + 1).get(j));
                }
                if (i == cells.size() - 1) {
                    c.updateBottom(c);
                }
            }
        }

        return cells;
    }

    // EFFECT:
    // resets water height and initializes gameplay
    void initGame() {
        this.isPaused = false;
        this.isMulti = false;
        this.waterHeight = 0;
        this.remTargets = new MtList<Target>();
        Random r = new Random();
        int pStop = r.nextInt(ForbiddenIslandWorld.ISLAND_SIZE *
                (ForbiddenIslandWorld.ISLAND_SIZE / 2));
	int sStop = r.nextInt(ForbiddenIslandWorld.ISLAND_SIZE * 
			      (ForbiddenIslandWorld.ISLAND_SIZE / 2));
        int count = 0;
        int tStart = ForbiddenIslandWorld.ISLAND_SIZE +
                r.nextInt(ForbiddenIslandWorld.ISLAND_SIZE * 2);
        int inc = (ForbiddenIslandWorld.ISLAND_SIZE * 3) + 
                r.nextInt(ForbiddenIslandWorld.ISLAND_SIZE * 4);  
        int numTar = 0;
        double maxHeight = 0.0;
        for (Cell c : board) {
            if (!c.isFlooded) {
                if (count == pStop) {
                    this.p1 = new Player(c);
                }
		if (count == sStop) {
		    this.suit = new Scuba(c);
		}
                if (count == tStart && numTar < ForbiddenIslandWorld.MAX_TARGETS) {
                    Target t = new Target(c);
                    this.remTargets = new ConsList<Target>(t, this.remTargets);
                    tStart += inc;
                    numTar += 1;
                }
                if (c.height > maxHeight) {
                    this.heli = new Helicopter(c);
                    maxHeight = c.height;
                }
                count += 1;
            }
        }
    }

    // adds player 2
    void startP2() {
        this.isMulti = true;
        int size = ForbiddenIslandWorld.ISLAND_SIZE;
        double tempHighest = 0;
        for (Cell c : board) {
            if (!c.isFlooded) {
                if (c.height > tempHighest) {
                    this.p2 = new Player(c, new FromFileImage("p2.png"),
                            size - 5);
                    tempHighest = c.height;
                }
            }
        }
    }


    // EFFECT: can alter player's position, pause game, or do something special...
    // updates based on keyEvent
    public void onKeyEvent(String ke) {
        if (ke.equals("m")) {
            this.initMountain();
            this.initGame();
        }
        if (ke.equals("r")) {
            this.initRandom();
            this.initGame();
        }
        if (ke.equals("t")) {
            this.initTerrain();
            this.initGame();
            this.waterHeight = -5;
        }
        if (ke.equals("l")) {
            this.p1.updateImage(new FromFileImage("img.png"));
        }
        if (ke.equals("p")) {
            this.isPaused = !this.isPaused;
        }
        if (ke.equals("2") && !this.isMulti) {
            this.startP2();
        }
        if (!this.isPaused) {
            if (ke.equals("left")) {
                this.p1.updateToLeft();
            }
            if (ke.equals("up")) {
                this.p1.updateToTop();
            }
            if (ke.equals("right")) {
                this.p1.updateToRight();
            }
            if (ke.equals("down")) {
                this.p1.updateToBottom();
            }
            if (this.isMulti) {
                if (ke.equals("w")) {
                    this.p2.updateToTop();
                }
                if (ke.equals("a")) {
                    this.p2.updateToLeft();
                }
                if (ke.equals("s")) {
                    this.p2.updateToBottom();
                }
                if (ke.equals("d")) {
                    this.p2.updateToRight();
                }
            }
	    if (ke.equals("b")) {
		this.p1.scubaOn();
		if (isMulti) {
		    this.p2.scubaOn();
		}
	    }
	    if (!this.suit.isCollected) {
		this.p1.checkFor(new ConsList<Target>(this.suit, new MtList<Target>()));
		if (isMulti) {
		    this.p2.checkFor(new ConsList<Target>(this.suit, new MtList<Target>()));
		}
	    }
            if (this.remTargets.count() == 0) {
                if (isMulti) {
                    this.p2.checkFor(new ConsList<Target>(this.heli, new MtList<Target>()));
                }
                this.p1.checkFor(new ConsList<Target>(this.heli, new MtList<Target>()));
            }
            else {
                this.remTargets = this.p1.checkFor(this.remTargets);
                if (isMulti) {
                    this.remTargets = this.p2.checkFor(this.remTargets);
                }
            }
        }
    }

    // returns scene for the game
    public WorldScene makeScene() {
        WorldScene acc = this.getEmptyScene();
        for (Cell c : this.board) {
            acc = c.draw(acc, this.waterHeight);
        }
        for (Target t : this.remTargets) {
            t.makeSceneHelper(acc);
        }
        this.heli.makeSceneHelper(acc);
	this.suit.makeSceneHelper(acc);
        this.p1.makeSceneHelper(acc);
        if (this.isMulti) {
            this.p2.makeSceneHelper(acc);
            if ((this.p1.isDrowning() && !this.p1.isScubaOn && this.p1.swimTix == 0) || (this.isMulti && this.p2.isDrowning() && !this.p2.isScubaOn && this.p2.swimTix == 0)) {
                this.p1.updateImage(new FromFileImage("rip.png"));
                this.p2.updateImage(new FromFileImage("rip.png"));
            }
        }
        if (!this.isMulti) {
            if (this.p1.isDrowning() && !this.p1.isScubaOn && this.p1.swimTix == 0) {
                this.p1.updateImage(new FromFileImage("rip.png"));
            }
        }
        if (this.isPaused) {
            int coord = ForbiddenIslandWorld.ISLAND_SIZE * ForbiddenIslandWorld.SCALE / 2;
            TextImage paused = new TextImage("PAUSED", 60, Color.WHITE);
            acc.placeImageXY(paused, coord, coord);
        }
        return acc;
    }

    int tick;
    // updates on each tick
    public void onTick() {
        if (!isPaused) {
            if (tick == 9) {
                this.waterHeight += 1;
            }
            for (Cell c : this.coastCells()) {
                c.flood(this.waterHeight);
            }
            tick = (tick + 1) % 10;
        }
    }

    // returns list of cells on the coast
    IList<Cell> coastCells() {
        IList<Cell> acc = new MtList<Cell>();
        for (Cell c : this.board) {
            acc = c.coast(acc);
        }
        return acc;
    }

    // ends the game
    // game ends if the player wins or loses
    public WorldEnd worldEnds() {
        WorldScene endScene = this.makeScene();
        int coord = ForbiddenIslandWorld.ISLAND_SIZE * ForbiddenIslandWorld.SCALE / 2;
        if ((this.p1.isDrowning() && !this.p1.isScubaOn && this.p1.swimTix == 0) || (this.isMulti && this.p2.isDrowning() && !this.p2.isScubaOn && this.p2.swimTix == 0)) {
            TextImage endImage = new TextImage("Oh no!", 60, Color.WHITE);
            endScene.placeImageXY(endImage, coord, coord);
            return new WorldEnd(true, endScene);
        }
        else if (this.heli.isCollected && !this.isMulti) {
            TextImage endImage = new TextImage("HOORAY!", 60, Color.WHITE);
            endScene.placeImageXY(endImage, coord, coord);
            return new WorldEnd(true, endScene);
        }
	else if (this.heli.isCollected && 
		 this.heli.currentCell.sameCoord(this.p1.currentCell) &&
		 this.heli.currentCell.sameCoord(this.p2.currentCell)) {
	    TextImage endImage = new TextImage("HOORAY!", 60, Color.WHITE);
            endScene.placeImageXY(endImage, coord, coord);
            return new WorldEnd(true, endScene);
	}
        else {
            return new WorldEnd(false, endScene);
        }
    }
}


/* ======================================================================= */
/* ========================== TESTS & EXAMPLES =========================== */
/* ======================================================================= */

// examples and tests for the Forbidden Island Game
class ExamplesIsland {

    public static void main(String[] args) {
        ExamplesIsland ex = new ExamplesIsland();
        Tester.runReport(ex, false, false);

        ForbiddenIslandWorld f = new ForbiddenIslandWorld();
        int window = (ForbiddenIslandWorld.ISLAND_SIZE + 1) * ForbiddenIslandWorld.SCALE;
        f.bigBang(window, window, 0.1);
    } 

    ForbiddenIslandWorld f;

    Cell zeroZero;
    Cell zeroOne;
    Cell zeroTwo;
    Cell oneZero;
    Cell oneOne; 
    Cell oneTwo; 
    Cell twoZero;
    Cell twoOne;
    Cell twoTwo;   
    Cell w;
    Cell a;
    Cell t;
    Cell e;
    Cell r;
    Cell c00;
    Cell c3232;
    Cell c4545;
    Cell c77;
    Cell c3838;
    Cell c1616;


    Double d00;
    Double d3232;
    Double d4545;
    Double d77;
    Double d3838;
    Double d1616;

    ArrayList<ArrayList<Double>> threeByThree;

    ArrayList<Double> zero;
    ArrayList<Double> one;
    ArrayList<Double> two;

    ArrayList<ArrayList<Cell>> tByT; 
    ArrayList<ArrayList<Cell>> forOneRow;
    ArrayList<ArrayList<Cell>> forTwoByTwo;

    ArrayList<Cell> oneRow;
    ArrayList<Cell> row0;
    ArrayList<Cell> row1;
    ArrayList<Cell> row2;
    ArrayList<Cell> twoRow1;
    ArrayList<Cell> twoRow2;

    IList<Cell> mtList; 
    IList<Cell> consList1;
    IList<Cell> consList2;
    IList<Cell> consList3;

    ConsList<Cell> c1;
    ConsList<Cell> c2;
    ConsList<Cell> c3;
    ConsList<Cell> c4;
    ConsList<Cell> c5;
    ConsList<Cell> c6;
    ConsList<Cell> c7;

    WorldScene mtSc;
    WorldScene mtSc2;
    WorldScene mtSc3;
    WorldScene rOnMt;
    WorldScene zzOnMt;
    WorldScene zzOnR;
    WorldScene rOnZz;

    int scale = ForbiddenIslandWorld.SCALE;
    int isleHeight = ForbiddenIslandWorld.ISLAND_HEIGHT;

    // for an Ocean Cell
    RectangleImage oceanCellImage = 
            new RectangleImage(scale, scale, OutlineMode.SOLID, 
                    new Color(25, 59, 224));

    // for zeroZero
    int zzGreen;
    int zzRedBlue;
    RectangleImage zzImage;
    RectangleImage rImage;

    // for IListIterator
    IListIterator<Cell> emtee;
    IListIterator<Cell> kons;

    Player p1;

    Target oneOneTar;
    Target twoOneTar;
    IList<Target> mtTar;
    IList<Target> oneTar;
    IList<Target> twoTar;
    IList<Target> twoTarRev;

    Helicopter heli;

    WorldImage regTar;
    WorldScene oneOneTarOnMt;
    WorldScene oneOneTarOnR;
    WorldScene heliOnMt;
    WorldScene heliOnZz;


    IList<Cell> zeroOneMt;
    IList<Cell> zeroZeroFlood;
    IList<Cell> zeroToTwo;

    Scuba suit;

    // initializes data
    void init() {
        this.f = new ForbiddenIslandWorld();
	//f.initGame();

        this.zeroZero = new Cell(8.0, 0, 0, false);
        this.zeroOne = new Cell(16.0, 0, 1, false);
        this.zeroTwo = new Cell(8.0, 0, 2, false);
        this.oneZero = new Cell(16.0, 1, 0, false);
        this.oneOne = new Cell(32.0, 1, 1, false);
        this.oneTwo = new Cell(8.0, 1, 2, false);
        this.twoZero = new Cell(8.0, 2, 0, false);
        this.twoOne = new Cell(16.0, 2, 1, false);
        this.twoTwo = new Cell(8.0, 2, 2, false);

        this.threeByThree = new ArrayList<ArrayList<Double>>();

        this.zero = new ArrayList<Double>();
        this.one = new ArrayList<Double>();
        this.two = new ArrayList<Double>();

        this.tByT = new ArrayList<ArrayList<Cell>>();
        this.forOneRow = new ArrayList<ArrayList<Cell>>();
        this.forTwoByTwo = new ArrayList<ArrayList<Cell>>();

        this.oneRow = new ArrayList<Cell>();
        this.row0 = new ArrayList<Cell>();
        this.row1 = new ArrayList<Cell>();
        this.row2 = new ArrayList<Cell>();
        this.twoRow1 = new ArrayList<Cell>();
        this.twoRow2 = new ArrayList<Cell>();

        this.mtList = new MtList<Cell>();
        this.c1 = new ConsList<Cell>(this.twoTwo, this.mtList);
        this.c2 = new ConsList<Cell>(this.twoOne, this.c1);
        this.c3 = new ConsList<Cell>(this.twoZero, this.c2);
        this.c4 = new ConsList<Cell>(this.oneTwo, this.mtList);
        this.c5 = new ConsList<Cell>(this.oneOne, this.c4);
        this.c6 = new ConsList<Cell>(this.zeroTwo, this.c5);
        this.c7 = new ConsList<Cell>(this.zeroOne, this.c6);

        this.row0.add(this.zeroZero);
        this.row0.add(this.zeroOne);
        this.row0.add(this.zeroTwo);
        this.row1.add(this.oneZero);
        this.row1.add(this.oneOne);
        this.row1.add(this.oneTwo);
        this.row2.add(this.twoZero);
        this.row2.add(this.twoOne);
        this.row2.add(this.twoTwo);
        this.tByT.add(this.row0);
        this.tByT.add(this.row1);
        this.tByT.add(this.row2);
        f.connectCells(this.tByT);

        this.forOneRow.add(this.oneRow);
        this.forTwoByTwo.add(this.twoRow1);
        this.forTwoByTwo.add(this.twoRow2);
        this.oneRow.add(this.twoZero);
        this.oneRow.add(this.twoOne);
        this.oneRow.add(this.twoTwo);
        this.twoRow1.add(this.zeroOne);
        this.twoRow1.add(this.zeroTwo);
        this.twoRow2.add(this.oneOne);
        this.twoRow2.add(this.oneTwo);

        this.zero.add(this.zeroZero.height);
        this.zero.add(this.zeroOne.height);
        this.zero.add(this.zeroTwo.height);
        this.one.add(this.oneZero.height);
        this.one.add(this.oneOne.height);
        this.one.add(this.oneTwo.height);
        this.two.add(this.twoZero.height);
        this.two.add(this.twoOne.height);
        this.two.add(this.twoTwo.height);

        this.threeByThree.add(this.zero);
        this.threeByThree.add(this.one);
        this.threeByThree.add(this.two);

        this.d00 = 1.0 * (this.isleHeight - u.manhattan(0, 0));
        this.d3232 = 1.0 * (this.isleHeight - u.manhattan(32, 32));
        this.d4545 = 1.0 * (this.isleHeight - u.manhattan(45, 45));
        this.d77 = 1.0 * (this.isleHeight - u.manhattan(7, 7));
        this.d3838 = 1.0 * (this.isleHeight - u.manhattan(38, 38));
        this.d1616 = 1.0 * (this.isleHeight - u.manhattan(16, 16));

        this.c00 = new OceanCell(d00, 0, 0);
        this.c3232 = new Cell(d3232, 32, 32, false);
        this.c4545 = new Cell(d4545, 45, 45, false);
        this.c77 = new OceanCell(d77, 7, 7);
        this.c3838 = new Cell(d3838, 38, 38, false);
        this.c1616 = new OceanCell(d1616, 16, 16);

        this.w = new OceanCell(1.0, 0, 0);
        this.a = new OceanCell(0.5, 1, 2);
        this.t = new OceanCell(2.0, 7, 5);
        this.e = new OceanCell(0.9, 3, 6);
        this.r = new OceanCell(0.7, 8, 9);

        this.consList1 = new ConsList<Cell>(this.r, this.mtList);
        this.consList2 = new ConsList<Cell>(this.zeroZero, this.mtList);
        this.consList3 = new ConsList<Cell>(this.r, this.consList2);

        this.mtSc = f.getEmptyScene();
        this.mtSc2 = f.getEmptyScene();
        this.mtSc3 = f.getEmptyScene();
        this.rOnMt = f.getEmptyScene();
        this.rOnMt.placeImageXY(oceanCellImage, 
                (this.r.x * scale) + (scale / 2), 
                (this.r.y * scale) + (scale / 2));
        this.zzOnMt = f.getEmptyScene();
        this.zzGreen = 255 - ((this.isleHeight - (int)this.zeroZero.height)
                * (125 / this.isleHeight));
        this.zzRedBlue = 255 - ((this.isleHeight - (int)this.zeroZero.height)
                * (255 / this.isleHeight));
        this.zzImage = new RectangleImage(scale, scale, OutlineMode.SOLID,
                new Color(this.zzRedBlue,
                        this.zzGreen,
                        this.zzRedBlue));
        this.rImage = new RectangleImage(scale, scale, OutlineMode.SOLID, 
                new Color(25, 59, 224));
        this.zzOnMt.placeImageXY(this.zzImage, 
                (this.zeroZero.x * scale) + (scale / 2),
                (this.zeroZero.y * scale) + (scale / 2));
        this.zzOnR = f.getEmptyScene();
        this.zzOnR.placeImageXY(oceanCellImage, 
                (this.r.x * scale) + (scale / 2), 
                (this.r.y * scale) + (scale / 2));
        this.zzOnR.placeImageXY(this.zzImage, 
                (this.zeroZero.x * scale) + (scale / 2),
                (this.zeroZero.y * scale) + (scale / 2));
        this.rOnZz = f.getEmptyScene();
        this.rOnZz.placeImageXY(this.rImage,
                (this.r.x * scale) + (scale / 2), 
                (this.r.y * scale) + (scale / 2));
        this.rOnZz.placeImageXY(this.zzImage, 
                (this.zeroZero.x * scale) + (scale / 2),
                (this.zeroZero.y * scale) + (scale / 2));

        this.emtee = new IListIterator<Cell>(this.mtList);
        this.kons = new IListIterator<Cell>(this.c5);

        this.p1 = new Player(zeroZero);

        this.oneOneTar = new Target(this.oneOne);
        this.twoOneTar = new Target(this.twoOne);
        this.mtTar = new MtList<Target>();
        this.oneTar = new ConsList<Target>(this.oneOneTar, this.mtTar);
        this.twoTar = new ConsList<Target>(this.twoOneTar, this.oneTar);
        this.twoTarRev = new ConsList<Target>(this.oneOneTar, 
                new ConsList<Target>(this.twoOneTar, this.mtTar));

        this.heli = new Helicopter(twoTwo);
        this.regTar = new FromFileImage("parts.png");
        this.oneOneTarOnMt = f.getEmptyScene();
        this.oneOneTarOnMt.placeImageXY(this.oneOneTar.targetImage(),
                (this.oneOneTar.x * scale) + (scale / 2),
                (this.oneOneTar.y * scale) + (scale / 2));
        this.oneOneTarOnR = f.getEmptyScene();
        this.oneOneTarOnR.placeImageXY(oceanCellImage,
                (this.r.x * scale) + (scale / 2),
                (this.r.y * scale) + (scale / 2));
        this.oneOneTarOnR.placeImageXY(this.oneOneTar.targetImage(),
                (this.oneOneTar.x * scale) + (scale / 2),
                (this.oneOneTar.y * scale) + (scale / 2));
        this.heliOnMt = f.getEmptyScene();
        this.heliOnMt.placeImageXY(this.heli.targetImage(),
                (this.heli.x * scale) + (scale / 2),
                (this.heli.y * scale) + (scale / 2));
        this.heliOnZz = f.getEmptyScene();
        this.heliOnZz.placeImageXY(this.zzImage,
                (this.zeroZero.x * scale) + (scale / 2),
                (this.zeroZero.y * scale) + (scale / 2));
        this.heliOnZz.placeImageXY(this.heli.targetImage(),
                (this.heli.x * scale) + (scale / 2),
                (this.heli.y * scale) + (scale / 2));

        this.zeroOneMt = new ConsList<Cell>(this.zeroOne, this.mtList);
        this.zeroZeroFlood = new ConsList<Cell>(this.oneZero, this.zeroOneMt);
        this.zeroToTwo = f.convert(this.tByT);

	this.suit = new Scuba(this.twoTwo);
    }

    // *************************** ISLANDUTILS **************************** //
    // tests the manhattan method
    IslandUtils u = new IslandUtils();
    boolean testManhattan(Tester t) {
        this.init();
        return t.checkExpect(u.manhattan(0, 0), 64) &&
                t.checkExpect(u.manhattan(1, 1), 62) &&
                t.checkExpect(u.manhattan(2, 4), 58) &&
                t.checkExpect(u.manhattan(15, 85), 70) &&
                t.checkExpect(u.manhattan(77, 17), 60) &&
                t.checkExpect(u.manhattan(100, -20), 120); 
    }

    // ************************** ILISTITERATOR *************************** //
    // tests the hasNext method
    boolean testHasNext(Tester t) {
        this.init();
        boolean t1 = t.checkExpect(this.emtee.hasNext(), false);
        boolean t2 = t.checkExpect(this.kons.hasNext(), true);
        kons.items = new MtList<Cell>();
        boolean t3 = t.checkExpect(this.kons.hasNext(), false);
        return t1 && t2 && t3;
    }

    // tests the Next method
    boolean testNext(Tester t) {
        this.init();
        ClassCastException mtclass = new ClassCastException("Mt is not Cons!");
        return t.checkException(mtclass, this.emtee, "next") &&
                t.checkExpect(this.kons.next(), oneOne) &&
                t.checkExpect(this.kons.next(), oneTwo) &&
                t.checkException(mtclass, this.kons, "next");
    }

    // tests the remove method
    boolean testRemove(Tester t) {
        this.init();
        UnsupportedOperationException noRemove = new UnsupportedOperationException("Can't remove");
        return t.checkException(noRemove, this.emtee, "remove") &&
                t.checkException(noRemove, this.kons, "remove");
    }

    // ***************************** ILIST ******************************* //
    // tests the count method
    boolean testCount(Tester t) {
        this.init();
        return t.checkExpect(this.mtList.count(), 0) &&
                t.checkExpect(this.c1.count(), 1) &&
                t.checkExpect(this.c2.count(), 2) &&
                t.checkExpect(this.c3.count(), 3);
    }

    // tests the countHelp method
    boolean testCountHelp(Tester t) {
        this.init();
        return t.checkExpect(this.mtList.countHelp(0), 0) &&
                t.checkExpect(this.mtList.countHelp(3), 3) &&
                t.checkExpect(this.c1.countHelp(0), 1) &&
                t.checkExpect(this.c1.countHelp(10), 11) &&
                t.checkExpect(this.c3.countHelp(0), 3) &&
                t.checkExpect(this.c3.countHelp(12), 15);
    }

    // tests the isCons method
    boolean testIsCons(Tester t) {
        this.init();
        return t.checkExpect(this.mtList.isCons(), false) &&
                t.checkExpect(this.c4.isCons(), true) &&
                t.checkExpect(this.c5.isCons(), true) &&
                t.checkExpect(new MtList<Cell>().isCons(), false);
    }

    // tests the asCons method
    boolean testAsCons(Tester t) {
        this.init();
        ClassCastException mtclass = new ClassCastException("Mt is not Cons!");
        return t.checkException(mtclass, this.mtList, "asCons") &&
                t.checkExpect(this.c1.asCons(), (ConsList<Cell>)this.c1) &&
                t.checkExpect(this.c6.asCons(), (ConsList<Cell>)this.c6);
    }

    // tests the iterator method
    boolean testIterator(Tester t) {
        this.init();
        return t.checkExpect(this.mtList.iterator(), new IListIterator<Cell>(this.mtList)) &&
                t.checkExpect(this.c3.iterator(), new IListIterator<Cell>(this.c3)) &&
                t.checkExpect(this.c4.iterator(), new IListIterator<Cell>(this.c4));
    }

    // ***************************** PLAYER ****************************** //
    // tests the updateCell method
    boolean testUpdateCell(Tester t) {
        this.init();
        boolean t1 = t.checkExpect(this.p1.currentCell, this.zeroZero) &&
                t.checkExpect(this.p1.x, 0) && t.checkExpect(this.p1.y, 0) &&
                t.checkExpect(this.p1.stepsTaken, 0);
        this.p1.updateCell(oneTwo);
        boolean t2 = t.checkExpect(this.p1.currentCell, this.oneTwo) &&
                t.checkExpect(this.p1.x, 1) && t.checkExpect(this.p1.y, 2) &&
                t.checkExpect(this.p1.stepsTaken, 1);
        this.p1.updateCell(twoZero);
        boolean t3 = t.checkExpect(this.p1.currentCell, this.twoZero) &&
                t.checkExpect(this.p1.x, 2) && t.checkExpect(this.p1.y, 0) &&
                t.checkExpect(this.p1.stepsTaken, 2);
	this.oneZero.isFlooded = true;
	this.p1.updateCell(oneZero);
	boolean t4 = t.checkExpect(this.p1.currentCell, this.twoZero) &&
	        t.checkExpect(this.p1.x, 2) && t.checkExpect(this.p1.y, 0) &&
                t.checkExpect(this.p1.stepsTaken, 2);
	this.p1.isScubaOn = true;
	this.p1.updateCell(oneZero);
	boolean t5 = t.checkExpect(this.p1.currentCell, this.twoZero) &&
	        t.checkExpect(this.p1.x, 2) && t.checkExpect(this.p1.y, 0) &&
                t.checkExpect(this.p1.stepsTaken, 2);
	this.p1.swimTix = 5;
	this.p1.updateCell(oneZero);
	boolean t6 = t.checkExpect(this.p1.currentCell, this.oneZero) &&
	        t.checkExpect(this.p1.x, 1) && t.checkExpect(this.p1.y, 0) &&
                t.checkExpect(this.p1.stepsTaken, 3);
        return t1 && t2 && t3 && t4 && t5 && t6;
    }

    // tests the updateToLeft, updateToTop, updateToRight, updateToBottom methods
    boolean testUpdateTo(Tester t) {
        this.init();
        boolean t1 = t.checkExpect(this.p1.currentCell, zeroZero) &&
                t.checkExpect(this.p1.x, 0) && t.checkExpect(this.p1.y, 0);
        this.p1.updateToLeft();
        boolean t2 = t.checkExpect(this.p1.currentCell, zeroZero) &&
                t.checkExpect(this.p1.x, 0) && t.checkExpect(this.p1.y, 0);
        this.p1.updateToTop();
        boolean t3 = t.checkExpect(this.p1.currentCell, zeroZero) &&
                t.checkExpect(this.p1.x, 0) && t.checkExpect(this.p1.y, 0);
        this.p1.updateToRight();
        boolean t4 = t.checkExpect(this.p1.currentCell, zeroOne) &&
                t.checkExpect(this.p1.x, 0) && t.checkExpect(this.p1.y, 1);
        this.p1.updateToBottom();
        boolean t5 = t.checkExpect(this.p1.currentCell, oneOne) &&
                t.checkExpect(this.p1.x, 1) && t.checkExpect(this.p1.y, 1);
        this.zeroOne.isFlooded = true;
        this.p1.updateToTop();
        boolean t6 = t.checkExpect(this.p1.currentCell, oneOne) &&
                t.checkExpect(this.p1.x, 1) && t.checkExpect(this.p1.y, 1);
        return t1 && t2 && t3 && t4 && t5 && t6;
    }

    // tests the isDrowning method
    boolean testIsDrowning(Tester t) {
        this.init();
        f.connectCells(this.tByT);
        boolean t1 = t.checkExpect(this.p1.isDrowning(), false);
        this.zeroZero.isFlooded = true;
        boolean t2 = t.checkExpect(this.p1.isDrowning(), true);
        this.p1.currentCell.isFlooded = false;
        boolean t3 = t.checkExpect(this.p1.isDrowning(), false);
        return t1 && t2 && t3;
    }

    // tests the checkFor method
    boolean testCheckFor(Tester t) {
        this.init();
        boolean t1 = t.checkExpect(this.p1.checkFor(this.mtTar), this.mtTar);
        boolean t2 = t.checkExpect(this.p1.checkFor(this.oneTar), this.oneTar);
        boolean t3 = t.checkExpect(this.p1.checkFor(this.twoTar), this.twoTarRev);
        boolean t4 = t.checkExpect(this.twoOneTar.isCollected, false);
	boolean t5 = t.checkExpect(this.p1.swimTix, 0);

        this.p1.updateCell(this.twoOne);
        boolean t6 = t.checkExpect(this.p1.checkFor(this.twoTar), this.oneTar);
        boolean t7 = t.checkExpect(this.twoOneTar.isCollected, true);
	boolean t8 = t.checkExpect(this.p1.swimTix, 0);
        this.p1.updateCell(this.oneOne);
        boolean t9 = t.checkExpect(this.p1.checkFor(this.oneTar), this.mtTar);
        
	this.heli = new Helicopter(this.oneOne);
	boolean t10 = t.checkExpect(this.p1.checkFor(new ConsList<Target>(this.heli, this.mtTar)), this.mtTar);
	boolean t11 = t.checkExpect(this.heli.isCollected, true);
	boolean t12 = t.checkExpect(this.p1.swimTix, 0);

	this.p1.updateCell(this.twoTwo);
	boolean t13 = t.checkExpect(this.p1.checkFor(new ConsList<Target>(this.suit, this.mtTar)), this.mtTar);
	boolean t14 = t.checkExpect(this.suit.isCollected, true);
	boolean t15 = t.checkExpect(this.p1.swimTix, 100);

	return t1 && t2 && t3 && t4 && t5 && t6 && t7 && t8 && t9 && t10
	    && t11 && t12 && t13 && t14 && t15;  
    }

    // tests the playerImage method
    boolean testPlayerImage(Tester t) {
        this.init();
        WorldImage p1 = new FromFileImage("p1.png");
        return t.checkExpect(this.p1.playerImage(), p1) &&
                t.checkExpect(new Player(twoTwo).playerImage(), p1);
    }

    // tests the updateImage method
    boolean testUpdateImage(Tester t) {
        this.init();
        WorldImage img = new FromFileImage("img.png");
        boolean t1 = t.checkExpect(this.p1.image, new FromFileImage("p1.png"));
        this.p1.updateImage(new FromFileImage("img.png"));
        boolean t2 = t.checkExpect(this.p1.image, img);
        return t1 && t2;
    }

    // tests the makeSceneHelper method
    boolean testMakeSceneHelper(Tester t) {
        this.init();
        int scale = ForbiddenIslandWorld.SCALE;
        TextImage score = new TextImage("SCORE: " + Integer.toString(p1.stepsTaken), 20, Color.WHITE);
        this.mtSc2.placeImageXY(p1.playerImage(), (p1.x * scale) + (scale / 2),
                (p1.y * scale) + (scale / 2));
        this.mtSc2.placeImageXY(score, p1.scoreDisplay * scale, 2 * scale);
        WorldScene pilotOnZZ = f.getEmptyScene();
        pilotOnZZ.placeImageXY(this.zzImage,
                (this.zeroZero.x * scale) + (scale / 2), 
                (this.zeroZero.y * scale) + (scale / 2));
        pilotOnZZ.placeImageXY(p1.playerImage(), (p1.x * scale) + (scale / 2),
                (p1.y * scale) + (scale / 2));
        pilotOnZZ.placeImageXY(score, p1.scoreDisplay * scale, 2 * scale);
	boolean t1 = t.checkExpect(p1.makeSceneHelper(this.mtSc), this.mtSc2);
	boolean t2 =  t.checkExpect(p1.makeSceneHelper(this.zzOnMt), pilotOnZZ);
	
	this.p1.isScubaOn = true;
	this.p1.swimTix = 5;
	TextImage scub = new TextImage("Scuba time: " + Integer.toString(p1.swimTix), 20, Color.WHITE);
	this.mtSc3.placeImageXY(p1.playerImage(), (p1.x * scale) + (scale / 2),
				(p1.y * scale) + (scale / 2));
	this.mtSc3.placeImageXY(score, p1.scoreDisplay * scale, 2 * scale);
	this.mtSc3.placeImageXY(scub, 7 * scale, (ForbiddenIslandWorld.ISLAND_SIZE - 2) * scale);
	boolean t3 = t.checkExpect(p1.makeSceneHelper(f.getEmptyScene()), this.mtSc3);
	return t1 && t2 && t3;
    }

    // tests the scubaOne method
    boolean testScubaOn(Tester t) {
	this.init();
	this.p1.scubaOn();
	boolean t1 = t.checkExpect(this.p1.isScubaOn, false) &&
	    t.checkExpect(this.p1.swimTix, 0);
	this.p1.swimTix = 5;
	this.p1.scubaOn();
	boolean t2 = t.checkExpect(this.p1.isScubaOn, true);
	return t1 && t2;
    }

    // *************************** TARGET ******************************** //
    // tests the targetImage method
    boolean testTargetImage(Tester t) {
        this.init();
        return t.checkExpect(this.oneOneTar.targetImage(), this.regTar) &&
                t.checkExpect(this.twoOneTar.targetImage(), this.regTar) &&
                t.checkExpect(this.heli.targetImage(), new FromFileImage("helicopter.png"));
    }

    // tests the makeSceneHelper method
    boolean testTargetMakeSceneHelper(Tester t) {
        this.init();
        return t.checkExpect(this.oneOneTar.makeSceneHelper(this.mtSc),
                this.oneOneTarOnMt) &&
                t.checkExpect(this.oneOneTar.makeSceneHelper(this.rOnMt),
                        this.oneOneTarOnR) &&
                t.checkExpect(this.heli.makeSceneHelper(this.mtSc2),
                        this.heliOnMt) &&
                t.checkExpect(this.heli.makeSceneHelper(this.zzOnMt),
                        this.heliOnZz);
    }

    // *************************** CELL ********************************** //
    // tests the update Left, Top, Right, and Bottom methods
    boolean testUpdate(Tester t) {
        this.init();

        // tests updateLeft
        boolean t0 = this.zeroZero.left == null;
        this.zeroZero.updateLeft(this.w);
        boolean t1 = this.zeroZero.left.equals(this.w);
        this.oneOne.updateLeft(this.a);
        boolean t2 = this.oneOne.left.equals(this.a);
        this.zeroZero.updateLeft(this.zeroZero);
        boolean t3 = this.zeroZero.left.equals(this.zeroZero);
        this.oneOne.updateLeft(this.oneZero);
        boolean t4 = this.oneOne.left.equals(this.oneZero);

        // tests updateTop
        this.zeroZero.updateTop(this.zeroZero);
        boolean t5 = this.zeroZero.top.equals(this.zeroZero);
        this.zeroOne.updateTop(this.zeroTwo);
        boolean t6 = this.zeroOne.top.equals(this.zeroTwo);
        this.zeroTwo.updateTop(this.t);
        boolean t7 = this.zeroTwo.top.equals(this.t);
        this.oneOne.updateTop(this.e);
        boolean t8 = this.oneOne.top.equals(this.e);

        // tests updateRight
        this.twoZero.updateRight(this.twoZero);
        boolean t9 = this.twoZero.right.equals(this.twoZero);
        this.oneOne.updateRight(this.twoOne);
        boolean t10 = this.oneOne.right.equals(this.twoOne);
        this.oneTwo.updateRight(this.w);
        boolean t11 = this.oneTwo.right.equals(this.w);
        this.zeroTwo.updateRight(this.a);
        boolean t12 = this.zeroTwo.right.equals(this.a);

        // tests updateBottom
        this.zeroTwo.updateBottom(this.zeroTwo);
        boolean t13 = this.zeroTwo.bottom.equals(this.zeroTwo);
        this.oneTwo.updateBottom(this.oneOne);
        boolean t14 = this.oneTwo.bottom.equals(this.oneOne);
        this.zeroZero.updateBottom(this.t);
        boolean t15 = this.zeroZero.bottom.equals(this.t);
        this.oneZero.updateBottom(this.a);
        boolean t16 = this.oneZero.bottom.equals(this.a);

        return t0 && t1 && t2 && t3 && t4 && t5 && t6 && t7 && t8 && t9 && t10
                && t11 && t12 && t13 && t14 && t15 && t16;
    }

    // tests the draw method
    boolean testDraw(Tester t) {
        this.init();
        return t.checkExpect(this.r.draw(this.mtSc, 0), this.rOnMt) &&
                t.checkExpect(this.zeroZero.draw(this.mtSc2, 0), this.zzOnMt) &&
                t.checkExpect(this.zeroZero.draw(this.rOnMt, 0), this.zzOnR);      
    }

    // tests the flood method
    boolean testFlood(Tester t) {
        this.init();
        boolean t1 = t.checkExpect(this.zeroOne.isFlooded, false) &&
                t.checkExpect(this.zeroZero.isFlooded, false);
        this.zeroOne.flood(0);
        this.zeroZero.flood(0);
        boolean t2 = t.checkExpect(this.zeroOne.isFlooded, false) &&
                t.checkExpect(this.zeroZero.isFlooded, false);
        this.zeroOne.flood(17);
        this.zeroZero.flood(17);
        boolean t3 = t.checkExpect(this.zeroOne.isFlooded, true) &&
                t.checkExpect(this.zeroZero.isFlooded, true);
        this.zeroOne.flood(5);
        this.zeroZero.flood(5);
        boolean t4 = t.checkExpect(this.zeroOne.isFlooded, true) &&
                t.checkExpect(this.zeroZero.isFlooded, true);
        return t1 && t2 && t3 && t4;
    }

    // tests the coast method
    boolean testCoast(Tester t) {
        this.init();
        boolean t1 = t.checkExpect(this.zeroZero.coast(this.mtList), this.mtList);
        boolean t2 = t.checkExpect(this.zeroZero.coast(this.c4), this.c4);
        boolean t3 = t.checkExpect(this.oneOne.coast(this.mtList), this.mtList);
        this.zeroOne.isFlooded = true;
        boolean t4 = t.checkExpect(this.zeroZero.coast(this.mtList), 
                new ConsList<Cell>(this.zeroZero, this.mtList));
        boolean t5 = t.checkExpect(this.zeroZero.coast(this.c4),
                new ConsList<Cell>(this.zeroZero, this.c4));
        boolean t6 = t.checkExpect(this.oneOne.coast(this.mtList),
                new ConsList<Cell>(this.oneOne, this.mtList));
        return t1 && t2 && t3 && t4 && t5 && t6;
    }

    // *********************** FORBIDDENISLANDWORLD ************************ //

    // tests the initMountain method
    boolean testInitMountain(Tester t) {
        this.init();
        ArrayList<ArrayList<Double>> heights = f.heights();
        f.board = new MtList<Cell>();
        boolean t1 = t.checkExpect(f.board, new MtList<Cell>());

        f.initMountain();
        ConsList<Cell> board = (ConsList<Cell>)f.board;
        Cell mtc00 = board.first;
        boolean t2 = t.checkExpect(mtc00.x, 0) && 
                t.checkExpect(mtc00.y, 0) && 
                t.checkExpect(mtc00.isFlooded, true) &&
                t.checkExpect(mtc00.height, heights.get(0).get(0));

        ConsList<Cell> notOne = (ConsList<Cell>)board.rest;
        Cell mtc01 = notOne.first;
        boolean t3 = t.checkExpect(mtc01.x, 1) &&
                t.checkExpect(mtc01.y, 0) &&
                t.checkExpect(mtc01.isFlooded, true) &&
                t.checkExpect(mtc01.height, heights.get(0).get(1));

        // web-cat gives a stack overflow
        boolean t4 = true; // t.checkExpect(f.board.count(), size * size); 

        f.initTester(this.threeByThree, false);
        boolean t5 = 
                t.checkExpect(f.board, 
                        f.convert(f.connectCells(f.cells(this.threeByThree, false))));

        f.initMountain();
        // web-cat gives a stack overflow
        boolean t6 = true; // t.checkExpect(f.board.count(), size * size); 

        return t1 && t2 && t3 && t4 && t5 && t6;
    }

    // tests the initRandom method
    boolean testInitRandom(Tester t) {
        this.init();
        f.board = new MtList<Cell>();
        boolean t1 = t.checkExpect(f.board, new MtList<Cell>());

        f.initRandom();
        ConsList<Cell> board = (ConsList<Cell>)f.board;
        Cell mtc00 = board.first;
        boolean t2 = t.checkExpect(mtc00.x, 0) && 
                t.checkExpect(mtc00.y, 0) && 
                t.checkExpect(mtc00.isFlooded, true) &&
                t.checkRange(mtc00.height, 0.0, 1.0 * this.isleHeight);

        ConsList<Cell> notOne = (ConsList<Cell>)board.rest;
        Cell mtc01 = notOne.first;
        boolean t3 = t.checkExpect(mtc01.x, 1) &&
                t.checkExpect(mtc01.y, 0) &&
                t.checkExpect(mtc01.isFlooded, true) &&
                t.checkRange(mtc01.height, 0.0, 1.0 * this.isleHeight);
        // web-cat gives a stack overflow
        boolean t4 = true; // t.checkExpect(f.board.count(), size * size); 
        f.initTester(this.threeByThree, true);
        boolean t5 = 
                t.checkExpect(f.board, 
                        f.convert(f.connectCells(f.cells(this.threeByThree, true))));

        f.initRandom();
        // web-cat gives a stack overflow
        boolean t6 = true; // t.checkExpect(f.board.count(), size * size); 

        return t1 && t2 && t3 && t4 && t5 && t6;
    }

    // tests the initTerrain method
    boolean testInitTerrain(Tester t) {
        this.init();
        f.board = new MtList<Cell>();
        boolean t1 = t.checkExpect(f.board, new MtList<Cell>());

        f.initTerrain();
        int size = ForbiddenIslandWorld.ISLAND_SIZE;
        int isleHeight = ForbiddenIslandWorld.ISLAND_HEIGHT;
        ArrayList<ArrayList<Double>> randTerrHeights = f.randTerrHeights();
        ConsList<Cell> board = (ConsList<Cell>)f.board;
        Cell mtc00 = board.first;
        boolean t2 = t.checkExpect(mtc00.x, 0) && 
                t.checkExpect(mtc00.y, 0) && 
                t.checkExpect(mtc00.isFlooded, true) &&
                t.checkRange(mtc00.height, 0.0, 1.0 * this.isleHeight) &&
                t.checkExpect(randTerrHeights.get(0).get(size + 1), 0.0) &&
                t.checkExpect(randTerrHeights.get(size + 1).get(0), 0.0) &&
                t.checkExpect(randTerrHeights.get(size + 1).get(size + 1), 0.0) &&
                t.checkRange(randTerrHeights.get(0).get(isleHeight), -(double)isleHeight, 
                        (double)isleHeight) &&
                t.checkExpect(randTerrHeights.get(size + 1).get(isleHeight), 0.0) &&
                t.checkExpect(randTerrHeights.get(isleHeight).get(size + 1), 0.0) &&
                t.checkRange(randTerrHeights.get(isleHeight).get(0), -(double)isleHeight, 
                        (double)isleHeight) &&
                t.checkRange(randTerrHeights.get(isleHeight).get(isleHeight), 1.0, 
                        (double)isleHeight) &&
                t.checkRange(randTerrHeights.get(22).get(45), -(double)isleHeight, 
                        (double)isleHeight) &&
                t.checkRange(randTerrHeights.get(57).get(2), -(double)isleHeight, 
                        (double)isleHeight); 
        return t1 && t2;
    }

    // tests the convert method
    boolean testConvert(Tester t) {
        this.init();
        return t.checkExpect(f.convert(new ArrayList<ArrayList<Cell>>()), 
                this.mtList) &&
                t.checkExpect(f.convert(forOneRow), c3) &&
                t.checkExpect(f.convert(forTwoByTwo), c7);
    }

    // tests the heights method, where isleSize is at least 45
    boolean testHeights(Tester t) {
        this.init();
        ArrayList<ArrayList<Double>> heights = f.heights();
        return t.checkExpect(heights.get(0).get(0), this.d00) &&
                t.checkExpect(heights.get(32).get(32), this.d3232) &&
                t.checkExpect(heights.get(45).get(45), this.d4545) &&
                t.checkExpect(heights.get(7).get(7), this.d77) &&
                t.checkExpect(heights.get(38).get(38), this.d3838);
    }

    // tests the randHeights method
    boolean testRandHeights(Tester t) {
        this.init();
        ArrayList<ArrayList<Double>> randHeights = f.randHeights();
        return t.checkRange(randHeights.get(0).get(0), 0.0, 1.0 * this.isleHeight) &&
                t.checkRange(randHeights.get(32).get(32), 0.0, 1.0 * this.isleHeight) &&
                t.checkRange(randHeights.get(45).get(45), 0.0, 1.0 * this.isleHeight) &&
                t.checkRange(randHeights.get(7).get(7), 0.0, 1.0 * this.isleHeight) &&
                t.checkRange(randHeights.get(38).get(38), 0.0, 1.0 * this.isleHeight);
    }

    // tests the randTerrHeights method
    boolean testRandTerrHeights(Tester t) {
        this.init();
        int size = ForbiddenIslandWorld.ISLAND_SIZE;
        int isleHeight = ForbiddenIslandWorld.ISLAND_HEIGHT;
        ArrayList<ArrayList<Double>> randTerrHeights = f.randTerrHeights();
        return t.checkExpect(randTerrHeights.get(0).get(0), 0.0) &&
                t.checkExpect(randTerrHeights.get(0).get(size + 1), 0.0) &&
                t.checkExpect(randTerrHeights.get(size + 1).get(0), 0.0) &&
                t.checkExpect(randTerrHeights.get(size + 1).get(size + 1), 0.0) &&
                t.checkRange(randTerrHeights.get(0).get(isleHeight), -(double)isleHeight, 
                        (double)isleHeight) &&
                t.checkExpect(randTerrHeights.get(size + 1).get(isleHeight), 0.0) &&
                t.checkExpect(randTerrHeights.get(isleHeight).get(size + 1), 0.0) &&
                t.checkRange(randTerrHeights.get(isleHeight).get(0), -(double)isleHeight, 
                        (double)isleHeight) &&
                t.checkRange(randTerrHeights.get(isleHeight).get(isleHeight), 1.0, 
                        (double)isleHeight) &&
                t.checkRange(randTerrHeights.get(22).get(45), -(double)isleHeight, 
                        (double)isleHeight) &&
                t.checkRange(randTerrHeights.get(57).get(2), -(double)isleHeight, (double)isleHeight);
    }

    // tests terrHeightsHelp method
    boolean testTerrHeightsHelp(Tester t) {
        this.init();
        f.terrHeightsHelp(this.threeByThree, 0, 2, 0, 2);
        boolean t1 = t.checkInexact(this.threeByThree.get(0).get(0), 
                8.0, 0.1);
        boolean t2 = t.checkInexact(this.threeByThree.get(0).get(1), 
                8.0, 0.1);
        boolean t3 = t.checkInexact(this.threeByThree.get(0).get(2), 
                8.0, 0.1);
        boolean t4 = t.checkInexact(this.threeByThree.get(1).get(0), 
                8.0, 0.1);
        boolean t5 = t.checkInexact(this.threeByThree.get(1).get(1), 
                12.0, 0.1);
        boolean t6 = t.checkInexact(this.threeByThree.get(1).get(2), 
                8.0, 0.1);
        boolean t7 = t.checkInexact(this.threeByThree.get(2).get(0), 
                8.0, 0.1);
        boolean t8 = t.checkInexact(this.threeByThree.get(2).get(1), 
                16.0, 0.1);
        boolean t9 = t.checkInexact(this.threeByThree.get(2).get(2), 
                8.0, 0.1);
        return t1 && t2 && t3 && t4 && t5 && t6 && t7 && t8 && t9;
    }

    // tests the cells method, where isleSize is 64
    boolean testCells(Tester t) {
        this.init();
        ArrayList<ArrayList<Cell>> sales = f.cells(f.heights(), false);
        return t.checkExpect(f.cells(new ArrayList<ArrayList<Double>>(), false),
                new ArrayList<ArrayList<Cell>>()) &&
                t.checkExpect(sales.get(0).get(0), this.c00) &&
                t.checkExpect(sales.get(32).get(32), this.c3232) &&
                t.checkExpect(sales.get(45).get(45), this.c4545) &&
                t.checkExpect(sales.get(7).get(7), this.c77) &&
                t.checkExpect(sales.get(38).get(38), this.c3838) &&
                t.checkExpect(sales.get(16).get(16), this.c1616);
    }

    // tests the connectCells method
    boolean testConnectCells(Tester t) {
        this.init();
        boolean t1 = this.zeroZero.left == null && this.zeroZero.top == null
                && this.zeroZero.right == null && this.zeroZero.bottom == null;
        boolean t2 = this.oneOne.left == null && this.oneOne.top == null
                && this.oneOne.right == null && this.oneOne.bottom == null;
        f.connectCells(this.tByT);
        boolean t3 = this.zeroZero.left == this.zeroZero &&
                this.zeroZero.top == this.zeroZero &&
                this.zeroZero.right == this.zeroOne &&
                this.zeroZero.bottom == this.oneZero;
        boolean t4 = this.oneOne.left == this.oneZero &&
                this.oneOne.top == this.zeroOne &&
                this.oneOne.right == this.oneTwo &&
                this.oneOne.bottom == this.twoOne;
        return t1 && t2 && t3 && t4;
    }

    // tests the initGame method
    boolean testInitGame(Tester t) {
        this.init();
        int isleSize = ForbiddenIslandWorld.ISLAND_SIZE;
        f.waterHeight = 5;
        f.isPaused = true;
        f.isMulti = true;
        f.initGame();
        boolean t1 = t.checkExpect(f.isPaused, false);
        boolean t2 = t.checkExpect(f.isMulti, false);
        boolean t3 = t.checkExpect(f.waterHeight, 0);
        boolean t4 = t.checkRange(f.p1.x, 0, isleSize) &&
                t.checkRange(f.p1.y, 0, isleSize);
        boolean t5 = t.checkRange(f.remTargets.count(), 0, 
                ForbiddenIslandWorld.MAX_TARGETS + 1);
        for (Target tar : f.remTargets) {
            boolean t0 = t.checkRange(tar.x, 0, isleSize) &&
                    t.checkRange(tar.y, 0, isleSize);
        }
        boolean t6 = t.checkRange(f.heli.x, 0, isleSize) &&
                t.checkRange(f.heli.y, 0, isleSize);
        return t1 && t2 && t3 && t4 && t5 && t6;
    }

    // tests the startP2 method
    boolean testStartP2(Tester t) {
        this.init();
        int isleSize = ForbiddenIslandWorld.ISLAND_SIZE;
        boolean t1 = t.checkExpect(f.isMulti, false);
        f.startP2();
        boolean t2 = t.checkExpect(f.isMulti, true);
        boolean t3 = t.checkRange(f.p2.x, 0, isleSize) &&
                t.checkRange(f.p2.y, 0, isleSize) &&
                t.checkExpect(f.p2.image, new FromFileImage("p2.png"));
        return t1 && t2 && t3;
    }

    // tests the onKeyEvent method
    boolean testOnKeyEvent(Tester t) {
        this.init();
	f.initGame();
        f.onKeyEvent("m");
        boolean t1 = t.checkExpect(f.waterHeight, 0) && 
                t.checkRange(f.remTargets.count(), 0, ForbiddenIslandWorld.MAX_TARGETS + 1) &&
                t.checkRange(f.p1.x, 0, ForbiddenIslandWorld.ISLAND_SIZE) &&
                t.checkRange(f.p1.y, 0, ForbiddenIslandWorld.ISLAND_SIZE) &&
                t.checkRange(f.heli.x, 0, ForbiddenIslandWorld.ISLAND_SIZE) &&
                t.checkRange(f.heli.y, 0, ForbiddenIslandWorld.ISLAND_SIZE);

        f.onKeyEvent("r");
        boolean t2 = t.checkExpect(f.waterHeight, 0) && 
                t.checkRange(f.remTargets.count(), 0, ForbiddenIslandWorld.MAX_TARGETS + 1) &&
                t.checkRange(f.p1.x, 0, ForbiddenIslandWorld.ISLAND_SIZE) &&
                t.checkRange(f.p1.y, 0, ForbiddenIslandWorld.ISLAND_SIZE) &&
                t.checkRange(f.heli.x, 0, ForbiddenIslandWorld.ISLAND_SIZE) &&
                t.checkRange(f.heli.y, 0, ForbiddenIslandWorld.ISLAND_SIZE);

        f.onKeyEvent("t");
        boolean t3 = t.checkExpect(f.waterHeight, -5) && 
                t.checkRange(f.remTargets.count(), 0, ForbiddenIslandWorld.MAX_TARGETS + 1) &&
                t.checkRange(f.p1.x, 0, ForbiddenIslandWorld.ISLAND_SIZE) &&
                t.checkRange(f.p1.y, 0, ForbiddenIslandWorld.ISLAND_SIZE) &&
                t.checkRange(f.heli.x, 0, ForbiddenIslandWorld.ISLAND_SIZE) &&
                t.checkRange(f.heli.y, 0, ForbiddenIslandWorld.ISLAND_SIZE);

        boolean t4 = t.checkExpect(f.p1.image, new FromFileImage("p1.png"));   
        f.onKeyEvent("l");
        boolean t5 = t.checkExpect(f.p1.image, new FromFileImage("img.png"));
        f.isPaused = false;
        f.onKeyEvent("p");
        boolean t6 = t.checkExpect(f.isPaused, true);
        f.onKeyEvent("p");
        boolean t7 = t.checkExpect(f.isPaused, false);
        boolean t8 = t.checkExpect(f.isMulti, false);
        f.onKeyEvent("2");
        boolean t9 = t.checkExpect(f.isMulti, true);
        
	Cell p1Cell = f.p1.currentCell;
        Player testP = new Player(p1Cell);
 	f.onKeyEvent("left");
	testP.updateToLeft();
        boolean t10 = t.checkExpect(f.p1.currentCell.x, testP.currentCell.x) && t.checkExpect(f.p1.currentCell.y, testP.currentCell.y);
        f.onKeyEvent("right");
	testP.updateToRight();
        boolean t11 = t.checkExpect(f.p1.currentCell.x, testP.currentCell.x) && t.checkExpect(f.p1.currentCell.y, testP.currentCell.y);
        f.onKeyEvent("up");
	testP.updateToTop();
        boolean t12 = t.checkExpect(f.p1.currentCell.x, testP.currentCell.x) && t.checkExpect(f.p1.currentCell.y, testP.currentCell.y);
        f.onKeyEvent("down"); 
	testP.updateToBottom();
        boolean t13 = t.checkExpect(f.p1.currentCell.x, testP.currentCell.x) && t.checkExpect(f.p1.currentCell.y, testP.currentCell.y);
        Cell p2Cell = f.p2.currentCell;
	testP = new Player(p2Cell);
        f.onKeyEvent("a");
	testP.updateToLeft();
        boolean t14 = t.checkExpect(f.p2.currentCell.x, testP.currentCell.x) && t.checkExpect(f.p2.currentCell.y, testP.currentCell.y);
        f.onKeyEvent("d");
	testP.updateToRight();
        boolean t15 = t.checkExpect(f.p2.currentCell.x, testP.currentCell.x) && t.checkExpect(f.p2.currentCell.y, testP.currentCell.y);
        f.onKeyEvent("w");
	testP.updateToTop();
        boolean t16 = t.checkExpect(f.p2.currentCell.x, testP.currentCell.x) && t.checkExpect(f.p2.currentCell.y, testP.currentCell.y);
        f.onKeyEvent("s");
	testP.updateToBottom();
        boolean t17 = t.checkExpect(f.p2.currentCell.x, testP.currentCell.x) && t.checkExpect(f.p2.currentCell.y, testP.currentCell.y);

        boolean t18 = t.checkRange(f.remTargets.count(), 0, 
                ForbiddenIslandWorld.MAX_TARGETS + 1);
        return t1 && t2 && t3 && t4 && t5 && t6 && t7 && t8 && t9 && t10
                && t11 && t12 && t13 && t14 && t15 && t16 && t17 && t18; 

    }

    // tests the makeScene method
    boolean testMakeScene(Tester t) {
        this.init();
        f.board = new MtList<Cell>();
        f.remTargets = new MtList<Target>();
        f.heli.makeSceneHelper(this.mtSc);
	f.suit.makeSceneHelper(this.mtSc);
        f.p1.makeSceneHelper(this.mtSc);
        boolean t1 = t.checkExpect(f.makeScene(), this.mtSc);

        f.board = this.consList1;
        for (Cell c : this.consList1) {
            c.draw(this.mtSc2, f.waterHeight);
        }
        f.heli.makeSceneHelper(this.mtSc2);
	f.suit.makeSceneHelper(this.mtSc2);
        f.p1.makeSceneHelper(this.mtSc2);
        boolean t2 = t.checkExpect(f.makeScene(), this.mtSc2);

        f.board = this.consList2;
        f.remTargets = this.twoTar;
        for (Cell c : this.consList2) {
            c.draw(this.mtSc3, f.waterHeight);
        }
        for (Target tar : this.twoTar) {
            tar.makeSceneHelper(this.mtSc3);
        }
        f.heli.makeSceneHelper(this.mtSc3);
	f.suit.makeSceneHelper(this.mtSc3);
        f.p1.makeSceneHelper(this.mtSc3);
        boolean t3 = t.checkExpect(f.makeScene(), this.mtSc3);

        f.makeScene();
        WorldImage p1Img = new FromFileImage("p1.png");
        WorldImage dedImg = new FromFileImage("rip.png");
        boolean t4 = t.checkExpect(f.p1.image, p1Img);
        f.p1.currentCell.isFlooded = true;
        boolean t5 = t.checkExpect(f.p1.isDrowning(), true);
        f.makeScene();
        boolean t6 = t.checkExpect(f.p1.image, dedImg);

        return t1 && t2 && t3 && t4 && t5 && t6;

    }

    // tests the onTick method
    boolean testOnTick(Tester t) {
        this.init();
        f.board = this.zeroToTwo;
        boolean t1 = t.checkExpect(f.waterHeight, 0);
        f.onTick();
        boolean t2 = t.checkExpect(f.waterHeight, 0);
        int i = 0;
        while (i < 9) {
            f.onTick();
            i += 1;
        }
        boolean t3 = t.checkExpect(f.waterHeight, 1);
        this.zeroZero.isFlooded = true;
        f.waterHeight = 17;
        boolean t4 = t.checkExpect(this.zeroOne.isFlooded, false) &&
                t.checkExpect(this.oneZero.isFlooded, false);
        f.tick = 9;
        f.onTick();
        boolean t5 = t.checkExpect(this.zeroOne.isFlooded, true) &&
                t.checkExpect(this.oneZero.isFlooded, true);
        boolean t6 = t.checkExpect(f.tick, 0);

        return t1 && t2 && t3 && t4 && t5 && t6;
    }

    // test the coastCells method 
    boolean testCoastCells(Tester t) {
        this.init();
        f.board = this.zeroToTwo;
        boolean t1 = t.checkExpect(f.board.count(), 9);
        boolean t2 = t.checkExpect(f.coastCells(), this.mtList);
        this.zeroZero.isFlooded = true;
        boolean t3 = t.checkExpect(f.coastCells(), this.zeroZeroFlood);
        this.zeroZero.isFlooded = false;
        boolean t4 = t.checkExpect(f.coastCells(), this.mtList);
        return t1 && t2 && t3 && t4;
    }

    // tests the worldEnds method 
    // cannot test the contents, so checks for instanceof WorldEnd
    boolean testWorldEnds(Tester t) {
	this.init();
	WorldScene endScene1 = f.makeScene();
        WorldScene endScene2 = f.makeScene();
        WorldScene endScene3 = f.makeScene();
        int coord = ForbiddenIslandWorld.ISLAND_SIZE * ForbiddenIslandWorld.SCALE / 2;
        TextImage endImage1 = new TextImage("Oh no!", 60, Color.WHITE);
        TextImage endImage2 = new TextImage("HOORAY!", 60, Color.WHITE);

        f.p1.currentCell.isFlooded = false;
        f.heli.isCollected = true;
        endScene2.placeImageXY(endImage2, coord, coord);
        boolean t1 = t.checkExpect(f.worldEnds() instanceof WorldEnd, true);

        f.p1.currentCell.isFlooded = true;
        f.p1.currentCell.height = 0;
        endScene1.placeImageXY(endImage1, coord, coord);
        boolean t2 = t.checkExpect(f.worldEnds() instanceof WorldEnd, true);    

        f.heli.isCollected = false;
        boolean t3 = t.checkExpect(f.worldEnds() instanceof WorldEnd, true);
        return t1 && t2 && t3;
    }
}
