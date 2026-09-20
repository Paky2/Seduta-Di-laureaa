package ImplementazionePostgresDAO;

import Database.Database;
import dao.SedutaLaureaDAO;
import model.SedutaLaurea;
import model.Studente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostgresSedutaLaureaDAO implements SedutaLaureaDAO {

    private Database database;

    public PostgresSedutaLaureaDAO() {
        database = new Database();
    }

    @Override
    public void SalvaSeduta(SedutaLaurea seduta) {

        String sql = """
                INSERT INTO seduta_laurea
                (data, ora, luogo, disponibilita)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = database.GetConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setDate(
                    1,
                    new java.sql.Date(
                            seduta.getData().getTime()
                    )
            );

            statement.setTime(
                    2,
                    java.sql.Time.valueOf(
                            seduta.getOra()
                    )
            );

            statement.setString(
                    3,
                    seduta.getLuogo()
            );

            statement.setBoolean(
                    4,
                    seduta.isDisponibilita()
            );

            statement.executeUpdate();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Errore durante il salvataggio della seduta: "
                            + e.getMessage(),
                    e
            );
        }
    }

    @Override
    public List<SedutaLaurea> ListaSedute() {

        List<SedutaLaurea> lista =
                new ArrayList<>();

        String sql = """
                SELECT id, data, ora, luogo, disponibilita
                FROM seduta_laurea
                ORDER BY data, ora
                """;

        try (Connection connection = database.GetConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                SedutaLaurea seduta =
                        new SedutaLaurea();

                seduta.setId(
                        resultSet.getInt("id")
                );

                seduta.setData(
                        resultSet.getDate("data")
                );

                seduta.setOra(
                        resultSet.getTime("ora").toLocalTime()
                );

                seduta.setLuogo(
                        resultSet.getString("luogo")
                );

                seduta.setDisponibilita(
                        resultSet.getBoolean("disponibilita")
                );

                lista.add(seduta);
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Errore nel caricamento delle sedute: "
                            + e.getMessage(),
                    e
            );
        }

        return lista;
    }

    @Override
    public List<Studente> ListaStudenti(
            SedutaLaurea seduta) {

        List<Studente> lista =
                new ArrayList<>();

        String sql = """
                SELECT s.matricola,
                       u.nome,
                       u.cognome,
                       u.email,
                       u.login,
                       u.password
                FROM tesi t
                JOIN studente s
                    ON t.matricola_studente = s.matricola
                JOIN utente u
                    ON s.id_utente = u.id
                WHERE t.id_seduta = ?
                """;

        try (Connection connection = database.GetConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    seduta.getId()
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    Studente studente =
                            new Studente(
                                    resultSet.getString("nome"),
                                    resultSet.getString("cognome"),
                                    resultSet.getString("email"),
                                    resultSet.getString("login"),
                                    resultSet.getString("password"),
                                    resultSet.getString("matricola")
                            );

                    lista.add(studente);
                }
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Errore nel caricamento degli studenti: "
                            + e.getMessage(),
                    e
            );
        }

        return lista;
    }

    @Override
    public void PrenotaSeduta(
            SedutaLaurea seduta,
            Studente studente) {

        String sqlControllo = """
                SELECT disponibilita
                FROM seduta_laurea
                WHERE id = ?
                FOR UPDATE
                """;

        String sqlPrenotazione = """
                INSERT INTO prenotazione_seduta
                (matricola_studente, id_seduta)
                VALUES (?, ?)
                """;

        String sqlDisponibilita = """
                UPDATE seduta_laurea
                SET disponibilita = FALSE
                WHERE id = ?
                """;

        Connection connection = null;

        try {

            connection = database.GetConnection();

            connection.setAutoCommit(false);

            boolean disponibile;

            // CONTROLLO DISPONIBILITA'
            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 sqlControllo
                         )) {

                statement.setInt(
                        1,
                        seduta.getId()
                );

                try (ResultSet resultSet =
                             statement.executeQuery()) {

                    if (!resultSet.next()) {

                        throw new IllegalStateException(
                                "La seduta selezionata non esiste."
                        );
                    }

                    disponibile =
                            resultSet.getBoolean(
                                    "disponibilita"
                            );
                }
            }

            // SEDUTA GIA' OCCUPATA
            if (!disponibile) {

                throw new IllegalStateException(
                        "La seduta selezionata è già piena."
                );
            }

            // INSERIMENTO PRENOTAZIONE
            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 sqlPrenotazione
                         )) {

                statement.setString(
                        1,
                        studente.getMatricola()
                );

                statement.setInt(
                        2,
                        seduta.getId()
                );

                statement.executeUpdate();
            }

            // LA SEDUTA DIVENTA NON DISPONIBILE
            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 sqlDisponibilita
                         )) {

                statement.setInt(
                        1,
                        seduta.getId()
                );

                int righe =
                        statement.executeUpdate();

                if (righe != 1) {

                    throw new IllegalStateException(
                            "Impossibile aggiornare la disponibilità della seduta."
                    );
                }
            }

            connection.commit();

        } catch (IllegalStateException e) {

            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ignored) {
                }
            }

            throw e;

        } catch (SQLException e) {

            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ignored) {
                }
            }

            // 23505 = violazione UNIQUE
            if ("23505".equals(e.getSQLState())) {

                throw new IllegalStateException(
                        "La seduta selezionata è già piena."
                );
            }

            throw new RuntimeException(
                    "Errore durante la prenotazione: "
                            + e.getMessage(),
                    e
            );

        } catch (Exception e) {

            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ignored) {
                }
            }

            throw new RuntimeException(
                    "Errore durante la prenotazione: "
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
}
