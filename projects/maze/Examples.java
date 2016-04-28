import java.util.*;
import java.awt.Color;
import javalib.impworld.*;
import javalib.worldimages.*;
import tester.*;

// examples and tests for the maze
class ExamplesMaze {

    public static void main(String[] args) {
        ExamplesMaze ex = new ExamplesMaze();
        Tester.runReport(ex, false, false);

        Maze m = new Maze();
        m.bigBang(Maze.MAZE_WIDTH * Maze.SCALE, Maze.MAZE_HEIGHT * Maze.SCALE, 0.00001);
    }

    Maze m;

    Cell zeroZero;
    Cell zeroOne;
    Cell zeroTwo;
    Cell oneZero;
    Cell oneOne; 
    Cell oneTwo; 
    Cell twoZero;
    Cell twoOne;
    Cell twoTwo; 

    ArrayList<Cell> row0;
    ArrayList<Cell> row1;
    ArrayList<Cell> row2;

    ArrayList<ArrayList<Cell>> tByT;
    ArrayList<ArrayList<Cell>> twoByThree;

    Edge c00c01;
    Edge c01c02;
    Edge c10c11;
    Edge c11c12;
    Edge c20c21;
    Edge c21c22;
    Edge c00c10;
    Edge c10c20;
    Edge c01c11;
    Edge c11c21;
    Edge c02c12;
    Edge c12c22;

    ArrayList<Edge> edgy;

    Deque<Cell> d1;
    Deque<Cell> d2;
    Deque<Cell> d3;

    Node<Cell> n1;
    Node<Cell> n2;
    Node<Cell> n3;
    Node<Cell> n4;

    Node<Cell> n5;
    Node<Cell> n6;
    Node<Cell> n7;

    Sentinel<Cell> s1;
    Sentinel<Cell> s2;

    Stack<Cell> stack1;
    Stack<Cell> stack2;
    Queue<Cell> queue1;

    HashMap<Cell, Cell> hashbrown;
    HashMap<Cell, Cell> hashtag;

    HashMap<Cell, Edge> hashCE;

    WorldScene mtSc;
    WorldScene mtSc2;

    Utils u;

    Player p1;
    Player p2;
    Movable mov;

    // initializes data
    void init() {
        this.m = new Maze();

        this.zeroZero = new Cell(0, 0);
        this.zeroOne = new Cell(0, 1);
        this.zeroTwo = new Cell(0, 2);
        this.oneZero = new Cell(1, 0);
        this.oneOne = new Cell(1, 1);
        this.oneTwo = new Cell(1, 2);
        this.twoZero = new Cell(2, 0);
        this.twoOne = new Cell(2, 1);
        this.twoTwo = new Cell(2, 2);

        this.row0 = new ArrayList<Cell>();
        this.row1 = new ArrayList<Cell>();
        this.row2 = new ArrayList<Cell>();

        this.tByT = new ArrayList<ArrayList<Cell>>();
        this.twoByThree = new ArrayList<ArrayList<Cell>>();

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

        this.twoByThree.add(this.row0);
        this.twoByThree.add(this.row1);

        this.edgy = new ArrayList<Edge>();

        this.c00c01 = new Edge(this.zeroZero, this.zeroOne, 1);
        this.c01c02 = new Edge(this.zeroOne, this.zeroTwo, 2);
        this.c10c11 = new Edge(this.oneZero, this.oneOne, 3);
        this.c11c12 = new Edge(this.oneOne, this.oneTwo, 4);
        this.c20c21 = new Edge(this.twoZero, this.twoOne, 5);
        this.c21c22 = new Edge(this.twoOne, this.twoTwo, 6);
        this.c00c10 = new Edge(this.zeroZero, this.oneZero, 7);
        this.c10c20 = new Edge(this.oneZero, this.twoZero, 8);
        this.c01c11 = new Edge(this.zeroOne, this.oneOne, 9);
        this.c11c21 = new Edge(this.oneOne, this.twoOne, 10);
        this.c02c12 = new Edge(this.zeroTwo, this.oneTwo, 11);
        this.c12c22 = new Edge(this.oneTwo, this.twoTwo, 12);

        this.edgy.add(this.c00c10);
        this.edgy.add(this.c00c01);
        this.edgy.add(this.c01c11);
        this.edgy.add(this.c01c02);
        this.edgy.add(this.c02c12);
        this.edgy.add(this.c10c20);
        this.edgy.add(this.c10c11);
        this.edgy.add(this.c11c21);
        this.edgy.add(this.c11c12);
        this.edgy.add(this.c12c22);
        this.edgy.add(this.c20c21);
        this.edgy.add(this.c21c22);

        this.d1 = new Deque<Cell>();
        this.d2 = new Deque<Cell>();
        this.d3 = new Deque<Cell>();

        this.s1 = new Sentinel<Cell>();
        this.s2 = new Sentinel<Cell>();

        this.n1 = new Node<Cell>(this.zeroZero, d2.header, d2.header);
        this.n2 = new Node<Cell>(this.zeroOne, d2.header, n1);
        this.n3 = new Node<Cell>(this.zeroTwo, d2.header, n2);
        this.n4 = new Node<Cell>(this.oneZero, d2.header, n3);

        this.n5 = new Node<Cell>(this.twoTwo, d3.header, d3.header);
        this.n6 = new Node<Cell>(this.twoOne, d3.header, n5);
        this.n7 = new Node<Cell>(this.twoZero, d3.header, n6);

        this.stack1 = new Stack<Cell>();
        this.stack2 = new Stack<Cell>();
        this.queue1 = new Queue<Cell>();

        this.hashbrown = m.hashStart(this.tByT);
        this.hashtag = new HashMap<Cell, Cell>();

        this.hashCE = new HashMap<Cell, Edge>();
        this.hashCE.put(this.zeroZero, new Edge(this.zeroZero, this.zeroZero, 0));
        this.hashCE.put(this.zeroOne, this.c00c01);
        this.hashCE.put(this.zeroTwo, this.c01c02);
        this.hashCE.put(this.oneZero, this.c00c10);
        this.hashCE.put(this.oneOne, this.c10c11);
        this.hashCE.put(this.oneTwo, this.c02c12);
        this.hashCE.put(this.twoZero, this.c10c20);
        this.hashCE.put(this.twoOne, this.c11c21);
        this.hashCE.put(this.twoTwo, this.c21c22);


        this.mtSc = m.getEmptyScene();
        this.mtSc2 = m.getEmptyScene();

        this.u = new Utils();

        this.p1 = new Player(this.zeroZero);
        this.p2 = new Player(this.zeroZero);
        this.mov = new Movable(this.oneTwo);

        for (ArrayList<Cell> arr : this.tByT) {
            for (Cell c : arr) {
                c.updateLeft(c);
                c.updateTop(c);
                c.updateRight(c);
                c.updateBottom(c);
            }
        }
    }

    /******************************** UTILS *******************************/
    // tests the toQueue method
    boolean testToQueue(Tester t) {
        this.init();
        ArrayList<Edge> arr = new ArrayList<Edge>();
        boolean t1 = (u.toQueue(arr) instanceof Queue<?>);

        ArrayList<Edge> arr2 = new ArrayList<Edge>();
        arr2.add(this.c00c01);
        arr2.add(this.c00c10);
        arr2.add(this.c01c11);
        arr2.add(this.c11c21);
        Queue<Edge> arr2q = u.toQueue(arr2);
        boolean t2 = (arr2q instanceof Queue<?>);
        for (Edge e : arr2) {
            boolean t0 = t.checkExpect(e, arr2q.remove());
        }
        boolean t3 = t.checkExpect(arr2.remove(0), this.c00c01);

        return t1 && t2 && t3;
    }

