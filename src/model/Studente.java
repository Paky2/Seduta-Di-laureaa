package model;

import java.util.List;

public class Studente extends Utente {

    private String matricola;

    public Studente() {
        super();
    }

    public Studente(String nome, String cognome, String email,
                    String login, String password, String matricola) {
        super(nome, cognome, email, login, password);
        this.matricola = matricola;
    }

    public String getMatricola() {
        return matricola;
    }

    public void setMatricola(String matricola) {
        this.matricola = matricola;
    }

    public void registrazione(String nome, String cognome,
                              String email, String matricola) {
        setNome(nome);
        setCognome(cognome);
        setEmail(email);
        this.matricola = matricola;
    }

    public boolean login(String login, String password) {
        return getLogin().equals(login) && getPassword().equals(password);
    }

    public List<Tirocinio> listaTirocini() {
        return null;
    }

    public void richiestaTirocinio(RichiestaTirocinio r) {
    }

    public void visualizzaRichiesta(RichiestaTirocinio r,
                                    StatoRichiesta s) {
    }

    public void prenotaSedutaLaurea(SedutaLaurea s) {
    }

    public void caricaTesi(Tesi t) {
    }
}