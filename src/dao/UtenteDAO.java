package dao;

import model.Utente;

public interface UtenteDAO {

    Utente Login(String login, String password);
}