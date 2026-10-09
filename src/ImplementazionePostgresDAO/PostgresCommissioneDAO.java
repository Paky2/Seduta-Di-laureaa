package ImplementazionePostgresDAO;

import Database.Database;
import dao.CommissioneDAO;
import model.Commissione;
import model.Docente;
import model.SedutaLaurea;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostgresCommissioneDAO implements CommissioneDAO {

    private final Database database;

    public PostgresCommissioneDAO() {
        database = new Database();
    }

    @Override
    public void SalvaCommissione(Commissione commissione) {

        if (commissione == null
                || commissione.getSeduta() == null
                || commissione.getDocenti() == null
                || commissione.getDocenti().isEmpty()) {

            throw new IllegalArgumentException(
                    "Seleziona una seduta e almeno un docente."
            );
        }

        int idSeduta = commissione.getSeduta().getId();

        String sqlControlloSeduta = """
                SELECT id
                FROM seduta_laurea
                WHERE id = ?
                FOR UPDATE
                """;

        String sqlCercaCommissione = """
                SELECT id
                FROM commissione
                WHERE id_seduta = ?
                """;

        String sqlInserisciCommissione = """
                INSERT INTO commissione (id_seduta)
                VALUES (?)
                RETURNING id
                """;

        String sqlEliminaDocenti = """
                DELETE FROM commissione_docente
                WHERE id_commissione = ?
                """;

        String sqlInserisciDocente = """
                INSERT INTO commissione_docente
                    (id_commissione, id_docente)
                VALUES (?, ?)
                """;

        Connection connection = null;

        try {

            connection = database.GetConnection();
            connection.setAutoCommit(false);

            // 1. Blocca la seduta durante l'operazione
            //    e verifica che esista.
            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 sqlControlloSeduta
                         )) {

                statement.setInt(1, idSeduta);

                try (ResultSet rs = statement.executeQuery()) {

                    if (!rs.next()) {
                        throw new IllegalArgumentException(
                                "La seduta selezionata non esiste."
                        );
                    }
                }
            }

            int idCommissione = -1;

            // 2. Verifica se esiste già una commissione
            //    per la seduta selezionata.
            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 sqlCercaCommissione
                         )) {

                statement.setInt(1, idSeduta);

                try (ResultSet rs = statement.executeQuery()) {

                    if (rs.next()) {
                        idCommissione = rs.getInt("id");
                    }
                }
            }

            if (idCommissione == -1) {

                // 3A. Non esiste: crea una nuova commissione.
                try (PreparedStatement statement =
                             connection.prepareStatement(
                                     sqlInserisciCommissione
                             )) {

                    statement.setInt(1, idSeduta);

                    try (ResultSet rs = statement.executeQuery()) {

                        if (!rs.next()) {
                            throw new SQLException(
                                    "Impossibile creare la commissione."
                            );
                        }

                        idCommissione = rs.getInt("id");
                    }
                }

            } else {

                // 3B. Esiste: rimuove i vecchi componenti.
                //     La commissione viene aggiornata,
                //     non duplicata.
                try (PreparedStatement statement =
                             connection.prepareStatement(
                                     sqlEliminaDocenti
                             )) {

                    statement.setInt(1, idCommissione);
                    statement.executeUpdate();
                }
            }

            // 4. Inserisce i docenti selezionati.
            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 sqlInserisciDocente
                         )) {

                for (Docente docente : commissione.getDocenti()) {

                    if (docente == null) {
                        throw new IllegalArgumentException(
                                "Uno dei docenti selezionati non è valido."
                        );
                    }

                    statement.setInt(1, idCommissione);
                    statement.setInt(
                            2,
                            docente.getIdDocente()
                    );

                    statement.executeUpdate();
                }
            }

            // 5. Conferma tutte le modifiche insieme.
            connection.commit();

        } catch (Exception e) {

            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackError) {
                    rollbackError.printStackTrace();
                }
            }

            throw new RuntimeException(
                    "Errore durante il salvataggio della commissione: "
                            + e.getMessage(),
                    e
            );

        } finally {

            if (connection != null) {

                try {
                    connection.setAutoCommit(true);
                } catch (SQLException ignored) {
                }

                database.CloseConnection(connection);
            }
        }
    }

    @Override
    public List<Commissione> ListaCommissioni() {

        List<Commissione> lista = new ArrayList<>();

        String sql = """
                SELECT id, id_seduta
                FROM commissione
                ORDER BY id
                """;

        try (Connection connection = database.GetConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Commissione commissione = new Commissione();

                commissione.setId(
                        resultSet.getInt("id")
                );

                SedutaLaurea seduta = new SedutaLaurea();

                seduta.setId(
                        resultSet.getInt("id_seduta")
                );

                commissione.setSeduta(seduta);

                lista.add(commissione);
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Errore nel caricamento delle commissioni: "
                            + e.getMessage(),
                    e
            );
        }

        return lista;
    }
}