package abstrdoublelist;

import java.util.Iterator;

public class AbstrDoubleList<T> implements IAbstrDoubleList<T> {

    private int pocet;
    private ListItem<T> prvni;
    private ListItem<T> aktualni;
    private ListItem<T> posledni;

    private static class ListItem<T> {

        private final T data;
        private ListItem<T> dalsi;
        private ListItem<T> predchozi;

        public ListItem(T data) {
            this.data = data;
            this.predchozi = null;
            this.dalsi = null;
        }
        
    }

    public AbstrDoubleList() {
        this.pocet = 0;
        this.prvni = null;
        this.aktualni = null;
        this.posledni = null;
    }

    @Override
    public void zrus() {
        this.pocet = 0;
        this.prvni = null;
        this.aktualni = null;
        this.posledni = null;
    }

    @Override
    public boolean jePrazdny() {
        return this.pocet == 0;
    }

    private void vlozDoPrazdneho(T data) {
        this.pocet++;

        this.prvni = new ListItem<T>(data);
        this.prvni.predchozi = this.prvni;
        this.prvni.dalsi = this.prvni;
        this.posledni = this.prvni;
        this.aktualni = this.prvni;
    }

    @Override
    public void vlozPrvni(T data) {
        if (data == null) {
            throw new NullPointerException();
        }

        if (this.pocet == 0) {
            vlozDoPrazdneho(data);
        } else {
            this.pocet++;

            // inicializace vkladaneho prvku
            // urceni mist predchudce a naslednika v seznamu
            ListItem<T> vkladany = new ListItem<T>(data);
            ListItem<T> pred = this.posledni;
            ListItem<T> po = this.prvni;

            // navazani vztahu s nove pridanym prvkem
            vkladany.dalsi = po;
            po.predchozi = vkladany;
            vkladany.predchozi = pred;
            pred.dalsi = vkladany;

            // zmena ukazatele na prvni prvek v seznamu na nove vlozeny
            this.prvni = vkladany;
        }
    }

    @Override
    public void vlozPosledni(T data) {
        if (data == null) {
            throw new NullPointerException();
        }

        // pokud je seznam prazdny, tak tato metoda inicializuje seznam
        if (this.pocet == 0) {
            vlozDoPrazdneho(data);
        } else {
            this.pocet++;

            // inicializace vkladaneho prvku
            // urceni mist predchudce a naslednika v seznamu
            ListItem<T> vkladany = new ListItem<T>(data);
            ListItem<T> pred = this.posledni;
            ListItem<T> po = this.prvni;

            // navazani vztahu s nove pridanym prvkem
            vkladany.dalsi = po;
            po.predchozi = vkladany;
            vkladany.predchozi = pred;
            pred.dalsi = vkladany;

            // zmena ukazatele na posledni prvek v seznamu na nove vlozeny
            this.posledni = vkladany;
        }
    }

    @Override
    public void vlozNaslednika(T data) throws AbstrDoubleListException {
        if (data == null) {
            throw new NullPointerException();
        }

        if (this.pocet == 0 || this.aktualni == null) {
            throw new AbstrDoubleListException("Seznam prazdny nebo nebyl zvolen aktualni prvek");
        }

        // pokud je seznam prazdny, tak tato metoda inicializuje seznam
        if (this.aktualni == this.posledni) {
            vlozPosledni(data);
        } else {
            this.pocet++;

            // inicializace vkladaneho prvku
            // urceni mist predchudce a naslednika v seznamu
            ListItem<T> vkladany = new ListItem<T>(data);
            ListItem<T> pred = this.aktualni;
            ListItem<T> po = this.aktualni.dalsi;

            // navazani vztahu s nove pridanym prvkem
            vkladany.dalsi = po;
            po.predchozi = vkladany;
            vkladany.predchozi = pred;
            pred.dalsi = vkladany;
        }
    }

