package dao;

import model.RichiestaTirocinio;
import model.StatoRichiesta;

import java.util.List;

public interface RichiestaTirocinioDAO {

    void SalvaRichiesta(RichiestaTirocinio richiesta);

    List<RichiestaTirocinio> ListaRichieste();

    List<RichiestaTirocinio> ListaRichiestePerStato(
            StatoRichiesta stato
    );

    void AggiornaStato(RichiestaTirocinio richiesta);
}