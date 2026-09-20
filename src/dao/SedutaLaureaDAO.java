package dao;

import model.SedutaLaurea;
import model.Studente;

import java.util.List;

public interface SedutaLaureaDAO {

    void SalvaSeduta(SedutaLaurea seduta);

    List<SedutaLaurea> ListaSedute();

    List<Studente> ListaStudenti(SedutaLaurea seduta);

    void PrenotaSeduta(SedutaLaurea seduta, Studente studente);
}