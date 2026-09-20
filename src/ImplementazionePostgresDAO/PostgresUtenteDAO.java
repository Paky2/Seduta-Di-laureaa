package ImplementazionePostgresDAO;

import Database.Database;
import dao.UtenteDAO;
import model.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PostgresUtenteDAO implements UtenteDAO {

    private Database database;

    public PostgresUtenteDAO() {
        database = new Database();
    }

    @Override
    public Utente Login(String login, String password) {

        String sql = "SELECT id, nome, cognome, email, login, password " +
                "FROM utente " +
                "WHERE login = ? AND password = ?";

        try (Connection connection = database.GetConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, login);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                Utente utente = new Utente(
                        resultSet.getString("nome"),
                        resultSet.getString("cognome"),
                        resultSet.getString("email"),
                        resultSet.getString("login"),
                        resultSet.getString("password")
                );

                return utente;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