    // tests the cellf method
    boolean testCellf(Tester t) {
        this.init();
        boolean t1 = t.checkExpect(this.zeroZero.left, this.zeroZero);
        boolean t2 = t.checkExpect(this.zeroZero.right, this.zeroZero);
        boolean t3 = t.checkExpect(this.zeroZero.top, this.zeroZero);
        boolean t4 = t.checkExpect(this.zeroZero.bottom, this.zeroZero);
        u.cellf(this.zeroZero);
        boolean t5 = t.checkExpect(this.zeroZero.left, this.zeroZero);
        boolean t6 = t.checkExpect(this.zeroZero.right, this.zeroZero);
        boolean t7 = t.checkExpect(this.zeroZero.top, this.zeroZero);
        boolean t8 = t.checkExpect(this.zeroZero.bottom, this.zeroZero);

        this.oneOne.updateLeft(this.oneZero);
        this.oneOne.updateRight(this.oneTwo);
        this.oneOne.updateTop(this.zeroOne);
        this.oneOne.updateBottom(this.twoOne);
        u.cellf(this.oneOne);
        boolean t9 = t.checkExpect(this.oneOne.left, this.oneOne);
        boolean t10 = t.checkExpect(this.oneOne.right, this.oneOne);
        boolean t11 = t.checkExpect(this.oneOne.top, this.oneOne);
        boolean t12 = t.checkExpect(this.oneOne.bottom, this.oneOne);

        return t1 && t2 && t3 && t4 && t5 && t6 && t7 && t8 && t9 && t10 && t11
                && t12;
    }


    /******************************** DEQUE *******************************/
    // tests the size method
    boolean testSize(Tester t) {
        this.init();
        return t.checkExpect(d1.size(), 0) &&
                t.checkExpect(d2.size(), 4) &&
                t.checkExpect(d3.size(), 3);
    }

    // tests isEmpty
    boolean testIsDequeEmpty(Tester t) {
        this.init();
        return t.checkExpect(new Deque<Cell>().isEmpty(), true) &&
                t.checkExpect(d1.isEmpty(), true) &&
                t.checkExpect(d2.isEmpty(), false) &&
                t.checkExpect(d3.isEmpty(), false);
    }

    // tests addAtHead
    boolean testAddAtHead(Tester t) {
        this.init();
        boolean t1 = t.checkExpect(this.d1.header, this.s1);

        ANode<Cell> s2 = new Sentinel<Cell>();
        ANode<Cell> sell = new Node<Cell>(new Cell(1, 2), s2, s2);
        this.d1.addAtHead(new Cell(1, 2));
        boolean t2 = t.checkExpect(this.d1.header, s2);

        ANode<Cell> sail = new Node<Cell>(new Cell(2, 3), sell, s2);
        this.d1.addAtHead(new Cell(2, 3));
        boolean t3 = t.checkExpect(this.d1.header, s2);

        Deque<Cell> jail = new Deque<Cell>();
        jail.addAtHead(this.oneZero);
        jail.addAtHead(this.zeroTwo);
        jail.addAtHead(this.zeroOne);
        jail.addAtHead(this.zeroZero);
        boolean t4 = t.checkExpect(this.d2, jail);

        return t1 && t2 && t3 && t4;
    } 

    // tests addAtTail
    boolean testAddAtTail(Tester t) {
        this.init();
        boolean t1 = t.checkExpect(this.d1.header, this.s1);

        ANode<Cell> s2 = new Sentinel<Cell>();
        ANode<Cell> sell = new Node<Cell>(new Cell(1, 2), s2, s2);
        this.d1.addAtTail(new Cell(1, 2));
        boolean t2 = t.checkExpect(this.d1.header, s2);

        ANode<Cell> sail = new Node<Cell>(new Cell(1, 2), sell, s2);
        this.d1.addAtTail(new Cell(1, 2));
        boolean t3 = t.checkExpect(this.d1.header, s2);

        Deque<Cell> jail = new Deque<Cell>();
        jail.addAtTail(this.twoTwo);
        jail.addAtTail(this.twoOne);
        jail.addAtTail(this.twoZero);
        boolean t4 = t.checkExpect(this.d3, jail);

        return t1 && t2 && t3 && t4;
    } 

    // tests removeFromHead
    boolean testRemoveFromHead(Tester t) {
        this.init();
        boolean t1 = t.checkExpect(this.d1.header, this.s1);

        Exception al = new RuntimeException("Can't remove from empty Deque!");
        boolean t2 = t.checkException(al, d1, "removeFromHead");
        boolean t3 = t.checkExpect(this.d1.header, this.s1);

        Sentinel<Cell> s2 = new Sentinel<Cell>();
        ANode<Cell> sell = new Node<Cell>(new Cell(1, 2), s2, s2);
        ANode<Cell> sail = new Node<Cell>(new Cell(3, 4), sell, s2);
        Deque<Cell> dek = new Deque<Cell>(s2);
        boolean t4 = t.checkExpect(dek.removeFromHead(), new Cell(3, 4));

        ANode<Cell> s3 = new Sentinel<Cell>();
        ANode<Cell> snail = new Node<Cell>(new Cell(1, 2), s3, s3);
        boolean t5 = t.checkExpect(dek.header, s3);
        s3 = new Sentinel<Cell>();
        boolean t6 = t.checkExpect(dek.removeFromHead(), new Cell(1, 2));
        boolean t7 = t.checkExpect(dek.header, s3);

        boolean t8 = t.checkExpect(d2.removeFromHead(), this.zeroZero);
        boolean t9 = t.checkExpect(d2.removeFromHead(), this.zeroOne);
        boolean t10 = t.checkExpect(d2.removeFromHead(), this.zeroTwo);
        boolean t11 = t.checkExpect(d2.removeFromHead(), this.oneZero);
        boolean t12 = t.checkException(al, d2, "removeFromHead");

        return t1 && t2 && t3 && t4 && t5 && t6 && t7 && t8 && t9 
                && t10 && t11 && t12;
    }

    // tests removeFromTail
    boolean testRemoveFromTail(Tester t) {
        this.init();
        boolean t1 = t.checkExpect(this.d1.header, this.s1);

        Exception al = new RuntimeException("Can't remove from empty Deque!");
        boolean t2 = t.checkException(al, d1, "removeFromTail");
        boolean t3 = t.checkExpect(this.d1.header, this.s1);

        Sentinel<Cell> s2 = new Sentinel<Cell>();
        ANode<Cell> sell = new Node<Cell>(new Cell(1, 2), s2, s2);
        ANode<Cell> sail = new Node<Cell>(new Cell(3, 4), sell, s2);
        Deque<Cell> dek = new Deque<Cell>(s2);
        boolean t4 = t.checkExpect(dek.removeFromTail(), new Cell(1, 2));

        ANode<Cell> s3 = new Sentinel<Cell>();
        ANode<Cell> snail = new Node<Cell>(new Cell(3, 4), s3, s3);
        boolean t5 = t.checkExpect(dek.header, s3);
        s3 = new Sentinel<Cell>();
        boolean t6 = t.checkExpect(dek.removeFromHead(), new Cell(3, 4));
        boolean t7 = t.checkExpect(dek.header, s3);

        boolean t8 = t.checkExpect(d3.removeFromTail(), this.twoZero);
        boolean t9 = t.checkExpect(d3.removeFromTail(), this.twoOne);
        boolean t10 = t.checkExpect(d3.removeFromTail(), this.twoTwo);
        boolean t11 = t.checkException(al, d3, "removeFromTail");

        return t1 && t2 && t3 && t4 && t5 && t6 && t7 && t8 && t9 
                && t10 && t11;
    }

    // tests updateNext
    boolean testUpdateNext(Tester t) {
        this.init();
        boolean t1 = t.checkExpect(this.s1.next, this.s1);
        this.s1.updateNext(this.n1);
        boolean t2 = t.checkExpect(this.s1.next, this.n1);
        this.n1.updateNext(this.s1);
        boolean t3 = t.checkExpect(this.n1.next, this.s1);
        this.n1.updateNext(this.n2);
        boolean t4 = t.checkExpect(this.n1.next, this.n2);
        this.n4.updateNext(this.n3);
        boolean t5 = t.checkExpect(this.n4.next, this.n3);
        return t1 && t2 && t3 && t4 && t5;
    }

    // tests updatePrev
    boolean testUpdatePrev(Tester t) {
        this.init();
        boolean t1 = t.checkExpect(this.s1.prev, this.s1);
        this.s1.updatePrev(this.n1);
        boolean t2 = t.checkExpect(this.s1.prev, this.n1);
        this.n1.updatePrev(this.n2);
        boolean t3 = t.checkExpect(this.n1.prev, this.n2);
        this.n3.updatePrev(this.n4);
        boolean t4 = t.checkExpect(this.n3.prev, this.n4);
        this.n5.updatePrev(this.s1);
        boolean t5 = t.checkExpect(this.n5.prev, this.s1);
        return t1 && t2 && t3 && t4 && t5;
    }

