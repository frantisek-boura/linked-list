package enums;

public enum EPozice {
    PRVNI("První"),
    POSLEDNI("Poslední"),
    PREDCHUDCE("Předchůdce"),
    NASLEDNIK("Následník"),
    AKTUALNI("Aktuální");

    private String nazev;

    EPozice(String nazev) {
        this.nazev = nazev;
    }

    @Override
    public String toString() {
        return this.nazev;
    }
}
