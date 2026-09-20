package dao;

import model.Tesi;

import java.util.List;

public interface TesiDAO {

    void SalvaTesi(Tesi tesi);

    List<Tesi> ListaTesi();

    void AggiornaStato(Tesi tesi);
}