    // tests startCount
    boolean testStartCount(Tester t) {
        this.init();
        boolean t1 = t.checkExpect(this.s1.startCount(), 0);
        this.s1.updateNext(this.n1);
        boolean t2 = t.checkExpect(this.s1.startCount(), 4);
        this.s1.updateNext(this.n5);
        boolean t3 = t.checkExpect(this.s1.startCount(), 3);
        return t1 && t2 && t3;
    }

    // tests count
    boolean testCount(Tester t) {
        this.init();
        return t.checkExpect(this.n1.count(0), 4) &&
                t.checkExpect(this.n2.count(0), 3) &&
                t.checkExpect(this.n3.count(0), 2) &&
                t.checkExpect(this.n4.count(0), 1) &&
                t.checkExpect(this.n5.count(0), 3) &&
                t.checkExpect(this.n6.count(2), 4) &&
                t.checkExpect(this.n7.count(3), 4) &&
                t.checkExpect(this.s1.count(0), 0);
    }

    // tests isSent
    boolean testIsSent(Tester t) {
        this.init();
        return t.checkExpect(this.s1.isSent(), true) &&
                t.checkExpect(this.s2.isSent(), true) &&
                t.checkExpect(this.n1.isSent(), false) &&
                t.checkExpect(this.n2.isSent(), false);
    }

    // tests link
    boolean testLink(Tester t) {
        this.init();
        boolean t1 = t.checkExpect(this.s1.next, this.s1);
        boolean t2 = t.checkExpect(this.s1.prev, this.s1);
        this.s1.link(this.n1, this.n2);
        boolean t3 = t.checkExpect(this.n1.next, this.n2);
        boolean t4 = t.checkExpect(this.n2.prev, this.n1);
        this.s1.link(this.n2, this.n4);
        boolean t5 = t.checkExpect(this.n2.next, this.n4);
        boolean t6 = t.checkExpect(this.n4.prev, this.n2);
        this.s1.link(this.n3, this.n7);
        boolean t7 = t.checkExpect(this.n3.next, this.n7);
        boolean t8 = t.checkExpect(this.n7.prev, this.n3);
        return t1 && t2 && t3 && t4 && t5 && t6 && t7 && t8;
    }

    // tests removeThis
    boolean testRemoveThis(Tester t) {
        this.init();
        Exception al = new RuntimeException("Can't remove from empty Deque!");
        return t.checkException(al, this.s1, "removeThis") &&
                t.checkException(al, this.d1.header, "removeThis") &&
                t.checkExpect(this.n1.removeThis(), this.zeroZero) &&
                t.checkExpect(this.n2.removeThis(), this.zeroOne) &&
                t.checkExpect(this.n5.removeThis(), this.twoTwo) &&
                t.checkExpect(this.n7.removeThis(), this.twoZero);
    }

    // tests addThat
    boolean testAddThat(Tester t) {
        this.init();
        boolean t1 = t.checkExpect(this.s1.prev, this.s1);
        boolean t2 = t.checkExpect(this.s1.next, this.s1);

        this.s1.addThat(this.zeroZero, true);
        boolean t3 = t.checkExpect(this.s1.prev, 
                new Node<Cell>(this.zeroZero, this.s1, this.s1));
        boolean t4 = t.checkExpect(this.s1.next,
                new Node<Cell>(this.zeroZero, this.s1, this.s1));

        new Sentinel<Cell>().addThat(this.zeroOne, false);
        boolean t5 = t.checkExpect(new Sentinel<Cell>().prev, 
                new Sentinel<Cell>());
        boolean t6 = t.checkExpect(this.s1.next,
                new Node<Cell>(this.zeroZero, this.s1, this.s1));

        new Sentinel<Cell>().addThat(this.zeroTwo, true);
        boolean t7 = t.checkExpect(new Sentinel<Cell>().prev,
                new Sentinel<Cell>());
        boolean t8 = t.checkExpect(new Sentinel<Cell>().next, 
                new Sentinel<Cell>());

        new Sentinel<Cell>().addThat(this.oneZero, false);
        boolean t9 = t.checkExpect(new Sentinel<Cell>().prev, 
                new Sentinel<Cell>());
        boolean t10 = t.checkExpect(this.s1.next,
                new Node<Cell>(this.zeroZero, this.s1, this.s1));

        return t1 && t2 && t3 && t4 && t5 && t6 && t7 && t8 && t9 && t10;
    }

    // tests removeThat
    boolean testRemoveThat(Tester t) {
        this.init();
        this.s1.updateNext(this.n1);
        this.s1.updatePrev(this.n4);
        boolean t1 = t.checkExpect(this.s1.removeThat(true), this.zeroZero);
        boolean t2 = t.checkExpect(this.s1.removeThat(false), this.oneZero);
        this.s1.updateNext(this.n2);
        this.s1.updatePrev(this.n5);
        boolean t3 = t.checkExpect(this.s1.removeThat(true), this.zeroOne);
        boolean t4 = t.checkExpect(this.s1.removeThat(false), this.twoTwo);
        this.s2.updateNext(this.n7);
        this.s2.updatePrev(this.n3);
        boolean t5 = t.checkExpect(this.s2.removeThat(true), this.twoZero);
        boolean t6 = t.checkExpect(this.s2.removeThat(false), this.zeroTwo);
        return t1 && t2 && t3 && t4 && t5 && t6;    
    }

    /***************************** ICOLLECTION ****************************/
    // tests isEmpty
    boolean testIsEmpty(Tester t) {
        this.init();
        boolean t1 = t.checkExpect(this.stack1.isEmpty(), true);
        this.stack1.add(this.zeroZero);
        boolean t2 = t.checkExpect(this.stack1.isEmpty(), false);

        boolean t3 = t.checkExpect(this.stack2.isEmpty(), true);
        this.stack2.add(this.twoOne);
        boolean t4 = t.checkExpect(this.stack2.isEmpty(), false);

        boolean t5 = t.checkExpect(this.queue1.isEmpty(), true);
        this.queue1.add(this.oneZero);
        boolean t6 = t.checkExpect(this.queue1.isEmpty(), false);
        return t1 && t2 && t3 && t4 && t5 && t6;
    }

    // tests add
    boolean testAdd(Tester t) {
        this.init();
        boolean t1 = t.checkExpect(this.stack1.isEmpty(), true);
        this.stack1.add(this.zeroZero);
        boolean t2 = t.checkExpect(this.stack1.isEmpty(), false) &&
                t.checkExpect(this.stack1.remove(), this.zeroZero);

        boolean t3 = t.checkExpect(this.stack1.isEmpty(), true);
        this.stack1.add(this.oneOne);
        boolean t4 = t.checkExpect(this.stack1.isEmpty(), false) &&
                t.checkExpect(this.stack1.remove(), this.oneOne);
        this.stack2.add(this.twoTwo);
        this.stack2.add(this.oneOne);
        boolean t5 = t.checkExpect(this.stack2.isEmpty(), false) &&
                t.checkExpect(this.stack2.remove(), this.oneOne);

        this.queue1.add(this.zeroOne);
        boolean t6 = t.checkExpect(this.queue1.isEmpty(), false) &&
                t.checkExpect(this.queue1.remove(), this.zeroOne);
        this.queue1.add(this.zeroTwo);
        this.queue1.add(this.twoOne);
        boolean t7 = t.checkExpect(this.queue1.isEmpty(), false) &&
                t.checkExpect(this.queue1.remove(), this.zeroTwo);
        return t1 && t2 && t3 && t4 && t5 && t6 && t7;
    }

    // tests remove
    boolean testRemove(Tester t) {
        this.init();
        Exception al = new RuntimeException("Can't remove from empty Deque!"); 
        this.stack1.add(this.zeroZero);
        this.stack1.add(this.zeroOne);
        this.stack1.add(this.zeroTwo);
        this.stack1.add(this.oneOne);
        this.stack1.add(this.oneTwo);
        this.queue1.add(this.twoTwo);
        this.queue1.add(this.twoOne);
        this.queue1.add(this.twoZero);
        return t.checkExpect(this.stack1.remove(), this.oneTwo) &&
                t.checkExpect(this.stack1.remove(), this.oneOne) &&
                t.checkExpect(this.stack1.remove(), this.zeroTwo) &&
                t.checkExpect(this.stack1.remove(), this.zeroOne) &&
                t.checkExpect(this.stack1.remove(), this.zeroZero) &&
                t.checkException(al, this.stack1, "remove") &&
                t.checkExpect(this.queue1.remove(), this.twoTwo) &&
                t.checkExpect(this.queue1.remove(), this.twoOne) &&
                t.checkExpect(this.queue1.remove(), this.twoZero) &&
                t.checkException(al, this.queue1, "remove");
    }

