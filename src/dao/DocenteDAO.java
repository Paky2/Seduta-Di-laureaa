package dao;

import model.Docente;
import java.util.List;

public interface DocenteDAO {

    void SalvaDocente(Docente docente);

    void SalvaCoordinatore(Docente coordinatore);

    List<Docente> ListaDocenti();

    Docente TrovaDocenteDaLogin(String login);

    Docente TrovaCoordinatoreDaLogin(String login);
}