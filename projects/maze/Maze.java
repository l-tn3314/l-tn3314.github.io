import java.util.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

// represents the maze
class Maze extends World {
    HashMap<Cell, Cell> representatives; // representatives of all the cells
    ArrayList<ArrayList<Cell>> cells; // all the cells in the maze

    ArrayList<Edge> edgesInTree; // edges in the minimum spanning tree
    Cell start; // player starting cell (0, 0)
    Cell tar; // player target cell (height - 1, width - 1)
    ArrayList<Edge> worklist; // all edges in graph, sorted by edge weights
    Queue<Edge> worklist2;
    ICollection<Cell> searchWorklist; // worklist for search
    HashMap<Cell, Edge> cameFromEdge; // records which edge was used to get to 
    // a not-yet-visited node
    ArrayList<Cell> visited; // visited cells from searching

    boolean drawStart; // animate knocking down the walls?
    boolean isBfs; // breadth-first searching?
    boolean isDfs; // depth-first searching?
    boolean showPath; // show the path(s) of the player(s)?
    boolean isMulti; // is the game currently multiplayer?

    static final int MAZE_WIDTH = 50; // size of width 
    static final int MAZE_HEIGHT = 30; // size of height
    static final int SCALE = 20; // scale of cell

    Movable ai; // the ai
    Player p1; // player 1
    Player p2; // player 2

    int hBias; // amount of horizontal bias
    int vBias; // amount of vertical bias

    // constructor
    Maze() {
        this.initDraw();
    }

    // EFFECT: initializes the maze before knocking down walls animation
    void initDraw() {
        Utils u = new Utils();
        this.cells = this.initCells(Maze.MAZE_HEIGHT, Maze.MAZE_WIDTH);
        this.worklist = this.makeEdges(this.cells);
        Collections.sort(this.worklist, new EdgeComparator());
        this.worklist2 = u.toQueue(this.worklist);
        this.representatives = this.hashStart(this.cells);
        this.edgesInTree = new ArrayList<Edge>();

        this.drawStart = true;
        this.isBfs = false;
        this.isDfs = false;
        this.showPath = false;
        this.isMulti = false;

        this.start = this.cells.get(0).get(0);
        this.start.updateColor(Color.GREEN);
        ArrayList<Cell> row = this.cells.get(cells.size() - 1);
        this.tar = row.get(row.size() - 1);
        row.get(row.size() - 1).updateColor(new Color(143, 26, 210));
        this.p1 = new Player(this.start);
        Random r = new Random();
        ArrayList<Cell> arr = this.cells.get(r.nextInt(this.cells.size()));
        this.ai = new Movable(arr.get(r.nextInt(arr.size())));
    }

    // initializes cell color
    void initCellColor() {
        for (ArrayList<Cell> arr : this.cells) {
            for (Cell c : arr) {
                c.updateColor(Color.GRAY);
            }
        }
        this.start.updateColor(Color.GREEN);
        this.tar.updateColor(new Color(143, 26, 210)); // purple
    }

    // returns an ArrayList<ArrayList<Cell>> that represents each Cell in the game
    ArrayList<ArrayList<Cell>> initCells(int height, int width) {
        ArrayList<ArrayList<Cell>> yCells = new ArrayList<ArrayList<Cell>>();
        Utils u = new Utils();
        for (int i = 0; i < height; i += 1) {
            ArrayList<Cell> xCells = new ArrayList<Cell>();
            for (int j = 0; j < width; j += 1) {
                Cell c = new Cell(i, j);
                u.cellf(c);
                xCells.add(c);
            }
            yCells.add(xCells);
        }
        return yCells;
    }