    // tests size
    boolean testCollectionSize(Tester t) {
        this.init();
        boolean t1 = t.checkExpect(this.stack1.size(), 0);
        this.stack1.add(this.zeroZero);
        boolean t2 = t.checkExpect(this.stack1.size(), 1);
        this.stack1.add(this.oneTwo);
        boolean t3 = t.checkExpect(this.stack1.size(), 2);
        this.stack1.add(this.twoZero);
        boolean t4 = t.checkExpect(this.stack1.size(), 3);
        this.stack1.add(this.oneZero);
        boolean t5 = t.checkExpect(this.stack1.size(), 4);

        boolean t6 = t.checkExpect(this.queue1.size(), 0);
        this.queue1.add(this.twoOne);
        boolean t7 = t.checkExpect(this.queue1.size(), 1);
        this.queue1.add(this.zeroOne);
        boolean t8 = t.checkExpect(this.queue1.size(), 2);
        return t1 && t2 && t3 && t4 && t5 && t6 && t7 && t8;
    }


    /******************************** CELL *******************************/
    // tests the equals method
    boolean testEquals(Tester t) {
        this.init();
        return t.checkExpect(this.zeroZero.equals(this.zeroZero), true) &&
                t.checkExpect(this.oneOne.equals(this.oneOne), true) &&
                t.checkExpect(this.oneTwo.equals(new Cell(1, 2)), true) &&
                t.checkExpect(new Cell(2, 0).equals(this.twoZero), true) &&
                t.checkExpect(this.oneTwo.equals(this.twoOne), false) &&
                t.checkExpect(this.twoOne.equals(new Cell(0, 2)), false) &&
                t.checkExpect(this.zeroZero.equals(this.row0), false) &&
                t.checkExpect(this.twoZero.equals(this.tByT), false);
    }

    // tests the updateColor method
    boolean testUpdateColor(Tester t) {
        this.init();
        boolean t1 = t.checkExpect(this.zeroZero.color, Color.GRAY);
        this.zeroZero.updateColor(Color.BLUE);
        boolean t2 = t.checkExpect(this.zeroZero.color, Color.BLUE);
        this.zeroZero.updateColor(Color.BLACK);
        boolean t3 = t.checkExpect(this.zeroZero.color, Color.BLACK);
        boolean t4 = t.checkExpect(this.oneOne.color, Color.GRAY);
        this.oneOne.updateColor(Color.MAGENTA);
        boolean t5 = t.checkExpect(this.oneOne.color, Color.MAGENTA);
        this.oneOne.updateColor(new Color(156, 35, 19));
        boolean t6 = t.checkExpect(this.oneOne.color, new Color(156, 35, 19));
        return t1 && t2 && t3 && t4 && t5 && t6;
    }

    // tests the updateLeft, updateTop, updateRight, updateBottom methods
    boolean testUpdate(Tester t) {
        this.init();

        // tests updateLeft
        boolean t1 = t.checkExpect(this.zeroZero.left, this.zeroZero);
        this.zeroZero.updateLeft(this.oneTwo);
        boolean t2 = t.checkExpect(this.zeroZero.left, this.oneTwo);
        this.zeroZero.updateLeft(this.twoOne);
        boolean t3 = t.checkExpect(this.zeroZero.left, this.twoOne);
        boolean t4 = t.checkExpect(this.oneOne.left, this.oneOne);
        this.oneOne.updateLeft(this.twoOne);
        boolean t5 = t.checkExpect(this.oneOne.left, this.twoOne);

        // tests updateTop
        boolean t6 = t.checkExpect(this.zeroOne.top, this.zeroOne);
        this.zeroOne.updateTop(this.oneOne);
        boolean t7 = t.checkExpect(this.zeroOne.top, this.oneOne);
        this.zeroOne.updateTop(this.zeroZero);
        boolean t8 = t.checkExpect(this.zeroOne.top, this.zeroZero);
        boolean t9 = t.checkExpect(this.twoTwo.top, this.twoTwo);
        this.twoTwo.updateTop(this.twoZero);
        boolean t10 = t.checkExpect(this.twoTwo.top, this.twoZero);

        // tests updateRight
        boolean t11 = t.checkExpect(this.oneTwo.right, this.oneTwo);
        this.oneTwo.updateRight(this.zeroOne);
        boolean t12 = t.checkExpect(this.oneTwo.right, this.zeroOne);
        this.oneTwo.updateRight(this.oneZero);
        boolean t13 = t.checkExpect(this.oneTwo.right, this.oneZero);
        boolean t14 = t.checkExpect(this.twoOne.right, this.twoOne);
        this.twoOne.updateRight(this.zeroZero);
        boolean t15 = t.checkExpect(this.twoOne.right, this.zeroZero);

        // tests updateBottom
        boolean t16 = t.checkExpect(this.twoZero.bottom, this.twoZero);
        this.twoZero.updateBottom(this.oneTwo);
        boolean t17 = t.checkExpect(this.twoZero.bottom, this.oneTwo);
        this.twoZero.updateBottom(this.zeroOne);
        boolean t18 = t.checkExpect(this.twoZero.bottom, this.zeroOne);
        boolean t19 = t.checkExpect(this.oneZero.bottom, this.oneZero);
        this.oneZero.updateBottom(this.zeroOne);
        boolean t20 = t.checkExpect(this.oneZero.bottom, this.zeroOne);
        return t1 && t2 && t3 && t4 && t5 && t6 && t7 && t8 && t9 && t10 && t11
                && t12 && t13 && t14 && t15 && t16 && t17 && t18 && t19 && t20;
    }

    // tests the connect method, under the assumptions of the method
    boolean testConnectCell(Tester t) {
        this.init();
        boolean t1 = t.checkExpect(this.zeroZero.bottom, this.zeroZero);
        this.zeroZero.connectCell(this.twoTwo);
        boolean t2 = t.checkExpect(this.zeroZero.bottom, this.zeroZero);
        this.zeroZero.connectCell(this.oneZero);
        boolean t3 = t.checkExpect(this.zeroZero.bottom, this.oneZero) &&
                t.checkExpect(this.oneZero.top, this.zeroZero);
        boolean t4 = t.checkExpect(this.oneOne.right, this.oneOne);
        this.oneOne.connectCell(this.twoZero);
        boolean t5 = t.checkExpect(this.oneOne.right, this.oneOne);
        this.oneOne.connectCell(this.oneTwo);
        boolean t6 = t.checkExpect(this.oneOne.right, this.oneTwo) &&
                t.checkExpect(this.oneTwo.left, this.oneOne);
        return t1 && t2 && t3 && t4 && t5 && t6;
    }

    // tests the hashCode method
    boolean testHashCode(Tester t) {
        this.init();
        return t.checkExpect(this.zeroZero.hashCode(), 0) &&
                t.checkExpect(this.zeroOne.hashCode(), 17) &&
                t.checkExpect(this.oneOne.hashCode(), 38) &&
                t.checkExpect(this.oneTwo.hashCode(), 55) &&
                t.checkExpect(this.twoZero.hashCode(), 42) &&
                t.checkExpect(this.twoTwo.hashCode(), 76) &&
                t.checkExpect(new Cell(4, 3).hashCode(), 135);
    }

