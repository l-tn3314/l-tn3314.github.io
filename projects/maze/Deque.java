//represents a deque
class Deque<T> {
    Sentinel<T> header;

    // constructor
    Deque() {
        this.header = new Sentinel<T>();
    }

    // convenience constructor
    Deque(Sentinel<T> header) {
        this.header = header;
    }

    // returns the number of nodes in this Deque
    int size() {
        return header.startCount();
    }

    // is this deque empty?
    boolean isEmpty() {
        return this.header.next.isSent(); // this.header is a sentinel
    }

    // EFFECT: inserts a Node with the given T value at the front of the list
    void addAtHead(T val) {
        this.header.addThat(val, true);
    }

    // EFFECT: inserts a Node with the given T value at the tail of the list
    void addAtTail(T val) {
        this.header.addThat(val, false);
    }

    // returns first node from this Deque
    // EFFECT: removes the first node from this Deque
    T removeFromHead() {
        return this.header.removeThat(true);
    }

    // returns last node from this Deque
    // EFFECT: removes the last node from this Deque
    T removeFromTail() {
        return this.header.removeThat(false);
    }
}

//represents a node
abstract class ANode<T> {
    ANode<T> next;
    ANode<T> prev;

    // constructor
    ANode(ANode<T> next, ANode<T> prev) {
        this.next = next;
        this.prev = prev;
    }

    // EFFECT: changes next to the given ANode<T>
    void updateNext(ANode<T> next) {
        this.next = next;
    }

    // EFFECT: changes prev to the given ANode<T>
    void updatePrev(ANode<T> prev) {
        this.prev = prev;
    }

    // returns number of nodes after this ANode
    abstract int count(int acc);

    // is this ANode a sentinel?
    abstract boolean isSent();

    // EFFECT: links two ANodes in the argument order
    void link(ANode<T> bef, ANode<T> aft) {
        bef.updateNext(aft);
        aft.updatePrev(bef);
    }

    // returns the data of the removed node
    // EFFECT: links the prev and next nodes
    abstract T removeThis();

}

//represents the end of the list, 
//where next links to the head and prev links to the tail
class Sentinel<T> extends ANode<T> {

    // constructor
    Sentinel() {
        super(null, null);
        super.next = this;
        super.prev = this;
    }

    // returns number of nodes after this Sentinel
    int startCount() {
        return this.next.count(0);
    }

    // returns number of nodes after this Sentinel
    int count(int acc) {
        return acc;
    }

    // is this a sentinel?
    boolean isSent() {
        return true;
    }

    // EFFECT: inserts a Node with the given T value
    void addThat(T value, boolean atHead) {
        ANode<T> senti = new Sentinel<T>();  // senti prevents infinite looping
        ANode<T> newNode = new Node<T>(value, senti, senti);
        if (atHead) {
            super.link(newNode, this.next);
            super.link(this, newNode);  
        }   
        else {
            super.link(this.prev, newNode);
            super.link(newNode, this);
        }

    }

    // returns removed node
    // EFFECT: removes either the first or last node
    T removeThat(boolean atHead) {
        if (atHead) {
            return this.next.removeThis();
        }
        else {
            return this.prev.removeThis();
        }
    }

    // removes this ANode
    T removeThis() {
        throw new RuntimeException("Can't remove from empty Deque!");
    }

}

//represents a data node
class Node<T> extends ANode<T> {
    T data;

    // constructor
    Node(T data) {
        super(null, null);
        this.data = data;
    }

    // convenience constructor
    Node(T data, ANode<T> next, ANode<T> prev) {
        super(next, prev);
        if (next == null || prev == null) {
            throw new IllegalArgumentException("The node(s) can't be null!");
        }
        this.data = data;
        next.updatePrev(this);
        prev.updateNext(this);
    }

    // returns number of nodes after this Node
    int count(int acc) {
        return this.next.count(acc + 1);
    }

    // is this a sentinel?
    boolean isSent() {
        return false;
    }

    // returns the data from this node
    // EFFECT: links the prev and next nodes
    T removeThis() {
        this.link(this.prev, this.next);
        return this.data;
    }

}