    @Override
    public void vlozPredchudce(T data) throws AbstrDoubleListException {
        if (data == null) {
            throw new NullPointerException();
        }

        if (this.pocet == 0 || this.aktualni == null) {
            throw new AbstrDoubleListException("Seznam prazdny nebo nebyl zvolen aktualni prvek");
        }

        if (this.aktualni == this.prvni) {
            vlozPrvni(data);
        } else {
            this.pocet++;

            // inicializace vkladaneho prvku
            // urceni mist predchudce a naslednika v seznamu
            ListItem<T> vkladany = new ListItem<T>(data);
            ListItem<T> pred = this.aktualni.predchozi;
            ListItem<T> po = this.aktualni;

            // navazani vztahu s nove pridanym prvkem
            vkladany.dalsi = po;
            po.predchozi = vkladany;
            vkladany.predchozi = pred;
            pred.dalsi = vkladany;
        }
    }

    @Override
    public T zpristupniAktualni() throws AbstrDoubleListException {
        if (this.pocet == 0 || this.aktualni == null) {
            throw new AbstrDoubleListException("Seznam prazdny nebo nebyl zvolen aktualni prvek");
        }

        ListItem<T> vybrany = this.aktualni;
        return vybrany.data;
    }

    @Override
    public T zpristupniPrvni() throws AbstrDoubleListException {
        if (this.pocet == 0) {
            throw new AbstrDoubleListException("Seznam prazdny");
        }

        this.aktualni = this.prvni;
        return zpristupniAktualni();
    }

    @Override
    public T zpristupniPosledni() throws AbstrDoubleListException {
        if (this.pocet == 0) {
            throw new AbstrDoubleListException("Seznam prazdny");
        }
        
        this.aktualni = this.posledni;
        return zpristupniAktualni();
    }

    @Override
    public T zpristupniNaslednika() throws AbstrDoubleListException {
        if (this.pocet == 0 || this.aktualni == null) {
            throw new AbstrDoubleListException("Seznam prazdny nebo nebyl zvolen aktualni prvek");
        }

        this.aktualni = this.aktualni.dalsi;
        return zpristupniAktualni();
    }

    @Override
    public T zpristupniPredchudce() throws AbstrDoubleListException {
        if (this.pocet == 0 || this.aktualni == null) {
            throw new AbstrDoubleListException("Seznam prazdny nebo nebyl zvolen aktualni prvek");
        }

        this.aktualni = this.aktualni.predchozi;
        return zpristupniAktualni();
    }

    @Override
    public T odeberAktualni() throws AbstrDoubleListException {
        if (this.pocet == 0 || this.aktualni == null) {
            throw new AbstrDoubleListException("Seznam prazdny nebo nebyl zvolen aktualni prvek");
        }

        // pokud je ukazatel aktualni nastaven na prvni prvek v seznamu
        // odebirani aktualniho prvku se chova stejne, jako odebirani prvniho prvku
        if (this.aktualni == this.prvni) {
            T odebirany = odeberPrvni();

            // po odebrani aktualniho se ukazatel aktualni nastavi na prvni prvek v seznamu
            this.aktualni = this.prvni;

            return odebirany;
        }

        // pokud je ukazatel aktualni nastaven na posledni prvek v seznamu
        // odebirani aktualniho prvku se chova stejne, jako odebirani posledniho prvku
        if (this.aktualni == this.posledni) {
            T odebirany = odeberPosledni();

            // po odebrani aktualniho se ukazatel aktualni nastavi na prvni prvek v seznamu
            this.aktualni = this.prvni;

            return odebirany;
        }

        this.pocet--;

        // inicializace odebiraneho prvku
        // urceni mist predchudce a naslednika v seznamu
        ListItem<T> odebirany = this.aktualni;
        ListItem<T> pred = this.aktualni.predchozi;
        ListItem<T> po = this.aktualni.dalsi;

        // navazani vztahu predchudce a naslednika tak, 
        // aby zmizel odebirany prvek
        pred.dalsi = po;
        po.predchozi = pred;

        // po odebrani aktualniho se ukazatel aktualni nastavi na prvni prvek v seznamu
        this.aktualni = this.prvni;
        return odebirany.data;
    }