    // tests the draw methods
    boolean testCellDraw(Tester t) {
        this.init();
        WorldImage cellImage = new RectangleImage(Maze.SCALE, Maze.SCALE, 
                OutlineMode.SOLID, Color.GRAY);
        WorldImage cellImage2 = new RectangleImage(Maze.SCALE, Maze.SCALE, 
                OutlineMode.SOLID, Color.RED);
        this.mtSc2.placeImageXY(cellImage, Maze.SCALE / 2, Maze.SCALE / 2);
        this.zeroZero.draw(this.mtSc);
        boolean t1 = t.checkExpect(this.mtSc, this.mtSc2);

        this.mtSc2.placeImageXY(cellImage, Maze.SCALE + Maze.SCALE / 2,
                Maze.SCALE + Maze.SCALE / 2);
        this.oneOne.draw(this.mtSc);
        boolean t2 = t.checkExpect(this.mtSc, this.mtSc2);

        this.mtSc2.placeImageXY(cellImage2, Maze.SCALE + Maze.SCALE / 2,
                2 * Maze.SCALE + Maze.SCALE / 2);
        this.twoOne.updateColor(Color.RED);
        this.twoOne.draw(this.mtSc);
        boolean t3 = t.checkExpect(this.mtSc, this.mtSc2);

        this.init();
        this.mtSc2.placeImageXY(cellImage, 2 * Maze.SCALE + Maze.SCALE / 2, 
                Maze.SCALE / 2);
        this.zeroTwo.draw(this.mtSc, Color.GRAY);
        boolean t4 = t.checkExpect(this.mtSc, this.mtSc2);

        this.mtSc2.placeImageXY(cellImage2, Maze.SCALE + Maze.SCALE / 2,
                2 * Maze.SCALE + Maze.SCALE / 2);
        this.twoOne.draw(this.mtSc, Color.RED);
        boolean t5 = t.checkExpect(this.mtSc, this.mtSc2);
        return t1 && t2 && t3 && t4 && t5;
    }

    /************************ EDGE & COMPARATOR ***************************/
    // test the isConnected method
    boolean testIsConnected(Tester t) {
        this.init();
        return t.checkExpect(this.c00c01.isConnected(this.zeroZero), true) &&
                t.checkExpect(this.c00c01.isConnected(this.twoTwo), false) &&
                t.checkExpect(this.c11c12.isConnected(this.oneTwo), true) &&
                t.checkExpect(this.c11c12.isConnected(this.zeroOne), false);
    }

    // test the otherCell method
    boolean testOtherCell(Tester t) {
        this.init();
        Exception e = new RuntimeException("Cell is not part of this edge!");
        return t.checkExpect(this.c01c02.otherCell(this.zeroOne), this.zeroTwo) &&
                t.checkExpect(this.c01c02.otherCell(this.zeroTwo), this.zeroOne) &&
                t.checkException(e, this.c01c02, "otherCell", this.twoTwo);
    }

    // tests the draw method
    boolean testEdgeDraw(Tester t) {
        this.init();
        WorldImage xLine = new LineImage(new Posn(Maze.SCALE, 0), Color.BLACK);
        WorldImage yLine = new LineImage(new Posn(0, Maze.SCALE), Color.BLACK);
        this.mtSc2.placeImageXY(yLine, 2 * Maze.SCALE, Maze.SCALE / 2);
        this.c01c02.draw(this.mtSc);
        boolean t1 = t.checkExpect(this.mtSc, this.mtSc2);

        this.mtSc2.placeImageXY(yLine, Maze.SCALE, Maze.SCALE / 2);
        this.c00c01.draw(this.mtSc);
        boolean t2 = t.checkExpect(this.mtSc, this.mtSc2);

        this.mtSc2.placeImageXY(xLine, Maze.SCALE / 2, 2 * Maze.SCALE);
        this.c10c20.draw(this.mtSc);
        boolean t3 = t.checkExpect(this.mtSc, this.mtSc2);
        return t1 && t2 && t3;
    }

    // tests the compare method
    boolean testCompare(Tester t) {
        this.init();
        EdgeComparator c = new EdgeComparator();
        return t.checkExpect(c.compare(this.c00c01, this.c01c02), -1) &&
                t.checkExpect(c.compare(this.c01c02, this.c00c01), 1) &&
                t.checkExpect(c.compare(this.c10c11, this.c11c12), -1) &&
                t.checkExpect(c.compare(this.c10c11, this.c00c01), 2) &&
                t.checkExpect(c.compare(this.c11c12, this.c00c01), 3);
    }

    /******************************* MOVABLE *******************************/
    // tests the updateCell method
    boolean testUpdateCell(Tester t) {
        this.init();
        boolean t1 = t.checkExpect(this.p1.visitedCells.contains(this.zeroZero), 
                true);
        this.p1.updateCell(this.zeroOne);
        boolean t2 = t.checkExpect(this.p1.visitedCells.contains(this.zeroOne),
                true);
        this.p1.updateCell(this.oneOne);
        boolean t3 = t.checkExpect(this.p1.visitedCells.contains(this.oneOne),
                true);
        boolean t4 = t.checkExpect(this.p1.visitedCells.contains(this.twoTwo),
                false);
        boolean t5 = t.checkExpect(this.p1.visitedCells.contains(this.oneTwo),
                false);
        this.mov.updateCell(this.twoOne);
        boolean t6 = t.checkExpect(this.mov.cur, this.twoOne);
        this.mov.updateCell(this.oneZero);
        boolean t7 = t.checkExpect(this.mov.cur, this.oneZero);
        return t1 && t2 && t3 && t4 && t5 && t6 && t7;
    }


    // tests the draw method
    boolean testDraw(Tester t) {
        this.init();
        WorldImage p1 = new FromFileImage("player.png");
        WorldImage sp = new FromFileImage("sp.png");
        this.mtSc2.placeImageXY(p1, (this.p1.cur.x * Maze.SCALE) + (Maze.SCALE / 2),
                (this.p1.cur.y * Maze.SCALE) + (Maze.SCALE / 2));
        this.p1.draw(this.mtSc);
        boolean t1 = t.checkExpect(this.mtSc, this.mtSc2);

        this.mtSc2.placeImageXY(p1, Maze.SCALE / 2, Maze.SCALE / 2);
        this.p1.draw(this.mtSc);
        boolean t2 = t.checkExpect(this.mtSc, this.mtSc2);

        this.p1.updateCell(this.oneOne);
        this.mtSc2.placeImageXY(p1, Maze.SCALE + (Maze.SCALE / 2), 
                Maze.SCALE + (Maze.SCALE / 2));
        this.p1.draw(this.mtSc);
        boolean t3 = t.checkExpect(this.mtSc, this.mtSc2); 

        this.mtSc2.placeImageXY(sp, 2 * Maze.SCALE + (Maze.SCALE / 2), 
                Maze.SCALE + (Maze.SCALE / 2));
        this.mov.draw(this.mtSc);
        boolean t4 = t.checkExpect(this.mtSc, this.mtSc2);

        this.mov.updateCell(this.twoOne);
        this.mtSc2.placeImageXY(sp,  Maze.SCALE + (Maze.SCALE / 2), 
                2 * Maze.SCALE + (Maze.SCALE / 2));
        this.mov.draw(this.mtSc);
        boolean t5 = t.checkExpect(this.mtSc, this.mtSc2);
        return t1 && t2 && t3 && t4 && t5;
    }


    /******************************** MAZE *******************************/

    // tests the initDraw method
    boolean testInitDraw(Tester t) {
        this.init();
        m.initDraw();
        int height = Maze.MAZE_HEIGHT;
        int width = Maze.MAZE_WIDTH;
        return t.checkExpect(m.cells.size(), height) &&
                t.checkExpect(m.cells.get(0).size(), width) &&
                t.checkExpect(m.worklist.size(), (height * (width - 1)) +
                        (width * (height - 1))) &&
                t.checkExpect(m.edgesInTree, new ArrayList<Edge>()) &&
                t.checkExpect(m.isBfs, false) &&
                t.checkExpect(m.isDfs, false) &&
                t.checkExpect(m.showPath, false) &&
                t.checkExpect(m.isMulti, false) &&
                t.checkExpect(m.start.x, 0) &&
                t.checkExpect(m.start.y, 0) &&
                t.checkExpect(m.start.color, Color.GREEN) && 
                t.checkExpect(m.tar.x, width - 1) &&
                t.checkExpect(m.tar.y, height - 1) &&
                t.checkExpect(m.tar.color, new Color(143, 26, 210)) &&
                t.checkExpect(m.p1.cur, m.start);
    }

    // tests the initCellColor method
    boolean testInitCellColor(Tester t) {
        this.init();
        for (ArrayList<Cell> arr : m.cells) {
            for (Cell c : arr) {
                c.updateColor(Color.RED);
            }
        }
        m.initCellColor();
        return t.checkExpect(m.start.color, Color.GREEN) &&
                t.checkExpect(m.tar.color, new Color(143, 26, 210)) &&
                t.checkExpect(m.cells.get(10).get(10).color, Color.GRAY) &&
                t.checkExpect(m.cells.get(0).get(15).color, Color.GRAY);
    }