    // takes in an ArrayList<ArrayList<Cell>> and returns an ArrayList of its Edges
    ArrayList<Edge> makeEdges(ArrayList<ArrayList<Cell>> cells) {
        ArrayList<Edge> edgy = new ArrayList<Edge>();
        Random r = new Random();
        for (int i = 0; i < cells.size(); i += 1) {
            for (int j = 0; j < cells.get(i).size(); j += 1) {
                int weight = 5 + r.nextInt(Maze.MAZE_WIDTH * Maze.MAZE_HEIGHT);
                if (i < cells.size() - 1) {
                    edgy.add(new Edge(cells.get(i).get(j), 
                            cells.get(i + 1).get(j), 
                            weight + this.hBias));
                }
                if (j < cells.get(i).size() - 1) {
                    edgy.add(new Edge(cells.get(i).get(j), 
                            cells.get(i).get(j + 1), 
                            weight + this.vBias));
                }
            }
        }
        return edgy;
    }

    // returns hashmap of cells with representatives as itself
    HashMap<Cell, Cell> hashStart(ArrayList<ArrayList<Cell>> cells) {
        HashMap<Cell, Cell> hash = new HashMap<Cell, Cell>();
        for (ArrayList<Cell> arr : cells) {
            for (Cell c : arr) {
                hash.put(c, c);
            }
        }
        return hash;
    }

    // finds the representative of the cell in the given hashmap
    Cell find(HashMap<Cell, Cell> reps, Cell c) {
        Cell temp = c;
        while (!reps.get(temp).equals(temp)) {
            temp = reps.get(temp);
        }
        return temp;
    }

    // EFFECT: unions representatives
    void union(HashMap<Cell, Cell> reps, Cell c1, Cell c2) {
        reps.put(c1, c2);
    }

    // returns ArrayList<Edge> of the minimum spanning tree
    // EFFECT: updates reps
    ArrayList<Edge> krus(ArrayList<Edge> worklist, HashMap<Cell, Cell> reps) {
        ArrayList<Edge> ans = new ArrayList<Edge>();
        int curInd = 0;
        while (curInd < worklist.size()) {
            Edge e = worklist.get(curInd);
            curInd += 1;
            Cell oneRep = this.find(reps, e.one);
            Cell twoRep = this.find(reps, e.two);
            if (oneRep.equals(twoRep)) {
                // nothing happens
            }
            else {
                ans.add(e);
                union(reps, oneRep, twoRep);
            }

        }
        return ans;    
    }

    // creates an ArrayList<Edge> of a maze from a full grid of edges
    ArrayList<Edge> krusDraw(Queue<Edge> worklist3, HashMap<Cell, Cell> reps, ArrayList<Edge> ans) {
        if (worklist3.isEmpty()) {
            this.drawStart = false;
            this.connect(ans);
        }
        else {
            Edge e = worklist3.remove();
            Cell oneRep = this.find(reps, e.one);
            Cell twoRep = this.find(reps, e.two);
            if (oneRep.equals(twoRep)) {
                // nothing happens
            }
            else {
                ans.add(e);
                union(reps, oneRep, twoRep);
            }
        }
        return ans;
    }

    // EFFECT: connects cells in the minimum spanning tree
    void connect(ArrayList<Edge> arr) {
        for (Edge e : arr) {
            // e.one and e.two are cells
            e.one.connectCell(e.two);
        }
    }

    // returns path from the source to the given target node
    ArrayList<Cell> reconstruct(HashMap<Cell, Edge> edges, Cell tar, boolean update) {
        ArrayList<Cell> ans = new ArrayList<Cell>();
        Cell cur = tar;
        Edge e = edges.get(cur);
        while (!e.otherCell(cur).equals(cur)) {
            if (update) {
                cur.updateColor(new Color(55, 112, 216)); // dark blue
            }
            ans.add(cur);
            cur = e.otherCell(cur);
            e = edges.get(cur);
        }
        if (update) {
            cur.updateColor(new Color(55, 112, 216)); // dark blue
        }
        return ans;
    }

