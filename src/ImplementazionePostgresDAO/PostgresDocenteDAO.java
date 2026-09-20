package ImplementazionePostgresDAO;

import Database.Database;
import dao.DocenteDAO;
import model.Docente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PostgresDocenteDAO implements DocenteDAO {

    private Database database;

    public PostgresDocenteDAO() {
        database = new Database();
    }

    @Override
    public void SalvaDocente(Docente docente) {

        String sqlUtente = """
                INSERT INTO utente(nome, cognome, email, login, password)
                VALUES (?, ?, ?, ?, ?)
                RETURNING id
                """;

        String sqlDocente = """
                INSERT INTO docente(id_utente)
                VALUES (?)
                """;

        Connection connection = null;

        try {
            connection = database.GetConnection();

            // Inizio transazione
            connection.setAutoCommit(false);

            int idUtente;

            // Inserimento in UTENTE
            try (PreparedStatement statement =
                         connection.prepareStatement(sqlUtente)) {

                statement.setString(1, docente.getNome());
                statement.setString(2, docente.getCognome());
                statement.setString(3, docente.getEmail());
                statement.setString(4, docente.getLogin());
                statement.setString(5, docente.getPassword());

                try (ResultSet resultSet =
                             statement.executeQuery()) {

                    if (!resultSet.next()) {
                        throw new Exception(
                                "Impossibile creare l'utente."
                        );
                    }

                    idUtente =
                            resultSet.getInt("id");
                }
            }

            // Inserimento in DOCENTE
            try (PreparedStatement statement =
                         connection.prepareStatement(sqlDocente)) {

                statement.setInt(1, idUtente);

                int righe =
                        statement.executeUpdate();

                if (righe != 1) {
                    throw new Exception(
                            "Impossibile creare il docente."
                    );
                }
            }

            // Conferma transazione
            connection.commit();

        } catch (Exception e) {

            // Annulla tutto se qualcosa è andato storto
            if (connection != null) {

                try {
                    connection.rollback();
                } catch (Exception rollbackError) {
                    rollbackError.printStackTrace();
                }
            }

            // Mostriamo l'errore alla GUI
            throw new RuntimeException(
                    "Errore durante la registrazione del docente: "
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
    public List<Docente> ListaDocenti() {

        List<Docente> lista =
                new ArrayList<>();

        String sql = """
                SELECT d.id_docente,
                       u.nome,
                       u.cognome,
                       u.email,
                       u.login,
                       u.password
                FROM docente d
                JOIN utente u
                  ON d.id_utente = u.id
                ORDER BY u.cognome, u.nome
                """;

        try (Connection connection =
                     database.GetConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                Docente docente =
                        new Docente(
                                resultSet.getString("nome"),
                                resultSet.getString("cognome"),
                                resultSet.getString("email"),
                                resultSet.getString("login"),
                                resultSet.getString("password"),
                                resultSet.getInt("id_docente")
                        );

                lista.add(docente);
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Errore nel caricamento dei docenti: "
                            + e.getMessage(),
                    e
            );
        }

        return lista;
    }

    @Override
    public Docente TrovaDocenteDaLogin(String login) {

        String sql = """
                SELECT d.id_docente,
                       u.nome,
                       u.cognome,
                       u.email,
                       u.login,
                       u.password
                FROM docente d
                JOIN utente u
                  ON d.id_utente = u.id
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

                    return new Docente(
                            resultSet.getString("nome"),
                            resultSet.getString("cognome"),
                            resultSet.getString("email"),
                            resultSet.getString("login"),
                            resultSet.getString("password"),
                            resultSet.getInt("id_docente")
                    );
                }
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Errore nella ricerca del docente: "
                            + e.getMessage(),
                    e
            );
        }

        return null;
    }

    @Override
    public Docente TrovaCoordinatoreDaLogin(String login) {

        String sql = """
                SELECT d.id_docente,
                       u.nome,
                       u.cognome,
                       u.email,
                       u.login,
                       u.password
                FROM coordinatore c
                JOIN docente d
                  ON c.id_docente = d.id_docente
                JOIN utente u
                  ON d.id_utente = u.id
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

                    return new Docente(
                            resultSet.getString("nome"),
                            resultSet.getString("cognome"),
                            resultSet.getString("email"),
                            resultSet.getString("login"),
                            resultSet.getString("password"),
                            resultSet.getInt("id_docente")
                    );
                }
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Errore nella ricerca del coordinatore: "
                            + e.getMessage(),
                    e
            );
        }

        return null;
    }
}