    // tests the initCells method
    boolean testInitCells(Tester t) {
        this.init();
        return t.checkExpect(m.initCells(0, 0), new ArrayList<ArrayList<Cell>>()) &&
                t.checkExpect(m.initCells(2, 3), this.twoByThree) &&
                t.checkExpect(m.initCells(3, 3), this.tByT);
    }

    // tests the makeEdges method
    boolean testMakeEdges(Tester t) {
        this.init();
        ArrayList<Edge> sev = m.makeEdges(this.twoByThree);
        ArrayList<Edge> orig = new ArrayList<Edge>();
        orig.add(this.c00c10);
        orig.add(this.c00c01);
        orig.add(this.c01c11);
        orig.add(this.c01c02);
        orig.add(this.c02c12);
        orig.add(this.c10c11);
        orig.add(this.c11c12);
        for (int i = 0; i < 7; i += 1) {
            boolean t0 = t.checkExpect(sev.get(i).one, orig.get(i).one) &&
                    t.checkExpect(sev.get(i).two, orig.get(i).two);
        }
        return true;
    }

    // tests the hashStart method
    boolean testHashStart(Tester t) {
        this.init();
        return t.checkExpect(this.hashbrown.get(this.zeroZero), this.zeroZero) &&
                t.checkExpect(this.hashbrown.get(this.zeroOne), this.zeroOne) &&
                t.checkExpect(this.hashbrown.get(this.oneZero), this.oneZero) &&
                t.checkExpect(this.hashbrown.get(this.oneOne), this.oneOne) &&
                t.checkExpect(this.hashbrown.get(this.twoOne), this.twoOne) &&
                t.checkExpect(this.hashbrown.get(this.twoTwo), this.twoTwo);
    }

    // tests the find method
    boolean testFind(Tester t) {
        this.init();
        HashMap<Cell, Cell> hashbrown = m.hashStart(this.tByT);
        return t.checkExpect(m.find(hashbrown, this.zeroZero), this.zeroZero) &&
                t.checkExpect(m.find(hashbrown, this.oneOne), this.oneOne) &&
                t.checkExpect(m.find(hashbrown, this.zeroTwo), this.zeroTwo);
    }

    // tests the union method
    boolean testUnion(Tester t) {
        this.init();
        boolean t1 = t.checkExpect(this.hashtag.get(this.zeroZero), null);
        m.union(this.hashtag, this.zeroZero, this.zeroOne);
        boolean t2 = t.checkExpect(this.hashtag.get(this.zeroZero), this.zeroOne);
        m.union(this.hashtag, this.zeroZero, this.oneTwo);
        boolean t3 = t.checkExpect(this.hashtag.get(this.zeroZero), this.oneTwo);
        return t1 && t2 && t3;      
    }

    // tests the krus method
    boolean testKrus(Tester t) {
        this.init();
        ArrayList<Edge> wow = new ArrayList<Edge>();
        ArrayList<Edge> now = new ArrayList<Edge>();
        boolean t1 = t.checkExpect(m.krus(wow, this.hashbrown), new ArrayList<Edge>());

        wow.add(this.c00c01);
        now.add(this.c00c01);
        boolean t2 = t.checkExpect(m.krus(wow, this.hashbrown), now);

        now = new ArrayList<Edge>();
        wow.add(this.c21c22);
        wow.add(this.c10c11);
        now.add(this.c21c22);
        now.add(this.c10c11);
        boolean t3 = t.checkExpect(m.krus(wow, this.hashbrown), now);
        return t1 && t2 && t3;      
    }

    // tests the krusDraw method
    boolean testKrusDraw(Tester t) {
        this.init();
        ArrayList<Edge> ed = new ArrayList<Edge>();
        ArrayList<Edge> ed2 = new ArrayList<Edge>();
        Queue<Edge> edgyQ = new Queue<Edge>();
        m.drawStart = true;
        boolean t1 = t.checkExpect(m.krusDraw(edgyQ, this.hashbrown, ed), ed) &&
                t.checkExpect(m.drawStart, false);
        ed.add(this.c11c12);
        ed2.add(this.c11c12);
        edgyQ.add(this.c11c12);
        edgyQ.add(this.c10c20);
        edgyQ.add(this.c01c11);
        boolean t2 = 
                t.checkExpect(m.krusDraw(edgyQ, this.hashbrown, new ArrayList<Edge>()), ed);

        boolean t3 = t.checkExpect(this.hashbrown.get(this.oneOne), this.oneTwo);
        ed2.add(this.c10c20);
        boolean t4 = t.checkExpect(m.krusDraw(edgyQ, this.hashbrown, ed), ed2);
        boolean t5 = t.checkExpect(this.hashbrown.get(this.oneZero), this.twoZero);
        ed2.add(this.c01c11);
        boolean t6 = t.checkExpect(m.krusDraw(edgyQ, this.hashbrown, ed), ed2);
        boolean t7 = t.checkExpect(this.hashbrown.get(this.zeroOne), this.oneTwo);

        return t1 && t2 && t3 && t4 && t5 && t6 && t7;
    }

    // tests the connect method
    boolean testConnect(Tester t) {
        this.init();
        for (Cell c : row0) {
            boolean t0 = t.checkExpect(c.right, c);
        }
        ArrayList<Edge> arr = new ArrayList<Edge>();
        arr.add(new Edge(this.oneZero, this.twoZero, 1));
        ArrayList<Edge> row0Edge = new ArrayList<Edge>();
        row0Edge.add(new Edge(this.zeroZero, this.zeroOne, 2));
        row0Edge.add(new Edge(this.zeroOne, this.zeroTwo, 3));
        m.connect(row0Edge);
        m.connect(arr);
        return t.checkExpect(this.zeroZero.right, this.zeroOne) &&
                t.checkExpect(this.zeroOne.left, this.zeroZero) &&
                t.checkExpect(this.zeroOne.right, this.zeroTwo) &&
                t.checkExpect(this.zeroTwo.left, this.zeroOne) &&
                t.checkExpect(this.oneZero.bottom, this.twoZero) &&
                t.checkExpect(this.twoZero.top, this.oneZero);
    }

    // tests the reconstruct method
    boolean testReconstruct(Tester t) {
        this.init();
        m.start = this.zeroZero;
        m.tar = this.twoTwo;
        Color darkBlue = new Color(55, 112, 216);
        ArrayList<Cell> ans = new ArrayList<Cell>();
        ans.add(this.twoTwo);
        ans.add(this.twoOne);
        ans.add(this.oneOne);
        ans.add(this.oneZero);

        boolean t1 = t.checkExpect(this.zeroZero.color, Color.GRAY);
        boolean t2 = t.checkExpect(m.reconstruct(this.hashCE, this.zeroZero, true),
                new ArrayList<Cell>());
        boolean t3 = t.checkExpect(this.zeroZero.color, darkBlue);

        boolean t4 = t.checkExpect(this.zeroZero.color, darkBlue) &&
                t.checkExpect(this.twoTwo.color, Color.GRAY) &&
                t.checkExpect(this.twoOne.color, Color.GRAY) &&
                t.checkExpect(this.oneOne.color, Color.GRAY) &&
                t.checkExpect(this.oneZero.color, Color.GRAY);
        boolean t5 = t.checkExpect(m.reconstruct(this.hashCE, this.twoTwo, true),
                ans) &&
                t.checkExpect(m.start.color, darkBlue);
        for (Cell c : ans) {
            boolean t0 = t.checkExpect(c.color, darkBlue);
        }
        return t1 && t2 && t3 && t4 && t5;
    } 

    // tests the aiSearch method
    boolean testAiSearch(Tester t) {
        this.init();
        m.ai.cur = this.oneTwo;
        return t.checkExpect(m.aiSearch(this.tByT, this.edgy, this.twoTwo).size() > 0, true) &&
                t.checkExpect(m.aiSearch(this.tByT, this.edgy, this.zeroZero).size() > 0, true) &&
                t.checkExpect(m.aiSearch(this.tByT, this.edgy, this.oneTwo).size(), 0);
    }