    // returns path for the AI to travel
    ArrayList<Cell> aiSearch(ArrayList<ArrayList<Cell>> cells, ArrayList<Edge> tree, Cell tar) {
        ICollection<Cell> worklist = new Stack<Cell>();   
        HashMap<Cell, Edge> cameFromEdge = new HashMap<Cell, Edge>();
        ArrayList<Cell> visited = new ArrayList<Cell>();

        //Cell tar = this.p1.cur;
        Cell start = this.ai.cur;
        cameFromEdge.put(start, new Edge(start, start, 0));  // to help with recursion

        worklist.add(start);

        while (!worklist.isEmpty()) {
            Cell next = worklist.remove();
            if (visited.contains(next)) {
                // does nothing
            }
            else if (next.equals(tar)) {
                return reconstruct(cameFromEdge, next, false);
            }
            else {
                for (Edge e : tree) {
                    if (e.isConnected(next) && !visited.contains(e.otherCell(next))) {
                        worklist.add(e.otherCell(next));
                        cameFromEdge.put(e.otherCell(next), e);
                    }
                }
                visited.add(next);
            }
        }
        return new ArrayList<Cell>();
    }


    // returns ArrayList<Cell> of the path from the beginning to the target cell
    // cells is non-empty
    ArrayList<Cell> search(ArrayList<ArrayList<Cell>> cells, ArrayList<Edge> tree) {
        Color lightBlue = new Color(130, 164, 228);

        Cell next = this.searchWorklist.remove();
        if (this.visited.contains(next)) {
            // does nothing
        }
        else if (next.equals(tar)) {
            this.isBfs = false;
            this.isDfs = false;
            return reconstruct(this.cameFromEdge, next, true);
        }
        else {
            for (Edge e : tree) {
                if (e.isConnected(next) && !this.visited.contains(e.otherCell(next))) {
                    this.searchWorklist.add(e.otherCell(next));
                    this.cameFromEdge.put(e.otherCell(next), e);
                }
            }
            this.visited.add(next);
            next.updateColor(lightBlue);
        }

        //throw new RuntimeException("Invalid maze!");
        return new ArrayList<Cell>();
    }

    // breadth-first search
    // EFFECT: changes cells' colors
    void bfs() {
        this.initCellColor();
        this.isBfs = true;
        this.isDfs = false;
        this.searchWorklist.add(this.start);
        // to help with recursion        
        this.cameFromEdge.put(this.start, new Edge(this.start, this.start, 0));  
        this.search(this.cells, this.edgesInTree);
    }

    // depth-first search
    // EFFECT: changes cells' colors
    void dfs() {
        this.initCellColor();
        this.isBfs = false;
        this.isDfs = true;
        this.searchWorklist.add(this.start);
        // to help with recursion
        this.cameFromEdge.put(this.start, new Edge(this.start, this.start, 0));  
        this.search(this.cells, this.edgesInTree);
    }

    // EFFECT: updates based on keyEvent
    public void onKeyEvent(String ke) {
        if (ke.equals("r")) {
            this.vBias = 0;
            this.hBias = 0;
            this.initDraw();
        }
        if (ke.equals("v")) {
            this.vBias = Maze.MAZE_HEIGHT * Maze.MAZE_WIDTH / 2;
            this.hBias = 0;
            this.initDraw();
        }
        if (ke.equals("h")) {
            this.vBias = 0;
            this.hBias = Maze.MAZE_HEIGHT * Maze.MAZE_WIDTH / 2;
            this.initDraw();
        }
        if (!drawStart) {
            if (ke.equals("b")) {
                this.searchWorklist = new Queue<Cell>();
                this.cameFromEdge = new HashMap<Cell, Edge>();
                this.visited = new ArrayList<Cell>();
                this.bfs();
            }
            if (ke.equals("n")) {
                this.searchWorklist = new Stack<Cell>();
                this.cameFromEdge = new HashMap<Cell, Edge>();
                this.visited = new ArrayList<Cell>();
                this.dfs();
            }
        }
        if (ke.equals("p")) {
            this.showPath = !this.showPath;
        }
        if (ke.equals("q")) {
            this.representatives = this.hashStart(this.cells);
            this.drawStart = false;
            this.edgesInTree = this.krus(this.worklist, this.representatives);
            this.connect(this.edgesInTree);
        }
        if (ke.equals("2")) {
            this.initDraw();
            this.isMulti = true;
            this.p1 = new Player(this.tar, new FromFileImage("p1.png"));
            this.p2 = new Player(this.start, new FromFileImage("p2.png"));
        }
        // p1.cur is a Cell
        if (ke.equals("left")) {
            this.p1.updateCell(p1.cur.left);
        }
        if (ke.equals("up")) {
            this.p1.updateCell(p1.cur.top);
        }
        if (ke.equals("right")) {
            this.p1.updateCell(p1.cur.right);
        }
        if (ke.equals("down")) {
            this.p1.updateCell(p1.cur.bottom);
        }
        // p2.cur is a Cell
        if (isMulti) {
            if (ke.equals("a")) {
                this.p2.updateCell(p2.cur.left);
            }
            if (ke.equals("w")) {
                this.p2.updateCell(p2.cur.top);
            }
            if (ke.equals("d")) {
                this.p2.updateCell(p2.cur.right);
            }
            if (ke.equals("s")) {
                this.p2.updateCell(p2.cur.bottom);
            }
        }
    }   

