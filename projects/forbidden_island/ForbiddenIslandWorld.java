import java.util.ArrayList;
import java.util.Random;
import java.awt.Color;
import javalib.impworld.*;
import javalib.worldimages.*;

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
