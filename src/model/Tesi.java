package model;

public class Tesi {

    private int id;
    private StatoTesi stato;
    private String file;
    private int versione;
    private Studente studente;
    private Docente relatore;
    private SedutaLaurea seduta;

    public Tesi() {
    }

    public Tesi(int id,
                StatoTesi stato,
                String file,
                int versione,
                Studente studente,
                Docente relatore,
                SedutaLaurea seduta) {
        this.id = id;
        this.stato = stato;
        this.file = file;
        this.versione = versione;
        this.studente = studente;
        this.relatore = relatore;
        this.seduta = seduta;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public StatoTesi getStato() {
        return stato;
    }

    public void setStato(StatoTesi stato) {
        this.stato = stato;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public int getVersione() {
        return versione;
    }

    public void setVersione(int versione) {
        this.versione = versione;
    }

    public Studente getStudente() {
        return studente;
    }

    public void setStudente(Studente studente) {
        this.studente = studente;
    }

    public Docente getRelatore() {
        return relatore;
    }

    public void setRelatore(Docente relatore) {
        this.relatore = relatore;
    }

    public SedutaLaurea getSeduta() {
        return seduta;
    }

    public void setSeduta(SedutaLaurea seduta) {
        this.seduta = seduta;
    }
}
