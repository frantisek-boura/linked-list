package procesy;

public abstract class Proces {
    
    private String id;
    private int casProcesu;

    public Proces(String id, int casProcesu) {
        if (id.isEmpty() || casProcesu < 0) {
            throw new IllegalArgumentException();
        }

        this.id = id;
        this.casProcesu = casProcesu;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCasProcesu() {
        return casProcesu;
    }

    public void setCasProcesu(int casProcesu) {
        this.casProcesu = casProcesu;
    }

    @Override
    public String toString() {
        return String.format("ID: %s; Cas procesu: %d", this.id, this.casProcesu);
    }
    
}
