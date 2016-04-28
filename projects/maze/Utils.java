import java.util.*;

// utils class
class Utils {

    // converts arraylist to queue
    <T> Queue<T> toQueue(ArrayList<T> arr) {
        Queue<T> q = new Queue<T>();
        for (T t : arr) {
            q.add(t);
        }
        return q;
    }

    // EFFECT: sets a cell's neighbors to itself
    void cellf(Cell c) {
        c.updateBottom(c);
        c.updateLeft(c);
        c.updateRight(c);
        c.updateTop(c);
    }
}
