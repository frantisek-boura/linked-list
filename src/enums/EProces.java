package enums;

public enum EProces {
    MANUALNI("Manuální"),
    ROBOTICKY("Robotický");

    private String nazev;

    EProces(String nazev) {
        this.nazev = nazev;
    }

    @Override
    public String toString() {
        return this.nazev;
    }
}