    // tests the search method
    boolean testSearch(Tester t) {
        this.init();
        m.isBfs = true;
        m.tar = this.zeroZero;
        m.searchWorklist = new Queue<Cell>();
        m.visited = new ArrayList<Cell>();
        m.cameFromEdge = this.hashCE;
        m.visited.add(this.zeroZero);
        m.searchWorklist.add(this.zeroZero);
        boolean t1 = t.checkExpect(m.search(this.tByT, this.edgy),
                new ArrayList<Cell>()) &&
                t.checkExpect(m.isBfs, true);
        m.visited = new ArrayList<Cell>();
        m.searchWorklist.add(this.zeroZero);
        boolean t2 = t.checkExpect(m.search(this.tByT, this.edgy),
                new ArrayList<Cell>()) &&
                t.checkExpect(m.isBfs, false);
        m.isBfs = true;
        m.tar = this.zeroOne;
        m.searchWorklist.add(this.zeroZero);
        boolean t3 = t.checkExpect(m.search(this.tByT, this.edgy),
                new ArrayList<Cell>());
        m.searchWorklist.add(this.zeroZero);
        m.visited = new ArrayList<Cell>();
        boolean t4 = t.checkExpect(m.search(this.tByT, this.edgy),
                new ArrayList<Cell>()) && 
                t.checkExpect(this.zeroZero.color, new Color(130, 164, 228));
        return t1 && t2 && t3 && t4;
    }

    // tests the bfs method
    boolean testBfs(Tester t) {
        this.init();
        m.initDraw();
        m.visited = new ArrayList<Cell>();
        m.searchWorklist = new Queue<Cell>();
        m.cameFromEdge = new HashMap<Cell, Edge>();
        boolean t1 = t.checkExpect(m.isBfs, false) &&
                t.checkExpect(m.isDfs, false);
        m.isDfs = true;
        m.edgesInTree = m.krus(m.worklist, m.representatives);
        m.bfs();
        boolean t2 = t.checkExpect(m.visited.size(), 1) &&
                t.checkExpect(m.isBfs, true) &&
                t.checkExpect(m.isDfs, false) &&
                t.checkExpect(m.searchWorklist.size() >= 1, true) &&
                t.checkExpect(m.cameFromEdge.get(m.start), 
                        new Edge(m.start, m.start, 0));
        return t1 && t2;
    }

    // tests the dfs method
    boolean testDfs(Tester t) {
        this.init();
        m.initDraw();
        m.visited = new ArrayList<Cell>();
        m.searchWorklist = new Stack<Cell>();
        m.cameFromEdge = new HashMap<Cell, Edge>();
        boolean t1 = t.checkExpect(m.isBfs, false) &&
                t.checkExpect(m.isDfs, false);
        m.isBfs = true;
        m.edgesInTree = m.krus(m.worklist, m.representatives);
        m.dfs();
        boolean t2 = t.checkExpect(m.visited.size(), 1) &&
                t.checkExpect(m.isBfs, false) &&
                t.checkExpect(m.isDfs, true) &&
                t.checkExpect(m.searchWorklist.size() >= 1, true) &&
                t.checkExpect(m.cameFromEdge.get(m.start), 
                        new Edge(m.start, m.start, 0));
        return t1 && t2;    
    }

    // tests the onKeyEvent method
    boolean testOnKeyEvent(Tester t) {
        this.init();
        int height = Maze.MAZE_HEIGHT;
        int width = Maze.MAZE_WIDTH;
        m.initDraw();
        m.onKeyEvent("r");
        boolean t1 = t.checkExpect(m.vBias, 0) &&
                t.checkExpect(m.hBias, 0) &&
                t.checkExpect(m.cells.size(), height) &&
                t.checkExpect(m.cells.get(0).size(), width) &&
                t.checkExpect(m.worklist.size(), (height * (width - 1)) +
                        (width * (height - 1))) &&
                t.checkExpect(m.edgesInTree, new ArrayList<Edge>()) &&
                t.checkExpect(m.isBfs, false) &&
                t.checkExpect(m.isDfs, false) &&
                t.checkExpect(m.showPath, false) &&
                t.checkExpect(m.start.x, 0) &&
                t.checkExpect(m.start.y, 0) &&
                t.checkExpect(m.start.color, Color.GREEN) && 
                t.checkExpect(m.tar.x, width - 1) &&
                t.checkExpect(m.tar.y, height - 1) &&
                t.checkExpect(m.tar.color, new Color(143, 26, 210)) &&
                t.checkExpect(m.p1.cur, m.start);

        m.onKeyEvent("v");
        boolean t2 = t.checkExpect(m.vBias, height * width / 2) &&
                t.checkExpect(m.hBias, 0) &&
                t.checkExpect(m.cells.size(), height) &&
                t.checkExpect(m.cells.get(0).size(), width) &&
                t.checkExpect(m.worklist.size(), (height * (width - 1)) +
                        (width * (height - 1))) &&
                t.checkExpect(m.edgesInTree, new ArrayList<Edge>()) &&
                t.checkExpect(m.isBfs, false) &&
                t.checkExpect(m.isDfs, false) &&
                t.checkExpect(m.showPath, false) &&
                t.checkExpect(m.start.x, 0) &&
                t.checkExpect(m.start.y, 0) &&
                t.checkExpect(m.start.color, Color.GREEN) && 
                t.checkExpect(m.tar.x, width - 1) &&
                t.checkExpect(m.tar.y, height - 1) &&
                t.checkExpect(m.tar.color, new Color(143, 26, 210)) &&
                t.checkExpect(m.p1.cur, m.start);

        m.onKeyEvent("h");
        boolean t3 = t.checkExpect(m.vBias, 0) &&
                t.checkExpect(m.hBias, height * width / 2) &&
                t.checkExpect(m.cells.size(), height) &&
                t.checkExpect(m.cells.get(0).size(), width) &&
                t.checkExpect(m.worklist.size(), (height * (width - 1)) +
                        (width * (height - 1))) &&
                t.checkExpect(m.edgesInTree, new ArrayList<Edge>()) &&
                t.checkExpect(m.isBfs, false) &&
                t.checkExpect(m.isDfs, false) &&
                t.checkExpect(m.showPath, false) &&
                t.checkExpect(m.start.x, 0) &&
                t.checkExpect(m.start.y, 0) &&
                t.checkExpect(m.start.color, Color.GREEN) && 
                t.checkExpect(m.tar.x, width - 1) &&
                t.checkExpect(m.tar.y, height - 1) &&
                t.checkExpect(m.tar.color, new Color(143, 26, 210)) &&
                t.checkExpect(m.p1.cur, m.start);

        m.drawStart = !m.drawStart;
        m.onKeyEvent("b");
        HashMap<Cell, Edge> hm = new HashMap<Cell, Edge>();
        hm.put(this.zeroZero, new Edge(this.zeroZero, this.zeroZero, 0));

        ArrayList<Cell> arr = new ArrayList<Cell>();
        arr.add(this.zeroZero);

        Color lightBlue = new Color(130, 164, 228);
        this.zeroZero.updateColor(lightBlue);

        boolean t4 = t.checkExpect(m.searchWorklist, new Queue<Cell>()) &&
                t.checkExpect(m.cameFromEdge, hm) &&
                t.checkExpect(m.visited, arr) &&
                t.checkExpect(m.isBfs, true);

        m.onKeyEvent("n");
        boolean t5 = t.checkExpect(m.searchWorklist, new Stack<Cell>()) &&
                t.checkExpect(m.cameFromEdge, hm) &&
                t.checkExpect(m.visited, arr) &&
                t.checkExpect(m.isDfs, true);

        boolean t6 = t.checkExpect(m.showPath, false);
        m.onKeyEvent("p");
        boolean t7 = t.checkExpect(m.showPath, true);

        this.init();
        m.cells = this.tByT;
        m.worklist = m.makeEdges(this.tByT);
        Collections.sort(m.worklist, new EdgeComparator());
        HashMap<Cell, Cell> reps = m.hashStart(m.cells);
        ArrayList<Edge> eTree = m.krus(m.worklist, reps);
        m.onKeyEvent("q");
        boolean t8 = t.checkExpect(m.drawStart, false) &&
                t.checkExpect(m.representatives, reps) &&
                t.checkExpect(m.edgesInTree, eTree);          

        m.p1.cur = this.zeroZero;
        this.zeroOne.updateColor(lightBlue);
        this.oneZero.updateColor(lightBlue);
        this.zeroZero.connectCell(this.zeroOne);
        this.zeroZero.connectCell(this.oneZero);

        boolean t9 = t.checkExpect(m.p1.cur, this.zeroZero);
        m.onKeyEvent("right");
        boolean t10 = t.checkExpect(m.p1.cur, this.zeroOne);
        m.onKeyEvent("left");
        boolean t11 = t.checkExpect(m.p1.cur, this.zeroZero);
        m.onKeyEvent("down");
        boolean t12 = t.checkExpect(m.p1.cur, this.oneZero);
        m.onKeyEvent("up");
        boolean t13 = t.checkExpect(m.p1.cur, this.zeroZero);

        m.p2 = new Player(this.zeroZero);
        m.isMulti = true;

        boolean t14 = t.checkExpect(m.p2.cur, this.zeroZero);
        m.onKeyEvent("d");
        boolean t15 = t.checkExpect(m.p2.cur, this.zeroOne);
        m.onKeyEvent("a");
        boolean t16 = t.checkExpect(m.p2.cur, this.zeroZero);
        m.onKeyEvent("s");
        boolean t17 = t.checkExpect(m.p2.cur, this.oneZero);
        m.onKeyEvent("w");
        boolean t18 = t.checkExpect(m.p2.cur, this.zeroZero);

        m.isMulti = false;
        boolean t19 = t.checkExpect(m.p1.img, new FromFileImage("player.png"));
        m.onKeyEvent("2");
        boolean t20 = t.checkExpect(m.isMulti, true);
        boolean t21 = t.checkExpect(m.p1.img, new FromFileImage("p1.png"));
        boolean t22 = t.checkExpect(m.p2.img, new FromFileImage("p2.png"));
        boolean t23 = t.checkExpect(m.p1.cur, m.tar);
        boolean t24 = t.checkExpect(m.p2.cur, m.start);


        return t1 && t2 && t3 && t4 && t5 && t6 && t7 && t8 && t9 && t10 && t11
                && t12 && t13 && t14 && t15 && t16 && t17 && t18 && t19 && t20
                && t21 && t22 && t23 && t24;

    } 

