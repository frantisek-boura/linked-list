package enums;

public enum EReorg {
    DEKOMPOZICE("Dekompozice"),
    AGREGACE("Agregace");

    private String nazev;

    EReorg(String nazev) {
        this.nazev = nazev;
    }

    @Override
    public String toString() {
        return this.nazev;
    }
}
