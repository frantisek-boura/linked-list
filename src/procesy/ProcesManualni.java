package procesy;

public class ProcesManualni extends Proces {
    
    private int pocetOsob;

    public ProcesManualni(String id, int pocetOsob, int casProcesu) {
        super(id, casProcesu);

        if (pocetOsob < 0) {
            throw new IllegalArgumentException();
        }

        this.pocetOsob = pocetOsob;
    }

    public int getPocetOsob() {
        return pocetOsob;
    }

    public void setPocetOsob(int pocetOsob) {
        this.pocetOsob = pocetOsob;
    }

    @Override
    public String toString() {
        return String.format("%s; Pocet osob: %d", super.toString(), this.pocetOsob);
    }
    
}
