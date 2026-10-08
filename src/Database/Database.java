package Database;

import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static final String HOST = "localhost";
    private static final String PORT = "5432";
    private static final String DATABASE = "seduta_di_laurea";
    private static final String ADMIN_DATABASE = "postgres";

    private static final String USERNAME = "postgres";

    private static String Password = "3349";

    public Database() {
    }

    public Connection GetConnection() throws SQLException {

        return DriverManager.getConnection(
                getUrl(DATABASE),
                USERNAME,
                Password
        );
    }

    public Connection GetAdminConnection() throws SQLException {

        try {

            return DriverManager.getConnection(
                    getUrl(ADMIN_DATABASE),
                    USERNAME,
                    Password
            );

        } catch (SQLException firstError) {

            String nuovaPassword = JOptionPane.showInputDialog(
                    null,
                    "Impossibile collegarsi a PostgreSQL.\n\n" +
                            "Inserisci la password dell'utente 'postgres':",
                    "Password PostgreSQL",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (nuovaPassword == null) {
                throw firstError;
            }

            try {

                Connection connection =
                        DriverManager.getConnection(
                                getUrl(ADMIN_DATABASE),
                                USERNAME,
                                nuovaPassword
                        );

                Password = nuovaPassword;

                return connection;

            } catch (SQLException secondError) {

                throw new SQLException(
                        "Password PostgreSQL non valida " +
                                "oppure server PostgreSQL non disponibile.",
                        secondError
                );
            }
        }
    }

    private String getUrl(String database) {

        return "jdbc:postgresql://"
                + HOST
                + ":"
                + PORT
                + "/"
                + database;
    }

    public void CloseConnection(Connection connection) {

        if (connection != null) {

            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}