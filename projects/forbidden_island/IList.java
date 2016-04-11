import java.util.Iterator;

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
