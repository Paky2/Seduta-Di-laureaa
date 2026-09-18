package model;

import java.util.List;

public class Docente extends Utente {

    private int idDocente;

    public Docente() {
        super();
    }

    public Docente(String nome, String cognome, String email,
                   String login, String password, int idDocente) {
        super(nome, cognome, email, login, password);
        this.idDocente = idDocente;
    }

    public int getIdDocente() {
        return idDocente;
    }

    public void setIdDocente(int idDocente) {
        this.idDocente = idDocente;
    }

    public void registrazione(String nome, String cognome, String email) {
        setNome(nome);
        setCognome(cognome);
        setEmail(email);
    }

    public boolean login(String login, String password) {
        return getLogin().equals(login) && getPassword().equals(password);
    }

    public void aggiuntaArgomentoTirocinio(Tirocinio t) {
    }

    public List<RichiestaTirocinio> visualizzaRichieste(StatoRichiesta stato) {
        return null;
    }

    public void accettaORifiutaRichiesta(RichiestaTirocinio r) {
    }

    public void accettaORifiutaTesi(Tesi t) {
    }
}