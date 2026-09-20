package model;

import java.util.ArrayList;
import java.util.List;

public class Commissione {

    private int id;
    private SedutaLaurea seduta;
    private List<Docente> docenti;

    public Commissione() {
        docenti = new ArrayList<>();
    }

    public Commissione(int id, SedutaLaurea seduta) {
        this.id = id;
        this.seduta = seduta;
        this.docenti = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SedutaLaurea getSeduta() {
        return seduta;
    }

    public void setSeduta(SedutaLaurea seduta) {
        this.seduta = seduta;
    }

    public List<Docente> getDocenti() {
        return docenti;
    }

    public void setDocenti(List<Docente> docenti) {
        this.docenti = docenti;
    }

    public void aggiungiDocente(Docente docente) {
        docenti.add(docente);
    }
}