    // returns the scene for the game
    public WorldScene makeScene() {
        WorldScene acc = this.getEmptyScene();
        int centX = (Maze.MAZE_WIDTH * Maze.SCALE) / 2;
        int centY = (Maze.MAZE_HEIGHT * Maze.SCALE) / 2;
        for (ArrayList<Cell> arr : cells) {
            for (Cell c : arr) {
                c.draw(acc);
            }
        }
        if (this.showPath) {
            for (Cell c : this.p1.visitedCells) {
                c.draw(acc, Color.RED);
            }
            if (isMulti) {
                for (Cell c : this.p2.visitedCells) {
                    c.draw(acc, Color.ORANGE);
                }
            }
        }
        for (Edge e : worklist) {
            if (!edgesInTree.contains(e)) {
                e.draw(acc);        
            }
        }
        this.p1.draw(acc);
        this.ai.draw(acc);
        if (isMulti) {
            this.p2.draw(acc);
        }
        if (this.drawStart) {
            TextImage knock = new TextImage("Knocking down walls...", 40,
                    Color.WHITE);
            acc.placeImageXY(knock, centX, centY);
        }
        if (this.ai.cur.equals(this.p1.cur) || (isMulti && this.ai.cur.equals(this.p2.cur))) {
            TextImage end = new TextImage("Oh no!", 60, Color.WHITE);
            Utils u = new Utils();
            acc.placeImageXY(end, centX, centY);
            u.cellf(this.ai.cur);
            u.cellf(this.p1.cur);
            if (isMulti) {
                u.cellf(this.p2.cur);
            }
        }
        if (isMulti) {
            if (this.p1.cur.equals(this.start) && this.p2.cur.equals(this.tar)) {
                TextImage win = new TextImage("AMAZING!", 60, Color.WHITE);
                acc.placeImageXY(win, centX, centY);
            }
        }
        else {
            if (this.p1.cur.equals(this.tar)) {
                TextImage win = new TextImage("YOU WON!", 60, Color.WHITE);
                acc.placeImageXY(win, centX, centY);
            }
        }
        return acc;
    }

    int tick;

    // updates the world on each tick
    public void onTick() {
        if (tick == 49) {
            Random r = new Random();
            ArrayList<Cell> sail;
            if (r.nextInt(3) == 0) {
                sail = this.aiSearch(this.cells, this.edgesInTree, this.p1.cur);
            }
            else {
                ArrayList<Cell> arr = this.cells.get(r.nextInt(this.cells.size()));
                Cell c = arr.get(r.nextInt(arr.size()));
                sail = this.aiSearch(this.cells, this.edgesInTree, c);
            }
            if (!sail.isEmpty()) {
                this.ai.updateCell(sail.get(sail.size() - 1));
            }
        }
        if (this.drawStart) {
            this.edgesInTree = this.krusDraw(this.worklist2, this.representatives, 
                    this.edgesInTree);
        }
        if (this.isBfs || this.isDfs) {
            this.search(this.cells, this.edgesInTree);
        }    
        tick = (tick + 1) % 50;
    }
}

