package dao;

import model.Tesi;

import java.util.List;

public interface TesiDAO {

    void SalvaTesi(Tesi tesi);

    List<Tesi> ListaTesi();

    List<Tesi> ListaTesiPerRelatore(int idDocente);

    List<Tesi> ListaTesiValutate();

    void AggiornaStato(Tesi tesi, int idDocente);

    boolean HaRichiestaTirocinioAccettata(String matricola);

    boolean HaTesiAccettataPerSeduta(
            String matricola,
            int idSeduta
    );
}