//represents a mutable collection of items
interface ICollection<T> {
    // is this collection empty?
    boolean isEmpty();

    // EFFECT: adds the item to the collection
    void add(T item);

    // returns the first item of the collection
    // EFFECT: removes that first item
    T remove();

    // returns the size
    int size();
}

//represents a stack
class Stack<T> implements ICollection<T> {
    Deque<T> contents;

    // constructor
    Stack() {
        this.contents = new Deque<T>();
    }

    // is this stack empty?
    public boolean isEmpty() {
        return this.contents.isEmpty();
    }

    // returns removed item
    // EFFECT: removes item from this stack
    public T remove() {
        return this.contents.removeFromHead();
    }

    // EFFECT: adds item to this stack
    public void add(T item) {
        this.contents.addAtHead(item);
    }

    // returns the size of this stack
    public int size() {
        return this.contents.size();
    }
}

//represents a queue
class Queue<T> implements ICollection<T> {
    Deque<T> contents;

    // constructor
    Queue() {
        this.contents = new Deque<T>();
    }

    // is this queue empty?
    public boolean isEmpty() {
        return this.contents.isEmpty();
    }

    // returns removed item
    // EFFECT: removes item from this queue
    public T remove() {
        return this.contents.removeFromHead();
    }

    // EFFECT: adds item to this queue
    public void add(T item) {
        this.contents.addAtTail(item); 
    }

    // returns the size of this queue
    public int size() {
        return this.contents.size();
    }
}
