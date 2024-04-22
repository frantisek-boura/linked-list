package abstrlifo;

import java.util.Iterator;

public interface IAbstrLifo<T> extends Iterable<T> {

    void zrus();
    boolean jePrazdny();

    void vloz(T data);
    T odeber() throws AbstrLifoException;

    @Override
    Iterator<T> iterator();

}
