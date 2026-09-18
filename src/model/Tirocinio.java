package model;

public class Tirocinio {

    private int id;
    private String argomento;
    private model.Docente docenteResponsabile;
    private StatoTirocinio stato;

    public Tirocinio() {
    }

    public Tirocinio(int id, String argomento,
                     Docente docenteResponsabile,
                     StatoTirocinio stato) {
        this.id = id;
        this.argomento = argomento;
        this.docenteResponsabile = docenteResponsabile;
        this.stato = stato;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getArgomento() {
        return argomento;
    }

    public void setArgomento(String argomento) {
        this.argomento = argomento;
    }

    public Docente getDocenteResponsabile() {
        return docenteResponsabile;
    }

    public void setDocenteResponsabile(Docente docenteResponsabile) {
        this.docenteResponsabile = docenteResponsabile;
    }

    public StatoTirocinio getStato() {
        return stato;
    }

    public void setStato(StatoTirocinio stato) {
        this.stato = stato;
    }
}
