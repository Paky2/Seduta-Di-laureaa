package dao;

import model.Commissione;

import java.util.List;

public interface CommissioneDAO {

    void SalvaCommissione(Commissione commissione);

    List<Commissione> ListaCommissioni();
}
