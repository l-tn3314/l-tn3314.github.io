import java.util.Iterator;

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
