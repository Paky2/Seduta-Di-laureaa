package model;

public class CoordinatoreCorsoLaurea extends model.Docente {

    public CoordinatoreCorsoLaurea() {
        super();
    }

    public CoordinatoreCorsoLaurea(String nome, String cognome, String email,
                                   String login, String password, int idDocente) {
        super(nome, cognome, email, login, password, idDocente);
    }

    public void inserisciSedutaLaurea(SedutaLaurea s) {
    }

    public void formaCommissione(Commissione c) {
    }

    public void visualizzaStudentiAppartSeduta(SedutaLaurea s) {
    }

    public void controllaTesi(Tesi t) {
    }
}