    // tests the makeScene method
    boolean testMakeScene(Tester t) {
        this.init();
        m.showPath = false;
        m.drawStart = false;
        m.start = this.zeroZero;
        m.tar = this.twoTwo;
        m.edgesInTree = new ArrayList<Edge>();
        m.worklist = this.edgy;
        m.cells = this.tByT;
        for (ArrayList<Cell> arr : m.cells) {
            for (Cell c : arr) {
                c.draw(this.mtSc);
            }
        }
        for (Edge e : m.worklist) {
            if (!m.edgesInTree.contains(e)) {
                e.draw(this.mtSc);        
            }
        }
        m.p1.draw(this.mtSc);
        m.ai.draw(this.mtSc);
        boolean t2 = t.checkExpect(m.makeScene(), this.mtSc);
        m.isMulti = true;
        m.p2 = new Player(m.start);
        m.p2.draw(this.mtSc);
        boolean t3 = t.checkExpect(m.makeScene(), this.mtSc);

        m.isMulti = false;
        m.showPath = true;
        m.p1.visitedCells.add(this.zeroZero);
        m.p1.visitedCells.add(this.zeroOne);
        for (ArrayList<Cell> arr : m.cells) {
            for (Cell c : arr) {
                c.draw(this.mtSc2);
            }
        }
        for (Cell c : m.p1.visitedCells) {
            c.draw(this.mtSc2, Color.RED);
        }
        for (Edge e : m.worklist) {
            if (!m.edgesInTree.contains(e)) {
                e.draw(this.mtSc2);   
            }
        }
        m.p1.draw(this.mtSc2);
        m.ai.draw(this.mtSc2);
        boolean t4 = t.checkExpect(m.makeScene(), this.mtSc2);

        m.drawStart = true;
        TextImage knock = new TextImage("Knocking down walls...", 40, Color.WHITE);
        this.mtSc2.placeImageXY(knock, (Maze.MAZE_WIDTH * Maze.SCALE) / 2,
                (Maze.MAZE_HEIGHT * Maze.SCALE) / 2);
        boolean t5 = t.checkExpect(m.makeScene(), this.mtSc2);

        m.tar = this.zeroZero;
        TextImage win = new TextImage("YOU WON!", 60, Color.WHITE);
        this.mtSc2.placeImageXY(win, (Maze.MAZE_WIDTH * Maze.SCALE) / 2,
                (Maze.MAZE_HEIGHT * Maze.SCALE) / 2);
        boolean t6 = t.checkExpect(m.makeScene(), this.mtSc2);

        m.showPath = false;
        m.isMulti = true;
        m.p2 = new Player(m.start);
        m.drawStart = false;
        m.start = m.p1.cur;
        m.tar = m.p2.cur;
        TextImage won = new TextImage("AMAZING!", 60, Color.WHITE);
        mtSc.placeImageXY(won, (Maze.MAZE_WIDTH * Maze.SCALE) / 2,
                (Maze.MAZE_HEIGHT * Maze.SCALE) / 2);
        boolean t7 = t.checkExpect(m.makeScene(), this.mtSc);
        return t2 && t3 && t4 && t5 && t6 && t7;
    }

    // tests the onTick method
    boolean testOnTick(Tester t) {
        this.init();
        int temp = m.edgesInTree.size();
        m.drawStart = false;
        m.visited = new ArrayList<Cell>();
        m.searchWorklist = new Queue<Cell>();
        m.searchWorklist.add(m.start);
        m.onTick();
        boolean t1 = t.checkExpect(m.edgesInTree.size(), temp) &&
                t.checkExpect(m.visited.size(), 0);
        m.drawStart = true;
        m.onTick();
        boolean t2 = t.checkExpect(m.edgesInTree.size(), temp + 1) &&
                t.checkExpect(m.visited.size(), 0);
        m.isBfs = true;
        m.onTick();
        boolean t3 = t.checkExpect(m.edgesInTree.size(), temp + 2) &&
                t.checkExpect(m.visited.size(), 1);
        m.drawStart = false;
        boolean t4 = t.checkExpect(m.edgesInTree.size(), temp + 2) &&
                t.checkExpect(m.visited.size() >= 1, true);
        int tempV = m.visited.size();
        m.isBfs = false;
        m.onTick();
        boolean t5 = t.checkExpect(m.edgesInTree.size(), temp + 2) &&
                t.checkExpect(m.visited.size(), tempV);

        this.init();
        temp = m.edgesInTree.size();
        m.drawStart = false;
        m.visited = new ArrayList<Cell>();
        m.searchWorklist = new Stack<Cell>();
        m.searchWorklist.add(m.start);
        m.onTick();
        boolean t6 = t.checkExpect(m.edgesInTree.size(), temp) &&
                t.checkExpect(m.visited.size(), 0);
        m.drawStart = true;
        m.onTick();
        boolean t7 = t.checkExpect(m.edgesInTree.size(), temp + 1) &&
                t.checkExpect(m.visited.size(), 0);
        m.isDfs = true;
        m.onTick();
        boolean t8 = t.checkExpect(m.edgesInTree.size(), temp + 2) &&
                t.checkExpect(m.visited.size(), 1);
        m.drawStart = false;
        boolean t9 = t.checkExpect(m.edgesInTree.size(), temp + 2) &&
                t.checkExpect(m.visited.size() >= 1, true);
        tempV = m.visited.size();
        m.isDfs = false;
        m.onTick();
        boolean t10 = t.checkExpect(m.edgesInTree.size(), temp + 2) &&
                t.checkExpect(m.visited.size(), tempV);
        boolean t11 = t.checkExpect(m.tick < 49, true) &&
                t.checkRange(m.ai.cur.x, 0, Maze.MAZE_WIDTH) &&
                t.checkRange(m.ai.cur.y, 0, Maze.MAZE_HEIGHT);
        m.tick = 49;
        m.onTick();
        boolean t12 = t.checkExpect(m.tick == 0, true) &&
                t.checkRange(m.ai.cur.x, 0, Maze.MAZE_WIDTH) &&
                t.checkRange(m.ai.cur.y, 0, Maze.MAZE_HEIGHT);

        return t1 && t2 && t3 && t4 && t5 && t6 && t7 && t8 && t9 && t10 && t11 && t12;
    }

} 
