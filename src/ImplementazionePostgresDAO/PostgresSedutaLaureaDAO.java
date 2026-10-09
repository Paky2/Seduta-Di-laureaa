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

        List<SedutaLaurea> lista = new ArrayList<>();

        String sql = """
            SELECT s.id,
                   s.data,
                   s.ora,
                   s.luogo,
                   (
                       s.disponibilita = TRUE
                       AND NOT EXISTS (
                           SELECT 1
                           FROM prenotazione_seduta p
                           WHERE p.id_seduta = s.id
                       )
                   ) AS disponibilita
            FROM seduta_laurea s
            ORDER BY s.data, s.ora
            """;

        try (Connection connection = database.GetConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                SedutaLaurea seduta = new SedutaLaurea();

                seduta.setId(resultSet.getInt("id"));
                seduta.setData(resultSet.getDate("data"));
                seduta.setOra(
                        resultSet.getTime("ora").toLocalTime()
                );
                seduta.setLuogo(resultSet.getString("luogo"));
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

        if (seduta == null || studente == null) {
            throw new IllegalArgumentException(
                    "Studente o seduta non validi."
            );
        }

        int idSeduta = seduta.getId();
        String matricola = studente.getMatricola();

        Connection connection = null;

        try {

            connection = database.GetConnection();
            connection.setAutoCommit(false);

            // 1. Controlla se lo studente ha già una prenotazione.
            List<Integer> prenotazioniPrecedenti =
                    new ArrayList<>();

            String sqlPrenotazioniStudente = """
                SELECT id_seduta
                FROM prenotazione_seduta
                WHERE matricola_studente = ?
                FOR UPDATE
                """;

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 sqlPrenotazioniStudente
                         )) {

                statement.setString(1, matricola);

                try (ResultSet rs = statement.executeQuery()) {

                    while (rs.next()) {

                        int idPrecedente =
                                rs.getInt("id_seduta");

                        if (idPrecedente == idSeduta) {
                            throw new IllegalStateException(
                                    "Hai già prenotato questa seduta."
                            );
                        }

                        prenotazioniPrecedenti.add(idPrecedente);
                    }
                }
            }

            // 2. Verifica che la seduta scelta esista e sia libera.
            String sqlControlloSeduta = """
                SELECT s.disponibilita,
                       EXISTS (
                           SELECT 1
                           FROM prenotazione_seduta p
                           WHERE p.id_seduta = s.id
                       ) AS gia_prenotata
                FROM seduta_laurea s
                WHERE s.id = ?
                FOR UPDATE
                """;

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 sqlControlloSeduta
                         )) {

                statement.setInt(1, idSeduta);

                try (ResultSet rs = statement.executeQuery()) {

                    if (!rs.next()) {
                        throw new IllegalStateException(
                                "La seduta selezionata non esiste."
                        );
                    }

                    boolean disponibile =
                            rs.getBoolean("disponibilita");

                    boolean giaPrenotata =
                            rs.getBoolean("gia_prenotata");

                    if (!disponibile || giaPrenotata) {
                        throw new IllegalStateException(
                                "La seduta selezionata è già piena. "
                                        + "Scegline un'altra."
                        );
                    }
                }
            }

            // 3. Cerca una tesi accettata dello studente.
            //    Non deve necessariamente essere già associata
            //    alla seduta che lo studente vuole prenotare.
            int idTesi;

            String sqlTesiAccettata = """
                SELECT id
                FROM tesi
                WHERE matricola_studente = ?
                  AND stato = 'ACCETTATA'::stato_tesi
                ORDER BY versione DESC, id DESC
                LIMIT 1
                FOR UPDATE
                """;

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 sqlTesiAccettata
                         )) {

                statement.setString(1, matricola);

                try (ResultSet rs = statement.executeQuery()) {

                    if (!rs.next()) {
                        throw new IllegalStateException(
                                "Non puoi prenotare una seduta: "
                                        + "devi avere una tesi accettata."
                        );
                    }

                    idTesi = rs.getInt("id");
                }
            }

            // 4. Se lo studente aveva prenotato un'altra seduta,
            //    libera quella precedente.
            String sqlEliminaPrenotazioniPrecedenti = """
                DELETE FROM prenotazione_seduta
                WHERE matricola_studente = ?
                  AND id_seduta <> ?
                """;

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 sqlEliminaPrenotazioniPrecedenti
                         )) {

                statement.setString(1, matricola);
                statement.setInt(2, idSeduta);

                statement.executeUpdate();
            }

            String sqlLiberaSedutaPrecedente = """
                UPDATE seduta_laurea
                SET disponibilita = TRUE
                WHERE id = ?
                  AND NOT EXISTS (
                      SELECT 1
                      FROM prenotazione_seduta p
                      WHERE p.id_seduta = seduta_laurea.id
                  )
                """;

            for (int idPrecedente : prenotazioniPrecedenti) {

                try (PreparedStatement statement =
                             connection.prepareStatement(
                                     sqlLiberaSedutaPrecedente
                             )) {

                    statement.setInt(1, idPrecedente);
                    statement.executeUpdate();
                }
            }

            // 5. Associa la tesi accettata alla seduta selezionata.
            String sqlAggiornaTesi = """
                UPDATE tesi
                SET id_seduta = ?
                WHERE id = ?
                  AND matricola_studente = ?
                  AND stato = 'ACCETTATA'::stato_tesi
                """;

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 sqlAggiornaTesi
                         )) {

                statement.setInt(1, idSeduta);
                statement.setInt(2, idTesi);
                statement.setString(3, matricola);

                if (statement.executeUpdate() != 1) {
                    throw new IllegalStateException(
                            "Impossibile associare la tesi alla seduta."
                    );
                }
            }

            // 6. Inserisce la nuova prenotazione.
            String sqlInserisciPrenotazione = """
                INSERT INTO prenotazione_seduta
                    (matricola_studente, id_seduta)
                VALUES (?, ?)
                """;

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 sqlInserisciPrenotazione
                         )) {

                statement.setString(1, matricola);
                statement.setInt(2, idSeduta);
                statement.executeUpdate();
            }

            // 7. Segna la seduta scelta come occupata.
            String sqlOccupaSeduta = """
                UPDATE seduta_laurea
                SET disponibilita = FALSE
                WHERE id = ?
                """;

            try (PreparedStatement statement =
                         connection.prepareStatement(sqlOccupaSeduta)) {

                statement.setInt(1, idSeduta);

                if (statement.executeUpdate() != 1) {
                    throw new IllegalStateException(
                            "Impossibile aggiornare la disponibilità."
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

            if ("23505".equals(e.getSQLState())) {
                throw new IllegalStateException(
                        "La seduta è stata prenotata nel frattempo. "
                                + "Aggiorna la lista e scegline un'altra."
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
