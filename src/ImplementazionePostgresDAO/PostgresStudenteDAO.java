package ImplementazionePostgresDAO;

import Database.Database;
import dao.StudenteDAO;
import model.Studente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PostgresStudenteDAO implements StudenteDAO {

    private Database database;

    public PostgresStudenteDAO() {
        database = new Database();
    }

    @Override
    public void SalvaStudente(Studente studente) {

        String sqlUtente = """
                INSERT INTO utente(nome, cognome, email, login, password)
                VALUES (?, ?, ?, ?, ?)
                RETURNING id
                """;

        String sqlStudente = """
                INSERT INTO studente(matricola, id_utente)
                VALUES (?, ?)
                """;

        Connection connection = null;

        try {

            connection = database.GetConnection();

            // Inizio transazione
            connection.setAutoCommit(false);

            int idUtente;

            // =========================
            // INSERIMENTO IN UTENTE
            // =========================

            try (PreparedStatement statement =
                         connection.prepareStatement(sqlUtente)) {

                statement.setString(1, studente.getNome());
                statement.setString(2, studente.getCognome());
                statement.setString(3, studente.getEmail());
                statement.setString(4, studente.getLogin());
                statement.setString(5, studente.getPassword());

                try (ResultSet resultSet =
                             statement.executeQuery()) {

                    if (!resultSet.next()) {

                        throw new Exception(
                                "Impossibile creare l'utente."
                        );
                    }

                    idUtente = resultSet.getInt("id");
                }
            }

            // =========================
            // INSERIMENTO IN STUDENTE
            // =========================

            try (PreparedStatement statement =
                         connection.prepareStatement(sqlStudente)) {

                statement.setString(1, studente.getMatricola());
                statement.setInt(2, idUtente);

                int righe = statement.executeUpdate();

                if (righe != 1) {

                    throw new Exception(
                            "Impossibile creare lo studente."
                    );
                }
            }

            // =========================
            // CONFERMA
            // =========================

            connection.commit();

            System.out.println(
                    "Studente registrato correttamente: "
                            + studente.getLogin()
            );

        } catch (Exception e) {

            // =========================
            // ANNULLAMENTO
            // =========================

            if (connection != null) {

                try {
                    connection.rollback();

                } catch (Exception rollbackError) {

                    rollbackError.printStackTrace();
                }
            }

            throw new RuntimeException(
                    "Errore durante la registrazione dello studente: "
                            + e.getMessage(),
                    e
            );

        } finally {

            if (connection != null) {

                try {
                    connection.setAutoCommit(true);

                } catch (Exception ignored) {
                }

                database.CloseConnection(connection);
            }
        }
    }

    @Override
    public Studente TrovaStudente(String matricola) {

        String sql = """
                SELECT u.nome,
                       u.cognome,
                       u.email,
                       u.login,
                       u.password,
                       s.matricola
                FROM studente s
                JOIN utente u
                  ON s.id_utente = u.id
                WHERE s.matricola = ?
                """;

        try (Connection connection =
                     database.GetConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, matricola);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    return new Studente(
                            resultSet.getString("nome"),
                            resultSet.getString("cognome"),
                            resultSet.getString("email"),
                            resultSet.getString("login"),
                            resultSet.getString("password"),
                            resultSet.getString("matricola")
                    );
                }
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Errore nella ricerca dello studente: "
                            + e.getMessage(),
                    e
            );
        }

        return null;
    }

    @Override
    public Studente TrovaStudenteDaLogin(String login) {

        String sql = """
                SELECT u.nome,
                       u.cognome,
                       u.email,
                       u.login,
                       u.password,
                       s.matricola
                FROM studente s
                JOIN utente u
                  ON s.id_utente = u.id
                WHERE u.login = ?
                """;

        try (Connection connection =
                     database.GetConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, login);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    return new Studente(
                            resultSet.getString("nome"),
                            resultSet.getString("cognome"),
                            resultSet.getString("email"),
                            resultSet.getString("login"),
                            resultSet.getString("password"),
                            resultSet.getString("matricola")
                    );
                }
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Errore nella ricerca dello studente: "
                            + e.getMessage(),
                    e
            );
        }

        return null;
    }
}