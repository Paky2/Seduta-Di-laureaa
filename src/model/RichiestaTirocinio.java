package model;

public class RichiestaTirocinio {

    private int id;
    private StatoRichiesta stato;
    private Studente studente;
    private Tirocinio tirocinio;
    private Docente docente;

    public RichiestaTirocinio() {
    }

    public RichiestaTirocinio(int id,
                              StatoRichiesta stato,
                              Studente studente,
                              Tirocinio tirocinio,
                              Docente docente) {
        this.id = id;
        this.stato = stato;
        this.studente = studente;
        this.tirocinio = tirocinio;
        this.docente = docente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public StatoRichiesta getStato() {
        return stato;
    }

    public void setStato(StatoRichiesta stato) {
        this.stato = stato;
    }

    public Studente getStudente() {
        return studente;
    }

    public void setStudente(Studente studente) {
        this.studente = studente;
    }

    public Tirocinio getTirocinio() {
        return tirocinio;
    }

    public void setTirocinio(Tirocinio tirocinio) {
        this.tirocinio = tirocinio;
    }

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }
}
