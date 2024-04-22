package abstrlifo;

import java.util.Iterator;
import abstrdoublelist.AbstrDoubleList;
import abstrdoublelist.AbstrDoubleListException;

public class AbstrLifo<T> implements IAbstrLifo<T> {

    private AbstrDoubleList<T> struktura;

    public AbstrLifo() {
        struktura = new AbstrDoubleList<T>();
    }

    @Override
    public void zrus() {
        struktura.zrus();
    }

    @Override
    public boolean jePrazdny() {
        return struktura.jePrazdny();
    }

    @Override
    public void vloz(T data) {
        if (data == null) {
            throw new NullPointerException();
        }

        try {
            struktura.vlozPosledni(data);
            struktura.zpristupniPosledni();
        } catch (AbstrDoubleListException e) {
            // sem se to nikdy nedostane
            // metoda zpristupniPosledni vyusti v chybu pouze tehdy, je-li seznam prazdny,
            // coz se nikdy nestane, jelikoz pred jejim zavolanim vkladam do seznamu data
            // ktera nemohou byt null, coz zajistuje podminka jak na zacatku teto metody,
            // tak na zacatku metody vlozPosledni
        }
    }

    @Override
    public T odeber() throws AbstrLifoException {
        try {
            return struktura.odeberPosledni();
        } catch (AbstrDoubleListException e) {
            throw new AbstrLifoException("Zasobnik je prazdny");
        }
    }

    @Override
    public Iterator<T> iterator() {
        return struktura.iterator();
    }
    
}