    @Override
    public T odeberPrvni() throws AbstrDoubleListException {
        if (this.pocet == 0) {
            throw new AbstrDoubleListException("Seznam prazdny");
        }

        // v pripade, ze aktualne vybrany prvek je posledni prvek
        // je aktualni (tedy posledni) prvek smazan
        // a ukazatel aktualni je nastaven na prvni prvek v seznamu
        boolean smazanAktualni = (this.prvni == this.aktualni);

        this.pocet--;

        // inicializace odebiraneho prvku
        // urceni mist predchudce a naslednika v seznamu
        ListItem<T> odebirany = this.prvni;
        ListItem<T> pred = this.prvni.predchozi;
        ListItem<T> po = this.prvni.dalsi;

        // navazani vztahu predchudce a naslednika tak, 
        // aby zmizel odebirany prvek
        pred.dalsi = po;
        po.predchozi = pred;

        // zmena ukazatele na prvni prvek v seznamu na jeho naslednika
        this.prvni = po;

        if (smazanAktualni) {
            this.aktualni = this.prvni;
        }

        return odebirany.data;
    }

    @Override
    public T odeberPosledni() throws AbstrDoubleListException {
        if (this.pocet == 0) {
            throw new AbstrDoubleListException("Seznam prazdny");
        }

        // v pripade, ze aktualne vybrany prvek je posledni prvek
        // je aktualni (tedy posledni) prvek smazan
        // a ukazatel aktualni je nastaven na prvni prvek v seznamu
        boolean smazanAktualni = (this.posledni == this.aktualni);
        
        this.pocet--;

        // inicializace odebiraneho prvku
        // urceni mist predchudce a naslednika v seznamu
        ListItem<T> odebirany = this.posledni;
        ListItem<T> pred = this.posledni.predchozi;
        ListItem<T> po = this.posledni.dalsi;

        // navazani vztahu predchudce a naslednika tak, 
        // aby zmizel odebirany prvek
        pred.dalsi = po;
        po.predchozi = pred;

        // zmena ukazatele na posledni prvek v seznamu na jeho predchudce
        this.posledni = pred;

        if (smazanAktualni) {
            this.aktualni = this.prvni;
        }

        return odebirany.data;

    }

    @Override
    public T odeberNaslednika() throws AbstrDoubleListException {
        if (this.pocet == 0 || this.aktualni == null) {
            throw new AbstrDoubleListException("Seznam prazdny nebo nebyl zvolen aktualni prvek");
        }

        // v pripade, ze naslednik je posledni, metoda se chova jako pri odebirani posledniho
        if (this.aktualni.dalsi == this.posledni) {
            return odeberPosledni();
        }

        // v pripade, ze naslednik je prvni, metoda se chova jako pri odebirani prvniho
        if (this.aktualni.dalsi == this.prvni) {
            return odeberPrvni();
        }

        this.pocet--;

        // inicializace odebiraneho prvku
        // urceni mist predchudce a naslednika v seznamu
        ListItem<T> odebirany = this.aktualni.dalsi;
        ListItem<T> pred = this.aktualni;
        ListItem<T> po = this.aktualni.dalsi.dalsi;

        // navazani vztahu predchudce a naslednika tak, 
        // aby zmizel odebirany prvek
        pred.dalsi = po;
        po.predchozi = pred;

        return odebirany.data;
    }

    @Override
    public T odeberPredchudce() throws AbstrDoubleListException {
        if (this.pocet == 0 || this.aktualni == null) {
            throw new AbstrDoubleListException("Seznam prazdny nebo nebyl zvolen aktualni prvek");
        }

        // v pripade, ze predchozi je posledni, metoda se chova jako pri odebirani posledniho
        if (this.aktualni.predchozi == this.posledni) {
            return odeberPosledni();
        }

        // v pripade, ze predchozi je prvni, metoda se chova jako pri odebirani prvniho
        if (this.aktualni.predchozi == this.prvni) {
            return odeberPrvni();
        }

        this.pocet--;

        // inicializace odebiraneho prvku
        // urceni mist predchudce a naslednika v seznamu
        ListItem<T> odebirany = this.aktualni.predchozi;
        ListItem<T> pred = this.aktualni.predchozi.predchozi;
        ListItem<T> po = this.aktualni;

        // navazani vztahu predchudce a naslednika tak, 
        // aby zmizel odebirany prvek
        pred.dalsi = po;
        po.predchozi = pred;

        return odebirany.data;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {

            private ListItem<T> akt = prvni;
            private boolean init = false;

            @Override
            public boolean hasNext() {
                if (pocet == 0) return false;

                return !(akt == prvni && init);
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    return null;
                }

                init = true;

                T data = akt.data;
                akt = akt.dalsi;
                return data;
            }
        };
    }

}
