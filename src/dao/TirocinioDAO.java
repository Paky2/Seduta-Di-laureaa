package dao;

import model.Tirocinio;
import java.util.List;

public interface TirocinioDAO {

    void SalvaTirocinio(Tirocinio tirocinio);

    List<Tirocinio> ListaTirocini();

    List<Tirocinio> ListaTirociniInCorso();
}
