package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {

    private String Url;
    private String Username;
    private String Password;

    public Database() {

        Url = "jdbc:postgresql://localhost:5432/seduta_di_laurea";
        Username = "postgres";
        Password = "3349";
    }

    public Database(String Url, String Username, String Password) {

        this.Url = Url;
        this.Username = Username;
        this.Password = Password;
    }

    public Connection GetConnection() throws SQLException {

        Connection connection = DriverManager.getConnection(
                Url,
                Username,
                Password
        );

        // Controllo diagnostico della connessione
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT current_database(), current_schema(), " +
                        "current_user, inet_server_addr(), inet_server_port()"
        );
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {

                System.out.println();
                System.out.println("========== DATABASE JAVA ==========");
                System.out.println("Database: " + resultSet.getString(1));
                System.out.println("Schema: " + resultSet.getString(2));
                System.out.println("User: " + resultSet.getString(3));
                System.out.println("Host: " + resultSet.getString(4));
                System.out.println("Porta: " + resultSet.getInt(5));
                System.out.println("==================================");
                System.out.println();
            }
        }

        return connection;
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