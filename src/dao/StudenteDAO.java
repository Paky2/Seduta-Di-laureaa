package dao;

import model.Studente;

public interface StudenteDAO {

    void SalvaStudente(Studente studente);

    Studente TrovaStudente(String matricola);

    Studente TrovaStudenteDaLogin(String login);
}
