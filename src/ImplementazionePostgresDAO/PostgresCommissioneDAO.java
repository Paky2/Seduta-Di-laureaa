package ImplementazionePostgresDAO;

import Database.Database;
import dao.CommissioneDAO;
import model.Commissione;
import model.Docente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PostgresCommissioneDAO implements CommissioneDAO {

    private Database database;

    public PostgresCommissioneDAO() {
        database = new Database();
    }

    @Override
    public void SalvaCommissione(Commissione commissione) {

        String sqlCommissione = """
                INSERT INTO commissione (id_seduta)
                VALUES (?)
                RETURNING id
                """;

        String sqlDocente = """
                INSERT INTO commissione_docente
                (id_commissione, id_docente)
                VALUES (?, ?)
                """;

        try (Connection connection = database.GetConnection()) {

            int idCommissione;

            try (PreparedStatement statement =
                         connection.prepareStatement(sqlCommissione)) {

                statement.setInt(
                        1,
                        commissione.getSeduta().getId()
                );

                ResultSet resultSet = statement.executeQuery();

                resultSet.next();

                idCommissione = resultSet.getInt("id");
            }

            try (PreparedStatement statement =
                         connection.prepareStatement(sqlDocente)) {

                for (Docente docente : commissione.getDocenti()) {

                    statement.setInt(1, idCommissione);
                    statement.setInt(2, docente.getIdDocente());

                    statement.executeUpdate();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Commissione> ListaCommissioni() {

        List<Commissione> lista = new ArrayList<>();

        String sql = """
                SELECT id, id_seduta
                FROM commissione
                """;

        try (Connection connection = database.GetConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Commissione commissione = new Commissione();

                commissione.setId(
                        resultSet.getInt("id")
                );

                model.SedutaLaurea seduta =
                        new model.SedutaLaurea();

                seduta.setId(
                        resultSet.getInt("id_seduta")
                );

                commissione.setSeduta(seduta);

                lista.add(commissione);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}